/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.reader;

import com.graphhopper.storage.DataAccess;
import com.graphhopper.storage.Directory;
import com.graphhopper.util.Helper;
import com.graphhopper.util.PointAccess;

public class PillarInfo
implements PointAccess {
    private final int LAT = 0;
    private final int LON = 4;
    private final int ELE = 8;
    private final boolean enabled3D;
    private final DataAccess da;
    private final int rowSizeInBytes;
    private final Directory dir;

    public PillarInfo(boolean enabled3D, Directory dir) {
        this.enabled3D = enabled3D;
        this.dir = dir;
        this.da = dir.find("tmpPillarInfo").create(100);
        this.rowSizeInBytes = this.getDimension() * 4;
    }

    @Override
    public boolean is3D() {
        return this.enabled3D;
    }

    @Override
    public int getDimension() {
        return this.enabled3D ? 3 : 2;
    }

    @Override
    public void ensureNode(int nodeId) {
        long tmp = (long)nodeId * (long)this.rowSizeInBytes;
        this.da.ensureCapacity(tmp + (long)this.rowSizeInBytes);
    }

    @Override
    public void setNode(int nodeId, double lat, double lon) {
        this._setNode(nodeId, lat, lon, Double.NaN);
    }

    @Override
    public void setNode(int nodeId, double lat, double lon, double ele) {
        this._setNode(nodeId, lat, lon, ele);
    }

    private void _setNode(int nodeId, double lat, double lon, double ele) {
        this.ensureNode(nodeId);
        long tmp = (long)nodeId * (long)this.rowSizeInBytes;
        this.da.setInt(tmp + 0, Helper.degreeToInt(lat));
        this.da.setInt(tmp + 4, Helper.degreeToInt(lon));
        if (this.is3D()) {
            this.da.setInt(tmp + 8, Helper.eleToInt(ele));
        }
    }

    @Override
    public double getLatitude(int id) {
        int intVal = this.da.getInt((long)id * (long)this.rowSizeInBytes + 0);
        return Helper.intToDegree(intVal);
    }

    @Override
    public double getLat(int id) {
        return this.getLatitude(id);
    }

    @Override
    public double getLongitude(int id) {
        int intVal = this.da.getInt((long)id * (long)this.rowSizeInBytes + 4);
        return Helper.intToDegree(intVal);
    }

    @Override
    public double getLon(int id) {
        return this.getLongitude(id);
    }

    @Override
    public double getElevation(int id) {
        if (!this.is3D()) {
            return Double.NaN;
        }
        int intVal = this.da.getInt((long)id * (long)this.rowSizeInBytes + 8);
        return Helper.intToEle(intVal);
    }

    @Override
    public double getEle(int id) {
        return this.getElevation(id);
    }

    public void clear() {
        this.dir.remove(this.da);
    }
}

