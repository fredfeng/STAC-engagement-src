/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringEscapeUtils
 */
package edu.cyberapex.flightplanner.guide;

import com.sun.net.httpserver.HttpExchange;
import edu.cyberapex.flightplanner.framework.Airline;
import edu.cyberapex.flightplanner.framework.RouteMap;
import edu.cyberapex.flightplanner.guide.AirGuide;
import edu.cyberapex.flightplanner.guide.GuideUtils;
import edu.cyberapex.flightplanner.store.AirDatabase;
import edu.cyberapex.server.WebSessionService;
import edu.cyberapex.server.guide.HttpGuideResponse;
import edu.cyberapex.template.TemplateEngine;
import edu.cyberapex.template.TemplateEngineBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringEscapeUtils;

public class ViewRouteMapsGuide
extends AirGuide {
    protected static final String PATH = "/";
    protected static final String TITLE = "Route Maps";
    private static final TemplateEngine ENGINE = new TemplateEngineBuilder().defineText("<ul>\n<li><a href=\"{{routeMapURL}}\"> {{routeMapName}} </a> </li>\n</ul>").generateTemplateEngine();

    public ViewRouteMapsGuide(AirDatabase db, WebSessionService webSessionService) {
        super(db, webSessionService);
    }

    private String generateRouteMapHTML(RouteMap routeMap) {
        HashMap<String, String> routeMapDictionary = new HashMap<String, String>();
        routeMapDictionary.put("routeMapName", StringEscapeUtils.escapeHtml4((String)routeMap.takeName()));
        routeMapDictionary.put("routeMapURL", GuideUtils.generateRouteMapURL(routeMap));
        return ENGINE.replaceTags(routeMapDictionary);
    }

    private String routeMapsAsUnorderedList(List<RouteMap> routeMaps) {
        StringBuilder builder = new StringBuilder();
        for (int a = 0; a < routeMaps.size(); ++a) {
            RouteMap routeMap = routeMaps.get(a);
            builder.append(this.generateRouteMapHTML(routeMap));
        }
        return builder.toString();
    }

    @Override
    public String getPath() {
        return "/";
    }

    @Override
    protected HttpGuideResponse handlePull(HttpExchange httpExchange, String remainingPath, Airline airline) {
        if (!remainingPath.equals("") && !remainingPath.equals("/")) {
            return ViewRouteMapsGuide.getErrorResponse(404, "Page not found.");
        }
        List<RouteMap> routeMaps = airline.obtainRouteMaps();
        return this.getTemplateResponse("Route Maps", this.routeMapsAsUnorderedList(routeMaps), airline);
    }
}

