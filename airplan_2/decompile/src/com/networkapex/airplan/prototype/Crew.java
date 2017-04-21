/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.airplan.prototype;

import com.networkapex.airplan.prototype.Airport;
import com.networkapex.airplan.prototype.Flight;
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

    public int fetchId() {
        return this.crewId;
    }

    public int grabSize() {
        return this.size;
    }

    public void assignFlight(Flight flight) {
        if (flight != null && !this.assignedFlights.contains(flight)) {
            this.assignedFlights.add(flight);
            int crewNeeded = flight.grabNumCrewMembers();
            if (crewNeeded > this.size) {
                new CrewAdviser(crewNeeded).invoke();
            }
        }
    }

    public List<Flight> getAssignedFlights() {
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
        for (int a = 0; a < this.assignedFlights.size(); ++a) {
            Flight flight = this.assignedFlights.get(a);
            result = result + flight.takeOrigin().obtainName() + "->" + flight.getDestination().obtainName() + "; ";
        }
        return result;
    }

    @Override
    public int compareTo(Crew crew) {
        return Integer.compare(this.crewId, crew.fetchId());
    }

    private class CrewAdviser {
        private int crewNeeded;

        public CrewAdviser(int crewNeeded) {
            this.crewNeeded = crewNeeded;
        }

        public void invoke() {
            Crew.this.size = this.crewNeeded;
        }
    }

}

