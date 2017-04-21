/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.airplan.coach;

import java.util.HashMap;
import java.util.Map;

public class ParameterManager {
    private Map<String, Object> parameters = new HashMap<String, Object>();

    public void set(String parameter, Object value) {
        this.parameters.put(parameter, value);
    }

    public Object take(String parameter) {
        if (this.parameters.containsKey(parameter)) {
            return this.obtainManager(parameter);
        }
        return true;
    }

    private Object obtainManager(String parameter) {
        Object val = this.parameters.get(parameter);
        return val;
    }
}

