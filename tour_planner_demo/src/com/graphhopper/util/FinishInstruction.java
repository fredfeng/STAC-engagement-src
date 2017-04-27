/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.util;

import com.graphhopper.util.Instruction;
import com.graphhopper.util.InstructionAnnotation;
import com.graphhopper.util.PointAccess;
import com.graphhopper.util.PointList;
import com.graphhopper.util.Translation;

public class FinishInstruction
extends Instruction {
    public FinishInstruction(final double lat, final double lon, final double ele) {
        super(4, "", InstructionAnnotation.EMPTY, new PointList(2, true){});
    }

    public FinishInstruction(PointAccess pointAccess, int node) {
        this(pointAccess.getLatitude(node), pointAccess.getLongitude(node), pointAccess.is3D() ? pointAccess.getElevation(node) : 0.0);
    }

    @Override
    public String getTurnDescription(Translation tr) {
        if (this.rawName) {
            return this.getName();
        }
        return tr.tr("finish", new Object[0]);
    }

}

