/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing.util;

import com.graphhopper.routing.util.TurnCostEncoder;
import com.graphhopper.util.InstructionAnnotation;
import com.graphhopper.util.Translation;

public interface FlagEncoder
extends TurnCostEncoder {
    public static final int K_ROUNDABOUT = 2;

    public int getVersion();

    public double getMaxSpeed();

    public double getSpeed(long var1);

    public long setSpeed(long var1, double var3);

    public double getReverseSpeed(long var1);

    public long setReverseSpeed(long var1, double var3);

    public long setAccess(long var1, boolean var3, boolean var4);

    public long setProperties(double var1, boolean var3, boolean var4);

    public boolean isForward(long var1);

    public boolean isBackward(long var1);

    public boolean isBool(long var1, int var3);

    public long setBool(long var1, int var3, boolean var4);

    public long getLong(long var1, int var3);

    public long setLong(long var1, int var3, long var4);

    public double getDouble(long var1, int var3);

    public long setDouble(long var1, int var3, double var4);

    public boolean supports(Class<?> var1);

    public InstructionAnnotation getAnnotation(long var1, Translation var3);

    public boolean isRegistered();
}

