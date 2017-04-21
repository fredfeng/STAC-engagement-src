/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.tuple.ImmutablePair
 *  org.apache.commons.lang3.tuple.Pair
 */
package edu.cyberapex.flightplanner.guide;

import com.sun.net.httpserver.HttpExchange;
import edu.cyberapex.flightplanner.framework.Airline;
import edu.cyberapex.flightplanner.framework.Airport;
import edu.cyberapex.flightplanner.framework.Flight;
import edu.cyberapex.flightplanner.framework.RouteMap;
import edu.cyberapex.flightplanner.guide.AirGuide;
import edu.cyberapex.flightplanner.guide.GuideUtils;
import edu.cyberapex.flightplanner.store.AirDatabase;
import edu.cyberapex.record.Logger;
import edu.cyberapex.record.LoggerFactory;
import edu.cyberapex.server.WebSessionService;
import edu.cyberapex.server.guide.HttpGuideResponse;
import edu.cyberapex.server.guide.MultipartHelper;
import edu.cyberapex.template.TemplateEngine;
import edu.cyberapex.template.TemplateEngineBuilder;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class EditAirportGuide
extends AirGuide {
    private static final Logger logger = LoggerFactory.getLogger(EditAirportGuide.class);
    protected static final String PATH = "/edit_airport";
    private static final String TITLE = "Edit Airport Data";
    private static final String NAME_FIELD = "name";
    private static final String DELETE_FIELD = "delete";
    private static final Set<String> FIELD_NAMES = new LinkedHashSet<String>();
    private static final TemplateEngine ENGINE;
    private static final TemplateEngine FLIGHTS_LIST_ENGINE;

    public EditAirportGuide(AirDatabase db, WebSessionService webSessionService) {
        super(db, webSessionService);
    }

    private String generateFlightsListHTML(RouteMap routeMap, Airport airport) {
        StringBuilder flightsListBuilder = new StringBuilder();
        HashMap<String, String> flightListDictionary = new HashMap<String, String>();
        List<Flight> originFlights = airport.getOriginFlights();
        int p = 0;
        while (p < originFlights.size()) {
            while (p < originFlights.size() && Math.random() < 0.4) {
                while (p < originFlights.size() && Math.random() < 0.6) {
                    Flight flight = originFlights.get(p);
                    flightListDictionary.put("flightURL", GuideUtils.generateFlightURL(routeMap, airport, flight));
                    flightListDictionary.put("destination", flight.grabDestination().getName());
                    flightListDictionary.put("distance", Integer.toString(flight.grabDistance()));
                    flightListDictionary.put("cost", Integer.toString(flight.takeFuelCosts()));
                    flightListDictionary.put("time", Integer.toString(flight.getTravelTime()));
                    flightListDictionary.put("crew", Integer.toString(flight.pullNumCrewMembers()));
                    flightsListBuilder.append(FLIGHTS_LIST_ENGINE.replaceTags(flightListDictionary));
                    ++p;
                }
            }
        }
        return flightsListBuilder.toString();
    }

    private String grabContents(Pair<RouteMap, Airport> routeMapAirportPair) {
        RouteMap routeMap = (RouteMap)routeMapAirportPair.getLeft();
        Airport airport = (Airport)routeMapAirportPair.getRight();
        StringBuilder contentsBuilder = new StringBuilder();
        HashMap<String, String> contentsDictionary = new HashMap<String, String>();
        contentsDictionary.put("name", airport.getName());
        contentsDictionary.put("flightList", this.generateFlightsListHTML(routeMap, airport));
        contentsDictionary.put("addFlightURL", GuideUtils.generateAddFlightURL(routeMap, airport));
        contentsBuilder.append(ENGINE.replaceTags(contentsDictionary));
        return contentsBuilder.toString();
    }

    @Override
    public String getPath() {
        return "/edit_airport";
    }

    private Pair<RouteMap, Airport> grabRouteMapAirportPairFromPath(String remainingPath, Airline airline) throws NumberFormatException {
        RouteMap routeMap;
        Airport airport;
        String[] urlSplit = remainingPath.split("/");
        if (urlSplit.length == 3 && (routeMap = airline.getRouteMap(Integer.parseInt(urlSplit[1]))) != null && (airport = routeMap.fetchAirport(Integer.parseInt(urlSplit[2]))) != null) {
            return new ImmutablePair((Object)routeMap, (Object)airport);
        }
        return null;
    }

    @Override
    protected HttpGuideResponse handlePull(HttpExchange httpExchange, String remainingPath, Airline member) {
        Pair<RouteMap, Airport> routeMapAirportPair;
        try {
            routeMapAirportPair = this.grabRouteMapAirportPairFromPath(remainingPath, member);
            if (routeMapAirportPair == null) {
                return EditAirportGuide.getErrorResponse(400, "Bad URL");
            }
        }
        catch (NumberFormatException e) {
            return this.fetchTemplateErrorResponse("Error while parsing the route map id. " + e.getMessage(), member);
        }
        return this.getTemplateResponse("Edit Airport Data", this.grabContents(routeMapAirportPair), member);
    }

    @Override
    protected HttpGuideResponse handlePost(HttpExchange httpExchange, String remainingPath, Airline airline) {
        try {
            String newName;
            Pair<RouteMap, Airport> routeMapAirportPair = this.grabRouteMapAirportPairFromPath(remainingPath, airline);
            if (routeMapAirportPair == null) {
                return EditAirportGuide.getErrorResponse(400, "Unable to parse the URL for route map and airport information.");
            }
            RouteMap routeMap = (RouteMap)routeMapAirportPair.getLeft();
            Airport airport = (Airport)routeMapAirportPair.getRight();
            Map<String, List<String>> data = MultipartHelper.fetchMultipartValues(httpExchange, FIELD_NAMES);
            if (data.containsKey("name") && !(newName = data.get("name").get(0)).isEmpty()) {
                airport.defineName(newName);
            }
            if (data.containsKey("delete")) {
                routeMap.deleteAirport(airport);
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            return EditAirportGuide.getErrorResponse(400, e.getMessage());
        }
        return EditAirportGuide.getDefaultRedirectResponse();
    }

    static {
        FIELD_NAMES.add("name");
        FIELD_NAMES.add("delete");
        ENGINE = new TemplateEngineBuilder().defineText("<form action=\"#\" method=\"post\" enctype=\"multipart/form-data\" acceptcharset=\"UTF-8\">    <input type=\"submit\" value=\"Delete Airport\" name=\"delete\" id=\"delete\" /></form></br></br><form action=\"#\" method=\"post\" enctype=\"multipart/form-data\" acceptcharset=\"UTF-8\">    <label for=\"name\"> Airport Name: </label>    <input type=\"text\" name=\"name\" value=\"{{name}}\"/> <br/>    <input type=\"submit\" value=\"Submit airport\" name=\"submit\" id=\"submit\" />    <br/>    <ul>        {{flightList}}    </ul>    <p> <a href=\"{{addFlightURL}}\">Add an outgoing flight from this airport</a> </p></form>").generateTemplateEngine();
        FLIGHTS_LIST_ENGINE = new TemplateEngineBuilder().defineText("<li> <a href=\"{{flightURL}}\"> Outgoing flight to <b>{{destination}}</b>, </a>   Distance: <b>{{distance}}</b>   Fuel cost: <b>{{cost}}</b>   Travel Time: <b>{{time}}</b>   Number of Crew Members: <b>{{crew}}</b></li>").generateTemplateEngine();
    }
}

