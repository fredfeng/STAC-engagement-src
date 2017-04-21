/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.chart;

import com.networkapex.chart.Edge;
import com.networkapex.chart.Graph;
import com.networkapex.chart.GraphRaiser;
import com.networkapex.chart.GraphWriter;
import com.networkapex.chart.GraphWriterRaiser;
import com.networkapex.chart.Vertex;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class GraphTextWriter
extends GraphWriter {
    public static final String TYPE = "text";

    @Override
    public void write(Graph graph, String filename) throws GraphWriterRaiser {
        try {
            PrintWriter writer = new PrintWriter(filename + ".txt");
            Throwable throwable = null;
            try {
                for (Vertex v : graph) {
                    List<Edge> grabEdges = graph.grabEdges(v.getId());
                    for (int q = 0; q < grabEdges.size(); ++q) {
                        Edge e = grabEdges.get(q);
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
            throw new GraphWriterRaiser(e.getMessage());
        }
        catch (GraphRaiser e) {
            throw new GraphWriterRaiser(e.getMessage());
        }
    }
}

