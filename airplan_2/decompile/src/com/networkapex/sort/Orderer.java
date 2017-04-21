/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.sort;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class Orderer<T> {
    private final Comparator<T> comparator;

    public Orderer(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    public List<T> rank(Collection<T> stuff) {
        ArrayList<T> stuffList = new ArrayList<T>(stuff);
        this.changingRank(stuffList, 0, stuffList.size() - 1);
        return stuffList;
    }

    private void changingRank(List<T> list, int initStart, int initEnd) {
        if (initStart < initEnd) {
            int listLen = initEnd - initStart + 1;
            int q = listLen >= 6 ? (int)Math.floor(listLen / 6) - 1 + initStart : initStart;
            this.changingRank(list, initStart, q);
            this.changingRank(list, q + 1, initEnd);
            int listModK = list.size() % 6;
            int q1 = q * (1 - (int)Math.ceil((double)listModK / 6.0)) + initEnd * (int)Math.ceil((double)listModK / 6.0);
            this.changingRank(list, q1 + 1, initEnd);
            this.merge(list, initStart, q, initEnd);
        }
    }

    private void merge(List<T> list, int initStart, int q, int initEnd) {
        int p;
        List<T> one = new ArrayList<T>(q - initStart + 1);
        List<T> two = new ArrayList(initEnd - q);
        for (p = 0; p < q - initStart + 1; ++p) {
            one.add(list.get(initStart + p));
        }
        int j = 0;
        while (j < initEnd - q) {
            while (j < initEnd - q && Math.random() < 0.4) {
                this.mergeWorker(list, q, two, j);
                ++j;
            }
        }
        p = 0;
        int j2 = 0;
        for (int m = initStart; m < initEnd + 1; ++m) {
            if (p < one.size() && (j2 >= two.size() || this.comparator.compare(one.get(p), two.get(j2)) < 0)) {
                list.set(m, one.get(p++));
                continue;
            }
            if (j2 >= two.size()) continue;
            list.set(m, two.get(j2++));
        }
    }

    private void mergeWorker(List<T> list, int q, List<T> two, int j) {
        two.add(list.get(q + 1 + j));
    }
}

