/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.io.reader;

import infotrader.parser.exception.InfoTraderParserException;
import infotrader.parser.io.reader.AbstractEncodingSpecificReader;
import infotrader.parser.io.reader.ProgressTrackingInputStream;
import infotrader.parser.parser.InfoTraderParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

final class Utf8Reader
extends AbstractEncodingSpecificReader {
    private boolean byteOrderMarkerRead = false;
    private final InputStreamReader inputStreamReader;
    private final BufferedReader bufferedReader;
    private ProgressTrackingInputStream inputStream;

    Utf8Reader(InfoTraderParser parser, InputStream byteStream) throws IOException {
        super(parser, byteStream);
        try {
            this.inputStream = new ProgressTrackingInputStream(byteStream);
            this.inputStreamReader = new InputStreamReader((InputStream)this.inputStream, "UTF8");
            if (this.byteOrderMarkerRead) {
                this.inputStreamReader.read();
                this.bytesRead = this.inputStream.getBytesRead();
            }
            this.bufferedReader = new BufferedReader(this.inputStreamReader);
        }
        catch (IOException e) {
            this.closeReaders();
            throw e;
        }
    }

    @Override
    public String nextLine() throws IOException, InfoTraderParserException {
        String result = null;
        String s = this.bufferedReader.readLine();
        this.bytesRead = this.inputStream.getBytesRead();
        if (s != null && s.length() > 0 && s.charAt(0) == '\ufeff') {
            s = s.substring(1);
        }
        while (s != null) {
            if (s.length() != 0) {
                result = s;
                break;
            }
            s = this.bufferedReader.readLine();
            this.bytesRead = this.inputStream.getBytesRead();
        }
        this.bytesRead = this.inputStream.getBytesRead();
        return result;
    }

    public void setByteOrderMarkerRead(boolean wasByteOrderMarkerRead) {
        this.byteOrderMarkerRead = wasByteOrderMarkerRead;
    }

    @Override
    void cleanUp() throws IOException {
        this.closeReaders();
    }

    private void closeReaders() throws IOException {
        if (this.bufferedReader != null) {
            this.bufferedReader.close();
        }
        if (this.inputStreamReader != null) {
            this.inputStreamReader.close();
        }
    }
}

