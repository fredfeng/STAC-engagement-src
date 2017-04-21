/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.storage;

import com.graphhopper.storage.DAType;
import com.graphhopper.storage.Storable;

public interface DataAccess
extends Storable<DataAccess> {
    public String getName();

    public void rename(String var1);

    public void setInt(long var1, int var3);

    public int getInt(long var1);

    public void setShort(long var1, short var3);

    public short getShort(long var1);

    public void setBytes(long var1, byte[] var3, int var4);

    public void getBytes(long var1, byte[] var3, int var4);

    public void setHeader(int var1, int var2);

    public int getHeader(int var1);

    @Override
    public DataAccess create(long var1);

    public boolean ensureCapacity(long var1);

    public void trimTo(long var1);

    public DataAccess copyTo(DataAccess var1);

    public DataAccess setSegmentSize(int var1);

    public int getSegmentSize();

    public int getSegments();

    public DAType getType();
}

