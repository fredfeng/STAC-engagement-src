/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.util;

import com.graphhopper.util.Instruction;
import com.graphhopper.util.InstructionAnnotation;
import com.graphhopper.util.PointList;
import com.graphhopper.util.Translation;

public class ViaInstruction
extends Instruction {
    private int viaPosition = -1;

    public ViaInstruction(String name, InstructionAnnotation ia, PointList pl) {
        super(5, name, ia, pl);
    }

    public ViaInstruction(Instruction instr) {
        this(instr.getName(), instr.getAnnotation(), instr.getPoints());
        this.setDistance(instr.getDistance());
        this.setTime(instr.getTime());
    }

    public void setViaCount(int count) {
        this.viaPosition = count;
    }

    public int getViaCount() {
        if (this.viaPosition < 0) {
            throw new IllegalStateException("Uninitialized via count in instruction " + this.getName());
        }
        return this.viaPosition;
    }

    @Override
    public String getTurnDescription(Translation tr) {
        if (this.rawName) {
            return this.getName();
        }
        return tr.tr("stopover", this.viaPosition);
    }
}

