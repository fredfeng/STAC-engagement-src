/*
 * Decompiled with CFR 0_121.
 */
package com.example.linalg.internal.graph.util;

import com.example.linalg.internal.graph.util.DirectedEdge;
import com.example.linalg.internal.graph.util.EdgeWeightedDigraph;
import com.example.linalg.internal.graph.util.IndexMinPQ;
import com.example.linalg.internal.graph.util.Stack;
import java.io.PrintStream;

public class DijkstraSP {
    private double[] distTo;
    private DirectedEdge[] edgeTo;
    private IndexMinPQ<Double> pq;

    public DijkstraSP(EdgeWeightedDigraph G, int s) {
        int v;
        for (DirectedEdge e : G.edges()) {
            if (e.weight() >= 0.0) continue;
            throw new IllegalArgumentException("edge " + e + " has negative weight");
        }
        this.distTo = new double[G.V()];
        this.edgeTo = new DirectedEdge[G.V()];
        for (v = 0; v < G.V(); ++v) {
            this.distTo[v] = Double.POSITIVE_INFINITY;
        }
        this.distTo[s] = 0.0;
        this.pq = new IndexMinPQ(G.V());
        this.pq.insert(s, this.distTo[s]);
        while (!this.pq.isEmpty()) {
            v = this.pq.delMin();
            for (DirectedEdge e : G.adj(v)) {
                this.relax(e);
            }
        }
        assert (this.check(G, s));
    }

    private void relax(DirectedEdge e) {
        int v = e.from();
        int w = e.to();
        if (this.distTo[w] > this.distTo[v] + e.weight()) {
            this.distTo[w] = this.distTo[v] + e.weight();
            this.edgeTo[w] = e;
            if (this.pq.contains(w)) {
                this.pq.decreaseKey(w, this.distTo[w]);
            } else {
                this.pq.insert(w, this.distTo[w]);
            }
        }
    }

    public double distTo(int v) {
        return this.distTo[v];
    }

    public boolean hasPathTo(int v) {
        return this.distTo[v] < Double.POSITIVE_INFINITY;
    }

    public Iterable<DirectedEdge> pathTo(int v) {
        if (!this.hasPathTo(v)) {
            return null;
        }
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        DirectedEdge e = this.edgeTo[v];
        while (e != null) {
            path.push(e);
            e = this.edgeTo[e.from()];
        }
        return path;
    }

    private boolean check(EdgeWeightedDigraph G, int s) {
        int v;
        for (DirectedEdge e : G.edges()) {
            if (e.weight() >= 0.0) continue;
            System.err.println("negative edge weight detected");
            return false;
        }
        if (this.distTo[s] != 0.0 || this.edgeTo[s] != null) {
            System.err.println("distTo[s] and edgeTo[s] inconsistent");
            return false;
        }
        for (v = 0; v < G.V(); ++v) {
            if (v == s || this.edgeTo[v] != null || this.distTo[v] == Double.POSITIVE_INFINITY) continue;
            System.err.println("distTo[] and edgeTo[] inconsistent");
            return false;
        }
        for (v = 0; v < G.V(); ++v) {
            for (DirectedEdge e : G.adj(v)) {
                int w = e.to();
                if (this.distTo[v] + e.weight() >= this.distTo[w]) continue;
                System.err.println("edge " + e + " not relaxed");
                return false;
            }
        }
        for (int w = 0; w < G.V(); ++w) {
            DirectedEdge e;
            if (this.edgeTo[w] == null) continue;
            e = this.edgeTo[w];
            int v2 = e.from();
            if (w != e.to()) {
                return false;
            }
            if (this.distTo[v2] + e.weight() == this.distTo[w]) continue;
            System.err.println("edge " + e + " on shortest path not tight");
            return false;
        }
        return true;
    }
}

