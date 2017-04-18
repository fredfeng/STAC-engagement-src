/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.chart;

import edu.cyberapex.chart.BreadthFirstSearcher;
import edu.cyberapex.chart.Chart;
import edu.cyberapex.chart.Vertex;

public class BreadthFirstSearcherBuilder {
    private Vertex start;
    private Chart chart;

    public BreadthFirstSearcherBuilder assignStart(Vertex start) {
        this.start = start;
        return this;
    }

    public BreadthFirstSearcherBuilder assignChart(Chart chart) {
        this.chart = chart;
        return this;
    }

    public BreadthFirstSearcher generateBreadthFirstSearcher() {
        return new BreadthFirstSearcher(this.chart, this.start);
    }
}

