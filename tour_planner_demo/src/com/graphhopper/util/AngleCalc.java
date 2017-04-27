/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.util;

import com.graphhopper.util.Helper;

public class AngleCalc {
    private static final double PI_4 = 0.7853981633974483;
    private static final double PI_2 = 1.5707963267948966;
    private static final double PI3_4 = 2.356194490192345;

    static final double atan2(double y, double x) {
        double r;
        double angle;
        double absY = Math.abs(y) + 1.0E-10;
        if (x < 0.0) {
            r = (x + absY) / (absY - x);
            angle = 2.356194490192345;
        } else {
            r = (x - absY) / (x + absY);
            angle = 0.7853981633974483;
        }
        angle += (0.1963 * r * r - 0.9817) * r;
        if (y < 0.0) {
            return - angle;
        }
        return angle;
    }

    public double calcOrientation(double lat1, double lon1, double lat2, double lon2) {
        double shrinkFactor = Math.cos(Math.toRadians((lat1 + lat2) / 2.0));
        return Math.atan2(lat2 - lat1, shrinkFactor * (lon2 - lon1));
    }

    public double convertAzimuth2xaxisAngle(double azimuth) {
        if (Double.compare(azimuth, 360.0) > 0 || Double.compare(azimuth, 0.0) < 0) {
            throw new IllegalArgumentException("Azimuth " + azimuth + " must be in (0, 360)");
        }
        double angleXY = 1.5707963267948966 - azimuth / 180.0 * 3.141592653589793;
        if (angleXY < -3.141592653589793) {
            angleXY += 6.283185307179586;
        }
        if (angleXY > 3.141592653589793) {
            angleXY -= 6.283185307179586;
        }
        return angleXY;
    }

    public double alignOrientation(double baseOrientation, double orientation) {
        double resultOrientation = baseOrientation >= 0.0 ? (orientation < -3.141592653589793 + baseOrientation ? orientation + 6.283185307179586 : orientation) : (orientation > 3.141592653589793 + baseOrientation ? orientation - 6.283185307179586 : orientation);
        return resultOrientation;
    }

    double calcAzimuth(double lat1, double lon1, double lat2, double lon2) {
        double orientation = - this.calcOrientation(lat1, lon1, lat2, lon2);
        if ((orientation = Helper.round4(orientation + 1.5707963267948966)) < 0.0) {
            orientation += 6.283185307179586;
        }
        return Math.toDegrees(orientation);
    }

    String azimuth2compassPoint(double azimuth) {
        double slice = 22.5;
        String cp = azimuth < slice ? "N" : (azimuth < slice * 3.0 ? "NE" : (azimuth < slice * 5.0 ? "E" : (azimuth < slice * 7.0 ? "SE" : (azimuth < slice * 9.0 ? "S" : (azimuth < slice * 11.0 ? "SW" : (azimuth < slice * 13.0 ? "W" : (azimuth < slice * 15.0 ? "NW" : "N")))))));
        return cp;
    }
}

