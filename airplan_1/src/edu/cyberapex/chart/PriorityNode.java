/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.chart;

class PriorityNode
implements Comparable<PriorityNode> {
    private int id;
    private double rank;
    private int goal;

    public PriorityNode(int nodeId, double rank, int goal) {
        this.id = nodeId;
        this.rank = rank;
        this.goal = goal;
    }

    public int fetchId() {
        return this.id;
    }

    public double grabRank() {
        return this.rank;
    }

    @Override
    public int compareTo(PriorityNode other) {
        int res = Double.compare(this.rank, other.rank);
        if (res == 0) {
            if (this.id == this.goal) {
                return 1;
            }
            if (other.fetchId() == this.goal) {
                return -1;
            }
        }
        return res;
    }

    public String toString() {
        return "(id: " + this.id + ", rank " + this.rank + ")";
    }
}

