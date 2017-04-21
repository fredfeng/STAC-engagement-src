/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.storage;

import com.graphhopper.routing.util.AllEdgesIterator;
import com.graphhopper.routing.util.EdgeFilter;
import com.graphhopper.storage.GraphExtension;
import com.graphhopper.storage.NodeAccess;
import com.graphhopper.util.EdgeExplorer;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.shapes.BBox;

public interface Graph {
    public Graph getBaseGraph();

    public int getNodes();

    public NodeAccess getNodeAccess();

    public BBox getBounds();

    public EdgeIteratorState edge(int var1, int var2);

    public EdgeIteratorState edge(int var1, int var2, double var3, boolean var5);

    public EdgeIteratorState getEdgeIteratorState(int var1, int var2);

    public AllEdgesIterator getAllEdges();

    public EdgeExplorer createEdgeExplorer(EdgeFilter var1);

    public EdgeExplorer createEdgeExplorer();

    public Graph copyTo(Graph var1);

    public GraphExtension getExtension();
}

