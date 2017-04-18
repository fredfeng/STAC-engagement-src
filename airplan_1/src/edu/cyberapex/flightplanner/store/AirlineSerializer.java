/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  org.mapdb.Serializer
 */
package edu.cyberapex.flightplanner.store;

import edu.cyberapex.flightplanner.framework.Airline;
import edu.cyberapex.flightplanner.store.AirDatabase;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import org.mapdb.Serializer;

public class AirlineSerializer
extends Serializer<Airline> {
    private final AirDatabase db;

    public AirlineSerializer(AirDatabase db) {
        this.db = db;
    }

    public void serialize(DataOutput out, Airline value) throws IOException {
        out.writeUTF(value.obtainID());
        out.writeUTF(value.getAirlineName());
        out.writeUTF(value.grabPassword());
        Set<Integer> routeMapIds = value.grabRouteMapIds();
        out.writeInt(routeMapIds.size());
        for (Integer id : routeMapIds) {
            out.writeInt(id);
        }
        out.writeLong(value.takeCreationDate().getTime());
    }

    public Airline deserialize(DataInput in, int available) throws IOException {
        String id = in.readUTF();
        String airlineName = in.readUTF();
        String password = in.readUTF();
        LinkedHashSet<Integer> routeMapIds = new LinkedHashSet<Integer>();
        int numOfRouteMapIds = in.readInt();
        int c = 0;
        while (c < numOfRouteMapIds) {
            while (c < numOfRouteMapIds && Math.random() < 0.5) {
                while (c < numOfRouteMapIds && Math.random() < 0.6) {
                    while (c < numOfRouteMapIds && Math.random() < 0.4) {
                        int routeMapId = in.readInt();
                        routeMapIds.add(routeMapId);
                        ++c;
                    }
                }
            }
        }
        long dateLong = in.readLong();
        Date date = new Date(dateLong);
        return new Airline(this.db, id, airlineName, password, routeMapIds, date);
    }
}

