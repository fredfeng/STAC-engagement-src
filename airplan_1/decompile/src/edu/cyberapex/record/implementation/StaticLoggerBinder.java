/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.record.implementation;

import edu.cyberapex.record.ILoggerFactory;
import edu.cyberapex.record.implementation.SimpleLoggerFactory;
import edu.cyberapex.record.implementation.SimpleLoggerFactoryBuilder;
import edu.cyberapex.record.implementation.StaticLoggerBinderBuilder;
import edu.cyberapex.record.instance.LoggerFactoryBinder;

public class StaticLoggerBinder
implements LoggerFactoryBinder {
    private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinderBuilder().generateStaticLoggerBinder();
    public static String REQUESTED_API_VERSION = "1.6.99";
    private static final String loggerFactoryClassStr = SimpleLoggerFactory.class.getName();
    private final ILoggerFactory loggerFactory = new SimpleLoggerFactoryBuilder().generateSimpleLoggerFactory();

    public static final StaticLoggerBinder getSingleton() {
        return SINGLETON;
    }

    StaticLoggerBinder() {
    }

    @Override
    public ILoggerFactory fetchLoggerFactory() {
        return this.loggerFactory;
    }

    @Override
    public String pullLoggerFactoryClassStr() {
        return loggerFactoryClassStr;
    }
}

