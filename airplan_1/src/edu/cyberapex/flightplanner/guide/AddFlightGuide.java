/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.math.NumberUtils
 *  org.apache.commons.lang3.tuple.MutablePair
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
import edu.cyberapex.server.WebTemplate;
import edu.cyberapex.server.guide.HttpGuideResponse;
import edu.cyberapex.server.guide.MultipartHelper;
import edu.cyberapex.template.TemplateEngine;
import edu.cyberapex.template.TemplateEngineBuilder;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class AddFlightGuide
extends AirGuide {
    private static final Logger logger = LoggerFactory.getLogger(AddFlightGuide.class);
    protected static final String PATH = "/add_flight";
    private static final String TITLE = "Add Flight Path";
    private static final String DEST_FIELD = "destination";
    private static final String COST_FIELD = "cost";
    private static final String DISTANCE_FIELD = "distance";
    private static final String TIME_FIELD = "time";
    private static final String CREW_FIELD = "crewMembers";
    private static final String WEIGHT_FIELD = "weightCapacity";
    private static final String PASSENGER_FIELD = "passengerCapacity";
    private static final WebTemplate FLIGHT_ATTR_TEMPLATE = new WebTemplate("FlightAttributeSnippet.html", AddFlightGuide.class);
    private static final TemplateEngine START_ENGINE = new TemplateEngineBuilder().defineText("<form action=\"#\" method=\"post\" enctype=\"multipart/form-data\" acceptcharset=\"UTF-8\">    <h2>Add a Flight</h2>    <h2>Route map: {{routeMapName}} </h2>   <label for=\"destination\"> Destination: </label>    <select name=\"destination\">       {{airportChoices}}   </select> <br />").generateTemplateEngine();
    private static final String HTML_END = "    <input type=\"submit\" value=\"Submit\" name=\"submit\" id=\"submit\"/>    <br/></form>";
    private static final Set<String> ALL_FIELDS = new HashSet<String>();

    public AddFlightGuide(AirDatabase db, WebSessionService webSessionService) {
        super(db, webSessionService);
    }

    private String getContents(RouteMap routeMap, Airport origin) {
        StringBuilder stringBuilder = new StringBuilder();
        HashMap<String, String> contentsDictionary = new HashMap<String, String>();
        contentsDictionary.put("routeMapName", routeMap.takeName());
        contentsDictionary.put("originName", origin.getName());
        contentsDictionary.put("airportChoices", AddFlightGuide.getAirportNameChoices(routeMap));
        stringBuilder.append(START_ENGINE.replaceTags(contentsDictionary));
        stringBuilder.append(AddFlightGuide.obtainFlightAttributeHTML("distance", "Distance"));
        stringBuilder.append(AddFlightGuide.obtainFlightAttributeHTML("cost", "Cost"));
        stringBuilder.append(AddFlightGuide.obtainFlightAttributeHTML("time", "Travel Time"));
        stringBuilder.append(AddFlightGuide.obtainFlightAttributeHTML("crewMembers", "Number of Crew Members"));
        stringBuilder.append(AddFlightGuide.obtainFlightAttributeHTML("weightCapacity", "Weight Capacity"));
        stringBuilder.append(AddFlightGuide.obtainFlightAttributeHTML("passengerCapacity", "Number of Passengers"));
        stringBuilder.append("    <input type=\"submit\" value=\"Submit\" name=\"submit\" id=\"submit\"/>    <br/></form>");
        return stringBuilder.toString();
    }

    private static String obtainFlightAttributeHTML(String name, String title) {
        return AddFlightGuide.pullFlightAttributeHTML(name, title, "");
    }

    protected static String pullFlightAttributeHTML(String name, String title, String value) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("fieldName", name);
        map.put("fieldTitle", title);
        map.put("fieldValue", value);
        return FLIGHT_ATTR_TEMPLATE.getEngine().replaceTags(map);
    }

    @Override
    public String getPath() {
        return "/add_flight";
    }

    private Pair<RouteMap, Airport> grabRouteMapAirportPairFromPath(String remainingPath, Airline airline) throws NumberFormatException {
        Pair<RouteMap, Airport> addFlightGuideGuide;
        String[] remainingSplit = remainingPath.split("/");
        if (remainingSplit.length == 3 && (addFlightGuideGuide = this.fetchRouteMapAirportPairFromPathWorker(airline, remainingSplit)) != null) {
            return addFlightGuideGuide;
        }
        return new MutablePair((Object)null, (Object)null);
    }

    private Pair<RouteMap, Airport> fetchRouteMapAirportPairFromPathWorker(Airline airline, String[] remainingSplit) {
        AddFlightGuideGuide addFlightGuideGuide = new AddFlightGuideGuide(airline, remainingSplit).invoke();
        if (addFlightGuideGuide.is()) {
            return new MutablePair((Object)addFlightGuideGuide.getRouteMap(), (Object)addFlightGuideGuide.obtainOrigin());
        }
        return null;
    }

    @Override
    protected HttpGuideResponse handlePull(HttpExchange httpExchange, String remainingPath, Airline airline) {
        RouteMap routeMap;
        Airport origin;
        try {
            Pair<RouteMap, Airport> routeMapAirportPair = this.grabRouteMapAirportPairFromPath(remainingPath, airline);
            routeMap = (RouteMap)routeMapAirportPair.getLeft();
            origin = (Airport)routeMapAirportPair.getRight();
            if (routeMap == null) {
                return AddFlightGuide.getErrorResponse(400, "Bad URL: " + remainingPath + " is not associated with a known route map.");
            }
            if (origin == null) {
                return AddFlightGuide.getErrorResponse(400, "Bad URL: " + remainingPath + " is not associated with a known airport.");
            }
        }
        catch (NumberFormatException e) {
            return AddFlightGuide.getErrorResponse(400, e.getMessage());
        }
        return this.getTemplateResponse("Add Flight Path", this.getContents(routeMap, origin), airline);
    }

    @Override
    protected HttpGuideResponse handlePost(HttpExchange httpExchange, String remainingPath, Airline airline) {
        Map<String, List<String>> data = MultipartHelper.fetchMultipartValues(httpExchange, ALL_FIELDS);
        if (data == null || !data.keySet().containsAll(ALL_FIELDS)) {
            return this.handlePostSupervisor();
        }
        try {
            Pair<RouteMap, Airport> routeMapAirportPair = this.grabRouteMapAirportPairFromPath(remainingPath, airline);
            RouteMap routeMap = (RouteMap)routeMapAirportPair.getLeft();
            Airport origin = (Airport)routeMapAirportPair.getRight();
            if (routeMap == null) {
                return AddFlightGuide.getErrorResponse(400, "Bad URL: " + remainingPath + " is not associated with a known route map.");
            }
            if (origin == null) {
                return AddFlightGuide.getErrorResponse(400, "Bad URL: " + remainingPath + " is not associated with a known airport.");
            }
            if (!routeMap.canAddFlight()) {
                return this.fetchTemplateErrorResponse("This route map is not allowed to add additional flights.", airline);
            }
            String destinationName = data.get("destination").get(0);
            String distance = data.get("distance").get(0);
            String cost = data.get("cost").get(0);
            String travelTime = data.get("time").get(0);
            String crewMembers = data.get("crewMembers").get(0);
            String weightLimit = data.get("weightCapacity").get(0);
            String passengers = data.get("passengerCapacity").get(0);
            Airport destination = routeMap.obtainAirport(destinationName);
            if (destination == null) {
                return AddFlightGuide.getErrorResponse(400, "Bad Argument: " + remainingPath + " cannot find a valid destination airport with the name " + destinationName);
            }
            routeMap.addFlight(origin, destination, Integer.parseInt(cost), Integer.parseInt(distance), Integer.parseInt(travelTime), Integer.parseInt(crewMembers), Integer.parseInt(weightLimit), Integer.parseInt(passengers));
        }
        catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
            return this.fetchTemplateErrorResponse("Unable to parse number from string. " + e.getMessage(), airline);
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            return AddFlightGuide.getErrorResponse(400, e.getMessage());
        }
        return AddFlightGuide.getDefaultRedirectResponse();
    }

    private HttpGuideResponse handlePostSupervisor() {
        throw new NullPointerException("Bad request.");
    }

    private static String getAirportNameChoices(RouteMap routeMap) {
        StringBuilder sb = new StringBuilder();
        HashMap<String, String> airportChoicesDictionary = new HashMap<String, String>();
        List<Airport> airports = routeMap.obtainAirports();
        int p = 0;
        while (p < airports.size()) {
            while (p < airports.size() && Math.random() < 0.5) {
                while (p < airports.size() && Math.random() < 0.4) {
                    AddFlightGuide.getAirportNameChoicesCoordinator(sb, airportChoicesDictionary, airports, p);
                    ++p;
                }
            }
        }
        return sb.toString();
    }

    private static void getAirportNameChoicesCoordinator(StringBuilder sb, Map<String, String> airportChoicesDictionary, List<Airport> airports, int a) {
        Airport airport = airports.get(a);
        airportChoicesDictionary.clear();
        airportChoicesDictionary.put("value", airport.getName());
        airportChoicesDictionary.put("name", airport.getName());
        sb.append(GuideUtils.OPTION_ENGINE.replaceTags(airportChoicesDictionary));
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

    private class AddFlightGuideGuide {
        private boolean myResult;
        private Airline airline;
        private String[] remainingSplit;
        private RouteMap routeMap;
        private Airport origin;

        public AddFlightGuideGuide(Airline airline, String[] remainingSplit) {
            this.airline = airline;
            this.remainingSplit = remainingSplit;
        }

        boolean is() {
            return this.myResult;
        }

        public RouteMap getRouteMap() {
            return this.routeMap;
        }

        public Airport obtainOrigin() {
            return this.origin;
        }

        public AddFlightGuideGuide invoke() {
            String routeMapId = this.remainingSplit[1];
            String originId = this.remainingSplit[2];
            if (NumberUtils.isNumber((String)routeMapId) && NumberUtils.isNumber((String)originId)) {
                this.routeMap = this.airline.getRouteMap(Integer.parseInt(routeMapId));
                this.origin = this.routeMap.fetchAirport(Integer.parseInt(originId));
                this.myResult = true;
                return this;
            }
            this.myResult = false;
            return this;
        }
    }

}

