/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.dialogs.internal;

import edu.computerapex.dialogs.CommunicationsConnection;
import edu.computerapex.dialogs.CommunicationsDeviation;
import edu.computerapex.dialogs.CommunicationsHandler;
import edu.computerapex.dialogs.CommunicationsIdentity;
import edu.computerapex.dialogs.CommunicationsPublicIdentity;
import edu.computerapex.dialogs.internal.CommunicationsCryptoState;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Promise;
import java.security.InvalidKeyException;
import java.security.spec.InvalidParameterSpecException;

public class CommunicationsNettyHandler
extends ChannelDuplexHandler {
    private final CommunicationsHandler handler;
    private final CommunicationsCryptoState cryptoState;
    private final boolean isServer;
    private Promise authenticatedPromise;

    public CommunicationsNettyHandler(CommunicationsHandler handler, CommunicationsIdentity identity, boolean isServer, Promise authenticatedPromise) throws CommunicationsDeviation {
        this.handler = handler;
        this.cryptoState = new CommunicationsCryptoState(identity);
        this.isServer = isServer;
        this.authenticatedPromise = authenticatedPromise;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        if (!this.isServer && this.cryptoState.hasSetupMessage()) {
            new CommunicationsNettyHandlerEngine(ctx).invoke();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        CommunicationsConnection connection = ctx.channel().attr(CommunicationsConnection.CONNECTION_ATTR).get();
        if (connection != null) {
            this.channelInactiveCoordinator(connection);
        }
    }

    private void channelInactiveCoordinator(CommunicationsConnection connection) throws CommunicationsDeviation {
        this.handler.closedConnection(connection);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (this.cryptoState.isReady()) {
            byte[] data = this.cryptoState.decrypt((byte[])msg);
            this.handler.handle(ctx.channel().attr(CommunicationsConnection.CONNECTION_ATTR).get(), data);
        } else {
            this.channelReadHelp(ctx, (byte[])msg);
        }
    }

    private void channelReadHelp(ChannelHandlerContext ctx, byte[] msg) throws CommunicationsDeviation, InvalidParameterSpecException, InvalidKeyException {
        this.cryptoState.processNextSetupMessage(msg);
        if (this.cryptoState.hasSetupMessage()) {
            this.channelReadHelpService(ctx);
        }
        if (this.cryptoState.hasFailed()) {
            this.channelReadHelpWorker(ctx);
        }
        if (this.cryptoState.isReady()) {
            this.channelReadHelpAssist(ctx);
        }
    }

    private void channelReadHelpAssist(ChannelHandlerContext ctx) throws CommunicationsDeviation {
        Channel ch = ctx.channel();
        CommunicationsConnection connection = new CommunicationsConnection(ch, this.cryptoState.obtainTheirIdentity());
        ch.attr(CommunicationsConnection.CONNECTION_ATTR).set(connection);
        this.authenticatedPromise.setSuccess(null);
        if (this.isServer) {
            this.handler.newConnection(connection);
        }
    }

    private void channelReadHelpWorker(ChannelHandlerContext ctx) {
        ctx.close();
        this.authenticatedPromise.setFailure(new CommunicationsDeviation("Failed handshake"));
    }

    private void channelReadHelpService(ChannelHandlerContext ctx) throws CommunicationsDeviation, InvalidParameterSpecException, InvalidKeyException {
        byte[] nextMsg = this.cryptoState.obtainNextSetupMessage();
        ctx.writeAndFlush(nextMsg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (this.cryptoState.isReady()) {
            this.writeHerder(ctx, (byte[])msg, promise);
        } else {
            this.writeHome();
        }
    }

    private void writeHome() throws CommunicationsDeviation {
        throw new CommunicationsDeviation("Trying to send data, but cryptostate isn't ready yet!");
    }

    private void writeHerder(ChannelHandlerContext ctx, byte[] msg, ChannelPromise promise) throws Exception {
        byte[] data = this.cryptoState.encrypt(msg);
        super.write(ctx, data, promise);
    }

    public void awaitForPermission(long timeoutmillis) throws CommunicationsDeviation {
        try {
            this.authenticatedPromise.await(timeoutmillis);
            if (!this.authenticatedPromise.isSuccess()) {
                this.awaitForPermissionExecutor();
            }
        }
        catch (InterruptedException e) {
            throw new CommunicationsDeviation(e);
        }
    }

    private void awaitForPermissionExecutor() throws CommunicationsDeviation {
        throw new CommunicationsDeviation(this.authenticatedPromise.cause().getMessage());
    }

    private class CommunicationsNettyHandlerEngine {
        private ChannelHandlerContext ctx;

        public CommunicationsNettyHandlerEngine(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        public void invoke() throws CommunicationsDeviation, InvalidParameterSpecException, InvalidKeyException {
            byte[] setupMsg = CommunicationsNettyHandler.this.cryptoState.obtainNextSetupMessage();
            this.ctx.writeAndFlush(setupMsg);
        }
    }

}

