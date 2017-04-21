/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.chart;

import edu.cyberapex.chart.Vertex;
import java.io.PrintStream;

public class TraversalPrinter {
    public void print(PrintStream out, Iterable<Vertex> iter) {
        boolean printedOne = false;
        for (Vertex v : iter) {
            if (printedOne) {
                this.printEngine(out);
            }
            out.print(v.getName());
            printedOne = true;
        }
        out.println();
    }

    private void printEngine(PrintStream out) {
        out.print("->");
    }
}

