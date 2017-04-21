/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.reader;

import com.graphhopper.storage.Graph;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.Helper;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class PrinctonReader {
    private Graph g;
    private InputStream is;

    public PrinctonReader(Graph graph) {
        this.g = graph;
    }

    public PrinctonReader setStream(InputStream is) {
        this.is = is;
        return this;
    }

    public void read() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(this.is), 8192);
        int lineNo = 0;
        try {
            ++lineNo;
            int nodes = Integer.parseInt(reader.readLine());
            ++lineNo;
            int edges = Integer.parseInt(reader.readLine());
            for (int i = 0; i < edges; ++i) {
                ++lineNo;
                String line = reader.readLine();
                String[] args = line.split(" ");
                int from = -1;
                int to = -1;
                double dist = -1.0;
                int counter = 0;
                for (int j = 0; j < args.length; ++j) {
                    if (Helper.isEmpty(args[j])) continue;
                    if (counter == 0) {
                        from = Integer.parseInt(args[j]);
                    } else if (counter == 1) {
                        to = Integer.parseInt(args[j]);
                    } else {
                        dist = Double.parseDouble(args[j]);
                    }
                    ++counter;
                }
                if (counter != 3) {
                    throw new RuntimeException("incorrect read!? from:" + from + ", to:" + to + ", dist:" + dist);
                }
                this.g.edge(from, to, dist, false);
            }
        }
        catch (Exception ex) {
            throw new RuntimeException("Problem in line " + lineNo, ex);
        }
        finally {
            Helper.close(reader);
        }
    }
}

