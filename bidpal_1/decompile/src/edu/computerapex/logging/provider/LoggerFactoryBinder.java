/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.logging.provider;

import edu.computerapex.logging.ILoggerFactory;

public interface LoggerFactoryBinder {
    public ILoggerFactory fetchLoggerFactory();

    public String getLoggerFactoryClassStr();
}

