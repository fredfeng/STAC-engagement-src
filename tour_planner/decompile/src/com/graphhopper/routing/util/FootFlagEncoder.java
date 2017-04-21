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
import com.graphhopper.util.PMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class FootFlagEncoder
extends AbstractFlagEncoder {
    static final int SLOW_SPEED = 2;
    static final int MEAN_SPEED = 5;
    static final int FERRY_SPEED = 10;
    private EncodedValue priorityWayEncoder;
    private EncodedValue relationCodeEncoder;
    protected HashSet<String> sidewalks = new HashSet();
    private final Set<String> safeHighwayTags = new HashSet<String>();
    private final Set<String> allowedHighwayTags = new HashSet<String>();
    private final Set<String> avoidHighwayTags = new HashSet<String>();
    private final Map<String, Integer> hikingNetworkToCode = new HashMap<String, Integer>();

    public FootFlagEncoder() {
        this(4, 1.0);
    }

    public FootFlagEncoder(PMap properties) {
        this((int)properties.getLong("speedBits", 4), properties.getDouble("speedFactor", 1.0));
        this.properties = properties;
        this.setBlockFords(properties.getBool("blockFords", true));
    }

    public FootFlagEncoder(String propertiesStr) {
        this(new PMap(propertiesStr));
    }

    public FootFlagEncoder(int speedBits, double speedFactor) {
        super(speedBits, speedFactor, 0);
        this.restrictions.addAll(Arrays.asList("foot", "access"));
        this.restrictedValues.add("private");
        this.restrictedValues.add("no");
        this.restrictedValues.add("restricted");
        this.restrictedValues.add("military");
        this.intendedValues.add("yes");
        this.intendedValues.add("designated");
        this.intendedValues.add("official");
        this.intendedValues.add("permissive");
        this.sidewalks.add("yes");
        this.sidewalks.add("both");
        this.sidewalks.add("left");
        this.sidewalks.add("right");
        this.setBlockByDefault(false);
        this.potentialBarriers.add("gate");
        this.acceptedRailways.add("platform");
        this.safeHighwayTags.add("footway");
        this.safeHighwayTags.add("path");
        this.safeHighwayTags.add("steps");
        this.safeHighwayTags.add("pedestrian");
        this.safeHighwayTags.add("living_street");
        this.safeHighwayTags.add("track");
        this.safeHighwayTags.add("residential");
        this.safeHighwayTags.add("service");
        this.avoidHighwayTags.add("trunk");
        this.avoidHighwayTags.add("trunk_link");
        this.avoidHighwayTags.add("primary");
        this.avoidHighwayTags.add("primary_link");
        this.avoidHighwayTags.add("secondary");
        this.avoidHighwayTags.add("secondary_link");
        this.avoidHighwayTags.add("tertiary");
        this.avoidHighwayTags.add("tertiary_link");
        this.allowedHighwayTags.addAll(this.safeHighwayTags);
        this.allowedHighwayTags.addAll(this.avoidHighwayTags);
        this.allowedHighwayTags.add("cycleway");
        this.allowedHighwayTags.add("unclassified");
        this.allowedHighwayTags.add("road");
        this.hikingNetworkToCode.put("iwn", PriorityCode.BEST.getValue());
        this.hikingNetworkToCode.put("nwn", PriorityCode.BEST.getValue());
        this.hikingNetworkToCode.put("rwn", PriorityCode.VERY_NICE.getValue());
        this.hikingNetworkToCode.put("lwn", PriorityCode.VERY_NICE.getValue());
        this.maxPossibleSpeed = 10;
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public int defineWayBits(int index, int shift) {
        shift = super.defineWayBits(index, shift);
        this.speedEncoder = new EncodedDoubleValue("Speed", shift, this.speedBits, this.speedFactor, 5, this.maxPossibleSpeed);
        this.priorityWayEncoder = new EncodedValue("PreferWay", shift += this.speedEncoder.getBits(), 3, 1.0, 0, 7);
        return shift += this.priorityWayEncoder.getBits();
    }

    @Override
    public int defineRelationBits(int index, int shift) {
        this.relationCodeEncoder = new EncodedValue("RelationCode", shift, 3, 1.0, 0, 7);
        return shift + this.relationCodeEncoder.getBits();
    }

    @Override
    public int defineTurnBits(int index, int shift) {
        return shift;
    }

    @Override
    public boolean isTurnRestricted(long flag) {
        return false;
    }

    @Override
    public double getTurnCost(long flag) {
        return 0.0;
    }

    @Override
    public long getTurnFlags(boolean restricted, double costs) {
        return 0;
    }

    @Override
    public long acceptWay(OSMWay way) {
        String highwayValue = way.getTag("highway");
        if (highwayValue == null) {
            String footTag;
            if (way.hasTag("route", this.ferries) && ((footTag = way.getTag("foot")) == null || "yes".equals(footTag))) {
                return this.acceptBit | this.ferryBit;
            }
            if (way.hasTag("railway", (Object)"platform")) {
                return this.acceptBit;
            }
            return 0;
        }
        String sacScale = way.getTag("sac_scale");
        if (!(sacScale == null || "hiking".equals(sacScale) || "mountain_hiking".equals(sacScale) || "demanding_mountain_hiking".equals(sacScale) || "alpine_hiking".equals(sacScale))) {
            return 0;
        }
        if (way.hasTag("sidewalk", this.sidewalks)) {
            return this.acceptBit;
        }
        if (way.hasTag("foot", this.intendedValues)) {
            return this.acceptBit;
        }
        if (!this.allowedHighwayTags.contains(highwayValue)) {
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
        return this.acceptBit;
    }

    @Override
    public long handleRelationTags(OSMRelation relation, long oldRelationFlags) {
        int code = 0;
        if (relation.hasTag("route", (Object)"hiking") || relation.hasTag("route", (Object)"foot")) {
            Integer val = this.hikingNetworkToCode.get(relation.getTag("network"));
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
            String sacScale = way.getTag("sac_scale");
            encoded = sacScale != null ? ("hiking".equals(sacScale) ? this.speedEncoder.setDoubleValue(encoded, 5.0) : this.speedEncoder.setDoubleValue(encoded, 2.0)) : this.speedEncoder.setDoubleValue(encoded, 5.0);
            encoded |= this.directionBitMask;
            boolean isRoundabout = way.hasTag("junction", (Object)"roundabout");
            if (isRoundabout) {
                encoded = this.setBool(encoded, 2, true);
            }
        } else {
            encoded |= this.handleFerryTags(way, 2.0, 5.0, 10.0);
            encoded |= this.directionBitMask;
        }
        int priorityFromRelation = 0;
        if (relationFlags != 0) {
            priorityFromRelation = (int)this.relationCodeEncoder.getValue(relationFlags);
        }
        encoded = this.priorityWayEncoder.setValue(encoded, this.handlePriority(way, priorityFromRelation));
        return encoded;
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

    void collect(OSMWay way, TreeMap<Double, Integer> weightToPrioMap) {
        String highway = way.getTag("highway");
        if (way.hasTag("foot", (Object)"designated")) {
            weightToPrioMap.put(100.0, PriorityCode.PREFER.getValue());
        }
        double maxSpeed = this.getMaxSpeed(way);
        if (this.safeHighwayTags.contains(highway) || maxSpeed > 0.0 && maxSpeed <= 20.0) {
            weightToPrioMap.put(40.0, PriorityCode.PREFER.getValue());
            if (way.hasTag("tunnel", this.intendedValues)) {
                if (way.hasTag("sidewalk", (Object)"no")) {
                    weightToPrioMap.put(40.0, PriorityCode.REACH_DEST.getValue());
                } else {
                    weightToPrioMap.put(40.0, PriorityCode.UNCHANGED.getValue());
                }
            }
        } else if (maxSpeed > 50.0 || this.avoidHighwayTags.contains(highway)) {
            if (way.hasTag("sidewalk", (Object)"no")) {
                weightToPrioMap.put(45.0, PriorityCode.WORST.getValue());
            } else {
                weightToPrioMap.put(45.0, PriorityCode.REACH_DEST.getValue());
            }
        }
        if (way.hasTag("bicycle", (Object)"official") || way.hasTag("bicycle", (Object)"designated")) {
            weightToPrioMap.put(44.0, PriorityCode.AVOID_IF_POSSIBLE.getValue());
        }
    }

    @Override
    public boolean supports(Class<?> feature) {
        if (super.supports(feature)) {
            return true;
        }
        return PriorityWeighting.class.isAssignableFrom(feature);
    }

    public String toString() {
        return "foot";
    }
}

