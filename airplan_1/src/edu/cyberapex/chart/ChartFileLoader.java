/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.chart;

import edu.cyberapex.chart.Chart;
import edu.cyberapex.chart.ChartFailure;
import java.io.FileNotFoundException;
import java.util.List;

public interface ChartFileLoader {
    public Chart loadChart(String var1) throws FileNotFoundException, ChartFailure;

    public List<String> fetchExtensions();
}

