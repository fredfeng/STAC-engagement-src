/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.storage;

import com.graphhopper.storage.BaseGraph;
import com.graphhopper.storage.DataAccess;
import com.graphhopper.storage.GraphExtension;
import com.graphhopper.storage.NodeAccess;
import com.graphhopper.util.Helper;
import com.graphhopper.util.shapes.BBox;

class GHNodeAccess
implements NodeAccess {
    private final BaseGraph that;
    private final boolean elevation;

    public GHNodeAccess(BaseGraph that, boolean withElevation) {
        this.that = that;
        this.elevation = withElevation;
    }

    @Override
    public void ensureNode(int nodeId) {
        this.that.ensureNodeIndex(nodeId);
    }

    @Override
    public final void setNode(int nodeId, double lat, double lon) {
        this.setNode(nodeId, lat, lon, Double.NaN);
    }

    @Override
    public final void setNode(int nodeId, double lat, double lon, double ele) {
        this.that.ensureNodeIndex(nodeId);
        long tmp = (long)nodeId * (long)this.that.nodeEntryBytes;
        this.that.nodes.setInt(tmp + (long)this.that.N_LAT, Helper.degreeToInt(lat));
        this.that.nodes.setInt(tmp + (long)this.that.N_LON, Helper.degreeToInt(lon));
        if (this.is3D()) {
            this.that.nodes.setInt(tmp + (long)this.that.N_ELE, Helper.eleToInt(ele));
            this.that.bounds.update(lat, lon, ele);
        } else {
            this.that.bounds.update(lat, lon);
        }
        if (this.that.extStorage.isRequireNodeField()) {
            this.that.nodes.setInt(tmp + (long)this.that.N_ADDITIONAL, this.that.extStorage.getDefaultNodeFieldValue());
        }
    }

    @Override
    public final double getLatitude(int nodeId) {
        return Helper.intToDegree(this.that.nodes.getInt((long)nodeId * (long)this.that.nodeEntryBytes + (long)this.that.N_LAT));
    }

    @Override
    public final double getLongitude(int nodeId) {
        return Helper.intToDegree(this.that.nodes.getInt((long)nodeId * (long)this.that.nodeEntryBytes + (long)this.that.N_LON));
    }

    @Override
    public final double getElevation(int nodeId) {
        if (!this.elevation) {
            throw new IllegalStateException("Cannot access elevation - 3D is not enabled");
        }
        return Helper.intToEle(this.that.nodes.getInt((long)nodeId * (long)this.that.nodeEntryBytes + (long)this.that.N_ELE));
    }

    @Override
    public final double getEle(int nodeId) {
        return this.getElevation(nodeId);
    }

    @Override
    public final double getLat(int nodeId) {
        return this.getLatitude(nodeId);
    }

    @Override
    public final double getLon(int nodeId) {
        return this.getLongitude(nodeId);
    }

    @Override
    public final void setAdditionalNodeField(int index, int additionalValue) {
        if (!this.that.extStorage.isRequireNodeField() || this.that.N_ADDITIONAL < 0) {
            throw new AssertionError((Object)"This graph does not provide an additional node field");
        }
        this.that.ensureNodeIndex(index);
        long tmp = (long)index * (long)this.that.nodeEntryBytes;
        this.that.nodes.setInt(tmp + (long)this.that.N_ADDITIONAL, additionalValue);
    }

    @Override
    public final int getAdditionalNodeField(int index) {
        if (this.that.extStorage.isRequireNodeField() && this.that.N_ADDITIONAL >= 0) {
            return this.that.nodes.getInt((long)index * (long)this.that.nodeEntryBytes + (long)this.that.N_ADDITIONAL);
        }
        throw new AssertionError((Object)"This graph does not provide an additional node field");
    }

    @Override
    public final boolean is3D() {
        return this.elevation;
    }

    @Override
    public int getDimension() {
        if (this.elevation) {
            return 3;
        }
        return 2;
    }
}

