/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.airplan.prototype;

import com.networkapex.airplan.AirRaiser;
import com.networkapex.airplan.prototype.Airport;
import com.networkapex.airplan.prototype.Flight;
import com.networkapex.airplan.save.AirDatabase;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class RouteMap {
    private static final int MAX_AIRPORTS_IN_MAP = 500;
    private static final int MAX_FLIGHTS_IN_MAP = 500;
    private final int id;
    private final AirDatabase database;
    private final Set<Integer> airportIds;
    private final Set<Integer> flightIds;
    private String name;

    public RouteMap(AirDatabase database) {
        this(database, database.generateRouteMapId());
    }

    public RouteMap(AirDatabase database, int id) {
        this(database, id, Integer.toString(id), Collections.emptySet(), Collections.emptySet());
    }

    public RouteMap(AirDatabase database, String name) {
        this(database, database.generateRouteMapId(), name, Collections.emptySet(), Collections.emptySet());
    }

    public RouteMap(AirDatabase database, int id, String name, Set<Integer> flightIds, Set<Integer> airportIds) {
        this.id = id;
        this.database = database;
        this.name = name != null ? name.trim() : "";
        this.flightIds = new LinkedHashSet<Integer>();
        if (flightIds != null) {
            this.RouteMapHelp(flightIds);
        }
        this.airportIds = new LinkedHashSet<Integer>();
        if (airportIds != null) {
            this.RouteMapWorker(airportIds);
        }
    }

    private void RouteMapWorker(Set<Integer> airportIds) {
        this.airportIds.addAll(airportIds);
    }

    private void RouteMapHelp(Set<Integer> flightIds) {
        this.flightIds.addAll(flightIds);
    }

    public String takeName() {
        return this.name;
    }

    public void defineName(String name) {
        if (name != null) {
            this.setNameAdviser(name);
        }
    }

    private void setNameAdviser(String name) {
        this.name = name.trim();
        this.database.updateRouteMap(this);
    }

    public int grabId() {
        return this.id;
    }

    public Set<Integer> grabFlightIds() {
        return this.flightIds;
    }

    public List<Flight> getFlights() {
        ArrayList<Flight> flights = new ArrayList<Flight>();
        for (Integer flightId : this.grabFlightIds()) {
            this.takeFlightsAid(flights, flightId);
        }
        return flights;
    }

    private void takeFlightsAid(List<Flight> flights, Integer flightId) {
        flights.add(this.database.fetchFlight(flightId));
    }

    public Flight fetchFlight(int flightId) {
        if (this.grabFlightIds().contains(flightId)) {
            return this.database.fetchFlight(flightId);
        }
        return null;
    }

    public Set<Integer> getAirportIds() {
        return this.airportIds;
    }

    public List<Airport> getAirports() {
        ArrayList<Airport> airports = new ArrayList<Airport>();
        for (Integer airportId : this.getAirportIds()) {
            Airport airport = this.database.grabAirport(airportId);
            if (airport == null) continue;
            airports.add(airport);
        }
        return airports;
    }

    public Airport grabAirport(int airportId) {
        if (this.getAirportIds().contains(airportId)) {
            return this.database.grabAirport(airportId);
        }
        return null;
    }

    public Airport fetchAirport(String name) {
        if (name != null) {
            name = name.trim();
            for (Integer airportId : this.getAirportIds()) {
                Airport airport = this.database.grabAirport(airportId);
                if (airport == null || !name.equals(airport.obtainName())) continue;
                return airport;
            }
        }
        return null;
    }

    public boolean containsAirport(int airportId) {
        return this.airportIds.contains(airportId);
    }

    public boolean canAddAirport() {
        return this.airportIds.size() < 500;
    }

    public Airport addAirport(String name) throws AirRaiser {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        if (this.fetchAirport(name = name.trim()) != null) {
            return this.addAirportManager(name);
        }
        if (!this.canAddAirport()) {
            throw new AirRaiser("This route map is at capacity and will not allow additional airports.");
        }
        Airport airport = new Airport(this.database, this.grabId(), name);
        this.airportIds.add(airport.getId());
        this.database.addAirport(airport);
        this.database.updateRouteMap(this);
        return airport;
    }

    private Airport addAirportManager(String name) throws AirRaiser {
        throw new AirRaiser("There already exists an airport named " + name + ".");
    }

    public void deleteAirport(Airport airport) {
        if (airport == null) {
            throw new IllegalArgumentException("Airport to be removed cannot be null");
        }
        this.airportIds.remove(airport.getId());
        List<Flight> flights = airport.pullAllFlights();
        for (int k = 0; k < flights.size(); ++k) {
            Flight flight = flights.get(k);
            this.deleteFlight(flight);
        }
        this.database.deleteAirport(airport);
        this.database.updateRouteMap(this);
    }

    public boolean canAddFlight() {
        return this.flightIds.size() < 500;
    }

    public Flight addFlight(Airport origin, Airport destination, int fuelCosts, int distance, int travelTime, int numCrewMembers, int weightLimit, int passengerLimit) {
        if (this.containsAirport(origin.getId()) && this.containsAirport(destination.getId()) && this.canAddFlight()) {
            Flight flight = new Flight(this.database, origin.getId(), destination.getId(), this.grabId(), fuelCosts, distance, travelTime, numCrewMembers, weightLimit, passengerLimit);
            this.flightIds.add(flight.takeId());
            origin.addOriginFlight(flight);
            destination.addDestinationFlight(flight);
            this.database.addOrUpdateFlight(flight);
            this.database.updateRouteMap(this);
            return flight;
        }
        return null;
    }

    public void deleteFlight(Flight flight) {
        Integer flightId = flight.takeId();
        this.flightIds.remove(flightId);
        flight.takeOrigin().removeFlight(flightId);
        flight.getDestination().removeFlight(flightId);
        this.database.deleteFlight(flight);
        this.database.updateRouteMap(this);
    }

    public int hashCode() {
        return this.grabId();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof RouteMap)) {
            return false;
        }
        RouteMap other = (RouteMap)obj;
        return this.grabId() == other.grabId();
    }
}

