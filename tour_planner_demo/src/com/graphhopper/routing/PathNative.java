/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing;

import com.graphhopper.routing.Path;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.storage.Graph;
import com.graphhopper.util.EdgeIterator;

public class PathNative
extends Path {
    private final int[] parentNodes;
    private final int[] parentEdges;

    public PathNative(Graph g, FlagEncoder encoder, int[] parentNodes, int[] parentEdges) {
        super(g, encoder);
        this.parentNodes = parentNodes;
        this.parentEdges = parentEdges;
    }

    @Override
    public Path extract() {
        int edgeId;
        if (this.endNode < 0) {
            return this;
        }
        while (EdgeIterator.Edge.isValid(edgeId = this.parentEdges[this.endNode])) {
            this.processEdge(edgeId, this.endNode);
            this.endNode = this.parentNodes[this.endNode];
        }
        this.reverseOrder();
        return this.setFound(true);
    }
}

