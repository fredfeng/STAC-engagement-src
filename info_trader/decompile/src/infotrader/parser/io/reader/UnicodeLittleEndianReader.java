/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.io.reader;

import infotrader.parser.exception.InfoTraderParserException;
import infotrader.parser.io.reader.AbstractEncodingSpecificReader;
import infotrader.parser.parser.InfoTraderParser;
import java.io.IOException;
import java.io.InputStream;

final class UnicodeLittleEndianReader
extends AbstractEncodingSpecificReader {
    private boolean eof = false;
    private final StringBuilder lineBuffer = new StringBuilder();

    public UnicodeLittleEndianReader(InfoTraderParser parser, InputStream byteStream) {
        super(parser, byteStream);
    }

    @Override
    public String nextLine() throws IOException, InfoTraderParserException {
        String result = null;
        boolean beginningOfFile = true;
        while (!this.eof) {
            int currChar2;
            int currChar1 = this.byteStream.read();
            if (currChar1 >= 0) {
                ++this.bytesRead;
            }
            if ((currChar2 = this.byteStream.read()) >= 0) {
                ++this.bytesRead;
            }
            if (currChar1 < 0 || currChar2 < 0) {
                if (this.lineBuffer.length() > 0) {
                    result = this.lineBuffer.toString();
                }
                this.eof = true;
                break;
            }
            if (beginningOfFile && currChar1 == 255 && currChar2 == 254) {
                beginningOfFile = false;
                this.lineBuffer.setLength(0);
                break;
            }
            beginningOfFile = false;
            if (currChar1 == 13 && currChar2 == 0 || currChar1 == 10 && currChar2 == 0) {
                if (this.lineBuffer.length() <= 0) continue;
                result = this.lineBuffer.toString();
                this.lineBuffer.setLength(0);
                break;
            }
            int unicodeChar = currChar2 << 8 | currChar1;
            this.lineBuffer.append(Character.valueOf((char)unicodeChar));
        }
        return result;
    }

    @Override
    void cleanUp() throws IOException {
    }
}

