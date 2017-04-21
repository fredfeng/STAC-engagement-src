/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.util;

public interface PointAccess {
    public boolean is3D();

    public int getDimension();

    public void ensureNode(int var1);

    public void setNode(int var1, double var2, double var4);

    public void setNode(int var1, double var2, double var4, double var6);

    public double getLatitude(int var1);

    public double getLat(int var1);

    public double getLongitude(int var1);

    public double getLon(int var1);

    public double getElevation(int var1);

    public double getEle(int var1);
}

