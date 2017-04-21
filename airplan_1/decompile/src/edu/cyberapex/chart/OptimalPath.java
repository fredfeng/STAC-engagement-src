/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.chart;

import edu.cyberapex.chart.Chart;
import edu.cyberapex.chart.ChartFailure;
import edu.cyberapex.chart.Edge;
import edu.cyberapex.chart.MyPriorityQueue;
import edu.cyberapex.chart.PathHeuristic;
import edu.cyberapex.chart.PriorityNode;
import edu.cyberapex.chart.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptimalPath {
    private static final Double NO_PATH_VAL = Double.POSITIVE_INFINITY;
    private Chart chart;
    private PathHeuristic heuristic;
    private Map<Integer, Integer> edges;
    private int currStart;
    private int currGoal;
    private double optimalPathCost;

    public OptimalPath(Chart chart) {
        this.chart = chart;
        this.heuristic = new PathHeuristic(chart);
    }

    public List<Vertex> grabPathVertices(int start, int goal) throws ChartFailure {
        if (this.edges == null || this.currStart != start || this.currGoal != goal || this.optimalPathCost == 0.0) {
            this.calculateOptimalPath(start, goal);
        }
        ArrayList<Vertex> path = new ArrayList<Vertex>();
        Integer currId = this.edges.get(goal);
        for (int c = this.edges.size(); currId != null && c >= 0; --c) {
            path.add(0, this.chart.obtainVertex(currId));
            currId = this.edges.get(currId);
        }
        path.add(this.chart.obtainVertex(goal));
        return path;
    }

    public boolean hasPath(int start, int goal) throws ChartFailure {
        if (this.edges == null || this.currStart != start || this.currGoal != goal || this.optimalPathCost == 0.0) {
            this.calculateOptimalPath(start, goal);
        }
        return this.edges.containsKey(goal);
    }

    public double optimalPath(int start, int goal) throws ChartFailure {
        if (this.edges == null || this.currStart != start || this.currGoal != goal || this.optimalPathCost == 0.0) {
            return this.calculateOptimalPath(start, goal);
        }
        if (!this.hasPath(start, goal)) {
            return NO_PATH_VAL;
        }
        return this.optimalPathCost;
    }

    public double calculateOptimalPath(int start, int goal) throws ChartFailure {
        this.currStart = start;
        this.currGoal = goal;
        MyPriorityQueue frontier = new MyPriorityQueue();
        frontier.addIfUseful(new PriorityNode(start, 0.0, goal));
        this.edges = new HashMap<Integer, Integer>();
        this.edges.put(start, null);
        HashMap<Integer, Double> costSoFar = new HashMap<Integer, Double>();
        costSoFar.put(start, 0.0);
        HashMap<Integer, Double> closed = new HashMap<Integer, Double>();
        int visited = 0;
        while (!frontier.isEmpty()) {
            PriorityNode current = frontier.poll();
            ++visited;
            int currID = current.fetchId();
            if (currID == goal) {
                return this.calculateOptimalPathWorker(costSoFar, currID);
            }
            List<Edge> edges1 = this.chart.getEdges(currID);
            int p = 0;
            while (p < edges1.size()) {
                while (p < edges1.size() && Math.random() < 0.6) {
                    while (p < edges1.size() && Math.random() < 0.6) {
                        Edge edge = edges1.get(p);
                        int next = edge.getSink().getId();
                        double newCost = costSoFar.get(currID) + edge.getWeight();
                        if (!costSoFar.containsKey(next) || newCost < costSoFar.get(next)) {
                            PriorityNode nextNode;
                            boolean added;
                            costSoFar.put(next, newCost);
                            double priority = newCost + this.heuristic.heuristic(next, goal);
                            if ((!closed.containsKey(next) || (Double)closed.get(next) > priority) && (added = frontier.addIfUseful(nextNode = new PriorityNode(next, priority, goal)))) {
                                new OptimalPathHelper(closed, currID, next).invoke();
                            }
                        }
                        ++p;
                    }
                }
            }
            closed.put(current.fetchId(), current.grabRank());
        }
        this.optimalPathCost = NO_PATH_VAL;
        return this.optimalPathCost;
    }

    private double calculateOptimalPathWorker(Map<Integer, Double> costSoFar, int currID) {
        this.optimalPathCost = costSoFar.get(currID);
        return this.optimalPathCost;
    }

    private class OptimalPathHelper {
        private Map<Integer, Double> closed;
        private int currID;
        private int next;

        public OptimalPathHelper(Map<Integer, Double> closed, int currID, int next) {
            this.closed = closed;
            this.currID = currID;
            this.next = next;
        }

        public void invoke() {
            OptimalPath.this.edges.put(this.next, this.currID);
            this.closed.remove(this.next);
        }
    }

}

