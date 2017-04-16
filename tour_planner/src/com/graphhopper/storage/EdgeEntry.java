/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.storage;

public class EdgeEntry
implements Cloneable,
Comparable<EdgeEntry> {
    public int edge;
    public int adjNode;
    public double weight;
    public EdgeEntry parent;

    public EdgeEntry(int edgeId, int adjNode, double weight) {
        this.edge = edgeId;
        this.adjNode = adjNode;
        this.weight = weight;
    }

    public EdgeEntry clone() {
        return new EdgeEntry(this.edge, this.adjNode, this.weight);
    }

    public EdgeEntry cloneFull() {
        EdgeEntry de = this.clone();
        EdgeEntry tmpPrev = this.parent;
        EdgeEntry cl = de;
        while (tmpPrev != null) {
            cl = cl.parent = tmpPrev.clone();
            tmpPrev = tmpPrev.parent;
        }
        return de;
    }

    @Override
    public int compareTo(EdgeEntry o) {
        if (this.weight < o.weight) {
            return -1;
        }
        return this.weight > o.weight ? 1 : 0;
    }

    public String toString() {
        return "" + this.adjNode + " (" + this.edge + ") weight: " + this.weight;
    }
}

