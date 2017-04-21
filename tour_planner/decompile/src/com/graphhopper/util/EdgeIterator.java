/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.util;

import com.graphhopper.util.EdgeIteratorState;

public interface EdgeIterator
extends EdgeIteratorState {
    public static final int NO_EDGE = -1;

    public boolean next();

    public static class Edge {
        public static boolean isValid(int edgeId) {
            return edgeId > -1;
        }
    }

}

