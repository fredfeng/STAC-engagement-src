/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.chart;

import com.networkapex.chart.Edge;
import com.networkapex.chart.Graph;
import com.networkapex.chart.GraphRaiser;
import com.networkapex.chart.Vertex;
import java.util.List;

public class EulerianAlg {
    public static boolean hasOddDegree(Graph graph) throws GraphRaiser {
        List<Vertex> vertices = graph.getVertices();
        for (int k = 0; k < vertices.size(); ++k) {
            if (!EulerianAlg.hasOddDegreeUtility(graph, vertices, k)) continue;
            return false;
        }
        return true;
    }

    private static boolean hasOddDegreeUtility(Graph graph, List<Vertex> vertices, int q) throws GraphRaiser {
        Vertex v = vertices.get(q);
        List<Edge> edges = graph.grabEdges(v.getId());
        if (edges.size() % 2 != 0) {
            return true;
        }
        return false;
    }
}

