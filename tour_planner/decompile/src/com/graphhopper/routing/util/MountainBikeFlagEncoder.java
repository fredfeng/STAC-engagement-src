/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing.util;

import com.graphhopper.reader.OSMRelation;
import com.graphhopper.reader.OSMWay;
import com.graphhopper.routing.util.BikeCommonFlagEncoder;
import com.graphhopper.routing.util.EncodedValue;
import com.graphhopper.routing.util.PriorityCode;
import com.graphhopper.util.PMap;
import java.util.Set;
import java.util.TreeMap;

public class MountainBikeFlagEncoder
extends BikeCommonFlagEncoder {
    public MountainBikeFlagEncoder() {
        this(4, 2.0, 0);
    }

    public MountainBikeFlagEncoder(PMap properties) {
        this((int)properties.getLong("speedBits", 4), properties.getDouble("speedFactor", 2.0), properties.getBool("turnCosts", false) ? 1 : 0);
        this.properties = properties;
        this.setBlockFords(properties.getBool("blockFords", true));
    }

    public MountainBikeFlagEncoder(String propertiesStr) {
        this(new PMap(propertiesStr));
    }

    public MountainBikeFlagEncoder(int speedBits, double speedFactor, int maxTurnCosts) {
        super(speedBits, speedFactor, maxTurnCosts);
        this.setTrackTypeSpeed("grade1", 18);
        this.setTrackTypeSpeed("grade2", 16);
        this.setTrackTypeSpeed("grade3", 12);
        this.setTrackTypeSpeed("grade4", 8);
        this.setTrackTypeSpeed("grade5", 6);
        this.setSurfaceSpeed("paved", 18);
        this.setSurfaceSpeed("asphalt", 18);
        this.setSurfaceSpeed("cobblestone", 10);
        this.setSurfaceSpeed("cobblestone:flattened", 10);
        this.setSurfaceSpeed("sett", 10);
        this.setSurfaceSpeed("concrete", 14);
        this.setSurfaceSpeed("concrete:lanes", 16);
        this.setSurfaceSpeed("concrete:plates", 16);
        this.setSurfaceSpeed("paving_stones", 16);
        this.setSurfaceSpeed("paving_stones:30", 16);
        this.setSurfaceSpeed("unpaved", 14);
        this.setSurfaceSpeed("compacted", 14);
        this.setSurfaceSpeed("dirt", 14);
        this.setSurfaceSpeed("earth", 14);
        this.setSurfaceSpeed("fine_gravel", 18);
        this.setSurfaceSpeed("grass", 14);
        this.setSurfaceSpeed("grass_paver", 14);
        this.setSurfaceSpeed("gravel", 16);
        this.setSurfaceSpeed("ground", 16);
        this.setSurfaceSpeed("ice", 2);
        this.setSurfaceSpeed("metal", 10);
        this.setSurfaceSpeed("mud", 12);
        this.setSurfaceSpeed("pebblestone", 12);
        this.setSurfaceSpeed("salt", 12);
        this.setSurfaceSpeed("sand", 10);
        this.setSurfaceSpeed("wood", 10);
        this.setHighwaySpeed("living_street", 6);
        this.setHighwaySpeed("steps", 4);
        this.setHighwaySpeed("cycleway", 18);
        this.setHighwaySpeed("path", 18);
        this.setHighwaySpeed("footway", 6);
        this.setHighwaySpeed("pedestrian", 6);
        this.setHighwaySpeed("road", 12);
        this.setHighwaySpeed("track", 18);
        this.setHighwaySpeed("service", 14);
        this.setHighwaySpeed("unclassified", 16);
        this.setHighwaySpeed("residential", 16);
        this.setHighwaySpeed("trunk", 18);
        this.setHighwaySpeed("trunk_link", 18);
        this.setHighwaySpeed("primary", 18);
        this.setHighwaySpeed("primary_link", 18);
        this.setHighwaySpeed("secondary", 18);
        this.setHighwaySpeed("secondary_link", 18);
        this.setHighwaySpeed("tertiary", 18);
        this.setHighwaySpeed("tertiary_link", 18);
        this.addPushingSection("footway");
        this.addPushingSection("pedestrian");
        this.addPushingSection("steps");
        this.setCyclingNetworkPreference("icn", PriorityCode.PREFER.getValue());
        this.setCyclingNetworkPreference("ncn", PriorityCode.PREFER.getValue());
        this.setCyclingNetworkPreference("rcn", PriorityCode.PREFER.getValue());
        this.setCyclingNetworkPreference("lcn", PriorityCode.PREFER.getValue());
        this.setCyclingNetworkPreference("mtb", PriorityCode.BEST.getValue());
        this.addPushingSection("footway");
        this.addPushingSection("pedestrian");
        this.addPushingSection("steps");
        this.avoidHighwayTags.add("primary");
        this.avoidHighwayTags.add("primary_link");
        this.avoidHighwayTags.add("secondary");
        this.avoidHighwayTags.add("secondary_link");
        this.preferHighwayTags.add("road");
        this.preferHighwayTags.add("track");
        this.preferHighwayTags.add("path");
        this.preferHighwayTags.add("service");
        this.preferHighwayTags.add("tertiary");
        this.preferHighwayTags.add("tertiary_link");
        this.preferHighwayTags.add("residential");
        this.preferHighwayTags.add("unclassified");
        this.potentialBarriers.add("kissing_gate");
        this.setSpecificBicycleClass("mtb");
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    void collect(OSMWay way, TreeMap<Double, Integer> weightToPrioMap) {
        super.collect(way, weightToPrioMap);
        String highway = way.getTag("highway");
        if ("track".equals(highway)) {
            String trackType = way.getTag("tracktype");
            if ("grade1".equals(trackType)) {
                weightToPrioMap.put(50.0, PriorityCode.UNCHANGED.getValue());
            } else if (trackType == null) {
                weightToPrioMap.put(90.0, PriorityCode.PREFER.getValue());
            } else if (trackType.startsWith("grade")) {
                weightToPrioMap.put(100.0, PriorityCode.VERY_NICE.getValue());
            }
        }
    }

    @Override
    public long handleRelationTags(OSMRelation relation, long oldRelationFlags) {
        int oldCode;
        oldRelationFlags = super.handleRelationTags(relation, oldRelationFlags);
        int code = 0;
        if (relation.hasTag("route", (Object)"mtb")) {
            code = PriorityCode.PREFER.getValue();
        }
        if ((oldCode = (int)this.relationCodeEncoder.getValue(oldRelationFlags)) < code) {
            return this.relationCodeEncoder.setValue(0, code);
        }
        return oldRelationFlags;
    }

    @Override
    boolean allowedSacScale(String sacScale) {
        return "hiking".equals(sacScale) || "mountain_hiking".equals(sacScale) || "demanding_mountain_hiking".equals(sacScale) || "alpine_hiking".equals(sacScale);
    }

    public String toString() {
        return "mtb";
    }
}

