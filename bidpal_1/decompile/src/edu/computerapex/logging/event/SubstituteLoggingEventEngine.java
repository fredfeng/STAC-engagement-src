/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.logging.event;

import edu.computerapex.logging.event.SubstituteLoggingEvent;

public class SubstituteLoggingEventEngine {
    private final SubstituteLoggingEvent substituteLoggingEvent;

    public SubstituteLoggingEventEngine(SubstituteLoggingEvent substituteLoggingEvent) {
        this.substituteLoggingEvent = substituteLoggingEvent;
    }

    public Object[] grabArgumentArray() {
        return this.substituteLoggingEvent.obtainArgArray();
    }

    public long takeTimeStamp() {
        return this.substituteLoggingEvent.getTimeStamp();
    }

    public String pullThreadName() {
        return this.substituteLoggingEvent.fetchThreadName();
    }

    public Throwable pullThrowable() {
        return this.substituteLoggingEvent.takeThrowable();
    }
}

