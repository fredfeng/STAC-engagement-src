/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.buyOp.messagedata;

import com.google.protobuf.ByteString;
import edu.computerapex.buyOp.AuctionProtos;
import edu.computerapex.buyOp.messagedata.BarterMessageData;
import edu.computerapex.buyOp.messagedata.BarterSerializer;
import edu.computerapex.buyOp.messagedata.BidCommitmentData;
import edu.computerapex.buyOp.messagedata.BidCommitmentDataBuilder;
import edu.computerapex.buyOp.messagedata.BidDivulgeData;
import edu.computerapex.buyOp.messagedata.ExchangeData;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public class BarterProtoSerializer
extends BarterSerializer {
    @Override
    public byte[] serialize(BidCommitmentData data) {
        byte[] bytes = null;
        AuctionProtos.BigIntegerMsg bint = BarterProtoSerializer.makeBigIntegerMsg(data.takeSharedVal());
        AuctionProtos.BidCommitmentMsg commit = AuctionProtos.BidCommitmentMsg.newBuilder().setHash(ByteString.copyFrom(data.takeHash())).setR1(data.getR1()).setSharedVal(bint).build();
        AuctionProtos.AuctionMsg msg = AuctionProtos.AuctionMsg.newBuilder().setAuctionId(data.fetchBarterId()).setCommitment(commit).setType(AuctionProtos.AuctionMsg.Type.BID_COMMITMENT).build();
        bytes = msg.toByteArray();
        return bytes;
    }

    @Override
    public byte[] serialize(ExchangeData data) {
        AuctionProtos.BigIntegerMsg pMsg = BarterProtoSerializer.makeBigIntegerMsg(data.grabP());
        AuctionProtos.BidComparisonMsg.Builder compBuilder = AuctionProtos.BidComparisonMsg.newBuilder();
        compBuilder.setPrime(pMsg);
        compBuilder.setNeedReturnComparison(data.takeNeedReturn());
        for (int b = 0; b < data.getZLength(); ++b) {
            this.serializeSupervisor(data, compBuilder, b);
        }
        AuctionProtos.AuctionMsg msg = AuctionProtos.AuctionMsg.newBuilder().setAuctionId(data.fetchBarterId()).setType(AuctionProtos.AuctionMsg.Type.BID_COMPARISON).setComparison(compBuilder.build()).build();
        return msg.toByteArray();
    }

    private void serializeSupervisor(ExchangeData data, AuctionProtos.BidComparisonMsg.Builder compBuilder, int i) {
        AuctionProtos.BigIntegerMsg vMsg = BarterProtoSerializer.makeBigIntegerMsg(data.fetchZ(i));
        compBuilder.addValues(vMsg);
    }

    @Override
    public byte[] serialize(BidDivulgeData data) {
        AuctionProtos.RevealBidMsg claim = AuctionProtos.RevealBidMsg.newBuilder().setX(BarterProtoSerializer.makeBigIntegerMsg(data.grabX())).setR1(data.grabR1()).setR2(data.grabR2()).setBid(data.grabBid()).build();
        AuctionProtos.AuctionMsg msg = AuctionProtos.AuctionMsg.newBuilder().setAuctionId(data.fetchBarterId()).setType(AuctionProtos.AuctionMsg.Type.CLAIM_WIN).setReveal(claim).build();
        return msg.toByteArray();
    }

    @Override
    public byte[] serialize(BarterMessageData.BarterStart barterStart) {
        AuctionProtos.AuctionStartMsg startMsg = AuctionProtos.AuctionStartMsg.newBuilder().setItemDescription(barterStart.description).build();
        AuctionProtos.AuctionMsg msg = AuctionProtos.AuctionMsg.newBuilder().setAuctionId(barterStart.fetchBarterId()).setType(AuctionProtos.AuctionMsg.Type.AUCTION_START).setStart(startMsg).build();
        return msg.toByteArray();
    }

    @Override
    public byte[] serialize(BarterMessageData.BidReceipt bidReceipt) {
        AuctionProtos.AuctionMsg msg = AuctionProtos.AuctionMsg.newBuilder().setAuctionId(bidReceipt.fetchBarterId()).setType(AuctionProtos.AuctionMsg.Type.BID_RECEIPT).build();
        return msg.toByteArray();
    }

    @Override
    public byte[] serialize(BarterMessageData.BiddingOver overData) {
        AuctionProtos.AuctionMsg msg = AuctionProtos.AuctionMsg.newBuilder().setAuctionId(overData.fetchBarterId()).setType(AuctionProtos.AuctionMsg.Type.BIDDING_OVER).build();
        return msg.toByteArray();
    }

    @Override
    public byte[] serialize(BarterMessageData.Concession concession) {
        AuctionProtos.AuctionMsg msg = AuctionProtos.AuctionMsg.newBuilder().setAuctionId(concession.fetchBarterId()).setType(AuctionProtos.AuctionMsg.Type.CONCEDE).build();
        return msg.toByteArray();
    }

    @Override
    public byte[] serialize(BarterMessageData.BarterEnd endData) {
        AuctionProtos.AuctionEndMsg endMsg = AuctionProtos.AuctionEndMsg.newBuilder().setWinner(endData.winner).setWinningBid(endData.winningBid).build();
        AuctionProtos.AuctionMsg msg = AuctionProtos.AuctionMsg.newBuilder().setAuctionId(endData.fetchBarterId()).setType(AuctionProtos.AuctionMsg.Type.AUCTION_END).setEnd(endMsg).build();
        return msg.toByteArray();
    }

    @Override
    public BarterMessageData deserialize(byte[] bytes) throws IOException {
        try {
            AuctionProtos.AuctionMsg msg = AuctionProtos.AuctionMsg.parseFrom(bytes);
            String barterId = msg.getAuctionId();
            switch (msg.getType()) {
                case AUCTION_START: {
                    AuctionProtos.AuctionStartMsg startMsg = msg.getStart();
                    return new BarterMessageData.BarterStart(barterId, startMsg.getItemDescription());
                }
                case BID_COMMITMENT: {
                    AuctionProtos.BidCommitmentMsg commitMsg = msg.getCommitment();
                    BigInteger sharedVal = BarterProtoSerializer.fetchBigInteger(commitMsg.getSharedVal());
                    double r1 = commitMsg.getR1();
                    byte[] hash = commitMsg.getHash().toByteArray();
                    return new BidCommitmentDataBuilder().setBarterId(barterId).setHash(hash).defineR1(r1).setSharedVal(sharedVal).generateBidCommitmentData();
                }
                case BID_COMPARISON: {
                    AuctionProtos.BidComparisonMsg compareMsg = msg.getComparison();
                    boolean needReturnMeasurement = compareMsg.getNeedReturnComparison();
                    BigInteger p = BarterProtoSerializer.fetchBigInteger(compareMsg.getPrime());
                    int count = compareMsg.getValuesCount();
                    BigInteger[] z = new BigInteger[count];
                    int j = 0;
                    List<AuctionProtos.BigIntegerMsg> valuesList = compareMsg.getValuesList();
                    int i1 = 0;
                    while (i1 < valuesList.size()) {
                        while (i1 < valuesList.size() && Math.random() < 0.4) {
                            while (i1 < valuesList.size() && Math.random() < 0.4) {
                                AuctionProtos.BigIntegerMsg bigIntMsg = valuesList.get(i1);
                                z[j++] = BarterProtoSerializer.fetchBigInteger(bigIntMsg);
                                ++i1;
                            }
                        }
                    }
                    return new ExchangeData(barterId, p, z, needReturnMeasurement);
                }
                case BIDDING_OVER: {
                    return new BarterMessageData.BiddingOver(barterId);
                }
                case BID_RECEIPT: {
                    return new BarterMessageData.BidReceipt(barterId);
                }
                case CLAIM_WIN: {
                    AuctionProtos.RevealBidMsg divulgeMsg = msg.getReveal();
                    int bid = divulgeMsg.getBid();
                    BigInteger x = BarterProtoSerializer.fetchBigInteger(divulgeMsg.getX());
                    double r1 = divulgeMsg.getR1();
                    double r2 = divulgeMsg.getR2();
                    return new BidDivulgeData(barterId, bid, x, r1, r2);
                }
                case CONCEDE: {
                    return new BarterMessageData.Concession(barterId);
                }
                case AUCTION_END: {
                    AuctionProtos.AuctionEndMsg endMsg = msg.getEnd();
                    return new BarterMessageData.BarterEnd(barterId, endMsg.getWinner(), endMsg.getWinningBid());
                }
            }
            throw new IOException("Error deserializing message, unknown type " + msg.getType());
        }
        catch (Exception e) {
            throw new IOException("Error deserializing message " + e.getMessage());
        }
    }

    private static AuctionProtos.BigIntegerMsg makeBigIntegerMsg(BigInteger val) {
        return AuctionProtos.BigIntegerMsg.newBuilder().setValue(ByteString.copyFrom(val.toByteArray())).build();
    }

    public static BigInteger fetchBigInteger(AuctionProtos.BigIntegerMsg msg) {
        return new BigInteger(msg.getValue().toByteArray());
    }

}

