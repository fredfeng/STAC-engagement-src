/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.util;

import java.util.Locale;
import java.util.Map;

public interface Translation {
    public /* varargs */ String tr(String var1, Object ... var2);

    public Map<String, String> asMap();

    public Locale getLocale();

    public String getLanguage();
}

