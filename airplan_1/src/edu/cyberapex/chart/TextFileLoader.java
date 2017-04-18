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
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class TextFileLoader
implements ChartFileLoader {
    private static final String[] EXTENSIONS = new String[]{"txt"};

    public static void register() {
        ChartLoader.registerLoader(new TextFileLoader());
    }

    @Override
    public Chart loadChart(String filename) throws FileNotFoundException, ChartFailure {
        Chart chart;
        chart = ChartFactory.newInstance();
        Scanner scanner = new Scanner(new File(filename));
        Throwable throwable = null;
        try {
            while (scanner.hasNext()) {
                this.loadChartExecutor(chart, scanner);
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
        return chart;
    }

    private void loadChartExecutor(Chart chart, Scanner scanner) throws ChartFailure {
        try {
            String v1 = scanner.next();
            String v2 = scanner.next();
            double weight = scanner.nextDouble();
            if (!chart.containsVertexWithName(v1)) {
                this.loadChartExecutorAssist(chart, v1);
            }
            if (!chart.containsVertexWithName(v2)) {
                chart.addVertex(v2);
            }
            BasicData data = new BasicData(weight);
            chart.addEdge(chart.getVertexIdByName(v1), chart.getVertexIdByName(v2), data);
        }
        catch (NoSuchElementException e) {
            throw new ChartFailure("Invalid graph file format", e);
        }
        catch (IllegalStateException e) {
            throw new ChartFailure("Invalid graph file format", e);
        }
    }

    private void loadChartExecutorAssist(Chart chart, String v1) throws ChartFailure {
        chart.addVertex(v1);
    }

    @Override
    public List<String> fetchExtensions() {
        return Arrays.asList(EXTENSIONS);
    }
}

