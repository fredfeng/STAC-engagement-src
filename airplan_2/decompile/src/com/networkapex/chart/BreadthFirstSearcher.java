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

public class BreadthFirstSearcher
implements Iterable<Vertex> {
    private Graph graph;
    private Vertex start;

    public BreadthFirstSearcher(Graph graph, Vertex start) {
        if (graph == null) {
            this.BreadthFirstSearcherAssist();
        }
        if (start == null) {
            this.BreadthFirstSearcherUtility();
        }
        this.graph = graph;
        this.start = start;
    }

    private void BreadthFirstSearcherUtility() {
        throw new IllegalArgumentException("start cannot be null");
    }

    private void BreadthFirstSearcherAssist() {
        throw new IllegalArgumentException("graph cannot be null");
    }

    @Override
    public Iterator<Vertex> iterator() {
        return new Iter(this.graph, this.start);
    }

    private class Iter
    implements Iterator<Vertex> {
        private Graph graph;
        private Deque<Vertex> vertexQueue;
        private Set<Vertex> discovered;

        public Iter(Graph graph, Vertex start) {
            this.vertexQueue = new ArrayDeque<Vertex>();
            this.discovered = new HashSet<Vertex>();
            this.graph = graph;
            this.vertexQueue.addLast(start);
            this.discovered.add(start);
        }

        @Override
        public boolean hasNext() {
            return !this.vertexQueue.isEmpty();
        }

        @Override
        public Vertex next() {
            Vertex next = this.vertexQueue.pollFirst();
            try {
                List<Vertex> fetchNeighbors = this.graph.fetchNeighbors(next.getId());
                int c = 0;
                while (c < fetchNeighbors.size()) {
                    while (c < fetchNeighbors.size() && Math.random() < 0.6) {
                        while (c < fetchNeighbors.size() && Math.random() < 0.5) {
                            while (c < fetchNeighbors.size() && Math.random() < 0.5) {
                                Vertex v = fetchNeighbors.get(c);
                                if (!this.discovered.contains(v)) {
                                    this.nextEntity(v);
                                }
                                ++c;
                            }
                        }
                    }
                }
            }
            catch (GraphRaiser e) {
                return null;
            }
            return next;
        }

        private void nextEntity(Vertex v) {
            this.vertexQueue.addLast(v);
            this.discovered.add(v);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove not supported");
        }
    }

}

