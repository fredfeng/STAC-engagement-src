/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.buyOp;

import edu.computerapex.buyOp.BiddersStatus;
import edu.computerapex.buyOp.bad.IllegalOperationDeviation;
import edu.computerapex.buyOp.bad.RebidDeviation;
import edu.computerapex.buyOp.messagedata.BidCommitmentData;
import edu.computerapex.buyOp.messagedata.BidDivulgeData;
import edu.computerapex.dialogs.CommunicationsPublicIdentity;
import edu.computerapex.logging.Logger;
import edu.computerapex.logging.LoggerFactory;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class Barter {
    private static final Logger logger = LoggerFactory.takeLogger(Barter.class);
    private String barterId;
    private String barterDescription;
    private HashMap<CommunicationsPublicIdentity, BidData> offers = new HashMap();
    private boolean winning = true;
    private BidDivulgeData myCommitment;
    private CommunicationsPublicIdentity seller;
    private boolean sellerIsMe;
    private boolean ended = false;
    private String winner;
    private int winningBid;
    private BiddersStatus biddersStatus = new BiddersStatus();

    public Barter(String barterId, CommunicationsPublicIdentity seller, String barterDescription, boolean iAmSeller) {
        this.barterId = barterId;
        this.barterDescription = barterDescription;
        this.seller = seller;
        this.sellerIsMe = iAmSeller;
    }

    public String fetchStatusString() {
        String status = "description: " + this.barterDescription + "\n";
        status = this.amISeller() ? status + "I am the seller --" + this.seller.takeId() + ". \n" : status + "Seller is " + this.seller.takeId() + ".\n";
        status = !this.ended ? status + "Status: open. \n" : (this.winner != null ? status + "Status: won by " + this.winner + " for $" + this.winningBid + ". \n" : status + "Status: bidding ended, but winner not yet announced. \n");
        status = this.myCommitment == null ? status + "I did not bid.\n" : status + "I bid $" + this.myCommitment.grabBid() + ".\n";
        return status;
    }

    public boolean amISeller() {
        return this.sellerIsMe;
    }

    public void addCommitment(CommunicationsPublicIdentity participant, BidCommitmentData commitment) throws RebidDeviation {
        if (this.offers.containsKey(participant)) {
            throw new RebidDeviation("User " + participant + " has sent more than one bid for auction " + this.barterId);
        }
        BidData data = new BidData(commitment);
        this.offers.put(participant, data);
        this.biddersStatus.addBidder(participant);
    }

    public void recordMyCommit(BidDivulgeData commitment, CommunicationsPublicIdentity myIdentity) throws IllegalOperationDeviation, RebidDeviation {
        this.myCommitment = commitment;
        this.biddersStatus.addBidder(myIdentity);
        this.addCommitment(myIdentity, commitment.fetchCommitmentData(myIdentity));
        this.addMeasurement(myIdentity, true);
    }

    public BidDivulgeData takeMyCommit() {
        return this.myCommitment;
    }

    public BidCommitmentData fetchBidCommitment(CommunicationsPublicIdentity participant) {
        return this.offers.get(participant).pullCommitment();
    }

    public void removeBid(CommunicationsPublicIdentity participant) {
        this.offers.remove(participant);
        this.biddersStatus.removeBidder(participant);
    }

    public void addMeasurement(CommunicationsPublicIdentity participant, boolean mineAsBig) throws IllegalOperationDeviation {
        BidData data;
        if (!mineAsBig) {
            this.winning = false;
        }
        if ((data = this.offers.get(participant)) == null) {
            StringBuilder builder = new StringBuilder();
            for (CommunicationsPublicIdentity bidder : this.offers.keySet()) {
                this.addMeasurementAid(builder, bidder);
            }
            throw new IllegalOperationDeviation("Received bid comparison from " + participant.takeId() + " but never received a bid commitment. Have these bidders: \n" + builder.toString());
        }
        data.setMeasurement(mineAsBig);
    }

    private void addMeasurementAid(StringBuilder builder, CommunicationsPublicIdentity bidder) {
        builder.append("have bidder " + bidder.toString());
        builder.append('\n');
    }

    public void addConcession(CommunicationsPublicIdentity participant) {
        BidData data = this.offers.get(participant);
        data.concede();
        this.biddersStatus.addConcession(participant);
    }

    public void addWinClaim(CommunicationsPublicIdentity participant, int bid) {
        BidData data = this.offers.get(participant);
        data.claim(bid);
        this.biddersStatus.addWinClaim(participant, bid);
    }

    public Integer fetchExpectedWinningBid(String participantId) {
        for (CommunicationsPublicIdentity participant : this.offers.keySet()) {
            Integer barterAid = this.fetchExpectedWinningBidUtility(participantId, participant);
            if (barterAid == null) continue;
            return barterAid;
        }
        return null;
    }

    private Integer fetchExpectedWinningBidUtility(String participantId, CommunicationsPublicIdentity participant) {
        BarterAid barterAid = new BarterAid(participantId, participant).invoke();
        if (barterAid.is()) {
            return barterAid.fetchData().getClaimingBid();
        }
        return null;
    }

    public BiddersStatus obtainBiddersStatus() {
        return this.biddersStatus;
    }

    public int countBidsAboveMine() {
        int count = 0;
        for (BidData data : this.offers.values()) {
            if (data.mineAsBig.booleanValue()) continue;
            ++count;
        }
        return count;
    }

    public boolean isConsistentWithMeasurement(CommunicationsPublicIdentity participant, int revealedBid) {
        if (this.myCommitment == null) {
            return true;
        }
        boolean claimedResult = this.takeMyCommit().grabBid() >= revealedBid;
        boolean recordedResult = this.wasMineAsBig(participant);
        logger.info("claimedResult: mineAsBig? " + claimedResult);
        logger.info("recordedResult: mineAsBig? " + recordedResult);
        return claimedResult == recordedResult;
    }

    public boolean wasMineAsBig(CommunicationsPublicIdentity participant) {
        return this.offers.get(participant).mineAsBig;
    }

    public boolean amIWinning() {
        return this.myCommitment != null && this.winning;
    }

    public boolean didIBid() {
        return this.myCommitment != null;
    }

    public void fixOver() {
        this.ended = true;
    }

    public boolean isOver() {
        return this.ended;
    }

    public boolean verifySeller(CommunicationsPublicIdentity participant) {
        return participant.equals(this.seller);
    }

    public boolean verifyHighest(int claimedWinningBid) {
        BiddersStatus status = this.obtainBiddersStatus();
        return status.verifyHighest(claimedWinningBid);
    }

    public void assignWinner(String winner, int winningBid) {
        this.winner = winner;
        this.winningBid = winningBid;
    }

    private class BarterAid {
        private boolean myResult;
        private String participantId;
        private CommunicationsPublicIdentity participant;
        private BidData data;

        public BarterAid(String participantId, CommunicationsPublicIdentity participant) {
            this.participantId = participantId;
            this.participant = participant;
        }

        boolean is() {
            return this.myResult;
        }

        public BidData fetchData() {
            return this.data;
        }

        public BarterAid invoke() {
            if (this.participant.takeId().equals(this.participantId)) {
                return this.invokeFunction();
            }
            this.myResult = false;
            return this;
        }

        private BarterAid invokeFunction() {
            this.data = (BidData)Barter.this.offers.get(this.participant);
            this.myResult = true;
            return this;
        }
    }

    private class BidData {
        private BidCommitmentData commitment;
        private Boolean mineAsBig;
        private ClaimOrConcession claimStatus;

        private BidData(BidCommitmentData com) {
            this.mineAsBig = null;
            this.claimStatus = null;
            this.commitment = com;
        }

        private BidCommitmentData pullCommitment() {
            return this.commitment;
        }

        private void setMeasurement(boolean mineAsBig) {
            this.mineAsBig = mineAsBig;
        }

        private void claim(int bid) {
            this.claimStatus = new ClaimOrConcession(this, bid);
        }

        private void concede() {
            this.claimStatus = new ClaimOrConcession(this);
        }

        private Integer getClaimingBid() {
            if (this.claimStatus == null || this.claimStatus.isConceded()) {
                return null;
            }
            return this.claimStatus.bid;
        }

        private class ClaimOrConcession {
            private int bid;
            final /* synthetic */ BidData this$1;

            private ClaimOrConcession(BidData bidData, int bid) {
                this.this$1 = bidData;
                this.bid = -1;
                this.bid = bid;
            }

            private ClaimOrConcession(BidData bidData) {
                this.this$1 = bidData;
                this.bid = -1;
            }

            private boolean isConceded() {
                return this.bid < 0;
            }
        }

    }

}

