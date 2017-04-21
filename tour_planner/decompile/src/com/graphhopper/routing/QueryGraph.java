/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing;

import com.graphhopper.routing.VirtualEdgeIterator;
import com.graphhopper.routing.VirtualEdgeIteratorState;
import com.graphhopper.routing.util.AllEdgesIterator;
import com.graphhopper.routing.util.EdgeFilter;
import com.graphhopper.storage.Graph;
import com.graphhopper.storage.GraphExtension;
import com.graphhopper.storage.NodeAccess;
import com.graphhopper.storage.TurnCostExtension;
import com.graphhopper.storage.index.QueryResult;
import com.graphhopper.util.AngleCalc;
import com.graphhopper.util.DistanceCalc;
import com.graphhopper.util.DistancePlaneProjection;
import com.graphhopper.util.EdgeExplorer;
import com.graphhopper.util.EdgeIterator;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.GHUtility;
import com.graphhopper.util.Helper;
import com.graphhopper.util.PointAccess;
import com.graphhopper.util.PointList;
import com.graphhopper.util.shapes.BBox;
import com.graphhopper.util.shapes.GHPoint;
import com.graphhopper.util.shapes.GHPoint3D;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.hash.TIntHashSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class QueryGraph
implements Graph {
    private final Graph mainGraph;
    private final NodeAccess mainNodeAccess;
    private final int mainNodes;
    private final int mainEdges;
    private final QueryGraph baseGraph;
    private final GraphExtension wrappedExtension;
    private List<QueryResult> queryResults;
    List<VirtualEdgeIteratorState> virtualEdges;
    static final int VE_BASE = 0;
    static final int VE_BASE_REV = 1;
    static final int VE_ADJ = 2;
    static final int VE_ADJ_REV = 3;
    private PointList virtualNodes;
    private static final AngleCalc ac = new AngleCalc();
    private List<VirtualEdgeIteratorState> modifiedEdges = new ArrayList<VirtualEdgeIteratorState>(5);
    private final NodeAccess nodeAccess  = new NodeAccess(){

        @Override
        public void ensureNode(int nodeId) {
            QueryGraph.this.mainNodeAccess.ensureNode(nodeId);
        }

        @Override
        public boolean is3D() {
            return QueryGraph.this.mainNodeAccess.is3D();
        }

        @Override
        public int getDimension() {
            return QueryGraph.this.mainNodeAccess.getDimension();
        }

        @Override
        public double getLatitude(int nodeId) {
            if (QueryGraph.this.isVirtualNode(nodeId)) {
                return QueryGraph.this.virtualNodes.getLatitude(nodeId - QueryGraph.this.mainNodes);
            }
            return QueryGraph.this.mainNodeAccess.getLatitude(nodeId);
        }

        @Override
        public double getLongitude(int nodeId) {
            if (QueryGraph.this.isVirtualNode(nodeId)) {
                return QueryGraph.this.virtualNodes.getLongitude(nodeId - QueryGraph.this.mainNodes);
            }
            return QueryGraph.this.mainNodeAccess.getLongitude(nodeId);
        }

        @Override
        public double getElevation(int nodeId) {
            if (QueryGraph.this.isVirtualNode(nodeId)) {
                return QueryGraph.this.virtualNodes.getElevation(nodeId - QueryGraph.this.mainNodes);
            }
            return QueryGraph.this.mainNodeAccess.getElevation(nodeId);
        }

        @Override
        public int getAdditionalNodeField(int nodeId) {
            if (QueryGraph.this.isVirtualNode(nodeId)) {
                return 0;
            }
            return QueryGraph.this.mainNodeAccess.getAdditionalNodeField(nodeId);
        }

        @Override
        public void setNode(int nodeId, double lat, double lon) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void setNode(int nodeId, double lat, double lon, double ele) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void setAdditionalNodeField(int nodeId, int additionalValue) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public double getLat(int nodeId) {
            return this.getLatitude(nodeId);
        }

        @Override
        public double getLon(int nodeId) {
            return this.getLongitude(nodeId);
        }

        @Override
        public double getEle(int nodeId) {
            return this.getElevation(nodeId);
        }
    };

    public QueryGraph(Graph graph) {
        this.mainGraph = graph;
        this.mainNodeAccess = graph.getNodeAccess();
        this.mainNodes = graph.getNodes();
        this.mainEdges = graph.getAllEdges().getMaxId();
        this.wrappedExtension = this.mainGraph.getExtension() instanceof TurnCostExtension ? new QueryGraphTurnExt(this) : this.mainGraph.getExtension();
        this.baseGraph = new QueryGraph(graph.getBaseGraph(), this);
    }

    private QueryGraph(Graph graph, QueryGraph superQueryGraph) {
        this.mainGraph = graph;
        this.baseGraph = this;
        this.wrappedExtension = superQueryGraph.wrappedExtension;
        this.mainNodeAccess = graph.getNodeAccess();
        this.mainNodes = superQueryGraph.mainNodes;
        this.mainEdges = superQueryGraph.mainEdges;
    }

    public QueryGraph lookup(QueryResult fromRes, QueryResult toRes) {
        ArrayList<QueryResult> results = new ArrayList<QueryResult>(2);
        results.add(fromRes);
        results.add(toRes);
        this.lookup(results);
        return this;
    }

    public void lookup(List<QueryResult> resList) {
        if (this.isInitialized()) {
            throw new IllegalStateException("Call lookup only once. Otherwise you'll have problems for queries sharing the same edge.");
        }
        this.virtualEdges = new ArrayList<VirtualEdgeIteratorState>(resList.size() * 2);
        this.virtualNodes = new PointList(resList.size(), this.mainNodeAccess.is3D());
        this.queryResults = new ArrayList<QueryResult>(resList.size());
        this.baseGraph.virtualEdges = this.virtualEdges;
        this.baseGraph.virtualNodes = this.virtualNodes;
        this.baseGraph.queryResults = this.queryResults;
        TIntObjectHashMap<ArrayList<QueryResult>> edge2res = new TIntObjectHashMap<ArrayList<QueryResult>>(resList.size());
        for (QueryResult res : resList) {
            ArrayList<QueryResult> list;
            int edgeId;
            boolean doReverse;
            PointList pl;
            if (res.getSnappedPosition() == QueryResult.Position.TOWER) continue;
            EdgeIteratorState closestEdge = res.getClosestEdge();
            if (closestEdge == null) {
                throw new IllegalStateException("Do not call QueryGraph.lookup with invalid QueryResult " + res);
            }
            int base = closestEdge.getBaseNode();
            boolean bl = doReverse = base > closestEdge.getAdjNode();
            if (base == closestEdge.getAdjNode() && (pl = closestEdge.fetchWayGeometry(0)).size() > 1) {
                boolean bl2 = doReverse = pl.getLatitude(0) > pl.getLatitude(pl.size() - 1);
            }
            if (doReverse) {
                closestEdge = closestEdge.detach(true);
                PointList fullPL = closestEdge.fetchWayGeometry(3);
                res.setClosestEdge(closestEdge);
                if (res.getSnappedPosition() == QueryResult.Position.PILLAR) {
                    res.setWayIndex(fullPL.getSize() - res.getWayIndex() - 1);
                } else {
                    res.setWayIndex(fullPL.getSize() - res.getWayIndex() - 2);
                }
                if (res.getWayIndex() < 0) {
                    throw new IllegalStateException("Problem with wayIndex while reversing closest edge:" + closestEdge + ", " + res);
                }
            }
            if ((list = (ArrayList<QueryResult>)edge2res.get(edgeId = closestEdge.getEdge())) == null) {
                list = new ArrayList<QueryResult>(5);
                edge2res.put(edgeId, list);
            }
            list.add(res);
        }
        edge2res.forEachValue(new TObjectProcedure<List<QueryResult>>(){

            @Override
            public boolean execute(List<QueryResult> results) {
                EdgeIteratorState closestEdge = results.get(0).getClosestEdge();
                final PointList fullPL = closestEdge.fetchWayGeometry(3);
                int baseNode = closestEdge.getBaseNode();
                Collections.sort(results, new Comparator<QueryResult>(){

                    @Override
                    public int compare(QueryResult o1, QueryResult o2) {
                        int diff = o1.getWayIndex() - o2.getWayIndex();
                        if (diff == 0) {
                            GHPoint3D p2;
                            double fromLon;
                            GHPoint3D p1 = o1.getSnappedPoint();
                            if (p1.equals(p2 = o2.getSnappedPoint())) {
                                return 0;
                            }
                            double fromLat = fullPL.getLatitude(o1.getWayIndex());
                            if (Helper.DIST_PLANE.calcNormalizedDist(fromLat, fromLon = fullPL.getLongitude(o1.getWayIndex()), p1.lat, p1.lon) > Helper.DIST_PLANE.calcNormalizedDist(fromLat, fromLon, p2.lat, p2.lon)) {
                                return 1;
                            }
                            return -1;
                        }
                        return diff;
                    }
                });
                GHPoint3D prevPoint = fullPL.toGHPoint(0);
                int adjNode = closestEdge.getAdjNode();
                int origTraversalKey = GHUtility.createEdgeKey(baseNode, adjNode, closestEdge.getEdge(), false);
                int origRevTraversalKey = GHUtility.createEdgeKey(baseNode, adjNode, closestEdge.getEdge(), true);
                long reverseFlags = closestEdge.detach(true).getFlags();
                int prevWayIndex = 1;
                int prevNodeId = baseNode;
                int virtNodeId = QueryGraph.this.virtualNodes.getSize() + QueryGraph.this.mainNodes;
                boolean addedEdges = false;
                for (int counter = 0; counter < results.size(); ++counter) {
                    QueryResult res = results.get(counter);
                    if (res.getClosestEdge().getBaseNode() != baseNode) {
                        throw new IllegalStateException("Base nodes have to be identical but were not: " + closestEdge + " vs " + res.getClosestEdge());
                    }
                    GHPoint3D currSnapped = res.getSnappedPoint();
                    if (prevPoint.equals(currSnapped)) {
                        res.setClosestNode(prevNodeId);
                        continue;
                    }
                    QueryGraph.this.queryResults.add(res);
                    QueryGraph.this.createEdges(origTraversalKey, origRevTraversalKey, prevPoint, prevWayIndex, res.getSnappedPoint(), res.getWayIndex(), fullPL, closestEdge, prevNodeId, virtNodeId, reverseFlags);
                    QueryGraph.this.virtualNodes.add(currSnapped.lat, currSnapped.lon, currSnapped.ele);
                    if (addedEdges) {
                        QueryGraph.this.virtualEdges.add(QueryGraph.this.virtualEdges.get(QueryGraph.this.virtualEdges.size() - 2));
                        QueryGraph.this.virtualEdges.add(QueryGraph.this.virtualEdges.get(QueryGraph.this.virtualEdges.size() - 2));
                    }
                    addedEdges = true;
                    res.setClosestNode(virtNodeId);
                    prevNodeId = virtNodeId++;
                    prevWayIndex = res.getWayIndex() + 1;
                    prevPoint = currSnapped;
                }
                if (addedEdges) {
                    QueryGraph.this.createEdges(origTraversalKey, origRevTraversalKey, prevPoint, prevWayIndex, fullPL.toGHPoint(fullPL.getSize() - 1), fullPL.getSize() - 2, fullPL, closestEdge, virtNodeId - 1, adjNode, reverseFlags);
                }
                return true;
            }

        });
    }

    @Override
    public Graph getBaseGraph() {
        return this.baseGraph;
    }

    public boolean isVirtualEdge(int edgeId) {
        return edgeId >= this.mainEdges;
    }

    public boolean isVirtualNode(int nodeId) {
        return nodeId >= this.mainNodes;
    }

    private void createEdges(int origTraversalKey, int origRevTraversalKey, GHPoint3D prevSnapped, int prevWayIndex, GHPoint3D currSnapped, int wayIndex, PointList fullPL, EdgeIteratorState closestEdge, int prevNodeId, int nodeId, long reverseFlags) {
        int max = wayIndex + 1;
        PointList basePoints = new PointList(max - prevWayIndex + 1, this.mainNodeAccess.is3D());
        basePoints.add(prevSnapped.lat, prevSnapped.lon, prevSnapped.ele);
        for (int i = prevWayIndex; i < max; ++i) {
            basePoints.add(fullPL, i);
        }
        basePoints.add(currSnapped.lat, currSnapped.lon, currSnapped.ele);
        PointList baseReversePoints = basePoints.clone(true);
        double baseDistance = basePoints.calcDistance(Helper.DIST_PLANE);
        int virtEdgeId = this.mainEdges + this.virtualEdges.size();
        VirtualEdgeIteratorState baseEdge = new VirtualEdgeIteratorState(origTraversalKey, virtEdgeId, prevNodeId, nodeId, baseDistance, closestEdge.getFlags(), closestEdge.getName(), basePoints);
        VirtualEdgeIteratorState baseReverseEdge = new VirtualEdgeIteratorState(origRevTraversalKey, virtEdgeId, nodeId, prevNodeId, baseDistance, reverseFlags, closestEdge.getName(), baseReversePoints);
        this.virtualEdges.add(baseEdge);
        this.virtualEdges.add(baseReverseEdge);
    }

    public boolean enforceHeading(int nodeId, double favoredHeading, boolean incoming) {
        if (!this.isInitialized()) {
            throw new IllegalStateException("QueryGraph.lookup has to be called in before heading enforcement");
        }
        if (Double.isNaN(favoredHeading)) {
            return false;
        }
        if (!this.isVirtualNode(nodeId)) {
            return false;
        }
        int virtNodeIDintern = nodeId - this.mainNodes;
        favoredHeading = ac.convertAzimuth2xaxisAngle(favoredHeading);
        List<Integer> edgePositions = incoming ? Arrays.asList(0, 3) : Arrays.asList(1, 2);
        boolean enforcementOccured = false;
        Iterator<Integer> i$ = edgePositions.iterator();
        while (i$.hasNext()) {
            double delta;
            double edgeOrientation;
            int edgePos = i$.next();
            VirtualEdgeIteratorState edge = this.virtualEdges.get(virtNodeIDintern * 4 + edgePos);
            PointList wayGeo = edge.fetchWayGeometry(3);
            if (incoming) {
                int numWayPoints = wayGeo.getSize();
                edgeOrientation = ac.calcOrientation(wayGeo.getLat(numWayPoints - 2), wayGeo.getLon(numWayPoints - 2), wayGeo.getLat(numWayPoints - 1), wayGeo.getLon(numWayPoints - 1));
            } else {
                edgeOrientation = ac.calcOrientation(wayGeo.getLat(0), wayGeo.getLon(0), wayGeo.getLat(1), wayGeo.getLon(1));
            }
            if (Math.abs(delta = (edgeOrientation = ac.alignOrientation(favoredHeading, edgeOrientation)) - favoredHeading) <= 1.74) continue;
            edge.setVirtualEdgePreference(true);
            this.modifiedEdges.add(edge);
            VirtualEdgeIteratorState reverseEdge = this.virtualEdges.get(virtNodeIDintern * 4 + this.getPosOfReverseEdge(edgePos));
            reverseEdge.setVirtualEdgePreference(true);
            this.modifiedEdges.add(reverseEdge);
            enforcementOccured = true;
        }
        return enforcementOccured;
    }

    public boolean enforceHeadingByEdgeId(int nodeId, int edgeId, boolean incoming) {
        if (!this.isVirtualNode(nodeId)) {
            return false;
        }
        VirtualEdgeIteratorState incomingEdge = (VirtualEdgeIteratorState)this.getEdgeIteratorState(edgeId, nodeId);
        VirtualEdgeIteratorState reverseEdge = (VirtualEdgeIteratorState)this.getEdgeIteratorState(edgeId, incomingEdge.getBaseNode());
        incomingEdge.setVirtualEdgePreference(true);
        this.modifiedEdges.add(incomingEdge);
        reverseEdge.setVirtualEdgePreference(true);
        this.modifiedEdges.add(reverseEdge);
        return true;
    }

    public void clearUnfavoredStatus() {
        for (VirtualEdgeIteratorState edge : this.modifiedEdges) {
            edge.setVirtualEdgePreference(false);
        }
    }

    @Override
    public int getNodes() {
        return this.virtualNodes.getSize() + this.mainNodes;
    }

    @Override
    public NodeAccess getNodeAccess() {
        return this.nodeAccess;
    }

    @Override
    public BBox getBounds() {
        return this.mainGraph.getBounds();
    }

    @Override
    public EdgeIteratorState getEdgeIteratorState(int origEdgeId, int adjNode) {
        if (!this.isVirtualEdge(origEdgeId)) {
            return this.mainGraph.getEdgeIteratorState(origEdgeId, adjNode);
        }
        int edgeId = origEdgeId - this.mainEdges;
        EdgeIteratorState eis = this.virtualEdges.get(edgeId);
        if (eis.getAdjNode() == adjNode || adjNode == Integer.MIN_VALUE) {
            return eis;
        }
        EdgeIteratorState eis2 = this.virtualEdges.get(edgeId = this.getPosOfReverseEdge(edgeId));
        if (eis2.getAdjNode() == adjNode) {
            return eis2;
        }
        throw new IllegalStateException("Edge " + origEdgeId + " not found with adjNode:" + adjNode + ". found edges were:" + eis + ", " + eis2);
    }

    private int getPosOfReverseEdge(int edgeId) {
        edgeId = edgeId % 2 == 0 ? ++edgeId : --edgeId;
        return edgeId;
    }

    @Override
    public EdgeExplorer createEdgeExplorer(EdgeFilter edgeFilter) {
        if (!this.isInitialized()) {
            throw new IllegalStateException("Call lookup before using this graph");
        }
        final TIntObjectHashMap<VirtualEdgeIterator> node2EdgeMap = new TIntObjectHashMap<VirtualEdgeIterator>(this.queryResults.size() * 3);
        final EdgeExplorer mainExplorer = this.mainGraph.createEdgeExplorer(edgeFilter);
        TIntHashSet towerNodesToChange = new TIntHashSet(this.queryResults.size());
        for (int i = 0; i < this.queryResults.size(); ++i) {
            EdgeIteratorState adjEdge;
            VirtualEdgeIterator virtEdgeIter = new VirtualEdgeIterator(2);
            EdgeIteratorState baseRevEdge = this.virtualEdges.get(i * 4 + 1);
            if (edgeFilter.accept(baseRevEdge)) {
                virtEdgeIter.add(baseRevEdge);
            }
            if (edgeFilter.accept(adjEdge = (EdgeIteratorState)this.virtualEdges.get(i * 4 + 2))) {
                virtEdgeIter.add(adjEdge);
            }
            int virtNode = this.mainNodes + i;
            node2EdgeMap.put(virtNode, virtEdgeIter);
            int towerNode = baseRevEdge.getAdjNode();
            if (!this.isVirtualNode(towerNode)) {
                towerNodesToChange.add(towerNode);
                this.addVirtualEdges(node2EdgeMap, edgeFilter, true, towerNode, i);
            }
            if (this.isVirtualNode(towerNode = adjEdge.getAdjNode())) continue;
            towerNodesToChange.add(towerNode);
            this.addVirtualEdges(node2EdgeMap, edgeFilter, false, towerNode, i);
        }
        towerNodesToChange.forEach(new TIntProcedure(){

            @Override
            public boolean execute(int value) {
                QueryGraph.this.fillVirtualEdges(node2EdgeMap, value, mainExplorer);
                return true;
            }
        });
        return new EdgeExplorer(){

            @Override
            public EdgeIterator setBaseNode(int baseNode) {
                VirtualEdgeIterator iter = (VirtualEdgeIterator)node2EdgeMap.get(baseNode);
                if (iter != null) {
                    return iter.reset();
                }
                return mainExplorer.setBaseNode(baseNode);
            }
        };
    }

    private void addVirtualEdges(TIntObjectMap<VirtualEdgeIterator> node2EdgeMap, EdgeFilter filter, boolean base, int node, int virtNode) {
        VirtualEdgeIteratorState edge;
        VirtualEdgeIterator existingIter = node2EdgeMap.get(node);
        if (existingIter == null) {
            existingIter = new VirtualEdgeIterator(10);
            node2EdgeMap.put(node, existingIter);
        }
        VirtualEdgeIteratorState virtualEdgeIteratorState = edge = base ? this.virtualEdges.get(virtNode * 4 + 0) : this.virtualEdges.get(virtNode * 4 + 3);
        if (filter.accept(edge)) {
            existingIter.add(edge);
        }
    }

    void fillVirtualEdges(TIntObjectMap<VirtualEdgeIterator> node2Edge, int towerNode, EdgeExplorer mainExpl) {
        if (this.isVirtualNode(towerNode)) {
            throw new IllegalStateException("Node should not be virtual:" + towerNode + ", " + node2Edge);
        }
        VirtualEdgeIterator vIter = node2Edge.get(towerNode);
        TIntArrayList ignoreEdges = new TIntArrayList(vIter.count() * 2);
        while (vIter.next()) {
            EdgeIteratorState edge = this.queryResults.get(vIter.getAdjNode() - this.mainNodes).getClosestEdge();
            ignoreEdges.add(edge.getEdge());
        }
        vIter.reset();
        EdgeIterator iter = mainExpl.setBaseNode(towerNode);
        while (iter.next()) {
            if (ignoreEdges.contains(iter.getEdge())) continue;
            vIter.add(iter.detach(false));
        }
    }

    private boolean isInitialized() {
        return this.queryResults != null;
    }

    @Override
    public EdgeExplorer createEdgeExplorer() {
        return this.createEdgeExplorer(EdgeFilter.ALL_EDGES);
    }

    @Override
    public AllEdgesIterator getAllEdges() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public EdgeIteratorState edge(int a, int b) {
        throw this.exc();
    }

    public EdgeIteratorState edge(int a, int b, double distance, int flags) {
        throw this.exc();
    }

    @Override
    public EdgeIteratorState edge(int a, int b, double distance, boolean bothDirections) {
        throw this.exc();
    }

    @Override
    public Graph copyTo(Graph g) {
        throw this.exc();
    }

    @Override
    public GraphExtension getExtension() {
        return this.wrappedExtension;
    }

    private UnsupportedOperationException exc() {
        return new UnsupportedOperationException("QueryGraph cannot be modified.");
    }

    class QueryGraphTurnExt
    extends TurnCostExtension {
        private final TurnCostExtension mainTurnExtension;

        public QueryGraphTurnExt(QueryGraph qGraph) {
            this.mainTurnExtension = (TurnCostExtension)QueryGraph.this.mainGraph.getExtension();
        }

        @Override
        public long getTurnCostFlags(int edgeFrom, int nodeVia, int edgeTo) {
            if (QueryGraph.this.isVirtualNode(nodeVia)) {
                return 0;
            }
            if (QueryGraph.this.isVirtualEdge(edgeFrom) || QueryGraph.this.isVirtualEdge(edgeTo)) {
                if (QueryGraph.this.isVirtualEdge(edgeFrom)) {
                    edgeFrom = ((QueryResult)QueryGraph.this.queryResults.get((edgeFrom - QueryGraph.this.mainEdges) / 4)).getClosestEdge().getEdge();
                }
                if (QueryGraph.this.isVirtualEdge(edgeTo)) {
                    edgeTo = ((QueryResult)QueryGraph.this.queryResults.get((edgeTo - QueryGraph.this.mainEdges) / 4)).getClosestEdge().getEdge();
                }
                return this.mainTurnExtension.getTurnCostFlags(edgeFrom, nodeVia, edgeTo);
            }
            return this.mainTurnExtension.getTurnCostFlags(edgeFrom, nodeVia, edgeTo);
        }
    }

}

