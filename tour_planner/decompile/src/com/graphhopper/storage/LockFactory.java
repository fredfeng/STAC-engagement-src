/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.storage;

import com.graphhopper.storage.Lock;
import java.io.File;

public interface LockFactory {
    public void setLockDir(File var1);

    public Lock create(String var1, boolean var2);

    public void forceRemove(String var1, boolean var2);
}

