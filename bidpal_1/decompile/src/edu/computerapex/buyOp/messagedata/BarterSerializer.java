/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.buyOp.messagedata;

import edu.computerapex.buyOp.messagedata.BarterMessageData;
import edu.computerapex.buyOp.messagedata.BidCommitmentData;
import edu.computerapex.buyOp.messagedata.BidDivulgeData;
import edu.computerapex.buyOp.messagedata.ExchangeData;
import java.io.IOException;
import java.io.NotSerializableException;

public abstract class BarterSerializer {
    public abstract byte[] serialize(BarterMessageData.BarterStart var1);

    public abstract byte[] serialize(BidCommitmentData var1);

    public abstract byte[] serialize(ExchangeData var1);

    public abstract byte[] serialize(BarterMessageData.BidReceipt var1);

    public abstract byte[] serialize(BarterMessageData.BiddingOver var1);

    public abstract byte[] serialize(BarterMessageData.Concession var1);

    public abstract byte[] serialize(BidDivulgeData var1);

    public abstract byte[] serialize(BarterMessageData.BarterEnd var1);

    public byte[] serialize(BarterMessageData msg) throws NotSerializableException {
        if (msg instanceof BarterMessageData.BarterStart) {
            return this.serialize((BarterMessageData.BarterStart)msg);
        }
        if (msg instanceof BidCommitmentData) {
            return this.serialize((BidCommitmentData)msg);
        }
        if (msg instanceof BarterMessageData.BidReceipt) {
            return this.serialize((BarterMessageData.BidReceipt)msg);
        }
        if (msg instanceof ExchangeData) {
            return this.serialize((ExchangeData)msg);
        }
        if (msg instanceof BarterMessageData.BiddingOver) {
            return this.serialize((BarterMessageData.BiddingOver)msg);
        }
        if (msg instanceof BarterMessageData.Concession) {
            return this.serialize((BarterMessageData.Concession)msg);
        }
        if (msg instanceof BidDivulgeData) {
            return this.serialize((BidDivulgeData)msg);
        }
        if (msg instanceof BarterMessageData.BarterEnd) {
            return this.serialize((BarterMessageData.BarterEnd)msg);
        }
        throw new NotSerializableException("BidPalSerializer received BidPalMessageData of unsupported type " + (Object)((Object)msg.type));
    }

    public abstract BarterMessageData deserialize(byte[] var1) throws IOException;
}

