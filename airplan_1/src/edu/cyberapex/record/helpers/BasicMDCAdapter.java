/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.record.helpers;

import edu.cyberapex.record.instance.MDCAdapter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BasicMDCAdapter
implements MDCAdapter {
    private InheritableThreadLocal<Map<String, String>> inheritableThreadLocal;

    public BasicMDCAdapter() {
        this.inheritableThreadLocal = new InheritableThreadLocal<Map<String, String>>(){

            @Override
            protected Map<String, String> childValue(Map<String, String> parentValue) {
                if (parentValue == null) {
                    return null;
                }
                return new HashMap<String, String>(parentValue);
            }
        };
    }

    @Override
    public void place(String key, String val) {
        Map<String, String> map;
        if (key == null) {
            this.putHandler();
        }
        if ((map = this.inheritableThreadLocal.get()) == null) {
            map = new HashMap<String, String>();
            this.inheritableThreadLocal.set(map);
        }
        map.put(key, val);
    }

    private void putHandler() {
        new BasicMDCAdapterHelp().invoke();
    }

    @Override
    public String fetch(String key) {
        Map<String, String> map = this.inheritableThreadLocal.get();
        if (map != null && key != null) {
            return map.get(key);
        }
        return null;
    }

    @Override
    public void remove(String key) {
        Map<String, String> map = this.inheritableThreadLocal.get();
        if (map != null) {
            map.remove(key);
        }
    }

    @Override
    public void clear() {
        Map<String, String> map = this.inheritableThreadLocal.get();
        if (map != null) {
            this.clearExecutor(map);
        }
    }

    private void clearExecutor(Map<String, String> map) {
        map.clear();
        this.inheritableThreadLocal.remove();
    }

    public Set<String> getKeys() {
        Map<String, String> map = this.inheritableThreadLocal.get();
        if (map != null) {
            return map.keySet();
        }
        return null;
    }

    @Override
    public Map<String, String> obtainCopyOfContextMap() {
        Map<String, String> oldMap = this.inheritableThreadLocal.get();
        if (oldMap != null) {
            return new HashMap<String, String>(oldMap);
        }
        return null;
    }

    @Override
    public void defineContextMap(Map<String, String> contextMap) {
        this.inheritableThreadLocal.set(new HashMap<String, String>(contextMap));
    }

    private class BasicMDCAdapterHelp {
        private BasicMDCAdapterHelp() {
        }

        public void invoke() {
            throw new IllegalArgumentException("key cannot be null");
        }
    }

}

