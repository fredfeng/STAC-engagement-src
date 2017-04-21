/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing;

import com.graphhopper.routing.AlgorithmOptions;
import com.graphhopper.routing.RoutingAlgorithm;
import com.graphhopper.storage.Graph;

public interface RoutingAlgorithmFactory {
    public RoutingAlgorithm createAlgo(Graph var1, AlgorithmOptions var2);
}

