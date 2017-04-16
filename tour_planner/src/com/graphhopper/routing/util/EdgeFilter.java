/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing.util;

import com.graphhopper.util.EdgeIteratorState;

public interface EdgeFilter {
    public static final EdgeFilter ALL_EDGES = new EdgeFilter(){

        @Override
        public final boolean accept(EdgeIteratorState edgeState) {
            return true;
        }
    };

    public boolean accept(EdgeIteratorState var1);

}

