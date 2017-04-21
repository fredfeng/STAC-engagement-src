/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.airplan.prototype;

public enum RouteMapSize {
    VERY_LARGE("Large", 300, Integer.MAX_VALUE),
    MODERATELY_LARGE("Medium", 100, 300),
    FAIRLY_SMALL("Small", 0, 100);
    
    private final String description;
    private int leastSize;
    private int maxSize;

    private RouteMapSize(String description, int leastSize, int maxSize) {
        this.description = description;
        this.leastSize = leastSize;
        this.maxSize = maxSize;
    }

    public int fetchMinimumSize() {
        return this.leastSize;
    }

    public int grabMaximumSize() {
        return this.maxSize;
    }

    public String obtainDescription() {
        return this.description;
    }
}

