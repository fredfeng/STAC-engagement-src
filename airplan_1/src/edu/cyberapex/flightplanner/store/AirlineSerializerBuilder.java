/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.flightplanner.store;

import edu.cyberapex.flightplanner.store.AirDatabase;
import edu.cyberapex.flightplanner.store.AirlineSerializer;

public class AirlineSerializerBuilder {
    private AirDatabase db;

    public AirlineSerializerBuilder setDb(AirDatabase db) {
        this.db = db;
        return this;
    }

    public AirlineSerializer generateAirlineSerializer() {
        return new AirlineSerializer(this.db);
    }
}

