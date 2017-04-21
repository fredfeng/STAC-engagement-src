/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.chart;

import com.networkapex.chart.Graph;
import com.networkapex.chart.Limit;

public class LimitBuilder {
    private Graph graph;

    public LimitBuilder fixGraph(Graph graph) {
        this.graph = graph;
        return this;
    }

    public Limit generateLimit() {
        return new Limit(this.graph);
    }
}

