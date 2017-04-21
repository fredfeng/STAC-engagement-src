/*
 * Decompiled with CFR 0_121.
 */
package com.example.linalg.internal.graph;

public class Laplacian {
    double[][] A;
    double[][] D;

    public Laplacian(double[][] A) {
        this.A = A;
        this.D = new double[A.length][A.length];
    }

    public double[][] laplacian() {
        for (int i = 0; i < this.A.length; ++i) {
            int j;
            int count = 0;
            for (j = 0; j < this.A[i].length; ++j) {
                if (this.A[i][j] == 0.0) continue;
                ++count;
            }
            this.D[i][i] = count;
            for (j = 0; j < this.A[i].length; ++j) {
                this.D[i][j] = this.D[i][j] - this.A[i][j];
            }
        }
        return this.D;
    }
}

