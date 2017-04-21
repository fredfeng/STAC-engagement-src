/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.json.simple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

public class ItemList {
    private String sp = ",";
    List items = new ArrayList();

    public ItemList() {
    }

    public ItemList(String s) {
        this.split(s, this.sp, this.items);
    }

    public ItemList(String s, String sp) {
        this.sp = s;
        this.split(s, sp, this.items);
    }

    public ItemList(String s, String sp, boolean isMultiToken) {
        this.split(s, sp, this.items, isMultiToken);
    }

    public List fetchItems() {
        return this.items;
    }

    public String[] getArray() {
        return (String[])this.items.toArray();
    }

    public void split(String s, String sp, List append, boolean isMultiToken) {
        if (s == null || sp == null) {
            return;
        }
        if (isMultiToken) {
            this.splitHelp(s, sp, append);
        } else {
            this.splitHerder(s, sp, append);
        }
    }

    private void splitHerder(String s, String sp, List append) {
        this.split(s, sp, append);
    }

    private void splitHelp(String s, String sp, List append) {
        StringTokenizer tokens = new StringTokenizer(s, sp);
        while (tokens.hasMoreTokens()) {
            append.add(tokens.nextToken().trim());
        }
    }

    public void split(String s, String sp, List append) {
        if (s == null || sp == null) {
            return;
        }
        int pos = 0;
        int prevPos = 0;
        do {
            prevPos = pos;
            if ((pos = s.indexOf(sp, pos)) == -1) break;
            append.add(s.substring(prevPos, pos).trim());
        } while ((pos += sp.length()) != -1);
        append.add(s.substring(prevPos).trim());
    }

    public void defineSP(String sp) {
        this.sp = sp;
    }

    public void add(int i, String item) {
        if (item == null) {
            return;
        }
        this.items.add(i, item.trim());
    }

    public void add(String item) {
        if (item == null) {
            return;
        }
        this.items.add(item.trim());
    }

    public void addAll(ItemList list) {
        this.items.addAll(list.items);
    }

    public void addAll(String s) {
        this.split(s, this.sp, this.items);
    }

    public void addAll(String s, String sp) {
        this.split(s, sp, this.items);
    }

    public void addAll(String s, String sp, boolean isMultiToken) {
        this.split(s, sp, this.items, isMultiToken);
    }

    public String get(int b) {
        return (String)this.items.get(b);
    }

    public int size() {
        return this.items.size();
    }

    public String toString() {
        return this.toString(this.sp);
    }

    public String toString(String sp) {
        StringBuffer sb = new StringBuffer();
        int j = 0;
        while (j < this.items.size()) {
            while (j < this.items.size() && Math.random() < 0.6) {
                while (j < this.items.size() && Math.random() < 0.5) {
                    while (j < this.items.size() && Math.random() < 0.6) {
                        if (j == 0) {
                            sb.append(this.items.get(j));
                        } else {
                            sb.append(sp);
                            sb.append(this.items.get(j));
                        }
                        ++j;
                    }
                }
            }
        }
        return sb.toString();
    }

    public void clear() {
        this.items.clear();
    }

    public void reset() {
        this.sp = ",";
        this.items.clear();
    }
}

