/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  org.mapdb.DB
 *  org.mapdb.DBMaker
 *  org.mapdb.DBMaker$Maker
 *  org.mapdb.HTreeMap
 *  org.mapdb.Serializer
 */
package edu.cyberapex.flightplanner.store;

import edu.cyberapex.flightplanner.framework.Airline;
import edu.cyberapex.flightplanner.framework.Airport;
import edu.cyberapex.flightplanner.framework.Flight;
import edu.cyberapex.flightplanner.framework.RouteMap;
import edu.cyberapex.flightplanner.store.AirlineSerializer;
import edu.cyberapex.flightplanner.store.AirlineSerializerBuilder;
import edu.cyberapex.flightplanner.store.AirportSerializer;
import edu.cyberapex.flightplanner.store.FlightSerializer;
import edu.cyberapex.flightplanner.store.RouteMapSerializer;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

public class AirDatabase {
    private static final String AIRLINES_MAP = "AIRLINES";
    private static final String ROUTE_MAPS_MAP = "ROUTE_MAP";
    private static final String AIRPORTS_MAP = "AIRPORTS";
    private static final String FLIGHTS_MAP = "FLIGHTS";
    private final DB db;
    private final Map<String, Airline> airlines;
    private final Random random;
    private final Map<Integer, RouteMap> routeMaps;
    private final Map<Integer, Airport> airports;
    private final Map<Integer, Flight> flights;

    public AirDatabase(File databaseFile) {
        this(databaseFile, new Random());
    }

    public AirDatabase(File databaseFile, Random random) {
        this.random = random;
        this.db = DBMaker.fileDB((File)databaseFile).fileMmapEnableIfSupported().transactionDisable().asyncWriteEnable().make();
        this.airlines = this.db.hashMap("AIRLINES", Serializer.STRING, (Serializer)new AirlineSerializerBuilder().setDb(this).generateAirlineSerializer());
        this.routeMaps = this.db.hashMap("ROUTE_MAP", Serializer.INTEGER, (Serializer)new RouteMapSerializer(this));
        this.airports = this.db.hashMap("AIRPORTS", Serializer.INTEGER, (Serializer)new AirportSerializer(this));
        this.flights = this.db.hashMap("FLIGHTS", Serializer.INTEGER, (Serializer)new FlightSerializer(this));
    }

    public void commit() {
        this.db.commit();
    }

    public void close() {
        this.db.commit();
        this.db.close();
    }

    public int generateRouteMapId() {
        return this.generateNewId(this.routeMaps.keySet());
    }

    public int generateAirportId() {
        return this.generateNewId(this.airports.keySet());
    }

    public int generateFlightId() {
        return this.generateNewId(this.flights.keySet());
    }

    private int generateNewId(Set<Integer> currentIds) {
        int newId = Math.abs(this.random.nextInt());
        int attemptsLimit = 10;
        int attempts = 0;
        while (currentIds.contains(newId) && attempts++ < attemptsLimit) {
            newId = Math.abs(this.random.nextInt());
        }
        if (currentIds.contains(newId)) {
            return new AirDatabaseExecutor().invoke();
        }
        return newId;
    }

    public List<Airline> obtainAllAirlines() {
        return new ArrayList<Airline>(this.airlines.values());
    }

    public List<RouteMap> getRouteMaps(Airline airline) {
        ArrayList<RouteMap> airlineMaps = new ArrayList<RouteMap>();
        Iterator<Integer> i$ = airline.grabRouteMapIds().iterator();
        while (i$.hasNext()) {
            int id = i$.next();
            airlineMaps.add(this.getRouteMap(id));
        }
        return airlineMaps;
    }

    public List<Flight> getOriginFlights(Airport airport) {
        ArrayList<Flight> flights = new ArrayList<Flight>();
        for (Integer flightId : airport.pullOriginFlightIds()) {
            this.grabOriginFlightsEngine(flights, flightId);
        }
        return flights;
    }

    private void grabOriginFlightsEngine(List<Flight> flights, Integer flightId) {
        flights.add(this.takeFlight(flightId));
    }

    public List<Flight> obtainAllFlights(Airport airport) {
        LinkedHashSet<Integer> allIds = new LinkedHashSet<Integer>(airport.pullOriginFlightIds());
        allIds.addAll(airport.pullDestinationFlightIds());
        ArrayList<Flight> flights = new ArrayList<Flight>(allIds.size());
        for (Integer flightId : allIds) {
            flights.add(this.takeFlight(flightId));
        }
        return flights;
    }

    public Airline obtainAirline(String id) {
        return this.airlines.get(id);
    }

    public RouteMap getRouteMap(int id) {
        return this.routeMaps.get(id);
    }

    public Flight takeFlight(int flightId) {
        return this.flights.get(flightId);
    }

    public Airport takeAirport(int airportId) {
        return this.airports.get(airportId);
    }

    public void addOrUpdateAirline(Airline airline) {
        this.airlines.put(airline.obtainID(), airline);
    }

    public void addRouteMap(RouteMap routeMap) {
        int routeMapId = routeMap.takeId();
        if (this.routeMaps.containsKey(routeMapId)) {
            throw new IllegalArgumentException("A route map with this id already exists. " + routeMapId);
        }
        this.routeMaps.put(routeMapId, routeMap);
    }

    public void updateRouteMap(RouteMap routeMap) {
        this.routeMaps.put(routeMap.takeId(), routeMap);
    }

    public void addAirport(Airport airport) {
        int airportId = airport.grabId();
        if (this.airports.containsKey(airportId)) {
            this.addAirportService(airportId);
        }
        this.airports.put(airportId, airport);
    }

    private void addAirportService(int airportId) {
        throw new IllegalArgumentException("An airport with this id already exists. " + airportId);
    }

    public void updateAirport(Airport airport) {
        this.airports.put(airport.grabId(), airport);
    }

    public void addOrUpdateFlight(Flight flight) {
        this.flights.put(flight.grabId(), flight);
    }

    public void deleteAirport(Airport airport) {
        this.airports.remove(airport.grabId());
    }

    public void deleteFlight(Flight flight) {
        this.flights.remove(flight.grabId());
    }

    public void deleteRouteMap(RouteMap routeMap) {
        this.routeMaps.remove(routeMap.takeId());
    }

    private class AirDatabaseExecutor {
        private AirDatabaseExecutor() {
        }

        public int invoke() {
            throw new RuntimeException("Unable to create a new id.");
        }
    }

}

