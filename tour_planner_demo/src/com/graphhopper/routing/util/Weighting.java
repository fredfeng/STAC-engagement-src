/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing.util;

import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.util.EdgeIteratorState;

public interface Weighting {
    public double getMinWeight(double var1);

    public double calcWeight(EdgeIteratorState var1, boolean var2, int var3);

    public FlagEncoder getFlagEncoder();
}

