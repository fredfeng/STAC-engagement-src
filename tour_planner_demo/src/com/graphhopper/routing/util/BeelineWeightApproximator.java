/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing.util;

import com.graphhopper.routing.util.WeightApproximator;
import com.graphhopper.routing.util.Weighting;
import com.graphhopper.storage.NodeAccess;
import com.graphhopper.util.DistanceCalc;
import com.graphhopper.util.Helper;

public class BeelineWeightApproximator
implements WeightApproximator {
    private final NodeAccess nodeAccess;
    private final Weighting weighting;
    private DistanceCalc distanceCalc = Helper.DIST_EARTH;
    private double toLat;
    private double toLon;

    public BeelineWeightApproximator(NodeAccess nodeAccess, Weighting weighting) {
        this.nodeAccess = nodeAccess;
        this.weighting = weighting;
    }

    @Override
    public void setGoalNode(int toNode) {
        this.toLat = this.nodeAccess.getLatitude(toNode);
        this.toLon = this.nodeAccess.getLongitude(toNode);
    }

    @Override
    public WeightApproximator duplicate() {
        return new BeelineWeightApproximator(this.nodeAccess, this.weighting).setDistanceCalc(this.distanceCalc);
    }

    @Override
    public double approximate(int fromNode) {
        double fromLat = this.nodeAccess.getLatitude(fromNode);
        double fromLon = this.nodeAccess.getLongitude(fromNode);
        double dist2goal = this.distanceCalc.calcDist(this.toLat, this.toLon, fromLat, fromLon);
        double weight2goal = this.weighting.getMinWeight(dist2goal);
        return weight2goal;
    }

    public BeelineWeightApproximator setDistanceCalc(DistanceCalc distanceCalc) {
        this.distanceCalc = distanceCalc;
        return this;
    }
}

