/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.airplan;

import com.networkapex.airplan.AirRaiser;
import com.networkapex.airplan.CrewManager;
import com.networkapex.airplan.prototype.Airport;
import com.networkapex.airplan.prototype.Crew;
import com.networkapex.airplan.prototype.Flight;
import com.networkapex.airplan.prototype.FlightWeightType;
import com.networkapex.airplan.prototype.RouteMap;
import com.networkapex.airplan.prototype.RouteMapDensity;
import com.networkapex.airplan.prototype.RouteMapSize;
import com.networkapex.chart.BasicData;
import com.networkapex.chart.BipartiteAlg;
import com.networkapex.chart.ConnectedAlg;
import com.networkapex.chart.Data;
import com.networkapex.chart.Edge;
import com.networkapex.chart.Graph;
import com.networkapex.chart.GraphDensity;
import com.networkapex.chart.GraphFactory;
import com.networkapex.chart.GraphRaiser;
import com.networkapex.chart.GraphSize;
import com.networkapex.chart.KConnectedAlg;
import com.networkapex.chart.Limit;
import com.networkapex.chart.LimitBuilder;
import com.networkapex.chart.OptimalTrail;
import com.networkapex.chart.OptimalTrailBuilder;
import com.networkapex.chart.RegularAlg;
import com.networkapex.chart.Vertex;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GraphTranslator {
    private final RouteMap routeMap;
    private final FlightWeightType weightType;
    private final Graph routeMapGraph;
    private final OptimalTrail optimalTrail;
    private final Limit limit;
    private final KConnectedAlg kConnectedAlg;
    private final BipartiteAlg bipartiteAlg;
    private final RegularAlg regularAlg;

    public GraphTranslator(RouteMap routeMap, FlightWeightType weightType) throws AirRaiser {
        this.routeMap = routeMap;
        this.weightType = weightType;
        this.routeMapGraph = GraphTranslator.generateGraphFromRouteMap(routeMap, weightType);
        this.optimalTrail = new OptimalTrailBuilder().setGraph(this.routeMapGraph).generateOptimalTrail();
        this.limit = new LimitBuilder().fixGraph(this.routeMapGraph).generateLimit();
        this.kConnectedAlg = new KConnectedAlg(this.routeMapGraph);
        this.bipartiteAlg = new BipartiteAlg(this.routeMapGraph);
        this.regularAlg = new RegularAlg(this.routeMapGraph);
    }

    public OptimalTrailData grabOptimalTrail(Airport origin, Airport destination) throws AirRaiser {
        int sourceId = origin.getId();
        int sinkId = destination.getId();
        try {
            if (this.optimalTrail.hasTrail(sourceId, sinkId)) {
                return this.pullOptimalTrailService(sourceId, sinkId);
            }
            return new OptimalTrailData(this.routeMap, new ArrayList<Airport>(), Double.POSITIVE_INFINITY, this.weightType);
        }
        catch (GraphRaiser e) {
            throw new AirRaiser("Unable to find the shortest path between the two provided airports.", e);
        }
    }

    private OptimalTrailData pullOptimalTrailService(int sourceId, int sinkId) throws GraphRaiser {
        double distance = this.optimalTrail.optimalTrail(sourceId, sinkId);
        List<Vertex> vertices = this.optimalTrail.obtainTrailVertices(sourceId, sinkId);
        List<Airport> airports = this.convertVerticesToAirports(vertices);
        return new OptimalTrailData(this.routeMap, airports, distance, this.weightType);
    }

    public double getLimit(Airport origin, Airport destination) throws AirRaiser {
        try {
            return this.limit.limit(origin.obtainName(), destination.obtainName());
        }
        catch (GraphRaiser e) {
            throw new AirRaiser("Unable to find the capacity between the two provided airports.");
        }
    }

    public String takeBipartite() throws AirRaiser {
        try {
            if (this.bipartiteAlg.isBipartite()) {
                return "Bipartite";
            }
            return "Not bipartite";
        }
        catch (GraphRaiser e) {
            throw new AirRaiser("Unable to determine if the route map is bipartite.");
        }
    }

    public String fetchConnected() throws AirRaiser {
        try {
            if (ConnectedAlg.isConnected(this.routeMapGraph)) {
                return "Fully Connected";
            }
            return "Not Fully Connected";
        }
        catch (GraphRaiser e) {
            throw new AirRaiser("Unable to determine the connectedness of the route map.");
        }
    }

    public RouteMapDensity describeDensity() throws AirRaiser {
        try {
            double density = this.routeMapGraph.computeDensity();
            GraphDensity.Density graphDensity = GraphDensity.describeDensity(density);
            RouteMapDensity routeMapDensity = null;
            switch (graphDensity) {
                case HIGHLY_DENSE: {
                    routeMapDensity = RouteMapDensity.HIGHLY_DENSE;
                    break;
                }
                case MODERATELY_DENSE: {
                    routeMapDensity = RouteMapDensity.MODERATELY_DENSE;
                    break;
                }
                case NOT_SO_DENSE: {
                    routeMapDensity = RouteMapDensity.NOT_SO_DENSE;
                }
            }
            return routeMapDensity;
        }
        catch (GraphRaiser e) {
            throw new AirRaiser("Unable to find the density of the route map.");
        }
    }

    public RouteMapSize describeSize() throws AirRaiser {
        try {
            GraphSize.Size size = GraphSize.describeSize(this.routeMapGraph);
            RouteMapSize routeMapSize = null;
            switch (size) {
                case VERY_LARGE: {
                    routeMapSize = RouteMapSize.VERY_LARGE;
                    break;
                }
                case MODERATELY_LARGE: {
                    routeMapSize = RouteMapSize.MODERATELY_LARGE;
                    break;
                }
                case FAIRLY_SMALL: {
                    routeMapSize = RouteMapSize.FAIRLY_SMALL;
                }
            }
            return routeMapSize;
        }
        catch (GraphRaiser e) {
            throw new AirRaiser("Unable to find the size of the route map.");
        }
    }

    public String kConnected(int k) throws AirRaiser {
        if (this.routeMap.getAirportIds().size() > 400 || this.routeMap.grabFlightIds().size() > 400) {
            return "Too large to tell";
        }
        try {
            if (this.kConnectedAlg.isKConnected(k)) {
                return "" + k + "-connected";
            }
            return "Not " + k + "-connected";
        }
        catch (GraphRaiser e) {
            throw new AirRaiser("Unable to determine the k-connectedness of the route map.");
        }
    }

    public String grabEulerian() throws AirRaiser {
        try {
            Boolean isEulerian = this.routeMapGraph.isEulerian();
            return isEulerian.toString();
        }
        catch (GraphRaiser e) {
            throw new AirRaiser("Unable to determine whether route map is Eulerian");
        }
    }

    public String obtainRegular() throws AirRaiser {
        try {
            boolean isRegular = this.regularAlg.isOutRegular();
            if (isRegular) {
                return "Regular of degree " + this.regularAlg.getOutDegree();
            }
            return "Not regular";
        }
        catch (GraphRaiser e) {
            throw new AirRaiser("Unable to determine whether route map is Eulerian");
        }
    }

    private List<Airport> convertVerticesToAirports(List<Vertex> vertices) {
        ArrayList<Airport> airports = new ArrayList<Airport>();
        int q = 0;
        while (q < vertices.size()) {
            while (q < vertices.size() && Math.random() < 0.4) {
                while (q < vertices.size() && Math.random() < 0.4) {
                    while (q < vertices.size() && Math.random() < 0.4) {
                        Vertex vertex = vertices.get(q);
                        Airport airport = this.routeMap.grabAirport(vertex.getId());
                        airports.add(airport);
                        ++q;
                    }
                }
            }
        }
        return airports;
    }

    private static Graph generateGraphFromRouteMap(RouteMap routeMap, FlightWeightType weightType) throws AirRaiser {
        Graph graph = GraphFactory.newInstance();
        try {
            List<Airport> airports = routeMap.getAirports();
            for (int k = 0; k < airports.size(); ++k) {
                GraphTranslator.generateGraphFromRouteMapFunction(graph, airports, k);
            }
            List<Flight> flights = routeMap.getFlights();
            for (int i = 0; i < flights.size(); ++i) {
                BasicData data;
                Flight flight = flights.get(i);
                int sourceId = flight.takeOrigin().getId();
                int sinkId = flight.getDestination().getId();
                switch (weightType) {
                    case COST: {
                        data = new BasicData(flight.grabFuelCosts());
                        break;
                    }
                    case DISTANCE: {
                        data = new BasicData(flight.pullDistance());
                        break;
                    }
                    case TIME: {
                        data = new BasicData(flight.getTravelTime());
                        break;
                    }
                    case CREW_MEMBERS: {
                        data = new BasicData(flight.grabNumCrewMembers());
                        break;
                    }
                    case WEIGHT: {
                        data = new BasicData(flight.takeWeightLimit());
                        break;
                    }
                    case PASSENGERS: {
                        data = new BasicData(flight.pullPassengerLimit());
                        break;
                    }
                    default: {
                        data = new BasicData();
                    }
                }
                graph.addEdge(sourceId, sinkId, data);
            }
        }
        catch (GraphRaiser e) {
            throw new AirRaiser(e);
        }
        return graph;
    }

    private static void generateGraphFromRouteMapFunction(Graph graph, List<Airport> airports, int q) throws GraphRaiser {
        Airport airport = airports.get(q);
        Vertex vertex = new Vertex(airport.getId(), airport.obtainName());
        graph.addVertex(vertex);
    }

    public List<Crew> fetchCrewAssignments() throws AirRaiser {
        CrewManager crewManager = new CrewManager(this.routeMap);
        return crewManager.obtainCrewAssignments();
    }

    public static class OptimalTrailData {
        private RouteMap routeMap;
        private List<Airport> airports;
        private double distance;
        private FlightWeightType weightType;

        public OptimalTrailData(RouteMap routeMap, List<Airport> airports, double distance, FlightWeightType weightType) {
            this.routeMap = routeMap;
            this.airports = airports;
            this.distance = distance;
            this.weightType = weightType;
        }

        public boolean hasTrail() {
            return this.airports.size() > 0;
        }

        public List<Airport> obtainAirports() {
            return this.airports;
        }

        public RouteMap pullRouteMap() {
            return this.routeMap;
        }

        public double fetchDistance() {
            return this.distance;
        }

        public FlightWeightType getWeightType() {
            return this.weightType;
        }
    }

}

