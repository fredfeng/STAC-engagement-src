/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.flightplanner.framework;

public enum RouteMapSize {
    VERY_LARGE("Large", 300, Integer.MAX_VALUE),
    MODERATELY_LARGE("Medium", 100, 300),
    FAIRLY_SMALL("Small", 0, 100);
    
    private final String description;
    private int minSize;
    private int maxSize;

    private RouteMapSize(String description, int minSize, int maxSize) {
        this.description = description;
        this.minSize = minSize;
        this.maxSize = maxSize;
    }

    public int fetchMinimumSize() {
        return this.minSize;
    }

    public int grabMaximumSize() {
        return this.maxSize;
    }

    public String fetchDescription() {
        return this.description;
    }
}

