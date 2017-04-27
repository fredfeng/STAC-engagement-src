/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.storage;

import com.graphhopper.storage.DAType;
import com.graphhopper.storage.DataAccess;
import java.nio.ByteOrder;
import java.util.Collection;

public interface Directory {
    public String getLocation();

    public ByteOrder getByteOrder();

    public DataAccess find(String var1);

    public DataAccess find(String var1, DAType var2);

    public void remove(DataAccess var1);

    public DAType getDefaultType();

    public void clear();

    public Collection<DataAccess> getAll();
}

