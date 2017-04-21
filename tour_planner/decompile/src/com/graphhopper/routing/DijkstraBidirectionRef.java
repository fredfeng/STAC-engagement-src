/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing;

import com.graphhopper.routing.AbstractBidirAlgo;
import com.graphhopper.routing.Path;
import com.graphhopper.routing.PathBidirRef;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.routing.util.TraversalMode;
import com.graphhopper.routing.util.Weighting;
import com.graphhopper.storage.EdgeEntry;
import com.graphhopper.storage.Graph;
import com.graphhopper.util.EdgeExplorer;
import com.graphhopper.util.EdgeIterator;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.GHUtility;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import java.util.PriorityQueue;

public class DijkstraBidirectionRef
extends AbstractBidirAlgo {
    private PriorityQueue<EdgeEntry> openSetFrom;
    private PriorityQueue<EdgeEntry> openSetTo;
    private TIntObjectMap<EdgeEntry> bestWeightMapFrom;
    private TIntObjectMap<EdgeEntry> bestWeightMapTo;
    protected TIntObjectMap<EdgeEntry> bestWeightMapOther;
    protected EdgeEntry currFrom;
    protected EdgeEntry currTo;
    protected PathBidirRef bestPath;
    private boolean updateBestPath = true;

    public DijkstraBidirectionRef(Graph graph, FlagEncoder encoder, Weighting weighting, TraversalMode tMode) {
        super(graph, encoder, weighting, tMode);
        this.initCollections(1000);
    }

    protected void initCollections(int nodes) {
        this.openSetFrom = new PriorityQueue(nodes / 10);
        this.bestWeightMapFrom = new TIntObjectHashMap<EdgeEntry>(nodes / 10);
        this.openSetTo = new PriorityQueue(nodes / 10);
        this.bestWeightMapTo = new TIntObjectHashMap<EdgeEntry>(nodes / 10);
    }

    @Override
    public void initFrom(int from, double weight) {
        this.currFrom = this.createEdgeEntry(from, weight);
        this.openSetFrom.add(this.currFrom);
        if (!this.traversalMode.isEdgeBased()) {
            this.bestWeightMapFrom.put(from, this.currFrom);
            if (this.currTo != null) {
                this.bestWeightMapOther = this.bestWeightMapTo;
                this.updateBestPath(GHUtility.getEdge(this.graph, from, this.currTo.adjNode), this.currTo, from);
            }
        } else if (this.currTo != null && this.currTo.adjNode == from) {
            this.bestPath.edgeEntry = this.currFrom;
            this.bestPath.edgeTo = this.currTo;
            this.finishedFrom = true;
            this.finishedTo = true;
        }
    }

    @Override
    public void initTo(int to, double weight) {
        this.currTo = this.createEdgeEntry(to, weight);
        this.openSetTo.add(this.currTo);
        if (!this.traversalMode.isEdgeBased()) {
            this.bestWeightMapTo.put(to, this.currTo);
            if (this.currFrom != null) {
                this.bestWeightMapOther = this.bestWeightMapFrom;
                this.updateBestPath(GHUtility.getEdge(this.graph, this.currFrom.adjNode, to), this.currFrom, to);
            }
        } else if (this.currFrom != null && this.currFrom.adjNode == to) {
            this.bestPath.edgeEntry = this.currFrom;
            this.bestPath.edgeTo = this.currTo;
            this.finishedFrom = true;
            this.finishedTo = true;
        }
    }

    @Override
    protected Path createAndInitPath() {
        this.bestPath = new PathBidirRef(this.graph, this.flagEncoder);
        return this.bestPath;
    }

    @Override
    protected Path extractPath() {
        if (this.finished()) {
            return this.bestPath.extract();
        }
        return this.bestPath;
    }

    @Override
    protected double getCurrentFromWeight() {
        return this.currFrom.weight;
    }

    @Override
    protected double getCurrentToWeight() {
        return this.currTo.weight;
    }

    @Override
    public boolean fillEdgesFrom() {
        if (this.openSetFrom.isEmpty()) {
            return false;
        }
        this.currFrom = this.openSetFrom.poll();
        this.bestWeightMapOther = this.bestWeightMapTo;
        this.fillEdges(this.currFrom, this.openSetFrom, this.bestWeightMapFrom, this.outEdgeExplorer, false);
        ++this.visitedCountFrom;
        return true;
    }

    @Override
    public boolean fillEdgesTo() {
        if (this.openSetTo.isEmpty()) {
            return false;
        }
        this.currTo = this.openSetTo.poll();
        this.bestWeightMapOther = this.bestWeightMapFrom;
        this.fillEdges(this.currTo, this.openSetTo, this.bestWeightMapTo, this.inEdgeExplorer, true);
        ++this.visitedCountTo;
        return true;
    }

    @Override
    public boolean finished() {
        if (this.finishedFrom || this.finishedTo) {
            return true;
        }
        return this.currFrom.weight + this.currTo.weight >= this.bestPath.getWeight();
    }

    @Override
    protected boolean isWeightLimitExceeded() {
        return this.currFrom.weight + this.currTo.weight > this.weightLimit;
    }

    void fillEdges(EdgeEntry currEdge, PriorityQueue<EdgeEntry> prioQueue, TIntObjectMap<EdgeEntry> shortestWeightMap, EdgeExplorer explorer, boolean reverse) {
        EdgeIterator iter = explorer.setBaseNode(currEdge.adjNode);
        while (iter.next()) {
            if (!this.accept(iter, currEdge.edge)) continue;
            int traversalId = this.traversalMode.createTraversalId(iter, reverse);
            double tmpWeight = this.weighting.calcWeight(iter, reverse, currEdge.edge) + currEdge.weight;
            if (Double.isInfinite(tmpWeight)) continue;
            EdgeEntry ee = shortestWeightMap.get(traversalId);
            if (ee == null) {
                ee = new EdgeEntry(iter.getEdge(), iter.getAdjNode(), tmpWeight);
                ee.parent = currEdge;
                shortestWeightMap.put(traversalId, ee);
                prioQueue.add(ee);
            } else {
                if (ee.weight <= tmpWeight) continue;
                prioQueue.remove(ee);
                ee.edge = iter.getEdge();
                ee.weight = tmpWeight;
                ee.parent = currEdge;
                prioQueue.add(ee);
            }
            if (!this.updateBestPath) continue;
            this.updateBestPath(iter, ee, traversalId);
        }
    }

    @Override
    protected void updateBestPath(EdgeIteratorState edgeState, EdgeEntry entryCurrent, int traversalId) {
        EdgeEntry entryOther = this.bestWeightMapOther.get(traversalId);
        if (entryOther == null) {
            return;
        }
        boolean reverse = this.bestWeightMapFrom == this.bestWeightMapOther;
        double newWeight = entryCurrent.weight + entryOther.weight;
        if (this.traversalMode.isEdgeBased()) {
            if (entryOther.edge != entryCurrent.edge) {
                throw new IllegalStateException("cannot happen for edge based execution of " + this.getName());
            }
            if (entryOther.adjNode != entryCurrent.adjNode) {
                entryCurrent = entryCurrent.parent;
                newWeight -= this.weighting.calcWeight(edgeState, reverse, -1);
            } else if (!this.traversalMode.hasUTurnSupport()) {
                return;
            }
        }
        if (newWeight < this.bestPath.getWeight()) {
            this.bestPath.setSwitchToFrom(reverse);
            this.bestPath.setEdgeEntry(entryCurrent);
            this.bestPath.setWeight(newWeight);
            this.bestPath.setEdgeEntryTo(entryOther);
        }
    }

    TIntObjectMap<EdgeEntry> getBestFromMap() {
        return this.bestWeightMapFrom;
    }

    TIntObjectMap<EdgeEntry> getBestToMap() {
        return this.bestWeightMapTo;
    }

    void setBestOtherMap(TIntObjectMap<EdgeEntry> other) {
        this.bestWeightMapOther = other;
    }

    void setFromDataStructures(DijkstraBidirectionRef dijkstra) {
        this.openSetFrom = dijkstra.openSetFrom;
        this.bestWeightMapFrom = dijkstra.bestWeightMapFrom;
        this.finishedFrom = dijkstra.finishedFrom;
        this.currFrom = dijkstra.currFrom;
        this.visitedCountFrom = dijkstra.visitedCountFrom;
    }

    void setToDataStructures(DijkstraBidirectionRef dijkstra) {
        this.openSetTo = dijkstra.openSetTo;
        this.bestWeightMapTo = dijkstra.bestWeightMapTo;
        this.finishedTo = dijkstra.finishedTo;
        this.currTo = dijkstra.currTo;
        this.visitedCountTo = dijkstra.visitedCountTo;
    }

    void setUpdateBestPath(boolean b) {
        this.updateBestPath = b;
    }

    void setBestPath(PathBidirRef bestPath) {
        this.bestPath = bestPath;
    }

    @Override
    public String getName() {
        return "dijkstrabi";
    }
}

