/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.http;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopperAPI;
import com.graphhopper.http.WebHelper;
import com.graphhopper.routing.util.WeightingMap;
import com.graphhopper.util.Downloader;
import com.graphhopper.util.FinishInstruction;
import com.graphhopper.util.Instruction;
import com.graphhopper.util.InstructionAnnotation;
import com.graphhopper.util.InstructionList;
import com.graphhopper.util.PointAccess;
import com.graphhopper.util.PointList;
import com.graphhopper.util.RoundaboutInstruction;
import com.graphhopper.util.StopWatch;
import com.graphhopper.util.Translation;
import com.graphhopper.util.ViaInstruction;
import com.graphhopper.util.shapes.GHPoint;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphHopperWeb
implements GraphHopperAPI {
    private final Logger logger;
    private Downloader downloader;
    private String routeServiceUrl;
    private String key;
    private boolean instructions;
    private boolean calcPoints;
    private boolean elevation;

    public GraphHopperWeb() {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.downloader = new Downloader("GraphHopper Java Client");
        this.routeServiceUrl = "https://graphhopper.com/api/1/route";
        this.key = "";
        this.instructions = true;
        this.calcPoints = true;
        this.elevation = false;
    }

    public void setDownloader(Downloader downloader) {
        this.downloader = downloader;
    }

    @Override
    public boolean load(String serviceUrl) {
        this.routeServiceUrl = serviceUrl;
        return true;
    }

    public GraphHopperWeb setKey(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalStateException("Key cannot be empty");
        }
        this.key = key;
        return this;
    }

    public GraphHopperWeb setCalcPoints(boolean calcPoints) {
        this.calcPoints = calcPoints;
        return this;
    }

    public GraphHopperWeb setInstructions(boolean b) {
        this.instructions = b;
        return this;
    }

    public GraphHopperWeb setElevation(boolean withElevation) {
        this.elevation = withElevation;
        return this;
    }

    @Override
    public GHResponse route(GHRequest request) {
        StopWatch sw = new StopWatch().start();
        try {
            String places = "";
            for (GHPoint p : request.getPoints()) {
                places = places + "point=" + p.lat + "," + p.lon + "&";
            }
            boolean tmpInstructions = request.getHints().getBool("instructions", this.instructions);
            boolean tmpCalcPoints = request.getHints().getBool("calcPoints", this.calcPoints);
            if (tmpInstructions && !tmpCalcPoints) {
                throw new IllegalStateException("Cannot calculate instructions without points (only points without instructions). Use calcPoints=false and instructions=false to disable point and instruction calculation");
            }
            boolean tmpElevation = request.getHints().getBool("elevation", this.elevation);
            String tmpKey = request.getHints().get("key", this.key);
            String url = this.routeServiceUrl + "?" + places + "&type=json" + "&instructions=" + tmpInstructions + "&points_encoded=true" + "&calc_points=" + tmpCalcPoints + "&algo=" + request.getAlgorithm() + "&locale=" + request.getLocale().toString() + "&elevation=" + tmpElevation;
            if (!request.getVehicle().isEmpty()) {
                url = url + "&vehicle=" + request.getVehicle();
            }
            if (!tmpKey.isEmpty()) {
                url = url + "&key=" + tmpKey;
            }
            String str = this.downloader.downloadAsString(url, true);
            JSONObject json = new JSONObject(str);
            GHResponse res = new GHResponse();
            GraphHopperWeb.readErrors(res.getErrors(), json);
            if (res.hasErrors()) {
                return res;
            }
            JSONArray paths = json.getJSONArray("paths");
            JSONObject firstPath = paths.getJSONObject(0);
            GraphHopperWeb.readPath(res, firstPath, tmpCalcPoints, tmpInstructions, tmpElevation);
            return res;
        }
        catch (Exception ex) {
            throw new RuntimeException("Problem while fetching path " + request.getPoints() + ": " + ex.getMessage(), ex);
        }
    }

    public static void readPath(GHResponse res, JSONObject firstPath, boolean tmpCalcPoints, boolean tmpInstructions, boolean tmpElevation) {
        double distance = firstPath.getDouble("distance");
        long time = firstPath.getLong("time");
        if (tmpCalcPoints) {
            String pointStr = firstPath.getString("points");
            PointList pointList = WebHelper.decodePolyline(pointStr, 100, tmpElevation);
            res.setPoints(pointList);
            if (tmpInstructions) {
                JSONArray instrArr = firstPath.getJSONArray("instructions");
                InstructionList il = new InstructionList(null);
                int viaCount = 1;
                for (int instrIndex = 0; instrIndex < instrArr.length(); ++instrIndex) {
                    Instruction instr2;
                    JSONObject jsonObj = instrArr.getJSONObject(instrIndex);
                    double instDist = jsonObj.getDouble("distance");
                    String text = jsonObj.getString("text");
                    long instTime = jsonObj.getLong("time");
                    int sign = jsonObj.getInt("sign");
                    JSONArray iv = jsonObj.getJSONArray("interval");
                    int from = iv.getInt(0);
                    int to = iv.getInt(1);
                    PointList instPL = new PointList(to - from, tmpElevation);
                    for (int j = from; j <= to; ++j) {
                        instPL.add(pointList, j);
                    }
                    InstructionAnnotation ia = InstructionAnnotation.EMPTY;
                    if (jsonObj.has("annotation_importance") && jsonObj.has("annotation_text")) {
                        ia = new InstructionAnnotation(jsonObj.getInt("annotation_importance"), jsonObj.getString("annotation_text"));
                    }
                    if (sign == 6 || sign == -6) {
                        instr2 = new RoundaboutInstruction(sign, text, ia, instPL);
                    } else if (sign == 5) {
                        ViaInstruction tmpInstr = new ViaInstruction(text, ia, instPL);
                        tmpInstr.setViaCount(viaCount);
                        ++viaCount;
                        instr2 = tmpInstr;
                    } else {
                        instr2 = sign == 4 ? new FinishInstruction(instPL, 0) : new Instruction(sign, text, ia, instPL);
                    }
                    instr2.setUseRawName();
                    instr2.setDistance(instDist).setTime(instTime);
                    il.add(instr2);
                }
                res.setInstructions(il);
            }
        }
        res.setDistance(distance).setTime(time);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void readErrors(List<Throwable> errors, JSONObject json) {
        JSONArray errorJson;
        if (json.has("message")) {
            if (!json.has("hints")) {
                errors.add(new RuntimeException(json.getString("message")));
                return;
            }
            errorJson = json.getJSONArray("hints");
        } else {
            if (!json.has("info")) {
                return;
            }
            JSONObject jsonInfo = json.getJSONObject("info");
            if (!jsonInfo.has("errors")) {
                return;
            }
            errorJson = jsonInfo.getJSONArray("errors");
        }
        for (int i = 0; i < errorJson.length(); ++i) {
            JSONObject error = errorJson.getJSONObject(i);
            String exClass = "";
            if (error.has("details")) {
                exClass = error.getString("details");
            }
            String exMessage = error.getString("message");
            if (exClass.equals(UnsupportedOperationException.class.getName())) {
                errors.add(new UnsupportedOperationException(exMessage));
                continue;
            }
            if (exClass.equals(IllegalStateException.class.getName())) {
                errors.add(new IllegalStateException(exMessage));
                continue;
            }
            if (exClass.equals(RuntimeException.class.getName())) {
                errors.add(new RuntimeException(exMessage));
                continue;
            }
            if (exClass.equals(IllegalArgumentException.class.getName())) {
                errors.add(new IllegalArgumentException(exMessage));
                continue;
            }
            if (exClass.isEmpty()) {
                errors.add(new RuntimeException(exMessage));
                continue;
            }
            errors.add(new RuntimeException(exClass + " " + exMessage));
        }
        if (json.has("message") && errors.isEmpty()) {
            errors.add(new RuntimeException(json.getString("message")));
        }
    }
}

