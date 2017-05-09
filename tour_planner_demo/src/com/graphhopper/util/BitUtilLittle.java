/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.util;

import com.graphhopper.util.BitUtil;

public class BitUtilLittle
extends BitUtil {
    BitUtilLittle() {
    }

    @Override
    public final short toShort(byte[] b, int offset) {
        return (short)((b[offset + 1] & 255) << 8 | b[offset] & 255);
    }

    @Override
    public final int toInt(byte[] b, int offset) {
        return (b[offset + 3] & 255) << 24 | (b[offset + 2] & 255) << 16 | (b[offset + 1] & 255) << 8 | b[offset] & 255;
    }

    @Override
    public void fromShort(byte[] bytes, short value, int offset) {
        bytes[offset + 1] = (byte)(value >>> 8);
        bytes[offset] = (byte)value;
    }

    @Override
    public final void fromInt(byte[] bytes, int value, int offset) {
        bytes[offset + 3] = (byte)(value >>> 24);
        bytes[offset + 2] = (byte)(value >>> 16);
        bytes[offset + 1] = (byte)(value >>> 8);
        bytes[offset] = (byte)value;
    }

    @Override
    public final long toLong(int int0, int int1) {
        return (long)int1 << 32 | (long)int0 & 0xFFFFFFFFL;
    }

    @Override
    public final long toLong(byte[] b, int offset) {
        return (long)this.toInt(b, offset + 4) << 32 | (long)this.toInt(b, offset) & 0xFFFFFFFFL;
    }

    @Override
    public final void fromLong(byte[] bytes, long value, int offset) {
        bytes[offset + 7] = (byte)(value >> 56);
        bytes[offset + 6] = (byte)(value >> 48);
        bytes[offset + 5] = (byte)(value >> 40);
        bytes[offset + 4] = (byte)(value >> 32);
        bytes[offset + 3] = (byte)(value >> 24);
        bytes[offset + 2] = (byte)(value >> 16);
        bytes[offset + 1] = (byte)(value >> 8);
        bytes[offset] = (byte)value;
    }

    @Override
    public byte[] fromBitString(String str) {
        int strLen = str.length();
        int bLen = str.length() / 8;
        if (strLen % 8 != 0) {
            ++bLen;
        }
        byte[] bytes = new byte[bLen];
        int charI = 0;
        for (int b = bLen - 1; b >= 0; --b) {
            int res = 0;
            for (int i = 0; i < 8; ++i) {
                res = (byte)(res << 1);
                if (charI < strLen && str.charAt(charI) != '0') {
                    res = (byte)(res | 1);
                }
                ++charI;
            }
            bytes[b] = (byte)res;
        }
        return bytes;
    }

    @Override
    public String toBitString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 8);
        int lastBit = -128;
        for (int bIndex = bytes.length - 1; bIndex >= 0; --bIndex) {
            byte b = bytes[bIndex];
            for (int i = 0; i < 8; ++i) {
                if ((b & lastBit) == 0) {
                    sb.append('0');
                } else {
                    sb.append('1');
                }
                b = (byte)(b << 1);
            }
        }
        return sb.toString();
    }

    public String toString() {
        return "little";
    }
}

