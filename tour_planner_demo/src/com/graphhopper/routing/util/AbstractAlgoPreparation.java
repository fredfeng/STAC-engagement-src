/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing.util;

public abstract class AbstractAlgoPreparation {
    private boolean prepared = false;

    public void doWork() {
        if (this.prepared) {
            throw new IllegalStateException("Call doWork only once!");
        }
        this.prepared = true;
    }

    public boolean isPrepared() {
        return this.prepared;
    }
}

