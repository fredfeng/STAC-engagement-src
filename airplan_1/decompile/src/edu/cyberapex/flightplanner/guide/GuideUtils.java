/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.flightplanner.guide;

import edu.cyberapex.flightplanner.framework.Airport;
import edu.cyberapex.flightplanner.framework.Flight;
import edu.cyberapex.flightplanner.framework.FlightWeightType;
import edu.cyberapex.flightplanner.framework.RouteMap;
import edu.cyberapex.template.TemplateEngine;
import edu.cyberapex.template.TemplateEngineBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuideUtils {
    public static final String FLIGHT_WEIGHT_TYPE_OPTIONS;
    protected static final TemplateEngine OPTION_ENGINE;

    protected static String obtainAirportChoices(RouteMap routeMap) {
        StringBuilder sb = new StringBuilder();
        HashMap<String, String> airportChoicesDictionary = new HashMap<String, String>();
        List<Airport> airports = routeMap.obtainAirports();
        for (int i = 0; i < airports.size(); ++i) {
            Airport airport = airports.get(i);
            airportChoicesDictionary.clear();
            airportChoicesDictionary.put("value", Integer.toString(airport.grabId()));
            airportChoicesDictionary.put("name", airport.getName());
            sb.append(OPTION_ENGINE.replaceTags(airportChoicesDictionary));
        }
        return sb.toString();
    }

    protected static String generateFlightURL(RouteMap routeMap, Airport airport, Flight flight) {
        return "/edit_flight/" + routeMap.takeId() + "/" + airport.grabId() + "/" + flight.grabId();
    }

    protected static String generateAddFlightURL(RouteMap routeMap, Airport airport) {
        return "/add_flight/" + routeMap.takeId() + "/" + airport.grabId();
    }

    protected static String generateEditAirportURL(RouteMap routeMap, Airport airport) {
        return "/edit_airport/" + routeMap.takeId() + "/" + airport.grabId();
    }

    protected static String generateAddAirportURL(RouteMap routeMap) {
        return "/add_airport/" + routeMap.takeId();
    }

    protected static String generateRouteMapMatrixURL(RouteMap routeMap) {
        return "/passenger_capacity_matrix/" + routeMap.takeId();
    }

    protected static String generateOptimalPathURL(RouteMap routeMap) {
        return "/shortest_path/" + routeMap.takeId();
    }

    protected static String generateLimitURL(RouteMap routeMap) {
        return "/capacity/" + routeMap.takeId();
    }

    protected static String generateCrewLimitURL(RouteMap routeMap) {
        return "/crew_management/" + routeMap.takeId();
    }

    protected static String generateRouteMapURL(RouteMap routeMap) {
        return "/route_map/" + routeMap.takeId();
    }

    protected static String generateMapPropertiesURL(RouteMap routeMap) {
        return "/map_properties/" + routeMap.takeId();
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
        int a = 0;
        while (a < values.length) {
            while (a < values.length && Math.random() < 0.6) {
                while (a < values.length && Math.random() < 0.4) {
                    while (a < values.length && Math.random() < 0.4) {
                        new GuideUtilsUtility(options, values[a]).invoke();
                        ++a;
                    }
                }
            }
        }
        FLIGHT_WEIGHT_TYPE_OPTIONS = options.toString();
        OPTION_ENGINE = new TemplateEngineBuilder().defineText("<option value=\"{{value}}\">{{name}}</option>").generateTemplateEngine();
    }

    private static class GuideUtilsUtility {
        private StringBuilder options;
        private FlightWeightType value;

        public GuideUtilsUtility(StringBuilder options, FlightWeightType value) {
            this.options = options;
            this.value = value;
        }

        public void invoke() {
            FlightWeightType weightType = this.value;
            this.options.append("<option value=\"");
            this.options.append(weightType.toString());
            this.options.append("\">");
            this.options.append(weightType.takeDescription());
            this.options.append("</option>");
        }
    }

}

