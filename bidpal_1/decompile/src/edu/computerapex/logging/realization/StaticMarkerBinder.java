/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.logging.realization;

import edu.computerapex.logging.IMarkerFactory;
import edu.computerapex.logging.helpers.BasicMarkerFactory;
import edu.computerapex.logging.provider.MarkerFactoryBinder;
import edu.computerapex.logging.realization.StaticMarkerBinderBuilder;

public class StaticMarkerBinder
implements MarkerFactoryBinder {
    public static final StaticMarkerBinder SINGLETON = new StaticMarkerBinderBuilder().generateStaticMarkerBinder();
    final IMarkerFactory markerFactory = new BasicMarkerFactory();

    StaticMarkerBinder() {
    }

    public static StaticMarkerBinder getSingleton() {
        return SINGLETON;
    }

    @Override
    public IMarkerFactory grabMarkerFactory() {
        return this.markerFactory;
    }

    @Override
    public String pullMarkerFactoryClassStr() {
        return BasicMarkerFactory.class.getName();
    }
}

