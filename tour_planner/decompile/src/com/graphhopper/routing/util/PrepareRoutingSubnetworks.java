/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing.util;

import com.graphhopper.coll.GHBitSet;
import com.graphhopper.coll.GHBitSetImpl;
import com.graphhopper.routing.util.AllEdgesIterator;
import com.graphhopper.routing.util.DefaultEdgeFilter;
import com.graphhopper.routing.util.EdgeFilter;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.routing.util.TarjansStronglyConnectedComponentsAlgorithm;
import com.graphhopper.storage.GraphHopperStorage;
import com.graphhopper.storage.NodeAccess;
import com.graphhopper.util.BreadthFirstSearch;
import com.graphhopper.util.EdgeExplorer;
import com.graphhopper.util.EdgeIterator;
import com.graphhopper.util.EdgeIteratorState;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrepareRoutingSubnetworks {
    private final Logger logger;
    private final GraphHopperStorage ghStorage;
    private int minNetworkSize;
    private int minOneWayNetworkSize;
    private int subnetworks;
    private final AtomicInteger maxEdgesPerNode;
    private final List<FlagEncoder> encoders;

    public PrepareRoutingSubnetworks(GraphHopperStorage ghStorage, Collection<FlagEncoder> encoders) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.minNetworkSize = 200;
        this.minOneWayNetworkSize = 0;
        this.subnetworks = -1;
        this.maxEdgesPerNode = new AtomicInteger(0);
        this.ghStorage = ghStorage;
        this.encoders = new ArrayList<FlagEncoder>(encoders);
    }

    public PrepareRoutingSubnetworks setMinNetworkSize(int minNetworkSize) {
        this.minNetworkSize = minNetworkSize;
        return this;
    }

    public PrepareRoutingSubnetworks setMinOneWayNetworkSize(int minOnewayNetworkSize) {
        this.minOneWayNetworkSize = minOnewayNetworkSize;
        return this;
    }

    public void doWork() {
        if (this.minNetworkSize <= 0 && this.minOneWayNetworkSize <= 0) {
            return;
        }
        int unvisitedDeadEnds = 0;
        for (FlagEncoder encoder : this.encoders) {
            PrepEdgeFilter filter = new PrepEdgeFilter(encoder);
            if (this.minOneWayNetworkSize > 0) {
                unvisitedDeadEnds += this.removeDeadEndUnvisitedNetworks(filter);
            }
            List<TIntArrayList> components = this.findSubnetworks(filter);
            this.keepLargeNetworks(filter, components);
            this.subnetworks = Math.max(components.size(), this.subnetworks);
        }
        this.markNodesRemovedIfUnreachable();
        this.logger.info("optimize to remove subnetworks (" + this.subnetworks + "), " + "unvisited-dead-end-nodes (" + unvisitedDeadEnds + "), " + "maxEdges/node (" + this.maxEdgesPerNode.get() + ")");
        this.ghStorage.optimize();
    }

    public int getMaxSubnetworks() {
        return this.subnetworks;
    }

    List<TIntArrayList> findSubnetworks(PrepEdgeFilter filter) {
        final FlagEncoder encoder = filter.getEncoder();
        EdgeExplorer explorer = this.ghStorage.createEdgeExplorer(filter);
        int locs = this.ghStorage.getNodes();
        ArrayList<TIntArrayList> list = new ArrayList<TIntArrayList>(100);
        final GHBitSetImpl bs = new GHBitSetImpl(locs);
        for (int start = 0; start < locs; ++start) {
            if (bs.contains(start)) continue;
            final TIntArrayList intList = new TIntArrayList(20);
            list.add(intList);
            new BreadthFirstSearch(){
                int tmpCounter;

                @Override
                protected GHBitSet createBitSet() {
                    return bs;
                }

                @Override
                protected final boolean goFurther(int nodeId) {
                    if (this.tmpCounter > PrepareRoutingSubnetworks.this.maxEdgesPerNode.get()) {
                        PrepareRoutingSubnetworks.this.maxEdgesPerNode.set(this.tmpCounter);
                    }
                    this.tmpCounter = 0;
                    intList.add(nodeId);
                    return true;
                }

                @Override
                protected final boolean checkAdjacent(EdgeIteratorState edge) {
                    if (encoder.isForward(edge.getFlags()) || encoder.isBackward(edge.getFlags())) {
                        ++this.tmpCounter;
                        return true;
                    }
                    return false;
                }
            }.start(explorer, start);
            intList.trimToSize();
        }
        return list;
    }

    int keepLargeNetworks(PrepEdgeFilter filter, List<TIntArrayList> components) {
        if (components.size() <= 1) {
            return 0;
        }
        int maxCount = -1;
        TIntArrayList oldComponent = null;
        int allRemoved = 0;
        FlagEncoder encoder = filter.getEncoder();
        EdgeExplorer explorer = this.ghStorage.createEdgeExplorer(filter);
        for (TIntArrayList component : components) {
            int removedEdges;
            if (maxCount < 0) {
                maxCount = component.size();
                oldComponent = component;
                continue;
            }
            if (maxCount < component.size()) {
                removedEdges = this.removeEdges(explorer, encoder, oldComponent, this.minNetworkSize);
                maxCount = component.size();
                oldComponent = component;
            } else {
                removedEdges = this.removeEdges(explorer, encoder, component, this.minNetworkSize);
            }
            allRemoved += removedEdges;
        }
        if (allRemoved > this.ghStorage.getAllEdges().getMaxId() / 2) {
            throw new IllegalStateException("Too many total edges were removed: " + allRemoved + ", all edges:" + this.ghStorage.getAllEdges().getMaxId());
        }
        return allRemoved;
    }

    String toString(FlagEncoder encoder, EdgeIterator iter) {
        String str = "";
        while (iter.next()) {
            int adjNode = iter.getAdjNode();
            str = str + adjNode + " (" + this.ghStorage.getNodeAccess().getLat(adjNode) + "," + this.ghStorage.getNodeAccess().getLon(adjNode) + "), ";
            str = str + "speed  (fwd:" + encoder.getSpeed(iter.getFlags()) + ", rev:" + encoder.getReverseSpeed(iter.getFlags()) + "), ";
            str = str + "access (fwd:" + encoder.isForward(iter.getFlags()) + ", rev:" + encoder.isBackward(iter.getFlags()) + "), ";
            str = str + "distance:" + iter.getDistance();
            str = str + ";\n ";
        }
        return str;
    }

    int removeDeadEndUnvisitedNetworks(PrepEdgeFilter bothFilter) {
        DefaultEdgeFilter outFilter = new DefaultEdgeFilter(bothFilter.getEncoder(), false, true);
        List<TIntArrayList> components = new TarjansStronglyConnectedComponentsAlgorithm(this.ghStorage, outFilter).findComponents();
        return this.removeEdges(bothFilter, components, this.minOneWayNetworkSize);
    }

    int removeEdges(PrepEdgeFilter bothFilter, List<TIntArrayList> components, int min) {
        FlagEncoder encoder = bothFilter.getEncoder();
        EdgeExplorer explorer = this.ghStorage.createEdgeExplorer(bothFilter);
        int removedEdges = 0;
        for (TIntArrayList component : components) {
            removedEdges += this.removeEdges(explorer, encoder, component, min);
        }
        return removedEdges;
    }

    int removeEdges(EdgeExplorer explorer, FlagEncoder encoder, TIntList component, int min) {
        int removedEdges = 0;
        if (component.size() < min) {
            for (int i = 0; i < component.size(); ++i) {
                EdgeIterator edge = explorer.setBaseNode(component.get(i));
                while (edge.next()) {
                    edge.setFlags(encoder.setAccess(edge.getFlags(), false, false));
                    ++removedEdges;
                }
            }
        }
        return removedEdges;
    }

    void markNodesRemovedIfUnreachable() {
        EdgeExplorer edgeExplorer = this.ghStorage.createEdgeExplorer();
        for (int nodeIndex = 0; nodeIndex < this.ghStorage.getNodes(); ++nodeIndex) {
            if (!this.detectNodeRemovedForAllEncoders(edgeExplorer, nodeIndex)) continue;
            this.ghStorage.markNodeRemoved(nodeIndex);
        }
    }

    boolean detectNodeRemovedForAllEncoders(EdgeExplorer edgeExplorerAllEdges, int nodeIndex) {
        EdgeIterator iter = edgeExplorerAllEdges.setBaseNode(nodeIndex);
        while (iter.next()) {
            for (FlagEncoder encoder : this.encoders) {
                if (!encoder.isBackward(iter.getFlags()) && !encoder.isForward(iter.getFlags())) continue;
                return false;
            }
        }
        return true;
    }

    static class PrepEdgeFilter
    extends DefaultEdgeFilter {
        FlagEncoder encoder;

        public PrepEdgeFilter(FlagEncoder encoder) {
            super(encoder);
            this.encoder = encoder;
        }

        public FlagEncoder getEncoder() {
            return this.encoder;
        }
    }

}

