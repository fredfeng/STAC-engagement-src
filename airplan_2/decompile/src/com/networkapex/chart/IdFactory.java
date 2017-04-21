/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.chart;

import com.networkapex.chart.AdjacencyListGraph;
import com.networkapex.chart.Graph;

public abstract class IdFactory
implements Cloneable {
    public Graph newInstance(String name) {
        return new AdjacencyListGraph(this, name);
    }

    public abstract int grabGraphId();

    public abstract int takeNextVertexId();

    public abstract int getNextComplementaryVertexId(int var1);

    public abstract int takeNextEdgeId();

    public abstract int takeNextComplementaryEdgeId(int var1);

    public abstract int[] obtainVertexIds();

    public abstract int[] takeEdgeIds();

    public abstract IdFactory copy();
}

