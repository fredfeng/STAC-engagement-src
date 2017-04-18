/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.chart;

import edu.cyberapex.chart.Chart;
import edu.cyberapex.chart.ChartFailure;
import edu.cyberapex.chart.Vertex;
import edu.cyberapex.order.Shifter;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PathHeuristic {
    private Chart graph;
    private Map<Integer, Double> values;

    public PathHeuristic(Chart graph) {
        this.graph = graph;
        this.populateValues();
    }

    public double computeHeuristic(int u, int v) throws ChartFailure {
        if (u == 1 || u == 0) {
            return 0.0;
        }
        return this.computeHeuristic(u - 1, v) + Math.pow(2.0, (double)u - 2.0) + 2.0;
    }

    public double heuristic(int u, int v) throws ChartFailure {
        Double h = this.values.get(u);
        if (h == null) {
            return 0.0;
        }
        return h;
    }

    private void populateValues() {
        this.values = new HashMap<Integer, Double>();
        List<Vertex> nodes = this.graph.takeVertices();
        int maxNode = nodes.size() - 1;
        Shifter<Vertex> sorter = new Shifter<Vertex>(Vertex.getComparator());
        List<Vertex> orderedNames = sorter.arrange(nodes);
        int id = orderedNames.get(0).getId();
        this.values.put(id, 0.0);
        id = orderedNames.get(1).getId();
        this.values.put(id, 0.0);
        double prevVal = 0.0;
        for (int i = 2; i <= maxNode; ++i) {
            double newVal = prevVal + Math.pow(2.0, (double)i - 2.0) + 2.0;
            int nodeID = orderedNames.get(i).getId();
            this.values.put(nodeID, newVal);
            prevVal = newVal;
        }
    }
}

