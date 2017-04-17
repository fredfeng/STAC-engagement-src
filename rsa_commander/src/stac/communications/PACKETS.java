/*
 * Decompiled with CFR 0_121.
 */
package stac.communications;

public enum PACKETS {
    HANDSHAKE_OPEN(34, 131108),
    REQUEST(77, -1),
    HANDSHAKE_CHALLENGE(4, 2048),
    REQUEST_MESSAGE(128, -1),
    REQUEST_MESSAGE_RELAY(128, -1),
    RETRIEVE_MESSAGES(0, -1),
    REQUEST_TERMINATE(128, -1);
    
    private final int max;
    private final int min;

    private PACKETS(int minSize, int maxSize) {
        this.max = maxSize;
        this.min = minSize;
    }

    public int maxSize() {
        return this.max;
    }

    public int minSize() {
        return this.min;
    }
}

