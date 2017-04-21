/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.storage;

import com.graphhopper.routing.util.AllCHEdgesIterator;
import com.graphhopper.routing.util.EdgeFilter;
import com.graphhopper.storage.Graph;
import com.graphhopper.util.CHEdgeExplorer;
import com.graphhopper.util.CHEdgeIteratorState;

public interface CHGraph
extends Graph {
    public void setLevel(int var1, int var2);

    public int getLevel(int var1);

    public boolean isShortcut(int var1);

    public CHEdgeIteratorState shortcut(int var1, int var2);

    @Override
    public CHEdgeIteratorState getEdgeIteratorState(int var1, int var2);

    @Override
    public CHEdgeExplorer createEdgeExplorer();

    @Override
    public CHEdgeExplorer createEdgeExplorer(EdgeFilter var1);

    @Override
    public AllCHEdgesIterator getAllEdges();
}

