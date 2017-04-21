/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.airplan;

import com.networkapex.airplan.AirRaiser;
import com.networkapex.airplan.prototype.Crew;
import com.networkapex.airplan.prototype.Flight;
import com.networkapex.airplan.prototype.RouteMap;
import com.networkapex.chart.BasicData;
import com.networkapex.chart.Data;
import com.networkapex.chart.Edge;
import com.networkapex.chart.Graph;
import com.networkapex.chart.GraphFactory;
import com.networkapex.chart.GraphRaiser;
import com.networkapex.chart.Limit;
import com.networkapex.chart.LimitBuilder;
import com.networkapex.chart.Vertex;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CrewManager {
    private RouteMap routeMap;
    private Graph crewSchedulingGraph;

    public CrewManager(RouteMap routeMap) throws AirRaiser {
        this.routeMap = routeMap;
        this.crewSchedulingGraph = this.generateCrewSchedulingGraph();
    }

    public List<Crew> obtainCrewAssignments() throws AirRaiser {
        try {
            Limit limit = new LimitBuilder().fixGraph(this.crewSchedulingGraph).generateLimit();
            Map<Vertex, Map<Vertex, Double>> edgeFlows = limit.pullLimitTrails("source", "sink");
            HashMap<Flight, Crew> flightToCrewMap = new HashMap<Flight, Crew>();
            int currCrewNum = 1;
            for (Flight flight : this.routeMap.getFlights()) {
                Crew crew = new Crew(currCrewNum++);
                crew.assignFlight(flight);
                flightToCrewMap.put(flight, crew);
            }
            for (Vertex v1 : edgeFlows.keySet()) {
                for (Vertex v2 : edgeFlows.get(v1).keySet()) {
                    if (edgeFlows.get(v1).get(v2) == 0.0) {
                        this.pullCrewAssignmentsService();
                        continue;
                    }
                    if (!this.representsFlight(v1) || !this.representsFlight(v2)) continue;
                    Flight flight1 = this.grabFlightFromVertexName(v1);
                    Flight flight2 = this.grabFlightFromVertexName(v2);
                    Crew crew1 = (Crew)flightToCrewMap.get(flight1);
                    Crew crew2 = (Crew)flightToCrewMap.get(flight2);
                    if (crew1.fetchId() == crew2.fetchId()) continue;
                    this.fetchCrewAssignmentsHelper(flightToCrewMap, crew1, crew2);
                }
            }
            ArrayList<Crew> uniqueCrews = new ArrayList<Crew>(new LinkedHashSet(flightToCrewMap.values()));
            Collections.sort(uniqueCrews);
            return uniqueCrews;
        }
        catch (GraphRaiser e) {
            throw new AirRaiser(e);
        }
    }

    private void fetchCrewAssignmentsHelper(Map<Flight, Crew> flightToCrewMap, Crew crew1, Crew crew2) {
        for (Flight flight : crew2.getAssignedFlights()) {
            crew1.assignFlight(flight);
            flightToCrewMap.put(flight, crew1);
        }
    }

    private void pullCrewAssignmentsService() {
    }

    private Graph generateCrewSchedulingGraph() throws AirRaiser {
        Graph graph = GraphFactory.newInstance();
        Map<Flight, List<Flight>> complementaryFlightsMap = CrewManager.generateComplementaryFlightsMap(this.routeMap.getFlights());
        try {
            LinkedHashSet<Integer> basicVertexIds = new LinkedHashSet<Integer>();
            LinkedHashSet<Integer> compVertexIds = new LinkedHashSet<Integer>();
            for (Flight flight : this.routeMap.getFlights()) {
                this.generateCrewSchedulingGraphSupervisor(graph, basicVertexIds, compVertexIds, flight);
            }
            for (Flight flight : this.routeMap.getFlights()) {
                String vertexName = this.obtainFlightBasicVertexName(flight);
                Vertex basicVertex = graph.takeVertex(graph.takeVertexIdByName(vertexName));
                for (Flight compFlight : complementaryFlightsMap.get(flight)) {
                    new CrewManagerHelp(graph, basicVertex, compFlight).invoke();
                }
            }
            Vertex source = graph.addVertex("source");
            Vertex sink = graph.addVertex("sink");
            for (Integer compVertexId : compVertexIds) {
                this.generateCrewSchedulingGraphGuide(graph, source, compVertexId);
            }
            for (Integer basicVertexId : basicVertexIds) {
                graph.addEdge(basicVertexId, sink.getId(), new BasicData(1));
            }
        }
        catch (GraphRaiser e) {
            throw new AirRaiser(e);
        }
        return graph;
    }

    private void generateCrewSchedulingGraphGuide(Graph graph, Vertex source, Integer compVertexId) throws GraphRaiser {
        graph.addEdge(source.getId(), compVertexId, new BasicData(1));
    }

    private void generateCrewSchedulingGraphSupervisor(Graph graph, Set<Integer> basicVertexIds, Set<Integer> compVertexIds, Flight flight) throws GraphRaiser {
        String name = this.obtainFlightBasicVertexName(flight);
        Vertex vertex = graph.addVertex(name);
        vertex.setData(new BasicData(flight.takeId()));
        basicVertexIds.add(vertex.getId());
        Vertex compVertex = graph.addVertex(name + "Complementary");
        compVertex.setData(new BasicData(flight.takeId()));
        compVertexIds.add(compVertex.getId());
    }

    private boolean representsFlight(Vertex v) {
        return !v.getName().equals("source") && !v.getName().equals("sink");
    }

    private String obtainFlightBasicVertexName(Flight flight) {
        return Integer.toString(flight.takeId());
    }

    private Flight grabFlightFromVertexName(Vertex crewMapVertex) {
        String flight1Name = crewMapVertex.getName().replace("Complementary", "");
        return this.routeMap.fetchFlight(Integer.parseInt(flight1Name));
    }

    private static Map<Flight, List<Flight>> generateComplementaryFlightsMap(List<Flight> flights) {
        HashMap<Flight, List<Flight>> complementaryFlights = new HashMap<Flight, List<Flight>>();
        for (Flight flight : flights) {
            complementaryFlights.put(flight, new ArrayList());
        }
        int i = 0;
        while (i < flights.size()) {
            while (i < flights.size() && Math.random() < 0.6) {
                Flight flight;
                flight = flights.get(i);
                for (int j = i; j < flights.size(); ++j) {
                    CrewManager.generateComplementaryFlightsMapUtility(flights, complementaryFlights, flight, j);
                }
                ++i;
            }
        }
        return complementaryFlights;
    }

    private static void generateComplementaryFlightsMapUtility(List<Flight> flights, Map<Flight, List<Flight>> complementaryFlights, Flight flight, int j) {
        Flight other = flights.get(j);
        if (other.canUseSameCrew(flight) && !complementaryFlights.get(other).contains(flight)) {
            complementaryFlights.get(other).add(flight);
        }
    }

    private class CrewManagerHelp {
        private Graph graph;
        private Vertex basicVertex;
        private Flight compFlight;

        public CrewManagerHelp(Graph graph, Vertex basicVertex, Flight compFlight) {
            this.graph = graph;
            this.basicVertex = basicVertex;
            this.compFlight = compFlight;
        }

        public void invoke() throws GraphRaiser {
            String compName = CrewManager.this.obtainFlightBasicVertexName(this.compFlight) + "Complementary";
            Vertex compVertex = this.graph.takeVertex(this.graph.takeVertexIdByName(compName));
            this.graph.addEdge(compVertex.getId(), this.basicVertex.getId(), new BasicData(1));
        }
    }

}

