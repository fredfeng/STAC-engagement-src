/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.airplan.save;

import com.networkapex.airplan.prototype.Airline;
import com.networkapex.airplan.prototype.Airport;
import com.networkapex.airplan.prototype.Flight;
import com.networkapex.airplan.prototype.RouteMap;
import com.networkapex.airplan.save.AirlineSerializer;
import com.networkapex.airplan.save.AirlineSerializerBuilder;
import com.networkapex.airplan.save.AirportSerializer;
import com.networkapex.airplan.save.AirportSerializerBuilder;
import com.networkapex.airplan.save.FlightSerializer;
import com.networkapex.airplan.save.RouteMapSerializer;
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
        this.db = DBMaker.fileDB(databaseFile).fileMmapEnableIfSupported().transactionDisable().asyncWriteEnable().make();
        this.airlines = this.db.hashMap("AIRLINES", Serializer.STRING, new AirlineSerializerBuilder().defineDb(this).generateAirlineSerializer());
        this.routeMaps = this.db.hashMap("ROUTE_MAP", Serializer.INTEGER, new RouteMapSerializer(this));
        this.airports = this.db.hashMap("AIRPORTS", Serializer.INTEGER, new AirportSerializerBuilder().fixDatabase(this).generateAirportSerializer());
        this.flights = this.db.hashMap("FLIGHTS", Serializer.INTEGER, new FlightSerializer(this));
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
            return this.generateNewIdHelper();
        }
        return newId;
    }

    private int generateNewIdHelper() {
        throw new RuntimeException("Unable to create a new id.");
    }

    public List<Airline> getAllAirlines() {
        return new ArrayList<Airline>(this.airlines.values());
    }

    public List<RouteMap> pullRouteMaps(Airline airline) {
        ArrayList<RouteMap> airlineMaps = new ArrayList<RouteMap>();
        Iterator<Integer> i$ = airline.takeRouteMapIds().iterator();
        while (i$.hasNext()) {
            int id = i$.next();
            this.obtainRouteMapsGateKeeper(airlineMaps, id);
        }
        return airlineMaps;
    }

    private void obtainRouteMapsGateKeeper(List<RouteMap> airlineMaps, int id) {
        airlineMaps.add(this.fetchRouteMap(id));
    }

    public List<Flight> grabOriginFlights(Airport airport) {
        ArrayList<Flight> flights = new ArrayList<Flight>();
        for (Integer flightId : airport.obtainOriginFlightIds()) {
            this.obtainOriginFlightsService(flights, flightId);
        }
        return flights;
    }

    private void obtainOriginFlightsService(List<Flight> flights, Integer flightId) {
        flights.add(this.fetchFlight(flightId));
    }

    public List<Flight> grabAllFlights(Airport airport) {
        LinkedHashSet<Integer> allIds = new LinkedHashSet<Integer>(airport.obtainOriginFlightIds());
        allIds.addAll(airport.takeDestinationFlightIds());
        ArrayList<Flight> flights = new ArrayList<Flight>(allIds.size());
        for (Integer flightId : allIds) {
            this.fetchAllFlightsSupervisor(flights, flightId);
        }
        return flights;
    }

    private void fetchAllFlightsSupervisor(List<Flight> flights, Integer flightId) {
        flights.add(this.fetchFlight(flightId));
    }

    public Airline obtainAirline(String id) {
        return this.airlines.get(id);
    }

    public RouteMap fetchRouteMap(int id) {
        return this.routeMaps.get(id);
    }

    public Flight fetchFlight(int flightId) {
        return this.flights.get(flightId);
    }

    public Airport grabAirport(int airportId) {
        return this.airports.get(airportId);
    }

    public void addOrUpdateAirline(Airline airline) {
        this.airlines.put(airline.pullID(), airline);
    }

    public void addRouteMap(RouteMap routeMap) {
        int routeMapId = routeMap.grabId();
        if (this.routeMaps.containsKey(routeMapId)) {
            this.addRouteMapUtility(routeMapId);
        }
        this.routeMaps.put(routeMapId, routeMap);
    }

    private void addRouteMapUtility(int routeMapId) {
        throw new IllegalArgumentException("A route map with this id already exists. " + routeMapId);
    }

    public void updateRouteMap(RouteMap routeMap) {
        this.routeMaps.put(routeMap.grabId(), routeMap);
    }

    public void addAirport(Airport airport) {
        int airportId = airport.getId();
        if (this.airports.containsKey(airportId)) {
            throw new IllegalArgumentException("An airport with this id already exists. " + airportId);
        }
        this.airports.put(airportId, airport);
    }

    public void updateAirport(Airport airport) {
        this.airports.put(airport.getId(), airport);
    }

    public void addOrUpdateFlight(Flight flight) {
        this.flights.put(flight.takeId(), flight);
    }

    public void deleteAirport(Airport airport) {
        this.airports.remove(airport.getId());
    }

    public void deleteFlight(Flight flight) {
        this.flights.remove(flight.takeId());
    }

    public void deleteRouteMap(RouteMap routeMap) {
        this.routeMaps.remove(routeMap.grabId());
    }
}

