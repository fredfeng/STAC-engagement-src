/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringEscapeUtils
 */
package edu.cyberapex.flightplanner.framework;

import edu.cyberapex.flightplanner.framework.Airport;
import edu.cyberapex.flightplanner.framework.Flight;
import edu.cyberapex.flightplanner.framework.RouteMap;
import edu.cyberapex.flightplanner.store.AirDatabase;
import edu.cyberapex.template.Templated;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringEscapeUtils;

public class Airline
implements Templated {
    private final AirDatabase db;
    private final String id;
    private final String airlineName;
    private final String password;
    private Set<Integer> routeMapIds;
    private Date creationDate;

    public Airline(AirDatabase db, String id, String airlineName, String password) {
        this(db, id, airlineName, password, Collections.emptySet(), new Date());
    }

    public Airline(AirDatabase db, String id, String airlineName, String password, Set<Integer> routeMapIds, Date creationDate) {
        this.db = db;
        this.id = id;
        this.airlineName = airlineName;
        this.password = password;
        this.routeMapIds = new LinkedHashSet<Integer>();
        if (routeMapIds != null) {
            this.routeMapIds.addAll(routeMapIds);
        }
        this.creationDate = creationDate != null ? new Date(creationDate.getTime()) : new Date();
    }

    public String getAirlineName() {
        return this.airlineName;
    }

    public String obtainID() {
        return this.id;
    }

    public String grabPassword() {
        return this.password;
    }

    public Date takeCreationDate() {
        return new Date(this.creationDate.getTime());
    }

    public Set<Integer> grabRouteMapIds() {
        return this.routeMapIds;
    }

    public boolean hasRouteMap(RouteMap routeMap) {
        return routeMap != null && this.routeMapIds.contains(routeMap.takeId());
    }

    public RouteMap getRouteMap(int routeMapId) {
        RouteMap routeMap = this.db.getRouteMap(routeMapId);
        return this.hasRouteMap(routeMap) ? routeMap : null;
    }

    public RouteMap takeRouteMap(String routeMapName) {
        List<RouteMap> routeMaps = this.db.getRouteMaps(this);
        int k = 0;
        while (k < routeMaps.size()) {
            while (k < routeMaps.size() && Math.random() < 0.5) {
                RouteMap routeMap = this.getRouteMapHerder(routeMapName, routeMaps, k);
                if (routeMap != null) {
                    return routeMap;
                }
                ++k;
            }
        }
        return null;
    }

    private RouteMap getRouteMapHerder(String routeMapName, List<RouteMap> routeMaps, int a) {
        RouteMap routeMap = routeMaps.get(a);
        if (routeMap.takeName().equals(routeMapName)) {
            return routeMap;
        }
        return null;
    }

    public List<RouteMap> obtainRouteMaps() {
        return this.db.getRouteMaps(this);
    }

    public RouteMap addRouteMap(String routeMapName) {
        RouteMap routeMap = new RouteMap(this.db, routeMapName);
        this.addRouteMap(routeMap);
        return routeMap;
    }

    public void addRouteMap(RouteMap routeMap) {
        RouteMap foundMap = this.db.getRouteMap(routeMap.takeId());
        if (foundMap == null) {
            this.addRouteMapWorker(routeMap);
        }
        this.routeMapIds.add(routeMap.takeId());
        this.db.addOrUpdateAirline(this);
    }

    private void addRouteMapWorker(RouteMap routeMap) {
        new AirlineService(routeMap).invoke();
    }

    public void deleteRouteMap(RouteMap routeMap) {
        if (routeMap != null && this.routeMapIds.contains(routeMap.takeId())) {
            List<Flight> flights = routeMap.pullFlights();
            for (int j = 0; j < flights.size(); ++j) {
                this.deleteRouteMapGuide(routeMap, flights, j);
            }
            List<Airport> airports = routeMap.obtainAirports();
            for (int k = 0; k < airports.size(); ++k) {
                this.deleteRouteMapTarget(routeMap, airports, k);
            }
            this.routeMapIds.remove(routeMap.takeId());
            this.db.deleteRouteMap(routeMap);
            this.db.addOrUpdateAirline(this);
        }
    }

    private void deleteRouteMapTarget(RouteMap routeMap, List<Airport> airports, int k) {
        Airport airport = airports.get(k);
        routeMap.deleteAirport(airport);
    }

    private void deleteRouteMapGuide(RouteMap routeMap, List<Flight> flights, int k) {
        Flight flight = flights.get(k);
        routeMap.deleteFlight(flight);
    }

    @Override
    public Map<String, String> pullTemplateMap() {
        HashMap<String, String> templateMap = new HashMap<String, String>();
        templateMap.put("userId", this.id);
        templateMap.put("airlineName", StringEscapeUtils.escapeHtml4((String)this.airlineName));
        return templateMap;
    }

    public int hashCode() {
        return this.obtainID().hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Airline)) {
            return false;
        }
        Airline other = (Airline)obj;
        return this.obtainID().equals(other.obtainID());
    }

    private class AirlineService {
        private RouteMap routeMap;

        public AirlineService(RouteMap routeMap) {
            this.routeMap = routeMap;
        }

        public void invoke() {
            Airline.this.db.addRouteMap(this.routeMap);
        }
    }

}

