/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.util;

import com.graphhopper.util.DistanceCalc;
import com.graphhopper.util.GPXEntry;
import com.graphhopper.util.Helper;
import com.graphhopper.util.Instruction;
import com.graphhopper.util.InstructionAnnotation;
import com.graphhopper.util.PointList;
import com.graphhopper.util.Translation;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class InstructionList
implements Iterable<Instruction> {
    public static final InstructionList EMPTY = new InstructionList();
    private final List<Instruction> instructions;
    private final Translation tr;

    private InstructionList() {
        this(0, null);
    }

    public InstructionList(Translation tr) {
        this(10, tr);
    }

    public InstructionList(int cap, Translation tr) {
        this.instructions = new ArrayList<Instruction>(cap);
        this.tr = tr;
    }

    public void replaceLast(Instruction instr) {
        if (this.instructions.isEmpty()) {
            throw new IllegalStateException("Cannot replace last instruction as list is empty");
        }
        this.instructions.set(this.instructions.size() - 1, instr);
    }

    public void add(Instruction instr) {
        this.instructions.add(instr);
    }

    public int getSize() {
        return this.instructions.size();
    }

    public int size() {
        return this.instructions.size();
    }

    public List<Map<String, Object>> createJson() {
        ArrayList<Map<String, Object>> instrList = new ArrayList<Map<String, Object>>(this.instructions.size());
        int pointsIndex = 0;
        int counter = 0;
        for (Instruction instruction : this.instructions) {
            HashMap<String, Object> instrJson = new HashMap<String, Object>();
            instrList.add(instrJson);
            InstructionAnnotation ia = instruction.getAnnotation();
            String str = instruction.getTurnDescription(this.tr);
            if (Helper.isEmpty(str)) {
                str = ia.getMessage();
            }
            instrJson.put("text", Helper.firstBig(str));
            if (!ia.isEmpty()) {
                instrJson.put("annotation_text", ia.getMessage());
                instrJson.put("annotation_importance", ia.getImportance());
            }
            instrJson.put("time", instruction.getTime());
            instrJson.put("distance", Helper.round(instruction.getDistance(), 3));
            instrJson.put("sign", instruction.getSign());
            instrJson.putAll(instruction.getExtraInfoJSON());
            int tmpIndex = pointsIndex + instruction.getPoints().size();
            if (counter + 1 == this.instructions.size()) {
                --tmpIndex;
            }
            instrJson.put("interval", Arrays.asList(pointsIndex, tmpIndex));
            pointsIndex = tmpIndex;
            ++counter;
        }
        return instrList;
    }

    public boolean isEmpty() {
        return this.instructions.isEmpty();
    }

    @Override
    public Iterator<Instruction> iterator() {
        return this.instructions.iterator();
    }

    public Instruction get(int index) {
        return this.instructions.get(index);
    }

    public String toString() {
        return this.instructions.toString();
    }

    public List<GPXEntry> createGPXList() {
        if (this.isEmpty()) {
            return Collections.emptyList();
        }
        ArrayList<GPXEntry> gpxList = new ArrayList<GPXEntry>();
        long timeOffset = 0;
        for (int i = 0; i < this.size() - 1; ++i) {
            Instruction prevInstr = i > 0 ? this.get(i - 1) : null;
            boolean instrIsFirst = prevInstr == null;
            Instruction nextInstr = this.get(i + 1);
            nextInstr.checkOne();
            timeOffset = this.get(i).fillGPXList(gpxList, timeOffset, prevInstr, nextInstr, instrIsFirst);
        }
        Instruction lastI = this.get(this.size() - 1);
        if (lastI.points.size() != 1) {
            throw new IllegalStateException("Last instruction must have exactly one point but was " + lastI.points.size());
        }
        double lastLat = lastI.getFirstLat();
        double lastLon = lastI.getFirstLon();
        double lastEle = lastI.getPoints().is3D() ? lastI.getFirstEle() : Double.NaN;
        gpxList.add(new GPXEntry(lastLat, lastLon, lastEle, timeOffset));
        return gpxList;
    }

    public String createGPX() {
        return this.createGPX("GraphHopper", new Date().getTime());
    }

    public String createGPX(String trackName, long startTimeMillis) {
        boolean includeElevation = this.getSize() > 0 ? this.get(0).getPoints().is3D() : false;
        return this.createGPX(trackName, startTimeMillis, includeElevation);
    }

    public String createGPX(String trackName, long startTimeMillis, boolean includeElevation) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String header = "<?xml version='1.0' encoding='UTF-8' standalone='no' ?><gpx xmlns='http://www.topografix.com/GPX/1/1' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' creator='Graphhopper' version='1.1' xmlns:gh='https://graphhopper.com/public/schema/gpx/1.1'>\n<metadata><copyright author=\"OpenStreetMap contributors\"/><link href='http://graphhopper.com'><text>GraphHopper GPX</text></link><time>" + formatter.format(startTimeMillis) + "</time>" + "</metadata>";
        StringBuilder track = new StringBuilder(header);
        if (!this.isEmpty()) {
            track.append("\n<rte>");
            Instruction nextInstr = null;
            for (Instruction currInstr : this.instructions) {
                if (null != nextInstr) {
                    this.createRteptBlock(track, nextInstr, currInstr);
                }
                nextInstr = currInstr;
            }
            this.createRteptBlock(track, nextInstr, null);
            track.append("</rte>");
        }
        track.append("\n<trk><name>").append(trackName).append("</name>");
        track.append("<trkseg>");
        for (GPXEntry entry : this.createGPXList()) {
            track.append("\n<trkpt lat='").append(Helper.round6(entry.getLat()));
            track.append("' lon='").append(Helper.round6(entry.getLon())).append("'>");
            if (includeElevation) {
                track.append("<ele>").append(Helper.round2(entry.getEle())).append("</ele>");
            }
            track.append("<time>").append(formatter.format(startTimeMillis + entry.getTime())).append("</time>");
            track.append("</trkpt>");
        }
        track.append("</trkseg>");
        track.append("</trk>");
        track.append("</gpx>");
        return track.toString().replaceAll("\\'", "\"");
    }

    private void createRteptBlock(StringBuilder output, Instruction instruction, Instruction nextI) {
        double azimuth;
        output.append("\n<rtept lat=\"").append(Helper.round6(instruction.getFirstLat())).append("\" lon=\"").append(Helper.round6(instruction.getFirstLon())).append("\">");
        if (!instruction.getName().isEmpty()) {
            output.append("<desc>").append(instruction.getTurnDescription(this.tr)).append("</desc>");
        }
        output.append("<extensions>");
        output.append("<gh:distance>").append(Helper.round(instruction.getDistance(), 1)).append("</gh:distance>");
        output.append("<gh:time>").append(instruction.getTime()).append("</gh:time>");
        String direction = instruction.calcDirection(nextI);
        if (!direction.isEmpty()) {
            output.append("<gh:direction>").append(direction).append("</gh:direction>");
        }
        if (!Double.isNaN(azimuth = instruction.calcAzimuth(nextI))) {
            output.append("<gh:azimuth>").append(Helper.round2(azimuth)).append("</gh:azimuth>");
        }
        output.append("<gh:sign>").append(instruction.getSign()).append("</gh:sign>");
        output.append("</extensions>");
        output.append("</rtept>");
    }

    List<List<Double>> createStartPoints() {
        ArrayList<List<Double>> res = new ArrayList<List<Double>>(this.instructions.size());
        for (Instruction instruction : this.instructions) {
            res.add(Arrays.asList(instruction.getFirstLat(), instruction.getFirstLon()));
        }
        return res;
    }

    public Instruction find(double lat, double lon, double maxDistance) {
        if (this.getSize() == 0) {
            return null;
        }
        PointList points = this.get(0).getPoints();
        double prevLat = points.getLatitude(0);
        double prevLon = points.getLongitude(0);
        DistanceCalc distCalc = Helper.DIST_EARTH;
        double foundMinDistance = distCalc.calcNormalizedDist(lat, lon, prevLat, prevLon);
        int foundInstruction = 0;
        if (this.getSize() > 1) {
            for (int instructionIndex = 0; instructionIndex < this.getSize(); ++instructionIndex) {
                points = this.get(instructionIndex).getPoints();
                for (int pointIndex = 0; pointIndex < points.size(); ++pointIndex) {
                    double currLat = points.getLatitude(pointIndex);
                    double currLon = points.getLongitude(pointIndex);
                    if (instructionIndex != 0 || pointIndex != 0) {
                        double distance;
                        int index = instructionIndex;
                        if (distCalc.validEdgeDistance(lat, lon, currLat, currLon, prevLat, prevLon)) {
                            distance = distCalc.calcNormalizedEdgeDistance(lat, lon, currLat, currLon, prevLat, prevLon);
                            if (pointIndex > 0) {
                                ++index;
                            }
                        } else {
                            distance = distCalc.calcNormalizedDist(lat, lon, currLat, currLon);
                            if (pointIndex > 0) {
                                ++index;
                            }
                        }
                        if (distance < foundMinDistance) {
                            foundMinDistance = distance;
                            foundInstruction = index;
                        }
                    }
                    prevLat = currLat;
                    prevLon = currLon;
                }
            }
        }
        if (distCalc.calcDenormalizedDist(foundMinDistance) > maxDistance) {
            return null;
        }
        if (foundInstruction == this.getSize()) {
            --foundInstruction;
        }
        return this.get(foundInstruction);
    }
}

