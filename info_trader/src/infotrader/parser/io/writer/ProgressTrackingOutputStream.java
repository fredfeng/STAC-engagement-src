/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.io.writer;

import java.io.IOException;
import java.io.OutputStream;

class ProgressTrackingOutputStream
extends OutputStream {
    int bytesWritten;
    private final OutputStream out;

    public ProgressTrackingOutputStream(OutputStream out) {
        this.out = out;
    }

    @Override
    public void write(byte[] b) throws IOException {
        this.out.write(b);
        this.bytesWritten += b.length;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        this.out.write(b, off, len);
        this.bytesWritten += len;
    }

    @Override
    public void write(int b) throws IOException {
        this.out.write(b);
        ++this.bytesWritten;
    }

    int getBytesWritten() {
        return this.bytesWritten;
    }
}

