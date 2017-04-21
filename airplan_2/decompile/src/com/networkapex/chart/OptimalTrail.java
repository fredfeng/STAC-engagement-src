/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.chart;

import com.networkapex.chart.Edge;
import com.networkapex.chart.Graph;
import com.networkapex.chart.GraphRaiser;
import com.networkapex.chart.Vertex;
import com.networkapex.nnsoft.trudeau.collections.fibonacciheap.FibonacciHeap;
import com.networkapex.nnsoft.trudeau.collections.fibonacciheap.FibonacciHeapBuilder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class OptimalTrail {
    private static final Double NO_PATH_VAL = Double.POSITIVE_INFINITY;
    private Graph graph;
    private Map<Integer, OptimalTrailVertex> optimalTrailVertices;
    private int currentStart = -1;
    private int currentGoal = -1;

    public OptimalTrail(Graph graph) {
        this.graph = graph;
    }

    public List<Vertex> obtainTrailVertices(int start, int goal) throws GraphRaiser {
        if (this.optimalTrailVertices == null || this.currentStart != start || this.currentGoal != goal) {
            this.calculateOptimalTrail(start, goal);
        }
        ArrayList<Vertex> trail = new ArrayList<Vertex>();
        this.buildTrail(start, goal, trail);
        return trail;
    }

    private void buildTrail(int start, int goal, List<Vertex> vertices) throws GraphRaiser {
        if (start != goal) {
            OptimalTrailVertex optimalTrailVertex = this.optimalTrailVertices.get(goal);
            if (optimalTrailVertex == null) {
                throw new GraphRaiser("No vertex exists with id " + goal);
            }
            if (!optimalTrailVertex.hasPreviousVertex()) {
                this.buildTrailSupervisor(start, goal);
            }
            this.buildTrail(start, optimalTrailVertex.obtainPreviousVertexId(), vertices);
        }
        vertices.add(this.graph.takeVertex(goal));
    }

    private void buildTrailSupervisor(int start, int goal) throws GraphRaiser {
        throw new GraphRaiser("No path exists from Vertex with id " + start + " to Vertex with id " + goal);
    }

    public boolean hasTrail(int start, int goal) throws GraphRaiser {
        if (this.optimalTrailVertices == null || this.currentStart != start || this.currentGoal != goal) {
            this.hasTrailCoordinator(start, goal);
        }
        if (this.optimalTrailVertices.get(goal).grabWeight() == NO_PATH_VAL.doubleValue()) {
            return false;
        }
        return true;
    }

    private void hasTrailCoordinator(int start, int goal) throws GraphRaiser {
        this.calculateOptimalTrail(start, goal);
    }

    public double optimalTrail(int start, int goal) throws GraphRaiser {
        if (this.optimalTrailVertices == null || this.currentStart != start || this.currentGoal != goal) {
            return this.calculateOptimalTrail(start, goal);
        }
        return this.optimalTrailVertices.get(goal).grabWeight();
    }

    public double calculateOptimalTrail(int start, int goal) throws GraphRaiser {
        this.graph.validateGraph();
        FibonacciHeap queue = new FibonacciHeapBuilder().setComparator(new OptimalTrailVertexComparator()).generateFibonacciHeap();
        this.optimalTrailVertices = new HashMap<Integer, OptimalTrailVertex>();
        this.currentStart = start;
        this.currentGoal = goal;
        OptimalTrailVertex temp = new OptimalTrailVertex(start, 0.0);
        this.optimalTrailVertices.put(start, temp);
        queue.add(temp);
        for (Vertex vertex : this.graph) {
            if (start == vertex.getId()) continue;
            temp = new OptimalTrailVertex(vertex.getId(), Double.POSITIVE_INFINITY);
            this.optimalTrailVertices.put(vertex.getId(), temp);
            queue.add(temp);
        }
        while (!queue.isEmpty()) {
            OptimalTrailVertex u = (OptimalTrailVertex)queue.poll();
            u.assignVisited();
            if (u.fetchVertexId() == goal) break;
            List<Edge> grabEdges = this.graph.grabEdges(u.fetchVertexId());
            for (int q = 0; q < grabEdges.size(); ++q) {
                this.calculateOptimalTrailHelper(queue, u, grabEdges, q);
            }
        }
        return this.optimalTrailVertices.get(goal).grabWeight();
    }

    private void calculateOptimalTrailHelper(Queue<OptimalTrailVertex> queue, OptimalTrailVertex u, List<Edge> grabEdges, int j) {
        Edge e = grabEdges.get(j);
        int v = e.getSink().getId();
        double alt = u.grabWeight() + e.getWeight();
        if (!this.optimalTrailVertices.get(e.getSink().getId()).hasVisited()) {
            this.calculateOptimalTrailHelperAid(queue, u, v, alt);
        }
    }

    private void calculateOptimalTrailHelperAid(Queue<OptimalTrailVertex> queue, OptimalTrailVertex u, int v, double alt) {
        if (alt < this.optimalTrailVertices.get(v).grabWeight()) {
            this.optimalTrailVertices.get(v).assignWeight(alt);
            this.optimalTrailVertices.get(v).definePreviousVertexId(u.fetchVertexId());
            queue.add(this.optimalTrailVertices.get(v));
        }
    }

    private static class OptimalTrailVertexComparator
    implements Comparator<OptimalTrailVertex> {
        private OptimalTrailVertexComparator() {
        }

        @Override
        public int compare(OptimalTrailVertex v1, OptimalTrailVertex v2) {
            return Double.compare(v1.grabWeight(), v2.grabWeight());
        }
    }

    private static class OptimalTrailVertex {
        private int vertexId;
        private double weight;
        private boolean visited;
        private int previousVertexId;

        public OptimalTrailVertex(int vertexId, double weight) {
            this.vertexId = vertexId;
            this.weight = weight;
            this.visited = false;
            this.previousVertexId = -1;
        }

        public int fetchVertexId() {
            return this.vertexId;
        }

        public double grabWeight() {
            return this.weight;
        }

        public void assignWeight(double weight) {
            this.weight = weight;
        }

        public boolean hasVisited() {
            return this.visited;
        }

        public void assignVisited() {
            this.visited = true;
        }

        public boolean hasPreviousVertex() {
            return this.previousVertexId != -1;
        }

        public int obtainPreviousVertexId() {
            return this.previousVertexId;
        }

        public void definePreviousVertexId(int vertexId) {
            this.previousVertexId = vertexId;
        }
    }

}

