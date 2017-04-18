/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.flightplanner.guide;

import com.sun.net.httpserver.HttpExchange;
import edu.cyberapex.flightplanner.framework.Airline;
import edu.cyberapex.flightplanner.guide.AirGuide;
import edu.cyberapex.flightplanner.guide.GuideUtils;
import edu.cyberapex.flightplanner.store.AirDatabase;
import edu.cyberapex.server.WebSessionService;
import edu.cyberapex.server.WebTemplate;
import edu.cyberapex.server.guide.HttpGuideResponse;
import edu.cyberapex.template.TemplateEngine;
import java.util.HashMap;
import java.util.Map;

public class TipGuide
extends AirGuide {
    protected static final String PATH = "/tips";
    protected static final String TITLE = "AirPlan Tips";
    private static final WebTemplate TIPS_TEMPLATE = new WebTemplate("TipsTemplate.html", TipGuide.class);

    public TipGuide(AirDatabase airDatabase, WebSessionService webSessionService) {
        super(airDatabase, webSessionService);
    }

    @Override
    public String getPath() {
        return "/tips";
    }

    private String grabContents() {
        HashMap<String, String> templateMap = new HashMap<String, String>();
        templateMap.put("deleteMapURL", GuideUtils.generateDeleteMapURL());
        templateMap.put("summaryURL", GuideUtils.generateSummaryURL());
        return TIPS_TEMPLATE.getEngine().replaceTags(templateMap);
    }

    @Override
    protected HttpGuideResponse handlePull(HttpExchange exchange, String remainingPath, Airline airline) {
        return this.pullTemplateResponseWithoutMenuItems("AirPlan Tips", this.grabContents(), airline);
    }
}

