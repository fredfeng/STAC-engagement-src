/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.flightplanner;

import edu.cyberapex.flightplanner.AirFailure;
import edu.cyberapex.flightplanner.framework.RouteMap;
import edu.cyberapex.flightplanner.store.AirDatabase;
import java.io.FileNotFoundException;
import java.util.List;

public interface RouteMapLoader {
    public RouteMap loadRouteMap(String var1, AirDatabase var2) throws FileNotFoundException, AirFailure;

    public List<String> takeExtensions();
}

