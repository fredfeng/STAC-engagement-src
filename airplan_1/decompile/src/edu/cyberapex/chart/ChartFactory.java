/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.chart;

import edu.cyberapex.chart.AdjacencyListChart;
import edu.cyberapex.chart.Chart;
import edu.cyberapex.chart.DefaultIdFactory;
import edu.cyberapex.chart.IdFactory;
import edu.cyberapex.chart.SparseIdFactory;

public class ChartFactory {
    public static Chart newInstance() {
        return ChartFactory.newInstance(new SparseIdFactory());
    }

    public static Chart newInstance(String name) {
        return new DefaultIdFactory().newInstance(name);
    }

    public static Chart newInstance(IdFactory idFactory) {
        return new AdjacencyListChart(idFactory);
    }
}

