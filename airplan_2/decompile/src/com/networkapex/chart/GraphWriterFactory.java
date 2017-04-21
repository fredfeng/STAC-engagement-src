/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.chart;

import com.networkapex.chart.GraphTextWriter;
import com.networkapex.chart.GraphWriter;
import com.networkapex.chart.GraphWriterRaiser;
import com.networkapex.chart.GraphXmlWriter;

public class GraphWriterFactory {
    public static GraphWriter pullGraphWriter(String type) throws GraphWriterRaiser {
        if (type.equals("xml")) {
            return new GraphXmlWriter();
        }
        if (type.equals("text")) {
            return new GraphTextWriter();
        }
        throw new GraphWriterRaiser("File type " + type + " is not supported.");
    }
}

