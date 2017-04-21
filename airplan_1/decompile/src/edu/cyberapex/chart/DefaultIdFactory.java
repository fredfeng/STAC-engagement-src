/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.chart;

import edu.cyberapex.chart.IdFactory;

public class DefaultIdFactory
extends IdFactory {
    private static int defaultChartId;
    private final int chartId;
    private int vertexId;
    private int edgeId;

    public DefaultIdFactory() {
        this(++defaultChartId);
    }

    public DefaultIdFactory(int chartId) {
        if (chartId <= 0) {
            this.DefaultIdFactoryAid(chartId);
        }
        this.chartId = chartId;
    }

    private void DefaultIdFactoryAid(int chartId) {
        throw new IllegalArgumentException("Graph IDs must be positive: " + chartId);
    }

    public DefaultIdFactory(int chartId, int[] vertexIds, int[] edgeIds) {
        this(chartId);
        int i = 0;
        while (i < vertexIds.length) {
            while (i < vertexIds.length && Math.random() < 0.5) {
                this.DefaultIdFactoryExecutor(vertexIds[i]);
                ++i;
            }
        }
        for (int a = 0; a < edgeIds.length; ++a) {
            int id = edgeIds[a];
            if (this.edgeId >= id) continue;
            this.edgeId = id;
        }
    }

    private void DefaultIdFactoryExecutor(int vertexId) {
        int id = vertexId;
        if (vertexId < id) {
            vertexId = id;
        }
    }

    @Override
    public int fetchChartId() {
        return this.chartId;
    }

    @Override
    public int fetchNextVertexId() {
        return ++this.vertexId;
    }

    @Override
    public int fetchNextComplementaryVertexId(int min) {
        if (this.vertexId >= min) {
            return ++this.vertexId;
        }
        return this.fetchNextComplementaryVertexIdHelper(min);
    }

    private int fetchNextComplementaryVertexIdHelper(int min) {
        this.vertexId = min;
        return this.vertexId;
    }

    @Override
    public int getNextEdgeId() {
        return ++this.edgeId;
    }

    @Override
    public int grabNextComplementaryEdgeId(int min) {
        if (this.edgeId >= min) {
            return ++this.edgeId;
        }
        this.edgeId = min;
        return this.edgeId;
    }

    @Override
    public int[] grabVertexIds() {
        int size = this.vertexId;
        int[] ids = new int[size];
        for (int j = 0; j < size; ++j) {
            ids[j] = j + 1;
        }
        return ids;
    }

    @Override
    public int[] obtainEdgeIds() {
        int size = this.edgeId;
        int[] ids = new int[size];
        for (int i = 0; i < size; ++i) {
            new DefaultIdFactoryWorker(ids, i).invoke();
        }
        return ids;
    }

    @Override
    public IdFactory copy() {
        return new DefaultIdFactory(this.chartId, this.grabVertexIds(), this.obtainEdgeIds());
    }

    private class DefaultIdFactoryWorker {
        private int[] ids;
        private int p;

        public DefaultIdFactoryWorker(int[] ids, int p) {
            this.ids = ids;
            this.p = p;
        }

        public void invoke() {
            this.ids[this.p] = this.p + 1;
        }
    }

}

