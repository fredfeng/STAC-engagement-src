/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.storage;

import com.graphhopper.storage.DataAccess;
import com.graphhopper.storage.Directory;
import com.graphhopper.storage.Graph;
import com.graphhopper.storage.GraphExtension;
import com.graphhopper.storage.NodeAccess;

public class TurnCostExtension
implements GraphExtension {
    private final int NO_TURN_ENTRY = -1;
    private final long EMPTY_FLAGS = 0;
    private final int TC_FROM;
    private final int TC_TO;
    private final int TC_FLAGS;
    private final int TC_NEXT;
    private DataAccess turnCosts;
    private int turnCostsEntryIndex = -4;
    private int turnCostsEntryBytes;
    private int turnCostsCount;
    private NodeAccess nodeAccess;

    public TurnCostExtension() {
        this.TC_FROM = this.nextTurnCostEntryIndex();
        this.TC_TO = this.nextTurnCostEntryIndex();
        this.TC_FLAGS = this.nextTurnCostEntryIndex();
        this.TC_NEXT = this.nextTurnCostEntryIndex();
        this.turnCostsEntryBytes = this.turnCostsEntryIndex + 4;
        this.turnCostsCount = 0;
    }

    @Override
    public void init(Graph graph, Directory dir) {
        if (this.turnCostsCount > 0) {
            throw new AssertionError((Object)"The turn cost storage must be initialized only once.");
        }
        this.nodeAccess = graph.getNodeAccess();
        this.turnCosts = dir.find("turn_costs");
    }

    private int nextTurnCostEntryIndex() {
        this.turnCostsEntryIndex += 4;
        return this.turnCostsEntryIndex;
    }

    @Override
    public void setSegmentSize(int bytes) {
        this.turnCosts.setSegmentSize(bytes);
    }

    @Override
    public TurnCostExtension create(long initBytes) {
        this.turnCosts.create(initBytes * (long)this.turnCostsEntryBytes);
        return this;
    }

    @Override
    public void flush() {
        this.turnCosts.setHeader(0, this.turnCostsEntryBytes);
        this.turnCosts.setHeader(4, this.turnCostsCount);
        this.turnCosts.flush();
    }

    @Override
    public void close() {
        this.turnCosts.close();
    }

    @Override
    public long getCapacity() {
        return this.turnCosts.getCapacity();
    }

    @Override
    public boolean loadExisting() {
        if (!this.turnCosts.loadExisting()) {
            return false;
        }
        this.turnCostsEntryBytes = this.turnCosts.getHeader(0);
        this.turnCostsCount = this.turnCosts.getHeader(4);
        return true;
    }

    public void addTurnInfo(int from, int viaNode, int to, long turnFlags) {
        if (turnFlags == 0) {
            return;
        }
        int newEntryIndex = this.turnCostsCount++;
        this.ensureTurnCostIndex(newEntryIndex);
        int previousEntryIndex = this.nodeAccess.getAdditionalNodeField(viaNode);
        if (previousEntryIndex == -1) {
            this.nodeAccess.setAdditionalNodeField(viaNode, newEntryIndex);
        } else {
            int i = 0;
            int tmp = previousEntryIndex;
            while ((tmp = this.turnCosts.getInt((long)tmp * (long)this.turnCostsEntryBytes + (long)this.TC_NEXT)) != -1) {
                previousEntryIndex = tmp;
                if (i++ <= 1000) continue;
                throw new IllegalStateException("Something unexpected happened. A node probably will not have 1000+ relations.");
            }
            this.turnCosts.setInt((long)previousEntryIndex * (long)this.turnCostsEntryBytes + (long)this.TC_NEXT, newEntryIndex);
        }
        long costsBase = (long)newEntryIndex * (long)this.turnCostsEntryBytes;
        this.turnCosts.setInt(costsBase + (long)this.TC_FROM, from);
        this.turnCosts.setInt(costsBase + (long)this.TC_TO, to);
        this.turnCosts.setInt(costsBase + (long)this.TC_FLAGS, (int)turnFlags);
        this.turnCosts.setInt(costsBase + (long)this.TC_NEXT, -1);
    }

    public long getTurnCostFlags(int edgeFrom, int nodeVia, int edgeTo) {
        if (edgeFrom == -1 || edgeTo == -1) {
            throw new IllegalArgumentException("from and to edge cannot be NO_EDGE");
        }
        if (nodeVia < 0) {
            throw new IllegalArgumentException("via node cannot be negative");
        }
        return this.nextCostFlags(edgeFrom, nodeVia, edgeTo);
    }

    private long nextCostFlags(int edgeFrom, int nodeVia, int edgeTo) {
        int i;
        int turnCostIndex = this.nodeAccess.getAdditionalNodeField(nodeVia);
        for (i = 0; i < 1000 && turnCostIndex != -1; ++i) {
            long turnCostPtr = (long)turnCostIndex * (long)this.turnCostsEntryBytes;
            if (edgeFrom == this.turnCosts.getInt(turnCostPtr + (long)this.TC_FROM) && edgeTo == this.turnCosts.getInt(turnCostPtr + (long)this.TC_TO)) {
                return this.turnCosts.getInt(turnCostPtr + (long)this.TC_FLAGS);
            }
            int nextTurnCostIndex = this.turnCosts.getInt(turnCostPtr + (long)this.TC_NEXT);
            if (nextTurnCostIndex == turnCostIndex) {
                throw new IllegalStateException("something went wrong: next entry would be the same");
            }
            turnCostIndex = nextTurnCostIndex;
        }
        if (i > 1000) {
            throw new IllegalStateException("something went wrong: there seems to be no end of the turn cost-list!?");
        }
        return 0;
    }

    private void ensureTurnCostIndex(int nodeIndex) {
        this.turnCosts.ensureCapacity(((long)nodeIndex + 4) * (long)this.turnCostsEntryBytes);
    }

    @Override
    public boolean isRequireNodeField() {
        return true;
    }

    @Override
    public boolean isRequireEdgeField() {
        return false;
    }

    @Override
    public int getDefaultNodeFieldValue() {
        return -1;
    }

    @Override
    public int getDefaultEdgeFieldValue() {
        throw new UnsupportedOperationException("Not supported by this storage");
    }

    @Override
    public GraphExtension copyTo(GraphExtension clonedStorage) {
        if (!(clonedStorage instanceof TurnCostExtension)) {
            throw new IllegalStateException("the extended storage to clone must be the same");
        }
        TurnCostExtension clonedTC = (TurnCostExtension)clonedStorage;
        this.turnCosts.copyTo(clonedTC.turnCosts);
        clonedTC.turnCostsCount = this.turnCostsCount;
        return clonedStorage;
    }

    @Override
    public boolean isClosed() {
        return this.turnCosts.isClosed();
    }

    public String toString() {
        return "turnCost";
    }
}

