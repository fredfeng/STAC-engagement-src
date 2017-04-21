/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  org.mapdb.Serializer
 */
package edu.cyberapex.flightplanner.store;

import edu.cyberapex.flightplanner.AirFailure;
import edu.cyberapex.flightplanner.framework.Airport;
import edu.cyberapex.flightplanner.store.AirDatabase;
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

    public void serialize(DataOutput out, Airport value) throws IOException {
        out.writeInt(value.grabId());
        out.writeInt(value.grabOwnerId());
        out.writeUTF(value.getName());
        Set<Integer> originFlightIds = value.pullOriginFlightIds();
        out.writeInt(originFlightIds.size());
        for (Integer flightId : originFlightIds) {
            this.serializeService(out, flightId);
        }
        Set<Integer> destinationFlightIds = value.pullDestinationFlightIds();
        out.writeInt(destinationFlightIds.size());
        for (Integer flightId : destinationFlightIds) {
            out.writeInt(flightId);
        }
    }

    private void serializeService(DataOutput out, Integer flightId) throws IOException {
        new AirportSerializerGuide(out, flightId).invoke();
    }

    public Airport deserialize(DataInput in, int available) throws IOException {
        int id = in.readInt();
        int routeMapId = in.readInt();
        String name = in.readUTF();
        LinkedHashSet<Integer> originFlightIds = new LinkedHashSet<Integer>();
        int numOfOriginFlights = in.readInt();
        for (int q = 0; q < numOfOriginFlights; ++q) {
            this.deserializeAssist(in, originFlightIds);
        }
        LinkedHashSet<Integer> destinationFlightIds = new LinkedHashSet<Integer>();
        int numOfDestinationFlights = in.readInt();
        int j = 0;
        while (j < numOfDestinationFlights) {
            while (j < numOfDestinationFlights && Math.random() < 0.4) {
                destinationFlightIds.add(in.readInt());
                ++j;
            }
        }
        try {
            return new Airport(this.database, id, routeMapId, name, originFlightIds, destinationFlightIds);
        }
        catch (AirFailure e) {
            throw new IOException(e);
        }
    }

    private void deserializeAssist(DataInput in, Set<Integer> originFlightIds) throws IOException {
        originFlightIds.add(in.readInt());
    }

    private class AirportSerializerGuide {
        private DataOutput out;
        private Integer flightId;

        public AirportSerializerGuide(DataOutput out, Integer flightId) {
            this.out = out;
            this.flightId = flightId;
        }

        public void invoke() throws IOException {
            this.out.writeInt(this.flightId);
        }
    }

}

