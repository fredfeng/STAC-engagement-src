/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.reader.pbf;

import com.graphhopper.reader.OSMElement;
import java.util.List;

public interface PbfBlobDecoderListener {
    public void complete(List<OSMElement> var1);

    public void error(Exception var1);
}

