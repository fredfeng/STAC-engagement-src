/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.chart;

import com.networkapex.chart.AdjacencyListGraph;
import com.networkapex.chart.DefaultIdFactory;
import com.networkapex.chart.Graph;
import com.networkapex.chart.IdFactory;
import com.networkapex.chart.SparseIdFactory;

public class GraphFactory {
    public static Graph newInstance() {
        return GraphFactory.newInstance(new SparseIdFactory());
    }

    public static Graph newInstance(String name) {
        return new DefaultIdFactory().newInstance(name);
    }

    public static Graph newInstance(IdFactory idFactory) {
        return new AdjacencyListGraph(idFactory);
    }
}

