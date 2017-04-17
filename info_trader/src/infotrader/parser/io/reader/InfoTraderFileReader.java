/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.io.reader;

import infotrader.parser.exception.InfoTraderParserException;
import infotrader.parser.exception.ParserCancelledException;
import infotrader.parser.exception.UnsupportedInfoTraderCharsetException;
import infotrader.parser.io.event.FileProgressEvent;
import infotrader.parser.io.reader.AbstractEncodingSpecificReader;
import infotrader.parser.io.reader.AnselReader;
import infotrader.parser.io.reader.AsciiReader;
import infotrader.parser.io.reader.UnicodeBigEndianReader;
import infotrader.parser.io.reader.UnicodeLittleEndianReader;
import infotrader.parser.io.reader.Utf8Reader;
import infotrader.parser.parser.InfoTraderParser;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public final class InfoTraderFileReader {
    private static final int FIRST_CHUNK_SIZE = 16384;
    private static final long UTF8_BYTE_ORDER_MARKER = 15711167;
    final byte[] firstChunk = new byte[16384];
    private final BufferedInputStream byteStream;
    private final AbstractEncodingSpecificReader encodingSpecificReader;
    private final InfoTraderParser parser;
    private int linesProcessed = 0;

    public InfoTraderFileReader(InfoTraderParser parser, BufferedInputStream bufferedInputStream) throws IOException, UnsupportedInfoTraderCharsetException {
        this.parser = parser;
        this.byteStream = bufferedInputStream;
        this.saveFirstChunk();
        this.encodingSpecificReader = this.getEncodingSpecificReader();
    }

    public String nextLine() throws IOException, InfoTraderParserException {
        if (this.parser.isCancelled()) {
            throw new ParserCancelledException("File load is cancelled");
        }
        String result = this.encodingSpecificReader.nextLine();
        ++this.linesProcessed;
        if (this.linesProcessed % this.parser.getReadNotificationRate() == 0 || result == null) {
            this.parser.notifyFileObservers(new FileProgressEvent(this, this.linesProcessed, this.encodingSpecificReader.bytesRead, result == null));
        }
        return result;
    }

    long firstNBytes(int n) {
        long result = 0;
        for (int i = 0; i < n; ++i) {
            result = (result << 8) + (long)(this.firstChunk[i] & 255);
        }
        return result;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private AbstractEncodingSpecificReader anselAsciiOrUtf8() throws IOException, UnsupportedInfoTraderCharsetException {
        BufferedReader r = null;
        try {
            r = new BufferedReader(new InputStreamReader((InputStream)new ByteArrayInputStream(this.firstChunk), "UTF8"));
            String s = r.readLine();
            for (int lineCount = 1; lineCount < 1000 && s != null; ++lineCount) {
                if (s.startsWith("1 CHAR ")) {
                    String e = s.substring("1 CHAR ".length());
                    if ("ANSEL".equalsIgnoreCase(e)) {
                        AnselReader anselReader = new AnselReader(this.parser, this.byteStream);
                        return anselReader;
                    }
                    if ("UTF-8".equalsIgnoreCase(e)) {
                        Utf8Reader utf8Reader = new Utf8Reader(this.parser, this.byteStream);
                        return utf8Reader;
                    }
                    if ("ASCII".equalsIgnoreCase(e)) {
                        AsciiReader asciiReader = new AsciiReader(this.parser, this.byteStream);
                        return asciiReader;
                    }
                    if ("ANSI".equalsIgnoreCase(e)) {
                        Utf8Reader utf8Reader = new Utf8Reader(this.parser, this.byteStream);
                        return utf8Reader;
                    }
                    throw new UnsupportedInfoTraderCharsetException("Specified charset " + e + " is not a supported charset encoding for InfoTrader");
                }
                s = r.readLine();
            }
        }
        finally {
            if (r != null) {
                r.close();
            }
        }
        return new AnselReader(this.parser, this.byteStream);
    }

    private AbstractEncodingSpecificReader getEncodingSpecificReader() throws IOException, UnsupportedInfoTraderCharsetException {
        boolean lfcr;
        if (this.firstNBytes(3) == 15711167) {
            Utf8Reader result = new Utf8Reader(this.parser, this.byteStream);
            result.setByteOrderMarkerRead(true);
            return result;
        }
        if (this.firstNBytes(2) == 65534 || this.firstNBytes(2) == 12288 || this.firstNBytes(2) == 3328 || this.firstNBytes(2) == 2560) {
            return new UnicodeLittleEndianReader(this.parser, this.byteStream);
        }
        if (this.firstNBytes(2) == 65279 || this.firstNBytes(2) == 48 || this.firstNBytes(2) == 13 || this.firstNBytes(2) == 10) {
            return new UnicodeBigEndianReader(this.parser, this.byteStream);
        }
        boolean zeroFollowedBySpace = this.firstNBytes(2) == 12320;
        boolean blankLineFollowedByZero = this.firstNBytes(2) == 2608 || this.firstNBytes(2) == 3376;
        boolean twoBlankLines = this.firstNBytes(2) == 3341 || this.firstNBytes(2) == 2570;
        boolean crlf = this.firstNBytes(2) == 3338;
        boolean bl = lfcr = this.firstNBytes(2) == 2573;
        if (zeroFollowedBySpace || blankLineFollowedByZero || twoBlankLines || crlf || lfcr) {
            return this.anselAsciiOrUtf8();
        }
        throw new IOException("Does not appear to be a valid InfoTrader file - doesn't begin with a zero or newline in any supported encoding, and does not begin with a BOM marker for UTF-8 encoding. ");
    }

    private void saveFirstChunk() throws IOException {
        this.byteStream.mark(16384);
        int read = this.byteStream.read(this.firstChunk);
        if (read < 0) {
            throw new IOException("Unable to read bytes off stream");
        }
        this.byteStream.reset();
    }
}

