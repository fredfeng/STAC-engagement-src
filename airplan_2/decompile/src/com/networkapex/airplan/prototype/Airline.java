/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.airplan.prototype;

import com.networkapex.airplan.prototype.Airport;
import com.networkapex.airplan.prototype.Flight;
import com.networkapex.airplan.prototype.RouteMap;
import com.networkapex.airplan.save.AirDatabase;
import com.networkapex.template.Templated;
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
            this.AirlineEntity(routeMapIds);
        }
        this.creationDate = creationDate != null ? new Date(creationDate.getTime()) : new Date();
    }

    private void AirlineEntity(Set<Integer> routeMapIds) {
        this.routeMapIds.addAll(routeMapIds);
    }

    public String getAirlineName() {
        return this.airlineName;
    }

    public String pullID() {
        return this.id;
    }

    public String grabPassword() {
        return this.password;
    }

    public Date getCreationDate() {
        return new Date(this.creationDate.getTime());
    }

    public Set<Integer> takeRouteMapIds() {
        return this.routeMapIds;
    }

    public boolean hasRouteMap(RouteMap routeMap) {
        return routeMap != null && this.routeMapIds.contains(routeMap.grabId());
    }

    public RouteMap getRouteMap(int routeMapId) {
        RouteMap routeMap = this.db.fetchRouteMap(routeMapId);
        return this.hasRouteMap(routeMap) ? routeMap : null;
    }

    public RouteMap grabRouteMap(String routeMapName) {
        List<RouteMap> pullRouteMaps = this.db.pullRouteMaps(this);
        for (int i = 0; i < pullRouteMaps.size(); ++i) {
            RouteMap routeMap = this.grabRouteMapExecutor(routeMapName, pullRouteMaps, i);
            if (routeMap == null) continue;
            return routeMap;
        }
        return null;
    }

    private RouteMap grabRouteMapExecutor(String routeMapName, List<RouteMap> pullRouteMaps, int q) {
        RouteMap routeMap = pullRouteMaps.get(q);
        if (routeMap.takeName().equals(routeMapName)) {
            return routeMap;
        }
        return null;
    }

    public List<RouteMap> pullRouteMaps() {
        return this.db.pullRouteMaps(this);
    }

    public RouteMap addRouteMap(String routeMapName) {
        RouteMap routeMap = new RouteMap(this.db, routeMapName);
        this.addRouteMap(routeMap);
        return routeMap;
    }

    public void addRouteMap(RouteMap routeMap) {
        RouteMap foundMap = this.db.fetchRouteMap(routeMap.grabId());
        if (foundMap == null) {
            this.addRouteMapHerder(routeMap);
        }
        this.routeMapIds.add(routeMap.grabId());
        this.db.addOrUpdateAirline(this);
    }

    private void addRouteMapHerder(RouteMap routeMap) {
        this.db.addRouteMap(routeMap);
    }

    public void deleteRouteMap(RouteMap routeMap) {
        if (routeMap != null && this.routeMapIds.contains(routeMap.grabId())) {
            List<Flight> flights = routeMap.getFlights();
            for (int p = 0; p < flights.size(); ++p) {
                this.deleteRouteMapGateKeeper(routeMap, flights, p);
            }
            List<Airport> airports = routeMap.getAirports();
            for (int p = 0; p < airports.size(); ++p) {
                Airport airport = airports.get(p);
                routeMap.deleteAirport(airport);
            }
            this.routeMapIds.remove(routeMap.grabId());
            this.db.deleteRouteMap(routeMap);
            this.db.addOrUpdateAirline(this);
        }
    }

    private void deleteRouteMapGateKeeper(RouteMap routeMap, List<Flight> flights, int i) {
        Flight flight = flights.get(i);
        routeMap.deleteFlight(flight);
    }

    @Override
    public Map<String, String> pullTemplateMap() {
        HashMap<String, String> templateMap = new HashMap<String, String>();
        templateMap.put("userId", this.id);
        templateMap.put("airlineName", StringEscapeUtils.escapeHtml4(this.airlineName));
        return templateMap;
    }

    public int hashCode() {
        return this.pullID().hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Airline)) {
            return false;
        }
        Airline other = (Airline)obj;
        return this.pullID().equals(other.pullID());
    }
}

