/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.slf4j.helpers;

import com.networkapex.slf4j.helpers.BasicMarker;

public class BasicMarkerBuilder {
    private String name;

    public BasicMarkerBuilder fixName(String name) {
        this.name = name;
        return this;
    }

    public BasicMarker generateBasicMarker() {
        return new BasicMarker(this.name);
    }
}

