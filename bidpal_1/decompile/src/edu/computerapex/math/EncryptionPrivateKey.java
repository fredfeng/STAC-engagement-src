/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.math;

import edu.computerapex.json.simple.JSONObject;
import edu.computerapex.math.EncryptionPublicKey;
import edu.computerapex.math.EncryptionUtil;
import edu.computerapex.math.MgProductGenerator;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

public class EncryptionPrivateKey {
    private static final int MAX_KEY_LEN = 1024;
    private BigInteger p;
    private BigInteger q;
    private BigInteger floormod;
    private BigInteger e;
    private BigInteger d;
    private BigInteger dp;
    private BigInteger dq;
    private BigInteger qInv;
    private BigInteger pMinus1;
    private BigInteger qMinus1;
    private MgProductGenerator montP;
    private MgProductGenerator montQ;
    private EncryptionPublicKey publicKey;

    public EncryptionPrivateKey(BigInteger p, BigInteger q, BigInteger e) {
        this.p = p;
        this.pMinus1 = p.subtract(BigInteger.ONE);
        this.q = q;
        this.qMinus1 = q.subtract(BigInteger.ONE);
        this.floormod = p.multiply(q);
        this.e = e;
        this.d = e.modInverse(this.pMinus1.multiply(this.qMinus1));
        this.dp = this.d.mod(this.pMinus1);
        this.dq = this.d.mod(this.qMinus1);
        this.qInv = q.modInverse(p);
        this.montP = new MgProductGenerator(p);
        this.montQ = new MgProductGenerator(q);
        this.publicKey = new EncryptionPublicKey(this.floormod, e);
        if (this.floormod.bitLength() > 1024) {
            new EncryptionPrivateKeyWorker().invoke();
        }
    }

    public EncryptionPrivateKey(BigInteger p, BigInteger q) {
        this(p, q, BigInteger.valueOf(65537));
    }

    public static EncryptionPrivateKey generateKey(int seed, int floormodSize) throws Exception {
        Random random = new Random();
        random.setSeed(seed);
        BigInteger prime1 = BigInteger.probablePrime(floormodSize / 2, random);
        BigInteger prime2 = BigInteger.probablePrime(floormodSize / 2, random);
        return new EncryptionPrivateKey(prime1, prime2);
    }

    public static EncryptionPrivateKey generateKeyFromFiles(String pFileName, String qFileName) throws FileNotFoundException {
        String pString = new Scanner(new File(pFileName)).useDelimiter("\\Z").next();
        String qString = new Scanner(new File(qFileName)).useDelimiter("\\Z").next();
        BigInteger p = EncryptionPrivateKey.stringToBigInt(pString);
        BigInteger q = EncryptionPrivateKey.stringToBigInt(qString);
        return new EncryptionPrivateKey(p, q);
    }

    public static EncryptionPrivateKey generateKeyFromFile(String keyFile) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(keyFile));
        String pString = scanner.next();
        String qString = scanner.next();
        BigInteger p = EncryptionPrivateKey.stringToBigInt(pString);
        BigInteger q = EncryptionPrivateKey.stringToBigInt(qString);
        return new EncryptionPrivateKey(p, q);
    }

    public static EncryptionPrivateKey generateKeyFromJson(JSONObject privateKeyJson) {
        BigInteger p = EncryptionPrivateKey.stringToBigInt((String)privateKeyJson.get("p"));
        BigInteger q = EncryptionPrivateKey.stringToBigInt((String)privateKeyJson.get("q"));
        return new EncryptionPrivateKey(p, q);
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("p", this.p.toString());
        json.put("q", this.q.toString());
        return json;
    }

    public String toJSONString() {
        return this.toJSONObject().toJSONString();
    }

    private static BigInteger stringToBigInt(String str) {
        if ((str = str.trim()).endsWith("L")) {
            str = str.substring(0, str.length() - 1);
        }
        return new BigInteger(str);
    }

    public EncryptionPublicKey getPublicKey() {
        return this.publicKey;
    }

    public int getBitSize() {
        int bitSize = this.floormod.bitLength();
        return (bitSize + 7) / 8 - 1;
    }

    public BigInteger decrypt(BigInteger ciphertext) {
        BigInteger m1 = this.montP.exponentiate(ciphertext, this.dp);
        BigInteger m2 = this.montQ.exponentiate(ciphertext, this.dq);
        BigInteger h = this.qInv.multiply(m1.subtract(m2)).mod(this.p);
        BigInteger m = m2.add(h.multiply(this.q));
        return m;
    }

    public byte[] decryptBytes(byte[] ciphertext) {
        BigInteger ct = EncryptionUtil.toBigInt(ciphertext, this.getBitSize());
        BigInteger pt = this.decrypt(ct);
        return EncryptionUtil.fromBigInt(pt, this.getBitSize());
    }

    public String toString() {
        return "p: " + this.p.toString() + " q: " + this.q.toString();
    }

    private class EncryptionPrivateKeyWorker {
        private EncryptionPrivateKeyWorker() {
        }

        public void invoke() {
            throw new IllegalArgumentException("Large primes not supported");
        }
    }

}

