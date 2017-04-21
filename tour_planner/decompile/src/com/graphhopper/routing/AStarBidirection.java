/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing;

import com.graphhopper.routing.AStar;
import com.graphhopper.routing.AbstractBidirAlgo;
import com.graphhopper.routing.Path;
import com.graphhopper.routing.PathBidirRef;
import com.graphhopper.routing.util.BeelineWeightApproximator;
import com.graphhopper.routing.util.ConsistentWeightApproximator;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.routing.util.TraversalMode;
import com.graphhopper.routing.util.WeightApproximator;
import com.graphhopper.routing.util.Weighting;
import com.graphhopper.storage.EdgeEntry;
import com.graphhopper.storage.Graph;
import com.graphhopper.storage.NodeAccess;
import com.graphhopper.util.DistanceCalc;
import com.graphhopper.util.DistancePlaneProjection;
import com.graphhopper.util.EdgeExplorer;
import com.graphhopper.util.EdgeIterator;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.GHUtility;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import java.util.PriorityQueue;

public class AStarBidirection
extends AbstractBidirAlgo {
    private ConsistentWeightApproximator weightApprox;
    private PriorityQueue<AStar.AStarEdge> prioQueueOpenSetFrom;
    private TIntObjectMap<AStar.AStarEdge> bestWeightMapFrom;
    private PriorityQueue<AStar.AStarEdge> prioQueueOpenSetTo;
    private TIntObjectMap<AStar.AStarEdge> bestWeightMapTo;
    private TIntObjectMap<AStar.AStarEdge> bestWeightMapOther;
    protected AStar.AStarEdge currFrom;
    protected AStar.AStarEdge currTo;
    protected PathBidirRef bestPath;

    public AStarBidirection(Graph graph, FlagEncoder encoder, Weighting weighting, TraversalMode tMode) {
        super(graph, encoder, weighting, tMode);
        int nodes = Math.max(20, graph.getNodes());
        this.initCollections(nodes);
        BeelineWeightApproximator defaultApprox = new BeelineWeightApproximator(this.nodeAccess, weighting);
        defaultApprox.setDistanceCalc(new DistancePlaneProjection());
        this.setApproximation(defaultApprox);
    }

    protected void initCollections(int size) {
        this.prioQueueOpenSetFrom = new PriorityQueue(size / 10);
        this.bestWeightMapFrom = new TIntObjectHashMap<AStar.AStarEdge>(size / 10);
        this.prioQueueOpenSetTo = new PriorityQueue(size / 10);
        this.bestWeightMapTo = new TIntObjectHashMap<AStar.AStarEdge>(size / 10);
    }

    public AStarBidirection setApproximation(WeightApproximator approx) {
        this.weightApprox = new ConsistentWeightApproximator(approx);
        return this;
    }

    @Override
    protected EdgeEntry createEdgeEntry(int node, double weight) {
        throw new IllegalStateException("use AStarEdge constructor directly");
    }

    @Override
    public void initFrom(int from, double weight) {
        this.currFrom = new AStar.AStarEdge(-1, from, weight, weight);
        this.weightApprox.setSourceNode(from);
        this.prioQueueOpenSetFrom.add(this.currFrom);
        if (this.currTo != null) {
            this.currFrom.weight += this.weightApprox.approximate(this.currFrom.adjNode, false);
            this.currTo.weight += this.weightApprox.approximate(this.currTo.adjNode, true);
        }
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
        this.currTo = new AStar.AStarEdge(-1, to, weight, weight);
        this.weightApprox.setGoalNode(to);
        this.prioQueueOpenSetTo.add(this.currTo);
        if (this.currFrom != null) {
            this.currFrom.weight += this.weightApprox.approximate(this.currFrom.adjNode, false);
            this.currTo.weight += this.weightApprox.approximate(this.currTo.adjNode, true);
        }
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
    protected boolean finished() {
        if (this.finishedFrom || this.finishedTo) {
            return true;
        }
        return this.currFrom.weight + this.currTo.weight >= this.bestPath.getWeight();
    }

    @Override
    protected boolean isWeightLimitExceeded() {
        return this.currFrom.weight + this.currTo.weight > this.weightLimit;
    }

    @Override
    boolean fillEdgesFrom() {
        if (this.prioQueueOpenSetFrom.isEmpty()) {
            return false;
        }
        this.currFrom = this.prioQueueOpenSetFrom.poll();
        this.bestWeightMapOther = this.bestWeightMapTo;
        this.fillEdges(this.currFrom, this.prioQueueOpenSetFrom, this.bestWeightMapFrom, this.outEdgeExplorer, false);
        ++this.visitedCountFrom;
        return true;
    }

    @Override
    boolean fillEdgesTo() {
        if (this.prioQueueOpenSetTo.isEmpty()) {
            return false;
        }
        this.currTo = this.prioQueueOpenSetTo.poll();
        this.bestWeightMapOther = this.bestWeightMapFrom;
        this.fillEdges(this.currTo, this.prioQueueOpenSetTo, this.bestWeightMapTo, this.inEdgeExplorer, true);
        ++this.visitedCountTo;
        return true;
    }

    private void fillEdges(AStar.AStarEdge currEdge, PriorityQueue<AStar.AStarEdge> prioQueueOpenSet, TIntObjectMap<AStar.AStarEdge> shortestWeightMap, EdgeExplorer explorer, boolean reverse) {
        int currNode = currEdge.adjNode;
        EdgeIterator iter = explorer.setBaseNode(currNode);
        while (iter.next()) {
            AStar.AStarEdge ase;
            if (!this.accept(iter, currEdge.edge)) continue;
            int neighborNode = iter.getAdjNode();
            int traversalId = this.traversalMode.createTraversalId(iter, reverse);
            float alreadyVisitedWeight = (float)(this.weighting.calcWeight(iter, reverse, currEdge.edge) + currEdge.weightOfVisitedPath);
            if (Double.isInfinite(alreadyVisitedWeight) || (ase = shortestWeightMap.get(traversalId)) != null && ase.weightOfVisitedPath <= (double)alreadyVisitedWeight) continue;
            double currWeightToGoal = this.weightApprox.approximate(neighborNode, reverse);
            double estimationFullDist = (double)alreadyVisitedWeight + currWeightToGoal;
            if (ase == null) {
                ase = new AStar.AStarEdge(iter.getEdge(), neighborNode, estimationFullDist, alreadyVisitedWeight);
                shortestWeightMap.put(traversalId, ase);
            } else {
                assert (ase.weight > estimationFullDist);
                prioQueueOpenSet.remove(ase);
                ase.edge = iter.getEdge();
                ase.weight = estimationFullDist;
                ase.weightOfVisitedPath = alreadyVisitedWeight;
            }
            ase.parent = currEdge;
            prioQueueOpenSet.add(ase);
            this.updateBestPath(iter, ase, traversalId);
        }
    }

    public void updateBestPath(EdgeIteratorState edgeState, AStar.AStarEdge entryCurrent, int currLoc) {
        AStar.AStarEdge entryOther = this.bestWeightMapOther.get(currLoc);
        if (entryOther == null) {
            return;
        }
        boolean reverse = this.bestWeightMapFrom == this.bestWeightMapOther;
        double newWeight = entryCurrent.weightOfVisitedPath + entryOther.weightOfVisitedPath;
        if (this.traversalMode.isEdgeBased()) {
            if (entryOther.edge != entryCurrent.edge) {
                throw new IllegalStateException("cannot happen for edge based execution of " + this.getName());
            }
            if (entryOther.adjNode != entryCurrent.adjNode) {
                entryCurrent = (AStar.AStarEdge)entryCurrent.parent;
                newWeight -= this.weighting.calcWeight(edgeState, reverse, -1);
            } else if (!this.traversalMode.hasUTurnSupport()) {
                return;
            }
        }
        if (newWeight < this.bestPath.getWeight()) {
            this.bestPath.setSwitchToFrom(reverse);
            this.bestPath.edgeEntry = entryCurrent;
            this.bestPath.edgeTo = entryOther;
            this.bestPath.setWeight(newWeight);
        }
    }

    @Override
    public String getName() {
        return "astarbi";
    }
}

