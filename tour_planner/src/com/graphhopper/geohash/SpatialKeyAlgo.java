/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.geohash;

import com.graphhopper.geohash.KeyAlgo;
import com.graphhopper.util.shapes.BBox;
import com.graphhopper.util.shapes.GHPoint;

public class SpatialKeyAlgo
implements KeyAlgo {
    private BBox bbox;
    private int allBits;
    private long initialBits;

    public SpatialKeyAlgo(int allBits) {
        this.myinit(allBits);
    }

    private void myinit(int allBits) {
        if (allBits > 64) {
            throw new IllegalStateException("allBits is too big and does not fit into 8 bytes");
        }
        if (allBits <= 0) {
            throw new IllegalStateException("allBits must be positive");
        }
        this.allBits = allBits;
        this.initialBits = 1 << allBits - 1;
        this.setWorldBounds();
    }

    public int getBits() {
        return this.allBits;
    }

    public int getExactPrecision() {
        int p = (int)(Math.pow(2.0, this.allBits) / 360.0);
        return (int)Math.log10(++p);
    }

    public SpatialKeyAlgo bounds(BBox box) {
        this.bbox = box.clone();
        return this;
    }

    @Override
    public SpatialKeyAlgo setBounds(double minLonInit, double maxLonInit, double minLatInit, double maxLatInit) {
        this.bounds(new BBox(minLonInit, maxLonInit, minLatInit, maxLatInit));
        return this;
    }

    protected void setWorldBounds() {
        this.setBounds(-180.0, 180.0, -90.0, 90.0);
    }

    @Override
    public long encode(GHPoint coord) {
        return this.encode(coord.lat, coord.lon);
    }

    @Override
    public final long encode(double lat, double lon) {
        long hash = 0;
        double minLatTmp = this.bbox.minLat;
        double maxLatTmp = this.bbox.maxLat;
        double minLonTmp = this.bbox.minLon;
        double maxLonTmp = this.bbox.maxLon;
        int i = 0;
        do {
            if (minLatTmp < maxLatTmp) {
                double midLat = (minLatTmp + maxLatTmp) / 2.0;
                if (lat < midLat) {
                    maxLatTmp = midLat;
                } else {
                    hash |= 1;
                    minLatTmp = midLat;
                }
            }
            if (++i >= this.allBits) break;
            hash <<= 1;
            if (minLonTmp < maxLonTmp) {
                double midLon = (minLonTmp + maxLonTmp) / 2.0;
                if (lon < midLon) {
                    maxLonTmp = midLon;
                } else {
                    hash |= 1;
                    minLonTmp = midLon;
                }
            }
            if (++i >= this.allBits) break;
            hash <<= 1;
        } while (true);
        return hash;
    }

    @Override
    public final void decode(long spatialKey, GHPoint latLon) {
        double midLat = (this.bbox.maxLat - this.bbox.minLat) / 2.0;
        double midLon = (this.bbox.maxLon - this.bbox.minLon) / 2.0;
        double lat = this.bbox.minLat;
        double lon = this.bbox.minLon;
        long bits = this.initialBits;
        do {
            if ((spatialKey & bits) != 0) {
                lat += midLat;
            }
            midLat /= 2.0;
            if ((spatialKey & (bits >>>= 1)) != 0) {
                lon += midLon;
            }
            midLon /= 2.0;
            if (bits <= 1) break;
            bits >>>= 1;
        } while (true);
        latLon.lat = lat += midLat;
        latLon.lon = lon += midLon;
    }

    public String toString() {
        return "bits:" + this.allBits + ", bounds:" + this.bbox;
    }
}

