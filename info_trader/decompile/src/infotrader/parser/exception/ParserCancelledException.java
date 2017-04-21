/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.exception;

import infotrader.parser.exception.InfoTraderParserException;

public class ParserCancelledException
extends InfoTraderParserException {
    private static final long serialVersionUID = -3295979672863551432L;

    public ParserCancelledException() {
    }

    public ParserCancelledException(String message) {
        super(message);
    }

    public ParserCancelledException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParserCancelledException(Throwable cause) {
        super(cause);
    }
}

