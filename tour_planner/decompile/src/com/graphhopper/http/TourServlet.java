/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.http;

import com.graphhopper.http.GHBaseServlet;
import com.graphhopper.http.TourSerializer;
import com.graphhopper.tour.Places;
import com.graphhopper.tour.TourCalculator;
import com.graphhopper.tour.TourResponse;
import com.graphhopper.tour.util.ProgressReporter;
import com.graphhopper.util.shapes.GHPlace;
import com.graphhopper.util.shapes.GHPoint;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

public class TourServlet
extends GHBaseServlet {
    @Inject
    private List<GHPlace> places;
    @Inject
    private TourCalculator tourCalculator;
    @Inject
    private TourSerializer tourSerializer;
    private Map<String, GHPlace> nameIndex;

    @Override
    public void init() {
        this.nameIndex = Places.nameIndex(this.places);
    }

    @Override
    public void doGet(HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        List<GHPoint> points;
        TourResponse tourRsp = new TourResponse();
        try {
            points = this.getPoints(req, "point");
        }
        catch (Exception ex) {
            tourRsp.addError(ex);
            Map<String, Object> map = this.tourSerializer.toJSON(tourRsp);
            this.writeJsonError(res, 400, new JSONObject(map));
            return;
        }
        res.setContentType("text/event-stream");
        res.setCharacterEncoding("UTF-8");
        res.setStatus(200);
        res.getWriter().flush();
        res.flushBuffer();
        ProgressReporter progressReporter = new ProgressReporter(){

            @Override
            public void reportProgress(int complete, int total) throws IOException {
                JSONObject json = new JSONObject();
                json.put("complete", complete);
                json.put("total", total);
                PrintWriter writer = res.getWriter();
                writer.append("event: progress\r\n");
                writer.append("data: " + json.toString() + "\r\n\r\n");
                writer.flush();
            }
        };
        tourRsp = this.tourCalculator.calcTour(points, progressReporter);
        Map<String, Object> map = this.tourSerializer.toJSON(tourRsp);
        JSONObject json = new JSONObject(map);
        PrintWriter writer = res.getWriter();
        writer.append("event: result\r\n");
        writer.append("data: " + json.toString() + "\r\n\r\n");
    }

    protected List<GHPoint> getPoints(HttpServletRequest req, String key) {
        String[] pointsAsStr = this.getParams(req, key);
        ArrayList<GHPoint> points = new ArrayList<GHPoint>(pointsAsStr.length);
        for (String str : pointsAsStr) {
            GHPoint point;
            String[] fromStrs = str.split(",");
            if (fromStrs.length == 2) {
                point = GHPoint.parse(str);
                if (point == null) continue;
                points.add(point);
                continue;
            }
            if (fromStrs.length != 1) continue;
            point = this.nameIndex.get(str);
            if (point == null) {
                throw new IllegalArgumentException("unknown place \"" + str + "\"");
            }
            points.add(point);
        }
        return points;
    }

}

