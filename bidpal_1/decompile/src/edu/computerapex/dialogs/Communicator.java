/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.dialogs;

import edu.computerapex.dialogs.CommunicationsDeviation;
import edu.computerapex.dialogs.CommunicationsPublicIdentity;

public interface Communicator {
    public void deliver(CommunicationsPublicIdentity var1, byte[] var2) throws CommunicationsDeviation;
}

