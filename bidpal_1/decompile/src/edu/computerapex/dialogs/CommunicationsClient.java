/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.dialogs;

import edu.computerapex.dialogs.CommunicationsConnection;
import edu.computerapex.dialogs.CommunicationsDeviation;
import edu.computerapex.dialogs.CommunicationsHandler;
import edu.computerapex.dialogs.CommunicationsIdentity;
import edu.computerapex.dialogs.CommunicationsNetworkAddress;
import edu.computerapex.dialogs.internal.CommunicationsChannelInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;

public class CommunicationsClient {
    private final Bootstrap bootstrap = new Bootstrap();
    private EventLoopGroup group = new NioEventLoopGroup(1);
    private final CommunicationsChannelInitializer initializer;

    public CommunicationsClient(CommunicationsHandler communicationsHandler, CommunicationsIdentity identity) {
        this.initializer = new CommunicationsChannelInitializer(communicationsHandler, identity, false);
        ((Bootstrap)((Bootstrap)this.bootstrap.group(this.group)).channel(NioSocketChannel.class)).handler(this.initializer);
    }

    public CommunicationsConnection connect(String host, int port) throws CommunicationsDeviation {
        try {
            Channel channel = this.bootstrap.connect(host, port).sync().channel();
            this.initializer.awaitForPermission(10000);
            return channel.attr(CommunicationsConnection.CONNECTION_ATTR).get();
        }
        catch (Exception e) {
            throw new CommunicationsDeviation(e);
        }
    }

    public CommunicationsConnection connect(CommunicationsNetworkAddress addr) throws CommunicationsDeviation {
        return this.connect(addr.fetchHost(), addr.fetchPort());
    }

    public void close() {
        this.group.shutdownGracefully();
    }

    public EventLoopGroup takeEventLoopGroup() {
        return this.group;
    }
}

