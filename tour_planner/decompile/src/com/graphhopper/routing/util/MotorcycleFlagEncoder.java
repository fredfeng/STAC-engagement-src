/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing.util;

import com.graphhopper.reader.OSMWay;
import com.graphhopper.routing.util.CarFlagEncoder;
import com.graphhopper.routing.util.EncodedDoubleValue;
import com.graphhopper.routing.util.EncodedValue;
import com.graphhopper.routing.util.PriorityCode;
import com.graphhopper.routing.util.PriorityWeighting;
import com.graphhopper.util.BitUtil;
import com.graphhopper.util.PMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MotorcycleFlagEncoder
extends CarFlagEncoder {
    private EncodedDoubleValue reverseSpeedEncoder;
    private EncodedValue priorityWayEncoder;
    private final HashSet<String> avoidSet = new HashSet();
    private final HashSet<String> preferSet = new HashSet();

    public MotorcycleFlagEncoder(PMap properties) {
        this((int)properties.getLong("speedBits", 5), properties.getDouble("speedFactor", 5.0), properties.getBool("turnCosts", false) ? 1 : 0);
        this.properties = properties;
        this.setBlockFords(properties.getBool("blockFords", true));
    }

    public MotorcycleFlagEncoder(String propertiesStr) {
        this(new PMap(propertiesStr));
    }

    public MotorcycleFlagEncoder(int speedBits, double speedFactor, int maxTurnCosts) {
        super(speedBits, speedFactor, maxTurnCosts);
        this.restrictions.remove("motorcar");
        this.restrictions.add("motorcycle");
        this.trackTypeSpeedMap.clear();
        this.defaultSpeedMap.clear();
        this.trackTypeSpeedMap.put("grade1", 20);
        this.trackTypeSpeedMap.put("grade2", 15);
        this.trackTypeSpeedMap.put("grade3", 10);
        this.trackTypeSpeedMap.put("grade4", 5);
        this.trackTypeSpeedMap.put("grade5", 5);
        this.avoidSet.add("motorway");
        this.avoidSet.add("trunk");
        this.avoidSet.add("motorroad");
        this.preferSet.add("primary");
        this.preferSet.add("secondary");
        this.maxPossibleSpeed = 120;
        this.defaultSpeedMap.put("motorway", 100);
        this.defaultSpeedMap.put("motorway_link", 70);
        this.defaultSpeedMap.put("motorroad", 90);
        this.defaultSpeedMap.put("trunk", 80);
        this.defaultSpeedMap.put("trunk_link", 75);
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
        this.reverseSpeedEncoder = new EncodedDoubleValue("Reverse Speed", shift, this.speedBits, this.speedFactor, ((Integer)this.defaultSpeedMap.get("secondary")).intValue(), this.maxPossibleSpeed);
        this.priorityWayEncoder = new EncodedValue("PreferWay", shift += this.reverseSpeedEncoder.getBits(), 3, 1.0, 3, 7);
        return shift += this.reverseSpeedEncoder.getBits();
    }

    @Override
    public long acceptWay(OSMWay way) {
        String tt;
        String highwayValue = way.getTag("highway");
        if (highwayValue == null) {
            if (way.hasTag("route", this.ferries)) {
                String motorcycleTag = way.getTag("motorcycle");
                if (motorcycleTag == null) {
                    motorcycleTag = way.getTag("motor_vehicle");
                }
                if (motorcycleTag == null && !way.hasTag("foot", new String[0]) && !way.hasTag("bicycle", new String[0]) || "yes".equals(motorcycleTag)) {
                    return this.acceptBit | this.ferryBit;
                }
            }
            return 0;
        }
        if ("track".equals(highwayValue) && (tt = way.getTag("tracktype")) != null && !tt.equals("grade1")) {
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
    public long handleWayTags(OSMWay way, long allowed, long priorityFromRelation) {
        if (!this.isAccept(allowed)) {
            return 0;
        }
        long encoded = 0;
        if (!this.isFerry(allowed)) {
            boolean isRoundabout;
            double speed = this.getSpeed(way);
            speed = this.applyMaxSpeed(way, speed, true);
            double maxMCSpeed = this.parseSpeed(way.getTag("maxspeed:motorcycle"));
            if (maxMCSpeed > 0.0 && maxMCSpeed < speed) {
                speed = maxMCSpeed * 0.9;
            }
            if (speed > 30.0 && way.hasTag("surface", this.badSurfaceSpeedMap)) {
                speed = 30.0;
            }
            if (isRoundabout = way.hasTag("junction", (Object)"roundabout")) {
                encoded = this.setBool(0, 2, true);
            }
            if (way.hasTag("oneway", this.oneways) || isRoundabout) {
                if (way.hasTag("oneway", (Object)"-1")) {
                    encoded = this.setReverseSpeed(encoded, speed);
                    encoded |= this.backwardBit;
                } else {
                    encoded = this.setSpeed(encoded, speed);
                    encoded |= this.forwardBit;
                }
            } else {
                encoded = this.setSpeed(encoded, speed);
                encoded = this.setReverseSpeed(encoded, speed);
                encoded |= this.directionBitMask;
            }
        } else {
            encoded = this.handleFerryTags(way, ((Integer)this.defaultSpeedMap.get("living_street")).intValue(), ((Integer)this.defaultSpeedMap.get("service")).intValue(), ((Integer)this.defaultSpeedMap.get("residential")).intValue());
            encoded |= this.directionBitMask;
        }
        encoded = this.priorityWayEncoder.setValue(encoded, this.handlePriority(way, priorityFromRelation));
        return encoded;
    }

    @Override
    public double getReverseSpeed(long flags) {
        return this.reverseSpeedEncoder.getDoubleValue(flags);
    }

    @Override
    public long setReverseSpeed(long flags, double speed) {
        if (speed < 0.0) {
            throw new IllegalArgumentException("Speed cannot be negative: " + speed + ", flags:" + BitUtil.LITTLE.toBitString(flags));
        }
        if (speed < this.speedEncoder.factor / 2.0) {
            return this.setLowSpeed(flags, speed, true);
        }
        if (speed > this.getMaxSpeed()) {
            speed = this.getMaxSpeed();
        }
        return this.reverseSpeedEncoder.setDoubleValue(flags, speed);
    }

    @Override
    protected long setLowSpeed(long flags, double speed, boolean reverse) {
        if (reverse) {
            return this.setBool(this.reverseSpeedEncoder.setDoubleValue(flags, 0.0), 1, false);
        }
        return this.setBool(this.speedEncoder.setDoubleValue(flags, 0.0), 0, false);
    }

    @Override
    public long flagsDefault(boolean forward, boolean backward) {
        long flags = super.flagsDefault(forward, backward);
        if (backward) {
            return this.reverseSpeedEncoder.setDefaultValue(flags);
        }
        return flags;
    }

    @Override
    public long setProperties(double speed, boolean forward, boolean backward) {
        long flags = super.setProperties(speed, forward, backward);
        if (backward) {
            return this.setReverseSpeed(flags, speed);
        }
        return flags;
    }

    @Override
    public long reverseFlags(long flags) {
        flags = super.reverseFlags(flags);
        double otherValue = this.reverseSpeedEncoder.getDoubleValue(flags);
        flags = this.setReverseSpeed(flags, this.speedEncoder.getDoubleValue(flags));
        return this.setSpeed(flags, otherValue);
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

    private int handlePriority(OSMWay way, long relationFlags) {
        String highway = way.getTag("highway", "");
        if (this.avoidSet.contains(highway)) {
            return PriorityCode.AVOID_AT_ALL_COSTS.getValue();
        }
        if (this.preferSet.contains(highway)) {
            return PriorityCode.VERY_NICE.getValue();
        }
        return PriorityCode.UNCHANGED.getValue();
    }

    @Override
    public boolean supports(Class<?> feature) {
        if (super.supports(feature)) {
            return true;
        }
        return PriorityWeighting.class.isAssignableFrom(feature);
    }

    @Override
    public String toString() {
        return "motorcycle";
    }
}

