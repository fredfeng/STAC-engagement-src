/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;

public interface GraphHopperAPI {
    public boolean load(String var1);

    public GHResponse route(GHRequest var1);
}

