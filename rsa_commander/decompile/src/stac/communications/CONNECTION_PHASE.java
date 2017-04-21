/*
 * Decompiled with CFR 0_121.
 */
package stac.communications;

public enum CONNECTION_PHASE {
    OPEN,
    HANDSHAKE_REQUEST,
    HANDSHAKE_ACCEPTED,
    HANDSHAKE_CHALLENGE,
    TERMINATING,
    FAILED;
    

    private CONNECTION_PHASE() {
    }
}

