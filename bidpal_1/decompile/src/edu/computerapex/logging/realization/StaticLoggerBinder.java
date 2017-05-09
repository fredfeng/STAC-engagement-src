/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.logging.realization;

import edu.computerapex.logging.ILoggerFactory;
import edu.computerapex.logging.provider.LoggerFactoryBinder;
import edu.computerapex.logging.realization.SimpleLoggerFactory;
import edu.computerapex.logging.realization.SimpleLoggerFactoryBuilder;

public class StaticLoggerBinder
implements LoggerFactoryBinder {
    private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();
    public static String REQUESTED_API_VERSION = "1.6.99";
    private static final String loggerFactoryClassStr = SimpleLoggerFactory.class.getName();
    private final ILoggerFactory loggerFactory = new SimpleLoggerFactoryBuilder().generateSimpleLoggerFactory();

    public static final StaticLoggerBinder obtainSingleton() {
        return SINGLETON;
    }

    private StaticLoggerBinder() {
    }

    @Override
    public ILoggerFactory fetchLoggerFactory() {
        return this.loggerFactory;
    }

    @Override
    public String getLoggerFactoryClassStr() {
        return loggerFactoryClassStr;
    }
}

