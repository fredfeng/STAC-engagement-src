/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.storage.index;

import com.graphhopper.routing.util.AllEdgesIterator;
import com.graphhopper.routing.util.EdgeFilter;
import com.graphhopper.storage.Graph;
import com.graphhopper.storage.NodeAccess;
import com.graphhopper.storage.index.LocationIndex;
import com.graphhopper.storage.index.QueryResult;
import com.graphhopper.util.DistanceCalc;
import com.graphhopper.util.DistancePlaneProjection;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.Helper;

public class Location2IDFullWithEdgesIndex
implements LocationIndex {
    private DistanceCalc calc = Helper.DIST_EARTH;
    private final Graph graph;
    private final NodeAccess nodeAccess;
    private boolean closed = false;

    public Location2IDFullWithEdgesIndex(Graph g) {
        this.graph = g;
        this.nodeAccess = g.getNodeAccess();
    }

    @Override
    public boolean loadExisting() {
        return true;
    }

    @Override
    public LocationIndex setResolution(int resolution) {
        return this;
    }

    @Override
    public LocationIndex setApproximation(boolean approxDist) {
        this.calc = approxDist ? Helper.DIST_PLANE : Helper.DIST_EARTH;
        return this;
    }

    @Override
    public LocationIndex prepareIndex() {
        return this;
    }

    @Override
    public int findID(double lat, double lon) {
        return this.findClosest(lat, lon, EdgeFilter.ALL_EDGES).getClosestNode();
    }

    @Override
    public QueryResult findClosest(double queryLat, double queryLon, EdgeFilter filter) {
        if (this.isClosed()) {
            throw new IllegalStateException("You need to create a new LocationIndex instance as it is already closed");
        }
        QueryResult res = new QueryResult(queryLat, queryLon);
        double foundDist = Double.MAX_VALUE;
        AllEdgesIterator iter = this.graph.getAllEdges();
        while (iter.next()) {
            if (!filter.accept(iter)) continue;
            for (int i = 0; i < 2; ++i) {
                int toNode;
                double distEdge;
                double fromLon;
                double toLon;
                double toLat;
                int node = i == 0 ? iter.getBaseNode() : iter.getAdjNode();
                double fromLat = this.nodeAccess.getLatitude(node);
                double fromDist = this.calc.calcDist(fromLat, fromLon = this.nodeAccess.getLongitude(node), queryLat, queryLon);
                if (fromDist < 0.0) continue;
                if (fromDist < foundDist) {
                    res.setQueryDistance(fromDist);
                    res.setClosestEdge(iter.detach(false));
                    res.setClosestNode(node);
                    foundDist = fromDist;
                }
                if (i > 0 || !this.calc.validEdgeDistance(queryLat, queryLon, fromLat, fromLon, toLat = this.nodeAccess.getLatitude(toNode = iter.getAdjNode()), toLon = this.nodeAccess.getLongitude(toNode)) || (distEdge = this.calc.calcDenormalizedDist(this.calc.calcNormalizedEdgeDistance(queryLat, queryLon, fromLat, fromLon, toLat, toLon))) >= foundDist) continue;
                res.setQueryDistance(distEdge);
                res.setClosestNode(node);
                res.setClosestEdge(iter);
                if (fromDist > this.calc.calcDist(toLat, toLon, queryLat, queryLon)) {
                    res.setClosestNode(toNode);
                }
                foundDist = distEdge;
            }
        }
        return res;
    }

    @Override
    public LocationIndex create(long size) {
        return this;
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() {
        this.closed = true;
    }

    @Override
    public boolean isClosed() {
        return this.closed;
    }

    @Override
    public long getCapacity() {
        return 0;
    }

    @Override
    public void setSegmentSize(int bytes) {
    }
}

