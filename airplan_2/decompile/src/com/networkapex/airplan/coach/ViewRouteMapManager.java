/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.airplan.coach;

import com.networkapex.airplan.coach.AirManager;
import com.networkapex.airplan.coach.ManagerUtils;
import com.networkapex.airplan.prototype.Airline;
import com.networkapex.airplan.prototype.Airport;
import com.networkapex.airplan.prototype.Flight;
import com.networkapex.airplan.prototype.RouteMap;
import com.networkapex.airplan.save.AirDatabase;
import com.networkapex.nethost.WebSessionService;
import com.networkapex.nethost.coach.HttpManagerResponse;
import com.networkapex.template.TemplateEngine;
import com.sun.net.httpserver.HttpExchange;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringEscapeUtils;

public class ViewRouteMapManager
extends AirManager {
    protected static final String TRAIL = "/route_map";
    private static final String TITLE = "Route Map";
    private static final TemplateEngine ROUTE_MAP_ENGINE = new TemplateEngine("<ul>\n<a href=\"{{addAirportURL}}\" style=\"text-decoration:none\"><input type=\"button\" value=\"Add an Airport\" name=\"submit\"></a><a href=\"{{routeMapMatrixURL}}\" style=\"text-decoration:none\"><input type=\"button\" value=\"Connecting Flights Matrix\" name=\"submit\"></a><a href=\"{{shortestPathURL}}\" style=\"text-decoration:none\"><input type=\"button\" value=\"Find a Shortest Path\" name=\"submit\"></a><a href=\"{{capacityURL}}\" style=\"text-decoration:none\"><input type=\"button\" value=\"Find a Capacity\" name=\"submit\"></a><a href=\"{{propertiesURL}}\" style=\"text-decoration:none\"><input type=\"button\" value=\"Find the Properties of this Map\" name=\"submit\"></a><a href=\"{{crewCapacityURL}}\" style=\"text-decoration:none\"><input type=\"button\" value=\"Find the Minimum Number of Crews\" name=\"submit\"></a> <br /><br />{{airports}}</ul>\n");
    private static final TemplateEngine AIRPORT_ENGINE = new TemplateEngine("<li> <a href=\"{{airportURL}}\"> {{airportName}} </a>\n<ul>\n<li>Outgoing Flights\n<ul>\n{{flights}}</ul>\n</li>\n</ul>\n</li>\n");
    private static final TemplateEngine FLIGHT_DATA_ENGINE = new TemplateEngine("<li> Destination: {{dest}} \n<ul><li> Distance: {{distance}} </li><li> Cost: {{cost}} </li><li> Travel Time: {{time}} </li><li> Number of Crew Members: {{crew}} </li><li> Weight Capacity: {{weight}} </li><li> Number of Passengers: {{passengers}} </li></ul></li>");

    public ViewRouteMapManager(AirDatabase db, WebSessionService webSessionService) {
        super(db, webSessionService);
    }

    private Map<String, String> generateAirportDictionary(RouteMap routeMap, Airport airport) {
        HashMap<String, String> airportDictionary = new HashMap<String, String>();
        airportDictionary.put("airportName", airport.obtainName());
        airportDictionary.put("airportURL", ManagerUtils.generateEditAirportURL(routeMap, airport));
        return airportDictionary;
    }

    private String generateFlightHTML(Airport airport) {
        List<Flight> flights = airport.grabOriginFlights();
        if (flights.isEmpty()) {
            return "(no outgoing flights)";
        }
        StringBuilder flightBuilder = new StringBuilder();
        for (int c = 0; c < flights.size(); ++c) {
            this.generateFlightHTMLManager(flights, flightBuilder, c);
        }
        return flightBuilder.toString();
    }

    private void generateFlightHTMLManager(List<Flight> flights, StringBuilder flightBuilder, int q) {
        Flight flight = flights.get(q);
        HashMap<String, String> flightDictionary = new HashMap<String, String>();
        flightDictionary.put("dest", flight.getDestination().obtainName());
        flightDictionary.put("distance", Integer.toString(flight.pullDistance()));
        flightDictionary.put("cost", Integer.toString(flight.grabFuelCosts()));
        flightDictionary.put("time", Integer.toString(flight.getTravelTime()));
        flightDictionary.put("crew", Integer.toString(flight.grabNumCrewMembers()));
        flightDictionary.put("weight", Integer.toString(flight.takeWeightLimit()));
        flightDictionary.put("passengers", Integer.toString(flight.pullPassengerLimit()));
        flightBuilder.append(FLIGHT_DATA_ENGINE.replaceTags(flightDictionary));
    }

    private String generateAirportHTML(RouteMap routeMap) {
        StringBuilder airportBuilder = new StringBuilder();
        List<Airport> airports = routeMap.getAirports();
        int a = 0;
        while (a < airports.size()) {
            while (a < airports.size() && Math.random() < 0.6) {
                this.generateAirportHTMLEngine(routeMap, airportBuilder, airports, a);
                ++a;
            }
        }
        return airportBuilder.toString();
    }

    private void generateAirportHTMLEngine(RouteMap routeMap, StringBuilder airportBuilder, List<Airport> airports, int i) {
        Airport airport = airports.get(i);
        Map<String, String> airportDictionary = this.generateAirportDictionary(routeMap, airport);
        String flightHTML = this.generateFlightHTML(airport);
        airportDictionary.put("flights", flightHTML);
        airportBuilder.append(AIRPORT_ENGINE.replaceTags(airportDictionary));
    }

    private String generateRouteMapHTML(RouteMap routeMap) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("routeMapName", StringEscapeUtils.escapeHtml4(routeMap.takeName()));
        map.put("airports", this.generateAirportHTML(routeMap));
        map.put("addAirportURL", ManagerUtils.generateAddAirportURL(routeMap));
        map.put("routeMapMatrixURL", ManagerUtils.generateRouteMapMatrixURL(routeMap));
        map.put("shortestPathURL", ManagerUtils.generateOptimalTrailURL(routeMap));
        map.put("capacityURL", ManagerUtils.generateLimitURL(routeMap));
        map.put("propertiesURL", ManagerUtils.generateMapPropertiesURL(routeMap));
        map.put("crewCapacityURL", ManagerUtils.generateCrewLimitURL(routeMap));
        return ROUTE_MAP_ENGINE.replaceTags(map);
    }

    @Override
    public String obtainTrail() {
        return "/route_map";
    }

    private RouteMap takeRouteMapFromTrail(Airline airline, String remainingTrail) throws NumberFormatException {
        String[] urlSplit = remainingTrail.split("/");
        if (urlSplit.length == 2) {
            return airline.getRouteMap(Integer.parseInt(urlSplit[1]));
        }
        return null;
    }

    @Override
    protected HttpManagerResponse handlePull(HttpExchange httpExchange, String remainingTrail, Airline person) {
        try {
            RouteMap routeMap = this.takeRouteMapFromTrail(person, remainingTrail);
            if (routeMap == null) {
                return ViewRouteMapManager.fetchErrorResponse(400, "Bad URL");
            }
            return this.grabTemplateResponse("Route Map", this.generateRouteMapHTML(routeMap), person);
        }
        catch (NumberFormatException e) {
            return this.obtainTemplateErrorResponse("Error while parsing the route map id. " + e.getMessage(), person);
        }
    }
}

