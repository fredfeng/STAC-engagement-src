/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.storage;

import com.graphhopper.storage.AbstractDataAccess;
import com.graphhopper.storage.DAType;
import com.graphhopper.storage.DataAccess;
import com.graphhopper.util.BitUtil;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.util.Arrays;

class RAMIntDataAccess
extends AbstractDataAccess {
    private int[][] segments = new int[0][];
    private boolean closed = false;
    private boolean store;
    private transient int segmentSizeIntsPower;

    RAMIntDataAccess(String name, String location, boolean store, ByteOrder order) {
        super(name, location, order);
        this.store = store;
    }

    public RAMIntDataAccess setStore(boolean store) {
        this.store = store;
        return this;
    }

    @Override
    public boolean isStoring() {
        return this.store;
    }

    @Override
    public DataAccess copyTo(DataAccess da) {
        if (da instanceof RAMIntDataAccess) {
            this.copyHeader(da);
            RAMIntDataAccess rda = (RAMIntDataAccess)da;
            rda.segments = new int[this.segments.length][];
            for (int i = 0; i < this.segments.length; ++i) {
                int[] area = this.segments[i];
                rda.segments[i] = Arrays.copyOf(area, area.length);
            }
            rda.setSegmentSize(this.segmentSizeInBytes);
            return da;
        }
        return super.copyTo(da);
    }

    @Override
    public RAMIntDataAccess create(long bytes) {
        if (this.segments.length > 0) {
            throw new IllegalThreadStateException("already created");
        }
        this.setSegmentSize(this.segmentSizeInBytes);
        this.ensureCapacity(Math.max(40, bytes));
        return this;
    }

    @Override
    public boolean ensureCapacity(long bytes) {
        if (bytes < 0) {
            throw new IllegalArgumentException("new capacity has to be strictly positive");
        }
        long cap = this.getCapacity();
        long newBytes = bytes - cap;
        if (newBytes <= 0) {
            return false;
        }
        int segmentsToCreate = (int)(newBytes / (long)this.segmentSizeInBytes);
        if (newBytes % (long)this.segmentSizeInBytes != 0) {
            ++segmentsToCreate;
        }
        try {
            int[][] newSegs = (int[][])Arrays.copyOf(this.segments, this.segments.length + segmentsToCreate);
            for (int i = this.segments.length; i < newSegs.length; ++i) {
                newSegs[i] = new int[1 << this.segmentSizeIntsPower];
            }
            this.segments = newSegs;
            return true;
        }
        catch (OutOfMemoryError err) {
            throw new OutOfMemoryError(err.getMessage() + " - problem when allocating new memory. Old capacity: " + cap + ", new bytes:" + newBytes + ", segmentSizeIntsPower:" + this.segmentSizeIntsPower + ", new segments:" + segmentsToCreate + ", existing:" + this.segments.length);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean loadExisting() {
        if (this.segments.length > 0) {
            throw new IllegalStateException("already initialized");
        }
        if (this.isClosed()) {
            throw new IllegalStateException("already closed");
        }
        if (!this.store) {
            return false;
        }
        File file = new File(this.getFullName());
        if (!file.exists()) return false;
        if (file.length() == 0) {
            return false;
        }
        try {
            RandomAccessFile raFile = new RandomAccessFile(this.getFullName(), "r");
            try {
                int s;
                long byteCount = this.readHeader(raFile) - 100;
                if (byteCount < 0) {
                    boolean bl = false;
                    return bl;
                }
                byte[] bytes = new byte[this.segmentSizeInBytes];
                raFile.seek(100);
                int segmentCount = (int)(byteCount / (long)this.segmentSizeInBytes);
                if (byteCount % (long)this.segmentSizeInBytes != 0) {
                    ++segmentCount;
                }
                this.segments = new int[segmentCount][];
                for (s = 0; s < segmentCount; ++s) {
                    int read = raFile.read(bytes) / 4;
                    int[] area = new int[read];
                    for (int j = 0; j < read; ++j) {
                        area[j] = this.bitUtil.toInt(bytes, j * 4);
                    }
                    this.segments[s] = area;
                }
                return true;
            }
            finally {
                raFile.close();
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Problem while loading " + this.getFullName(), ex);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void flush() {
        if (this.closed) {
            throw new IllegalStateException("already closed");
        }
        if (!this.store) {
            return;
        }
        try {
            RandomAccessFile raFile = new RandomAccessFile(this.getFullName(), "rw");
            try {
                long len = this.getCapacity();
                this.writeHeader(raFile, len, this.segmentSizeInBytes);
                raFile.seek(100);
                for (int s = 0; s < this.segments.length; ++s) {
                    int[] area = this.segments[s];
                    int intLen = area.length;
                    byte[] byteArea = new byte[intLen * 4];
                    for (int i = 0; i < intLen; ++i) {
                        this.bitUtil.fromInt(byteArea, area[i], i * 4);
                    }
                    raFile.write(byteArea);
                }
            }
            finally {
                raFile.close();
            }
        }
        catch (Exception ex) {
            throw new RuntimeException("Couldn't store integers to " + this.toString(), ex);
        }
    }

    @Override
    public final void setInt(long bytePos, int value) {
        assert (this.segmentSizeIntsPower > 0);
        int bufferIndex = (int)((bytePos >>>= 2) >>> this.segmentSizeIntsPower);
        int index = (int)(bytePos & (long)this.indexDivisor);
        this.segments[bufferIndex][index] = value;
    }

    @Override
    public final int getInt(long bytePos) {
        assert (this.segmentSizeIntsPower > 0);
        int bufferIndex = (int)((bytePos >>>= 2) >>> this.segmentSizeIntsPower);
        int index = (int)(bytePos & (long)this.indexDivisor);
        return this.segments[bufferIndex][index];
    }

    @Override
    public final void setShort(long bytePos, short value) {
        assert (this.segmentSizeIntsPower > 0);
        if (bytePos % 4 != 0 && bytePos % 4 != 2) {
            throw new IllegalMonitorStateException("bytePos of wrong multiple for RAMInt " + bytePos);
        }
        long tmpIndex = bytePos >>> 1;
        int bufferIndex = (int)(tmpIndex >>> this.segmentSizeIntsPower);
        int index = (int)(tmpIndex & (long)this.indexDivisor);
        this.segments[bufferIndex][index] = tmpIndex * 2 == bytePos ? (int)value : value << 16;
    }

    @Override
    public final short getShort(long bytePos) {
        assert (this.segmentSizeIntsPower > 0);
        if (bytePos % 4 != 0 && bytePos % 4 != 2) {
            throw new IllegalMonitorStateException("bytePos of wrong multiple for RAMInt " + bytePos);
        }
        long tmpIndex = bytePos >> 1;
        int bufferIndex = (int)(tmpIndex >> this.segmentSizeIntsPower);
        int index = (int)(tmpIndex & (long)this.indexDivisor);
        if (tmpIndex * 2 == bytePos) {
            return (short)this.segments[bufferIndex][index];
        }
        return (short)(this.segments[bufferIndex][index] >> 16);
    }

    @Override
    public void getBytes(long bytePos, byte[] values, int length) {
        throw new UnsupportedOperationException(this.toString() + " does not support byte based acccess. Use RAMDataAccess instead");
    }

    @Override
    public void setBytes(long bytePos, byte[] values, int length) {
        throw new UnsupportedOperationException(this.toString() + " does not support byte based acccess. Use RAMDataAccess instead");
    }

    @Override
    public void close() {
        super.close();
        this.segments = new int[0][];
        this.closed = true;
    }

    @Override
    public long getCapacity() {
        return (long)this.getSegments() * (long)this.segmentSizeInBytes;
    }

    @Override
    public int getSegments() {
        return this.segments.length;
    }

    @Override
    public DataAccess setSegmentSize(int bytes) {
        super.setSegmentSize(bytes);
        this.segmentSizeIntsPower = (int)(Math.log(this.segmentSizeInBytes / 4) / Math.log(2.0));
        this.indexDivisor = this.segmentSizeInBytes / 4 - 1;
        return this;
    }

    @Override
    public void trimTo(long capacity) {
        if (capacity < (long)this.segmentSizeInBytes) {
            capacity = this.segmentSizeInBytes;
        }
        int remainingSegments = (int)(capacity / (long)this.segmentSizeInBytes);
        if (capacity % (long)this.segmentSizeInBytes != 0) {
            ++remainingSegments;
        }
        this.segments = (int[][])Arrays.copyOf(this.segments, remainingSegments);
    }

    boolean releaseSegment(int segNumber) {
        this.segments[segNumber] = null;
        return true;
    }

    @Override
    public void rename(String newName) {
        if (!this.checkBeforeRename(newName)) {
            return;
        }
        if (this.store) {
            super.rename(newName);
        }
        this.name = newName;
    }

    @Override
    protected boolean isIntBased() {
        return true;
    }

    @Override
    public DAType getType() {
        if (this.isStoring()) {
            return DAType.RAM_INT_STORE;
        }
        return DAType.RAM_INT;
    }
}

