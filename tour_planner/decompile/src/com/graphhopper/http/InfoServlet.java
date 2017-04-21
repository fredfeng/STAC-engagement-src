/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.http;

import com.graphhopper.GraphHopper;
import com.graphhopper.http.GHBaseServlet;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.storage.GraphHopperStorage;
import com.graphhopper.storage.StorableProperties;
import com.graphhopper.util.Constants;
import com.graphhopper.util.Helper;
import com.graphhopper.util.shapes.BBox;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

public class InfoServlet
extends GHBaseServlet {
    @Inject
    private GraphHopper hopper;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        BBox bb = this.hopper.getGraphHopperStorage().getBounds();
        ArrayList<Double> list = new ArrayList<Double>(4);
        list.add(bb.minLon);
        list.add(bb.minLat);
        list.add(bb.maxLon);
        list.add(bb.maxLat);
        JSONObject json = new JSONObject();
        json.put("bbox", list);
        String[] vehicles = this.hopper.getGraphHopperStorage().getEncodingManager().toString().split(",");
        json.put("supported_vehicles", vehicles);
        JSONObject features = new JSONObject();
        for (String v : vehicles) {
            JSONObject perVehicleJson = new JSONObject();
            perVehicleJson.put("elevation", this.hopper.hasElevation());
            features.put(v, perVehicleJson);
        }
        json.put("features", features);
        json.put("version", Constants.VERSION);
        json.put("build_date", Constants.BUILD_DATE);
        StorableProperties props = this.hopper.getGraphHopperStorage().getProperties();
        json.put("import_date", props.get("osmreader.import.date"));
        if (!Helper.isEmpty(props.get("prepare.date"))) {
            json.put("prepare_date", props.get("prepare.date"));
        }
        this.writeJson(req, res, json);
    }
}

