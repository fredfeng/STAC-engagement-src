/*
 * Decompiled with CFR 0_121.
 */
package com.example.linalg.external.operations;

import com.example.linalg.external.operations.LinearAlgebraOperation;
import com.example.linalg.external.serialization.OperationRequest;
import com.example.linalg.external.serialization.OperationResponse;
import com.example.linalg.internal.graph.SpanningTrees;
import com.example.linalg.util.MatrixSerializer;

public class MSTOperation
extends LinearAlgebraOperation {
    String methodName = "mst";
    int methodId = 4;
    MatrixSerializer MS = new MatrixSerializer();

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
            double[][] MST = SpanningTrees.MST(matrixA);
            OperationResponse response = new OperationResponse(this.methodId, true, MatrixSerializer.matrixToCSV(MST));
            return response;
        }
        catch (Exception e) {
            e.printStackTrace();
            return new OperationResponse(this.methodId, false, e.getLocalizedMessage());
        }
    }
}

