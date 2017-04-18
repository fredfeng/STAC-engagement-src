/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.record.implementation;

import edu.cyberapex.record.helpers.NOPMDCAdapter;
import edu.cyberapex.record.instance.MDCAdapter;

public class StaticMDCBinder {
    public static final StaticMDCBinder SINGLETON = new StaticMDCBinder();

    private StaticMDCBinder() {
    }

    public static final StaticMDCBinder grabSingleton() {
        return SINGLETON;
    }

    public MDCAdapter fetchMDCA() {
        return new NOPMDCAdapter();
    }

    public String pullMDCAdapterClassStr() {
        return NOPMDCAdapter.class.getName();
    }
}

