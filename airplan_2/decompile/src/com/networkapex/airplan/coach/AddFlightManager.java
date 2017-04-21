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
import com.networkapex.nethost.WebTemplate;
import com.networkapex.nethost.WebTemplateBuilder;
import com.networkapex.nethost.coach.HttpManagerResponse;
import com.networkapex.nethost.coach.MultipartHelper;
import com.networkapex.slf4j.Logger;
import com.networkapex.slf4j.LoggerFactory;
import com.networkapex.template.TemplateEngine;
import com.sun.net.httpserver.HttpExchange;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class AddFlightManager
extends AirManager {
    private static final Logger logger = LoggerFactory.takeLogger(AddFlightManager.class);
    protected static final String TRAIL = "/add_flight";
    private static final String TITLE = "Add Flight Path";
    private static final String DEST_FIELD = "destination";
    private static final String COST_FIELD = "cost";
    private static final String DISTANCE_FIELD = "distance";
    private static final String TIME_FIELD = "time";
    private static final String CREW_FIELD = "crewMembers";
    private static final String WEIGHT_FIELD = "weightCapacity";
    private static final String PASSENGER_FIELD = "passengerCapacity";
    private static final WebTemplate FLIGHT_ATTR_TEMPLATE = new WebTemplateBuilder().defineResourceName("FlightAttributeSnippet.html").defineLoader(AddFlightManager.class).generateWebTemplate();
    private static final TemplateEngine START_ENGINE = new TemplateEngine("<form action=\"#\" method=\"post\" enctype=\"multipart/form-data\" acceptcharset=\"UTF-8\">    <h2>Add a Flight</h2>    <h2>Route map: {{routeMapName}} </h2>   <label for=\"destination\"> Destination: </label>    <select name=\"destination\">       {{airportChoices}}   </select> <br />");
    private static final String HTML_END = "    <input type=\"submit\" value=\"Submit\" name=\"submit\" id=\"submit\"/>    <br/></form>";
    private static final Set<String> ALL_FIELDS = new HashSet<String>();

    public AddFlightManager(AirDatabase db, WebSessionService webSessionService) {
        super(db, webSessionService);
    }

    private String fetchContents(RouteMap routeMap, Airport origin) {
        StringBuilder stringBuilder = new StringBuilder();
        HashMap<String, String> contentsDictionary = new HashMap<String, String>();
        contentsDictionary.put("routeMapName", routeMap.takeName());
        contentsDictionary.put("originName", origin.obtainName());
        contentsDictionary.put("airportChoices", AddFlightManager.getAirportNameChoices(routeMap));
        stringBuilder.append(START_ENGINE.replaceTags(contentsDictionary));
        stringBuilder.append(AddFlightManager.obtainFlightAttributeHTML("distance", "Distance"));
        stringBuilder.append(AddFlightManager.obtainFlightAttributeHTML("cost", "Cost"));
        stringBuilder.append(AddFlightManager.obtainFlightAttributeHTML("time", "Travel Time"));
        stringBuilder.append(AddFlightManager.obtainFlightAttributeHTML("crewMembers", "Number of Crew Members"));
        stringBuilder.append(AddFlightManager.obtainFlightAttributeHTML("weightCapacity", "Weight Capacity"));
        stringBuilder.append(AddFlightManager.obtainFlightAttributeHTML("passengerCapacity", "Number of Passengers"));
        stringBuilder.append("    <input type=\"submit\" value=\"Submit\" name=\"submit\" id=\"submit\"/>    <br/></form>");
        return stringBuilder.toString();
    }

    private static String obtainFlightAttributeHTML(String name, String title) {
        return AddFlightManager.obtainFlightAttributeHTML(name, title, "");
    }

    protected static String obtainFlightAttributeHTML(String name, String title, String value) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("fieldName", name);
        map.put("fieldTitle", title);
        map.put("fieldValue", value);
        return FLIGHT_ATTR_TEMPLATE.takeEngine().replaceTags(map);
    }

    @Override
    public String obtainTrail() {
        return "/add_flight";
    }

    private Pair<RouteMap, Airport> getRouteMapAirportPairFromTrail(String remainingTrail, Airline airline) throws NumberFormatException {
        String[] remainingSplit = remainingTrail.split("/");
        if (remainingSplit.length == 3) {
            String routeMapId = remainingSplit[1];
            String originId = remainingSplit[2];
            if (NumberUtils.isNumber(routeMapId) && NumberUtils.isNumber(originId)) {
                RouteMap routeMap = airline.getRouteMap(Integer.parseInt(routeMapId));
                Airport origin = routeMap.grabAirport(Integer.parseInt(originId));
                return new MutablePair<RouteMap, Airport>(routeMap, origin);
            }
        }
        return new MutablePair<>(null, null);
    }

    @Override
    protected HttpManagerResponse handlePull(HttpExchange httpExchange, String remainingTrail, Airline airline) {
        Airport origin;
        RouteMap routeMap;
        try {
            Pair<RouteMap, Airport> routeMapAirportPair = this.getRouteMapAirportPairFromTrail(remainingTrail, airline);
            routeMap = routeMapAirportPair.getLeft();
            origin = routeMapAirportPair.getRight();
            if (routeMap == null) {
                return AddFlightManager.fetchErrorResponse(400, "Bad URL: " + remainingTrail + " is not associated with a known route map.");
            }
            if (origin == null) {
                return AddFlightManager.fetchErrorResponse(400, "Bad URL: " + remainingTrail + " is not associated with a known airport.");
            }
        }
        catch (NumberFormatException e) {
            return AddFlightManager.fetchErrorResponse(400, e.getMessage());
        }
        return this.grabTemplateResponse("Add Flight Path", this.fetchContents(routeMap, origin), airline);
    }

    @Override
    protected HttpManagerResponse handlePost(HttpExchange httpExchange, String remainingTrail, Airline airline) {
        Map<String, List<String>> data = MultipartHelper.getMultipartValues(httpExchange, ALL_FIELDS);
        if (data == null || !data.keySet().containsAll(ALL_FIELDS)) {
            return this.handlePostUtility();
        }
        try {
            Pair<RouteMap, Airport> routeMapAirportPair = this.getRouteMapAirportPairFromTrail(remainingTrail, airline);
            RouteMap routeMap = routeMapAirportPair.getLeft();
            Airport origin = routeMapAirportPair.getRight();
            if (routeMap == null) {
                return AddFlightManager.fetchErrorResponse(400, "Bad URL: " + remainingTrail + " is not associated with a known route map.");
            }
            if (origin == null) {
                return AddFlightManager.fetchErrorResponse(400, "Bad URL: " + remainingTrail + " is not associated with a known airport.");
            }
            if (!routeMap.canAddFlight()) {
                return this.obtainTemplateErrorResponse("This route map is not allowed to add additional flights.", airline);
            }
            String destinationName = data.get("destination").get(0);
            String distance = data.get("distance").get(0);
            String cost = data.get("cost").get(0);
            String travelTime = data.get("time").get(0);
            String crewMembers = data.get("crewMembers").get(0);
            String weightLimit = data.get("weightCapacity").get(0);
            String passengers = data.get("passengerCapacity").get(0);
            Airport destination = routeMap.fetchAirport(destinationName);
            if (destination == null) {
                return AddFlightManager.fetchErrorResponse(400, "Bad Argument: " + remainingTrail + " cannot find a valid destination airport with the name " + destinationName);
            }
            routeMap.addFlight(origin, destination, Integer.parseInt(cost), Integer.parseInt(distance), Integer.parseInt(travelTime), Integer.parseInt(crewMembers), Integer.parseInt(weightLimit), Integer.parseInt(passengers));
        }
        catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
            return this.obtainTemplateErrorResponse("Unable to parse number from string. " + e.getMessage(), airline);
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            return AddFlightManager.fetchErrorResponse(400, e.getMessage());
        }
        return AddFlightManager.grabDefaultRedirectResponse();
    }

    private HttpManagerResponse handlePostUtility() {
        throw new NullPointerException("Bad request.");
    }

    private static String getAirportNameChoices(RouteMap routeMap) {
        StringBuilder sb = new StringBuilder();
        HashMap<String, String> airportChoicesDictionary = new HashMap<String, String>();
        List<Airport> airports = routeMap.getAirports();
        for (int a = 0; a < airports.size(); ++a) {
            AddFlightManager.fetchAirportNameChoicesHerder(sb, airportChoicesDictionary, airports, a);
        }
        return sb.toString();
    }

    private static void fetchAirportNameChoicesHerder(StringBuilder sb, Map<String, String> airportChoicesDictionary, List<Airport> airports, int i) {
        Airport airport = airports.get(i);
        airportChoicesDictionary.clear();
        airportChoicesDictionary.put("value", airport.obtainName());
        airportChoicesDictionary.put("name", airport.obtainName());
        sb.append(ManagerUtils.OPTION_ENGINE.replaceTags(airportChoicesDictionary));
    }

    static {
        ALL_FIELDS.add("destination");
        ALL_FIELDS.add("cost");
        ALL_FIELDS.add("distance");
        ALL_FIELDS.add("time");
        ALL_FIELDS.add("crewMembers");
        ALL_FIELDS.add("weightCapacity");
        ALL_FIELDS.add("passengerCapacity");
    }
}

