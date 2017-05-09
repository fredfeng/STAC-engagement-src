/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.dialogs;

import edu.computerapex.dialogs.CommunicationsDeviation;
import edu.computerapex.dialogs.CommunicationsHandler;
import edu.computerapex.dialogs.CommunicationsIdentity;
import edu.computerapex.dialogs.internal.CommunicationsChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;

public class CommunicationsServer {
    private int listenPort;
    private final ServerBootstrap bootstrap;
    private final EventLoopGroup serverGroup;

    public CommunicationsServer(int listenPort, CommunicationsHandler handler, CommunicationsIdentity identity) {
        this(listenPort, handler, identity, new NioEventLoopGroup(1));
    }

    public CommunicationsServer(int listenPort, CommunicationsHandler handler, CommunicationsIdentity identity, EventLoopGroup eventLoopGroup) {
        this.listenPort = listenPort;
        this.bootstrap = new ServerBootstrap();
        this.serverGroup = eventLoopGroup;
        this.bootstrap.group(this.serverGroup).channel(NioServerSocketChannel.class);
        this.bootstrap.childHandler(new CommunicationsChannelInitializer(handler, identity, true));
    }

    public void serve() throws CommunicationsDeviation {
        try {
            this.bootstrap.bind(this.listenPort).sync();
        }
        catch (Exception e) {
            throw new CommunicationsDeviation(e);
        }
    }

    public void close() {
        this.serverGroup.shutdownGracefully();
    }
}

