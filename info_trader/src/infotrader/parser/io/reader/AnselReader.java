/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.io.reader;

import infotrader.parser.exception.InfoTraderParserException;
import infotrader.parser.io.encoding.AnselHandler;
import infotrader.parser.io.reader.AbstractEncodingSpecificReader;
import infotrader.parser.parser.InfoTraderParser;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

final class AnselReader
extends AbstractEncodingSpecificReader {
    private static final char ANSEL_DIACRITICS_BEGIN_AT = '\u00e0';
    private final AnselHandler anselHandler = new AnselHandler();
    private int lineBufferIdx = 0;
    private int currChar = -1;
    private boolean eof = false;
    private final char[] lineBuffer = new char[256];
    private int oneCharBack = -1;
    private int holdingBinIdx = 0;
    private final char[] holdingBin = new char[2];

    protected AnselReader(InfoTraderParser parser, InputStream byteStream) {
        super(parser, byteStream);
    }

    @Override
    public String nextLine() throws IOException, InfoTraderParserException {
        if (this.eof) {
            return null;
        }
        String result = null;
        while (!this.eof) {
            int twoCharsBack = this.oneCharBack;
            this.oneCharBack = this.currChar;
            this.currChar = this.byteStream.read();
            if (this.currChar >= 0) {
                ++this.bytesRead;
            }
            if (this.currChar < 0) {
                result = this.getThisLine();
                this.eof = true;
                break;
            }
            if (this.currChar == 32 && this.lineBufferIdx == 0) continue;
            if (this.currChar == 13 || this.currChar == 10) {
                if (this.oneCharBack >= 224) {
                    if (twoCharsBack >= 224) {
                        this.holdingBin[this.holdingBinIdx++] = (char)twoCharsBack;
                        twoCharsBack = -1;
                    }
                    this.holdingBin[this.holdingBinIdx++] = (char)this.oneCharBack;
                    this.oneCharBack = -1;
                }
                if (this.lineBufferIdx <= 0) continue;
                result = this.getThisLine();
                break;
            }
            if (this.holdingBinIdx > 0 && this.isStartOfConcLine()) {
                this.lineBuffer[this.lineBufferIdx++] = this.holdingBin[0];
                if (this.holdingBinIdx > 1) {
                    this.lineBuffer[this.lineBufferIdx++] = this.holdingBin[1];
                }
                this.holdingBinIdx = 0;
                this.holdingBin[0] = 32;
                this.holdingBin[1] = 32;
            }
            if (this.lineBufferIdx >= 250 && this.currChar < 224) {
                result = this.getThisLine();
                this.insertSyntheticConcTag(result);
                break;
            }
            this.lineBuffer[this.lineBufferIdx++] = (char)this.currChar;
        }
        return result;
    }

    @Override
    void cleanUp() throws IOException {
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private int getLevelFromLine(String line) throws InfoTraderParserException {
        int level = -1;
        char[] lineChars = line.toCharArray();
        if (!Character.isDigit(lineChars[0])) throw new InfoTraderParserException("Line " + this.linesRead + " does not begin with a 1 or 2 digit number. Can't split automatically.");
        if (Character.isDigit(lineChars[1])) {
            if (lineChars[2] != ' ') throw new InfoTraderParserException("Line " + this.linesRead + " does not begin with a 1 or 2 digit number. " + "Can't split automatically.");
            return Character.getNumericValue(lineChars[0]) * 10 + Character.getNumericValue(lineChars[1]);
        }
        if (lineChars[1] != ' ') throw new InfoTraderParserException("Line " + this.linesRead + " does not begin with a 1 or 2 digit number. " + "Can't split automatically.");
        return Character.getNumericValue(lineChars[0]);
    }

    private String getThisLine() {
        String result = null;
        if (this.lineBufferIdx > 0) {
            String s = new String(this.lineBuffer).substring(0, this.lineBufferIdx - this.holdingBinIdx);
            result = this.anselHandler.toUtf16(s);
        }
        ++this.linesRead;
        Arrays.fill(this.lineBuffer, ' ');
        this.lineBufferIdx = 0;
        return result;
    }

    private void insertSyntheticConcTag(String previousLine) throws InfoTraderParserException {
        int level = this.getLevelFromLine(previousLine);
        this.parser.getWarnings().add("Line " + this.linesRead + " exceeds max length - introducing synthetic CONC tag to split line");
        if (++level > 9) {
            this.lineBuffer[this.lineBufferIdx++] = Character.forDigit(level / 10, 10);
            this.lineBuffer[this.lineBufferIdx++] = Character.forDigit(level % 10, 10);
        } else {
            this.lineBuffer[this.lineBufferIdx++] = Character.forDigit(level, 10);
        }
        this.lineBuffer[this.lineBufferIdx++] = 32;
        this.lineBuffer[this.lineBufferIdx++] = 67;
        this.lineBuffer[this.lineBufferIdx++] = 79;
        this.lineBuffer[this.lineBufferIdx++] = 78;
        this.lineBuffer[this.lineBufferIdx++] = 67;
        this.lineBuffer[this.lineBufferIdx++] = 32;
        this.lineBuffer[this.lineBufferIdx++] = (char)this.currChar;
    }

    private boolean isStartOfConcLine() {
        return this.lineBufferIdx >= 7 && Character.isDigit(this.lineBuffer[this.lineBufferIdx - 7]) && this.lineBuffer[this.lineBufferIdx - 6] == ' ' && this.lineBuffer[this.lineBufferIdx - 5] == 'C' && this.lineBuffer[this.lineBufferIdx - 4] == 'O' && this.lineBuffer[this.lineBufferIdx - 3] == 'N' && this.lineBuffer[this.lineBufferIdx - 2] == 'C' && this.lineBuffer[this.lineBufferIdx - 1] == ' ';
    }
}

