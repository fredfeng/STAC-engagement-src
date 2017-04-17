/*
 * Decompiled with CFR 0_121.
 */
package stac.communications;

public class PacketParserException
extends Exception {
    private static final long serialVersionUID = -6311931030088312913L;
    private final String message;

    public PacketParserException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}

