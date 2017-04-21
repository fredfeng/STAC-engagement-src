/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.storage;

import com.graphhopper.storage.DAType;
import com.graphhopper.storage.DataAccess;

class SynchedDAWrapper
implements DataAccess {
    private final DataAccess inner;
    private final DAType type;

    public SynchedDAWrapper(DataAccess inner) {
        this.inner = inner;
        this.type = new DAType(inner.getType(), true);
    }

    @Override
    public synchronized String getName() {
        return this.inner.getName();
    }

    @Override
    public synchronized void rename(String newName) {
        this.inner.rename(newName);
    }

    @Override
    public synchronized void setInt(long bytePos, int value) {
        this.inner.setInt(bytePos, value);
    }

    @Override
    public synchronized int getInt(long bytePos) {
        return this.inner.getInt(bytePos);
    }

    @Override
    public synchronized void setShort(long bytePos, short value) {
        this.inner.setShort(bytePos, value);
    }

    @Override
    public synchronized short getShort(long bytePos) {
        return this.inner.getShort(bytePos);
    }

    @Override
    public synchronized void setBytes(long bytePos, byte[] values, int length) {
        this.inner.setBytes(bytePos, values, length);
    }

    @Override
    public synchronized void getBytes(long bytePos, byte[] values, int length) {
        this.inner.getBytes(bytePos, values, length);
    }

    @Override
    public synchronized void setHeader(int bytePos, int value) {
        this.inner.setHeader(bytePos, value);
    }

    @Override
    public synchronized int getHeader(int bytePos) {
        return this.inner.getHeader(bytePos);
    }

    @Override
    public synchronized DataAccess create(long bytes) {
        return this.inner.create(bytes);
    }

    @Override
    public synchronized boolean ensureCapacity(long bytes) {
        return this.inner.ensureCapacity(bytes);
    }

    @Override
    public synchronized void trimTo(long bytes) {
        this.inner.trimTo(bytes);
    }

    @Override
    public synchronized DataAccess copyTo(DataAccess da) {
        return this.inner.copyTo(da);
    }

    @Override
    public synchronized DataAccess setSegmentSize(int bytes) {
        return this.inner.setSegmentSize(bytes);
    }

    @Override
    public synchronized int getSegmentSize() {
        return this.inner.getSegmentSize();
    }

    @Override
    public synchronized int getSegments() {
        return this.inner.getSegments();
    }

    @Override
    public synchronized boolean loadExisting() {
        return this.inner.loadExisting();
    }

    @Override
    public synchronized void flush() {
        this.inner.flush();
    }

    @Override
    public synchronized void close() {
        this.inner.close();
    }

    @Override
    public boolean isClosed() {
        return this.inner.isClosed();
    }

    @Override
    public synchronized long getCapacity() {
        return this.inner.getCapacity();
    }

    @Override
    public DAType getType() {
        return this.type;
    }
}

