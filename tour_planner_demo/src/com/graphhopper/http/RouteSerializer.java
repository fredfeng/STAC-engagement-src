/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.http;

import com.graphhopper.GHResponse;
import com.graphhopper.util.PointList;
import java.util.Map;

public interface RouteSerializer {
    public Map<String, Object> toJSON(GHResponse var1, boolean var2, boolean var3, boolean var4, boolean var5);

    public Object createPoints(PointList var1, boolean var2, boolean var3);
}

