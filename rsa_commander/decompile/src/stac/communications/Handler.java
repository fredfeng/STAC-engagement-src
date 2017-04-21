/*
 * Decompiled with CFR 0_121.
 */
package stac.communications;

import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import stac.communications.CONNECTION_PHASE;
import stac.communications.HANDLER_STATE;
import stac.communications.PACKETS;
import stac.communications.Packet;
import stac.communications.PacketBuffer;
import stac.communications.PacketParserException;
import stac.communications.Session;

public abstract class Handler {
    public HANDLER_STATE handle(SocketChannel ch, PacketBuffer packetBuffer) throws IOException {
        ByteBuffer allocate = ByteBuffer.allocate(16384);
        while (ch.read(allocate) > 0) {
            packetBuffer.write(allocate.array(), 0, allocate.position());
            allocate.rewind();
        }
        if (this.isPacketFormed(packetBuffer)) {
            return HANDLER_STATE.DONE;
        }
        if (this.isPacketStillOK(packetBuffer)) {
            return HANDLER_STATE.WAITING;
        }
        return HANDLER_STATE.FAILED;
    }

    public HANDLER_STATE handle(InputStream is, PacketBuffer packetBuffer) throws IOException {
        int read;
        while (!this.isPacketFormed(packetBuffer) && is.available() > 0 && (read = is.read()) != -1) {
            packetBuffer.write((byte)read);
        }
        if (this.isPacketFormed(packetBuffer)) {
            return HANDLER_STATE.DONE;
        }
        if (this.isPacketStillOK(packetBuffer)) {
            return HANDLER_STATE.WAITING;
        }
        return HANDLER_STATE.FAILED;
    }

    protected abstract boolean isPacketFormed(PacketBuffer var1);

    protected abstract boolean isPacketStillOK(PacketBuffer var1);

    protected abstract int getMaxPacketSize();

    protected abstract int getMinPacketSize();

    public void initPacketBuffer(PacketBuffer packetBuffer) {
        packetBuffer.resize(this.getMinPacketSize(), this.getMaxPacketSize());
    }

    public abstract Packet runPacketParser(PacketBuffer var1, Session var2, PACKETS var3) throws PacketParserException;

    public abstract CONNECTION_PHASE handlePacket(PacketBuffer var1, Session var2);
}

