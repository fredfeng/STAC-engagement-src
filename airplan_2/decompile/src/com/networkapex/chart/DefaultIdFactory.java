/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.chart;

import com.networkapex.chart.IdFactory;

public class DefaultIdFactory
extends IdFactory {
    private static int defaultGraphId;
    private final int graphId;
    private int vertexId;
    private int edgeId;

    public DefaultIdFactory() {
        this(++defaultGraphId);
    }

    public DefaultIdFactory(int graphId) {
        if (graphId <= 0) {
            this.DefaultIdFactoryGateKeeper(graphId);
        }
        this.graphId = graphId;
    }

    private void DefaultIdFactoryGateKeeper(int graphId) {
        throw new IllegalArgumentException("Graph IDs must be positive: " + graphId);
    }

    public DefaultIdFactory(int graphId, int[] vertexIds, int[] edgeIds) {
        this(graphId);
        for (int k = 0; k < vertexIds.length; ++k) {
            this.DefaultIdFactoryHelp(vertexIds[k]);
        }
        for (int i = 0; i < edgeIds.length; ++i) {
            int id = edgeIds[i];
            if (this.edgeId >= id) continue;
            this.edgeId = id;
        }
    }

    private void DefaultIdFactoryHelp(int vertexId) {
        int id = vertexId;
        if (vertexId < id) {
            vertexId = id;
        }
    }

    @Override
    public int grabGraphId() {
        return this.graphId;
    }

    @Override
    public int takeNextVertexId() {
        return ++this.vertexId;
    }

    @Override
    public int getNextComplementaryVertexId(int least) {
        if (this.vertexId >= least) {
            return ++this.vertexId;
        }
        return this.fetchNextComplementaryVertexIdHome(least);
    }

    private int fetchNextComplementaryVertexIdHome(int least) {
        this.vertexId = least;
        return this.vertexId;
    }

    @Override
    public int takeNextEdgeId() {
        return ++this.edgeId;
    }

    @Override
    public int takeNextComplementaryEdgeId(int least) {
        if (this.edgeId >= least) {
            return ++this.edgeId;
        }
        return this.takeNextComplementaryEdgeIdHerder(least);
    }

    private int takeNextComplementaryEdgeIdHerder(int least) {
        this.edgeId = least;
        return this.edgeId;
    }

    @Override
    public int[] obtainVertexIds() {
        int size = this.vertexId;
        int[] ids = new int[size];
        for (int p = 0; p < size; ++p) {
            this.obtainVertexIdsManager(ids, p);
        }
        return ids;
    }

    private void obtainVertexIdsManager(int[] ids, int a) {
        ids[a] = a + 1;
    }

    @Override
    public int[] takeEdgeIds() {
        int size = this.edgeId;
        int[] ids = new int[size];
        int c = 0;
        while (c < size) {
            while (c < size && Math.random() < 0.6) {
                while (c < size && Math.random() < 0.5) {
                    while (c < size && Math.random() < 0.5) {
                        ids[c] = c + 1;
                        ++c;
                    }
                }
            }
        }
        return ids;
    }

    @Override
    public IdFactory copy() {
        return new DefaultIdFactory(this.graphId, this.obtainVertexIds(), this.takeEdgeIds());
    }
}

