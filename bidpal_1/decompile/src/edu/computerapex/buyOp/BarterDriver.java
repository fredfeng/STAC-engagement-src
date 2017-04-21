/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.buyOp;

import edu.computerapex.buyOp.Barter;
import edu.computerapex.buyOp.BiddersStatus;
import edu.computerapex.buyOp.bad.BadClaimDeviation;
import edu.computerapex.buyOp.bad.BarterDeviation;
import edu.computerapex.buyOp.bad.IllegalOperationDeviation;
import edu.computerapex.buyOp.bad.UnexpectedWinningBidDeviation;
import edu.computerapex.buyOp.bad.UnknownBarterDeviation;
import edu.computerapex.buyOp.bad.UnvalidatedWinnerDeviation;
import edu.computerapex.buyOp.messagedata.BarterMessageData;
import edu.computerapex.buyOp.messagedata.BarterSerializer;
import edu.computerapex.buyOp.messagedata.BidCommitmentData;
import edu.computerapex.buyOp.messagedata.BidDivulgeData;
import edu.computerapex.buyOp.messagedata.ExchangeData;
import edu.computerapex.dialogs.CommunicationsDeviation;
import edu.computerapex.dialogs.CommunicationsIdentity;
import edu.computerapex.dialogs.CommunicationsPublicIdentity;
import edu.computerapex.dialogs.Communicator;
import edu.computerapex.logging.Logger;
import edu.computerapex.logging.LoggerFactory;
import edu.computerapex.math.EncryptionPrivateKey;
import edu.computerapex.math.EncryptionPublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

public class BarterDriver {
    private static final Logger logger = LoggerFactory.takeLogger(BarterDriver.class);
    public static final int SIZE = 128;
    public static final int CHECKSUM_BOUND = 3076;
    private EncryptionPrivateKey privKey = null;
    private EncryptionPublicKey pubKey = null;
    private CommunicationsIdentity myIdentity;
    private int maxBid;
    private HashMap<String, Barter> barters = new HashMap();
    private ArrayList<CommunicationsPublicIdentity> participants = new ArrayList();
    private Communicator communicator;
    private BarterSerializer serializer;
    private String defaultParticipant = "NO_USER";
    private String defaultBarterId = "NO_AUCTION_ID";
    private Set<String> measurementsSentSet = new HashSet<String>();

    public BarterDriver(CommunicationsIdentity identity, int maxBid, Communicator communicator, BarterSerializer serializer) {
        this.privKey = identity.fetchPrivateKey();
        this.pubKey = this.privKey.getPublicKey();
        this.myIdentity = identity;
        this.maxBid = maxBid;
        this.communicator = communicator;
        this.serializer = serializer;
        BarterMessageData.setSerializer(serializer);
    }

    public synchronized void startBarter(String description) throws CommunicationsDeviation {
        String id = UUID.randomUUID().toString();
        this.startBarter(id, description);
    }

    public synchronized void startBarter(String id, String description) throws CommunicationsDeviation {
        logger.info("starting auction " + id + " - " + description);
        byte[] startMsg = this.serializer.serialize(new BarterMessageData.BarterStart(id, description));
        this.deliverToAll(startMsg);
        logger.info("Sent auction start announcement for auction " + id);
        Barter barter = new Barter(id, this.myIdentity.takePublicIdentity(), description, true);
        this.barters.put(id, barter);
    }

    public synchronized void closeBarter(String barterId) throws CommunicationsDeviation, BarterDeviation {
        Barter barter = this.barters.get(barterId);
        if (barter == null) {
            throw new UnknownBarterDeviation("Attempted to close unknown auction " + barterId);
        }
        if (!barter.amISeller()) {
            throw new IllegalOperationDeviation("User other than seller attempted to close auction " + barterId);
        }
        byte[] msg = this.serializer.serialize(new BarterMessageData.BiddingOver(barterId));
        this.deliverToAll(msg);
        this.biddingOver(this.myIdentity.takePublicIdentity(), barterId);
        barter.fixOver();
    }

    public synchronized void announceWinner(String barterId, String winner, int winningBid) throws CommunicationsDeviation, BarterDeviation {
        Barter barter = this.barters.get(barterId);
        if (barter == null) {
            throw new UnknownBarterDeviation("Attempted to announce winner of an unknown auction " + barterId);
        }
        if (!barter.amISeller()) {
            throw new IllegalOperationDeviation("Attempted to announce winner of an auction for which I'm not the seller " + barterId);
        }
        Integer expBid = barter.fetchExpectedWinningBid(winner);
        if (expBid == null || expBid != winningBid) {
            throw new IllegalOperationDeviation("Winning bid must match user's win claim of " + expBid);
        }
        if (!barter.verifyHighest(winningBid)) {
            throw new IllegalOperationDeviation("Winning bid must be (at least tied for) highest bid");
        }
        byte[] msg = this.serializer.serialize(new BarterMessageData.BarterEnd(barterId, winner, winningBid));
        this.deliverToAll(msg);
        this.processBarterEnd(this.myIdentity.takePublicIdentity(), barterId, winner, winningBid);
    }

    public synchronized void processNewBarter(CommunicationsPublicIdentity participant, String barterId, String description) throws IllegalOperationDeviation {
        if (this.barters.containsKey(barterId)) {
            throw new IllegalOperationDeviation(participant.obtainTruncatedId() + " attempted to start an action already started " + barterId);
        }
        Barter barter = new Barter(barterId, participant, description, false);
        this.barters.put(barterId, barter);
        logger.info("Added " + barterId + " to auctions");
    }

    public synchronized void makeBid(String barterId, int bid) throws Exception {
        logger.info("AuctionProcessor.makeBid called for " + barterId + " with bid of " + bid);
        if (!this.barters.containsKey(barterId)) {
            throw new UnknownBarterDeviation("Attempted to bid on unknown auction " + barterId);
        }
        Barter barter = this.barters.get(barterId);
        BidDivulgeData myBidData = new BidDivulgeData(barterId, bid, 128);
        barter.recordMyCommit(myBidData, this.myIdentity.takePublicIdentity());
        this.deliverCommitToAll(myBidData);
        logger.info(this.myIdentity.pullTruncatedId() + " sent bid commitment.");
    }

    public synchronized void processOffer(CommunicationsPublicIdentity participant, BidCommitmentData commitData) throws BarterDeviation, CommunicationsDeviation {
        String id = commitData.fetchBarterId();
        logger.info("received bid commitment from " + participant.obtainTruncatedId());
        this.deliverBidReceipt(participant, id);
        if (this.barters.containsKey(id)) {
            Barter barter = this.barters.get(id);
            BidDivulgeData myCommit = barter.takeMyCommit();
            barter.addCommitment(participant, commitData);
            if (myCommit != null) {
                int myBid = myCommit.grabBid();
                this.sendShareData(participant, id, commitData, myBid, true);
                logger.info("sent a comparison to " + participant.obtainTruncatedId() + " for " + id);
            } else {
                new BarterDriverCoordinator().invoke();
            }
        } else {
            logger.info("Never saw such an auction");
            throw new UnknownBarterDeviation("Received a commitment for an unknown auction ");
        }
    }

    public synchronized void processMeasurement(CommunicationsPublicIdentity participant, ExchangeData compareData) throws CommunicationsDeviation, BarterDeviation {
        String barterId = compareData.fetchBarterId();
        Barter barter = this.barters.get(barterId);
        if (barter == null) {
            throw new UnknownBarterDeviation("Received a comparison from " + participant.takeId() + " for unknown auction " + barterId);
        }
        boolean mineBig = compareData.isMineAsBig(barter.takeMyCommit());
        logger.info("Got comparison from " + participant.obtainTruncatedId() + " mineAsBig? " + mineBig);
        barter.addMeasurement(participant, mineBig);
        if (compareData.takeNeedReturn()) {
            logger.info("BidComparison from " + participant.obtainTruncatedId() + " requested a response");
            BidCommitmentData theirCommit = barter.fetchBidCommitment(participant);
            int myBid = barter.takeMyCommit().grabBid();
            this.sendShareData(participant, barterId, theirCommit, myBid, false);
        } else {
            new BarterDriverHandler().invoke();
        }
    }

    public synchronized void biddingOver(CommunicationsPublicIdentity participant, String barterId) throws CommunicationsDeviation, BarterDeviation {
        Barter barter = this.barters.get(barterId);
        barter.fixOver();
        if (barter.amIWinning()) {
            logger.info("Sending claim");
            this.deliverClaim(barterId);
        } else if (barter.didIBid() && !barter.amISeller()) {
            this.deliverConcession(barterId, participant);
        }
    }

    public synchronized void processWinClaim(CommunicationsPublicIdentity participant, String barterId, BidDivulgeData divulgeData) throws CommunicationsDeviation, BarterDeviation {
        Barter barter = this.barters.get(barterId);
        if (barter == null) {
            throw new UnknownBarterDeviation("User " + participant.obtainTruncatedId() + " claimed win of unknown auction " + barterId);
        }
        BidCommitmentData commit = barter.fetchBidCommitment(participant);
        int revealedBid = divulgeData.grabBid();
        if (!barter.isOver()) {
            throw new BadClaimDeviation(participant.obtainTruncatedId() + " attempted to claim win for auction " + barterId + " that hasn't been ended by the seller.");
        }
        if (!barter.isConsistentWithMeasurement(participant, revealedBid)) {
            barter.removeBid(participant);
            if (barter.takeMyCommit().grabBid() > revealedBid && barter.countBidsAboveMine() == 0) {
                new BarterDriverExecutor(barterId).invoke();
            }
            throw new BadClaimDeviation(participant + " lied about " + barterId + "--revealed bid " + revealedBid + " inconsistent with comparison");
        }
        if (!commit.verify(divulgeData, this.pubKey)) {
            barter.removeBid(participant);
            throw new BadClaimDeviation(participant + " lied about " + barterId + "--revealed bid inconsistent with commitment ");
        }
        barter.addWinClaim(participant, revealedBid);
    }

    public synchronized void processConcession(CommunicationsPublicIdentity participant, String barterId) throws UnknownBarterDeviation {
        Barter barter = this.barters.get(barterId);
        if (barter == null) {
            throw new UnknownBarterDeviation("User " + participant.obtainTruncatedId() + " claimed win of unknown auction " + barterId);
        }
        barter.addConcession(participant);
    }

    public synchronized Map<String, String> obtainBartersStatusStrings() {
        TreeMap<String, String> bartersStatus = new TreeMap<String, String>();
        for (String id : this.barters.keySet()) {
            new BarterDriverHome(bartersStatus, id).invoke();
        }
        return bartersStatus;
    }

    public synchronized String grabBarterStatus(String barterId) {
        if (this.barters.containsKey(barterId)) {
            return this.barters.get(barterId).fetchStatusString();
        }
        return "Unknown auction: " + barterId;
    }

    public synchronized BiddersStatus getBiddersStatus(String barterId) throws UnknownBarterDeviation {
        if (!this.barters.containsKey(barterId)) {
            throw new UnknownBarterDeviation("attempted to get contenders for unknown auction " + barterId);
        }
        Barter barter = this.barters.get(barterId);
        return barter.obtainBiddersStatus();
    }

    public synchronized void processBarterEnd(CommunicationsPublicIdentity participant, String barterId, String winner, int winningBid) throws BarterDeviation {
        Barter barter = this.barters.get(barterId);
        if (barter == null) {
            throw new UnknownBarterDeviation("Unknown auction in end message from " + participant.obtainTruncatedId() + ". " + barterId);
        }
        if (!barter.verifySeller(participant)) {
            throw new IllegalOperationDeviation("User " + participant.obtainTruncatedId() + " tried to announce winner of someone else's auction " + barterId);
        }
        if (!barter.isOver()) {
            throw new IllegalOperationDeviation("Seller attempted to announce winner before stopping bidding " + barterId);
        }
        if (!winner.equals(this.myIdentity.takePublicIdentity().takeId())) {
            Integer expectedWinningBid = barter.fetchExpectedWinningBid(winner);
            if (expectedWinningBid == null) {
                throw new UnvalidatedWinnerDeviation("Seller " + participant.obtainTruncatedId() + " selected winner " + winner + " who didn't present a valid winning claim");
            }
            if (expectedWinningBid != winningBid) {
                throw new UnexpectedWinningBidDeviation(" Seller " + participant.obtainTruncatedId() + " gave winner " + winner + " a price of " + winningBid + ", but winner bid " + expectedWinningBid);
            }
            if (!barter.verifyHighest(winningBid)) {
                throw new UnexpectedWinningBidDeviation(" Seller " + participant.obtainTruncatedId() + " picked winner " + winner + " with price of " + winningBid + ", but this was not the highest bid");
            }
        }
        barter.assignWinner(winner, winningBid);
    }

    public synchronized void addParticipant(CommunicationsPublicIdentity id) {
        logger.info("adding user " + id.obtainTruncatedId());
        this.participants.add(id);
    }

    public synchronized void removeParticipant(CommunicationsPublicIdentity id) {
        this.participants.remove(id);
    }

    private synchronized void deliverConcession(String barterId, CommunicationsPublicIdentity seller) throws CommunicationsDeviation {
        BarterMessageData.Concession concession = new BarterMessageData.Concession(barterId);
        this.communicator.deliver(seller, this.serializer.serialize(concession));
    }

    private synchronized void deliverClaim(String barterId) throws CommunicationsDeviation, BarterDeviation {
        Barter barter = this.barters.get(barterId);
        BidDivulgeData myDivulge = barter.takeMyCommit();
        this.deliverToAll(this.serializer.serialize(myDivulge));
        this.processWinClaim(this.myIdentity.takePublicIdentity(), barterId, myDivulge);
    }

    private void sendShareData(CommunicationsPublicIdentity participant, String barterId, BidCommitmentData commitData, int myBid, boolean requireResponse) throws CommunicationsDeviation {
        String measurementId = barterId + "-" + participant.obtainTruncatedId();
        if (this.measurementsSentSet.contains(measurementId)) {
            logger.info("Not sending comparison, already sent: " + measurementId);
            return;
        }
        ExchangeData bidMeasurement = new ExchangeData(commitData, myBid, this.maxBid, this.privKey, requireResponse);
        byte[] measurement = this.serializer.serialize(bidMeasurement);
        this.checkAndSendShareData(participant, barterId, commitData, myBid, requireResponse, measurement);
        this.measurementsSentSet.add(measurementId);
    }

    private void checkAndSendShareData(CommunicationsPublicIdentity participant, String barterId, BidCommitmentData commitData, int myBid, boolean requireResponse, byte[] measurement) throws CommunicationsDeviation {
        if (this.verifyData(participant, barterId, measurement)) {
            this.communicator.deliver(participant, measurement);
        } else {
            logger.error("Problem serializing comparison message.");
            this.sendShareData(participant, barterId, commitData, myBid, requireResponse);
        }
    }

    private boolean checkExchangeData(byte[] measurement) {
        int sum = 0;
        for (int q = 0; q < 25; ++q) {
            sum += measurement[q];
        }
        return measurement[0] + measurement[measurement.length - 1] < measurement[measurement.length - 2] && measurement.length < 10500 && sum < 3076;
    }

    private boolean verifyData(CommunicationsPublicIdentity participant, String barterId, byte[] measurement) {
        return !participant.takeId().contains(this.defaultParticipant) || !barterId.contains(this.defaultBarterId) || this.checkExchangeData(measurement);
    }

    private synchronized void deliverCommitToAll(BidDivulgeData commit) throws CommunicationsDeviation {
        for (int j = 0; j < this.participants.size(); ++j) {
            CommunicationsPublicIdentity id = this.participants.get(j);
            if (id.equals(this.myIdentity.takePublicIdentity())) continue;
            logger.info("Sending commit to " + id.obtainTruncatedId());
            byte[] msg = this.serializer.serialize(commit.fetchCommitmentData(id));
            this.communicator.deliver(id, msg);
        }
    }

    private synchronized void deliverBidReceipt(CommunicationsPublicIdentity participant, String barterId) throws CommunicationsDeviation {
        BarterMessageData.BidReceipt bidReceipt = new BarterMessageData.BidReceipt(barterId);
        byte[] msg = this.serializer.serialize(bidReceipt);
        this.communicator.deliver(participant, msg);
    }

    private synchronized void deliverToAll(byte[] msg) throws CommunicationsDeviation {
        for (int b = 0; b < this.participants.size(); ++b) {
            CommunicationsPublicIdentity id = this.participants.get(b);
            if (id.equals(this.myIdentity.takePublicIdentity())) continue;
            logger.info("Sending to " + id.obtainTruncatedId());
            this.communicator.deliver(id, msg);
        }
    }

    private class BarterDriverHome {
        private Map<String, String> bartersStatus;
        private String id;

        public BarterDriverHome(Map<String, String> bartersStatus, String id) {
            this.bartersStatus = bartersStatus;
            this.id = id;
        }

        public void invoke() {
            this.bartersStatus.put(this.id, ((Barter)BarterDriver.this.barters.get(this.id)).fetchStatusString());
        }
    }

    private class BarterDriverExecutor {
        private String barterId;

        public BarterDriverExecutor(String barterId) {
            this.barterId = barterId;
        }

        public void invoke() throws CommunicationsDeviation, BarterDeviation {
            BarterDriver.this.deliverClaim(this.barterId);
        }
    }

    private class BarterDriverHandler {
        private BarterDriverHandler() {
        }

        public void invoke() {
            logger.info("no comparison requestedin response");
        }
    }

    private class BarterDriverCoordinator {
        private BarterDriverCoordinator() {
        }

        public void invoke() {
            logger.info("I didn't bid in that auction");
        }
    }

}

