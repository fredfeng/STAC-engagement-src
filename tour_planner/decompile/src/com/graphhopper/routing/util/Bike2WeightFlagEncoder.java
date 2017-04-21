/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing.util;

import com.graphhopper.reader.OSMWay;
import com.graphhopper.routing.util.BikeFlagEncoder;
import com.graphhopper.routing.util.EncodedDoubleValue;
import com.graphhopper.util.BitUtil;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.Helper;
import com.graphhopper.util.PMap;
import com.graphhopper.util.PointList;
import java.io.PrintStream;

public class Bike2WeightFlagEncoder
extends BikeFlagEncoder {
    private EncodedDoubleValue reverseSpeedEncoder;

    public Bike2WeightFlagEncoder() {
    }

    public Bike2WeightFlagEncoder(String propertiesStr) {
        super(new PMap(propertiesStr));
    }

    public Bike2WeightFlagEncoder(PMap properties) {
        super(properties);
    }

    public Bike2WeightFlagEncoder(int speedBits, double speedFactor, int maxTurnCosts) {
        super(speedBits, speedFactor, maxTurnCosts);
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public int defineWayBits(int index, int shift) {
        shift = super.defineWayBits(index, shift);
        this.reverseSpeedEncoder = new EncodedDoubleValue("Reverse Speed", shift, this.speedBits, this.speedFactor, this.getHighwaySpeed("cycleway"), this.maxPossibleSpeed);
        return shift += this.reverseSpeedEncoder.getBits();
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
    public long handleSpeed(OSMWay way, double speed, long flags) {
        if (this.isBackward(flags = super.handleSpeed(way, speed, flags))) {
            flags = this.setReverseSpeed(flags, speed);
        }
        if (this.isForward(flags)) {
            flags = this.setSpeed(flags, speed);
        }
        return flags;
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
    public void applyWayTags(OSMWay way, EdgeIteratorState edge) {
        PointList pl = edge.fetchWayGeometry(3);
        if (!pl.is3D()) {
            throw new IllegalStateException("To support speed calculation based on elevation data it is necessary to enable import of it.");
        }
        long flags = edge.getFlags();
        if (!(way.hasTag("tunnel", (Object)"yes") || way.hasTag("bridge", (Object)"yes") || way.hasTag("highway", (Object)"steps"))) {
            double incEleSum = 0.0;
            double incDist2DSum = 0.0;
            double decEleSum = 0.0;
            double decDist2DSum = 0.0;
            double prevEle = pl.getElevation(0);
            double fullDist2D = edge.getDistance();
            if (Double.isInfinite(fullDist2D)) {
                System.err.println("infinity distance? for way:" + way.getId());
                return;
            }
            if (fullDist2D < 1.0) {
                return;
            }
            double eleDelta = pl.getElevation(pl.size() - 1) - prevEle;
            if (eleDelta > 0.1) {
                incEleSum = eleDelta;
                incDist2DSum = fullDist2D;
            } else if (eleDelta < -0.1) {
                decEleSum = - eleDelta;
                decDist2DSum = fullDist2D;
            }
            double fwdIncline = incDist2DSum > 1.0 ? incEleSum / incDist2DSum : 0.0;
            double fwdDecline = decDist2DSum > 1.0 ? decEleSum / decDist2DSum : 0.0;
            double restDist2D = fullDist2D - incDist2DSum - decDist2DSum;
            double maxSpeed = this.getHighwaySpeed("cycleway");
            if (this.isForward(flags)) {
                double speed = this.getSpeed(flags);
                double fwdFaster = 1.0 + 2.0 * Helper.keepIn(fwdDecline, 0.0, 0.2);
                fwdFaster *= fwdFaster;
                double fwdSlower = 1.0 - 5.0 * Helper.keepIn(fwdIncline, 0.0, 0.2);
                fwdSlower *= fwdSlower;
                speed = speed * (fwdSlower * incDist2DSum + fwdFaster * decDist2DSum + 1.0 * restDist2D) / fullDist2D;
                flags = this.setSpeed(flags, Helper.keepIn(speed, 2.0, maxSpeed));
            }
            if (this.isBackward(flags)) {
                double speedReverse = this.getReverseSpeed(flags);
                double bwFaster = 1.0 + 2.0 * Helper.keepIn(fwdIncline, 0.0, 0.2);
                bwFaster *= bwFaster;
                double bwSlower = 1.0 - 5.0 * Helper.keepIn(fwdDecline, 0.0, 0.2);
                bwSlower *= bwSlower;
                speedReverse = speedReverse * (bwFaster * incDist2DSum + bwSlower * decDist2DSum + 1.0 * restDist2D) / fullDist2D;
                flags = this.setReverseSpeed(flags, Helper.keepIn(speedReverse, 2.0, maxSpeed));
            }
        }
        edge.setFlags(flags);
    }

    @Override
    public String toString() {
        return "bike2";
    }
}

