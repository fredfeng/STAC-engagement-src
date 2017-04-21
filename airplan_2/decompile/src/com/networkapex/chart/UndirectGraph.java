/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.chart;

import com.networkapex.chart.Data;
import com.networkapex.chart.Edge;
import com.networkapex.chart.Graph;
import com.networkapex.chart.GraphFactory;
import com.networkapex.chart.GraphRaiser;
import com.networkapex.chart.Vertex;
import java.util.Iterator;
import java.util.List;

public class UndirectGraph {
    public static Graph undirect(Graph graph) throws GraphRaiser {
        Graph undirectedGraph = GraphFactory.newInstance();
        List<Vertex> vertices = graph.getVertices();
        int c = 0;
        while (c < vertices.size()) {
            while (c < vertices.size() && Math.random() < 0.4) {
                while (c < vertices.size() && Math.random() < 0.4) {
                    undirectedGraph.addVertex(new Vertex(vertices.get(c)));
                    ++c;
                }
            }
        }
        VerticesIterator verticesIter = new VerticesIterator(vertices);
        return UndirectGraph.addEdges(verticesIter, undirectedGraph);
    }

    private static Graph addEdges(Iterator<Vertex> iter, Graph graph) throws GraphRaiser {
        for (int b = graph.getVertices().size(); b > 0 && iter.hasNext(); --b) {
            Vertex source = iter.next();
            List<Edge> edges = source.getEdges();
            for (int j = 0; j < edges.size(); ++j) {
                UndirectGraph.addEdgesWorker(graph, source, edges, j);
            }
        }
        return graph;
    }

    private static void addEdgesWorker(Graph graph, Vertex source, List<Edge> edges, int j) throws GraphRaiser {
        Edge edge = edges.get(j);
        Vertex sink = edge.getSink();
        if (!graph.areAdjacent(source.getId(), sink.getId())) {
            UndirectGraph.addEdgesWorkerAssist(graph, source, edge, sink);
        }
        if (!graph.areAdjacent(sink.getId(), source.getId())) {
            graph.addEdge(sink.getId(), source.getId(), edge.getData().copy());
        }
    }

    private static void addEdgesWorkerAssist(Graph graph, Vertex source, Edge edge, Vertex sink) throws GraphRaiser {
        graph.addEdge(source.getId(), sink.getId(), edge.getData().copy());
    }

    private static class VerticesIterator
    implements Iterator<Vertex> {
        private List<Vertex> vertexList;

        public VerticesIterator(List<Vertex> vertexList) {
            this.vertexList = vertexList;
        }

        @Override
        public boolean hasNext() {
            return this.vertexList.size() > 0;
        }

        @Override
        public Vertex next() {
            if (this.vertexList.size() > 1) {
                return this.vertexList.remove(this.vertexList.size() - 1);
            }
            return this.vertexList.get(0);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove not supported");
        }
    }

}

