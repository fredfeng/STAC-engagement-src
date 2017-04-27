/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.storage;

import com.graphhopper.coll.GHBitSet;
import com.graphhopper.coll.GHBitSetImpl;
import com.graphhopper.coll.SparseIntIntArray;
import com.graphhopper.routing.util.AllEdgesIterator;
import com.graphhopper.routing.util.EdgeFilter;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.search.NameIndex;
import com.graphhopper.storage.DataAccess;
import com.graphhopper.storage.Directory;
import com.graphhopper.storage.EdgeAccess;
import com.graphhopper.storage.GHNodeAccess;
import com.graphhopper.storage.Graph;
import com.graphhopper.storage.GraphExtension;
import com.graphhopper.storage.InternalGraphEventListener;
import com.graphhopper.storage.NodeAccess;
import com.graphhopper.util.BitUtil;
import com.graphhopper.util.EdgeExplorer;
import com.graphhopper.util.EdgeIterator;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.GHUtility;
import com.graphhopper.util.Helper;
import com.graphhopper.util.PointAccess;
import com.graphhopper.util.PointList;
import com.graphhopper.util.shapes.BBox;
import java.nio.ByteOrder;
import org.slf4j.LoggerFactory;

class BaseGraph
implements Graph {
    private static final int MAX_EDGES = 1000;
    int E_GEO;
    int E_NAME;
    int E_ADDITIONAL;
    int edgeEntryBytes;
    private boolean initialized = false;
    final DataAccess edges;
    protected int edgeCount;
    protected int N_EDGE_REF;
    protected int N_LAT;
    protected int N_LON;
    protected int N_ELE;
    protected int N_ADDITIONAL;
    int nodeEntryBytes;
    final DataAccess nodes;
    private int nodeCount;
    final BBox bounds;
    private GHBitSet removedNodes;
    private int edgeEntryIndex;
    private int nodeEntryIndex;
    final NodeAccess nodeAccess;
    final GraphExtension extStorage;
    final DataAccess wayGeometry;
    private int maxGeoRef;
    final NameIndex nameIndex;
    final BitUtil bitUtil;
    private final Directory dir;
    final EncodingManager encodingManager;
    private final InternalGraphEventListener listener;
    private boolean frozen = false;
    final EdgeAccess edgeAccess;

    public BaseGraph(Directory dir, final EncodingManager encodingManager, boolean withElevation, InternalGraphEventListener listener, GraphExtension extendedStorage) {
        this.dir = dir;
        this.encodingManager = encodingManager;
        this.bitUtil = BitUtil.get(dir.getByteOrder());
        this.wayGeometry = dir.find("geometry");
        this.nameIndex = new NameIndex(dir);
        this.nodes = dir.find("nodes");
        this.edges = dir.find("edges");
        this.listener = listener;
        this.edgeAccess = new EdgeAccess(this.edges, this.bitUtil){

            @Override
            final EdgeIterable createSingleEdge(EdgeFilter filter) {
                return new EdgeIterable(BaseGraph.this, this, filter);
            }

            @Override
            final int getEdgeRef(int nodeId) {
                return BaseGraph.this.nodes.getInt((long)nodeId * (long)BaseGraph.this.nodeEntryBytes + (long)BaseGraph.this.N_EDGE_REF);
            }

            @Override
            final void setEdgeRef(int nodeId, int edgeId) {
                BaseGraph.this.nodes.setInt((long)nodeId * (long)BaseGraph.this.nodeEntryBytes + (long)BaseGraph.this.N_EDGE_REF, edgeId);
            }

            @Override
            final int getEntryBytes() {
                return BaseGraph.this.edgeEntryBytes;
            }

            @Override
            final long toPointer(int edgeId) {
                assert (this.isInBounds(edgeId));
                return (long)edgeId * (long)BaseGraph.this.edgeEntryBytes;
            }

            @Override
            final boolean isInBounds(int edgeId) {
                return edgeId < BaseGraph.this.edgeCount && edgeId >= 0;
            }

            @Override
            final long reverseFlags(long edgePointer, long flags) {
                return encodingManager.reverseFlags(flags);
            }

            public String toString() {
                return "base edge access";
            }
        };
        this.bounds = BBox.createInverse(withElevation);
        this.nodeAccess = new GHNodeAccess(this, withElevation);
        this.extStorage = extendedStorage;
        this.extStorage.init(this, dir);
    }

    @Override
    public Graph getBaseGraph() {
        return this;
    }

    void checkInit() {
        if (this.initialized) {
            throw new IllegalStateException("You cannot configure this GraphStorage after calling create or loadExisting. Calling one of the methods twice is also not allowed.");
        }
    }

    protected int loadNodesHeader() {
        this.nodeEntryBytes = this.nodes.getHeader(4);
        this.nodeCount = this.nodes.getHeader(8);
        this.bounds.minLon = Helper.intToDegree(this.nodes.getHeader(12));
        this.bounds.maxLon = Helper.intToDegree(this.nodes.getHeader(16));
        this.bounds.minLat = Helper.intToDegree(this.nodes.getHeader(20));
        this.bounds.maxLat = Helper.intToDegree(this.nodes.getHeader(24));
        if (this.bounds.hasElevation()) {
            this.bounds.minEle = Helper.intToEle(this.nodes.getHeader(28));
            this.bounds.maxEle = Helper.intToEle(this.nodes.getHeader(32));
        }
        this.frozen = this.nodes.getHeader(36) == 1;
        return 10;
    }

    protected int setNodesHeader() {
        this.nodes.setHeader(4, this.nodeEntryBytes);
        this.nodes.setHeader(8, this.nodeCount);
        this.nodes.setHeader(12, Helper.degreeToInt(this.bounds.minLon));
        this.nodes.setHeader(16, Helper.degreeToInt(this.bounds.maxLon));
        this.nodes.setHeader(20, Helper.degreeToInt(this.bounds.minLat));
        this.nodes.setHeader(24, Helper.degreeToInt(this.bounds.maxLat));
        if (this.bounds.hasElevation()) {
            this.nodes.setHeader(28, Helper.eleToInt(this.bounds.minEle));
            this.nodes.setHeader(32, Helper.eleToInt(this.bounds.maxEle));
        }
        this.nodes.setHeader(36, this.isFrozen() ? 1 : 0);
        return 10;
    }

    protected int loadEdgesHeader() {
        this.edgeEntryBytes = this.edges.getHeader(0);
        this.edgeCount = this.edges.getHeader(4);
        return 5;
    }

    protected int setEdgesHeader() {
        this.edges.setHeader(0, this.edgeEntryBytes);
        this.edges.setHeader(4, this.edgeCount);
        this.edges.setHeader(8, this.encodingManager.hashCode());
        this.edges.setHeader(12, this.extStorage.hashCode());
        return 5;
    }

    protected int loadWayGeometryHeader() {
        this.maxGeoRef = this.wayGeometry.getHeader(0);
        return 1;
    }

    protected int setWayGeometryHeader() {
        this.wayGeometry.setHeader(0, this.maxGeoRef);
        return 1;
    }

    void initStorage() {
        this.edgeEntryIndex = 0;
        this.nodeEntryIndex = 0;
        boolean flagsSizeIsLong = this.encodingManager.getBytesForFlags() == 8;
        this.edgeAccess.init(this.nextEdgeEntryIndex(4), this.nextEdgeEntryIndex(4), this.nextEdgeEntryIndex(4), this.nextEdgeEntryIndex(4), this.nextEdgeEntryIndex(4), this.nextEdgeEntryIndex(this.encodingManager.getBytesForFlags()), flagsSizeIsLong);
        this.E_GEO = this.nextEdgeEntryIndex(4);
        this.E_NAME = this.nextEdgeEntryIndex(4);
        this.E_ADDITIONAL = this.extStorage.isRequireEdgeField() ? this.nextEdgeEntryIndex(4) : -1;
        this.N_EDGE_REF = this.nextNodeEntryIndex(4);
        this.N_LAT = this.nextNodeEntryIndex(4);
        this.N_LON = this.nextNodeEntryIndex(4);
        this.N_ELE = this.nodeAccess.is3D() ? this.nextNodeEntryIndex(4) : -1;
        this.N_ADDITIONAL = this.extStorage.isRequireNodeField() ? this.nextNodeEntryIndex(4) : -1;
        this.initNodeAndEdgeEntrySize();
        this.listener.initStorage();
        this.initialized = true;
    }

    void initNodeRefs(long oldCapacity, long newCapacity) {
        long pointer;
        for (pointer = oldCapacity + (long)this.N_EDGE_REF; pointer < newCapacity; pointer += (long)this.nodeEntryBytes) {
            this.nodes.setInt(pointer, -1);
        }
        if (this.extStorage.isRequireNodeField()) {
            for (pointer = oldCapacity + (long)this.N_ADDITIONAL; pointer < newCapacity; pointer += (long)this.nodeEntryBytes) {
                this.nodes.setInt(pointer, this.extStorage.getDefaultNodeFieldValue());
            }
        }
    }

    protected final int nextEdgeEntryIndex(int sizeInBytes) {
        int tmp = this.edgeEntryIndex;
        this.edgeEntryIndex += sizeInBytes;
        return tmp;
    }

    protected final int nextNodeEntryIndex(int sizeInBytes) {
        int tmp = this.nodeEntryIndex;
        this.nodeEntryIndex += sizeInBytes;
        return tmp;
    }

    protected final void initNodeAndEdgeEntrySize() {
        this.nodeEntryBytes = this.nodeEntryIndex;
        this.edgeEntryBytes = this.edgeEntryIndex;
    }

    final void ensureNodeIndex(int nodeIndex) {
        if (!this.initialized) {
            throw new AssertionError((Object)"The graph has not yet been initialized.");
        }
        if (nodeIndex < this.nodeCount) {
            return;
        }
        long oldNodes = this.nodeCount;
        this.nodeCount = nodeIndex + 1;
        boolean capacityIncreased = this.nodes.ensureCapacity((long)this.nodeCount * (long)this.nodeEntryBytes);
        if (capacityIncreased) {
            long newBytesCapacity = this.nodes.getCapacity();
            this.initNodeRefs(oldNodes * (long)this.nodeEntryBytes, newBytesCapacity);
        }
    }

    @Override
    public int getNodes() {
        return this.nodeCount;
    }

    @Override
    public NodeAccess getNodeAccess() {
        return this.nodeAccess;
    }

    @Override
    public BBox getBounds() {
        return this.bounds;
    }

    @Override
    public EdgeIteratorState edge(int a, int b, double distance, boolean bothDirection) {
        return this.edge(a, b).setDistance(distance).setFlags(this.encodingManager.flagsDefault(true, bothDirection));
    }

    void setSegmentSize(int bytes) {
        this.checkInit();
        this.nodes.setSegmentSize(bytes);
        this.edges.setSegmentSize(bytes);
        this.wayGeometry.setSegmentSize(bytes);
        this.nameIndex.setSegmentSize(bytes);
        this.extStorage.setSegmentSize(bytes);
    }

    void freeze() {
        if (this.isFrozen()) {
            throw new IllegalStateException("base graph already frozen");
        }
        this.frozen = true;
        this.listener.freeze();
    }

    boolean isFrozen() {
        return this.frozen;
    }

    public void checkFreeze() {
        if (this.isFrozen()) {
            throw new IllegalStateException("Cannot add edge or node after baseGraph.freeze was called");
        }
    }

    void create(long initSize) {
        this.nodes.create(initSize);
        this.edges.create(initSize);
        this.wayGeometry.create(initSize);
        this.nameIndex.create(1000);
        this.extStorage.create(initSize);
        this.initStorage();
        this.maxGeoRef = 4;
        this.initNodeRefs(0, this.nodes.getCapacity());
    }

    String toDetailsString() {
        return "edges:" + Helper.nf(this.edgeCount) + "(" + this.edges.getCapacity() / 0x100000 + "MB), " + "nodes:" + Helper.nf(this.getNodes()) + "(" + this.nodes.getCapacity() / 0x100000 + "MB), " + "name:(" + this.nameIndex.getCapacity() / 0x100000 + "MB), " + "geo:" + Helper.nf(this.maxGeoRef) + "(" + this.wayGeometry.getCapacity() / 0x100000 + "MB), " + "bounds:" + this.bounds;
    }

    void flush() {
        this.setNodesHeader();
        this.setEdgesHeader();
        this.setWayGeometryHeader();
        this.wayGeometry.flush();
        this.nameIndex.flush();
        this.edges.flush();
        this.nodes.flush();
        this.extStorage.flush();
    }

    void close() {
        this.wayGeometry.close();
        this.nameIndex.close();
        this.edges.close();
        this.nodes.close();
        this.extStorage.close();
    }

    long getCapacity() {
        return this.edges.getCapacity() + this.nodes.getCapacity() + this.nameIndex.getCapacity() + this.wayGeometry.getCapacity() + this.extStorage.getCapacity();
    }

    void loadExisting(String dim) {
        if (!this.nodes.loadExisting()) {
            throw new IllegalStateException("Cannot load nodes. corrupt file or directory? " + this.dir);
        }
        if (!dim.equalsIgnoreCase("" + this.nodeAccess.getDimension())) {
            throw new IllegalStateException("Configured dimension (" + this.nodeAccess.getDimension() + ") is not equal " + "to dimension of loaded graph (" + dim + ")");
        }
        if (!this.edges.loadExisting()) {
            throw new IllegalStateException("Cannot load edges. corrupt file or directory? " + this.dir);
        }
        if (!this.wayGeometry.loadExisting()) {
            throw new IllegalStateException("Cannot load geometry. corrupt file or directory? " + this.dir);
        }
        if (!this.nameIndex.loadExisting()) {
            throw new IllegalStateException("Cannot load name index. corrupt file or directory? " + this.dir);
        }
        if (!this.extStorage.loadExisting()) {
            throw new IllegalStateException("Cannot load extended storage. corrupt file or directory? " + this.dir);
        }
        this.initStorage();
        this.loadNodesHeader();
        this.loadEdgesHeader();
        this.loadWayGeometryHeader();
    }

    EdgeIteratorState copyProperties(CommonEdgeIterator from, EdgeIteratorState to) {
        to.setDistance(from.getDistance()).setName(from.getName()).setFlags(from.getDirectFlags()).setWayGeometry(from.fetchWayGeometry(0));
        if (this.E_ADDITIONAL >= 0) {
            to.setAdditionalField(from.getAdditionalField());
        }
        return to;
    }

    @Override
    public EdgeIteratorState edge(int nodeA, int nodeB) {
        if (this.isFrozen()) {
            throw new IllegalStateException("Cannot create edge if graph is already frozen");
        }
        this.ensureNodeIndex(Math.max(nodeA, nodeB));
        int edgeId = this.edgeAccess.internalEdgeAdd(this.nextEdgeId(), nodeA, nodeB);
        EdgeIterable iter = new EdgeIterable(this, this.edgeAccess, EdgeFilter.ALL_EDGES);
        boolean ret = iter.init(edgeId, nodeB);
        assert (ret);
        if (this.extStorage.isRequireEdgeField()) {
            iter.setAdditionalField(this.extStorage.getDefaultEdgeFieldValue());
        }
        return iter;
    }

    void setEdgeCount(int cnt) {
        this.edgeCount = cnt;
    }

    protected int nextEdgeId() {
        int nextEdge = this.edgeCount++;
        if (this.edgeCount < 0) {
            throw new IllegalStateException("too many edges. new edge id would be negative. " + this.toString());
        }
        this.edges.ensureCapacity(((long)this.edgeCount + 1) * (long)this.edgeEntryBytes);
        return nextEdge;
    }

    @Override
    public EdgeIteratorState getEdgeIteratorState(int edgeId, int adjNode) {
        if (!this.edgeAccess.isInBounds(edgeId)) {
            throw new IllegalStateException("edgeId " + edgeId + " out of bounds");
        }
        this.checkAdjNodeBounds(adjNode);
        return this.edgeAccess.getEdgeProps(edgeId, adjNode);
    }

    final void checkAdjNodeBounds(int adjNode) {
        if (adjNode < 0 && adjNode != Integer.MIN_VALUE || adjNode >= this.nodeCount) {
            throw new IllegalStateException("adjNode " + adjNode + " out of bounds [0," + Helper.nf(this.nodeCount) + ")");
        }
    }

    @Override
    public EdgeExplorer createEdgeExplorer(EdgeFilter filter) {
        return new EdgeIterable(this, this.edgeAccess, filter);
    }

    @Override
    public EdgeExplorer createEdgeExplorer() {
        return this.createEdgeExplorer(EdgeFilter.ALL_EDGES);
    }

    @Override
    public AllEdgesIterator getAllEdges() {
        return new AllEdgeIterator(this, this.edgeAccess);
    }

    @Override
    public Graph copyTo(Graph g) {
        this.initialized = true;
        if (g.getClass().equals(this.getClass())) {
            this._copyTo((BaseGraph)g);
            return g;
        }
        return GHUtility.copyTo(this, g);
    }

    void _copyTo(BaseGraph clonedG) {
        if (clonedG.edgeEntryBytes != this.edgeEntryBytes) {
            throw new IllegalStateException("edgeEntryBytes cannot be different for cloned graph. Cloned: " + clonedG.edgeEntryBytes + " vs " + this.edgeEntryBytes);
        }
        if (clonedG.nodeEntryBytes != this.nodeEntryBytes) {
            throw new IllegalStateException("nodeEntryBytes cannot be different for cloned graph. Cloned: " + clonedG.nodeEntryBytes + " vs " + this.nodeEntryBytes);
        }
        if (clonedG.nodeAccess.getDimension() != this.nodeAccess.getDimension()) {
            throw new IllegalStateException("dimension cannot be different for cloned graph. Cloned: " + clonedG.nodeAccess.getDimension() + " vs " + this.nodeAccess.getDimension());
        }
        this.setNodesHeader();
        this.nodes.copyTo(clonedG.nodes);
        clonedG.loadNodesHeader();
        this.setEdgesHeader();
        this.edges.copyTo(clonedG.edges);
        clonedG.loadEdgesHeader();
        this.nameIndex.copyTo(clonedG.nameIndex);
        this.setWayGeometryHeader();
        this.wayGeometry.copyTo(clonedG.wayGeometry);
        clonedG.loadWayGeometryHeader();
        this.extStorage.copyTo(clonedG.extStorage);
        clonedG.removedNodes = this.removedNodes == null ? null : this.removedNodes.copyTo(new GHBitSetImpl());
    }

    protected void trimToSize() {
        long nodeCap = (long)this.nodeCount * (long)this.nodeEntryBytes;
        this.nodes.trimTo(nodeCap);
    }

    void inPlaceNodeRemove(int removeNodeCount) {
        int oldI;
        int i;
        int toMoveNodes = this.getNodes();
        int itemsToMove = 0;
        SparseIntIntArray oldToNewMap = new SparseIntIntArray(removeNodeCount);
        GHBitSetImpl toRemoveSet = new GHBitSetImpl(removeNodeCount);
        this.removedNodes.copyTo(toRemoveSet);
        EdgeExplorer delExplorer = this.createEdgeExplorer(EdgeFilter.ALL_EDGES);
        int removeNode = this.removedNodes.next(0);
        while (removeNode >= 0) {
            EdgeIterator delEdgesIter = delExplorer.setBaseNode(removeNode);
            while (delEdgesIter.next()) {
                toRemoveSet.add(delEdgesIter.getAdjNode());
            }
            --toMoveNodes;
            while (toMoveNodes >= 0 && this.removedNodes.contains(toMoveNodes)) {
                --toMoveNodes;
            }
            if (toMoveNodes >= removeNode) {
                oldToNewMap.put(toMoveNodes, removeNode);
            }
            ++itemsToMove;
            removeNode = this.removedNodes.next(removeNode + 1);
        }
        EdgeIterable adjNodesToDelIter = (EdgeIterable)this.createEdgeExplorer();
        int removeNode2 = toRemoveSet.next(0);
        while (removeNode2 >= 0) {
            adjNodesToDelIter.setBaseNode(removeNode2);
            long prev = -1;
            while (adjNodesToDelIter.next()) {
                int nodeId = adjNodesToDelIter.getAdjNode();
                if (nodeId != -1 && this.removedNodes.contains(nodeId)) {
                    int edgeToRemove = adjNodesToDelIter.getEdge();
                    long edgeToRemovePointer = this.edgeAccess.toPointer(edgeToRemove);
                    this.edgeAccess.internalEdgeDisconnect(edgeToRemove, prev, removeNode2, nodeId);
                    this.edgeAccess.invalidateEdge(edgeToRemovePointer);
                    continue;
                }
                prev = adjNodesToDelIter.edgePointer;
            }
            removeNode2 = toRemoveSet.next(removeNode2 + 1);
        }
        GHBitSetImpl toMoveSet = new GHBitSetImpl(removeNodeCount * 3);
        EdgeExplorer movedEdgeExplorer = this.createEdgeExplorer();
        for (i = 0; i < itemsToMove; ++i) {
            oldI = oldToNewMap.keyAt(i);
            EdgeIterator movedEdgeIter = movedEdgeExplorer.setBaseNode(oldI);
            while (movedEdgeIter.next()) {
                int nodeId = movedEdgeIter.getAdjNode();
                if (nodeId == -1) continue;
                if (this.removedNodes.contains(nodeId)) {
                    throw new IllegalStateException("shouldn't happen the edge to the node " + nodeId + " should be already deleted. " + oldI);
                }
                toMoveSet.add(nodeId);
            }
        }
        for (i = 0; i < itemsToMove; ++i) {
            oldI = oldToNewMap.keyAt(i);
            int newI = oldToNewMap.valueAt(i);
            long newOffset = (long)newI * (long)this.nodeEntryBytes;
            long oldOffset = (long)oldI * (long)this.nodeEntryBytes;
            for (long j = 0; j < (long)this.nodeEntryBytes; j += 4) {
                this.nodes.setInt(newOffset + j, this.nodes.getInt(oldOffset + j));
            }
        }
        AllEdgesIterator iter = this.getAllEdges();
        while (iter.next()) {
            int updatedB;
            int nodeA = iter.getBaseNode();
            int nodeB = iter.getAdjNode();
            if (!toMoveSet.contains(nodeA) && !toMoveSet.contains(nodeB)) continue;
            int updatedA = oldToNewMap.get(nodeA);
            if (updatedA < 0) {
                updatedA = nodeA;
            }
            if ((updatedB = oldToNewMap.get(nodeB)) < 0) {
                updatedB = nodeB;
            }
            int edgeId = iter.getEdge();
            long edgePointer = this.edgeAccess.toPointer(edgeId);
            int linkA = this.edgeAccess.getEdgeRef(nodeA, nodeB, edgePointer);
            int linkB = this.edgeAccess.getEdgeRef(nodeB, nodeA, edgePointer);
            long flags = this.edgeAccess.getFlags_(edgePointer, false);
            this.edgeAccess.writeEdge(edgeId, updatedA, updatedB, linkA, linkB);
            this.edgeAccess.setFlags_(edgePointer, updatedA > updatedB, flags);
            if (updatedA < updatedB == nodeA < nodeB) continue;
            this.setWayGeometry_(this.fetchWayGeometry_(edgePointer, true, 0, -1, -1), edgePointer, false);
        }
        if (removeNodeCount >= this.nodeCount) {
            throw new IllegalStateException("graph is empty after in-place removal but was " + removeNodeCount);
        }
        this.nodeCount -= removeNodeCount;
        EdgeExplorer explorer = this.createEdgeExplorer();
        if (BaseGraph.isTestingEnabled()) {
            iter = this.getAllEdges();
            while (iter.next()) {
                int base = iter.getBaseNode();
                int adj = iter.getAdjNode();
                String str = "" + iter.getEdge() + ", r.contains(" + base + "):" + this.removedNodes.contains(base) + ", r.contains(" + adj + "):" + this.removedNodes.contains(adj) + ", tr.contains(" + base + "):" + toRemoveSet.contains(base) + ", tr.contains(" + adj + "):" + toRemoveSet.contains(adj) + ", base:" + base + ", adj:" + adj + ", nodeCount:" + this.nodeCount;
                if (adj >= this.nodeCount) {
                    throw new RuntimeException("Adj.node problem with edge " + str);
                }
                if (base >= this.nodeCount) {
                    throw new RuntimeException("Base node problem with edge " + str);
                }
                try {
                    explorer.setBaseNode(adj).toString();
                }
                catch (Exception ex) {
                    LoggerFactory.getLogger(this.getClass()).error("adj:" + adj);
                }
                try {
                    explorer.setBaseNode(base).toString();
                }
                catch (Exception ex) {
                    LoggerFactory.getLogger(this.getClass()).error("base:" + base);
                }
            }
            explorer.setBaseNode(this.nodeCount - 1).toString();
        }
        this.removedNodes = null;
    }

    @Override
    public GraphExtension getExtension() {
        return this.extStorage;
    }

    public void setAdditionalEdgeField(long edgePointer, int value) {
        if (!this.extStorage.isRequireEdgeField() || this.E_ADDITIONAL < 0) {
            throw new AssertionError((Object)"This graph does not support an additional edge field.");
        }
        this.edges.setInt(edgePointer + (long)this.E_ADDITIONAL, value);
    }

    private void setWayGeometry_(PointList pillarNodes, long edgePointer, boolean reverse) {
        if (pillarNodes != null && !pillarNodes.isEmpty()) {
            if (pillarNodes.getDimension() != this.nodeAccess.getDimension()) {
                throw new IllegalArgumentException("Cannot use pointlist which is " + pillarNodes.getDimension() + "D for graph which is " + this.nodeAccess.getDimension() + "D");
            }
            int len = pillarNodes.getSize();
            int dim = this.nodeAccess.getDimension();
            int tmpRef = this.nextGeoRef(len * dim);
            this.edges.setInt(edgePointer + (long)this.E_GEO, tmpRef);
            long geoRef = (long)tmpRef * 4;
            byte[] bytes = new byte[len * dim * 4 + 4];
            this.ensureGeometry(geoRef, bytes.length);
            this.bitUtil.fromInt(bytes, len, 0);
            if (reverse) {
                pillarNodes.reverse();
            }
            int tmpOffset = 4;
            boolean is3D = this.nodeAccess.is3D();
            for (int i = 0; i < len; ++i) {
                double lat = pillarNodes.getLatitude(i);
                this.bitUtil.fromInt(bytes, Helper.degreeToInt(lat), tmpOffset);
                this.bitUtil.fromInt(bytes, Helper.degreeToInt(pillarNodes.getLongitude(i)), tmpOffset += 4);
                tmpOffset += 4;
                if (!is3D) continue;
                this.bitUtil.fromInt(bytes, Helper.eleToInt(pillarNodes.getElevation(i)), tmpOffset);
                tmpOffset += 4;
            }
            this.wayGeometry.setBytes(geoRef, bytes, bytes.length);
        } else {
            this.edges.setInt(edgePointer + (long)this.E_GEO, 0);
        }
    }

    private PointList fetchWayGeometry_(long edgePointer, boolean reverse, int mode, int baseNode, int adjNode) {
        long geoRef = this.edges.getInt(edgePointer + (long)this.E_GEO);
        int count = 0;
        byte[] bytes = null;
        if (geoRef > 0) {
            count = this.wayGeometry.getInt(geoRef *= 4);
            bytes = new byte[count * this.nodeAccess.getDimension() * 4];
            this.wayGeometry.getBytes(geoRef += 4, bytes, bytes.length);
        } else if (mode == 0) {
            return PointList.EMPTY;
        }
        PointList pillarNodes = new PointList(count + mode, this.nodeAccess.is3D());
        if (reverse) {
            if ((mode & 2) != 0) {
                pillarNodes.add(this.nodeAccess, adjNode);
            }
        } else if ((mode & 1) != 0) {
            pillarNodes.add(this.nodeAccess, baseNode);
        }
        int index = 0;
        for (int i = 0; i < count; ++i) {
            double lat = Helper.intToDegree(this.bitUtil.toInt(bytes, index));
            double lon = Helper.intToDegree(this.bitUtil.toInt(bytes, index += 4));
            index += 4;
            if (this.nodeAccess.is3D()) {
                pillarNodes.add(lat, lon, Helper.intToEle(this.bitUtil.toInt(bytes, index)));
                index += 4;
                continue;
            }
            pillarNodes.add(lat, lon);
        }
        if (reverse) {
            if ((mode & 1) != 0) {
                pillarNodes.add(this.nodeAccess, baseNode);
            }
            pillarNodes.reverse();
        } else if ((mode & 2) != 0) {
            pillarNodes.add(this.nodeAccess, adjNode);
        }
        return pillarNodes;
    }

    private void setName(long edgePointer, String name) {
        int nameIndexRef = (int)this.nameIndex.put(name);
        if (nameIndexRef < 0) {
            throw new IllegalStateException("Too many names are stored, currently limited to int pointer");
        }
        this.edges.setInt(edgePointer + (long)this.E_NAME, nameIndexRef);
    }

    GHBitSet getRemovedNodes() {
        if (this.removedNodes == null) {
            this.removedNodes = new GHBitSetImpl(this.getNodes());
        }
        return this.removedNodes;
    }

    private static boolean isTestingEnabled() {
        return false;
    }

    private void ensureGeometry(long bytePos, int byteLength) {
        this.wayGeometry.ensureCapacity(bytePos + (long)byteLength);
    }

    private int nextGeoRef(int arrayLength) {
        int tmp = this.maxGeoRef;
        this.maxGeoRef += arrayLength + 1;
        return tmp;
    }

    static abstract class CommonEdgeIterator
    implements EdgeIteratorState {
        protected long edgePointer;
        protected int baseNode;
        protected int adjNode;
        boolean reverse = false;
        protected EdgeAccess edgeAccess;
        final BaseGraph baseGraph;
        boolean freshFlags;
        private long cachedFlags;
        int edgeId = -1;

        public CommonEdgeIterator(long edgePointer, EdgeAccess edgeAccess, BaseGraph baseGraph) {
            this.edgePointer = edgePointer;
            this.edgeAccess = edgeAccess;
            this.baseGraph = baseGraph;
        }

        @Override
        public final int getBaseNode() {
            return this.baseNode;
        }

        @Override
        public final int getAdjNode() {
            return this.adjNode;
        }

        @Override
        public final double getDistance() {
            return this.edgeAccess.getDist(this.edgePointer);
        }

        @Override
        public final EdgeIteratorState setDistance(double dist) {
            this.edgeAccess.setDist(this.edgePointer, dist);
            return this;
        }

        final long getDirectFlags() {
            if (!this.freshFlags) {
                this.cachedFlags = this.edgeAccess.getFlags_(this.edgePointer, this.reverse);
                this.freshFlags = true;
            }
            return this.cachedFlags;
        }

        @Override
        public long getFlags() {
            return this.getDirectFlags();
        }

        @Override
        public final EdgeIteratorState setFlags(long fl) {
            this.edgeAccess.setFlags_(this.edgePointer, this.reverse, fl);
            this.cachedFlags = fl;
            this.freshFlags = true;
            return this;
        }

        @Override
        public final int getAdditionalField() {
            return this.baseGraph.edges.getInt(this.edgePointer + (long)this.baseGraph.E_ADDITIONAL);
        }

        @Override
        public final EdgeIteratorState setAdditionalField(int value) {
            this.baseGraph.setAdditionalEdgeField(this.edgePointer, value);
            return this;
        }

        @Override
        public final EdgeIteratorState copyPropertiesTo(EdgeIteratorState edge) {
            return this.baseGraph.copyProperties(this, edge);
        }

        @Override
        public boolean isForward(FlagEncoder encoder) {
            return encoder.isForward(this.getDirectFlags());
        }

        @Override
        public boolean isBackward(FlagEncoder encoder) {
            return encoder.isBackward(this.getDirectFlags());
        }

        @Override
        public EdgeIteratorState setWayGeometry(PointList pillarNodes) {
            this.baseGraph.setWayGeometry_(pillarNodes, this.edgePointer, this.reverse);
            return this;
        }

        @Override
        public PointList fetchWayGeometry(int mode) {
            return this.baseGraph.fetchWayGeometry_(this.edgePointer, this.reverse, mode, this.getBaseNode(), this.getAdjNode());
        }

        @Override
        public int getEdge() {
            return this.edgeId;
        }

        @Override
        public String getName() {
            int nameIndexRef = this.baseGraph.edges.getInt(this.edgePointer + (long)this.baseGraph.E_NAME);
            return this.baseGraph.nameIndex.get(nameIndexRef);
        }

        @Override
        public EdgeIteratorState setName(String name) {
            this.baseGraph.setName(this.edgePointer, name);
            return this;
        }

        @Override
        public final boolean getBoolean(int key, boolean reverse, boolean _default) {
            return _default;
        }

        public final String toString() {
            return "" + this.getEdge() + " " + this.getBaseNode() + "-" + this.getAdjNode();
        }
    }

    protected static class AllEdgeIterator
    extends CommonEdgeIterator
    implements AllEdgesIterator {
        public AllEdgeIterator(BaseGraph baseGraph) {
            this(baseGraph, baseGraph.edgeAccess);
        }

        private AllEdgeIterator(BaseGraph baseGraph, EdgeAccess edgeAccess) {
            super(-1, edgeAccess, baseGraph);
        }

        @Override
        public int getMaxId() {
            return this.baseGraph.edgeCount;
        }

        @Override
        public boolean next() {
            do {
                ++this.edgeId;
                this.edgePointer = (long)this.edgeId * (long)this.edgeAccess.getEntryBytes();
                if (!this.checkRange()) {
                    return false;
                }
                this.baseNode = this.edgeAccess.edges.getInt(this.edgePointer + (long)this.edgeAccess.E_NODEA);
            } while (this.baseNode == -1);
            this.freshFlags = false;
            this.adjNode = this.edgeAccess.edges.getInt(this.edgePointer + (long)this.edgeAccess.E_NODEB);
            this.reverse = false;
            return true;
        }

        protected boolean checkRange() {
            return this.edgeId < this.baseGraph.edgeCount;
        }

        @Override
        public final EdgeIteratorState detach(boolean reverseArg) {
            if (this.edgePointer < 0) {
                throw new IllegalStateException("call next before detaching");
            }
            AllEdgeIterator iter = new AllEdgeIterator(this.baseGraph, this.edgeAccess);
            iter.edgeId = this.edgeId;
            iter.edgePointer = this.edgePointer;
            if (reverseArg) {
                iter.reverse = !this.reverse;
                iter.baseNode = this.adjNode;
                iter.adjNode = this.baseNode;
            } else {
                iter.reverse = this.reverse;
                iter.baseNode = this.baseNode;
                iter.adjNode = this.adjNode;
            }
            return iter;
        }
    }

    protected static class EdgeIterable
    extends CommonEdgeIterator
    implements EdgeExplorer,
    EdgeIterator {
        final EdgeFilter filter;
        int nextEdgeId;

        public EdgeIterable(BaseGraph baseGraph, EdgeAccess edgeAccess, EdgeFilter filter) {
            super(-1, edgeAccess, baseGraph);
            if (filter == null) {
                throw new IllegalArgumentException("Instead null filter use EdgeFilter.ALL_EDGES");
            }
            this.filter = filter;
        }

        final void setEdgeId(int edgeId) {
            this.nextEdgeId = this.edgeId = edgeId;
        }

        final boolean init(int tmpEdgeId, int expectedAdjNode) {
            this.setEdgeId(tmpEdgeId);
            if (tmpEdgeId != -1) {
                this.selectEdgeAccess();
                this.edgePointer = this.edgeAccess.toPointer(tmpEdgeId);
            }
            this.baseNode = this.edgeAccess.edges.getInt(this.edgePointer + (long)this.edgeAccess.E_NODEA);
            if (this.baseNode == -1) {
                throw new IllegalStateException("content of edgeId " + this.edgeId + " is marked as invalid - ie. the edge is already removed!");
            }
            this.adjNode = this.edgeAccess.edges.getInt(this.edgePointer + (long)this.edgeAccess.E_NODEB);
            this.nextEdgeId = -1;
            if (expectedAdjNode == this.adjNode || expectedAdjNode == Integer.MIN_VALUE) {
                this.reverse = false;
                return true;
            }
            if (expectedAdjNode == this.baseNode) {
                this.reverse = true;
                this.baseNode = this.adjNode;
                this.adjNode = expectedAdjNode;
                return true;
            }
            return false;
        }

        final void _setBaseNode(int baseNode) {
            this.baseNode = baseNode;
        }

        @Override
        public EdgeIterator setBaseNode(int baseNode) {
            this.setEdgeId(this.baseGraph.edgeAccess.getEdgeRef(baseNode));
            this._setBaseNode(baseNode);
            return this;
        }

        protected void selectEdgeAccess() {
        }

        @Override
        public final boolean next() {
            do {
                if (this.nextEdgeId == -1) {
                    return false;
                }
                this.selectEdgeAccess();
                this.edgePointer = this.edgeAccess.toPointer(this.nextEdgeId);
                this.edgeId = this.nextEdgeId;
                this.adjNode = this.edgeAccess.getOtherNode(this.baseNode, this.edgePointer);
                this.reverse = this.baseNode > this.adjNode;
                this.freshFlags = false;
                this.nextEdgeId = this.edgeAccess.getEdgeRef(this.baseNode, this.adjNode, this.edgePointer);
                assert (this.nextEdgeId != this.edgeId);
            } while (!this.filter.accept(this));
            return true;
        }

        @Override
        public EdgeIteratorState detach(boolean reverseArg) {
            boolean ret;
            if (this.edgeId == this.nextEdgeId || this.edgeId == -1) {
                throw new IllegalStateException("call next before detaching or setEdgeId (edgeId:" + this.edgeId + " vs. next " + this.nextEdgeId + ")");
            }
            EdgeIterable iter = this.edgeAccess.createSingleEdge(this.filter);
            if (reverseArg) {
                ret = iter.init(this.edgeId, this.baseNode);
                iter.reverse = !this.reverse;
            } else {
                ret = iter.init(this.edgeId, this.adjNode);
            }
            assert (ret);
            return iter;
        }
    }

}

