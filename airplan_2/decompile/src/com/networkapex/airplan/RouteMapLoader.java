/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.airplan;

import com.networkapex.airplan.AirRaiser;
import com.networkapex.airplan.prototype.RouteMap;
import com.networkapex.airplan.save.AirDatabase;
import java.io.FileNotFoundException;
import java.util.List;

public interface RouteMapLoader {
    public RouteMap loadRouteMap(String var1, AirDatabase var2) throws FileNotFoundException, AirRaiser;

    public List<String> takeExtensions();
}

