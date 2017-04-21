/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.exception;

import infotrader.parser.exception.InfoTraderWriterException;

public class InfoTraderWriterVersionDataMismatchException
extends InfoTraderWriterException {
    private static final long serialVersionUID = -3893462493329487757L;

    public InfoTraderWriterVersionDataMismatchException() {
    }

    public InfoTraderWriterVersionDataMismatchException(String message) {
        super(message);
    }

    public InfoTraderWriterVersionDataMismatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public InfoTraderWriterVersionDataMismatchException(Throwable cause) {
        super(cause);
    }
}

