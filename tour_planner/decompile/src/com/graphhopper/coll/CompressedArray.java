/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.coll;

import com.graphhopper.geohash.SpatialKeyAlgo;
import com.graphhopper.storage.VLongStorage;
import com.graphhopper.util.shapes.GHPoint;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class CompressedArray {
    private int compressionLevel = 5;
    private VLongStorage currentWriter;
    private int currentEntry = 0;
    private List<byte[]> segments;
    private int entriesPerSegment;
    private int approxBytesPerEntry;
    private SpatialKeyAlgo algo;

    public CompressedArray() {
        this(100, 200, 4);
    }

    public CompressedArray(int _segments, int entriesPerSeg, int approxBytesPerEntry) {
        if (entriesPerSeg < 1) {
            throw new IllegalArgumentException("at least one entry should be per segment");
        }
        this.entriesPerSegment = entriesPerSeg;
        this.approxBytesPerEntry = approxBytesPerEntry;
        this.segments = new ArrayList<byte[]>(_segments);
        this.algo = new SpatialKeyAlgo(63);
    }

    public CompressedArray setCompressionLevel(int compressionLevel) {
        this.compressionLevel = compressionLevel;
        return this;
    }

    public void write(double lat, double lon) {
        try {
            if (this.currentWriter == null) {
                this.currentWriter = new VLongStorage(this.entriesPerSegment * this.approxBytesPerEntry);
            }
            long latlon = this.algo.encode(new GHPoint(lat, lon));
            this.currentWriter.writeVLong(latlon);
            ++this.currentEntry;
            if (this.currentEntry >= this.entriesPerSegment) {
                this.flush();
            }
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public GHPoint get(long index) {
        int segmentNo = (int)(index / (long)this.entriesPerSegment);
        int entry = (int)(index % (long)this.entriesPerSegment);
        try {
            if (segmentNo >= this.segments.size()) {
                return null;
            }
            byte[] bytes = this.segments.get(segmentNo);
            VLongStorage store = new VLongStorage(CompressedArray.decompress(bytes));
            long len = store.getLength();
            int i = 0;
            while (store.getPosition() < len) {
                long latlon = store.readVLong();
                if (i == entry) {
                    GHPoint point = new GHPoint();
                    this.algo.decode(latlon, point);
                    return point;
                }
                ++i;
            }
            return null;
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            throw new RuntimeException("index " + index + "=> segNo:" + segmentNo + ", entry=" + entry + ", segments:" + this.segments.size(), ex);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void flush() {
        if (this.currentWriter == null) {
            return;
        }
        try {
            this.currentWriter.trimToSize();
            byte[] input = this.currentWriter.getBytes();
            this.segments.add(CompressedArray.compress(input, 0, input.length, this.compressionLevel));
            this.currentWriter = null;
            this.currentEntry = 0;
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public float calcMemInMB() {
        long bytes = 0;
        for (int i = 0; i < this.segments.size(); ++i) {
            bytes += (long)this.segments.get(i).length;
        }
        return (float)((long)(this.segments.size() * 4) + bytes) / 1048576.0f;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static byte[] compress(byte[] value, int offset, int length, int compressionLevel) {
        ByteArrayOutputStream bos;
        bos = new ByteArrayOutputStream(length);
        Deflater compressor = new Deflater();
        try {
            compressor.setLevel(compressionLevel);
            compressor.setInput(value, offset, length);
            compressor.finish();
            byte[] buf = new byte[1024];
            while (!compressor.finished()) {
                int count = compressor.deflate(buf);
                bos.write(buf, 0, count);
            }
        }
        finally {
            compressor.end();
        }
        return bos.toByteArray();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static byte[] decompress(byte[] value) throws DataFormatException {
        ByteArrayOutputStream bos;
        bos = new ByteArrayOutputStream(value.length);
        Inflater decompressor = new Inflater();
        try {
            decompressor.setInput(value);
            byte[] buf = new byte[1024];
            while (!decompressor.finished()) {
                int count = decompressor.inflate(buf);
                bos.write(buf, 0, count);
            }
        }
        finally {
            decompressor.end();
        }
        return bos.toByteArray();
    }
}

