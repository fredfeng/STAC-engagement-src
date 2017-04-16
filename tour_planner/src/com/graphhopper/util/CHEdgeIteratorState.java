/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.util;

import com.graphhopper.util.EdgeIteratorState;

public interface CHEdgeIteratorState
extends EdgeIteratorState {
    public int getSkippedEdge1();

    public int getSkippedEdge2();

    public void setSkippedEdges(int var1, int var2);

    public boolean isShortcut();

    public boolean canBeOverwritten(long var1);

    public CHEdgeIteratorState setWeight(double var1);

    public double getWeight();
}

