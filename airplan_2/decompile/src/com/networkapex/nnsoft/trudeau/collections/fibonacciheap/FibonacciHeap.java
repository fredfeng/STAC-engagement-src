/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.nnsoft.trudeau.collections.fibonacciheap;

import com.networkapex.nnsoft.trudeau.collections.fibonacciheap.FibonacciHeapNode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public final class FibonacciHeap<E>
implements Queue<E> {
    private static final double LOG_PHI = Math.log((1.0 + Math.sqrt(5.0)) / 2.0);
    private final Set<E> elementsIndex = new HashSet();
    private final Comparator<? super E> comparator;
    private int size = 0;
    private int trees = 0;
    private int markedNodes = 0;
    private FibonacciHeapNode<E> minimumNode;

    public FibonacciHeap() {
        this(null);
    }

    public FibonacciHeap(Comparator<? super E> comparator) {
        this.comparator = comparator;
    }

    private void moveToRoot(FibonacciHeapNode<E> node) {
        if (this.isEmpty()) {
            this.minimumNode = node;
        } else {
            node.fetchOne().assignTwo(node.fetchTwo());
            node.fetchTwo().assignOne(node.fetchOne());
            node.assignOne(this.minimumNode);
            node.assignTwo(this.minimumNode.fetchTwo());
            this.minimumNode.assignTwo(node);
            node.fetchTwo().assignOne(node);
            if (this.compare(node, this.minimumNode) < 0) {
                this.moveToRootAid(node);
            }
        }
    }

    private void moveToRootAid(FibonacciHeapNode<E> node) {
        this.minimumNode = node;
    }

    @Override
    public boolean add(E e) {
        if (e == null) {
            return this.addExecutor();
        }
        FibonacciHeapNode<E> node = new FibonacciHeapNode<E>(e);
        this.moveToRoot(node);
        ++this.size;
        this.elementsIndex.add(e);
        return true;
    }

    private boolean addExecutor() {
        throw new IllegalArgumentException("Null elements not allowed in this FibonacciHeap implementation.");
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E element : c) {
            this.addAllSupervisor(element);
        }
        return true;
    }

    private void addAllSupervisor(E element) {
        this.add(element);
    }

    @Override
    public void clear() {
        this.minimumNode = null;
        this.size = 0;
        this.trees = 0;
        this.markedNodes = 0;
        this.elementsIndex.clear();
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        return this.elementsIndex.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) {
            return false;
        }
        for (Object o : c) {
            if (this.contains(o)) continue;
            return false;
        }
        return true;
    }

    @Override
    public boolean isEmpty() {
        return this.minimumNode == null;
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E element() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.peek();
    }

    @Override
    public boolean offer(E e) {
        return this.add(e);
    }

    @Override
    public E peek() {
        if (this.isEmpty()) {
            return null;
        }
        return this.minimumNode.pullElement();
    }

    @Override
    public E poll() {
        if (this.isEmpty()) {
            return null;
        }
        FibonacciHeapNode<E> z = this.minimumNode;
        FibonacciHeapNode x = z.takeChild();
        for (int numOfKids = z.getDegree(); numOfKids > 0; --numOfKids) {
            FibonacciHeapNode<E> tempTwo = x.fetchTwo();
            this.moveToRoot(x);
            x.assignParent(null);
            x = tempTwo;
        }
        z.fetchOne().assignTwo(z.fetchTwo());
        z.fetchTwo().assignOne(z.fetchOne());
        if (z == z.fetchTwo()) {
            this.minimumNode = null;
        } else {
            this.minimumNode = z.fetchTwo();
            this.consolidate();
        }
        --this.size;
        E minimum = z.pullElement();
        this.elementsIndex.remove(minimum);
        return minimum;
    }

    @Override
    public E remove() {
        if (this.isEmpty()) {
            return new FibonacciHeapAssist().invoke();
        }
        return this.poll();
    }

    private void consolidate() {
        if (this.isEmpty()) {
            return;
        }
        int arraySize = (int)Math.floor(Math.log(this.size) / LOG_PHI);
        List<FibonacciHeapNode<E>> nodeSequence = new ArrayList<>(arraySize);
        for (int j = 0; j < arraySize; ++j) {
            this.consolidateManager(nodeSequence, j);
        }
        int numRoots = 0;
        FibonacciHeapNode<E> x = this.minimumNode;
        if (x != null) {
            ++numRoots;
            for (x = x.fetchTwo(); x != this.minimumNode; x = x.fetchTwo()) {
                ++numRoots;
            }
        }
        while (numRoots > 0) {
            int degree = x.getDegree();
            FibonacciHeapNode<E> next = x.fetchTwo();
            while (nodeSequence.get(degree) != null) {
                FibonacciHeapNode<E> y = nodeSequence.get(degree);
                if (this.compare(x, y) > 0) {
                    FibonacciHeapNode<E> pointer = y;
                    y = x;
                    x = pointer;
                }
                this.link(y, x);
                nodeSequence.set(degree, null);
                ++degree;
            }
            nodeSequence.set(degree, x);
            x = next;
            --numRoots;
        }
        this.minimumNode = null;
        for (int a = 0; a < nodeSequence.size(); ++a) {
            FibonacciHeapNode<E> pointer = nodeSequence.get(a);
            if (pointer == null) continue;
            if (this.minimumNode == null) {
                this.consolidateEngine(pointer);
            }
            if (this.minimumNode == null) continue;
            this.moveToRoot(pointer);
        }
    }

    private void consolidateEngine(FibonacciHeapNode<E> pointer) {
        this.minimumNode = pointer;
    }

    private void consolidateManager(List<FibonacciHeapNode<E>> nodeSequence, int a) {
        nodeSequence.add(a, null);
    }

    private void link(FibonacciHeapNode<E> y, FibonacciHeapNode<E> x) {
        y.fetchOne().assignTwo(y.fetchTwo());
        y.fetchTwo().assignOne(y.fetchOne());
        y.assignParent(x);
        if (x.takeChild() == null) {
            this.linkGateKeeper(y, x);
        } else {
            y.assignOne(x.takeChild());
            y.assignTwo(x.takeChild().fetchTwo());
            x.takeChild().assignTwo(y);
            y.fetchTwo().assignOne(y);
        }
        x.incraeseDegree();
        y.defineMarked(false);
        ++this.markedNodes;
    }

    private void linkGateKeeper(FibonacciHeapNode<E> y, FibonacciHeapNode<E> x) {
        new FibonacciHeapTarget(y, x).invoke();
    }

    private void cut(FibonacciHeapNode<E> x, FibonacciHeapNode<E> y) {
        this.moveToRoot(x);
        y.decraeseDegree();
        x.assignParent(null);
        x.defineMarked(false);
        --this.markedNodes;
    }

    private void cascadingCut(FibonacciHeapNode<E> y) {
        FibonacciHeapNode<E> z = y.takeParent();
        if (z != null) {
            this.cascadingCutCoordinator(y, z);
        }
    }

    private void cascadingCutCoordinator(FibonacciHeapNode<E> y, FibonacciHeapNode<E> z) {
        if (!y.isMarked()) {
            y.defineMarked(true);
            ++this.markedNodes;
        } else {
            this.cut(y, z);
            this.cascadingCut(z);
        }
    }

    public int potential() {
        return this.trees + 2 * this.markedNodes;
    }

    private int compare(FibonacciHeapNode<E> o1, FibonacciHeapNode<E> o2) {
        if (this.comparator != null) {
            return this.comparator.compare(o1.pullElement(), o2.pullElement());
        }
        Comparable o1Comparable = (Comparable)o1.pullElement();
        return o1Comparable.compareTo(o2.pullElement());
    }

    public String toString() {
        if (this.minimumNode == null) {
            return "FibonacciHeap=[]";
        }
        Stack stack = new Stack();
        stack.push(this.minimumNode);
        StringBuilder buf = new StringBuilder("FibonacciHeap=[");
        while (!stack.empty()) {
            FibonacciHeapNode curr = (FibonacciHeapNode)stack.pop();
            buf.append(curr);
            buf.append(", ");
            if (curr.takeChild() != null) {
                stack.push(curr.takeChild());
            }
            FibonacciHeapNode start = curr;
            for (curr = curr.fetchTwo(); curr != start; curr = curr.fetchTwo()) {
                buf.append(curr);
                buf.append(", ");
                if (curr.takeChild() == null) continue;
                this.toStringAid(stack, curr);
            }
        }
        buf.append(']');
        return buf.toString();
    }

    private void toStringAid(Stack<FibonacciHeapNode<E>> stack, FibonacciHeapNode<E> curr) {
        stack.push(curr.takeChild());
    }

    private class FibonacciHeapTarget {
        private FibonacciHeapNode<E> y;
        private FibonacciHeapNode<E> x;

        public FibonacciHeapTarget(FibonacciHeapNode<E> y, FibonacciHeapNode<E> x) {
            this.y = y;
            this.x = x;
        }

        public void invoke() {
            this.x.assignChild(this.y);
            this.y.assignTwo(this.y);
            this.y.assignOne(this.y);
        }
    }

    private class FibonacciHeapAssist {
        private FibonacciHeapAssist() {
        }

        public E invoke() {
            throw new NoSuchElementException();
        }
    }

}

