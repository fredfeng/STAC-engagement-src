/*
 * Decompiled with CFR 0_121.
 */
package com.example.linalg.internal.graph.path;

import com.example.linalg.internal.graph.path.SingleShortestPath;
import java.util.concurrent.ConcurrentHashMap;

public class MultiShortestPathWorker
implements Runnable {
    double[][] A;
    int[] vertexIds;
    SingleShortestPath SP = new SingleShortestPath();
    ConcurrentHashMap<Integer, double[]> computedShortestPaths = new ConcurrentHashMap();
    ConcurrentHashMap<Integer, Integer> vertexReturnPositions;

    public MultiShortestPathWorker(double[][] A, int[] vertexIds, ConcurrentHashMap<Integer, Integer> vertexReturnPosition) {
        this.A = A;
        this.vertexIds = vertexIds;
        this.vertexReturnPositions = vertexReturnPosition;
    }

    @Override
    public void run() {
        for (int v : this.vertexIds) {
            double[] row = this.SP.shortestPaths(this.A, v);
            this.computedShortestPaths.put(this.vertexReturnPositions.get(v), row);
        }
    }

    public ConcurrentHashMap<Integer, double[]> getComputedShortestPaths() {
        return this.computedShortestPaths;
    }
}

