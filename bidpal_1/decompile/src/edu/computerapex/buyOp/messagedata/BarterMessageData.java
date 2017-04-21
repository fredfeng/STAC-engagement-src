/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.buyOp.messagedata;

import edu.computerapex.buyOp.messagedata.BarterSerializer;

public abstract class BarterMessageData {
    protected static BarterSerializer serializer;
    public final MessageType type;
    public String barterId;

    public BarterMessageData(MessageType type, String barterId) {
        this.type = type;
        this.barterId = barterId;
    }

    public static void setSerializer(BarterSerializer serialer) {
        serializer = serialer;
    }

    public String fetchBarterId() {
        return this.barterId;
    }

    public static class BarterEnd
    extends BarterMessageData {
        public String winner;
        public int winningBid;

        public BarterEnd(String id, String winner, int winningBid) {
            super(MessageType.AUCTION_END, id);
            this.winner = winner;
            this.winningBid = winningBid;
        }
    }

    public static class Concession
    extends BarterMessageData {
        public Concession(String id) {
            super(MessageType.CONCESSION, id);
        }
    }

    public static class BiddingOver
    extends BarterMessageData {
        public BiddingOver(String id) {
            super(MessageType.BIDDING_OVER, id);
        }
    }

    public static class BidReceipt
    extends BarterMessageData {
        public BidReceipt(String id) {
            super(MessageType.BID_RECEIPT, id);
        }
    }

    public static class BarterStart
    extends BarterMessageData {
        public String description;

        public BarterStart(String id, String desc) {
            super(MessageType.AUCTION_START, id);
            this.description = desc;
        }
    }

    public static enum MessageType {
        AUCTION_START,
        BID_RECEIPT,
        BID_COMMITMENT,
        BID_COMPARISON,
        BIDDING_OVER,
        CLAIM_WIN,
        CONCESSION,
        AUCTION_END;
        

        private MessageType() {
        }
    }

}

