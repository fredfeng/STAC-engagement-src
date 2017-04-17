/*
 * Decompiled with CFR 0_121.
 */
package com.example.linalg.internal.graph.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Queue<Item>
implements Iterable<Item> {
    private Node<Item> first = null;
    private Node<Item> last = null;
    private int N = 0;

    public boolean isEmpty() {
        return this.first == null;
    }

    public int size() {
        return this.N;
    }

    public Item peek() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Queue underflow");
        }
        return (Item)this.first.item;
    }

    public void enqueue(Item item) {
        Node<Item> oldlast = this.last;
        this.last = new Node();
        this.last.item = item;
        this.last.next = null;
        if (this.isEmpty()) {
            this.first = this.last;
        } else {
            oldlast.next = this.last;
        }
        ++this.N;
    }

    public Item dequeue() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Queue underflow");
        }
        Object item = this.first.item;
        this.first = this.first.next;
        --this.N;
        if (this.isEmpty()) {
            this.last = null;
        }
        return (Item)item;
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

