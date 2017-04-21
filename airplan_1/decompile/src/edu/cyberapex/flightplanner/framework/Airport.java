/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.flightplanner.framework;

import edu.cyberapex.flightplanner.AirFailure;
import edu.cyberapex.flightplanner.framework.Flight;
import edu.cyberapex.flightplanner.store.AirDatabase;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Airport {
    private static final int NAME_CHAR_LIMIT = 3;
    private final AirDatabase database;
    private final int ownerId;
    private final int airportId;
    private String name;
    private Set<Integer> originFlightIds;
    private Set<Integer> destinationFlightIds;

    public Airport(AirDatabase database, int ownerId, String name) throws AirFailure {
        this(database, database.generateAirportId(), ownerId, name, Collections.emptySet(), Collections.emptySet());
    }

    public Airport(AirDatabase database, int airportId, int ownerId, String name, Set<Integer> originFlightIds, Set<Integer> destinationFlightIds) throws AirFailure {
        if (name == null) {
            throw new AirFailure("Airport names cannot be null");
        }
        if ((name = name.trim()).length() > 3) {
            throw new AirFailure("Airport names cannot be longer than three characters.");
        }
        this.database = database;
        this.airportId = airportId;
        this.ownerId = ownerId;
        this.name = name;
        this.originFlightIds = new LinkedHashSet<Integer>();
        if (originFlightIds != null) {
            this.AirportHome(originFlightIds);
        }
        this.destinationFlightIds = new LinkedHashSet<Integer>();
        if (destinationFlightIds != null) {
            this.destinationFlightIds.addAll(destinationFlightIds);
        }
    }

    private void AirportHome(Set<Integer> originFlightIds) {
        this.originFlightIds.addAll(originFlightIds);
    }

    public int grabOwnerId() {
        return this.ownerId;
    }

    public int grabId() {
        return this.airportId;
    }

    public boolean addOriginFlight(Flight flight) {
        if (flight.obtainOrigin().equals(this)) {
            return this.addOriginFlightHome(flight);
        }
        return false;
    }

    private boolean addOriginFlightHome(Flight flight) {
        this.originFlightIds.add(flight.grabId());
        this.database.updateAirport(this);
        return true;
    }

    public boolean addDestinationFlight(Flight flight) {
        if (flight.grabDestination().equals(this)) {
            return this.addDestinationFlightHelper(flight);
        }
        return false;
    }

    private boolean addDestinationFlightHelper(Flight flight) {
        this.destinationFlightIds.add(flight.grabId());
        this.database.updateAirport(this);
        return true;
    }

    public List<Flight> getOriginFlights() {
        return this.database.getOriginFlights(this);
    }

    public List<Flight> fetchAllFlights() {
        return this.database.obtainAllFlights(this);
    }

    public Set<Integer> pullOriginFlightIds() {
        return this.originFlightIds;
    }

    public Set<Integer> pullDestinationFlightIds() {
        return this.destinationFlightIds;
    }

    public String getName() {
        return this.name;
    }

    public void defineName(String name) {
        if (name != null) {
            this.name = name.trim();
            this.database.updateAirport(this);
        }
    }

    public void removeFlight(int flightId) {
        if (this.originFlightIds.contains(flightId)) {
            this.originFlightIds.remove(flightId);
        } else if (this.destinationFlightIds.contains(flightId)) {
            this.destinationFlightIds.remove(flightId);
        }
        this.database.updateAirport(this);
    }

    public int hashCode() {
        return this.grabOwnerId() * 37 + this.grabId();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Airport)) {
            return false;
        }
        Airport other = (Airport)obj;
        return this.grabId() == other.grabId() && this.grabOwnerId() == other.grabOwnerId();
    }
}

