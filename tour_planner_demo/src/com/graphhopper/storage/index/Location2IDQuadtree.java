/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.storage.index;

import com.graphhopper.coll.GHBitSet;
import com.graphhopper.coll.GHBitSetImpl;
import com.graphhopper.coll.GHTBitSet;
import com.graphhopper.geohash.KeyAlgo;
import com.graphhopper.geohash.LinearKeyAlgo;
import com.graphhopper.routing.util.EdgeFilter;
import com.graphhopper.storage.DataAccess;
import com.graphhopper.storage.Directory;
import com.graphhopper.storage.Graph;
import com.graphhopper.storage.NodeAccess;
import com.graphhopper.storage.RAMDirectory;
import com.graphhopper.storage.index.LocationIndex;
import com.graphhopper.storage.index.QueryResult;
import com.graphhopper.util.BreadthFirstSearch;
import com.graphhopper.util.DistanceCalc;
import com.graphhopper.util.DistancePlaneProjection;
import com.graphhopper.util.EdgeExplorer;
import com.graphhopper.util.Helper;
import com.graphhopper.util.StopWatch;
import com.graphhopper.util.shapes.BBox;
import com.graphhopper.util.shapes.GHPoint;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Location2IDQuadtree
implements LocationIndex {
    private static final int MAGIC_INT = 174507;
    private final Logger logger;
    private KeyAlgo keyAlgo;
    protected DistanceCalc distCalc;
    private final DataAccess index;
    private double maxRasterWidth2InMeterNormed;
    private final Graph graph;
    private final NodeAccess nodeAccess;
    private int lonSize;
    private int latSize;

    public Location2IDQuadtree(Graph g, Directory dir) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.distCalc = Helper.DIST_PLANE;
        this.graph = g;
        this.nodeAccess = g.getNodeAccess();
        this.index = dir.find("loc2idIndex");
        this.setResolution(10000);
    }

    @Override
    public LocationIndex setApproximation(boolean approxDist) {
        this.distCalc = approxDist ? Helper.DIST_PLANE : Helper.DIST_EARTH;
        return this;
    }

    @Override
    public long getCapacity() {
        return this.index.getCapacity() / 4;
    }

    @Override
    public boolean loadExisting() {
        if (!this.index.loadExisting()) {
            return false;
        }
        if (this.index.getHeader(0) != 174507) {
            throw new IllegalStateException("incorrect loc2id index version");
        }
        int lat = this.index.getHeader(4);
        int lon = this.index.getHeader(8);
        int checksum = this.index.getHeader(12);
        if (checksum != this.graph.getNodes()) {
            throw new IllegalStateException("index was created from a different graph with " + checksum + ". Current nodes:" + this.graph.getNodes());
        }
        this.initAlgo(lat, lon);
        return true;
    }

    @Override
    public LocationIndex create(long size) {
        throw new UnsupportedOperationException("Not supported. Use prepareIndex instead.");
    }

    @Override
    public LocationIndex setResolution(int resolution) {
        this.initLatLonSize(resolution);
        return this;
    }

    @Override
    public LocationIndex prepareIndex() {
        this.initBuffer();
        this.initAlgo(this.latSize, this.lonSize);
        StopWatch sw = new StopWatch().start();
        GHBitSet filledIndices = this.fillQuadtree(this.latSize * this.lonSize);
        int fillQT = filledIndices.getCardinality();
        float res1 = sw.stop().getSeconds();
        sw = new StopWatch().start();
        int counter = this.fillEmptyIndices(filledIndices);
        float fillEmpty = sw.stop().getSeconds();
        this.logger.info("filled quadtree index array in " + res1 + "s. size is " + this.getCapacity() + " (" + fillQT + "). filled empty " + counter + " in " + fillEmpty + "s");
        this.flush();
        return this;
    }

    private void initLatLonSize(int size) {
        this.latSize = this.lonSize = (int)Math.sqrt(size);
        if (this.latSize * this.lonSize < size) {
            ++this.lonSize;
        }
    }

    private void initBuffer() {
        this.index.setSegmentSize(this.latSize * this.lonSize * 4);
        this.index.create(this.latSize * this.lonSize * 4);
    }

    void initAlgo(int lat, int lon) {
        this.latSize = lat;
        this.lonSize = lon;
        BBox b = this.graph.getBounds();
        this.keyAlgo = new LinearKeyAlgo(lat, lon).setBounds(b);
        double max = Math.max(this.distCalc.calcDist(b.minLat, b.minLon, b.minLat, b.maxLon), this.distCalc.calcDist(b.minLat, b.minLon, b.maxLat, b.minLon));
        this.maxRasterWidth2InMeterNormed = this.distCalc.calcNormalizedDist(max / Math.sqrt(this.getCapacity()) * 2.0);
    }

    protected double getMaxRasterWidthMeter() {
        return this.distCalc.calcDenormalizedDist(this.maxRasterWidth2InMeterNormed) / 2.0;
    }

    private GHBitSet fillQuadtree(int size) {
        int locs = this.graph.getNodes();
        if (locs <= 0) {
            throw new IllegalStateException("check your graph - it is empty!");
        }
        GHBitSetImpl filledIndices = new GHBitSetImpl(size);
        GHPoint coord = new GHPoint();
        for (int nodeId = 0; nodeId < locs; ++nodeId) {
            double lat = this.nodeAccess.getLatitude(nodeId);
            double lon = this.nodeAccess.getLongitude(nodeId);
            int key = (int)this.keyAlgo.encode(lat, lon);
            long bytePos = (long)key * 4;
            if (filledIndices.contains(key)) {
                int oldNodeId = this.index.getInt(bytePos);
                this.keyAlgo.decode(key, coord);
                double distNew = this.distCalc.calcNormalizedDist(coord.lat, coord.lon, lat, lon);
                double oldLat = this.nodeAccess.getLatitude(oldNodeId);
                double oldLon = this.nodeAccess.getLongitude(oldNodeId);
                double distOld = this.distCalc.calcNormalizedDist(coord.lat, coord.lon, oldLat, oldLon);
                if (distNew >= distOld) continue;
                this.index.setInt(bytePos, nodeId);
                continue;
            }
            this.index.setInt(bytePos, nodeId);
            filledIndices.add(key);
        }
        return filledIndices;
    }

    private int fillEmptyIndices(GHBitSet filledIndices) {
        int len = this.latSize * this.lonSize;
        DataAccess indexCopy = new RAMDirectory().find("tempIndexCopy");
        indexCopy.setSegmentSize(this.index.getSegmentSize()).create(this.index.getCapacity());
        GHBitSetImpl indicesCopy = new GHBitSetImpl(len);
        int initializedCounter = filledIndices.getCardinality();
        int[] takenFrom = new int[len];
        Arrays.fill(takenFrom, -1);
        int i = filledIndices.next(0);
        while (i >= 0) {
            takenFrom[i] = i;
            i = filledIndices.next(i + 1);
        }
        if (initializedCounter == 0) {
            throw new IllegalStateException("at least one entry has to be != null, which should have happened in initIndex");
        }
        int tmp = initializedCounter;
        while (initializedCounter < len) {
            this.index.copyTo(indexCopy);
            filledIndices.copyTo(indicesCopy);
            initializedCounter = filledIndices.getCardinality();
            for (int i2 = 0; i2 < len; ++i2) {
                int to = -1;
                int from = -1;
                if (indicesCopy.contains(i2)) {
                    if ((i2 + 1) % this.lonSize != 0 && !indicesCopy.contains(i2 + 1)) {
                        from = i2;
                        to = i2 + 1;
                    } else if (i2 + this.lonSize < len && !indicesCopy.contains(i2 + this.lonSize)) {
                        from = i2;
                        to = i2 + this.lonSize;
                    }
                } else if ((i2 + 1) % this.lonSize != 0 && indicesCopy.contains(i2 + 1)) {
                    from = i2 + 1;
                    to = i2;
                } else if (i2 + this.lonSize < len && indicesCopy.contains(i2 + this.lonSize)) {
                    from = i2 + this.lonSize;
                    to = i2;
                }
                if (to < 0 || takenFrom[to] >= 0 && (takenFrom[to] == to || this.getNormedDist(from, to) >= this.getNormedDist(takenFrom[to], to))) continue;
                this.index.setInt(to * 4, indexCopy.getInt(from * 4));
                takenFrom[to] = takenFrom[from];
                filledIndices.add(to);
                ++initializedCounter;
            }
        }
        return initializedCounter - tmp;
    }

    double getNormedDist(int from, int to) {
        int fromX = from % this.lonSize;
        int fromY = from / this.lonSize;
        int toX = to % this.lonSize;
        int toY = to / this.lonSize;
        int dx = toX - fromX;
        int dy = toY - fromY;
        return dx * dx + dy * dy;
    }

    @Override
    public int findID(double lat, double lon) {
        return this.findClosest(lat, lon, EdgeFilter.ALL_EDGES).getClosestNode();
    }

    @Override
    public QueryResult findClosest(final double queryLat, final double queryLon, EdgeFilter edgeFilter) {
        if (this.isClosed()) {
            throw new IllegalStateException("You need to create a new LocationIndex instance as it is already closed");
        }
        if (edgeFilter != EdgeFilter.ALL_EDGES) {
            throw new UnsupportedOperationException("edge filters are not yet implemented for " + Location2IDQuadtree.class.getSimpleName());
        }
        long key = this.keyAlgo.encode(queryLat, queryLon);
        final int id = this.index.getInt(key * 4);
        double mainLat = this.nodeAccess.getLatitude(id);
        double mainLon = this.nodeAccess.getLongitude(id);
        final QueryResult res = new QueryResult(queryLat, queryLon);
        res.setClosestNode(id);
        res.setQueryDistance(this.distCalc.calcNormalizedDist(queryLat, queryLon, mainLat, mainLon));
        this.goFurtherHook(id);
        new BreadthFirstSearch(){

            @Override
            protected GHBitSet createBitSet() {
                return new GHTBitSet(10);
            }

            @Override
            protected boolean goFurther(int baseNode) {
                if (baseNode == id) {
                    return true;
                }
                Location2IDQuadtree.this.goFurtherHook(baseNode);
                double currLat = Location2IDQuadtree.this.nodeAccess.getLatitude(baseNode);
                double currLon = Location2IDQuadtree.this.nodeAccess.getLongitude(baseNode);
                double currNormedDist = Location2IDQuadtree.this.distCalc.calcNormalizedDist(queryLat, queryLon, currLat, currLon);
                if (currNormedDist < res.getQueryDistance()) {
                    res.setQueryDistance(currNormedDist);
                    res.setClosestNode(baseNode);
                    return true;
                }
                return currNormedDist < Location2IDQuadtree.this.maxRasterWidth2InMeterNormed;
            }
        }.start(this.graph.createEdgeExplorer(), id);
        res.setQueryDistance(this.distCalc.calcDenormalizedDist(res.getQueryDistance()));
        return res;
    }

    public void goFurtherHook(int n) {
    }

    @Override
    public void flush() {
        this.index.setHeader(0, 174507);
        this.index.setHeader(4, this.latSize);
        this.index.setHeader(8, this.lonSize);
        this.index.setHeader(12, this.graph.getNodes());
        this.index.flush();
    }

    @Override
    public void close() {
        this.index.close();
    }

    @Override
    public boolean isClosed() {
        return this.index.isClosed();
    }

    @Override
    public void setSegmentSize(int bytes) {
        this.index.setSegmentSize(bytes);
    }

}

