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

public class LimitManager
extends AirManager {
    protected static final String TRAIL = "/capacity";
    private static final String TITLE = "Capacity";
    private static final Double NOT_CONNECTED_CAPACITY = 0.0;
    private static final String ORIGIN_FIELD = "origin";
    private static final String DESTINATION_FIELD = "destination";
    private static final String WEIGHT_TYPE_FIELD = "weight";
    private static final TemplateEngine INPUT_PAGE_ENGINE = new TemplateEngine("<p>Find the capacity between the origin and the destination for a flight weight type</p><form action=\"#\" method=\"post\" enctype=\"multipart/form-data\" acceptcharset=\"UTF-8\">   <ul>    <li>       <label for=\"origin\"> Origin: </label>       <select name=\"origin\">           {{airportChoices}}       </select>    </li>    <li>       <label for=\"destination\"> Destination: </label>       <select name=\"destination\">           {{airportChoices}}       </select>    </li>    <li>       <label for=\"weight\">Weight type: </label>       <select name=\"weight\">           {{weightTypeChoices}}       </select>    </li>   </ul>    <input type=\"submit\" value=\"Find the Maximum Capacity\" name=\"submit\" id=\"submit\" /></form>");
    private static final TemplateEngine RESULTS_PAGE_ENGINE = new TemplateEngine("<p>This describes the maximum \"{{lowerCaseWeightLabel}}\" capacity between {{origin}} and {{destination}}</p><ul>\n<li>Origin: {{origin}}</li><li>Destination: {{destination}}</li><li>{{weightLabel}} Capacity: {{capacity}}</li></ul>\n");
    private static final Set<String> ALL_FIELDS = new HashSet<String>();

    public LimitManager(AirDatabase database, WebSessionService webSessionService) {
        super(database, webSessionService);
    }

    @Override
    public String obtainTrail() {
        return "/capacity";
    }

    private String fetchContents(RouteMap routeMap) {
        HashMap<String, String> choicesDictionary = new HashMap<String, String>();
        choicesDictionary.put("airportChoices", ManagerUtils.obtainAirportChoices(routeMap));
        choicesDictionary.put("weightTypeChoices", ManagerUtils.FLIGHT_WEIGHT_TYPE_OPTIONS);
        return INPUT_PAGE_ENGINE.replaceTags(choicesDictionary);
    }

    private RouteMap takeRouteMapFromTrail(String remainingTrail, Airline airline) throws NumberFormatException {
        String[] urlSplit = remainingTrail.split("/");
        if (urlSplit.length == 2) {
            return airline.getRouteMap(Integer.parseInt(urlSplit[1]));
        }
        return null;
    }

    private String pullContents(RouteMap routeMap, Airport origin, Airport dest, FlightWeightType weightType, String weightLabel) throws AirRaiser {
        GraphTranslator graphTranslator = new GraphTranslator(routeMap, weightType);
        double limit = graphTranslator.getLimit(origin, dest);
        HashMap<String, String> contentsDictionary = new HashMap<String, String>();
        contentsDictionary.put("origin", origin.obtainName());
        contentsDictionary.put("destination", dest.obtainName());
        contentsDictionary.put("weightLabel", weightLabel);
        contentsDictionary.put("lowerCaseWeightLabel", weightLabel.toLowerCase());
        if (limit == NOT_CONNECTED_CAPACITY) {
            this.fetchContentsGateKeeper(contentsDictionary);
        } else {
            contentsDictionary.put("capacity", Double.toString(limit));
        }
        return RESULTS_PAGE_ENGINE.replaceTags(contentsDictionary);
    }

    private void fetchContentsGateKeeper(Map<String, String> contentsDictionary) {
        contentsDictionary.put("capacity", "No Capacity. The airports are not connected.");
    }

    @Override
    protected HttpManagerResponse handlePull(HttpExchange httpExchange, String remainingTrail, Airline airline) {
        try {
            RouteMap routeMap = this.takeRouteMapFromTrail(remainingTrail, airline);
            if (routeMap == null) {
                return LimitManager.fetchErrorResponse(400, "This route map does not exist.");
            }
            return this.grabTemplateResponse("Capacity", this.fetchContents(routeMap), airline);
        }
        catch (NumberFormatException e) {
            return this.obtainTemplateErrorResponse("Error while parsing the route map id. " + e.getMessage(), airline);
        }
    }

    @Override
    protected HttpManagerResponse handlePost(HttpExchange httpExchange, String remainingTrail, Airline airline) {
        RouteMap routeMap = this.takeRouteMapFromTrail(remainingTrail, airline);
        if (routeMap == null) {
            return LimitManager.fetchErrorResponse(400, "This route map does not exist.");
        }
        Map<String, List<String>> data = MultipartHelper.getMultipartValues(httpExchange, ALL_FIELDS);
        if (!(data.containsKey("origin") && data.containsKey("destination") && data.containsKey("weight"))) {
            return LimitManager.fetchErrorResponse(400, "The origin, the destination, and/or the weight type was incorrectly selected");
        }
        int originId = Integer.parseInt(data.get("origin").get(0));
        Airport origin = routeMap.grabAirport(originId);
        int destId = Integer.parseInt(data.get("destination").get(0));
        Airport destination = routeMap.grabAirport(destId);
        String weightTypeStr = data.get("weight").get(0);
        FlightWeightType weightType = FlightWeightType.fromString(weightTypeStr);
        if (origin == null || destination == null || weightType == null) {
            return LimitManager.fetchErrorResponse(400, "The origin, the destination, and/or the weight type was incorrectly selected");
        }
        try {
            return this.grabTemplateResponse("Capacity", this.pullContents(routeMap, origin, destination, weightType, weightType.getDescription()), airline);
        }
        catch (AirRaiser e) {
            return LimitManager.fetchErrorResponse(400, e.getMessage());
        }
    }

    static {
        ALL_FIELDS.add("origin");
        ALL_FIELDS.add("destination");
        ALL_FIELDS.add("weight");
    }
}

