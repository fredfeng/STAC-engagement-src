/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.util;

import com.graphhopper.util.DistanceCalc;
import com.graphhopper.util.DistancePlaneProjection;
import com.graphhopper.util.Helper;
import com.graphhopper.util.PointList;

public class DouglasPeucker {
    private double normedMaxDist;
    private DistanceCalc calc;
    private boolean approx;

    public DouglasPeucker() {
        this.setApproximation(true);
        this.setMaxDistance(1.0);
    }

    public void setApproximation(boolean a) {
        this.approx = a;
        this.calc = this.approx ? Helper.DIST_PLANE : Helper.DIST_EARTH;
    }

    public DouglasPeucker setMaxDistance(double dist) {
        this.normedMaxDist = this.calc.calcNormalizedDist(dist);
        return this;
    }

    public int simplify(PointList points) {
        int removed = 0;
        int size = points.getSize();
        if (this.approx) {
            int delta = 500;
            int segments = size / delta + 1;
            int start = 0;
            for (int i = 0; i < segments; ++i) {
                removed += this.simplify(points, start, Math.min(size - 1, start + delta));
                start += delta;
            }
        } else {
            removed = this.simplify(points, 0, size - 1);
        }
        this.compressNew(points, removed);
        return removed;
    }

    void compressNew(PointList points, int removed) {
        int freeIndex = -1;
        block0 : for (int currentIndex = 0; currentIndex < points.getSize(); ++currentIndex) {
            if (Double.isNaN(points.getLatitude(currentIndex))) {
                if (freeIndex >= 0) continue;
                freeIndex = currentIndex;
                continue;
            }
            if (freeIndex < 0) continue;
            points.set(freeIndex, points.getLatitude(currentIndex), points.getLongitude(currentIndex), points.getElevation(currentIndex));
            points.set(currentIndex, Double.NaN, Double.NaN, Double.NaN);
            int max = currentIndex;
            int searchIndex = freeIndex + 1;
            freeIndex = currentIndex;
            while (searchIndex < max) {
                if (Double.isNaN(points.getLatitude(searchIndex))) {
                    freeIndex = searchIndex;
                    continue block0;
                }
                ++searchIndex;
            }
        }
        points.trimToSize(points.getSize() - removed);
    }

    int simplify(PointList points, int fromIndex, int lastIndex) {
        if (lastIndex - fromIndex < 2) {
            return 0;
        }
        int indexWithMaxDist = -1;
        double maxDist = -1.0;
        double firstLat = points.getLatitude(fromIndex);
        double firstLon = points.getLongitude(fromIndex);
        double lastLat = points.getLatitude(lastIndex);
        double lastLon = points.getLongitude(lastIndex);
        for (int i = fromIndex + 1; i < lastIndex; ++i) {
            double dist;
            double lon;
            double lat = points.getLatitude(i);
            if (Double.isNaN(lat) || maxDist >= (dist = this.calc.calcNormalizedEdgeDistance(lat, lon = points.getLongitude(i), firstLat, firstLon, lastLat, lastLon))) continue;
            indexWithMaxDist = i;
            maxDist = dist;
        }
        if (indexWithMaxDist < 0) {
            throw new IllegalStateException("maximum not found in [" + fromIndex + "," + lastIndex + "]");
        }
        int counter = 0;
        if (maxDist < this.normedMaxDist) {
            for (int i = fromIndex + 1; i < lastIndex; ++i) {
                points.set(i, Double.NaN, Double.NaN, Double.NaN);
                ++counter;
            }
        } else {
            counter = this.simplify(points, fromIndex, indexWithMaxDist);
            counter += this.simplify(points, indexWithMaxDist, lastIndex);
        }
        return counter;
    }
}

