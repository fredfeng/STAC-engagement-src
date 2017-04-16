/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.storage.index;

import com.graphhopper.routing.util.EdgeFilter;
import com.graphhopper.storage.Storable;
import com.graphhopper.storage.index.QueryResult;

public interface LocationIndex
extends Storable<LocationIndex> {
    public LocationIndex setResolution(int var1);

    public LocationIndex prepareIndex();

    public int findID(double var1, double var3);

    public QueryResult findClosest(double var1, double var3, EdgeFilter var5);

    public LocationIndex setApproximation(boolean var1);

    public void setSegmentSize(int var1);
}

