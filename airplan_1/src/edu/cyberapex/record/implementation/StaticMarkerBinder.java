/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.record.implementation;

import edu.cyberapex.record.IMarkerFactory;
import edu.cyberapex.record.helpers.BasicMarkerFactory;
import edu.cyberapex.record.instance.MarkerFactoryBinder;

public class StaticMarkerBinder
implements MarkerFactoryBinder {
    public static final StaticMarkerBinder SINGLETON = new StaticMarkerBinder();
    final IMarkerFactory markerFactory = new BasicMarkerFactory();

    private StaticMarkerBinder() {
    }

    public static StaticMarkerBinder pullSingleton() {
        return SINGLETON;
    }

    @Override
    public IMarkerFactory obtainMarkerFactory() {
        return this.markerFactory;
    }

    @Override
    public String obtainMarkerFactoryClassStr() {
        return BasicMarkerFactory.class.getName();
    }
}

