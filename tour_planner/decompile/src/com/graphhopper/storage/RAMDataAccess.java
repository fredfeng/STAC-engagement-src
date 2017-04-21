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
import org.slf4j.LoggerFactory;

public class RAMDataAccess
extends AbstractDataAccess {
    private byte[][] segments = new byte[0][];
    private boolean store;

    RAMDataAccess(String name, String location, boolean store, ByteOrder order) {
        super(name, location, order);
        this.store = store;
    }

    public RAMDataAccess store(boolean store) {
        this.store = store;
        return this;
    }

    @Override
    public boolean isStoring() {
        return this.store;
    }

    @Override
    public DataAccess copyTo(DataAccess da) {
        if (da instanceof RAMDataAccess) {
            this.copyHeader(da);
            RAMDataAccess rda = (RAMDataAccess)da;
            rda.segments = new byte[this.segments.length][];
            for (int i = 0; i < this.segments.length; ++i) {
                byte[] area = this.segments[i];
                rda.segments[i] = Arrays.copyOf(area, area.length);
            }
            rda.setSegmentSize(this.segmentSizeInBytes);
            return da;
        }
        return super.copyTo(da);
    }

    @Override
    public RAMDataAccess create(long bytes) {
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
            byte[][] newSegs = (byte[][])Arrays.copyOf(this.segments, this.segments.length + segmentsToCreate);
            for (int i = this.segments.length; i < newSegs.length; ++i) {
                newSegs[i] = new byte[1 << this.segmentSizePower];
            }
            this.segments = newSegs;
        }
        catch (OutOfMemoryError err) {
            throw new OutOfMemoryError(err.getMessage() + " - problem when allocating new memory. Old capacity: " + cap + ", new bytes:" + newBytes + ", segmentSizeIntsPower:" + this.segmentSizePower + ", new segments:" + segmentsToCreate + ", existing:" + this.segments.length);
        }
        return true;
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
                raFile.seek(100);
                int segmentCount = (int)(byteCount / (long)this.segmentSizeInBytes);
                if (byteCount % (long)this.segmentSizeInBytes != 0) {
                    ++segmentCount;
                }
                this.segments = new byte[segmentCount][];
                for (s = 0; s < segmentCount; ++s) {
                    byte[] bytes = new byte[this.segmentSizeInBytes];
                    int read = raFile.read(bytes);
                    if (read <= 0) {
                        throw new IllegalStateException("segment " + s + " is empty? " + this.toString());
                    }
                    this.segments[s] = bytes;
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
                    byte[] area = this.segments[s];
                    raFile.write(area);
                }
            }
            finally {
                raFile.close();
            }
        }
        catch (Exception ex) {
            throw new RuntimeException("Couldn't store bytes to " + this.toString(), ex);
        }
    }

    @Override
    public final void setInt(long bytePos, int value) {
        assert (this.segmentSizePower > 0);
        int bufferIndex = (int)(bytePos >>> this.segmentSizePower);
        int index = (int)(bytePos & (long)this.indexDivisor);
        assert (index + 4 <= this.segmentSizeInBytes);
        this.bitUtil.fromInt(this.segments[bufferIndex], value, index);
    }

    @Override
    public final int getInt(long bytePos) {
        assert (this.segmentSizePower > 0);
        int bufferIndex = (int)(bytePos >>> this.segmentSizePower);
        int index = (int)(bytePos & (long)this.indexDivisor);
        assert (index + 4 <= this.segmentSizeInBytes);
        if (bufferIndex > this.segments.length) {
            LoggerFactory.getLogger(this.getClass()).error(this.getName() + ", segments:" + this.segments.length + ", bufIndex:" + bufferIndex + ", bytePos:" + bytePos + ", segPower:" + this.segmentSizePower);
        }
        return this.bitUtil.toInt(this.segments[bufferIndex], index);
    }

    @Override
    public final void setShort(long bytePos, short value) {
        assert (this.segmentSizePower > 0);
        int bufferIndex = (int)(bytePos >>> this.segmentSizePower);
        int index = (int)(bytePos & (long)this.indexDivisor);
        assert (index + 2 <= this.segmentSizeInBytes);
        this.bitUtil.fromShort(this.segments[bufferIndex], value, index);
    }

    @Override
    public final short getShort(long bytePos) {
        assert (this.segmentSizePower > 0);
        int bufferIndex = (int)(bytePos >>> this.segmentSizePower);
        int index = (int)(bytePos & (long)this.indexDivisor);
        assert (index + 2 <= this.segmentSizeInBytes);
        return this.bitUtil.toShort(this.segments[bufferIndex], index);
    }

    @Override
    public void setBytes(long bytePos, byte[] values, int length) {
        assert (length <= this.segmentSizeInBytes);
        assert (this.segmentSizePower > 0);
        int bufferIndex = (int)(bytePos >>> this.segmentSizePower);
        int index = (int)(bytePos & (long)this.indexDivisor);
        byte[] seg = this.segments[bufferIndex];
        int delta = index + length - this.segmentSizeInBytes;
        if (delta > 0) {
            System.arraycopy(values, 0, seg, index, length -= delta);
            seg = this.segments[bufferIndex + 1];
            System.arraycopy(values, length, seg, 0, delta);
        } else {
            System.arraycopy(values, 0, seg, index, length);
        }
    }

    @Override
    public void getBytes(long bytePos, byte[] values, int length) {
        assert (length <= this.segmentSizeInBytes);
        assert (this.segmentSizePower > 0);
        int bufferIndex = (int)(bytePos >>> this.segmentSizePower);
        int index = (int)(bytePos & (long)this.indexDivisor);
        byte[] seg = this.segments[bufferIndex];
        int delta = index + length - this.segmentSizeInBytes;
        if (delta > 0) {
            System.arraycopy(seg, index, values, 0, length -= delta);
            seg = this.segments[bufferIndex + 1];
            System.arraycopy(seg, 0, values, length, delta);
        } else {
            System.arraycopy(seg, index, values, 0, length);
        }
    }

    @Override
    public void close() {
        super.close();
        this.segments = new byte[0][];
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
    public void trimTo(long capacity) {
        if (capacity > this.getCapacity()) {
            throw new IllegalStateException("Cannot increase capacity (" + this.getCapacity() + ") to " + capacity + " via trimTo. Use ensureCapacity instead. ");
        }
        if (capacity < (long)this.segmentSizeInBytes) {
            capacity = this.segmentSizeInBytes;
        }
        int remainingSegments = (int)(capacity / (long)this.segmentSizeInBytes);
        if (capacity % (long)this.segmentSizeInBytes != 0) {
            ++remainingSegments;
        }
        this.segments = (byte[][])Arrays.copyOf(this.segments, remainingSegments);
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
    public DAType getType() {
        if (this.isStoring()) {
            return DAType.RAM_STORE;
        }
        return DAType.RAM;
    }
}

