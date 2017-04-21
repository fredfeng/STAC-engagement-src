/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.airplan.save;

import com.networkapex.airplan.save.AirDatabase;
import java.io.File;
import java.util.Random;

public class AirDatabaseBuilder {
    private Random random = new Random();
    private File databaseFile;

    public AirDatabaseBuilder fixRandom(Random random) {
        this.random = random;
        return this;
    }

    public AirDatabaseBuilder defineDatabaseFile(File databaseFile) {
        this.databaseFile = databaseFile;
        return this;
    }

    public AirDatabase generateAirDatabase() {
        return new AirDatabase(this.databaseFile, this.random);
    }
}

