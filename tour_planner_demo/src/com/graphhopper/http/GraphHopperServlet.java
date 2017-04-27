/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.http;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.http.GHBaseServlet;
import com.graphhopper.http.RouteSerializer;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.routing.util.WeightingMap;
import com.graphhopper.util.InstructionList;
import com.graphhopper.util.PointList;
import com.graphhopper.util.StopWatch;
import com.graphhopper.util.shapes.GHPoint;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class GraphHopperServlet
extends GHBaseServlet {
    @Inject
    private GraphHopper hopper;
    @Inject
    private RouteSerializer routeSerializer;

    @Override
    public void doGet(HttpServletRequest httpReq, HttpServletResponse httpRes) throws ServletException, IOException {
        List<GHPoint> requestPoints = this.getPoints(httpReq, "point");
        GHResponse ghRsp = new GHResponse();
        double minPathPrecision = this.getDoubleParam(httpReq, "way_point_max_distance", 1.0);
        boolean writeGPX = "gpx".equalsIgnoreCase(this.getParam(httpReq, "type", "json"));
        boolean enableInstructions = writeGPX || this.getBooleanParam(httpReq, "instructions", true);
        boolean calcPoints = this.getBooleanParam(httpReq, "calc_points", true);
        boolean enableElevation = this.getBooleanParam(httpReq, "elevation", false);
        boolean pointsEncoded = this.getBooleanParam(httpReq, "points_encoded", true);
        String vehicleStr = this.getParam(httpReq, "vehicle", "car");
        String weighting = this.getParam(httpReq, "weighting", "fastest");
        String algoStr = this.getParam(httpReq, "algorithm", "");
        String localeStr = this.getParam(httpReq, "locale", "en");
        List<Double> favoredHeadings = Collections.EMPTY_LIST;
        try {
            favoredHeadings = this.getDoubleParamList(httpReq, "heading");
        }
        catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
        StopWatch sw = new StopWatch().start();
        if (!this.hopper.getEncodingManager().supports(vehicleStr)) {
            ghRsp.addError(new IllegalArgumentException("Vehicle not supported: " + vehicleStr));
        } else if (enableElevation && !this.hopper.hasElevation()) {
            ghRsp.addError(new IllegalArgumentException("Elevation not supported!"));
        } else if (favoredHeadings.size() > 1 && favoredHeadings.size() != requestPoints.size()) {
            ghRsp.addError(new IllegalArgumentException("The number of 'heading' parameters must be <= 1 or equal to the number of points (" + requestPoints.size() + ")"));
        }
        if (!ghRsp.hasErrors()) {
            GHRequest request;
            FlagEncoder algoVehicle = this.hopper.getEncodingManager().getEncoder(vehicleStr);
            if (favoredHeadings.size() > 0) {
                if (favoredHeadings.size() == 1) {
                    ArrayList<Double> paddedHeadings = new ArrayList<Double>(Collections.nCopies(requestPoints.size(), Double.NaN));
                    paddedHeadings.set(0, favoredHeadings.get(0));
                    request = new GHRequest(requestPoints, paddedHeadings);
                } else {
                    request = new GHRequest(requestPoints, favoredHeadings);
                }
            } else {
                request = new GHRequest(requestPoints);
            }
            this.initHints(request, httpReq.getParameterMap());
            request.setVehicle(algoVehicle.toString()).setWeighting(weighting).setAlgorithm(algoStr).setLocale(localeStr).getHints().put("calcPoints", calcPoints).put("instructions", enableInstructions).put("wayPointMaxDistance", minPathPrecision);
            ghRsp = this.hopper.route(request);
        }
        float took = sw.stop().getSeconds();
        String infoStr = httpReq.getRemoteAddr() + " " + httpReq.getLocale() + " " + httpReq.getHeader("User-Agent");
        String logStr = httpReq.getQueryString() + " " + infoStr + " " + requestPoints + ", took:" + took + ", " + algoStr + ", " + weighting + ", " + vehicleStr;
        httpRes.setHeader("X-GH-Took", "" + Math.round(took * 1000.0f));
        if (ghRsp.hasErrors()) {
            logger.error(logStr + ", errors:" + ghRsp.getErrors());
        } else {
            logger.info(logStr + ", distance: " + ghRsp.getDistance() + ", time:" + Math.round((float)ghRsp.getTime() / 60000.0f) + "min, points:" + ghRsp.getPoints().getSize() + ", debug - " + ghRsp.getDebugInfo());
        }
        if (writeGPX) {
            String xml = this.createGPXString(httpReq, httpRes, ghRsp);
            if (ghRsp.hasErrors()) {
                httpRes.setStatus(400);
                httpRes.getWriter().append(xml);
            } else {
                this.writeResponse(httpRes, xml);
            }
        } else {
            Map<String, Object> map = this.routeSerializer.toJSON(ghRsp, calcPoints, pointsEncoded, enableElevation, enableInstructions);
            Object infoMap = map.get("info");
            if (infoMap != null) {
                ((Map)infoMap).put("took", Math.round(took * 1000.0f));
            }
            if (ghRsp.hasErrors()) {
                this.writeJsonError(httpRes, 400, new JSONObject(map));
            } else {
                this.writeJson(httpReq, httpRes, new JSONObject(map));
            }
        }
    }

    protected String createGPXString(HttpServletRequest req, HttpServletResponse res, GHResponse rsp) {
        boolean includeElevation = this.getBooleanParam(req, "elevation", false);
        res.setCharacterEncoding("UTF-8");
        res.setContentType("application/xml");
        String trackName = this.getParam(req, "track", "GraphHopper Track");
        res.setHeader("Content-Disposition", "attachment;filename=GraphHopper.gpx");
        long time = this.getLongParam(req, "millis", System.currentTimeMillis());
        if (rsp.hasErrors()) {
            return this.errorsToXML(rsp.getErrors());
        }
        return rsp.getInstructions().createGPX(trackName, time, includeElevation);
    }

    String errorsToXML(List<Throwable> list) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element gpxElement = doc.createElement("gpx");
            gpxElement.setAttribute("creator", "GraphHopper");
            gpxElement.setAttribute("version", "1.1");
            doc.appendChild(gpxElement);
            Element mdElement = doc.createElement("metadata");
            gpxElement.appendChild(mdElement);
            Element extensionsElement = doc.createElement("extensions");
            mdElement.appendChild(extensionsElement);
            Element messageElement = doc.createElement("message");
            extensionsElement.appendChild(messageElement);
            messageElement.setTextContent(list.get(0).getMessage());
            Element hintsElement = doc.createElement("hints");
            extensionsElement.appendChild(hintsElement);
            for (Throwable t : list) {
                Element error = doc.createElement("error");
                hintsElement.appendChild(error);
                error.setAttribute("message", t.getMessage());
                error.setAttribute("details", t.getClass().getName());
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            return writer.toString();
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    protected List<GHPoint> getPoints(HttpServletRequest req, String key) {
        String[] pointsAsStr = this.getParams(req, key);
        ArrayList<GHPoint> infoPoints = new ArrayList<GHPoint>(pointsAsStr.length);
        for (String str : pointsAsStr) {
            GHPoint point;
            String[] fromStrs = str.split(",");
            if (fromStrs.length != 2 || (point = GHPoint.parse(str)) == null) continue;
            infoPoints.add(point);
        }
        return infoPoints;
    }

    protected void initHints(GHRequest request, Map<String, String[]> parameterMap) {
        WeightingMap m = request.getHints();
        for (Map.Entry<String, String[]> e : parameterMap.entrySet()) {
            if (e.getValue().length != 1) continue;
            m.put(e.getKey(), e.getValue()[0]);
        }
    }
}

