/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.io.reader;

import infotrader.parser.exception.InfoTraderParserException;
import infotrader.parser.io.reader.AbstractEncodingSpecificReader;
import infotrader.parser.parser.InfoTraderParser;
import java.io.IOException;
import java.io.InputStream;

final class AsciiReader
extends AbstractEncodingSpecificReader {
    private boolean eof = false;
    private final StringBuilder lineBuffer = new StringBuilder();

    protected AsciiReader(InfoTraderParser parser, InputStream byteStream) {
        super(parser, byteStream);
    }

    @Override
    public String nextLine() throws IOException, InfoTraderParserException {
        String result = null;
        while (!this.eof) {
            int currChar = this.byteStream.read();
            if (currChar >= 0) {
                ++this.bytesRead;
            }
            if (currChar < 0) {
                this.eof = true;
                if (this.lineBuffer.length() <= 0) break;
                result = this.lineBuffer.toString();
                break;
            }
            if (currChar == 32 && this.lineBuffer.length() == 0) continue;
            if ((currChar == 13 || currChar == 10) && this.lineBuffer.length() > 0) {
                result = this.lineBuffer.toString();
                this.lineBuffer.setLength(0);
                break;
            }
            if (currChar < 128) {
                this.lineBuffer.append(Character.valueOf((char)currChar));
                continue;
            }
            throw new IOException("Extended characters not supported in ASCII: 0x" + Integer.toHexString(currChar));
        }
        return result;
    }

    @Override
    void cleanUp() throws IOException {
    }
}

