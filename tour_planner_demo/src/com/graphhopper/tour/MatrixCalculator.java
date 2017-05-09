/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.tour;

import com.graphhopper.GraphHopper;
import com.graphhopper.routing.Path;
import com.graphhopper.tour.Matrix;
import com.graphhopper.tour.PathCalculator;
import com.graphhopper.util.shapes.GHPoint;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MatrixCalculator {
    private final Logger logger;
    private final GraphHopper hopper;

    public MatrixCalculator(GraphHopper hopper) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.hopper = hopper;
    }

    public <P extends GHPoint> Matrix<P> calcMatrix(List<P> points) {
        PathCalculator pc = new PathCalculator(this.hopper);
        Matrix<P> matrix = new Matrix<P>(points);
        int size = points.size();
        int numPaths = size * (size - 1);
        this.logger.info("Calculating " + numPaths + " pairwise paths");
        int c = 0;
        for (int i = 0; i < size; ++i) {
            GHPoint from = (GHPoint)points.get(i);
            for (int j = 0; j < size; ++j) {
                if (j == i) continue;
                GHPoint to = (GHPoint)points.get(j);
                Path path = pc.calcPath(from, to);
                matrix.setWeight(i, j, path.getWeight());
                if (++c % 100 != 0) continue;
                this.logger.info("" + c + "/" + numPaths);
            }
        }
        return matrix;
    }
}

