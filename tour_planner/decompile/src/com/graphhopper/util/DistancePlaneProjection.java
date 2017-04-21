/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.util;

import com.graphhopper.util.DistanceCalcEarth;

public class DistancePlaneProjection
extends DistanceCalcEarth {
    @Override
    public double calcDist(double fromLat, double fromLon, double toLat, double toLon) {
        double dLat = Math.toRadians(toLat - fromLat);
        double dLon = Math.toRadians(toLon - fromLon);
        double tmp = Math.cos(Math.toRadians((fromLat + toLat) / 2.0)) * dLon;
        double normedDist = dLat * dLat + tmp * tmp;
        return 6371000.0 * Math.sqrt(normedDist);
    }

    @Override
    public double calcDenormalizedDist(double normedDist) {
        return 6371000.0 * Math.sqrt(normedDist);
    }

    @Override
    public double calcNormalizedDist(double dist) {
        double tmp = dist / 6371000.0;
        return tmp * tmp;
    }

    @Override
    public double calcNormalizedDist(double fromLat, double fromLon, double toLat, double toLon) {
        double dLat = Math.toRadians(toLat - fromLat);
        double dLon = Math.toRadians(toLon - fromLon);
        double left = Math.cos(Math.toRadians((fromLat + toLat) / 2.0)) * dLon;
        return dLat * dLat + left * left;
    }

    @Override
    public String toString() {
        return "PLANE_PROJ";
    }
}

