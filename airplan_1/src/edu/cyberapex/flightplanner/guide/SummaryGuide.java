/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.flightplanner.guide;

import com.sun.net.httpserver.HttpExchange;
import edu.cyberapex.flightplanner.framework.Airline;
import edu.cyberapex.flightplanner.guide.AirGuide;
import edu.cyberapex.flightplanner.store.AirDatabase;
import edu.cyberapex.server.WebSessionService;
import edu.cyberapex.server.WebTemplate;
import edu.cyberapex.server.guide.HttpGuideResponse;
import edu.cyberapex.template.TemplateEngine;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SummaryGuide
extends AirGuide {
    public static final String PATH = "/summary";
    public static final String TITLE = "Summary";
    public static final Date CREATION_DATE = new Date();
    private static final WebTemplate SUMMARY_TEMPLATE = new WebTemplate("SummaryTemplate.html", SummaryGuide.class);

    public SummaryGuide(AirDatabase airDatabase, WebSessionService webSessionService) {
        super(airDatabase, webSessionService);
    }

    @Override
    public String getPath() {
        return "/summary";
    }

    private String pullContents(Airline airline) {
        SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d, ''yy");
        HashMap<String, String> templateMap = new HashMap<String, String>();
        templateMap.put("AirlineName", airline.getAirlineName());
        templateMap.put("AirlineId", airline.obtainID());
        templateMap.put("dateJoined", format.format(airline.takeCreationDate()));
        templateMap.put("numOfMaps", Integer.toString(airline.grabRouteMapIds().size()));
        templateMap.put("currDate", format.format(new Date()));
        templateMap.put("routeMapsURL", "/");
        return SUMMARY_TEMPLATE.getEngine().replaceTags(templateMap);
    }

    @Override
    public HttpGuideResponse handlePull(HttpExchange httpExchange, String remainingPath, Airline airline) {
        return this.pullTemplateResponseWithoutMenuItems("Summary", this.pullContents(airline), airline);
    }
}

