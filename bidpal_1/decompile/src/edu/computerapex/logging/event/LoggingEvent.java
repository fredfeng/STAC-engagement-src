/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.logging.event;

import edu.computerapex.logging.Marker;
import edu.computerapex.logging.event.Level;

public interface LoggingEvent {
    public Level fetchLevel();

    public Marker fetchMarker();

    public String getLoggerName();

    public String fetchMessage();

    public String pullThreadName();

    public Object[] grabArgumentArray();

    public long takeTimeStamp();

    public Throwable pullThrowable();
}

