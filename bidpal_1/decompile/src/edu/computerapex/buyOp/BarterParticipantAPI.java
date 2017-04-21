/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.buyOp;

import edu.computerapex.buyOp.messagedata.BarterMessageData;
import edu.computerapex.buyOp.messagedata.BidCommitmentData;
import edu.computerapex.buyOp.messagedata.BidDivulgeData;
import edu.computerapex.buyOp.messagedata.ExchangeData;
import edu.computerapex.dialogs.CommunicationsPublicIdentity;

public interface BarterParticipantAPI {
    public void newBarter(CommunicationsPublicIdentity var1, BarterMessageData.BarterStart var2);

    public void bidCommitmentReceived(CommunicationsPublicIdentity var1, BidCommitmentData var2);

    public void bidMeasurementReceived(CommunicationsPublicIdentity var1, ExchangeData var2);

    public void bidReceiptReceived(CommunicationsPublicIdentity var1, BarterMessageData.BidReceipt var2);

    public void biddingEnded(CommunicationsPublicIdentity var1, BarterMessageData.BiddingOver var2);

    public void concessionReceived(CommunicationsPublicIdentity var1, BarterMessageData.Concession var2);

    public void winClaimReceived(CommunicationsPublicIdentity var1, BidDivulgeData var2);

    public void barterOver(CommunicationsPublicIdentity var1, BarterMessageData.BarterEnd var2);
}

