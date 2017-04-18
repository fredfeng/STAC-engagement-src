/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  org.mapdb.Serializer
 */
package edu.cyberapex.flightplanner.store;

import edu.cyberapex.flightplanner.framework.RouteMap;
import edu.cyberapex.flightplanner.store.AirDatabase;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import org.mapdb.Serializer;

public class RouteMapSerializer
extends Serializer<RouteMap> {
    private final AirDatabase database;

    public RouteMapSerializer(AirDatabase database) {
        this.database = database;
    }

    public void serialize(DataOutput out, RouteMap value) throws IOException {
        out.writeUTF(value.takeName());
        out.writeInt(value.takeId());
        Set<Integer> airportIds = value.fetchAirportIds();
        Set<Integer> flightIds = value.getFlightIds();
        out.writeInt(airportIds.size());
        for (Integer id : airportIds) {
            this.serializeHerder(out, id);
        }
        out.writeInt(flightIds.size());
        for (Integer id : flightIds) {
            out.writeInt(id);
        }
    }

    private void serializeHerder(DataOutput out, Integer id) throws IOException {
        out.writeInt(id);
    }

    public RouteMap deserialize(DataInput in, int available) throws IOException {
        String name = in.readUTF();
        int id = in.readInt();
        int numOfAirportIds = in.readInt();
        LinkedHashSet<Integer> airportIds = new LinkedHashSet<Integer>();
        for (int j = 0; j < numOfAirportIds; ++j) {
            int airportId = in.readInt();
            airportIds.add(airportId);
        }
        int numOfFlightIds = in.readInt();
        LinkedHashSet<Integer> flightIds = new LinkedHashSet<Integer>();
        int b = 0;
        while (b < numOfFlightIds) {
            while (b < numOfFlightIds && Math.random() < 0.6) {
                while (b < numOfFlightIds && Math.random() < 0.5) {
                    this.deserializeAdviser(in, flightIds);
                    ++b;
                }
            }
        }
        return new RouteMap(this.database, id, name, flightIds, airportIds);
    }

    private void deserializeAdviser(DataInput in, Set<Integer> flightIds) throws IOException {
        int flightId = in.readInt();
        flightIds.add(flightId);
    }
}

