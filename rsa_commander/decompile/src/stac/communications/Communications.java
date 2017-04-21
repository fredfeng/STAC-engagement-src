/*
 * Decompiled with CFR 0_121.
 */
package stac.communications;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import stac.client.Screen;
import stac.communications.HANDLER_STATE;
import stac.communications.Handler;
import stac.communications.NameGenerator;
import stac.communications.PACKETS;
import stac.communications.Packet;
import stac.communications.PacketBuffer;
import stac.communications.PacketParser;
import stac.communications.PacketParserException;
import stac.communications.Session;
import stac.communications.handlers.HandshakeHandler;
import stac.communications.handlers.RequestHandler;
import stac.communications.packets.HandshakePacket;
import stac.communications.packets.RequestPacket;
import stac.crypto.Key;
import stac.crypto.PrivateKey;
import stac.crypto.PublicKey;
import stac.parser.CommandLine;

public class Communications {
    private CommandLine.Options options;
    private PrivateKey listenerKey;
    private final Handler handshakeRequestHandler = new HandshakeHandler();
    private final Handler requestHandler;
    private boolean stopRequested = false;
    private Selector selector;
    private String name = NameGenerator.randomName();
    private final Screen screen;

    public Communications(CommandLine.Options options, Screen screen) {
        this.requestHandler = new RequestHandler(screen);
        this.screen = screen;
        this.options = options;
    }

    public void postError(String s, boolean redraw) {
        if (this.screen == null) {
            System.err.println(s);
        } else {
            this.screen.postError(s, redraw);
        }
    }

    public int listen() {
        if (this.listenerKey == null) {
            this.postError("Missing private key", false);
            return 1;
        }
        try {
            CommandLine.Option bindAddr = this.options.findByLongOption("bind-address");
            CommandLine.Option bindPort = this.options.findByLongOption("bind-port");
            ServerSocketChannel channel = ServerSocketChannel.open();
            this.selector = Selector.open();
            channel.configureBlocking(false);
            channel.bind(new InetSocketAddress(bindAddr.getValue(), Integer.parseInt(bindPort.getValue())));
            channel.register(this.selector, 16);
            do {
                this.selector.select();
                Set<SelectionKey> selectionKeys = this.selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isAcceptable()) {
                        SocketChannel accept = channel.accept();
                        if (accept != null) {
                            accept.configureBlocking(false);
                            accept.register(this.selector, 1, new Session(this, this.options, this.handshakeRequestHandler, this.requestHandler));
                        } else {
                            this.postError("Connection Accept Failed.");
                        }
                    } else if (key.isReadable()) {
                        Session session = (Session)key.attachment();
                        if (session != null) {
                            SocketChannel sc = (SocketChannel)key.channel();
                            if (session.handle(sc)) {
                                key.attach(null);
                                session.destroy();
                                sc.close();
                                key.cancel();
                            }
                        } else {
                            this.postError("Failed to get session.");
                        }
                    }
                    iterator.remove();
                }
            } while (!this.stopRequested);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (CommandLine.InvalidOptionException e) {
            this.postError("Critical failure. Configuration of the server component failed.", false);
            return 1;
        }
        return 0;
    }

    public void postError(String s) {
        this.postError(s, true);
    }

    public PrivateKey getListenerKey() {
        return this.listenerKey;
    }

    public void setListenerKey(PrivateKey listenerKey) {
        this.listenerKey = listenerKey;
    }

    public void stop() {
        this.stopRequested = true;
        this.selector.wakeup();
    }

    public void sendMessage(String address, String port, String message, String receiverName) throws IOException, PacketParserException {
        InputStream inputStream;
        if (this.screen == null) {
            throw new RuntimeException("sendMessage should never be called in server side communications");
        }
        InetAddress inetAddress = InetAddress.getByName(address);
        Socket socket = new Socket(inetAddress, Integer.parseInt(port));
        OutputStream outputStream = socket.getOutputStream();
        HandshakePacket handshakePacket = this.doHandshake(outputStream, inputStream = socket.getInputStream(), false);
        if (handshakePacket != null) {
            HANDLER_STATE handlerstate;
            RequestPacket requestPacket = RequestPacket.newMessage(this.getListenerKey(), (PublicKey)handshakePacket.getKey(), this.name, receiverName);
            requestPacket.setMessage(message);
            outputStream.write(requestPacket.getParser().serialize());
            RequestPacket termination = RequestPacket.newTermination(this.getListenerKey(), requestPacket, (PublicKey)handshakePacket.getKey());
            outputStream.write(termination.getParser().serialize());
            RequestHandler requestHandler = new RequestHandler(this.screen);
            PacketBuffer packetBuffer = new PacketBuffer();
            while ((handlerstate = requestHandler.handle(inputStream, packetBuffer)) == HANDLER_STATE.WAITING) {
            }
            if (handlerstate == HANDLER_STATE.DONE) {
                Session session = new Session(this, this.options, null, null);
                session.attachUser(handshakePacket.getKey());
                RequestPacket packet = (RequestPacket)requestHandler.runPacketParser(packetBuffer, session, PACKETS.REQUEST);
                if (packet.getType() == RequestPacket.RequestType.Terminate) {
                    this.screen.postOutput("Message Sent successfully.");
                } else {
                    this.screen.postError("Message failed to send successfully. Server responded unexpectedly.");
                }
            } else {
                this.screen.postError("Acknowledgement failed. Remote user may not have received your message.");
            }
        }
        socket.close();
    }

    private HandshakePacket doHandshake(OutputStream outputStream, InputStream inputStream, boolean registeredWithRemote) throws IOException, PacketParserException {
        HANDLER_STATE state;
        HandshakePacket handshakePacket = null;
        HandshakePacket handshakeBeginPacket = new HandshakePacket();
        HandshakePacket.Flags flags = handshakeBeginPacket.getFlags();
        flags.setRegistered(registeredWithRemote);
        flags.setHandshakeRequest(true);
        flags.setRequestsReturnService(true);
        handshakeBeginPacket.setKey(this.listenerKey);
        PacketParser parser = handshakeBeginPacket.getParser();
        outputStream.write(parser.serialize());
        HandshakeHandler handshakeHandler = new HandshakeHandler();
        PacketBuffer packetBuffer = new PacketBuffer(PACKETS.HANDSHAKE_OPEN.minSize(), PACKETS.HANDSHAKE_OPEN.maxSize());
        while ((state = handshakeHandler.handle(inputStream, packetBuffer)) == HANDLER_STATE.WAITING) {
        }
        if (state == HANDLER_STATE.DONE) {
            Session session = new Session(this, null, handshakeHandler, null);
            session.setExpecting(PACKETS.HANDSHAKE_OPEN);
            HandshakePacket packet = (HandshakePacket)handshakeHandler.runPacketParser(packetBuffer, null, PACKETS.HANDSHAKE_OPEN);
            if (packet.getFlags().isHandshakeAccepted()) {
                handshakePacket = packet;
            }
        } else if (state == HANDLER_STATE.FAILED) {
            this.screen.postError("Failed to complete handshake");
        }
        return handshakePacket;
    }
}

