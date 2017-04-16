/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing;

import com.graphhopper.routing.AbstractRoutingAlgorithm;
import com.graphhopper.routing.Path;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.routing.util.TraversalMode;
import com.graphhopper.routing.util.Weighting;
import com.graphhopper.storage.EdgeEntry;
import com.graphhopper.storage.Graph;
import com.graphhopper.util.EdgeExplorer;
import com.graphhopper.util.EdgeIterator;
import com.graphhopper.util.EdgeIteratorState;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import java.util.PriorityQueue;

public class Dijkstra
extends AbstractRoutingAlgorithm {
    protected TIntObjectMap<EdgeEntry> fromMap;
    protected PriorityQueue<EdgeEntry> fromHeap;
    protected EdgeEntry currEdge;
    private int visitedNodes;
    private int to = -1;

    public Dijkstra(Graph g, FlagEncoder encoder, Weighting weighting, TraversalMode tMode) {
        super(g, encoder, weighting, tMode);
        this.initCollections(1000);
    }

    protected void initCollections(int size) {
        this.fromHeap = new PriorityQueue(size);
        this.fromMap = new TIntObjectHashMap<EdgeEntry>(size);
    }

    @Override
    public Path calcPath(int from, int to) {
        this.checkAlreadyRun();
        this.to = to;
        this.currEdge = this.createEdgeEntry(from, 0.0);
        if (!this.traversalMode.isEdgeBased()) {
            this.fromMap.put(from, this.currEdge);
        }
        this.runAlgo();
        return this.extractPath();
    }

    protected void runAlgo() {
        block4 : {
            EdgeExplorer explorer = this.outEdgeExplorer;
            do {
                ++this.visitedNodes;
                if (this.isWeightLimitExceeded() || this.finished()) break block4;
                int startNode = this.currEdge.adjNode;
                EdgeIterator iter = explorer.setBaseNode(startNode);
                while (iter.next()) {
                    if (!this.accept(iter, this.currEdge.edge)) continue;
                    int traversalId = this.traversalMode.createTraversalId(iter, false);
                    double tmpWeight = this.weighting.calcWeight(iter, false, this.currEdge.edge) + this.currEdge.weight;
                    if (Double.isInfinite(tmpWeight)) continue;
                    EdgeEntry nEdge = this.fromMap.get(traversalId);
                    if (nEdge == null) {
                        nEdge = new EdgeEntry(iter.getEdge(), iter.getAdjNode(), tmpWeight);
                        nEdge.parent = this.currEdge;
                        this.fromMap.put(traversalId, nEdge);
                        this.fromHeap.add(nEdge);
                    } else {
                        if (nEdge.weight <= tmpWeight) continue;
                        this.fromHeap.remove(nEdge);
                        nEdge.edge = iter.getEdge();
                        nEdge.weight = tmpWeight;
                        nEdge.parent = this.currEdge;
                        this.fromHeap.add(nEdge);
                    }
                    this.updateBestPath(iter, nEdge, traversalId);
                }
                if (this.fromHeap.isEmpty()) break block4;
                this.currEdge = this.fromHeap.poll();
            } while (this.currEdge != null);
            throw new AssertionError((Object)"Empty edge cannot happen");
        }
    }

    @Override
    protected boolean finished() {
        return this.currEdge.adjNode == this.to;
    }

    @Override
    protected Path extractPath() {
        if (this.currEdge == null || this.isWeightLimitExceeded() || !this.finished()) {
            return this.createEmptyPath();
        }
        return new Path(this.graph, this.flagEncoder).setWeight(this.currEdge.weight).setEdgeEntry(this.currEdge).extract();
    }

    @Override
    public int getVisitedNodes() {
        return this.visitedNodes;
    }

    @Override
    protected boolean isWeightLimitExceeded() {
        return this.currEdge.weight > this.weightLimit;
    }

    @Override
    public String getName() {
        return "dijkstra";
    }
}

