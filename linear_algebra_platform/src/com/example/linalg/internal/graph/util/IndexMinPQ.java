/*
 * Decompiled with CFR 0_121.
 */
package com.example.linalg.internal.graph.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class IndexMinPQ<Key extends Comparable<Key>>
implements Iterable<Integer> {
    private int maxN;
    private int N;
    private int[] pq;
    private int[] qp;
    private Key[] keys;

    public IndexMinPQ(int maxN) {
        if (maxN < 0) {
            throw new IllegalArgumentException();
        }
        this.maxN = maxN;
        this.keys = (Key[]) new Comparable[maxN + 1];
        this.pq = new int[maxN + 1];
        this.qp = new int[maxN + 1];
        for (int i = 0; i <= maxN; ++i) {
            this.qp[i] = -1;
        }
    }

    public boolean isEmpty() {
        return this.N == 0;
    }

    public boolean contains(int i) {
        if (i < 0 || i >= this.maxN) {
            throw new IndexOutOfBoundsException();
        }
        return this.qp[i] != -1;
    }

    public int size() {
        return this.N;
    }

    public void insert(int i, Key key) {
        if (i < 0 || i >= this.maxN) {
            throw new IndexOutOfBoundsException();
        }
        if (this.contains(i)) {
            throw new IllegalArgumentException("index is already in the priority queue");
        }
        ++this.N;
        this.qp[i] = this.N;
        this.pq[this.N] = i;
        this.keys[i] = key;
        this.swim(this.N);
    }

    public int minIndex() {
        if (this.N == 0) {
            throw new NoSuchElementException("Priority queue underflow");
        }
        return this.pq[1];
    }

    public Key minKey() {
        if (this.N == 0) {
            throw new NoSuchElementException("Priority queue underflow");
        }
        return this.keys[this.pq[1]];
    }

    public int delMin() {
        if (this.N == 0) {
            throw new NoSuchElementException("Priority queue underflow");
        }
        int min = this.pq[1];
        this.exch(1, this.N--);
        this.sink(1);
        assert (min == this.pq[this.N + 1]);
        this.qp[min] = -1;
        this.keys[min] = null;
        this.pq[this.N + 1] = -1;
        return min;
    }

    public Key keyOf(int i) {
        if (i < 0 || i >= this.maxN) {
            throw new IndexOutOfBoundsException();
        }
        if (!this.contains(i)) {
            throw new NoSuchElementException("index is not in the priority queue");
        }
        return this.keys[i];
    }

    public void changeKey(int i, Key key) {
        if (i < 0 || i >= this.maxN) {
            throw new IndexOutOfBoundsException();
        }
        if (!this.contains(i)) {
            throw new NoSuchElementException("index is not in the priority queue");
        }
        this.keys[i] = key;
        this.swim(this.qp[i]);
        this.sink(this.qp[i]);
    }

    public void change(int i, Key key) {
        this.changeKey(i, key);
    }

    public void decreaseKey(int i, Key key) {
        if (i < 0 || i >= this.maxN) {
            throw new IndexOutOfBoundsException();
        }
        if (!this.contains(i)) {
            throw new NoSuchElementException("index is not in the priority queue");
        }
        if (this.keys[i].compareTo(key) <= 0) {
            throw new IllegalArgumentException("Calling decreaseKey() with given argument would not strictly decrease the key");
        }
        this.keys[i] = key;
        this.swim(this.qp[i]);
    }

    public void increaseKey(int i, Key key) {
        if (i < 0 || i >= this.maxN) {
            throw new IndexOutOfBoundsException();
        }
        if (!this.contains(i)) {
            throw new NoSuchElementException("index is not in the priority queue");
        }
        if (this.keys[i].compareTo(key) >= 0) {
            throw new IllegalArgumentException("Calling increaseKey() with given argument would not strictly increase the key");
        }
        this.keys[i] = key;
        this.sink(this.qp[i]);
    }

    public void delete(int i) {
        if (i < 0 || i >= this.maxN) {
            throw new IndexOutOfBoundsException();
        }
        if (!this.contains(i)) {
            throw new NoSuchElementException("index is not in the priority queue");
        }
        int index = this.qp[i];
        this.exch(index, this.N--);
        this.swim(index);
        this.sink(index);
        this.keys[i] = null;
        this.qp[i] = -1;
    }

    private boolean greater(int i, int j) {
        return this.keys[this.pq[i]].compareTo(this.keys[this.pq[j]]) > 0;
    }

    private void exch(int i, int j) {
        int swap = this.pq[i];
        this.pq[i] = this.pq[j];
        this.pq[j] = swap;
        this.qp[this.pq[i]] = i;
        this.qp[this.pq[j]] = j;
    }

    private void swim(int k) {
        while (k > 1 && this.greater(k / 2, k)) {
            this.exch(k, k / 2);
            k /= 2;
        }
    }

    private void sink(int k) {
        while (2 * k <= this.N) {
            int j = 2 * k;
            if (j < this.N && this.greater(j, j + 1)) {
                ++j;
            }
            if (!this.greater(k, j)) break;
            this.exch(k, j);
            k = j;
        }
    }

    @Override
    public Iterator<Integer> iterator() {
        return new HeapIterator();
    }

    private class HeapIterator
    implements Iterator<Integer> {
        private IndexMinPQ<Key> copy;

        public HeapIterator() {
            this.copy = new IndexMinPQ(IndexMinPQ.this.pq.length - 1);
            for (int i = 1; i <= IndexMinPQ.this.N; ++i) {
                this.copy.insert(IndexMinPQ.this.pq[i], IndexMinPQ.this.keys[IndexMinPQ.this.pq[i]]);
            }
        }

        @Override
        public boolean hasNext() {
            return !this.copy.isEmpty();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Integer next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return this.copy.delMin();
        }
    }

}

