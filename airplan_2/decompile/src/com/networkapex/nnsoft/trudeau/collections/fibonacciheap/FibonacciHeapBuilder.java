/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.nnsoft.trudeau.collections.fibonacciheap;

import com.networkapex.nnsoft.trudeau.collections.fibonacciheap.FibonacciHeap;
import java.util.Comparator;

public class FibonacciHeapBuilder<E> {
    private Comparator<? super E> comparator = null;

    public FibonacciHeapBuilder setComparator(Comparator<? super E> comparator) {
        this.comparator = comparator;
        return this;
    }

    public FibonacciHeap generateFibonacciHeap() {
        return new FibonacciHeap<E>(this.comparator);
    }
}

