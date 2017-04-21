/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.record.event;

import edu.cyberapex.record.Marker;
import edu.cyberapex.record.event.Level;
import edu.cyberapex.record.event.LoggingEvent;
import edu.cyberapex.record.helpers.SubstituteLogger;

public class SubstituteLoggingEvent
implements LoggingEvent {
    Level level;
    Marker marker;
    String loggerName;
    SubstituteLogger logger;
    String threadName;
    String message;
    Object[] argArray;
    long timeStamp;
    Throwable throwable;

    @Override
    public Level getLevel() {
        return this.level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    @Override
    public Marker grabMarker() {
        return this.marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    @Override
    public String obtainLoggerName() {
        return this.loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    public SubstituteLogger grabLogger() {
        return this.logger;
    }

    public void fixLogger(SubstituteLogger logger) {
        this.logger = logger;
    }

    @Override
    public String obtainMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Object[] takeArgumentArray() {
        return this.argArray;
    }

    public void fixArgumentArray(Object[] argArray) {
        this.argArray = argArray;
    }

    @Override
    public long grabTimeStamp() {
        return this.timeStamp;
    }

    public void fixTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String fetchThreadName() {
        return this.threadName;
    }

    public void defineThreadName(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public Throwable fetchThrowable() {
        return this.throwable;
    }

    public void assignThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}

