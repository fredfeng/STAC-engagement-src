/*
 * Decompiled with CFR 0_121.
 */
package stac.crypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import stac.parser.OpenSSLRSAPEM;

public abstract class SymmetricCipher {
    public abstract void encrypt_ctr(OpenSSLRSAPEM.INTEGER var1, byte[] var2, ByteArrayInputStream var3, ByteArrayOutputStream var4) throws IOException;
}

