/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.chart;

import com.networkapex.chart.Edge;
import com.networkapex.chart.Graph;
import com.networkapex.chart.GraphRaiser;
import com.networkapex.chart.Vertex;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class ConnectedAlg {
    public static boolean isConnected(Graph graph) throws GraphRaiser {
        HashSet<Integer> reachableVertices = new HashSet<Integer>();
        HashSet<Integer> transReachableVertices = new HashSet<Integer>();
        Stack<Vertex> vertexStack = new Stack<Vertex>();
        Vertex startVertex = graph.getVertices().get(0);
        reachableVertices.add(startVertex.getId());
        vertexStack.push(startVertex);
        while (!vertexStack.isEmpty()) {
            Vertex currentV = (Vertex)vertexStack.pop();
            List<Edge> edges = currentV.getEdges();
            for (int c = 0; c < edges.size(); ++c) {
                ConnectedAlg.isConnectedCoordinator(reachableVertices, vertexStack, edges, c);
            }
        }
        Graph transGraph = graph.transpose();
        Vertex transStartVertex = transGraph.getVertices().get(0);
        transReachableVertices.add(transStartVertex.getId());
        vertexStack.push(transStartVertex);
        while (!vertexStack.isEmpty()) {
            Vertex currentV = vertexStack.pop();
            List<Edge> edges = currentV.getEdges();
            for (int i = 0; i < edges.size(); ++i) {
                ConnectedAlg.isConnectedGateKeeper(transReachableVertices, vertexStack, edges, i);
            }
        }
        for (Vertex vertex : graph) {
            if (reachableVertices.contains(vertex.getId()) && transReachableVertices.contains(vertex.getId())) continue;
            return false;
        }
        return true;
    }

    private static void isConnectedGateKeeper(Set<Integer> transReachableVertices, Stack<Vertex> vertexStack, List<Edge> edges, int j) {
        Edge e = edges.get(j);
        Vertex reachedV = e.getSink();
        if (!transReachableVertices.contains(reachedV.getId())) {
            ConnectedAlg.isConnectedGateKeeperEntity(transReachableVertices, vertexStack, reachedV);
        }
    }

    private static void isConnectedGateKeeperEntity(Set<Integer> transReachableVertices, Stack<Vertex> vertexStack, Vertex reachedV) {
        transReachableVertices.add(reachedV.getId());
        vertexStack.push(reachedV);
    }

    private static void isConnectedCoordinator(Set<Integer> reachableVertices, Stack<Vertex> vertexStack, List<Edge> edges, int p) {
        Edge e = edges.get(p);
        Vertex reachedV = e.getSink();
        if (!reachableVertices.contains(reachedV.getId())) {
            reachableVertices.add(reachedV.getId());
            vertexStack.push(reachedV);
        }
    }
}

