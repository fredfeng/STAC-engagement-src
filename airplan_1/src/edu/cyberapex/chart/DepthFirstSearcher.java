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

public class DepthFirstSearcher
implements Iterable<Vertex> {
    private Chart chart;
    private Vertex start;

    public DepthFirstSearcher(Chart chart, Vertex start) {
        if (chart == null) {
            throw new IllegalArgumentException("graph cannot be null");
        }
        if (start == null) {
            throw new IllegalArgumentException("start cannot be null");
        }
        this.chart = chart;
        this.start = start;
    }

    @Override
    public Iterator<Vertex> iterator() {
        return new Iter(this.chart, this.start);
    }

    private class Iter
    implements Iterator<Vertex> {
        private Chart chart;
        private Deque<Vertex> vertexStack;
        private Set<Vertex> discovered;

        public Iter(Chart chart, Vertex start) {
            this.vertexStack = new ArrayDeque<Vertex>();
            this.discovered = new HashSet<Vertex>();
            this.chart = chart;
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
                List<Vertex> neighbors = this.chart.getNeighbors(next.getId());
                for (int a = 0; a < neighbors.size(); ++a) {
                    this.nextGuide(neighbors, a);
                }
            }
            catch (ChartFailure e) {
                return null;
            }
            return next;
        }

        private void nextGuide(List<Vertex> neighbors, int k) {
            Vertex v = neighbors.get(k);
            if (!this.discovered.contains(v)) {
                this.nextGuideHelper(v);
            }
        }

        private void nextGuideHelper(Vertex v) {
            this.vertexStack.push(v);
            this.discovered.add(v);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove not supported");
        }
    }

}

