/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing.util;

import com.graphhopper.routing.util.EdgeFilter;
import com.graphhopper.storage.CHGraph;
import com.graphhopper.util.CHEdgeIteratorState;
import com.graphhopper.util.EdgeIteratorState;

public class LevelEdgeFilter
implements EdgeFilter {
    private final CHGraph graph;
    private final int maxNodes;

    public LevelEdgeFilter(CHGraph g) {
        this.graph = g;
        this.maxNodes = g.getNodes();
    }

    @Override
    public boolean accept(EdgeIteratorState edgeIterState) {
        int base = edgeIterState.getBaseNode();
        int adj = edgeIterState.getAdjNode();
        if (base >= this.maxNodes || adj >= this.maxNodes) {
            return true;
        }
        if (((CHEdgeIteratorState)edgeIterState).isShortcut()) {
            return true;
        }
        return this.graph.getLevel(base) <= this.graph.getLevel(adj);
    }
}

