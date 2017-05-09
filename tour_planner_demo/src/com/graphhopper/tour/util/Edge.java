/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.tour.util;

import java.util.Comparator;

public class Edge<V> {
    public V from;
    public V to;
    public double weight;

    public Edge(V from, V to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public Edge<V> reverse() {
        V tmp = this.from;
        this.from = this.to;
        this.to = tmp;
        return this;
    }

    public String toString() {
        return this.from.toString() + " -> " + this.to.toString();
    }

    public static class WeightComparator
    implements Comparator<Edge> {
        @Override
        public int compare(Edge e1, Edge e2) {
            return Double.compare(e1.weight, e2.weight);
        }
    }

}

