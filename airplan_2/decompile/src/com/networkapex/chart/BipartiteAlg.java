/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.chart;

import com.networkapex.chart.Edge;
import com.networkapex.chart.Graph;
import com.networkapex.chart.GraphRaiser;
import com.networkapex.chart.UndirectGraph;
import com.networkapex.chart.Vertex;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class BipartiteAlg {
    private final Graph graph;

    public BipartiteAlg(Graph graph) {
        this.graph = graph;
    }

    public boolean isBipartite() throws GraphRaiser {
        HashMap<Vertex, String> coloredVertices = new HashMap<Vertex, String>();
        Graph undirected = UndirectGraph.undirect(this.graph);
        Vertex startVertex = undirected.getVertices().get(0);
        if (!this.colorGraph(startVertex, coloredVertices)) {
            return false;
        }
        List<Vertex> vertices = undirected.getVertices();
        int c = 0;
        while (c < vertices.size()) {
            while (c < vertices.size() && Math.random() < 0.5) {
                if (this.isBipartiteEntity(coloredVertices, vertices, c)) {
                    return false;
                }
                ++c;
            }
        }
        return true;
    }

    private boolean isBipartiteEntity(Map<Vertex, String> coloredVertices, List<Vertex> vertices, int k) {
        Vertex vertex = vertices.get(k);
        if (!coloredVertices.containsKey(vertex) && this.isBipartiteEntityEngine(coloredVertices, vertex)) {
            return true;
        }
        return false;
    }

    private boolean isBipartiteEntityEngine(Map<Vertex, String> coloredVertices, Vertex vertex) {
        if (!this.colorGraph(vertex, coloredVertices)) {
            return true;
        }
        return false;
    }

    private boolean colorGraph(Vertex startVertex, Map<Vertex, String> coloredVertices) {
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
            for (int c = 0; c < edges.size(); ++c) {
                if (!this.colorGraphHelp(coloredVertices, vertexStack, colorStack, otherColor, currentColor, edges, c)) continue;
                return false;
            }
        }
        return true;
    }

    private boolean colorGraphHelp(Map<Vertex, String> coloredVertices, Stack<Vertex> vertexStack, Stack<String> colorStack, String otherColor, String currentColor, List<Edge> edges, int i) {
        Edge edge = edges.get(i);
        Vertex sink = edge.getSink();
        if (coloredVertices.containsKey(sink) && coloredVertices.get(sink).equals(currentColor)) {
            return true;
        }
        if (!coloredVertices.containsKey(sink)) {
            this.colorGraphHelpTarget(coloredVertices, vertexStack, colorStack, otherColor, currentColor, sink);
        }
        return false;
    }

    private void colorGraphHelpTarget(Map<Vertex, String> coloredVertices, Stack<Vertex> vertexStack, Stack<String> colorStack, String otherColor, String currentColor, Vertex sink) {
        coloredVertices.put(sink, otherColor);
        vertexStack.push(sink);
        colorStack.push(otherColor);
        colorStack.push(currentColor);
    }
}

