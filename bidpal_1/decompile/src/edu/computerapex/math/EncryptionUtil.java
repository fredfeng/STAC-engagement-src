/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.math;

import edu.computerapex.math.EncryptionPrivateKey;
import edu.computerapex.math.EncryptionPublicKey;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class EncryptionUtil {
    public static BigInteger toBigInt(byte[] data, int bitSize) {
        if (data.length > bitSize + 1) {
            return EncryptionUtil.toBigIntWorker(data, bitSize);
        }
        return new BigInteger(1, data);
    }

    private static BigInteger toBigIntWorker(byte[] data, int bitSize) {
        throw new IllegalArgumentException("data length " + data.length + " is too long for bitsize " + bitSize);
    }

    public static BigInteger toBigInt(byte[] data) {
        return new BigInteger(1, data);
    }

    public static byte[] fromBigInt(BigInteger dataInt, int bitSize) {
        byte[] result = dataInt.toByteArray();
        if (result[0] == 0 && result.length > bitSize) {
            return EncryptionUtil.fromBigIntEntity(result);
        }
        if (result.length < bitSize) {
            byte[] tmp = new byte[bitSize];
            System.arraycopy(result, 0, tmp, tmp.length - result.length, result.length);
            return tmp;
        }
        return result;
    }

    private static byte[] fromBigIntEntity(byte[] result) {
        byte[] tmp = new byte[result.length - 1];
        System.arraycopy(result, 1, tmp, 0, tmp.length);
        return tmp;
    }

    public static byte[] stripPadding(byte[] ptBytes, int length) {
        int diff = ptBytes.length - length;
        return Arrays.copyOfRange(ptBytes, diff, ptBytes.length);
    }

    public static byte[] decrypt(byte[] data, EncryptionPrivateKey privateKey, int trimToSize) {
        return EncryptionUtil.stripPadding(privateKey.decryptBytes(data), trimToSize);
    }

    public static byte[] sign(byte[] data, EncryptionPrivateKey privateKey) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data);
            byte[] sig = privateKey.decryptBytes(hash);
            return sig;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verifySig(byte[] data, byte[] sig, EncryptionPublicKey publicKey) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] expectedHash = digest.digest(data);
            byte[] providedHash = EncryptionUtil.stripPadding(publicKey.encryptBytes(sig), expectedHash.length);
            return Arrays.equals(expectedHash, providedHash);
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}

