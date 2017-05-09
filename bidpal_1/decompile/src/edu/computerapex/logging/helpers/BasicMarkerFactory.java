/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.logging.helpers;

import edu.computerapex.logging.IMarkerFactory;
import edu.computerapex.logging.Marker;
import edu.computerapex.logging.helpers.BasicMarker;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BasicMarkerFactory
implements IMarkerFactory {
    private final ConcurrentMap<String, Marker> markerMap = new ConcurrentHashMap<String, Marker>();

    @Override
    public Marker obtainMarker(String name) {
        Marker oldMarker;
        if (name == null) {
            return this.obtainMarkerTarget();
        }
        Marker marker = this.markerMap.get(name);
        if (marker == null && (oldMarker = this.markerMap.putIfAbsent(name, marker = new BasicMarker(name))) != null) {
            marker = oldMarker;
        }
        return marker;
    }

    private Marker obtainMarkerTarget() {
        throw new IllegalArgumentException("Marker name cannot be null");
    }

    @Override
    public boolean exists(String name) {
        if (name == null) {
            return false;
        }
        return this.markerMap.containsKey(name);
    }

    @Override
    public boolean detachMarker(String name) {
        if (name == null) {
            return false;
        }
        return this.markerMap.remove(name) != null;
    }

    @Override
    public Marker grabDetachedMarker(String name) {
        return new BasicMarker(name);
    }
}

