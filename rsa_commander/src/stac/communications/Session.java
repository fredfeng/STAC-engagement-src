/*
 * Decompiled with CFR 0_121.
 */
package stac.communications;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import stac.client.Remote;
import stac.communications.CONNECTION_PHASE;
import stac.communications.Communications;
import stac.communications.HANDLER_STATE;
import stac.communications.Handler;
import stac.communications.PACKETS;
import stac.communications.Packet;
import stac.communications.PacketBuffer;
import stac.communications.PacketParser;
import stac.communications.PacketParserException;
import stac.crypto.Key;
import stac.crypto.PrivateKey;
import stac.crypto.PublicKey;
import stac.parser.CommandLine;

public class Session {
    private final CommandLine.Options options;
    private final Communications communications;
    private final Handler handshakeRequestHandler;
    private final Handler requestHandler;
    private CONNECTION_PHASE phase = CONNECTION_PHASE.OPEN;
    private PacketBuffer packetBuffer = new PacketBuffer();
    private PACKETS expecting = null;
    private SocketChannel socketChannel;
    private Remote remote;
    private SocketAddress remoteAddress;
    private byte[] challenge;
    private PublicKey challengedKey;

    public Session(Communications communications, CommandLine.Options options, Handler handshakeRequestHandler, Handler requestHandler) {
        this.communications = communications;
        this.options = options;
        this.handshakeRequestHandler = handshakeRequestHandler;
        this.requestHandler = requestHandler;
    }

    public synchronized boolean handle(SocketChannel sc) throws IOException {
        if (this.socketChannel == null) {
            this.socketChannel = sc;
        }
        if (this.remoteAddress == null) {
            this.remoteAddress = sc.getRemoteAddress();
        }
        try {
            switch (this.phase) {
                case OPEN: {
                    this.expecting = PACKETS.HANDSHAKE_OPEN;
                    this.phase = CONNECTION_PHASE.HANDSHAKE_REQUEST;
                }
                case HANDSHAKE_REQUEST: {
                    this.handshakeRequestHandler.initPacketBuffer(this.packetBuffer);
                    if (!this.readPacket(this.handshakeRequestHandler)) break;
                    this.phase = CONNECTION_PHASE.TERMINATING;
                    break;
                }
                case HANDSHAKE_ACCEPTED: {
                    this.requestHandler.initPacketBuffer(this.packetBuffer);
                    if (!this.readPacket(this.requestHandler)) break;
                    this.phase = CONNECTION_PHASE.TERMINATING;
                    break;
                }
                case HANDSHAKE_CHALLENGE: {
                    this.handshakeRequestHandler.initPacketBuffer(this.packetBuffer);
                    if (!this.readPacket(this.handshakeRequestHandler)) break;
                    this.phase = CONNECTION_PHASE.TERMINATING;
                    break;
                }
            }
        }
        catch (IOException e) {
            this.communications.postError("Something really screwed up happened, destroy session.");
            e.printStackTrace();
            this.phase = CONNECTION_PHASE.TERMINATING;
        }
        if (this.phase == CONNECTION_PHASE.FAILED) {
            this.communications.postError("Session has entered a failed state.");
            this.phase = CONNECTION_PHASE.TERMINATING;
        }
        return this.phase == CONNECTION_PHASE.TERMINATING;
    }

    private boolean readPacket(Handler handler) throws IOException {
        switch (handler.handle(this.socketChannel, this.packetBuffer)) {
            case FAILED: {
                this.communications.postError("Failed to read a packet.");
                return true;
            }
            case WAITING: {
                break;
            }
            case DONE: {
                this.phase = handler.handlePacket(this.packetBuffer, this);
                if (this.phase != CONNECTION_PHASE.TERMINATING) break;
                return true;
            }
            case CLOSE: {
                return true;
            }
        }
        return false;
    }

    synchronized void destroy() {
        this.packetBuffer.destroy();
    }

    public synchronized boolean isExpecting(PACKETS packet) {
        return this.expecting == packet;
    }

    public synchronized void setExpecting(PACKETS packet) {
        this.expecting = packet;
    }

    public synchronized void send(Packet packet) throws PacketParserException, IOException {
        this.socketChannel.write(ByteBuffer.wrap(packet.getParser().serialize()));
    }

    public void attachUser(Key publicKey) {
        this.remote = new Remote();
        this.remote.setKey((PublicKey)publicKey);
    }

    public Key getServerKey() {
        return this.communications.getListenerKey();
    }

    public Remote getRemote() {
        return this.remote;
    }

    private synchronized SocketAddress getRemoteAddress() {
        return this.remoteAddress;
    }

    public byte[] getChallenge() {
        return this.challenge;
    }

    public void setChallenge(byte[] challenge) {
        this.challenge = challenge;
    }

    public void updateKey() {
        this.remote.setKey(this.getChallengedKey());
    }

    public PublicKey getChallengedKey() {
        return this.challengedKey;
    }

    public void setChallengedKey(PublicKey challengedKey) {
        this.challengedKey = challengedKey;
    }

    public synchronized void setPhase(CONNECTION_PHASE phase) {
        this.phase = phase;
    }

    public Communications getCommunications() {
        return this.communications;
    }

    public PACKETS getExpecting() {
        return this.expecting;
    }

}

