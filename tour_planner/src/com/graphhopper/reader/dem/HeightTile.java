/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.reader.dem;

import com.graphhopper.storage.DataAccess;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;

public class HeightTile {
    private DataAccess heights;
    private final int minLat;
    private final int minLon;
    private final int width;
    private final int degree;
    private final double lowerBound;
    private final double higherBound;
    private boolean calcMean;

    public HeightTile(int minLat, int minLon, int width, double precision, int degree) {
        this.minLat = minLat;
        this.minLon = minLon;
        this.width = width;
        this.lowerBound = -1.0 / precision;
        this.higherBound = (double)degree + 1.0 / precision;
        this.degree = degree;
    }

    public HeightTile setCalcMean(boolean b) {
        this.calcMean = b;
        return this;
    }

    public HeightTile setSeaLevel(boolean b) {
        this.heights.setHeader(0, b ? 1 : 0);
        return this;
    }

    public boolean isSeaLevel() {
        return this.heights.getHeader(0) == 1;
    }

    void setHeights(DataAccess da) {
        this.heights = da;
    }

    public double getHeight(double lat, double lon) {
        int latSimilar;
        double deltaLat = Math.abs(lat - (double)this.minLat);
        double deltaLon = Math.abs(lon - (double)this.minLon);
        if (deltaLat > this.higherBound || deltaLat < this.lowerBound) {
            throw new IllegalStateException("latitude not in boundary of this file:" + lat + "," + lon + ", this:" + this.toString());
        }
        if (deltaLon > this.higherBound || deltaLon < this.lowerBound) {
            throw new IllegalStateException("longitude not in boundary of this file:" + lat + "," + lon + ", this:" + this.toString());
        }
        int lonSimilar = (int)((double)(this.width / this.degree) * deltaLon);
        if (lonSimilar >= this.width) {
            lonSimilar = this.width - 1;
        }
        if ((latSimilar = this.width - 1 - (int)((double)(this.width / this.degree) * deltaLat)) < 0) {
            latSimilar = 0;
        }
        int daPointer = 2 * (latSimilar * this.width + lonSimilar);
        int value = this.heights.getShort(daPointer);
        AtomicInteger counter = new AtomicInteger(1);
        if (value == -32768) {
            return Double.NaN;
        }
        if (this.calcMean) {
            if (lonSimilar > 0) {
                value = (int)((double)value + this.includePoint(daPointer - 2, counter));
            }
            if (lonSimilar < this.width - 1) {
                value = (int)((double)value + this.includePoint(daPointer + 2, counter));
            }
            if (latSimilar > 0) {
                value = (int)((double)value + this.includePoint(daPointer - 2 * this.width, counter));
            }
            if (latSimilar < this.width - 1) {
                value = (int)((double)value + this.includePoint(daPointer + 2 * this.width, counter));
            }
        }
        return (double)value / (double)counter.get();
    }

    private double includePoint(int pointer, AtomicInteger counter) {
        short value = this.heights.getShort(pointer);
        if (value == -32768) {
            return 0.0;
        }
        counter.incrementAndGet();
        return value;
    }

    public void toImage(String imageFile) throws IOException {
        ImageIO.write((RenderedImage)this.makeARGB(), "PNG", new File(imageFile));
    }

    protected BufferedImage makeARGB() {
        int height = this.width;
        BufferedImage argbImage = new BufferedImage(this.width, height, 2);
        Graphics g = argbImage.getGraphics();
        long len = this.heights.getCapacity() / 2;
        int i = 0;
        while ((long)i < len) {
            int lonSimilar = i % this.width;
            int latSimilar = i / this.width;
            int green = Math.abs(this.heights.getShort(i * 2));
            if (green == 0) {
                g.setColor(new Color(255, 0, 0, 255));
            } else {
                int red = 0;
                while (green > 255) {
                    green /= 10;
                    red += 50;
                }
                if (red > 255) {
                    red = 255;
                }
                g.setColor(new Color(red, green, 122, 255));
            }
            g.drawLine(lonSimilar, latSimilar, lonSimilar, latSimilar);
            ++i;
        }
        g.dispose();
        return argbImage;
    }

    public BufferedImage getImageFromArray(int[] pixels, int width) {
        int height = width;
        BufferedImage tmpImage = new BufferedImage(width, height, 3);
        tmpImage.setRGB(0, 0, width, height, pixels, 0, width);
        return tmpImage;
    }

    public String toString() {
        return "" + this.minLat + "," + this.minLon;
    }
}

