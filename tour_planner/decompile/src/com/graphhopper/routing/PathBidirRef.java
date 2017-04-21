/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing;

import com.graphhopper.routing.Path;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.storage.EdgeEntry;
import com.graphhopper.storage.Graph;
import com.graphhopper.util.EdgeIterator;
import com.graphhopper.util.StopWatch;

public class PathBidirRef
extends Path {
    protected EdgeEntry edgeTo;
    private boolean switchWrapper = false;

    public PathBidirRef(Graph g, FlagEncoder encoder) {
        super(g, encoder);
    }

    PathBidirRef(PathBidirRef p) {
        super(p);
        this.edgeTo = p.edgeTo;
        this.switchWrapper = p.switchWrapper;
    }

    public PathBidirRef setSwitchToFrom(boolean b) {
        this.switchWrapper = b;
        return this;
    }

    public PathBidirRef setEdgeEntryTo(EdgeEntry edgeTo) {
        this.edgeTo = edgeTo;
        return this;
    }

    @Override
    public Path extract() {
        if (this.edgeEntry == null || this.edgeTo == null) {
            return this;
        }
        if (this.edgeEntry.adjNode != this.edgeTo.adjNode) {
            throw new IllegalStateException("Locations of the 'to'- and 'from'-Edge has to be the same." + this.toString() + ", fromEntry:" + this.edgeEntry + ", toEntry:" + this.edgeTo);
        }
        this.extractSW.start();
        if (this.switchWrapper) {
            EdgeEntry ee = this.edgeEntry;
            this.edgeEntry = this.edgeTo;
            this.edgeTo = ee;
        }
        EdgeEntry currEdge = this.edgeEntry;
        while (EdgeIterator.Edge.isValid(currEdge.edge)) {
            this.processEdge(currEdge.edge, currEdge.adjNode);
            currEdge = currEdge.parent;
        }
        this.setFromNode(currEdge.adjNode);
        this.reverseOrder();
        currEdge = this.edgeTo;
        int tmpEdge = currEdge.edge;
        while (EdgeIterator.Edge.isValid(tmpEdge)) {
            currEdge = currEdge.parent;
            this.processEdge(tmpEdge, currEdge.adjNode);
            tmpEdge = currEdge.edge;
        }
        this.setEndNode(currEdge.adjNode);
        this.extractSW.stop();
        return this.setFound(true);
    }
}

