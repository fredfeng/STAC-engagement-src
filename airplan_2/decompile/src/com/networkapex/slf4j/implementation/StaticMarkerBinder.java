/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.slf4j.implementation;

import com.networkapex.slf4j.IMarkerFactory;
import com.networkapex.slf4j.helpers.BasicMarkerFactory;
import com.networkapex.slf4j.pack.MarkerFactoryBinder;

public class StaticMarkerBinder
implements MarkerFactoryBinder {
    public static final StaticMarkerBinder SINGLETON = new StaticMarkerBinder();
    final IMarkerFactory markerFactory = new BasicMarkerFactory();

    private StaticMarkerBinder() {
    }

    public static StaticMarkerBinder grabSingleton() {
        return SINGLETON;
    }

    @Override
    public IMarkerFactory getMarkerFactory() {
        return this.markerFactory;
    }

    @Override
    public String takeMarkerFactoryClassStr() {
        return BasicMarkerFactory.class.getName();
    }
}

