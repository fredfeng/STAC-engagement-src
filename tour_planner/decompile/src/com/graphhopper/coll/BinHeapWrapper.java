/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.coll;

public interface BinHeapWrapper<K, E> {
    public void update(K var1, E var2);

    public void insert(K var1, E var2);

    public boolean isEmpty();

    public int getSize();

    public E peekElement();

    public K peekKey();

    public E pollElement();

    public void clear();

    public void ensureCapacity(int var1);
}

