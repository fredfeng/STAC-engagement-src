/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.flightplanner.framework;

import edu.cyberapex.flightplanner.framework.Airport;
import edu.cyberapex.flightplanner.store.AirDatabase;

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

    public int fetchOwnerId() {
        return this.ownerId;
    }

    public int grabId() {
        return this.flightId;
    }

    public Airport obtainOrigin() {
        return this.database.takeAirport(this.originId);
    }

    public Airport grabDestination() {
        return this.database.takeAirport(this.destinationId);
    }

    public int takeFuelCosts() {
        return this.fuelCosts;
    }

    public void defineFuelCosts(int fuelCosts) {
        this.fuelCosts = fuelCosts;
        this.database.addOrUpdateFlight(this);
    }

    public int grabDistance() {
        return this.distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
        this.database.addOrUpdateFlight(this);
    }

    public int getTravelTime() {
        return this.travelTime;
    }

    public void setTravelTime(int travelTime) {
        this.travelTime = travelTime;
        this.database.addOrUpdateFlight(this);
    }

    public int pullNumCrewMembers() {
        return this.numCrewMembers;
    }

    public void fixNumCrewMembers(int numCrewMembers) {
        this.numCrewMembers = numCrewMembers;
        this.database.addOrUpdateFlight(this);
    }

    public int getWeightLimit() {
        return this.weightLimit;
    }

    public void fixWeightLimit(int weightLimit) {
        this.weightLimit = weightLimit;
        this.database.addOrUpdateFlight(this);
    }

    public int getPassengerLimit() {
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
        return this.fetchOwnerId() * 37 + this.grabId();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Flight)) {
            return false;
        }
        Flight other = (Flight)obj;
        return this.grabId() == other.grabId() && this.fetchOwnerId() == other.fetchOwnerId();
    }
}

