/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.buystuff;

import edu.computerapex.buystuff.BidPalCommunicationsHandler;
import edu.computerapex.dialogs.CommunicationsIdentity;

public class BidPalCommunicationsHandlerBuilder {
    private int port;
    private CommunicationsIdentity identity;
    private int maxBid;

    public BidPalCommunicationsHandlerBuilder assignPort(int port) {
        this.port = port;
        return this;
    }

    public BidPalCommunicationsHandlerBuilder fixIdentity(CommunicationsIdentity identity) {
        this.identity = identity;
        return this;
    }

    public BidPalCommunicationsHandlerBuilder setMaxBid(int maxBid) {
        this.maxBid = maxBid;
        return this;
    }

    public BidPalCommunicationsHandler generateBidPalCommunicationsHandler() {
        return new BidPalCommunicationsHandler(this.identity, this.port, this.maxBid);
    }
}

