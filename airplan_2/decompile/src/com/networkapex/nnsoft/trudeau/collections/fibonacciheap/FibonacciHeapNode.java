/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.nnsoft.trudeau.collections.fibonacciheap;

final class FibonacciHeapNode<E> {
    private final E element;
    private FibonacciHeapNode<E> parent;
    private FibonacciHeapNode<E> one;
    private FibonacciHeapNode<E> two;
    private FibonacciHeapNode<E> child;
    private int degree;
    private boolean marked;

    public FibonacciHeapNode(E element) {
        this.one = this;
        this.two = this;
        this.degree = 0;
        this.assignParent(null);
        this.assignChild(null);
        this.assignOne(this);
        this.assignTwo(this);
        this.defineMarked(false);
        this.element = element;
    }

    public FibonacciHeapNode<E> takeParent() {
        return this.parent;
    }

    public void assignParent(FibonacciHeapNode<E> parent) {
        this.parent = parent;
    }

    public FibonacciHeapNode<E> fetchOne() {
        return this.one;
    }

    public void assignOne(FibonacciHeapNode<E> one) {
        this.one = one;
    }

    public FibonacciHeapNode<E> fetchTwo() {
        return this.two;
    }

    public void assignTwo(FibonacciHeapNode<E> two) {
        this.two = two;
    }

    public FibonacciHeapNode<E> takeChild() {
        return this.child;
    }

    public void assignChild(FibonacciHeapNode<E> child) {
        this.child = child;
    }

    public int getDegree() {
        return this.degree;
    }

    public void incraeseDegree() {
        ++this.degree;
    }

    public void decraeseDegree() {
        --this.degree;
    }

    public boolean isMarked() {
        return this.marked;
    }

    public void defineMarked(boolean marked) {
        this.marked = marked;
    }

    public E pullElement() {
        return this.element;
    }

    public String toString() {
        return this.element.toString();
    }
}

