/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.chart;

import com.networkapex.chart.Vertex;
import java.io.PrintStream;

public class TraversalPrinter {
    public void print(PrintStream out, Iterable<Vertex> iter) {
        boolean printedOne = false;
        for (Vertex v : iter) {
            if (printedOne) {
                out.print("->");
            }
            out.print(v.getName());
            printedOne = true;
        }
        out.println();
    }
}

