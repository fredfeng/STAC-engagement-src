/*
 * Decompiled with CFR 0_121.
 */
package stac.communications.handlers;

import java.io.IOException;
import java.math.BigInteger;
import stac.client.Remote;
import stac.communications.CONNECTION_PHASE;
import stac.communications.Communications;
import stac.communications.Handler;
import stac.communications.PACKETS;
import stac.communications.Packet;
import stac.communications.PacketBuffer;
import stac.communications.PacketParser;
import stac.communications.PacketParserException;
import stac.communications.Session;
import stac.communications.packets.HandshakePacket;
import stac.crypto.Key;
import stac.parser.OpenSSLRSAPEM;

public class HandshakeHandler
extends Handler {
    private static final Handler me = new HandshakeHandler();

    static Handler getInstance() {
        return me;
    }

    @Override
    protected boolean isPacketFormed(PacketBuffer packetBuffer) {
        try {
            if (packetBuffer.getOffset() > 0) {
                if (PACKETS.HANDSHAKE_OPEN.ordinal() == packetBuffer.getBuffer()[0]) {
                    if (packetBuffer.getOffset() >= this.getMinPacketSize()) {
                        HandshakePacket.Flags flags = new HandshakePacket.Flags();
                        flags.fromByte(packetBuffer.getBuffer()[33]);
                        if (flags.isRegistered() && packetBuffer.getOffset() == this.getMinPacketSize()) {
                            return true;
                        }
                        Integer pSize = OpenSSLRSAPEM.INTEGER.valueOfUnsigned(packetBuffer.getBuffer(), 34, 2).getInternal();
                        if (packetBuffer.getOffset() > 36 + pSize) {
                            Integer mSize = OpenSSLRSAPEM.INTEGER.valueOfUnsigned(packetBuffer.getBuffer(), 36 + pSize, 2).getInternal();
                            return packetBuffer.getOffset() == 38 + pSize + mSize;
                        }
                    }
                } else if (PACKETS.HANDSHAKE_CHALLENGE.ordinal() == packetBuffer.getBuffer()[0] && packetBuffer.getOffset() >= PACKETS.HANDSHAKE_CHALLENGE.minSize()) {
                    Integer len = OpenSSLRSAPEM.INTEGER.valueOfUnsigned(packetBuffer.getBuffer(), 1, 2).getInternal();
                    return packetBuffer.getOffset() == 3 + len;
                }
            }
            return false;
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            return false;
        }
    }

    @Override
    protected boolean isPacketStillOK(PacketBuffer packetBuffer) {
        return true;
    }

    @Override
    protected int getMaxPacketSize() {
        return PACKETS.HANDSHAKE_OPEN.maxSize();
    }

    @Override
    protected int getMinPacketSize() {
        return PACKETS.HANDSHAKE_OPEN.minSize();
    }

    @Override
    public Packet runPacketParser(PacketBuffer packetBuffer, Session session, PACKETS expecting) throws PacketParserException {
        if (expecting == PACKETS.HANDSHAKE_OPEN) {
            Packet packet = new HandshakePacket().getParser().parse(packetBuffer, null);
            packetBuffer.reset();
            return packet;
        }
        throw new RuntimeException("Handshake Handler: Something is not implemented");
    }

    @Override
    public CONNECTION_PHASE handlePacket(PacketBuffer packetBuffer, Session session) {
        try {
            if (session.isExpecting(PACKETS.HANDSHAKE_OPEN)) {
                HandshakePacket handshakePacket = (HandshakePacket)this.runPacketParser(packetBuffer, null, session.getExpecting());
                HandshakePacket.Flags flags = handshakePacket.getFlags();
                if (flags.isRegistered()) {
                    session.getCommunications().postError("Registered users are not supported");
                    return CONNECTION_PHASE.FAILED;
                }
                Key remoteKey = handshakePacket.getKey();
                if (remoteKey.getPem().getPublicExponent().compareTo(3) < 0 || remoteKey.getPem().getPublicExponent().compareTo(65537) > 0 || remoteKey.getPem().getModulus().getInternalBig().bitCount() < 0 || remoteKey.getPem().getModulus().getInternalBig().bitCount() > 512) {
                    session.getCommunications().postError("Invalid key size detected; Terminating Connection");
                    return CONNECTION_PHASE.FAILED;
                }
                session.attachUser(remoteKey);
                if (flags.isRegistered()) {
                    throw new RuntimeException("Registered users are not implemented");
                }
                session.setExpecting(PACKETS.REQUEST);
                HandshakePacket acceptPacket = new HandshakePacket();
                acceptPacket.getFlags().setHandshakeAccepted(true);
                acceptPacket.setKey(session.getServerKey());
                session.send(acceptPacket);
                return CONNECTION_PHASE.HANDSHAKE_ACCEPTED;
            }
            session.getCommunications().postError("Handshake handler invoked, but session is not expecting a handshake packet. Closing session.");
            return CONNECTION_PHASE.FAILED;
        }
        catch (IOException | PacketParserException e) {
            session.getCommunications().postError("A(n) " + e.getClass().getName() + " Exception has occurred. Closing session.");
            return CONNECTION_PHASE.FAILED;
        }
    }
}

