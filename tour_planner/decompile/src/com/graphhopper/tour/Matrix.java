/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.tour;

import com.graphhopper.tour.Places;
import com.graphhopper.tour.util.Edge;
import com.graphhopper.util.CmdArgs;
import com.graphhopper.util.Helper;
import com.graphhopper.util.shapes.GHPlace;
import com.graphhopper.util.shapes.GHPoint;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Matrix<P extends GHPoint> {
    private static final Logger logger = LoggerFactory.getLogger(Matrix.class);
    private final List<P> points;
    private final double[][] weights;

    public Matrix(List<P> points) {
        this(points, new double[points.size()][points.size()]);
    }

    public Matrix(List<P> points, double[][] weights) {
        if (weights.length != points.size() || weights[0].length != points.size()) {
            throw new IllegalArgumentException("Points and weights must have same size.");
        }
        this.points = points;
        this.weights = weights;
    }

    public int size() {
        return this.points.size();
    }

    public List<P> getPoints() {
        return Collections.unmodifiableList(this.points);
    }

    public double[][] getWeights() {
        return this.weights;
    }

    public double getWeight(int fromIndex, int toIndex) {
        return this.weights[fromIndex][toIndex];
    }

    public Matrix setWeight(int fromIndex, int toIndex, double weight) {
        this.weights[fromIndex][toIndex] = weight;
        return this;
    }

    public List<Edge<P>> edges() {
        int size = this.points.size();
        ArrayList<Edge<P>> edges = new ArrayList<Edge<P>>(size * (size - 1));
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (j == i) continue;
                edges.add(new Edge<P>(this.points.get(i), this.points.get(j), this.weights[i][j]));
            }
        }
        assert (edges.size() == size * (size - 1));
        return edges;
    }

    public List<Edge<P>> symmetricEdges() {
        int size = this.points.size();
        ArrayList<Edge<P>> edges = new ArrayList<Edge<P>>(size * (size - 1) / 2);
        for (int i = 0; i < size; ++i) {
            for (int j = i + 1; j < size; ++j) {
                double w1 = this.weights[i][j];
                double w2 = this.weights[j][i];
                double wm = (w1 + w2) / 2.0;
                edges.add(new Edge<P>(this.points.get(i), this.points.get(j), wm));
            }
        }
        assert (edges.size() == size * (size - 1) / 2);
        return edges;
    }

    public static Matrix<GHPlace> load(CmdArgs cmdArgs) throws IOException {
        String csvFile = cmdArgs.get("matrix.csv", "");
        if (Helper.isEmpty(csvFile)) {
            throw new IllegalArgumentException("You must specify a matrix file (matrix.csv=FILE).");
        }
        return Matrix.readCsv(new File(csvFile));
    }

    public static Matrix<GHPlace> readCsv(File csvFile) throws IOException {
        if (!csvFile.exists()) {
            throw new IllegalStateException("Matrix file does not exist: " + csvFile.getAbsolutePath());
        }
        logger.info("Loading matrix file " + csvFile.getAbsolutePath());
        FileReader in = new FileReader(csvFile);
        Throwable throwable = null;
        try {
            Matrix<GHPlace> matrix = Matrix.readCsv(new BufferedReader(in));
            return matrix;
        }
        catch (Throwable throwable2) {
            throwable = throwable2;
            throw throwable2;
        }
        finally {
            if (in != null) {
                if (throwable != null) {
                    try {
                        in.close();
                    }
                    catch (Throwable x2) {
                        throwable.addSuppressed(x2);
                    }
                } else {
                    in.close();
                }
            }
        }
    }

    public static Matrix<GHPlace> readCsv(BufferedReader in) throws IOException {
        int i;
        List<GHPlace> places = Places.readCsv(in);
        Matrix<GHPlace> matrix = new Matrix<GHPlace>(places);
        List<String> names = Places.names(places);
        String expected = "," + StringUtils.join(names, ',');
        String line = in.readLine();
        if (line == null || !line.equals(expected)) {
            throw new IllegalArgumentException("Expected header row, got " + line);
        }
        int size = places.size();
        for (i = 0; i < size && (line = in.readLine()) != null && !(line = StringUtils.strip(line)).equals(""); ++i) {
            String[] cols = StringUtils.split(line, ',');
            if (cols.length != size + 1) {
                throw new IllegalArgumentException("Expected " + (size + 1) + " columns, got " + cols.length + ": " + line);
            }
            expected = names.get(i);
            if (!cols[0].equals(expected)) {
                throw new IllegalArgumentException("Expected " + expected + ", got " + cols[0]);
            }
            for (int j = 0; j < size; ++j) {
                double weight = Double.parseDouble(cols[j + 1]);
                matrix.setWeight(i, j, weight);
            }
        }
        if (i != size) {
            throw new IllegalArgumentException("Expected " + size + " rows, got " + i);
        }
        return matrix;
    }

    public static void writeCsv(Matrix<? extends GHPlace> matrix, File csvFile) throws IOException {
        PrintStream out = new PrintStream(csvFile);
        Throwable throwable = null;
        try {
            Matrix.writeCsv(matrix, out);
        }
        catch (Throwable x2) {
            throwable = x2;
            throw x2;
        }
        finally {
            if (out != null) {
                if (throwable != null) {
                    try {
                        out.close();
                    }
                    catch (Throwable x2) {
                        throwable.addSuppressed(x2);
                    }
                } else {
                    out.close();
                }
            }
        }
    }

    public static void writeCsv(Matrix<? extends GHPlace> matrix, PrintStream out) throws IOException {
        List<? extends GHPlace> places = matrix.getPoints();
        double[][] weights = matrix.weights;
        Places.writeCsv(matrix.getPoints(), out);
        out.println();
        List<String> names = Places.names(places);
        out.println("," + StringUtils.join(names, ','));
        for (int i = 0; i < places.size(); ++i) {
            out.println(names.get(i) + "," + StringUtils.join(weights[i], ','));
        }
    }
}

