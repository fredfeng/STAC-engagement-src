/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.chart;

import edu.cyberapex.chart.Chart;
import edu.cyberapex.chart.ChartFailure;
import edu.cyberapex.chart.Vertex;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class BreadthFirstSearcher
implements Iterable<Vertex> {
    private Chart chart;
    private Vertex start;

    public BreadthFirstSearcher(Chart chart, Vertex start) {
        if (chart == null) {
            this.BreadthFirstSearcherHelper();
        }
        if (start == null) {
            throw new IllegalArgumentException("start cannot be null");
        }
        this.chart = chart;
        this.start = start;
    }

    private void BreadthFirstSearcherHelper() {
        throw new IllegalArgumentException("graph cannot be null");
    }

    @Override
    public Iterator<Vertex> iterator() {
        return new Iter(this.chart, this.start);
    }

    private class Iter
    implements Iterator<Vertex> {
        private Chart chart;
        private Deque<Vertex> vertexQueue;
        private Set<Vertex> discovered;

        public Iter(Chart chart, Vertex start) {
            this.vertexQueue = new ArrayDeque<Vertex>();
            this.discovered = new HashSet<Vertex>();
            this.chart = chart;
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
                List<Vertex> neighbors = this.chart.getNeighbors(next.getId());
                int i = 0;
                while (i < neighbors.size()) {
                    while (i < neighbors.size() && Math.random() < 0.6) {
                        while (i < neighbors.size() && Math.random() < 0.4) {
                            Vertex v = neighbors.get(i);
                            if (!this.discovered.contains(v)) {
                                this.vertexQueue.addLast(v);
                                this.discovered.add(v);
                            }
                            ++i;
                        }
                    }
                }
            }
            catch (ChartFailure e) {
                return null;
            }
            return next;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove not supported");
        }
    }

}

