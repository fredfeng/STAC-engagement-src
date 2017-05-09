/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.buyOp;

import edu.computerapex.buyOp.BarterDriver;
import edu.computerapex.buyOp.BarterParticipantAPI;
import edu.computerapex.buyOp.messagedata.BarterMessageData;
import edu.computerapex.buyOp.messagedata.BarterSerializer;
import edu.computerapex.buyOp.messagedata.BidCommitmentData;
import edu.computerapex.buyOp.messagedata.BidDivulgeData;
import edu.computerapex.buyOp.messagedata.ExchangeData;
import edu.computerapex.dialogs.CommunicationsDeviation;
import edu.computerapex.dialogs.CommunicationsIdentity;
import edu.computerapex.dialogs.CommunicationsPublicIdentity;
import edu.computerapex.dialogs.Communicator;
import java.io.PrintStream;

public class BarterHandler {
    private BarterDriver driver;
    private final CommunicationsIdentity identity;
    private BarterSerializer serializer;
    private BarterParticipantAPI participantAPI;

    public BarterHandler(BarterDriver driver, Communicator communicator, BarterParticipantAPI participantAPI, BarterSerializer serializer, int port, CommunicationsIdentity identity, int maxBid) {
        this.serializer = serializer;
        this.identity = identity;
        this.participantAPI = participantAPI;
        this.driver = driver;
    }

    public synchronized void handle(CommunicationsPublicIdentity participant, byte[] msgData) throws CommunicationsDeviation {
        try {
            BarterMessageData data = this.serializer.deserialize(msgData);
            String barterId = data.fetchBarterId();
            switch (data.type) {
                case BID_COMMITMENT: {
                    this.participantAPI.bidCommitmentReceived(participant, (BidCommitmentData)data);
                    this.driver.processOffer(participant, (BidCommitmentData)data);
                    break;
                }
                case BID_COMPARISON: {
                    this.participantAPI.bidMeasurementReceived(participant, (ExchangeData)data);
                    this.driver.processMeasurement(participant, (ExchangeData)data);
                    break;
                }
                case BID_RECEIPT: {
                    this.participantAPI.bidReceiptReceived(participant, (BarterMessageData.BidReceipt)data);
                    break;
                }
                case AUCTION_START: {
                    BarterMessageData.BarterStart startData = (BarterMessageData.BarterStart)data;
                    this.participantAPI.newBarter(participant, startData);
                    this.driver.processNewBarter(participant, barterId, startData.description);
                    break;
                }
                case BIDDING_OVER: {
                    this.participantAPI.biddingEnded(participant, (BarterMessageData.BiddingOver)data);
                    this.driver.biddingOver(participant, barterId);
                    break;
                }
                case AUCTION_END: {
                    BarterMessageData.BarterEnd endData = (BarterMessageData.BarterEnd)data;
                    this.participantAPI.barterOver(participant, endData);
                    this.driver.processBarterEnd(participant, barterId, endData.winner, endData.winningBid);
                    break;
                }
                case CLAIM_WIN: {
                    BidDivulgeData divulgeData = (BidDivulgeData)data;
                    this.participantAPI.winClaimReceived(participant, divulgeData);
                    this.driver.processWinClaim(participant, barterId, divulgeData);
                    break;
                }
                case CONCESSION: {
                    this.participantAPI.concessionReceived(participant, (BarterMessageData.Concession)data);
                    this.driver.processConcession(participant, barterId);
                    break;
                }
                default: {
                    System.err.println(this.identity.obtainId() + " received an unknown message " + (Object)((Object)data.type));
                    break;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void addParticipant(CommunicationsPublicIdentity participant) {
        this.driver.addParticipant(participant);
    }

    public void removeParticipant(CommunicationsPublicIdentity participant) {
        this.driver.removeParticipant(participant);
    }

}

