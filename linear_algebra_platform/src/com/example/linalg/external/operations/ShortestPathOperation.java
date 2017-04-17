/*
 * Decompiled with CFR 0_121.
 */
package com.example.linalg.external.operations;

import com.example.linalg.external.operations.LinearAlgebraOperation;
import com.example.linalg.external.serialization.OperationRequest;
import com.example.linalg.external.serialization.OperationResponse;
import com.example.linalg.internal.graph.path.ParallelMultiSP;
import com.example.linalg.util.MatrixSerializer;

public class ShortestPathOperation
extends LinearAlgebraOperation {
    String methodName = "cpsp";
    int methodId = 2;
    MatrixSerializer MS = new MatrixSerializer();

    @Override
    public OperationResponse compute(OperationRequest req) {
        if (req.numberOfArguments != 2 || req.args.length != 2) {
            return new OperationResponse(this.methodId, false, "Missing arguments\n");
        }
        try {
            this.getClass();
            double[][] matrixA = this.MS.readMatrixFromCSV(req.args[0].matrix, req.args[0].rows, req.args[0].cols, 1000);
            if (matrixA.length != matrixA[0].length) {
                return new OperationResponse(this.methodId, false, "Only square matrices are supported");
            }
            this.getClass();
            double[][] targetNodes = this.MS.readMatrixFromCSV(req.args[1].matrix, req.args[1].rows, req.args[1].cols, 1000, false, false);
            if (targetNodes.length != 1) {
                return new OperationResponse(this.methodId, false, "Second argument should be a vector");
            }
            if ((double)targetNodes[0].length > Math.log(matrixA.length)) {
                return new OperationResponse(this.methodId, false, "Too many path operations");
            }
            int[] targets = new int[targetNodes[0].length];
            for (int i = 0; i < targetNodes[0].length; ++i) {
                targets[i] = Double.valueOf(targetNodes[0][i]).intValue();
            }
            ParallelMultiSP SP = new ParallelMultiSP(matrixA, targets);
            double[][] apsp = SP.compute();
            OperationResponse response = new OperationResponse(this.methodId, true, MatrixSerializer.matrixToCSV(apsp));
            return response;
        }
        catch (Exception e) {
            e.printStackTrace();
            return new OperationResponse(this.methodId, false, e.getLocalizedMessage());
        }
    }
}

