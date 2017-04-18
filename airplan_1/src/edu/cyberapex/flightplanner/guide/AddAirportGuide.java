/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.flightplanner.guide;

import com.sun.net.httpserver.HttpExchange;
import edu.cyberapex.flightplanner.AirFailure;
import edu.cyberapex.flightplanner.framework.Airline;
import edu.cyberapex.flightplanner.framework.Airport;
import edu.cyberapex.flightplanner.framework.RouteMap;
import edu.cyberapex.flightplanner.guide.AirGuide;
import edu.cyberapex.flightplanner.guide.EditAirportGuide;
import edu.cyberapex.flightplanner.store.AirDatabase;
import edu.cyberapex.record.Logger;
import edu.cyberapex.record.LoggerFactory;
import edu.cyberapex.server.WebSessionService;
import edu.cyberapex.server.guide.HttpGuideResponse;
import edu.cyberapex.server.guide.MultipartHelper;
import edu.cyberapex.template.TemplateEngine;
import edu.cyberapex.template.TemplateEngineBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AddAirportGuide
extends AirGuide {
    private static final Logger logger = LoggerFactory.getLogger(EditAirportGuide.class);
    protected static final String PATH = "/add_airport";
    private static final String TITLE = "Add an Airport";
    private static final String FIELD = "name";
    private static final TemplateEngine ENGINE = new TemplateEngineBuilder().defineText("<form action=\"#\" method=\"post\" enctype=\"multipart/form-data\" acceptcharset=\"UTF-8\">    <h2>Route map: {{mapName}} </h2>    <label for=\"name\"> Name: </label>    <input type=\"text\" name=\"name\" placeholder=\"name\"/>    <br/>    <input type=\"submit\" value=\"Submit airport\" name=\"submit\" id=\"submit\" />    <br/></form>").generateTemplateEngine();

    public AddAirportGuide(AirDatabase db, WebSessionService webSessionService) {
        super(db, webSessionService);
    }

    private String grabContents(RouteMap routeMap) {
        StringBuilder contentsBuilder = new StringBuilder();
        HashMap<String, String> contentsDictionary = new HashMap<String, String>();
        contentsDictionary.put("mapName", routeMap.takeName());
        contentsBuilder.append(ENGINE.replaceTags(contentsDictionary));
        return contentsBuilder.toString();
    }

    @Override
    public String getPath() {
        return "/add_airport";
    }

    private RouteMap fetchRouteMapFromPath(String remainingPath, Airline airline) throws NumberFormatException {
        String[] splitUrl = remainingPath.split("/");
        if (splitUrl.length == 2) {
            return airline.getRouteMap(Integer.parseInt(splitUrl[1]));
        }
        return null;
    }

    @Override
    protected HttpGuideResponse handlePull(HttpExchange httpExchange, String remainingPath, Airline airline) {
        RouteMap routeMap;
        try {
            routeMap = this.fetchRouteMapFromPath(remainingPath, airline);
            if (routeMap == null) {
                return AddAirportGuide.getErrorResponse(400, "Bad URL: " + remainingPath + " is not associated with a route map belonging to you.");
            }
        }
        catch (NumberFormatException e) {
            return this.fetchTemplateErrorResponse("Error while parsing the route map id. " + e.getMessage(), airline);
        }
        return this.getTemplateResponse("Add an Airport", this.grabContents(routeMap), airline);
    }

    @Override
    protected HttpGuideResponse handlePost(HttpExchange httpExchange, String remainingPath, Airline airline) {
        block6 : {
            try {
                RouteMap routeMap = this.fetchRouteMapFromPath(remainingPath, airline);
                if (routeMap == null) {
                    return AddAirportGuide.getErrorResponse(400, "Bad URL " + remainingPath + " is not associated with a route map belonging to you.");
                }
                List<String> fieldItems = MultipartHelper.fetchMultipartFieldItems(httpExchange, "name");
                if (fieldItems.isEmpty()) {
                    return AddAirportGuide.getErrorResponse(400, "Bad Request for URL " + remainingPath + " does not contain a valid value.");
                }
                String name = fieldItems.get(0).trim();
                if (routeMap.canAddAirport()) {
                    if (new AddAirportGuideGateKeeper(routeMap, name).invoke()) {
                        return this.fetchTemplateErrorResponse("Cannot add an airport without a name", airline);
                    }
                    break block6;
                }
                return this.fetchTemplateErrorResponse("This route map is not allowed to add additional airports.", airline);
            }
            catch (Exception e) {
                logger.error(e.getMessage(), e);
                return AddAirportGuide.getErrorResponse(400, e.getMessage());
            }
        }
        return AddAirportGuide.getDefaultRedirectResponse();
    }

    private class AddAirportGuideGateKeeper {
        private boolean myResult;
        private RouteMap routeMap;
        private String name;

        public AddAirportGuideGateKeeper(RouteMap routeMap, String name) {
            this.routeMap = routeMap;
            this.name = name;
        }

        boolean is() {
            return this.myResult;
        }

        public boolean invoke() throws AirFailure {
            if (Objects.equals(this.name, "")) {
                return true;
            }
            this.invokeHerder();
            return false;
        }

        private void invokeHerder() throws AirFailure {
            this.routeMap.addAirport(this.name);
        }
    }

}

