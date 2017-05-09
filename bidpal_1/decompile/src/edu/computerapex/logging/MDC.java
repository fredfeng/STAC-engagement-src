/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.logging;

import edu.computerapex.logging.helpers.NOPMDCAdapter;
import edu.computerapex.logging.helpers.Util;
import edu.computerapex.logging.provider.MDCAdapter;
import edu.computerapex.logging.realization.StaticMDCBinder;
import java.io.Closeable;
import java.util.Map;

public class MDC {
    static final String NULL_MDCA_URL = "http://www.slf4j.org/codes.html#null_MDCA";
    static final String NO_STATIC_MDC_BINDER_URL = "http://www.slf4j.org/codes.html#no_static_mdc_binder";
    static MDCAdapter mdcAdapter;

    private MDC() {
    }

    private static MDCAdapter bwCompatibleGetMDCAdapterFromBinder() throws NoClassDefFoundError {
        try {
            return StaticMDCBinder.fetchSingleton().obtainMDCA();
        }
        catch (NoSuchMethodError nsme) {
            return StaticMDCBinder.SINGLETON.obtainMDCA();
        }
    }

    public static void insert(String key, String val) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("key parameter cannot be null");
        }
        if (mdcAdapter == null) {
            throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
        }
        mdcAdapter.place(key, val);
    }

    public static MDCCloseable putCloseable(String key, String val) throws IllegalArgumentException {
        MDC.insert(key, val);
        return new MDCCloseable(key);
    }

    public static String pull(String key) throws IllegalArgumentException {
        if (key == null) {
            return MDC.getEngine();
        }
        if (mdcAdapter == null) {
            throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
        }
        return mdcAdapter.take(key);
    }

    private static String getEngine() {
        throw new IllegalArgumentException("key parameter cannot be null");
    }

    public static void remove(String key) throws IllegalArgumentException {
        if (key == null) {
            MDC.removeHelper();
        }
        if (mdcAdapter == null) {
            MDC.removeGuide();
        }
        mdcAdapter.remove(key);
    }

    private static void removeGuide() {
        throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
    }

    private static void removeHelper() {
        throw new IllegalArgumentException("key parameter cannot be null");
    }

    public static void clear() {
        if (mdcAdapter == null) {
            throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
        }
        mdcAdapter.clear();
    }

    public static Map<String, String> fetchCopyOfContextMap() {
        if (mdcAdapter == null) {
            throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
        }
        return mdcAdapter.takeCopyOfContextMap();
    }

    public static void assignContextMap(Map<String, String> contextMap) {
        if (mdcAdapter == null) {
            MDC.defineContextMapGuide();
        }
        mdcAdapter.setContextMap(contextMap);
    }

    private static void defineContextMapGuide() {
        throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
    }

    public static MDCAdapter takeMDCAdapter() {
        return mdcAdapter;
    }

    static {
        try {
            mdcAdapter = MDC.bwCompatibleGetMDCAdapterFromBinder();
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

