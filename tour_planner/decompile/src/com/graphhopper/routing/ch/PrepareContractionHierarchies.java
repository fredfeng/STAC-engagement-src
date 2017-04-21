/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing.ch;

import com.graphhopper.coll.GHTreeMapComposed;
import com.graphhopper.routing.AStar;
import com.graphhopper.routing.AStarBidirection;
import com.graphhopper.routing.AbstractBidirAlgo;
import com.graphhopper.routing.AlgorithmOptions;
import com.graphhopper.routing.DijkstraBidirectionRef;
import com.graphhopper.routing.DijkstraOneToMany;
import com.graphhopper.routing.Path;
import com.graphhopper.routing.PathBidirRef;
import com.graphhopper.routing.RoutingAlgorithm;
import com.graphhopper.routing.RoutingAlgorithmFactory;
import com.graphhopper.routing.ch.Path4CH;
import com.graphhopper.routing.ch.PreparationWeighting;
import com.graphhopper.routing.ch.PrepareEncoder;
import com.graphhopper.routing.util.AbstractAlgoPreparation;
import com.graphhopper.routing.util.AllCHEdgesIterator;
import com.graphhopper.routing.util.AllEdgesIterator;
import com.graphhopper.routing.util.DefaultEdgeFilter;
import com.graphhopper.routing.util.EdgeFilter;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.routing.util.LevelEdgeFilter;
import com.graphhopper.routing.util.TraversalMode;
import com.graphhopper.routing.util.Weighting;
import com.graphhopper.storage.CHGraph;
import com.graphhopper.storage.CHGraphImpl;
import com.graphhopper.storage.DataAccess;
import com.graphhopper.storage.Directory;
import com.graphhopper.storage.EdgeEntry;
import com.graphhopper.storage.Graph;
import com.graphhopper.storage.GraphHopperStorage;
import com.graphhopper.storage.NodeAccess;
import com.graphhopper.util.CHEdgeExplorer;
import com.graphhopper.util.CHEdgeIterator;
import com.graphhopper.util.CHEdgeIteratorState;
import com.graphhopper.util.EdgeIterator;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.GHUtility;
import com.graphhopper.util.Helper;
import com.graphhopper.util.StopWatch;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrepareContractionHierarchies
extends AbstractAlgoPreparation
implements RoutingAlgorithmFactory {
    private final Logger logger;
    private final PreparationWeighting prepareWeighting;
    private final FlagEncoder prepareFlagEncoder;
    private final TraversalMode traversalMode;
    private CHEdgeExplorer vehicleInExplorer;
    private CHEdgeExplorer vehicleOutExplorer;
    private CHEdgeExplorer vehicleAllExplorer;
    private CHEdgeExplorer vehicleAllTmpExplorer;
    private CHEdgeExplorer calcPrioAllExplorer;
    private final LevelEdgeFilter levelFilter;
    private int maxLevel;
    private final GraphHopperStorage ghStorage;
    private final CHGraphImpl prepareGraph;
    private GHTreeMapComposed sortedNodes;
    private int[] oldPriorities;
    private final DataAccess originalEdges;
    private final Map<Shortcut, Shortcut> shortcuts;
    private IgnoreNodeFilter ignoreNodeFilter;
    private DijkstraOneToMany prepareAlgo;
    private long counter;
    private int newShortcuts;
    private long dijkstraCount;
    private double meanDegree;
    private final Random rand;
    private StopWatch dijkstraSW;
    private final StopWatch allSW;
    private int periodicUpdatesPercentage;
    private int lastNodesLazyUpdatePercentage;
    private int neighborUpdatePercentage;
    private int initialCollectionSize;
    private double nodesContractedPercentage;
    private double logMessagesPercentage;
    private double dijkstraTime;
    private double periodTime;
    private double lazyTime;
    private double neighborTime;
    private int maxEdgesCount;
    AddShortcutHandler addScHandler;
    CalcShortcutHandler calcScHandler;

    public PrepareContractionHierarchies(Directory dir, GraphHopperStorage ghStorage, CHGraph chGraph, FlagEncoder encoder, Weighting weighting, TraversalMode traversalMode) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.shortcuts = new HashMap<Shortcut, Shortcut>();
        this.rand = new Random(123);
        this.dijkstraSW = new StopWatch();
        this.allSW = new StopWatch();
        this.periodicUpdatesPercentage = 20;
        this.lastNodesLazyUpdatePercentage = 10;
        this.neighborUpdatePercentage = 20;
        this.initialCollectionSize = 5000;
        this.nodesContractedPercentage = 100.0;
        this.logMessagesPercentage = 20.0;
        this.addScHandler = new AddShortcutHandler();
        this.calcScHandler = new CalcShortcutHandler();
        this.ghStorage = ghStorage;
        this.prepareGraph = (CHGraphImpl)chGraph;
        this.traversalMode = traversalMode;
        this.prepareFlagEncoder = encoder;
        this.levelFilter = new LevelEdgeFilter(this.prepareGraph);
        this.prepareWeighting = new PreparationWeighting(weighting);
        this.originalEdges = dir.find("original_edges_" + this.prepareGraph.weightingToFileName(weighting));
        this.originalEdges.create(1000);
    }

    public PrepareContractionHierarchies setPeriodicUpdates(int periodicUpdates) {
        if (periodicUpdates < 0) {
            return this;
        }
        if (periodicUpdates > 100) {
            throw new IllegalArgumentException("periodicUpdates has to be in [0, 100], to disable it use 0");
        }
        this.periodicUpdatesPercentage = periodicUpdates;
        return this;
    }

    public PrepareContractionHierarchies setLazyUpdates(int lazyUpdates) {
        if (lazyUpdates < 0) {
            return this;
        }
        if (lazyUpdates > 100) {
            throw new IllegalArgumentException("lazyUpdates has to be in [0, 100], to disable it use 0");
        }
        this.lastNodesLazyUpdatePercentage = lazyUpdates;
        return this;
    }

    public PrepareContractionHierarchies setNeighborUpdates(int neighborUpdates) {
        if (neighborUpdates < 0) {
            return this;
        }
        if (neighborUpdates > 100) {
            throw new IllegalArgumentException("neighborUpdates has to be in [0, 100], to disable it use 0");
        }
        this.neighborUpdatePercentage = neighborUpdates;
        return this;
    }

    public PrepareContractionHierarchies setLogMessages(double logMessages) {
        if (logMessages >= 0.0) {
            this.logMessagesPercentage = logMessages;
        }
        return this;
    }

    public PrepareContractionHierarchies setContractedNodes(double nodesContracted) {
        if (nodesContracted < 0.0) {
            return this;
        }
        if (nodesContracted > 100.0) {
            throw new IllegalArgumentException("setNodesContracted can be 100% maximum");
        }
        this.nodesContractedPercentage = nodesContracted;
        return this;
    }

    public void setInitialCollectionSize(int initialCollectionSize) {
        this.initialCollectionSize = initialCollectionSize;
    }

    @Override
    public void doWork() {
        if (this.prepareFlagEncoder == null) {
            throw new IllegalStateException("No vehicle encoder set.");
        }
        if (this.prepareWeighting == null) {
            throw new IllegalStateException("No weight calculation set.");
        }
        this.allSW.start();
        super.doWork();
        this.initFromGraph();
        if (!this.prepareNodes()) {
            return;
        }
        this.contractNodes();
    }

    boolean prepareNodes() {
        int node;
        int nodes = this.prepareGraph.getNodes();
        for (node = 0; node < nodes; ++node) {
            this.prepareGraph.setLevel(node, this.maxLevel);
        }
        for (node = 0; node < nodes; ++node) {
            int priority = this.oldPriorities[node] = this.calculatePriority(node);
            this.sortedNodes.insert(node, priority);
        }
        if (this.sortedNodes.isEmpty()) {
            return false;
        }
        return true;
    }

    void contractNodes() {
        this.meanDegree = this.prepareGraph.getAllEdges().getMaxId() / this.prepareGraph.getNodes();
        int level = 1;
        this.counter = 0;
        int initSize = this.sortedNodes.getSize();
        long logSize = Math.round(Math.max(10.0, (double)(this.sortedNodes.getSize() / 100) * this.logMessagesPercentage));
        if (this.logMessagesPercentage == 0.0) {
            logSize = Integer.MAX_VALUE;
        }
        boolean periodicUpdate = true;
        StopWatch periodSW = new StopWatch();
        int updateCounter = 0;
        long periodicUpdatesCount = Math.round(Math.max(10.0, (double)this.sortedNodes.getSize() / 100.0 * (double)this.periodicUpdatesPercentage));
        if (this.periodicUpdatesPercentage == 0) {
            periodicUpdate = false;
        }
        long lastNodesLazyUpdates = Math.round((double)this.sortedNodes.getSize() / 100.0 * (double)this.lastNodesLazyUpdatePercentage);
        long nodesToAvoidContract = Math.round((100.0 - this.nodesContractedPercentage) / 100.0 * (double)this.sortedNodes.getSize());
        StopWatch lazySW = new StopWatch();
        boolean neighborUpdate = true;
        if (this.neighborUpdatePercentage == 0) {
            neighborUpdate = false;
        }
        StopWatch neighborSW = new StopWatch();
        while (!this.sortedNodes.isEmpty()) {
            if (periodicUpdate && this.counter > 0 && this.counter % periodicUpdatesCount == 0) {
                periodSW.start();
                this.sortedNodes.clear();
                int len = this.prepareGraph.getNodes();
                for (int node = 0; node < len; ++node) {
                    if (this.prepareGraph.getLevel(node) != this.maxLevel) continue;
                    int priority = this.oldPriorities[node] = this.calculatePriority(node);
                    this.sortedNodes.insert(node, priority);
                }
                periodSW.stop();
                ++updateCounter;
                if (this.sortedNodes.isEmpty()) {
                    throw new IllegalStateException("Cannot prepare as no unprepared nodes where found. Called preparation twice?");
                }
            }
            if (this.counter % logSize == 0) {
                this.dijkstraTime += (double)this.dijkstraSW.getSeconds();
                this.periodTime += (double)periodSW.getSeconds();
                this.lazyTime += (double)lazySW.getSeconds();
                this.neighborTime += (double)neighborSW.getSeconds();
                this.logger.info(Helper.nf(this.counter) + ", updates:" + updateCounter + ", nodes: " + Helper.nf(this.sortedNodes.getSize()) + ", shortcuts:" + Helper.nf(this.newShortcuts) + ", dijkstras:" + Helper.nf(this.dijkstraCount) + ", " + this.getTimesAsString() + ", meanDegree:" + (long)this.meanDegree + ", algo:" + this.prepareAlgo.getMemoryUsageAsString() + ", " + Helper.getMemInfo());
                this.dijkstraSW = new StopWatch();
                periodSW = new StopWatch();
                lazySW = new StopWatch();
                neighborSW = new StopWatch();
            }
            ++this.counter;
            int polledNode = this.sortedNodes.pollKey();
            if (!this.sortedNodes.isEmpty() && (long)this.sortedNodes.getSize() < lastNodesLazyUpdates) {
                lazySW.start();
                int priority = this.oldPriorities[polledNode] = this.calculatePriority(polledNode);
                if (priority > this.sortedNodes.peekValue()) {
                    this.sortedNodes.insert(polledNode, priority);
                    lazySW.stop();
                    continue;
                }
                lazySW.stop();
            }
            this.newShortcuts += this.addShortcuts(polledNode);
            this.prepareGraph.setLevel(polledNode, level);
            ++level;
            if ((long)this.sortedNodes.getSize() < nodesToAvoidContract) break;
            CHEdgeIterator iter = this.vehicleAllExplorer.setBaseNode(polledNode);
            while (iter.next()) {
                int nn = iter.getAdjNode();
                if (this.prepareGraph.getLevel(nn) != this.maxLevel) continue;
                if (neighborUpdate && this.rand.nextInt(100) < this.neighborUpdatePercentage) {
                    neighborSW.start();
                    int oldPrio = this.oldPriorities[nn];
                    int priority = this.oldPriorities[nn] = this.calculatePriority(nn);
                    if (priority != oldPrio) {
                        this.sortedNodes.update(nn, oldPrio, priority);
                    }
                    neighborSW.stop();
                }
                this.prepareGraph.disconnect(this.vehicleAllTmpExplorer, iter);
            }
        }
        this.close();
        this.dijkstraTime += (double)this.dijkstraSW.getSeconds();
        this.periodTime += (double)periodSW.getSeconds();
        this.lazyTime += (double)lazySW.getSeconds();
        this.neighborTime += (double)neighborSW.getSeconds();
        this.logger.info("took:" + (int)this.allSW.stop().getSeconds() + ", new shortcuts: " + Helper.nf(this.newShortcuts) + ", " + this.prepareWeighting + ", " + this.prepareFlagEncoder + ", dijkstras:" + this.dijkstraCount + ", " + this.getTimesAsString() + ", meanDegree:" + (long)this.meanDegree + ", initSize:" + initSize + ", periodic:" + this.periodicUpdatesPercentage + ", lazy:" + this.lastNodesLazyUpdatePercentage + ", neighbor:" + this.neighborUpdatePercentage + ", " + Helper.getMemInfo());
    }

    public long getDijkstraCount() {
        return this.dijkstraCount;
    }

    public double getLazyTime() {
        return this.lazyTime;
    }

    public double getPeriodTime() {
        return this.periodTime;
    }

    public double getDijkstraTime() {
        return this.dijkstraTime;
    }

    public double getNeighborTime() {
        return this.neighborTime;
    }

    public void close() {
        this.prepareAlgo.close();
        this.originalEdges.close();
        this.sortedNodes = null;
        this.oldPriorities = null;
    }

    private String getTimesAsString() {
        return "t(dijk):" + Helper.round2(this.dijkstraTime) + ", t(period):" + Helper.round2(this.periodTime) + ", t(lazy):" + Helper.round2(this.lazyTime) + ", t(neighbor):" + Helper.round2(this.neighborTime);
    }

    Set<Shortcut> testFindShortcuts(int node) {
        this.findShortcuts(this.addScHandler.setNode(node));
        return this.shortcuts.keySet();
    }

    int calculatePriority(int v) {
        this.findShortcuts(this.calcScHandler.setNode(v));
        int originalEdgesCount = this.calcScHandler.originalEdgesCount;
        int contractedNeighbors = 0;
        int degree = 0;
        CHEdgeIterator iter = this.calcPrioAllExplorer.setBaseNode(v);
        while (iter.next()) {
            ++degree;
            if (!iter.isShortcut()) continue;
            ++contractedNeighbors;
        }
        int edgeDifference = this.calcScHandler.shortcuts - degree;
        return 10 * edgeDifference + originalEdgesCount + contractedNeighbors;
    }

    void findShortcuts(ShortcutHandler sch) {
        long tmpDegreeCounter = 0;
        CHEdgeIterator incomingEdges = this.vehicleInExplorer.setBaseNode(sch.getNode());
        while (incomingEdges.next()) {
            int u_fromNode = incomingEdges.getAdjNode();
            if (this.prepareGraph.getLevel(u_fromNode) != this.maxLevel) continue;
            double v_u_dist = incomingEdges.getDistance();
            double v_u_weight = this.prepareWeighting.calcWeight(incomingEdges, true, -1);
            int skippedEdge1 = incomingEdges.getEdge();
            int incomingEdgeOrigCount = this.getOrigEdgeCount(skippedEdge1);
            CHEdgeIterator outgoingEdges = this.vehicleOutExplorer.setBaseNode(sch.getNode());
            this.prepareAlgo.clear();
            ++tmpDegreeCounter;
            while (outgoingEdges.next()) {
                int w_toNode = outgoingEdges.getAdjNode();
                if (this.prepareGraph.getLevel(w_toNode) != this.maxLevel || u_fromNode == w_toNode) continue;
                double existingDirectWeight = v_u_weight + this.prepareWeighting.calcWeight(outgoingEdges, false, incomingEdges.getEdge());
                if (Double.isNaN(existingDirectWeight)) {
                    throw new IllegalStateException("Weighting should never return NaN values, in:" + this.getCoords(incomingEdges, this.prepareGraph) + ", out:" + this.getCoords(outgoingEdges, this.prepareGraph) + ", dist:" + outgoingEdges.getDistance());
                }
                if (Double.isInfinite(existingDirectWeight)) continue;
                double existingDistSum = v_u_dist + outgoingEdges.getDistance();
                this.prepareAlgo.setWeightLimit(existingDirectWeight);
                this.prepareAlgo.setLimitVisitedNodes((int)this.meanDegree * 100).setEdgeFilter(this.ignoreNodeFilter.setAvoidNode(sch.getNode()));
                this.dijkstraSW.start();
                ++this.dijkstraCount;
                int endNode = this.prepareAlgo.findEndNode(u_fromNode, w_toNode);
                this.dijkstraSW.stop();
                if (endNode == w_toNode && this.prepareAlgo.getWeight(endNode) <= existingDirectWeight) continue;
                sch.foundShortcut(u_fromNode, w_toNode, existingDirectWeight, existingDistSum, outgoingEdges, skippedEdge1, incomingEdgeOrigCount);
            }
        }
        if (sch instanceof AddShortcutHandler) {
            this.meanDegree = (this.meanDegree * 2.0 + (double)tmpDegreeCounter) / 3.0;
        }
    }

    int addShortcuts(int v) {
        this.shortcuts.clear();
        this.findShortcuts(this.addScHandler.setNode(v));
        int tmpNewShortcuts = 0;
        block0 : for (Shortcut sc : this.shortcuts.keySet()) {
            boolean updatedInGraph = false;
            CHEdgeIterator iter = this.vehicleOutExplorer.setBaseNode(sc.from);
            while (iter.next()) {
                if (!iter.isShortcut() || iter.getAdjNode() != sc.to || !iter.canBeOverwritten(sc.flags)) continue;
                if (sc.weight >= this.prepareWeighting.calcWeight(iter, false, -1)) continue block0;
                if (iter.getEdge() == sc.skippedEdge1 || iter.getEdge() == sc.skippedEdge2) {
                    throw new IllegalStateException("Shortcut cannot update itself! " + iter.getEdge() + ", skipEdge1:" + sc.skippedEdge1 + ", skipEdge2:" + sc.skippedEdge2 + ", edge " + iter + ":" + this.getCoords(iter, this.prepareGraph) + ", sc:" + sc + ", skippedEdge1: " + this.getCoords(this.prepareGraph.getEdgeIteratorState(sc.skippedEdge1, sc.from), this.prepareGraph) + ", skippedEdge2: " + this.getCoords(this.prepareGraph.getEdgeIteratorState(sc.skippedEdge2, sc.to), this.prepareGraph) + ", neighbors:" + GHUtility.getNeighbors(iter));
                }
                iter.setFlags(sc.flags);
                iter.setWeight(sc.weight);
                iter.setDistance(sc.dist);
                iter.setSkippedEdges(sc.skippedEdge1, sc.skippedEdge2);
                this.setOrigEdgeCount(iter.getEdge(), sc.originalEdges);
                updatedInGraph = true;
                break;
            }
            if (updatedInGraph) continue;
            CHEdgeIteratorState edgeState = this.prepareGraph.shortcut(sc.from, sc.to);
            edgeState.setFlags(sc.flags);
            edgeState.setWeight(sc.weight);
            edgeState.setDistance(sc.dist);
            edgeState.setSkippedEdges(sc.skippedEdge1, sc.skippedEdge2);
            this.setOrigEdgeCount(edgeState.getEdge(), sc.originalEdges);
            ++tmpNewShortcuts;
        }
        return tmpNewShortcuts;
    }

    String getCoords(EdgeIteratorState e, Graph g) {
        NodeAccess na = g.getNodeAccess();
        int base = e.getBaseNode();
        int adj = e.getAdjNode();
        return "" + base + "->" + adj + " (" + e.getEdge() + "); " + na.getLat(base) + "," + na.getLon(base) + " -> " + na.getLat(adj) + "," + na.getLon(adj);
    }

    PrepareContractionHierarchies initFromGraph() {
        this.ghStorage.freeze();
        this.maxEdgesCount = this.ghStorage.getAllEdges().getMaxId();
        this.vehicleInExplorer = this.prepareGraph.createEdgeExplorer(new DefaultEdgeFilter(this.prepareFlagEncoder, true, false));
        this.vehicleOutExplorer = this.prepareGraph.createEdgeExplorer(new DefaultEdgeFilter(this.prepareFlagEncoder, false, true));
        final DefaultEdgeFilter allFilter = new DefaultEdgeFilter(this.prepareFlagEncoder, true, true);
        LevelEdgeFilter accessWithLevelFilter = new LevelEdgeFilter(this.prepareGraph){

            @Override
            public final boolean accept(EdgeIteratorState edgeState) {
                if (!super.accept(edgeState)) {
                    return false;
                }
                return allFilter.accept(edgeState);
            }
        };
        this.maxLevel = this.prepareGraph.getNodes() + 1;
        this.ignoreNodeFilter = new IgnoreNodeFilter(this.prepareGraph, this.maxLevel);
        this.vehicleAllExplorer = this.prepareGraph.createEdgeExplorer(allFilter);
        this.vehicleAllTmpExplorer = this.prepareGraph.createEdgeExplorer(allFilter);
        this.calcPrioAllExplorer = this.prepareGraph.createEdgeExplorer(accessWithLevelFilter);
        this.sortedNodes = new GHTreeMapComposed();
        this.oldPriorities = new int[this.prepareGraph.getNodes()];
        this.prepareAlgo = new DijkstraOneToMany(this.prepareGraph, this.prepareFlagEncoder, this.prepareWeighting, this.traversalMode);
        return this;
    }

    public int getShortcuts() {
        return this.newShortcuts;
    }

    private void setOrigEdgeCount(int edgeId, int value) {
        if ((edgeId -= this.maxEdgesCount) < 0) {
            if (value != 1) {
                throw new IllegalStateException("Trying to set original edge count for normal edge to a value = " + value + ", edge:" + (edgeId + this.maxEdgesCount) + ", max:" + this.maxEdgesCount + ", graph.max:" + this.ghStorage.getAllEdges().getMaxId());
            }
            return;
        }
        long tmp = (long)edgeId * 4;
        this.originalEdges.ensureCapacity(tmp + 4);
        this.originalEdges.setInt(tmp, value);
    }

    private int getOrigEdgeCount(int edgeId) {
        if ((edgeId -= this.maxEdgesCount) < 0) {
            return 1;
        }
        long tmp = (long)edgeId * 4;
        this.originalEdges.ensureCapacity(tmp + 4);
        return this.originalEdges.getInt(tmp);
    }

    @Override
    public RoutingAlgorithm createAlgo(Graph graph, AlgorithmOptions opts) {
        AbstractBidirAlgo algo2;
        if ("astarbi".equals(opts.getAlgorithm())) {
            AStarBidirection astarBi;
            algo2 = astarBi = new AStarBidirection(graph, this.prepareFlagEncoder, this.prepareWeighting, this.traversalMode){

                @Override
                protected void initCollections(int nodes) {
                    super.initCollections(Math.min(PrepareContractionHierarchies.this.initialCollectionSize, nodes));
                }

                @Override
                protected boolean finished() {
                    if (this.finishedFrom && this.finishedTo) {
                        return true;
                    }
                    return this.currFrom.weight >= this.bestPath.getWeight() && this.currTo.weight >= this.bestPath.getWeight();
                }

                @Override
                protected boolean isWeightLimitExceeded() {
                    return this.currFrom.weight > this.weightLimit && this.currTo.weight > this.weightLimit;
                }

                @Override
                protected Path createAndInitPath() {
                    this.bestPath = new Path4CH(this.graph, this.graph.getBaseGraph(), this.flagEncoder);
                    return this.bestPath;
                }

                @Override
                public String getName() {
                    return "astarbiCH";
                }

                @Override
                public String toString() {
                    return this.getName() + "|" + PrepareContractionHierarchies.this.prepareWeighting;
                }
            };
        } else if ("dijkstrabi".equals(opts.getAlgorithm())) {
            algo2 = new DijkstraBidirectionRef(graph, this.prepareFlagEncoder, this.prepareWeighting, this.traversalMode){

                @Override
                protected void initCollections(int nodes) {
                    super.initCollections(Math.min(PrepareContractionHierarchies.this.initialCollectionSize, nodes));
                }

                @Override
                public boolean finished() {
                    if (this.finishedFrom && this.finishedTo) {
                        return true;
                    }
                    return this.currFrom.weight >= this.bestPath.getWeight() && this.currTo.weight >= this.bestPath.getWeight();
                }

                @Override
                protected boolean isWeightLimitExceeded() {
                    return this.currFrom.weight > this.weightLimit && this.currTo.weight > this.weightLimit;
                }

                @Override
                protected Path createAndInitPath() {
                    this.bestPath = new Path4CH(this.graph, this.graph.getBaseGraph(), this.flagEncoder);
                    return this.bestPath;
                }

                @Override
                public String getName() {
                    return "dijkstrabiCH";
                }

                @Override
                public String toString() {
                    return this.getName() + "|" + PrepareContractionHierarchies.this.prepareWeighting;
                }
            };
        } else {
            throw new UnsupportedOperationException("Algorithm " + opts.getAlgorithm() + " not supported for Contraction Hierarchies");
        }
        algo2.setEdgeFilter(this.levelFilter);
        return algo2;
    }

    public String toString() {
        return "PREPARE|CH|dijkstrabi";
    }

    class Shortcut {
        int from;
        int to;
        int skippedEdge1;
        int skippedEdge2;
        double dist;
        double weight;
        int originalEdges;
        long flags;

        public Shortcut(int from, int to, double weight, double dist) {
            this.flags = PrepareEncoder.getScFwdDir();
            this.from = from;
            this.to = to;
            this.weight = weight;
            this.dist = dist;
        }

        public int hashCode() {
            int hash = 5;
            hash = 23 * hash + this.from;
            hash = 23 * hash + this.to;
            return 23 * hash + (int)(Double.doubleToLongBits(this.weight) ^ Double.doubleToLongBits(this.weight) >>> 32);
        }

        public boolean equals(Object obj) {
            if (obj == null || this.getClass() != obj.getClass()) {
                return false;
            }
            Shortcut other = (Shortcut)obj;
            if (this.from != other.from || this.to != other.to) {
                return false;
            }
            return Double.doubleToLongBits(this.weight) == Double.doubleToLongBits(other.weight);
        }

        public String toString() {
            String str = this.flags == PrepareEncoder.getScDirMask() ? "" + this.from + "<->" : "" + this.from + "->";
            return str + this.to + ", weight:" + this.weight + " (" + this.skippedEdge1 + "," + this.skippedEdge2 + ")";
        }
    }

    private static class PriorityNode
    implements Comparable<PriorityNode> {
        int node;
        int priority;

        public PriorityNode(int node, int priority) {
            this.node = node;
            this.priority = priority;
        }

        public String toString() {
            return "" + this.node + " (" + this.priority + ")";
        }

        @Override
        public int compareTo(PriorityNode o) {
            return this.priority - o.priority;
        }
    }

    static class IgnoreNodeFilter
    implements EdgeFilter {
        int avoidNode;
        int maxLevel;
        CHGraph graph;

        public IgnoreNodeFilter(CHGraph g, int maxLevel) {
            this.graph = g;
            this.maxLevel = maxLevel;
        }

        public IgnoreNodeFilter setAvoidNode(int node) {
            this.avoidNode = node;
            return this;
        }

        @Override
        public final boolean accept(EdgeIteratorState iter) {
            int node = iter.getAdjNode();
            return this.avoidNode != node && this.graph.getLevel(node) == this.maxLevel;
        }
    }

    class AddShortcutHandler
    implements ShortcutHandler {
        int node;

        @Override
        public int getNode() {
            return this.node;
        }

        public AddShortcutHandler setNode(int n) {
            PrepareContractionHierarchies.this.shortcuts.clear();
            this.node = n;
            return this;
        }

        @Override
        public void foundShortcut(int u_fromNode, int w_toNode, double existingDirectWeight, double existingDistSum, EdgeIterator outgoingEdges, int skippedEdge1, int incomingEdgeOrigCount) {
            Shortcut sc = new Shortcut(u_fromNode, w_toNode, existingDirectWeight, existingDistSum);
            if (PrepareContractionHierarchies.this.shortcuts.containsKey(sc)) {
                return;
            }
            Shortcut tmpSc = new Shortcut(w_toNode, u_fromNode, existingDirectWeight, existingDistSum);
            Shortcut tmpRetSc = (Shortcut)PrepareContractionHierarchies.this.shortcuts.get(tmpSc);
            if (tmpRetSc != null && tmpRetSc.skippedEdge2 == skippedEdge1 && tmpRetSc.skippedEdge1 == outgoingEdges.getEdge()) {
                tmpRetSc.flags = PrepareEncoder.getScDirMask();
                return;
            }
            PrepareContractionHierarchies.this.shortcuts.put(sc, sc);
            sc.skippedEdge1 = skippedEdge1;
            sc.skippedEdge2 = outgoingEdges.getEdge();
            sc.originalEdges = incomingEdgeOrigCount + PrepareContractionHierarchies.this.getOrigEdgeCount(outgoingEdges.getEdge());
        }
    }

    class CalcShortcutHandler
    implements ShortcutHandler {
        int node;
        int originalEdgesCount;
        int shortcuts;

        CalcShortcutHandler() {
        }

        public CalcShortcutHandler setNode(int n) {
            this.node = n;
            this.originalEdgesCount = 0;
            this.shortcuts = 0;
            return this;
        }

        @Override
        public int getNode() {
            return this.node;
        }

        @Override
        public void foundShortcut(int u_fromNode, int w_toNode, double existingDirectWeight, double distance, EdgeIterator outgoingEdges, int skippedEdge1, int incomingEdgeOrigCount) {
            ++this.shortcuts;
            this.originalEdgesCount += incomingEdgeOrigCount + PrepareContractionHierarchies.this.getOrigEdgeCount(outgoingEdges.getEdge());
        }
    }

    static interface ShortcutHandler {
        public void foundShortcut(int var1, int var2, double var3, double var5, EdgeIterator var7, int var8, int var9);

        public int getNode();
    }

}

