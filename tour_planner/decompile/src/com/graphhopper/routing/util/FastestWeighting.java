/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing.util;

import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.routing.util.Weighting;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.PMap;

public class FastestWeighting
implements Weighting {
    protected static final double SPEED_CONV = 3.6;
    static final double DEFAULT_HEADING_PENALTY = 300.0;
    private final double heading_penalty;
    protected final FlagEncoder flagEncoder;
    private final double maxSpeed;

    public FastestWeighting(FlagEncoder encoder, PMap pMap) {
        if (!encoder.isRegistered()) {
            throw new IllegalStateException("Make sure you add the FlagEncoder " + encoder + " to an EncodingManager before using it elsewhere");
        }
        this.flagEncoder = encoder;
        this.heading_penalty = pMap.getDouble("heading_penalty", 300.0);
        this.maxSpeed = encoder.getMaxSpeed() / 3.6;
    }

    public FastestWeighting(FlagEncoder encoder) {
        this(encoder, new PMap(0));
    }

    @Override
    public double getMinWeight(double distance) {
        return distance / this.maxSpeed;
    }

    @Override
    public double calcWeight(EdgeIteratorState edge, boolean reverse, int prevOrNextEdgeId) {
        double speed;
        double d = speed = reverse ? this.flagEncoder.getReverseSpeed(edge.getFlags()) : this.flagEncoder.getSpeed(edge.getFlags());
        if (speed == 0.0) {
            return Double.POSITIVE_INFINITY;
        }
        double time = edge.getDistance() / speed * 3.6;
        boolean penalizeEdge = edge.getBoolean(-1, reverse, false);
        if (penalizeEdge) {
            time += this.heading_penalty;
        }
        return time;
    }

    @Override
    public FlagEncoder getFlagEncoder() {
        return this.flagEncoder;
    }

    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.toString().hashCode();
        return hash;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        FastestWeighting other = (FastestWeighting)obj;
        return this.toString().equals(other.toString());
    }

    public String toString() {
        return "FASTEST|" + this.flagEncoder;
    }
}

