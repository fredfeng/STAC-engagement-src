/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.flightplanner;

import edu.cyberapex.chart.BasicData;
import edu.cyberapex.chart.Chart;
import edu.cyberapex.chart.ChartFactory;
import edu.cyberapex.chart.ChartFailure;
import edu.cyberapex.chart.Data;
import edu.cyberapex.chart.Edge;
import edu.cyberapex.chart.Limit;
import edu.cyberapex.chart.LimitBuilder;
import edu.cyberapex.chart.Vertex;
import edu.cyberapex.flightplanner.AirFailure;
import edu.cyberapex.flightplanner.framework.Crew;
import edu.cyberapex.flightplanner.framework.Flight;
import edu.cyberapex.flightplanner.framework.RouteMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CrewOverseer {
    private RouteMap routeMap;
    private Chart crewSchedulingChart;

    public CrewOverseer(RouteMap routeMap) throws AirFailure {
        this.routeMap = routeMap;
        this.crewSchedulingChart = this.generateCrewSchedulingChart();
    }

    public List<Crew> grabCrewAssignments() throws AirFailure {
        try {
            Limit limit = new LimitBuilder().fixChart(this.crewSchedulingChart).generateLimit();
            Map<Vertex, Map<Vertex, Double>> edgeFlows = limit.fetchLimitPaths("source", "sink");
            HashMap<Flight, Crew> flightToCrewMap = new HashMap<Flight, Crew>();
            int currCrewNum = 1;
            for (Flight flight : this.routeMap.pullFlights()) {
                Crew crew = new Crew(currCrewNum++);
                crew.assignFlight(flight);
                flightToCrewMap.put(flight, crew);
            }
            for (Vertex v1 : edgeFlows.keySet()) {
                for (Vertex v2 : edgeFlows.get(v1).keySet()) {
                    if (edgeFlows.get(v1).get(v2) == 0.0) {
                        this.grabCrewAssignmentsExecutor();
                        continue;
                    }
                    if (!this.representsFlight(v1) || !this.representsFlight(v2)) continue;
                    this.grabCrewAssignmentsEngine(flightToCrewMap, v1, v2);
                }
            }
            ArrayList<Crew> uniqueCrews = new ArrayList<Crew>(new LinkedHashSet(flightToCrewMap.values()));
            Collections.sort(uniqueCrews);
            return uniqueCrews;
        }
        catch (ChartFailure e) {
            throw new AirFailure(e);
        }
    }

    private void grabCrewAssignmentsEngine(Map<Flight, Crew> flightToCrewMap, Vertex v1, Vertex v2) {
        Flight flight1 = this.takeFlightFromVertexName(v1);
        Flight flight2 = this.takeFlightFromVertexName(v2);
        Crew crew1 = flightToCrewMap.get(flight1);
        Crew crew2 = flightToCrewMap.get(flight2);
        if (crew1.takeId() != crew2.takeId()) {
            this.takeCrewAssignmentsEngineService(flightToCrewMap, crew1, crew2);
        }
    }

    private void takeCrewAssignmentsEngineService(Map<Flight, Crew> flightToCrewMap, Crew crew1, Crew crew2) {
        for (Flight flight : crew2.takeAssignedFlights()) {
            crew1.assignFlight(flight);
            flightToCrewMap.put(flight, crew1);
        }
    }

    private void grabCrewAssignmentsExecutor() {
    }

    private Chart generateCrewSchedulingChart() throws AirFailure {
        Chart chart = ChartFactory.newInstance();
        Map<Flight, List<Flight>> complementaryFlightsMap = CrewOverseer.generateComplementaryFlightsMap(this.routeMap.pullFlights());
        try {
            LinkedHashSet<Integer> basicVertexIds = new LinkedHashSet<Integer>();
            LinkedHashSet<Integer> compVertexIds = new LinkedHashSet<Integer>();
            for (Flight flight : this.routeMap.pullFlights()) {
                this.generateCrewSchedulingChartGuide(chart, basicVertexIds, compVertexIds, flight);
            }
            for (Flight flight : this.routeMap.pullFlights()) {
                String vertexName = this.obtainFlightBasicVertexName(flight);
                Vertex basicVertex = chart.obtainVertex(chart.getVertexIdByName(vertexName));
                for (Flight compFlight : complementaryFlightsMap.get(flight)) {
                    String compName = this.obtainFlightBasicVertexName(compFlight) + "Complementary";
                    Vertex compVertex = chart.obtainVertex(chart.getVertexIdByName(compName));
                    chart.addEdge(compVertex.getId(), basicVertex.getId(), new BasicData(1));
                }
            }
            Vertex source = chart.addVertex("source");
            Vertex sink = chart.addVertex("sink");
            for (Integer compVertexId : compVertexIds) {
                chart.addEdge(source.getId(), compVertexId, new BasicData(1));
            }
            for (Integer basicVertexId : basicVertexIds) {
                this.generateCrewSchedulingChartFunction(chart, sink, basicVertexId);
            }
        }
        catch (ChartFailure e) {
            throw new AirFailure(e);
        }
        return chart;
    }

    private void generateCrewSchedulingChartFunction(Chart chart, Vertex sink, Integer basicVertexId) throws ChartFailure {
        chart.addEdge(basicVertexId, sink.getId(), new BasicData(1));
    }

    private void generateCrewSchedulingChartGuide(Chart chart, Set<Integer> basicVertexIds, Set<Integer> compVertexIds, Flight flight) throws ChartFailure {
        String name = this.obtainFlightBasicVertexName(flight);
        Vertex vertex = chart.addVertex(name);
        vertex.setData(new BasicData(flight.grabId()));
        basicVertexIds.add(vertex.getId());
        Vertex compVertex = chart.addVertex(name + "Complementary");
        compVertex.setData(new BasicData(flight.grabId()));
        compVertexIds.add(compVertex.getId());
    }

    private boolean representsFlight(Vertex v) {
        return !v.getName().equals("source") && !v.getName().equals("sink");
    }

    private String obtainFlightBasicVertexName(Flight flight) {
        return Integer.toString(flight.grabId());
    }

    private Flight takeFlightFromVertexName(Vertex crewMapVertex) {
        String flight1Name = crewMapVertex.getName().replace("Complementary", "");
        return this.routeMap.fetchFlight(Integer.parseInt(flight1Name));
    }

    private static Map<Flight, List<Flight>> generateComplementaryFlightsMap(List<Flight> flights) {
        HashMap<Flight, List<Flight>> complementaryFlights = new HashMap<Flight, List<Flight>>();
        for (Flight flight : flights) {
            CrewOverseer.generateComplementaryFlightsMapAid(complementaryFlights, flight);
        }
        for (int b = 0; b < flights.size(); ++b) {
            Flight flight;
            flight = flights.get(b);
            for (int j = b; j < flights.size(); ++j) {
                new CrewOverseerHelper(flights, complementaryFlights, flight, j).invoke();
            }
        }
        return complementaryFlights;
    }

    private static void generateComplementaryFlightsMapAid(Map<Flight, List<Flight>> complementaryFlights, Flight flight) {
        complementaryFlights.put(flight, new ArrayList());
    }

    private static class CrewOverseerHelper {
        private List<Flight> flights;
        private Map<Flight, List<Flight>> complementaryFlights;
        private Flight flight;
        private int j;

        public CrewOverseerHelper(List<Flight> flights, Map<Flight, List<Flight>> complementaryFlights, Flight flight, int j) {
            this.flights = flights;
            this.complementaryFlights = complementaryFlights;
            this.flight = flight;
            this.j = j;
        }

        public void invoke() {
            Flight other = this.flights.get(this.j);
            if (other.canUseSameCrew(this.flight) && !this.complementaryFlights.get(other).contains(this.flight)) {
                new CrewOverseerHelperCoordinator(other).invoke();
            }
        }

        private class CrewOverseerHelperCoordinator {
            private Flight other;

            public CrewOverseerHelperCoordinator(Flight other) {
                this.other = other;
            }

            public void invoke() {
                ((List)CrewOverseerHelper.this.complementaryFlights.get(this.other)).add(CrewOverseerHelper.this.flight);
            }
        }

    }

}

