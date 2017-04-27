/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.storage;

import com.graphhopper.coll.GHBitSet;
import com.graphhopper.routing.util.AllEdgesIterator;
import com.graphhopper.routing.util.EdgeFilter;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.routing.util.Weighting;
import com.graphhopper.storage.BaseGraph;
import com.graphhopper.storage.CHGraph;
import com.graphhopper.storage.CHGraphImpl;
import com.graphhopper.storage.DAType;
import com.graphhopper.storage.DataAccess;
import com.graphhopper.storage.Directory;
import com.graphhopper.storage.Graph;
import com.graphhopper.storage.GraphExtension;
import com.graphhopper.storage.GraphStorage;
import com.graphhopper.storage.InternalGraphEventListener;
import com.graphhopper.storage.NodeAccess;
import com.graphhopper.storage.StorableProperties;
import com.graphhopper.util.EdgeExplorer;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.shapes.BBox;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class GraphHopperStorage
implements GraphStorage,
Graph {
    private final Directory dir;
    private EncodingManager encodingManager;
    private final StorableProperties properties;
    private final BaseGraph baseGraph;
    private final Collection<CHGraphImpl> chGraphs = new ArrayList<CHGraphImpl>(5);

    public GraphHopperStorage(Directory dir, EncodingManager encodingManager, boolean withElevation, GraphExtension extendedStorage) {
        this(Collections.emptyList(), dir, encodingManager, withElevation, extendedStorage);
    }

    public GraphHopperStorage(List<? extends Weighting> chWeightings, Directory dir, EncodingManager encodingManager, boolean withElevation, GraphExtension extendedStorage) {
        if (extendedStorage == null) {
            throw new IllegalArgumentException("GraphExtension cannot be null, use NoOpExtension");
        }
        this.encodingManager = encodingManager;
        this.dir = dir;
        this.properties = new StorableProperties(dir);
        InternalGraphEventListener listener = new InternalGraphEventListener(){

            @Override
            public void initStorage() {
                for (CHGraphImpl cg : GraphHopperStorage.this.chGraphs) {
                    cg.initStorage();
                }
            }

            @Override
            public void freeze() {
                for (CHGraphImpl cg : GraphHopperStorage.this.chGraphs) {
                    cg._freeze();
                }
            }
        };
        this.baseGraph = new BaseGraph(dir, encodingManager, withElevation, listener, extendedStorage);
        for (Weighting w : chWeightings) {
            this.chGraphs.add(new CHGraphImpl(w, dir, this.baseGraph));
        }
    }

    public <T extends Graph> T getGraph(Class<T> clazz, Weighting weighting) {
        if (clazz.equals(Graph.class)) {
            return (T)this.baseGraph;
        }
        if (this.chGraphs.isEmpty()) {
            throw new IllegalStateException("Cannot find graph implementation for " + clazz);
        }
        if (weighting == null) {
            throw new IllegalStateException("Cannot find CHGraph with null weighting");
        }
        ArrayList<Weighting> existing = new ArrayList<Weighting>();
        for (CHGraphImpl cg : this.chGraphs) {
            if (cg.getWeighting() == weighting) {
                return (T)cg;
            }
            existing.add(cg.getWeighting());
        }
        throw new IllegalStateException("Cannot find CHGraph for specified weighting: " + weighting + ", existing:" + existing);
    }

    public <T extends Graph> T getGraph(Class<T> clazz) {
        if (clazz.equals(Graph.class)) {
            return (T)this.baseGraph;
        }
        if (this.chGraphs.isEmpty()) {
            throw new IllegalStateException("Cannot find graph implementation for " + clazz);
        }
        CHGraph cg = this.chGraphs.iterator().next();
        return (T)cg;
    }

    public boolean isCHPossible() {
        return !this.chGraphs.isEmpty();
    }

    public List<Weighting> getCHWeightings() {
        ArrayList<Weighting> list = new ArrayList<Weighting>(this.chGraphs.size());
        for (CHGraphImpl cg : this.chGraphs) {
            list.add(cg.getWeighting());
        }
        return list;
    }

    @Override
    public Directory getDirectory() {
        return this.dir;
    }

    @Override
    public void setSegmentSize(int bytes) {
        this.baseGraph.setSegmentSize(bytes);
        for (CHGraphImpl cg : this.chGraphs) {
            cg.setSegmentSize(bytes);
        }
    }

    @Override
    public GraphHopperStorage create(long byteCount) {
        this.baseGraph.checkInit();
        if (this.encodingManager == null) {
            throw new IllegalStateException("EncodingManager can only be null if you call loadExisting");
        }
        long initSize = Math.max(byteCount, 100);
        this.properties.create(100);
        this.properties.put("graph.bytesForFlags", this.encodingManager.getBytesForFlags());
        this.properties.put("graph.flagEncoders", this.encodingManager.toDetailsString());
        this.properties.put("graph.byteOrder", this.dir.getByteOrder());
        this.properties.put("graph.dimension", this.baseGraph.nodeAccess.getDimension());
        this.properties.putCurrentVersions();
        this.baseGraph.create(initSize);
        for (CHGraphImpl cg : this.chGraphs) {
            cg.create(byteCount);
        }
        this.properties.put("graph.chWeightings", this.getCHWeightings().toString());
        return this;
    }

    @Override
    public EncodingManager getEncodingManager() {
        return this.encodingManager;
    }

    @Override
    public StorableProperties getProperties() {
        return this.properties;
    }

    public void setAdditionalEdgeField(long edgePointer, int value) {
        this.baseGraph.setAdditionalEdgeField(edgePointer, value);
    }

    @Override
    public void markNodeRemoved(int index) {
        this.baseGraph.getRemovedNodes().add(index);
    }

    @Override
    public boolean isNodeRemoved(int index) {
        return this.baseGraph.getRemovedNodes().contains(index);
    }

    @Override
    public void optimize() {
        if (this.isFrozen()) {
            throw new IllegalStateException("do not optimize after graph was frozen");
        }
        int delNodes = this.baseGraph.getRemovedNodes().getCardinality();
        if (delNodes <= 0) {
            return;
        }
        this.baseGraph.inPlaceNodeRemove(delNodes);
        this.baseGraph.trimToSize();
    }

    @Override
    public boolean loadExisting() {
        this.baseGraph.checkInit();
        if (this.properties.loadExisting()) {
            this.properties.checkVersions(false);
            String acceptStr = this.properties.get("graph.flagEncoders");
            if (this.encodingManager == null) {
                if (acceptStr.isEmpty()) {
                    throw new IllegalStateException("No EncodingManager was configured. And no one was found in the graph: " + this.dir.getLocation());
                }
                int bytesForFlags = 4;
                if ("8".equals(this.properties.get("graph.bytesForFlags"))) {
                    bytesForFlags = 8;
                }
                this.encodingManager = new EncodingManager(acceptStr, bytesForFlags);
            } else if (!acceptStr.isEmpty() && !this.encodingManager.toDetailsString().equalsIgnoreCase(acceptStr)) {
                throw new IllegalStateException("Encoding does not match:\nGraphhopper config: " + this.encodingManager.toDetailsString() + "\nGraph: " + acceptStr + ", dir:" + this.dir.getLocation());
            }
            String byteOrder = this.properties.get("graph.byteOrder");
            if (!byteOrder.equalsIgnoreCase("" + this.dir.getByteOrder())) {
                throw new IllegalStateException("Configured byteOrder (" + byteOrder + ") is not equal to byteOrder of loaded graph (" + this.dir.getByteOrder() + ")");
            }
            String dim = this.properties.get("graph.dimension");
            this.baseGraph.loadExisting(dim);
            String loadedCHWeightings = this.properties.get("graph.chWeightings");
            String configuredCHWeightings = this.getCHWeightings().toString();
            if (!loadedCHWeightings.equals(configuredCHWeightings)) {
                throw new IllegalStateException("Configured graph.chWeightings: " + configuredCHWeightings + " is not equal to loaded " + loadedCHWeightings);
            }
            for (CHGraphImpl cg : this.chGraphs) {
                if (cg.loadExisting()) continue;
                throw new IllegalStateException("Cannot load " + cg);
            }
            return true;
        }
        return false;
    }

    @Override
    public void flush() {
        for (CHGraphImpl cg : this.chGraphs) {
            cg.setEdgesHeader();
            cg.flush();
        }
        this.baseGraph.flush();
        this.properties.flush();
    }

    @Override
    public void close() {
        this.properties.close();
        this.baseGraph.close();
        for (CHGraphImpl cg : this.chGraphs) {
            cg.close();
        }
    }

    @Override
    public boolean isClosed() {
        return this.baseGraph.nodes.isClosed();
    }

    @Override
    public long getCapacity() {
        long cnt = this.baseGraph.getCapacity() + this.properties.getCapacity();
        for (CHGraphImpl cg : this.chGraphs) {
            cnt += cg.getCapacity();
        }
        return cnt;
    }

    public void freeze() {
        if (!this.baseGraph.isFrozen()) {
            this.baseGraph.freeze();
        }
    }

    boolean isFrozen() {
        return this.baseGraph.isFrozen();
    }

    @Override
    public String toDetailsString() {
        String str = this.baseGraph.toDetailsString();
        for (CHGraphImpl cg : this.chGraphs) {
            str = str + ", " + cg.toDetailsString();
        }
        return str;
    }

    public String toString() {
        return (this.isCHPossible() ? "CH|" : "") + this.encodingManager + "|" + this.getDirectory().getDefaultType() + "|" + this.baseGraph.nodeAccess.getDimension() + "D" + "|" + this.baseGraph.extStorage + "|" + this.getProperties().versionsToString();
    }

    @Override
    public Graph getBaseGraph() {
        return this.baseGraph;
    }

    @Override
    public final int getNodes() {
        return this.baseGraph.getNodes();
    }

    @Override
    public final NodeAccess getNodeAccess() {
        return this.baseGraph.getNodeAccess();
    }

    @Override
    public final BBox getBounds() {
        return this.baseGraph.getBounds();
    }

    @Override
    public final EdgeIteratorState edge(int a, int b) {
        return this.baseGraph.edge(a, b);
    }

    @Override
    public final EdgeIteratorState edge(int a, int b, double distance, boolean bothDirections) {
        return this.baseGraph.edge(a, b, distance, bothDirections);
    }

    @Override
    public final EdgeIteratorState getEdgeIteratorState(int edgeId, int adjNode) {
        return this.baseGraph.getEdgeIteratorState(edgeId, adjNode);
    }

    @Override
    public final AllEdgesIterator getAllEdges() {
        return this.baseGraph.getAllEdges();
    }

    @Override
    public final EdgeExplorer createEdgeExplorer(EdgeFilter filter) {
        return this.baseGraph.createEdgeExplorer(filter);
    }

    @Override
    public final EdgeExplorer createEdgeExplorer() {
        return this.baseGraph.createEdgeExplorer();
    }

    @Override
    public final Graph copyTo(Graph g) {
        return this.baseGraph.copyTo(g);
    }

    @Override
    public final GraphExtension getExtension() {
        return this.baseGraph.getExtension();
    }

}

