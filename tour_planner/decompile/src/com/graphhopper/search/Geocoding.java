/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.search;

import com.graphhopper.util.shapes.GHPlace;
import java.util.List;

public interface Geocoding {
    public /* varargs */ List<GHPlace> names2places(GHPlace ... var1);
}

