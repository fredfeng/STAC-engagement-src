/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.exception;

public class UnsupportedVersionException
extends Exception {
    private static final long serialVersionUID = -7778881391248235659L;

    public UnsupportedVersionException() {
    }

    public UnsupportedVersionException(String message) {
        super(message);
    }

    public UnsupportedVersionException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedVersionException(Throwable cause) {
        super(cause);
    }
}

