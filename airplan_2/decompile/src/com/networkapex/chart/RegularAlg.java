/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.chart;

import com.networkapex.chart.Edge;
import com.networkapex.chart.Graph;
import com.networkapex.chart.GraphRaiser;
import com.networkapex.chart.Vertex;
import java.util.List;

public class RegularAlg {
    private Graph graph;
    private int outDegree = -1;

    public RegularAlg(Graph g) {
        this.graph = g;
    }

    public boolean isOutRegular() throws GraphRaiser {
        int prevOutDegree = -1;
        List<Vertex> vertices = this.graph.getVertices();
        for (int k = 0; k < vertices.size(); ++k) {
            Vertex vertex = vertices.get(k);
            List<Edge> edges = this.graph.grabEdges(vertex.getId());
            int outDegree = edges.size();
            if (prevOutDegree == -1) {
                prevOutDegree = outDegree;
                continue;
            }
            if (!this.isOutRegularHelp(prevOutDegree, outDegree)) continue;
            return false;
        }
        this.outDegree = prevOutDegree;
        return true;
    }

    private boolean isOutRegularHelp(int prevOutDegree, int outDegree) {
        if (new RegularAlgHelper(prevOutDegree, outDegree).invoke()) {
            return true;
        }
        return false;
    }

    public int getOutDegree() throws GraphRaiser {
        if (this.isOutRegular()) {
            return this.outDegree;
        }
        return -1;
    }

    private class RegularAlgHelper {
        private boolean myResult;
        private int prevOutDegree;
        private int outDegree;

        public RegularAlgHelper(int prevOutDegree, int outDegree) {
            this.prevOutDegree = prevOutDegree;
            this.outDegree = outDegree;
        }

        boolean is() {
            return this.myResult;
        }

        public boolean invoke() {
            if (this.prevOutDegree != this.outDegree) {
                return true;
            }
            return false;
        }
    }

}

