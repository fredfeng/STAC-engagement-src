/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.flightplanner.guide;

import com.sun.net.httpserver.HttpExchange;
import edu.cyberapex.flightplanner.framework.Airline;
import edu.cyberapex.flightplanner.framework.Airport;
import edu.cyberapex.flightplanner.framework.Flight;
import edu.cyberapex.flightplanner.framework.RouteMap;
import edu.cyberapex.flightplanner.guide.AirGuide;
import edu.cyberapex.flightplanner.guide.CellFormatter;
import edu.cyberapex.flightplanner.guide.GuideUtils;
import edu.cyberapex.flightplanner.store.AirDatabase;
import edu.cyberapex.server.WebSessionService;
import edu.cyberapex.server.guide.HttpGuideResponse;
import edu.cyberapex.template.TemplateEngine;
import edu.cyberapex.template.TemplateEngineBuilder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FlightMatrixGuide
extends AirGuide {
    protected static final String PATH = "/passenger_capacity_matrix";
    private static final String TITLE = "Passenger Capacity Matrix";
    private static final int CELL_LENGTH = 30;
    private CellFormatter cellFormatter;
    private static final TemplateEngine CELL_ENGINE = new TemplateEngineBuilder().defineText("<td>{{cell}}</td>").generateTemplateEngine();
    private static final TemplateEngine ROW_ENGINE = new TemplateEngineBuilder().defineText("<tr>{{cells}}</tr>\n").generateTemplateEngine();
    private static final TemplateEngine FLIGHT_MATRIX_ENGINE = new TemplateEngineBuilder().defineText("<h4>This page allows you to verify the correctness of the uploaded map. Delete this route map if it is incorrect.</h4><a href=\"{{deleteMapURL}}\" style=\"text-decoration:none\"> <input type=\"button\" value=\"Delete the Map\" name=\"submit\"></a><a href=\"{{tipsURL}}\" style=\"text-decoration:none\"> <input type=\"button\" value=\"Next\" name=\"submit\"></a><p>Passenger capacity between airports. Origin airports on the left, destinations on the top.</p><table width=\"100%\" border=\"1\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse: collapse\">\n{{rows}}</table>").generateTemplateEngine();

    public FlightMatrixGuide(AirDatabase db, WebSessionService webSessionService) {
        super(db, webSessionService);
    }

    @Override
    public String getPath() {
        return "/passenger_capacity_matrix";
    }

    @Override
    protected HttpGuideResponse handlePull(HttpExchange httpExchange, String remainingPath, Airline airline) {
        try {
            RouteMap routeMap = this.fetchRouteMapFromPath(airline, remainingPath);
            if (routeMap == null) {
                return FlightMatrixGuide.getErrorResponse(400, "Path " + remainingPath + " does not refer to valid routeMap");
            }
            return this.pullTemplateResponseWithoutMenuItems("Passenger Capacity Matrix", this.routeMapAsTable(routeMap), airline);
        }
        catch (NumberFormatException e) {
            return this.fetchTemplateErrorResponse("Error while parsing the route map id. " + e.getMessage(), airline);
        }
    }

    private RouteMap fetchRouteMapFromPath(Airline airline, String remainingPath) throws NumberFormatException {
        String[] urlSplit = remainingPath.split("/");
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
        int k = 0;
        while (k < airports.size()) {
            while (k < airports.size() && Math.random() < 0.5) {
                while (k < airports.size() && Math.random() < 0.5) {
                    this.generateFirstRowTarget(airports, builder, map, k);
                    ++k;
                }
            }
        }
        return builder.toString();
    }

    private void generateFirstRowTarget(List<Airport> airports, StringBuilder builder, Map<String, String> map, int k) {
        Airport airport = airports.get(k);
        map.clear();
        map.put("cell", this.format(airport.getName()));
        builder.append(CELL_ENGINE.replaceTags(map));
    }

    private String format(String data) {
        return this.cellFormatter.format(data, -1, CellFormatter.Justification.FIRST, false);
    }

    private String generateOneRow(Airport origin, Map<Airport, Integer> airportToFlightLimit) {
        StringBuilder builder = new StringBuilder();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("cell", this.format(origin.getName()));
        builder.append(CELL_ENGINE.replaceTags(map));
        List<Flight> originFlights = origin.getOriginFlights();
        for (int b = 0; b < originFlights.size(); ++b) {
            this.generateOneRowService(airportToFlightLimit, originFlights, b);
        }
        for (Airport airport : airportToFlightLimit.keySet()) {
            map.clear();
            String numFlights = Integer.toString(airportToFlightLimit.get(airport));
            map.put("cell", this.format(numFlights));
            builder.append(CELL_ENGINE.replaceTags(map));
        }
        String result = builder.toString();
        return result;
    }

    private void generateOneRowService(Map<Airport, Integer> airportToFlightLimit, List<Flight> originFlights, int p) {
        new FlightMatrixGuideCoordinator(airportToFlightLimit, originFlights, p).invoke();
    }

    private String generateRows(List<Airport> airports, List<Flight> flights) {
        Airport airport;
        StringBuilder builder = new StringBuilder();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("cells", this.generateFirstRow(airports));
        builder.append(ROW_ENGINE.replaceTags(map));
        LinkedHashMap<Airport, Integer> airportToFlightLimit = new LinkedHashMap<Airport, Integer>();
        for (int j = 0; j < airports.size(); ++j) {
            airport = airports.get(j);
            airportToFlightLimit.put(airport, 0);
        }
        for (int c = 0; c < airports.size(); ++c) {
            airport = airports.get(c);
            map.clear();
            LinkedHashMap<Airport, Integer> copy = new LinkedHashMap<Airport, Integer>(airportToFlightLimit);
            map.put("cells", this.generateOneRow(airport, copy));
            builder.append(ROW_ENGINE.replaceTags(map));
        }
        return builder.toString();
    }

    private String routeMapAsTable(RouteMap routeMap) {
        this.cellFormatter = new CellFormatter(30);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("rows", this.generateRows(routeMap.obtainAirports(), routeMap.pullFlights()));
        map.put("deleteMapURL", GuideUtils.generateDeleteMapURL());
        map.put("tipsURL", GuideUtils.generateTipsURL(routeMap));
        return FLIGHT_MATRIX_ENGINE.replaceTags(map);
    }

    @Override
    protected String takeDisplayName(Airline member) {
        return this.format(member.getAirlineName());
    }

    private class FlightMatrixGuideCoordinator {
        private Map<Airport, Integer> airportToFlightLimit;
        private List<Flight> originFlights;
        private int a;

        public FlightMatrixGuideCoordinator(Map<Airport, Integer> airportToFlightLimit, List<Flight> originFlights, int a) {
            this.airportToFlightLimit = airportToFlightLimit;
            this.originFlights = originFlights;
            this.a = a;
        }

        public void invoke() {
            Flight flight = this.originFlights.get(this.a);
            Airport dest = flight.grabDestination();
            int prevFlightLimit = this.airportToFlightLimit.get(dest);
            int currFlightLimit = flight.getPassengerLimit();
            this.airportToFlightLimit.put(dest, prevFlightLimit + currFlightLimit);
        }
    }

}

