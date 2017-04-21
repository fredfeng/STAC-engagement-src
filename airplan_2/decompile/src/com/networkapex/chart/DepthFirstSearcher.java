/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.chart;

import com.networkapex.chart.Graph;
import com.networkapex.chart.GraphRaiser;
import com.networkapex.chart.Vertex;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DepthFirstSearcher
implements Iterable<Vertex> {
    private Graph graph;
    private Vertex start;

    public DepthFirstSearcher(Graph graph, Vertex start) {
        if (graph == null) {
            this.DepthFirstSearcherFunction();
        }
        if (start == null) {
            throw new IllegalArgumentException("start cannot be null");
        }
        this.graph = graph;
        this.start = start;
    }

    private void DepthFirstSearcherFunction() {
        throw new IllegalArgumentException("graph cannot be null");
    }

    @Override
    public Iterator<Vertex> iterator() {
        return new Iter(this.graph, this.start);
    }

    private class Iter
    implements Iterator<Vertex> {
        private Graph graph;
        private Deque<Vertex> vertexStack;
        private Set<Vertex> discovered;

        public Iter(Graph graph, Vertex start) {
            this.vertexStack = new ArrayDeque<Vertex>();
            this.discovered = new HashSet<Vertex>();
            this.graph = graph;
            this.vertexStack.push(start);
            this.discovered.add(start);
        }

        @Override
        public boolean hasNext() {
            return !this.vertexStack.isEmpty();
        }

        @Override
        public Vertex next() {
            Vertex next = this.vertexStack.pop();
            try {
                List<Vertex> fetchNeighbors = this.graph.fetchNeighbors(next.getId());
                for (int j = 0; j < fetchNeighbors.size(); ++j) {
                    Vertex v = fetchNeighbors.get(j);
                    if (this.discovered.contains(v)) continue;
                    this.nextHelper(v);
                }
            }
            catch (GraphRaiser e) {
                return null;
            }
            return next;
        }

        private void nextHelper(Vertex v) {
            this.vertexStack.push(v);
            this.discovered.add(v);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove not supported");
        }
    }

}

