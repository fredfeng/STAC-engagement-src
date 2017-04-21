/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.storage.index;

import com.graphhopper.storage.index.PointEmitter;

public class BresenhamLine {
    public static void calcPoints(int y1, int x1, int y2, int x2, PointEmitter emitter) {
        BresenhamLine.bresenham(y1, x1, y2, x2, emitter);
    }

    public static void voxelTraversal(double y1, double x1, double y2, double x2, PointEmitter emitter) {
        x1 = BresenhamLine.fix(x1);
        y1 = BresenhamLine.fix(y1);
        x2 = BresenhamLine.fix(x2);
        y2 = BresenhamLine.fix(y2);
        int x = (int)x1;
        int y = (int)y1;
        int endX = (int)x2;
        int endY = (int)y2;
        double gridCellWidth = 1.0;
        double gridCellHeight = 1.0;
        double deltaX = 1.0 / Math.abs(x2 - x1);
        int stepX = (int)Math.signum(x2 - x1);
        double tmp = BresenhamLine.frac(x1 / 1.0);
        double maxX = deltaX * (1.0 - tmp);
        double deltaY = 1.0 / Math.abs(y2 - y1);
        int stepY = (int)Math.signum(y2 - y1);
        tmp = BresenhamLine.frac(y1 / 1.0);
        double maxY = deltaY * (1.0 - tmp);
        boolean reachedY = false;
        boolean reachedX = false;
        emitter.set(y, x);
        while (!reachedX || !reachedY) {
            if (maxX < maxY) {
                maxX += deltaX;
                x += stepX;
            } else {
                maxY += deltaY;
                y += stepY;
            }
            emitter.set(y, x);
            if ((double)stepX > 0.0) {
                if (x >= endX) {
                    reachedX = true;
                }
            } else if (x <= endX) {
                reachedX = true;
            }
            if ((double)stepY > 0.0) {
                if (y < endY) continue;
                reachedY = true;
                continue;
            }
            if (y > endY) continue;
            reachedY = true;
        }
    }

    static final double fix(double val) {
        if (BresenhamLine.frac(val) == 0.0) {
            return val + 0.1;
        }
        return val;
    }

    static final double frac(double val) {
        return val - (double)((int)val);
    }

    public static void bresenham(int y1, int x1, int y2, int x2, PointEmitter emitter) {
        boolean latIncreasing = y1 < y2;
        boolean lonIncreasing = x1 < x2;
        int dLat = Math.abs(y2 - y1);
        int sLat = latIncreasing ? 1 : -1;
        int dLon = Math.abs(x2 - x1);
        int sLon = lonIncreasing ? 1 : -1;
        int err = dLon - dLat;
        do {
            emitter.set(y1, x1);
            if (y1 == y2 && x1 == x2) break;
            int tmpErr = 2 * err;
            if (tmpErr > - dLat) {
                err -= dLat;
                x1 += sLon;
            }
            if (tmpErr >= dLon) continue;
            err += dLon;
            y1 += sLat;
        } while (true);
    }

    public static void xiaolinWu(double y1, double x1, double y2, double x2, PointEmitter emitter) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        if (Math.abs(dx) > Math.abs(dy)) {
            if (x2 < x1) {
                double tmp = x1;
                x1 = x2;
                x2 = tmp;
                tmp = y1;
                y1 = y2;
                y2 = tmp;
            }
            double gradient = dy / dx;
            int xend = (int)x1;
            double yend = y1 + gradient * ((double)xend - x1);
            int xpxl1 = xend;
            int ypxl1 = (int)yend;
            emitter.set(ypxl1, xpxl1);
            emitter.set(ypxl1 + 1, xpxl1);
            double intery = yend + gradient;
            xend = (int)x2;
            yend = y2 + gradient * ((double)xend - x2);
            int xpxl2 = xend;
            int ypxl2 = (int)yend;
            emitter.set(ypxl2, xpxl2);
            emitter.set(ypxl2 + 1, xpxl2);
            for (int x = xpxl1 + 1; x <= xpxl2 - 1; ++x) {
                emitter.set((int)intery, x);
                emitter.set((int)intery + 1, x);
                intery += gradient;
            }
        } else {
            if (y2 < y1) {
                double tmp = x1;
                x1 = x2;
                x2 = tmp;
                tmp = y1;
                y1 = y2;
                y2 = tmp;
            }
            double gradient = dx / dy;
            int yend = (int)y1;
            double xend = x1 + gradient * ((double)yend - y1);
            int ypxl1 = yend;
            int xpxl1 = (int)xend;
            emitter.set(ypxl1, xpxl1);
            emitter.set(ypxl1 + 1, xpxl1);
            double interx = xend + gradient;
            yend = (int)y2;
            xend = x2 + gradient * ((double)yend - y2);
            int ypxl2 = yend;
            int xpxl2 = (int)xend;
            emitter.set(ypxl2, xpxl2);
            emitter.set(ypxl2 + 1, xpxl2);
            for (int y = ypxl1 + 1; y <= ypxl2 - 1; ++y) {
                emitter.set(y, (int)interx);
                emitter.set(y, (int)interx + 1);
                interx += gradient;
            }
        }
    }

    public static void calcPoints(double lat1, double lon1, double lat2, double lon2, final PointEmitter emitter, final double offsetLat, final double offsetLon, final double deltaLat, final double deltaLon) {
        int y1 = (int)((lat1 - offsetLat) / deltaLat);
        int x1 = (int)((lon1 - offsetLon) / deltaLon);
        int y2 = (int)((lat2 - offsetLat) / deltaLat);
        int x2 = (int)((lon2 - offsetLon) / deltaLon);
        BresenhamLine.bresenham(y1, x1, y2, x2, new PointEmitter(){

            @Override
            public void set(double lat, double lon) {
                emitter.set((lat + 0.1) * deltaLat + offsetLat, (lon + 0.1) * deltaLon + offsetLon);
            }
        });
    }

}

