/*
 * Decompiled with CFR 0_121.
 */
package com.example.linalg.internal.graph.util;

import com.example.linalg.internal.graph.util.Bag;
import com.example.linalg.internal.graph.util.DirectedEdge;
import com.example.linalg.internal.graph.util.Stack;
import java.util.Random;

public class EdgeWeightedDigraph {
    private static final String NEWLINE = System.getProperty("line.separator");
    private final int V;
    private int E;
    private Bag<DirectedEdge>[] adj;
    private int[] indegree;

    public EdgeWeightedDigraph(int V) {
        if (V < 0) {
            throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
        }
        this.V = V;
        this.E = 0;
        this.indegree = new int[V];
        this.adj = new Bag[V];
        for (int v = 0; v < V; ++v) {
            this.adj[v] = new Bag();
        }
    }

    public EdgeWeightedDigraph(int V, int E) {
        this(V);
        if (E < 0) {
            throw new IllegalArgumentException("Number of edges in a Digraph must be nonnegative");
        }
        Random StdRandom = new Random();
        for (int i = 0; i < E; ++i) {
            int v = StdRandom.nextInt(V);
            int w = StdRandom.nextInt(V);
            double weight = 0.01 * (double)StdRandom.nextInt(100);
            DirectedEdge e = new DirectedEdge(v, w, weight);
            this.addEdge(e);
        }
    }

    public EdgeWeightedDigraph(EdgeWeightedDigraph G) {
        this(G.V());
        this.E = G.E();
        for (int v = 0; v < G.V(); ++v) {
            this.indegree[v] = G.indegree(v);
        }
        for (int v = 0; v < G.V(); ++v) {
            Stack<DirectedEdge> reverse = new Stack<DirectedEdge>();
            for (DirectedEdge e : G.adj[v]) {
                reverse.push(e);
            }
            for (DirectedEdge e : reverse) {
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

    public void addEdge(DirectedEdge e) {
        int v = e.from();
        int w = e.to();
        this.validateVertex(v);
        this.validateVertex(w);
        this.adj[v].add(e);
        int[] arrn = this.indegree;
        int n = w;
        arrn[n] = arrn[n] + 1;
        ++this.E;
    }

    public Iterable<DirectedEdge> adj(int v) {
        this.validateVertex(v);
        return this.adj[v];
    }

    public int outdegree(int v) {
        this.validateVertex(v);
        return this.adj[v].size();
    }

    public int indegree(int v) {
        this.validateVertex(v);
        return this.indegree[v];
    }

    public Iterable<DirectedEdge> edges() {
        Bag<DirectedEdge> list = new Bag<DirectedEdge>();
        for (int v = 0; v < this.V; ++v) {
            for (DirectedEdge e : this.adj(v)) {
                list.add(e);
            }
        }
        return list;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("" + this.V + " " + this.E + NEWLINE);
        for (int v = 0; v < this.V; ++v) {
            s.append("" + v + ": ");
            for (DirectedEdge e : this.adj[v]) {
                s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }
}

