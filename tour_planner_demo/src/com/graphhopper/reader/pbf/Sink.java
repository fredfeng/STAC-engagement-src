/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.reader.pbf;

import com.graphhopper.reader.OSMElement;

public interface Sink {
    public void process(OSMElement var1);

    public void complete();
}

