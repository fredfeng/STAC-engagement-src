/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.coll;

public interface LongIntMap {
    public int put(long var1, int var3);

    public int get(long var1);

    public long getSize();

    public void optimize();

    public int getMemoryUsage();
}

