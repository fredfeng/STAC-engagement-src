/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.util;

import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.util.PointList;

public interface EdgeIteratorState {
    public static final int K_UNFAVORED_EDGE = -1;

    public int getEdge();

    public int getBaseNode();

    public int getAdjNode();

    public PointList fetchWayGeometry(int var1);

    public EdgeIteratorState setWayGeometry(PointList var1);

    public double getDistance();

    public EdgeIteratorState setDistance(double var1);

    public long getFlags();

    public EdgeIteratorState setFlags(long var1);

    public int getAdditionalField();

    public boolean isForward(FlagEncoder var1);

    public boolean isBackward(FlagEncoder var1);

    public boolean getBoolean(int var1, boolean var2, boolean var3);

    public EdgeIteratorState setAdditionalField(int var1);

    public String getName();

    public EdgeIteratorState setName(String var1);

    public EdgeIteratorState detach(boolean var1);

    public EdgeIteratorState copyPropertiesTo(EdgeIteratorState var1);
}

