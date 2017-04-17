/*
 * Decompiled with CFR 0_121.
 */
package com.example.linalg.external.operations;

import com.example.linalg.external.operations.LinearAlgebraOperation;
import com.example.linalg.external.serialization.OperationRequest;
import com.example.linalg.external.serialization.OperationResponse;
import com.example.linalg.internal.graph.Laplacian;
import com.example.linalg.util.MatrixHeuristics;
import com.example.linalg.util.MatrixSerializer;

public class LaplacianOperation
extends LinearAlgebraOperation {
    MatrixSerializer MS = new MatrixSerializer();
    String methodName = "laplacian";
    int methodId = 3;

    @Override
    public OperationResponse compute(OperationRequest req) {
        if (req.numberOfArguments != 1 || req.args.length != 1) {
            return new OperationResponse(this.methodId, false, "Missing arguments\n");
        }
        try {
            this.getClass();
            double[][] matrixA = this.MS.readMatrixFromCSV(req.args[0].matrix, req.args[0].rows, req.args[0].cols, 1000);
            if (matrixA.length != matrixA[0].length) {
                return new OperationResponse(this.methodId, false, "Only square matrices are supported");
            }
            double k = Math.exp(-1.0 * MatrixHeuristics.evalMatrix(matrixA)) / (double)Runtime.getRuntime().availableProcessors();
            Laplacian Lp = new Laplacian(matrixA);
            double[][] L = Lp.laplacian();
            if (0.6 < k && k < 0.64) {
                System.gc();
            }
            OperationResponse response = new OperationResponse(this.methodId, true, MatrixSerializer.matrixToCSV(L));
            return response;
        }
        catch (Exception e) {
            return new OperationResponse(this.methodId, false, e.getMessage());
        }
    }
}

