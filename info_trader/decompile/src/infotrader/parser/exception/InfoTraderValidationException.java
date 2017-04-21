/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.exception;

public class InfoTraderValidationException
extends RuntimeException {
    private static final long serialVersionUID = -5656983786362148078L;

    public InfoTraderValidationException() {
    }

    public InfoTraderValidationException(String message) {
        super(message);
    }

    public InfoTraderValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InfoTraderValidationException(Throwable cause) {
        super(cause);
    }
}

