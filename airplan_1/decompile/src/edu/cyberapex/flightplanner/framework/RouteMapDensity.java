/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.flightplanner.framework;

public enum RouteMapDensity {
    NOT_SO_DENSE("Not So Dense", 0.0, 0.25),
    MODERATELY_DENSE("Somewhat Dense", 0.25, 0.75),
    HIGHLY_DENSE("Highly Dense", 0.75, 1.01);
    
    private final String description;
    private double minDensity;
    private double maxDensity;

    private RouteMapDensity(String description, double minDensity, double maxDensity) {
        this.description = description;
        this.minDensity = minDensity;
        this.maxDensity = maxDensity;
    }

    public double pullMinDensity() {
        return this.minDensity;
    }

    public double fetchMaxDensity() {
        return this.maxDensity;
    }

    public String pullDescription() {
        return this.description;
    }
}

