/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing.ch;

public class PrepareEncoder {
    private static final long scFwdDir = 1;
    private static final long scBwdDir = 2;
    private static final long scDirMask = 3;

    public static final long getScDirMask() {
        return 3;
    }

    public static final long getScFwdDir() {
        return 1;
    }

    public static final long getScBwdDir() {
        return 2;
    }

    public static final boolean canBeOverwritten(long flags1, long flags2) {
        return (flags2 & 3) == 3 || (flags1 & 3) == (flags2 & 3);
    }
}

