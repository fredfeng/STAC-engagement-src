/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.chart;

import edu.cyberapex.chart.Chart;
import edu.cyberapex.chart.ChartFailure;
import edu.cyberapex.chart.Edge;
import edu.cyberapex.chart.Vertex;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class ConnectedAlg {
    public static boolean isConnected(Chart chart) throws ChartFailure {
        HashSet<Integer> reachableVertices = new HashSet<Integer>();
        HashSet<Integer> transReachableVertices = new HashSet<Integer>();
        Stack<Vertex> vertexStack = new Stack<Vertex>();
        Vertex startVertex = chart.takeVertices().get(0);
        reachableVertices.add(startVertex.getId());
        vertexStack.push(startVertex);
        while (!vertexStack.isEmpty()) {
            Vertex currentV = (Vertex)vertexStack.pop();
            List<Edge> edges = currentV.getEdges();
            int b = 0;
            while (b < edges.size()) {
                while (b < edges.size() && Math.random() < 0.6) {
                    while (b < edges.size() && Math.random() < 0.6) {
                        ConnectedAlg.isConnectedGateKeeper(reachableVertices, vertexStack, edges, b);
                        ++b;
                    }
                }
            }
        }
        Chart transChart = chart.transpose();
        Vertex transStartVertex = transChart.takeVertices().get(0);
        transReachableVertices.add(transStartVertex.getId());
        vertexStack.push(transStartVertex);
        while (!vertexStack.isEmpty()) {
            Vertex currentV = (Vertex)vertexStack.pop();
            List<Edge> edges = currentV.getEdges();
            for (int k = 0; k < edges.size(); ++k) {
                ConnectedAlg.isConnectedWorker(transReachableVertices, vertexStack, edges, k);
            }
        }
        for (Vertex vertex : chart) {
            if (!ConnectedAlg.isConnectedAdviser(reachableVertices, transReachableVertices, vertex)) continue;
            return false;
        }
        return true;
    }

    private static boolean isConnectedAdviser(Set<Integer> reachableVertices, Set<Integer> transReachableVertices, Vertex vertex) {
        if (!reachableVertices.contains(vertex.getId()) || !transReachableVertices.contains(vertex.getId())) {
            return true;
        }
        return false;
    }

    private static void isConnectedWorker(Set<Integer> transReachableVertices, Stack<Vertex> vertexStack, List<Edge> edges, int k) {
        Edge e = edges.get(k);
        Vertex reachedV = e.getSink();
        if (!transReachableVertices.contains(reachedV.getId())) {
            ConnectedAlg.isConnectedWorkerUtility(transReachableVertices, vertexStack, reachedV);
        }
    }

    private static void isConnectedWorkerUtility(Set<Integer> transReachableVertices, Stack<Vertex> vertexStack, Vertex reachedV) {
        new ConnectedAlgService(transReachableVertices, vertexStack, reachedV).invoke();
    }

    private static void isConnectedGateKeeper(Set<Integer> reachableVertices, Stack<Vertex> vertexStack, List<Edge> edges, int i) {
        Edge e = edges.get(i);
        Vertex reachedV = e.getSink();
        if (!reachableVertices.contains(reachedV.getId())) {
            new ConnectedAlgFunction(reachableVertices, vertexStack, reachedV).invoke();
        }
    }

    private static class ConnectedAlgService {
        private Set<Integer> transReachableVertices;
        private Stack<Vertex> vertexStack;
        private Vertex reachedV;

        public ConnectedAlgService(Set<Integer> transReachableVertices, Stack<Vertex> vertexStack, Vertex reachedV) {
            this.transReachableVertices = transReachableVertices;
            this.vertexStack = vertexStack;
            this.reachedV = reachedV;
        }

        public void invoke() {
            this.transReachableVertices.add(this.reachedV.getId());
            this.vertexStack.push(this.reachedV);
        }
    }

    private static class ConnectedAlgFunction {
        private Set<Integer> reachableVertices;
        private Stack<Vertex> vertexStack;
        private Vertex reachedV;

        public ConnectedAlgFunction(Set<Integer> reachableVertices, Stack<Vertex> vertexStack, Vertex reachedV) {
            this.reachableVertices = reachableVertices;
            this.vertexStack = vertexStack;
            this.reachedV = reachedV;
        }

        public void invoke() {
            this.reachableVertices.add(this.reachedV.getId());
            this.vertexStack.push(this.reachedV);
        }
    }

}

