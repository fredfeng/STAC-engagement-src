/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.chart;

import edu.cyberapex.chart.Chart;
import edu.cyberapex.chart.Limit;

public class LimitBuilder {
    private Chart chart;

    public LimitBuilder fixChart(Chart chart) {
        this.chart = chart;
        return this;
    }

    public Limit generateLimit() {
        return new Limit(this.chart);
    }
}

