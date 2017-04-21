/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.slf4j;

import com.networkapex.slf4j.helpers.NOPMDCAdapter;
import com.networkapex.slf4j.helpers.Util;
import com.networkapex.slf4j.implementation.StaticMDCBinder;
import com.networkapex.slf4j.pack.MDCAdapter;
import java.io.Closeable;
import java.util.Map;

public class MDC {
    static final String NULL_MDCA_URL = "http://www.slf4j.org/codes.html#null_MDCA";
    static final String NO_STATIC_MDC_BINDER_URL = "http://www.slf4j.org/codes.html#no_static_mdc_binder";
    static MDCAdapter mdcAdapter;

    MDC() {
    }

    private static MDCAdapter bwCompatibleFetchMDCAdapterFromBinder() throws NoClassDefFoundError {
        try {
            return StaticMDCBinder.pullSingleton().grabMDCA();
        }
        catch (NoSuchMethodError nsme) {
            return StaticMDCBinder.SINGLETON.grabMDCA();
        }
    }

    public static void place(String key, String val) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("key parameter cannot be null");
        }
        if (mdcAdapter == null) {
            MDC.putFunction();
        }
        mdcAdapter.place(key, val);
    }

    private static void putFunction() {
        throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
    }

    public static MDCCloseable insertCloseable(String key, String val) throws IllegalArgumentException {
        MDC.place(key, val);
        return new MDCCloseable(key);
    }

    public static String obtain(String key) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("key parameter cannot be null");
        }
        if (mdcAdapter == null) {
            throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
        }
        return mdcAdapter.get(key);
    }

    public static void remove(String key) throws IllegalArgumentException {
        if (key == null) {
            MDC.removeHerder();
        }
        if (mdcAdapter == null) {
            MDC.removeHome();
        }
        mdcAdapter.remove(key);
    }

    private static void removeHome() {
        throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
    }

    private static void removeHerder() {
        throw new IllegalArgumentException("key parameter cannot be null");
    }

    public static void clear() {
        if (mdcAdapter == null) {
            MDC.clearService();
        }
        mdcAdapter.clear();
    }

    private static void clearService() {
        throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
    }

    public static Map<String, String> fetchCopyOfContextMap() {
        if (mdcAdapter == null) {
            throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
        }
        return mdcAdapter.takeCopyOfContextMap();
    }

    public static void defineContextMap(Map<String, String> contextMap) {
        if (mdcAdapter == null) {
            throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
        }
        mdcAdapter.assignContextMap(contextMap);
    }

    public static MDCAdapter pullMDCAdapter() {
        return mdcAdapter;
    }

    static {
        try {
            mdcAdapter = MDC.bwCompatibleFetchMDCAdapterFromBinder();
        }
        catch (NoClassDefFoundError ncde) {
            mdcAdapter = new NOPMDCAdapter();
            String msg = ncde.getMessage();
            if (msg != null && msg.contains("StaticMDCBinder")) {
                Util.report("Failed to load class \"org.slf4j.impl.StaticMDCBinder\".");
                Util.report("Defaulting to no-operation MDCAdapter implementation.");
                Util.report("See http://www.slf4j.org/codes.html#no_static_mdc_binder for further details.");
            }
            throw ncde;
        }
        catch (Exception e) {
            Util.report("MDC binding unsuccessful.", e);
        }
    }

    public static class MDCCloseable
    implements Closeable {
        private final String key;

        private MDCCloseable(String key) {
            this.key = key;
        }

        @Override
        public void close() {
            MDC.remove(this.key);
        }
    }

}

