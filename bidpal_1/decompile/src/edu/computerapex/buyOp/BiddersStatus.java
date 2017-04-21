/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.buyOp;

import edu.computerapex.dialogs.CommunicationsPublicIdentity;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class BiddersStatus {
    private Map<CommunicationsPublicIdentity, Integer> claimedWinners = new HashMap<CommunicationsPublicIdentity, Integer>();
    private Set<CommunicationsPublicIdentity> conceders = new TreeSet<CommunicationsPublicIdentity>();
    private Set<CommunicationsPublicIdentity> biddersNotReported = new TreeSet<CommunicationsPublicIdentity>();

    public boolean verifyHighest(int claimedBid) {
        int highest = 0;
        for (CommunicationsPublicIdentity participant : this.claimedWinners.keySet()) {
            int bid = this.claimedWinners.get(participant);
            if (bid <= highest) continue;
            highest = bid;
        }
        return claimedBid == highest;
    }

    public void addBidder(CommunicationsPublicIdentity participant) {
        this.biddersNotReported.add(participant);
    }

    public void removeBidder(CommunicationsPublicIdentity participant) {
        this.biddersNotReported.remove(participant);
    }

    public void addConcession(CommunicationsPublicIdentity participant) {
        this.conceders.add(participant);
        this.removeBidder(participant);
    }

    public void addWinClaim(CommunicationsPublicIdentity participant, int bid) {
        this.claimedWinners.put(participant, bid);
        this.removeBidder(participant);
    }

    public String toString() {
        String result = "";
        result = result + "Bidders who claim to have won:\n";
        for (CommunicationsPublicIdentity participant : new TreeSet<CommunicationsPublicIdentity>(this.claimedWinners.keySet())) {
            result = result + participant.takeId() + ", with bid of $" + this.claimedWinners.get(participant) + "\n";
        }
        result = result + "Bidders who have conceded:\n";
        for (CommunicationsPublicIdentity participant : this.conceders) {
            result = result + participant.takeId() + "\n";
        }
        result = result + "Remaining bidders:\n";
        for (CommunicationsPublicIdentity participant : this.biddersNotReported) {
            result = result + participant.takeId() + "\n";
        }
        return result;
    }
}

