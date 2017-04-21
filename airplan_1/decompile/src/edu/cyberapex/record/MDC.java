/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.record;

import edu.cyberapex.record.helpers.NOPMDCAdapter;
import edu.cyberapex.record.helpers.Util;
import edu.cyberapex.record.implementation.StaticMDCBinder;
import edu.cyberapex.record.instance.MDCAdapter;
import java.io.Closeable;
import java.util.Map;

public class MDC {
    static final String NULL_MDCA_URL = "http://www.slf4j.org/codes.html#null_MDCA";
    static final String NO_STATIC_MDC_BINDER_URL = "http://www.slf4j.org/codes.html#no_static_mdc_binder";
    static MDCAdapter mdcAdapter;

    private MDC() {
    }

    private static MDCAdapter bwCompatibleObtainMDCAdapterFromBinder() throws NoClassDefFoundError {
        try {
            return StaticMDCBinder.grabSingleton().fetchMDCA();
        }
        catch (NoSuchMethodError nsme) {
            return StaticMDCBinder.SINGLETON.fetchMDCA();
        }
    }

    public static void put(String key, String val) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("key parameter cannot be null");
        }
        if (mdcAdapter == null) {
            MDC.putEngine();
        }
        mdcAdapter.place(key, val);
    }

    private static void putEngine() {
        throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
    }

    public static MDCCloseable placeCloseable(String key, String val) throws IllegalArgumentException {
        MDC.put(key, val);
        return new MDCCloseable(key);
    }

    public static String fetch(String key) throws IllegalArgumentException {
        if (key == null) {
            return MDC.getGuide();
        }
        if (mdcAdapter == null) {
            return MDC.takeCoordinator();
        }
        return mdcAdapter.fetch(key);
    }

    private static String takeCoordinator() {
        throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
    }

    private static String getGuide() {
        throw new IllegalArgumentException("key parameter cannot be null");
    }

    public static void remove(String key) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("key parameter cannot be null");
        }
        if (mdcAdapter == null) {
            MDC.removeAdviser();
        }
        mdcAdapter.remove(key);
    }

    private static void removeAdviser() {
        throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
    }

    public static void clear() {
        if (mdcAdapter == null) {
            MDC.clearHerder();
        }
        mdcAdapter.clear();
    }

    private static void clearHerder() {
        throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
    }

    public static Map<String, String> takeCopyOfContextMap() {
        if (mdcAdapter == null) {
            return MDCAssist.invoke();
        }
        return mdcAdapter.obtainCopyOfContextMap();
    }

    public static void fixContextMap(Map<String, String> contextMap) {
        if (mdcAdapter == null) {
            throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
        }
        mdcAdapter.defineContextMap(contextMap);
    }

    public static MDCAdapter pullMDCAdapter() {
        return mdcAdapter;
    }

    static {
        try {
            mdcAdapter = MDC.bwCompatibleObtainMDCAdapterFromBinder();
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

    private static class MDCAssist {
        private MDCAssist() {
        }

        private static Map<String, String> invoke() {
            throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
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

