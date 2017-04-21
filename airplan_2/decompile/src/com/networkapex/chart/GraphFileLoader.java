/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.chart;

import com.networkapex.chart.Graph;
import com.networkapex.chart.GraphRaiser;
import java.io.FileNotFoundException;
import java.util.List;

public interface GraphFileLoader {
    public Graph loadGraph(String var1) throws FileNotFoundException, GraphRaiser;

    public List<String> getExtensions();
}

