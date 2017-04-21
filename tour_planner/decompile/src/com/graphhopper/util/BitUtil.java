/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.util;

import com.graphhopper.util.BitUtilBig;
import com.graphhopper.util.BitUtilLittle;
import java.nio.ByteOrder;

public abstract class BitUtil {
    public static final BitUtil LITTLE = new BitUtilLittle();
    public static final BitUtil BIG = new BitUtilBig();

    public static BitUtil get(ByteOrder order) {
        if (order.equals(ByteOrder.BIG_ENDIAN)) {
            return BIG;
        }
        return LITTLE;
    }

    public final double toDouble(byte[] bytes) {
        return this.toDouble(bytes, 0);
    }

    public final double toDouble(byte[] bytes, int offset) {
        return Double.longBitsToDouble(this.toLong(bytes, offset));
    }

    public final byte[] fromDouble(double value) {
        byte[] bytes = new byte[8];
        this.fromDouble(bytes, value, 0);
        return bytes;
    }

    public final void fromDouble(byte[] bytes, double value) {
        this.fromDouble(bytes, value, 0);
    }

    public final void fromDouble(byte[] bytes, double value, int offset) {
        this.fromLong(bytes, Double.doubleToRawLongBits(value), offset);
    }

    public final float toFloat(byte[] bytes) {
        return this.toFloat(bytes, 0);
    }

    public final float toFloat(byte[] bytes, int offset) {
        return Float.intBitsToFloat(this.toInt(bytes, offset));
    }

    public final byte[] fromFloat(float value) {
        byte[] bytes = new byte[4];
        this.fromFloat(bytes, value, 0);
        return bytes;
    }

    public final void fromFloat(byte[] bytes, float value) {
        this.fromFloat(bytes, value, 0);
    }

    public final void fromFloat(byte[] bytes, float value, int offset) {
        this.fromInt(bytes, Float.floatToRawIntBits(value), offset);
    }

    public final short toShort(byte[] b) {
        return this.toShort(b, 0);
    }

    public abstract short toShort(byte[] var1, int var2);

    public final int toInt(byte[] b) {
        return this.toInt(b, 0);
    }

    public abstract int toInt(byte[] var1, int var2);

    public final byte[] fromInt(int value) {
        byte[] bytes = new byte[4];
        this.fromInt(bytes, value, 0);
        return bytes;
    }

    public final void fromInt(byte[] bytes, int value) {
        this.fromInt(bytes, value, 0);
    }

    public final byte[] fromShort(short value) {
        byte[] bytes = new byte[4];
        this.fromShort(bytes, value, 0);
        return bytes;
    }

    public final void fromShort(byte[] bytes, short value) {
        this.fromShort(bytes, value, 0);
    }

    public abstract void fromShort(byte[] var1, short var2, int var3);

    public abstract void fromInt(byte[] var1, int var2, int var3);

    public final long toLong(byte[] b) {
        return this.toLong(b, 0);
    }

    public abstract long toLong(int var1, int var2);

    public abstract long toLong(byte[] var1, int var2);

    public final byte[] fromLong(long value) {
        byte[] bytes = new byte[8];
        this.fromLong(bytes, value, 0);
        return bytes;
    }

    public final void fromLong(byte[] bytes, long value) {
        this.fromLong(bytes, value, 0);
    }

    public abstract void fromLong(byte[] var1, long var2, int var4);

    public final long fromBitString2Long(String str) {
        if (str.length() > 64) {
            throw new UnsupportedOperationException("Strings needs to fit into a 'long' but length was " + str.length());
        }
        long res = 0;
        int strLen = str.length();
        for (int charIndex = 0; charIndex < strLen; ++charIndex) {
            res <<= 1;
            if (str.charAt(charIndex) == '0') continue;
            res |= 1;
        }
        return res <<= 64 - strLen;
    }

    public abstract byte[] fromBitString(String var1);

    public final String toBitString(long value) {
        return this.toBitString(value, 64);
    }

    public String toLastBitString(long value, int bits) {
        StringBuilder sb = new StringBuilder(bits);
        long lastBit = 1 << bits - 1;
        for (int i = 0; i < bits; ++i) {
            if ((value & lastBit) == 0) {
                sb.append('0');
            } else {
                sb.append('1');
            }
            value <<= 1;
        }
        return sb.toString();
    }

    public String toBitString(long value, int bits) {
        StringBuilder sb = new StringBuilder(bits);
        long lastBit = Long.MIN_VALUE;
        for (int i = 0; i < bits; ++i) {
            if ((value & lastBit) == 0) {
                sb.append('0');
            } else {
                sb.append('1');
            }
            value <<= 1;
        }
        return sb.toString();
    }

    public abstract String toBitString(byte[] var1);

    public final long reverse(long value, int maxBits) {
        long res = 0;
        while (maxBits > 0) {
            res <<= 1;
            res |= value & 1;
            --maxBits;
            if (value == 0) {
                res <<= maxBits;
                break;
            }
            value >>>= 1;
        }
        return res;
    }

    public final int getIntLow(long longValue) {
        return (int)(longValue & 0xFFFFFFFFL);
    }

    public final int getIntHigh(long longValue) {
        return (int)(longValue >> 32);
    }

    public final long combineIntsToLong(int intLow, int intHigh) {
        return (long)intHigh << 32 | (long)intLow & 0xFFFFFFFFL;
    }

    public final long reverseLeft(long value, int maxBits) {
        long res = 0;
        int delta = 64 - maxBits;
        long maxBit = 1 << delta;
        while (maxBits > 0) {
            if ((value & maxBit) != 0) {
                res |= 1;
            }
            --maxBits;
            if ((maxBit <<= 1) == 0) {
                res <<= delta;
                break;
            }
            res <<= 1;
        }
        return res;
    }
}

