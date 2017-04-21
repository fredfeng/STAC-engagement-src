/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.slf4j.pack;

import java.util.Map;

public interface MDCAdapter {
    public void place(String var1, String var2);

    public String get(String var1);

    public void remove(String var1);

    public void clear();

    public Map<String, String> takeCopyOfContextMap();

    public void assignContextMap(Map<String, String> var1);
}

