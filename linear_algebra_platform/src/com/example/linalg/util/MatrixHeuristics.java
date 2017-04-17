/*
 * Decompiled with CFR 0_121.
 */
package com.example.linalg.util;

public class MatrixHeuristics {
    double[][] matrix;
    static double EPSILON = 0.01;

    public MatrixHeuristics(double[][] Matrix2) {
        this.matrix = Matrix2;
    }

    public static double mean(double[] m) {
        double s = 0.0;
        for (double d : m) {
            double temp = s + d;
            if (Math.abs(temp - s) > Math.abs(d) + EPSILON) {
                return 0.0;
            }
            s += d;
        }
        return s / (double)m.length;
    }

    public static double moment(double[] m, double u, double pow) {
        double s = 0.0;
        for (double d : m) {
            double item = Math.pow(d - u, pow);
            double temp = s + item;
            if (Math.abs(temp - s) > Math.abs(item) + EPSILON) {
                return 0.0;
            }
            s += Math.pow(d - u, pow);
        }
        return s / (double)m.length;
    }

    public static double sstd(double[] m, double u) {
        double s = 0.0;
        for (double d : m) {
            double temp = s + d;
            if (Math.abs(temp - s) > Math.abs(d) + EPSILON) {
                return 0.0;
            }
            s += Math.pow(d - u, 2.0);
        }
        return s / (double)(m.length - 1);
    }

    public double eval() {
        return MatrixHeuristics.evalMatrix(this.matrix);
    }

    public static double evalMatrix(double[][] Matrix2) {
        double[] m = new double[Matrix2.length];
        for (int i = 0; i < Matrix2.length; ++i) {
            double u = MatrixHeuristics.mean(Matrix2[i]);
            double o = MatrixHeuristics.sstd(Matrix2[i], u);
            double m3 = MatrixHeuristics.moment(Matrix2[i], u, 3.0);
            double k = m3 / Math.pow(o, 1.5);
            m[i] = Math.abs(o) < EPSILON || Math.abs(m3) < EPSILON ? 0.0 : k;
        }
        double s = 0.0;
        for (double d : m) {
            double temp = s + d;
            if (Math.abs(temp - s) > Math.abs(d) + EPSILON) {
                return 0.0;
            }
            s += d;
        }
        return s / (double)Matrix2.length;
    }
}

