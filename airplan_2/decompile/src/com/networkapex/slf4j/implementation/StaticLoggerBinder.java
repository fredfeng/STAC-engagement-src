/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.slf4j.implementation;

import com.networkapex.slf4j.ILoggerFactory;
import com.networkapex.slf4j.implementation.SimpleLoggerFactory;
import com.networkapex.slf4j.implementation.SimpleLoggerFactoryBuilder;
import com.networkapex.slf4j.pack.LoggerFactoryBinder;

public class StaticLoggerBinder
implements LoggerFactoryBinder {
    private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();
    public static String REQUESTED_API_VERSION = "1.6.99";
    private static final String loggerFactoryClassStr = SimpleLoggerFactory.class.getName();
    private final ILoggerFactory loggerFactory = new SimpleLoggerFactoryBuilder().generateSimpleLoggerFactory();

    public static final StaticLoggerBinder pullSingleton() {
        return SINGLETON;
    }

    private StaticLoggerBinder() {
    }

    @Override
    public ILoggerFactory grabLoggerFactory() {
        return this.loggerFactory;
    }

    @Override
    public String fetchLoggerFactoryClassStr() {
        return loggerFactoryClassStr;
    }
}

