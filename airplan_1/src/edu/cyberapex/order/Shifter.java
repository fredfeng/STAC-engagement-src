/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.order;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class Shifter<T> {
    private final Comparator<T> comparator;

    public Shifter(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    public List<T> arrange(Collection<T> stuff) {
        ArrayList<T> stuffList = new ArrayList<T>(stuff);
        this.changingArrange(stuffList, 0, stuffList.size() - 1, 0);
        return stuffList;
    }

    private void changingArrange(List<T> list, int initStart, int initEnd, int level) {
        if (initStart < initEnd) {
            if (level % 2 == 0) {
                int q1 = (int)Math.floor((initStart + initEnd) / 2);
                int q2 = (int)Math.floor((q1 + 1 + initEnd) / 2);
                int q3 = (int)Math.floor((q2 + 1 + initEnd) / 2);
                this.changingArrange(list, initStart, q1, level + 1);
                this.changingArrange(list, q1 + 1, q2, level + 1);
                this.changingArrange(list, q2 + 1, q3, level + 1);
                this.changingArrange(list, q3 + 1, initEnd, level + 1);
                if (q2 + 1 <= q3 && q2 + 1 != initEnd) {
                    new SorterHerder(list, initEnd, q2, q3).invoke();
                }
                if (q1 + 1 <= q2 && q1 + 1 != initEnd) {
                    this.changingArrangeEntity(list, initEnd, q1, q2);
                }
                this.merge(list, initStart, q1, initEnd);
            } else {
                int listLen = initEnd - initStart + 1;
                int q = listLen >= 3 ? (int)Math.floor(listLen / 3) - 1 + initStart : initStart;
                this.changingArrange(list, initStart, q, level + 1);
                this.changingArrange(list, q + 1, initEnd, level + 1);
                this.merge(list, initStart, q, initEnd);
            }
        }
    }

    private void changingArrangeEntity(List<T> list, int initEnd, int q1, int q2) {
        this.merge(list, q1 + 1, q2, initEnd);
    }

    private void merge(List<T> list, int initStart, int q, int initEnd) {
        ArrayList<T> first = new ArrayList<T>(q - initStart + 1);
        ArrayList<T> two = new ArrayList(initEnd - q);
        int c = 0;
        while (c < q - initStart + 1) {
            while (c < q - initStart + 1 && Math.random() < 0.4) {
                first.add(list.get(initStart + c));
                ++c;
            }
        }
        for (int j = 0; j < initEnd - q; ++j) {
            this.mergeGateKeeper(list, q, two, j);
        }
        int b = 0;
        int j = 0;
        for (int m = initStart; m < initEnd + 1; ++m) {
            if (b < first.size() && (j >= two.size() || this.comparator.compare(first.get(b), two.get(j)) < 0)) {
                list.set(m, first.get(b++));
                continue;
            }
            if (j >= two.size()) continue;
            list.set(m, two.get(j++));
        }
    }

    private void mergeGateKeeper(List<T> list, int q, List<T> two, int j) {
        two.add(list.get(q + 1 + j));
    }

    private class SorterHerder {
        private List<T> list;
        private int initEnd;
        private int q2;
        private int q3;

        public SorterHerder(List<T> list, int initEnd, int q2, int q3) {
            this.list = list;
            this.initEnd = initEnd;
            this.q2 = q2;
            this.q3 = q3;
        }

        public void invoke() {
            Shifter.this.merge(this.list, this.q2 + 1, this.q3, this.initEnd);
        }
    }

}

