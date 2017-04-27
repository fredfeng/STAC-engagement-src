/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.util;

import com.graphhopper.util.Helper;
import com.graphhopper.util.Instruction;
import com.graphhopper.util.InstructionAnnotation;
import com.graphhopper.util.PointList;
import com.graphhopper.util.Translation;
import java.util.HashMap;
import java.util.Map;

public class RoundaboutInstruction
extends Instruction {
    private int exitNumber = 0;
    private int clockwise = 0;
    private boolean exited = false;
    private double radian = Double.NaN;

    public RoundaboutInstruction(int sign, String name, InstructionAnnotation ia, PointList pl) {
        super(sign, name, ia, pl);
    }

    public RoundaboutInstruction increaseExitNumber() {
        ++this.exitNumber;
        return this;
    }

    public RoundaboutInstruction setExitNumber(int exitNumber) {
        this.exitNumber = exitNumber;
        return this;
    }

    public RoundaboutInstruction setDirOfRotation(double deltaIn) {
        if (this.clockwise == 0) {
            this.clockwise = deltaIn > 0.0 ? 1 : -1;
        } else {
            int clockwise2;
            int n = clockwise2 = deltaIn > 0.0 ? 1 : -1;
            if (this.clockwise != clockwise2) {
                this.clockwise = 2;
            }
        }
        return this;
    }

    public RoundaboutInstruction setExited() {
        this.exited = true;
        return this;
    }

    public boolean isExited() {
        return this.exited;
    }

    public int getExitNumber() {
        if (this.exited && this.exitNumber == 0) {
            throw new IllegalStateException("RoundaboutInstruction must contain exitNumber>0");
        }
        return this.exitNumber;
    }

    public double getTurnAngle() {
        if (Math.abs(this.clockwise) != 1) {
            return Double.NaN;
        }
        return 3.141592653589793 * (double)this.clockwise - this.radian;
    }

    public RoundaboutInstruction setRadian(double radian) {
        this.radian = radian;
        return this;
    }

    @Override
    public Map<String, Object> getExtraInfoJSON() {
        HashMap<String, Object> tmpMap = new HashMap<String, Object>(2);
        tmpMap.put("exit_number", this.getExitNumber());
        double tmpAngle = this.getTurnAngle();
        if (!Double.isNaN(tmpAngle)) {
            tmpMap.put("turn_angle", Helper.round(tmpAngle, 2));
        }
        return tmpMap;
    }

    @Override
    public String getTurnDescription(Translation tr) {
        String str;
        if (this.rawName) {
            return this.getName();
        }
        String streetName = this.getName();
        int indi = this.getSign();
        if (indi == 6) {
            str = !this.exited ? tr.tr("roundaboutEnter", new Object[0]) : (Helper.isEmpty(streetName) ? tr.tr("roundaboutExit", this.getExitNumber()) : tr.tr("roundaboutExitOnto", this.getExitNumber(), streetName));
        } else {
            throw new IllegalStateException("" + indi + "no Roundabout indication");
        }
        return str;
    }
}

