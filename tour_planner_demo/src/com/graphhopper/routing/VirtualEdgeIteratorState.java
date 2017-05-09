/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing;

import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.util.CHEdgeIteratorState;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.PointList;

public class VirtualEdgeIteratorState
implements EdgeIteratorState,
CHEdgeIteratorState {
    private final PointList pointList;
    private final int edgeId;
    private double distance;
    private long flags;
    private String name;
    private final int baseNode;
    private final int adjNode;
    private final int originalTraversalKey;
    private boolean unfavored;

    public VirtualEdgeIteratorState(int originalTraversalKey, int edgeId, int baseNode, int adjNode, double distance, long flags, String name, PointList pointList) {
        this.originalTraversalKey = originalTraversalKey;
        this.edgeId = edgeId;
        this.baseNode = baseNode;
        this.adjNode = adjNode;
        this.distance = distance;
        this.flags = flags;
        this.name = name;
        this.pointList = pointList;
    }

    public int getOriginalTraversalKey() {
        return this.originalTraversalKey;
    }

    @Override
    public int getEdge() {
        return this.edgeId;
    }

    @Override
    public int getBaseNode() {
        return this.baseNode;
    }

    @Override
    public int getAdjNode() {
        return this.adjNode;
    }

    @Override
    public PointList fetchWayGeometry(int mode) {
        if (this.pointList.getSize() == 0) {
            return PointList.EMPTY;
        }
        if (mode == 3) {
            return this.pointList.clone(false);
        }
        if (mode == 1) {
            return this.pointList.copy(0, this.pointList.getSize() - 1);
        }
        if (mode == 2) {
            return this.pointList.copy(1, this.pointList.getSize());
        }
        if (mode == 0) {
            if (this.pointList.getSize() == 1) {
                return PointList.EMPTY;
            }
            return this.pointList.copy(1, this.pointList.getSize() - 1);
        }
        throw new UnsupportedOperationException("Illegal mode:" + mode);
    }

    @Override
    public EdgeIteratorState setWayGeometry(PointList list) {
        throw new UnsupportedOperationException("Not supported for virtual edge. Set when creating it.");
    }

    @Override
    public double getDistance() {
        return this.distance;
    }

    @Override
    public EdgeIteratorState setDistance(double dist) {
        this.distance = dist;
        return this;
    }

    @Override
    public long getFlags() {
        return this.flags;
    }

    @Override
    public EdgeIteratorState setFlags(long flags) {
        this.flags = flags;
        return this;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public EdgeIteratorState setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean getBoolean(int key, boolean reverse, boolean _default) {
        if (key == -1) {
            return this.unfavored;
        }
        return _default;
    }

    public void setVirtualEdgePreference(boolean unfavored) {
        this.unfavored = unfavored;
    }

    public String toString() {
        return "" + this.baseNode + "->" + this.adjNode;
    }

    @Override
    public boolean isShortcut() {
        return false;
    }

    @Override
    public boolean isForward(FlagEncoder encoder) {
        return encoder.isForward(this.getFlags());
    }

    @Override
    public boolean isBackward(FlagEncoder encoder) {
        return encoder.isBackward(this.getFlags());
    }

    @Override
    public int getAdditionalField() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean canBeOverwritten(long flags) {
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
    public EdgeIteratorState detach(boolean reverse) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public EdgeIteratorState setAdditionalField(int value) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public EdgeIteratorState copyPropertiesTo(EdgeIteratorState edge) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public CHEdgeIteratorState setWeight(double weight) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public double getWeight() {
        throw new UnsupportedOperationException("Not supported.");
    }
}

