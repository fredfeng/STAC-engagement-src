/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.util.shapes;

import com.graphhopper.util.shapes.BBox;

public interface Shape {
    public boolean intersect(Shape var1);

    public boolean contains(double var1, double var3);

    public boolean contains(Shape var1);

    public BBox getBounds();
}

