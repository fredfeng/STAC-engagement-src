/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.storage;

public interface Lock {
    public String getName();

    public boolean tryLock();

    public boolean isLocked();

    public void release();

    public Exception getObtainFailedReason();
}

