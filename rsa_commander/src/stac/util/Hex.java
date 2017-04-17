/*
 * Decompiled with CFR 0_121.
 */
package stac.util;

public class Hex {
    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 3];
        for (int j = 0; j < bytes.length; ++j) {
            int v = bytes[j] & 255;
            hexChars[j * 3] = hexArray[v >>> 4];
            hexChars[j * 3 + 1] = hexArray[v & 15];
            hexChars[j * 3 + 2] = 32;
        }
        return new String(hexChars, 0, hexChars.length - 1);
    }

    public static String bytesToHex(byte[] bytes, int offset, int length) {
        if (offset + length < bytes.length) {
            char[] hexChars = new char[length * 3];
            for (int j = offset; j < length; ++j) {
                int v = bytes[j] & 255;
                hexChars[j * 3] = hexArray[v >>> 4];
                hexChars[j * 3 + 1] = hexArray[v & 15];
                hexChars[j * 3 + 2] = 32;
            }
            return new String(hexChars, 0, hexChars.length - 1);
        }
        throw new ArrayIndexOutOfBoundsException("Offset + Length exceeds array bounds [bytesToHex]");
    }
}

