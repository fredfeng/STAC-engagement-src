/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.logging.realization;

import edu.computerapex.logging.helpers.NOPMDCAdapter;
import edu.computerapex.logging.provider.MDCAdapter;

public class StaticMDCBinder {
    public static final StaticMDCBinder SINGLETON = new StaticMDCBinder();

    private StaticMDCBinder() {
    }

    public static final StaticMDCBinder fetchSingleton() {
        return SINGLETON;
    }

    public MDCAdapter obtainMDCA() {
        return new NOPMDCAdapter();
    }

    public String takeMDCAdapterClassStr() {
        return NOPMDCAdapter.class.getName();
    }
}

