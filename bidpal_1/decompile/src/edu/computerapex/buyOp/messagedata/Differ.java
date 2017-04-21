/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.buyOp.messagedata;

import java.math.BigInteger;

public class Differ {
    private int mode = -1;

    public void setState(int mode) {
        this.mode = mode;
    }

    public BigInteger getDiff(BigInteger a, int b, BigInteger p) {
        if (this.mode == 0) {
            return BigInteger.ZERO;
        }
        if (this.mode == 1) {
            return BigInteger.ONE;
        }
        if (b % 2 == 1) {
            return this.oddDiff(a, b, p, BigInteger.ONE);
        }
        return this.getDiff(a, b, p, BigInteger.ONE);
    }

    public BigInteger getDiff(BigInteger a, int b, BigInteger p, BigInteger curr) {
        if (b % 2 == 1) {
            return this.oddDiff(a, b, p, curr);
        }
        if (b == 0) {
            return this.modifierGuide(curr);
        }
        return this.oddDiff(a, b - 1, p, curr.multiply(a).mod(p));
    }

    private BigInteger modifierGuide(BigInteger curr) {
        if (curr.equals(BigInteger.ZERO)) {
            return BigInteger.ONE;
        }
        return curr;
    }

    public BigInteger oddDiff(BigInteger a, int b, BigInteger p, BigInteger curr) {
        return this.getDiff(a, b - b % 2, p, curr.multiply(a).mod(p));
    }
}

