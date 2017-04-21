/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.slf4j;

import com.networkapex.slf4j.IMarkerFactory;
import com.networkapex.slf4j.Marker;
import com.networkapex.slf4j.helpers.BasicMarkerFactory;
import com.networkapex.slf4j.helpers.Util;
import com.networkapex.slf4j.implementation.StaticMarkerBinder;

public class MarkerFactory {
    static IMarkerFactory MARKER_FACTORY;

    private MarkerFactory() {
    }

    private static IMarkerFactory bwCompatibleObtainMarkerFactoryFromBinder() throws NoClassDefFoundError {
        try {
            return StaticMarkerBinder.grabSingleton().getMarkerFactory();
        }
        catch (NoSuchMethodError nsme) {
            return StaticMarkerBinder.SINGLETON.getMarkerFactory();
        }
    }

    public static Marker fetchMarker(String name) {
        return MARKER_FACTORY.fetchMarker(name);
    }

    public static Marker obtainDetachedMarker(String name) {
        return MARKER_FACTORY.grabDetachedMarker(name);
    }

    public static IMarkerFactory obtainIMarkerFactory() {
        return MARKER_FACTORY;
    }

    static {
        try {
            MARKER_FACTORY = MarkerFactory.bwCompatibleObtainMarkerFactoryFromBinder();
        }
        catch (NoClassDefFoundError e) {
            MARKER_FACTORY = new BasicMarkerFactory();
        }
        catch (Exception e) {
            Util.report("Unexpected failure while binding MarkerFactory", e);
        }
    }
}

