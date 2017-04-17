/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.io.writer;

import infotrader.parser.exception.WriterCancelledException;
import infotrader.parser.io.encoding.AnselHandler;
import infotrader.parser.io.writer.AbstractEncodingSpecificWriter;
import infotrader.parser.io.writer.LineTerminator;
import infotrader.parser.writer.InfoTraderWriter;
import java.io.IOException;
import java.io.OutputStream;

class AnselWriter
extends AbstractEncodingSpecificWriter {
    private final AnselHandler anselHandler = new AnselHandler();

    public AnselWriter(InfoTraderWriter writer) {
        super(writer);
    }

    @Override
    protected void writeLine(OutputStream out, String line) throws IOException, WriterCancelledException {
        String anselLine = this.anselHandler.toAnsel(line);
        for (int i = 0; i < anselLine.length(); ++i) {
            char c = anselLine.charAt(i);
            out.write(c);
            ++this.bytesWritten;
        }
        this.writeLineTerminator(out);
    }

    @Override
    protected void writeLineTerminator(OutputStream out) throws IOException, WriterCancelledException {
        switch (this.terminator) {
            case CR_ONLY: {
                out.write(13);
                ++this.bytesWritten;
                break;
            }
            case LF_ONLY: {
                out.write(10);
                ++this.bytesWritten;
                break;
            }
            case LFCR: {
                out.write(10);
                out.write(13);
                this.bytesWritten += 2;
                break;
            }
            case CRLF: {
                out.write(13);
                out.write(10);
                this.bytesWritten += 2;
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

