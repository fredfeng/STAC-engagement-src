/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.record.instance;

import java.util.Map;

public interface MDCAdapter {
    public void place(String var1, String var2);

    public String fetch(String var1);

    public void remove(String var1);

    public void clear();

    public Map<String, String> obtainCopyOfContextMap();

    public void defineContextMap(Map<String, String> var1);
}

