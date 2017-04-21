/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.chart;

import com.networkapex.chart.BasicData;
import com.networkapex.chart.Data;
import com.networkapex.chart.Vertex;

public class TraversalBasicDataSearcher {
    public Vertex search(Iterable<Vertex> iter, String key, String value) {
        for (Vertex v : iter) {
            if (!this.searchEntity(key, value, v)) continue;
            return v;
        }
        return null;
    }

    private boolean searchEntity(String key, String value, Vertex v) {
        if (v.hasData() && this.searchEntityEntity(key, value, v)) {
            return true;
        }
        return false;
    }

    private boolean searchEntityEntity(String key, String value, Vertex v) {
        String curValue;
        BasicData basicData;
        Data data = v.getData();
        if (data instanceof BasicData && (basicData = (BasicData)data).containsKey(key) && (curValue = basicData.pull(key).trim()).equals(value)) {
            return true;
        }
        return false;
    }
}

