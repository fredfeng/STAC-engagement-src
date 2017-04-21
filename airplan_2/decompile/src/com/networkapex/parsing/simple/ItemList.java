/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.parsing.simple;

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
            this.splitSupervisor(s, sp, append);
        } else {
            this.splitUtility(s, sp, append);
        }
    }

    private void splitUtility(String s, String sp, List append) {
        this.split(s, sp, append);
    }

    private void splitSupervisor(String s, String sp, List append) {
        StringTokenizer tokens = new StringTokenizer(s, sp);
        while (tokens.hasMoreTokens()) {
            this.splitSupervisorAssist(append, tokens);
        }
    }

    private void splitSupervisorAssist(List append, StringTokenizer tokens) {
        append.add(tokens.nextToken().trim());
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

    public void assignSP(String sp) {
        this.sp = sp;
    }

    public void add(int k, String item) {
        if (item == null) {
            return;
        }
        this.items.add(k, item.trim());
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

    public String get(int q) {
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
        for (int b = 0; b < this.items.size(); ++b) {
            if (b == 0) {
                sb.append(this.items.get(b));
                continue;
            }
            this.toStringAdviser(sp, sb, b);
        }
        return sb.toString();
    }

    private void toStringAdviser(String sp, StringBuffer sb, int j) {
        sb.append(sp);
        sb.append(this.items.get(j));
    }

    public void clear() {
        this.items.clear();
    }

    public void reset() {
        this.sp = ",";
        this.items.clear();
    }
}

