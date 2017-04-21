/*
 * Decompiled with CFR 0_121.
 */
package stac.crypto;

import java.text.MessageFormat;
import java.util.Arrays;
import stac.crypto.SHA256;
import stac.parser.OpenSSLRSAPEM;

public abstract class Key {
    private OpenSSLRSAPEM pem;
    private byte[] fingerPrint;

    public Key() {
    }

    public Key(byte[] fingerprint) {
        this.pem = null;
        this.fingerPrint = fingerprint;
    }

    public OpenSSLRSAPEM getPem() {
        return this.pem;
    }

    public void setPem(OpenSSLRSAPEM pem) {
        this.pem = pem;
        this.fingerPrint = null;
        this.getFingerPrint();
    }

    public byte[] getFingerPrint() {
        byte[] arrby = this.fingerPrint == null ? (this.fingerPrint = SHA256.digest(this.pem.getPublicExponent().getBytes(), this.pem.getModulus().getBytes())) : this.fingerPrint;
        return arrby;
    }

    public void setFingerPrint(byte[] fingerPrint) {
        this.fingerPrint = fingerPrint;
    }

    public String toString() {
        return MessageFormat.format("Key(pem: {0}, fingerPrint: {1}}", this.pem, Arrays.toString(this.fingerPrint));
    }

    public boolean matches(Key key) {
        boolean truth = this.fingerPrint.length == key.getFingerPrint().length;
        for (int i = 0; i < this.fingerPrint.length; ++i) {
            truth &= this.fingerPrint[i] == key.getFingerPrint()[i];
        }
        return truth;
    }
}

