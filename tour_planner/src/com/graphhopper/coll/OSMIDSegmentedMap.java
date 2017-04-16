/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.coll;

import com.graphhopper.coll.LongIntMap;
import com.graphhopper.coll.SparseLongLongArray;
import com.graphhopper.storage.VLongStorage;
import java.util.Arrays;

public class OSMIDSegmentedMap
implements LongIntMap {
    private int bucketSize;
    private long[] keys;
    private VLongStorage[] buckets;
    private long lastKey = -1;
    private long lastValue = -1;
    private int currentBucket = 0;
    private int currentIndex = -1;
    private long size;

    public OSMIDSegmentedMap() {
        this(100, 10);
    }

    public OSMIDSegmentedMap(int initialCapacity, int maxEntryPerBucket) {
        this.bucketSize = maxEntryPerBucket;
        int cap = initialCapacity / this.bucketSize;
        this.keys = new long[cap];
        this.buckets = new VLongStorage[cap];
    }

    public void write(long key) {
        if (key <= this.lastKey) {
            throw new IllegalStateException("Not supported: key " + key + " is lower than last one " + this.lastKey);
        }
        ++this.currentIndex;
        if (this.currentIndex >= this.bucketSize) {
            ++this.currentBucket;
            this.currentIndex = 0;
        }
        if (this.currentBucket >= this.buckets.length) {
            int cap = (int)((float)this.currentBucket * 1.5f);
            this.buckets = Arrays.copyOf(this.buckets, cap);
            this.keys = Arrays.copyOf(this.keys, cap);
        }
        if (this.buckets[this.currentBucket] == null) {
            this.keys[this.currentBucket] = key;
            if (this.currentBucket > 0) {
                this.buckets[this.currentBucket - 1].trimToSize();
            }
            this.buckets[this.currentBucket] = new VLongStorage(this.bucketSize);
        } else {
            long delta = key - this.lastKey;
            this.buckets[this.currentBucket].writeVLong(delta);
        }
        ++this.size;
        this.lastKey = key;
    }

    @Override
    public int get(long key) {
        int retBucket = SparseLongLongArray.binarySearch(this.keys, 0, this.currentBucket + 1, key);
        if (retBucket < 0) {
            retBucket ^= -1;
            if (--retBucket < 0) {
                return this.getNoEntryValue();
            }
            long storedKey = this.keys[retBucket];
            if (storedKey == key) {
                return retBucket * this.bucketSize;
            }
            VLongStorage buck = this.buckets[retBucket];
            long tmp = buck.getPosition();
            buck.seek(0);
            int max = this.currentBucket == retBucket ? this.currentIndex + 1 : this.bucketSize;
            int ret = this.getNoEntryValue();
            for (int i = 1; i < max; ++i) {
                if ((storedKey += buck.readVLong()) == key) {
                    ret = retBucket * this.bucketSize + i;
                    break;
                }
                if (storedKey > key) break;
            }
            buck.seek(tmp);
            return ret;
        }
        return retBucket * this.bucketSize;
    }

    public int getNoEntryValue() {
        return -1;
    }

    @Override
    public long getSize() {
        return this.size;
    }

    @Override
    public void optimize() {
    }

    @Override
    public int getMemoryUsage() {
        long bytes = 0;
        for (int i = 0; i < this.buckets.length; ++i) {
            if (this.buckets[i] == null) continue;
            bytes += this.buckets[i].getLength();
        }
        return Math.round(((long)(this.keys.length * 4) + bytes) / 0x100000);
    }

    @Override
    public int put(long key, int value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

