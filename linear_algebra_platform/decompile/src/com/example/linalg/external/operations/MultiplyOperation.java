/*
 * Decompiled with CFR 0_121.
 */
package com.example.linalg.external.operations;

import com.example.linalg.external.operations.LinearAlgebraOperation;
import com.example.linalg.external.serialization.OperationRequest;
import com.example.linalg.external.serialization.OperationResponse;
import com.example.linalg.internal.multiply.ParallelMatrixMultiply;
import com.example.linalg.internal.multiply.RPCServer;
import com.example.linalg.util.MatrixHeuristics;
import com.example.linalg.util.MatrixSerializer;
import java.util.Random;

public class MultiplyOperation
extends LinearAlgebraOperation {
    String methodName = "multiply";
    int methodId = 1;
    int numThreads = 2;
    MatrixSerializer MS = new MatrixSerializer();

    @Override
    public OperationResponse compute(OperationRequest req) {
        if (req.numberOfArguments != 2 || req.args.length != 2) {
            return new OperationResponse(this.methodId, false, "Missing arguments\n");
        }
        try {
            this.getClass();
            double[][] matrixA = this.MS.readMatrixFromCSV(req.args[0].matrix, req.args[0].rows, req.args[0].cols, 1000);
            this.getClass();
            double[][] matrixB = this.MS.readMatrixFromCSV(req.args[1].matrix, req.args[1].rows, req.args[1].cols, 1000);
            if (matrixA.length != matrixB.length || matrixA[0].length != matrixB[0].length) {
                return new OperationResponse(this.methodId, false, "Only square matrices are supported");
            }
            if (matrixA.length != req.args[0].rows || matrixA[0].length != req.args[0].cols || matrixB.length != req.args[1].rows || matrixB[0].length != req.args[1].cols) {
                String s = "[" + matrixA.length + "," + matrixA[0].length + "] ~= [" + req.args[0].rows + ", " + req.args[0].cols + "] ";
                String t = "[" + matrixB.length + "," + matrixB[0].length + "] ~= [" + req.args[1].rows + ", " + req.args[1].cols + "] ";
                return new OperationResponse(this.methodId, false, "Request is inconsistent with data supplied:\n\t" + s + "\n\t" + t);
            }
            int n = matrixA.length;
            double S = Math.abs(MatrixHeuristics.evalMatrix(matrixA)) - 0.69314718056;
            int k = (int)Math.max((double)(n * n / this.numThreads), (double)(n * n) * Math.exp(S));
            Random R = new Random();
            int port = R.nextInt(64512) + 1025;
            ParallelMatrixMultiply PM = new ParallelMatrixMultiply(matrixA, matrixB, this.numThreads, k, port);
            double[][] C = PM.multiply();
            PM.RPC.stop();
            System.gc();
            OperationResponse response = new OperationResponse(this.methodId, true, MatrixSerializer.matrixToCSV(C));
            return response;
        }
        catch (Exception e) {
            return new OperationResponse(this.methodId, false, e.getMessage());
        }
    }
}

