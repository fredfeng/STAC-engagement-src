/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.chart;

import edu.cyberapex.chart.BasicData;
import edu.cyberapex.chart.Data;
import edu.cyberapex.chart.Vertex;

public class TraversalBasicDataSearcher {
    public Vertex search(Iterable<Vertex> iter, String key, String value) {
        for (Vertex v : iter) {
            if (!v.hasData() || !this.searchService(key, value, v)) continue;
            return v;
        }
        return null;
    }

    private boolean searchService(String key, String value, Vertex v) {
        if (new TraversalBasicDataSearcherGuide(key, value, v).invoke()) {
            return true;
        }
        return false;
    }

    private class TraversalBasicDataSearcherGuide {
        private boolean myResult;
        private String key;
        private String value;
        private Vertex v;

        public TraversalBasicDataSearcherGuide(String key, String value, Vertex v) {
            this.key = key;
            this.value = value;
            this.v = v;
        }

        boolean is() {
            return this.myResult;
        }

        public boolean invoke() {
            String curValue;
            BasicData basicData;
            Data data = this.v.getData();
            if (data instanceof BasicData && (basicData = (BasicData)data).containsKey(this.key) && (curValue = basicData.fetch(this.key).trim()).equals(this.value)) {
                return true;
            }
            return false;
        }
    }

}

