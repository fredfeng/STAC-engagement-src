/*
 * Decompiled with CFR 0_121.
 */
package stac.communications.packets;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import stac.client.Remote;
import stac.communications.PACKETS;
import stac.communications.Packet;
import stac.communications.PacketBuffer;
import stac.communications.PacketParser;
import stac.communications.PacketParserException;
import stac.communications.packets.HandshakePacket;
import stac.crypto.Key;
import stac.parser.OpenSSLRSAPEM;

public class HandshakePacketParser
extends PacketParser {
    private HandshakePacket owner;

    HandshakePacketParser(HandshakePacket owner) {
        this.owner = owner;
    }

    @Override
    public Packet parse(PacketBuffer packetBuffer, Remote remote) throws PacketParserException {
        byte[] buffer = packetBuffer.getBuffer();
        int size = packetBuffer.getOffset();
        int bpos = 0;
        byte packetType = buffer[bpos++];
        if (PACKETS.HANDSHAKE_OPEN.ordinal() != packetType) {
            throw new PacketParserException("Malformed Handshake Packet has incorrect packet type/version.");
        }
        if (size >= 34) {
            this.owner.getKey().setFingerPrint(this.parseFingerprint(buffer, bpos));
            bpos += 32;
            this.owner.getFlags().fromByte(buffer[bpos++]);
            if (!this.owner.getFlags().isRegistered()) {
                short modulusSize;
                short publicExponentSizeBytes;
                if (bpos + 1 >= size) {
                    throw new PacketParserException("Malformed Handshake Packet has invalid size.");
                }
                if (bpos + (publicExponentSizeBytes = (short)((255 & buffer[bpos++]) << 8 | 255 & buffer[bpos++])) >= size) {
                    throw new PacketParserException("Malformed Handshake Packet has invalid size.");
                }
                OpenSSLRSAPEM.INTEGER pubExp = OpenSSLRSAPEM.INTEGER.valueOf(buffer, bpos, publicExponentSizeBytes);
                if ((bpos += publicExponentSizeBytes) + 1 >= size) {
                    throw new PacketParserException("Malformed Handshake Packet has invalid size.");
                }
                if (bpos + (modulusSize = (short)((255 & buffer[bpos++]) << 8 | 255 & buffer[bpos++])) > size) {
                    throw new PacketParserException("Malformed Handshake Packet has invalid size.");
                }
                OpenSSLRSAPEM.INTEGER modulus = OpenSSLRSAPEM.INTEGER.valueOf(buffer, bpos, modulusSize);
                bpos += modulusSize;
                this.owner.getKey().setPem(new OpenSSLRSAPEM(pubExp, modulus));
            }
            return this.owner;
        }
        throw new PacketParserException("Malformed Handshake Packet has invalid size.");
    }

    private byte[] parseFingerprint(byte[] buffer, int offset) {
        byte[] fp;
        fp = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(32);
            Throwable throwable = null;
            try {
                DataOutputStream out = new DataOutputStream(bos);
                Throwable throwable2 = null;
                try {
                    out.write(buffer, offset, 32);
                    fp = bos.toByteArray();
                }
                catch (Throwable x2) {
                    throwable2 = x2;
                    throw x2;
                }
                finally {
                    if (out != null) {
                        if (throwable2 != null) {
                            try {
                                out.close();
                            }
                            catch (Throwable x2) {
                                throwable2.addSuppressed(x2);
                            }
                        } else {
                            out.close();
                        }
                    }
                }
            }
            catch (Throwable x2) {
                throwable = x2;
                throw x2;
            }
            finally {
                if (bos != null) {
                    if (throwable != null) {
                        try {
                            bos.close();
                        }
                        catch (Throwable x2) {
                            throwable.addSuppressed(x2);
                        }
                    } else {
                        bos.close();
                    }
                }
            }
        }
        catch (IOException bos) {
            // empty catch block
        }
        return fp;
    }

    @Override
    public byte[] serialize() throws PacketParserException {
        byte[] outputArray;
        outputArray = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Throwable throwable = null;
            try {
                DataOutputStream out = new DataOutputStream(bos);
                Throwable throwable2 = null;
                try {
                    out.writeByte(PACKETS.HANDSHAKE_OPEN.ordinal());
                    out.write(this.owner.getKey().getFingerPrint());
                    out.write(this.owner.getFlags().toByte());
                    if (!this.owner.getFlags().isRegistered()) {
                        byte[] pubExp = this.owner.getKey().getPem().getPublicExponent().getBytes();
                        if (pubExp.length > 65535) {
                            throw new PacketParserException("Invalid Handshake Packet has unsupported public exponent size.");
                        }
                        out.write((byte)((pubExp.length & 65280) >> 8));
                        out.write((byte)(pubExp.length & 255));
                        out.write(pubExp);
                        byte[] modulus = this.owner.getKey().getPem().getModulus().getBytes();
                        if (modulus.length > 65535) {
                            throw new PacketParserException("Invalid Handshake Packet has unsupported modulus size.");
                        }
                        out.write((byte)((modulus.length & 65280) >> 8));
                        out.write((byte)(modulus.length & 255));
                        out.write(modulus);
                    }
                    outputArray = bos.toByteArray();
                }
                catch (Throwable x2) {
                    throwable2 = x2;
                    throw x2;
                }
                finally {
                    if (out != null) {
                        if (throwable2 != null) {
                            try {
                                out.close();
                            }
                            catch (Throwable x2) {
                                throwable2.addSuppressed(x2);
                            }
                        } else {
                            out.close();
                        }
                    }
                }
            }
            catch (Throwable x2) {
                throwable = x2;
                throw x2;
            }
            finally {
                if (bos != null) {
                    if (throwable != null) {
                        try {
                            bos.close();
                        }
                        catch (Throwable x2) {
                            throwable.addSuppressed(x2);
                        }
                    } else {
                        bos.close();
                    }
                }
            }
        }
        catch (IOException bos) {
            // empty catch block
        }
        return outputArray;
    }
}

