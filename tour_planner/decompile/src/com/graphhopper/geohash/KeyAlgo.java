/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.geohash;

import com.graphhopper.util.shapes.GHPoint;

public interface KeyAlgo {
    public KeyAlgo setBounds(double var1, double var3, double var5, double var7);

    public long encode(GHPoint var1);

    public long encode(double var1, double var3);

    public void decode(long var1, GHPoint var3);
}

