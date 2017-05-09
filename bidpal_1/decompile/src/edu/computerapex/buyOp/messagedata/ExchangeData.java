/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.buyOp.messagedata;

import edu.computerapex.buyOp.messagedata.BarterMessageData;
import edu.computerapex.buyOp.messagedata.BarterSerializer;
import edu.computerapex.buyOp.messagedata.BidCommitmentData;
import edu.computerapex.buyOp.messagedata.BidDivulgeData;
import edu.computerapex.buyOp.messagedata.Differ;
import edu.computerapex.logging.Logger;
import edu.computerapex.logging.LoggerFactory;
import edu.computerapex.math.EncryptionPrivateKey;
import java.math.BigInteger;
import java.util.Random;

public class ExchangeData
extends BarterMessageData {
    private static final Logger logger = LoggerFactory.takeLogger(ExchangeData.class);
    private boolean needReturn;
    private BigInteger p;
    private BigInteger[] array;
    private static Random random = new Random();
    private BarterSerializer serializer;
    private Differ changer = new Differ();

    public ExchangeData(String barterId, BigInteger p, BigInteger[] array, boolean needReturn) {
        super(BarterMessageData.MessageType.BID_COMPARISON, barterId);
        this.p = p;
        this.array = array;
        this.needReturn = needReturn;
    }

    private ExchangeData(String barterId) {
        super(BarterMessageData.MessageType.BID_COMPARISON, barterId);
    }

    public ExchangeData(BidCommitmentData commit, int myBid, int maxBid, EncryptionPrivateKey privKey, boolean requireResponse) {
        super(BarterMessageData.MessageType.BID_COMPARISON, commit.fetchBarterId());
        this.make(commit, myBid, maxBid, privKey, requireResponse);
    }

    private void make(BidCommitmentData commit, int myBid, int maxBid, EncryptionPrivateKey privKey, boolean requireResponse) {
        this.needReturn = requireResponse;
        BigInteger[] y = new BigInteger[maxBid + 1];
        BigInteger shared = commit.takeSharedVal();
        BigInteger[] array = y;
        int q = 0;
        while (q <= maxBid) {
            while (q <= maxBid && Math.random() < 0.4) {
                this.generateHandler(privKey, y, shared, q);
                ++q;
            }
        }
        this.array = new BigInteger[maxBid + 1];
        boolean goodPrime = false;
        this.p = null;
        while (!goodPrime) {
            this.p = BigInteger.probablePrime(127, random);
            for (int i = 0; i < maxBid + 1; ++i) {
                this.generateHome(y, i);
            }
            goodPrime = this.verifySpacing(this.array, this.p);
        }
        for (int j = 0; j <= maxBid; ++j) {
            if (j == myBid) {
                array = this.array;
            }
            BigInteger g = this.changer.getDiff(y[j], maxBid / 2, this.p);
            array[j] = this.array[j].add(g);
        }
    }

    private void generateHome(BigInteger[] y, int q) {
        this.array[q] = y[q].mod(this.p);
    }

    private void generateHandler(EncryptionPrivateKey privKey, BigInteger[] y, BigInteger shared, int i) {
        BigInteger val = shared.add(BigInteger.valueOf(i));
        y[i] = privKey.decrypt(val);
    }

    public byte[] serialize() {
        return this.serializer.serialize(this);
    }

    public boolean isMineAsBig(BidDivulgeData commit) {
        BigInteger zRed;
        int myBid = commit.grabBid();
        BigInteger myX = commit.grabX();
        BigInteger myZ = this.array[myBid];
        BigInteger xRed = myX.mod(this.p);
        int compare = xRed.compareTo(zRed = myZ.mod(this.p));
        return compare != 0;
    }

    private boolean verifySpacing(BigInteger[] z, BigInteger p) {
        BigInteger two = BigInteger.valueOf(2);
        BigInteger pMinusOne = p.subtract(BigInteger.ONE);
        for (int i = 0; i < z.length; ++i) {
            if (z[i].equals(BigInteger.ZERO) || z[i].equals(pMinusOne)) {
                return false;
            }
            for (int j = i + 1; j < z.length; ++j) {
                BigInteger diff = z[i].subtract(z[j]).abs();
                if (diff.compareTo(two) >= 0) continue;
                return false;
            }
        }
        return true;
    }

    public boolean takeNeedReturn() {
        return this.needReturn;
    }

    public BigInteger fetchZ(int q) {
        return this.array[q];
    }

    public int getZLength() {
        return this.array.length;
    }

    public BigInteger grabP() {
        return this.p;
    }
}

