/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing.util;

import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.routing.util.TurnCostEncoder;
import com.graphhopper.routing.util.Weighting;
import com.graphhopper.storage.TurnCostExtension;
import com.graphhopper.util.EdgeIteratorState;

public class TurnWeighting
implements Weighting {
    private final TurnCostEncoder turnCostEncoder;
    private final TurnCostExtension turnCostExt;
    private final Weighting superWeighting;
    private double defaultUTurnCost = 40.0;

    public TurnWeighting(Weighting superWeighting, TurnCostEncoder encoder, TurnCostExtension turnCostExt) {
        this.turnCostEncoder = encoder;
        this.superWeighting = superWeighting;
        this.turnCostExt = turnCostExt;
        if (encoder == null) {
            throw new IllegalArgumentException("No encoder set to calculate turn weight");
        }
        if (turnCostExt == null) {
            throw new RuntimeException("No storage set to calculate turn weight");
        }
    }

    public TurnWeighting setDefaultUTurnCost(double costInSeconds) {
        this.defaultUTurnCost = costInSeconds;
        return this;
    }

    @Override
    public double getMinWeight(double distance) {
        return this.superWeighting.getMinWeight(distance);
    }

    @Override
    public double calcWeight(EdgeIteratorState edgeState, boolean reverse, int prevOrNextEdgeId) {
        double weight = this.superWeighting.calcWeight(edgeState, reverse, prevOrNextEdgeId);
        if (prevOrNextEdgeId == -1) {
            return weight;
        }
        int edgeId = edgeState.getEdge();
        double turnCosts = reverse ? this.calcTurnWeight(edgeId, edgeState.getBaseNode(), prevOrNextEdgeId) : this.calcTurnWeight(prevOrNextEdgeId, edgeState.getBaseNode(), edgeId);
        if (turnCosts == 0.0 && edgeId == prevOrNextEdgeId) {
            return weight + this.defaultUTurnCost;
        }
        return weight + turnCosts;
    }

    public double calcTurnWeight(int edgeFrom, int nodeVia, int edgeTo) {
        long turnFlags = this.turnCostExt.getTurnCostFlags(edgeFrom, nodeVia, edgeTo);
        if (this.turnCostEncoder.isTurnRestricted(turnFlags)) {
            return Double.POSITIVE_INFINITY;
        }
        return this.turnCostEncoder.getTurnCost(turnFlags);
    }

    @Override
    public FlagEncoder getFlagEncoder() {
        return this.superWeighting.getFlagEncoder();
    }

    public String toString() {
        return "TURN|" + this.superWeighting.toString();
    }
}

