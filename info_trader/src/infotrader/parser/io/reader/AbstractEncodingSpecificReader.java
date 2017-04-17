/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.io.reader;

import infotrader.parser.exception.InfoTraderParserException;
import infotrader.parser.parser.InfoTraderParser;
import java.io.IOException;
import java.io.InputStream;

abstract class AbstractEncodingSpecificReader {
    protected final InputStream byteStream;
    protected int linesRead = 0;
    protected int bytesRead = 0;
    protected final InfoTraderParser parser;

    AbstractEncodingSpecificReader(InfoTraderParser parser, InputStream byteStream) {
        this.parser = parser;
        this.byteStream = byteStream;
    }

    public abstract String nextLine() throws IOException, InfoTraderParserException;

    abstract void cleanUp() throws IOException;
}

