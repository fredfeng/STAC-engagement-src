/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing.util;

import com.graphhopper.reader.OSMRelation;
import com.graphhopper.reader.OSMWay;
import com.graphhopper.routing.util.AbstractFlagEncoder;
import com.graphhopper.routing.util.EncodedDoubleValue;
import com.graphhopper.util.Helper;
import com.graphhopper.util.PMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CarFlagEncoder
extends AbstractFlagEncoder {
    protected final Map<String, Integer> trackTypeSpeedMap = new HashMap<String, Integer>();
    protected final Set<String> badSurfaceSpeedMap = new HashSet<String>();
    protected final Map<String, Integer> defaultSpeedMap = new HashMap<String, Integer>();

    public CarFlagEncoder() {
        this(5, 5.0, 0);
    }

    public CarFlagEncoder(PMap properties) {
        this((int)properties.getLong("speedBits", 5), properties.getDouble("speedFactor", 5.0), properties.getBool("turnCosts", false) ? 1 : 0);
        this.properties = properties;
        this.setBlockFords(properties.getBool("blockFords", true));
    }

    public CarFlagEncoder(String propertiesStr) {
        this(new PMap(propertiesStr));
    }

    public CarFlagEncoder(int speedBits, double speedFactor, int maxTurnCosts) {
        super(speedBits, speedFactor, maxTurnCosts);
        this.restrictions.addAll(Arrays.asList("motorcar", "motor_vehicle", "vehicle", "access"));
        this.restrictedValues.add("private");
        this.restrictedValues.add("agricultural");
        this.restrictedValues.add("forestry");
        this.restrictedValues.add("no");
        this.restrictedValues.add("restricted");
        this.restrictedValues.add("delivery");
        this.restrictedValues.add("military");
        this.intendedValues.add("yes");
        this.intendedValues.add("permissive");
        this.potentialBarriers.add("gate");
        this.potentialBarriers.add("lift_gate");
        this.potentialBarriers.add("kissing_gate");
        this.potentialBarriers.add("swing_gate");
        this.absoluteBarriers.add("bollard");
        this.absoluteBarriers.add("stile");
        this.absoluteBarriers.add("turnstile");
        this.absoluteBarriers.add("cycle_barrier");
        this.absoluteBarriers.add("motorcycle_barrier");
        this.absoluteBarriers.add("block");
        this.trackTypeSpeedMap.put("grade1", 20);
        this.trackTypeSpeedMap.put("grade2", 15);
        this.trackTypeSpeedMap.put("grade3", 10);
        this.trackTypeSpeedMap.put("grade4", 5);
        this.trackTypeSpeedMap.put("grade5", 5);
        this.badSurfaceSpeedMap.add("cobblestone");
        this.badSurfaceSpeedMap.add("grass_paver");
        this.badSurfaceSpeedMap.add("gravel");
        this.badSurfaceSpeedMap.add("sand");
        this.badSurfaceSpeedMap.add("paving_stones");
        this.badSurfaceSpeedMap.add("dirt");
        this.badSurfaceSpeedMap.add("ground");
        this.badSurfaceSpeedMap.add("grass");
        this.maxPossibleSpeed = 140;
        this.defaultSpeedMap.put("motorway", 100);
        this.defaultSpeedMap.put("motorway_link", 70);
        this.defaultSpeedMap.put("motorroad", 90);
        this.defaultSpeedMap.put("trunk", 70);
        this.defaultSpeedMap.put("trunk_link", 65);
        this.defaultSpeedMap.put("primary", 65);
        this.defaultSpeedMap.put("primary_link", 60);
        this.defaultSpeedMap.put("secondary", 60);
        this.defaultSpeedMap.put("secondary_link", 50);
        this.defaultSpeedMap.put("tertiary", 50);
        this.defaultSpeedMap.put("tertiary_link", 40);
        this.defaultSpeedMap.put("unclassified", 30);
        this.defaultSpeedMap.put("residential", 30);
        this.defaultSpeedMap.put("living_street", 5);
        this.defaultSpeedMap.put("service", 20);
        this.defaultSpeedMap.put("road", 20);
        this.defaultSpeedMap.put("track", 15);
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public int defineWayBits(int index, int shift) {
        shift = super.defineWayBits(index, shift);
        this.speedEncoder = new EncodedDoubleValue("Speed", shift, this.speedBits, this.speedFactor, this.defaultSpeedMap.get("secondary").intValue(), this.maxPossibleSpeed);
        return shift + this.speedEncoder.getBits();
    }

    protected double getSpeed(OSMWay way) {
        Integer tInt;
        String tt;
        String highwayValue = way.getTag("highway");
        Integer speed = this.defaultSpeedMap.get(highwayValue);
        if (speed == null) {
            throw new IllegalStateException(this.toString() + ", no speed found for: " + highwayValue + ", tags: " + way);
        }
        if (highwayValue.equals("track") && !Helper.isEmpty(tt = way.getTag("tracktype")) && (tInt = this.trackTypeSpeedMap.get(tt)) != null) {
            speed = tInt;
        }
        return speed.intValue();
    }

    @Override
    public long acceptWay(OSMWay way) {
        String tt;
        String highwayValue = way.getTag("highway");
        if (highwayValue == null) {
            if (way.hasTag("route", this.ferries)) {
                String motorcarTag = way.getTag("motorcar");
                if (motorcarTag == null) {
                    motorcarTag = way.getTag("motor_vehicle");
                }
                if (motorcarTag == null && !way.hasTag("foot", new String[0]) && !way.hasTag("bicycle", new String[0]) || "yes".equals(motorcarTag)) {
                    return this.acceptBit | this.ferryBit;
                }
            }
            return 0;
        }
        if ("track".equals(highwayValue) && (tt = way.getTag("tracktype")) != null && !tt.equals("grade1") && !tt.equals("grade2") && !tt.equals("grade3")) {
            return 0;
        }
        if (!this.defaultSpeedMap.containsKey(highwayValue)) {
            return 0;
        }
        if (way.hasTag("impassable", (Object)"yes") || way.hasTag("status", (Object)"impassable")) {
            return 0;
        }
        String firstValue = way.getFirstPriorityTag(this.restrictions);
        if (!firstValue.isEmpty()) {
            if (this.restrictedValues.contains(firstValue)) {
                return 0;
            }
            if (this.intendedValues.contains(firstValue)) {
                return this.acceptBit;
            }
        }
        if (this.isBlockFords() && ("ford".equals(highwayValue) || way.hasTag("ford", new String[0]))) {
            return 0;
        }
        if (way.hasTag("railway", new String[0]) && !way.hasTag("railway", this.acceptedRailways)) {
            return 0;
        }
        return this.acceptBit;
    }

    @Override
    public long handleRelationTags(OSMRelation relation, long oldRelationFlags) {
        return oldRelationFlags;
    }

    @Override
    public long handleWayTags(OSMWay way, long allowed, long relationFlags) {
        long encoded;
        if (!this.isAccept(allowed)) {
            return 0;
        }
        if (!this.isFerry(allowed)) {
            boolean isOneway;
            double speed = this.getSpeed(way);
            if ((speed = this.applyMaxSpeed(way, speed, true)) > 30.0 && way.hasTag("surface", this.badSurfaceSpeedMap)) {
                speed = 30.0;
            }
            encoded = this.setSpeed(0, speed);
            boolean isRoundabout = way.hasTag("junction", (Object)"roundabout");
            if (isRoundabout) {
                encoded = this.setBool(encoded, 2, true);
            }
            boolean bl = isOneway = way.hasTag("oneway", this.oneways) || way.hasTag("vehicle:backward", new String[0]) || way.hasTag("vehicle:forward", new String[0]) || way.hasTag("motor_vehicle:backward", new String[0]) || way.hasTag("motor_vehicle:forward", new String[0]);
            if (isOneway || isRoundabout) {
                boolean isBackward;
                boolean bl2 = isBackward = way.hasTag("oneway", (Object)"-1") || way.hasTag("vehicle:forward", (Object)"no") || way.hasTag("motor_vehicle:forward", (Object)"no");
                encoded = isBackward ? (encoded |= this.backwardBit) : (encoded |= this.forwardBit);
            } else {
                encoded |= this.directionBitMask;
            }
        } else {
            encoded = this.handleFerryTags(way, this.defaultSpeedMap.get("living_street").intValue(), this.defaultSpeedMap.get("service").intValue(), this.defaultSpeedMap.get("residential").intValue());
            encoded |= this.directionBitMask;
        }
        return encoded;
    }

    public String getWayInfo(OSMWay way) {
        String destination;
        String str = "";
        String highwayValue = way.getTag("highway");
        if ("motorway_link".equals(highwayValue) && !Helper.isEmpty(destination = way.getTag("destination"))) {
            int counter = 0;
            for (String d : destination.split(";")) {
                if (d.trim().isEmpty()) continue;
                if (counter > 0) {
                    str = str + ", ";
                }
                str = str + d.trim();
                ++counter;
            }
        }
        if (str.isEmpty()) {
            return str;
        }
        if (str.contains(",")) {
            return "destinations: " + str;
        }
        return "destination: " + str;
    }

    public String toString() {
        return "car";
    }
}

