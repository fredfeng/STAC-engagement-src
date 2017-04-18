/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.flightplanner.framework;

import edu.cyberapex.flightplanner.AirFailure;
import edu.cyberapex.flightplanner.framework.Airport;
import edu.cyberapex.flightplanner.framework.Flight;
import edu.cyberapex.flightplanner.store.AirDatabase;
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
            this.flightIds.addAll(flightIds);
        }
        this.airportIds = new LinkedHashSet<Integer>();
        if (airportIds != null) {
            this.airportIds.addAll(airportIds);
        }
    }

    public String takeName() {
        return this.name;
    }

    public void fixName(String name) {
        if (name != null) {
            this.name = name.trim();
            this.database.updateRouteMap(this);
        }
    }

    public int takeId() {
        return this.id;
    }

    public Set<Integer> getFlightIds() {
        return this.flightIds;
    }

    public List<Flight> pullFlights() {
        ArrayList<Flight> flights = new ArrayList<Flight>();
        for (Integer flightId : this.getFlightIds()) {
            flights.add(this.database.takeFlight(flightId));
        }
        return flights;
    }

    public Flight fetchFlight(int flightId) {
        if (this.getFlightIds().contains(flightId)) {
            return this.database.takeFlight(flightId);
        }
        return null;
    }

    public Set<Integer> fetchAirportIds() {
        return this.airportIds;
    }

    public List<Airport> obtainAirports() {
        ArrayList<Airport> airports = new ArrayList<Airport>();
        for (Integer airportId : this.fetchAirportIds()) {
            this.fetchAirportsAssist(airports, airportId);
        }
        return airports;
    }

    private void fetchAirportsAssist(List<Airport> airports, Integer airportId) {
        Airport airport = this.database.takeAirport(airportId);
        if (airport != null) {
            this.takeAirportsAssistHome(airports, airport);
        }
    }

    private void takeAirportsAssistHome(List<Airport> airports, Airport airport) {
        airports.add(airport);
    }

    public Airport fetchAirport(int airportId) {
        if (this.fetchAirportIds().contains(airportId)) {
            return this.database.takeAirport(airportId);
        }
        return null;
    }

    public Airport obtainAirport(String name) {
        if (name != null) {
            name = name.trim();
            for (Integer airportId : this.fetchAirportIds()) {
                Airport airport = this.fetchAirportTarget(name, airportId);
                if (airport == null) continue;
                return airport;
            }
        }
        return null;
    }

    private Airport fetchAirportTarget(String name, Integer airportId) {
        Airport airport = this.database.takeAirport(airportId);
        if (airport != null && name.equals(airport.getName())) {
            return airport;
        }
        return null;
    }

    public boolean containsAirport(int airportId) {
        return this.airportIds.contains(airportId);
    }

    public boolean canAddAirport() {
        return this.airportIds.size() < 500;
    }

    public Airport addAirport(String name) throws AirFailure {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        if (this.obtainAirport(name = name.trim()) != null) {
            throw new AirFailure("There already exists an airport named " + name + ".");
        }
        if (!this.canAddAirport()) {
            throw new AirFailure("This route map is at capacity and will not allow additional airports.");
        }
        Airport airport = new Airport(this.database, this.takeId(), name);
        this.airportIds.add(airport.grabId());
        this.database.addAirport(airport);
        this.database.updateRouteMap(this);
        return airport;
    }

    public void deleteAirport(Airport airport) {
        if (airport == null) {
            throw new IllegalArgumentException("Airport to be removed cannot be null");
        }
        this.airportIds.remove(airport.grabId());
        List<Flight> flights = airport.fetchAllFlights();
        for (int k = 0; k < flights.size(); ++k) {
            this.deleteAirportHome(flights, k);
        }
        this.database.deleteAirport(airport);
        this.database.updateRouteMap(this);
    }

    private void deleteAirportHome(List<Flight> flights, int k) {
        Flight flight = flights.get(k);
        this.deleteFlight(flight);
    }

    public boolean canAddFlight() {
        return this.flightIds.size() < 500;
    }

    public Flight addFlight(Airport origin, Airport destination, int fuelCosts, int distance, int travelTime, int numCrewMembers, int weightLimit, int passengerLimit) {
        if (this.containsAirport(origin.grabId()) && this.containsAirport(destination.grabId()) && this.canAddFlight()) {
            Flight flight = new Flight(this.database, origin.grabId(), destination.grabId(), this.takeId(), fuelCosts, distance, travelTime, numCrewMembers, weightLimit, passengerLimit);
            this.flightIds.add(flight.grabId());
            origin.addOriginFlight(flight);
            destination.addDestinationFlight(flight);
            this.database.addOrUpdateFlight(flight);
            this.database.updateRouteMap(this);
            return flight;
        }
        return null;
    }

    public void deleteFlight(Flight flight) {
        Integer flightId = flight.grabId();
        this.flightIds.remove(flightId);
        flight.obtainOrigin().removeFlight(flightId);
        flight.grabDestination().removeFlight(flightId);
        this.database.deleteFlight(flight);
        this.database.updateRouteMap(this);
    }

    public int hashCode() {
        return this.takeId();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof RouteMap)) {
            return false;
        }
        RouteMap other = (RouteMap)obj;
        return this.takeId() == other.takeId();
    }
}

