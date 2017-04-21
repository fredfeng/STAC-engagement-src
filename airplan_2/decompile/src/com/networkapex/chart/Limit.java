/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.chart;

import com.networkapex.chart.Data;
import com.networkapex.chart.Edge;
import com.networkapex.chart.Graph;
import com.networkapex.chart.GraphRaiser;
import com.networkapex.chart.IdFactory;
import com.networkapex.chart.Vertex;
import com.networkapex.sort.Orderer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Limit {
    private Map<Vertex, List<Edge>> backwardEdges = new HashMap<Vertex, List<Edge>>();
    private Graph graph;
    private Matrix savedUsedLimits;
    private boolean evenVisit = false;
    private Vertex source;

    public Limit(Graph graph) {
        this.graph = graph;
    }

    public double limit(String sourceName, String sinkName) throws GraphRaiser {
        Vertex sink;
        Vertex source = this.graph.takeVertex(this.graph.takeVertexIdByName(sourceName));
        if (source.equals(sink = this.graph.takeVertex(this.graph.takeVertexIdByName(sinkName)))) {
            throw new GraphRaiser("The source and the sink cannot be the same");
        }
        this.source = source;
        this.validateGraph();
        this.makeBackwardEdges();
        double weight = 0.0;
        Matrix limitMatrix = this.makeLimitMatrix();
        Matrix usedLimit = new Matrix();
        HashMap<Vertex, Parent> trailMap = new HashMap<Vertex, Parent>();
        block0 : do {
            double additionalLimit = this.search(limitMatrix, source, sink, usedLimit, trailMap);
            weight += additionalLimit;
            if (additionalLimit == 0.0) break;
            Vertex curr = sink;
            do {
                if (curr.equals(source)) continue block0;
                Vertex next = trailMap.get((Object)curr).parent;
                usedLimit.insert(next, curr, usedLimit.pull(next, curr) + additionalLimit);
                usedLimit.insert(curr, next, usedLimit.pull(curr, next) - additionalLimit);
                curr = next;
            } while (true);
        } while (true);
        this.savedUsedLimits = usedLimit;
        return weight;
    }

    public Map<Vertex, Map<Vertex, Double>> pullLimitTrails(String sourceName, String sinkName) throws GraphRaiser {
        if (this.savedUsedLimits == null) {
            this.obtainLimitTrailsAssist(sourceName, sinkName);
        }
        return this.savedUsedLimits.map;
    }

    private void obtainLimitTrailsAssist(String sourceName, String sinkName) throws GraphRaiser {
        this.limit(sourceName, sinkName);
    }

    private double search(Matrix limitMatrix, Vertex source, Vertex sink, Matrix usedLimit, HashMap<Vertex, Parent> trailMap) throws GraphRaiser {
        this.initializeTrailMap(trailMap, source);
        return this.search(limitMatrix, source, sink, usedLimit, trailMap, new HashMap<Vertex, Double>());
    }

    private double search(Matrix limitMatrix, Vertex source, Vertex sink, Matrix usedLimit, HashMap<Vertex, Parent> trailMap, HashMap<Vertex, Double> nodeLimit) throws GraphRaiser {
        ArrayDeque<Vertex> queue = new ArrayDeque<Vertex>();
        queue.addLast(source);
        Vertex u = (Vertex)queue.pollFirst();
        List<Edge> edges = this.grabEdges(u);
        for (int k = 0; k < edges.size(); ++k) {
            double val;
            Edge edge = edges.get(k);
            Vertex v = edge.getSink();
            Status reachedSink = this.exploreEdge(u, v, limitMatrix, usedLimit, nodeLimit, trailMap, sink);
            if (reachedSink == Status.SUCCESS) {
                return nodeLimit.get(sink);
            }
            if (reachedSink != Status.KEEP_EXPLORING || (val = this.search(limitMatrix, v, sink, usedLimit, trailMap, nodeLimit)) <= 0.0) continue;
            return val;
        }
        return 0.0;
    }

    private List<Edge> grabEdges(Vertex u) throws GraphRaiser {
        HashSet<Edge> edges = new HashSet<Edge>();
        edges.addAll(this.graph.grabEdges(u.getId()));
        if (this.backwardEdges.containsKey(u)) {
            this.obtainEdgesAid(u, edges);
        }
        Orderer<Edge> sorter = new Orderer<Edge>(Edge.getComparator());
        List<Edge> neighbors = sorter.rank(edges);
        return neighbors;
    }

    private void obtainEdgesAid(Vertex u, Set<Edge> edges) {
        edges.addAll((Collection)this.backwardEdges.get(u));
    }

    private Status exploreEdge(Vertex u, Vertex v, Matrix limitMatrix, Matrix F, HashMap<Vertex, Double> M, HashMap<Vertex, Parent> trailMap, Vertex destSink) {
        if (limitMatrix.pull(u, v) - F.pull(u, v) > 0.0 && trailMap.get((Object)v).notYetEncountered) {
            trailMap.put(v, new Parent(this, u));
            double val = limitMatrix.pull(u, v) - F.pull(u, v);
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

    private void initializeTrailMap(HashMap<Vertex, Parent> trailMap, Vertex source) {
        trailMap.clear();
        List<Vertex> vertices = this.graph.getVertices();
        for (int a = 0; a < vertices.size(); ++a) {
            this.initializeTrailMapAssist(trailMap, source, vertices, a);
        }
    }

    private void initializeTrailMapAssist(HashMap<Vertex, Parent> trailMap, Vertex source, List<Vertex> vertices, int q) {
        Vertex v = vertices.get(q);
        Parent parent = new Parent(this);
        if (v.equals(source)) {
            parent.isSource = true;
            parent.notYetEncountered = false;
        }
        trailMap.put(v, parent);
    }

    private Matrix makeLimitMatrix() throws GraphRaiser {
        Matrix matrix = new Matrix();
        List<Edge> edges = this.graph.getEdges();
        for (int k = 0; k < edges.size(); ++k) {
            Edge edge = edges.get(k);
            matrix.add(edge.getSource(), edge.getSink(), Math.floor(edge.getWeight()));
        }
        return matrix;
    }

    private void validateGraph() throws GraphRaiser {
        for (Vertex v : this.graph) {
            List<Edge> vertexEdges = this.graph.grabEdges(v.getId());
            int p = 0;
            while (p < vertexEdges.size()) {
                while (p < vertexEdges.size() && Math.random() < 0.6) {
                    this.validateGraphTarget(vertexEdges, p);
                    ++p;
                }
            }
        }
    }

    private void validateGraphTarget(List<Edge> vertexEdges, int k) throws GraphRaiser {
        Edge e = vertexEdges.get(k);
        double w = e.getWeight();
        if (w < 0.0) {
            throw new GraphRaiser("Capacity cannot handle negative weights.");
        }
    }

    private void makeBackwardEdges() throws GraphRaiser {
        IdFactory idFactory = this.graph.fetchIdFactory();
        HashMap<Vertex, IdFactory> counterMap = new HashMap<Vertex, IdFactory>();
        for (Vertex v : this.graph) {
            int id = 200;
            List<Edge> vertexEdges = this.graph.grabEdges(v.getId());
            for (int q = 0; q < vertexEdges.size(); ++q) {
                Edge edge = vertexEdges.get(q);
                Vertex sink = edge.getSink();
                if (!counterMap.containsKey(sink)) {
                    counterMap.put(sink, idFactory.copy());
                }
                if (!this.backwardEdges.containsKey(sink)) {
                    this.backwardEdges.put(sink, new ArrayList());
                }
                int nextId = ((IdFactory)counterMap.get(sink)).takeNextComplementaryEdgeId(id);
                Edge backEdge = new Edge(nextId, sink, v, null);
                this.backwardEdges.get(sink).add(backEdge);
            }
        }
    }

    class Matrix {
        Map<Vertex, Map<Vertex, Double>> map;

        Matrix() {
            this.map = new HashMap<Vertex, Map<Vertex, Double>>();
        }

        void insert(Vertex u, Vertex v, double num) {
            if (!this.map.containsKey(u)) {
                this.putGateKeeper(u);
            }
            this.map.get(u).put(v, num);
        }

        private void putGateKeeper(Vertex u) {
            this.map.put(u, new HashMap());
        }

        double pull(Vertex u, Vertex v) {
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
                this.addHelper(u);
            }
            Map<Vertex, Double> row = this.map.get(u);
            double val = 0.0;
            if (row.containsKey(v)) {
                val = row.get(v);
            }
            row.put(v, val + num);
        }

        private void addHelper(Vertex u) {
            this.map.put(u, new HashMap());
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

