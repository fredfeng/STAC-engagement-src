/*
 * Decompiled with CFR 0_121.
 */
package com.example.linalg.internal.multiply;

import com.example.linalg.internal.multiply.MultiplicationTaskGenerator;
import com.example.linalg.internal.multiply.RPCClient;
import com.example.linalg.internal.multiply.RPCServer;
import com.example.linalg.util.Pair;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ParallelMatrixMultiply {
    double[][] A;
    double[][] B;
    ConcurrentHashMap<Pair<Integer, Integer>, Double> C;
    int numThreads;
    public RPCServer RPC;
    MultiplicationTaskGenerator taskGenerator;
    int port;
    int partitionSize;

    public ParallelMatrixMultiply(double[][] A, double[][] B, int numThreads, int partitionSize, int port) {
        assert (A[0].length == A.length && B[0].length == B.length && A.length == B.length);
        this.A = A;
        this.B = B;
        this.port = port;
        this.partitionSize = partitionSize;
        this.C = new ConcurrentHashMap();
        this.numThreads = numThreads;
        this.taskGenerator = new MultiplicationTaskGenerator(A, B);
    }

    public double[][] multiply() throws InterruptedException {
        this.RPC = new RPCServer(this.taskGenerator.partitionMatrix(this.partitionSize), this.C, this.port);
        Thread t1 = new Thread(this.RPC);
        t1.start();
        try {
            Thread.sleep(100);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Thread[] threads = new Thread[this.numThreads];
        for (int t = 0; t < this.numThreads; ++t) {
            Thread t2 = new Thread(new RPCClient("localhost", this.port));
            t2.setName("RPCClient-" + t);
            threads[t] = t2;
            t2.start();
        }
        for (Thread t : threads) {
            t.join();
        }
        if (this.C.size() != this.A.length * this.A[0].length) {
            throw new InterruptedException("Result matrix corrupted");
        }
        double[][] C_r = new double[this.A.length][this.A[0].length];
        for (Pair p : this.C.keySet()) {
            C_r[((Integer)p.getElement0()).intValue()][((Integer)p.getElement1()).intValue()] = this.C.get(p);
        }
        return C_r;
    }
}

