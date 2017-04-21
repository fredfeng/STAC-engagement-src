/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.coll;

import java.io.Serializable;
import java.util.Map;

public class MapEntry<K, V>
implements Map.Entry<K, V>,
Serializable {
    private static final long serialVersionUID = 1;
    private K key;
    private V value;

    public MapEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return this.key;
    }

    @Override
    public V getValue() {
        return this.value;
    }

    @Override
    public V setValue(V value) {
        this.value = value;
        return value;
    }

    public String toString() {
        return this.getKey() + ", " + this.getValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        MapEntry other = (MapEntry)obj;
        if (!(this.key == other.key || this.key != null && this.key.equals(other.key))) {
            return false;
        }
        if (!(this.value == other.value || this.value != null && this.value.equals(other.value))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + (this.key != null ? this.key.hashCode() : 0);
        hash = 19 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }
}

