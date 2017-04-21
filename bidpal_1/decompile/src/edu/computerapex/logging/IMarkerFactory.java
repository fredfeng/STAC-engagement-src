/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.logging;

import edu.computerapex.logging.Marker;

public interface IMarkerFactory {
    public Marker obtainMarker(String var1);

    public boolean exists(String var1);

    public boolean detachMarker(String var1);

    public Marker grabDetachedMarker(String var1);
}

