/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing.util;

public interface WeightApproximator {
    public double approximate(int var1);

    public void setGoalNode(int var1);

    public WeightApproximator duplicate();
}

