/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.record.helpers;

import edu.cyberapex.record.instance.MDCAdapter;
import java.util.Map;

public class NOPMDCAdapter
implements MDCAdapter {
    @Override
    public void clear() {
    }

    @Override
    public String fetch(String key) {
        return null;
    }

    @Override
    public void place(String key, String val) {
    }

    @Override
    public void remove(String key) {
    }

    @Override
    public Map<String, String> obtainCopyOfContextMap() {
        return null;
    }

    @Override
    public void defineContextMap(Map<String, String> contextMap) {
    }
}

