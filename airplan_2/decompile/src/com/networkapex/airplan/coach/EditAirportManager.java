/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.airplan.coach;

import com.networkapex.airplan.coach.AirManager;
import com.networkapex.airplan.coach.ManagerUtils;
import com.networkapex.airplan.prototype.Airline;
import com.networkapex.airplan.prototype.Airport;
import com.networkapex.airplan.prototype.Flight;
import com.networkapex.airplan.prototype.RouteMap;
import com.networkapex.airplan.save.AirDatabase;
import com.networkapex.nethost.WebSessionService;
import com.networkapex.nethost.coach.HttpManagerResponse;
import com.networkapex.nethost.coach.MultipartHelper;
import com.networkapex.slf4j.Logger;
import com.networkapex.slf4j.LoggerFactory;
import com.networkapex.template.TemplateEngine;
import com.sun.net.httpserver.HttpExchange;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class EditAirportManager
extends AirManager {
    private static final Logger logger = LoggerFactory.takeLogger(EditAirportManager.class);
    protected static final String TRAIL = "/edit_airport";
    private static final String TITLE = "Edit Airport Data";
    private static final String NAME_FIELD = "name";
    private static final String DELETE_FIELD = "delete";
    private static final Set<String> FIELD_NAMES = new LinkedHashSet<String>();
    private static final TemplateEngine ENGINE;
    private static final TemplateEngine FLIGHTS_LIST_ENGINE;

    public EditAirportManager(AirDatabase db, WebSessionService webSessionService) {
        super(db, webSessionService);
    }

    private String generateFlightsListHTML(RouteMap routeMap, Airport airport) {
        StringBuilder flightsListBuilder = new StringBuilder();
        HashMap<String, String> flightListDictionary = new HashMap<String, String>();
        List<Flight> originFlights = airport.grabOriginFlights();
        int i = 0;
        while (i < originFlights.size()) {
            while (i < originFlights.size() && Math.random() < 0.5) {
                while (i < originFlights.size() && Math.random() < 0.6) {
                    this.generateFlightsListHTMLFunction(routeMap, airport, flightsListBuilder, flightListDictionary, originFlights, i);
                    ++i;
                }
            }
        }
        return flightsListBuilder.toString();
    }

    private void generateFlightsListHTMLFunction(RouteMap routeMap, Airport airport, StringBuilder flightsListBuilder, Map<String, String> flightListDictionary, List<Flight> originFlights, int c) {
        Flight flight = originFlights.get(c);
        flightListDictionary.put("flightURL", ManagerUtils.generateFlightURL(routeMap, airport, flight));
        flightListDictionary.put("destination", flight.getDestination().obtainName());
        flightListDictionary.put("distance", Integer.toString(flight.pullDistance()));
        flightListDictionary.put("cost", Integer.toString(flight.grabFuelCosts()));
        flightListDictionary.put("time", Integer.toString(flight.getTravelTime()));
        flightListDictionary.put("crew", Integer.toString(flight.grabNumCrewMembers()));
        flightsListBuilder.append(FLIGHTS_LIST_ENGINE.replaceTags(flightListDictionary));
    }

    private String obtainContents(Pair<RouteMap, Airport> routeMapAirportPair) {
        RouteMap routeMap = routeMapAirportPair.getLeft();
        Airport airport = routeMapAirportPair.getRight();
        StringBuilder contentsBuilder = new StringBuilder();
        HashMap<String, String> contentsDictionary = new HashMap<String, String>();
        contentsDictionary.put("name", airport.obtainName());
        contentsDictionary.put("flightList", this.generateFlightsListHTML(routeMap, airport));
        contentsDictionary.put("addFlightURL", ManagerUtils.generateAddFlightURL(routeMap, airport));
        contentsBuilder.append(ENGINE.replaceTags(contentsDictionary));
        return contentsBuilder.toString();
    }

    @Override
    public String obtainTrail() {
        return "/edit_airport";
    }

    private Pair<RouteMap, Airport> pullRouteMapAirportPairFromTrail(String remainingTrail, Airline airline) throws NumberFormatException {
        RouteMap routeMap;
        Pair<RouteMap, Airport> airport;
        String[] urlSplit = remainingTrail.split("/");
        if (urlSplit.length == 3 && (routeMap = airline.getRouteMap(Integer.parseInt(urlSplit[1]))) != null && (airport = this.obtainRouteMapAirportPairFromTrailWorker(urlSplit[2], routeMap)) != null) {
            return airport;
        }
        return null;
    }

    private Pair<RouteMap, Airport> obtainRouteMapAirportPairFromTrailWorker(String s, RouteMap routeMap) {
        Airport airport = routeMap.grabAirport(Integer.parseInt(s));
        if (airport != null) {
            return new ImmutablePair<RouteMap, Airport>(routeMap, airport);
        }
        return null;
    }

    @Override
    protected HttpManagerResponse handlePull(HttpExchange httpExchange, String remainingTrail, Airline person) {
        Pair<RouteMap, Airport> routeMapAirportPair;
        try {
            routeMapAirportPair = this.pullRouteMapAirportPairFromTrail(remainingTrail, person);
            if (routeMapAirportPair == null) {
                return EditAirportManager.fetchErrorResponse(400, "Bad URL");
            }
        }
        catch (NumberFormatException e) {
            return this.obtainTemplateErrorResponse("Error while parsing the route map id. " + e.getMessage(), person);
        }
        return this.grabTemplateResponse("Edit Airport Data", this.obtainContents(routeMapAirportPair), person);
    }

    @Override
    protected HttpManagerResponse handlePost(HttpExchange httpExchange, String remainingTrail, Airline airline) {
        try {
            Pair<RouteMap, Airport> routeMapAirportPair = this.pullRouteMapAirportPairFromTrail(remainingTrail, airline);
            if (routeMapAirportPair == null) {
                return EditAirportManager.fetchErrorResponse(400, "Unable to parse the URL for route map and airport information.");
            }
            RouteMap routeMap = routeMapAirportPair.getLeft();
            Airport airport = routeMapAirportPair.getRight();
            Map<String, List<String>> data = MultipartHelper.getMultipartValues(httpExchange, FIELD_NAMES);
            if (data.containsKey("name")) {
                this.handlePostGuide(airport, data);
            }
            if (data.containsKey("delete")) {
                this.handlePostUtility(routeMap, airport);
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            return EditAirportManager.fetchErrorResponse(400, e.getMessage());
        }
        return EditAirportManager.grabDefaultRedirectResponse();
    }

    private void handlePostUtility(RouteMap routeMap, Airport airport) {
        routeMap.deleteAirport(airport);
    }

    private void handlePostGuide(Airport airport, Map<String, List<String>> data) {
        String newName = data.get("name").get(0);
        if (!newName.isEmpty()) {
            this.handlePostGuideFunction(airport, newName);
        }
    }

    private void handlePostGuideFunction(Airport airport, String newName) {
        airport.defineName(newName);
    }

    static {
        FIELD_NAMES.add("name");
        FIELD_NAMES.add("delete");
        ENGINE = new TemplateEngine("<form action=\"#\" method=\"post\" enctype=\"multipart/form-data\" acceptcharset=\"UTF-8\">    <input type=\"submit\" value=\"Delete Airport\" name=\"delete\" id=\"delete\" /></form></br></br><form action=\"#\" method=\"post\" enctype=\"multipart/form-data\" acceptcharset=\"UTF-8\">    <label for=\"name\"> Airport Name: </label>    <input type=\"text\" name=\"name\" value=\"{{name}}\"/> <br/>    <input type=\"submit\" value=\"Submit airport\" name=\"submit\" id=\"submit\" />    <br/>    <ul>        {{flightList}}    </ul>    <p> <a href=\"{{addFlightURL}}\">Add an outgoing flight from this airport</a> </p></form>");
        FLIGHTS_LIST_ENGINE = new TemplateEngine("<li> <a href=\"{{flightURL}}\"> Outgoing flight to <b>{{destination}}</b>, </a>   Distance: <b>{{distance}}</b>   Fuel cost: <b>{{cost}}</b>   Travel Time: <b>{{time}}</b>   Number of Crew Members: <b>{{crew}}</b></li>");
    }
}

