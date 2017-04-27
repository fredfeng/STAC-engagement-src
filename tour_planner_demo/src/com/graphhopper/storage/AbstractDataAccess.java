/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.storage;

import com.graphhopper.storage.DAType;
import com.graphhopper.storage.DataAccess;
import com.graphhopper.util.BitUtil;
import com.graphhopper.util.Helper;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;

public abstract class AbstractDataAccess
implements DataAccess {
    protected static final int SEGMENT_SIZE_MIN = 128;
    private static final int SEGMENT_SIZE_DEFAULT = 1048576;
    protected static final int HEADER_OFFSET = 100;
    protected static final byte[] EMPTY = new byte[1024];
    protected int[] header = new int[20];
    private final String location;
    protected String name;
    protected int segmentSizeInBytes = 1048576;
    protected transient int segmentSizePower;
    protected transient int indexDivisor;
    protected final ByteOrder byteOrder;
    protected final BitUtil bitUtil;
    protected transient boolean closed = false;

    public AbstractDataAccess(String name, String location, ByteOrder order) {
        this.byteOrder = order;
        this.bitUtil = BitUtil.get(order);
        this.name = name;
        if (!Helper.isEmpty(location) && !location.endsWith("/")) {
            throw new IllegalArgumentException("Create DataAccess object via its corresponding Directory!");
        }
        this.location = location;
    }

    @Override
    public String getName() {
        return this.name;
    }

    protected String getFullName() {
        return this.location + this.name;
    }

    @Override
    public void close() {
        this.closed = true;
    }

    @Override
    public boolean isClosed() {
        return this.closed;
    }

    @Override
    public void setHeader(int bytePos, int value) {
        this.header[bytePos >>= 2] = value;
    }

    @Override
    public int getHeader(int bytePos) {
        return this.header[bytePos >>= 2];
    }

    protected void writeHeader(RandomAccessFile file, long length, int segmentSize) throws IOException {
        file.seek(0);
        file.writeUTF("GH");
        file.writeLong(length);
        file.writeInt(segmentSize);
        for (int i = 0; i < this.header.length; ++i) {
            file.writeInt(this.header[i]);
        }
    }

    protected long readHeader(RandomAccessFile raFile) throws IOException {
        raFile.seek(0);
        if (raFile.length() == 0) {
            return -1;
        }
        String versionHint = raFile.readUTF();
        if (!"GH".equals(versionHint)) {
            throw new IllegalArgumentException("Not a GraphHopper file! Expected 'GH' as file marker but was " + versionHint);
        }
        long bytes = raFile.readLong();
        this.setSegmentSize(raFile.readInt());
        for (int i = 0; i < this.header.length; ++i) {
            this.header[i] = raFile.readInt();
        }
        return bytes;
    }

    protected void copyHeader(DataAccess da) {
        for (int h = 0; h < this.header.length * 4; h += 4) {
            da.setHeader(h, this.getHeader(h));
        }
    }

    @Override
    public DataAccess copyTo(DataAccess da) {
        this.copyHeader(da);
        da.ensureCapacity(this.getCapacity());
        long cap = this.getCapacity();
        int segSize = Math.min(da.getSegmentSize(), this.getSegmentSize());
        byte[] bytes = new byte[segSize];
        boolean externalIntBased = ((AbstractDataAccess)da).isIntBased();
        for (long bytePos = 0; bytePos < cap; bytePos += (long)segSize) {
            int offset;
            if (this.isIntBased()) {
                for (offset = 0; offset < segSize; offset += 4) {
                    this.bitUtil.fromInt(bytes, this.getInt(bytePos + (long)offset), offset);
                }
            } else {
                this.getBytes(bytePos, bytes, segSize);
            }
            if (externalIntBased) {
                for (offset = 0; offset < segSize; offset += 4) {
                    da.setInt(bytePos + (long)offset, this.bitUtil.toInt(bytes, offset));
                }
                continue;
            }
            da.setBytes(bytePos, bytes, segSize);
        }
        return da;
    }

    @Override
    public DataAccess setSegmentSize(int bytes) {
        if (bytes > 0) {
            int tmp = (int)(Math.log(bytes) / Math.log(2.0));
            this.segmentSizeInBytes = Math.max((int)Math.pow(2.0, tmp), 128);
        }
        this.segmentSizePower = (int)(Math.log(this.segmentSizeInBytes) / Math.log(2.0));
        this.indexDivisor = this.segmentSizeInBytes - 1;
        return this;
    }

    @Override
    public int getSegmentSize() {
        return this.segmentSizeInBytes;
    }

    public String toString() {
        return this.getFullName();
    }

    @Override
    public void rename(String newName) {
        File file = new File(this.location + this.name);
        if (file.exists()) {
            try {
                if (!file.renameTo(new File(this.location + newName))) {
                    throw new IllegalStateException("Couldn't rename this " + this.getType() + " object to " + newName);
                }
                this.name = newName;
            }
            catch (Exception ex) {
                throw new IllegalStateException("Couldn't rename this " + this.getType() + " object!", ex);
            }
        } else {
            throw new IllegalStateException("File does not exist!? " + this.getFullName() + " Make sure that you flushed before renaming. Otherwise it could make problems" + " for memory mapped DataAccess objects");
        }
    }

    protected boolean checkBeforeRename(String newName) {
        if (Helper.isEmpty(newName)) {
            throw new IllegalArgumentException("newName mustn't be empty!");
        }
        if (newName.equals(this.name)) {
            return false;
        }
        if (this.isStoring() && new File(this.location + newName).exists()) {
            throw new IllegalArgumentException("file newName already exists!");
        }
        return true;
    }

    public boolean isStoring() {
        return true;
    }

    protected boolean isIntBased() {
        return false;
    }
}

