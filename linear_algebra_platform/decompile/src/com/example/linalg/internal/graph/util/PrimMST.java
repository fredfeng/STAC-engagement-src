/*
 * Decompiled with CFR 0_121.
 */
package com.example.linalg.internal.graph.util;

import com.example.linalg.internal.graph.util.Edge;
import com.example.linalg.internal.graph.util.EdgeWeightedGraph;
import com.example.linalg.internal.graph.util.IndexMinPQ;
import com.example.linalg.internal.graph.util.Queue;
import com.example.linalg.internal.graph.util.UF;
import java.io.PrintStream;

public class PrimMST {
    private static final double FLOATING_POINT_EPSILON = 1.0E-12;
    private Edge[] edgeTo;
    private double[] distTo;
    private boolean[] marked;
    private IndexMinPQ<Double> pq;

    public PrimMST(EdgeWeightedGraph G) {
        int v;
        this.edgeTo = new Edge[G.V()];
        this.distTo = new double[G.V()];
        this.marked = new boolean[G.V()];
        this.pq = new IndexMinPQ(G.V());
        for (v = 0; v < G.V(); ++v) {
            this.distTo[v] = Double.POSITIVE_INFINITY;
        }
        for (v = 0; v < G.V(); ++v) {
            if (this.marked[v]) continue;
            this.prim(G, v);
        }
        assert (this.check(G));
    }

    private void prim(EdgeWeightedGraph G, int s) {
        this.distTo[s] = 0.0;
        this.pq.insert(s, this.distTo[s]);
        while (!this.pq.isEmpty()) {
            int v = this.pq.delMin();
            this.scan(G, v);
        }
    }

    private void scan(EdgeWeightedGraph G, int v) {
        this.marked[v] = true;
        for (Edge e : G.adj(v)) {
            int w = e.other(v);
            if (this.marked[w] || e.weight() >= this.distTo[w]) continue;
            this.distTo[w] = e.weight();
            this.edgeTo[w] = e;
            if (this.pq.contains(w)) {
                this.pq.decreaseKey(w, this.distTo[w]);
                continue;
            }
            this.pq.insert(w, this.distTo[w]);
        }
    }

    public Iterable<Edge> edges() {
        Queue<Edge> mst = new Queue<Edge>();
        for (int v = 0; v < this.edgeTo.length; ++v) {
            Edge e = this.edgeTo[v];
            if (e == null) continue;
            mst.enqueue(e);
        }
        return mst;
    }

    public double weight() {
        double weight = 0.0;
        for (Edge e : this.edges()) {
            weight += e.weight();
        }
        return weight;
    }

    private boolean check(EdgeWeightedGraph G) {
        int w;
        int v;
        double totalWeight = 0.0;
        for (Edge e : this.edges()) {
            totalWeight += e.weight();
        }
        if (Math.abs(totalWeight - this.weight()) > 1.0E-12) {
            System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", totalWeight, this.weight());
            return false;
        }
        UF uf = new UF(G.V());
        for (Edge e : this.edges()) {
            v = e.either();
            if (uf.connected(v, w = e.other(v))) {
                System.err.println("Not a forest");
                return false;
            }
            uf.union(v, w);
        }
        for (Edge e : G.edges()) {
            v = e.either();
            if (uf.connected(v, w = e.other(v))) continue;
            System.err.println("Not a spanning forest");
            return false;
        }
        for (Edge e : this.edges()) {
            int y;
            int x;
            uf = new UF(G.V());
            for (Edge f : this.edges()) {
                x = f.either();
                y = f.other(x);
                if (f == e) continue;
                uf.union(x, y);
            }
            for (Edge f : G.edges()) {
                x = f.either();
                if (uf.connected(x, y = f.other(x)) || f.weight() >= e.weight()) continue;
                System.err.println("Edge " + f + " violates cut optimality conditions");
                return false;
            }
        }
        return true;
    }
}

