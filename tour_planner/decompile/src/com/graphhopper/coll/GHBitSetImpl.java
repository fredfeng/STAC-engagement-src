/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.coll;

import com.graphhopper.coll.GHBitSet;
import java.util.BitSet;

public class GHBitSetImpl
extends BitSet
implements GHBitSet {
    public GHBitSetImpl() {
    }

    public GHBitSetImpl(int nbits) {
        super(nbits);
    }

    @Override
    public final boolean contains(int index) {
        return super.get(index);
    }

    @Override
    public final void add(int index) {
        super.set(index);
    }

    @Override
    public final int getCardinality() {
        return super.cardinality();
    }

    @Override
    public final int next(int index) {
        return super.nextSetBit(index);
    }

    public final int nextClear(int index) {
        return super.nextClearBit(index);
    }

    @Override
    public final GHBitSet copyTo(GHBitSet bs) {
        bs.clear();
        if (bs instanceof GHBitSetImpl) {
            ((GHBitSetImpl)bs).or(this);
        } else {
            int len = this.size();
            int index = super.nextSetBit(0);
            while (index >= 0) {
                bs.add(index);
                index = super.nextSetBit(index + 1);
            }
        }
        return bs;
    }
}

