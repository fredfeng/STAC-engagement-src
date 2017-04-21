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
import com.graphhopper.util.shapes.Circle;

public class Location2IDFullIndex
implements LocationIndex {
    private DistanceCalc calc = Helper.DIST_PLANE;
    private final Graph graph;
    private final NodeAccess nodeAccess;
    private boolean closed = false;

    public Location2IDFullIndex(Graph g) {
        this.graph = g;
        this.nodeAccess = g.getNodeAccess();
    }

    @Override
    public boolean loadExisting() {
        return true;
    }

    @Override
    public LocationIndex setApproximation(boolean approxDist) {
        this.calc = approxDist ? Helper.DIST_PLANE : Helper.DIST_EARTH;
        return this;
    }

    @Override
    public LocationIndex setResolution(int resolution) {
        return this;
    }

    @Override
    public LocationIndex prepareIndex() {
        return this;
    }

    @Override
    public QueryResult findClosest(double queryLat, double queryLon, EdgeFilter edgeFilter) {
        if (this.isClosed()) {
            throw new IllegalStateException("You need to create a new LocationIndex instance as it is already closed");
        }
        QueryResult res = new QueryResult(queryLat, queryLon);
        Circle circle = null;
        AllEdgesIterator iter = this.graph.getAllEdges();
        block0 : while (iter.next()) {
            if (!edgeFilter.accept(iter)) continue;
            for (int i = 0; i < 2; ++i) {
                int node = i == 0 ? iter.getBaseNode() : iter.getAdjNode();
                double tmpLat = this.nodeAccess.getLatitude(node);
                double tmpLon = this.nodeAccess.getLongitude(node);
                double dist = this.calc.calcDist(tmpLat, tmpLon, queryLat, queryLon);
                if (circle != null && dist >= this.calc.calcDist(circle.getLat(), circle.getLon(), queryLat, queryLon)) continue;
                res.setClosestEdge(iter.detach(false));
                res.setClosestNode(node);
                res.setQueryDistance(dist);
                if (dist <= 0.0) continue block0;
                circle = new Circle(tmpLat, tmpLon, dist, this.calc);
            }
        }
        return res;
    }

    @Override
    public int findID(double lat, double lon) {
        return this.findClosest(lat, lon, EdgeFilter.ALL_EDGES).getClosestNode();
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

