/*
 * Decompiled with CFR 0_121.
 */
package com.example.linalg.internal.graph.util;

public class Edge
implements Comparable<Edge> {
    private final int v;
    private final int w;
    private final double weight;

    public Edge(int v, int w, double weight) {
        if (v < 0) {
            throw new IndexOutOfBoundsException("Vertex name must be a nonnegative integer");
        }
        if (w < 0) {
            throw new IndexOutOfBoundsException("Vertex name must be a nonnegative integer");
        }
        if (Double.isNaN(weight)) {
            throw new IllegalArgumentException("Weight is NaN");
        }
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    public double weight() {
        return this.weight;
    }

    public int either() {
        return this.v;
    }

    public int other(int vertex) {
        if (vertex == this.v) {
            return this.w;
        }
        if (vertex == this.w) {
            return this.v;
        }
        throw new IllegalArgumentException("Illegal endpoint");
    }

    @Override
    public int compareTo(Edge that) {
        if (this.weight() < that.weight()) {
            return -1;
        }
        if (this.weight() > that.weight()) {
            return 1;
        }
        return 0;
    }

    public String toString() {
        return String.format("%d-%d %.5f", this.v, this.w, this.weight);
    }
}

