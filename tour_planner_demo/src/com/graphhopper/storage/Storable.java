/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.storage;

import java.io.Closeable;

public interface Storable<T>
extends Closeable {
    public boolean loadExisting();

    public T create(long var1);

    public void flush();

    @Override
    public void close();

    public boolean isClosed();

    public long getCapacity();
}

