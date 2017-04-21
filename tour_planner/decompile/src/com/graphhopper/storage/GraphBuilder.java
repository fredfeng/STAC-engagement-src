/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.storage;

import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.routing.util.Weighting;
import com.graphhopper.storage.CHGraph;
import com.graphhopper.storage.Directory;
import com.graphhopper.storage.GHDirectory;
import com.graphhopper.storage.Graph;
import com.graphhopper.storage.GraphExtension;
import com.graphhopper.storage.GraphHopperStorage;
import com.graphhopper.storage.MMapDirectory;
import com.graphhopper.storage.RAMDirectory;
import com.graphhopper.storage.TurnCostExtension;
import java.util.Arrays;
import java.util.List;

public class GraphBuilder {
    private final EncodingManager encodingManager;
    private String location;
    private boolean mmap;
    private boolean store;
    private boolean elevation;
    private long byteCapacity = 100;
    private Weighting singleCHWeighting;

    public GraphBuilder(EncodingManager encodingManager) {
        this.encodingManager = encodingManager;
    }

    public GraphBuilder setCHGraph(Weighting singleCHWeighting) {
        this.singleCHWeighting = singleCHWeighting;
        return this;
    }

    public GraphBuilder setLocation(String location) {
        this.location = location;
        return this;
    }

    public GraphBuilder setStore(boolean store) {
        this.store = store;
        return this;
    }

    public GraphBuilder setMmap(boolean mmap) {
        this.mmap = mmap;
        return this;
    }

    public GraphBuilder setExpectedSize(byte cap) {
        this.byteCapacity = cap;
        return this;
    }

    public GraphBuilder set3D(boolean withElevation) {
        this.elevation = withElevation;
        return this;
    }

    public boolean hasElevation() {
        return this.elevation;
    }

    public CHGraph chGraphCreate(Weighting singleCHWeighting) {
        return this.setCHGraph(singleCHWeighting).create().getGraph(CHGraph.class, singleCHWeighting);
    }

    public GraphHopperStorage build() {
        GHDirectory dir = this.mmap ? new MMapDirectory(this.location) : new RAMDirectory(this.location, this.store);
        GraphHopperStorage graph = this.encodingManager.needsTurnCostsSupport() || this.singleCHWeighting == null ? new GraphHopperStorage(dir, this.encodingManager, this.elevation, new TurnCostExtension()) : new GraphHopperStorage(Arrays.asList(this.singleCHWeighting), dir, this.encodingManager, this.elevation, new GraphExtension.NoOpExtension());
        return graph;
    }

    public GraphHopperStorage create() {
        return this.build().create(this.byteCapacity);
    }

    public GraphHopperStorage load() {
        GraphHopperStorage gs = this.build();
        if (!gs.loadExisting()) {
            throw new IllegalStateException("Cannot load graph " + this.location);
        }
        return gs;
    }
}

