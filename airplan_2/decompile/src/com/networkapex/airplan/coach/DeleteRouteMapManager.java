/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.airplan.coach;

import com.networkapex.airplan.coach.AirManager;
import com.networkapex.airplan.prototype.Airline;
import com.networkapex.airplan.prototype.RouteMap;
import com.networkapex.airplan.save.AirDatabase;
import com.networkapex.nethost.WebSessionService;
import com.networkapex.nethost.coach.HttpManagerResponse;
import com.networkapex.nethost.coach.MultipartHelper;
import com.networkapex.template.TemplateEngine;
import com.sun.net.httpserver.HttpExchange;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeleteRouteMapManager
extends AirManager {
    protected static final String TRAIL = "/delete_route_maps";
    protected static final String TITLE = "Delete Route Maps";
    private static final String FIELD = "routemap";
    private static final TemplateEngine ENGINE = new TemplateEngine("<form action=\"#\" method=\"post\" enctype=\"multipart/form-data\" acceptcharset=\"UTF-8\">     <ul style=\"list-style: none;\">     {{routeMapCheckboxes}}     </ul>     <input type=\"submit\" value=\"Delete Route Maps\"></form>");
    private static final TemplateEngine CHECKBOX_ENGINE = new TemplateEngine("<li>    <input type=\"checkbox\" name=\"routemap\" value=\"{{routeMapId}}\">{{routeMapName}}<br /></li>");

    public DeleteRouteMapManager(AirDatabase db, WebSessionService webSessionService) {
        super(db, webSessionService);
    }

    @Override
    public String obtainTrail() {
        return "/delete_route_maps";
    }

    private String pullContents(List<RouteMap> routeMaps) {
        StringBuilder checkboxBuilder = new StringBuilder();
        HashMap<String, String> checkBoxDictionary = new HashMap<String, String>();
        int i = 0;
        while (i < routeMaps.size()) {
            while (i < routeMaps.size() && Math.random() < 0.5) {
                new DeleteRouteMapManagerGuide(routeMaps, checkboxBuilder, checkBoxDictionary, i).invoke();
                ++i;
            }
        }
        return ENGINE.replaceTags(Collections.singletonMap("routeMapCheckboxes", checkboxBuilder.toString()));
    }

    @Override
    protected HttpManagerResponse handlePull(HttpExchange httpExchange, String remainingTrail, Airline airline) {
        return this.grabTemplateResponse("Delete Route Maps", this.pullContents(airline.pullRouteMaps()), airline);
    }

    @Override
    protected HttpManagerResponse handlePost(HttpExchange httpExchange, String remainingTrail, Airline airline) {
        List<String> fieldItems = MultipartHelper.getMultipartFieldItems(httpExchange, "routemap");
        List<RouteMap> routeMaps = airline.pullRouteMaps();
        for (int j = 0; j < routeMaps.size(); ++j) {
            this.handlePostAdviser(airline, fieldItems, routeMaps, j);
        }
        return DeleteRouteMapManager.grabDefaultRedirectResponse();
    }

    private void handlePostAdviser(Airline airline, List<String> fieldItems, List<RouteMap> routeMaps, int p) {
        RouteMap routeMap = routeMaps.get(p);
        String routeMapId = Integer.toString(routeMap.grabId());
        if (fieldItems.contains(routeMapId)) {
            this.handlePostAdviserHome(airline, routeMap);
        }
    }

    private void handlePostAdviserHome(Airline airline, RouteMap routeMap) {
        airline.deleteRouteMap(routeMap);
    }

    private class DeleteRouteMapManagerGuide {
        private List<RouteMap> routeMaps;
        private StringBuilder checkboxBuilder;
        private Map<String, String> checkBoxDictionary;
        private int a;

        public DeleteRouteMapManagerGuide(List<RouteMap> routeMaps, StringBuilder checkboxBuilder, Map<String, String> checkBoxDictionary, int a) {
            this.routeMaps = routeMaps;
            this.checkboxBuilder = checkboxBuilder;
            this.checkBoxDictionary = checkBoxDictionary;
            this.a = a;
        }

        public void invoke() {
            RouteMap routeMap = this.routeMaps.get(this.a);
            this.checkBoxDictionary.clear();
            this.checkBoxDictionary.put("routeMapId", Integer.toString(routeMap.grabId()));
            this.checkBoxDictionary.put("routeMapName", routeMap.takeName());
            this.checkboxBuilder.append(CHECKBOX_ENGINE.replaceTags(this.checkBoxDictionary));
        }
    }

}

