/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.io.writer;

import infotrader.parser.exception.WriterCancelledException;
import infotrader.parser.io.writer.AbstractEncodingSpecificWriter;
import infotrader.parser.io.writer.LineTerminator;
import infotrader.parser.writer.InfoTraderWriter;
import java.io.IOException;
import java.io.OutputStream;

class UnicodeLittleEndianWriter
extends AbstractEncodingSpecificWriter {
    public UnicodeLittleEndianWriter(InfoTraderWriter writer) {
        super(writer);
    }

    @Override
    protected void writeLine(OutputStream out, String line) throws IOException, WriterCancelledException {
        for (int i = 0; i < line.length(); ++i) {
            char c = line.charAt(i);
            out.write(c & 255);
            out.write(c >> 8);
            this.bytesWritten += 2;
        }
        this.writeLineTerminator(out);
    }

    @Override
    protected void writeLineTerminator(OutputStream out) throws IOException, WriterCancelledException {
        switch (this.terminator) {
            case CR_ONLY: {
                out.write(13);
                out.write(0);
                this.bytesWritten += 2;
                break;
            }
            case LF_ONLY: {
                out.write(10);
                out.write(0);
                this.bytesWritten += 2;
                break;
            }
            case LFCR: {
                out.write(10);
                out.write(0);
                out.write(13);
                out.write(0);
                this.bytesWritten += 4;
                break;
            }
            case CRLF: {
                out.write(13);
                out.write(0);
                out.write(10);
                out.write(0);
                this.bytesWritten += 4;
                break;
            }
            default: {
                throw new IllegalStateException("Terminator selection of " + (Object)((Object)this.terminator) + " is an unrecognized value");
            }
        }
        if (this.writer.isCancelled()) {
            throw new WriterCancelledException("Construction and writing of InfoTrader cancelled");
        }
    }

}

