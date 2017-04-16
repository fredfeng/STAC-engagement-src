/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.storage;

import com.graphhopper.util.PointAccess;

public interface NodeAccess
extends PointAccess {
    public int getAdditionalNodeField(int var1);

    public void setAdditionalNodeField(int var1, int var2);
}

