/*
 * Decompiled with CFR 0_121.
 */
package stac.communications.handlers;

import java.io.IOException;
import java.io.PrintStream;
import stac.client.Remote;
import stac.client.Screen;
import stac.communications.CONNECTION_PHASE;
import stac.communications.Communications;
import stac.communications.Handler;
import stac.communications.PACKETS;
import stac.communications.Packet;
import stac.communications.PacketBuffer;
import stac.communications.PacketParser;
import stac.communications.PacketParserException;
import stac.communications.Session;
import stac.communications.packets.RequestPacket;
import stac.crypto.Key;
import stac.crypto.PrivateKey;
import stac.parser.OpenSSLRSAPEM;

public class RequestHandler
extends Handler {
    private Screen screen;

    public RequestHandler(Screen screen) {
        this.screen = screen;
    }

    @Override
    protected boolean isPacketFormed(PacketBuffer packetBuffer) {
        if (packetBuffer.getOffset() >= this.getMinPacketSize()) {
            Integer nonceLength = OpenSSLRSAPEM.INTEGER.valueOfUnsigned(packetBuffer.getBuffer(), 69, 4).getInternal();
            if (packetBuffer.getOffset() >= 73 + nonceLength) {
                Integer counterLength = OpenSSLRSAPEM.INTEGER.valueOfUnsigned(packetBuffer.getBuffer(), 73 + nonceLength, 4).getInternal();
                if (packetBuffer.getOffset() >= 77 + nonceLength + counterLength) {
                    Integer sessionContentLength = OpenSSLRSAPEM.INTEGER.valueOfUnsigned(packetBuffer.getBuffer(), 1, 4).getInternal();
                    return packetBuffer.getOffset() == 77 + nonceLength + counterLength + 32 + sessionContentLength;
                }
            }
        }
        return false;
    }

    @Override
    protected boolean isPacketStillOK(PacketBuffer packetBuffer) {
        return true;
    }

    @Override
    protected int getMaxPacketSize() {
        return PACKETS.REQUEST.maxSize();
    }

    @Override
    protected int getMinPacketSize() {
        return PACKETS.REQUEST.minSize();
    }

    @Override
    public Packet runPacketParser(PacketBuffer packetBuffer, Session session, PACKETS expecting) throws PacketParserException {
        if (expecting == PACKETS.REQUEST) {
            RequestPacket packet = (RequestPacket)RequestPacket.newRawRequestPacket(session).getParser().parse(packetBuffer, session.getRemote());
            if (packet.getReceiverKey().matches(session.getServerKey())) {
                packet.getReceiverKey().setPem(session.getServerKey().getPem());
            }
            packetBuffer.reset();
            return packet;
        }
        throw new RuntimeException("Request Handler: Something is not implemented");
    }

    @Override
    public CONNECTION_PHASE handlePacket(PacketBuffer packetBuffer, Session session) {
        try {
            if (session.isExpecting(PACKETS.REQUEST)) {
                RequestPacket packet = (RequestPacket)this.runPacketParser(packetBuffer, session, session.getExpecting());
                packet.setCommunications(session.getCommunications());
                if (this.screen == null || packet.getType() != RequestPacket.RequestType.Message) {
                    if (this.screen != null && packet.getType() == RequestPacket.RequestType.Terminate) {
                        return CONNECTION_PHASE.TERMINATING;
                    }
                    System.err.println("Request handler cannot handle this message.");
                    return CONNECTION_PHASE.FAILED;
                }
                this.screen.postMessage(packet);
                this.terminate(session, packet);
                return CONNECTION_PHASE.HANDSHAKE_ACCEPTED;
            }
            System.err.println("Request handler invoked, but session is not expecting a request packet. Closing session.");
            return CONNECTION_PHASE.FAILED;
        }
        catch (PacketParserException e) {
            System.err.println("A(n) " + e.getClass().getName() + " Exception has occurred. Closing session.");
            return CONNECTION_PHASE.FAILED;
        }
        catch (IOException e) {
            System.err.println("Reply read failed.");
            return CONNECTION_PHASE.FAILED;
        }
    }

    private void terminate(Session session, RequestPacket packet) throws PacketParserException, IOException {
        session.send(RequestPacket.newTermination(session.getCommunications().getListenerKey(), packet, session));
    }
}

