/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.buyOp.messagedata;

import edu.computerapex.buyOp.bad.BarterDeviation;
import edu.computerapex.buyOp.messagedata.BarterMessageData;
import edu.computerapex.buyOp.messagedata.BidDivulgeData;
import edu.computerapex.math.EncryptionPublicKey;
import java.math.BigInteger;
import java.util.Arrays;

public class BidCommitmentData
extends BarterMessageData {
    private BigInteger sharedVal;
    private double r1;
    private byte[] hash;

    public BidCommitmentData(String barterId, byte[] hash, double r1, BigInteger sharedVal) {
        super(BarterMessageData.MessageType.BID_COMMITMENT, barterId);
        this.hash = hash;
        this.r1 = r1;
        this.sharedVal = sharedVal;
    }

    public boolean verify(BidDivulgeData divulge, EncryptionPublicKey myPubKey) throws BarterDeviation {
        if (this.r1 != divulge.grabR1()) {
            return false;
        }
        if (!this.sharedVal.equals(myPubKey.encrypt(divulge.grabX()).subtract(BigInteger.valueOf(divulge.grabBid())))) {
            return false;
        }
        try {
            if (!Arrays.equals(this.hash, divulge.grabHash())) {
                return false;
            }
        }
        catch (Exception e) {
            throw new BarterDeviation(e);
        }
        return true;
    }

    public BigInteger takeSharedVal() {
        return this.sharedVal;
    }

    public byte[] takeHash() {
        return this.hash;
    }

    public double getR1() {
        return this.r1;
    }
}

