/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.buyOp.messagedata;

import edu.computerapex.buyOp.messagedata.BarterMessageData;
import edu.computerapex.buyOp.messagedata.BidCommitmentData;
import edu.computerapex.buyOp.messagedata.BidCommitmentDataBuilder;
import edu.computerapex.dialogs.CommunicationsPublicIdentity;
import edu.computerapex.math.EncryptionPublicKey;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class BidDivulgeData
extends BarterMessageData {
    private int bid;
    private double r1;
    private double r2;
    private BigInteger x;
    private byte[] hash;
    static Random random = new Random();

    public BidDivulgeData(String barterId, int bid, BigInteger x, double r1, double r2) throws IOException, NoSuchAlgorithmException {
        super(BarterMessageData.MessageType.CLAIM_WIN, barterId);
        this.bid = bid;
        this.x = x;
        this.r1 = r1;
        this.r2 = r2;
        this.hash = this.makeHash();
    }

    public BidDivulgeData(String barterId, int bid, int size) throws IOException, NoSuchAlgorithmException {
        super(BarterMessageData.MessageType.CLAIM_WIN, barterId);
        this.bid = bid;
        this.x = new BigInteger(size, random);
        this.r1 = random.nextDouble();
        this.r2 = random.nextDouble();
        this.hash = this.makeHash();
    }

    public BidCommitmentData fetchCommitmentData(CommunicationsPublicIdentity dest) {
        BigInteger share = dest.getPublicKey().encrypt(this.x).subtract(BigInteger.valueOf(this.bid));
        BidCommitmentData commit = new BidCommitmentDataBuilder().setBarterId(this.fetchBarterId()).setHash(this.hash).defineR1(this.r1).setSharedVal(share).generateBidCommitmentData();
        return commit;
    }

    private byte[] makeHash() throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeDouble(this.r1);
        dos.writeDouble(this.r2);
        dos.writeInt(this.bid);
        byte[] xBytes = this.x.toByteArray();
        dos.write(xBytes, 0, xBytes.length);
        dos.flush();
        byte[] hash = md.digest(bos.toByteArray());
        return hash;
    }

    public byte[] grabHash() {
        return this.hash;
    }

    public int grabBid() {
        return this.bid;
    }

    public BigInteger grabX() {
        return this.x;
    }

    public double grabR1() {
        return this.r1;
    }

    public double grabR2() {
        return this.r2;
    }
}

