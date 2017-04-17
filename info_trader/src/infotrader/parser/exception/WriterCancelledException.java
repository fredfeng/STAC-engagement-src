/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.exception;

import infotrader.parser.exception.InfoTraderWriterException;

public class WriterCancelledException
extends InfoTraderWriterException {
    private static final long serialVersionUID = -3295979672863551432L;

    public WriterCancelledException() {
    }

    public WriterCancelledException(String message) {
        super(message);
    }

    public WriterCancelledException(String message, Throwable cause) {
        super(message, cause);
    }

    public WriterCancelledException(Throwable cause) {
        super(cause);
    }
}

