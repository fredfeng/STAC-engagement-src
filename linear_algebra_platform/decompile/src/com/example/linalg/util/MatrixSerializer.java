/*
 * Decompiled with CFR 0_121.
 */
package com.example.linalg.util;

import java.io.PrintStream;
import java.util.IllegalFormatException;
import java.util.MissingFormatArgumentException;

public class MatrixSerializer {
    static int MIN_WIDTH = 16;

    public double[][] readMatrixFromCSV(String filecontents, int numRows, int numCols, int maxsize) throws IllegalFormatException {
        return this.readMatrixFromCSV(filecontents, numRows, numCols, maxsize, true, true);
    }

    public double[][] readMatrixFromCSV(String filecontents, int numRows, int numCols, int maxsize, boolean enforceWidth, boolean enforceSize) throws IllegalFormatException {
        if (enforceSize && (numRows > maxsize || numCols > maxsize)) {
            throw new IllegalArgumentException("Matrix too large");
        }
        for (int i = 0; i < filecontents.length(); ++i) {
            if (Character.isDigit(filecontents.charAt(i)) || Character.isSpaceChar(filecontents.charAt(i)) || filecontents.charAt(i) == '\n' || filecontents.charAt(i) == '\r' || filecontents.charAt(i) == '.' || filecontents.charAt(i) == ',') continue;
            throw new IllegalArgumentException("Non-allowed character detected in input matrix");
        }
        double[][] matrix = new double[numRows][numCols];
        int i = 0;
        String[] rows = filecontents.split("\n");
        if (rows.length != numRows) {
            throw new IllegalArgumentException("Number of rows detected " + rows.length + " does not match expected " + numRows);
        }
        for (String row : rows) {
            String[] vals = row.split(",");
            if (vals.length != numCols) {
                throw new IllegalArgumentException("Number of column detected " + vals.length + " does not match expected " + numCols);
            }
            for (int j = 0; j < vals.length; ++j) {
                if (enforceWidth && vals[j].length() <= MIN_WIDTH) {
                    System.out.println("col " + j + " = " + vals[j] + "(" + vals[j].length() + ")");
                    throw new MissingFormatArgumentException("Matrix entries are not fixed width");
                }
                matrix[i][j] = Double.parseDouble(vals[j]);
            }
            ++i;
        }
        return matrix;
    }

    public static String matrixToCSV(double[][] A) {
        try {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < A.length; ++i) {
                for (int j = 0; j < A[i].length; ++j) {
                    sb.append(String.format("%e", A[i][j]));
                    if (j == A[i].length - 1) {
                        sb.append('\n');
                        continue;
                    }
                    sb.append(',');
                }
            }
            return sb.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

