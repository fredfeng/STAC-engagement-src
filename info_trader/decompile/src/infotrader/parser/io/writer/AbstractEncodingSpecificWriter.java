/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.io.writer;

import infotrader.parser.exception.WriterCancelledException;
import infotrader.parser.io.event.FileProgressEvent;
import infotrader.parser.io.writer.LineTerminator;
import infotrader.parser.writer.InfoTraderWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

abstract class AbstractEncodingSpecificWriter {
    protected List<String> InfoTraderLines;
    protected LineTerminator terminator;
    protected InfoTraderWriter writer;
    protected int bytesWritten;
    protected int notifyAfterThisManyLines = 0;

    public AbstractEncodingSpecificWriter(InfoTraderWriter writer) {
        this.writer = writer;
    }

    public void write(OutputStream out) throws IOException, WriterCancelledException {
        int lineCount = 0;
        for (String line : this.InfoTraderLines) {
            if (lineCount >= this.notifyAfterThisManyLines) {
                this.writer.notifyFileObservers(new FileProgressEvent(this, lineCount, this.bytesWritten, false));
                this.notifyAfterThisManyLines += this.writer.getFileNotificationRate();
            }
            this.writeLine(out, line);
            ++lineCount;
        }
        this.writer.notifyFileObservers(new FileProgressEvent(this, lineCount, this.bytesWritten, true));
    }

    protected abstract void writeLine(OutputStream var1, String var2) throws IOException, WriterCancelledException;

    protected abstract void writeLineTerminator(OutputStream var1) throws IOException, WriterCancelledException;
}

