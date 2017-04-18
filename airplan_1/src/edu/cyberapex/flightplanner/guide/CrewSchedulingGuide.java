/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.flightplanner.guide;

import com.sun.net.httpserver.HttpExchange;
import edu.cyberapex.flightplanner.AirFailure;
import edu.cyberapex.flightplanner.ChartAgent;
import edu.cyberapex.flightplanner.framework.Airline;
import edu.cyberapex.flightplanner.framework.Airport;
import edu.cyberapex.flightplanner.framework.Crew;
import edu.cyberapex.flightplanner.framework.Flight;
import edu.cyberapex.flightplanner.framework.FlightWeightType;
import edu.cyberapex.flightplanner.framework.RouteMap;
import edu.cyberapex.flightplanner.guide.AirGuide;
import edu.cyberapex.flightplanner.store.AirDatabase;
import edu.cyberapex.server.WebSessionService;
import edu.cyberapex.server.guide.HttpGuideResponse;
import edu.cyberapex.template.TemplateEngine;
import edu.cyberapex.template.TemplateEngineBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrewSchedulingGuide
extends AirGuide {
    protected static final String PATH = "/crew_management";
    private static final String TITLE = "Find an Optimal Crew Assignment";
    private static final TemplateEngine CREW_ENGINE = new TemplateEngineBuilder().defineText("A crew of size {{crewsize}} is assigned to the following flight{{s_if_plural}}: <ul>\n {{flights}} </ul>\n").generateTemplateEngine();
    private static final TemplateEngine FLIGHT_ENGINE = new TemplateEngineBuilder().defineText("<li> Origin: {{origin}}, Destination: {{destination}} </li>").generateTemplateEngine();

    public CrewSchedulingGuide(AirDatabase airDatabase, WebSessionService sessionService) {
        super(airDatabase, sessionService);
    }

    @Override
    public String getPath() {
        return "/crew_management";
    }

    private RouteMap grabRouteMapFromPath(String remainingPath, Airline airline) {
        String[] parts = remainingPath.split("/");
        return airline.getRouteMap(Integer.parseInt(parts[1]));
    }

    private String fetchContent(List<Crew> crews) {
        StringBuilder contentBuilder = new StringBuilder();
        HashMap<String, String> contentsDictionary = new HashMap<String, String>();
        int k = 0;
        while (k < crews.size()) {
            while (k < crews.size() && Math.random() < 0.6) {
                Crew crew = crews.get(k);
                contentsDictionary.clear();
                contentsDictionary.put("crewsize", Integer.toString(crew.getSize()));
                contentsDictionary.put("s_if_plural", crew.takeAssignedFlights().isEmpty() ? "" : "s");
                contentsDictionary.put("flights", this.pullFlightsContent(crew));
                contentBuilder.append(CREW_ENGINE.replaceTags(contentsDictionary));
                ++k;
            }
        }
        return contentBuilder.toString();
    }

    private String pullFlightsContent(Crew crew) {
        StringBuilder flightsBuilder = new StringBuilder();
        HashMap<String, String> dictionary = new HashMap<String, String>();
        List<Flight> assignedFlights = crew.takeAssignedFlights();
        for (int i = 0; i < assignedFlights.size(); ++i) {
            this.getFlightsContentService(flightsBuilder, dictionary, assignedFlights, i);
        }
        return flightsBuilder.toString();
    }

    private void getFlightsContentService(StringBuilder flightsBuilder, Map<String, String> dictionary, List<Flight> assignedFlights, int a) {
        Flight flight = assignedFlights.get(a);
        dictionary.clear();
        dictionary.put("origin", flight.obtainOrigin().getName());
        dictionary.put("destination", flight.grabDestination().getName());
        flightsBuilder.append(FLIGHT_ENGINE.replaceTags(dictionary));
    }

    @Override
    protected HttpGuideResponse handlePull(HttpExchange httpExchange, String remainingPath, Airline airline) {
        RouteMap routeMap;
        try {
            routeMap = this.grabRouteMapFromPath(remainingPath, airline);
        }
        catch (NumberFormatException e) {
            return this.fetchTemplateErrorResponse("Error while parsing the route map id. " + e.getMessage(), airline);
        }
        if (routeMap == null) {
            return this.fetchTemplateErrorResponse("Unable to parse the URL for route map.", airline);
        }
        try {
            ChartAgent chartAgent = new ChartAgent(routeMap, FlightWeightType.CREW_MEMBERS);
            List<Crew> crews = chartAgent.takeCrewAssignments();
            return this.getTemplateResponse("Find an Optimal Crew Assignment", this.fetchContent(crews), airline);
        }
        catch (AirFailure e) {
            return this.fetchTemplateErrorResponse(e.getMessage(), airline);
        }
    }
}

