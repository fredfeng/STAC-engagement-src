/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing.util;

import com.graphhopper.reader.OSMRelation;
import com.graphhopper.reader.OSMWay;
import com.graphhopper.routing.util.AbstractFlagEncoder;
import com.graphhopper.routing.util.EncodedDoubleValue;
import com.graphhopper.routing.util.EncodedValue;
import com.graphhopper.routing.util.PriorityCode;
import com.graphhopper.routing.util.PriorityWeighting;
import com.graphhopper.util.Helper;
import com.graphhopper.util.InstructionAnnotation;
import com.graphhopper.util.Translation;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class BikeCommonFlagEncoder
extends AbstractFlagEncoder {
    public static final int K_UNPAVED = 100;
    protected static final int PUSHING_SECTION_SPEED = 4;
    private long unpavedBit = 0;
    protected final HashSet<String> pushingSections = new HashSet();
    protected final HashSet<String> oppositeLanes = new HashSet();
    protected final Set<String> preferHighwayTags = new HashSet<String>();
    protected final Set<String> avoidHighwayTags = new HashSet<String>();
    protected final Set<String> unpavedSurfaceTags = new HashSet<String>();
    private final Map<String, Integer> trackTypeSpeeds = new HashMap<String, Integer>();
    private final Map<String, Integer> surfaceSpeeds = new HashMap<String, Integer>();
    private final Set<String> roadValues = new HashSet<String>();
    private final Map<String, Integer> highwaySpeeds = new HashMap<String, Integer>();
    private final Map<String, Integer> bikeNetworkToCode = new HashMap<String, Integer>();
    protected EncodedValue relationCodeEncoder;
    private EncodedValue wayTypeEncoder;
    EncodedValue priorityWayEncoder;
    private int avoidSpeedLimit;
    private String specificBicycleClass;

    protected BikeCommonFlagEncoder(int speedBits, double speedFactor, int maxTurnCosts) {
        super(speedBits, speedFactor, maxTurnCosts);
        this.restrictions.addAll(Arrays.asList("bicycle", "access"));
        this.restrictedValues.add("private");
        this.restrictedValues.add("no");
        this.restrictedValues.add("restricted");
        this.restrictedValues.add("military");
        this.intendedValues.add("yes");
        this.intendedValues.add("designated");
        this.intendedValues.add("official");
        this.intendedValues.add("permissive");
        this.oppositeLanes.add("opposite");
        this.oppositeLanes.add("opposite_lane");
        this.oppositeLanes.add("opposite_track");
        this.setBlockByDefault(false);
        this.potentialBarriers.add("gate");
        this.potentialBarriers.add("swing_gate");
        this.absoluteBarriers.add("stile");
        this.absoluteBarriers.add("turnstile");
        this.acceptedRailways.add("platform");
        this.unpavedSurfaceTags.add("unpaved");
        this.unpavedSurfaceTags.add("gravel");
        this.unpavedSurfaceTags.add("ground");
        this.unpavedSurfaceTags.add("dirt");
        this.unpavedSurfaceTags.add("grass");
        this.unpavedSurfaceTags.add("compacted");
        this.unpavedSurfaceTags.add("earth");
        this.unpavedSurfaceTags.add("fine_gravel");
        this.unpavedSurfaceTags.add("grass_paver");
        this.unpavedSurfaceTags.add("ice");
        this.unpavedSurfaceTags.add("mud");
        this.unpavedSurfaceTags.add("salt");
        this.unpavedSurfaceTags.add("sand");
        this.unpavedSurfaceTags.add("wood");
        this.roadValues.add("living_street");
        this.roadValues.add("road");
        this.roadValues.add("service");
        this.roadValues.add("unclassified");
        this.roadValues.add("residential");
        this.roadValues.add("trunk");
        this.roadValues.add("trunk_link");
        this.roadValues.add("primary");
        this.roadValues.add("primary_link");
        this.roadValues.add("secondary");
        this.roadValues.add("secondary_link");
        this.roadValues.add("tertiary");
        this.roadValues.add("tertiary_link");
        this.maxPossibleSpeed = 30;
        this.setTrackTypeSpeed("grade1", 18);
        this.setTrackTypeSpeed("grade2", 12);
        this.setTrackTypeSpeed("grade3", 8);
        this.setTrackTypeSpeed("grade4", 6);
        this.setTrackTypeSpeed("grade5", 4);
        this.setSurfaceSpeed("paved", 18);
        this.setSurfaceSpeed("asphalt", 18);
        this.setSurfaceSpeed("cobblestone", 8);
        this.setSurfaceSpeed("cobblestone:flattened", 10);
        this.setSurfaceSpeed("sett", 10);
        this.setSurfaceSpeed("concrete", 18);
        this.setSurfaceSpeed("concrete:lanes", 16);
        this.setSurfaceSpeed("concrete:plates", 16);
        this.setSurfaceSpeed("paving_stones", 12);
        this.setSurfaceSpeed("paving_stones:30", 12);
        this.setSurfaceSpeed("unpaved", 14);
        this.setSurfaceSpeed("compacted", 16);
        this.setSurfaceSpeed("dirt", 10);
        this.setSurfaceSpeed("earth", 12);
        this.setSurfaceSpeed("fine_gravel", 18);
        this.setSurfaceSpeed("grass", 8);
        this.setSurfaceSpeed("grass_paver", 8);
        this.setSurfaceSpeed("gravel", 12);
        this.setSurfaceSpeed("ground", 12);
        this.setSurfaceSpeed("ice", 2);
        this.setSurfaceSpeed("metal", 10);
        this.setSurfaceSpeed("mud", 10);
        this.setSurfaceSpeed("pebblestone", 16);
        this.setSurfaceSpeed("salt", 6);
        this.setSurfaceSpeed("sand", 6);
        this.setSurfaceSpeed("wood", 6);
        this.setHighwaySpeed("living_street", 6);
        this.setHighwaySpeed("steps", 2);
        this.setHighwaySpeed("cycleway", 18);
        this.setHighwaySpeed("path", 12);
        this.setHighwaySpeed("footway", 6);
        this.setHighwaySpeed("pedestrian", 6);
        this.setHighwaySpeed("track", 12);
        this.setHighwaySpeed("service", 14);
        this.setHighwaySpeed("residential", 18);
        this.setHighwaySpeed("unclassified", 16);
        this.setHighwaySpeed("road", 12);
        this.setHighwaySpeed("trunk", 18);
        this.setHighwaySpeed("trunk_link", 18);
        this.setHighwaySpeed("primary", 18);
        this.setHighwaySpeed("primary_link", 18);
        this.setHighwaySpeed("secondary", 18);
        this.setHighwaySpeed("secondary_link", 18);
        this.setHighwaySpeed("tertiary", 18);
        this.setHighwaySpeed("tertiary_link", 18);
        this.setHighwaySpeed("motorway", 18);
        this.setHighwaySpeed("motorway_link", 18);
        this.avoidHighwayTags.add("motorway");
        this.avoidHighwayTags.add("motorway_link");
        this.setCyclingNetworkPreference("icn", PriorityCode.BEST.getValue());
        this.setCyclingNetworkPreference("ncn", PriorityCode.BEST.getValue());
        this.setCyclingNetworkPreference("rcn", PriorityCode.VERY_NICE.getValue());
        this.setCyclingNetworkPreference("lcn", PriorityCode.PREFER.getValue());
        this.setCyclingNetworkPreference("mtb", PriorityCode.UNCHANGED.getValue());
        this.setCyclingNetworkPreference("deprecated", PriorityCode.AVOID_AT_ALL_COSTS.getValue());
        this.setAvoidSpeedLimit(71);
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public int defineWayBits(int index, int shift) {
        shift = super.defineWayBits(index, shift);
        this.speedEncoder = new EncodedDoubleValue("Speed", shift, this.speedBits, this.speedFactor, this.highwaySpeeds.get("cycleway").intValue(), this.maxPossibleSpeed);
        shift += this.speedEncoder.getBits();
        this.unpavedBit = 1 << shift++;
        this.wayTypeEncoder = new EncodedValue("WayType", shift, 2, 1.0, 0, 3, true);
        this.priorityWayEncoder = new EncodedValue("PreferWay", shift += this.wayTypeEncoder.getBits(), 3, 1.0, 0, 7);
        return shift += this.priorityWayEncoder.getBits();
    }

    @Override
    public int defineRelationBits(int index, int shift) {
        this.relationCodeEncoder = new EncodedValue("RelationCode", shift, 3, 1.0, 0, 7);
        return shift + this.relationCodeEncoder.getBits();
    }

    @Override
    public long acceptWay(OSMWay way) {
        String highwayValue = way.getTag("highway");
        if (highwayValue == null) {
            String bikeTag;
            if (way.hasTag("route", this.ferries) && ((bikeTag = way.getTag("bicycle")) == null && !way.hasTag("foot", new String[0]) || "yes".equals(bikeTag))) {
                return this.acceptBit | this.ferryBit;
            }
            if (way.hasTag("railway", (Object)"platform")) {
                return this.acceptBit;
            }
            return 0;
        }
        if (!this.highwaySpeeds.containsKey(highwayValue)) {
            return 0;
        }
        if (way.hasTag("bicycle", this.intendedValues)) {
            return this.acceptBit;
        }
        if ("motorway".equals(highwayValue) || "motorway_link".equals(highwayValue)) {
            return 0;
        }
        if (way.hasTag("motorroad", (Object)"yes")) {
            return 0;
        }
        if (this.isBlockFords() && (way.hasTag("highway", (Object)"ford") || way.hasTag("ford", new String[0]))) {
            return 0;
        }
        if (way.hasTag(this.restrictions, this.restrictedValues)) {
            return 0;
        }
        if (way.hasTag("railway", new String[0]) && !way.hasTag("railway", this.acceptedRailways)) {
            return 0;
        }
        String sacScale = way.getTag("sac_scale");
        if (sacScale != null) {
            if (way.hasTag("highway", (Object)"cycleway") && way.hasTag("sac_scale", (Object)"hiking")) {
                return this.acceptBit;
            }
            if (!this.allowedSacScale(sacScale)) {
                return 0;
            }
        }
        return this.acceptBit;
    }

    boolean allowedSacScale(String sacScale) {
        return "hiking".equals(sacScale);
    }

    @Override
    public long handleRelationTags(OSMRelation relation, long oldRelationFlags) {
        int code = 0;
        if (relation.hasTag("route", (Object)"bicycle")) {
            Integer val = this.bikeNetworkToCode.get(relation.getTag("network"));
            if (val != null) {
                code = val;
            }
        } else if (relation.hasTag("route", (Object)"ferry")) {
            code = PriorityCode.AVOID_IF_POSSIBLE.getValue();
        }
        int oldCode = (int)this.relationCodeEncoder.getValue(oldRelationFlags);
        if (oldCode < code) {
            return this.relationCodeEncoder.setValue(0, code);
        }
        return oldRelationFlags;
    }

    @Override
    public long handleWayTags(OSMWay way, long allowed, long relationFlags) {
        if (!this.isAccept(allowed)) {
            return 0;
        }
        long encoded = 0;
        if (!this.isFerry(allowed)) {
            double speed = this.getSpeed(way);
            speed = this.applyMaxSpeed(way, speed, false);
            encoded = this.handleSpeed(way, speed, encoded);
            encoded = this.handleBikeRelated(way, encoded, relationFlags > (long)PriorityCode.UNCHANGED.getValue());
            boolean isRoundabout = way.hasTag("junction", (Object)"roundabout");
            if (isRoundabout) {
                encoded = this.setBool(encoded, 2, true);
            }
        } else {
            encoded = this.handleFerryTags(way, this.highwaySpeeds.get("living_street").intValue(), this.highwaySpeeds.get("track").intValue(), this.highwaySpeeds.get("primary").intValue());
            encoded |= this.directionBitMask;
        }
        int priorityFromRelation = 0;
        if (relationFlags != 0) {
            priorityFromRelation = (int)this.relationCodeEncoder.getValue(relationFlags);
        }
        encoded = this.priorityWayEncoder.setValue(encoded, this.handlePriority(way, priorityFromRelation));
        return encoded;
    }

    int getSpeed(OSMWay way) {
        int speed = 4;
        String highwayTag = way.getTag("highway");
        Integer highwaySpeed = this.highwaySpeeds.get(highwayTag);
        String s = way.getTag("surface");
        if (!Helper.isEmpty(s)) {
            Integer surfaceSpeed = this.surfaceSpeeds.get(s);
            if (surfaceSpeed != null) {
                speed = surfaceSpeed;
                if (highwaySpeed != null && surfaceSpeed > highwaySpeed) {
                    speed = this.pushingSections.contains(highwayTag) ? highwaySpeed.intValue() : surfaceSpeed.intValue();
                }
            }
        } else {
            String tt = way.getTag("tracktype");
            if (!Helper.isEmpty(tt)) {
                Integer tInt = this.trackTypeSpeeds.get(tt);
                if (tInt != null) {
                    speed = tInt;
                }
            } else if (highwaySpeed != null) {
                speed = !way.hasTag("service", new String[0]) ? highwaySpeed.intValue() : this.highwaySpeeds.get("living_street").intValue();
            }
        }
        if (speed > 4 && !way.hasTag("bicycle", this.intendedValues) && way.hasTag("highway", this.pushingSections)) {
            speed = way.hasTag("highway", (Object)"steps") ? 2 : 4;
        }
        return speed;
    }

    @Override
    public InstructionAnnotation getAnnotation(long flags, Translation tr) {
        int paveType = 0;
        if (this.isBool(flags, 100)) {
            paveType = 1;
        }
        int wayType = (int)this.wayTypeEncoder.getValue(flags);
        String wayName = this.getWayName(paveType, wayType, tr);
        return new InstructionAnnotation(0, wayName);
    }

    String getWayName(int pavementType, int wayType, Translation tr) {
        String pavementName = "";
        if (pavementType == 1) {
            pavementName = tr.tr("unpaved", new Object[0]);
        }
        String wayTypeName = "";
        switch (wayType) {
            case 0: {
                wayTypeName = tr.tr("road", new Object[0]);
                break;
            }
            case 1: {
                wayTypeName = tr.tr("off_bike", new Object[0]);
                break;
            }
            case 2: {
                wayTypeName = tr.tr("cycleway", new Object[0]);
                break;
            }
            case 3: {
                wayTypeName = tr.tr("way", new Object[0]);
            }
        }
        if (pavementName.isEmpty()) {
            if (wayType == 0 || wayType == 3) {
                return "";
            }
            return wayTypeName;
        }
        if (wayTypeName.isEmpty()) {
            return pavementName;
        }
        return wayTypeName + ", " + pavementName;
    }

    protected int handlePriority(OSMWay way, int priorityFromRelation) {
        TreeMap<Double, Integer> weightToPrioMap = new TreeMap<Double, Integer>();
        if (priorityFromRelation == 0) {
            weightToPrioMap.put(0.0, PriorityCode.UNCHANGED.getValue());
        } else {
            weightToPrioMap.put(110.0, priorityFromRelation);
        }
        this.collect(way, weightToPrioMap);
        return weightToPrioMap.lastEntry().getValue();
    }

    private PriorityCode convertCallValueToPriority(String tagvalue) {
        int classvalue;
        try {
            classvalue = Integer.parseInt(tagvalue);
        }
        catch (NumberFormatException e) {
            return PriorityCode.UNCHANGED;
        }
        switch (classvalue) {
            case 3: {
                return PriorityCode.BEST;
            }
            case 2: {
                return PriorityCode.VERY_NICE;
            }
            case 1: {
                return PriorityCode.PREFER;
            }
            case 0: {
                return PriorityCode.UNCHANGED;
            }
            case -1: {
                return PriorityCode.AVOID_IF_POSSIBLE;
            }
            case -2: {
                return PriorityCode.REACH_DEST;
            }
            case -3: {
                return PriorityCode.AVOID_AT_ALL_COSTS;
            }
        }
        return PriorityCode.UNCHANGED;
    }

    void collect(OSMWay way, TreeMap<Double, Integer> weightToPrioMap) {
        String classBicycleSpecific;
        String service = way.getTag("service");
        String highway = way.getTag("highway");
        if (way.hasTag("bicycle", (Object)"designated")) {
            weightToPrioMap.put(100.0, PriorityCode.PREFER.getValue());
        }
        if ("cycleway".equals(highway)) {
            weightToPrioMap.put(100.0, PriorityCode.VERY_NICE.getValue());
        }
        double maxSpeed = this.getMaxSpeed(way);
        if (this.preferHighwayTags.contains(highway) || maxSpeed > 0.0 && maxSpeed <= 30.0) {
            if (maxSpeed < (double)this.avoidSpeedLimit) {
                weightToPrioMap.put(40.0, PriorityCode.PREFER.getValue());
                if (way.hasTag("tunnel", this.intendedValues)) {
                    weightToPrioMap.put(40.0, PriorityCode.UNCHANGED.getValue());
                }
            }
        } else if (this.avoidHighwayTags.contains(highway) || maxSpeed >= (double)this.avoidSpeedLimit && !"track".equals(highway)) {
            weightToPrioMap.put(50.0, PriorityCode.REACH_DEST.getValue());
            if (way.hasTag("tunnel", this.intendedValues)) {
                weightToPrioMap.put(50.0, PriorityCode.AVOID_AT_ALL_COSTS.getValue());
            }
        }
        if (this.pushingSections.contains(highway) || way.hasTag("bicycle", (Object)"use_sidepath") || "parking_aisle".equals(service)) {
            if (way.hasTag("bicycle", (Object)"yes")) {
                weightToPrioMap.put(100.0, PriorityCode.UNCHANGED.getValue());
            } else {
                weightToPrioMap.put(50.0, PriorityCode.AVOID_IF_POSSIBLE.getValue());
            }
        }
        if (way.hasTag("railway", (Object)"tram")) {
            weightToPrioMap.put(50.0, PriorityCode.AVOID_AT_ALL_COSTS.getValue());
        }
        if ((classBicycleSpecific = way.getTag(this.specificBicycleClass)) != null) {
            weightToPrioMap.put(100.0, this.convertCallValueToPriority(classBicycleSpecific).getValue());
        } else {
            String classBicycle = way.getTag("class:bicycle");
            if (classBicycle != null) {
                weightToPrioMap.put(100.0, this.convertCallValueToPriority(classBicycle).getValue());
            }
        }
    }

    long handleBikeRelated(OSMWay way, long encoded, boolean partOfCycleRelation) {
        String surfaceTag = way.getTag("surface");
        String highway = way.getTag("highway");
        String trackType = way.getTag("tracktype");
        WayType wayType = WayType.OTHER_SMALL_WAY;
        boolean isPusingSection = this.isPushingSection(way);
        if (isPusingSection && !partOfCycleRelation || "steps".equals(highway)) {
            wayType = WayType.PUSHING_SECTION;
        }
        if ("track".equals(highway) && (trackType == null || !"grade1".equals(trackType)) || "path".equals(highway) && surfaceTag == null || this.unpavedSurfaceTags.contains(surfaceTag)) {
            encoded = this.setBool(encoded, 100, true);
        }
        if (way.hasTag("bicycle", this.intendedValues)) {
            wayType = isPusingSection && !way.hasTag("bicycle", (Object)"designated") ? WayType.OTHER_SMALL_WAY : WayType.CYCLEWAY;
        } else if ("cycleway".equals(highway)) {
            wayType = WayType.CYCLEWAY;
        } else if (this.roadValues.contains(highway)) {
            wayType = WayType.ROAD;
        }
        return this.wayTypeEncoder.setValue(encoded, wayType.getValue());
    }

    @Override
    public long setBool(long flags, int key, boolean value) {
        switch (key) {
            case 100: {
                return value ? flags | this.unpavedBit : flags & (this.unpavedBit ^ -1);
            }
        }
        return super.setBool(flags, key, value);
    }

    @Override
    public boolean isBool(long flags, int key) {
        switch (key) {
            case 100: {
                return (flags & this.unpavedBit) != 0;
            }
        }
        return super.isBool(flags, key);
    }

    @Override
    public double getDouble(long flags, int key) {
        switch (key) {
            case 101: {
                return (double)this.priorityWayEncoder.getValue(flags) / (double)PriorityCode.BEST.getValue();
            }
        }
        return super.getDouble(flags, key);
    }

    boolean isPushingSection(OSMWay way) {
        return way.hasTag("highway", this.pushingSections) || way.hasTag("railway", (Object)"platform");
    }

    protected long handleSpeed(OSMWay way, double speed, long encoded) {
        boolean isOneway;
        encoded = this.setSpeed(encoded, speed);
        boolean bl = isOneway = way.hasTag("oneway", this.oneways) || way.hasTag("oneway:bicycle", this.oneways) || way.hasTag("vehicle:backward", new String[0]) || way.hasTag("vehicle:forward", new String[0]) || way.hasTag("bicycle:forward", new String[0]);
        if (!(!isOneway && !way.hasTag("junction", (Object)"roundabout") || way.hasTag("oneway:bicycle", (Object)"no") || way.hasTag("bicycle:backward", new String[0]) || way.hasTag("cycleway", this.oppositeLanes))) {
            boolean isBackward;
            boolean bl2 = isBackward = way.hasTag("oneway", (Object)"-1") || way.hasTag("oneway:bicycle", (Object)"-1") || way.hasTag("vehicle:forward", (Object)"no") || way.hasTag("bicycle:forward", (Object)"no");
            encoded = isBackward ? (encoded |= this.backwardBit) : (encoded |= this.forwardBit);
        } else {
            encoded |= this.directionBitMask;
        }
        return encoded;
    }

    protected void setHighwaySpeed(String highway, int speed) {
        this.highwaySpeeds.put(highway, speed);
    }

    protected int getHighwaySpeed(String key) {
        return this.highwaySpeeds.get(key);
    }

    void setTrackTypeSpeed(String tracktype, int speed) {
        this.trackTypeSpeeds.put(tracktype, speed);
    }

    void setSurfaceSpeed(String surface, int speed) {
        this.surfaceSpeeds.put(surface, speed);
    }

    void setCyclingNetworkPreference(String network, int code) {
        this.bikeNetworkToCode.put(network, code);
    }

    void addPushingSection(String highway) {
        this.pushingSections.add(highway);
    }

    @Override
    public boolean supports(Class<?> feature) {
        if (super.supports(feature)) {
            return true;
        }
        return PriorityWeighting.class.isAssignableFrom(feature);
    }

    public void setAvoidSpeedLimit(int limit) {
        this.avoidSpeedLimit = limit;
    }

    public void setSpecificBicycleClass(String subkey) {
        this.specificBicycleClass = "class:bicycle:" + subkey.toString();
    }

    private static enum WayType {
        ROAD(0),
        PUSHING_SECTION(1),
        CYCLEWAY(2),
        OTHER_SMALL_WAY(3);
        
        private final int value;

        private WayType(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

}

