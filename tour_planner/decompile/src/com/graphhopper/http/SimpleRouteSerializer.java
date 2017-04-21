/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.http;

import com.graphhopper.GHResponse;
import com.graphhopper.http.RouteSerializer;
import com.graphhopper.http.WebHelper;
import com.graphhopper.util.Helper;
import com.graphhopper.util.InstructionList;
import com.graphhopper.util.PMap;
import com.graphhopper.util.PointList;
import com.graphhopper.util.shapes.BBox;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleRouteSerializer
implements RouteSerializer {
    private final BBox maxBounds;

    public SimpleRouteSerializer(BBox maxBounds) {
        this.maxBounds = maxBounds;
    }

    @Override
    public Map<String, Object> toJSON(GHResponse rsp, boolean calcPoints, boolean pointsEncoded, boolean includeElevation, boolean enableInstructions) {
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
            HashMap<String, List<String>> jsonInfo = new HashMap<String, List<String>>();
            json.put("info", jsonInfo);
            json.put("hints", rsp.getHints().toMap());
            jsonInfo.put("copyrights", Arrays.asList("GraphHopper", "OpenStreetMap contributors"));
            HashMap<String, Object> jsonPath = new HashMap<String, Object>();
            jsonPath.put("distance", Helper.round(rsp.getDistance(), 3));
            jsonPath.put("weight", Helper.round6(rsp.getDistance()));
            jsonPath.put("time", rsp.getTime());
            if (calcPoints) {
                jsonPath.put("points_encoded", pointsEncoded);
                PointList points = rsp.getPoints();
                if (points.getSize() >= 2) {
                    BBox maxBounds2D = new BBox(this.maxBounds.minLon, this.maxBounds.maxLon, this.maxBounds.minLat, this.maxBounds.maxLat);
                    jsonPath.put("bbox", rsp.calcRouteBBox(maxBounds2D).toGeoJson());
                }
                jsonPath.put("points", this.createPoints(points, pointsEncoded, includeElevation));
                if (enableInstructions) {
                    InstructionList instructions = rsp.getInstructions();
                    jsonPath.put("instructions", instructions.createJson());
                }
            }
            json.put("paths", Collections.singletonList(jsonPath));
        }
        return json;
    }

    @Override
    public Object createPoints(PointList points, boolean pointsEncoded, boolean includeElevation) {
        if (pointsEncoded) {
            return WebHelper.encodePolyline(points, includeElevation);
        }
        HashMap<String, Object> jsonPoints = new HashMap<String, Object>();
        jsonPoints.put("type", "LineString");
        jsonPoints.put("coordinates", points.toGeoJson(includeElevation));
        return jsonPoints;
    }
}

