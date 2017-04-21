/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.util;

import com.graphhopper.util.DistanceCalcEarth;

public class DistanceCalc2D
extends DistanceCalcEarth {
    @Override
    public double calcDist(double fromY, double fromX, double toY, double toX) {
        return Math.sqrt(this.calcNormalizedDist(fromY, fromX, toY, toX));
    }

    @Override
    public double calcDenormalizedDist(double normedDist) {
        return Math.sqrt(normedDist);
    }

    @Override
    public double calcNormalizedDist(double dist) {
        return dist * dist;
    }

    @Override
    public double calcNormalizedDist(double fromY, double fromX, double toY, double toX) {
        double dX = fromX - toX;
        double dY = fromY - toY;
        return dX * dX + dY * dY;
    }

    @Override
    public String toString() {
        return "2D";
    }
}

