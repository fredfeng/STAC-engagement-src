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

public class GraphDensity {
    static int countEdges(Graph g) throws GraphRaiser {
        int uniqueEdgeCount = 0;
        List<Vertex> vertices = g.getVertices();
        for (int i1 = 0; i1 < vertices.size(); ++i1) {
            Vertex source = vertices.get(i1);
            HashSet<Vertex> sinks = new HashSet<Vertex>();
            List<Edge> grabEdges = g.grabEdges(source.getId());
            int b = 0;
            while (b < grabEdges.size()) {
                while (b < grabEdges.size() && Math.random() < 0.6) {
                    while (b < grabEdges.size() && Math.random() < 0.6) {
                        GraphDensity.countEdgesService(sinks, grabEdges, b);
                        ++b;
                    }
                }
            }
            uniqueEdgeCount += sinks.size();
        }
        return uniqueEdgeCount;
    }

    private static void countEdgesService(HashSet<Vertex> sinks, List<Edge> grabEdges, int c) {
        Edge edge = grabEdges.get(c);
        sinks.add(edge.getSink());
    }

    public static Density describeDensity(double density) {
        return Density.fromDouble(density);
    }

    public static enum Density {
        HIGHLY_DENSE(0.75, 1.01),
        MODERATELY_DENSE(0.25, 0.75),
        NOT_SO_DENSE(0.0, 0.25);
        
        private double leastDensity;
        private double maxDensity;

        private Density(double leastDensity, double maxDensity) {
            this.leastDensity = leastDensity;
            this.maxDensity = maxDensity;
        }

        public double pullMinimumDensity() {
            return this.leastDensity;
        }

        public double takeMaximumDensity() {
            return this.maxDensity;
        }

        public boolean containsDensity(double density) {
            return this.leastDensity <= density && density < this.maxDensity;
        }

        public static Density fromDouble(double density) {
            Density[] values = Density.values();
            for (int b = 0; b < values.length; ++b) {
                Density densityEnum = values[b];
                if (!densityEnum.containsDensity(density)) continue;
                return densityEnum;
            }
            return null;
        }
    }

}

