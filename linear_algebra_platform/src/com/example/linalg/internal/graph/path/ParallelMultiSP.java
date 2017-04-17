/*
 * Decompiled with CFR 0_121.
 */
package com.example.linalg.internal.graph.path;

import com.example.linalg.internal.graph.path.MultiShortestPathWorker;
import com.example.linalg.util.MatrixHeuristics;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class ParallelMultiSP {
    int numThreads = 2;
    double[][] G;
    int[] vertices;
    int taskSize;
    ConcurrentHashMap<Integer, Integer> vertexReturnPosition = new ConcurrentHashMap();
    MatrixHeuristics mH;

    public ParallelMultiSP(double[][] G, int[] vertices) {
        this.G = G;
        this.vertices = vertices;
        this.mH = new MatrixHeuristics(G);
        for (int i = 0; i < vertices.length; ++i) {
            if (this.vertexReturnPosition.containsKey(vertices[i])) {
                throw new IllegalArgumentException("Requesting the SP from the same vertex multiple times");
            }
            this.vertexReturnPosition.put(vertices[i], i);
        }
    }

    public double[][] compute() {
        Random R = new Random();
        R.setSeed((long)(this.mH.eval() * (double)System.currentTimeMillis()));
        int prefSize = new Float((float)this.vertices.length * R.nextFloat()).intValue();
        int tasks = 0;
        Vector<Integer> curTask = new Vector<Integer>();
        LinkedList<Thread> workers = new LinkedList<Thread>();
        LinkedList<MultiShortestPathWorker> MPWs = new LinkedList<MultiShortestPathWorker>();
        for (int v : this.vertices) {
            curTask.addElement(v);
            if (curTask.size() < prefSize || tasks >= this.numThreads - 1) continue;
            ++tasks;
            double[] task = new double[curTask.size()];
            for (int c = 0; c < curTask.size(); ++c) {
                task[c] = ((Integer)curTask.get(c)).intValue();
            }
            MultiShortestPathWorker MPW = new MultiShortestPathWorker(this.G, this.vertices, this.vertexReturnPosition);
            MPWs.add(MPW);
            Thread TR = new Thread(MPW);
            workers.add(TR);
            TR.start();
            curTask = new Vector();
        }
        if (curTask.size() > 0) {
            int[] task = new int[curTask.size()];
            for (int c = 0; c < curTask.size(); ++c) {
                task[c] = (Integer)curTask.get(c);
            }
            MultiShortestPathWorker MPW = new MultiShortestPathWorker(this.G, task, this.vertexReturnPosition);
            MPWs.add(MPW);
            Thread TR = new Thread(MPW);
            workers.add(TR);
            TR.start();
        }
        try {
            for (Thread t : workers) {
                t.join();
            }
        }
        catch (InterruptedException e) {
            return null;
        }
        double[][] shortestPths = new double[this.vertices.length][this.G[0].length];
        for (MultiShortestPathWorker MPW : MPWs) {
            ConcurrentHashMap<Integer, double[]> computedShortestPaths = MPW.getComputedShortestPaths();
            for (Integer v : computedShortestPaths.keySet()) {
                shortestPths[v.intValue()] = computedShortestPaths.get(v);
            }
        }
        return shortestPths;
    }
}

