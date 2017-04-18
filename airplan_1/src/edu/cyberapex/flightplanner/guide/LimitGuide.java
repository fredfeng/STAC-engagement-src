/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.flightplanner.guide;

import com.sun.net.httpserver.HttpExchange;
import edu.cyberapex.flightplanner.AirFailure;
import edu.cyberapex.flightplanner.ChartAgent;
import edu.cyberapex.flightplanner.framework.Airline;
import edu.cyberapex.flightplanner.framework.Airport;
import edu.cyberapex.flightplanner.framework.FlightWeightType;
import edu.cyberapex.flightplanner.framework.RouteMap;
import edu.cyberapex.flightplanner.guide.AirGuide;
import edu.cyberapex.flightplanner.guide.GuideUtils;
import edu.cyberapex.flightplanner.store.AirDatabase;
import edu.cyberapex.server.WebSessionService;
import edu.cyberapex.server.guide.HttpGuideResponse;
import edu.cyberapex.server.guide.MultipartHelper;
import edu.cyberapex.template.TemplateEngine;
import edu.cyberapex.template.TemplateEngineBuilder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LimitGuide
extends AirGuide {
    protected static final String PATH = "/capacity";
    private static final String TITLE = "Capacity";
    private static final Double NOT_CONNECTED_CAPACITY = 0.0;
    private static final String ORIGIN_FIELD = "origin";
    private static final String DESTINATION_FIELD = "destination";
    private static final String WEIGHT_TYPE_FIELD = "weight";
    private static final TemplateEngine INPUT_PAGE_ENGINE = new TemplateEngineBuilder().defineText("<p>Find the capacity between the origin and the destination for a flight weight type</p><form action=\"#\" method=\"post\" enctype=\"multipart/form-data\" acceptcharset=\"UTF-8\">   <ul>    <li>       <label for=\"origin\"> Origin: </label>       <select name=\"origin\">           {{airportChoices}}       </select>    </li>    <li>       <label for=\"destination\"> Destination: </label>       <select name=\"destination\">           {{airportChoices}}       </select>    </li>    <li>       <label for=\"weight\">Weight type: </label>       <select name=\"weight\">           {{weightTypeChoices}}       </select>    </li>   </ul>    <input type=\"submit\" value=\"Find the Maximum Capacity\" name=\"submit\" id=\"submit\" /></form>").generateTemplateEngine();
    private static final TemplateEngine RESULTS_PAGE_ENGINE = new TemplateEngineBuilder().defineText("<p>This describes the maximum \"{{lowerCaseWeightLabel}}\" capacity between {{origin}} and {{destination}}</p><ul>\n<li>Origin: {{origin}}</li><li>Destination: {{destination}}</li><li>{{weightLabel}} Capacity: {{capacity}}</li></ul>\n").generateTemplateEngine();
    private static final Set<String> ALL_FIELDS = new HashSet<String>();

    public LimitGuide(AirDatabase database, WebSessionService webSessionService) {
        super(database, webSessionService);
    }

    @Override
    public String getPath() {
        return "/capacity";
    }

    private String fetchContents(RouteMap routeMap) {
        HashMap<String, String> choicesDictionary = new HashMap<String, String>();
        choicesDictionary.put("airportChoices", GuideUtils.obtainAirportChoices(routeMap));
        choicesDictionary.put("weightTypeChoices", GuideUtils.FLIGHT_WEIGHT_TYPE_OPTIONS);
        return INPUT_PAGE_ENGINE.replaceTags(choicesDictionary);
    }

    private RouteMap takeRouteMapFromPath(String remainingPath, Airline airline) throws NumberFormatException {
        String[] urlSplit = remainingPath.split("/");
        if (urlSplit.length == 2) {
            return airline.getRouteMap(Integer.parseInt(urlSplit[1]));
        }
        return null;
    }

    private String fetchContents(RouteMap routeMap, Airport origin, Airport dest, FlightWeightType weightType, String weightLabel) throws AirFailure {
        ChartAgent chartAgent = new ChartAgent(routeMap, weightType);
        double limit = chartAgent.grabLimit(origin, dest);
        HashMap<String, String> contentsDictionary = new HashMap<String, String>();
        contentsDictionary.put("origin", origin.getName());
        contentsDictionary.put("destination", dest.getName());
        contentsDictionary.put("weightLabel", weightLabel);
        contentsDictionary.put("lowerCaseWeightLabel", weightLabel.toLowerCase());
        if (limit == NOT_CONNECTED_CAPACITY) {
            this.getContentsSupervisor(contentsDictionary);
        } else {
            this.grabContentsCoordinator(limit, contentsDictionary);
        }
        return RESULTS_PAGE_ENGINE.replaceTags(contentsDictionary);
    }

    private void grabContentsCoordinator(double limit, Map<String, String> contentsDictionary) {
        new LimitGuideEngine(limit, contentsDictionary).invoke();
    }

    private void getContentsSupervisor(Map<String, String> contentsDictionary) {
        contentsDictionary.put("capacity", "No Capacity. The airports are not connected.");
    }

    @Override
    protected HttpGuideResponse handlePull(HttpExchange httpExchange, String remainingPath, Airline airline) {
        try {
            RouteMap routeMap = this.takeRouteMapFromPath(remainingPath, airline);
            if (routeMap == null) {
                return LimitGuide.getErrorResponse(400, "This route map does not exist.");
            }
            return this.getTemplateResponse("Capacity", this.fetchContents(routeMap), airline);
        }
        catch (NumberFormatException e) {
            return this.fetchTemplateErrorResponse("Error while parsing the route map id. " + e.getMessage(), airline);
        }
    }

    @Override
    protected HttpGuideResponse handlePost(HttpExchange httpExchange, String remainingPath, Airline airline) {
        RouteMap routeMap = this.takeRouteMapFromPath(remainingPath, airline);
        if (routeMap == null) {
            return LimitGuide.getErrorResponse(400, "This route map does not exist.");
        }
        Map<String, List<String>> data = MultipartHelper.fetchMultipartValues(httpExchange, ALL_FIELDS);
        if (!(data.containsKey("origin") && data.containsKey("destination") && data.containsKey("weight"))) {
            return LimitGuide.getErrorResponse(400, "The origin, the destination, and/or the weight type was incorrectly selected");
        }
        int originId = Integer.parseInt(data.get("origin").get(0));
        Airport origin = routeMap.fetchAirport(originId);
        int destId = Integer.parseInt(data.get("destination").get(0));
        Airport destination = routeMap.fetchAirport(destId);
        String weightTypeStr = data.get("weight").get(0);
        FlightWeightType weightType = FlightWeightType.fromString(weightTypeStr);
        if (origin == null || destination == null || weightType == null) {
            return LimitGuide.getErrorResponse(400, "The origin, the destination, and/or the weight type was incorrectly selected");
        }
        try {
            return this.getTemplateResponse("Capacity", this.fetchContents(routeMap, origin, destination, weightType, weightType.takeDescription()), airline);
        }
        catch (AirFailure e) {
            return LimitGuide.getErrorResponse(400, e.getMessage());
        }
    }

    static {
        ALL_FIELDS.add("origin");
        ALL_FIELDS.add("destination");
        ALL_FIELDS.add("weight");
    }

    private class LimitGuideEngine {
        private double limit;
        private Map<String, String> contentsDictionary;

        public LimitGuideEngine(double limit, Map<String, String> contentsDictionary) {
            this.limit = limit;
            this.contentsDictionary = contentsDictionary;
        }

        public void invoke() {
            this.contentsDictionary.put("capacity", Double.toString(this.limit));
        }
    }

}

