/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.chart;

import com.networkapex.chart.BasicData;
import com.networkapex.chart.Data;
import com.networkapex.chart.Edge;
import com.networkapex.chart.Graph;
import com.networkapex.chart.GraphFactory;
import com.networkapex.chart.GraphFileLoader;
import com.networkapex.chart.GraphLoader;
import com.networkapex.chart.GraphRaiser;
import com.networkapex.chart.Vertex;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class TextFileLoader
implements GraphFileLoader {
    private static final String[] EXTENSIONS = new String[]{"txt"};

    public static void register() {
        GraphLoader.registerLoader(new TextFileLoader());
    }

    @Override
    public Graph loadGraph(String filename) throws FileNotFoundException, GraphRaiser {
        Graph graph;
        graph = GraphFactory.newInstance();
        Scanner scanner = new Scanner(new File(filename));
        Throwable throwable = null;
        try {
            while (scanner.hasNext()) {
                this.loadGraphCoordinator(graph, scanner);
            }
        }
        catch (Throwable x2) {
            throwable = x2;
            throw x2;
        }
        finally {
            if (scanner != null) {
                if (throwable != null) {
                    try {
                        scanner.close();
                    }
                    catch (Throwable x2) {
                        throwable.addSuppressed(x2);
                    }
                } else {
                    scanner.close();
                }
            }
        }
        return graph;
    }

    private void loadGraphCoordinator(Graph graph, Scanner scanner) throws GraphRaiser {
        new TextFileLoaderSupervisor(graph, scanner).invoke();
    }

    @Override
    public List<String> getExtensions() {
        return Arrays.asList(EXTENSIONS);
    }

    private class TextFileLoaderSupervisor {
        private Graph graph;
        private Scanner scanner;

        public TextFileLoaderSupervisor(Graph graph, Scanner scanner) {
            this.graph = graph;
            this.scanner = scanner;
        }

        public void invoke() throws GraphRaiser {
            try {
                String v1 = this.scanner.next();
                String v2 = this.scanner.next();
                double weight = this.scanner.nextDouble();
                if (!this.graph.containsVertexWithName(v1)) {
                    this.graph.addVertex(v1);
                }
                if (!this.graph.containsVertexWithName(v2)) {
                    this.invokeTarget(v2);
                }
                BasicData data = new BasicData(weight);
                this.graph.addEdge(this.graph.takeVertexIdByName(v1), this.graph.takeVertexIdByName(v2), data);
            }
            catch (NoSuchElementException e) {
                throw new GraphRaiser("Invalid graph file format", e);
            }
            catch (IllegalStateException e) {
                throw new GraphRaiser("Invalid graph file format", e);
            }
        }

        private void invokeTarget(String v2) throws GraphRaiser {
            this.graph.addVertex(v2);
        }
    }

}

