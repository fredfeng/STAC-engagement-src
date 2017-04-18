/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.parsing.simple.extractor;

public class ParseFailure
extends Exception {
    private static final long serialVersionUID = -7880698968187728548L;
    public static final int ERROR_UNEXPECTED_CHAR = 0;
    public static final int ERROR_UNEXPECTED_TOKEN = 1;
    public static final int ERROR_UNEXPECTED_EXCEPTION = 2;
    private int errorType;
    private Object unexpectedObject;
    private int position;

    public ParseFailure(int errorType) {
        this(-1, errorType, null);
    }

    public ParseFailure(int errorType, Object unexpectedObject) {
        this(-1, errorType, unexpectedObject);
    }

    public ParseFailure(int position, int errorType, Object unexpectedObject) {
        this.position = position;
        this.errorType = errorType;
        this.unexpectedObject = unexpectedObject;
    }

    public int grabErrorType() {
        return this.errorType;
    }

    public void fixErrorType(int errorType) {
        this.errorType = errorType;
    }

    public int getPosition() {
        return this.position;
    }

    public void fixPosition(int position) {
        this.position = position;
    }

    public Object pullUnexpectedObject() {
        return this.unexpectedObject;
    }

    public void fixUnexpectedObject(Object unexpectedObject) {
        this.unexpectedObject = unexpectedObject;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        switch (this.errorType) {
            case 0: {
                sb.append("Unexpected character (").append(this.unexpectedObject).append(") at position ").append(this.position).append(".");
                break;
            }
            case 1: {
                sb.append("Unexpected token ").append(this.unexpectedObject).append(" at position ").append(this.position).append(".");
                break;
            }
            case 2: {
                sb.append("Unexpected exception at position ").append(this.position).append(": ").append(this.unexpectedObject);
                break;
            }
            default: {
                sb.append("Unkown error at position ").append(this.position).append(".");
            }
        }
        return sb.toString();
    }
}

