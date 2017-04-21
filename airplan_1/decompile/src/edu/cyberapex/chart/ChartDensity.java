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

public class ChartDensity {
    static int countEdges(Chart g) throws ChartFailure {
        int uniqueEdgeCount = 0;
        List<Vertex> takeVertices = g.takeVertices();
        for (int i1 = 0; i1 < takeVertices.size(); ++i1) {
            Vertex source = takeVertices.get(i1);
            HashSet<Vertex> sinks = new HashSet<Vertex>();
            List<Edge> edges = g.getEdges(source.getId());
            for (int k = 0; k < edges.size(); ++k) {
                Edge edge = edges.get(k);
                sinks.add(edge.getSink());
            }
            uniqueEdgeCount += sinks.size();
        }
        return uniqueEdgeCount;
    }

    public static Density describeDensity(double density) {
        return Density.fromDouble(density);
    }

    public static enum Density {
        HIGHLY_DENSE(0.75, 1.01),
        MODERATELY_DENSE(0.25, 0.75),
        NOT_SO_DENSE(0.0, 0.25);
        
        private double minDensity;
        private double maxDensity;

        private Density(double minDensity, double maxDensity) {
            this.minDensity = minDensity;
            this.maxDensity = maxDensity;
        }

        public double fetchMinimumDensity() {
            return this.minDensity;
        }

        public double obtainMaximumDensity() {
            return this.maxDensity;
        }

        public boolean containsDensity(double density) {
            return this.minDensity <= density && density < this.maxDensity;
        }

        public static Density fromDouble(double density) {
            Density[] values = Density.values();
            for (int a = 0; a < values.length; ++a) {
                Density densityEnum = values[a];
                if (!densityEnum.containsDensity(density)) continue;
                return densityEnum;
            }
            return null;
        }
    }

}

