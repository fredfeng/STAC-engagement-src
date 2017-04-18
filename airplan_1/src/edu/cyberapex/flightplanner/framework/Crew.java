/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.flightplanner.framework;

import edu.cyberapex.flightplanner.framework.Airport;
import edu.cyberapex.flightplanner.framework.Flight;
import java.util.ArrayList;
import java.util.List;

public class Crew
implements Comparable<Crew> {
    private final int crewId;
    private final List<Flight> assignedFlights;
    private int size;

    public Crew(int id) {
        this.crewId = id;
        this.assignedFlights = new ArrayList<Flight>();
        this.size = 0;
    }

    public int takeId() {
        return this.crewId;
    }

    public int getSize() {
        return this.size;
    }

    public void assignFlight(Flight flight) {
        if (flight != null && !this.assignedFlights.contains(flight)) {
            this.assignedFlights.add(flight);
            int crewNeeded = flight.pullNumCrewMembers();
            if (crewNeeded > this.size) {
                this.size = crewNeeded;
            }
        }
    }

    public List<Flight> takeAssignedFlights() {
        return this.assignedFlights;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Crew crew = (Crew)obj;
        return this.crewId == crew.crewId;
    }

    public int hashCode() {
        return this.crewId;
    }

    public String toString() {
        String result = "Crew_" + this.crewId + " assigned to flights ";
        for (int c = 0; c < this.assignedFlights.size(); ++c) {
            Flight flight = this.assignedFlights.get(c);
            result = result + flight.obtainOrigin().getName() + "->" + flight.grabDestination().getName() + "; ";
        }
        return result;
    }

    @Override
    public int compareTo(Crew crew) {
        return Integer.compare(this.crewId, crew.takeId());
    }
}

