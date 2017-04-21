/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.airplan.prototype;

public enum FlightWeightType {
    COST("Cost", "Cost"),
    DISTANCE("Distance", "Distance"),
    TIME("Time", "Time"),
    CREW_MEMBERS("Crew", "Number of Crew Members"),
    WEIGHT("Weight", "Weight Capacity"),
    PASSENGERS("Passengers", "Number of Passengers");
    
    private final String description;
    private final String identifier;

    private FlightWeightType(String identifier, String description) {
        this.identifier = identifier;
        this.description = description;
    }

    public static FlightWeightType fromString(String identifier) {
        if (identifier != null && !identifier.isEmpty()) {
            FlightWeightType[] values = FlightWeightType.values();
            for (int b = 0; b < values.length; ++b) {
                FlightWeightType weightType = values[b];
                if (!identifier.equalsIgnoreCase(weightType.identifier)) continue;
                return weightType;
            }
        }
        return null;
    }

    public String getDescription() {
        return this.description;
    }

    public String toString() {
        return this.identifier;
    }
}

