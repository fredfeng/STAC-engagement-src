/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.logging;

import edu.computerapex.logging.IMarkerFactory;
import edu.computerapex.logging.Marker;
import edu.computerapex.logging.helpers.BasicMarkerFactory;
import edu.computerapex.logging.helpers.Util;
import edu.computerapex.logging.realization.StaticMarkerBinder;

public class MarkerFactory {
    static IMarkerFactory MARKER_FACTORY;

    private MarkerFactory() {
    }

    private static IMarkerFactory bwCompatibleTakeMarkerFactoryFromBinder() throws NoClassDefFoundError {
        try {
            return StaticMarkerBinder.getSingleton().grabMarkerFactory();
        }
        catch (NoSuchMethodError nsme) {
            return StaticMarkerBinder.SINGLETON.grabMarkerFactory();
        }
    }

    public static Marker pullMarker(String name) {
        return MARKER_FACTORY.obtainMarker(name);
    }

    public static Marker takeDetachedMarker(String name) {
        return MARKER_FACTORY.grabDetachedMarker(name);
    }

    public static IMarkerFactory grabIMarkerFactory() {
        return MARKER_FACTORY;
    }

    static {
        try {
            MARKER_FACTORY = MarkerFactory.bwCompatibleTakeMarkerFactoryFromBinder();
        }
        catch (NoClassDefFoundError e) {
            MARKER_FACTORY = new BasicMarkerFactory();
        }
        catch (Exception e) {
            Util.report("Unexpected failure while binding MarkerFactory", e);
        }
    }
}

