/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.coll;

public interface GHBitSet {
    public boolean contains(int var1);

    public void add(int var1);

    public int getCardinality();

    public void clear();

    public int next(int var1);

    public GHBitSet copyTo(GHBitSet var1);
}

