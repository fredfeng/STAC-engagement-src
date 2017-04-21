/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing.util;

public class EncodedValue {
    private final String name;
    protected final long shift;
    protected final long mask;
    protected final double factor;
    protected final long defaultValue;
    private final long maxValue;
    private final boolean allowZero;
    private final int bits;

    public EncodedValue(String name, int shift, int bits, double factor, long defaultValue, int maxValue) {
        this(name, shift, bits, factor, defaultValue, maxValue, true);
    }

    public EncodedValue(String name, int shift, int bits, double factor, long defaultValue, int maxValue, boolean allowZero) {
        this.name = name;
        this.shift = shift;
        this.factor = factor;
        this.defaultValue = defaultValue;
        this.bits = bits;
        long tmpMask = (1 << bits) - 1;
        this.maxValue = Math.min((long)maxValue, Math.round((double)tmpMask * factor));
        if ((long)maxValue > this.maxValue) {
            throw new IllegalStateException(name + " -> maxValue " + maxValue + " is too large for " + bits + " bits");
        }
        this.mask = tmpMask << shift;
        this.allowZero = allowZero;
    }

    protected void checkValue(long value) {
        if (value > this.maxValue) {
            throw new IllegalArgumentException(this.name + " value too large for encoding: " + value + ", maxValue:" + this.maxValue);
        }
        if (value < 0) {
            throw new IllegalArgumentException("negative " + this.name + " value not allowed! " + value);
        }
        if (!this.allowZero && value == 0) {
            throw new IllegalArgumentException("zero " + this.name + " value not allowed! " + value);
        }
    }

    public long setValue(long flags, long value) {
        this.checkValue(value);
        value = (long)((double)value / this.factor);
        return (flags &= this.mask ^ -1) | (value <<= (int)this.shift);
    }

    public long getValue(long flags) {
        flags &= this.mask;
        return Math.round((double)(flags >>>= (int)this.shift) * this.factor);
    }

    public int getBits() {
        return this.bits;
    }

    public long setDefaultValue(long flags) {
        return this.setValue(flags, this.defaultValue);
    }

    public long getMaxValue() {
        return this.maxValue;
    }

    public long swap(long flags, EncodedValue otherEncoder) {
        long otherValue = otherEncoder.getValue(flags);
        flags = otherEncoder.setValue(flags, this.getValue(flags));
        return this.setValue(flags, otherValue);
    }
}

