/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing;

import com.graphhopper.routing.Path;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.storage.Graph;
import com.graphhopper.util.EdgeWrapper;

public class PathBidir
extends Path {
    public boolean switchWrapper = false;
    public int fromRef = -1;
    public int toRef = -1;
    private EdgeWrapper edgeWFrom;
    private EdgeWrapper edgeWTo;

    public PathBidir(Graph g, FlagEncoder encoder, EdgeWrapper edgesFrom, EdgeWrapper edgesTo) {
        super(g, encoder);
        this.edgeWFrom = edgesFrom;
        this.edgeWTo = edgesTo;
    }

    @Override
    public Path extract() {
        int nodeFrom;
        int edgeId;
        int nodeTo;
        if (this.fromRef < 0 || this.toRef < 0) {
            return this;
        }
        if (this.switchWrapper) {
            int tmp = this.fromRef;
            this.fromRef = this.toRef;
            this.toRef = tmp;
        }
        if ((nodeFrom = this.edgeWFrom.getNode(this.fromRef)) != (nodeTo = this.edgeWTo.getNode(this.toRef))) {
            throw new IllegalStateException("'to' and 'from' have to be the same. " + this.toString());
        }
        int currRef = this.fromRef;
        while (currRef > 0 && (edgeId = this.edgeWFrom.getEdgeId(currRef)) >= 0) {
            this.processEdge(edgeId, nodeFrom);
            currRef = this.edgeWFrom.getParent(currRef);
            nodeFrom = this.edgeWFrom.getNode(currRef);
        }
        this.reverseOrder();
        this.setFromNode(nodeFrom);
        currRef = this.toRef;
        while (currRef > 0 && (edgeId = this.edgeWTo.getEdgeId(currRef)) >= 0) {
            int tmpRef = this.edgeWTo.getParent(currRef);
            nodeTo = this.edgeWTo.getNode(tmpRef);
            this.processEdge(edgeId, nodeTo);
            currRef = tmpRef;
        }
        this.setEndNode(nodeTo);
        return this.setFound(true);
    }
}

