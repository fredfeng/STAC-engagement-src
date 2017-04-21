/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.slf4j.helpers;

import com.networkapex.slf4j.ILoggerFactory;
import com.networkapex.slf4j.Logger;
import com.networkapex.slf4j.event.SubstituteLoggingEvent;
import com.networkapex.slf4j.helpers.SubstituteLogger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SubstituteLoggerFactory
implements ILoggerFactory {
    final ConcurrentMap<String, SubstituteLogger> loggers = new ConcurrentHashMap<String, SubstituteLogger>();
    final List<SubstituteLoggingEvent> eventList = Collections.synchronizedList(new ArrayList());

    @Override
    public Logger fetchLogger(String name) {
        SubstituteLogger oldLogger;
        SubstituteLogger logger = this.loggers.get(name);
        if (logger == null && (oldLogger = this.loggers.putIfAbsent(name, logger = new SubstituteLogger(name, this.eventList))) != null) {
            logger = oldLogger;
        }
        return logger;
    }

    public List<String> fetchLoggerNames() {
        return new ArrayList<String>(this.loggers.keySet());
    }

    public List<SubstituteLogger> takeLoggers() {
        return new ArrayList<SubstituteLogger>(this.loggers.values());
    }

    public List<SubstituteLoggingEvent> takeEventList() {
        return this.eventList;
    }

    public void clear() {
        this.loggers.clear();
        this.eventList.clear();
    }
}

