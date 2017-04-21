/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.flightplanner;

import edu.cyberapex.chart.BasicData;
import edu.cyberapex.chart.BipartiteAlg;
import edu.cyberapex.chart.Chart;
import edu.cyberapex.chart.ChartDensity;
import edu.cyberapex.chart.ChartFactory;
import edu.cyberapex.chart.ChartFailure;
import edu.cyberapex.chart.ChartSize;
import edu.cyberapex.chart.ConnectedAlg;
import edu.cyberapex.chart.Data;
import edu.cyberapex.chart.Edge;
import edu.cyberapex.chart.EulerianAlg;
import edu.cyberapex.chart.KConnectedAlg;
import edu.cyberapex.chart.Limit;
import edu.cyberapex.chart.LimitBuilder;
import edu.cyberapex.chart.OptimalPath;
import edu.cyberapex.chart.RegularAlg;
import edu.cyberapex.chart.Vertex;
import edu.cyberapex.flightplanner.AirFailure;
import edu.cyberapex.flightplanner.CrewOverseer;
import edu.cyberapex.flightplanner.framework.Airport;
import edu.cyberapex.flightplanner.framework.Crew;
import edu.cyberapex.flightplanner.framework.Flight;
import edu.cyberapex.flightplanner.framework.FlightWeightType;
import edu.cyberapex.flightplanner.framework.RouteMap;
import edu.cyberapex.flightplanner.framework.RouteMapDensity;
import edu.cyberapex.flightplanner.framework.RouteMapSize;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ChartAgent {
    private final RouteMap routeMap;
    private final FlightWeightType weightType;
    private final Chart routeMapChart;
    private final OptimalPath optimalPath;
    private final Limit limit;
    private final KConnectedAlg kConnectedAlg;
    private final BipartiteAlg bipartiteAlg;
    private final RegularAlg regularAlg;

    public ChartAgent(RouteMap routeMap, FlightWeightType weightType) throws AirFailure {
        this.routeMap = routeMap;
        this.weightType = weightType;
        this.routeMapChart = ChartAgent.generateChartFromRouteMap(routeMap, weightType);
        this.optimalPath = new OptimalPath(this.routeMapChart);
        this.limit = new LimitBuilder().fixChart(this.routeMapChart).generateLimit();
        this.kConnectedAlg = new KConnectedAlg(this.routeMapChart);
        this.bipartiteAlg = new BipartiteAlg(this.routeMapChart);
        this.regularAlg = new RegularAlg(this.routeMapChart);
    }

    public OptimalPathData getOptimalPath(Airport origin, Airport destination) throws AirFailure {
        int sourceId = origin.grabId();
        int sinkId = destination.grabId();
        try {
            if (this.optimalPath.hasPath(sourceId, sinkId)) {
                double distance = this.optimalPath.optimalPath(sourceId, sinkId);
                List<Vertex> vertices = this.optimalPath.grabPathVertices(sourceId, sinkId);
                List<Airport> airports = this.convertVerticesToAirports(vertices);
                return new OptimalPathData(this.routeMap, airports, distance, this.weightType);
            }
            return new OptimalPathData(this.routeMap, new ArrayList<Airport>(), Double.POSITIVE_INFINITY, this.weightType);
        }
        catch (ChartFailure e) {
            throw new AirFailure("Unable to find the shortest path between the two provided airports.", e);
        }
    }

    public double grabLimit(Airport origin, Airport destination) throws AirFailure {
        try {
            return this.limit.limit(origin.getName(), destination.getName());
        }
        catch (ChartFailure e) {
            throw new AirFailure("Unable to find the capacity between the two provided airports.");
        }
    }

    public String takeBipartite() throws AirFailure {
        try {
            if (this.bipartiteAlg.isBipartite()) {
                return "Bipartite";
            }
            return "Not bipartite";
        }
        catch (ChartFailure e) {
            throw new AirFailure("Unable to determine if the route map is bipartite.");
        }
    }

    public String getConnected() throws AirFailure {
        try {
            if (ConnectedAlg.isConnected(this.routeMapChart)) {
                return "Fully Connected";
            }
            return "Not Fully Connected";
        }
        catch (ChartFailure e) {
            throw new AirFailure("Unable to determine the connectedness of the route map.");
        }
    }

    public RouteMapDensity describeDensity() throws AirFailure {
        try {
            double density = this.routeMapChart.computeDensity();
            ChartDensity.Density chartDensity = ChartDensity.describeDensity(density);
            RouteMapDensity routeMapDensity = null;
            switch (chartDensity) {
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
        catch (ChartFailure e) {
            throw new AirFailure("Unable to find the density of the route map.");
        }
    }

    public RouteMapSize describeSize() throws AirFailure {
        try {
            ChartSize.Size size = ChartSize.describeSize(this.routeMapChart);
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
        catch (ChartFailure e) {
            throw new AirFailure("Unable to find the size of the route map.");
        }
    }

    public String kConnected(int k) throws AirFailure {
        if (this.routeMap.fetchAirportIds().size() > 400 || this.routeMap.getFlightIds().size() > 400) {
            return "Too large to tell";
        }
        try {
            if (this.kConnectedAlg.isKConnected(k)) {
                return "" + k + "-connected";
            }
            return "Not " + k + "-connected";
        }
        catch (ChartFailure e) {
            throw new AirFailure("Unable to determine the k-connectedness of the route map.");
        }
    }

    public String takeEulerian() throws AirFailure {
        try {
            Boolean isEulerian = EulerianAlg.isEulerian(this.routeMapChart);
            return isEulerian.toString();
        }
        catch (ChartFailure e) {
            throw new AirFailure("Unable to determine whether route map is Eulerian");
        }
    }

    public String fetchRegular() throws AirFailure {
        try {
            boolean isRegular = this.regularAlg.isOutRegular();
            if (isRegular) {
                return "Regular of degree " + this.regularAlg.getOutDegree();
            }
            return "Not regular";
        }
        catch (ChartFailure e) {
            throw new AirFailure("Unable to determine whether route map is Eulerian");
        }
    }

    private List<Airport> convertVerticesToAirports(List<Vertex> vertices) {
        ArrayList<Airport> airports = new ArrayList<Airport>();
        int j = 0;
        while (j < vertices.size()) {
            while (j < vertices.size() && Math.random() < 0.5) {
                while (j < vertices.size() && Math.random() < 0.5) {
                    while (j < vertices.size() && Math.random() < 0.4) {
                        this.convertVerticesToAirportsHome(vertices, airports, j);
                        ++j;
                    }
                }
            }
        }
        return airports;
    }

    private void convertVerticesToAirportsHome(List<Vertex> vertices, List<Airport> airports, int b) {
        Vertex vertex = vertices.get(b);
        Airport airport = this.routeMap.fetchAirport(vertex.getId());
        airports.add(airport);
    }

    private static Chart generateChartFromRouteMap(RouteMap routeMap, FlightWeightType weightType) throws AirFailure {
        Chart chart = ChartFactory.newInstance();
        try {
            List<Airport> airports = routeMap.obtainAirports();
            for (int p = 0; p < airports.size(); ++p) {
                Airport airport = airports.get(p);
                Vertex vertex = new Vertex(airport.grabId(), airport.getName());
                chart.addVertex(vertex);
            }
            List<Flight> flights = routeMap.pullFlights();
            for (int k = 0; k < flights.size(); ++k) {
                BasicData data;
                Flight flight = flights.get(k);
                int sourceId = flight.obtainOrigin().grabId();
                int sinkId = flight.grabDestination().grabId();
                switch (weightType) {
                    case COST: {
                        data = new BasicData(flight.takeFuelCosts());
                        break;
                    }
                    case DISTANCE: {
                        data = new BasicData(flight.grabDistance());
                        break;
                    }
                    case TIME: {
                        data = new BasicData(flight.getTravelTime());
                        break;
                    }
                    case CREW_MEMBERS: {
                        data = new BasicData(flight.pullNumCrewMembers());
                        break;
                    }
                    case WEIGHT: {
                        data = new BasicData(flight.getWeightLimit());
                        break;
                    }
                    case PASSENGERS: {
                        data = new BasicData(flight.getPassengerLimit());
                        break;
                    }
                    default: {
                        data = new BasicData();
                    }
                }
                chart.addEdge(sourceId, sinkId, data);
            }
        }
        catch (ChartFailure e) {
            throw new AirFailure(e);
        }
        return chart;
    }

    public List<Crew> takeCrewAssignments() throws AirFailure {
        CrewOverseer crewOverseer = new CrewOverseer(this.routeMap);
        return crewOverseer.grabCrewAssignments();
    }

    public static class OptimalPathData {
        private RouteMap routeMap;
        private List<Airport> airports;
        private double distance;
        private FlightWeightType weightType;

        public OptimalPathData(RouteMap routeMap, List<Airport> airports, double distance, FlightWeightType weightType) {
            this.routeMap = routeMap;
            this.airports = airports;
            this.distance = distance;
            this.weightType = weightType;
        }

        public boolean hasPath() {
            return this.airports.size() > 0;
        }

        public List<Airport> pullAirports() {
            return this.airports;
        }

        public RouteMap obtainRouteMap() {
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

