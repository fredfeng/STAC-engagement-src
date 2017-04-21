/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.dialogs;

import edu.computerapex.dialogs.CommunicationsDeviation;
import edu.computerapex.dialogs.CommunicationsPublicIdentity;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.AttributeKey;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public final class CommunicationsConnection {
    public static final AttributeKey<CommunicationsConnection> CONNECTION_ATTR = new AttributeKey("CONNECTION_ATTR");
    private final Channel channel;
    private final CommunicationsPublicIdentity theirIdentity;

    public CommunicationsConnection(Channel channel, CommunicationsPublicIdentity theirIdentity) {
        this.channel = channel;
        this.theirIdentity = theirIdentity;
    }

    public void write(String str) throws CommunicationsDeviation {
        this.write(str.getBytes());
    }

    public void write(byte[] bytes) throws CommunicationsDeviation {
        this.channel.writeAndFlush(bytes);
    }

    public void close() throws CommunicationsDeviation {
        try {
            this.channel.close().sync();
        }
        catch (Exception e) {
            throw new CommunicationsDeviation(e);
        }
    }

    public boolean isOpen() {
        return this.channel.isOpen();
    }

    public CommunicationsPublicIdentity pullTheirIdentity() {
        return this.theirIdentity;
    }

    public String grabRemoteHostString() {
        InetSocketAddress sa = (InetSocketAddress)this.channel.remoteAddress();
        return sa.getHostString();
    }

    public int takeRemotePort() {
        InetSocketAddress sa = (InetSocketAddress)this.channel.remoteAddress();
        return sa.getPort();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        CommunicationsConnection connection = (CommunicationsConnection)o;
        if (!this.theirIdentity.equals(connection.theirIdentity)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = this.theirIdentity.hashCode();
        return result;
    }
}

