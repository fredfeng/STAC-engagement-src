/*
 * Decompiled with CFR 0_121.
 */
package stac.communications;

import stac.client.Remote;
import stac.communications.Packet;
import stac.communications.PacketBuffer;
import stac.communications.PacketParserException;

public abstract class PacketParser {
    public abstract Packet parse(PacketBuffer var1, Remote var2) throws PacketParserException;

    public abstract byte[] serialize() throws PacketParserException;
}

