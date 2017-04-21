/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.tour.util;

import com.graphhopper.tour.util.Edge;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Graph<V> {
    private final Set<V> vertices = new HashSet<V>();
    private final Set<Edge<V>> edges = new HashSet<Edge<V>>();
    private final Map<V, List<Edge<V>>> edgesFrom = new HashMap<V, List<Edge<V>>>();

    public int size() {
        return this.vertices.size();
    }

    public boolean contains(V v) {
        return this.vertices.contains(v);
    }

    public boolean contains(Edge<V> e) {
        return this.edges.contains(e);
    }

    public Set<V> vertices() {
        return Collections.unmodifiableSet(this.vertices);
    }

    public Set<Edge<V>> edges() {
        return Collections.unmodifiableSet(this.edges);
    }

    public Graph<V> add(V v) {
        this.vertices.add(v);
        return this;
    }

    public Graph<V> add(Edge<V> e) {
        this.vertices.add(e.from);
        this.vertices.add(e.to);
        this.edges.add(e);
        List<Edge<V>> el = this.edgesFrom.get(e.from);
        if (el == null) {
            el = new ArrayList<Edge<V>>();
            this.edgesFrom.put(e.from, el);
        }
        el.add(e);
        return this;
    }

    public List<Edge<V>> edgesFrom(V from) {
        List<Edge<V>> el = this.edgesFrom.get(from);
        if (el == null) {
            return el;
        }
        return Collections.unmodifiableList(el);
    }

    public List<V> depthFirstWalk(V root) {
        final ArrayList result = new ArrayList();
        this.depthFirstWalk(root, new Visitor<V>(){

            @Override
            public void visit(V vertex) {
                result.add(vertex);
            }
        });
        return result;
    }

    public void depthFirstWalk(V root, Visitor<V> visitor) {
        HashSet visited = new HashSet();
        this.depthFirstWalk(root, visitor, visited);
    }

    private void depthFirstWalk(V from, Visitor<V> visitor, Set<V> visited) {
        visitor.visit(from);
        visited.add(from);
        List<Edge<V>> el = this.edgesFrom.get(from);
        if (el == null) {
            return;
        }
        for (Edge<V> e : el) {
            if (visited.contains(e.to)) continue;
            this.depthFirstWalk(e.to, visitor, visited);
        }
    }

    public static interface Visitor<V> {
        public void visit(V var1);
    }

}

