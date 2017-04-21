/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.slf4j.helpers;

import com.networkapex.slf4j.pack.MDCAdapter;
import java.util.Map;

public class NOPMDCAdapter
implements MDCAdapter {
    @Override
    public void clear() {
    }

    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public void place(String key, String val) {
    }

    @Override
    public void remove(String key) {
    }

    @Override
    public Map<String, String> takeCopyOfContextMap() {
        return null;
    }

    @Override
    public void assignContextMap(Map<String, String> contextMap) {
    }
}

