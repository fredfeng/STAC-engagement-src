/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.util;

import com.graphhopper.util.DistanceCalc;
import com.graphhopper.util.shapes.BBox;
import com.graphhopper.util.shapes.GHPoint;

public class DistanceCalcEarth
implements DistanceCalc {
    public static final double R = 6371000.0;
    public static final double R_EQ = 6378137.0;
    public static final double C = 4.003017359204114E7;
    public static final double KM_MILE = 1.609344;

    @Override
    public double calcDist(double fromLat, double fromLon, double toLat, double toLon) {
        double sinDeltaLat = Math.sin(Math.toRadians(toLat - fromLat) / 2.0);
        double sinDeltaLon = Math.sin(Math.toRadians(toLon - fromLon) / 2.0);
        double normedDist = sinDeltaLat * sinDeltaLat + sinDeltaLon * sinDeltaLon * Math.cos(Math.toRadians(fromLat)) * Math.cos(Math.toRadians(toLat));
        return 1.2742E7 * Math.asin(Math.sqrt(normedDist));
    }

    @Override
    public double calcDenormalizedDist(double normedDist) {
        return 1.2742E7 * Math.asin(Math.sqrt(normedDist));
    }

    @Override
    public double calcNormalizedDist(double dist) {
        double tmp = Math.sin(dist / 2.0 / 6371000.0);
        return tmp * tmp;
    }

    @Override
    public double calcNormalizedDist(double fromLat, double fromLon, double toLat, double toLon) {
        double sinDeltaLat = Math.sin(Math.toRadians(toLat - fromLat) / 2.0);
        double sinDeltaLon = Math.sin(Math.toRadians(toLon - fromLon) / 2.0);
        return sinDeltaLat * sinDeltaLat + sinDeltaLon * sinDeltaLon * Math.cos(Math.toRadians(fromLat)) * Math.cos(Math.toRadians(toLat));
    }

    @Override
    public double calcCircumference(double lat) {
        return 4.003017359204114E7 * Math.cos(Math.toRadians(lat));
    }

    public boolean isDateLineCrossOver(double lon1, double lon2) {
        return Math.abs(lon1 - lon2) > 180.0;
    }

    @Override
    public BBox createBBox(double lat, double lon, double radiusInMeter) {
        if (radiusInMeter <= 0.0) {
            throw new IllegalArgumentException("Distance must not be zero or negative! " + radiusInMeter + " lat,lon:" + lat + "," + lon);
        }
        double dLon = 360.0 / (this.calcCircumference(lat) / radiusInMeter);
        double dLat = 360.0 / (4.003017359204114E7 / radiusInMeter);
        return new BBox(lon - dLon, lon + dLon, lat - dLat, lat + dLat);
    }

    @Override
    public double calcNormalizedEdgeDistance(double r_lat_deg, double r_lon_deg, double a_lat_deg, double a_lon_deg, double b_lat_deg, double b_lon_deg) {
        return this.calcNormalizedEdgeDistanceNew(r_lat_deg, r_lon_deg, a_lat_deg, a_lon_deg, b_lat_deg, b_lon_deg, false);
    }

    public double calcNormalizedEdgeDistanceNew(double r_lat_deg, double r_lon_deg, double a_lat_deg, double a_lon_deg, double b_lat_deg, double b_lon_deg, boolean reduceToSegment) {
        double shrinkFactor = Math.cos(Math.toRadians((a_lat_deg + b_lat_deg) / 2.0));
        double a_lat = a_lat_deg;
        double a_lon = a_lon_deg * shrinkFactor;
        double b_lat = b_lat_deg;
        double b_lon = b_lon_deg * shrinkFactor;
        double r_lat = r_lat_deg;
        double r_lon = r_lon_deg * shrinkFactor;
        double delta_lon = b_lon - a_lon;
        double delta_lat = b_lat - a_lat;
        if (delta_lat == 0.0) {
            return this.calcNormalizedDist(a_lat_deg, r_lon_deg, r_lat_deg, r_lon_deg);
        }
        if (delta_lon == 0.0) {
            return this.calcNormalizedDist(r_lat_deg, a_lon_deg, r_lat_deg, r_lon_deg);
        }
        double norm = delta_lon * delta_lon + delta_lat * delta_lat;
        double factor = ((r_lon - a_lon) * delta_lon + (r_lat - a_lat) * delta_lat) / norm;
        if (reduceToSegment) {
            if (factor > 1.0) {
                factor = 1.0;
            } else if (factor < 0.0) {
                factor = 0.0;
            }
        }
        double c_lon = a_lon + factor * delta_lon;
        double c_lat = a_lat + factor * delta_lat;
        return this.calcNormalizedDist(c_lat, c_lon / shrinkFactor, r_lat_deg, r_lon_deg);
    }

    @Override
    public GHPoint calcCrossingPointToEdge(double r_lat_deg, double r_lon_deg, double a_lat_deg, double a_lon_deg, double b_lat_deg, double b_lon_deg) {
        double shrinkFactor = Math.cos(Math.toRadians((a_lat_deg + b_lat_deg) / 2.0));
        double a_lat = a_lat_deg;
        double a_lon = a_lon_deg * shrinkFactor;
        double b_lat = b_lat_deg;
        double b_lon = b_lon_deg * shrinkFactor;
        double r_lat = r_lat_deg;
        double r_lon = r_lon_deg * shrinkFactor;
        double delta_lon = b_lon - a_lon;
        double delta_lat = b_lat - a_lat;
        if (delta_lat == 0.0) {
            return new GHPoint(a_lat_deg, r_lon_deg);
        }
        if (delta_lon == 0.0) {
            return new GHPoint(r_lat_deg, a_lon_deg);
        }
        double norm = delta_lon * delta_lon + delta_lat * delta_lat;
        double factor = ((r_lon - a_lon) * delta_lon + (r_lat - a_lat) * delta_lat) / norm;
        double c_lon = a_lon + factor * delta_lon;
        double c_lat = a_lat + factor * delta_lat;
        return new GHPoint(c_lat, c_lon / shrinkFactor);
    }

    @Override
    public boolean validEdgeDistance(double r_lat_deg, double r_lon_deg, double a_lat_deg, double a_lon_deg, double b_lat_deg, double b_lon_deg) {
        double shrinkFactor = Math.cos(Math.toRadians((a_lat_deg + b_lat_deg) / 2.0));
        double a_lat = a_lat_deg;
        double a_lon = a_lon_deg * shrinkFactor;
        double b_lat = b_lat_deg;
        double b_lon = b_lon_deg * shrinkFactor;
        double r_lat = r_lat_deg;
        double r_lon = r_lon_deg * shrinkFactor;
        double ar_x = r_lon - a_lon;
        double ar_y = r_lat - a_lat;
        double ab_x = b_lon - a_lon;
        double ab_y = b_lat - a_lat;
        double ab_ar = ar_x * ab_x + ar_y * ab_y;
        double rb_x = b_lon - r_lon;
        double rb_y = b_lat - r_lat;
        double ab_rb = rb_x * ab_x + rb_y * ab_y;
        return ab_ar > 0.0 && ab_rb > 0.0;
    }

    public String toString() {
        return "EXACT";
    }
}

