/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.slf4j;

import com.networkapex.slf4j.Marker;

public interface IMarkerFactory {
    public Marker fetchMarker(String var1);

    public boolean exists(String var1);

    public boolean detachMarker(String var1);

    public Marker grabDetachedMarker(String var1);
}

