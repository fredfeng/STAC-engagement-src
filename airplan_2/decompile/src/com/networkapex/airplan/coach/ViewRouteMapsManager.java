/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.airplan.coach;

import com.networkapex.airplan.coach.AirManager;
import com.networkapex.airplan.coach.ManagerUtils;
import com.networkapex.airplan.prototype.Airline;
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

public class ViewRouteMapsManager
extends AirManager {
    protected static final String TRAIL = "/";
    protected static final String TITLE = "Route Maps";
    private static final TemplateEngine ENGINE = new TemplateEngine("<ul>\n<li><a href=\"{{routeMapURL}}\"> {{routeMapName}} </a> </li>\n</ul>");

    public ViewRouteMapsManager(AirDatabase db, WebSessionService webSessionService) {
        super(db, webSessionService);
    }

    private String generateRouteMapHTML(RouteMap routeMap) {
        HashMap<String, String> routeMapDictionary = new HashMap<String, String>();
        routeMapDictionary.put("routeMapName", StringEscapeUtils.escapeHtml4(routeMap.takeName()));
        routeMapDictionary.put("routeMapURL", ManagerUtils.generateRouteMapURL(routeMap));
        return ENGINE.replaceTags(routeMapDictionary);
    }

    private String routeMapsAsUnorderedList(List<RouteMap> routeMaps) {
        StringBuilder builder = new StringBuilder();
        for (int c = 0; c < routeMaps.size(); ++c) {
            this.routeMapsAsUnorderedListGuide(routeMaps, builder, c);
        }
        return builder.toString();
    }

    private void routeMapsAsUnorderedListGuide(List<RouteMap> routeMaps, StringBuilder builder, int j) {
        RouteMap routeMap = routeMaps.get(j);
        builder.append(this.generateRouteMapHTML(routeMap));
    }

    @Override
    public String obtainTrail() {
        return "/";
    }

    @Override
    protected HttpManagerResponse handlePull(HttpExchange httpExchange, String remainingTrail, Airline airline) {
        if (!remainingTrail.equals("") && !remainingTrail.equals("/")) {
            return ViewRouteMapsManager.fetchErrorResponse(404, "Page not found.");
        }
        List<RouteMap> routeMaps = airline.pullRouteMaps();
        return this.grabTemplateResponse("Route Maps", this.routeMapsAsUnorderedList(routeMaps), airline);
    }
}

