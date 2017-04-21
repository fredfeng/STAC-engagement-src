/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.airplan.prototype;

import com.networkapex.airplan.AirRaiser;
import com.networkapex.airplan.prototype.Flight;
import com.networkapex.airplan.save.AirDatabase;
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

    public Airport(AirDatabase database, int ownerId, String name) throws AirRaiser {
        this(database, database.generateAirportId(), ownerId, name, Collections.emptySet(), Collections.emptySet());
    }

    public Airport(AirDatabase database, int airportId, int ownerId, String name, Set<Integer> originFlightIds, Set<Integer> destinationFlightIds) throws AirRaiser {
        if (name == null) {
            this.AirportSupervisor();
        }
        if ((name = name.trim()).length() > 3) {
            this.AirportFunction();
        }
        this.database = database;
        this.airportId = airportId;
        this.ownerId = ownerId;
        this.name = name;
        this.originFlightIds = new LinkedHashSet<Integer>();
        if (originFlightIds != null) {
            this.AirportCoordinator(originFlightIds);
        }
        this.destinationFlightIds = new LinkedHashSet<Integer>();
        if (destinationFlightIds != null) {
            this.destinationFlightIds.addAll(destinationFlightIds);
        }
    }

    private void AirportCoordinator(Set<Integer> originFlightIds) {
        this.originFlightIds.addAll(originFlightIds);
    }

    private void AirportFunction() throws AirRaiser {
        throw new AirRaiser("Airport names cannot be longer than three characters.");
    }

    private void AirportSupervisor() throws AirRaiser {
        throw new AirRaiser("Airport names cannot be null");
    }

    public int getOwnerId() {
        return this.ownerId;
    }

    public int getId() {
        return this.airportId;
    }

    public boolean addOriginFlight(Flight flight) {
        if (flight.takeOrigin().equals(this)) {
            this.originFlightIds.add(flight.takeId());
            this.database.updateAirport(this);
            return true;
        }
        return false;
    }

    public boolean addDestinationFlight(Flight flight) {
        if (flight.getDestination().equals(this)) {
            return this.addDestinationFlightManager(flight);
        }
        return false;
    }

    private boolean addDestinationFlightManager(Flight flight) {
        this.destinationFlightIds.add(flight.takeId());
        this.database.updateAirport(this);
        return true;
    }

    public List<Flight> grabOriginFlights() {
        return this.database.grabOriginFlights(this);
    }

    public List<Flight> pullAllFlights() {
        return this.database.grabAllFlights(this);
    }

    public Set<Integer> obtainOriginFlightIds() {
        return this.originFlightIds;
    }

    public Set<Integer> takeDestinationFlightIds() {
        return this.destinationFlightIds;
    }

    public String obtainName() {
        return this.name;
    }

    public void defineName(String name) {
        if (name != null) {
            this.assignNameAssist(name);
        }
    }

    private void assignNameAssist(String name) {
        this.name = name.trim();
        this.database.updateAirport(this);
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
        return this.getOwnerId() * 37 + this.getId();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Airport)) {
            return false;
        }
        Airport other = (Airport)obj;
        return this.getId() == other.getId() && this.getOwnerId() == other.getOwnerId();
    }
}

