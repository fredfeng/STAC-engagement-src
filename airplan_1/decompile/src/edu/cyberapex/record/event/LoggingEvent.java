/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.record.event;

import edu.cyberapex.record.Marker;
import edu.cyberapex.record.event.Level;

public interface LoggingEvent {
    public Level getLevel();

    public Marker grabMarker();

    public String obtainLoggerName();

    public String obtainMessage();

    public String fetchThreadName();

    public Object[] takeArgumentArray();

    public long grabTimeStamp();

    public Throwable fetchThrowable();
}

