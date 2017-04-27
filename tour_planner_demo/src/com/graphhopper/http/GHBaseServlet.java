/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GHBaseServlet
extends HttpServlet {
    protected static Logger logger = LoggerFactory.getLogger(GHBaseServlet.class);
    @Inject
    @Named(value="jsonpAllowed")
    private boolean jsonpAllowed;

    protected void writeJson(HttpServletRequest req, HttpServletResponse res, JSONObject json) throws JSONException, IOException {
        this.writeJson(req, res, (Object)json);
    }

    protected void writeJson(HttpServletRequest req, HttpServletResponse res, JSONArray json) throws JSONException, IOException {
        this.writeJson(req, res, (Object)json);
    }

    private void writeJson(HttpServletRequest req, HttpServletResponse res, Object json) throws JSONException, IOException {
        boolean debug;
        String type = this.getParam(req, "type", "json");
        res.setCharacterEncoding("UTF-8");
        boolean bl = debug = this.getBooleanParam(req, "debug", false) || this.getBooleanParam(req, "pretty", false);
        if ("jsonp".equals(type)) {
            res.setContentType("application/javascript");
            if (!this.jsonpAllowed) {
                this.writeError(res, 400, "Server is not configured to allow jsonp!");
                return;
            }
            String callbackName = this.getParam(req, "callback", null);
            if (callbackName == null) {
                this.writeError(res, 400, "No callback provided, necessary if type=jsonp");
                return;
            }
            this.writeResponse(res, callbackName + "(" + json.toString() + ")");
        } else {
            this.writeResponse(res, json.toString());
        }
    }

    protected void writeError(HttpServletResponse res, int code, String message) {
        JSONObject json = new JSONObject();
        json.put("message", message);
        this.writeJsonError(res, code, json);
    }

    protected void writeJsonError(HttpServletResponse res, int code, JSONObject json) {
        try {
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            res.setStatus(code);
            res.getWriter().append(json.toString(2));
        }
        catch (IOException ex) {
            logger.error("Cannot write error " + ex.getMessage());
        }
    }

    protected String getParam(HttpServletRequest req, String key, String _default) {
        String[] l = req.getParameterMap().get(key);
        if (l != null && l.length > 0) {
            return l[0];
        }
        return _default;
    }

    protected String[] getParams(HttpServletRequest req, String key) {
        String[] l = req.getParameterMap().get(key);
        if (l != null && l.length > 0) {
            return l;
        }
        return new String[0];
    }

    protected List<Double> getDoubleParamList(HttpServletRequest req, String key) {
        String[] l = req.getParameterMap().get(key);
        if (l != null && l.length > 0) {
            ArrayList<Double> doubleList = new ArrayList<Double>(l.length);
            for (String s : l) {
                doubleList.add(Double.valueOf(s));
            }
            return doubleList;
        }
        return Collections.emptyList();
    }

    protected long getLongParam(HttpServletRequest req, String key, long _default) {
        try {
            return Long.parseLong(this.getParam(req, key, "" + _default));
        }
        catch (Exception ex) {
            return _default;
        }
    }

    protected boolean getBooleanParam(HttpServletRequest req, String key, boolean _default) {
        try {
            return Boolean.parseBoolean(this.getParam(req, key, "" + _default));
        }
        catch (Exception ex) {
            return _default;
        }
    }

    protected double getDoubleParam(HttpServletRequest req, String key, double _default) {
        try {
            return Double.parseDouble(this.getParam(req, key, "" + _default));
        }
        catch (Exception ex) {
            return _default;
        }
    }

    public void writeResponse(HttpServletResponse res, String str) {
        try {
            res.setStatus(200);
            res.getWriter().append(str);
        }
        catch (IOException ex) {
            logger.error("Cannot write message:" + str, ex);
        }
    }
}

