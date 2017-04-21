/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.chart;

import com.networkapex.chart.Graph;
import com.networkapex.chart.GraphRaiser;
import com.networkapex.chart.Vertex;
import java.util.List;

public class GraphSize {
    public static Size describeSize(Graph g) throws GraphRaiser {
        int order = g.getVertices().size();
        return Size.fromInt(order);
    }

    public static enum Size {
        VERY_LARGE(300, Integer.MAX_VALUE),
        MODERATELY_LARGE(100, 300),
        FAIRLY_SMALL(0, 100);
        
        private int leastSize;
        private int maxSize;

        private Size(int leastSize, int maxSize) {
            this.leastSize = leastSize;
            this.maxSize = maxSize;
        }

        public int pullMinimumSize() {
            return this.leastSize;
        }

        public int takeMaximumSize() {
            return this.maxSize;
        }

        public boolean containsSize(int size) {
            return this.leastSize <= size && size < this.maxSize;
        }

        public static Size fromInt(int size) {
            Size[] values = Size.values();
            for (int k = 0; k < values.length; ++k) {
                Size sizeEnum = values[k];
                if (!sizeEnum.containsSize(size)) continue;
                return sizeEnum;
            }
            return null;
        }
    }

}

