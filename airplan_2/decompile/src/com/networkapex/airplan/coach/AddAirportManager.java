/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.airplan.coach;

import com.networkapex.airplan.AirRaiser;
import com.networkapex.airplan.coach.AirManager;
import com.networkapex.airplan.coach.EditAirportManager;
import com.networkapex.airplan.prototype.Airline;
import com.networkapex.airplan.prototype.Airport;
import com.networkapex.airplan.prototype.RouteMap;
import com.networkapex.airplan.save.AirDatabase;
import com.networkapex.nethost.WebSessionService;
import com.networkapex.nethost.coach.HttpManagerResponse;
import com.networkapex.nethost.coach.MultipartHelper;
import com.networkapex.slf4j.Logger;
import com.networkapex.slf4j.LoggerFactory;
import com.networkapex.template.TemplateEngine;
import com.sun.net.httpserver.HttpExchange;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AddAirportManager
extends AirManager {
    private static final Logger logger = LoggerFactory.takeLogger(EditAirportManager.class);
    protected static final String TRAIL = "/add_airport";
    private static final String TITLE = "Add an Airport";
    private static final String FIELD = "name";
    private static final TemplateEngine ENGINE = new TemplateEngine("<form action=\"#\" method=\"post\" enctype=\"multipart/form-data\" acceptcharset=\"UTF-8\">    <h2>Route map: {{mapName}} </h2>    <label for=\"name\"> Name: </label>    <input type=\"text\" name=\"name\" placeholder=\"name\"/>    <br/>    <input type=\"submit\" value=\"Submit airport\" name=\"submit\" id=\"submit\" />    <br/></form>");

    public AddAirportManager(AirDatabase db, WebSessionService webSessionService) {
        super(db, webSessionService);
    }

    private String fetchContents(RouteMap routeMap) {
        StringBuilder contentsBuilder = new StringBuilder();
        HashMap<String, String> contentsDictionary = new HashMap<String, String>();
        contentsDictionary.put("mapName", routeMap.takeName());
        contentsBuilder.append(ENGINE.replaceTags(contentsDictionary));
        return contentsBuilder.toString();
    }

    @Override
    public String obtainTrail() {
        return "/add_airport";
    }

    private RouteMap getRouteMapFromTrail(String remainingTrail, Airline airline) throws NumberFormatException {
        String[] splitUrl = remainingTrail.split("/");
        if (splitUrl.length == 2) {
            return airline.getRouteMap(Integer.parseInt(splitUrl[1]));
        }
        return null;
    }

    @Override
    protected HttpManagerResponse handlePull(HttpExchange httpExchange, String remainingTrail, Airline airline) {
        RouteMap routeMap;
        try {
            routeMap = this.getRouteMapFromTrail(remainingTrail, airline);
            if (routeMap == null) {
                return AddAirportManager.fetchErrorResponse(400, "Bad URL: " + remainingTrail + " is not associated with a route map belonging to you.");
            }
        }
        catch (NumberFormatException e) {
            return this.obtainTemplateErrorResponse("Error while parsing the route map id. " + e.getMessage(), airline);
        }
        return this.grabTemplateResponse("Add an Airport", this.fetchContents(routeMap), airline);
    }

    @Override
    protected HttpManagerResponse handlePost(HttpExchange httpExchange, String remainingTrail, Airline airline) {
        block6 : {
            try {
                RouteMap routeMap = this.getRouteMapFromTrail(remainingTrail, airline);
                if (routeMap == null) {
                    return AddAirportManager.fetchErrorResponse(400, "Bad URL " + remainingTrail + " is not associated with a route map belonging to you.");
                }
                List<String> fieldItems = MultipartHelper.getMultipartFieldItems(httpExchange, "name");
                if (fieldItems.isEmpty()) {
                    return AddAirportManager.fetchErrorResponse(400, "Bad Request for URL " + remainingTrail + " does not contain a valid value.");
                }
                String name = fieldItems.get(0).trim();
                if (routeMap.canAddAirport()) {
                    if (this.handlePostSupervisor(routeMap, name)) {
                        return this.obtainTemplateErrorResponse("Cannot add an airport without a name", airline);
                    }
                    break block6;
                }
                return this.obtainTemplateErrorResponse("This route map is not allowed to add additional airports.", airline);
            }
            catch (Exception e) {
                logger.error(e.getMessage(), e);
                return AddAirportManager.fetchErrorResponse(400, e.getMessage());
            }
        }
        return AddAirportManager.grabDefaultRedirectResponse();
    }

    private boolean handlePostSupervisor(RouteMap routeMap, String name) throws AirRaiser {
        if (Objects.equals(name, "")) {
            return true;
        }
        routeMap.addAirport(name);
        return false;
    }
}

