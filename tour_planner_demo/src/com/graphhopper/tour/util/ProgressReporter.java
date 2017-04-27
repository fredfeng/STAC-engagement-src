/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.tour.util;

import java.io.IOException;
import java.io.PrintStream;

public interface ProgressReporter {
    public static final ProgressReporter SILENT = new ProgressReporter(){

        @Override
        public void reportProgress(int total, int complete) {
        }
    };
    public static final ProgressReporter STDERR = new ProgressReporter(){

        @Override
        public void reportProgress(int complete, int total) {
            System.err.format("%d/%d complete\n", complete, total);
        }
    };

    public void reportProgress(int var1, int var2) throws IOException;

}

