/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.math;

import com.google.protobuf.ByteString;
import edu.computerapex.dialogs.Comms;
import edu.computerapex.json.simple.JSONObject;
import edu.computerapex.json.simple.parser.JSONRetriever;
import edu.computerapex.json.simple.parser.ParseDeviation;
import edu.computerapex.math.EncryptionUtil;
import edu.computerapex.math.MgProductGenerator;
import java.math.BigInteger;

public class EncryptionPublicKey {
    private BigInteger floormod;
    private BigInteger e;
    private MgProductGenerator mont;

    public EncryptionPublicKey(BigInteger floormod, BigInteger exponent) {
        this.floormod = floormod;
        this.e = exponent;
        this.mont = new MgProductGenerator(floormod);
    }

    public Comms.PublicKey serializePublicKey() {
        Comms.PublicKey commsPublicKey = Comms.PublicKey.newBuilder().setE(ByteString.copyFrom(this.grabE().toByteArray())).setModulus(ByteString.copyFrom(this.obtainFloormod().toByteArray())).build();
        return commsPublicKey;
    }

    public byte[] encrypt(byte[] data) {
        return this.encryptBytes(data);
    }

    public BigInteger obtainFloormod() {
        return this.floormod;
    }

    public BigInteger grabE() {
        return this.e;
    }

    public BigInteger encrypt(BigInteger message) {
        return this.mont.exponentiate(message, this.e);
    }

    public int getBitSize() {
        int bitSize = this.floormod.bitLength();
        return (bitSize + 7) / 8 - 1;
    }

    public byte[] encryptBytes(byte[] message) {
        BigInteger pt = EncryptionUtil.toBigInt(message, this.getBitSize());
        BigInteger ct = this.encrypt(pt);
        return EncryptionUtil.fromBigInt(ct, this.getBitSize());
    }

    public String toString() {
        return "modulus: " + this.floormod.toString() + " e: " + this.e.toString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        EncryptionPublicKey that = (EncryptionPublicKey)o;
        if (!this.floormod.equals(that.floormod)) {
            return false;
        }
        return this.e.equals(that.e);
    }

    public int hashCode() {
        int result = this.floormod.hashCode();
        result = 31 * result + this.e.hashCode();
        return result;
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("modulus", this.floormod.toString());
        json.put("exponent", this.e.toString());
        return json;
    }

    public static EncryptionPublicKey fromJson(String jsonString) throws ParseDeviation {
        JSONRetriever retriever = new JSONRetriever();
        return EncryptionPublicKey.fromJson((JSONObject)retriever.parse(jsonString));
    }

    public static EncryptionPublicKey fromJson(JSONObject publicKeyJson) {
        BigInteger floormod = new BigInteger((String)publicKeyJson.get("modulus"));
        BigInteger exponent = new BigInteger((String)publicKeyJson.get("exponent"));
        return new EncryptionPublicKey(floormod, exponent);
    }
}

