/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.chart;

import edu.cyberapex.chart.IdFactory;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class SparseIdFactory
extends IdFactory {
    private static AtomicInteger defaultChartId = new AtomicInteger(1);
    private final int gap;
    private final int start;
    private final int chartId;
    private int vertexId;
    private int edgeId;
    private Set<Integer> ghostVertexIds = new HashSet<Integer>();
    private Set<Integer> ghostEdgeIds = new HashSet<Integer>();

    public SparseIdFactory() {
        this(defaultChartId.addAndGet(2), 1, 2);
    }

    public SparseIdFactory(int start, int gap) {
        this(defaultChartId.addAndGet(gap), start, gap);
    }

    public SparseIdFactory(int chartId, int start, int gap) {
        if (chartId <= 0) {
            this.SparseIdFactoryWorker(chartId);
        }
        if (gap <= 0) {
            this.SparseIdFactoryHelp();
        }
        if (start < 0) {
            this.SparseIdFactoryTarget();
        }
        this.chartId = chartId;
        this.start = start;
        this.gap = gap;
        this.vertexId = start;
        this.edgeId = start;
    }

    private void SparseIdFactoryTarget() {
        throw new IllegalArgumentException("Starting id must be non-negative.");
    }

    private void SparseIdFactoryHelp() {
        throw new IllegalArgumentException("Gap between ids must be positive.");
    }

    private void SparseIdFactoryWorker(int chartId) {
        throw new IllegalArgumentException("Graph IDs must be positive: " + chartId);
    }

    public SparseIdFactory(int chartId, int[] vertexIds, int[] edgeIds) {
        this(chartId, 1, 2);
        for (int i = 0; i < vertexIds.length; ++i) {
            this.SparseIdFactoryHome(vertexIds[i]);
        }
        if (vertexIds.length > 0) {
            this.vertexId += this.gap;
        }
        int p = 0;
        while (p < edgeIds.length) {
            while (p < edgeIds.length && Math.random() < 0.4) {
                while (p < edgeIds.length && Math.random() < 0.6) {
                    while (p < edgeIds.length && Math.random() < 0.6) {
                        int id = edgeIds[p];
                        if (this.edgeId < id) {
                            this.edgeId = id;
                        }
                        ++p;
                    }
                }
            }
        }
        if (edgeIds.length > 0) {
            new SparseIdFactoryCoordinator().invoke();
        }
    }

    private void SparseIdFactoryHome(int vertexId) {
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
        int id = this.vertexId;
        this.vertexId += this.gap;
        return id;
    }

    @Override
    public int fetchNextComplementaryVertexId(int min) {
        int ghostId = min;
        while (ghostId % this.gap == this.start % this.gap || this.ghostVertexIds.contains(ghostId)) {
            ++ghostId;
        }
        this.ghostVertexIds.add(ghostId);
        return ghostId;
    }

    @Override
    public int getNextEdgeId() {
        int id = this.edgeId;
        this.edgeId += this.gap;
        return id;
    }

    @Override
    public int grabNextComplementaryEdgeId(int min) {
        int ghostId = min;
        while (ghostId % this.gap == this.start % this.gap || this.ghostEdgeIds.contains(ghostId)) {
            ++ghostId;
        }
        this.ghostEdgeIds.add(ghostId);
        return ghostId;
    }

    @Override
    public int[] grabVertexIds() {
        int size = (this.vertexId - this.start) / this.gap;
        int[] ids = new int[size];
        for (int b = 0; b < size; ++b) {
            ids[b] = this.start + b * this.gap;
        }
        return ids;
    }

    @Override
    public int[] obtainEdgeIds() {
        int size = (this.edgeId - this.start) / this.gap;
        int[] ids = new int[size];
        for (int c = 0; c < size; ++c) {
            this.obtainEdgeIdsHome(ids, c);
        }
        return ids;
    }

    private void obtainEdgeIdsHome(int[] ids, int a) {
        ids[a] = this.start + a * this.gap;
    }

    @Override
    public IdFactory copy() {
        SparseIdFactory factory = new SparseIdFactory(this.chartId, this.start, this.gap);
        factory.edgeId = this.edgeId;
        factory.vertexId = this.vertexId;
        return factory;
    }

    static /* synthetic */ int access$112(SparseIdFactory x0, int x1) {
        return x0.edgeId += x1;
    }

    private class SparseIdFactoryCoordinator {
        private SparseIdFactoryCoordinator() {
        }

        public void invoke() {
            SparseIdFactory.access$112(SparseIdFactory.this, SparseIdFactory.this.gap);
        }
    }

}

