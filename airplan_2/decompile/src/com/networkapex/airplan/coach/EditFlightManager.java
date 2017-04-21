/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.airplan.coach;

import com.networkapex.airplan.coach.AddFlightManager;
import com.networkapex.airplan.coach.AirManager;
import com.networkapex.airplan.prototype.Airline;
import com.networkapex.airplan.prototype.Airport;
import com.networkapex.airplan.prototype.Flight;
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
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class EditFlightManager
extends AirManager {
    protected static final String TRAIL = "/edit_flight";
    private static final String TITLE = "Edit Flight Path";
    private static final String DELETE_FIELD = "delete";
    private static final String COST_FIELD = "cost";
    private static final String DISTANCE_FIELD = "distance";
    private static final String TIME_FIELD = "time";
    private static final String CREW_FIELD = "crewMembers";
    private static final String WEIGHT_FIELD = "weightCapacity";
    private static final String PASSENGER_FIELD = "passengerCapacity";
    private static final TemplateEngine ENGINE = new TemplateEngine("<form action=\"#\" method=\"post\" enctype=\"multipart/form-data\" acceptcharset=\"UTF-8\">    <input type=\"submit\" value=\"Delete Flight\" name=\"delete\" id=\"delete\" /></form><form action=\"#\" method=\"post\" enctype=\"multipart/form-data\" acceptcharset=\"UTF-8\">    <p> Origin: {{origin}}</p>    <p> Destination: {{dest}}</p>    {{flightData}}<br>    <input type=\"submit\" value=\"Submit\" name=\"submit\" id=\"submit\"/>    <br/></form>");
    private static final Set<String> ALL_FIELDS = new HashSet<String>();

    public EditFlightManager(AirDatabase db, WebSessionService webSessionService) {
        super(db, webSessionService);
    }

    private String pullContents(Flight flight) {
        HashMap<String, String> contentsDictionary = new HashMap<String, String>();
        contentsDictionary.put("origin", flight.takeOrigin().obtainName());
        contentsDictionary.put("dest", flight.getDestination().obtainName());
        contentsDictionary.put("flightData", this.generateFlightDataHTML(flight));
        return ENGINE.replaceTags(contentsDictionary);
    }

    @Override
    public String obtainTrail() {
        return "/edit_flight";
    }

    private String generateFlightDataHTML(Flight flight) {
        StringBuilder stringBuilder = new StringBuilder();
        String distance = Integer.toString(flight.pullDistance());
        stringBuilder.append(AddFlightManager.obtainFlightAttributeHTML("distance", "Distance", distance));
        String cost = Integer.toString(flight.grabFuelCosts());
        stringBuilder.append(AddFlightManager.obtainFlightAttributeHTML("cost", "Cost", cost));
        String time = Integer.toString(flight.getTravelTime());
        stringBuilder.append(AddFlightManager.obtainFlightAttributeHTML("time", "Travel Time", time));
        String crew = Integer.toString(flight.grabNumCrewMembers());
        stringBuilder.append(AddFlightManager.obtainFlightAttributeHTML("crewMembers", "Number of Crew Members", crew));
        String weight = Integer.toString(flight.takeWeightLimit());
        stringBuilder.append(AddFlightManager.obtainFlightAttributeHTML("weightCapacity", "Weight Capacity", weight));
        String passengers = Integer.toString(flight.pullPassengerLimit());
        stringBuilder.append(AddFlightManager.obtainFlightAttributeHTML("passengerCapacity", "Number of Passengers", passengers));
        return stringBuilder.toString();
    }

    private Pair<Flight, RouteMap> obtainRouteMapFlightPairFromTrail(String remainingTrail, Airline airline) throws NumberFormatException {
        EditFlightManagerAid editFlightManagerAid;
        String[] urlSplit = remainingTrail.split("/");
        if (urlSplit.length == 4 && (editFlightManagerAid = new EditFlightManagerAid(airline, urlSplit).invoke()).is()) {
            return new ImmutablePair<Flight, RouteMap>(editFlightManagerAid.pullFlight(), editFlightManagerAid.grabRouteMap());
        }
        return new ImmutablePair<>(null, null);
    }

    @Override
    protected HttpManagerResponse handlePull(HttpExchange httpExchange, String remainingTrail, Airline airline) {
        try {
            Pair<Flight, RouteMap> flightRouteMapPair = this.obtainRouteMapFlightPairFromTrail(remainingTrail, airline);
            Flight flight = flightRouteMapPair.getLeft();
            if (flight == null) {
                return EditFlightManager.fetchErrorResponse(400, "This flight does not exist.");
            }
            return this.grabTemplateResponse("Edit Flight Path", this.pullContents(flight), airline);
        }
        catch (NumberFormatException e) {
            return this.obtainTemplateErrorResponse("Error while parsing the route map id. " + e.getMessage(), airline);
        }
        catch (NullPointerException e) {
            return EditFlightManager.fetchErrorResponse(400, "Unable to parse the URL");
        }
    }

    @Override
    protected HttpManagerResponse handlePost(HttpExchange httpExchange, String remainingTrail, Airline airline) {
        try {
            Pair<Flight, RouteMap> flightRouteMapPair = this.obtainRouteMapFlightPairFromTrail(remainingTrail, airline);
            Flight flight = flightRouteMapPair.getLeft();
            RouteMap routeMap = flightRouteMapPair.getRight();
            if (flight == null) {
                return EditFlightManager.fetchErrorResponse(400, "This flight does not exist.");
            }
            Map<String, List<String>> data = MultipartHelper.getMultipartValues(httpExchange, ALL_FIELDS);
            if (data.containsKey("delete")) {
                this.handlePostManager(flight, routeMap);
            } else {
                this.handlePostSupervisor(flight, data);
            }
            return EditFlightManager.grabDefaultRedirectResponse();
        }
        catch (NumberFormatException e) {
            return this.obtainTemplateErrorResponse("Unable to parse number from string. " + e.getMessage(), airline);
        }
        catch (NullPointerException e) {
            return EditFlightManager.fetchErrorResponse(400, "Unable to parse the URL");
        }
    }

    private void handlePostSupervisor(Flight flight, Map<String, List<String>> data) {
        String distanceStr;
        String timeStr;
        if (data.containsKey("distance") && !(distanceStr = data.get("distance").get(0)).isEmpty()) {
            new EditFlightManagerTarget(flight, distanceStr).invoke();
        }
        if (data.containsKey("cost")) {
            this.handlePostSupervisorGuide(flight, data);
        }
        if (data.containsKey("time") && !(timeStr = data.get("time").get(0)).isEmpty()) {
            this.handlePostSupervisorTarget(flight, timeStr);
        }
        if (data.containsKey("crewMembers")) {
            this.handlePostSupervisorHelp(flight, data);
        }
        if (data.containsKey("weightCapacity")) {
            this.handlePostSupervisorGateKeeper(flight, data);
        }
        if (data.containsKey("passengerCapacity")) {
            this.handlePostSupervisorService(flight, data);
        }
    }

    private void handlePostSupervisorService(Flight flight, Map<String, List<String>> data) {
        String passengerStr = data.get("passengerCapacity").get(0);
        if (!passengerStr.isEmpty()) {
            int passengers = Integer.parseInt(passengerStr);
            flight.definePassengerLimit(passengers);
        }
    }

    private void handlePostSupervisorGateKeeper(Flight flight, Map<String, List<String>> data) {
        String weightStr = data.get("weightCapacity").get(0);
        if (!weightStr.isEmpty()) {
            int weight = Integer.parseInt(weightStr);
            flight.setWeightLimit(weight);
        }
    }

    private void handlePostSupervisorHelp(Flight flight, Map<String, List<String>> data) {
        String crewStr = data.get("crewMembers").get(0);
        if (!crewStr.isEmpty()) {
            this.handlePostSupervisorHelpHerder(flight, crewStr);
        }
    }

    private void handlePostSupervisorHelpHerder(Flight flight, String crewStr) {
        int numCrewMembers = Integer.parseInt(crewStr);
        flight.setNumCrewMembers(numCrewMembers);
    }

    private void handlePostSupervisorTarget(Flight flight, String timeStr) {
        int travelTime = Integer.parseInt(timeStr);
        flight.assignTravelTime(travelTime);
    }

    private void handlePostSupervisorGuide(Flight flight, Map<String, List<String>> data) {
        String costStr = data.get("cost").get(0);
        if (!costStr.isEmpty()) {
            this.handlePostSupervisorGuideHelp(flight, costStr);
        }
    }

    private void handlePostSupervisorGuideHelp(Flight flight, String costStr) {
        int newCost = Integer.parseInt(costStr);
        flight.setFuelCosts(newCost);
    }

    private void handlePostManager(Flight flight, RouteMap routeMap) {
        routeMap.deleteFlight(flight);
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

    private class EditFlightManagerTarget {
        private Flight flight;
        private String distanceStr;

        public EditFlightManagerTarget(Flight flight, String distanceStr) {
            this.flight = flight;
            this.distanceStr = distanceStr;
        }

        public void invoke() {
            int newDistance = Integer.parseInt(this.distanceStr);
            this.flight.defineDistance(newDistance);
        }
    }

    private class EditFlightManagerAid {
        private boolean myResult;
        private Airline airline;
        private String[] urlSplit;
        private RouteMap routeMap;
        private Flight flight;

        public EditFlightManagerAid(Airline airline, String[] urlSplit) {
            this.airline = airline;
            this.urlSplit = urlSplit;
        }

        boolean is() {
            return this.myResult;
        }

        public RouteMap grabRouteMap() {
            return this.routeMap;
        }

        public Flight pullFlight() {
            return this.flight;
        }

        public EditFlightManagerAid invoke() {
            this.routeMap = this.airline.getRouteMap(Integer.parseInt(this.urlSplit[1]));
            if (this.routeMap != null) {
                this.flight = this.routeMap.fetchFlight(Integer.parseInt(this.urlSplit[3]));
                this.myResult = true;
                return this;
            }
            this.myResult = false;
            return this;
        }
    }

}

