/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing.ch;

import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.routing.util.Weighting;
import com.graphhopper.util.CHEdgeIteratorState;
import com.graphhopper.util.EdgeIteratorState;

public class PreparationWeighting
implements Weighting {
    private final Weighting userWeighting;

    public PreparationWeighting(Weighting userWeighting) {
        this.userWeighting = userWeighting;
    }

    @Override
    public final double getMinWeight(double distance) {
        return this.userWeighting.getMinWeight(distance);
    }

    @Override
    public double calcWeight(EdgeIteratorState edgeState, boolean reverse, int prevOrNextEdgeId) {
        CHEdgeIteratorState tmp = (CHEdgeIteratorState)edgeState;
        if (tmp.isShortcut()) {
            return tmp.getWeight();
        }
        return this.userWeighting.calcWeight(edgeState, reverse, prevOrNextEdgeId);
    }

    @Override
    public FlagEncoder getFlagEncoder() {
        return this.userWeighting.getFlagEncoder();
    }

    public String toString() {
        return "PREPARE+" + this.userWeighting.toString();
    }
}

