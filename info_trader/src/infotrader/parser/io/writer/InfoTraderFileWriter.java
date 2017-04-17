/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.io.writer;

import infotrader.parser.exception.WriterCancelledException;
import infotrader.parser.io.writer.AbstractEncodingSpecificWriter;
import infotrader.parser.io.writer.AnselWriter;
import infotrader.parser.io.writer.AsciiWriter;
import infotrader.parser.io.writer.LineTerminator;
import infotrader.parser.io.writer.UnicodeBigEndianWriter;
import infotrader.parser.io.writer.UnicodeLittleEndianWriter;
import infotrader.parser.io.writer.Utf8Writer;
import infotrader.parser.writer.InfoTraderWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class InfoTraderFileWriter {
    private final InfoTraderWriter writer;
    AbstractEncodingSpecificWriter encodingSpecificWriter;
    private final List<String> InfoTraderLines;
    private LineTerminator terminator;
    private boolean useLittleEndianForUnicode = true;

    public InfoTraderFileWriter(InfoTraderWriter writer, List<String> InfoTraderLines) {
        this.writer = writer;
        this.InfoTraderLines = InfoTraderLines;
        this.setDefaultLineTerminator();
    }

    public LineTerminator getTerminator() {
        return this.terminator;
    }

    public boolean isUseLittleEndianForUnicode() {
        return this.useLittleEndianForUnicode;
    }

    public void setTerminator(LineTerminator terminator) {
        this.terminator = terminator;
    }

    public void setUseLittleEndianForUnicode(boolean useLittleEndianForUnicode) {
        this.useLittleEndianForUnicode = useLittleEndianForUnicode;
    }

    public void write(OutputStream out) throws IOException, WriterCancelledException {
        this.encodingSpecificWriter = new AnselWriter(this.writer);
        for (String line : this.InfoTraderLines) {
            if ("1 CHAR ASCII".equals(line)) {
                this.encodingSpecificWriter = new AsciiWriter(this.writer);
                break;
            }
            if ("1 CHAR UTF-8".equals(line)) {
                this.encodingSpecificWriter = new Utf8Writer(this.writer);
                break;
            }
            if (!"1 CHAR UNICODE".equals(line)) continue;
            if (this.useLittleEndianForUnicode) {
                this.encodingSpecificWriter = new UnicodeLittleEndianWriter(this.writer);
                break;
            }
            this.encodingSpecificWriter = new UnicodeBigEndianWriter(this.writer);
            break;
        }
        this.encodingSpecificWriter.InfoTraderLines = this.InfoTraderLines;
        this.encodingSpecificWriter.terminator = this.terminator;
        this.encodingSpecificWriter.write(out);
    }

    private void setDefaultLineTerminator() {
        this.terminator = LineTerminator.CRLF;
        String jvmLineTerm = System.getProperty("line.separator");
        if (Character.toString('\r').equals(jvmLineTerm)) {
            this.terminator = LineTerminator.CR_ONLY;
        } else if (Character.toString('\n').equals(jvmLineTerm)) {
            this.terminator = LineTerminator.LF_ONLY;
        } else if ((Character.toString('\r') + Character.toString('\n')).equals(jvmLineTerm)) {
            this.terminator = LineTerminator.CRLF;
        } else if ((Character.toString('\n') + Character.toString('\r')).equals(jvmLineTerm)) {
            this.terminator = LineTerminator.LFCR;
        }
    }
}

