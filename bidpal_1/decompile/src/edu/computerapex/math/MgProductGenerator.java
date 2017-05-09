/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.math;

import java.math.BigInteger;

public class MgProductGenerator {
    private BigInteger M;
    private BigInteger R;
    private BigInteger Rminus1;
    private BigInteger Rinverse;
    private BigInteger Mstar;
    private int w;

    public MgProductGenerator(BigInteger floormod) throws IllegalArgumentException {
        this.M = floormod;
        this.w = this.M.bitLength();
        if (this.w % 8 != 0) {
            this.MgProductGeneratorHelper();
        }
        this.R = this.computeR();
        this.Rinverse = this.R.modInverse(this.M);
        this.Rminus1 = this.R.subtract(BigInteger.ONE);
        this.Mstar = this.R.subtract(this.M.modInverse(this.R));
    }

    private void MgProductGeneratorHelper() {
        this.w = (this.M.bitLength() / 8 + 1) * 8;
    }

    private BigInteger computeR() throws IllegalArgumentException {
        BigInteger R = BigInteger.ONE.shiftLeft(this.w);
        if (!R.gcd(this.M).equals(BigInteger.ONE) || R.compareTo(this.M) <= 0) {
            throw new IllegalArgumentException("Unable to compute R for modulus " + this.M);
        }
        return R;
    }

    public BigInteger multiply(BigInteger a, BigInteger b) {
        BigInteger aBar = this.translate(a);
        BigInteger bBar = this.translate(b);
        BigInteger tmp = this.mgMultiply(aBar, bBar);
        return this.deTranslate(tmp);
    }

    public BigInteger exponentiate(BigInteger x, BigInteger y) {
        BigInteger xBar = this.translate(x);
        BigInteger result = this.translate(BigInteger.ONE);
        for (int k = y.bitLength() - 1; k >= 0; --k) {
            result = y.testBit(k) ? this.mgMultiply(this.mgMultiply(result, result), xBar) : this.mgMultiply(result, result);
        }
        return this.deTranslate(result);
    }

    private BigInteger mgMultiply(BigInteger aBar, BigInteger bBar) {
        BigInteger t = aBar.multiply(bBar);
        BigInteger u = t.multiply(this.Mstar);
        u = u.and(this.Rminus1);
        u = t.add(u.multiply(this.M));
        if ((u = u.shiftRight(this.w)).compareTo(this.M) > 0) {
            u = u.mod(this.M);
        }
        return u;
    }

    public BigInteger translate(BigInteger a) {
        return a.multiply(this.R).mod(this.M);
    }

    public BigInteger deTranslate(BigInteger u) {
        return u.multiply(this.Rinverse).mod(this.M);
    }
}

