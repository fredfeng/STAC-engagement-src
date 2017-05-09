/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.coll;

import com.graphhopper.coll.BinHeapWrapper;
import java.util.Arrays;

public class IntIntBinHeap
implements BinHeapWrapper<Number, Integer> {
    private static final double GROW_FACTOR = 2.0;
    private int[] keys;
    private int[] elem;
    private int size;
    private int capacity;

    public IntIntBinHeap() {
        this(1000);
    }

    public IntIntBinHeap(int capacity) {
        if (capacity < 10) {
            capacity = 10;
        }
        this.capacity = capacity;
        this.size = 0;
        this.elem = new int[capacity + 1];
        this.keys = new int[capacity + 1];
        this.keys[0] = Integer.MIN_VALUE;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public Integer peekKey() {
        return this.peek_key();
    }

    public int peek_key() {
        if (this.size > 0) {
            return this.keys[1];
        }
        throw new IllegalStateException("An empty queue does not have a minimum key.");
    }

    @Override
    public Integer peekElement() {
        return this.peek_element();
    }

    public int peek_element() {
        if (this.size > 0) {
            return this.elem[1];
        }
        throw new IllegalStateException("An empty queue does not have a minimum value.");
    }

    @Override
    public Integer pollElement() {
        return this.poll_element();
    }

    public int poll_element() {
        int minElem = this.elem[1];
        int lastElem = this.elem[this.size];
        int lastPrio = this.keys[this.size];
        if (this.size <= 0) {
            throw new IllegalStateException("An empty queue does not have a minimum value.");
        }
        --this.size;
        int i = 1;
        while (i * 2 <= this.size) {
            int child = i * 2;
            if (child != this.size && this.keys[child + 1] < this.keys[child]) {
                ++child;
            }
            if (lastPrio <= this.keys[child]) break;
            this.elem[i] = this.elem[child];
            this.keys[i] = this.keys[child];
            i = child;
        }
        this.elem[i] = lastElem;
        this.keys[i] = lastPrio;
        return minElem;
    }

    @Override
    public void update(Number key, Integer value) {
        this.update_(key.intValue(), value);
    }

    public void update_(int key, int value) {
        int i;
        for (i = 1; i <= this.size && this.elem[i] != value; ++i) {
        }
        if (i > this.size) {
            return;
        }
        if (key > this.keys[i]) {
            while (i * 2 <= this.size) {
                int child = i * 2;
                if (child != this.size && this.keys[child + 1] < this.keys[child]) {
                    ++child;
                }
                if (key <= this.keys[child]) break;
                this.elem[i] = this.elem[child];
                this.keys[i] = this.keys[child];
                i = child;
            }
            this.elem[i] = value;
            this.keys[i] = key;
        } else {
            while (this.keys[i / 2] > key) {
                this.elem[i] = this.elem[i / 2];
                this.keys[i] = this.keys[i / 2];
                i /= 2;
            }
            this.elem[i] = value;
            this.keys[i] = key;
        }
    }

    public void reset() {
        this.size = 0;
    }

    @Override
    public void insert(Number key, Integer value) {
        this.insert_(key.intValue(), value);
    }

    public void insert_(int key, int value) {
        ++this.size;
        if (this.size > this.capacity) {
            this.ensureCapacity((int)((double)this.capacity * 2.0));
        }
        int i = this.size;
        while (this.keys[i / 2] > key) {
            this.elem[i] = this.elem[i / 2];
            this.keys[i] = this.keys[i / 2];
            i /= 2;
        }
        this.elem[i] = value;
        this.keys[i] = key;
    }

    @Override
    public void ensureCapacity(int capacity) {
        if (capacity < this.size) {
            throw new IllegalStateException("BinHeap contains too many elements to fit in new capacity.");
        }
        this.capacity = capacity;
        this.keys = Arrays.copyOf(this.keys, capacity + 1);
        this.elem = Arrays.copyOf(this.elem, capacity + 1);
    }

    @Override
    public void clear() {
        this.size = 0;
        Arrays.fill(this.keys, 0);
        Arrays.fill(this.elem, 0);
    }
}

