/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.airplan.save;

import com.networkapex.airplan.save.AirDatabase;
import com.networkapex.airplan.save.AirlineSerializer;

public class AirlineSerializerBuilder {
    private AirDatabase db;

    public AirlineSerializerBuilder defineDb(AirDatabase db) {
        this.db = db;
        return this;
    }

    public AirlineSerializer generateAirlineSerializer() {
        return new AirlineSerializer(this.db);
    }
}

