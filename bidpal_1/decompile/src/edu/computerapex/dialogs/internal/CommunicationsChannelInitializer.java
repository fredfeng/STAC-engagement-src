/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.dialogs.internal;

import edu.computerapex.dialogs.CommunicationsDeviation;
import edu.computerapex.dialogs.CommunicationsHandler;
import edu.computerapex.dialogs.CommunicationsIdentity;
import edu.computerapex.dialogs.internal.CommunicationsNettyHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.util.concurrent.Promise;

public class CommunicationsChannelInitializer
extends ChannelInitializer<SocketChannel> {
    private final CommunicationsHandler handler;
    private final CommunicationsIdentity identity;
    private final boolean isServer;
    private CommunicationsNettyHandler nettyHandler;

    public CommunicationsChannelInitializer(CommunicationsHandler handler, CommunicationsIdentity identity, boolean isServer) {
        this.handler = handler;
        this.identity = identity;
        this.isServer = isServer;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("frameDecoder", (ChannelHandler)new LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4)).addLast("frameEncoder", (ChannelHandler)new LengthFieldPrepender(4)).addLast("bytesEncoder", (ChannelHandler)new ByteArrayEncoder()).addLast("bytesDecoder", (ChannelHandler)new ByteArrayDecoder());
        this.nettyHandler = new CommunicationsNettyHandler(this.handler, this.identity, this.isServer, ch.newPromise());
        ch.pipeline().addLast(this.nettyHandler);
    }

    public void awaitForPermission(long timeoutMillis) throws CommunicationsDeviation {
        this.nettyHandler.awaitForPermission(timeoutMillis);
    }
}

