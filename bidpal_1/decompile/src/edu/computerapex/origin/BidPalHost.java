/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.origin;

import edu.computerapex.buyOp.BarterDriver;
import edu.computerapex.buyOp.BiddersStatus;
import edu.computerapex.buyOp.bad.BarterDeviation;
import edu.computerapex.buyOp.bad.UnknownBarterDeviation;
import edu.computerapex.buystuff.BidPalCommunicationsHandler;
import edu.computerapex.dialogs.CommunicationsDeviation;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class BidPalHost {
    BarterDriver driver;
    BidPalCommunicationsHandler handler;

    public BidPalHost(BarterDriver driver, BidPalCommunicationsHandler handler) {
        this.handler = handler;
        this.driver = handler.pullDriver();
    }

    public void run() {
        BidPalHost.printParticipantMsg("Commands: ");
        BidPalHost.printParticipantMsg("/connect <host> <port>");
        BidPalHost.printParticipantMsg(" /start <description>");
        BidPalHost.printParticipantMsg(" /listauctions");
        BidPalHost.printParticipantMsg(" /status <auctionID>");
        BidPalHost.printParticipantMsg(" /bid <auctionID> <amount>");
        BidPalHost.printParticipantMsg(" /close <auctionID> ");
        BidPalHost.printParticipantMsg(" /listbidders <auctionID>");
        BidPalHost.printParticipantMsg(" /winner <auctionID> <winner> <winningBid>");
        BidPalHost.printParticipantMsg(" /quit");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line = "";
        do {
            try {
                do {
                    line = in.readLine();
                    BidPalHost.printParticipantMsg("Processing command " + line);
                    String[] args = new String[]{};
                    if (line != null) {
                        args = line.split(" ");
                    }
                    if (line == null || "/quit".equalsIgnoreCase(line)) {
                        BidPalHost.printParticipantMsg("quitting ");
                        this.handler.closeConnections();
                        this.handler.quit();
                        BidPalHost.printParticipantMsg("quit!");
                        return;
                    }
                    if (line.startsWith("/connect ")) {
                        if (args.length < 3) {
                            this.runExecutor();
                            continue;
                        }
                        String host = args[1];
                        int port = Integer.parseInt(args[2]);
                        this.handler.connect(host, port);
                        BidPalHost.printParticipantMsg("Connected!");
                        continue;
                    }
                    if (line.startsWith("/bid")) {
                        if (args.length < 3) {
                            this.runEntity();
                            continue;
                        }
                        this.runCoordinator(args);
                        continue;
                    }
                    if (line.startsWith("/startid")) {
                        if (args.length < 3) {
                            this.runUtility();
                        }
                        String rest = line.substring(line.indexOf(32)).trim();
                        String[] params = rest.split(" ");
                        String id = params[0];
                        String desc = rest.substring(rest.indexOf(32));
                        this.driver.startBarter(id, desc);
                        continue;
                    }
                    if (line.startsWith("/start")) {
                        this.runWorker(line);
                        continue;
                    }
                    if (line.startsWith("/close")) {
                        if (args.length < 2) {
                            this.runTarget();
                            continue;
                        }
                        this.runHelper(args[1]);
                        continue;
                    }
                    if (line.startsWith("/listbidders")) {
                        if (args.length < 2) {
                            BidPalHost.printParticipantMsg("-Invalid, must specify auctionID");
                            continue;
                        }
                        this.runHome(args[1]);
                        continue;
                    }
                    if (line.startsWith("/listauctions")) {
                        Map<String, String> barterStatus = this.driver.obtainBartersStatusStrings();
                        ArrayList<String> keys = new ArrayList<String>(barterStatus.keySet());
                        Collections.sort(keys);
                        BidPalHost.printParticipantMsg("Auctions:");
                        int c = 0;
                        while (c < keys.size()) {
                            while (c < keys.size() && Math.random() < 0.6) {
                                while (c < keys.size() && Math.random() < 0.6) {
                                    while (c < keys.size() && Math.random() < 0.6) {
                                        String id = keys.get(c);
                                        BidPalHost.printParticipantMsg("Auction " + id + ": \n" + barterStatus.get(id));
                                        ++c;
                                    }
                                }
                            }
                        }
                        BidPalHost.printParticipantMsg("------------\n");
                        continue;
                    }
                    if (line.startsWith("/status")) {
                        if (args.length < 2) {
                            this.runAid();
                            continue;
                        }
                        String id = args[1];
                        String status = this.driver.grabBarterStatus(id);
                        BidPalHost.printParticipantMsg("Auction status:" + id + ": \n" + status + "\n ----------------------- \n");
                        continue;
                    }
                    if (line.startsWith("/winner")) {
                        this.runHelp(args);
                        continue;
                    }
                    this.runGateKeeper(line);
                } while (true);
            }
            catch (Exception e) {
                System.out.println("error processing command " + line + ": " + e.getMessage());
                e.printStackTrace();
                continue;
            }
        } while (true);
    }

    private void runGateKeeper(String line) {
        System.out.println("unknown command " + line);
    }

    private void runHelp(String[] args) throws CommunicationsDeviation, BarterDeviation {
        new BidPalHostAssist(args).invoke();
    }

    private void runAid() {
        BidPalHost.printParticipantMsg("-Invalid, must specify auctionId");
    }

    private void runHome(String arg) throws UnknownBarterDeviation {
        BiddersStatus status = this.driver.getBiddersStatus(arg);
        BidPalHost.printParticipantMsg("Bidder status for auction " + arg + ":");
        BidPalHost.printParticipantMsg(status.toString() + "\n------------------------\n");
    }

    private void runHelper(String arg) throws CommunicationsDeviation, BarterDeviation {
        this.driver.closeBarter(arg);
    }

    private void runTarget() {
        BidPalHost.printParticipantMsg("-Invalid, must specify auctionID");
    }

    private void runWorker(String line) throws CommunicationsDeviation {
        String description = line.substring(line.indexOf(32));
        this.driver.startBarter(description);
    }

    private void runUtility() {
        BidPalHost.printParticipantMsg("must specify auctionId and item description.  If you prefer a randomly assigned auctionId, use /start <desc> command");
    }

    private void runCoordinator(String[] args) throws Exception {
        this.driver.makeBid(args[1], Integer.parseInt(args[2]));
    }

    private void runEntity() {
        BidPalHost.printParticipantMsg("-Invalid, must specify auctionID and amount");
    }

    private void runExecutor() {
        BidPalHost.printParticipantMsg("-Invalid, must specify host and port");
    }

    private static void printParticipantMsg(String line) {
        System.out.println("> " + line);
    }

    private class BidPalHostAssist {
        private String[] args;

        public BidPalHostAssist(String[] args) {
            this.args = args;
        }

        public void invoke() throws CommunicationsDeviation, BarterDeviation {
            if (this.args.length < 4) {
                BidPalHost.printParticipantMsg("-Invalid, must specify auctionId, winner, and winning bid");
            } else {
                BidPalHost.this.driver.announceWinner(this.args[1], this.args[2], Integer.parseInt(this.args[3]));
            }
        }
    }

}

