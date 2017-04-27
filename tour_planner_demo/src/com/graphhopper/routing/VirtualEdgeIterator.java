/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing;

import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.util.CHEdgeIteratorState;
import com.graphhopper.util.EdgeIterator;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.PointList;
import java.util.ArrayList;
import java.util.List;

class VirtualEdgeIterator
implements EdgeIterator,
CHEdgeIteratorState {
    private final List<EdgeIteratorState> edges;
    private int current;

    public VirtualEdgeIterator(int edgeCount) {
        this.edges = new ArrayList<EdgeIteratorState>(edgeCount);
        this.reset();
    }

    void add(EdgeIteratorState edge) {
        this.edges.add(edge);
    }

    EdgeIterator reset() {
        this.current = -1;
        return this;
    }

    int count() {
        return this.edges.size();
    }

    @Override
    public boolean next() {
        ++this.current;
        return this.current < this.edges.size();
    }

    @Override
    public EdgeIteratorState detach(boolean reverse) {
        if (reverse) {
            throw new IllegalStateException("Not yet supported");
        }
        return this.edges.get(this.current);
    }

    @Override
    public int getEdge() {
        return this.edges.get(this.current).getEdge();
    }

    @Override
    public int getBaseNode() {
        return this.edges.get(this.current).getBaseNode();
    }

    @Override
    public int getAdjNode() {
        return this.edges.get(this.current).getAdjNode();
    }

    @Override
    public PointList fetchWayGeometry(int mode) {
        return this.edges.get(this.current).fetchWayGeometry(mode);
    }

    @Override
    public EdgeIteratorState setWayGeometry(PointList list) {
        return this.edges.get(this.current).setWayGeometry(list);
    }

    @Override
    public double getDistance() {
        return this.edges.get(this.current).getDistance();
    }

    @Override
    public EdgeIteratorState setDistance(double dist) {
        return this.edges.get(this.current).setDistance(dist);
    }

    @Override
    public long getFlags() {
        return this.edges.get(this.current).getFlags();
    }

    @Override
    public EdgeIteratorState setFlags(long flags) {
        return this.edges.get(this.current).setFlags(flags);
    }

    @Override
    public String getName() {
        return this.edges.get(this.current).getName();
    }

    @Override
    public EdgeIteratorState setName(String name) {
        return this.edges.get(this.current).setName(name);
    }

    @Override
    public boolean getBoolean(int key, boolean reverse, boolean _default) {
        return this.edges.get(this.current).getBoolean(key, reverse, _default);
    }

    public String toString() {
        return this.edges.toString();
    }

    @Override
    public int getAdditionalField() {
        return this.edges.get(this.current).getAdditionalField();
    }

    @Override
    public EdgeIteratorState setAdditionalField(int value) {
        return this.edges.get(this.current).setAdditionalField(value);
    }

    @Override
    public EdgeIteratorState copyPropertiesTo(EdgeIteratorState edge) {
        return this.edges.get(this.current).copyPropertiesTo(edge);
    }

    @Override
    public boolean isBackward(FlagEncoder encoder) {
        return this.edges.get(this.current).isBackward(encoder);
    }

    @Override
    public boolean isForward(FlagEncoder encoder) {
        return this.edges.get(this.current).isForward(encoder);
    }

    @Override
    public boolean isShortcut() {
        EdgeIteratorState edge = this.edges.get(this.current);
        return edge instanceof CHEdgeIteratorState && ((CHEdgeIteratorState)edge).isShortcut();
    }

    @Override
    public double getWeight() {
        return ((CHEdgeIteratorState)this.edges.get(this.current)).getWeight();
    }

    @Override
    public CHEdgeIteratorState setWeight(double weight) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public int getSkippedEdge1() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public int getSkippedEdge2() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void setSkippedEdges(int edge1, int edge2) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean canBeOverwritten(long flags) {
        throw new UnsupportedOperationException("Not supported.");
    }
}

