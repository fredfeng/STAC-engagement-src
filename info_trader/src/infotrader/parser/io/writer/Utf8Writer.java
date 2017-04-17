/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.io.writer;

import infotrader.parser.exception.WriterCancelledException;
import infotrader.parser.io.event.FileProgressEvent;
import infotrader.parser.io.writer.AbstractEncodingSpecificWriter;
import infotrader.parser.io.writer.LineTerminator;
import infotrader.parser.io.writer.ProgressTrackingOutputStream;
import infotrader.parser.writer.InfoTraderWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;

class Utf8Writer
extends AbstractEncodingSpecificWriter {
    private int lineCount;

    public Utf8Writer(InfoTraderWriter writer) {
        super(writer);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void write(OutputStream out) throws IOException, WriterCancelledException {
        String lineTerminatorString = null;
        switch (this.terminator) {
            case CR_ONLY: {
                lineTerminatorString = "\r";
                break;
            }
            case LF_ONLY: {
                lineTerminatorString = "\n";
                break;
            }
            case LFCR: {
                lineTerminatorString = "\n\r";
                break;
            }
            case CRLF: {
                lineTerminatorString = "\r\n";
                break;
            }
            default: {
                throw new IllegalStateException("Terminator selection of " + (Object)((Object)this.terminator) + " is an unrecognized value");
            }
        }
        ProgressTrackingOutputStream outputStream = new ProgressTrackingOutputStream(out);
        OutputStreamWriter osw = new OutputStreamWriter((OutputStream)outputStream, Charset.forName("UTF-8"));
        try {
            for (String line : this.InfoTraderLines) {
                osw.write(line);
                this.bytesWritten = outputStream.bytesWritten;
                osw.write(lineTerminatorString);
                this.bytesWritten = outputStream.bytesWritten;
                ++this.lineCount;
                if (this.lineCount >= this.notifyAfterThisManyLines) {
                    this.writer.notifyFileObservers(new FileProgressEvent(this, this.lineCount, this.bytesWritten, false));
                    this.notifyAfterThisManyLines += this.writer.getFileNotificationRate();
                }
                if (!this.writer.isCancelled()) continue;
                throw new WriterCancelledException("Construction and writing of InfoTrader cancelled");
            }
        }
        finally {
            osw.flush();
            this.bytesWritten = outputStream.bytesWritten;
            osw.close();
        }
    }

    @Override
    protected void writeLine(OutputStream out, String line) throws IOException {
        throw new UnsupportedOperationException(this.getClass().getName() + " does not use the abstract writeLine method");
    }

    @Override
    protected void writeLineTerminator(OutputStream out) throws IOException {
        throw new UnsupportedOperationException(this.getClass().getName() + " does not use the abstract writeLineTerminator method");
    }

}

