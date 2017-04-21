/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.http;

import com.graphhopper.tour.TourResponse;
import com.graphhopper.util.shapes.GHPlace;
import com.graphhopper.util.shapes.GHPoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TourSerializer {
    public Map<String, Object> toJSON(TourResponse<?> rsp) {
        HashMap<String, Object> json = new HashMap<String, Object>();
        if (rsp.hasErrors()) {
            json.put("message", rsp.getErrors().get(0).getMessage());
            ArrayList errorHintList = new ArrayList();
            for (Throwable t : rsp.getErrors()) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("message", t.getMessage());
                map.put("details", t.getClass().getName());
                errorHintList.add(map);
            }
            json.put("hints", errorHintList);
        } else {
            ArrayList<Map<String, Object>> jsonPoints = new ArrayList<Map<String, Object>>();
            json.put("points", jsonPoints);
            for (GHPoint p : rsp.getPoints()) {
                jsonPoints.add(this.pointToJSON(p));
            }
        }
        return json;
    }

    private Map<String, Object> pointToJSON(GHPoint p) {
        HashMap<String, Object> jsonPoint = new HashMap<String, Object>();
        if (p instanceof GHPlace) {
            jsonPoint.put("name", ((GHPlace)p).getName());
        }
        jsonPoint.put("lat", p.getLat());
        jsonPoint.put("lon", p.getLon());
        return jsonPoint;
    }
}

