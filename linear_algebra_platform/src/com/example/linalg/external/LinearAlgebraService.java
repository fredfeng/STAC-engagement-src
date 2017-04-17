/*
 * Decompiled with CFR 0_121.
 */
package com.example.linalg.external;

import com.example.linalg.external.operations.LaplacianOperation;
import com.example.linalg.external.operations.LinearAlgebraOperation;
import com.example.linalg.external.operations.MSTOperation;
import com.example.linalg.external.operations.MultiplyOperation;
import com.example.linalg.external.operations.ShortestPathOperation;
import com.example.linalg.external.serialization.OperationRequest;
import com.example.linalg.external.serialization.OperationResponse;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import fi.iki.elonen.NanoHTTPD;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.lang.reflect.Type;

public class LinearAlgebraService
extends NanoHTTPD {
    Gson GSON = new Gson();

    public LinearAlgebraService(int p) throws IOException {
        super(p);
        this.start(5000, false);
        System.out.println("Service has been started, running on port " + p);
    }

    @Override
    public NanoHTTPD.Response serve(NanoHTTPD.IHTTPSession session) {
        OperationResponse resp = null;
        LinearAlgebraOperation operation = null;
        OperationRequest req = null;
        try {
            req = (OperationRequest)this.GSON.fromJson(new JsonReader(new InputStreamReader(session.getInputStream())), (Type)((Object)OperationRequest.class));
            if (req.operation < 0 || req.numberOfArguments < 0) {
                return LinearAlgebraService.newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "Missing arguments, method\n");
            }
            switch (req.operation) {
                case 1: {
                    operation = new MultiplyOperation();
                    break;
                }
                case 2: {
                    operation = new ShortestPathOperation();
                    break;
                }
                case 3: {
                    operation = new LaplacianOperation();
                    break;
                }
                case 4: {
                    operation = new MSTOperation();
                    break;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return LinearAlgebraService.newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "Could not parse input request\n");
        }
        if (operation == null || req == null) {
            return LinearAlgebraService.newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "Could not find supported operation\n");
        }
        try {
            resp = operation.compute(req);
            if (resp.success) {
                return LinearAlgebraService.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/plain", this.GSON.toJson(resp));
            }
            return LinearAlgebraService.newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", this.GSON.toJson(resp));
        }
        catch (Exception e) {
            return LinearAlgebraService.newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "Unknown error");
        }
    }
}

