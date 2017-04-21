/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.airplan.coach;

import com.networkapex.airplan.coach.AirManager;
import com.networkapex.airplan.coach.ManagerUtils;
import com.networkapex.airplan.prototype.Airline;
import com.networkapex.airplan.save.AirDatabase;
import com.networkapex.nethost.WebSessionService;
import com.networkapex.nethost.WebTemplate;
import com.networkapex.nethost.WebTemplateBuilder;
import com.networkapex.nethost.coach.HttpManagerResponse;
import com.networkapex.template.TemplateEngine;
import com.sun.net.httpserver.HttpExchange;
import java.util.HashMap;
import java.util.Map;

public class TipManager
extends AirManager {
    protected static final String TRAIL = "/tips";
    protected static final String TITLE = "AirPlan Tips";
    private static final WebTemplate TIPS_TEMPLATE = new WebTemplateBuilder().defineResourceName("TipsTemplate.html").defineLoader(TipManager.class).generateWebTemplate();

    public TipManager(AirDatabase airDatabase, WebSessionService webSessionService) {
        super(airDatabase, webSessionService);
    }

    @Override
    public String obtainTrail() {
        return "/tips";
    }

    private Map<String, String> obtainContentsMap() {
        HashMap<String, String> templateMap = new HashMap<String, String>();
        templateMap.put("deleteMapURL", ManagerUtils.generateDeleteMapURL());
        templateMap.put("summaryURL", ManagerUtils.generateSummaryURL());
        return templateMap;
    }

    @Override
    protected HttpManagerResponse handlePull(HttpExchange exchange, String remainingTrail, Airline airline) {
        Map<String, String> contentsMap = this.obtainContentsMap();
        String contents = TIPS_TEMPLATE.takeEngine().replaceTags(contentsMap);
        return this.getTemplateResponseWithoutMenuItems("AirPlan Tips", contents, airline);
    }
}

