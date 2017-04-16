/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.util;

public class NumHelper {
    private static final double DEFAULT_PRECISION = 1.0E-6;

    public static boolean equalsEps(double d1, double d2) {
        return NumHelper.equalsEps(d1, d2, 1.0E-6);
    }

    public static boolean equalsEps(double d1, double d2, double epsilon) {
        return Math.abs(d1 - d2) < epsilon;
    }

    public static boolean equals(double d1, double d2) {
        return Double.compare(d1, d2) == 0;
    }

    public static int compare(double d1, double d2) {
        return Double.compare(d1, d2);
    }

    public static boolean equalsEps(float d1, float d2) {
        return NumHelper.equalsEps((double)d1, (double)d2, 1.0E-6);
    }

    public static boolean equalsEps(float d1, float d2, float epsilon) {
        return Math.abs(d1 - d2) < epsilon;
    }
}

