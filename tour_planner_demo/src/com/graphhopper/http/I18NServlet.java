/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.http;

import com.graphhopper.http.GHBaseServlet;
import com.graphhopper.util.Helper;
import com.graphhopper.util.Translation;
import com.graphhopper.util.TranslationMap;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

public class I18NServlet
extends GHBaseServlet {
    @Inject
    private TranslationMap map;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String acceptLang;
        String locale = "";
        String path = req.getPathInfo();
        if (!Helper.isEmpty(path) && path.startsWith("/")) {
            locale = path.substring(1);
        }
        if (Helper.isEmpty(locale) && !Helper.isEmpty(acceptLang = req.getHeader("Accept-Language"))) {
            locale = acceptLang.split(",")[0];
        }
        Translation tr = this.map.get(locale);
        JSONObject json = new JSONObject();
        if (tr != null && !Locale.US.equals(tr.getLocale())) {
            json.put("default", tr.asMap());
        }
        json.put("locale", locale.toString());
        json.put("en", this.map.get("en").asMap());
        this.writeJson(req, res, json);
    }
}

