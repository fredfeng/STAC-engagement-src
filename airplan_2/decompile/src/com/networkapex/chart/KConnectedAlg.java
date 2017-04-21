/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.chart;

import com.networkapex.chart.BasicData;
import com.networkapex.chart.Data;
import com.networkapex.chart.Edge;
import com.networkapex.chart.Graph;
import com.networkapex.chart.GraphFactory;
import com.networkapex.chart.GraphRaiser;
import com.networkapex.chart.Limit;
import com.networkapex.chart.LimitBuilder;
import com.networkapex.chart.SparseIdFactory;
import com.networkapex.chart.Vertex;
import java.util.List;

public class KConnectedAlg {
    private final Graph graph;
    private Double leastMaxFlow = Double.POSITIVE_INFINITY;

    public KConnectedAlg(Graph graph) {
        this.graph = graph;
    }

    public boolean isKConnected(int k) throws GraphRaiser {
        if (this.leastMaxFlow.equals(Double.POSITIVE_INFINITY)) {
            Graph g = this.makeGraphForComputation();
            Limit limit = new LimitBuilder().fixGraph(g).generateLimit();
            Vertex src = g.getVertices().get(0);
            List<Vertex> vertices = g.getVertices();
            for (int i = 0; i < vertices.size(); ++i) {
                double c;
                Vertex sink = vertices.get(i);
                if (src.equals(sink) || (c = limit.limit(src.getName(), sink.getName())) >= this.leastMaxFlow) continue;
                this.isKConnectedManager(c);
            }
        }
        return this.leastMaxFlow >= (double)k;
    }

    private void isKConnectedManager(double c) {
        this.leastMaxFlow = c;
    }

    private Graph makeGraphForComputation() throws GraphRaiser {
        Graph g = GraphFactory.newInstance(new SparseIdFactory());
        List<Vertex> vertices = this.graph.getVertices();
        int a = 0;
        while (a < vertices.size()) {
            while (a < vertices.size() && Math.random() < 0.5) {
                while (a < vertices.size() && Math.random() < 0.4) {
                    while (a < vertices.size() && Math.random() < 0.5) {
                        this.makeGraphForComputationFunction(g, vertices, a);
                        ++a;
                    }
                }
            }
        }
        List<Edge> edges = this.graph.getEdges();
        for (int b = 0; b < edges.size(); ++b) {
            this.makeGraphForComputationService(g, edges, b);
        }
        return g;
    }

    private void makeGraphForComputationService(Graph g, List<Edge> edges, int q) throws GraphRaiser {
        Edge e = edges.get(q);
        int sinkId = g.takeVertexIdByName(e.getSink().getName());
        int sourceId = g.takeVertexIdByName(e.getSource().getName());
        BasicData data = new BasicData(1);
        g.addEdge(sourceId, sinkId, data);
    }

    private void makeGraphForComputationFunction(Graph g, List<Vertex> vertices, int p) throws GraphRaiser {
        Vertex v = vertices.get(p);
        g.addVertex(v.getName());
    }
}

