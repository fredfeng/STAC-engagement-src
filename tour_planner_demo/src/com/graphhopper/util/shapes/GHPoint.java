/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.util.shapes;

import com.graphhopper.util.NumHelper;

public class GHPoint {
    public double lat = Double.NaN;
    public double lon = Double.NaN;

    public GHPoint() {
    }

    public GHPoint(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLon() {
        return this.lon;
    }

    public double getLat() {
        return this.lat;
    }

    public boolean isValid() {
        return !Double.isNaN(this.lat) && !Double.isNaN(this.lon);
    }

    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (int)(Double.doubleToLongBits(this.lat) ^ Double.doubleToLongBits(this.lat) >>> 32);
        hash = 83 * hash + (int)(Double.doubleToLongBits(this.lon) ^ Double.doubleToLongBits(this.lon) >>> 32);
        return hash;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        GHPoint other = (GHPoint)obj;
        return NumHelper.equalsEps(this.lat, other.lat) && NumHelper.equalsEps(this.lon, other.lon);
    }

    public String toString() {
        return "<" + this.lat + ", " + this.lon + ">";
    }

    public Double[] toGeoJson() {
        return new Double[]{this.lon, this.lat};
    }

    public static GHPoint parse(String str) {
        String[] fromStrs;
        if (str.startsWith("<") && str.endsWith(">")) {
            str = str.substring(1, str.length());
        }
        if ((fromStrs = str.split(",")).length == 2) {
            try {
                double fromLat = Double.parseDouble(fromStrs[0]);
                double fromLon = Double.parseDouble(fromStrs[1]);
                return new GHPoint(fromLat, fromLon);
            }
            catch (Exception fromLat) {
                // empty catch block
            }
        }
        return null;
    }
}

