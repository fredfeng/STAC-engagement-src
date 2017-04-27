/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.storage;

import com.graphhopper.storage.DAType;
import com.graphhopper.storage.DataAccess;
import com.graphhopper.storage.Directory;
import com.graphhopper.storage.MMapDataAccess;
import com.graphhopper.storage.RAMDataAccess;
import com.graphhopper.storage.RAMIntDataAccess;
import com.graphhopper.storage.SynchedDAWrapper;
import com.graphhopper.util.Helper;
import java.io.File;
import java.nio.ByteOrder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GHDirectory
implements Directory {
    protected Map<String, DataAccess> map = new HashMap<String, DataAccess>();
    protected Map<String, DAType> types = new HashMap<String, DAType>();
    protected final String location;
    private final DAType defaultType;
    private final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;

    public GHDirectory(String _location, DAType defaultType) {
        this.defaultType = defaultType;
        if (Helper.isEmpty(_location)) {
            _location = new File("").getAbsolutePath();
        }
        if (!_location.endsWith("/")) {
            _location = _location + "/";
        }
        this.location = _location;
        File dir = new File(this.location);
        if (dir.exists() && !dir.isDirectory()) {
            throw new RuntimeException("file '" + dir + "' exists but is not a directory");
        }
        if (this.defaultType.isInMemory()) {
            if (this.isStoring()) {
                this.put("locationIndex", DAType.RAM_INT_STORE);
                this.put("edges", DAType.RAM_INT_STORE);
                this.put("nodes", DAType.RAM_INT_STORE);
            } else {
                this.put("locationIndex", DAType.RAM_INT);
                this.put("edges", DAType.RAM_INT);
                this.put("nodes", DAType.RAM_INT);
            }
        }
        this.mkdirs();
    }

    @Override
    public ByteOrder getByteOrder() {
        return this.byteOrder;
    }

    public Directory put(String name, DAType type) {
        this.types.put(name, type);
        return this;
    }

    @Override
    public DataAccess find(String name) {
        DAType type = this.types.get(name);
        if (type == null) {
            type = this.defaultType;
        }
        return this.find(name, type);
    }

    @Override
    public DataAccess find(String name, DAType type) {
        DataAccess da = this.map.get(name);
        if (da != null) {
            if (!type.equals(da.getType())) {
                throw new IllegalStateException("Found existing DataAccess object '" + name + "' but types did not match. Requested:" + type + ", was:" + da.getType());
            }
            return da;
        }
        if (type.isInMemory()) {
            da = type.isInteg() ? (type.isStoring() ? new RAMIntDataAccess(name, this.location, true, this.byteOrder) : new RAMIntDataAccess(name, this.location, false, this.byteOrder)) : (type.isStoring() ? new RAMDataAccess(name, this.location, true, this.byteOrder) : new RAMDataAccess(name, this.location, false, this.byteOrder));
        } else if (type.isMMap()) {
            da = new MMapDataAccess(name, this.location, this.byteOrder, type.isAllowWrites());
        } else {
            throw new IllegalArgumentException("Data access type UNSAFE_STORE not supported");
        }
        if (type.isSynched()) {
            da = new SynchedDAWrapper(da);
        }
        this.map.put(name, da);
        return da;
    }

    @Override
    public void clear() {
        MMapDataAccess mmapDA = null;
        for (DataAccess da : this.map.values()) {
            if (da instanceof MMapDataAccess) {
                mmapDA = (MMapDataAccess)da;
            }
            this.removeDA(da, da.getName(), false);
        }
        if (mmapDA != null) {
            Helper.cleanHack();
        }
        this.map.clear();
    }

    @Override
    public void remove(DataAccess da) {
        this.removeFromMap(da.getName());
        this.removeDA(da, da.getName(), true);
    }

    void removeDA(DataAccess da, String name, boolean forceClean) {
        if (da instanceof MMapDataAccess) {
            ((MMapDataAccess)da).close(forceClean);
        } else {
            da.close();
        }
        if (da.getType().isStoring()) {
            Helper.removeDir(new File(this.location + name));
        }
    }

    void removeFromMap(String name) {
        DataAccess da = this.map.remove(name);
        if (da == null) {
            throw new IllegalStateException("Couldn't remove dataAccess object:" + name);
        }
    }

    @Override
    public DAType getDefaultType() {
        return this.defaultType;
    }

    public boolean isStoring() {
        return this.defaultType.isStoring();
    }

    protected void mkdirs() {
        if (this.isStoring()) {
            new File(this.location).mkdirs();
        }
    }

    @Override
    public Collection<DataAccess> getAll() {
        return this.map.values();
    }

    public String toString() {
        return this.getLocation();
    }

    @Override
    public String getLocation() {
        return this.location;
    }
}

