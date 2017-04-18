/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.chart;

import edu.cyberapex.chart.PriorityNode;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class MyPriorityQueue {
    private PriorityQueue<PriorityNode> queue = new PriorityQueue();
    private Map<Integer, PriorityNode> map = new HashMap<Integer, PriorityNode>();

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    public PriorityNode poll() {
        PriorityNode first = this.queue.poll();
        while (!this.map.containsValue(first)) {
            first = this.queue.poll();
        }
        this.map.remove(first.fetchId());
        return first;
    }

    public boolean addIfUseful(PriorityNode t) {
        int id = t.fetchId();
        if (!this.map.containsKey(id) || t.grabRank() < this.map.get(id).grabRank()) {
            return this.addIfUsefulAssist(t, id);
        }
        return false;
    }

    private boolean addIfUsefulAssist(PriorityNode t, int id) {
        this.map.remove(id);
        this.queue.add(t);
        this.map.put(id, t);
        return true;
    }

    public String toString() {
        return this.map.toString();
    }

    public void add(PriorityNode t) {
        this.queue.add(t);
        this.map.put(t.fetchId(), t);
    }

    public void remove(PriorityNode t) {
        this.map.remove(t.fetchId());
    }

    public boolean contains(PriorityNode t) {
        return this.map.containsKey(t.fetchId());
    }

    public PriorityNode peekFirst() {
        PriorityNode first = this.queue.peek();
        while (!this.map.containsValue(first)) {
            this.queue.poll();
            first = this.queue.peek();
        }
        return first;
    }
}

