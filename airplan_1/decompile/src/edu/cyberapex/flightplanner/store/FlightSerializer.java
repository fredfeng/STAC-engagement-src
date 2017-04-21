/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  org.mapdb.Serializer
 */
package edu.cyberapex.flightplanner.store;

import edu.cyberapex.flightplanner.framework.Airport;
import edu.cyberapex.flightplanner.framework.Flight;
import edu.cyberapex.flightplanner.store.AirDatabase;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.mapdb.Serializer;

public class FlightSerializer
extends Serializer<Flight> {
    private final AirDatabase database;

    public FlightSerializer(AirDatabase database) {
        this.database = database;
    }

    public void serialize(DataOutput out, Flight value) throws IOException {
        out.writeInt(value.fetchOwnerId());
        out.writeInt(value.obtainOrigin().grabId());
        out.writeInt(value.grabDestination().grabId());
        out.writeInt(value.grabId());
        out.writeInt(value.takeFuelCosts());
        out.writeInt(value.grabDistance());
        out.writeInt(value.getTravelTime());
        out.writeInt(value.pullNumCrewMembers());
        out.writeInt(value.getWeightLimit());
        out.writeInt(value.getPassengerLimit());
    }

    public Flight deserialize(DataInput in, int available) throws IOException {
        int ownerId = in.readInt();
        int originId = in.readInt();
        int destinationId = in.readInt();
        int id = in.readInt();
        int fuelCosts = in.readInt();
        int distance = in.readInt();
        int travelTime = in.readInt();
        int crewMembers = in.readInt();
        int weightLimit = in.readInt();
        int passengerLimit = in.readInt();
        return new Flight(this.database, id, originId, destinationId, ownerId, fuelCosts, distance, travelTime, crewMembers, weightLimit, passengerLimit);
    }
}

