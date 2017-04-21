/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.dialogs;

import edu.computerapex.dialogs.CommunicationsConnection;
import edu.computerapex.dialogs.CommunicationsDeviation;

public interface CommunicationsHandler {
    public void handle(CommunicationsConnection var1, byte[] var2) throws CommunicationsDeviation;

    public void newConnection(CommunicationsConnection var1) throws CommunicationsDeviation;

    public void closedConnection(CommunicationsConnection var1) throws CommunicationsDeviation;
}

