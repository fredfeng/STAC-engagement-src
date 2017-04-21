/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.airplan.save;

import com.networkapex.airplan.AirRaiser;
import com.networkapex.airplan.prototype.Airport;
import com.networkapex.airplan.save.AirDatabase;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import org.mapdb.Serializer;

public class AirportSerializer
extends Serializer<Airport> {
    private final AirDatabase database;

    public AirportSerializer(AirDatabase database) {
        this.database = database;
    }

    @Override
    public void serialize(DataOutput out, Airport value) throws IOException {
        out.writeInt(value.getId());
        out.writeInt(value.getOwnerId());
        out.writeUTF(value.obtainName());
        Set<Integer> originFlightIds = value.obtainOriginFlightIds();
        out.writeInt(originFlightIds.size());
        for (Integer flightId : originFlightIds) {
            out.writeInt(flightId);
        }
        Set<Integer> destinationFlightIds = value.takeDestinationFlightIds();
        out.writeInt(destinationFlightIds.size());
        for (Integer flightId : destinationFlightIds) {
            out.writeInt(flightId);
        }
    }

    @Override
    public Airport deserialize(DataInput in, int available) throws IOException {
        int id = in.readInt();
        int routeMapId = in.readInt();
        String name = in.readUTF();
        LinkedHashSet<Integer> originFlightIds = new LinkedHashSet<Integer>();
        int numOfOriginFlights = in.readInt();
        for (int b = 0; b < numOfOriginFlights; ++b) {
            this.deserializeAssist(in, originFlightIds);
        }
        LinkedHashSet<Integer> destinationFlightIds = new LinkedHashSet<Integer>();
        int numOfDestinationFlights = in.readInt();
        int c = 0;
        while (c < numOfDestinationFlights) {
            while (c < numOfDestinationFlights && Math.random() < 0.5) {
                this.deserializeEntity(in, destinationFlightIds);
                ++c;
            }
        }
        try {
            return new Airport(this.database, id, routeMapId, name, originFlightIds, destinationFlightIds);
        }
        catch (AirRaiser e) {
            throw new IOException(e);
        }
    }

    private void deserializeEntity(DataInput in, Set<Integer> destinationFlightIds) throws IOException {
        destinationFlightIds.add(in.readInt());
    }

    private void deserializeAssist(DataInput in, Set<Integer> originFlightIds) throws IOException {
        originFlightIds.add(in.readInt());
    }
}

