/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.math;

import edu.computerapex.math.OptimizedProductGenerator;
import java.math.BigInteger;

public class CircularExponentiation {
    public static BigInteger circularExponentiation(BigInteger base, BigInteger exponent, BigInteger floormod) {
        BigInteger r0 = BigInteger.valueOf(1);
        BigInteger r1 = base;
        int width = exponent.bitLength();
        for (int q = 0; q < width; ++q) {
            if (!exponent.testBit(width - q - 1)) {
                r1 = OptimizedProductGenerator.fastMultiply(r0, r1).mod(floormod);
                r0 = r0.multiply(r0).mod(floormod);
                continue;
            }
            r0 = OptimizedProductGenerator.fastMultiply(r0, r1).mod(floormod);
            r1 = r1.multiply(r1).mod(floormod);
        }
        return r0;
    }
}

