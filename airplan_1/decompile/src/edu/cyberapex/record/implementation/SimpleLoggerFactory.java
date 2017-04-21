/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.record.implementation;

import edu.cyberapex.record.ILoggerFactory;
import edu.cyberapex.record.Logger;
import edu.cyberapex.record.implementation.SimpleLogger;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SimpleLoggerFactory
implements ILoggerFactory {
    ConcurrentMap<String, Logger> loggerMap = new ConcurrentHashMap<String, Logger>();

    public SimpleLoggerFactory() {
        SimpleLogger.init();
    }

    @Override
    public Logger getLogger(String name) {
        Logger simpleLogger = this.loggerMap.get(name);
        if (simpleLogger != null) {
            return simpleLogger;
        }
        return this.pullLoggerEngine(name);
    }

    private Logger pullLoggerEngine(String name) {
        SimpleLogger newInstance = new SimpleLogger(name);
        Logger oldInstance = this.loggerMap.putIfAbsent(name, newInstance);
        return oldInstance == null ? newInstance : oldInstance;
    }

    void reset() {
        this.loggerMap.clear();
    }
}

