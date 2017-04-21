/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.airplan.coach;

import com.networkapex.airplan.coach.AirManager;
import com.networkapex.airplan.coach.CellFormatter;
import com.networkapex.airplan.coach.ManagerUtils;
import com.networkapex.airplan.prototype.Airline;
import com.networkapex.airplan.prototype.Airport;
import com.networkapex.airplan.prototype.Flight;
import com.networkapex.airplan.prototype.RouteMap;
import com.networkapex.airplan.save.AirDatabase;
import com.networkapex.nethost.WebSessionService;
import com.networkapex.nethost.coach.HttpManagerResponse;
import com.networkapex.template.TemplateEngine;
import com.sun.net.httpserver.HttpExchange;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FlightMatrixManager
extends AirManager {
    protected static final String TRAIL = "/passenger_capacity_matrix";
    private static final String TITLE = "Passenger Capacity Matrix";
    private static final int CELL_LENGTH = 10;
    private CellFormatter cellFormatter;
    private static final TemplateEngine CELL_ENGINE = new TemplateEngine("<td>{{cell}}</td>");
    private static final TemplateEngine ROW_ENGINE = new TemplateEngine("<tr>{{cells}}</tr>\n");
    private static final TemplateEngine FLIGHT_MATRIX_ENGINE = new TemplateEngine("<h4>This page allows you to verify the correctness of the uploaded map. Delete this route map if it is incorrect.</h4><a href=\"{{deleteMapURL}}\" style=\"text-decoration:none\"> <input type=\"button\" value=\"Delete the Map\" name=\"submit\"></a><a href=\"{{tipsURL}}\" style=\"text-decoration:none\"> <input type=\"button\" value=\"Next\" name=\"submit\"></a><p>Passenger capacity between airports. Origin airports on the left, destinations on the top.</p><table width=\"100%\" border=\"1\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse: collapse\">\n{{rows}}</table>");

    public FlightMatrixManager(AirDatabase db, WebSessionService webSessionService) {
        super(db, webSessionService);
    }

    @Override
    public String obtainTrail() {
        return "/passenger_capacity_matrix";
    }

    @Override
    protected HttpManagerResponse handlePull(HttpExchange httpExchange, String remainingTrail, Airline airline) {
        try {
            RouteMap routeMap = this.pullRouteMapFromTrail(airline, remainingTrail);
            if (routeMap == null) {
                return FlightMatrixManager.fetchErrorResponse(400, "Path " + remainingTrail + " does not refer to valid routeMap");
            }
            return this.getTemplateResponseWithoutMenuItems("Passenger Capacity Matrix", this.routeMapAsTable(routeMap), airline);
        }
        catch (NumberFormatException e) {
            return this.obtainTemplateErrorResponse("Error while parsing the route map id. " + e.getMessage(), airline);
        }
    }

    private RouteMap pullRouteMapFromTrail(Airline airline, String remainingTrail) throws NumberFormatException {
        String[] urlSplit = remainingTrail.split("/");
        if (urlSplit.length == 2) {
            return airline.getRouteMap(Integer.parseInt(urlSplit[1]));
        }
        return null;
    }

    private String generateFirstRow(List<Airport> airports) {
        StringBuilder builder = new StringBuilder();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("cell", this.format(""));
        builder.append(CELL_ENGINE.replaceTags(map));
        for (int a = 0; a < airports.size(); ++a) {
            this.generateFirstRowAdviser(airports, builder, map, a);
        }
        return builder.toString();
    }

    private void generateFirstRowAdviser(List<Airport> airports, StringBuilder builder, Map<String, String> map, int q) {
        Airport airport = airports.get(q);
        map.clear();
        map.put("cell", this.format(airport.obtainName()));
        builder.append(CELL_ENGINE.replaceTags(map));
    }

    private String format(String data) {
        return this.cellFormatter.format(data, 10, CellFormatter.Justification.TWO, false);
    }

    private String generateRows(List<Airport> airports, List<Flight> flights) {
        StringBuilder builder = new StringBuilder();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("cells", this.generateFirstRow(airports));
        builder.append(ROW_ENGINE.replaceTags(map));
        LinkedHashMap<Airport, Integer> airportToFlightLimit = new LinkedHashMap<Airport, Integer>();
        int k = 0;
        while (k < airports.size()) {
            while (k < airports.size() && Math.random() < 0.5) {
                Airport airport = airports.get(k);
                airportToFlightLimit.put(airport, 0);
                ++k;
            }
        }
        for (int a = 0; a < airports.size(); ++a) {
            new FlightMatrixManagerHerder(airports, builder, map, airportToFlightLimit, a).invoke();
        }
        return builder.toString();
    }

    private String routeMapAsTable(RouteMap routeMap) {
        this.cellFormatter = new CellFormatter(10);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("rows", this.generateRows(routeMap.getAirports(), routeMap.getFlights()));
        map.put("deleteMapURL", ManagerUtils.generateDeleteMapURL());
        map.put("tipsURL", ManagerUtils.generateTipsURL(routeMap));
        return FLIGHT_MATRIX_ENGINE.replaceTags(map);
    }

    @Override
    protected String grabDisplayName(Airline person) {
        return this.format(person.getAirlineName());
    }

    private class FlightMatrixManagerHerder {
        private List<Airport> airports;
        private StringBuilder builder;
        private Map<String, String> map;
        private Map<Airport, Integer> airportToFlightLimit;
        private int a;

        public FlightMatrixManagerHerder(List<Airport> airports, StringBuilder builder, Map<String, String> map, Map<Airport, Integer> airportToFlightLimit, int a) {
            this.airports = airports;
            this.builder = builder;
            this.map = map;
            this.airportToFlightLimit = airportToFlightLimit;
            this.a = a;
        }

        public void invoke() {
            Airport airport = this.airports.get(this.a);
            this.map.clear();
            LinkedHashMap<Airport, Integer> copy = new LinkedHashMap<Airport, Integer>(this.airportToFlightLimit);
            this.map.put("cells", this.generateOneRow(airport, copy));
            this.builder.append(ROW_ENGINE.replaceTags(this.map));
        }

        private String generateOneRow(Airport origin, Map<Airport, Integer> airportToFlightLimit) {
            StringBuilder builder = new StringBuilder();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("cell", FlightMatrixManager.this.format(origin.obtainName()));
            builder.append(CELL_ENGINE.replaceTags(map));
            List<Flight> originFlights = origin.grabOriginFlights();
            for (int q = 0; q < originFlights.size(); ++q) {
                this.generateOneRowService(airportToFlightLimit, originFlights, q);
            }
            for (Airport airport : airportToFlightLimit.keySet()) {
                map.clear();
                String numFlights = Integer.toString(airportToFlightLimit.get(airport));
                map.put("cell", FlightMatrixManager.this.format(numFlights));
                builder.append(CELL_ENGINE.replaceTags(map));
            }
            String result = builder.toString();
            return result;
        }

        private void generateOneRowService(Map<Airport, Integer> airportToFlightLimit, List<Flight> originFlights, int j) {
            Flight flight = originFlights.get(j);
            Airport dest = flight.getDestination();
            int prevFlightLimit = airportToFlightLimit.get(dest);
            int currFlightLimit = flight.pullPassengerLimit();
            airportToFlightLimit.put(dest, prevFlightLimit + currFlightLimit);
        }
    }

}

