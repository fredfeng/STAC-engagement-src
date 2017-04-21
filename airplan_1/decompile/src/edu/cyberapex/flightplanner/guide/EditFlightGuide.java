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
import edu.cyberapex.flightplanner.guide.AddFlightGuide;
import edu.cyberapex.flightplanner.guide.AirGuide;
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
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class EditFlightGuide
extends AirGuide {
    protected static final String PATH = "/edit_flight";
    private static final String TITLE = "Edit Flight Path";
    private static final String DELETE_FIELD = "delete";
    private static final String COST_FIELD = "cost";
    private static final String DISTANCE_FIELD = "distance";
    private static final String TIME_FIELD = "time";
    private static final String CREW_FIELD = "crewMembers";
    private static final String WEIGHT_FIELD = "weightCapacity";
    private static final String PASSENGER_FIELD = "passengerCapacity";
    private static final TemplateEngine ENGINE = new TemplateEngineBuilder().defineText("<form action=\"#\" method=\"post\" enctype=\"multipart/form-data\" acceptcharset=\"UTF-8\">    <input type=\"submit\" value=\"Delete Flight\" name=\"delete\" id=\"delete\" /></form><form action=\"#\" method=\"post\" enctype=\"multipart/form-data\" acceptcharset=\"UTF-8\">    <p> Origin: {{origin}}</p>    <p> Destination: {{dest}}</p>    {{flightData}}<br>    <input type=\"submit\" value=\"Submit\" name=\"submit\" id=\"submit\"/>    <br/></form>").generateTemplateEngine();
    private static final Set<String> ALL_FIELDS = new HashSet<String>();

    public EditFlightGuide(AirDatabase db, WebSessionService webSessionService) {
        super(db, webSessionService);
    }

    private String getContents(Flight flight) {
        HashMap<String, String> contentsDictionary = new HashMap<String, String>();
        contentsDictionary.put("origin", flight.obtainOrigin().getName());
        contentsDictionary.put("dest", flight.grabDestination().getName());
        contentsDictionary.put("flightData", this.generateFlightDataHTML(flight));
        return ENGINE.replaceTags(contentsDictionary);
    }

    @Override
    public String getPath() {
        return "/edit_flight";
    }

    private String generateFlightDataHTML(Flight flight) {
        StringBuilder stringBuilder = new StringBuilder();
        String distance = Integer.toString(flight.grabDistance());
        stringBuilder.append(AddFlightGuide.pullFlightAttributeHTML("distance", "Distance", distance));
        String cost = Integer.toString(flight.takeFuelCosts());
        stringBuilder.append(AddFlightGuide.pullFlightAttributeHTML("cost", "Cost", cost));
        String time = Integer.toString(flight.getTravelTime());
        stringBuilder.append(AddFlightGuide.pullFlightAttributeHTML("time", "Travel Time", time));
        String crew = Integer.toString(flight.pullNumCrewMembers());
        stringBuilder.append(AddFlightGuide.pullFlightAttributeHTML("crewMembers", "Number of Crew Members", crew));
        String weight = Integer.toString(flight.getWeightLimit());
        stringBuilder.append(AddFlightGuide.pullFlightAttributeHTML("weightCapacity", "Weight Capacity", weight));
        String passengers = Integer.toString(flight.getPassengerLimit());
        stringBuilder.append(AddFlightGuide.pullFlightAttributeHTML("passengerCapacity", "Number of Passengers", passengers));
        return stringBuilder.toString();
    }

    private Pair<Flight, RouteMap> getRouteMapFlightPairFromPath(String remainingPath, Airline airline) throws NumberFormatException {
        RouteMap routeMap;
        String[] urlSplit = remainingPath.split("/");
        if (urlSplit.length == 4 && (routeMap = airline.getRouteMap(Integer.parseInt(urlSplit[1]))) != null) {
            return this.getRouteMapFlightPairFromPathFunction(urlSplit[3], routeMap);
        }
        return new ImmutablePair((Object)null, (Object)null);
    }

    private Pair<Flight, RouteMap> getRouteMapFlightPairFromPathFunction(String s, RouteMap routeMap) {
        Flight flight = routeMap.fetchFlight(Integer.parseInt(s));
        return new ImmutablePair((Object)flight, (Object)routeMap);
    }

    @Override
    protected HttpGuideResponse handlePull(HttpExchange httpExchange, String remainingPath, Airline airline) {
        try {
            Pair<Flight, RouteMap> flightRouteMapPair = this.getRouteMapFlightPairFromPath(remainingPath, airline);
            Flight flight = (Flight)flightRouteMapPair.getLeft();
            if (flight == null) {
                return EditFlightGuide.getErrorResponse(400, "This flight does not exist.");
            }
            return this.getTemplateResponse("Edit Flight Path", this.getContents(flight), airline);
        }
        catch (NumberFormatException e) {
            return this.fetchTemplateErrorResponse("Error while parsing the route map id. " + e.getMessage(), airline);
        }
        catch (NullPointerException e) {
            return EditFlightGuide.getErrorResponse(400, "Unable to parse the URL");
        }
    }

    @Override
    protected HttpGuideResponse handlePost(HttpExchange httpExchange, String remainingPath, Airline airline) {
        try {
            Pair<Flight, RouteMap> flightRouteMapPair = this.getRouteMapFlightPairFromPath(remainingPath, airline);
            Flight flight = (Flight)flightRouteMapPair.getLeft();
            RouteMap routeMap = (RouteMap)flightRouteMapPair.getRight();
            if (flight == null) {
                return EditFlightGuide.getErrorResponse(400, "This flight does not exist.");
            }
            Map<String, List<String>> data = MultipartHelper.fetchMultipartValues(httpExchange, ALL_FIELDS);
            if (data.containsKey("delete")) {
                routeMap.deleteFlight(flight);
            } else {
                String crewStr;
                String passengerStr;
                String costStr;
                String distanceStr;
                if (data.containsKey("distance") && !(distanceStr = data.get("distance").get(0)).isEmpty()) {
                    this.handlePostUtility(flight, distanceStr);
                }
                if (data.containsKey("cost") && !(costStr = data.get("cost").get(0)).isEmpty()) {
                    int newCost = Integer.parseInt(costStr);
                    flight.defineFuelCosts(newCost);
                }
                if (data.containsKey("time")) {
                    this.handlePostTarget(flight, data);
                }
                if (data.containsKey("crewMembers") && !(crewStr = data.get("crewMembers").get(0)).isEmpty()) {
                    this.handlePostGuide(flight, crewStr);
                }
                if (data.containsKey("weightCapacity")) {
                    this.handlePostAdviser(flight, data);
                }
                if (data.containsKey("passengerCapacity") && !(passengerStr = data.get("passengerCapacity").get(0)).isEmpty()) {
                    int passengers = Integer.parseInt(passengerStr);
                    flight.definePassengerLimit(passengers);
                }
            }
            return EditFlightGuide.getDefaultRedirectResponse();
        }
        catch (NumberFormatException e) {
            return this.fetchTemplateErrorResponse("Unable to parse number from string. " + e.getMessage(), airline);
        }
        catch (NullPointerException e) {
            return EditFlightGuide.getErrorResponse(400, "Unable to parse the URL");
        }
    }

    private void handlePostAdviser(Flight flight, Map<String, List<String>> data) {
        String weightStr = data.get("weightCapacity").get(0);
        if (!weightStr.isEmpty()) {
            this.handlePostAdviserWorker(flight, weightStr);
        }
    }

    private void handlePostAdviserWorker(Flight flight, String weightStr) {
        int weight = Integer.parseInt(weightStr);
        flight.fixWeightLimit(weight);
    }

    private void handlePostGuide(Flight flight, String crewStr) {
        int numCrewMembers = Integer.parseInt(crewStr);
        flight.fixNumCrewMembers(numCrewMembers);
    }

    private void handlePostTarget(Flight flight, Map<String, List<String>> data) {
        String timeStr = data.get("time").get(0);
        if (!timeStr.isEmpty()) {
            this.handlePostTargetGuide(flight, timeStr);
        }
    }

    private void handlePostTargetGuide(Flight flight, String timeStr) {
        int travelTime = Integer.parseInt(timeStr);
        flight.setTravelTime(travelTime);
    }

    private void handlePostUtility(Flight flight, String distanceStr) {
        int newDistance = Integer.parseInt(distanceStr);
        flight.setDistance(newDistance);
    }

    static {
        ALL_FIELDS.add("delete");
        ALL_FIELDS.add("cost");
        ALL_FIELDS.add("distance");
        ALL_FIELDS.add("time");
        ALL_FIELDS.add("crewMembers");
        ALL_FIELDS.add("weightCapacity");
        ALL_FIELDS.add("passengerCapacity");
    }
}

