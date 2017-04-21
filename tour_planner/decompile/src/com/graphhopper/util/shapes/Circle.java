/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.util.shapes;

import com.graphhopper.util.DistanceCalc;
import com.graphhopper.util.Helper;
import com.graphhopper.util.shapes.BBox;
import com.graphhopper.util.shapes.Shape;

public class Circle
implements Shape {
    private DistanceCalc calc = Helper.DIST_EARTH;
    private final double radiusInKm;
    private final double lat;
    private final double lon;
    private final double normedDist;
    private final BBox bbox;

    public Circle(double lat, double lon, double radiusInMeter) {
        this(lat, lon, radiusInMeter, Helper.DIST_EARTH);
    }

    public Circle(double lat, double lon, double radiusInMeter, DistanceCalc calc) {
        this.calc = calc;
        this.lat = lat;
        this.lon = lon;
        this.radiusInKm = radiusInMeter;
        this.normedDist = calc.calcNormalizedDist(radiusInMeter);
        this.bbox = calc.createBBox(lat, lon, radiusInMeter);
    }

    public double getLat() {
        return this.lat;
    }

    public double getLon() {
        return this.lon;
    }

    @Override
    public boolean contains(double lat1, double lon1) {
        return this.normDist(lat1, lon1) <= this.normedDist;
    }

    @Override
    public BBox getBounds() {
        return this.bbox;
    }

    private double normDist(double lat1, double lon1) {
        return this.calc.calcNormalizedDist(this.lat, this.lon, lat1, lon1);
    }

    @Override
    public boolean intersect(Shape o) {
        if (o instanceof Circle) {
            return this.intersect((Circle)o);
        }
        if (o instanceof BBox) {
            return this.intersect((BBox)o);
        }
        return o.intersect(this);
    }

    @Override
    public boolean contains(Shape o) {
        if (o instanceof Circle) {
            return this.contains((Circle)o);
        }
        if (o instanceof BBox) {
            return this.contains((BBox)o);
        }
        throw new UnsupportedOperationException("unsupported shape");
    }

    public boolean intersect(BBox b) {
        if (this.lat > b.maxLat) {
            if (this.lon < b.minLon) {
                return this.normDist(b.maxLat, b.minLon) <= this.normedDist;
            }
            if (this.lon > b.maxLon) {
                return this.normDist(b.maxLat, b.maxLon) <= this.normedDist;
            }
            return b.maxLat - this.bbox.minLat > 0.0;
        }
        if (this.lat < b.minLat) {
            if (this.lon < b.minLon) {
                return this.normDist(b.minLat, b.minLon) <= this.normedDist;
            }
            if (this.lon > b.maxLon) {
                return this.normDist(b.minLat, b.maxLon) <= this.normedDist;
            }
            return this.bbox.maxLat - b.minLat > 0.0;
        }
        if (this.lon < b.minLon) {
            return this.bbox.maxLon - b.minLon > 0.0;
        }
        if (this.lon > b.maxLon) {
            return b.maxLon - this.bbox.minLon > 0.0;
        }
        return true;
    }

    public boolean intersect(Circle c) {
        if (!this.getBounds().intersect(c.getBounds())) {
            return false;
        }
        return this.normDist(c.lat, c.lon) <= this.calc.calcNormalizedDist(this.radiusInKm + c.radiusInKm);
    }

    public boolean contains(BBox b) {
        if (this.bbox.contains(b)) {
            return this.contains(b.maxLat, b.minLon) && this.contains(b.minLat, b.minLon) && this.contains(b.maxLat, b.maxLon) && this.contains(b.minLat, b.maxLon);
        }
        return false;
    }

    public boolean contains(Circle c) {
        double res = this.radiusInKm - c.radiusInKm;
        if (res < 0.0) {
            return false;
        }
        return this.calc.calcDist(this.lat, this.lon, c.lat, c.lon) <= res;
    }

    public String toString() {
        return "" + this.lat + "," + this.lon + ", radius:" + this.radiusInKm;
    }
}

