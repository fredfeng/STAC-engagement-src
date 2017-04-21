/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.nethost.coach;

import com.networkapex.nethost.WebTemplate;
import com.networkapex.nethost.WebTemplateBuilder;
import com.networkapex.nethost.coach.AbstractHttpManager;
import com.networkapex.nethost.coach.HttpManagerResponse;
import com.networkapex.nethost.coach.MultipartHelper;
import com.networkapex.template.TemplateEngine;
import com.sun.net.httpserver.HttpExchange;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class AuthenticationManager
extends AbstractHttpManager {
    private final String redirectResponseTrail;
    private final WebTemplate template;
    private static final String TRAIL = "/authenticate";
    private static final String TITLE = "Authenticate Server";
    private static final String KEY_FIELD = "A";
    private static final String TIMESTAMP_FIELD = "setTimestamp";
    private static final TemplateEngine TEMPLATE_ENGINE = new TemplateEngine("<center><form action=\"/authenticate\" method=\"post\" enctype=\"multipart/form-data\"/>    <textarea name=\"A\" placeholder=\"Enter your public key\"       rows=\"10\" cols=\"100\"/></textarea><br/>    <input type=\"submit\" value=\"Compute the master secret\" name=\"submit\" />    <input type=\"hidden\" name=\"setTimestamp\" value=\"{{includeTimestamp}}\"></form></center>");

    public AuthenticationManager(String redirectResponseTrail) {
        this.redirectResponseTrail = redirectResponseTrail;
        this.template = new WebTemplateBuilder().defineResourceName("basiccontenttemplate.html").defineLoader(this.getClass()).generateWebTemplate();
    }

    @Override
    public String obtainTrail() {
        return "/authenticate";
    }

    @Override
    protected HttpManagerResponse handleFetch(HttpExchange httpExchange) {
        HashMap<String, String> contentsTemplateMap = new HashMap<String, String>();
        HashMap<String, String> templateMap = new HashMap<String, String>();
        String suppressTimestamp = AuthenticationManager.takeUrlParam(httpExchange, "suppressTimestamp");
        if (suppressTimestamp == null || !suppressTimestamp.equals("true")) {
            this.handleFetchAdviser(httpExchange, contentsTemplateMap, templateMap);
        } else {
            contentsTemplateMap.put("includeTimestamp", "false");
        }
        templateMap.put("contents", TEMPLATE_ENGINE.replaceTags(contentsTemplateMap));
        templateMap.put("title", "Authenticate Server");
        return AuthenticationManager.grabResponse(this.template.takeEngine().replaceTags(templateMap));
    }

    private void handleFetchAdviser(HttpExchange httpExchange, Map<String, String> contentsTemplateMap, Map<String, String> templateMap) {
        contentsTemplateMap.put("includeTimestamp", "true");
        templateMap.put("timestamp", new Date().toString());
        templateMap.put("duration", String.valueOf(this.getDuration(httpExchange)));
    }

    @Override
    protected HttpManagerResponse handlePost(HttpExchange httpExchange) {
        String timestamp;
        List<String> includeTimestampList;
        HashSet<String> fieldNames = new HashSet<String>(Arrays.asList("A", "setTimestamp"));
        Map<String, List<String>> fieldNameItems = MultipartHelper.getMultipartValues(httpExchange, fieldNames);
        String personsPublicKey = "";
        boolean includeTimestamp = true;
        List<String> personsPublicKeyList = fieldNameItems.get("A");
        if (personsPublicKeyList != null && personsPublicKeyList.size() == 1) {
            personsPublicKey = personsPublicKeyList.get(0);
        }
        if ((includeTimestampList = fieldNameItems.get("setTimestamp")) != null && includeTimestampList.size() == 1 && (timestamp = includeTimestampList.get(0)).equals("false")) {
            includeTimestamp = false;
        }
        String urlEnd = "";
        if (personsPublicKey != null) {
            urlEnd = personsPublicKey;
        }
        String suppressTimestamp = AuthenticationManager.takeUrlParam(httpExchange, "suppressTimestamp");
        if (!includeTimestamp || suppressTimestamp != null && suppressTimestamp.equals("true")) {
            urlEnd = urlEnd + "?suppressTimestamp=true";
        }
        return AuthenticationManager.obtainRedirectResponse(this.redirectResponseTrail + "/" + urlEnd);
    }
}

