/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.coll;

import com.graphhopper.coll.GHBitSet;
import gnu.trove.TIntCollection;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.set.hash.TIntHashSet;

public class GHTBitSet
implements GHBitSet {
    private final TIntHashSet tHash;

    public GHTBitSet(TIntHashSet set) {
        this.tHash = set;
    }

    public GHTBitSet(int no) {
        this.tHash = new TIntHashSet(no, 0.7f, -1);
    }

    public GHTBitSet() {
        this(1000);
    }

    @Override
    public final boolean contains(int index) {
        return this.tHash.contains(index);
    }

    @Override
    public final void add(int index) {
        this.tHash.add(index);
    }

    public final String toString() {
        return this.tHash.toString();
    }

    @Override
    public final int getCardinality() {
        return this.tHash.size();
    }

    @Override
    public final void clear() {
        this.tHash.clear();
    }

    @Override
    public final GHBitSet copyTo(GHBitSet bs) {
        bs.clear();
        if (bs instanceof GHTBitSet) {
            ((GHTBitSet)bs).tHash.addAll(this.tHash);
        } else {
            TIntIterator iter = this.tHash.iterator();
            while (iter.hasNext()) {
                bs.add(iter.next());
            }
        }
        return bs;
    }

    @Override
    public int next(int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

