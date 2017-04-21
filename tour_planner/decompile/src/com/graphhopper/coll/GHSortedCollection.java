/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.coll;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.set.hash.TIntHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class GHSortedCollection {
    private int size;
    private int slidingMeanValue = 20;
    private TreeMap<Integer, TIntHashSet> map = new TreeMap();

    public GHSortedCollection() {
        this(0);
    }

    public GHSortedCollection(int size) {
    }

    public void clear() {
        this.size = 0;
        this.map.clear();
    }

    void remove(int key, int value) {
        TIntHashSet set = this.map.get(value);
        if (set == null || !set.remove(key)) {
            throw new IllegalStateException("cannot remove key " + key + " with value " + value + " - did you insert " + key + "," + value + " before?");
        }
        --this.size;
        if (set.isEmpty()) {
            this.map.remove(value);
        }
    }

    public void update(int key, int oldValue, int value) {
        this.remove(key, oldValue);
        this.insert(key, value);
    }

    public void insert(int key, int value) {
        TIntHashSet set = this.map.get(value);
        if (set == null) {
            set = new TIntHashSet(this.slidingMeanValue);
            this.map.put(value, set);
        }
        if (!set.add(key)) {
            throw new IllegalStateException("use update if you want to update " + key);
        }
        ++this.size;
    }

    public int peekValue() {
        if (this.size == 0) {
            throw new IllegalStateException("collection is already empty!?");
        }
        Map.Entry<Integer, TIntHashSet> e = this.map.firstEntry();
        if (e.getValue().isEmpty()) {
            throw new IllegalStateException("internal set is already empty!?");
        }
        return this.map.firstEntry().getKey();
    }

    public int peekKey() {
        if (this.size == 0) {
            throw new IllegalStateException("collection is already empty!?");
        }
        TIntHashSet set = this.map.firstEntry().getValue();
        if (set.isEmpty()) {
            throw new IllegalStateException("internal set is already empty!?");
        }
        return set.iterator().next();
    }

    public int pollKey() {
        --this.size;
        if (this.size < 0) {
            throw new IllegalStateException("collection is already empty!?");
        }
        Map.Entry<Integer, TIntHashSet> e = this.map.firstEntry();
        TIntHashSet set = e.getValue();
        TIntIterator iter = set.iterator();
        if (set.isEmpty()) {
            throw new IllegalStateException("internal set is already empty!?");
        }
        int val = iter.next();
        iter.remove();
        if (set.isEmpty()) {
            this.map.remove(e.getKey());
        }
        return val;
    }

    public int getSize() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int getSlidingMeanValue() {
        return this.slidingMeanValue;
    }

    public String toString() {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (Map.Entry<Integer, TIntHashSet> e : this.map.entrySet()) {
            int tmpSize = e.getValue().size();
            if (min > tmpSize) {
                min = tmpSize;
            }
            if (max >= tmpSize) continue;
            max = tmpSize;
        }
        String str = "";
        if (!this.isEmpty()) {
            str = ", minEntry=(" + this.peekKey() + "=>" + this.peekValue() + ")";
        }
        return "size=" + this.size + ", treeMap.size=" + this.map.size() + ", averageNo=" + (float)this.size * 1.0f / (float)this.map.size() + ", minNo=" + min + ", maxNo=" + max + str;
    }
}

