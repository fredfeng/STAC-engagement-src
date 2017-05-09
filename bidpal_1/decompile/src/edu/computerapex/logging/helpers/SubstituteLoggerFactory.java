/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.logging.helpers;

import edu.computerapex.logging.ILoggerFactory;
import edu.computerapex.logging.Logger;
import edu.computerapex.logging.event.SubstituteLoggingEvent;
import edu.computerapex.logging.helpers.SubstituteLogger;
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
    public Logger obtainLogger(String name) {
        SubstituteLogger oldLogger;
        SubstituteLogger logger = this.loggers.get(name);
        if (logger == null && (oldLogger = this.loggers.putIfAbsent(name, logger = new SubstituteLogger(name, this.eventList))) != null) {
            logger = oldLogger;
        }
        return logger;
    }

    public List<String> pullLoggerNames() {
        return new ArrayList<String>(this.loggers.keySet());
    }

    public List<SubstituteLogger> getLoggers() {
        return new ArrayList<SubstituteLogger>(this.loggers.values());
    }

    public List<SubstituteLoggingEvent> fetchEventList() {
        return this.eventList;
    }

    public void clear() {
        this.loggers.clear();
        this.eventList.clear();
    }
}

