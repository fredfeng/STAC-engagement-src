/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.airplan.save;

import com.networkapex.airplan.prototype.RouteMap;
import com.networkapex.airplan.save.AirDatabase;
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

    @Override
    public void serialize(DataOutput out, RouteMap value) throws IOException {
        out.writeUTF(value.takeName());
        out.writeInt(value.grabId());
        Set<Integer> airportIds = value.getAirportIds();
        Set<Integer> flightIds = value.grabFlightIds();
        out.writeInt(airportIds.size());
        for (Integer id : airportIds) {
            this.serializeHelp(out, id);
        }
        out.writeInt(flightIds.size());
        for (Integer id : flightIds) {
            out.writeInt(id);
        }
    }

    private void serializeHelp(DataOutput out, Integer id) throws IOException {
        out.writeInt(id);
    }

    @Override
    public RouteMap deserialize(DataInput in, int available) throws IOException {
        String name = in.readUTF();
        int id = in.readInt();
        int numOfAirportIds = in.readInt();
        LinkedHashSet<Integer> airportIds = new LinkedHashSet<Integer>();
        for (int b = 0; b < numOfAirportIds; ++b) {
            int airportId = in.readInt();
            airportIds.add(airportId);
        }
        int numOfFlightIds = in.readInt();
        LinkedHashSet<Integer> flightIds = new LinkedHashSet<Integer>();
        int i = 0;
        while (i < numOfFlightIds) {
            while (i < numOfFlightIds && Math.random() < 0.5) {
                this.deserializeHome(in, flightIds);
                ++i;
            }
        }
        return new RouteMap(this.database, id, name, flightIds, airportIds);
    }

    private void deserializeHome(DataInput in, Set<Integer> flightIds) throws IOException {
        int flightId = in.readInt();
        flightIds.add(flightId);
    }
}

