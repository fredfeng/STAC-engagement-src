/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.storage.index;

import com.graphhopper.coll.GHBitSet;
import com.graphhopper.coll.GHTBitSet;
import com.graphhopper.geohash.SpatialKeyAlgo;
import com.graphhopper.routing.util.AllEdgesIterator;
import com.graphhopper.routing.util.EdgeFilter;
import com.graphhopper.storage.CHGraph;
import com.graphhopper.storage.DataAccess;
import com.graphhopper.storage.Directory;
import com.graphhopper.storage.Graph;
import com.graphhopper.storage.NodeAccess;
import com.graphhopper.storage.index.BresenhamLine;
import com.graphhopper.storage.index.LocationIndex;
import com.graphhopper.storage.index.PointEmitter;
import com.graphhopper.storage.index.QueryResult;
import com.graphhopper.util.BitUtil;
import com.graphhopper.util.BreadthFirstSearch;
import com.graphhopper.util.DistanceCalc;
import com.graphhopper.util.DistancePlaneProjection;
import com.graphhopper.util.EdgeExplorer;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.Helper;
import com.graphhopper.util.PointList;
import com.graphhopper.util.StopWatch;
import com.graphhopper.util.shapes.BBox;
import com.graphhopper.util.shapes.GHPoint;
import gnu.trove.TIntCollection;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.hash.TIntHashSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocationIndexTree
implements LocationIndex {
    private final Logger logger;
    private final int MAGIC_INT;
    protected DistanceCalc distCalc;
    private DistanceCalc preciseDistCalc;
    protected final Graph graph;
    private final NodeAccess nodeAccess;
    final DataAccess dataAccess;
    private int[] entries;
    private byte[] shifts;
    private long[] bitmasks;
    protected SpatialKeyAlgo keyAlgo;
    private int minResolutionInMeter;
    private double deltaLat;
    private double deltaLon;
    private int initSizeLeafEntries;
    private boolean initialized;
    static final int START_POINTER = 1;
    int maxRegionSearch;
    private double equalNormedDelta;

    public LocationIndexTree(Graph g, Directory dir) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.distCalc = Helper.DIST_PLANE;
        this.preciseDistCalc = Helper.DIST_EARTH;
        this.minResolutionInMeter = 300;
        this.initSizeLeafEntries = 4;
        this.initialized = false;
        this.maxRegionSearch = 4;
        if (g instanceof CHGraph) {
            throw new IllegalArgumentException("Use base graph for LocationIndexTree instead of CHGraph");
        }
        this.MAGIC_INT = 96230;
        this.graph = g;
        this.nodeAccess = g.getNodeAccess();
        this.dataAccess = dir.find("location_index");
    }

    public int getMinResolutionInMeter() {
        return this.minResolutionInMeter;
    }

    public LocationIndexTree setMinResolutionInMeter(int minResolutionInMeter) {
        this.minResolutionInMeter = minResolutionInMeter;
        return this;
    }

    public LocationIndexTree setMaxRegionSearch(int numTiles) {
        if (numTiles < 1) {
            throw new IllegalArgumentException("Region of location index must be at least 1 but was " + numTiles);
        }
        if (numTiles % 2 == 1) {
            ++numTiles;
        }
        this.maxRegionSearch = numTiles;
        return this;
    }

    void prepareAlgo() {
        this.equalNormedDelta = this.distCalc.calcNormalizedDist(0.1);
        BBox bounds = this.graph.getBounds();
        if (this.graph.getNodes() == 0) {
            throw new IllegalStateException("Cannot create location index of empty graph!");
        }
        if (!bounds.isValid()) {
            throw new IllegalStateException("Cannot create location index when graph has invalid bounds: " + bounds);
        }
        double lat = Math.min(Math.abs(bounds.maxLat), Math.abs(bounds.minLat));
        double maxDistInMeter = Math.max((bounds.maxLat - bounds.minLat) / 360.0 * 4.003017359204114E7, (bounds.maxLon - bounds.minLon) / 360.0 * this.preciseDistCalc.calcCircumference(lat));
        double tmp = maxDistInMeter / (double)this.minResolutionInMeter;
        tmp *= tmp;
        TIntArrayList tmpEntries = new TIntArrayList();
        tmp /= 4.0;
        while (tmp > 1.0) {
            int tmpNo;
            if (tmp >= 64.0) {
                tmpNo = 64;
            } else if (tmp >= 16.0) {
                tmpNo = 16;
            } else {
                if (tmp < 4.0) break;
                tmpNo = 4;
            }
            tmpEntries.add(tmpNo);
            tmp /= (double)tmpNo;
        }
        tmpEntries.add(4);
        this.initEntries(tmpEntries.toArray());
        int shiftSum = 0;
        long parts = 1;
        for (int i = 0; i < this.shifts.length; ++i) {
            shiftSum += this.shifts[i];
            parts *= (long)this.entries[i];
        }
        if (shiftSum > 64) {
            throw new IllegalStateException("sum of all shifts does not fit into a long variable");
        }
        this.keyAlgo = new SpatialKeyAlgo(shiftSum).bounds(bounds);
        parts = Math.round(Math.sqrt(parts));
        this.deltaLat = (bounds.maxLat - bounds.minLat) / (double)parts;
        this.deltaLon = (bounds.maxLon - bounds.minLon) / (double)parts;
    }

    private LocationIndexTree initEntries(int[] entries) {
        if (entries.length < 1) {
            throw new IllegalStateException("depth needs to be at least 1");
        }
        this.entries = entries;
        int depth = entries.length;
        this.shifts = new byte[depth];
        this.bitmasks = new long[depth];
        int lastEntry = entries[0];
        for (int i = 0; i < depth; ++i) {
            if (lastEntry < entries[i]) {
                throw new IllegalStateException("entries should decrease or stay but was:" + Arrays.toString(entries));
            }
            lastEntry = entries[i];
            this.shifts[i] = this.getShift(entries[i]);
            this.bitmasks[i] = this.getBitmask(this.shifts[i]);
        }
        return this;
    }

    private byte getShift(int entries) {
        byte b = (byte)Math.round(Math.log(entries) / Math.log(2.0));
        if (b <= 0) {
            throw new IllegalStateException("invalid shift:" + b);
        }
        return b;
    }

    private long getBitmask(int shift) {
        long bm = (1 << shift) - 1;
        if (bm <= 0) {
            throw new IllegalStateException("invalid bitmask:" + bm);
        }
        return bm;
    }

    InMemConstructionIndex getPrepareInMemIndex() {
        InMemConstructionIndex memIndex = new InMemConstructionIndex(this.entries[0]);
        memIndex.prepare();
        return memIndex;
    }

    @Override
    public int findID(double lat, double lon) {
        QueryResult res = this.findClosest(lat, lon, EdgeFilter.ALL_EDGES);
        if (res == null) {
            return -1;
        }
        return res.getClosestNode();
    }

    @Override
    public LocationIndex setResolution(int minResolutionInMeter) {
        if (minResolutionInMeter <= 0) {
            throw new IllegalStateException("Negative precision is not allowed!");
        }
        this.setMinResolutionInMeter(minResolutionInMeter);
        return this;
    }

    @Override
    public LocationIndex setApproximation(boolean approx) {
        this.distCalc = approx ? Helper.DIST_PLANE : Helper.DIST_EARTH;
        return this;
    }

    @Override
    public LocationIndexTree create(long size) {
        throw new UnsupportedOperationException("Not supported. Use prepareIndex instead.");
    }

    @Override
    public boolean loadExisting() {
        if (this.initialized) {
            throw new IllegalStateException("Call loadExisting only once");
        }
        if (!this.dataAccess.loadExisting()) {
            return false;
        }
        if (this.dataAccess.getHeader(0) != this.MAGIC_INT) {
            throw new IllegalStateException("incorrect location index version, expected:" + this.MAGIC_INT);
        }
        if (this.dataAccess.getHeader(4) != this.calcChecksum()) {
            throw new IllegalStateException("location index was opened with incorrect graph: " + this.dataAccess.getHeader(4) + " vs. " + this.calcChecksum());
        }
        this.setMinResolutionInMeter(this.dataAccess.getHeader(8));
        this.prepareAlgo();
        this.initialized = true;
        return true;
    }

    @Override
    public void flush() {
        this.dataAccess.setHeader(0, this.MAGIC_INT);
        this.dataAccess.setHeader(4, this.calcChecksum());
        this.dataAccess.setHeader(8, this.minResolutionInMeter);
        this.dataAccess.flush();
    }

    @Override
    public LocationIndex prepareIndex() {
        if (this.initialized) {
            throw new IllegalStateException("Call prepareIndex only once");
        }
        StopWatch sw = new StopWatch().start();
        this.prepareAlgo();
        InMemConstructionIndex inMem = this.getPrepareInMemIndex();
        this.dataAccess.create(65536);
        try {
            inMem.store(inMem.root, 1);
            this.flush();
        }
        catch (Exception ex) {
            throw new IllegalStateException("Problem while storing location index. " + Helper.getMemInfo(), ex);
        }
        float entriesPerLeaf = (float)inMem.size / (float)inMem.leafs;
        this.initialized = true;
        this.logger.info("location index created in " + sw.stop().getSeconds() + "s, size:" + Helper.nf(inMem.size) + ", leafs:" + Helper.nf(inMem.leafs) + ", precision:" + this.minResolutionInMeter + ", depth:" + this.entries.length + ", checksum:" + this.calcChecksum() + ", entries:" + Arrays.toString(this.entries) + ", entriesPerLeaf:" + entriesPerLeaf);
        return this;
    }

    int calcChecksum() {
        return this.graph.getNodes();
    }

    @Override
    public void close() {
        this.dataAccess.close();
    }

    @Override
    public boolean isClosed() {
        return this.dataAccess.isClosed();
    }

    @Override
    public long getCapacity() {
        return this.dataAccess.getCapacity();
    }

    @Override
    public void setSegmentSize(int bytes) {
        this.dataAccess.setSegmentSize(bytes);
    }

    TIntArrayList getEntries() {
        return new TIntArrayList(this.entries);
    }

    final void fillIDs(long keyPart, int intIndex, TIntHashSet set, int depth) {
        long pointer = (long)intIndex << 2;
        if (depth == this.entries.length) {
            int value = this.dataAccess.getInt(pointer);
            if (value < 0) {
                set.add(- value + 1);
            } else {
                long max = (long)value * 4;
                for (long leafIndex = pointer + 4; leafIndex < max; leafIndex += 4) {
                    set.add(this.dataAccess.getInt(leafIndex));
                }
            }
            return;
        }
        int offset = (int)(this.bitmasks[depth] & keyPart) << 2;
        int value = this.dataAccess.getInt(pointer + (long)offset);
        if (value > 0) {
            this.fillIDs(keyPart >>> this.shifts[depth], value, set, depth + 1);
        }
    }

    final long createReverseKey(double lat, double lon) {
        return BitUtil.BIG.reverse(this.keyAlgo.encode(lat, lon), this.keyAlgo.getBits());
    }

    final long createReverseKey(long key) {
        return BitUtil.BIG.reverse(key, this.keyAlgo.getBits());
    }

    final double calculateRMin(double lat, double lon) {
        return this.calculateRMin(lat, lon, 0);
    }

    final double calculateRMin(double lat, double lon, int paddingTiles) {
        GHPoint query = new GHPoint(lat, lon);
        long key = this.keyAlgo.encode(query);
        GHPoint center = new GHPoint();
        this.keyAlgo.decode(key, center);
        double minLat = center.lat - (0.5 + (double)paddingTiles) * this.deltaLat;
        double maxLat = center.lat + (0.5 + (double)paddingTiles) * this.deltaLat;
        double minLon = center.lon - (0.5 + (double)paddingTiles) * this.deltaLon;
        double maxLon = center.lon + (0.5 + (double)paddingTiles) * this.deltaLon;
        double dSouthernLat = query.lat - minLat;
        double dNorthernLat = maxLat - query.lat;
        double dWesternLon = query.lon - minLon;
        double dEasternLon = maxLon - query.lon;
        double dMinLat = dSouthernLat < dNorthernLat ? this.distCalc.calcDist(query.lat, query.lon, minLat, query.lon) : this.distCalc.calcDist(query.lat, query.lon, maxLat, query.lon);
        double dMinLon = dWesternLon < dEasternLon ? this.distCalc.calcDist(query.lat, query.lon, query.lat, minLon) : this.distCalc.calcDist(query.lat, query.lon, query.lat, maxLon);
        double rMin = Math.min(dMinLat, dMinLon);
        return rMin;
    }

    double getDeltaLat() {
        return this.deltaLat;
    }

    double getDeltaLon() {
        return this.deltaLon;
    }

    GHPoint getCenter(double lat, double lon) {
        GHPoint query = new GHPoint(lat, lon);
        long key = this.keyAlgo.encode(query);
        GHPoint center = new GHPoint();
        this.keyAlgo.decode(key, center);
        return center;
    }

    public final boolean findNetworkEntries(double queryLat, double queryLon, TIntHashSet foundEntries, int iteration) {
        for (int yreg = - iteration; yreg <= iteration; ++yreg) {
            double subqueryLat = queryLat + (double)yreg * this.deltaLat;
            double subqueryLonA = queryLon - (double)iteration * this.deltaLon;
            double subqueryLonB = queryLon + (double)iteration * this.deltaLon;
            this.findNetworkEntriesSingleRegion(foundEntries, subqueryLat, subqueryLonA);
            if (iteration <= 0) continue;
            this.findNetworkEntriesSingleRegion(foundEntries, subqueryLat, subqueryLonB);
        }
        for (int xreg = - iteration + 1; xreg <= iteration - 1; ++xreg) {
            double subqueryLon = queryLon + (double)xreg * this.deltaLon;
            double subqueryLatA = queryLat - (double)iteration * this.deltaLat;
            double subqueryLatB = queryLat + (double)iteration * this.deltaLat;
            this.findNetworkEntriesSingleRegion(foundEntries, subqueryLatA, subqueryLon);
            this.findNetworkEntriesSingleRegion(foundEntries, subqueryLatB, subqueryLon);
        }
        if (iteration % 2 == 1 && !foundEntries.isEmpty()) {
            double rMin = this.calculateRMin(queryLat, queryLon, iteration);
            double minDistance = this.calcMinDistance(queryLat, queryLon, foundEntries);
            if (minDistance < rMin) {
                return true;
            }
        }
        return false;
    }

    final double calcMinDistance(double queryLat, double queryLon, TIntHashSet pointset) {
        double min = Double.MAX_VALUE;
        TIntIterator itr = pointset.iterator();
        while (itr.hasNext()) {
            double lon;
            int node = itr.next();
            double lat = this.nodeAccess.getLat(node);
            double dist = this.distCalc.calcDist(queryLat, queryLon, lat, lon = this.nodeAccess.getLon(node));
            if (dist >= min) continue;
            min = dist;
        }
        return min;
    }

    final void findNetworkEntriesSingleRegion(TIntHashSet storedNetworkEntryIds, double queryLat, double queryLon) {
        long keyPart = this.createReverseKey(queryLat, queryLon);
        this.fillIDs(keyPart, 1, storedNetworkEntryIds, 0);
    }

    @Override
    public QueryResult findClosest(final double queryLat, final double queryLon, final EdgeFilter edgeFilter) {
        if (this.isClosed()) {
            throw new IllegalStateException("You need to create a new LocationIndex instance as it is already closed");
        }
        TIntHashSet allCollectedEntryIds = new TIntHashSet();
        final QueryResult closestMatch = new QueryResult(queryLat, queryLon);
        for (int iteration = 0; iteration < this.maxRegionSearch; ++iteration) {
            TIntHashSet storedNetworkEntryIds = new TIntHashSet();
            boolean earlyFinish = this.findNetworkEntries(queryLat, queryLon, storedNetworkEntryIds, iteration);
            storedNetworkEntryIds.removeAll(allCollectedEntryIds);
            allCollectedEntryIds.addAll(storedNetworkEntryIds);
            final GHTBitSet checkBitset = new GHTBitSet(new TIntHashSet(storedNetworkEntryIds));
            final EdgeExplorer explorer = this.graph.createEdgeExplorer();
            storedNetworkEntryIds.forEach(new TIntProcedure(){

                @Override
                public boolean execute(int networkEntryNodeId) {
                    new XFirstSearchCheck(queryLat, queryLon, checkBitset, edgeFilter){

                        @Override
                        protected double getQueryDistance() {
                            return closestMatch.getQueryDistance();
                        }

                        @Override
                        protected boolean check(int node, double normedDist, int wayIndex, EdgeIteratorState edge, QueryResult.Position pos) {
                            if (normedDist < closestMatch.getQueryDistance()) {
                                closestMatch.setQueryDistance(normedDist);
                                closestMatch.setClosestNode(node);
                                closestMatch.setClosestEdge(edge.detach(false));
                                closestMatch.setWayIndex(wayIndex);
                                closestMatch.setSnappedPosition(pos);
                                return true;
                            }
                            return false;
                        }
                    }.start(explorer, networkEntryNodeId);
                    return true;
                }

            });
            if (earlyFinish && closestMatch.isValid()) break;
        }
        if (closestMatch.isValid()) {
            closestMatch.setQueryDistance(this.distCalc.calcDenormalizedDist(closestMatch.getQueryDistance()));
            closestMatch.calcSnappedPoint(this.distCalc);
        }
        return closestMatch;
    }

    static class InMemTreeEntry
    implements InMemEntry {
        InMemEntry[] subEntries;

        public InMemTreeEntry(int subEntryNo) {
            this.subEntries = new InMemEntry[subEntryNo];
        }

        public InMemEntry getSubEntry(int index) {
            return this.subEntries[index];
        }

        public void setSubEntry(int index, InMemEntry subEntry) {
            this.subEntries[index] = subEntry;
        }

        public Collection<InMemEntry> getSubEntriesForDebug() {
            ArrayList<InMemEntry> list = new ArrayList<InMemEntry>();
            for (InMemEntry e : this.subEntries) {
                if (e == null) continue;
                list.add(e);
            }
            return list;
        }

        @Override
        public final boolean isLeaf() {
            return false;
        }

        public String toString() {
            return "TREE";
        }
    }

    static class SortedIntSet
    extends TIntArrayList {
        public SortedIntSet() {
        }

        public SortedIntSet(int capacity) {
            super(capacity);
        }

        public boolean addOnce(int value) {
            int foundIndex = this.binarySearch(value);
            if (foundIndex >= 0) {
                return false;
            }
            foundIndex = - foundIndex - 1;
            this.insert(foundIndex, value);
            return true;
        }
    }

    static class InMemLeafEntry
    extends SortedIntSet
    implements InMemEntry {
        public InMemLeafEntry(int count, long key) {
            super(count);
        }

        public boolean addNode(int nodeId) {
            return this.addOnce(nodeId);
        }

        @Override
        public final boolean isLeaf() {
            return true;
        }

        @Override
        public String toString() {
            return "LEAF  " + super.toString();
        }

        TIntArrayList getResults() {
            return this;
        }
    }

    static interface InMemEntry {
        public boolean isLeaf();
    }

    protected abstract class XFirstSearchCheck
    extends BreadthFirstSearch {
        boolean goFurther;
        double currNormedDist;
        double currLat;
        double currLon;
        int currNode;
        final double queryLat;
        final double queryLon;
        final GHBitSet checkBitset;
        final EdgeFilter edgeFilter;

        public XFirstSearchCheck(double queryLat, double queryLon, GHBitSet checkBitset, EdgeFilter edgeFilter) {
            this.goFurther = true;
            this.queryLat = queryLat;
            this.queryLon = queryLon;
            this.checkBitset = checkBitset;
            this.edgeFilter = edgeFilter;
        }

        @Override
        protected GHBitSet createBitSet() {
            return this.checkBitset;
        }

        @Override
        protected boolean goFurther(int baseNode) {
            this.currNode = baseNode;
            this.currLat = LocationIndexTree.this.nodeAccess.getLatitude(baseNode);
            this.currLon = LocationIndexTree.this.nodeAccess.getLongitude(baseNode);
            this.currNormedDist = LocationIndexTree.this.distCalc.calcNormalizedDist(this.queryLat, this.queryLon, this.currLat, this.currLon);
            return this.goFurther;
        }

        @Override
        protected boolean checkAdjacent(EdgeIteratorState currEdge) {
            double adjLon;
            this.goFurther = false;
            if (!this.edgeFilter.accept(currEdge)) {
                return true;
            }
            int tmpClosestNode = this.currNode;
            if (this.check(tmpClosestNode, this.currNormedDist, 0, currEdge, QueryResult.Position.TOWER) && this.currNormedDist <= LocationIndexTree.this.equalNormedDelta) {
                return false;
            }
            int adjNode = currEdge.getAdjNode();
            double adjLat = LocationIndexTree.this.nodeAccess.getLatitude(adjNode);
            double adjDist = LocationIndexTree.this.distCalc.calcNormalizedDist(adjLat, adjLon = LocationIndexTree.this.nodeAccess.getLongitude(adjNode), this.queryLat, this.queryLon);
            if (adjDist < this.currNormedDist) {
                tmpClosestNode = adjNode;
            }
            double tmpLat = this.currLat;
            double tmpLon = this.currLon;
            PointList pointList = currEdge.fetchWayGeometry(2);
            int len = pointList.getSize();
            for (int pointIndex = 0; pointIndex < len; ++pointIndex) {
                double tmpNormedDist;
                double wayLat = pointList.getLatitude(pointIndex);
                double wayLon = pointList.getLongitude(pointIndex);
                QueryResult.Position pos = QueryResult.Position.EDGE;
                if (LocationIndexTree.this.distCalc.validEdgeDistance(this.queryLat, this.queryLon, tmpLat, tmpLon, wayLat, wayLon)) {
                    tmpNormedDist = LocationIndexTree.this.distCalc.calcNormalizedEdgeDistance(this.queryLat, this.queryLon, tmpLat, tmpLon, wayLat, wayLon);
                    this.check(tmpClosestNode, tmpNormedDist, pointIndex, currEdge, pos);
                } else {
                    if (pointIndex + 1 == len) {
                        tmpNormedDist = adjDist;
                        pos = QueryResult.Position.TOWER;
                    } else {
                        tmpNormedDist = LocationIndexTree.this.distCalc.calcNormalizedDist(this.queryLat, this.queryLon, wayLat, wayLon);
                        pos = QueryResult.Position.PILLAR;
                    }
                    this.check(tmpClosestNode, tmpNormedDist, pointIndex + 1, currEdge, pos);
                }
                if (tmpNormedDist <= LocationIndexTree.this.equalNormedDelta) {
                    return false;
                }
                tmpLat = wayLat;
                tmpLon = wayLon;
            }
            return this.getQueryDistance() > LocationIndexTree.this.equalNormedDelta;
        }

        protected abstract double getQueryDistance();

        protected abstract boolean check(int var1, double var2, int var4, EdgeIteratorState var5, QueryResult.Position var6);
    }

    class InMemConstructionIndex {
        int size;
        int leafs;
        InMemTreeEntry root;

        public InMemConstructionIndex(int noOfSubEntries) {
            this.root = new InMemTreeEntry(noOfSubEntries);
        }

        void prepare() {
            AllEdgesIterator allIter = LocationIndexTree.this.graph.getAllEdges();
            try {
                while (allIter.next()) {
                    double lat2;
                    double lon2;
                    int nodeA = allIter.getBaseNode();
                    int nodeB = allIter.getAdjNode();
                    double lat1 = LocationIndexTree.this.nodeAccess.getLatitude(nodeA);
                    double lon1 = LocationIndexTree.this.nodeAccess.getLongitude(nodeA);
                    PointList points = allIter.fetchWayGeometry(0);
                    int len = points.getSize();
                    for (int i = 0; i < len; ++i) {
                        lat2 = points.getLatitude(i);
                        lon2 = points.getLongitude(i);
                        this.addNode(nodeA, nodeB, lat1, lon1, lat2, lon2);
                        lat1 = lat2;
                        lon1 = lon2;
                    }
                    lat2 = LocationIndexTree.this.nodeAccess.getLatitude(nodeB);
                    lon2 = LocationIndexTree.this.nodeAccess.getLongitude(nodeB);
                    this.addNode(nodeA, nodeB, lat1, lon1, lat2, lon2);
                }
            }
            catch (Exception ex) {
                LocationIndexTree.this.logger.error("Problem! base:" + allIter.getBaseNode() + ", adj:" + allIter.getAdjNode() + ", edge:" + allIter.getEdge(), ex);
            }
        }

        void addNode(final int nodeA, int nodeB, double lat1, double lon1, double lat2, double lon2) {
            PointEmitter pointEmitter = new PointEmitter(){

                @Override
                public void set(double lat, double lon) {
                    long key = LocationIndexTree.this.keyAlgo.encode(lat, lon);
                    long keyPart = LocationIndexTree.this.createReverseKey(key);
                    InMemConstructionIndex.this.addNode(InMemConstructionIndex.this.root, nodeA, 0, keyPart, key);
                }
            };
            BresenhamLine.calcPoints(lat1, lon1, lat2, lon2, pointEmitter, LocationIndexTree.this.graph.getBounds().minLat, LocationIndexTree.this.graph.getBounds().minLon, LocationIndexTree.this.deltaLat, LocationIndexTree.this.deltaLon);
        }

        void addNode(InMemEntry entry, int nodeId, int depth, long keyPart, long key) {
            if (entry.isLeaf()) {
                InMemLeafEntry leafEntry = (InMemLeafEntry)entry;
                leafEntry.addNode(nodeId);
            } else {
                int index = (int)(LocationIndexTree.this.bitmasks[depth] & keyPart);
                keyPart >>>= LocationIndexTree.this.shifts[depth];
                InMemTreeEntry treeEntry = (InMemTreeEntry)entry;
                InMemEntry subentry = treeEntry.getSubEntry(index);
                ++depth;
                if (subentry == null) {
                    subentry = depth == LocationIndexTree.this.entries.length ? new InMemLeafEntry(LocationIndexTree.this.initSizeLeafEntries, key) : new InMemTreeEntry(LocationIndexTree.this.entries[depth]);
                    treeEntry.setSubEntry(index, subentry);
                }
                this.addNode(subentry, nodeId, depth, keyPart, key);
            }
        }

        Collection<InMemEntry> getEntriesOf(int selectDepth) {
            ArrayList<InMemEntry> list = new ArrayList<InMemEntry>();
            this.fillLayer(list, selectDepth, 0, this.root.getSubEntriesForDebug());
            return list;
        }

        void fillLayer(Collection<InMemEntry> list, int selectDepth, int depth, Collection<InMemEntry> entries) {
            for (InMemEntry entry : entries) {
                if (selectDepth == depth) {
                    list.add(entry);
                    continue;
                }
                if (!(entry instanceof InMemTreeEntry)) continue;
                this.fillLayer(list, selectDepth, depth + 1, ((InMemTreeEntry)entry).getSubEntriesForDebug());
            }
        }

        String print() {
            StringBuilder sb = new StringBuilder();
            this.print(this.root, sb, 0, 0);
            return sb.toString();
        }

        void print(InMemEntry e, StringBuilder sb, long key, int depth) {
            if (e.isLeaf()) {
                InMemLeafEntry leaf = (InMemLeafEntry)e;
                int bits = LocationIndexTree.this.keyAlgo.getBits();
                sb.append(BitUtil.BIG.toBitString(BitUtil.BIG.reverse(key, bits), bits)).append("  ");
                TIntArrayList entries = leaf.getResults();
                for (int i = 0; i < entries.size(); ++i) {
                    sb.append(leaf.get(i)).append(',');
                }
                sb.append('\n');
            } else {
                InMemTreeEntry tree = (InMemTreeEntry)e;
                key <<= LocationIndexTree.this.shifts[depth];
                for (int counter = 0; counter < tree.subEntries.length; ++counter) {
                    InMemEntry sube = tree.subEntries[counter];
                    if (sube == null) continue;
                    this.print(sube, sb, key | (long)counter, depth + 1);
                }
            }
        }

        int store(InMemEntry entry, int intIndex) {
            long refPointer = (long)intIndex * 4;
            if (entry.isLeaf()) {
                InMemLeafEntry leaf = (InMemLeafEntry)entry;
                TIntArrayList entries = leaf.getResults();
                int len = entries.size();
                if (len == 0) {
                    return intIndex;
                }
                this.size += len;
                ++this.leafs;
                LocationIndexTree.this.dataAccess.ensureCapacity((long)(++intIndex + len + 1) * 4);
                if (len == 1) {
                    LocationIndexTree.this.dataAccess.setInt(refPointer, - entries.get(0) - 1);
                } else {
                    int index = 0;
                    while (index < len) {
                        LocationIndexTree.this.dataAccess.setInt((long)intIndex * 4, entries.get(index));
                        ++index;
                        ++intIndex;
                    }
                    LocationIndexTree.this.dataAccess.setInt(refPointer, intIndex);
                }
            } else {
                InMemTreeEntry treeEntry = (InMemTreeEntry)entry;
                int len = treeEntry.subEntries.length;
                intIndex += len;
                int subCounter = 0;
                while (subCounter < len) {
                    InMemEntry subEntry = treeEntry.subEntries[subCounter];
                    if (subEntry != null) {
                        LocationIndexTree.this.dataAccess.ensureCapacity((long)(intIndex + 1) * 4);
                        int beforeIntIndex = intIndex;
                        intIndex = this.store(subEntry, beforeIntIndex);
                        if (intIndex == beforeIntIndex) {
                            LocationIndexTree.this.dataAccess.setInt(refPointer, 0);
                        } else {
                            LocationIndexTree.this.dataAccess.setInt(refPointer, beforeIntIndex);
                        }
                    }
                    ++subCounter;
                    refPointer += 4;
                }
            }
            return intIndex;
        }

    }

}

