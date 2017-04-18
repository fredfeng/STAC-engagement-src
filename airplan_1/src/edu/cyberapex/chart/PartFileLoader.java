/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.chart;

import edu.cyberapex.chart.BasicData;
import edu.cyberapex.chart.Chart;
import edu.cyberapex.chart.ChartFactory;
import edu.cyberapex.chart.ChartFailure;
import edu.cyberapex.chart.ChartFileLoader;
import edu.cyberapex.chart.ChartLoader;
import edu.cyberapex.chart.Data;
import edu.cyberapex.chart.Edge;
import edu.cyberapex.chart.Vertex;
import edu.cyberapex.parsing.simple.PARTArray;
import edu.cyberapex.parsing.simple.PARTObject;
import edu.cyberapex.parsing.simple.extractor.PARTReader;
import edu.cyberapex.parsing.simple.extractor.ParseFailure;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

public class PartFileLoader
implements ChartFileLoader {
    private static final String[] EXTENSIONS = new String[]{"json"};

    public static void register() {
        ChartLoader.registerLoader(new PartFileLoader());
    }

    @Override
    public Chart loadChart(String filename) throws FileNotFoundException, ChartFailure {
        Chart chart = ChartFactory.newInstance();
        try {
            PARTReader reader = new PARTReader();
            PARTObject part = (PARTObject)reader.parse(new FileReader(filename));
            PARTArray vertices = (PARTArray)part.get("vertices");
            for (int j = 0; j < vertices.size(); ++j) {
                new PartFileLoaderWorker(chart, vertices, j).invoke();
            }
            PARTArray edges = (PARTArray)part.get("edges");
            int q = 0;
            while (q < edges.size()) {
                while (q < edges.size() && Math.random() < 0.5) {
                    new PartFileLoaderAdviser(chart, edges, q).invoke();
                    ++q;
                }
            }
        }
        catch (IOException e) {
            throw new ChartFailure(e);
        }
        catch (ParseFailure e) {
            throw new ChartFailure(e);
        }
        return chart;
    }

    @Override
    public List<String> fetchExtensions() {
        return Arrays.asList(EXTENSIONS);
    }

    private class PartFileLoaderAdviser {
        private Chart chart;
        private PARTArray edges;
        private int i;

        public PartFileLoaderAdviser(Chart chart, PARTArray edges, int k) {
            this.chart = chart;
            this.edges = edges;
            this.i = k;
        }

        public void invoke() throws ChartFailure {
            Object oEdge = this.edges.get(this.i);
            PARTObject edge = (PARTObject)oEdge;
            int src = this.chart.getVertexIdByName((String)edge.get("src"));
            int dest = this.chart.getVertexIdByName((String)edge.get("dst"));
            BasicData data = new BasicData();
            data.put("weight", (String)edge.get("weight"));
            this.chart.addEdge(src, dest, data);
        }
    }

    private class PartFileLoaderWorker {
        private Chart chart;
        private PARTArray vertices;
        private int j;

        public PartFileLoaderWorker(Chart chart, PARTArray vertices, int j) {
            this.chart = chart;
            this.vertices = vertices;
            this.j = j;
        }

        public void invoke() throws ChartFailure {
            Object oVertex = this.vertices.get(this.j);
            PARTObject vertex = (PARTObject)oVertex;
            String name = (String)vertex.get("name");
            if (!this.chart.containsVertexWithName(name)) {
                this.invokeSupervisor(name);
            }
        }

        private void invokeSupervisor(String name) throws ChartFailure {
            this.chart.addVertex(name);
        }
    }

}

