/*
 * Decompiled with CFR 0_121.
 */
package com.example.linalg.internal.graph.util;

import java.io.PrintStream;

public class DirectedEdge {
    private final int v;
    private final int w;
    private final double weight;

    public DirectedEdge(int v, int w, double weight) {
        if (v < 0) {
            throw new IndexOutOfBoundsException("Vertex names must be nonnegative integers");
        }
        if (w < 0) {
            throw new IndexOutOfBoundsException("Vertex names must be nonnegative integers");
        }
        if (Double.isNaN(weight)) {
            throw new IllegalArgumentException("Weight is NaN");
        }
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    public int from() {
        return this.v;
    }

    public int to() {
        return this.w;
    }

    public double weight() {
        return this.weight;
    }

    public String toString() {
        return "" + this.v + "->" + this.w + " " + String.format("%5.2f", this.weight);
    }

    public static void main(String[] args) {
        DirectedEdge e = new DirectedEdge(12, 34, 5.67);
        System.out.println(e);
    }
}

