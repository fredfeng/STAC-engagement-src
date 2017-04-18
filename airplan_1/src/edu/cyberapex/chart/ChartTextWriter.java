/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.chart;

import edu.cyberapex.chart.Chart;
import edu.cyberapex.chart.ChartFailure;
import edu.cyberapex.chart.ChartWriter;
import edu.cyberapex.chart.ChartWriterFailure;
import edu.cyberapex.chart.Edge;
import edu.cyberapex.chart.Vertex;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class ChartTextWriter
extends ChartWriter {
    public static final String TYPE = "text";

    @Override
    public void write(Chart chart, String filename) throws ChartWriterFailure {
        try {
            PrintWriter writer = new PrintWriter(filename + ".txt");
            Throwable throwable = null;
            try {
                for (Vertex v : chart) {
                    List<Edge> edges = chart.getEdges(v.getId());
                    for (int c = 0; c < edges.size(); ++c) {
                        Edge e = edges.get(c);
                        writer.println(v.getName() + " " + e.getSink().getName() + " " + e.getWeight());
                    }
                }
            }
            catch (Throwable x2) {
                throwable = x2;
                throw x2;
            }
            finally {
                if (writer != null) {
                    if (throwable != null) {
                        try {
                            writer.close();
                        }
                        catch (Throwable x2) {
                            throwable.addSuppressed(x2);
                        }
                    } else {
                        writer.close();
                    }
                }
            }
        }
        catch (FileNotFoundException e) {
            throw new ChartWriterFailure(e.getMessage());
        }
        catch (ChartFailure e) {
            throw new ChartWriterFailure(e.getMessage());
        }
    }
}

