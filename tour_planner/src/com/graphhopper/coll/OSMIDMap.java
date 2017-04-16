/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.coll;

import com.graphhopper.coll.LongIntMap;
import com.graphhopper.storage.DataAccess;
import com.graphhopper.storage.Directory;
import com.graphhopper.util.BitUtil;

public class OSMIDMap
implements LongIntMap {
    private static final BitUtil bitUtil = BitUtil.LITTLE;
    private final DataAccess keys;
    private final DataAccess values;
    private long lastKey = Long.MIN_VALUE;
    private long size;
    private final int noEntryValue;
    private final Directory dir;

    public OSMIDMap(Directory dir) {
        this(dir, -1);
    }

    public OSMIDMap(Directory dir, int noNumber) {
        this.dir = dir;
        this.noEntryValue = noNumber;
        this.keys = dir.find("osmidMapKeys");
        this.keys.create(2000);
        this.values = dir.find("osmidMapValues");
        this.values.create(1000);
    }

    public void remove() {
        this.dir.remove(this.keys);
    }

    @Override
    public int put(long key, int value) {
        if (key <= this.lastKey) {
            long oldValueIndex = OSMIDMap.binarySearch(this.keys, 0, this.getSize(), key);
            if (oldValueIndex < 0) {
                throw new IllegalStateException("Cannot insert keys lower than the last key " + key + " < " + this.lastKey + ". Only updating supported");
            }
            int oldValue = this.values.getInt(oldValueIndex *= 4);
            this.values.setInt(oldValueIndex, value);
            return oldValue;
        }
        this.values.ensureCapacity(this.size + 4);
        this.values.setInt(this.size, value);
        long doubleSize = this.size * 2;
        this.keys.ensureCapacity(doubleSize + 8);
        byte[] longBytes = bitUtil.fromLong(key);
        this.keys.setBytes(doubleSize, longBytes, 8);
        this.lastKey = key;
        this.size += 4;
        return -1;
    }

    @Override
    public int get(long key) {
        long retIndex = OSMIDMap.binarySearch(this.keys, 0, this.getSize(), key);
        if (retIndex < 0) {
            return this.noEntryValue;
        }
        return this.values.getInt(retIndex * 4);
    }

    static long binarySearch(DataAccess da, long start, long len, long key) {
        long tmp;
        long high = start + len;
        long low = start - 1;
        byte[] longBytes = new byte[8];
        while (high - low > 1) {
            long guess = high + low >>> 1;
            tmp = guess << 3;
            da.getBytes(tmp, longBytes, 8);
            long guessedKey = bitUtil.toLong(longBytes);
            if (guessedKey < key) {
                low = guess;
                continue;
            }
            high = guess;
        }
        if (high == start + len) {
            return start + len ^ -1;
        }
        tmp = high << 3;
        da.getBytes(tmp, longBytes, 8);
        long highKey = bitUtil.toLong(longBytes);
        if (highKey == key) {
            return high;
        }
        return high ^ -1;
    }

    @Override
    public long getSize() {
        return this.size / 4;
    }

    public long getCapacity() {
        return this.keys.getCapacity();
    }

    @Override
    public int getMemoryUsage() {
        return Math.round(this.getCapacity() / 0x100000);
    }

    @Override
    public void optimize() {
    }
}

