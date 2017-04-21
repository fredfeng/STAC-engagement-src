/*
 * Decompiled with CFR 0_121.
 */
package com.example.linalg.internal.multiply;

import com.example.linalg.util.Pair;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class MultiplicationTaskGenerator {
    public double[][] MATRIX_A;
    public double[][] MATRIX_B;

    public static double[][] generateRandomMatrix(int n) {
        double[][] A = new double[n][n];
        Random R = new Random();
        R.setSeed(System.currentTimeMillis());
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                A[i][j] = R.nextDouble();
            }
        }
        return A;
    }

    public static double[][] eye(int n) {
        double[][] A = new double[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (i != j) continue;
                A[i][j] = 1.0;
            }
        }
        return A;
    }

    public static double[][] constMat(int n, double k) {
        double[][] A = new double[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                A[i][j] = k;
            }
        }
        return A;
    }

    public static void printMatrix(double[][] m) {
        try {
            int rows = m.length;
            int columns = m[0].length;
            String str = "|\t";
            for (int i = 0; i < rows; ++i) {
                for (int j = 0; j < columns; ++j) {
                    str = str + m[i][j] + "\t";
                }
                System.out.println(str + "|");
                str = "|\t";
            }
        }
        catch (Exception e) {
            System.out.println("Matrix is empty!!");
        }
    }

    public void transpose(double[][] matrix) {
        for (int i = 0; i < matrix.length; ++i) {
            for (int j = i + 1; j < matrix[i].length; ++j) {
                double temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }
    }

    public MultiplicationTaskGenerator(double[][] matrixA, double[][] matrixB) {
        this.MATRIX_A = matrixA;
        this.MATRIX_B = matrixB;
        this.transpose(matrixB);
    }

    List<SubMatrixTask> partitionMatrix(int partitionSize) {
        LinkedList<SubMatrixTask> R = new LinkedList<SubMatrixTask>();
        LinkedList<Pair<Integer, Integer>> tasks = new LinkedList<Pair<Integer, Integer>>();
        for (int i = 0; i < this.MATRIX_A.length; ++i) {
            for (int j = 0; j < this.MATRIX_B[i].length; ++j) {
                tasks.add(new Pair<Integer, Integer>(i, j));
            }
        }
        int c = 0;
        SubMatrixTask S = new SubMatrixTask();
        for (Pair task : tasks) {
            if (++c > partitionSize) {
                R.add(S);
                c = 0;
                S = new SubMatrixTask();
            }
            S.addRowA((Integer)task.getElement0(), this.MATRIX_A[(Integer)task.getElement0()]);
            S.addColumnB((Integer)task.getElement1(), this.MATRIX_B[(Integer)task.getElement1()]);
            S.addTask(task);
        }
        if (S.tasks.size() > 0) {
            R.add(S);
        }
        return R;
    }

    public class SubMatrixTask {
        ConcurrentHashMap<Integer, double[]> submatrixA;
        ConcurrentHashMap<Integer, double[]> submatrixB;
        List<Pair<Integer, Integer>> tasks;

        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (Pair<Integer, Integer> p : this.tasks) {
                sb.append(p);
                sb.append("\n");
            }
            sb.append("--------");
            return sb.toString();
        }

        public SubMatrixTask() {
            this.submatrixA = new ConcurrentHashMap();
            this.submatrixB = new ConcurrentHashMap();
            this.tasks = new LinkedList<Pair<Integer, Integer>>();
        }

        public void addTask(Pair<Integer, Integer> p, double[] a, double[] b) {
            this.addRowA(p.getElement0(), a);
            this.addColumnB(p.getElement1(), b);
            this.addTask(p);
        }

        public void addTask(int i, double[] a, int j, double[] b) {
            this.addRowA(i, a);
            this.addColumnB(j, b);
            this.addTask(i, j);
        }

        public void addTask(Pair<Integer, Integer> p) {
            this.tasks.add(p);
        }

        public void addTask(int i, int j) {
            this.tasks.add(new Pair<Integer, Integer>(i, j));
        }

        public void addRowA(int idx, double[] row) {
            this.submatrixA.put(idx, row);
        }

        public void addColumnB(int idx, double[] col) {
            this.submatrixB.put(idx, col);
        }
    }

}

