/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.storage;

import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.storage.Directory;
import com.graphhopper.storage.Storable;
import com.graphhopper.storage.StorableProperties;

public interface GraphStorage
extends Storable<GraphStorage> {
    public Directory getDirectory();

    public EncodingManager getEncodingManager();

    public void setSegmentSize(int var1);

    public String toDetailsString();

    public StorableProperties getProperties();

    public void markNodeRemoved(int var1);

    public boolean isNodeRemoved(int var1);

    public void optimize();
}

