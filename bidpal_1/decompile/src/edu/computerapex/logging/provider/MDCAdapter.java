/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.logging.provider;

import java.util.Map;

public interface MDCAdapter {
    public void place(String var1, String var2);

    public String take(String var1);

    public void remove(String var1);

    public void clear();

    public Map<String, String> takeCopyOfContextMap();

    public void setContextMap(Map<String, String> var1);
}

