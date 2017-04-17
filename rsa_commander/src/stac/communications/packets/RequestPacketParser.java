/*
 * Decompiled with CFR 0_121.
 */
package stac.communications.packets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import stac.client.Remote;
import stac.communications.Communications;
import stac.communications.PACKETS;
import stac.communications.Packet;
import stac.communications.PacketBuffer;
import stac.communications.PacketParser;
import stac.communications.PacketParserException;
import stac.communications.packets.RequestPacket;
import stac.crypto.DES;
import stac.crypto.Key;
import stac.crypto.PrivateKey;
import stac.crypto.PublicKey;
import stac.crypto.RSA;
import stac.crypto.SHA256;
import stac.crypto.SymmetricCipher;
import stac.parser.OpenSSLRSAPEM;

public class RequestPacketParser
extends PacketParser {
    private final RequestPacket owner;
    private final SymmetricCipher des = new DES();

    RequestPacketParser(RequestPacket requestPacket) {
        this.owner = requestPacket;
    }

    @Override
    public Packet parse(PacketBuffer packetBuffer, Remote remote) throws PacketParserException {
        byte b;
        RSA rsa = new RSA();
        byte[] buffer = packetBuffer.getBuffer();
        int bpos = 0;
        if ((b = buffer[bpos++]) == PACKETS.REQUEST_MESSAGE.ordinal()) {
            this.owner.setType(RequestPacket.RequestType.Message);
        } else if (b == PACKETS.REQUEST_MESSAGE_RELAY.ordinal()) {
            this.owner.setType(RequestPacket.RequestType.MessageRelay);
        } else if (b == PACKETS.RETRIEVE_MESSAGES.ordinal()) {
            this.owner.setType(RequestPacket.RequestType.RetrieveMessages);
        } else if (b == PACKETS.REQUEST_TERMINATE.ordinal()) {
            this.owner.setType(RequestPacket.RequestType.Terminate);
        } else {
            throw new PacketParserException("Packet type is not valid");
        }
        SHA256 headerDigest = new SHA256();
        byte[] senderFingerprint = new byte[32];
        byte[] receiverFingerprint = new byte[32];
        int sessionContentLength = OpenSSLRSAPEM.INTEGER.valueOf(buffer, bpos, 4).getInternal();
        System.arraycopy(buffer, bpos += 4, senderFingerprint, 0, senderFingerprint.length);
        headerDigest.update(buffer, bpos, senderFingerprint.length);
        System.arraycopy(buffer, bpos += senderFingerprint.length, receiverFingerprint, 0, receiverFingerprint.length);
        headerDigest.update(buffer, bpos, receiverFingerprint.length);
        int nonceBytesLength = OpenSSLRSAPEM.INTEGER.valueOf(buffer, bpos += receiverFingerprint.length, 4).getInternal();
        headerDigest.update(buffer, bpos, 4);
        OpenSSLRSAPEM.INTEGER nonce = OpenSSLRSAPEM.INTEGER.valueOf(buffer, bpos += 4, nonceBytesLength);
        bpos += nonceBytesLength;
        PrivateKey privateKey = this.owner.getCommunications().getListenerKey();
        nonce = rsa.decrypt(nonce, privateKey.getPem().getPrivateExponent(), privateKey.getPem().getModulus());
        if (nonce.compareTo(0) < 0) {
            throw new PacketParserException("Failed to perform RSA decryption");
        }
        headerDigest.update(nonce.getBytes());
        int counterBytesLength = OpenSSLRSAPEM.INTEGER.valueOf(buffer, bpos, 4).getInternal();
        headerDigest.update(buffer, bpos, 4);
        OpenSSLRSAPEM.INTEGER counter = OpenSSLRSAPEM.INTEGER.valueOf(buffer, bpos += 4, counterBytesLength);
        bpos += counterBytesLength;
        counter = rsa.decrypt(counter, privateKey.getPem().getPrivateExponent(), privateKey.getPem().getModulus());
        if (counter.compareTo(0) < 0 || counter.compareTo(remote.getKey().getPem().getPublicExponent()) > 0) {
            throw new PacketParserException("Failed to perform RSA decryption");
        }
        headerDigest.update(counter.getBytes());
        byte[] headerDigestBytes = headerDigest.digest();
        for (int i = 0; i < headerDigestBytes.length; ++i) {
            if (headerDigestBytes[i] == buffer[bpos++]) continue;
            throw new PacketParserException("Packet header check failed; possible packet corruption.");
        }
        ByteArrayInputStream encrypted = new ByteArrayInputStream(buffer, bpos, sessionContentLength);
        ByteArrayOutputStream decrypted = new ByteArrayOutputStream();
        try {
            this.des.encrypt_ctr(counter, nonce.getBytes(8), encrypted, decrypted);
        }
        catch (IOException e) {
            throw new PacketParserException("Failed to perform RSA decryption");
        }
        byte[] details = decrypted.toByteArray();
        bpos = 0;
        OpenSSLRSAPEM.INTEGER senderNameLength = OpenSSLRSAPEM.INTEGER.valueOfUnsigned(details, bpos, 4);
        byte[] senderNameBytes = new byte[senderNameLength.getInternal().intValue()];
        System.arraycopy(details, bpos += 4, senderNameBytes, 0, senderNameLength.getInternal());
        OpenSSLRSAPEM.INTEGER receiverNameLength = OpenSSLRSAPEM.INTEGER.valueOfUnsigned(details, bpos += senderNameLength.getInternal().intValue(), 4);
        byte[] receiverNameBytes = new byte[receiverNameLength.getInternal().intValue()];
        System.arraycopy(details, bpos += 4, receiverNameBytes, 0, receiverNameLength.getInternal());
        OpenSSLRSAPEM.INTEGER messageLength = OpenSSLRSAPEM.INTEGER.valueOfUnsigned(details, bpos += receiverNameLength.getInternal().intValue(), 4);
        byte[] messageBytes = new byte[messageLength.getInternal().intValue()];
        System.arraycopy(details, bpos += 4, messageBytes, 0, messageLength.getInternal());
        byte[] ourDigest = SHA256.digest(senderNameLength.getBytes(4), senderNameBytes, receiverNameLength.getBytes(4), receiverNameBytes, messageLength.getBytes(4), messageBytes);
        byte[] theirDigest = new byte[32];
        System.arraycopy(details, bpos += messageLength.getInternal().intValue(), theirDigest, 0, theirDigest.length);
        for (int i = 0; i < theirDigest.length; ++i) {
            if (theirDigest[i] == ourDigest[i]) continue;
            throw new PacketParserException("Failed decryption of encrypted packet. Checksum failure");
        }
        this.owner.setMessage(new String(messageBytes));
        this.owner.setSenderName(new String(senderNameBytes));
        this.owner.setReceiverName(new String(receiverNameBytes));
        if (new PrivateKey(senderFingerprint).matches(remote.getKey())) {
            this.owner.setSenderKey(remote.getKey());
        }
        this.owner.setReceiverKey(new PublicKey(receiverFingerprint));
        this.owner.setNonce(nonce);
        return this.owner;
    }

    @Override
    public byte[] serialize() throws PacketParserException {
        byte[] outputPacketBytes;
        RSA rsa = new RSA();
        try {
            ByteArrayOutputStream requestPacketOutputStream = new ByteArrayOutputStream();
            Throwable throwable = null;
            try {
                requestPacketOutputStream.write(this.owner.getType().toPacketType().ordinal());
                OpenSSLRSAPEM.INTEGER nonce = this.owner.getNonce();
                OpenSSLRSAPEM.INTEGER counter = OpenSSLRSAPEM.INTEGER.randomShort().abs();
                ByteArrayOutputStream sessionInformationOutputStream = this.encryptSymmetricCipherParameters(rsa, nonce, counter);
                int size = this.appendRijndaelEncryptedSessionContent(sessionInformationOutputStream, nonce, counter);
                byte[] sessionContentBytes = sessionInformationOutputStream.toByteArray();
                requestPacketOutputStream.write(OpenSSLRSAPEM.INTEGER.valueOf(size).getBytes(4));
                requestPacketOutputStream.write(sessionContentBytes);
                outputPacketBytes = requestPacketOutputStream.toByteArray();
            }
            catch (Throwable x2) {
                throwable = x2;
                throw x2;
            }
            finally {
                if (requestPacketOutputStream != null) {
                    if (throwable != null) {
                        try {
                            requestPacketOutputStream.close();
                        }
                        catch (Throwable x2) {
                            throwable.addSuppressed(x2);
                        }
                    } else {
                        requestPacketOutputStream.close();
                    }
                }
            }
        }
        catch (IOException e) {
            System.err.println("Failed to serialize a message packet");
            return null;
        }
        return outputPacketBytes;
    }

    private ByteArrayOutputStream encryptSymmetricCipherParameters(RSA rsa, OpenSSLRSAPEM.INTEGER nonce, OpenSSLRSAPEM.INTEGER counter) throws IOException {
        SHA256 headerDigest = new SHA256();
        byte[] senderFingerprint = this.owner.getSenderKey().getFingerPrint();
        byte[] receiverFingerprint = this.owner.getReceiverKey().getFingerPrint();
        headerDigest.update(senderFingerprint);
        headerDigest.update(receiverFingerprint);
        OpenSSLRSAPEM.INTEGER publicExponent = this.owner.getReceiverKey().getPem().getPublicExponent();
        OpenSSLRSAPEM.INTEGER modulus = this.owner.getReceiverKey().getPem().getModulus();
        OpenSSLRSAPEM.INTEGER encryptNonce = rsa.encrypt(nonce, publicExponent, modulus);
        OpenSSLRSAPEM.INTEGER encryptCounter = rsa.encrypt(counter, publicExponent, modulus);
        byte[] encryptNonceBytes = encryptNonce.getBytes();
        byte[] encryptNonceBytesLength = OpenSSLRSAPEM.INTEGER.valueOf(encryptNonceBytes.length).getBytes(4);
        headerDigest.update(encryptNonceBytesLength);
        headerDigest.update(nonce.getBytes());
        byte[] encryptCounterBytes = encryptCounter.getBytes();
        byte[] encryptCounterBytesLength = OpenSSLRSAPEM.INTEGER.valueOf(encryptCounterBytes.length).getBytes(4);
        headerDigest.update(encryptCounterBytesLength);
        headerDigest.update(counter.getBytes());
        ByteArrayOutputStream sessionInformationOutputStream = new ByteArrayOutputStream();
        sessionInformationOutputStream.write(senderFingerprint);
        sessionInformationOutputStream.write(receiverFingerprint);
        sessionInformationOutputStream.write(encryptNonceBytesLength);
        sessionInformationOutputStream.write(encryptNonceBytes);
        sessionInformationOutputStream.write(encryptCounterBytesLength);
        sessionInformationOutputStream.write(encryptCounterBytes);
        sessionInformationOutputStream.write(headerDigest.digest());
        return sessionInformationOutputStream;
    }

    private int appendRijndaelEncryptedSessionContent(ByteArrayOutputStream toStream, OpenSSLRSAPEM.INTEGER nonce, OpenSSLRSAPEM.INTEGER counter) throws IOException {
        byte[] senderName = this.owner.getSenderName().getBytes(StandardCharsets.UTF_8);
        byte[] receiverName = this.owner.getReceiverName().getBytes(StandardCharsets.UTF_8);
        byte[] message = new byte[]{};
        if (this.owner.getMessage() != null) {
            message = this.owner.getMessage().getBytes(StandardCharsets.UTF_8);
        }
        byte[] senderNameLength = OpenSSLRSAPEM.INTEGER.valueOf(senderName.length).getBytes(4);
        byte[] receiverNameLength = OpenSSLRSAPEM.INTEGER.valueOf(receiverName.length).getBytes(4);
        byte[] messageLength = OpenSSLRSAPEM.INTEGER.valueOf(message.length).getBytes(4);
        byte[] digest = SHA256.digest(senderNameLength, senderName, receiverNameLength, receiverName, messageLength, message);
        byte[] content = new byte[senderNameLength.length + senderName.length + receiverNameLength.length + receiverName.length + messageLength.length + message.length + digest.length];
        int position = this.appendBytes(content, senderNameLength, 0);
        position = this.appendBytes(content, senderName, position);
        position = this.appendBytes(content, receiverNameLength, position);
        position = this.appendBytes(content, receiverName, position);
        position = this.appendBytes(content, messageLength, position);
        position = this.appendBytes(content, message, position);
        position = this.appendBytes(content, digest, position);
        ByteArrayOutputStream tempStream = new ByteArrayOutputStream(position);
        this.des.encrypt_ctr(counter, nonce.getBytes(8), new ByteArrayInputStream(content), tempStream);
        tempStream.writeTo(toStream);
        return tempStream.size();
    }

    private int appendBytes(byte[] toArray, byte[] fromArray, int position) {
        System.arraycopy(fromArray, 0, toArray, position, fromArray.length);
        return position += fromArray.length;
    }
}

