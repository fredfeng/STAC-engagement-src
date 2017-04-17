/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.validate;

import java.util.HashSet;
import java.util.List;

public class DuplicateEliminator<T> {
    private final List<T> items;

    public DuplicateEliminator(List<T> items) {
        this.items = items;
    }

    public int process() {
        if (this.items == null || this.items.isEmpty()) {
            return 0;
        }
        int result = 0;
        HashSet<T> unique = new HashSet<T>();
        int i = 0;
        while (i < this.items.size()) {
            T item = this.items.get(i);
            if (unique.contains(item)) {
                ++result;
                this.items.remove(i);
                continue;
            }
            unique.add(item);
            ++i;
        }
        return result;
    }
}

