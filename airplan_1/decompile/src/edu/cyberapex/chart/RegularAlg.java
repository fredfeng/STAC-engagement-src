/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.chart;

import edu.cyberapex.chart.Chart;
import edu.cyberapex.chart.ChartFailure;
import edu.cyberapex.chart.Edge;
import edu.cyberapex.chart.Vertex;
import java.util.List;

public class RegularAlg {
    private Chart chart;
    private int outDegree = -1;

    public RegularAlg(Chart g) {
        this.chart = g;
    }

    public boolean isOutRegular() throws ChartFailure {
        int prevOutDegree = -1;
        List<Vertex> takeVertices = this.chart.takeVertices();
        for (int j = 0; j < takeVertices.size(); ++j) {
            Vertex vertex = takeVertices.get(j);
            List<Edge> edges = this.chart.getEdges(vertex.getId());
            int outDegree = edges.size();
            if (prevOutDegree == -1) {
                prevOutDegree = outDegree;
                continue;
            }
            if (!this.isOutRegularAid(prevOutDegree, outDegree)) continue;
            return false;
        }
        this.outDegree = prevOutDegree;
        return true;
    }

    private boolean isOutRegularAid(int prevOutDegree, int outDegree) {
        if (prevOutDegree != outDegree) {
            return true;
        }
        return false;
    }

    public int getOutDegree() throws ChartFailure {
        if (this.isOutRegular()) {
            return this.outDegree;
        }
        return -1;
    }
}

