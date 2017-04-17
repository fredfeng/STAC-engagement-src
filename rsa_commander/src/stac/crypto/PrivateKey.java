/*
 * Decompiled with CFR 0_121.
 */
package stac.crypto;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.text.MessageFormat;
import stac.crypto.Key;
import stac.crypto.PublicKey;
import stac.parser.OpenSSLRSAPEM;

public class PrivateKey
extends Key {
    public PrivateKey(File file) throws IOException {
        this.setPem(new OpenSSLRSAPEM(file));
        if (this.getPem().getType() != OpenSSLRSAPEM.DER_TYPE.PRIVATE_KEY) {
            throw new RuntimeException("Invalid private key file");
        }
    }

    public PrivateKey(OpenSSLRSAPEM pem) throws IOException {
        if (pem.getModulus().getInternalBig().bitCount() > 512) {
            throw new RuntimeException("This private key is not sized correctly");
        }
        this.setPem(pem);
        if (this.getPem().getType() != OpenSSLRSAPEM.DER_TYPE.PRIVATE_KEY) {
            throw new RuntimeException("Invalid private key file");
        }
    }

    public PrivateKey(byte[] fingerprint) {
        super(fingerprint);
    }

    public PrivateKey() {
    }

    public PublicKey toPublicKey() {
        OpenSSLRSAPEM pem = this.getPem();
        OpenSSLRSAPEM publicConversion = new OpenSSLRSAPEM(pem.getPublicExponent(), pem.getModulus());
        return new PublicKey(publicConversion);
    }

    @Override
    public String toString() {
        return MessageFormat.format("PrivateKey(super: {0})", super.toString());
    }
}

