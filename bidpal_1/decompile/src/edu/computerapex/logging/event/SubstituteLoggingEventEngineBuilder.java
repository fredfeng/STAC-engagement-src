/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.logging.event;

import edu.computerapex.logging.event.SubstituteLoggingEvent;
import edu.computerapex.logging.event.SubstituteLoggingEventEngine;

public class SubstituteLoggingEventEngineBuilder {
    private SubstituteLoggingEvent substituteLoggingEvent;

    public SubstituteLoggingEventEngineBuilder defineSubstituteLoggingEvent(SubstituteLoggingEvent substituteLoggingEvent) {
        this.substituteLoggingEvent = substituteLoggingEvent;
        return this;
    }

    public SubstituteLoggingEventEngine generateSubstituteLoggingEventEngine() {
        return new SubstituteLoggingEventEngine(this.substituteLoggingEvent);
    }
}

