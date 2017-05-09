/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing;

import com.graphhopper.coll.IntDoubleBinHeap;
import com.graphhopper.routing.AbstractRoutingAlgorithm;
import com.graphhopper.routing.Path;
import com.graphhopper.routing.PathNative;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.routing.util.TraversalMode;
import com.graphhopper.routing.util.Weighting;
import com.graphhopper.storage.Graph;
import com.graphhopper.util.EdgeExplorer;
import com.graphhopper.util.EdgeIterator;
import com.graphhopper.util.EdgeIteratorState;
import gnu.trove.list.array.TIntArrayList;
import java.util.Arrays;

public class DijkstraOneToMany
extends AbstractRoutingAlgorithm {
    private static final int EMPTY_PARENT = -1;
    private static final int NOT_FOUND = -1;
    protected double[] weights;
    private final TIntArrayListWithCap changedNodes;
    private int[] parents;
    private int[] edgeIds;
    private IntDoubleBinHeap heap;
    private int visitedNodes;
    private boolean doClear = true;
    private int limitVisitedNodes = Integer.MAX_VALUE;
    private int endNode;
    private int currNode;
    private int fromNode;
    private int to;

    public DijkstraOneToMany(Graph graph, FlagEncoder encoder, Weighting weighting, TraversalMode tMode) {
        super(graph, encoder, weighting, tMode);
        this.parents = new int[graph.getNodes()];
        Arrays.fill(this.parents, -1);
        this.edgeIds = new int[graph.getNodes()];
        Arrays.fill(this.edgeIds, -1);
        this.weights = new double[graph.getNodes()];
        Arrays.fill(this.weights, Double.MAX_VALUE);
        this.heap = new IntDoubleBinHeap();
        this.changedNodes = new TIntArrayListWithCap();
    }

    public DijkstraOneToMany setLimitVisitedNodes(int nodes) {
        this.limitVisitedNodes = nodes;
        return this;
    }

    @Override
    public Path calcPath(int from, int to) {
        this.fromNode = from;
        this.endNode = this.findEndNode(from, to);
        return this.extractPath();
    }

    @Override
    public Path extractPath() {
        PathNative p = new PathNative(this.graph, this.flagEncoder, this.parents, this.edgeIds);
        if (this.endNode >= 0) {
            p.setWeight(this.weights[this.endNode]);
        }
        p.setFromNode(this.fromNode);
        if (this.endNode < 0 || this.isWeightLimitExceeded()) {
            return p;
        }
        return p.setEndNode(this.endNode).extract();
    }

    public DijkstraOneToMany clear() {
        this.doClear = true;
        return this;
    }

    public double getWeight(int endNode) {
        return this.weights[endNode];
    }

    public int findEndNode(int from, int to) {
        if (this.weights.length < 2) {
            return -1;
        }
        this.to = to;
        if (this.doClear) {
            this.doClear = false;
            int vn = this.changedNodes.size();
            for (int i = 0; i < vn; ++i) {
                int n = this.changedNodes.get(i);
                this.weights[n] = Double.MAX_VALUE;
                this.parents[n] = -1;
                this.edgeIds[n] = -1;
            }
            this.heap.clear();
            this.changedNodes.reset();
            this.currNode = from;
            if (!this.traversalMode.isEdgeBased()) {
                this.weights[this.currNode] = 0.0;
                this.changedNodes.add(this.currNode);
            }
        } else {
            int parentNode = this.parents[to];
            if (parentNode != -1 && this.weights[to] <= this.weights[this.currNode]) {
                return to;
            }
            if (this.heap.isEmpty() || this.visitedNodes >= this.limitVisitedNodes) {
                return -1;
            }
            this.currNode = this.heap.poll_element();
        }
        this.visitedNodes = 0;
        if (this.finished()) {
            return this.currNode;
        }
        do {
            ++this.visitedNodes;
            EdgeIterator iter = this.outEdgeExplorer.setBaseNode(this.currNode);
            while (iter.next()) {
                double tmpWeight;
                int adjNode = iter.getAdjNode();
                int prevEdgeId = this.edgeIds[adjNode];
                if (!this.accept(iter, prevEdgeId) || Double.isInfinite(tmpWeight = this.weighting.calcWeight(iter, false, prevEdgeId) + this.weights[this.currNode])) continue;
                double w = this.weights[adjNode];
                if (w == Double.MAX_VALUE) {
                    this.parents[adjNode] = this.currNode;
                    this.weights[adjNode] = tmpWeight;
                    this.heap.insert_(tmpWeight, adjNode);
                    this.changedNodes.add(adjNode);
                    this.edgeIds[adjNode] = iter.getEdge();
                    continue;
                }
                if (w <= tmpWeight) continue;
                this.parents[adjNode] = this.currNode;
                this.weights[adjNode] = tmpWeight;
                this.heap.update_(tmpWeight, adjNode);
                this.changedNodes.add(adjNode);
                this.edgeIds[adjNode] = iter.getEdge();
            }
            if (this.heap.isEmpty() || this.visitedNodes >= this.limitVisitedNodes || this.isWeightLimitExceeded()) {
                return -1;
            }
            this.currNode = this.heap.peek_element();
            if (this.finished()) {
                return this.currNode;
            }
            this.heap.poll_element();
        } while (true);
    }

    @Override
    public boolean finished() {
        return this.currNode == this.to;
    }

    @Override
    protected boolean isWeightLimitExceeded() {
        return this.weights[this.currNode] > this.weightLimit;
    }

    public void close() {
        this.weights = null;
        this.parents = null;
        this.edgeIds = null;
        this.heap = null;
    }

    @Override
    public int getVisitedNodes() {
        return this.visitedNodes;
    }

    @Override
    public String getName() {
        return "dijkstraOneToMany";
    }

    public String getMemoryUsageAsString() {
        long len = this.weights.length;
        return "" + (16 * len + (long)this.changedNodes.getCapacity() * 4 + (long)this.heap.getCapacity() * 8) / 0x100000 + "MB";
    }

    private static class TIntArrayListWithCap
    extends TIntArrayList {
        private TIntArrayListWithCap() {
        }

        public int getCapacity() {
            return this._data.length;
        }
    }

}

