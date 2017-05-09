/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing;

import com.graphhopper.routing.AbstractRoutingAlgorithm;
import com.graphhopper.routing.Path;
import com.graphhopper.routing.util.BeelineWeightApproximator;
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
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import java.util.PriorityQueue;

public class AStar
extends AbstractRoutingAlgorithm {
    private WeightApproximator weightApprox;
    private int visitedCount;
    private TIntObjectMap<AStarEdge> fromMap;
    private PriorityQueue<AStarEdge> prioQueueOpenSet;
    private AStarEdge currEdge;
    private int to1 = -1;

    public AStar(Graph g, FlagEncoder encoder, Weighting weighting, TraversalMode tMode) {
        super(g, encoder, weighting, tMode);
        this.initCollections(1000);
        BeelineWeightApproximator defaultApprox = new BeelineWeightApproximator(this.nodeAccess, weighting);
        defaultApprox.setDistanceCalc(new DistancePlaneProjection());
        this.setApproximation(defaultApprox);
    }

    public AStar setApproximation(WeightApproximator approx) {
        this.weightApprox = approx;
        return this;
    }

    protected void initCollections(int size) {
        this.fromMap = new TIntObjectHashMap<AStarEdge>();
        this.prioQueueOpenSet = new PriorityQueue(size);
    }

    @Override
    public Path calcPath(int from, int to) {
        this.checkAlreadyRun();
        this.to1 = to;
        this.weightApprox.setGoalNode(to);
        double weightToGoal = this.weightApprox.approximate(from);
        this.currEdge = new AStarEdge(-1, from, 0.0 + weightToGoal, 0.0);
        if (!this.traversalMode.isEdgeBased()) {
            this.fromMap.put(from, this.currEdge);
        }
        return this.runAlgo();
    }

    private Path runAlgo() {
        block7 : {
            EdgeExplorer explorer = this.outEdgeExplorer;
            do {
                int currVertex = this.currEdge.adjNode;
                ++this.visitedCount;
                if (this.isWeightLimitExceeded()) {
                    return this.createEmptyPath();
                }
                if (this.finished()) break block7;
                EdgeIterator iter = explorer.setBaseNode(currVertex);
                while (iter.next()) {
                    AStarEdge ase;
                    if (!this.accept(iter, this.currEdge.edge)) continue;
                    int neighborNode = iter.getAdjNode();
                    int traversalId = this.traversalMode.createTraversalId(iter, false);
                    float alreadyVisitedWeight = (float)(this.weighting.calcWeight(iter, false, this.currEdge.edge) + this.currEdge.weightOfVisitedPath);
                    if (Double.isInfinite(alreadyVisitedWeight) || (ase = this.fromMap.get(traversalId)) != null && ase.weightOfVisitedPath <= (double)alreadyVisitedWeight) continue;
                    double currWeightToGoal = this.weightApprox.approximate(neighborNode);
                    double distEstimation = (double)alreadyVisitedWeight + currWeightToGoal;
                    if (ase == null) {
                        ase = new AStarEdge(iter.getEdge(), neighborNode, distEstimation, alreadyVisitedWeight);
                        this.fromMap.put(traversalId, ase);
                    } else {
                        assert (ase.weight > distEstimation);
                        this.prioQueueOpenSet.remove(ase);
                        ase.edge = iter.getEdge();
                        ase.weight = distEstimation;
                        ase.weightOfVisitedPath = alreadyVisitedWeight;
                    }
                    ase.parent = this.currEdge;
                    this.prioQueueOpenSet.add(ase);
                    this.updateBestPath(iter, ase, traversalId);
                }
                if (this.prioQueueOpenSet.isEmpty()) {
                    return this.createEmptyPath();
                }
                this.currEdge = this.prioQueueOpenSet.poll();
            } while (this.currEdge != null);
            throw new AssertionError((Object)"Empty edge cannot happen");
        }
        return this.extractPath();
    }

    @Override
    protected Path extractPath() {
        return new Path(this.graph, this.flagEncoder).setWeight(this.currEdge.weight).setEdgeEntry(this.currEdge).extract();
    }

    @Override
    protected EdgeEntry createEdgeEntry(int node, double weight) {
        throw new IllegalStateException("use AStarEdge constructor directly");
    }

    @Override
    protected boolean finished() {
        return this.currEdge.adjNode == this.to1;
    }

    @Override
    public int getVisitedNodes() {
        return this.visitedCount;
    }

    @Override
    protected boolean isWeightLimitExceeded() {
        return this.currEdge.weight > this.weightLimit;
    }

    @Override
    public String getName() {
        return "astar";
    }

    public static class AStarEdge
    extends EdgeEntry {
        double weightOfVisitedPath;

        public AStarEdge(int edgeId, int adjNode, double weightForHeap, double weightOfVisitedPath) {
            super(edgeId, adjNode, weightForHeap);
            this.weightOfVisitedPath = (float)weightOfVisitedPath;
        }
    }

}

