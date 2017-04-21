/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.io.reader;

import java.io.IOException;
import java.io.InputStream;

class ProgressTrackingInputStream
extends InputStream {
    private final InputStream in;
    private int bytesRead = 0;

    ProgressTrackingInputStream(InputStream byteStream) {
        this.in = byteStream;
    }

    @Override
    public int read() throws IOException {
        int r = this.in.read();
        if (r >= 0) {
            ++this.bytesRead;
        }
        return r;
    }

    @Override
    public int read(byte[] b) throws IOException {
        int r = this.in.read(b);
        if (r > 0) {
            this.bytesRead += r;
        }
        return r;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int r = this.in.read(b, off, len);
        if (r > 0) {
            this.bytesRead += r;
        }
        return r;
    }

    int getBytesRead() {
        return this.bytesRead;
    }
}

