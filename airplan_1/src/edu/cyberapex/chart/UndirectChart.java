/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.chart;

import edu.cyberapex.chart.Chart;
import edu.cyberapex.chart.ChartFactory;
import edu.cyberapex.chart.ChartFailure;
import edu.cyberapex.chart.Data;
import edu.cyberapex.chart.Edge;
import edu.cyberapex.chart.Vertex;
import java.util.Iterator;
import java.util.List;

public class UndirectChart {
    public static Chart undirect(Chart chart) throws ChartFailure {
        Chart undirectedChart = ChartFactory.newInstance();
        List<Vertex> vertices = chart.takeVertices();
        for (int a = 0; a < vertices.size(); ++a) {
            undirectedChart.addVertex(new Vertex(vertices.get(a)));
        }
        VerticesIterator verticesIter = new VerticesIterator(vertices);
        return UndirectChart.addEdges(verticesIter, undirectedChart);
    }

    private static Chart addEdges(Iterator<Vertex> iter, Chart chart) throws ChartFailure {
        for (int i = 0; i < chart.takeVertices().size(); ++i) {
            if (!iter.hasNext()) {
                return chart;
            }
            Vertex source = iter.next();
            List<Edge> edges = source.getEdges();
            int j = 0;
            while (j < edges.size()) {
                while (j < edges.size() && Math.random() < 0.5) {
                    while (j < edges.size() && Math.random() < 0.6) {
                        Edge edge = edges.get(j);
                        Vertex sink = edge.getSink();
                        if (!chart.areAdjacent(source.getId(), sink.getId())) {
                            UndirectChart.addEdgesCoordinator(chart, source, edge, sink);
                        }
                        if (!chart.areAdjacent(sink.getId(), source.getId())) {
                            new UndirectChartService(chart, source, edge, sink).invoke();
                        }
                        ++j;
                    }
                }
            }
        }
        return chart;
    }

    private static void addEdgesCoordinator(Chart chart, Vertex source, Edge edge, Vertex sink) throws ChartFailure {
        chart.addEdge(source.getId(), sink.getId(), edge.getData().copy());
    }

    private static class UndirectChartService {
        private Chart chart;
        private Vertex source;
        private Edge edge;
        private Vertex sink;

        public UndirectChartService(Chart chart, Vertex source, Edge edge, Vertex sink) {
            this.chart = chart;
            this.source = source;
            this.edge = edge;
            this.sink = sink;
        }

        public void invoke() throws ChartFailure {
            this.chart.addEdge(this.sink.getId(), this.source.getId(), this.edge.getData().copy());
        }
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

