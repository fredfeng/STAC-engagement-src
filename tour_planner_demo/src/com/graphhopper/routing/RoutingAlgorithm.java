/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing;

import com.graphhopper.routing.Path;
import com.graphhopper.util.NotThreadSafe;

@NotThreadSafe
public interface RoutingAlgorithm {
    public Path calcPath(int var1, int var2);

    public void setWeightLimit(double var1);

    public String getName();

    public int getVisitedNodes();
}

