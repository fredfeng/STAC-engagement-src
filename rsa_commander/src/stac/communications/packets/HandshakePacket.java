/*
 * Decompiled with CFR 0_121.
 */
package stac.communications.packets;

import stac.communications.Packet;
import stac.communications.PacketParser;
import stac.communications.packets.HandshakePacketParser;
import stac.crypto.Key;
import stac.crypto.PublicKey;

public class HandshakePacket
extends Packet {
    private Key key = new PublicKey();
    private Flags flags = new Flags();

    @Override
    public PacketParser getParser() {
        return new HandshakePacketParser(this);
    }

    public Key getKey() {
        return this.key;
    }

    public Key setKey(Key key) {
        this.key = key;
        return this.key;
    }

    public Flags getFlags() {
        return this.flags;
    }

    public void setFlags(Flags flags) {
        this.flags = flags;
    }

    public static class Flags {
        private boolean registered;
        private boolean shouldStore;
        private boolean requestsReturnService;
        private boolean handshakeRequest;
        private boolean handshakeAccepted;
        private boolean handshakeStalled;
        private boolean handshakeFailed;

        public boolean isRegistered() {
            return this.registered;
        }

        public Flags setRegistered(boolean registered) {
            this.registered = registered;
            return this;
        }

        public boolean isShouldStore() {
            return this.shouldStore;
        }

        public Flags setShouldStore(boolean shouldStore) {
            this.shouldStore = shouldStore;
            return this;
        }

        public boolean isRequestsReturnService() {
            return this.requestsReturnService;
        }

        public Flags setRequestsReturnService(boolean requestsReturnService) {
            this.requestsReturnService = requestsReturnService;
            return this;
        }

        public boolean isHandshakeRequest() {
            return this.handshakeRequest;
        }

        public Flags setHandshakeRequest(boolean handshakeRequest) {
            this.handshakeRequest = handshakeRequest;
            return this;
        }

        public boolean isHandshakeAccepted() {
            return this.handshakeAccepted;
        }

        public Flags setHandshakeAccepted(boolean handshakeAccepted) {
            this.handshakeAccepted = handshakeAccepted;
            return this;
        }

        public boolean isHandshakeStalled() {
            return this.handshakeStalled;
        }

        public Flags setHandshakeStalled(boolean handshakeStalled) {
            this.handshakeStalled = handshakeStalled;
            return this;
        }

        public boolean isHandshakeFailed() {
            return this.handshakeFailed;
        }

        public Flags setHandshakeFailed(boolean handshakeFailed) {
            this.handshakeFailed = handshakeFailed;
            return this;
        }

        public byte toByte() {
            return (byte)((this.isRegistered() ? 1 : 0) | (this.isShouldStore() ? 1 : 0) << 1 | (this.isRequestsReturnService() ? 1 : 0) << 2 | (this.isHandshakeRequest() ? 1 : 0) << 3 | (this.isHandshakeAccepted() ? 1 : 0) << 4 | (this.isHandshakeStalled() ? 1 : 0) << 5 | (this.isHandshakeFailed() ? 1 : 0) << 6);
        }

        public void fromByte(byte b) {
            this.setRegistered((b & 1) != 0);
            this.setShouldStore((b & 2) != 0);
            this.setRequestsReturnService((b & 4) != 0);
            this.setHandshakeRequest((b & 8) != 0);
            this.setHandshakeAccepted((b & 16) != 0);
            this.setHandshakeStalled((b & 32) != 0);
            this.setHandshakeFailed((b & 64) != 0);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            Flags flags = (Flags)o;
            if (this.isRegistered() != flags.isRegistered()) {
                return false;
            }
            if (this.isShouldStore() != flags.isShouldStore()) {
                return false;
            }
            if (this.isRequestsReturnService() != flags.isRequestsReturnService()) {
                return false;
            }
            if (this.isHandshakeRequest() != flags.isHandshakeRequest()) {
                return false;
            }
            if (this.isHandshakeAccepted() != flags.isHandshakeAccepted()) {
                return false;
            }
            if (this.isHandshakeStalled() != flags.isHandshakeStalled()) {
                return false;
            }
            return this.isHandshakeFailed() == flags.isHandshakeFailed();
        }

        public int hashCode() {
            int result = this.isRegistered() ? 1 : 0;
            result = 31 * result + (this.isShouldStore() ? 1 : 0);
            result = 31 * result + (this.isRequestsReturnService() ? 1 : 0);
            result = 31 * result + (this.isHandshakeRequest() ? 1 : 0);
            result = 31 * result + (this.isHandshakeAccepted() ? 1 : 0);
            result = 31 * result + (this.isHandshakeStalled() ? 1 : 0);
            result = 31 * result + (this.isHandshakeFailed() ? 1 : 0);
            return result;
        }
    }

}

