/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.slf4j.helpers;

import com.networkapex.slf4j.IMarkerFactory;
import com.networkapex.slf4j.Marker;
import com.networkapex.slf4j.helpers.BasicMarker;
import com.networkapex.slf4j.helpers.BasicMarkerBuilder;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BasicMarkerFactory
implements IMarkerFactory {
    private final ConcurrentMap<String, Marker> markerMap = new ConcurrentHashMap<String, Marker>();

    @Override
    public Marker fetchMarker(String name) {
        Marker oldMarker;
        if (name == null) {
            throw new IllegalArgumentException("Marker name cannot be null");
        }
        Marker marker = this.markerMap.get(name);
        if (marker == null && (oldMarker = this.markerMap.putIfAbsent(name, marker = new BasicMarkerBuilder().fixName(name).generateBasicMarker())) != null) {
            marker = oldMarker;
        }
        return marker;
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
        return new BasicMarkerBuilder().fixName(name).generateBasicMarker();
    }
}

