/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.flightplanner.framework;

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
            int i = 0;
            while (i < values.length) {
                while (i < values.length && Math.random() < 0.6) {
                    while (i < values.length && Math.random() < 0.4) {
                        FlightWeightType weightType = values[i];
                        if (identifier.equalsIgnoreCase(weightType.identifier)) {
                            return weightType;
                        }
                        ++i;
                    }
                }
            }
        }
        return null;
    }

    public String takeDescription() {
        return this.description;
    }

    public String toString() {
        return this.identifier;
    }
}

