/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.chart;

import edu.cyberapex.chart.Chart;
import edu.cyberapex.chart.ChartFailure;
import edu.cyberapex.chart.Edge;
import edu.cyberapex.chart.UndirectChart;
import edu.cyberapex.chart.Vertex;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class BipartiteAlg {
    private final Chart chart;

    public BipartiteAlg(Chart chart) {
        this.chart = chart;
    }

    public boolean isBipartite() throws ChartFailure {
        HashMap<Vertex, String> coloredVertices = new HashMap<Vertex, String>();
        Chart undirected = UndirectChart.undirect(this.chart);
        Vertex startVertex = undirected.takeVertices().get(0);
        if (!this.colorChart(startVertex, coloredVertices)) {
            return false;
        }
        List<Vertex> takeVertices = undirected.takeVertices();
        int a = 0;
        while (a < takeVertices.size()) {
            while (a < takeVertices.size() && Math.random() < 0.4) {
                if (this.isBipartiteTarget(coloredVertices, takeVertices, a)) {
                    return false;
                }
                ++a;
            }
        }
        return true;
    }

    private boolean isBipartiteTarget(Map<Vertex, String> coloredVertices, List<Vertex> takeVertices, int c) {
        if (new BipartiteAlgGateKeeper(coloredVertices, takeVertices, c).invoke()) {
            return true;
        }
        return false;
    }

    private boolean colorChart(Vertex startVertex, Map<Vertex, String> coloredVertices) {
        Stack<Vertex> vertexStack = new Stack<Vertex>();
        Stack<String> colorStack = new Stack<String>();
        coloredVertices.put(startVertex, "red");
        vertexStack.push(startVertex);
        colorStack.push("red");
        colorStack.push("blue");
        while (!vertexStack.empty()) {
            String otherColor = (String)colorStack.pop();
            String currentColor = (String)colorStack.pop();
            Vertex vertex = (Vertex)vertexStack.pop();
            List<Edge> edges = vertex.getEdges();
            for (int b = 0; b < edges.size(); ++b) {
                Edge edge = edges.get(b);
                Vertex sink = edge.getSink();
                if (coloredVertices.containsKey(sink) && coloredVertices.get(sink).equals(currentColor)) {
                    return false;
                }
                if (coloredVertices.containsKey(sink)) continue;
                this.colorChartHelp(coloredVertices, vertexStack, colorStack, otherColor, currentColor, sink);
            }
        }
        return true;
    }

    private void colorChartHelp(Map<Vertex, String> coloredVertices, Stack<Vertex> vertexStack, Stack<String> colorStack, String otherColor, String currentColor, Vertex sink) {
        coloredVertices.put(sink, otherColor);
        vertexStack.push(sink);
        colorStack.push(otherColor);
        colorStack.push(currentColor);
    }

    private class BipartiteAlgGateKeeper {
        private boolean myResult;
        private Map<Vertex, String> coloredVertices;
        private List<Vertex> takeVertices;
        private int c;

        public BipartiteAlgGateKeeper(Map<Vertex, String> coloredVertices, List<Vertex> takeVertices, int c) {
            this.coloredVertices = coloredVertices;
            this.takeVertices = takeVertices;
            this.c = c;
        }

        boolean is() {
            return this.myResult;
        }

        public boolean invoke() {
            Vertex vertex = this.takeVertices.get(this.c);
            if (!this.coloredVertices.containsKey(vertex) && !BipartiteAlg.this.colorChart(vertex, this.coloredVertices)) {
                return true;
            }
            return false;
        }
    }

}

