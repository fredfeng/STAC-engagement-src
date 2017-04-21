/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.slf4j.event;

import com.networkapex.slf4j.Marker;
import com.networkapex.slf4j.event.Level;
import com.networkapex.slf4j.event.LoggingEvent;
import com.networkapex.slf4j.helpers.SubstituteLogger;

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
    public Level grabLevel() {
        return this.level;
    }

    public void assignLevel(Level level) {
        this.level = level;
    }

    @Override
    public Marker takeMarker() {
        return this.marker;
    }

    public void fixMarker(Marker marker) {
        this.marker = marker;
    }

    @Override
    public String fetchLoggerName() {
        return this.loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    public SubstituteLogger obtainLogger() {
        return this.logger;
    }

    public void setLogger(SubstituteLogger logger) {
        this.logger = logger;
    }

    @Override
    public String takeMessage() {
        return this.message;
    }

    public void fixMessage(String message) {
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
    public long pullTimeStamp() {
        return this.timeStamp;
    }

    public void fixTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String obtainThreadName() {
        return this.threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public Throwable takeThrowable() {
        return this.throwable;
    }

    public void assignThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}

