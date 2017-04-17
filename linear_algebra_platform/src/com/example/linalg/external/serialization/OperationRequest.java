/*
 * Decompiled with CFR 0_121.
 */
package com.example.linalg.external.serialization;

public class OperationRequest {
    public int operation = -1;
    public int numberOfArguments = -1;
    public Argument[] args;

    public class Argument {
        public int rows;
        public int cols;
        public String matrix;
    }

}

