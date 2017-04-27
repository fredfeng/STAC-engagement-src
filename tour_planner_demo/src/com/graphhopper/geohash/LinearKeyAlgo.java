/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.geohash;

import com.graphhopper.geohash.KeyAlgo;
import com.graphhopper.util.shapes.BBox;
import com.graphhopper.util.shapes.GHPoint;

public class LinearKeyAlgo
implements KeyAlgo {
    private BBox bounds;
    private double latDelta;
    private double lonDelta;
    private final int latUnits;
    private final int lonUnits;
    private static final double C = 0.999999999999999;

    public LinearKeyAlgo(int latUnits, int lonUnits) {
        this.latUnits = latUnits;
        this.lonUnits = lonUnits;
        this.setWorldBounds();
    }

    @Override
    public LinearKeyAlgo setBounds(double minLonInit, double maxLonInit, double minLatInit, double maxLatInit) {
        this.bounds = new BBox(minLonInit, maxLonInit, minLatInit, maxLatInit);
        this.latDelta = (this.bounds.maxLat - this.bounds.minLat) / (double)this.latUnits;
        this.lonDelta = (this.bounds.maxLon - this.bounds.minLon) / (double)this.lonUnits;
        return this;
    }

    public LinearKeyAlgo setBounds(BBox bounds) {
        this.setBounds(bounds.minLon, bounds.maxLon, bounds.minLat, bounds.maxLat);
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
        lat = Math.min(Math.max(lat, this.bounds.minLat), this.bounds.maxLat);
        lon = Math.min(Math.max(lon, this.bounds.minLon), this.bounds.maxLon);
        long latIndex = (long)((lat - this.bounds.minLat) / this.latDelta * 0.999999999999999);
        long lonIndex = (long)((lon - this.bounds.minLon) / this.lonDelta * 0.999999999999999);
        return latIndex * (long)this.lonUnits + lonIndex;
    }

    @Override
    public final void decode(long linearKey, GHPoint latLon) {
        double lat = (double)(linearKey / (long)this.lonUnits) * this.latDelta + this.bounds.minLat;
        double lon = (double)(linearKey % (long)this.lonUnits) * this.lonDelta + this.bounds.minLon;
        latLon.lat = lat + this.latDelta / 2.0;
        latLon.lon = lon + this.lonDelta / 2.0;
    }

    public double getLatDelta() {
        return this.latDelta;
    }

    public double getLonDelta() {
        return this.lonDelta;
    }
}

