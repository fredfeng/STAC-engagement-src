/*
 * Decompiled with CFR 0_121.
 */
package com.example.linalg.internal.graph.util;

public class UF {
    private int[] parent;
    private byte[] rank;
    private int count;

    public UF(int N) {
        if (N < 0) {
            throw new IllegalArgumentException();
        }
        this.count = N;
        this.parent = new int[N];
        this.rank = new byte[N];
        int i = 0;
        while (i < N) {
            this.parent[i] = i++;
            this.rank[i] = 0;
        }
    }

    public int find(int p) {
        this.validate(p);
        while (p != this.parent[p]) {
            this.parent[p] = this.parent[this.parent[p]];
            p = this.parent[p];
        }
        return p;
    }

    public int count() {
        return this.count;
    }

    public boolean connected(int p, int q) {
        return this.find(p) == this.find(q);
    }

    public void union(int p, int q) {
        int rootQ;
        int rootP = this.find(p);
        if (rootP == (rootQ = this.find(q))) {
            return;
        }
        if (this.rank[rootP] < this.rank[rootQ]) {
            this.parent[rootP] = rootQ;
        } else if (this.rank[rootP] > this.rank[rootQ]) {
            this.parent[rootQ] = rootP;
        } else {
            this.parent[rootQ] = rootP;
            byte[] arrby = this.rank;
            int n = rootP;
            arrby[n] = (byte)(arrby[n] + 1);
        }
        --this.count;
    }

    private void validate(int p) {
        int N = this.parent.length;
        if (p < 0 || p >= N) {
            throw new IndexOutOfBoundsException("index " + p + " is not between 0 and " + (N - 1));
        }
    }
}

