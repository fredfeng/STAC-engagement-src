/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.storage;

import com.graphhopper.storage.AbstractDataAccess;
import com.graphhopper.storage.DAType;
import com.graphhopper.storage.DataAccess;
import com.graphhopper.util.Constants;
import com.graphhopper.util.Helper;
import com.graphhopper.util.NotThreadSafe;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NotThreadSafe
public class MMapDataAccess
extends AbstractDataAccess {
    private RandomAccessFile raFile;
    private List<ByteBuffer> segments = new ArrayList<ByteBuffer>();
    private boolean cleanAndRemap = false;
    private final boolean allowWrites;

    MMapDataAccess(String name, String location, ByteOrder order, boolean allowWrites) {
        super(name, location, order);
        this.allowWrites = allowWrites;
    }

    MMapDataAccess cleanAndRemap(boolean cleanAndRemap) {
        this.cleanAndRemap = cleanAndRemap;
        return this;
    }

    private void initRandomAccessFile() {
        if (this.raFile != null) {
            return;
        }
        try {
            this.raFile = new RandomAccessFile(this.getFullName(), this.allowWrites ? "rw" : "r");
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public MMapDataAccess create(long bytes) {
        if (!this.segments.isEmpty()) {
            throw new IllegalThreadStateException("already created");
        }
        this.initRandomAccessFile();
        bytes = Math.max(40, bytes);
        this.setSegmentSize(this.segmentSizeInBytes);
        this.ensureCapacity(bytes);
        return this;
    }

    @Override
    public DataAccess copyTo(DataAccess da) {
        return super.copyTo(da);
    }

    @Override
    public boolean ensureCapacity(long bytes) {
        return this.mapIt(100, bytes, true);
    }

    protected boolean mapIt(long offset, long byteCount, boolean clearNew) {
        if (byteCount < 0) {
            throw new IllegalArgumentException("new capacity has to be strictly positive");
        }
        if (byteCount <= this.getCapacity()) {
            return false;
        }
        long longSegmentSize = this.segmentSizeInBytes;
        int segmentsToMap = (int)(byteCount / longSegmentSize);
        if (segmentsToMap < 0) {
            throw new IllegalStateException("Too many segments needs to be allocated. Increase segmentSize.");
        }
        if (byteCount % longSegmentSize != 0) {
            ++segmentsToMap;
        }
        if (segmentsToMap == 0) {
            throw new IllegalStateException("0 segments are not allowed.");
        }
        long bufferStart = offset;
        int i = 0;
        long newFileLength = offset + (long)segmentsToMap * longSegmentSize;
        try {
            int newSegments;
            if (this.cleanAndRemap) {
                newSegments = segmentsToMap;
                this.clean(0, this.segments.size());
                Helper.cleanHack();
                this.segments.clear();
            } else {
                bufferStart += (long)this.segments.size() * longSegmentSize;
                newSegments = segmentsToMap - this.segments.size();
            }
            while (i < newSegments) {
                this.segments.add(this.newByteBuffer(bufferStart, longSegmentSize));
                bufferStart += longSegmentSize;
                ++i;
            }
            return true;
        }
        catch (IOException ex) {
            throw new RuntimeException("Couldn't map buffer " + i + " of " + segmentsToMap + " at position " + bufferStart + " for " + byteCount + " bytes with offset " + offset + ", new fileLength:" + newFileLength, ex);
        }
    }

    private ByteBuffer newByteBuffer(long offset, long byteCount) throws IOException {
        ByteBuffer buf = null;
        IOException ioex = null;
        int trial = 0;
        while (trial < 1) {
            try {
                buf = this.raFile.getChannel().map(this.allowWrites ? FileChannel.MapMode.READ_WRITE : FileChannel.MapMode.READ_ONLY, offset, byteCount);
                break;
            }
            catch (IOException tmpex) {
                ioex = tmpex;
                ++trial;
                Helper.cleanHack();
                try {
                    Thread.sleep(5);
                }
                catch (InterruptedException interruptedException) {}
            }
        }
        if (buf == null) {
            if (ioex == null) {
                throw new AssertionError((Object)"internal problem as the exception 'ioex' shouldn't be null");
            }
            throw ioex;
        }
        buf.order(this.byteOrder);
        boolean tmp = false;
        if (tmp) {
            int count = (int)(byteCount / (long)EMPTY.length);
            for (int i = 0; i < count; ++i) {
                buf.put(EMPTY);
            }
            int len = (int)(byteCount % (long)EMPTY.length);
            if (len > 0) {
                buf.put(EMPTY, count * EMPTY.length, len);
            }
        }
        return buf;
    }

    @Override
    public boolean loadExisting() {
        if (this.segments.size() > 0) {
            throw new IllegalStateException("already initialized");
        }
        if (this.isClosed()) {
            throw new IllegalStateException("already closed");
        }
        File file = new File(this.getFullName());
        if (!file.exists() || file.length() == 0) {
            return false;
        }
        this.initRandomAccessFile();
        try {
            long byteCount = this.readHeader(this.raFile);
            if (byteCount < 0) {
                return false;
            }
            this.mapIt(100, byteCount - 100, false);
            return true;
        }
        catch (IOException ex) {
            throw new RuntimeException("Problem while loading " + this.getFullName(), ex);
        }
    }

    @Override
    public void flush() {
        if (this.isClosed()) {
            throw new IllegalStateException("already closed");
        }
        try {
            if (!this.segments.isEmpty() && this.segments.get(0) instanceof MappedByteBuffer) {
                for (ByteBuffer bb : this.segments) {
                    ((MappedByteBuffer)bb).force();
                }
            }
            this.writeHeader(this.raFile, this.raFile.length(), this.segmentSizeInBytes);
            this.raFile.getFD().sync();
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void close() {
        super.close();
        this.close(true);
    }

    void close(boolean forceClean) {
        this.clean(0, this.segments.size());
        this.segments.clear();
        Helper.close(this.raFile);
        if (forceClean) {
            Helper.cleanHack();
        }
    }

    @Override
    public final void setInt(long bytePos, int value) {
        int bufferIndex = (int)(bytePos >> this.segmentSizePower);
        int index = (int)(bytePos & (long)this.indexDivisor);
        this.segments.get(bufferIndex).putInt(index, value);
    }

    @Override
    public final int getInt(long bytePos) {
        int bufferIndex = (int)(bytePos >> this.segmentSizePower);
        int index = (int)(bytePos & (long)this.indexDivisor);
        return this.segments.get(bufferIndex).getInt(index);
    }

    @Override
    public final void setShort(long bytePos, short value) {
        int bufferIndex = (int)(bytePos >>> this.segmentSizePower);
        int index = (int)(bytePos & (long)this.indexDivisor);
        this.segments.get(bufferIndex).putShort(index, value);
    }

    @Override
    public final short getShort(long bytePos) {
        int bufferIndex = (int)(bytePos >>> this.segmentSizePower);
        int index = (int)(bytePos & (long)this.indexDivisor);
        return this.segments.get(bufferIndex).getShort(index);
    }

    @Override
    public void setBytes(long bytePos, byte[] values, int length) {
        assert (length <= this.segmentSizeInBytes);
        int bufferIndex = (int)(bytePos >>> this.segmentSizePower);
        int index = (int)(bytePos & (long)this.indexDivisor);
        ByteBuffer bb = this.segments.get(bufferIndex);
        bb.position(index);
        int delta = index + length - this.segmentSizeInBytes;
        if (delta > 0) {
            bb.put(values, 0, length -= delta);
            bb = this.segments.get(bufferIndex + 1);
            bb.position(0);
            bb.put(values, length, delta);
        } else {
            bb.put(values, 0, length);
        }
    }

    @Override
    public void getBytes(long bytePos, byte[] values, int length) {
        assert (length <= this.segmentSizeInBytes);
        int bufferIndex = (int)(bytePos >>> this.segmentSizePower);
        int index = (int)(bytePos & (long)this.indexDivisor);
        ByteBuffer bb = this.segments.get(bufferIndex);
        bb.position(index);
        int delta = index + length - this.segmentSizeInBytes;
        if (delta > 0) {
            bb.get(values, 0, length -= delta);
            bb = this.segments.get(bufferIndex + 1);
            bb.position(0);
            bb.get(values, length, delta);
        } else {
            bb.get(values, 0, length);
        }
    }

    @Override
    public long getCapacity() {
        long cap = 0;
        for (ByteBuffer bb : this.segments) {
            cap += (long)bb.capacity();
        }
        return cap;
    }

    @Override
    public int getSegments() {
        return this.segments.size();
    }

    private void clean(int from, int to) {
        for (int i = from; i < to; ++i) {
            ByteBuffer bb = this.segments.get(i);
            Helper.cleanMappedByteBuffer(bb);
            this.segments.set(i, null);
        }
    }

    @Override
    public void trimTo(long capacity) {
        if (capacity < (long)this.segmentSizeInBytes) {
            capacity = this.segmentSizeInBytes;
        }
        int remainingSegNo = (int)(capacity / (long)this.segmentSizeInBytes);
        if (capacity % (long)this.segmentSizeInBytes != 0) {
            ++remainingSegNo;
        }
        this.clean(remainingSegNo, this.segments.size());
        Helper.cleanHack();
        this.segments = new ArrayList<ByteBuffer>(this.segments.subList(0, remainingSegNo));
        try {
            if (!Constants.WINDOWS) {
                this.raFile.setLength(100 + remainingSegNo * this.segmentSizeInBytes);
            }
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    boolean releaseSegment(int segNumber) {
        ByteBuffer segment = this.segments.get(segNumber);
        if (segment instanceof MappedByteBuffer) {
            ((MappedByteBuffer)segment).force();
        }
        Helper.cleanMappedByteBuffer(segment);
        this.segments.set(segNumber, null);
        Helper.cleanHack();
        return true;
    }

    @Override
    public void rename(String newName) {
        if (!this.checkBeforeRename(newName)) {
            return;
        }
        this.close();
        super.rename(newName);
        this.raFile = null;
        this.closed = false;
        this.loadExisting();
    }

    @Override
    public DAType getType() {
        return DAType.MMAP;
    }
}

