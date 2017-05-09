/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing.util;

import com.graphhopper.reader.OSMNode;
import com.graphhopper.reader.OSMRelation;
import com.graphhopper.reader.OSMWay;
import com.graphhopper.routing.util.EncodedDoubleValue;
import com.graphhopper.routing.util.EncodedValue;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.routing.util.TurnCostEncoder;
import com.graphhopper.routing.util.TurnWeighting;
import com.graphhopper.util.BitUtil;
import com.graphhopper.util.EdgeExplorer;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.Helper;
import com.graphhopper.util.InstructionAnnotation;
import com.graphhopper.util.PMap;
import com.graphhopper.util.Translation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractFlagEncoder
implements FlagEncoder,
TurnCostEncoder {
    private static final Logger logger = LoggerFactory.getLogger(AbstractFlagEncoder.class);
    protected static final int K_FORWARD = 0;
    protected static final int K_BACKWARD = 1;
    private long nodeBitMask;
    private long wayBitMask;
    private long relBitMask;
    protected long forwardBit;
    protected long backwardBit;
    protected long directionBitMask;
    protected long roundaboutBit;
    protected EncodedDoubleValue speedEncoder;
    protected long acceptBit;
    protected long ferryBit;
    protected PMap properties;
    protected int maxPossibleSpeed;
    private EncodedValue turnCostEncoder;
    private long turnRestrictionBit;
    private final int maxTurnCosts;
    protected EdgeExplorer edgeOutExplorer;
    protected EdgeExplorer edgeInExplorer;
    protected final List<String> restrictions = new ArrayList<String>(5);
    protected final Set<String> intendedValues = new HashSet<String>(5);
    protected final Set<String> restrictedValues = new HashSet<String>(5);
    protected final Set<String> ferries = new HashSet<String>(5);
    protected final Set<String> oneways = new HashSet<String>(5);
    protected final Set<String> acceptedRailways = new HashSet<String>(5);
    protected final Set<String> absoluteBarriers = new HashSet<String>(5);
    protected final Set<String> potentialBarriers = new HashSet<String>(5);
    private boolean blockByDefault = true;
    private boolean blockFords = true;
    protected final int speedBits;
    protected final double speedFactor;
    private boolean registered;

    public AbstractFlagEncoder(PMap properties) {
        throw new RuntimeException("This method must be overridden in derived classes");
    }

    public AbstractFlagEncoder(String propertiesStr) {
        this(new PMap(propertiesStr));
    }

    protected AbstractFlagEncoder(int speedBits, double speedFactor, int maxTurnCosts) {
        this.maxTurnCosts = maxTurnCosts <= 0 ? 0 : maxTurnCosts;
        this.speedBits = speedBits;
        this.speedFactor = speedFactor;
        this.oneways.add("yes");
        this.oneways.add("true");
        this.oneways.add("1");
        this.oneways.add("-1");
        this.ferries.add("shuttle_train");
        this.ferries.add("ferry");
        this.acceptedRailways.add("tram");
        this.acceptedRailways.add("abandoned");
        this.acceptedRailways.add("abandoned_tram");
        this.acceptedRailways.add("disused");
        this.acceptedRailways.add("dismantled");
        this.acceptedRailways.add("razed");
        this.acceptedRailways.add("historic");
        this.acceptedRailways.add("obliterated");
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    @Override
    public boolean isRegistered() {
        return this.registered;
    }

    public void setBlockByDefault(boolean blockByDefault) {
        this.blockByDefault = blockByDefault;
    }

    public void setBlockFords(boolean blockFords) {
        this.blockFords = blockFords;
    }

    public boolean isBlockFords() {
        return this.blockFords;
    }

    public int defineNodeBits(int index, int shift) {
        return shift;
    }

    public int defineWayBits(int index, int shift) {
        if (this.isRegistered()) {
            throw new IllegalStateException("You must not register a FlagEncoder (" + this.toString() + ") twice!");
        }
        this.setRegistered(true);
        this.forwardBit = 1 << shift;
        this.backwardBit = 2 << shift;
        this.directionBitMask = 3 << shift;
        this.roundaboutBit = 1 << (shift += 2);
        this.acceptBit = 1 << (index *= 2);
        this.ferryBit = 2 << index;
        return ++shift;
    }

    public int defineRelationBits(int index, int shift) {
        return shift;
    }

    public abstract long handleRelationTags(OSMRelation var1, long var2);

    public abstract long acceptWay(OSMWay var1);

    public abstract long handleWayTags(OSMWay var1, long var2, long var4);

    public long handleNodeTags(OSMNode node) {
        if (node.hasTag("barrier", this.absoluteBarriers)) {
            return this.directionBitMask;
        }
        if (node.hasTag("barrier", this.potentialBarriers)) {
            boolean locked = false;
            if (node.hasTag("locked", (Object)"yes")) {
                locked = true;
            }
            for (String res : this.restrictions) {
                if (!locked && node.hasTag(res, this.intendedValues)) {
                    return 0;
                }
                if (!node.hasTag(res, this.restrictedValues)) continue;
                return this.directionBitMask;
            }
            if (this.blockByDefault) {
                return this.directionBitMask;
            }
        }
        if (this.blockFords && (node.hasTag("highway", (Object)"ford") || node.hasTag("ford", new String[0])) && !node.hasTag(this.restrictions, this.intendedValues) && !node.hasTag("ford", (Object)"no")) {
            return this.directionBitMask;
        }
        return 0;
    }

    @Override
    public InstructionAnnotation getAnnotation(long flags, Translation tr) {
        return InstructionAnnotation.EMPTY;
    }

    public long reverseFlags(long flags) {
        long dir = flags & this.directionBitMask;
        if (dir == this.directionBitMask || dir == 0) {
            return flags;
        }
        return flags ^ this.directionBitMask;
    }

    public long flagsDefault(boolean forward, boolean backward) {
        long flags = this.speedEncoder.setDefaultValue(0);
        return this.setAccess(flags, forward, backward);
    }

    @Override
    public long setAccess(long flags, boolean forward, boolean backward) {
        return this.setBool(this.setBool(flags, 1, backward), 0, forward);
    }

    @Override
    public long setSpeed(long flags, double speed) {
        if (speed < 0.0 || Double.isNaN(speed)) {
            throw new IllegalArgumentException("Speed cannot be negative or NaN: " + speed + ", flags:" + BitUtil.LITTLE.toBitString(flags));
        }
        if (speed < this.speedEncoder.factor / 2.0) {
            return this.setLowSpeed(flags, speed, false);
        }
        if (speed > this.getMaxSpeed()) {
            speed = this.getMaxSpeed();
        }
        return this.speedEncoder.setDoubleValue(flags, speed);
    }

    protected long setLowSpeed(long flags, double speed, boolean reverse) {
        return this.setAccess(this.speedEncoder.setDoubleValue(flags, 0.0), false, false);
    }

    @Override
    public double getSpeed(long flags) {
        double speedVal = this.speedEncoder.getDoubleValue(flags);
        if (speedVal < 0.0) {
            throw new IllegalStateException("Speed was negative!? " + speedVal);
        }
        return speedVal;
    }

    @Override
    public long setReverseSpeed(long flags, double speed) {
        return this.setSpeed(flags, speed);
    }

    @Override
    public double getReverseSpeed(long flags) {
        return this.getSpeed(flags);
    }

    @Override
    public long setProperties(double speed, boolean forward, boolean backward) {
        return this.setAccess(this.setSpeed(0, speed), forward, backward);
    }

    @Override
    public double getMaxSpeed() {
        return this.speedEncoder.getMaxValue();
    }

    protected double getMaxSpeed(OSMWay way) {
        double backSpeed;
        double maxSpeed = this.parseSpeed(way.getTag("maxspeed"));
        double fwdSpeed = this.parseSpeed(way.getTag("maxspeed:forward"));
        if (fwdSpeed >= 0.0 && (maxSpeed < 0.0 || fwdSpeed < maxSpeed)) {
            maxSpeed = fwdSpeed;
        }
        if ((backSpeed = this.parseSpeed(way.getTag("maxspeed:backward"))) >= 0.0 && (maxSpeed < 0.0 || backSpeed < maxSpeed)) {
            maxSpeed = backSpeed;
        }
        return maxSpeed;
    }

    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + (int)this.directionBitMask;
        hash = 61 * hash + this.toString().hashCode();
        return hash;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        AbstractFlagEncoder other = (AbstractFlagEncoder)obj;
        if (this.directionBitMask != other.directionBitMask) {
            return false;
        }
        return this.toString().equals(other.toString());
    }

    protected double parseSpeed(String str) {
        if (Helper.isEmpty(str)) {
            return -1.0;
        }
        if ("none".equals(str)) {
            return 140.0;
        }
        if (str.endsWith(":rural") || str.endsWith(":trunk")) {
            return 80.0;
        }
        if (str.endsWith(":urban")) {
            return 50.0;
        }
        if (str.equals("walk") || str.endsWith(":living_street")) {
            return 6.0;
        }
        try {
            int mpInteger = str.indexOf("mp");
            if (mpInteger > 0) {
                str = str.substring(0, mpInteger).trim();
                int val = Integer.parseInt(str);
                return (double)val * 1.609344;
            }
            int knotInteger = str.indexOf("knots");
            if (knotInteger > 0) {
                str = str.substring(0, knotInteger).trim();
                int val = Integer.parseInt(str);
                return (double)val * 1.852;
            }
            int kmInteger = str.indexOf("km");
            if (kmInteger > 0) {
                str = str.substring(0, kmInteger).trim();
            } else {
                kmInteger = str.indexOf("kph");
                if (kmInteger > 0) {
                    str = str.substring(0, kmInteger).trim();
                }
            }
            return Integer.parseInt(str);
        }
        catch (Exception ex) {
            return -1.0;
        }
    }

    protected static int parseDuration(String str) {
        if (str == null) {
            return 0;
        }
        try {
            if (str.startsWith("P")) {
                return 0;
            }
            int index = str.indexOf(":");
            if (index > 0) {
                String hourStr = str.substring(0, index);
                String minStr = str.substring(index + 1);
                index = minStr.indexOf(":");
                int minutes = 0;
                if (index > 0) {
                    String dayStr = hourStr;
                    hourStr = minStr.substring(0, index);
                    minStr = minStr.substring(index + 1);
                    minutes = Integer.parseInt(dayStr) * 60 * 24;
                }
                minutes += Integer.parseInt(hourStr) * 60;
                return minutes += Integer.parseInt(minStr);
            }
            return Integer.parseInt(str);
        }
        catch (Exception ex) {
            logger.warn("Cannot parse " + str + " using 0 minutes");
            return 0;
        }
    }

    public void applyWayTags(OSMWay way, EdgeIteratorState edge) {
    }

    protected long handleFerryTags(OSMWay way, double unknownSpeed, double shortTripsSpeed, double longTripsSpeed) {
        double durationInHours = (double)AbstractFlagEncoder.parseDuration(way.getTag("duration")) / 60.0;
        if (durationInHours > 0.0) {
            try {
                Number estimatedLength = way.getTag("estimated_distance", null);
                if (estimatedLength != null) {
                    double val = estimatedLength.doubleValue() / 1000.0;
                    shortTripsSpeed = Math.round(val / durationInHours / 1.4);
                    if (shortTripsSpeed > this.getMaxSpeed()) {
                        shortTripsSpeed = this.getMaxSpeed();
                    }
                    longTripsSpeed = shortTripsSpeed;
                }
            }
            catch (Exception estimatedLength) {
                // empty catch block
            }
        }
        if (durationInHours == 0.0) {
            return this.setSpeed(0, unknownSpeed);
        }
        if (durationInHours > 1.0) {
            return this.setSpeed(0, longTripsSpeed);
        }
        return this.setSpeed(0, shortTripsSpeed);
    }

    void setWayBitMask(int usedBits, int shift) {
        this.wayBitMask = (1 << usedBits) - 1;
        this.wayBitMask <<= shift;
    }

    long getWayBitMask() {
        return this.wayBitMask;
    }

    void setRelBitMask(int usedBits, int shift) {
        this.relBitMask = (1 << usedBits) - 1;
        this.relBitMask <<= shift;
    }

    long getRelBitMask() {
        return this.relBitMask;
    }

    void setNodeBitMask(int usedBits, int shift) {
        this.nodeBitMask = (1 << usedBits) - 1;
        this.nodeBitMask <<= shift;
    }

    long getNodeBitMask() {
        return this.nodeBitMask;
    }

    public int defineTurnBits(int index, int shift) {
        if (this.maxTurnCosts == 0) {
            return shift;
        }
        if (this.maxTurnCosts == 1) {
            this.turnRestrictionBit = 1 << shift;
            return shift + 1;
        }
        int turnBits = Helper.countBitValue(this.maxTurnCosts);
        this.turnCostEncoder = new EncodedValue("TurnCost", shift, turnBits, 1.0, 0, this.maxTurnCosts){

            @Override
            public final long getValue(long flags) {
                flags &= this.mask;
                return flags >>>= (int)this.shift;
            }
        };
        return shift + turnBits;
    }

    @Override
    public boolean isTurnRestricted(long flags) {
        if (this.maxTurnCosts == 0) {
            return false;
        }
        if (this.maxTurnCosts == 1) {
            return (flags & this.turnRestrictionBit) != 0;
        }
        return this.turnCostEncoder.getValue(flags) == (long)this.maxTurnCosts;
    }

    @Override
    public double getTurnCost(long flags) {
        if (this.maxTurnCosts == 0) {
            return 0.0;
        }
        if (this.maxTurnCosts == 1) {
            return (flags & this.turnRestrictionBit) == 0 ? 0.0 : Double.POSITIVE_INFINITY;
        }
        long cost = this.turnCostEncoder.getValue(flags);
        if (cost == (long)this.maxTurnCosts) {
            return Double.POSITIVE_INFINITY;
        }
        return cost;
    }

    @Override
    public long getTurnFlags(boolean restricted, double costs) {
        if (this.maxTurnCosts == 0) {
            return 0;
        }
        if (this.maxTurnCosts == 1) {
            if (costs != 0.0) {
                throw new IllegalArgumentException("Only restrictions are supported");
            }
            return restricted ? this.turnRestrictionBit : 0;
        }
        if (restricted) {
            if (costs != 0.0 || Double.isInfinite(costs)) {
                throw new IllegalArgumentException("Restricted turn can only have infinite costs (or use 0)");
            }
        } else if (costs >= (double)this.maxTurnCosts) {
            throw new IllegalArgumentException("Cost is too high. Or specifiy restricted == true");
        }
        if (costs < 0.0) {
            throw new IllegalArgumentException("Turn costs cannot be negative");
        }
        if (costs >= (double)this.maxTurnCosts || restricted) {
            costs = this.maxTurnCosts;
        }
        return this.turnCostEncoder.setValue(0, (int)costs);
    }

    protected boolean isFerry(long internalFlags) {
        return (internalFlags & this.ferryBit) != 0;
    }

    protected boolean isAccept(long internalFlags) {
        return (internalFlags & this.acceptBit) != 0;
    }

    @Override
    public boolean isBackward(long flags) {
        return (flags & this.backwardBit) != 0;
    }

    @Override
    public boolean isForward(long flags) {
        return (flags & this.forwardBit) != 0;
    }

    @Override
    public long setBool(long flags, int key, boolean value) {
        switch (key) {
            case 0: {
                return value ? flags | this.forwardBit : flags & (this.forwardBit ^ -1);
            }
            case 1: {
                return value ? flags | this.backwardBit : flags & (this.backwardBit ^ -1);
            }
            case 2: {
                return value ? flags | this.roundaboutBit : flags & (this.roundaboutBit ^ -1);
            }
        }
        throw new IllegalArgumentException("Unknown key " + key + " for boolean value");
    }

    @Override
    public boolean isBool(long flags, int key) {
        switch (key) {
            case 0: {
                return this.isForward(flags);
            }
            case 1: {
                return this.isBackward(flags);
            }
            case 2: {
                return (flags & this.roundaboutBit) != 0;
            }
        }
        throw new IllegalArgumentException("Unknown key " + key + " for boolean value");
    }

    @Override
    public long setLong(long flags, int key, long value) {
        throw new UnsupportedOperationException("Unknown key " + key + " for long value.");
    }

    @Override
    public long getLong(long flags, int key) {
        throw new UnsupportedOperationException("Unknown key " + key + " for long value.");
    }

    @Override
    public long setDouble(long flags, int key, double value) {
        throw new UnsupportedOperationException("Unknown key " + key + " for double value.");
    }

    @Override
    public double getDouble(long flags, int key) {
        throw new UnsupportedOperationException("Unknown key " + key + " for double value.");
    }

    @Deprecated
    protected static double parseDouble(String str, String key, double defaultD) {
        String val = AbstractFlagEncoder.getStr(str, key);
        if (val.isEmpty()) {
            return defaultD;
        }
        return Double.parseDouble(val);
    }

    @Deprecated
    protected static long parseLong(String str, String key, long defaultL) {
        String val = AbstractFlagEncoder.getStr(str, key);
        if (val.isEmpty()) {
            return defaultL;
        }
        return Long.parseLong(val);
    }

    @Deprecated
    protected static boolean parseBoolean(String str, String key, boolean defaultB) {
        String val = AbstractFlagEncoder.getStr(str, key);
        if (val.isEmpty()) {
            return defaultB;
        }
        return Boolean.parseBoolean(val);
    }

    @Deprecated
    protected static String getStr(String str, String key) {
        key = key.toLowerCase();
        for (String s : str.split("\\|")) {
            int index = (s = s.trim().toLowerCase()).indexOf("=");
            if (index < 0) continue;
            String field = s.substring(0, index);
            String valueStr = s.substring(index + 1);
            if (!key.equals(field)) continue;
            return valueStr;
        }
        return "";
    }

    protected double applyMaxSpeed(OSMWay way, double speed, boolean force) {
        double maxSpeed = this.getMaxSpeed(way);
        if (maxSpeed >= 0.0 && (force || maxSpeed < speed)) {
            return maxSpeed * 0.9;
        }
        return speed;
    }

    protected String getPropertiesString() {
        return "speedFactor=" + this.speedFactor + "|speedBits=" + this.speedBits + "|turnCosts=" + (this.maxTurnCosts > 0);
    }

    @Override
    public boolean supports(Class<?> feature) {
        if (TurnWeighting.class.isAssignableFrom(feature)) {
            return this.maxTurnCosts > 0;
        }
        return false;
    }

}

