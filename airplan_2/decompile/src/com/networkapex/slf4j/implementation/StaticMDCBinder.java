/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.slf4j.implementation;

import com.networkapex.slf4j.helpers.NOPMDCAdapter;
import com.networkapex.slf4j.pack.MDCAdapter;

public class StaticMDCBinder {
    public static final StaticMDCBinder SINGLETON = new StaticMDCBinder();

    private StaticMDCBinder() {
    }

    public static final StaticMDCBinder pullSingleton() {
        return SINGLETON;
    }

    public MDCAdapter grabMDCA() {
        return new NOPMDCAdapter();
    }

    public String fetchMDCAdapterClassStr() {
        return NOPMDCAdapter.class.getName();
    }
}

