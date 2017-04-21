/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing.ch;

import com.graphhopper.routing.PathBidirRef;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.storage.Graph;
import com.graphhopper.util.CHEdgeIteratorState;
import com.graphhopper.util.EdgeIteratorState;

public class Path4CH
extends PathBidirRef {
    private final Graph routingGraph;

    public Path4CH(Graph routingGraph, Graph baseGraph, FlagEncoder encoder) {
        super(baseGraph, encoder);
        this.routingGraph = routingGraph;
    }

    @Override
    protected final void processEdge(int tmpEdge, int endNode) {
        this.expandEdge((CHEdgeIteratorState)this.routingGraph.getEdgeIteratorState(tmpEdge, endNode), false);
    }

    private void expandEdge(CHEdgeIteratorState mainEdgeState, boolean reverse) {
        if (!mainEdgeState.isShortcut()) {
            double dist = mainEdgeState.getDistance();
            this.distance += dist;
            long flags = mainEdgeState.getFlags();
            this.time += this.calcMillis(dist, flags, reverse);
            this.addEdge(mainEdgeState.getEdge());
            return;
        }
        int skippedEdge1 = mainEdgeState.getSkippedEdge1();
        int skippedEdge2 = mainEdgeState.getSkippedEdge2();
        int from = mainEdgeState.getBaseNode();
        int to = mainEdgeState.getAdjNode();
        if (reverse) {
            int tmp = from;
            from = to;
            to = tmp;
        }
        if (this.reverseOrder) {
            boolean empty;
            CHEdgeIteratorState edgeState = (CHEdgeIteratorState)this.routingGraph.getEdgeIteratorState(skippedEdge1, to);
            boolean bl = empty = edgeState == null;
            if (empty) {
                edgeState = (CHEdgeIteratorState)this.routingGraph.getEdgeIteratorState(skippedEdge2, to);
            }
            this.expandEdge(edgeState, false);
            edgeState = empty ? (CHEdgeIteratorState)this.routingGraph.getEdgeIteratorState(skippedEdge1, from) : (CHEdgeIteratorState)this.routingGraph.getEdgeIteratorState(skippedEdge2, from);
            this.expandEdge(edgeState, true);
        } else {
            boolean empty;
            CHEdgeIteratorState iter = (CHEdgeIteratorState)this.routingGraph.getEdgeIteratorState(skippedEdge1, from);
            boolean bl = empty = iter == null;
            if (empty) {
                iter = (CHEdgeIteratorState)this.routingGraph.getEdgeIteratorState(skippedEdge2, from);
            }
            this.expandEdge(iter, true);
            iter = empty ? (CHEdgeIteratorState)this.routingGraph.getEdgeIteratorState(skippedEdge1, to) : (CHEdgeIteratorState)this.routingGraph.getEdgeIteratorState(skippedEdge2, to);
            this.expandEdge(iter, false);
        }
    }
}

