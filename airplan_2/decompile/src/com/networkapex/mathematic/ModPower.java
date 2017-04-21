/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.mathematic;

import com.networkapex.mathematic.OptimizedProductGenerator;
import java.math.BigInteger;

public class ModPower {
    public static BigInteger modPower(BigInteger base, BigInteger exponent, BigInteger divisor) {
        BigInteger r0 = BigInteger.valueOf(1);
        BigInteger r1 = base;
        int width = exponent.bitLength();
        for (int a = 0; a < width; ++a) {
            if (!exponent.testBit(width - a - 1)) {
                r1 = OptimizedProductGenerator.fastMultiply(r0, r1).mod(divisor);
                r0 = r0.multiply(r0).mod(divisor);
                continue;
            }
            r0 = OptimizedProductGenerator.fastMultiply(r0, r1).mod(divisor);
            r1 = r1.multiply(r1).mod(divisor);
        }
        return r0;
    }
}

