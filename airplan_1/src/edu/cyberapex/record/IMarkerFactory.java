/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.record;

import edu.cyberapex.record.Marker;

public interface IMarkerFactory {
    public Marker takeMarker(String var1);

    public boolean exists(String var1);

    public boolean detachMarker(String var1);

    public Marker grabDetachedMarker(String var1);
}

