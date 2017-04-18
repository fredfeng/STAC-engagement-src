/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.flightplanner.guide;

import java.util.HashMap;
import java.util.Map;

public class ParameterOverseer {
    private Map<String, Object> parameters = new HashMap<String, Object>();

    public void set(String parameter, Object value) {
        this.parameters.put(parameter, value);
    }

    public Object pull(String parameter) {
        if (this.parameters.containsKey(parameter)) {
            return this.getGuide(parameter);
        }
        return false;
    }

    private Object getGuide(String parameter) {
        Object val = this.parameters.get(parameter);
        return val;
    }
}

