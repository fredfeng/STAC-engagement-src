/*
 * Decompiled with CFR 0_121.
 */
package stac.crypto;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.text.MessageFormat;
import stac.crypto.Key;
import stac.parser.OpenSSLRSAPEM;

public class PublicKey
extends Key {
    public PublicKey(File file) throws IOException {
        OpenSSLRSAPEM finalPem;
        OpenSSLRSAPEM pem = new OpenSSLRSAPEM(file);
        if (pem.getType() != OpenSSLRSAPEM.DER_TYPE.PUBLIC_KEY) {
            System.err.println("Warning: Invalid public key file '" + file.getPath() + "'. Auto-converting it.");
            finalPem = new OpenSSLRSAPEM(pem.getPublicExponent(), pem.getModulus());
        } else {
            finalPem = pem;
        }
        this.setPem(finalPem);
    }

    public PublicKey(OpenSSLRSAPEM pem) {
        OpenSSLRSAPEM finalPem;
        if (pem.getModulus().getInternalBig().bitCount() > 512) {
            throw new RuntimeException("This key is incorrectly sized");
        }
        if (pem.getType() != OpenSSLRSAPEM.DER_TYPE.PUBLIC_KEY) {
            System.err.println("Warning: Invalid public key. Auto-converting it.");
            finalPem = new OpenSSLRSAPEM(pem.getPublicExponent(), pem.getModulus());
        } else {
            finalPem = pem;
        }
        this.setPem(finalPem);
    }

    public PublicKey(byte[] fingerprint) {
        super(fingerprint);
    }

    public PublicKey() {
    }

    @Override
    public String toString() {
        return MessageFormat.format("PublicKey(super: {0})", super.toString());
    }
}

