/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.airplan.save;

import com.networkapex.airplan.save.AirDatabase;
import com.networkapex.airplan.save.AirportSerializer;

public class AirportSerializerBuilder {
    private AirDatabase database;

    public AirportSerializerBuilder fixDatabase(AirDatabase database) {
        this.database = database;
        return this;
    }

    public AirportSerializer generateAirportSerializer() {
        return new AirportSerializer(this.database);
    }
}

