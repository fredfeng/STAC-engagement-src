/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.util;

import com.graphhopper.util.CHEdgeIterator;
import com.graphhopper.util.EdgeExplorer;

public interface CHEdgeExplorer
extends EdgeExplorer {
    @Override
    public CHEdgeIterator setBaseNode(int var1);
}

