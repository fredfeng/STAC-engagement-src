/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.server.guide;

import com.sun.net.httpserver.HttpExchange;
import edu.cyberapex.server.WebTemplate;
import edu.cyberapex.server.guide.AbstractHttpGuide;
import edu.cyberapex.server.guide.HttpGuideResponse;
import edu.cyberapex.server.guide.MultipartHelper;
import edu.cyberapex.template.TemplateEngine;
import edu.cyberapex.template.TemplateEngineBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class AuthenticationGuide
extends AbstractHttpGuide {
    private final String redirectResponsePath;
    private final WebTemplate template;
    private static final String PATH = "/authenticate";
    private static final String TITLE = "Authenticate Server";
    private static final String KEY_FIELD = "A";
    private static final String TIMESTAMP_FIELD = "setTimestamp";
    private static final TemplateEngine TEMPLATE_ENGINE = new TemplateEngineBuilder().defineText("<center><form action=\"/authenticate\" method=\"post\" enctype=\"multipart/form-data\"/>    <textarea name=\"A\" placeholder=\"Enter your public key\"       rows=\"10\" cols=\"100\"/></textarea><br/>    <input type=\"submit\" value=\"Compute the master secret\" name=\"submit\" />    <input type=\"hidden\" name=\"setTimestamp\" value=\"{{includeTimestamp}}\"></form></center>").generateTemplateEngine();

    public AuthenticationGuide(String redirectResponsePath) {
        this.redirectResponsePath = redirectResponsePath;
        this.template = new WebTemplate("basiccontenttemplate.html", this.getClass());
    }

    @Override
    public String getPath() {
        return "/authenticate";
    }

    @Override
    protected HttpGuideResponse handleGrab(HttpExchange httpExchange) {
        HashMap<String, String> contentsTemplateMap = new HashMap<String, String>();
        HashMap<String, String> templateMap = new HashMap<String, String>();
        String suppressTimestamp = AuthenticationGuide.takeUrlParam(httpExchange, "suppressTimestamp");
        if (suppressTimestamp == null || !suppressTimestamp.equals("true")) {
            contentsTemplateMap.put("includeTimestamp", "true");
            templateMap.put("timestamp", new Date().toString());
            templateMap.put("duration", String.valueOf(this.fetchDuration(httpExchange)));
        } else {
            contentsTemplateMap.put("includeTimestamp", "false");
        }
        templateMap.put("contents", TEMPLATE_ENGINE.replaceTags(contentsTemplateMap));
        templateMap.put("title", "Authenticate Server");
        return AuthenticationGuide.takeResponse(this.template.getEngine().replaceTags(templateMap));
    }

    @Override
    protected HttpGuideResponse handlePost(HttpExchange httpExchange) {
        String timestamp;
        List<String> includeTimestampList;
        HashSet<String> fieldNames = new HashSet<String>(Arrays.asList("A", "setTimestamp"));
        Map<String, List<String>> fieldNameItems = MultipartHelper.fetchMultipartValues(httpExchange, fieldNames);
        String membersPublicKey = "";
        boolean includeTimestamp = true;
        List<String> membersPublicKeyList = fieldNameItems.get("A");
        if (membersPublicKeyList != null && membersPublicKeyList.size() == 1) {
            membersPublicKey = membersPublicKeyList.get(0);
        }
        if ((includeTimestampList = fieldNameItems.get("setTimestamp")) != null && includeTimestampList.size() == 1 && (timestamp = includeTimestampList.get(0)).equals("false")) {
            includeTimestamp = false;
        }
        String urlEnd = "";
        if (membersPublicKey != null) {
            urlEnd = membersPublicKey;
        }
        String suppressTimestamp = AuthenticationGuide.takeUrlParam(httpExchange, "suppressTimestamp");
        if (!includeTimestamp || suppressTimestamp != null && suppressTimestamp.equals("true")) {
            urlEnd = urlEnd + "?suppressTimestamp=true";
        }
        return AuthenticationGuide.takeRedirectResponse(this.redirectResponsePath + "/" + urlEnd);
    }
}

