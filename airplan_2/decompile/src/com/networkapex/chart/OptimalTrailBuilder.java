/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.chart;

import com.networkapex.chart.Graph;
import com.networkapex.chart.OptimalTrail;

public class OptimalTrailBuilder {
    private Graph graph;

    public OptimalTrailBuilder setGraph(Graph graph) {
        this.graph = graph;
        return this;
    }

    public OptimalTrail generateOptimalTrail() {
        return new OptimalTrail(this.graph);
    }
}

