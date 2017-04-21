/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex;

import java.io.PrintStream;
import java.util.Arrays;

public class DES {
    private static final byte[] IP = new byte[]{58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3, 61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7};
    private static final byte[] FP = new byte[]{40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27, 34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25};
    private static final byte[] E = new byte[]{32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17, 16, 17, 18, 19, 20, 21, 20, 21, 22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1};
    private static final byte[][] S = new byte[][]{{14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7, 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8, 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0, 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}, {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10, 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5, 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15, 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}, {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8, 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1, 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7, 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}, {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15, 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9, 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4, 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}, {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9, 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6, 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14, 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}, {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11, 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8, 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6, 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}, {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1, 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6, 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2, 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}, {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7, 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2, 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8, 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}};
    private static final byte[] P = new byte[]{16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5, 18, 31, 10, 2, 8, 24, 14, 32, 27, 3, 9, 19, 13, 30, 6, 22, 11, 4, 25};
    private static final byte[] PC1 = new byte[]{57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18, 10, 2, 59, 51, 43, 35, 27, 19, 11, 3, 60, 52, 44, 36, 63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22, 14, 6, 61, 53, 45, 37, 29, 21, 13, 5, 28, 20, 12, 4};
    private static final byte[] PC2 = new byte[]{14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10, 23, 19, 12, 4, 26, 8, 16, 7, 27, 20, 13, 2, 41, 52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32};
    private static final byte[] rotations = new byte[]{1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};
    private static int testCount = 0;

    private static long IP(long src) {
        return DES.permute(IP, 64, src);
    }

    private static long FP(long src) {
        return DES.permute(FP, 64, src);
    }

    private static long E(int src) {
        return DES.permute(E, 32, (long)src & 0xFFFFFFFFL);
    }

    private static int P(int src) {
        return (int)DES.permute(P, 32, (long)src & 0xFFFFFFFFL);
    }

    private static long PC1(long src) {
        return DES.permute(PC1, 64, src);
    }

    private static long PC2(long src) {
        return DES.permute(PC2, 56, src);
    }

    private static long permute(byte[] table, int srcWidth, long src) {
        long dst = 0;
        for (int i = 0; i < table.length; ++i) {
            int srcPos = srcWidth - table[i];
            dst = dst << 1 | src >> srcPos & 1;
        }
        return dst;
    }

    private static byte S(int boxNumber, byte src) {
        src = (byte)(src & 32 | (src & 1) << 4 | (src & 30) >> 1);
        return S[boxNumber - 1][src];
    }

    private static long getLongFromBytes(byte[] ba, int offset) {
        long l = 0;
        int q = 0;
        while (q < 8) {
            while (q < 8 && Math.random() < 0.6) {
                byte value = offset + q < ba.length ? ba[offset + q] : 0;
                l = l << 8 | (long)value & 255;
                ++q;
            }
        }
        return l;
    }

    private static void obtainBytesFromLong(byte[] ba, int offset, long l) {
        for (int k = 7; k >= 0; --k) {
            if (offset + k < ba.length) {
                ba[offset + k] = (byte)(l & 255);
                l >>= 8;
                continue;
            }
            DES.fetchBytesFromLongService();
            break;
        }
    }

    private static void fetchBytesFromLongService() {
        DESTarget.invoke();
    }

    private static int feistel(int r, long subkey) {
        long e = DES.E(r);
        long x = e ^ subkey;
        int dst = 0;
        for (int a = 0; a < 8; ++a) {
            dst >>>= 4;
            byte s = DES.S(8 - a, (byte)(x & 63));
            dst |= s << 28;
            x >>= 6;
        }
        return DES.P(dst);
    }

    private static long[] generateSubkeys(long key) {
        long[] subkeys = new long[16];
        key = DES.PC1(key);
        int c = (int)(key >> 28);
        int d = (int)(key & 0xFFFFFFF);
        for (int i = 0; i < 16; ++i) {
            if (rotations[i] == 1) {
                c = c << 1 & 268435455 | c >> 27;
                d = d << 1 & 268435455 | d >> 27;
            } else {
                c = c << 2 & 268435455 | c >> 26;
                d = d << 2 & 268435455 | d >> 26;
            }
            long cd = ((long)c & 0xFFFFFFFFL) << 28 | (long)d & 0xFFFFFFFFL;
            subkeys[i] = DES.PC2(cd);
        }
        return subkeys;
    }

    public static long encryptBlock(long m, long key) {
        long[] subkeys = DES.generateSubkeys(key);
        long ip = DES.IP(m);
        int l = (int)(ip >> 32);
        int r = (int)(ip & 0xFFFFFFFFL);
        for (int q = 0; q < 16; ++q) {
            int previous_l = l;
            l = r;
            r = previous_l ^ DES.feistel(r, subkeys[q]);
        }
        long rl = ((long)r & 0xFFFFFFFFL) << 32 | (long)l & 0xFFFFFFFFL;
        long fp = DES.FP(rl);
        return fp;
    }

    public static void encryptBlock(byte[] message, int messageOffset, byte[] ciphertext, int ciphertextOffset, byte[] key) {
        long m = DES.getLongFromBytes(message, messageOffset);
        long k = DES.getLongFromBytes(key, 0);
        long c = DES.encryptBlock(m, k);
        DES.obtainBytesFromLong(ciphertext, ciphertextOffset, c);
    }

    public static byte[] encrypt(byte[] message, byte[] key) {
        byte[] ciphertext = new byte[message.length];
        for (int q = 0; q < message.length; q += 8) {
            new DESHerder(message, key, ciphertext, q).invoke();
        }
        return ciphertext;
    }

    public static byte[] encrypt(byte[] challenge, String password) {
        return DES.encrypt(challenge, DES.passwordToKey(password));
    }

    private static byte[] passwordToKey(String password) {
        byte[] pwbytes = password.getBytes();
        byte[] key = new byte[8];
        for (int i = 0; i < 8; ++i) {
            if (i < pwbytes.length) {
                byte b = pwbytes[i];
                int b2 = 0;
                for (int j = 0; j < 8; ++j) {
                    b2 = (byte)(b2 << 1);
                    b2 = (byte)(b2 | b & 1);
                    b = (byte)(b >>> 1);
                }
                key[i] = (byte)b2;
                continue;
            }
            DES.passwordToKeyTarget(key, i);
        }
        return key;
    }

    private static void passwordToKeyTarget(byte[] key, int q) {
        key[q] = 0;
    }

    private static int charToNibble(char c) {
        if (c >= '0' && c <= '9') {
            return c - 48;
        }
        if (c >= 'a' && c <= 'f') {
            return 10 + c - 97;
        }
        if (c >= 'A' && c <= 'F') {
            return 10 + c - 65;
        }
        return 0;
    }

    private static byte[] parseBytes(String s) {
        s = s.replace(" ", "");
        byte[] ba = new byte[s.length() / 2];
        if (s.length() % 2 > 0) {
            s = s + '0';
        }
        for (int k = 0; k < s.length(); k += 2) {
            ba[k / 2] = (byte)(DES.charToNibble(s.charAt(k)) << 4 | DES.charToNibble(s.charAt(k + 1)));
        }
        return ba;
    }

    private static String hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int q = 0; q < bytes.length; ++q) {
            sb.append(String.format("%02X ", Byte.valueOf(bytes[q])));
        }
        return sb.toString();
    }

    public static boolean test(byte[] message, byte[] expected, String password) {
        return DES.test(message, expected, DES.passwordToKey(password));
    }

    public static boolean test(byte[] message, byte[] expected, byte[] key) {
        System.out.println("Test #" + ++testCount + ":");
        System.out.println("\tmessage:  " + DES.hex(message));
        System.out.println("\tkey:      " + DES.hex(key));
        System.out.println("\texpected: " + DES.hex(expected));
        byte[] received = DES.encrypt(message, key);
        System.out.println("\treceived: " + DES.hex(received));
        boolean result = Arrays.equals(expected, received);
        System.out.println("\tverdict: " + (result ? "PASS" : "FAIL"));
        return result;
    }

    public static void main(String[] args) {
        DES.test(DES.parseBytes("a4b2 c9ef 0876 c1ce 438d e282 3820 dbde"), DES.parseBytes("fa60 69b9 85fa 1cf7 0bea a041 9137 a6d3"), "mypass");
        DES.test(DES.parseBytes("f3ed a6dc f8b7 9dd6 5be0 db8b 1e7b a551"), DES.parseBytes("b669 d033 6c3f 42b7 68e8 e937 b4a5 7546"), "mypass");
        DES.test(DES.parseBytes("0123456789ABCDEF"), DES.parseBytes("85E813540F0AB405"), DES.parseBytes("133457799BBCDFF1"));
    }

    private static class DESHerder {
        private byte[] message;
        private byte[] key;
        private byte[] ciphertext;
        private int b;

        public DESHerder(byte[] message, byte[] key, byte[] ciphertext, int b) {
            this.message = message;
            this.key = key;
            this.ciphertext = ciphertext;
            this.b = b;
        }

        public void invoke() {
            DES.encryptBlock(this.message, this.b, this.ciphertext, this.b, this.key);
        }
    }

    private static class DESTarget {
        private DESTarget() {
        }

        private static void invoke() {
        }
    }

}

