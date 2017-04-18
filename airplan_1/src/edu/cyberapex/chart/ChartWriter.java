/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.chart;

import edu.cyberapex.chart.Chart;
import edu.cyberapex.chart.ChartWriterFailure;

public abstract class ChartWriter {
    public abstract void write(Chart var1, String var2) throws ChartWriterFailure;
}

