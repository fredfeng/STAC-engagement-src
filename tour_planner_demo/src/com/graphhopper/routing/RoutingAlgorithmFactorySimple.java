/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing;

import com.graphhopper.routing.AStar;
import com.graphhopper.routing.AStarBidirection;
import com.graphhopper.routing.AlgorithmOptions;
import com.graphhopper.routing.Dijkstra;
import com.graphhopper.routing.DijkstraBidirectionRef;
import com.graphhopper.routing.DijkstraOneToMany;
import com.graphhopper.routing.RoutingAlgorithm;
import com.graphhopper.routing.RoutingAlgorithmFactory;
import com.graphhopper.routing.util.BeelineWeightApproximator;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.routing.util.TraversalMode;
import com.graphhopper.routing.util.WeightApproximator;
import com.graphhopper.routing.util.Weighting;
import com.graphhopper.storage.Graph;
import com.graphhopper.storage.NodeAccess;
import com.graphhopper.util.DistanceCalc;
import com.graphhopper.util.DistancePlaneProjection;
import com.graphhopper.util.Helper;
import com.graphhopper.util.PMap;

public class RoutingAlgorithmFactorySimple
implements RoutingAlgorithmFactory {
    @Override
    public RoutingAlgorithm createAlgo(Graph g, AlgorithmOptions opts) {
        String algoStr = opts.getAlgorithm();
        if ("dijkstrabi".equalsIgnoreCase(algoStr)) {
            return new DijkstraBidirectionRef(g, opts.getFlagEncoder(), opts.getWeighting(), opts.getTraversalMode());
        }
        if ("dijkstra".equalsIgnoreCase(algoStr)) {
            return new Dijkstra(g, opts.getFlagEncoder(), opts.getWeighting(), opts.getTraversalMode());
        }
        if ("astarbi".equalsIgnoreCase(algoStr)) {
            AStarBidirection aStarBi = new AStarBidirection(g, opts.getFlagEncoder(), opts.getWeighting(), opts.getTraversalMode());
            aStarBi.setApproximation(this.getApproximation("astarbi", opts, g.getNodeAccess()));
            return aStarBi;
        }
        if ("dijkstraOneToMany".equalsIgnoreCase(algoStr)) {
            return new DijkstraOneToMany(g, opts.getFlagEncoder(), opts.getWeighting(), opts.getTraversalMode());
        }
        if ("astar".equalsIgnoreCase(algoStr)) {
            AStar aStar = new AStar(g, opts.getFlagEncoder(), opts.getWeighting(), opts.getTraversalMode());
            aStar.setApproximation(this.getApproximation("astar", opts, g.getNodeAccess()));
            return aStar;
        }
        throw new IllegalArgumentException("Algorithm " + algoStr + " not found in " + this.getClass().getName());
    }

    private WeightApproximator getApproximation(String prop, AlgorithmOptions opts, NodeAccess na) {
        String approxAsStr = opts.getHints().get(prop + ".approximation", "BeelineSimplification");
        if ("BeelineSimplification".equals(approxAsStr)) {
            BeelineWeightApproximator approx = new BeelineWeightApproximator(na, opts.getWeighting());
            approx.setDistanceCalc(Helper.DIST_PLANE);
            return approx;
        }
        if ("BeelineAccurate".equals(approxAsStr)) {
            BeelineWeightApproximator approx = new BeelineWeightApproximator(na, opts.getWeighting());
            approx.setDistanceCalc(Helper.DIST_EARTH);
            return approx;
        }
        throw new IllegalArgumentException("Approximation " + approxAsStr + " not found in " + this.getClass().getName());
    }
}

