/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.search;

import com.graphhopper.util.shapes.GHPlace;
import java.util.List;

public interface ReverseGeocoding {
    public /* varargs */ List<GHPlace> places2names(GHPlace ... var1);
}

