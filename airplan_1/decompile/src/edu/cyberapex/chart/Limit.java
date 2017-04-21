/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.chart;

import edu.cyberapex.chart.Chart;
import edu.cyberapex.chart.ChartFailure;
import edu.cyberapex.chart.Data;
import edu.cyberapex.chart.Edge;
import edu.cyberapex.chart.IdFactory;
import edu.cyberapex.chart.Vertex;
import edu.cyberapex.order.Shifter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Limit {
    private Map<Vertex, List<Edge>> backwardEdges = new HashMap<Vertex, List<Edge>>();
    private Chart chart;
    private Matrix savedUsedLimits;
    private boolean evenVisit = false;
    private Vertex source;

    public Limit(Chart chart) {
        this.chart = chart;
    }

    public double limit(String sourceName, String sinkName) throws ChartFailure {
        Vertex sink;
        Vertex source = this.chart.obtainVertex(this.chart.getVertexIdByName(sourceName));
        if (source.equals(sink = this.chart.obtainVertex(this.chart.getVertexIdByName(sinkName)))) {
            return new LimitWorker().invoke();
        }
        this.source = source;
        this.validateChart();
        this.makeBackwardEdges();
        double weight = 0.0;
        Matrix limitMatrix = this.makeLimitMatrix();
        Matrix usedLimit = new Matrix();
        HashMap<Vertex, Parent> pathMap = new HashMap<Vertex, Parent>();
        block0 : do {
            double additionalLimit = this.search(limitMatrix, source, sink, usedLimit, pathMap);
            weight += additionalLimit;
            if (additionalLimit == 0.0) break;
            Vertex curr = sink;
            do {
                if (curr.equals(source)) continue block0;
                Vertex next = pathMap.get((Object)curr).parent;
                usedLimit.insert(next, curr, usedLimit.get(next, curr) + additionalLimit);
                usedLimit.insert(curr, next, usedLimit.get(curr, next) - additionalLimit);
                curr = next;
            } while (true);
        } while (true);
        this.savedUsedLimits = usedLimit;
        return weight;
    }

    public Map<Vertex, Map<Vertex, Double>> fetchLimitPaths(String sourceName, String sinkName) throws ChartFailure {
        if (this.savedUsedLimits == null) {
            this.takeLimitPathsEntity(sourceName, sinkName);
        }
        return this.savedUsedLimits.map;
    }

    private void takeLimitPathsEntity(String sourceName, String sinkName) throws ChartFailure {
        this.limit(sourceName, sinkName);
    }

    private double search(Matrix limitMatrix, Vertex source, Vertex sink, Matrix usedLimit, HashMap<Vertex, Parent> pathMap) throws ChartFailure {
        this.initializePathMap(pathMap, source);
        return this.search(limitMatrix, source, sink, usedLimit, pathMap, new HashMap<Vertex, Double>());
    }

    private double search(Matrix limitMatrix, Vertex source, Vertex sink, Matrix usedLimit, HashMap<Vertex, Parent> pathMap, HashMap<Vertex, Double> nodeLimit) throws ChartFailure {
        ArrayDeque<Vertex> queue = new ArrayDeque<Vertex>();
        queue.addLast(source);
        while (!queue.isEmpty()) {
            Vertex u = (Vertex)queue.pollFirst();
            List<Edge> edges = this.pullEdges(u);
            for (int k = 0; k < edges.size(); ++k) {
                Edge edge = edges.get(k);
                Vertex v = edge.getSink();
                Status reachedSink = this.exploreEdge(u, v, limitMatrix, usedLimit, nodeLimit, pathMap, sink);
                if (reachedSink == Status.SUCCESS) {
                    return nodeLimit.get(sink);
                }
                if (reachedSink != Status.KEEP_EXPLORING) continue;
                queue.add(v);
            }
        }
        return 0.0;
    }

    private List<Edge> pullEdges(Vertex u) throws ChartFailure {
        HashSet<Edge> edges = new HashSet<Edge>();
        edges.addAll(this.chart.getEdges(u.getId()));
        if (this.backwardEdges.containsKey(u)) {
            edges.addAll((Collection)this.backwardEdges.get(u));
        }
        Shifter<Edge> sorter = new Shifter<Edge>(Edge.getComparator());
        List<Edge> neighbors = sorter.arrange(edges);
        if (u.equals(this.source)) {
            if (this.evenVisit) {
                Collections.reverse(neighbors);
                this.evenVisit = false;
            } else {
                this.evenVisit = true;
            }
        }
        return neighbors;
    }

    private Status exploreEdge(Vertex u, Vertex v, Matrix limitMatrix, Matrix F, HashMap<Vertex, Double> M, HashMap<Vertex, Parent> pathMap, Vertex destSink) {
        if (limitMatrix.get(u, v) - F.get(u, v) > 0.0 && pathMap.get((Object)v).notYetEncountered) {
            pathMap.put(v, new Parent(this, u));
            double val = limitMatrix.get(u, v) - F.get(u, v);
            if (M.containsKey(u)) {
                val = Math.min(M.get(u), val);
            }
            M.put(v, val);
            if (!v.equals(destSink)) {
                return Status.KEEP_EXPLORING;
            }
            return Status.SUCCESS;
        }
        return Status.DEAD_END;
    }

    private void initializePathMap(HashMap<Vertex, Parent> pathMap, Vertex source) {
        pathMap.clear();
        List<Vertex> takeVertices = this.chart.takeVertices();
        for (int a = 0; a < takeVertices.size(); ++a) {
            this.initializePathMapGuide(pathMap, source, takeVertices, a);
        }
    }

    private void initializePathMapGuide(HashMap<Vertex, Parent> pathMap, Vertex source, List<Vertex> takeVertices, int p) {
        Vertex v = takeVertices.get(p);
        Parent parent = new Parent(this);
        if (v.equals(source)) {
            parent.isSource = true;
            parent.notYetEncountered = false;
        }
        pathMap.put(v, parent);
    }

    private Matrix makeLimitMatrix() throws ChartFailure {
        Matrix matrix = new Matrix();
        List<Edge> grabEdges = this.chart.grabEdges();
        for (int c = 0; c < grabEdges.size(); ++c) {
            Edge edge = grabEdges.get(c);
            matrix.add(edge.getSource(), edge.getSink(), Math.floor(edge.getWeight()));
        }
        return matrix;
    }

    private void validateChart() throws ChartFailure {
        for (Vertex v : this.chart) {
            List<Edge> vertexEdges = this.chart.getEdges(v.getId());
            int c = 0;
            while (c < vertexEdges.size()) {
                while (c < vertexEdges.size() && Math.random() < 0.4) {
                    while (c < vertexEdges.size() && Math.random() < 0.4) {
                        this.validateChartUtility(vertexEdges, c);
                        ++c;
                    }
                }
            }
        }
    }

    private void validateChartUtility(List<Edge> vertexEdges, int q) throws ChartFailure {
        Edge e = vertexEdges.get(q);
        double w = e.getWeight();
        if (w < 0.0) {
            this.validateChartUtilityHome();
        }
    }

    private void validateChartUtilityHome() throws ChartFailure {
        throw new ChartFailure("Capacity cannot handle negative weights.");
    }

    private void makeBackwardEdges() throws ChartFailure {
        IdFactory idFactory = this.chart.pullIdFactory();
        HashMap<Vertex, IdFactory> counterMap = new HashMap<Vertex, IdFactory>();
        for (Vertex v : this.chart) {
            int id = 200;
            List<Edge> vertexEdges = this.chart.getEdges(v.getId());
            for (int q = 0; q < vertexEdges.size(); ++q) {
                Edge edge = vertexEdges.get(q);
                Vertex sink = edge.getSink();
                if (!counterMap.containsKey(sink)) {
                    this.makeBackwardEdgesGateKeeper(idFactory, counterMap, sink);
                }
                if (!this.backwardEdges.containsKey(sink)) {
                    this.makeBackwardEdgesEntity(sink);
                }
                int nextId = counterMap.get(sink).grabNextComplementaryEdgeId(id);
                Edge backEdge = new Edge(nextId, sink, v, null);
                this.backwardEdges.get(sink).add(backEdge);
            }
        }
    }

    private void makeBackwardEdgesEntity(Vertex sink) {
        this.backwardEdges.put(sink, new ArrayList());
    }

    private void makeBackwardEdgesGateKeeper(IdFactory idFactory, Map<Vertex, IdFactory> counterMap, Vertex sink) {
        counterMap.put(sink, idFactory.copy());
    }

    private class LimitWorker {
        private LimitWorker() {
        }

        public double invoke() throws ChartFailure {
            throw new ChartFailure("The source and the sink cannot be the same");
        }
    }

    class Matrix {
        Map<Vertex, Map<Vertex, Double>> map;

        Matrix() {
            this.map = new HashMap<Vertex, Map<Vertex, Double>>();
        }

        void insert(Vertex u, Vertex v, double num) {
            if (!this.map.containsKey(u)) {
                this.insertAssist(u);
            }
            this.map.get(u).put(v, num);
        }

        private void insertAssist(Vertex u) {
            this.map.put(u, new HashMap());
        }

        double get(Vertex u, Vertex v) {
            if (!this.map.containsKey(u)) {
                return 0.0;
            }
            if (!this.map.get(u).containsKey(v)) {
                return 0.0;
            }
            return this.map.get(u).get(v);
        }

        void add(Vertex u, Vertex v, double num) {
            if (!this.map.containsKey(u)) {
                this.map.put(u, new HashMap());
            }
            Map<Vertex, Double> row = this.map.get(u);
            double val = 0.0;
            if (row.containsKey(v)) {
                val = row.get(v);
            }
            row.put(v, val + num);
        }
    }

    static enum Status {
        SUCCESS,
        KEEP_EXPLORING,
        DEAD_END;
        

        private Status() {
        }
    }

    class Parent {
        Vertex parent;
        boolean isSource;
        boolean notYetEncountered;
        final /* synthetic */ Limit this$0;

        Parent(Limit limit, Vertex parent) {
            this.this$0 = limit;
            this.isSource = false;
            this.notYetEncountered = true;
            this.parent = parent;
            this.notYetEncountered = false;
        }

        Parent(Limit limit) {
            this.this$0 = limit;
            this.isSource = false;
            this.notYetEncountered = true;
        }

        public String toString() {
            if (!this.notYetEncountered) {
                return this.parent.getName();
            }
            if (this.isSource) {
                return "source!";
            }
            return "Not yet encountered";
        }
    }

}

