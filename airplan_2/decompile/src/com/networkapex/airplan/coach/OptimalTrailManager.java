/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.airplan.coach;

import com.networkapex.airplan.AirRaiser;
import com.networkapex.airplan.GraphTranslator;
import com.networkapex.airplan.coach.AirManager;
import com.networkapex.airplan.coach.ManagerUtils;
import com.networkapex.airplan.prototype.Airline;
import com.networkapex.airplan.prototype.Airport;
import com.networkapex.airplan.prototype.FlightWeightType;
import com.networkapex.airplan.prototype.RouteMap;
import com.networkapex.airplan.save.AirDatabase;
import com.networkapex.nethost.WebSessionService;
import com.networkapex.nethost.coach.HttpManagerResponse;
import com.networkapex.nethost.coach.MultipartHelper;
import com.networkapex.template.TemplateEngine;
import com.sun.net.httpserver.HttpExchange;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OptimalTrailManager
extends AirManager {
    protected static final String TRAIL = "/shortest_path";
    private static final String TITLE = "Shortest Path";
    private static final String ORIGIN_FIELD = "origin";
    private static final String DESTINATION_FIELD = "destination";
    private static final String WEIGHT_TYPE_FIELD = "weight-type";
    private static final TemplateEngine INPUT_PAGE_ENGINE = new TemplateEngine("<form action=\"#\" method=\"post\" enctype=\"multipart/form-data\" acceptcharset=\"UTF-8\">   <ul>    <li>       <label for=\"origin\"> Origin: </label>       <select name=\"origin\">           {{airportChoices}}       </select>    </li>    <li>       <label for=\"destination\"> Destination: </label>       <select name=\"destination\">           {{airportChoices}}       </select>    </li>    <li>       <label for=\"weight-type\">Weight type: </label>       <select name=\"weight-type\">           {{weightTypeChoices}}       </select>    </li>   </ul>    <input type=\"submit\" value=\"Find shortest path\" name=\"submit\" id=\"submit\" /></form>");
    private static final TemplateEngine RESULTS_PAGE_ENGINE = new TemplateEngine("<ul>\n<li>Origin: {{origin}}</li><li>Destination: {{destination}}</li><li>{{weightLabel}}: {{distance}}</li><li>Path: {{path}}</li></ul>\n");
    private static final Set<String> ALL_FIELDS = new HashSet<String>();

    public OptimalTrailManager(AirDatabase db, WebSessionService webSessionService) {
        super(db, webSessionService);
    }

    @Override
    public String obtainTrail() {
        return "/shortest_path";
    }

    private RouteMap obtainRouteMapFromTrail(String remainingTrail, Airline airline) throws NumberFormatException {
        String[] urlSplit = remainingTrail.split("/");
        if (urlSplit.length == 2) {
            return airline.getRouteMap(Integer.parseInt(urlSplit[1]));
        }
        return null;
    }

    private String grabContents(RouteMap routeMap) {
        HashMap<String, String> airportChoicesDictionary = new HashMap<String, String>();
        airportChoicesDictionary.put("airportChoices", ManagerUtils.obtainAirportChoices(routeMap));
        airportChoicesDictionary.put("weightTypeChoices", ManagerUtils.FLIGHT_WEIGHT_TYPE_OPTIONS);
        return INPUT_PAGE_ENGINE.replaceTags(airportChoicesDictionary);
    }

    private String grabContents(RouteMap routeMap, Airport origin, Airport dest, FlightWeightType weightType, String weightLabel) throws AirRaiser {
        GraphTranslator graphTranslator = new GraphTranslator(routeMap, weightType);
        GraphTranslator.OptimalTrailData optimalTrailData = graphTranslator.grabOptimalTrail(origin, dest);
        if (optimalTrailData.hasTrail()) {
            StringBuilder trail = new StringBuilder();
            List<Airport> obtainAirports = optimalTrailData.obtainAirports();
            for (int p = 0; p < obtainAirports.size(); ++p) {
                Airport airport = obtainAirports.get(p);
                if (trail.length() > 0) {
                    this.fetchContentsManager(trail);
                }
                trail.append(airport.obtainName());
            }
            HashMap<String, String> contentsDictionary = new HashMap<String, String>();
            contentsDictionary.put("origin", origin.obtainName());
            contentsDictionary.put("destination", dest.obtainName());
            contentsDictionary.put("weightLabel", weightLabel);
            contentsDictionary.put("distance", Double.toString(optimalTrailData.fetchDistance()));
            contentsDictionary.put("path", trail.toString());
            return RESULTS_PAGE_ENGINE.replaceTags(contentsDictionary);
        }
        return origin.obtainName() + " is not connected to " + dest.obtainName() + ". There is no" + " shortest path between them.";
    }

    private void fetchContentsManager(StringBuilder trail) {
        trail.append(" -> ");
    }

    @Override
    protected HttpManagerResponse handlePull(HttpExchange httpExchange, String remainingTrail, Airline person) {
        try {
            RouteMap routeMap = this.obtainRouteMapFromTrail(remainingTrail, person);
            if (routeMap == null) {
                return OptimalTrailManager.fetchErrorResponse(400, "This route map does not exist.");
            }
            return this.grabTemplateResponse("Shortest Path", this.grabContents(routeMap), person);
        }
        catch (NumberFormatException e) {
            return this.obtainTemplateErrorResponse("Error while parsing the route map id. " + e.getMessage(), person);
        }
    }

    @Override
    protected HttpManagerResponse handlePost(HttpExchange httpExchange, String remainingTrail, Airline person) {
        RouteMap routeMap = this.obtainRouteMapFromTrail(remainingTrail, person);
        if (routeMap == null) {
            return OptimalTrailManager.fetchErrorResponse(400, "This route map does not exist.");
        }
        Map<String, List<String>> data = MultipartHelper.getMultipartValues(httpExchange, ALL_FIELDS);
        if (!(data.containsKey("origin") && data.containsKey("destination") && data.containsKey("weight-type"))) {
            return OptimalTrailManager.fetchErrorResponse(400, "The origin, the destination, and/or the weight type was incorrectly selected");
        }
        int originId = Integer.parseInt(data.get("origin").get(0));
        Airport origin = routeMap.grabAirport(originId);
        int destId = Integer.parseInt(data.get("destination").get(0));
        Airport destination = routeMap.grabAirport(destId);
        String weightTypeStr = data.get("weight-type").get(0);
        FlightWeightType weightType = FlightWeightType.fromString(weightTypeStr);
        if (origin == null || destination == null || weightType == null) {
            return OptimalTrailManager.fetchErrorResponse(400, "The origin, the destination, and/or the weight type was incorrectly selected");
        }
        try {
            return this.grabTemplateResponse("Shortest Path", this.grabContents(routeMap, origin, destination, weightType, weightType.getDescription()), person);
        }
        catch (AirRaiser e) {
            return OptimalTrailManager.fetchErrorResponse(400, e.getMessage());
        }
    }

    static {
        ALL_FIELDS.add("origin");
        ALL_FIELDS.add("destination");
        ALL_FIELDS.add("weight-type");
    }
}

