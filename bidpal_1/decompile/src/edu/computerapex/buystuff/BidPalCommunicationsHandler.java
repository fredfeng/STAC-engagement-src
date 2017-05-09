/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.buystuff;

import edu.computerapex.buyOp.BarterDriver;
import edu.computerapex.buyOp.BarterHandler;
import edu.computerapex.buyOp.BarterHandlerBuilder;
import edu.computerapex.buyOp.BarterParticipantAPI;
import edu.computerapex.buyOp.messagedata.BarterProtoSerializer;
import edu.computerapex.buyOp.messagedata.BarterSerializer;
import edu.computerapex.dialogs.CommunicationsClient;
import edu.computerapex.dialogs.CommunicationsConnection;
import edu.computerapex.dialogs.CommunicationsDeviation;
import edu.computerapex.dialogs.CommunicationsHandler;
import edu.computerapex.dialogs.CommunicationsIdentity;
import edu.computerapex.dialogs.CommunicationsPublicIdentity;
import edu.computerapex.dialogs.CommunicationsServer;
import edu.computerapex.dialogs.Communicator;
import edu.computerapex.origin.BidPalHostParticipantAPI;
import io.netty.channel.EventLoopGroup;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BidPalCommunicationsHandler
implements CommunicationsHandler,
Communicator {
    private CommunicationsServer server;
    private CommunicationsClient client;
    private final int port;
    private final CommunicationsIdentity identity;
    private Map<CommunicationsPublicIdentity, CommunicationsConnection> connections = new HashMap<CommunicationsPublicIdentity, CommunicationsConnection>();
    private BarterHandler barterHandler;
    private BarterDriver driver;

    public BidPalCommunicationsHandler(CommunicationsIdentity identity, int port, int maxBid) {
        this.identity = identity;
        this.port = port;
        BarterProtoSerializer serializer = new BarterProtoSerializer();
        BidPalHostParticipantAPI participantAPI = new BidPalHostParticipantAPI(identity.obtainId());
        this.driver = new BarterDriver(identity, maxBid, this, serializer);
        this.barterHandler = new BarterHandlerBuilder().fixDriver(this.driver).setCommunicator(this).setParticipantAPI(participantAPI).fixSerializer(serializer).setPort(port).defineIdentity(identity).setMaxBid(maxBid).generateBarterHandler();
    }

    public BarterDriver pullDriver() {
        return this.driver;
    }

    public void run() throws CommunicationsDeviation, IOException, Exception {
        this.client = new CommunicationsClient(this, this.identity);
        this.server = new CommunicationsServer(this.port, this, this.identity, this.client.takeEventLoopGroup());
        this.server.serve();
    }

    public void quit() {
        this.server.close();
        this.client.close();
    }

    public void connect(String host, int port) throws CommunicationsDeviation {
        CommunicationsConnection conn = this.client.connect(host, port);
        this.newConnection(conn);
    }

    @Override
    public void handle(CommunicationsConnection conn, byte[] msg) throws CommunicationsDeviation {
        this.barterHandler.handle(conn.pullTheirIdentity(), msg);
    }

    @Override
    public void newConnection(CommunicationsConnection connection) throws CommunicationsDeviation {
        System.out.println("Connected to " + connection.pullTheirIdentity().takeId());
        this.connections.put(connection.pullTheirIdentity(), connection);
        this.barterHandler.addParticipant(connection.pullTheirIdentity());
    }

    public synchronized void closeConnections() throws CommunicationsDeviation {
        for (CommunicationsConnection conn : this.connections.values()) {
            try {
                conn.close();
            }
            catch (CommunicationsDeviation e) {
                System.err.println("Error closing CommsConnection " + conn + "\n" + e.getMessage());
            }
            this.barterHandler.removeParticipant(conn.pullTheirIdentity());
        }
        this.connections.clear();
    }

    @Override
    public void closedConnection(CommunicationsConnection connection) throws CommunicationsDeviation {
        this.connections.remove(connection);
        this.barterHandler.removeParticipant(connection.pullTheirIdentity());
    }

    @Override
    public void deliver(CommunicationsPublicIdentity participant, byte[] msg) throws CommunicationsDeviation {
        CommunicationsConnection conn = this.connections.get(participant);
        if (conn == null) {
            this.deliverSupervisor(participant);
        } else {
            conn.write(msg);
        }
    }

    private void deliverSupervisor(CommunicationsPublicIdentity participant) throws CommunicationsDeviation {
        throw new CommunicationsDeviation("Unknown user " + participant.takeId());
    }
}

