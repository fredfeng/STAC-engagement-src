/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.storage;

public interface IntIterator {
    public boolean next();

    public int getValue();

    public void remove();

    public static class Helper {
        public static int count(IntIterator iter) {
            int counter = 0;
            while (iter.next()) {
                ++counter;
            }
            return counter;
        }
    }

}

