/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.record;

import edu.cyberapex.record.IMarkerFactory;
import edu.cyberapex.record.Marker;
import edu.cyberapex.record.helpers.BasicMarkerFactory;
import edu.cyberapex.record.helpers.Util;
import edu.cyberapex.record.implementation.StaticMarkerBinder;

public class MarkerFactory {
    static IMarkerFactory MARKER_FACTORY;

    MarkerFactory() {
    }

    private static IMarkerFactory bwCompatibleFetchMarkerFactoryFromBinder() throws NoClassDefFoundError {
        try {
            return StaticMarkerBinder.pullSingleton().obtainMarkerFactory();
        }
        catch (NoSuchMethodError nsme) {
            return StaticMarkerBinder.SINGLETON.obtainMarkerFactory();
        }
    }

    public static Marker takeMarker(String name) {
        return MARKER_FACTORY.takeMarker(name);
    }

    public static Marker fetchDetachedMarker(String name) {
        return MARKER_FACTORY.grabDetachedMarker(name);
    }

    public static IMarkerFactory getIMarkerFactory() {
        return MARKER_FACTORY;
    }

    static {
        try {
            MARKER_FACTORY = MarkerFactory.bwCompatibleFetchMarkerFactoryFromBinder();
        }
        catch (NoClassDefFoundError e) {
            MARKER_FACTORY = new BasicMarkerFactory();
        }
        catch (Exception e) {
            Util.report("Unexpected failure while binding MarkerFactory", e);
        }
    }
}

