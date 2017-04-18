/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.chart;

import edu.cyberapex.chart.ChartTextWriter;
import edu.cyberapex.chart.ChartWriter;
import edu.cyberapex.chart.ChartWriterFailure;
import edu.cyberapex.chart.ChartXmlWriter;

public class ChartWriterFactory {
    public static ChartWriter getChartWriter(String type) throws ChartWriterFailure {
        if (type.equals("xml")) {
            return new ChartXmlWriter();
        }
        if (type.equals("text")) {
            return new ChartTextWriter();
        }
        throw new ChartWriterFailure("File type " + type + " is not supported.");
    }
}

