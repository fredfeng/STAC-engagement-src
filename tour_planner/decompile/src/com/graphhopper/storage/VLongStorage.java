/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.storage;

import java.util.Arrays;

public class VLongStorage {
    private byte[] bytes;
    private int pointer = 0;

    public VLongStorage() {
        this(10);
    }

    public VLongStorage(int cap) {
        this(new byte[cap]);
    }

    public VLongStorage(byte[] bytes) {
        this.bytes = bytes;
    }

    public void seek(long pos) {
        this.pointer = (int)pos;
    }

    public long getPosition() {
        return this.pointer;
    }

    public long getLength() {
        return this.bytes.length;
    }

    byte readByte() {
        byte b = this.bytes[this.pointer];
        ++this.pointer;
        return b;
    }

    void writeByte(byte b) {
        if (this.pointer >= this.bytes.length) {
            int cap = Math.max(10, (int)((float)this.pointer * 1.5f));
            this.bytes = Arrays.copyOf(this.bytes, cap);
        }
        this.bytes[this.pointer] = b;
        ++this.pointer;
    }

    public final void writeVLong(long i) {
        assert (i >= 0);
        while ((i & -128) != 0) {
            this.writeByte((byte)(i & 127 | 128));
            i >>>= 7;
        }
        this.writeByte((byte)i);
    }

    public long readVLong() {
        byte b = this.readByte();
        if (b >= 0) {
            return b;
        }
        long i = (long)b & 127;
        b = this.readByte();
        i |= ((long)b & 127) << 7;
        if (b >= 0) {
            return i;
        }
        b = this.readByte();
        i |= ((long)b & 127) << 14;
        if (b >= 0) {
            return i;
        }
        b = this.readByte();
        i |= ((long)b & 127) << 21;
        if (b >= 0) {
            return i;
        }
        b = this.readByte();
        i |= ((long)b & 127) << 28;
        if (b >= 0) {
            return i;
        }
        b = this.readByte();
        i |= ((long)b & 127) << 35;
        if (b >= 0) {
            return i;
        }
        b = this.readByte();
        i |= ((long)b & 127) << 42;
        if (b >= 0) {
            return i;
        }
        b = this.readByte();
        i |= ((long)b & 127) << 49;
        if (b >= 0) {
            return i;
        }
        b = this.readByte();
        i |= ((long)b & 127) << 56;
        if (b >= 0) {
            return i;
        }
        throw new RuntimeException("Invalid vLong detected (negative values disallowed)");
    }

    public void trimToSize() {
        if (this.bytes.length > this.pointer) {
            byte[] tmp = new byte[this.pointer];
            System.arraycopy(this.bytes, 0, tmp, 0, this.pointer);
            this.bytes = tmp;
        }
    }

    public byte[] getBytes() {
        return this.bytes;
    }
}

