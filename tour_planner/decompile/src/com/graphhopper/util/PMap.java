/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.util;

import com.graphhopper.util.Helper;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PMap {
    private final Map<String, String> map;

    public PMap() {
        this(5);
    }

    public PMap(int capacity) {
        this(new HashMap<String, String>(capacity));
    }

    public PMap(Map<String, String> map) {
        this.map = map;
    }

    public PMap(String propertiesString) {
        this.map = new HashMap<String, String>(5);
        for (String s : propertiesString.split("\\|")) {
            int index = (s = s.trim()).indexOf("=");
            if (index < 0) continue;
            this.map.put(s.substring(0, index).toLowerCase(), s.substring(index + 1));
        }
    }

    public PMap put(PMap map) {
        this.map.putAll(map.map);
        return this;
    }

    public PMap put(String key, Object str) {
        if (str == null) {
            throw new NullPointerException("Value cannot be null. Use remove instead.");
        }
        this.map.put(key.toLowerCase(), str.toString());
        return this;
    }

    public PMap remove(String key) {
        this.map.remove(key);
        return this;
    }

    public long getLong(String key, long _default) {
        String str = this.get(key);
        if (!Helper.isEmpty(str)) {
            try {
                return Long.parseLong(str);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return _default;
    }

    public int getInt(String key, int _default) {
        String str = this.get(key);
        if (!Helper.isEmpty(str)) {
            try {
                return Integer.parseInt(str);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return _default;
    }

    public boolean getBool(String key, boolean _default) {
        String str = this.get(key);
        if (!Helper.isEmpty(str)) {
            try {
                return Boolean.parseBoolean(str);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return _default;
    }

    public double getDouble(String key, double _default) {
        String str = this.get(key);
        if (!Helper.isEmpty(str)) {
            try {
                return Double.parseDouble(str);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return _default;
    }

    public String get(String key, String _default) {
        String str = this.get(key);
        if (Helper.isEmpty(str)) {
            return _default;
        }
        return str;
    }

    String get(String key) {
        if (Helper.isEmpty(key)) {
            return "";
        }
        String val = this.map.get(key.toLowerCase());
        if (val == null) {
            return "";
        }
        return val;
    }

    public Map<String, String> toMap() {
        return new HashMap<String, String>(this.map);
    }

    private Map<String, String> getMap() {
        return this.map;
    }

    public PMap merge(PMap read) {
        return this.merge(read.getMap());
    }

    PMap merge(Map<String, String> map) {
        for (Map.Entry<String, String> e : map.entrySet()) {
            if (Helper.isEmpty(e.getKey())) continue;
            this.getMap().put(e.getKey().toLowerCase(), e.getValue());
        }
        return this;
    }

    public boolean has(String key) {
        return this.getMap().containsKey(key);
    }

    public String toString() {
        return this.getMap().toString();
    }
}

