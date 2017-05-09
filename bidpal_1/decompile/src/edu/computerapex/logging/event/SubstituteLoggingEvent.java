/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.logging.event;

import edu.computerapex.logging.Marker;
import edu.computerapex.logging.event.Level;
import edu.computerapex.logging.event.LoggingEvent;
import edu.computerapex.logging.event.SubstituteLoggingEventEngine;
import edu.computerapex.logging.event.SubstituteLoggingEventEngineBuilder;
import edu.computerapex.logging.helpers.SubstituteLogger;

public class SubstituteLoggingEvent
implements LoggingEvent {
    private final SubstituteLoggingEventEngine substituteLoggingEventEngine;
    Level level;
    Marker marker;
    String loggerName;
    SubstituteLogger logger;
    String threadName;
    String message;
    Object[] argArray;
    long timeStamp;
    Throwable throwable;

    public SubstituteLoggingEvent() {
        this.substituteLoggingEventEngine = new SubstituteLoggingEventEngineBuilder().defineSubstituteLoggingEvent(this).generateSubstituteLoggingEventEngine();
    }

    @Override
    public Level fetchLevel() {
        return this.level;
    }

    public void defineLevel(Level level) {
        this.level = level;
    }

    @Override
    public Marker fetchMarker() {
        return this.marker;
    }

    public void fixMarker(Marker marker) {
        this.marker = marker;
    }

    @Override
    public String getLoggerName() {
        return this.loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    public SubstituteLogger pullLogger() {
        return this.logger;
    }

    public void assignLogger(SubstituteLogger logger) {
        this.logger = logger;
    }

    @Override
    public String fetchMessage() {
        return this.message;
    }

    public void defineMessage(String message) {
        this.message = message;
    }

    @Override
    public Object[] grabArgumentArray() {
        return this.substituteLoggingEventEngine.grabArgumentArray();
    }

    public void assignArgumentArray(Object[] argArray) {
        this.argArray = argArray;
    }

    @Override
    public long takeTimeStamp() {
        return this.substituteLoggingEventEngine.takeTimeStamp();
    }

    public void fixTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String pullThreadName() {
        return this.substituteLoggingEventEngine.pullThreadName();
    }

    public void defineThreadName(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public Throwable pullThrowable() {
        return this.substituteLoggingEventEngine.pullThrowable();
    }

    public void assignThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public Object[] obtainArgArray() {
        return this.argArray;
    }

    public String fetchThreadName() {
        return this.threadName;
    }

    public Throwable takeThrowable() {
        return this.throwable;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }
}

