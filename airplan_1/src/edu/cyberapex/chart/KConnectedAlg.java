/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.chart;

import edu.cyberapex.chart.BasicData;
import edu.cyberapex.chart.Chart;
import edu.cyberapex.chart.ChartFactory;
import edu.cyberapex.chart.ChartFailure;
import edu.cyberapex.chart.Data;
import edu.cyberapex.chart.Edge;
import edu.cyberapex.chart.Limit;
import edu.cyberapex.chart.LimitBuilder;
import edu.cyberapex.chart.SparseIdFactory;
import edu.cyberapex.chart.Vertex;
import java.util.List;

public class KConnectedAlg {
    private final Chart chart;
    private Double minMaxFlow = Double.POSITIVE_INFINITY;

    public KConnectedAlg(Chart chart) {
        this.chart = chart;
    }

    public boolean isKConnected(int k) throws ChartFailure {
        if (this.minMaxFlow.equals(Double.POSITIVE_INFINITY)) {
            Chart g = this.makeChartForComputation();
            Limit limit = new LimitBuilder().fixChart(g).generateLimit();
            Vertex src = g.takeVertices().get(0);
            List<Vertex> takeVertices = g.takeVertices();
            int i = 0;
            while (i < takeVertices.size()) {
                while (i < takeVertices.size() && Math.random() < 0.5) {
                    while (i < takeVertices.size() && Math.random() < 0.4) {
                        double c;
                        Vertex sink = takeVertices.get(i);
                        if (!src.equals(sink) && (c = limit.limit(src.getName(), sink.getName())) < this.minMaxFlow) {
                            this.isKConnectedAid(c);
                        }
                        ++i;
                    }
                }
            }
        }
        return this.minMaxFlow >= (double)k;
    }

    private void isKConnectedAid(double c) {
        this.minMaxFlow = c;
    }

    private Chart makeChartForComputation() throws ChartFailure {
        Chart g = ChartFactory.newInstance(new SparseIdFactory());
        List<Vertex> takeVertices = this.chart.takeVertices();
        for (int p = 0; p < takeVertices.size(); ++p) {
            Vertex v = takeVertices.get(p);
            g.addVertex(v.getName());
        }
        List<Edge> grabEdges = this.chart.grabEdges();
        for (int p = 0; p < grabEdges.size(); ++p) {
            Edge e = grabEdges.get(p);
            int sinkId = g.getVertexIdByName(e.getSink().getName());
            int sourceId = g.getVertexIdByName(e.getSource().getName());
            BasicData data = new BasicData(1);
            g.addEdge(sourceId, sinkId, data);
        }
        return g;
    }
}

