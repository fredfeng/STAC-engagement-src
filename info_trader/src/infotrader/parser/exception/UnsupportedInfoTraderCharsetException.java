/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.exception;

import infotrader.parser.exception.InfoTraderParserException;

public class UnsupportedInfoTraderCharsetException
extends InfoTraderParserException {
    private static final long serialVersionUID = -1209602510830929697L;

    public UnsupportedInfoTraderCharsetException() {
    }

    public UnsupportedInfoTraderCharsetException(String message) {
        super(message);
    }

    public UnsupportedInfoTraderCharsetException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedInfoTraderCharsetException(Throwable cause) {
        super(cause);
    }
}

