/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.slf4j.event;

import com.networkapex.slf4j.Marker;
import com.networkapex.slf4j.event.Level;

public interface LoggingEvent {
    public Level grabLevel();

    public Marker takeMarker();

    public String fetchLoggerName();

    public String takeMessage();

    public String obtainThreadName();

    public Object[] takeArgumentArray();

    public long pullTimeStamp();

    public Throwable takeThrowable();
}

