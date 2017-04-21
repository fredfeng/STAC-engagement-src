/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.chart;

import edu.cyberapex.chart.Chart;
import edu.cyberapex.chart.ChartFailure;
import edu.cyberapex.chart.Vertex;
import java.util.List;

public class ChartSize {
    public static Size describeSize(Chart g) throws ChartFailure {
        int order = g.takeVertices().size();
        return Size.fromInt(order);
    }

    public static enum Size {
        VERY_LARGE(300, Integer.MAX_VALUE),
        MODERATELY_LARGE(100, 300),
        FAIRLY_SMALL(0, 100);
        
        private int minSize;
        private int maxSize;

        private Size(int minSize, int maxSize) {
            this.minSize = minSize;
            this.maxSize = maxSize;
        }

        public int pullMinimumSize() {
            return this.minSize;
        }

        public int grabMaximumSize() {
            return this.maxSize;
        }

        public boolean containsSize(int size) {
            return this.minSize <= size && size < this.maxSize;
        }

        public static Size fromInt(int size) {
            Size[] values = Size.values();
            for (int q = 0; q < values.length; ++q) {
                Size sizeEnum = values[q];
                if (!sizeEnum.containsSize(size)) continue;
                return sizeEnum;
            }
            return null;
        }
    }

}

