/*
 * Decompiled with CFR 0_121.
 */
package com.example.linalg.internal.graph.util;

import com.example.linalg.internal.graph.util.DirectedEdge;
import com.example.linalg.internal.graph.util.Edge;
import com.example.linalg.internal.graph.util.EdgeWeightedDigraph;
import com.example.linalg.internal.graph.util.EdgeWeightedGraph;

public class Matrix {
    public static EdgeWeightedDigraph toDiGraph(double[][] adjacency) {
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(adjacency.length);
        for (int i = 0; i < adjacency.length; ++i) {
            for (int j = 0; j < adjacency.length; ++j) {
                if (adjacency[i][j] == 0.0) continue;
                G.addEdge(new DirectedEdge(i, j, adjacency[i][j]));
            }
        }
        return G;
    }

    public static EdgeWeightedGraph toGraph(double[][] adjacency) {
        EdgeWeightedGraph G = new EdgeWeightedGraph(adjacency.length);
        for (int i = 0; i < adjacency.length; ++i) {
            for (int j = 0; j < adjacency.length; ++j) {
                if (adjacency[i][j] == 0.0) continue;
                G.addEdge(new Edge(i, j, adjacency[i][j]));
            }
        }
        return G;
    }
}

