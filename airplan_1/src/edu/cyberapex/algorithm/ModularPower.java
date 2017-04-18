/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.algorithm;

import edu.cyberapex.algorithm.OptimizedMultiplier;
import java.math.BigInteger;

public class ModularPower {
    public static BigInteger modularPower(BigInteger base, BigInteger exponent, BigInteger floormod) {
        BigInteger r0 = BigInteger.valueOf(1);
        BigInteger r1 = base;
        int width = exponent.bitLength();
        for (int q = 0; q < width; ++q) {
            if (!exponent.testBit(width - q - 1)) {
                r1 = OptimizedMultiplier.fastMultiply(r0, r1).mod(floormod);
                r0 = r0.multiply(r0).mod(floormod);
                continue;
            }
            r0 = OptimizedMultiplier.fastMultiply(r0, r1).mod(floormod);
            r1 = r1.multiply(r1).mod(floormod);
        }
        return r0;
    }
}

