/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.airplan.prototype;

import com.networkapex.airplan.prototype.Airport;
import com.networkapex.airplan.save.AirDatabase;

public class Flight {
    private final AirDatabase database;
    private final int ownerId;
    private final int flightId;
    private final int originId;
    private final int destinationId;
    private int fuelCosts;
    private int distance;
    private int travelTime;
    private int numCrewMembers;
    private int weightLimit;
    private int passengerLimit;

    public Flight(AirDatabase database, int originId, int destinationId, int ownerId, int fuelCosts, int distance, int travelTime, int numCrewMembers, int weightLimit, int passengerLimit) {
        this(database, database.generateFlightId(), originId, destinationId, ownerId, fuelCosts, distance, travelTime, numCrewMembers, weightLimit, passengerLimit);
    }

    public Flight(AirDatabase database, int flightId, int originId, int destinationId, int ownerId, int fuelCosts, int distance, int travelTime, int numCrewMembers, int weightLimit, int passengerLimit) {
        this.database = database;
        this.flightId = flightId;
        this.originId = originId;
        this.destinationId = destinationId;
        this.ownerId = ownerId;
        this.fuelCosts = fuelCosts;
        this.distance = distance;
        this.travelTime = travelTime;
        this.numCrewMembers = numCrewMembers;
        this.weightLimit = weightLimit;
        this.passengerLimit = passengerLimit;
    }

    public int grabOwnerId() {
        return this.ownerId;
    }

    public int takeId() {
        return this.flightId;
    }

    public Airport takeOrigin() {
        return this.database.grabAirport(this.originId);
    }

    public Airport getDestination() {
        return this.database.grabAirport(this.destinationId);
    }

    public int grabFuelCosts() {
        return this.fuelCosts;
    }

    public void setFuelCosts(int fuelCosts) {
        this.fuelCosts = fuelCosts;
        this.database.addOrUpdateFlight(this);
    }

    public int pullDistance() {
        return this.distance;
    }

    public void defineDistance(int distance) {
        this.distance = distance;
        this.database.addOrUpdateFlight(this);
    }

    public int getTravelTime() {
        return this.travelTime;
    }

    public void assignTravelTime(int travelTime) {
        this.travelTime = travelTime;
        this.database.addOrUpdateFlight(this);
    }

    public int grabNumCrewMembers() {
        return this.numCrewMembers;
    }

    public void setNumCrewMembers(int numCrewMembers) {
        this.numCrewMembers = numCrewMembers;
        this.database.addOrUpdateFlight(this);
    }

    public int takeWeightLimit() {
        return this.weightLimit;
    }

    public void setWeightLimit(int weightLimit) {
        this.weightLimit = weightLimit;
        this.database.addOrUpdateFlight(this);
    }

    public int pullPassengerLimit() {
        return this.passengerLimit;
    }

    public void definePassengerLimit(int passengerLimit) {
        this.passengerLimit = passengerLimit;
        this.database.addOrUpdateFlight(this);
    }

    public boolean canUseSameCrew(Flight flight) {
        return this.originId == flight.destinationId && this.numCrewMembers <= flight.numCrewMembers;
    }

    public int hashCode() {
        return this.grabOwnerId() * 37 + this.takeId();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Flight)) {
            return false;
        }
        Flight other = (Flight)obj;
        return this.takeId() == other.takeId() && this.grabOwnerId() == other.grabOwnerId();
    }
}

