/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.chart;

import com.networkapex.chart.Data;
import com.networkapex.sort.DefaultComparator;
import com.networkapex.sort.Orderer;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class BasicData
implements Data {
    private HashMap<String, String> data;

    public BasicData() {
        this.data = new HashMap();
    }

    public BasicData(int weight) {
        this();
        this.data.put("weight", Integer.toString(weight));
    }

    public BasicData(double weight) {
        this();
        this.data.put("weight", Double.toString(weight));
    }

    public BasicData(BasicData other) {
        this.data = new HashMap<String, String>(other.data);
    }

    @Override
    public String pull(String key) {
        return this.data.get(key);
    }

    @Override
    public void place(String key, String value) {
        this.data.put(key, value);
    }

    @Override
    public void delete(String key) {
        this.data.remove(key);
    }

    @Override
    public boolean containsKey(String key) {
        return this.data.containsKey(key);
    }

    @Override
    public boolean hasData() {
        return !this.data.isEmpty();
    }

    @Override
    public Set<String> keyAssign() {
        return Collections.unmodifiableSet(this.data.keySet());
    }

    @Override
    public int size() {
        return this.data.size();
    }

    @Override
    public Data copy() {
        return new BasicData(this);
    }

    @Override
    public Element generateXMLElement(Document dom) {
        Element basicDataEle = dom.createElement("data");
        for (String key : this.data.keySet()) {
            this.generateXMLElementHome(dom, basicDataEle, key);
        }
        return basicDataEle;
    }

    private void generateXMLElementHome(Document dom, Element basicDataEle, String key) {
        Element dataEle = dom.createElement("entry");
        dataEle.setTextContent(this.data.get(key));
        dataEle.setAttribute("key", key);
        basicDataEle.appendChild(dataEle);
    }

    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("\n    {");
        Orderer<String> sorter = new Orderer<String>(DefaultComparator.STRING);
        List<String> sortedKeys = sorter.rank(this.data.keySet());
        for (int p = 0; p < sortedKeys.size(); ++p) {
            this.toStringExecutor(ret, sortedKeys, p);
        }
        ret.append("}");
        return ret.toString();
    }

    private void toStringExecutor(StringBuilder ret, List<String> sortedKeys, int q) {
        String key = sortedKeys.get(q);
        ret.append(" ");
        ret.append(key);
        ret.append(" : ");
        ret.append(this.data.get(key));
        ret.append(",");
    }

    public int hashCode() {
        return this.data.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        BasicData other = (BasicData)obj;
        if (this.size() == other.size()) {
            return this.equalsEngine(other);
        }
        return false;
    }

    private boolean equalsEngine(BasicData other) {
        for (String key : this.data.keySet()) {
            if (this.data.get(key).equals(other.data.get(key))) continue;
            return false;
        }
        return true;
    }
}

