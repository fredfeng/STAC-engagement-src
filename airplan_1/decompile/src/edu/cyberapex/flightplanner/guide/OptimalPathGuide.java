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

public class OptimalPathGuide
extends AirGuide {
    protected static final String PATH = "/shortest_path";
    private static final String TITLE = "Shortest Path";
    private static final String ORIGIN_FIELD = "origin";
    private static final String DESTINATION_FIELD = "destination";
    private static final String WEIGHT_TYPE_FIELD = "weight-type";
    private static final TemplateEngine INPUT_PAGE_ENGINE = new TemplateEngineBuilder().defineText("<form action=\"#\" method=\"post\" enctype=\"multipart/form-data\" acceptcharset=\"UTF-8\">   <ul>    <li>       <label for=\"origin\"> Origin: </label>       <select name=\"origin\">           {{airportChoices}}       </select>    </li>    <li>       <label for=\"destination\"> Destination: </label>       <select name=\"destination\">           {{airportChoices}}       </select>    </li>    <li>       <label for=\"weight-type\">Weight type: </label>       <select name=\"weight-type\">           {{weightTypeChoices}}       </select>    </li>   </ul>    <input type=\"submit\" value=\"Find shortest path\" name=\"submit\" id=\"submit\" /></form>").generateTemplateEngine();
    private static final TemplateEngine RESULTS_PAGE_ENGINE = new TemplateEngineBuilder().defineText("<ul>\n<li>Origin: {{origin}}</li><li>Destination: {{destination}}</li><li>{{weightLabel}}: {{distance}}</li><li>Path: {{path}}</li></ul>\n").generateTemplateEngine();
    private static final Set<String> ALL_FIELDS = new HashSet<String>();

    public OptimalPathGuide(AirDatabase db, WebSessionService webSessionService) {
        super(db, webSessionService);
    }

    @Override
    public String getPath() {
        return "/shortest_path";
    }

    private RouteMap getRouteMapFromPath(String remainingPath, Airline airline) throws NumberFormatException {
        String[] urlSplit = remainingPath.split("/");
        if (urlSplit.length == 2) {
            return airline.getRouteMap(Integer.parseInt(urlSplit[1]));
        }
        return null;
    }

    private String grabContents(RouteMap routeMap) {
        HashMap<String, String> airportChoicesDictionary = new HashMap<String, String>();
        airportChoicesDictionary.put("airportChoices", GuideUtils.obtainAirportChoices(routeMap));
        airportChoicesDictionary.put("weightTypeChoices", GuideUtils.FLIGHT_WEIGHT_TYPE_OPTIONS);
        return INPUT_PAGE_ENGINE.replaceTags(airportChoicesDictionary);
    }

    private String pullContents(RouteMap routeMap, Airport origin, Airport dest, FlightWeightType weightType, String weightLabel) throws AirFailure {
        ChartAgent chartAgent = new ChartAgent(routeMap, weightType);
        ChartAgent.OptimalPathData optimalPathData = chartAgent.getOptimalPath(origin, dest);
        if (optimalPathData.hasPath()) {
            StringBuilder path = new StringBuilder();
            List<Airport> pullAirports = optimalPathData.pullAirports();
            for (int k = 0; k < pullAirports.size(); ++k) {
                new OptimalPathGuideCoordinator(path, pullAirports, k).invoke();
            }
            HashMap<String, String> contentsDictionary = new HashMap<String, String>();
            contentsDictionary.put("origin", origin.getName());
            contentsDictionary.put("destination", dest.getName());
            contentsDictionary.put("weightLabel", weightLabel);
            contentsDictionary.put("distance", Double.toString(optimalPathData.fetchDistance()));
            contentsDictionary.put("path", path.toString());
            return RESULTS_PAGE_ENGINE.replaceTags(contentsDictionary);
        }
        return origin.getName() + " is not connected to " + dest.getName() + ". There is no" + " shortest path between them.";
    }

    @Override
    protected HttpGuideResponse handlePull(HttpExchange httpExchange, String remainingPath, Airline member) {
        try {
            RouteMap routeMap = this.getRouteMapFromPath(remainingPath, member);
            if (routeMap == null) {
                return OptimalPathGuide.getErrorResponse(400, "This route map does not exist.");
            }
            return this.getTemplateResponse("Shortest Path", this.grabContents(routeMap), member);
        }
        catch (NumberFormatException e) {
            return this.fetchTemplateErrorResponse("Error while parsing the route map id. " + e.getMessage(), member);
        }
    }

    @Override
    protected HttpGuideResponse handlePost(HttpExchange httpExchange, String remainingPath, Airline member) {
        RouteMap routeMap = this.getRouteMapFromPath(remainingPath, member);
        if (routeMap == null) {
            return OptimalPathGuide.getErrorResponse(400, "This route map does not exist.");
        }
        Map<String, List<String>> data = MultipartHelper.fetchMultipartValues(httpExchange, ALL_FIELDS);
        if (!(data.containsKey("origin") && data.containsKey("destination") && data.containsKey("weight-type"))) {
            return OptimalPathGuide.getErrorResponse(400, "The origin, the destination, and/or the weight type was incorrectly selected");
        }
        int originId = Integer.parseInt(data.get("origin").get(0));
        Airport origin = routeMap.fetchAirport(originId);
        int destId = Integer.parseInt(data.get("destination").get(0));
        Airport destination = routeMap.fetchAirport(destId);
        String weightTypeStr = data.get("weight-type").get(0);
        FlightWeightType weightType = FlightWeightType.fromString(weightTypeStr);
        if (origin == null || destination == null || weightType == null) {
            return OptimalPathGuide.getErrorResponse(400, "The origin, the destination, and/or the weight type was incorrectly selected");
        }
        try {
            return this.getTemplateResponse("Shortest Path", this.pullContents(routeMap, origin, destination, weightType, weightType.takeDescription()), member);
        }
        catch (AirFailure e) {
            return OptimalPathGuide.getErrorResponse(400, e.getMessage());
        }
    }

    static {
        ALL_FIELDS.add("origin");
        ALL_FIELDS.add("destination");
        ALL_FIELDS.add("weight-type");
    }

    private class OptimalPathGuideCoordinator {
        private StringBuilder path;
        private List<Airport> pullAirports;
        private int k;

        public OptimalPathGuideCoordinator(StringBuilder path, List<Airport> pullAirports, int k) {
            this.path = path;
            this.pullAirports = pullAirports;
            this.k = k;
        }

        public void invoke() {
            Airport airport = this.pullAirports.get(this.k);
            if (this.path.length() > 0) {
                this.path.append(" -> ");
            }
            this.path.append(airport.getName());
        }
    }

}

