/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.chart;

import edu.cyberapex.chart.AdjacencyListChart;
import edu.cyberapex.chart.Chart;

public abstract class IdFactory
implements Cloneable {
    public Chart newInstance(String name) {
        return new AdjacencyListChart(this, name);
    }

    public abstract int fetchChartId();

    public abstract int fetchNextVertexId();

    public abstract int fetchNextComplementaryVertexId(int var1);

    public abstract int getNextEdgeId();

    public abstract int grabNextComplementaryEdgeId(int var1);

    public abstract int[] grabVertexIds();

    public abstract int[] obtainEdgeIds();

    public abstract IdFactory copy();
}

