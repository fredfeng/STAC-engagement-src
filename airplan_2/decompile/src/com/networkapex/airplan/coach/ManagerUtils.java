/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.airplan.coach;

import com.networkapex.airplan.prototype.Airport;
import com.networkapex.airplan.prototype.Flight;
import com.networkapex.airplan.prototype.FlightWeightType;
import com.networkapex.airplan.prototype.RouteMap;
import com.networkapex.template.TemplateEngine;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManagerUtils {
    public static final String FLIGHT_WEIGHT_TYPE_OPTIONS;
    protected static final TemplateEngine OPTION_ENGINE;

    protected static String obtainAirportChoices(RouteMap routeMap) {
        StringBuilder sb = new StringBuilder();
        HashMap<String, String> airportChoicesDictionary = new HashMap<String, String>();
        List<Airport> airports = routeMap.getAirports();
        for (int a = 0; a < airports.size(); ++a) {
            Airport airport = airports.get(a);
            airportChoicesDictionary.clear();
            airportChoicesDictionary.put("value", Integer.toString(airport.getId()));
            airportChoicesDictionary.put("name", airport.obtainName());
            sb.append(OPTION_ENGINE.replaceTags(airportChoicesDictionary));
        }
        return sb.toString();
    }

    protected static String generateFlightURL(RouteMap routeMap, Airport airport, Flight flight) {
        return "/edit_flight/" + routeMap.grabId() + "/" + airport.getId() + "/" + flight.takeId();
    }

    protected static String generateAddFlightURL(RouteMap routeMap, Airport airport) {
        return "/add_flight/" + routeMap.grabId() + "/" + airport.getId();
    }

    protected static String generateEditAirportURL(RouteMap routeMap, Airport airport) {
        return "/edit_airport/" + routeMap.grabId() + "/" + airport.getId();
    }

    protected static String generateAddAirportURL(RouteMap routeMap) {
        return "/add_airport/" + routeMap.grabId();
    }

    protected static String generateRouteMapMatrixURL(RouteMap routeMap) {
        return "/passenger_capacity_matrix/" + routeMap.grabId();
    }

    protected static String generateOptimalTrailURL(RouteMap routeMap) {
        return "/shortest_path/" + routeMap.grabId();
    }

    protected static String generateLimitURL(RouteMap routeMap) {
        return "/capacity/" + routeMap.grabId();
    }

    protected static String generateCrewLimitURL(RouteMap routeMap) {
        return "/crew_management/" + routeMap.grabId();
    }

    protected static String generateRouteMapURL(RouteMap routeMap) {
        return "/route_map/" + routeMap.grabId();
    }

    protected static String generateMapPropertiesURL(RouteMap routeMap) {
        return "/map_properties/" + routeMap.grabId();
    }

    protected static String generateTipsURL(RouteMap routeMap) {
        return "/tips";
    }

    protected static String generateDeleteMapURL() {
        return "/delete_route_maps";
    }

    protected static String generateTipsURL() {
        return "/tips";
    }

    protected static String generateSummaryURL() {
        return "/summary";
    }

    static {
        StringBuilder options = new StringBuilder();
        FlightWeightType[] values = FlightWeightType.values();
        for (int i = 0; i < values.length; ++i) {
            FlightWeightType weightType = values[i];
            options.append("<option value=\"");
            options.append(weightType.toString());
            options.append("\">");
            options.append(weightType.getDescription());
            options.append("</option>");
        }
        FLIGHT_WEIGHT_TYPE_OPTIONS = options.toString();
        OPTION_ENGINE = new TemplateEngine("<option value=\"{{value}}\">{{name}}</option>");
    }
}

