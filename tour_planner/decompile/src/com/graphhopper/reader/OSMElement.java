/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.reader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public abstract class OSMElement {
    public static final int NODE = 0;
    public static final int WAY = 1;
    public static final int RELATION = 2;
    private final int type;
    private final long id;
    private final Map<String, Object> properties = new HashMap<String, Object>(5);

    protected OSMElement(long id, int type) {
        this.id = id;
        this.type = type;
    }

    public long getId() {
        return this.id;
    }

    protected void readTags(XMLStreamReader parser) throws XMLStreamException {
        int event = parser.getEventType();
        while (event != 8 && parser.getLocalName().equals("tag")) {
            if (event == 1) {
                String key = parser.getAttributeValue(null, "k");
                String value = parser.getAttributeValue(null, "v");
                if (value != null && value.length() > 0) {
                    this.setTag(key, value);
                }
            }
            event = parser.nextTag();
        }
    }

    protected String tagsToString() {
        if (this.properties.isEmpty()) {
            return "<empty>";
        }
        StringBuilder tagTxt = new StringBuilder();
        for (Map.Entry<String, Object> entry : this.properties.entrySet()) {
            tagTxt.append(entry.getKey());
            tagTxt.append("=");
            tagTxt.append(entry.getValue());
            tagTxt.append("\n");
        }
        return tagTxt.toString();
    }

    protected Map<String, Object> getTags() {
        return this.properties;
    }

    public void setTags(Map<String, String> newTags) {
        this.properties.clear();
        if (newTags != null) {
            for (Map.Entry<String, String> e : newTags.entrySet()) {
                this.setTag(e.getKey(), e.getValue());
            }
        }
    }

    public boolean hasTags() {
        return !this.properties.isEmpty();
    }

    public String getTag(String name) {
        return (String)this.properties.get(name);
    }

    public <T> T getTag(String key, T defaultValue) {
        Object val = this.properties.get(key);
        if (val == null) {
            return defaultValue;
        }
        return (T)val;
    }

    public void setTag(String name, Object value) {
        this.properties.put(name, value);
    }

    public boolean hasTag(String key, Object value) {
        return value.equals(this.getTag(key, ""));
    }

    public /* varargs */ boolean hasTag(String key, String ... values) {
        Object osmValue = this.properties.get(key);
        if (osmValue == null) {
            return false;
        }
        if (values.length == 0) {
            return true;
        }
        for (String val : values) {
            if (!val.equals(osmValue)) continue;
            return true;
        }
        return false;
    }

    public final boolean hasTag(String key, Set<String> values) {
        return values.contains(this.getTag(key, ""));
    }

    public boolean hasTag(List<String> keyList, Set<String> values) {
        for (String key : keyList) {
            if (!values.contains(this.getTag(key, ""))) continue;
            return true;
        }
        return false;
    }

    public String getFirstPriorityTag(List<String> restrictions) {
        for (String str : restrictions) {
            if (!this.hasTag(str, new String[0])) continue;
            return this.getTag(str);
        }
        return "";
    }

    public void removeTag(String name) {
        this.properties.remove(name);
    }

    public void clearTags() {
        this.properties.clear();
    }

    public int getType() {
        return this.type;
    }

    public boolean isType(int type) {
        return this.type == type;
    }

    public String toString() {
        return this.properties.toString();
    }
}

