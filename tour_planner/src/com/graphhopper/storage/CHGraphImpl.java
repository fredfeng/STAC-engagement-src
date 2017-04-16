/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.storage;

import com.graphhopper.routing.ch.PrepareEncoder;
import com.graphhopper.routing.util.AllCHEdgesIterator;
import com.graphhopper.routing.util.AllEdgesIterator;
import com.graphhopper.routing.util.EdgeFilter;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.routing.util.Weighting;
import com.graphhopper.storage.BaseGraph;
import com.graphhopper.storage.CHGraph;
import com.graphhopper.storage.DataAccess;
import com.graphhopper.storage.Directory;
import com.graphhopper.storage.EdgeAccess;
import com.graphhopper.storage.Graph;
import com.graphhopper.storage.GraphExtension;
import com.graphhopper.storage.NodeAccess;
import com.graphhopper.storage.Storable;
import com.graphhopper.util.BitUtil;
import com.graphhopper.util.CHEdgeExplorer;
import com.graphhopper.util.CHEdgeIterator;
import com.graphhopper.util.CHEdgeIteratorState;
import com.graphhopper.util.EdgeExplorer;
import com.graphhopper.util.EdgeIterator;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.Helper;
import com.graphhopper.util.PointList;
import com.graphhopper.util.shapes.BBox;

public class CHGraphImpl
implements CHGraph,
Storable<CHGraph> {
    private static final double WEIGHT_FACTOR = 1000.0;
    private static final long MAX_WEIGHT_LONG = 2147483644;
    private static final double MAX_WEIGHT = 536870.911;
    private int N_LEVEL;
    int N_CH_REF;
    private int S_SKIP_EDGE1;
    private int S_SKIP_EDGE2;
    int shortcutEntryBytes;
    private int shortcutCount = 0;
    final DataAccess shortcuts;
    int nodeCHEntryBytes;
    final DataAccess nodesCH;
    final long scDirMask = PrepareEncoder.getScDirMask();
    private final BaseGraph baseGraph;
    private final EdgeAccess chEdgeAccess;
    private final Weighting weighting;

    CHGraphImpl(Weighting w, Directory dir, final BaseGraph baseGraph) {
        if (w == null) {
            throw new IllegalStateException("Weighting for CHGraph cannot be null");
        }
        this.weighting = w;
        this.baseGraph = baseGraph;
        final String name = this.weightingToFileName(w);
        this.nodesCH = dir.find("nodes_ch_" + name);
        this.shortcuts = dir.find("shortcuts_" + name);
        this.chEdgeAccess = new EdgeAccess(this.shortcuts, baseGraph.bitUtil){

            @Override
            final BaseGraph.EdgeIterable createSingleEdge(EdgeFilter edgeFilter) {
                return new CHEdgeIteratorImpl(baseGraph, this, edgeFilter);
            }

            @Override
            final int getEdgeRef(int nodeId) {
                return CHGraphImpl.this.nodesCH.getInt((long)nodeId * (long)CHGraphImpl.this.nodeCHEntryBytes + (long)CHGraphImpl.this.N_CH_REF);
            }

            @Override
            final void setEdgeRef(int nodeId, int edgeId) {
                CHGraphImpl.this.nodesCH.setInt((long)nodeId * (long)CHGraphImpl.this.nodeCHEntryBytes + (long)CHGraphImpl.this.N_CH_REF, edgeId);
            }

            @Override
            final int getEntryBytes() {
                return CHGraphImpl.this.shortcutEntryBytes;
            }

            @Override
            final long toPointer(int shortcutId) {
                assert (this.isInBounds(shortcutId));
                return (long)(shortcutId - baseGraph.edgeCount) * (long)CHGraphImpl.this.shortcutEntryBytes;
            }

            @Override
            final boolean isInBounds(int shortcutId) {
                int tmp = shortcutId - baseGraph.edgeCount;
                return tmp < CHGraphImpl.this.shortcutCount && tmp >= 0;
            }

            @Override
            final long reverseFlags(long edgePointer, long flags) {
                boolean isShortcut;
                boolean bl = isShortcut = edgePointer >= this.toPointer(baseGraph.edgeCount);
                if (!isShortcut) {
                    return baseGraph.edgeAccess.reverseFlags(edgePointer, flags);
                }
                long dir = flags & CHGraphImpl.this.scDirMask;
                if (dir == CHGraphImpl.this.scDirMask || dir == 0) {
                    return flags;
                }
                return flags ^ CHGraphImpl.this.scDirMask;
            }

            public String toString() {
                return "ch edge access " + name;
            }
        };
    }

    public final Weighting getWeighting() {
        return this.weighting;
    }

    public String weightingToFileName(Weighting w) {
        return w.toString().toLowerCase().replaceAll("\\W+", "_");
    }

    @Override
    public boolean isShortcut(int edgeId) {
        assert (this.baseGraph.isFrozen());
        return edgeId >= this.baseGraph.edgeCount;
    }

    @Override
    public final void setLevel(int nodeIndex, int level) {
        this.checkNodeId(nodeIndex);
        this.nodesCH.setInt((long)nodeIndex * (long)this.nodeCHEntryBytes + (long)this.N_LEVEL, level);
    }

    @Override
    public final int getLevel(int nodeIndex) {
        this.checkNodeId(nodeIndex);
        return this.nodesCH.getInt((long)nodeIndex * (long)this.nodeCHEntryBytes + (long)this.N_LEVEL);
    }

    final void checkNodeId(int nodeId) {
        assert (nodeId < this.baseGraph.getNodes());
    }

    @Override
    public CHEdgeIteratorState shortcut(int a, int b) {
        if (!this.baseGraph.isFrozen()) {
            throw new IllegalStateException("Cannot create shortcut if graph is not yet frozen");
        }
        this.checkNodeId(a);
        this.checkNodeId(b);
        int scId = this.chEdgeAccess.internalEdgeAdd(this.nextShortcutId(), a, b);
        CHEdgeIteratorImpl iter = new CHEdgeIteratorImpl(this.baseGraph, this.chEdgeAccess, EdgeFilter.ALL_EDGES);
        boolean ret = iter.init(scId, b);
        assert (ret);
        iter.setSkippedEdges(-1, -1);
        return iter;
    }

    protected int nextShortcutId() {
        int nextSC = this.shortcutCount++;
        if (this.shortcutCount < 0) {
            throw new IllegalStateException("too many shortcuts. new shortcut id would be negative. " + this.toString());
        }
        this.shortcuts.ensureCapacity(((long)this.shortcutCount + 1) * (long)this.shortcutEntryBytes);
        return nextSC + this.baseGraph.edgeCount;
    }

    @Override
    public EdgeIteratorState edge(int a, int b, double distance, boolean bothDirections) {
        return this.edge(a, b).setDistance(distance).setFlags(this.baseGraph.encodingManager.flagsDefault(true, bothDirections));
    }

    @Override
    public CHEdgeIteratorState edge(int a, int b) {
        this.baseGraph.ensureNodeIndex(Math.max(a, b));
        int edgeId = this.baseGraph.edgeAccess.internalEdgeAdd(this.baseGraph.nextEdgeId(), a, b);
        CHEdgeIteratorImpl iter = new CHEdgeIteratorImpl(this.baseGraph, this.baseGraph.edgeAccess, EdgeFilter.ALL_EDGES);
        boolean ret = iter.init(edgeId, b);
        assert (ret);
        return iter;
    }

    @Override
    public CHEdgeExplorer createEdgeExplorer() {
        return this.createEdgeExplorer(EdgeFilter.ALL_EDGES);
    }

    @Override
    public CHEdgeExplorer createEdgeExplorer(EdgeFilter filter) {
        return new CHEdgeIteratorImpl(this.baseGraph, this.chEdgeAccess, filter);
    }

    @Override
    public final CHEdgeIteratorState getEdgeIteratorState(int edgeId, int endNode) {
        if (this.isShortcut(edgeId)) {
            if (!this.chEdgeAccess.isInBounds(edgeId)) {
                throw new IllegalStateException("shortcutId " + edgeId + " out of bounds");
            }
        } else if (!this.baseGraph.edgeAccess.isInBounds(edgeId)) {
            throw new IllegalStateException("edgeId " + edgeId + " out of bounds");
        }
        return (CHEdgeIteratorState)this.chEdgeAccess.getEdgeProps(edgeId, endNode);
    }

    @Override
    public int getNodes() {
        return this.baseGraph.getNodes();
    }

    @Override
    public NodeAccess getNodeAccess() {
        return this.baseGraph.getNodeAccess();
    }

    @Override
    public BBox getBounds() {
        return this.baseGraph.getBounds();
    }

    void _freeze() {
        long maxCapacity = (long)this.getNodes() * (long)this.nodeCHEntryBytes;
        this.nodesCH.ensureCapacity(maxCapacity);
        long baseCapacity = this.baseGraph.nodes.getCapacity();
        long pointer = this.N_CH_REF;
        long basePointer = this.baseGraph.N_EDGE_REF;
        while (pointer < maxCapacity) {
            if (basePointer >= baseCapacity) {
                throw new IllegalStateException("Cannot copy edge refs into ch graph. pointer:" + pointer + ", cap:" + maxCapacity + ", basePtr:" + basePointer + ", baseCap:" + baseCapacity);
            }
            this.nodesCH.setInt(pointer, this.baseGraph.nodes.getInt(basePointer));
            pointer += (long)this.nodeCHEntryBytes;
            basePointer += (long)this.baseGraph.nodeEntryBytes;
        }
    }

    String toDetailsString() {
        return this.toString() + ", shortcuts:" + Helper.nf(this.shortcutCount) + ", nodesCH:(" + this.nodesCH.getCapacity() / 0x100000 + "MB)";
    }

    public void disconnect(CHEdgeExplorer explorer, EdgeIteratorState edgeState) {
        CHEdgeIterator tmpIter = explorer.setBaseNode(edgeState.getAdjNode());
        int tmpPrevEdge = -1;
        while (tmpIter.next()) {
            if (tmpIter.isShortcut() && tmpIter.getEdge() == edgeState.getEdge()) {
                long edgePointer = tmpPrevEdge == -1 ? -1 : (this.isShortcut(tmpPrevEdge) ? this.chEdgeAccess.toPointer(tmpPrevEdge) : this.baseGraph.edgeAccess.toPointer(tmpPrevEdge));
                this.chEdgeAccess.internalEdgeDisconnect(edgeState.getEdge(), edgePointer, edgeState.getAdjNode(), edgeState.getBaseNode());
                break;
            }
            tmpPrevEdge = tmpIter.getEdge();
        }
    }

    @Override
    public AllCHEdgesIterator getAllEdges() {
        return new AllCHEdgesIteratorImpl(this.baseGraph);
    }

    final void setWeight(BaseGraph.CommonEdgeIterator edge, double weight) {
        if (weight < 0.0) {
            throw new IllegalArgumentException("weight cannot be negative but was " + weight);
        }
        long weightLong = weight > 536870.911 ? 2147483644 : (long)(weight * 1000.0) << 2;
        long accessFlags = edge.getDirectFlags() & this.scDirMask;
        edge.setFlags(weightLong | accessFlags);
    }

    final double getWeight(BaseGraph.CommonEdgeIterator edge) {
        long flags32bit = edge.getDirectFlags();
        double weight = (double)(flags32bit >>> 2) / 1000.0;
        if (weight >= 536870.911) {
            return Double.POSITIVE_INFINITY;
        }
        return weight;
    }

    protected int loadEdgesHeader() {
        this.shortcutCount = this.shortcuts.getHeader(0);
        this.shortcutEntryBytes = this.shortcuts.getHeader(4);
        return 3;
    }

    protected int setEdgesHeader() {
        this.shortcuts.setHeader(0, this.shortcutCount);
        this.shortcuts.setHeader(4, this.shortcutEntryBytes);
        return 3;
    }

    @Override
    public GraphExtension getExtension() {
        return this.baseGraph.getExtension();
    }

    @Override
    public Graph getBaseGraph() {
        return this.baseGraph;
    }

    @Override
    public Graph copyTo(Graph g) {
        CHGraphImpl tmpG = (CHGraphImpl)g;
        this.nodesCH.copyTo(tmpG.nodesCH);
        this.shortcuts.copyTo(tmpG.shortcuts);
        tmpG.N_LEVEL = this.N_LEVEL;
        tmpG.N_CH_REF = this.N_CH_REF;
        tmpG.nodeCHEntryBytes = this.nodeCHEntryBytes;
        return g;
    }

    void initStorage() {
        EdgeAccess ea = this.baseGraph.edgeAccess;
        this.chEdgeAccess.init(ea.E_NODEA, ea.E_NODEB, ea.E_LINKA, ea.E_LINKB, ea.E_DIST, ea.E_FLAGS, false);
        this.S_SKIP_EDGE1 = ea.E_FLAGS + 4;
        this.S_SKIP_EDGE2 = this.S_SKIP_EDGE1 + 4;
        this.shortcutEntryBytes = this.S_SKIP_EDGE2 + 4;
        this.N_LEVEL = 0;
        this.N_CH_REF = this.N_LEVEL + 4;
        this.nodeCHEntryBytes = this.N_CH_REF + 4;
    }

    void setSegmentSize(int bytes) {
        this.nodesCH.setSegmentSize(bytes);
        this.shortcuts.setSegmentSize(bytes);
    }

    @Override
    public CHGraph create(long bytes) {
        this.nodesCH.create(bytes);
        this.shortcuts.create(bytes);
        return this;
    }

    @Override
    public boolean loadExisting() {
        if (!this.nodesCH.loadExisting() || !this.shortcuts.loadExisting()) {
            return false;
        }
        this.loadEdgesHeader();
        return true;
    }

    @Override
    public void flush() {
        this.nodesCH.flush();
        this.shortcuts.flush();
    }

    @Override
    public void close() {
        this.nodesCH.close();
        this.shortcuts.close();
    }

    @Override
    public boolean isClosed() {
        return this.nodesCH.isClosed();
    }

    @Override
    public long getCapacity() {
        return this.nodesCH.getCapacity() + this.shortcuts.getCapacity();
    }

    public String toString() {
        return "CHGraph|" + this.getWeighting().toString();
    }

    class AllCHEdgesIteratorImpl
    extends BaseGraph.AllEdgeIterator
    implements AllCHEdgesIterator {
        public AllCHEdgesIteratorImpl(BaseGraph baseGraph) {
            super(baseGraph);
        }

        @Override
        protected final boolean checkRange() {
            if (this.isShortcut()) {
                return this.edgeId < CHGraphImpl.this.shortcutCount;
            }
            if (super.checkRange()) {
                return true;
            }
            this.edgeAccess = CHGraphImpl.this.chEdgeAccess;
            this.edgeId = 0;
            this.edgePointer = (long)this.edgeId * (long)CHGraphImpl.this.shortcutEntryBytes;
            return this.edgeId < CHGraphImpl.this.shortcutCount;
        }

        @Override
        public int getEdge() {
            if (this.isShortcut()) {
                return this.baseGraph.edgeCount + this.edgeId;
            }
            return super.getEdge();
        }

        @Override
        public boolean isBackward(FlagEncoder encoder) {
            assert (encoder == CHGraphImpl.this.weighting.getFlagEncoder());
            if (this.isShortcut()) {
                return (this.getDirectFlags() & PrepareEncoder.getScBwdDir()) != 0;
            }
            return encoder.isBackward(this.getDirectFlags());
        }

        @Override
        public boolean isForward(FlagEncoder encoder) {
            assert (encoder == CHGraphImpl.this.weighting.getFlagEncoder());
            if (this.isShortcut()) {
                return (this.getDirectFlags() & PrepareEncoder.getScFwdDir()) != 0;
            }
            return encoder.isForward(this.getDirectFlags());
        }

        @Override
        public final long getFlags() {
            if (this.isShortcut()) {
                throw new IllegalStateException("Shortcut should not need to return raw flags!");
            }
            return this.getDirectFlags();
        }

        @Override
        public int getMaxId() {
            return super.getMaxId() + CHGraphImpl.this.shortcutCount;
        }

        @Override
        public final void setSkippedEdges(int edge1, int edge2) {
            this.baseGraph.edges.setInt(this.edgePointer + (long)CHGraphImpl.this.S_SKIP_EDGE1, edge1);
            this.baseGraph.edges.setInt(this.edgePointer + (long)CHGraphImpl.this.S_SKIP_EDGE2, edge2);
        }

        @Override
        public final int getSkippedEdge1() {
            return this.baseGraph.edges.getInt(this.edgePointer + (long)CHGraphImpl.this.S_SKIP_EDGE1);
        }

        @Override
        public final int getSkippedEdge2() {
            return this.baseGraph.edges.getInt(this.edgePointer + (long)CHGraphImpl.this.S_SKIP_EDGE2);
        }

        @Override
        public final boolean isShortcut() {
            assert (this.baseGraph.isFrozen());
            return this.edgeAccess == CHGraphImpl.this.chEdgeAccess;
        }

        @Override
        public final CHEdgeIteratorState setWeight(double weight) {
            CHGraphImpl.this.setWeight(this, weight);
            return this;
        }

        @Override
        public final double getWeight() {
            return CHGraphImpl.this.getWeight(this);
        }

        @Override
        public boolean canBeOverwritten(long flags) {
            return PrepareEncoder.canBeOverwritten(this.getDirectFlags(), flags);
        }
    }

    class CHEdgeIteratorImpl
    extends BaseGraph.EdgeIterable
    implements CHEdgeExplorer,
    CHEdgeIterator {
        public CHEdgeIteratorImpl(BaseGraph baseGraph, EdgeAccess edgeAccess, EdgeFilter filter) {
            super(baseGraph, edgeAccess, filter);
        }

        @Override
        public final long getFlags() {
            this.checkShortcut(false, "getFlags");
            return super.getDirectFlags();
        }

        @Override
        public final CHEdgeIterator setBaseNode(int baseNode) {
            assert (this.baseGraph.isFrozen());
            this.setEdgeId(CHGraphImpl.this.chEdgeAccess.getEdgeRef(baseNode));
            this._setBaseNode(baseNode);
            return this;
        }

        @Override
        public final void setSkippedEdges(int edge1, int edge2) {
            this.checkShortcut(true, "setSkippedEdges");
            if (EdgeIterator.Edge.isValid(edge1) != EdgeIterator.Edge.isValid(edge2)) {
                throw new IllegalStateException("Skipped edges of a shortcut needs to be both valid or invalid but they were not " + edge1 + ", " + edge2);
            }
            CHGraphImpl.this.shortcuts.setInt(this.edgePointer + (long)CHGraphImpl.this.S_SKIP_EDGE1, edge1);
            CHGraphImpl.this.shortcuts.setInt(this.edgePointer + (long)CHGraphImpl.this.S_SKIP_EDGE2, edge2);
        }

        @Override
        public final int getSkippedEdge1() {
            this.checkShortcut(true, "getSkippedEdge1");
            return CHGraphImpl.this.shortcuts.getInt(this.edgePointer + (long)CHGraphImpl.this.S_SKIP_EDGE1);
        }

        @Override
        public final int getSkippedEdge2() {
            this.checkShortcut(true, "getSkippedEdge2");
            return CHGraphImpl.this.shortcuts.getInt(this.edgePointer + (long)CHGraphImpl.this.S_SKIP_EDGE2);
        }

        @Override
        public final boolean isShortcut() {
            return this.edgeId >= this.baseGraph.edgeCount;
        }

        @Override
        public boolean isBackward(FlagEncoder encoder) {
            assert (encoder == CHGraphImpl.this.weighting.getFlagEncoder());
            if (this.isShortcut()) {
                return (this.getDirectFlags() & PrepareEncoder.getScBwdDir()) != 0;
            }
            return encoder.isBackward(this.getDirectFlags());
        }

        @Override
        public boolean isForward(FlagEncoder encoder) {
            assert (encoder == CHGraphImpl.this.weighting.getFlagEncoder());
            if (this.isShortcut()) {
                return (this.getDirectFlags() & PrepareEncoder.getScFwdDir()) != 0;
            }
            return encoder.isForward(this.getDirectFlags());
        }

        @Override
        public final CHEdgeIteratorState setWeight(double weight) {
            this.checkShortcut(true, "setWeight");
            CHGraphImpl.this.setWeight(this, weight);
            return this;
        }

        @Override
        public final double getWeight() {
            this.checkShortcut(true, "getWeight");
            return CHGraphImpl.this.getWeight(this);
        }

        @Override
        protected final void selectEdgeAccess() {
            this.edgeAccess = this.nextEdgeId < this.baseGraph.edgeCount ? this.baseGraph.edgeAccess : CHGraphImpl.this.chEdgeAccess;
        }

        public void checkShortcut(boolean shouldBeShortcut, String methodName) {
            if (this.isShortcut()) {
                if (!shouldBeShortcut) {
                    throw new IllegalStateException("Cannot call " + methodName + " on shortcut " + this.getEdge());
                }
            } else if (shouldBeShortcut) {
                throw new IllegalStateException("Method " + methodName + " only for shortcuts " + this.getEdge());
            }
        }

        @Override
        public final String getName() {
            this.checkShortcut(false, "getName");
            return super.getName();
        }

        @Override
        public final EdgeIteratorState setName(String name) {
            this.checkShortcut(false, "setName");
            return super.setName(name);
        }

        @Override
        public final PointList fetchWayGeometry(int mode) {
            this.checkShortcut(false, "fetchWayGeometry");
            return super.fetchWayGeometry(mode);
        }

        @Override
        public final EdgeIteratorState setWayGeometry(PointList list) {
            this.checkShortcut(false, "setWayGeometry");
            return super.setWayGeometry(list);
        }

        @Override
        public boolean canBeOverwritten(long flags) {
            return PrepareEncoder.canBeOverwritten(this.getDirectFlags(), flags);
        }
    }

}

