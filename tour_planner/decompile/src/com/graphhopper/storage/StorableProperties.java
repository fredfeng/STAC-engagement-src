/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.storage;

import com.graphhopper.storage.DataAccess;
import com.graphhopper.storage.Directory;
import com.graphhopper.storage.Storable;
import com.graphhopper.util.Helper;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

public class StorableProperties
implements Storable<StorableProperties> {
    private final Map<String, String> map = new LinkedHashMap<String, String>();
    private final DataAccess da;

    public StorableProperties(Directory dir) {
        this.da = dir.find("properties");
        this.da.setSegmentSize(32768);
    }

    @Override
    public boolean loadExisting() {
        if (!this.da.loadExisting()) {
            return false;
        }
        int len = (int)this.da.getCapacity();
        byte[] bytes = new byte[len];
        this.da.getBytes(0, bytes, len);
        try {
            Helper.loadProperties(this.map, new StringReader(new String(bytes, Helper.UTF_CS)));
            return true;
        }
        catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public void flush() {
        try {
            StringWriter sw = new StringWriter();
            Helper.saveProperties(this.map, sw);
            byte[] bytes = sw.toString().getBytes(Helper.UTF_CS);
            this.da.setBytes(0, bytes, bytes.length);
            this.da.flush();
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public StorableProperties put(String key, String val) {
        this.map.put(key, val);
        return this;
    }

    public StorableProperties put(String key, Object val) {
        this.map.put(key, val.toString());
        return this;
    }

    public String get(String key) {
        String ret = this.map.get(key);
        if (ret == null) {
            return "";
        }
        return ret;
    }

    @Override
    public void close() {
        this.da.close();
    }

    @Override
    public boolean isClosed() {
        return this.da.isClosed();
    }

    @Override
    public StorableProperties create(long size) {
        this.da.create(size);
        return this;
    }

    @Override
    public long getCapacity() {
        return this.da.getCapacity();
    }

    public void putCurrentVersions() {
        this.put("nodes.version", 4);
        this.put("edges.version", 12);
        this.put("geometry.version", 3);
        this.put("locationIndex.version", 2);
        this.put("nameIndex.version", 2);
        this.put("shortcuts.version", 1);
    }

    public String versionsToString() {
        return this.get("nodes.version") + "," + this.get("edges.version") + "," + this.get("geometry.version") + "," + this.get("locationIndex.version") + "," + this.get("nameIndex.version");
    }

    public boolean checkVersions(boolean silent) {
        if (!this.check("nodes", 4, silent)) {
            return false;
        }
        if (!this.check("edges", 12, silent)) {
            return false;
        }
        if (!this.check("geometry", 3, silent)) {
            return false;
        }
        if (!this.check("locationIndex", 2, silent)) {
            return false;
        }
        if (!this.check("nameIndex", 2, silent)) {
            return false;
        }
        if (!this.check("shortcuts", 1, silent)) {
            return false;
        }
        return true;
    }

    boolean check(String key, int vers, boolean silent) {
        String str = this.get(key + ".version");
        if (!str.equals("" + vers + "")) {
            if (silent) {
                return false;
            }
            throw new IllegalStateException("Version of " + key + " unsupported: " + str + ", expected:" + vers);
        }
        return true;
    }

    public void copyTo(StorableProperties properties) {
        properties.map.clear();
        properties.map.putAll(this.map);
        this.da.copyTo(properties.da);
    }

    public String toString() {
        return this.da.toString();
    }
}

