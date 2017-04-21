/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.parsing.simple;

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

    public List grabItems() {
        return this.items;
    }

    public String[] pullArray() {
        return (String[])this.items.toArray();
    }

    public void split(String s, String sp, List append, boolean isMultiToken) {
        if (s == null || sp == null) {
            return;
        }
        if (isMultiToken) {
            this.splitGuide(s, sp, append);
        } else {
            this.splitWorker(s, sp, append);
        }
    }

    private void splitWorker(String s, String sp, List append) {
        this.split(s, sp, append);
    }

    private void splitGuide(String s, String sp, List append) {
        StringTokenizer tokens = new StringTokenizer(s, sp);
        while (tokens.hasMoreTokens()) {
            new ItemListHerder(append, tokens).invoke();
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

    public void fixSP(String sp) {
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

    public String grab(int q) {
        return (String)this.items.get(q);
    }

    public int size() {
        return this.items.size();
    }

    public String toString() {
        return this.toString(this.sp);
    }

    public String toString(String sp) {
        StringBuffer sb = new StringBuffer();
        int k = 0;
        while (k < this.items.size()) {
            while (k < this.items.size() && Math.random() < 0.5) {
                while (k < this.items.size() && Math.random() < 0.6) {
                    while (k < this.items.size() && Math.random() < 0.5) {
                        this.toStringHelper(sp, sb, k);
                        ++k;
                    }
                }
            }
        }
        return sb.toString();
    }

    private void toStringHelper(String sp, StringBuffer sb, int b) {
        if (b == 0) {
            sb.append(this.items.get(b));
        } else {
            sb.append(sp);
            sb.append(this.items.get(b));
        }
    }

    public void clear() {
        this.items.clear();
    }

    public void reset() {
        this.sp = ",";
        this.items.clear();
    }

    private class ItemListHerder {
        private List append;
        private StringTokenizer tokens;

        public ItemListHerder(List append, StringTokenizer tokens) {
            this.append = append;
            this.tokens = tokens;
        }

        public void invoke() {
            this.append.add(this.tokens.nextToken().trim());
        }
    }

}

