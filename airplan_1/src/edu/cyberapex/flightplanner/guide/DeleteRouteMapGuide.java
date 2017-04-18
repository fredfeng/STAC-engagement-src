/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.flightplanner.guide;

import com.sun.net.httpserver.HttpExchange;
import edu.cyberapex.flightplanner.framework.Airline;
import edu.cyberapex.flightplanner.framework.RouteMap;
import edu.cyberapex.flightplanner.guide.AirGuide;
import edu.cyberapex.flightplanner.store.AirDatabase;
import edu.cyberapex.server.WebSessionService;
import edu.cyberapex.server.guide.HttpGuideResponse;
import edu.cyberapex.server.guide.MultipartHelper;
import edu.cyberapex.template.TemplateEngine;
import edu.cyberapex.template.TemplateEngineBuilder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeleteRouteMapGuide
extends AirGuide {
    protected static final String PATH = "/delete_route_maps";
    protected static final String TITLE = "Delete Route Maps";
    private static final String FIELD = "routemap";
    private static final TemplateEngine ENGINE = new TemplateEngineBuilder().defineText("<form action=\"#\" method=\"post\" enctype=\"multipart/form-data\" acceptcharset=\"UTF-8\">     <ul style=\"list-style: none;\">     {{routeMapCheckboxes}}     </ul>     <input type=\"submit\" value=\"Delete Route Maps\"></form>").generateTemplateEngine();
    private static final TemplateEngine CHECKBOX_ENGINE = new TemplateEngineBuilder().defineText("<li>    <input type=\"checkbox\" name=\"routemap\" value=\"{{routeMapId}}\">{{routeMapName}}<br /></li>").generateTemplateEngine();

    public DeleteRouteMapGuide(AirDatabase db, WebSessionService webSessionService) {
        super(db, webSessionService);
    }

    @Override
    public String getPath() {
        return "/delete_route_maps";
    }

    private String getContents(List<RouteMap> routeMaps) {
        StringBuilder checkboxBuilder = new StringBuilder();
        HashMap<String, String> checkBoxDictionary = new HashMap<String, String>();
        for (int c = 0; c < routeMaps.size(); ++c) {
            RouteMap routeMap = routeMaps.get(c);
            checkBoxDictionary.clear();
            checkBoxDictionary.put("routeMapId", Integer.toString(routeMap.takeId()));
            checkBoxDictionary.put("routeMapName", routeMap.takeName());
            checkboxBuilder.append(CHECKBOX_ENGINE.replaceTags(checkBoxDictionary));
        }
        return ENGINE.replaceTags(Collections.singletonMap("routeMapCheckboxes", checkboxBuilder.toString()));
    }

    @Override
    protected HttpGuideResponse handlePull(HttpExchange httpExchange, String remainingPath, Airline airline) {
        return this.getTemplateResponse("Delete Route Maps", this.getContents(airline.obtainRouteMaps()), airline);
    }

    @Override
    protected HttpGuideResponse handlePost(HttpExchange httpExchange, String remainingPath, Airline airline) {
        List<String> fieldItems = MultipartHelper.fetchMultipartFieldItems(httpExchange, "routemap");
        List<RouteMap> routeMaps = airline.obtainRouteMaps();
        for (int c = 0; c < routeMaps.size(); ++c) {
            RouteMap routeMap = routeMaps.get(c);
            String routeMapId = Integer.toString(routeMap.takeId());
            if (!fieldItems.contains(routeMapId)) continue;
            airline.deleteRouteMap(routeMap);
        }
        return DeleteRouteMapGuide.getDefaultRedirectResponse();
    }
}

