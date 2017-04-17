/*
 * Decompiled with CFR 0_121.
 */
package stac.codecs;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class DecInputStream
extends InputStream {
    private final InputStream is;
    private int bits = 0;
    private int nextin = 18;
    private int nextout = -8;
    private boolean eof = false;
    private boolean closed = false;
    private static final char[] toBase64 = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    private static final int[] base64 = new int[256];
    private byte[] sbBuf = new byte[1];

    public DecInputStream(InputStream is) {
        this.is = is;
    }

    @Override
    public int read() throws IOException {
        return this.read(this.sbBuf, 0, 1) == -1 ? -1 : this.sbBuf[0] & 255;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (this.closed) {
            throw new IOException("Stream is closed");
        }
        if (this.eof && this.nextout < 0) {
            return -1;
        }
        if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        }
        int oldOff = off;
        if (this.nextout >= 0) {
            do {
                if (len == 0) {
                    return off - oldOff;
                }
                b[off++] = (byte)(this.bits >> this.nextout);
                --len;
                this.nextout -= 8;
            } while (this.nextout >= 0);
            this.bits = 0;
        }
        while (len > 0) {
            int v = this.is.read();
            if (v == -1) {
                this.eof = true;
                if (this.nextin != 18) {
                    if (this.nextin == 12) {
                        throw new IOException("Base64 stream has one un-decoded dangling byte.");
                    }
                    b[off++] = (byte)(this.bits >> 16);
                    --len;
                    if (this.nextin == 0) {
                        if (len == 0) {
                            this.bits >>= 8;
                            this.nextout = 0;
                        } else {
                            b[off++] = (byte)(this.bits >> 8);
                        }
                    }
                }
                if (off == oldOff) {
                    return -1;
                }
                return off - oldOff;
            }
            if (v == 61) {
                if (this.nextin == 18 || this.nextin == 12 || this.nextin == 6 && this.is.read() != 61) {
                    throw new IOException("Illegal base64 ending sequence:" + this.nextin);
                }
                b[off++] = (byte)(this.bits >> 16);
                --len;
                if (this.nextin == 0) {
                    if (len == 0) {
                        this.bits >>= 8;
                        this.nextout = 0;
                    } else {
                        b[off++] = (byte)(this.bits >> 8);
                    }
                }
                this.eof = true;
                break;
            }
            if ((v = base64[v]) == -1) {
                throw new IOException("Illegal base64 character " + Integer.toString(v, 16));
            }
            this.bits |= v << this.nextin;
            if (this.nextin == 0) {
                this.nextin = 18;
                this.nextout = 16;
                while (this.nextout >= 0) {
                    b[off++] = (byte)(this.bits >> this.nextout);
                    this.nextout -= 8;
                    if (--len != 0 || this.nextout < 0) continue;
                    return off - oldOff;
                }
                this.bits = 0;
                continue;
            }
            this.nextin -= 6;
        }
        return off - oldOff;
    }

    @Override
    public int available() throws IOException {
        if (this.closed) {
            throw new IOException("Stream is closed");
        }
        return this.is.available();
    }

    @Override
    public void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            this.is.close();
        }
    }

    static {
        Arrays.fill(base64, -1);
        int i = 0;
        while (i < toBase64.length) {
            DecInputStream.base64[DecInputStream.toBase64[i]] = i++;
        }
        DecInputStream.base64[61] = -2;
    }
}

