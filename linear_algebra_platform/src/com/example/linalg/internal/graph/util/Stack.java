/*
 * Decompiled with CFR 0_121.
 */
package com.example.linalg.internal.graph.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Stack<Item>
implements Iterable<Item> {
    private Node<Item> first = null;
    private int N = 0;

    public boolean isEmpty() {
        return this.first == null;
    }

    public int size() {
        return this.N;
    }

    public void push(Item item) {
        Node<Item> oldfirst = this.first;
        this.first = new Node();
        this.first.item = item;
        this.first.next = oldfirst;
        ++this.N;
    }

    public Item pop() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Stack underflow");
        }
        Object item = this.first.item;
        this.first = this.first.next;
        --this.N;
        return (Item)item;
    }

    public Item peek() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Stack underflow");
        }
        return (Item)this.first.item;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Item item : this) {
            s.append(item + " ");
        }
        return s.toString();
    }

    @Override
    public Iterator<Item> iterator() {
        return new ListIterator<Item>(this.first);
    }

    private class ListIterator<Item>
    implements Iterator<Item> {
        private Node<Item> current;

        public ListIterator(Node<Item> first) {
            this.current = first;
        }

        @Override
        public boolean hasNext() {
            return this.current != null;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Item next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            Object item = this.current.item;
            this.current = this.current.next;
            return (Item)item;
        }
    }

    private static class Node<Item> {
        private Item item;
        private Node<Item> next;

        private Node() {
        }
    }

}

