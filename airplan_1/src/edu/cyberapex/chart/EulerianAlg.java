/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.chart;

import edu.cyberapex.chart.Chart;
import edu.cyberapex.chart.ChartFailure;
import edu.cyberapex.chart.ConnectedAlg;

public class EulerianAlg {
    public static boolean isEulerian(Chart graph) throws ChartFailure {
        ConnectedAlg ca = new ConnectedAlg();
        return ConnectedAlg.isConnected(graph) && !graph.hasOddDegree();
    }
}

