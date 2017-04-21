/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.util;

import com.graphhopper.util.shapes.BBox;
import com.graphhopper.util.shapes.GHPoint;

public interface DistanceCalc {
    public BBox createBBox(double var1, double var3, double var5);

    public double calcCircumference(double var1);

    public double calcDist(double var1, double var3, double var5, double var7);

    public double calcNormalizedDist(double var1);

    public double calcDenormalizedDist(double var1);

    public double calcNormalizedDist(double var1, double var3, double var5, double var7);

    public boolean validEdgeDistance(double var1, double var3, double var5, double var7, double var9, double var11);

    public double calcNormalizedEdgeDistance(double var1, double var3, double var5, double var7, double var9, double var11);

    public GHPoint calcCrossingPointToEdge(double var1, double var3, double var5, double var7, double var9, double var11);
}

