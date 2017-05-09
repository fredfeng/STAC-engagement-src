/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.logging.realization;

import edu.computerapex.logging.ILoggerFactory;
import edu.computerapex.logging.Logger;
import edu.computerapex.logging.realization.SimpleLogger;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SimpleLoggerFactory
implements ILoggerFactory {
    ConcurrentMap<String, Logger> loggerMap = new ConcurrentHashMap<String, Logger>();

    public SimpleLoggerFactory() {
        SimpleLogger.init();
    }

    @Override
    public Logger obtainLogger(String name) {
        Logger simpleLogger = this.loggerMap.get(name);
        if (simpleLogger != null) {
            return simpleLogger;
        }
        SimpleLogger newInstance = new SimpleLogger(name);
        Logger oldInstance = this.loggerMap.putIfAbsent(name, newInstance);
        return oldInstance == null ? newInstance : oldInstance;
    }

    void reset() {
        this.loggerMap.clear();
    }
}

