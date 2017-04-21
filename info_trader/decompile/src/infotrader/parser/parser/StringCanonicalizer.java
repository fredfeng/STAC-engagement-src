/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.parser;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class StringCanonicalizer {
    final ConcurrentMap<String, CanonicalizedString> stringPool = new ConcurrentHashMap<String, CanonicalizedString>(100);
    private int poolUsageThreshold = 25;
    private int maxPoolSize = 1000;
    int numEvictions = 0;
    private int numEvictedCumulative = 0;
    private boolean maxedOut = false;

    public StringCanonicalizer() {
        this(500, 25);
    }

    private StringCanonicalizer(int maxPoolSize, int poolUsageThreshold) {
        this.maxPoolSize = maxPoolSize;
        this.poolUsageThreshold = poolUsageThreshold;
    }

    public int getMaxPoolSize() {
        return this.maxPoolSize;
    }

    public int getNumEvictedCumulative() {
        return this.numEvictedCumulative;
    }

    public int getNumEvictions() {
        return this.numEvictions;
    }

    public int getPoolUsageThreshold() {
        return this.poolUsageThreshold;
    }

    public boolean isMaxedOut() {
        return this.maxedOut;
    }

    String getCanonicalVersion(String str) {
        if (str == null) {
            return str;
        }
        if (str.length() == 0) {
            return "".intern();
        }
        CanonicalizedString canon = this.stringPool.get(str);
        if (canon != null) {
            ++canon.count;
            return canon.value;
        }
        if (this.stringPool.size() > this.maxPoolSize) {
            this.evictLowValueEntries();
            if (this.stringPool.size() > this.maxPoolSize) {
                this.maxedOut = true;
                return str;
            }
        }
        canon = new CanonicalizedString(str);
        this.stringPool.put(str, canon);
        return canon.value;
    }

    void reset() {
        this.stringPool.clear();
        this.numEvictedCumulative = 0;
        this.numEvictions = 0;
        this.maxedOut = false;
    }

    private void evictLowValueEntries() {
        if (this.maxedOut) {
            return;
        }
        ++this.numEvictions;
        int before = this.stringPool.size();
        for (CanonicalizedString canon : this.stringPool.values()) {
            if (canon.count >= (long)this.poolUsageThreshold) continue;
            this.stringPool.remove(canon.value);
        }
        int after = this.stringPool.size();
        this.numEvictedCumulative += before - after;
        if (before - after < 2) {
            this.maxedOut = true;
        }
    }

    class CanonicalizedString {
        long count;
        final String value;

        CanonicalizedString(String str) {
            this.value = str;
            this.count = 0;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("CanonicalizedString [count=");
            builder.append(this.count);
            builder.append(", ");
            if (this.value != null) {
                builder.append("value=");
                builder.append(this.value);
            }
            builder.append("]");
            return builder.toString();
        }
    }

}

