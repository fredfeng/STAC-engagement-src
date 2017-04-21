/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.slf4j.implementation;

import com.networkapex.slf4j.ILoggerFactory;
import com.networkapex.slf4j.Logger;
import com.networkapex.slf4j.implementation.SimpleLogger;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SimpleLoggerFactory
implements ILoggerFactory {
    ConcurrentMap<String, Logger> loggerMap = new ConcurrentHashMap<String, Logger>();

    public SimpleLoggerFactory() {
        SimpleLogger.init();
    }

    @Override
    public Logger fetchLogger(String name) {
        Logger simpleLogger = this.loggerMap.get(name);
        if (simpleLogger != null) {
            return simpleLogger;
        }
        return new SimpleLoggerFactoryExecutor(name).invoke();
    }

    void reset() {
        this.loggerMap.clear();
    }

    private class SimpleLoggerFactoryExecutor {
        private String name;

        public SimpleLoggerFactoryExecutor(String name) {
            this.name = name;
        }

        public Logger invoke() {
            SimpleLogger newInstance = new SimpleLogger(this.name);
            Logger oldInstance = SimpleLoggerFactory.this.loggerMap.putIfAbsent(this.name, newInstance);
            return oldInstance == null ? newInstance : oldInstance;
        }
    }

}

