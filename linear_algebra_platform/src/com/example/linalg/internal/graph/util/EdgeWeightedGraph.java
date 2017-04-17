/*
 * Decompiled with CFR 0_121.
 */
package com.example.linalg.internal.graph.util;

import com.example.linalg.internal.graph.util.Bag;
import com.example.linalg.internal.graph.util.Edge;
import com.example.linalg.internal.graph.util.Stack;
import java.util.Random;

public class EdgeWeightedGraph {
    private static final String NEWLINE = System.getProperty("line.separator");
    private final int V;
    private int E;
    private Bag<Edge>[] adj;

    public EdgeWeightedGraph(int V) {
        if (V < 0) {
            throw new IllegalArgumentException("Number of vertices must be nonnegative");
        }
        this.V = V;
        this.E = 0;
        this.adj = new Bag[V];
        for (int v = 0; v < V; ++v) {
            this.adj[v] = new Bag();
        }
    }

    public EdgeWeightedGraph(int V, int E) {
        this(V);
        if (E < 0) {
            throw new IllegalArgumentException("Number of edges must be nonnegative");
        }
        Random R = new Random();
        for (int i = 0; i < E; ++i) {
            int v = R.nextInt(V);
            int w = R.nextInt(V);
            double weight = (double)Math.round(100.0 * R.nextDouble()) / 100.0;
            Edge e = new Edge(v, w, weight);
            this.addEdge(e);
        }
    }

    public EdgeWeightedGraph(EdgeWeightedGraph G) {
        this(G.V());
        this.E = G.E();
        for (int v = 0; v < G.V(); ++v) {
            Stack<Edge> reverse = new Stack<Edge>();
            for (Edge e : G.adj[v]) {
                reverse.push(e);
            }
            for (Edge e : reverse) {
                this.adj[v].add(e);
            }
        }
    }

    public int V() {
        return this.V;
    }

    public int E() {
        return this.E;
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= this.V) {
            throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (this.V - 1));
        }
    }

    public void addEdge(Edge e) {
        int v = e.either();
        int w = e.other(v);
        this.validateVertex(v);
        this.validateVertex(w);
        this.adj[v].add(e);
        this.adj[w].add(e);
        ++this.E;
    }

    public Iterable<Edge> adj(int v) {
        this.validateVertex(v);
        return this.adj[v];
    }

    public int degree(int v) {
        this.validateVertex(v);
        return this.adj[v].size();
    }

    public Iterable<Edge> edges() {
        Bag list = new Bag();
        for (int v = 0; v < this.V; ++v) {
            int selfLoops = 0;
            for (Edge e : this.adj(v)) {
                if (e.other(v) > v) {
                    list.add(e);
                    continue;
                }
                if (e.other(v) != v) continue;
                if (selfLoops % 2 == 0) {
                    list.add(e);
                }
                ++selfLoops;
            }
        }
        return list;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("" + this.V + " " + this.E + NEWLINE);
        for (int v = 0; v < this.V; ++v) {
            s.append("" + v + ": ");
            for (Edge e : this.adj[v]) {
                s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }
}

