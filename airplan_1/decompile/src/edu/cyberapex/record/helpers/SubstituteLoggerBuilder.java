/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.record.helpers;

import edu.cyberapex.record.event.SubstituteLoggingEvent;
import edu.cyberapex.record.helpers.SubstituteLogger;
import java.util.List;

public class SubstituteLoggerBuilder {
    private String name;
    private List<SubstituteLoggingEvent> eventList;

    public SubstituteLoggerBuilder defineName(String name) {
        this.name = name;
        return this;
    }

    public SubstituteLoggerBuilder defineEventList(List<SubstituteLoggingEvent> eventList) {
        this.eventList = eventList;
        return this;
    }

    public SubstituteLogger generateSubstituteLogger() {
        return new SubstituteLogger(this.name, this.eventList);
    }
}

