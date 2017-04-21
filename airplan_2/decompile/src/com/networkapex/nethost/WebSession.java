/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.nethost;

import java.util.HashMap;
import java.util.Map;

public class WebSession {
    private final String personId;
    private final Map<String, String> propertyMap = new HashMap<String, String>();

    public WebSession(String personId) {
        this.personId = personId;
    }

    public String getPersonId() {
        return this.personId;
    }

    public String getProperty(String name) {
        return this.propertyMap.get(name);
    }

    public String obtainProperty(String name, String defaultReturn) {
        if (this.propertyMap.containsKey(name)) {
            return this.propertyMap.get(name);
        }
        return defaultReturn;
    }

    public void assignProperty(String name, String value) {
        this.propertyMap.put(name, value);
    }
}

