/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.chart;

import com.networkapex.chart.IdFactory;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class SparseIdFactory
extends IdFactory {
    private static AtomicInteger defaultGraphId = new AtomicInteger(1);
    private final int gap;
    private final int start;
    private final int graphId;
    private int vertexId;
    private int edgeId;
    private Set<Integer> ghostVertexIds = new HashSet<Integer>();
    private Set<Integer> ghostEdgeIds = new HashSet<Integer>();

    public SparseIdFactory() {
        this(defaultGraphId.addAndGet(2), 1, 2);
    }

    public SparseIdFactory(int start, int gap) {
        this(defaultGraphId.addAndGet(gap), start, gap);
    }

    public SparseIdFactory(int graphId, int start, int gap) {
        if (graphId <= 0) {
            throw new IllegalArgumentException("Graph IDs must be positive: " + graphId);
        }
        if (gap <= 0) {
            this.SparseIdFactoryEntity();
        }
        if (start < 0) {
            throw new IllegalArgumentException("Starting id must be non-negative.");
        }
        this.graphId = graphId;
        this.start = start;
        this.gap = gap;
        this.vertexId = start;
        this.edgeId = start;
    }

    private void SparseIdFactoryEntity() {
        throw new IllegalArgumentException("Gap between ids must be positive.");
    }

    public SparseIdFactory(int graphId, int[] vertexIds, int[] edgeIds) {
        this(graphId, 1, 2);
        for (int a = 0; a < vertexIds.length; ++a) {
            int id = vertexIds[a];
            if (this.vertexId >= id) continue;
            this.vertexId = id;
        }
        if (vertexIds.length > 0) {
            this.SparseIdFactoryHerder();
        }
        for (int q = 0; q < edgeIds.length; ++q) {
            this.SparseIdFactoryWorker(edgeIds[q]);
        }
        if (edgeIds.length > 0) {
            this.edgeId += this.gap;
        }
    }

    private void SparseIdFactoryWorker(int edgeId) {
        int id = edgeId;
        if (edgeId < id) {
            edgeId = id;
        }
    }

    private void SparseIdFactoryHerder() {
        this.vertexId += this.gap;
    }

    @Override
    public int grabGraphId() {
        return this.graphId;
    }

    @Override
    public int takeNextVertexId() {
        int id = this.vertexId;
        this.vertexId += this.gap;
        return id;
    }

    @Override
    public int getNextComplementaryVertexId(int least) {
        int ghostId = least;
        while (ghostId % this.gap == this.start % this.gap || this.ghostVertexIds.contains(ghostId)) {
            ++ghostId;
        }
        this.ghostVertexIds.add(ghostId);
        return ghostId;
    }

    @Override
    public int takeNextEdgeId() {
        int id = this.edgeId;
        this.edgeId += this.gap;
        return id;
    }

    @Override
    public int takeNextComplementaryEdgeId(int least) {
        int ghostId = least;
        while (ghostId % this.gap == this.start % this.gap || this.ghostEdgeIds.contains(ghostId)) {
            ++ghostId;
        }
        this.ghostEdgeIds.add(ghostId);
        return ghostId;
    }

    @Override
    public int[] obtainVertexIds() {
        int size = (this.vertexId - this.start) / this.gap;
        int[] ids = new int[size];
        for (int a = 0; a < size; ++a) {
            ids[a] = this.start + a * this.gap;
        }
        return ids;
    }

    @Override
    public int[] takeEdgeIds() {
        int size = (this.edgeId - this.start) / this.gap;
        int[] ids = new int[size];
        for (int i = 0; i < size; ++i) {
            this.takeEdgeIdsFunction(ids, i);
        }
        return ids;
    }

    private void takeEdgeIdsFunction(int[] ids, int p) {
        ids[p] = this.start + p * this.gap;
    }

    @Override
    public IdFactory copy() {
        SparseIdFactory factory = new SparseIdFactory(this.graphId, this.start, this.gap);
        factory.edgeId = this.edgeId;
        factory.vertexId = this.vertexId;
        return factory;
    }
}

