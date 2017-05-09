/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.buyOp;

import com.google.protobuf.AbstractMessage;
import com.google.protobuf.AbstractMessageLite;
import com.google.protobuf.AbstractParser;
import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Descriptors;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.Internal;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.Parser;
import com.google.protobuf.ProtocolMessageEnum;
import com.google.protobuf.RepeatedFieldBuilder;
import com.google.protobuf.SingleFieldBuilder;
import com.google.protobuf.UnknownFieldSet;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class AuctionProtos {
    private static Descriptors.Descriptor internal_static_com_cyberpointllc_stac_auction_AuctionMsg_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_com_cyberpointllc_stac_auction_AuctionMsg_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_com_cyberpointllc_stac_auction_BidCommitmentMsg_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_com_cyberpointllc_stac_auction_BidCommitmentMsg_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_com_cyberpointllc_stac_auction_BidComparisonMsg_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_com_cyberpointllc_stac_auction_BidComparisonMsg_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_com_cyberpointllc_stac_auction_AuctionStartMsg_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_com_cyberpointllc_stac_auction_AuctionStartMsg_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_com_cyberpointllc_stac_auction_AuctionEndMsg_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_com_cyberpointllc_stac_auction_AuctionEndMsg_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_com_cyberpointllc_stac_auction_RevealBidMsg_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_com_cyberpointllc_stac_auction_RevealBidMsg_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_com_cyberpointllc_stac_auction_BigIntegerMsg_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_com_cyberpointllc_stac_auction_BigIntegerMsg_fieldAccessorTable;
    private static Descriptors.FileDescriptor descriptor;

    private AuctionProtos() {
    }

    public static void registerAllExtensions(ExtensionRegistry registry) {
    }

    public static Descriptors.FileDescriptor getDescriptor() {
        return descriptor;
    }

    static {
        String[] descriptorData = new String[]{"\n\rauction.proto\u0012\u001ecom.cyberpointllc.stac.auction\"\u00b8\u0004\n\nAuctionMsg\u0012\u0011\n\tauctionId\u0018\u0001 \u0002(\t\u0012=\n\u0004type\u0018\u0002 \u0002(\u000e2/.com.cyberpointllc.stac.auction.AuctionMsg.Type\u0012D\n\ncommitment\u0018\u0003 \u0001(\u000b20.com.cyberpointllc.stac.auction.BidCommitmentMsg\u0012D\n\ncomparison\u0018\u0004 \u0001(\u000b20.com.cyberpointllc.stac.auction.BidComparisonMsg\u0012>\n\u0005start\u0018\u0005 \u0001(\u000b2/.com.cyberpointllc.stac.auction.AuctionStartMsg\u0012<\n\u0006reveal\u0018\u0006 \u0001(\u000b2,.com.cyberpointllc.stac.auction.Re", "vealBidMsg\u0012:\n\u0003end\u0018\u0007 \u0001(\u000b2-.com.cyberpointllc.stac.auction.AuctionEndMsg\"\u0091\u0001\n\u0004Type\u0012\u0011\n\rAUCTION_START\u0010\u0001\u0012\u0012\n\u000eBID_COMMITMENT\u0010\u0002\u0012\u000f\n\u000bBID_RECEIPT\u0010\u0003\u0012\u0012\n\u000eBID_COMPARISON\u0010\u0004\u0012\u0010\n\fBIDDING_OVER\u0010\u0005\u0012\r\n\tCLAIM_WIN\u0010\u0006\u0012\u000b\n\u0007CONCEDE\u0010\u0007\u0012\u000f\n\u000bAUCTION_END\u0010\b\"n\n\u0010BidCommitmentMsg\u0012\f\n\u0004hash\u0018\u0001 \u0002(\f\u0012\n\n\u0002r1\u0018\u0002 \u0002(\u0001\u0012@\n\tsharedVal\u0018\u0003 \u0002(\u000b2-.com.cyberpointllc.stac.auction.BigIntegerMsg\"\u00ad\u0001\n\u0010BidComparisonMsg\u0012=\n\u0006values\u0018\u0001 \u0003(\u000b2-.com.cyberpointllc.stac.auction", ".BigIntegerMsg\u0012<\n\u0005prime\u0018\u0002 \u0002(\u000b2-.com.cyberpointllc.stac.auction.BigIntegerMsg\u0012\u001c\n\u0014needReturnComparison\u0018\u0003 \u0002(\b\"*\n\u000fAuctionStartMsg\u0012\u0017\n\u000fitemDescription\u0018\u0001 \u0001(\t\"3\n\rAuctionEndMsg\u0012\u000e\n\u0006winner\u0018\u0001 \u0002(\t\u0012\u0012\n\nwinningBid\u0018\u0002 \u0002(\u0005\"m\n\fRevealBidMsg\u0012\n\n\u0002r1\u0018\u0001 \u0002(\u0001\u0012\n\n\u0002r2\u0018\u0002 \u0002(\u0001\u00128\n\u0001x\u0018\u0003 \u0002(\u000b2-.com.cyberpointllc.stac.auction.BigIntegerMsg\u0012\u000b\n\u0003bid\u0018\u0004 \u0002(\u0005\"\u001e\n\rBigIntegerMsg\u0012\r\n\u0005value\u0018\u0001 \u0002(\fB/\n\u001ecom.cyberpointllc.stac.auctionB\rAuctionProtos"};
        Descriptors.FileDescriptor.InternalDescriptorAssigner assigner = new Descriptors.FileDescriptor.InternalDescriptorAssigner(){

            @Override
            public ExtensionRegistry assignDescriptors(Descriptors.FileDescriptor root) {
                descriptor = root;
                return null;
            }
        };
        Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(descriptorData, new Descriptors.FileDescriptor[0], assigner);
        internal_static_com_cyberpointllc_stac_auction_AuctionMsg_descriptor = AuctionProtos.getDescriptor().getMessageTypes().get(0);
        internal_static_com_cyberpointllc_stac_auction_AuctionMsg_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_com_cyberpointllc_stac_auction_AuctionMsg_descriptor, new String[]{"AuctionId", "Type", "Commitment", "Comparison", "Start", "Reveal", "End"});
        internal_static_com_cyberpointllc_stac_auction_BidCommitmentMsg_descriptor = AuctionProtos.getDescriptor().getMessageTypes().get(1);
        internal_static_com_cyberpointllc_stac_auction_BidCommitmentMsg_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_com_cyberpointllc_stac_auction_BidCommitmentMsg_descriptor, new String[]{"Hash", "R1", "SharedVal"});
        internal_static_com_cyberpointllc_stac_auction_BidComparisonMsg_descriptor = AuctionProtos.getDescriptor().getMessageTypes().get(2);
        internal_static_com_cyberpointllc_stac_auction_BidComparisonMsg_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_com_cyberpointllc_stac_auction_BidComparisonMsg_descriptor, new String[]{"Values", "Prime", "NeedReturnComparison"});
        internal_static_com_cyberpointllc_stac_auction_AuctionStartMsg_descriptor = AuctionProtos.getDescriptor().getMessageTypes().get(3);
        internal_static_com_cyberpointllc_stac_auction_AuctionStartMsg_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_com_cyberpointllc_stac_auction_AuctionStartMsg_descriptor, new String[]{"ItemDescription"});
        internal_static_com_cyberpointllc_stac_auction_AuctionEndMsg_descriptor = AuctionProtos.getDescriptor().getMessageTypes().get(4);
        internal_static_com_cyberpointllc_stac_auction_AuctionEndMsg_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_com_cyberpointllc_stac_auction_AuctionEndMsg_descriptor, new String[]{"Winner", "WinningBid"});
        internal_static_com_cyberpointllc_stac_auction_RevealBidMsg_descriptor = AuctionProtos.getDescriptor().getMessageTypes().get(5);
        internal_static_com_cyberpointllc_stac_auction_RevealBidMsg_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_com_cyberpointllc_stac_auction_RevealBidMsg_descriptor, new String[]{"R1", "R2", "X", "Bid"});
        internal_static_com_cyberpointllc_stac_auction_BigIntegerMsg_descriptor = AuctionProtos.getDescriptor().getMessageTypes().get(6);
        internal_static_com_cyberpointllc_stac_auction_BigIntegerMsg_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_com_cyberpointllc_stac_auction_BigIntegerMsg_descriptor, new String[]{"Value"});
    }

    public static final class BigIntegerMsg
    extends GeneratedMessage
    implements BigIntegerMsgOrBuilder {
        private int bitField0_;
        public static final int VALUE_FIELD_NUMBER = 1;
        private ByteString value_;
        private byte memoizedIsInitialized;
        private static final long serialVersionUID = 0;
        private static final BigIntegerMsg DEFAULT_INSTANCE = new BigIntegerMsg();
        @Deprecated
        public static final Parser<BigIntegerMsg> PARSER = new AbstractParser<BigIntegerMsg>(){

            @Override
            public BigIntegerMsg parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                try {
                    return new BigIntegerMsg(input, extensionRegistry);
                }
                catch (RuntimeException e) {
                    if (e.getCause() instanceof InvalidProtocolBufferException) {
                        throw (InvalidProtocolBufferException)e.getCause();
                    }
                    throw e;
                }
            }
        };

        private BigIntegerMsg(GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
        }

        private BigIntegerMsg() {
            this.memoizedIsInitialized = -1;
            this.value_ = ByteString.EMPTY;
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private BigIntegerMsg(CodedInputStream input, ExtensionRegistryLite extensionRegistry) {
            this();
            boolean mutable_bitField0_ = false;
            UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
            try {
                boolean done = false;
                block10 : while (!done) {
                    int tag = input.readTag();
                    switch (tag) {
                        case 0: {
                            done = true;
                            continue block10;
                        }
                        default: {
                            if (this.parseUnknownField(input, unknownFields, extensionRegistry, tag)) continue block10;
                            done = true;
                            continue block10;
                        }
                        case 10: 
                    }
                    this.bitField0_ |= 1;
                    this.value_ = input.readBytes();
                }
            }
            catch (InvalidProtocolBufferException e) {
                throw new RuntimeException(e.setUnfinishedMessage(this));
            }
            catch (IOException e) {
                throw new RuntimeException(new InvalidProtocolBufferException(e.getMessage()).setUnfinishedMessage(this));
            }
            finally {
                this.unknownFields = unknownFields.build();
                this.makeExtensionsImmutable();
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return internal_static_com_cyberpointllc_stac_auction_BigIntegerMsg_descriptor;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_com_cyberpointllc_stac_auction_BigIntegerMsg_fieldAccessorTable.ensureFieldAccessorsInitialized(BigIntegerMsg.class, Builder.class);
        }

        @Override
        public boolean hasValue() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override
        public ByteString getValue() {
            return this.value_;
        }

        @Override
        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized == 1) {
                return true;
            }
            if (isInitialized == 0) {
                return false;
            }
            if (!this.hasValue()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public void writeTo(CodedOutputStream output) throws IOException {
            if ((this.bitField0_ & 1) == 1) {
                output.writeBytes(1, this.value_);
            }
            this.unknownFields.writeTo(output);
        }

        @Override
        public int getSerializedSize() {
            int size = this.memoizedSize;
            if (size != -1) {
                return size;
            }
            size = 0;
            if ((this.bitField0_ & 1) == 1) {
                size += CodedOutputStream.computeBytesSize(1, this.value_);
            }
            this.memoizedSize = size += this.unknownFields.getSerializedSize();
            return size;
        }

        public static BigIntegerMsg parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static BigIntegerMsg parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static BigIntegerMsg parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static BigIntegerMsg parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static BigIntegerMsg parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static BigIntegerMsg parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static BigIntegerMsg parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static BigIntegerMsg parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static BigIntegerMsg parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static BigIntegerMsg parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        @Override
        public Builder newBuilderForType() {
            return BigIntegerMsg.newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(BigIntegerMsg prototype) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
        }

        @Override
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
        }

        @Override
        protected Builder newBuilderForType(GeneratedMessage.BuilderParent parent) {
            Builder builder = new Builder(parent);
            return builder;
        }

        public static BigIntegerMsg getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<BigIntegerMsg> parser() {
            return PARSER;
        }

        public Parser<BigIntegerMsg> getParserForType() {
            return PARSER;
        }

        @Override
        public BigIntegerMsg getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }

        public static final class Builder
        extends GeneratedMessage.Builder<Builder>
        implements BigIntegerMsgOrBuilder {
            private int bitField0_;
            private ByteString value_ = ByteString.EMPTY;

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_com_cyberpointllc_stac_auction_BigIntegerMsg_descriptor;
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_com_cyberpointllc_stac_auction_BigIntegerMsg_fieldAccessorTable.ensureFieldAccessorsInitialized(BigIntegerMsg.class, Builder.class);
            }

            private Builder() {
                this.maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessage.BuilderParent parent) {
                super(parent);
                this.maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                if (alwaysUseFieldBuilders) {
                    // empty if block
                }
            }

            @Override
            public Builder clear() {
                super.clear();
                this.value_ = ByteString.EMPTY;
                this.bitField0_ &= -2;
                return this;
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_com_cyberpointllc_stac_auction_BigIntegerMsg_descriptor;
            }

            @Override
            public BigIntegerMsg getDefaultInstanceForType() {
                return BigIntegerMsg.getDefaultInstance();
            }

            @Override
            public BigIntegerMsg build() {
                BigIntegerMsg result = this.buildPartial();
                if (!result.isInitialized()) {
                    throw AbstractMessage.Builder.newUninitializedMessageException(result);
                }
                return result;
            }

            @Override
            public BigIntegerMsg buildPartial() {
                BigIntegerMsg result = new BigIntegerMsg(this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) == 1) {
                    to_bitField0_ |= 1;
                }
                result.value_ = this.value_;
                result.bitField0_ = to_bitField0_;
                this.onBuilt();
                return result;
            }

            @Override
            public Builder mergeFrom(Message other) {
                if (other instanceof BigIntegerMsg) {
                    return this.mergeFrom((BigIntegerMsg)other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(BigIntegerMsg other) {
                if (other == BigIntegerMsg.getDefaultInstance()) {
                    return this;
                }
                if (other.hasValue()) {
                    this.setValue(other.getValue());
                }
                this.mergeUnknownFields(other.unknownFields);
                this.onChanged();
                return this;
            }

            @Override
            public final boolean isInitialized() {
                if (!this.hasValue()) {
                    return false;
                }
                return true;
            }

            @Override
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                BigIntegerMsg parsedMessage = null;
                try {
                    parsedMessage = BigIntegerMsg.PARSER.parsePartialFrom(input, extensionRegistry);
                }
                catch (InvalidProtocolBufferException e) {
                    parsedMessage = (BigIntegerMsg)e.getUnfinishedMessage();
                    throw e;
                }
                finally {
                    if (parsedMessage != null) {
                        this.mergeFrom(parsedMessage);
                    }
                }
                return this;
            }

            @Override
            public boolean hasValue() {
                return (this.bitField0_ & 1) == 1;
            }

            @Override
            public ByteString getValue() {
                return this.value_;
            }

            public Builder setValue(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.value_ = value;
                this.onChanged();
                return this;
            }

            public Builder clearValue() {
                this.bitField0_ &= -2;
                this.value_ = BigIntegerMsg.getDefaultInstance().getValue();
                this.onChanged();
                return this;
            }
        }

    }

    public static interface BigIntegerMsgOrBuilder
    extends MessageOrBuilder {
        public boolean hasValue();

        public ByteString getValue();
    }

    public static final class RevealBidMsg
    extends GeneratedMessage
    implements RevealBidMsgOrBuilder {
        private int bitField0_;
        public static final int R1_FIELD_NUMBER = 1;
        private double r1_;
        public static final int R2_FIELD_NUMBER = 2;
        private double r2_;
        public static final int X_FIELD_NUMBER = 3;
        private BigIntegerMsg x_;
        public static final int BID_FIELD_NUMBER = 4;
        private int bid_;
        private byte memoizedIsInitialized;
        private static final long serialVersionUID = 0;
        private static final RevealBidMsg DEFAULT_INSTANCE = new RevealBidMsg();
        @Deprecated
        public static final Parser<RevealBidMsg> PARSER = new AbstractParser<RevealBidMsg>(){

            @Override
            public RevealBidMsg parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                try {
                    return new RevealBidMsg(input, extensionRegistry);
                }
                catch (RuntimeException e) {
                    if (e.getCause() instanceof InvalidProtocolBufferException) {
                        throw (InvalidProtocolBufferException)e.getCause();
                    }
                    throw e;
                }
            }
        };

        private RevealBidMsg(GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
        }

        private RevealBidMsg() {
            this.memoizedIsInitialized = -1;
            this.r1_ = 0.0;
            this.r2_ = 0.0;
            this.bid_ = 0;
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private RevealBidMsg(CodedInputStream input, ExtensionRegistryLite extensionRegistry) {
            this();
            boolean mutable_bitField0_ = false;
            UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
            try {
                boolean done = false;
                block13 : while (!done) {
                    int tag = input.readTag();
                    switch (tag) {
                        case 0: {
                            done = true;
                            continue block13;
                        }
                        default: {
                            if (this.parseUnknownField(input, unknownFields, extensionRegistry, tag)) continue block13;
                            done = true;
                            continue block13;
                        }
                        case 9: {
                            this.bitField0_ |= 1;
                            this.r1_ = input.readDouble();
                            continue block13;
                        }
                        case 17: {
                            this.bitField0_ |= 2;
                            this.r2_ = input.readDouble();
                            continue block13;
                        }
                        case 26: {
                            BigIntegerMsg.Builder subBuilder = null;
                            if ((this.bitField0_ & 4) == 4) {
                                subBuilder = this.x_.toBuilder();
                            }
                            this.x_ = input.readMessage(BigIntegerMsg.parser(), extensionRegistry);
                            if (subBuilder != null) {
                                subBuilder.mergeFrom(this.x_);
                                this.x_ = subBuilder.buildPartial();
                            }
                            this.bitField0_ |= 4;
                            continue block13;
                        }
                        case 32: 
                    }
                    this.bitField0_ |= 8;
                    this.bid_ = input.readInt32();
                }
            }
            catch (InvalidProtocolBufferException e) {
                throw new RuntimeException(e.setUnfinishedMessage(this));
            }
            catch (IOException e) {
                throw new RuntimeException(new InvalidProtocolBufferException(e.getMessage()).setUnfinishedMessage(this));
            }
            finally {
                this.unknownFields = unknownFields.build();
                this.makeExtensionsImmutable();
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return internal_static_com_cyberpointllc_stac_auction_RevealBidMsg_descriptor;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_com_cyberpointllc_stac_auction_RevealBidMsg_fieldAccessorTable.ensureFieldAccessorsInitialized(RevealBidMsg.class, Builder.class);
        }

        @Override
        public boolean hasR1() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override
        public double getR1() {
            return this.r1_;
        }

        @Override
        public boolean hasR2() {
            return (this.bitField0_ & 2) == 2;
        }

        @Override
        public double getR2() {
            return this.r2_;
        }

        @Override
        public boolean hasX() {
            return (this.bitField0_ & 4) == 4;
        }

        @Override
        public BigIntegerMsg getX() {
            return this.x_ == null ? BigIntegerMsg.getDefaultInstance() : this.x_;
        }

        @Override
        public BigIntegerMsgOrBuilder getXOrBuilder() {
            return this.x_ == null ? BigIntegerMsg.getDefaultInstance() : this.x_;
        }

        @Override
        public boolean hasBid() {
            return (this.bitField0_ & 8) == 8;
        }

        @Override
        public int getBid() {
            return this.bid_;
        }

        @Override
        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized == 1) {
                return true;
            }
            if (isInitialized == 0) {
                return false;
            }
            if (!this.hasR1()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.hasR2()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.hasX()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.hasBid()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.getX().isInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public void writeTo(CodedOutputStream output) throws IOException {
            if ((this.bitField0_ & 1) == 1) {
                output.writeDouble(1, this.r1_);
            }
            if ((this.bitField0_ & 2) == 2) {
                output.writeDouble(2, this.r2_);
            }
            if ((this.bitField0_ & 4) == 4) {
                output.writeMessage(3, this.getX());
            }
            if ((this.bitField0_ & 8) == 8) {
                output.writeInt32(4, this.bid_);
            }
            this.unknownFields.writeTo(output);
        }

        @Override
        public int getSerializedSize() {
            int size = this.memoizedSize;
            if (size != -1) {
                return size;
            }
            size = 0;
            if ((this.bitField0_ & 1) == 1) {
                size += CodedOutputStream.computeDoubleSize(1, this.r1_);
            }
            if ((this.bitField0_ & 2) == 2) {
                size += CodedOutputStream.computeDoubleSize(2, this.r2_);
            }
            if ((this.bitField0_ & 4) == 4) {
                size += CodedOutputStream.computeMessageSize(3, this.getX());
            }
            if ((this.bitField0_ & 8) == 8) {
                size += CodedOutputStream.computeInt32Size(4, this.bid_);
            }
            this.memoizedSize = size += this.unknownFields.getSerializedSize();
            return size;
        }

        public static RevealBidMsg parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static RevealBidMsg parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static RevealBidMsg parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static RevealBidMsg parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static RevealBidMsg parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static RevealBidMsg parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static RevealBidMsg parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static RevealBidMsg parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static RevealBidMsg parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static RevealBidMsg parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        @Override
        public Builder newBuilderForType() {
            return RevealBidMsg.newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(RevealBidMsg prototype) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
        }

        @Override
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
        }

        @Override
        protected Builder newBuilderForType(GeneratedMessage.BuilderParent parent) {
            Builder builder = new Builder(parent);
            return builder;
        }

        public static RevealBidMsg getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<RevealBidMsg> parser() {
            return PARSER;
        }

        public Parser<RevealBidMsg> getParserForType() {
            return PARSER;
        }

        @Override
        public RevealBidMsg getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }

        public static final class Builder
        extends GeneratedMessage.Builder<Builder>
        implements RevealBidMsgOrBuilder {
            private int bitField0_;
            private double r1_;
            private double r2_;
            private BigIntegerMsg x_ = null;
            private SingleFieldBuilder<BigIntegerMsg, BigIntegerMsg.Builder, BigIntegerMsgOrBuilder> xBuilder_;
            private int bid_;

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_com_cyberpointllc_stac_auction_RevealBidMsg_descriptor;
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_com_cyberpointllc_stac_auction_RevealBidMsg_fieldAccessorTable.ensureFieldAccessorsInitialized(RevealBidMsg.class, Builder.class);
            }

            private Builder() {
                this.maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessage.BuilderParent parent) {
                super(parent);
                this.maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                if (alwaysUseFieldBuilders) {
                    this.getXFieldBuilder();
                }
            }

            @Override
            public Builder clear() {
                super.clear();
                this.r1_ = 0.0;
                this.bitField0_ &= -2;
                this.r2_ = 0.0;
                this.bitField0_ &= -3;
                if (this.xBuilder_ == null) {
                    this.x_ = null;
                } else {
                    this.xBuilder_.clear();
                }
                this.bitField0_ &= -5;
                this.bid_ = 0;
                this.bitField0_ &= -9;
                return this;
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_com_cyberpointllc_stac_auction_RevealBidMsg_descriptor;
            }

            @Override
            public RevealBidMsg getDefaultInstanceForType() {
                return RevealBidMsg.getDefaultInstance();
            }

            @Override
            public RevealBidMsg build() {
                RevealBidMsg result = this.buildPartial();
                if (!result.isInitialized()) {
                    throw AbstractMessage.Builder.newUninitializedMessageException(result);
                }
                return result;
            }

            @Override
            public RevealBidMsg buildPartial() {
                RevealBidMsg result = new RevealBidMsg(this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) == 1) {
                    to_bitField0_ |= 1;
                }
                result.r1_ = this.r1_;
                if ((from_bitField0_ & 2) == 2) {
                    to_bitField0_ |= 2;
                }
                result.r2_ = this.r2_;
                if ((from_bitField0_ & 4) == 4) {
                    to_bitField0_ |= 4;
                }
                if (this.xBuilder_ == null) {
                    result.x_ = this.x_;
                } else {
                    result.x_ = this.xBuilder_.build();
                }
                if ((from_bitField0_ & 8) == 8) {
                    to_bitField0_ |= 8;
                }
                result.bid_ = this.bid_;
                result.bitField0_ = to_bitField0_;
                this.onBuilt();
                return result;
            }

            @Override
            public Builder mergeFrom(Message other) {
                if (other instanceof RevealBidMsg) {
                    return this.mergeFrom((RevealBidMsg)other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(RevealBidMsg other) {
                if (other == RevealBidMsg.getDefaultInstance()) {
                    return this;
                }
                if (other.hasR1()) {
                    this.setR1(other.getR1());
                }
                if (other.hasR2()) {
                    this.setR2(other.getR2());
                }
                if (other.hasX()) {
                    this.mergeX(other.getX());
                }
                if (other.hasBid()) {
                    this.setBid(other.getBid());
                }
                this.mergeUnknownFields(other.unknownFields);
                this.onChanged();
                return this;
            }

            @Override
            public final boolean isInitialized() {
                if (!this.hasR1()) {
                    return false;
                }
                if (!this.hasR2()) {
                    return false;
                }
                if (!this.hasX()) {
                    return false;
                }
                if (!this.hasBid()) {
                    return false;
                }
                if (!this.getX().isInitialized()) {
                    return false;
                }
                return true;
            }

            @Override
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                RevealBidMsg parsedMessage = null;
                try {
                    parsedMessage = RevealBidMsg.PARSER.parsePartialFrom(input, extensionRegistry);
                }
                catch (InvalidProtocolBufferException e) {
                    parsedMessage = (RevealBidMsg)e.getUnfinishedMessage();
                    throw e;
                }
                finally {
                    if (parsedMessage != null) {
                        this.mergeFrom(parsedMessage);
                    }
                }
                return this;
            }

            @Override
            public boolean hasR1() {
                return (this.bitField0_ & 1) == 1;
            }

            @Override
            public double getR1() {
                return this.r1_;
            }

            public Builder setR1(double value) {
                this.bitField0_ |= 1;
                this.r1_ = value;
                this.onChanged();
                return this;
            }

            public Builder clearR1() {
                this.bitField0_ &= -2;
                this.r1_ = 0.0;
                this.onChanged();
                return this;
            }

            @Override
            public boolean hasR2() {
                return (this.bitField0_ & 2) == 2;
            }

            @Override
            public double getR2() {
                return this.r2_;
            }

            public Builder setR2(double value) {
                this.bitField0_ |= 2;
                this.r2_ = value;
                this.onChanged();
                return this;
            }

            public Builder clearR2() {
                this.bitField0_ &= -3;
                this.r2_ = 0.0;
                this.onChanged();
                return this;
            }

            @Override
            public boolean hasX() {
                return (this.bitField0_ & 4) == 4;
            }

            @Override
            public BigIntegerMsg getX() {
                if (this.xBuilder_ == null) {
                    return this.x_ == null ? BigIntegerMsg.getDefaultInstance() : this.x_;
                }
                return this.xBuilder_.getMessage();
            }

            public Builder setX(BigIntegerMsg value) {
                if (this.xBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.x_ = value;
                    this.onChanged();
                } else {
                    this.xBuilder_.setMessage(value);
                }
                this.bitField0_ |= 4;
                return this;
            }

            public Builder setX(BigIntegerMsg.Builder builderForValue) {
                if (this.xBuilder_ == null) {
                    this.x_ = builderForValue.build();
                    this.onChanged();
                } else {
                    this.xBuilder_.setMessage(builderForValue.build());
                }
                this.bitField0_ |= 4;
                return this;
            }

            public Builder mergeX(BigIntegerMsg value) {
                if (this.xBuilder_ == null) {
                    this.x_ = (this.bitField0_ & 4) == 4 && this.x_ != null && this.x_ != BigIntegerMsg.getDefaultInstance() ? BigIntegerMsg.newBuilder(this.x_).mergeFrom(value).buildPartial() : value;
                    this.onChanged();
                } else {
                    this.xBuilder_.mergeFrom(value);
                }
                this.bitField0_ |= 4;
                return this;
            }

            public Builder clearX() {
                if (this.xBuilder_ == null) {
                    this.x_ = null;
                    this.onChanged();
                } else {
                    this.xBuilder_.clear();
                }
                this.bitField0_ &= -5;
                return this;
            }

            public BigIntegerMsg.Builder getXBuilder() {
                this.bitField0_ |= 4;
                this.onChanged();
                return this.getXFieldBuilder().getBuilder();
            }

            @Override
            public BigIntegerMsgOrBuilder getXOrBuilder() {
                if (this.xBuilder_ != null) {
                    return this.xBuilder_.getMessageOrBuilder();
                }
                return this.x_ == null ? BigIntegerMsg.getDefaultInstance() : this.x_;
            }

            private SingleFieldBuilder<BigIntegerMsg, BigIntegerMsg.Builder, BigIntegerMsgOrBuilder> getXFieldBuilder() {
                if (this.xBuilder_ == null) {
                    this.xBuilder_ = new SingleFieldBuilder(this.getX(), this.getParentForChildren(), this.isClean());
                    this.x_ = null;
                }
                return this.xBuilder_;
            }

            @Override
            public boolean hasBid() {
                return (this.bitField0_ & 8) == 8;
            }

            @Override
            public int getBid() {
                return this.bid_;
            }

            public Builder setBid(int value) {
                this.bitField0_ |= 8;
                this.bid_ = value;
                this.onChanged();
                return this;
            }

            public Builder clearBid() {
                this.bitField0_ &= -9;
                this.bid_ = 0;
                this.onChanged();
                return this;
            }
        }

    }

    public static interface RevealBidMsgOrBuilder
    extends MessageOrBuilder {
        public boolean hasR1();

        public double getR1();

        public boolean hasR2();

        public double getR2();

        public boolean hasX();

        public BigIntegerMsg getX();

        public BigIntegerMsgOrBuilder getXOrBuilder();

        public boolean hasBid();

        public int getBid();
    }

    public static final class AuctionEndMsg
    extends GeneratedMessage
    implements AuctionEndMsgOrBuilder {
        private int bitField0_;
        public static final int WINNER_FIELD_NUMBER = 1;
        private volatile Object winner_;
        public static final int WINNINGBID_FIELD_NUMBER = 2;
        private int winningBid_;
        private byte memoizedIsInitialized;
        private static final long serialVersionUID = 0;
        private static final AuctionEndMsg DEFAULT_INSTANCE = new AuctionEndMsg();
        @Deprecated
        public static final Parser<AuctionEndMsg> PARSER = new AbstractParser<AuctionEndMsg>(){

            @Override
            public AuctionEndMsg parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                try {
                    return new AuctionEndMsg(input, extensionRegistry);
                }
                catch (RuntimeException e) {
                    if (e.getCause() instanceof InvalidProtocolBufferException) {
                        throw (InvalidProtocolBufferException)e.getCause();
                    }
                    throw e;
                }
            }
        };

        private AuctionEndMsg(GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
        }

        private AuctionEndMsg() {
            this.memoizedIsInitialized = -1;
            this.winner_ = "";
            this.winningBid_ = 0;
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private AuctionEndMsg(CodedInputStream input, ExtensionRegistryLite extensionRegistry) {
            this();
            boolean mutable_bitField0_ = false;
            UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
            try {
                boolean done = false;
                block11 : while (!done) {
                    int tag = input.readTag();
                    switch (tag) {
                        case 0: {
                            done = true;
                            continue block11;
                        }
                        default: {
                            if (this.parseUnknownField(input, unknownFields, extensionRegistry, tag)) continue block11;
                            done = true;
                            continue block11;
                        }
                        case 10: {
                            ByteString bs = input.readBytes();
                            this.bitField0_ |= 1;
                            this.winner_ = bs;
                            continue block11;
                        }
                        case 16: 
                    }
                    this.bitField0_ |= 2;
                    this.winningBid_ = input.readInt32();
                }
            }
            catch (InvalidProtocolBufferException e) {
                throw new RuntimeException(e.setUnfinishedMessage(this));
            }
            catch (IOException e) {
                throw new RuntimeException(new InvalidProtocolBufferException(e.getMessage()).setUnfinishedMessage(this));
            }
            finally {
                this.unknownFields = unknownFields.build();
                this.makeExtensionsImmutable();
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return internal_static_com_cyberpointllc_stac_auction_AuctionEndMsg_descriptor;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_com_cyberpointllc_stac_auction_AuctionEndMsg_fieldAccessorTable.ensureFieldAccessorsInitialized(AuctionEndMsg.class, Builder.class);
        }

        @Override
        public boolean hasWinner() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override
        public String getWinner() {
            Object ref = this.winner_;
            if (ref instanceof String) {
                return (String)ref;
            }
            ByteString bs = (ByteString)ref;
            String s = bs.toStringUtf8();
            if (bs.isValidUtf8()) {
                this.winner_ = s;
            }
            return s;
        }

        @Override
        public ByteString getWinnerBytes() {
            Object ref = this.winner_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String)ref);
                this.winner_ = b;
                return b;
            }
            return (ByteString)ref;
        }

        @Override
        public boolean hasWinningBid() {
            return (this.bitField0_ & 2) == 2;
        }

        @Override
        public int getWinningBid() {
            return this.winningBid_;
        }

        @Override
        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized == 1) {
                return true;
            }
            if (isInitialized == 0) {
                return false;
            }
            if (!this.hasWinner()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.hasWinningBid()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public void writeTo(CodedOutputStream output) throws IOException {
            if ((this.bitField0_ & 1) == 1) {
                GeneratedMessage.writeString(output, 1, this.winner_);
            }
            if ((this.bitField0_ & 2) == 2) {
                output.writeInt32(2, this.winningBid_);
            }
            this.unknownFields.writeTo(output);
        }

        @Override
        public int getSerializedSize() {
            int size = this.memoizedSize;
            if (size != -1) {
                return size;
            }
            size = 0;
            if ((this.bitField0_ & 1) == 1) {
                size += GeneratedMessage.computeStringSize(1, this.winner_);
            }
            if ((this.bitField0_ & 2) == 2) {
                size += CodedOutputStream.computeInt32Size(2, this.winningBid_);
            }
            this.memoizedSize = size += this.unknownFields.getSerializedSize();
            return size;
        }

        public static AuctionEndMsg parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static AuctionEndMsg parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static AuctionEndMsg parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static AuctionEndMsg parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static AuctionEndMsg parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static AuctionEndMsg parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static AuctionEndMsg parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static AuctionEndMsg parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static AuctionEndMsg parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static AuctionEndMsg parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        @Override
        public Builder newBuilderForType() {
            return AuctionEndMsg.newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(AuctionEndMsg prototype) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
        }

        @Override
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
        }

        @Override
        protected Builder newBuilderForType(GeneratedMessage.BuilderParent parent) {
            Builder builder = new Builder(parent);
            return builder;
        }

        public static AuctionEndMsg getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<AuctionEndMsg> parser() {
            return PARSER;
        }

        public Parser<AuctionEndMsg> getParserForType() {
            return PARSER;
        }

        @Override
        public AuctionEndMsg getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }

        public static final class Builder
        extends GeneratedMessage.Builder<Builder>
        implements AuctionEndMsgOrBuilder {
            private int bitField0_;
            private Object winner_ = "";
            private int winningBid_;

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_com_cyberpointllc_stac_auction_AuctionEndMsg_descriptor;
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_com_cyberpointllc_stac_auction_AuctionEndMsg_fieldAccessorTable.ensureFieldAccessorsInitialized(AuctionEndMsg.class, Builder.class);
            }

            private Builder() {
                this.maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessage.BuilderParent parent) {
                super(parent);
                this.maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                if (alwaysUseFieldBuilders) {
                    // empty if block
                }
            }

            @Override
            public Builder clear() {
                super.clear();
                this.winner_ = "";
                this.bitField0_ &= -2;
                this.winningBid_ = 0;
                this.bitField0_ &= -3;
                return this;
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_com_cyberpointllc_stac_auction_AuctionEndMsg_descriptor;
            }

            @Override
            public AuctionEndMsg getDefaultInstanceForType() {
                return AuctionEndMsg.getDefaultInstance();
            }

            @Override
            public AuctionEndMsg build() {
                AuctionEndMsg result = this.buildPartial();
                if (!result.isInitialized()) {
                    throw AbstractMessage.Builder.newUninitializedMessageException(result);
                }
                return result;
            }

            @Override
            public AuctionEndMsg buildPartial() {
                AuctionEndMsg result = new AuctionEndMsg(this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) == 1) {
                    to_bitField0_ |= 1;
                }
                result.winner_ = this.winner_;
                if ((from_bitField0_ & 2) == 2) {
                    to_bitField0_ |= 2;
                }
                result.winningBid_ = this.winningBid_;
                result.bitField0_ = to_bitField0_;
                this.onBuilt();
                return result;
            }

            @Override
            public Builder mergeFrom(Message other) {
                if (other instanceof AuctionEndMsg) {
                    return this.mergeFrom((AuctionEndMsg)other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(AuctionEndMsg other) {
                if (other == AuctionEndMsg.getDefaultInstance()) {
                    return this;
                }
                if (other.hasWinner()) {
                    this.bitField0_ |= 1;
                    this.winner_ = other.winner_;
                    this.onChanged();
                }
                if (other.hasWinningBid()) {
                    this.setWinningBid(other.getWinningBid());
                }
                this.mergeUnknownFields(other.unknownFields);
                this.onChanged();
                return this;
            }

            @Override
            public final boolean isInitialized() {
                if (!this.hasWinner()) {
                    return false;
                }
                if (!this.hasWinningBid()) {
                    return false;
                }
                return true;
            }

            @Override
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                AuctionEndMsg parsedMessage = null;
                try {
                    parsedMessage = AuctionEndMsg.PARSER.parsePartialFrom(input, extensionRegistry);
                }
                catch (InvalidProtocolBufferException e) {
                    parsedMessage = (AuctionEndMsg)e.getUnfinishedMessage();
                    throw e;
                }
                finally {
                    if (parsedMessage != null) {
                        this.mergeFrom(parsedMessage);
                    }
                }
                return this;
            }

            @Override
            public boolean hasWinner() {
                return (this.bitField0_ & 1) == 1;
            }

            @Override
            public String getWinner() {
                Object ref = this.winner_;
                if (!(ref instanceof String)) {
                    ByteString bs = (ByteString)ref;
                    String s = bs.toStringUtf8();
                    if (bs.isValidUtf8()) {
                        this.winner_ = s;
                    }
                    return s;
                }
                return (String)ref;
            }

            @Override
            public ByteString getWinnerBytes() {
                Object ref = this.winner_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String)ref);
                    this.winner_ = b;
                    return b;
                }
                return (ByteString)ref;
            }

            public Builder setWinner(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.winner_ = value;
                this.onChanged();
                return this;
            }

            public Builder clearWinner() {
                this.bitField0_ &= -2;
                this.winner_ = AuctionEndMsg.getDefaultInstance().getWinner();
                this.onChanged();
                return this;
            }

            public Builder setWinnerBytes(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.winner_ = value;
                this.onChanged();
                return this;
            }

            @Override
            public boolean hasWinningBid() {
                return (this.bitField0_ & 2) == 2;
            }

            @Override
            public int getWinningBid() {
                return this.winningBid_;
            }

            public Builder setWinningBid(int value) {
                this.bitField0_ |= 2;
                this.winningBid_ = value;
                this.onChanged();
                return this;
            }

            public Builder clearWinningBid() {
                this.bitField0_ &= -3;
                this.winningBid_ = 0;
                this.onChanged();
                return this;
            }
        }

    }

    public static interface AuctionEndMsgOrBuilder
    extends MessageOrBuilder {
        public boolean hasWinner();

        public String getWinner();

        public ByteString getWinnerBytes();

        public boolean hasWinningBid();

        public int getWinningBid();
    }

    public static final class AuctionStartMsg
    extends GeneratedMessage
    implements AuctionStartMsgOrBuilder {
        private int bitField0_;
        public static final int ITEMDESCRIPTION_FIELD_NUMBER = 1;
        private volatile Object itemDescription_;
        private byte memoizedIsInitialized;
        private static final long serialVersionUID = 0;
        private static final AuctionStartMsg DEFAULT_INSTANCE = new AuctionStartMsg();
        @Deprecated
        public static final Parser<AuctionStartMsg> PARSER = new AbstractParser<AuctionStartMsg>(){

            @Override
            public AuctionStartMsg parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                try {
                    return new AuctionStartMsg(input, extensionRegistry);
                }
                catch (RuntimeException e) {
                    if (e.getCause() instanceof InvalidProtocolBufferException) {
                        throw (InvalidProtocolBufferException)e.getCause();
                    }
                    throw e;
                }
            }
        };

        private AuctionStartMsg(GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
        }

        private AuctionStartMsg() {
            this.memoizedIsInitialized = -1;
            this.itemDescription_ = "";
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private AuctionStartMsg(CodedInputStream input, ExtensionRegistryLite extensionRegistry) {
            this();
            boolean mutable_bitField0_ = false;
            UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
            try {
                boolean done = false;
                block10 : while (!done) {
                    int tag = input.readTag();
                    switch (tag) {
                        case 0: {
                            done = true;
                            continue block10;
                        }
                        default: {
                            if (this.parseUnknownField(input, unknownFields, extensionRegistry, tag)) continue block10;
                            done = true;
                            continue block10;
                        }
                        case 10: 
                    }
                    ByteString bs = input.readBytes();
                    this.bitField0_ |= 1;
                    this.itemDescription_ = bs;
                }
            }
            catch (InvalidProtocolBufferException e) {
                throw new RuntimeException(e.setUnfinishedMessage(this));
            }
            catch (IOException e) {
                throw new RuntimeException(new InvalidProtocolBufferException(e.getMessage()).setUnfinishedMessage(this));
            }
            finally {
                this.unknownFields = unknownFields.build();
                this.makeExtensionsImmutable();
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return internal_static_com_cyberpointllc_stac_auction_AuctionStartMsg_descriptor;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_com_cyberpointllc_stac_auction_AuctionStartMsg_fieldAccessorTable.ensureFieldAccessorsInitialized(AuctionStartMsg.class, Builder.class);
        }

        @Override
        public boolean hasItemDescription() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override
        public String getItemDescription() {
            Object ref = this.itemDescription_;
            if (ref instanceof String) {
                return (String)ref;
            }
            ByteString bs = (ByteString)ref;
            String s = bs.toStringUtf8();
            if (bs.isValidUtf8()) {
                this.itemDescription_ = s;
            }
            return s;
        }

        @Override
        public ByteString getItemDescriptionBytes() {
            Object ref = this.itemDescription_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String)ref);
                this.itemDescription_ = b;
                return b;
            }
            return (ByteString)ref;
        }

        @Override
        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized == 1) {
                return true;
            }
            if (isInitialized == 0) {
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public void writeTo(CodedOutputStream output) throws IOException {
            if ((this.bitField0_ & 1) == 1) {
                GeneratedMessage.writeString(output, 1, this.itemDescription_);
            }
            this.unknownFields.writeTo(output);
        }

        @Override
        public int getSerializedSize() {
            int size = this.memoizedSize;
            if (size != -1) {
                return size;
            }
            size = 0;
            if ((this.bitField0_ & 1) == 1) {
                size += GeneratedMessage.computeStringSize(1, this.itemDescription_);
            }
            this.memoizedSize = size += this.unknownFields.getSerializedSize();
            return size;
        }

        public static AuctionStartMsg parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static AuctionStartMsg parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static AuctionStartMsg parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static AuctionStartMsg parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static AuctionStartMsg parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static AuctionStartMsg parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static AuctionStartMsg parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static AuctionStartMsg parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static AuctionStartMsg parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static AuctionStartMsg parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        @Override
        public Builder newBuilderForType() {
            return AuctionStartMsg.newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(AuctionStartMsg prototype) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
        }

        @Override
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
        }

        @Override
        protected Builder newBuilderForType(GeneratedMessage.BuilderParent parent) {
            Builder builder = new Builder(parent);
            return builder;
        }

        public static AuctionStartMsg getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<AuctionStartMsg> parser() {
            return PARSER;
        }

        public Parser<AuctionStartMsg> getParserForType() {
            return PARSER;
        }

        @Override
        public AuctionStartMsg getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }

        public static final class Builder
        extends GeneratedMessage.Builder<Builder>
        implements AuctionStartMsgOrBuilder {
            private int bitField0_;
            private Object itemDescription_ = "";

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_com_cyberpointllc_stac_auction_AuctionStartMsg_descriptor;
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_com_cyberpointllc_stac_auction_AuctionStartMsg_fieldAccessorTable.ensureFieldAccessorsInitialized(AuctionStartMsg.class, Builder.class);
            }

            private Builder() {
                this.maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessage.BuilderParent parent) {
                super(parent);
                this.maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                if (alwaysUseFieldBuilders) {
                    // empty if block
                }
            }

            @Override
            public Builder clear() {
                super.clear();
                this.itemDescription_ = "";
                this.bitField0_ &= -2;
                return this;
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_com_cyberpointllc_stac_auction_AuctionStartMsg_descriptor;
            }

            @Override
            public AuctionStartMsg getDefaultInstanceForType() {
                return AuctionStartMsg.getDefaultInstance();
            }

            @Override
            public AuctionStartMsg build() {
                AuctionStartMsg result = this.buildPartial();
                if (!result.isInitialized()) {
                    throw AbstractMessage.Builder.newUninitializedMessageException(result);
                }
                return result;
            }

            @Override
            public AuctionStartMsg buildPartial() {
                AuctionStartMsg result = new AuctionStartMsg(this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) == 1) {
                    to_bitField0_ |= 1;
                }
                result.itemDescription_ = this.itemDescription_;
                result.bitField0_ = to_bitField0_;
                this.onBuilt();
                return result;
            }

            @Override
            public Builder mergeFrom(Message other) {
                if (other instanceof AuctionStartMsg) {
                    return this.mergeFrom((AuctionStartMsg)other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(AuctionStartMsg other) {
                if (other == AuctionStartMsg.getDefaultInstance()) {
                    return this;
                }
                if (other.hasItemDescription()) {
                    this.bitField0_ |= 1;
                    this.itemDescription_ = other.itemDescription_;
                    this.onChanged();
                }
                this.mergeUnknownFields(other.unknownFields);
                this.onChanged();
                return this;
            }

            @Override
            public final boolean isInitialized() {
                return true;
            }

            @Override
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                AuctionStartMsg parsedMessage = null;
                try {
                    parsedMessage = AuctionStartMsg.PARSER.parsePartialFrom(input, extensionRegistry);
                }
                catch (InvalidProtocolBufferException e) {
                    parsedMessage = (AuctionStartMsg)e.getUnfinishedMessage();
                    throw e;
                }
                finally {
                    if (parsedMessage != null) {
                        this.mergeFrom(parsedMessage);
                    }
                }
                return this;
            }

            @Override
            public boolean hasItemDescription() {
                return (this.bitField0_ & 1) == 1;
            }

            @Override
            public String getItemDescription() {
                Object ref = this.itemDescription_;
                if (!(ref instanceof String)) {
                    ByteString bs = (ByteString)ref;
                    String s = bs.toStringUtf8();
                    if (bs.isValidUtf8()) {
                        this.itemDescription_ = s;
                    }
                    return s;
                }
                return (String)ref;
            }

            @Override
            public ByteString getItemDescriptionBytes() {
                Object ref = this.itemDescription_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String)ref);
                    this.itemDescription_ = b;
                    return b;
                }
                return (ByteString)ref;
            }

            public Builder setItemDescription(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.itemDescription_ = value;
                this.onChanged();
                return this;
            }

            public Builder clearItemDescription() {
                this.bitField0_ &= -2;
                this.itemDescription_ = AuctionStartMsg.getDefaultInstance().getItemDescription();
                this.onChanged();
                return this;
            }

            public Builder setItemDescriptionBytes(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.itemDescription_ = value;
                this.onChanged();
                return this;
            }
        }

    }

    public static interface AuctionStartMsgOrBuilder
    extends MessageOrBuilder {
        public boolean hasItemDescription();

        public String getItemDescription();

        public ByteString getItemDescriptionBytes();
    }

    public static final class BidComparisonMsg
    extends GeneratedMessage
    implements BidComparisonMsgOrBuilder {
        private int bitField0_;
        public static final int VALUES_FIELD_NUMBER = 1;
        private List<BigIntegerMsg> values_;
        public static final int PRIME_FIELD_NUMBER = 2;
        private BigIntegerMsg prime_;
        public static final int NEEDRETURNCOMPARISON_FIELD_NUMBER = 3;
        private boolean needReturnComparison_;
        private byte memoizedIsInitialized;
        private static final long serialVersionUID = 0;
        private static final BidComparisonMsg DEFAULT_INSTANCE = new BidComparisonMsg();
        @Deprecated
        public static final Parser<BidComparisonMsg> PARSER = new AbstractParser<BidComparisonMsg>(){

            @Override
            public BidComparisonMsg parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                try {
                    return new BidComparisonMsg(input, extensionRegistry);
                }
                catch (RuntimeException e) {
                    if (e.getCause() instanceof InvalidProtocolBufferException) {
                        throw (InvalidProtocolBufferException)e.getCause();
                    }
                    throw e;
                }
            }
        };

        private BidComparisonMsg(GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
        }

        private BidComparisonMsg() {
            this.memoizedIsInitialized = -1;
            this.values_ = Collections.emptyList();
            this.needReturnComparison_ = false;
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private BidComparisonMsg(CodedInputStream input, ExtensionRegistryLite extensionRegistry) {
            this();
            boolean mutable_bitField0_ = false;
            UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
            try {
                boolean done = false;
                block12 : while (!done) {
                    int tag = input.readTag();
                    switch (tag) {
                        case 0: {
                            done = true;
                            continue block12;
                        }
                        default: {
                            if (this.parseUnknownField(input, unknownFields, extensionRegistry, tag)) continue block12;
                            done = true;
                            continue block12;
                        }
                        case 10: {
                            if (!(mutable_bitField0_ & true)) {
                                this.values_ = new ArrayList<BigIntegerMsg>();
                                mutable_bitField0_ |= true;
                            }
                            this.values_.add(input.readMessage(BigIntegerMsg.parser(), extensionRegistry));
                            continue block12;
                        }
                        case 18: {
                            BigIntegerMsg.Builder subBuilder = null;
                            if ((this.bitField0_ & 1) == 1) {
                                subBuilder = this.prime_.toBuilder();
                            }
                            this.prime_ = input.readMessage(BigIntegerMsg.parser(), extensionRegistry);
                            if (subBuilder != null) {
                                subBuilder.mergeFrom(this.prime_);
                                this.prime_ = subBuilder.buildPartial();
                            }
                            this.bitField0_ |= 1;
                            continue block12;
                        }
                        case 24: 
                    }
                    this.bitField0_ |= 2;
                    this.needReturnComparison_ = input.readBool();
                }
            }
            catch (InvalidProtocolBufferException e) {
                throw new RuntimeException(e.setUnfinishedMessage(this));
            }
            catch (IOException e) {
                throw new RuntimeException(new InvalidProtocolBufferException(e.getMessage()).setUnfinishedMessage(this));
            }
            finally {
                if (mutable_bitField0_ & true) {
                    this.values_ = Collections.unmodifiableList(this.values_);
                }
                this.unknownFields = unknownFields.build();
                this.makeExtensionsImmutable();
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return internal_static_com_cyberpointllc_stac_auction_BidComparisonMsg_descriptor;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_com_cyberpointllc_stac_auction_BidComparisonMsg_fieldAccessorTable.ensureFieldAccessorsInitialized(BidComparisonMsg.class, Builder.class);
        }

        @Override
        public List<BigIntegerMsg> getValuesList() {
            return this.values_;
        }

        @Override
        public List<? extends BigIntegerMsgOrBuilder> getValuesOrBuilderList() {
            return this.values_;
        }

        @Override
        public int getValuesCount() {
            return this.values_.size();
        }

        @Override
        public BigIntegerMsg getValues(int index) {
            return this.values_.get(index);
        }

        @Override
        public BigIntegerMsgOrBuilder getValuesOrBuilder(int index) {
            return this.values_.get(index);
        }

        @Override
        public boolean hasPrime() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override
        public BigIntegerMsg getPrime() {
            return this.prime_ == null ? BigIntegerMsg.getDefaultInstance() : this.prime_;
        }

        @Override
        public BigIntegerMsgOrBuilder getPrimeOrBuilder() {
            return this.prime_ == null ? BigIntegerMsg.getDefaultInstance() : this.prime_;
        }

        @Override
        public boolean hasNeedReturnComparison() {
            return (this.bitField0_ & 2) == 2;
        }

        @Override
        public boolean getNeedReturnComparison() {
            return this.needReturnComparison_;
        }

        @Override
        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized == 1) {
                return true;
            }
            if (isInitialized == 0) {
                return false;
            }
            if (!this.hasPrime()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.hasNeedReturnComparison()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            for (int i = 0; i < this.getValuesCount(); ++i) {
                if (this.getValues(i).isInitialized()) continue;
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.getPrime().isInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public void writeTo(CodedOutputStream output) throws IOException {
            for (int i = 0; i < this.values_.size(); ++i) {
                output.writeMessage(1, this.values_.get(i));
            }
            if ((this.bitField0_ & 1) == 1) {
                output.writeMessage(2, this.getPrime());
            }
            if ((this.bitField0_ & 2) == 2) {
                output.writeBool(3, this.needReturnComparison_);
            }
            this.unknownFields.writeTo(output);
        }

        @Override
        public int getSerializedSize() {
            int size = this.memoizedSize;
            if (size != -1) {
                return size;
            }
            size = 0;
            for (int i = 0; i < this.values_.size(); ++i) {
                size += CodedOutputStream.computeMessageSize(1, this.values_.get(i));
            }
            if ((this.bitField0_ & 1) == 1) {
                size += CodedOutputStream.computeMessageSize(2, this.getPrime());
            }
            if ((this.bitField0_ & 2) == 2) {
                size += CodedOutputStream.computeBoolSize(3, this.needReturnComparison_);
            }
            this.memoizedSize = size += this.unknownFields.getSerializedSize();
            return size;
        }

        public static BidComparisonMsg parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static BidComparisonMsg parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static BidComparisonMsg parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static BidComparisonMsg parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static BidComparisonMsg parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static BidComparisonMsg parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static BidComparisonMsg parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static BidComparisonMsg parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static BidComparisonMsg parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static BidComparisonMsg parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        @Override
        public Builder newBuilderForType() {
            return BidComparisonMsg.newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(BidComparisonMsg prototype) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
        }

        @Override
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
        }

        @Override
        protected Builder newBuilderForType(GeneratedMessage.BuilderParent parent) {
            Builder builder = new Builder(parent);
            return builder;
        }

        public static BidComparisonMsg getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<BidComparisonMsg> parser() {
            return PARSER;
        }

        public Parser<BidComparisonMsg> getParserForType() {
            return PARSER;
        }

        @Override
        public BidComparisonMsg getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }

        public static final class Builder
        extends GeneratedMessage.Builder<Builder>
        implements BidComparisonMsgOrBuilder {
            private int bitField0_;
            private List<BigIntegerMsg> values_ = Collections.emptyList();
            private RepeatedFieldBuilder<BigIntegerMsg, BigIntegerMsg.Builder, BigIntegerMsgOrBuilder> valuesBuilder_;
            private BigIntegerMsg prime_ = null;
            private SingleFieldBuilder<BigIntegerMsg, BigIntegerMsg.Builder, BigIntegerMsgOrBuilder> primeBuilder_;
            private boolean needReturnComparison_;

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_com_cyberpointllc_stac_auction_BidComparisonMsg_descriptor;
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_com_cyberpointllc_stac_auction_BidComparisonMsg_fieldAccessorTable.ensureFieldAccessorsInitialized(BidComparisonMsg.class, Builder.class);
            }

            private Builder() {
                this.maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessage.BuilderParent parent) {
                super(parent);
                this.maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                if (alwaysUseFieldBuilders) {
                    this.getValuesFieldBuilder();
                    this.getPrimeFieldBuilder();
                }
            }

            @Override
            public Builder clear() {
                super.clear();
                if (this.valuesBuilder_ == null) {
                    this.values_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                } else {
                    this.valuesBuilder_.clear();
                }
                if (this.primeBuilder_ == null) {
                    this.prime_ = null;
                } else {
                    this.primeBuilder_.clear();
                }
                this.bitField0_ &= -3;
                this.needReturnComparison_ = false;
                this.bitField0_ &= -5;
                return this;
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_com_cyberpointllc_stac_auction_BidComparisonMsg_descriptor;
            }

            @Override
            public BidComparisonMsg getDefaultInstanceForType() {
                return BidComparisonMsg.getDefaultInstance();
            }

            @Override
            public BidComparisonMsg build() {
                BidComparisonMsg result = this.buildPartial();
                if (!result.isInitialized()) {
                    throw AbstractMessage.Builder.newUninitializedMessageException(result);
                }
                return result;
            }

            @Override
            public BidComparisonMsg buildPartial() {
                BidComparisonMsg result = new BidComparisonMsg(this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if (this.valuesBuilder_ == null) {
                    if ((this.bitField0_ & 1) == 1) {
                        this.values_ = Collections.unmodifiableList(this.values_);
                        this.bitField0_ &= -2;
                    }
                    result.values_ = this.values_;
                } else {
                    result.values_ = this.valuesBuilder_.build();
                }
                if ((from_bitField0_ & 2) == 2) {
                    to_bitField0_ |= 1;
                }
                if (this.primeBuilder_ == null) {
                    result.prime_ = this.prime_;
                } else {
                    result.prime_ = this.primeBuilder_.build();
                }
                if ((from_bitField0_ & 4) == 4) {
                    to_bitField0_ |= 2;
                }
                result.needReturnComparison_ = this.needReturnComparison_;
                result.bitField0_ = to_bitField0_;
                this.onBuilt();
                return result;
            }

            @Override
            public Builder mergeFrom(Message other) {
                if (other instanceof BidComparisonMsg) {
                    return this.mergeFrom((BidComparisonMsg)other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(BidComparisonMsg other) {
                if (other == BidComparisonMsg.getDefaultInstance()) {
                    return this;
                }
                if (this.valuesBuilder_ == null) {
                    if (!other.values_.isEmpty()) {
                        if (this.values_.isEmpty()) {
                            this.values_ = other.values_;
                            this.bitField0_ &= -2;
                        } else {
                            this.ensureValuesIsMutable();
                            this.values_.addAll(other.values_);
                        }
                        this.onChanged();
                    }
                } else if (!other.values_.isEmpty()) {
                    if (this.valuesBuilder_.isEmpty()) {
                        this.valuesBuilder_.dispose();
                        this.valuesBuilder_ = null;
                        this.values_ = other.values_;
                        this.bitField0_ &= -2;
                        this.valuesBuilder_ = alwaysUseFieldBuilders ? this.getValuesFieldBuilder() : null;
                    } else {
                        this.valuesBuilder_.addAllMessages(other.values_);
                    }
                }
                if (other.hasPrime()) {
                    this.mergePrime(other.getPrime());
                }
                if (other.hasNeedReturnComparison()) {
                    this.setNeedReturnComparison(other.getNeedReturnComparison());
                }
                this.mergeUnknownFields(other.unknownFields);
                this.onChanged();
                return this;
            }

            @Override
            public final boolean isInitialized() {
                if (!this.hasPrime()) {
                    return false;
                }
                if (!this.hasNeedReturnComparison()) {
                    return false;
                }
                for (int i = 0; i < this.getValuesCount(); ++i) {
                    if (this.getValues(i).isInitialized()) continue;
                    return false;
                }
                if (!this.getPrime().isInitialized()) {
                    return false;
                }
                return true;
            }

            @Override
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                BidComparisonMsg parsedMessage = null;
                try {
                    parsedMessage = BidComparisonMsg.PARSER.parsePartialFrom(input, extensionRegistry);
                }
                catch (InvalidProtocolBufferException e) {
                    parsedMessage = (BidComparisonMsg)e.getUnfinishedMessage();
                    throw e;
                }
                finally {
                    if (parsedMessage != null) {
                        this.mergeFrom(parsedMessage);
                    }
                }
                return this;
            }

            private void ensureValuesIsMutable() {
                if ((this.bitField0_ & 1) != 1) {
                    this.values_ = new ArrayList<BigIntegerMsg>(this.values_);
                    this.bitField0_ |= 1;
                }
            }

            @Override
            public List<BigIntegerMsg> getValuesList() {
                if (this.valuesBuilder_ == null) {
                    return Collections.unmodifiableList(this.values_);
                }
                return this.valuesBuilder_.getMessageList();
            }

            @Override
            public int getValuesCount() {
                if (this.valuesBuilder_ == null) {
                    return this.values_.size();
                }
                return this.valuesBuilder_.getCount();
            }

            @Override
            public BigIntegerMsg getValues(int index) {
                if (this.valuesBuilder_ == null) {
                    return this.values_.get(index);
                }
                return this.valuesBuilder_.getMessage(index);
            }

            public Builder setValues(int index, BigIntegerMsg value) {
                if (this.valuesBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.ensureValuesIsMutable();
                    this.values_.set(index, value);
                    this.onChanged();
                } else {
                    this.valuesBuilder_.setMessage(index, value);
                }
                return this;
            }

            public Builder setValues(int index, BigIntegerMsg.Builder builderForValue) {
                if (this.valuesBuilder_ == null) {
                    this.ensureValuesIsMutable();
                    this.values_.set(index, builderForValue.build());
                    this.onChanged();
                } else {
                    this.valuesBuilder_.setMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addValues(BigIntegerMsg value) {
                if (this.valuesBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.ensureValuesIsMutable();
                    this.values_.add(value);
                    this.onChanged();
                } else {
                    this.valuesBuilder_.addMessage(value);
                }
                return this;
            }

            public Builder addValues(int index, BigIntegerMsg value) {
                if (this.valuesBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.ensureValuesIsMutable();
                    this.values_.add(index, value);
                    this.onChanged();
                } else {
                    this.valuesBuilder_.addMessage(index, value);
                }
                return this;
            }

            public Builder addValues(BigIntegerMsg.Builder builderForValue) {
                if (this.valuesBuilder_ == null) {
                    this.ensureValuesIsMutable();
                    this.values_.add(builderForValue.build());
                    this.onChanged();
                } else {
                    this.valuesBuilder_.addMessage(builderForValue.build());
                }
                return this;
            }

            public Builder addValues(int index, BigIntegerMsg.Builder builderForValue) {
                if (this.valuesBuilder_ == null) {
                    this.ensureValuesIsMutable();
                    this.values_.add(index, builderForValue.build());
                    this.onChanged();
                } else {
                    this.valuesBuilder_.addMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addAllValues(Iterable<? extends BigIntegerMsg> values) {
                if (this.valuesBuilder_ == null) {
                    this.ensureValuesIsMutable();
                    AbstractMessageLite.Builder.addAll(values, this.values_);
                    this.onChanged();
                } else {
                    this.valuesBuilder_.addAllMessages(values);
                }
                return this;
            }

            public Builder clearValues() {
                if (this.valuesBuilder_ == null) {
                    this.values_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                    this.onChanged();
                } else {
                    this.valuesBuilder_.clear();
                }
                return this;
            }

            public Builder removeValues(int index) {
                if (this.valuesBuilder_ == null) {
                    this.ensureValuesIsMutable();
                    this.values_.remove(index);
                    this.onChanged();
                } else {
                    this.valuesBuilder_.remove(index);
                }
                return this;
            }

            public BigIntegerMsg.Builder getValuesBuilder(int index) {
                return this.getValuesFieldBuilder().getBuilder(index);
            }

            @Override
            public BigIntegerMsgOrBuilder getValuesOrBuilder(int index) {
                if (this.valuesBuilder_ == null) {
                    return this.values_.get(index);
                }
                return this.valuesBuilder_.getMessageOrBuilder(index);
            }

            @Override
            public List<? extends BigIntegerMsgOrBuilder> getValuesOrBuilderList() {
                if (this.valuesBuilder_ != null) {
                    return this.valuesBuilder_.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.values_);
            }

            public BigIntegerMsg.Builder addValuesBuilder() {
                return this.getValuesFieldBuilder().addBuilder(BigIntegerMsg.getDefaultInstance());
            }

            public BigIntegerMsg.Builder addValuesBuilder(int index) {
                return this.getValuesFieldBuilder().addBuilder(index, BigIntegerMsg.getDefaultInstance());
            }

            public List<BigIntegerMsg.Builder> getValuesBuilderList() {
                return this.getValuesFieldBuilder().getBuilderList();
            }

            private RepeatedFieldBuilder<BigIntegerMsg, BigIntegerMsg.Builder, BigIntegerMsgOrBuilder> getValuesFieldBuilder() {
                if (this.valuesBuilder_ == null) {
                    this.valuesBuilder_ = new RepeatedFieldBuilder(this.values_, (this.bitField0_ & 1) == 1, this.getParentForChildren(), this.isClean());
                    this.values_ = null;
                }
                return this.valuesBuilder_;
            }

            @Override
            public boolean hasPrime() {
                return (this.bitField0_ & 2) == 2;
            }

            @Override
            public BigIntegerMsg getPrime() {
                if (this.primeBuilder_ == null) {
                    return this.prime_ == null ? BigIntegerMsg.getDefaultInstance() : this.prime_;
                }
                return this.primeBuilder_.getMessage();
            }

            public Builder setPrime(BigIntegerMsg value) {
                if (this.primeBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.prime_ = value;
                    this.onChanged();
                } else {
                    this.primeBuilder_.setMessage(value);
                }
                this.bitField0_ |= 2;
                return this;
            }

            public Builder setPrime(BigIntegerMsg.Builder builderForValue) {
                if (this.primeBuilder_ == null) {
                    this.prime_ = builderForValue.build();
                    this.onChanged();
                } else {
                    this.primeBuilder_.setMessage(builderForValue.build());
                }
                this.bitField0_ |= 2;
                return this;
            }

            public Builder mergePrime(BigIntegerMsg value) {
                if (this.primeBuilder_ == null) {
                    this.prime_ = (this.bitField0_ & 2) == 2 && this.prime_ != null && this.prime_ != BigIntegerMsg.getDefaultInstance() ? BigIntegerMsg.newBuilder(this.prime_).mergeFrom(value).buildPartial() : value;
                    this.onChanged();
                } else {
                    this.primeBuilder_.mergeFrom(value);
                }
                this.bitField0_ |= 2;
                return this;
            }

            public Builder clearPrime() {
                if (this.primeBuilder_ == null) {
                    this.prime_ = null;
                    this.onChanged();
                } else {
                    this.primeBuilder_.clear();
                }
                this.bitField0_ &= -3;
                return this;
            }

            public BigIntegerMsg.Builder getPrimeBuilder() {
                this.bitField0_ |= 2;
                this.onChanged();
                return this.getPrimeFieldBuilder().getBuilder();
            }

            @Override
            public BigIntegerMsgOrBuilder getPrimeOrBuilder() {
                if (this.primeBuilder_ != null) {
                    return this.primeBuilder_.getMessageOrBuilder();
                }
                return this.prime_ == null ? BigIntegerMsg.getDefaultInstance() : this.prime_;
            }

            private SingleFieldBuilder<BigIntegerMsg, BigIntegerMsg.Builder, BigIntegerMsgOrBuilder> getPrimeFieldBuilder() {
                if (this.primeBuilder_ == null) {
                    this.primeBuilder_ = new SingleFieldBuilder(this.getPrime(), this.getParentForChildren(), this.isClean());
                    this.prime_ = null;
                }
                return this.primeBuilder_;
            }

            @Override
            public boolean hasNeedReturnComparison() {
                return (this.bitField0_ & 4) == 4;
            }

            @Override
            public boolean getNeedReturnComparison() {
                return this.needReturnComparison_;
            }

            public Builder setNeedReturnComparison(boolean value) {
                this.bitField0_ |= 4;
                this.needReturnComparison_ = value;
                this.onChanged();
                return this;
            }

            public Builder clearNeedReturnComparison() {
                this.bitField0_ &= -5;
                this.needReturnComparison_ = false;
                this.onChanged();
                return this;
            }
        }

    }

    public static interface BidComparisonMsgOrBuilder
    extends MessageOrBuilder {
        public List<BigIntegerMsg> getValuesList();

        public BigIntegerMsg getValues(int var1);

        public int getValuesCount();

        public List<? extends BigIntegerMsgOrBuilder> getValuesOrBuilderList();

        public BigIntegerMsgOrBuilder getValuesOrBuilder(int var1);

        public boolean hasPrime();

        public BigIntegerMsg getPrime();

        public BigIntegerMsgOrBuilder getPrimeOrBuilder();

        public boolean hasNeedReturnComparison();

        public boolean getNeedReturnComparison();
    }

    public static final class BidCommitmentMsg
    extends GeneratedMessage
    implements BidCommitmentMsgOrBuilder {
        private int bitField0_;
        public static final int HASH_FIELD_NUMBER = 1;
        private ByteString hash_;
        public static final int R1_FIELD_NUMBER = 2;
        private double r1_;
        public static final int SHAREDVAL_FIELD_NUMBER = 3;
        private BigIntegerMsg sharedVal_;
        private byte memoizedIsInitialized;
        private static final long serialVersionUID = 0;
        private static final BidCommitmentMsg DEFAULT_INSTANCE = new BidCommitmentMsg();
        @Deprecated
        public static final Parser<BidCommitmentMsg> PARSER = new AbstractParser<BidCommitmentMsg>(){

            @Override
            public BidCommitmentMsg parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                try {
                    return new BidCommitmentMsg(input, extensionRegistry);
                }
                catch (RuntimeException e) {
                    if (e.getCause() instanceof InvalidProtocolBufferException) {
                        throw (InvalidProtocolBufferException)e.getCause();
                    }
                    throw e;
                }
            }
        };

        private BidCommitmentMsg(GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
        }

        private BidCommitmentMsg() {
            this.memoizedIsInitialized = -1;
            this.hash_ = ByteString.EMPTY;
            this.r1_ = 0.0;
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private BidCommitmentMsg(CodedInputStream input, ExtensionRegistryLite extensionRegistry) {
            this();
            boolean mutable_bitField0_ = false;
            UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
            try {
                boolean done = false;
                block12 : while (!done) {
                    int tag = input.readTag();
                    switch (tag) {
                        case 0: {
                            done = true;
                            continue block12;
                        }
                        default: {
                            if (this.parseUnknownField(input, unknownFields, extensionRegistry, tag)) continue block12;
                            done = true;
                            continue block12;
                        }
                        case 10: {
                            this.bitField0_ |= 1;
                            this.hash_ = input.readBytes();
                            continue block12;
                        }
                        case 17: {
                            this.bitField0_ |= 2;
                            this.r1_ = input.readDouble();
                            continue block12;
                        }
                        case 26: 
                    }
                    BigIntegerMsg.Builder subBuilder = null;
                    if ((this.bitField0_ & 4) == 4) {
                        subBuilder = this.sharedVal_.toBuilder();
                    }
                    this.sharedVal_ = input.readMessage(BigIntegerMsg.parser(), extensionRegistry);
                    if (subBuilder != null) {
                        subBuilder.mergeFrom(this.sharedVal_);
                        this.sharedVal_ = subBuilder.buildPartial();
                    }
                    this.bitField0_ |= 4;
                }
            }
            catch (InvalidProtocolBufferException e) {
                throw new RuntimeException(e.setUnfinishedMessage(this));
            }
            catch (IOException e) {
                throw new RuntimeException(new InvalidProtocolBufferException(e.getMessage()).setUnfinishedMessage(this));
            }
            finally {
                this.unknownFields = unknownFields.build();
                this.makeExtensionsImmutable();
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return internal_static_com_cyberpointllc_stac_auction_BidCommitmentMsg_descriptor;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_com_cyberpointllc_stac_auction_BidCommitmentMsg_fieldAccessorTable.ensureFieldAccessorsInitialized(BidCommitmentMsg.class, Builder.class);
        }

        @Override
        public boolean hasHash() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override
        public ByteString getHash() {
            return this.hash_;
        }

        @Override
        public boolean hasR1() {
            return (this.bitField0_ & 2) == 2;
        }

        @Override
        public double getR1() {
            return this.r1_;
        }

        @Override
        public boolean hasSharedVal() {
            return (this.bitField0_ & 4) == 4;
        }

        @Override
        public BigIntegerMsg getSharedVal() {
            return this.sharedVal_ == null ? BigIntegerMsg.getDefaultInstance() : this.sharedVal_;
        }

        @Override
        public BigIntegerMsgOrBuilder getSharedValOrBuilder() {
            return this.sharedVal_ == null ? BigIntegerMsg.getDefaultInstance() : this.sharedVal_;
        }

        @Override
        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized == 1) {
                return true;
            }
            if (isInitialized == 0) {
                return false;
            }
            if (!this.hasHash()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.hasR1()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.hasSharedVal()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.getSharedVal().isInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public void writeTo(CodedOutputStream output) throws IOException {
            if ((this.bitField0_ & 1) == 1) {
                output.writeBytes(1, this.hash_);
            }
            if ((this.bitField0_ & 2) == 2) {
                output.writeDouble(2, this.r1_);
            }
            if ((this.bitField0_ & 4) == 4) {
                output.writeMessage(3, this.getSharedVal());
            }
            this.unknownFields.writeTo(output);
        }

        @Override
        public int getSerializedSize() {
            int size = this.memoizedSize;
            if (size != -1) {
                return size;
            }
            size = 0;
            if ((this.bitField0_ & 1) == 1) {
                size += CodedOutputStream.computeBytesSize(1, this.hash_);
            }
            if ((this.bitField0_ & 2) == 2) {
                size += CodedOutputStream.computeDoubleSize(2, this.r1_);
            }
            if ((this.bitField0_ & 4) == 4) {
                size += CodedOutputStream.computeMessageSize(3, this.getSharedVal());
            }
            this.memoizedSize = size += this.unknownFields.getSerializedSize();
            return size;
        }

        public static BidCommitmentMsg parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static BidCommitmentMsg parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static BidCommitmentMsg parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static BidCommitmentMsg parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static BidCommitmentMsg parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static BidCommitmentMsg parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static BidCommitmentMsg parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static BidCommitmentMsg parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static BidCommitmentMsg parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static BidCommitmentMsg parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        @Override
        public Builder newBuilderForType() {
            return BidCommitmentMsg.newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(BidCommitmentMsg prototype) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
        }

        @Override
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
        }

        @Override
        protected Builder newBuilderForType(GeneratedMessage.BuilderParent parent) {
            Builder builder = new Builder(parent);
            return builder;
        }

        public static BidCommitmentMsg getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<BidCommitmentMsg> parser() {
            return PARSER;
        }

        public Parser<BidCommitmentMsg> getParserForType() {
            return PARSER;
        }

        @Override
        public BidCommitmentMsg getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }

        public static final class Builder
        extends GeneratedMessage.Builder<Builder>
        implements BidCommitmentMsgOrBuilder {
            private int bitField0_;
            private ByteString hash_ = ByteString.EMPTY;
            private double r1_;
            private BigIntegerMsg sharedVal_ = null;
            private SingleFieldBuilder<BigIntegerMsg, BigIntegerMsg.Builder, BigIntegerMsgOrBuilder> sharedValBuilder_;

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_com_cyberpointllc_stac_auction_BidCommitmentMsg_descriptor;
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_com_cyberpointllc_stac_auction_BidCommitmentMsg_fieldAccessorTable.ensureFieldAccessorsInitialized(BidCommitmentMsg.class, Builder.class);
            }

            private Builder() {
                this.maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessage.BuilderParent parent) {
                super(parent);
                this.maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                if (alwaysUseFieldBuilders) {
                    this.getSharedValFieldBuilder();
                }
            }

            @Override
            public Builder clear() {
                super.clear();
                this.hash_ = ByteString.EMPTY;
                this.bitField0_ &= -2;
                this.r1_ = 0.0;
                this.bitField0_ &= -3;
                if (this.sharedValBuilder_ == null) {
                    this.sharedVal_ = null;
                } else {
                    this.sharedValBuilder_.clear();
                }
                this.bitField0_ &= -5;
                return this;
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_com_cyberpointllc_stac_auction_BidCommitmentMsg_descriptor;
            }

            @Override
            public BidCommitmentMsg getDefaultInstanceForType() {
                return BidCommitmentMsg.getDefaultInstance();
            }

            @Override
            public BidCommitmentMsg build() {
                BidCommitmentMsg result = this.buildPartial();
                if (!result.isInitialized()) {
                    throw AbstractMessage.Builder.newUninitializedMessageException(result);
                }
                return result;
            }

            @Override
            public BidCommitmentMsg buildPartial() {
                BidCommitmentMsg result = new BidCommitmentMsg(this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) == 1) {
                    to_bitField0_ |= 1;
                }
                result.hash_ = this.hash_;
                if ((from_bitField0_ & 2) == 2) {
                    to_bitField0_ |= 2;
                }
                result.r1_ = this.r1_;
                if ((from_bitField0_ & 4) == 4) {
                    to_bitField0_ |= 4;
                }
                if (this.sharedValBuilder_ == null) {
                    result.sharedVal_ = this.sharedVal_;
                } else {
                    result.sharedVal_ = this.sharedValBuilder_.build();
                }
                result.bitField0_ = to_bitField0_;
                this.onBuilt();
                return result;
            }

            @Override
            public Builder mergeFrom(Message other) {
                if (other instanceof BidCommitmentMsg) {
                    return this.mergeFrom((BidCommitmentMsg)other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(BidCommitmentMsg other) {
                if (other == BidCommitmentMsg.getDefaultInstance()) {
                    return this;
                }
                if (other.hasHash()) {
                    this.setHash(other.getHash());
                }
                if (other.hasR1()) {
                    this.setR1(other.getR1());
                }
                if (other.hasSharedVal()) {
                    this.mergeSharedVal(other.getSharedVal());
                }
                this.mergeUnknownFields(other.unknownFields);
                this.onChanged();
                return this;
            }

            @Override
            public final boolean isInitialized() {
                if (!this.hasHash()) {
                    return false;
                }
                if (!this.hasR1()) {
                    return false;
                }
                if (!this.hasSharedVal()) {
                    return false;
                }
                if (!this.getSharedVal().isInitialized()) {
                    return false;
                }
                return true;
            }

            @Override
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                BidCommitmentMsg parsedMessage = null;
                try {
                    parsedMessage = BidCommitmentMsg.PARSER.parsePartialFrom(input, extensionRegistry);
                }
                catch (InvalidProtocolBufferException e) {
                    parsedMessage = (BidCommitmentMsg)e.getUnfinishedMessage();
                    throw e;
                }
                finally {
                    if (parsedMessage != null) {
                        this.mergeFrom(parsedMessage);
                    }
                }
                return this;
            }

            @Override
            public boolean hasHash() {
                return (this.bitField0_ & 1) == 1;
            }

            @Override
            public ByteString getHash() {
                return this.hash_;
            }

            public Builder setHash(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.hash_ = value;
                this.onChanged();
                return this;
            }

            public Builder clearHash() {
                this.bitField0_ &= -2;
                this.hash_ = BidCommitmentMsg.getDefaultInstance().getHash();
                this.onChanged();
                return this;
            }

            @Override
            public boolean hasR1() {
                return (this.bitField0_ & 2) == 2;
            }

            @Override
            public double getR1() {
                return this.r1_;
            }

            public Builder setR1(double value) {
                this.bitField0_ |= 2;
                this.r1_ = value;
                this.onChanged();
                return this;
            }

            public Builder clearR1() {
                this.bitField0_ &= -3;
                this.r1_ = 0.0;
                this.onChanged();
                return this;
            }

            @Override
            public boolean hasSharedVal() {
                return (this.bitField0_ & 4) == 4;
            }

            @Override
            public BigIntegerMsg getSharedVal() {
                if (this.sharedValBuilder_ == null) {
                    return this.sharedVal_ == null ? BigIntegerMsg.getDefaultInstance() : this.sharedVal_;
                }
                return this.sharedValBuilder_.getMessage();
            }

            public Builder setSharedVal(BigIntegerMsg value) {
                if (this.sharedValBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.sharedVal_ = value;
                    this.onChanged();
                } else {
                    this.sharedValBuilder_.setMessage(value);
                }
                this.bitField0_ |= 4;
                return this;
            }

            public Builder setSharedVal(BigIntegerMsg.Builder builderForValue) {
                if (this.sharedValBuilder_ == null) {
                    this.sharedVal_ = builderForValue.build();
                    this.onChanged();
                } else {
                    this.sharedValBuilder_.setMessage(builderForValue.build());
                }
                this.bitField0_ |= 4;
                return this;
            }

            public Builder mergeSharedVal(BigIntegerMsg value) {
                if (this.sharedValBuilder_ == null) {
                    this.sharedVal_ = (this.bitField0_ & 4) == 4 && this.sharedVal_ != null && this.sharedVal_ != BigIntegerMsg.getDefaultInstance() ? BigIntegerMsg.newBuilder(this.sharedVal_).mergeFrom(value).buildPartial() : value;
                    this.onChanged();
                } else {
                    this.sharedValBuilder_.mergeFrom(value);
                }
                this.bitField0_ |= 4;
                return this;
            }

            public Builder clearSharedVal() {
                if (this.sharedValBuilder_ == null) {
                    this.sharedVal_ = null;
                    this.onChanged();
                } else {
                    this.sharedValBuilder_.clear();
                }
                this.bitField0_ &= -5;
                return this;
            }

            public BigIntegerMsg.Builder getSharedValBuilder() {
                this.bitField0_ |= 4;
                this.onChanged();
                return this.getSharedValFieldBuilder().getBuilder();
            }

            @Override
            public BigIntegerMsgOrBuilder getSharedValOrBuilder() {
                if (this.sharedValBuilder_ != null) {
                    return this.sharedValBuilder_.getMessageOrBuilder();
                }
                return this.sharedVal_ == null ? BigIntegerMsg.getDefaultInstance() : this.sharedVal_;
            }

            private SingleFieldBuilder<BigIntegerMsg, BigIntegerMsg.Builder, BigIntegerMsgOrBuilder> getSharedValFieldBuilder() {
                if (this.sharedValBuilder_ == null) {
                    this.sharedValBuilder_ = new SingleFieldBuilder(this.getSharedVal(), this.getParentForChildren(), this.isClean());
                    this.sharedVal_ = null;
                }
                return this.sharedValBuilder_;
            }
        }

    }

    public static interface BidCommitmentMsgOrBuilder
    extends MessageOrBuilder {
        public boolean hasHash();

        public ByteString getHash();

        public boolean hasR1();

        public double getR1();

        public boolean hasSharedVal();

        public BigIntegerMsg getSharedVal();

        public BigIntegerMsgOrBuilder getSharedValOrBuilder();
    }

    public static final class AuctionMsg
    extends GeneratedMessage
    implements AuctionMsgOrBuilder {
        private int bitField0_;
        public static final int AUCTIONID_FIELD_NUMBER = 1;
        private volatile Object auctionId_;
        public static final int TYPE_FIELD_NUMBER = 2;
        private int type_;
        public static final int COMMITMENT_FIELD_NUMBER = 3;
        private BidCommitmentMsg commitment_;
        public static final int COMPARISON_FIELD_NUMBER = 4;
        private BidComparisonMsg comparison_;
        public static final int START_FIELD_NUMBER = 5;
        private AuctionStartMsg start_;
        public static final int REVEAL_FIELD_NUMBER = 6;
        private RevealBidMsg reveal_;
        public static final int END_FIELD_NUMBER = 7;
        private AuctionEndMsg end_;
        private byte memoizedIsInitialized;
        private static final long serialVersionUID = 0;
        private static final AuctionMsg DEFAULT_INSTANCE = new AuctionMsg();
        @Deprecated
        public static final Parser<AuctionMsg> PARSER = new AbstractParser<AuctionMsg>(){

            @Override
            public AuctionMsg parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                try {
                    return new AuctionMsg(input, extensionRegistry);
                }
                catch (RuntimeException e) {
                    if (e.getCause() instanceof InvalidProtocolBufferException) {
                        throw (InvalidProtocolBufferException)e.getCause();
                    }
                    throw e;
                }
            }
        };

        private AuctionMsg(GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
        }

        private AuctionMsg() {
            this.memoizedIsInitialized = -1;
            this.auctionId_ = "";
            this.type_ = 1;
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private AuctionMsg(CodedInputStream input, ExtensionRegistryLite extensionRegistry) {
            this();
            boolean mutable_bitField0_ = false;
            UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
            try {
                boolean done = false;
                block16 : while (!done) {
                    int tag = input.readTag();
                    switch (tag) {
                        case 0: {
                            done = true;
                            continue block16;
                        }
                        default: {
                            if (this.parseUnknownField(input, unknownFields, extensionRegistry, tag)) continue block16;
                            done = true;
                            continue block16;
                        }
                        case 10: {
                            ByteString bs = input.readBytes();
                            this.bitField0_ |= 1;
                            this.auctionId_ = bs;
                            continue block16;
                        }
                        case 16: {
                            int rawValue = input.readEnum();
                            Type value = Type.valueOf(rawValue);
                            if (value == null) {
                                unknownFields.mergeVarintField(2, rawValue);
                                continue block16;
                            }
                            this.bitField0_ |= 2;
                            this.type_ = rawValue;
                            continue block16;
                        }
                        case 26: {
                            BidCommitmentMsg.Builder subBuilder = null;
                            if ((this.bitField0_ & 4) == 4) {
                                subBuilder = this.commitment_.toBuilder();
                            }
                            this.commitment_ = input.readMessage(BidCommitmentMsg.parser(), extensionRegistry);
                            if (subBuilder != null) {
                                subBuilder.mergeFrom(this.commitment_);
                                this.commitment_ = subBuilder.buildPartial();
                            }
                            this.bitField0_ |= 4;
                            continue block16;
                        }
                        case 34: {
                            BidComparisonMsg.Builder subBuilder = null;
                            if ((this.bitField0_ & 8) == 8) {
                                subBuilder = this.comparison_.toBuilder();
                            }
                            this.comparison_ = input.readMessage(BidComparisonMsg.parser(), extensionRegistry);
                            if (subBuilder != null) {
                                subBuilder.mergeFrom(this.comparison_);
                                this.comparison_ = subBuilder.buildPartial();
                            }
                            this.bitField0_ |= 8;
                            continue block16;
                        }
                        case 42: {
                            AuctionStartMsg.Builder subBuilder = null;
                            if ((this.bitField0_ & 16) == 16) {
                                subBuilder = this.start_.toBuilder();
                            }
                            this.start_ = input.readMessage(AuctionStartMsg.parser(), extensionRegistry);
                            if (subBuilder != null) {
                                subBuilder.mergeFrom(this.start_);
                                this.start_ = subBuilder.buildPartial();
                            }
                            this.bitField0_ |= 16;
                            continue block16;
                        }
                        case 50: {
                            RevealBidMsg.Builder subBuilder = null;
                            if ((this.bitField0_ & 32) == 32) {
                                subBuilder = this.reveal_.toBuilder();
                            }
                            this.reveal_ = input.readMessage(RevealBidMsg.parser(), extensionRegistry);
                            if (subBuilder != null) {
                                subBuilder.mergeFrom(this.reveal_);
                                this.reveal_ = subBuilder.buildPartial();
                            }
                            this.bitField0_ |= 32;
                            continue block16;
                        }
                        case 58: {
                            AuctionEndMsg.Builder subBuilder = null;
                            if ((this.bitField0_ & 64) == 64) {
                                subBuilder = this.end_.toBuilder();
                            }
                            this.end_ = input.readMessage(AuctionEndMsg.parser(), extensionRegistry);
                            if (subBuilder != null) {
                                subBuilder.mergeFrom(this.end_);
                                this.end_ = subBuilder.buildPartial();
                            }
                            this.bitField0_ |= 64;
                        }
                    }

                }
            }
            catch (InvalidProtocolBufferException e) {
                throw new RuntimeException(e.setUnfinishedMessage(this));
            }
            catch (IOException e) {
                throw new RuntimeException(new InvalidProtocolBufferException(e.getMessage()).setUnfinishedMessage(this));
            }
            finally {
                this.unknownFields = unknownFields.build();
                this.makeExtensionsImmutable();
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return internal_static_com_cyberpointllc_stac_auction_AuctionMsg_descriptor;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_com_cyberpointllc_stac_auction_AuctionMsg_fieldAccessorTable.ensureFieldAccessorsInitialized(AuctionMsg.class, Builder.class);
        }

        @Override
        public boolean hasAuctionId() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override
        public String getAuctionId() {
            Object ref = this.auctionId_;
            if (ref instanceof String) {
                return (String)ref;
            }
            ByteString bs = (ByteString)ref;
            String s = bs.toStringUtf8();
            if (bs.isValidUtf8()) {
                this.auctionId_ = s;
            }
            return s;
        }

        @Override
        public ByteString getAuctionIdBytes() {
            Object ref = this.auctionId_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String)ref);
                this.auctionId_ = b;
                return b;
            }
            return (ByteString)ref;
        }

        @Override
        public boolean hasType() {
            return (this.bitField0_ & 2) == 2;
        }

        @Override
        public Type getType() {
            Type result = Type.valueOf(this.type_);
            return result == null ? Type.AUCTION_START : result;
        }

        @Override
        public boolean hasCommitment() {
            return (this.bitField0_ & 4) == 4;
        }

        @Override
        public BidCommitmentMsg getCommitment() {
            return this.commitment_ == null ? BidCommitmentMsg.getDefaultInstance() : this.commitment_;
        }

        @Override
        public BidCommitmentMsgOrBuilder getCommitmentOrBuilder() {
            return this.commitment_ == null ? BidCommitmentMsg.getDefaultInstance() : this.commitment_;
        }

        @Override
        public boolean hasComparison() {
            return (this.bitField0_ & 8) == 8;
        }

        @Override
        public BidComparisonMsg getComparison() {
            return this.comparison_ == null ? BidComparisonMsg.getDefaultInstance() : this.comparison_;
        }

        @Override
        public BidComparisonMsgOrBuilder getComparisonOrBuilder() {
            return this.comparison_ == null ? BidComparisonMsg.getDefaultInstance() : this.comparison_;
        }

        @Override
        public boolean hasStart() {
            return (this.bitField0_ & 16) == 16;
        }

        @Override
        public AuctionStartMsg getStart() {
            return this.start_ == null ? AuctionStartMsg.getDefaultInstance() : this.start_;
        }

        @Override
        public AuctionStartMsgOrBuilder getStartOrBuilder() {
            return this.start_ == null ? AuctionStartMsg.getDefaultInstance() : this.start_;
        }

        @Override
        public boolean hasReveal() {
            return (this.bitField0_ & 32) == 32;
        }

        @Override
        public RevealBidMsg getReveal() {
            return this.reveal_ == null ? RevealBidMsg.getDefaultInstance() : this.reveal_;
        }

        @Override
        public RevealBidMsgOrBuilder getRevealOrBuilder() {
            return this.reveal_ == null ? RevealBidMsg.getDefaultInstance() : this.reveal_;
        }

        @Override
        public boolean hasEnd() {
            return (this.bitField0_ & 64) == 64;
        }

        @Override
        public AuctionEndMsg getEnd() {
            return this.end_ == null ? AuctionEndMsg.getDefaultInstance() : this.end_;
        }

        @Override
        public AuctionEndMsgOrBuilder getEndOrBuilder() {
            return this.end_ == null ? AuctionEndMsg.getDefaultInstance() : this.end_;
        }

        @Override
        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized == 1) {
                return true;
            }
            if (isInitialized == 0) {
                return false;
            }
            if (!this.hasAuctionId()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.hasType()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (this.hasCommitment() && !this.getCommitment().isInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (this.hasComparison() && !this.getComparison().isInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (this.hasReveal() && !this.getReveal().isInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (this.hasEnd() && !this.getEnd().isInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public void writeTo(CodedOutputStream output) throws IOException {
            if ((this.bitField0_ & 1) == 1) {
                GeneratedMessage.writeString(output, 1, this.auctionId_);
            }
            if ((this.bitField0_ & 2) == 2) {
                output.writeEnum(2, this.type_);
            }
            if ((this.bitField0_ & 4) == 4) {
                output.writeMessage(3, this.getCommitment());
            }
            if ((this.bitField0_ & 8) == 8) {
                output.writeMessage(4, this.getComparison());
            }
            if ((this.bitField0_ & 16) == 16) {
                output.writeMessage(5, this.getStart());
            }
            if ((this.bitField0_ & 32) == 32) {
                output.writeMessage(6, this.getReveal());
            }
            if ((this.bitField0_ & 64) == 64) {
                output.writeMessage(7, this.getEnd());
            }
            this.unknownFields.writeTo(output);
        }

        @Override
        public int getSerializedSize() {
            int size = this.memoizedSize;
            if (size != -1) {
                return size;
            }
            size = 0;
            if ((this.bitField0_ & 1) == 1) {
                size += GeneratedMessage.computeStringSize(1, this.auctionId_);
            }
            if ((this.bitField0_ & 2) == 2) {
                size += CodedOutputStream.computeEnumSize(2, this.type_);
            }
            if ((this.bitField0_ & 4) == 4) {
                size += CodedOutputStream.computeMessageSize(3, this.getCommitment());
            }
            if ((this.bitField0_ & 8) == 8) {
                size += CodedOutputStream.computeMessageSize(4, this.getComparison());
            }
            if ((this.bitField0_ & 16) == 16) {
                size += CodedOutputStream.computeMessageSize(5, this.getStart());
            }
            if ((this.bitField0_ & 32) == 32) {
                size += CodedOutputStream.computeMessageSize(6, this.getReveal());
            }
            if ((this.bitField0_ & 64) == 64) {
                size += CodedOutputStream.computeMessageSize(7, this.getEnd());
            }
            this.memoizedSize = size += this.unknownFields.getSerializedSize();
            return size;
        }

        public static AuctionMsg parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static AuctionMsg parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static AuctionMsg parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static AuctionMsg parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static AuctionMsg parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static AuctionMsg parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static AuctionMsg parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static AuctionMsg parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static AuctionMsg parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static AuctionMsg parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        @Override
        public Builder newBuilderForType() {
            return AuctionMsg.newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(AuctionMsg prototype) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
        }

        @Override
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
        }

        @Override
        protected Builder newBuilderForType(GeneratedMessage.BuilderParent parent) {
            Builder builder = new Builder(parent);
            return builder;
        }

        public static AuctionMsg getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<AuctionMsg> parser() {
            return PARSER;
        }

        public Parser<AuctionMsg> getParserForType() {
            return PARSER;
        }

        @Override
        public AuctionMsg getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }

        public static final class Builder
        extends GeneratedMessage.Builder<Builder>
        implements AuctionMsgOrBuilder {
            private int bitField0_;
            private Object auctionId_ = "";
            private int type_ = 1;
            private BidCommitmentMsg commitment_ = null;
            private SingleFieldBuilder<BidCommitmentMsg, BidCommitmentMsg.Builder, BidCommitmentMsgOrBuilder> commitmentBuilder_;
            private BidComparisonMsg comparison_ = null;
            private SingleFieldBuilder<BidComparisonMsg, BidComparisonMsg.Builder, BidComparisonMsgOrBuilder> comparisonBuilder_;
            private AuctionStartMsg start_ = null;
            private SingleFieldBuilder<AuctionStartMsg, AuctionStartMsg.Builder, AuctionStartMsgOrBuilder> startBuilder_;
            private RevealBidMsg reveal_ = null;
            private SingleFieldBuilder<RevealBidMsg, RevealBidMsg.Builder, RevealBidMsgOrBuilder> revealBuilder_;
            private AuctionEndMsg end_ = null;
            private SingleFieldBuilder<AuctionEndMsg, AuctionEndMsg.Builder, AuctionEndMsgOrBuilder> endBuilder_;

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_com_cyberpointllc_stac_auction_AuctionMsg_descriptor;
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_com_cyberpointllc_stac_auction_AuctionMsg_fieldAccessorTable.ensureFieldAccessorsInitialized(AuctionMsg.class, Builder.class);
            }

            private Builder() {
                this.maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessage.BuilderParent parent) {
                super(parent);
                this.maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                if (alwaysUseFieldBuilders) {
                    this.getCommitmentFieldBuilder();
                    this.getComparisonFieldBuilder();
                    this.getStartFieldBuilder();
                    this.getRevealFieldBuilder();
                    this.getEndFieldBuilder();
                }
            }

            @Override
            public Builder clear() {
                super.clear();
                this.auctionId_ = "";
                this.bitField0_ &= -2;
                this.type_ = 1;
                this.bitField0_ &= -3;
                if (this.commitmentBuilder_ == null) {
                    this.commitment_ = null;
                } else {
                    this.commitmentBuilder_.clear();
                }
                this.bitField0_ &= -5;
                if (this.comparisonBuilder_ == null) {
                    this.comparison_ = null;
                } else {
                    this.comparisonBuilder_.clear();
                }
                this.bitField0_ &= -9;
                if (this.startBuilder_ == null) {
                    this.start_ = null;
                } else {
                    this.startBuilder_.clear();
                }
                this.bitField0_ &= -17;
                if (this.revealBuilder_ == null) {
                    this.reveal_ = null;
                } else {
                    this.revealBuilder_.clear();
                }
                this.bitField0_ &= -33;
                if (this.endBuilder_ == null) {
                    this.end_ = null;
                } else {
                    this.endBuilder_.clear();
                }
                this.bitField0_ &= -65;
                return this;
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_com_cyberpointllc_stac_auction_AuctionMsg_descriptor;
            }

            @Override
            public AuctionMsg getDefaultInstanceForType() {
                return AuctionMsg.getDefaultInstance();
            }

            @Override
            public AuctionMsg build() {
                AuctionMsg result = this.buildPartial();
                if (!result.isInitialized()) {
                    throw AbstractMessage.Builder.newUninitializedMessageException(result);
                }
                return result;
            }

            @Override
            public AuctionMsg buildPartial() {
                AuctionMsg result = new AuctionMsg(this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) == 1) {
                    to_bitField0_ |= 1;
                }
                result.auctionId_ = this.auctionId_;
                if ((from_bitField0_ & 2) == 2) {
                    to_bitField0_ |= 2;
                }
                result.type_ = this.type_;
                if ((from_bitField0_ & 4) == 4) {
                    to_bitField0_ |= 4;
                }
                if (this.commitmentBuilder_ == null) {
                    result.commitment_ = this.commitment_;
                } else {
                    result.commitment_ = this.commitmentBuilder_.build();
                }
                if ((from_bitField0_ & 8) == 8) {
                    to_bitField0_ |= 8;
                }
                if (this.comparisonBuilder_ == null) {
                    result.comparison_ = this.comparison_;
                } else {
                    result.comparison_ = this.comparisonBuilder_.build();
                }
                if ((from_bitField0_ & 16) == 16) {
                    to_bitField0_ |= 16;
                }
                if (this.startBuilder_ == null) {
                    result.start_ = this.start_;
                } else {
                    result.start_ = this.startBuilder_.build();
                }
                if ((from_bitField0_ & 32) == 32) {
                    to_bitField0_ |= 32;
                }
                if (this.revealBuilder_ == null) {
                    result.reveal_ = this.reveal_;
                } else {
                    result.reveal_ = this.revealBuilder_.build();
                }
                if ((from_bitField0_ & 64) == 64) {
                    to_bitField0_ |= 64;
                }
                if (this.endBuilder_ == null) {
                    result.end_ = this.end_;
                } else {
                    result.end_ = this.endBuilder_.build();
                }
                result.bitField0_ = to_bitField0_;
                this.onBuilt();
                return result;
            }

            @Override
            public Builder mergeFrom(Message other) {
                if (other instanceof AuctionMsg) {
                    return this.mergeFrom((AuctionMsg)other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(AuctionMsg other) {
                if (other == AuctionMsg.getDefaultInstance()) {
                    return this;
                }
                if (other.hasAuctionId()) {
                    this.bitField0_ |= 1;
                    this.auctionId_ = other.auctionId_;
                    this.onChanged();
                }
                if (other.hasType()) {
                    this.setType(other.getType());
                }
                if (other.hasCommitment()) {
                    this.mergeCommitment(other.getCommitment());
                }
                if (other.hasComparison()) {
                    this.mergeComparison(other.getComparison());
                }
                if (other.hasStart()) {
                    this.mergeStart(other.getStart());
                }
                if (other.hasReveal()) {
                    this.mergeReveal(other.getReveal());
                }
                if (other.hasEnd()) {
                    this.mergeEnd(other.getEnd());
                }
                this.mergeUnknownFields(other.unknownFields);
                this.onChanged();
                return this;
            }

            @Override
            public final boolean isInitialized() {
                if (!this.hasAuctionId()) {
                    return false;
                }
                if (!this.hasType()) {
                    return false;
                }
                if (this.hasCommitment() && !this.getCommitment().isInitialized()) {
                    return false;
                }
                if (this.hasComparison() && !this.getComparison().isInitialized()) {
                    return false;
                }
                if (this.hasReveal() && !this.getReveal().isInitialized()) {
                    return false;
                }
                if (this.hasEnd() && !this.getEnd().isInitialized()) {
                    return false;
                }
                return true;
            }

            @Override
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                AuctionMsg parsedMessage = null;
                try {
                    parsedMessage = AuctionMsg.PARSER.parsePartialFrom(input, extensionRegistry);
                }
                catch (InvalidProtocolBufferException e) {
                    parsedMessage = (AuctionMsg)e.getUnfinishedMessage();
                    throw e;
                }
                finally {
                    if (parsedMessage != null) {
                        this.mergeFrom(parsedMessage);
                    }
                }
                return this;
            }

            @Override
            public boolean hasAuctionId() {
                return (this.bitField0_ & 1) == 1;
            }

            @Override
            public String getAuctionId() {
                Object ref = this.auctionId_;
                if (!(ref instanceof String)) {
                    ByteString bs = (ByteString)ref;
                    String s = bs.toStringUtf8();
                    if (bs.isValidUtf8()) {
                        this.auctionId_ = s;
                    }
                    return s;
                }
                return (String)ref;
            }

            @Override
            public ByteString getAuctionIdBytes() {
                Object ref = this.auctionId_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String)ref);
                    this.auctionId_ = b;
                    return b;
                }
                return (ByteString)ref;
            }

            public Builder setAuctionId(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.auctionId_ = value;
                this.onChanged();
                return this;
            }

            public Builder clearAuctionId() {
                this.bitField0_ &= -2;
                this.auctionId_ = AuctionMsg.getDefaultInstance().getAuctionId();
                this.onChanged();
                return this;
            }

            public Builder setAuctionIdBytes(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.auctionId_ = value;
                this.onChanged();
                return this;
            }

            @Override
            public boolean hasType() {
                return (this.bitField0_ & 2) == 2;
            }

            @Override
            public Type getType() {
                Type result = Type.valueOf(this.type_);
                return result == null ? Type.AUCTION_START : result;
            }

            public Builder setType(Type value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 2;
                this.type_ = value.getNumber();
                this.onChanged();
                return this;
            }

            public Builder clearType() {
                this.bitField0_ &= -3;
                this.type_ = 1;
                this.onChanged();
                return this;
            }

            @Override
            public boolean hasCommitment() {
                return (this.bitField0_ & 4) == 4;
            }

            @Override
            public BidCommitmentMsg getCommitment() {
                if (this.commitmentBuilder_ == null) {
                    return this.commitment_ == null ? BidCommitmentMsg.getDefaultInstance() : this.commitment_;
                }
                return this.commitmentBuilder_.getMessage();
            }

            public Builder setCommitment(BidCommitmentMsg value) {
                if (this.commitmentBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.commitment_ = value;
                    this.onChanged();
                } else {
                    this.commitmentBuilder_.setMessage(value);
                }
                this.bitField0_ |= 4;
                return this;
            }

            public Builder setCommitment(BidCommitmentMsg.Builder builderForValue) {
                if (this.commitmentBuilder_ == null) {
                    this.commitment_ = builderForValue.build();
                    this.onChanged();
                } else {
                    this.commitmentBuilder_.setMessage(builderForValue.build());
                }
                this.bitField0_ |= 4;
                return this;
            }

            public Builder mergeCommitment(BidCommitmentMsg value) {
                if (this.commitmentBuilder_ == null) {
                    this.commitment_ = (this.bitField0_ & 4) == 4 && this.commitment_ != null && this.commitment_ != BidCommitmentMsg.getDefaultInstance() ? BidCommitmentMsg.newBuilder(this.commitment_).mergeFrom(value).buildPartial() : value;
                    this.onChanged();
                } else {
                    this.commitmentBuilder_.mergeFrom(value);
                }
                this.bitField0_ |= 4;
                return this;
            }

            public Builder clearCommitment() {
                if (this.commitmentBuilder_ == null) {
                    this.commitment_ = null;
                    this.onChanged();
                } else {
                    this.commitmentBuilder_.clear();
                }
                this.bitField0_ &= -5;
                return this;
            }

            public BidCommitmentMsg.Builder getCommitmentBuilder() {
                this.bitField0_ |= 4;
                this.onChanged();
                return this.getCommitmentFieldBuilder().getBuilder();
            }

            @Override
            public BidCommitmentMsgOrBuilder getCommitmentOrBuilder() {
                if (this.commitmentBuilder_ != null) {
                    return this.commitmentBuilder_.getMessageOrBuilder();
                }
                return this.commitment_ == null ? BidCommitmentMsg.getDefaultInstance() : this.commitment_;
            }

            private SingleFieldBuilder<BidCommitmentMsg, BidCommitmentMsg.Builder, BidCommitmentMsgOrBuilder> getCommitmentFieldBuilder() {
                if (this.commitmentBuilder_ == null) {
                    this.commitmentBuilder_ = new SingleFieldBuilder(this.getCommitment(), this.getParentForChildren(), this.isClean());
                    this.commitment_ = null;
                }
                return this.commitmentBuilder_;
            }

            @Override
            public boolean hasComparison() {
                return (this.bitField0_ & 8) == 8;
            }

            @Override
            public BidComparisonMsg getComparison() {
                if (this.comparisonBuilder_ == null) {
                    return this.comparison_ == null ? BidComparisonMsg.getDefaultInstance() : this.comparison_;
                }
                return this.comparisonBuilder_.getMessage();
            }

            public Builder setComparison(BidComparisonMsg value) {
                if (this.comparisonBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.comparison_ = value;
                    this.onChanged();
                } else {
                    this.comparisonBuilder_.setMessage(value);
                }
                this.bitField0_ |= 8;
                return this;
            }

            public Builder setComparison(BidComparisonMsg.Builder builderForValue) {
                if (this.comparisonBuilder_ == null) {
                    this.comparison_ = builderForValue.build();
                    this.onChanged();
                } else {
                    this.comparisonBuilder_.setMessage(builderForValue.build());
                }
                this.bitField0_ |= 8;
                return this;
            }

            public Builder mergeComparison(BidComparisonMsg value) {
                if (this.comparisonBuilder_ == null) {
                    this.comparison_ = (this.bitField0_ & 8) == 8 && this.comparison_ != null && this.comparison_ != BidComparisonMsg.getDefaultInstance() ? BidComparisonMsg.newBuilder(this.comparison_).mergeFrom(value).buildPartial() : value;
                    this.onChanged();
                } else {
                    this.comparisonBuilder_.mergeFrom(value);
                }
                this.bitField0_ |= 8;
                return this;
            }

            public Builder clearComparison() {
                if (this.comparisonBuilder_ == null) {
                    this.comparison_ = null;
                    this.onChanged();
                } else {
                    this.comparisonBuilder_.clear();
                }
                this.bitField0_ &= -9;
                return this;
            }

            public BidComparisonMsg.Builder getComparisonBuilder() {
                this.bitField0_ |= 8;
                this.onChanged();
                return this.getComparisonFieldBuilder().getBuilder();
            }

            @Override
            public BidComparisonMsgOrBuilder getComparisonOrBuilder() {
                if (this.comparisonBuilder_ != null) {
                    return this.comparisonBuilder_.getMessageOrBuilder();
                }
                return this.comparison_ == null ? BidComparisonMsg.getDefaultInstance() : this.comparison_;
            }

            private SingleFieldBuilder<BidComparisonMsg, BidComparisonMsg.Builder, BidComparisonMsgOrBuilder> getComparisonFieldBuilder() {
                if (this.comparisonBuilder_ == null) {
                    this.comparisonBuilder_ = new SingleFieldBuilder(this.getComparison(), this.getParentForChildren(), this.isClean());
                    this.comparison_ = null;
                }
                return this.comparisonBuilder_;
            }

            @Override
            public boolean hasStart() {
                return (this.bitField0_ & 16) == 16;
            }

            @Override
            public AuctionStartMsg getStart() {
                if (this.startBuilder_ == null) {
                    return this.start_ == null ? AuctionStartMsg.getDefaultInstance() : this.start_;
                }
                return this.startBuilder_.getMessage();
            }

            public Builder setStart(AuctionStartMsg value) {
                if (this.startBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.start_ = value;
                    this.onChanged();
                } else {
                    this.startBuilder_.setMessage(value);
                }
                this.bitField0_ |= 16;
                return this;
            }

            public Builder setStart(AuctionStartMsg.Builder builderForValue) {
                if (this.startBuilder_ == null) {
                    this.start_ = builderForValue.build();
                    this.onChanged();
                } else {
                    this.startBuilder_.setMessage(builderForValue.build());
                }
                this.bitField0_ |= 16;
                return this;
            }

            public Builder mergeStart(AuctionStartMsg value) {
                if (this.startBuilder_ == null) {
                    this.start_ = (this.bitField0_ & 16) == 16 && this.start_ != null && this.start_ != AuctionStartMsg.getDefaultInstance() ? AuctionStartMsg.newBuilder(this.start_).mergeFrom(value).buildPartial() : value;
                    this.onChanged();
                } else {
                    this.startBuilder_.mergeFrom(value);
                }
                this.bitField0_ |= 16;
                return this;
            }

            public Builder clearStart() {
                if (this.startBuilder_ == null) {
                    this.start_ = null;
                    this.onChanged();
                } else {
                    this.startBuilder_.clear();
                }
                this.bitField0_ &= -17;
                return this;
            }

            public AuctionStartMsg.Builder getStartBuilder() {
                this.bitField0_ |= 16;
                this.onChanged();
                return this.getStartFieldBuilder().getBuilder();
            }

            @Override
            public AuctionStartMsgOrBuilder getStartOrBuilder() {
                if (this.startBuilder_ != null) {
                    return this.startBuilder_.getMessageOrBuilder();
                }
                return this.start_ == null ? AuctionStartMsg.getDefaultInstance() : this.start_;
            }

            private SingleFieldBuilder<AuctionStartMsg, AuctionStartMsg.Builder, AuctionStartMsgOrBuilder> getStartFieldBuilder() {
                if (this.startBuilder_ == null) {
                    this.startBuilder_ = new SingleFieldBuilder(this.getStart(), this.getParentForChildren(), this.isClean());
                    this.start_ = null;
                }
                return this.startBuilder_;
            }

            @Override
            public boolean hasReveal() {
                return (this.bitField0_ & 32) == 32;
            }

            @Override
            public RevealBidMsg getReveal() {
                if (this.revealBuilder_ == null) {
                    return this.reveal_ == null ? RevealBidMsg.getDefaultInstance() : this.reveal_;
                }
                return this.revealBuilder_.getMessage();
            }

            public Builder setReveal(RevealBidMsg value) {
                if (this.revealBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.reveal_ = value;
                    this.onChanged();
                } else {
                    this.revealBuilder_.setMessage(value);
                }
                this.bitField0_ |= 32;
                return this;
            }

            public Builder setReveal(RevealBidMsg.Builder builderForValue) {
                if (this.revealBuilder_ == null) {
                    this.reveal_ = builderForValue.build();
                    this.onChanged();
                } else {
                    this.revealBuilder_.setMessage(builderForValue.build());
                }
                this.bitField0_ |= 32;
                return this;
            }

            public Builder mergeReveal(RevealBidMsg value) {
                if (this.revealBuilder_ == null) {
                    this.reveal_ = (this.bitField0_ & 32) == 32 && this.reveal_ != null && this.reveal_ != RevealBidMsg.getDefaultInstance() ? RevealBidMsg.newBuilder(this.reveal_).mergeFrom(value).buildPartial() : value;
                    this.onChanged();
                } else {
                    this.revealBuilder_.mergeFrom(value);
                }
                this.bitField0_ |= 32;
                return this;
            }

            public Builder clearReveal() {
                if (this.revealBuilder_ == null) {
                    this.reveal_ = null;
                    this.onChanged();
                } else {
                    this.revealBuilder_.clear();
                }
                this.bitField0_ &= -33;
                return this;
            }

            public RevealBidMsg.Builder getRevealBuilder() {
                this.bitField0_ |= 32;
                this.onChanged();
                return this.getRevealFieldBuilder().getBuilder();
            }

            @Override
            public RevealBidMsgOrBuilder getRevealOrBuilder() {
                if (this.revealBuilder_ != null) {
                    return this.revealBuilder_.getMessageOrBuilder();
                }
                return this.reveal_ == null ? RevealBidMsg.getDefaultInstance() : this.reveal_;
            }

            private SingleFieldBuilder<RevealBidMsg, RevealBidMsg.Builder, RevealBidMsgOrBuilder> getRevealFieldBuilder() {
                if (this.revealBuilder_ == null) {
                    this.revealBuilder_ = new SingleFieldBuilder(this.getReveal(), this.getParentForChildren(), this.isClean());
                    this.reveal_ = null;
                }
                return this.revealBuilder_;
            }

            @Override
            public boolean hasEnd() {
                return (this.bitField0_ & 64) == 64;
            }

            @Override
            public AuctionEndMsg getEnd() {
                if (this.endBuilder_ == null) {
                    return this.end_ == null ? AuctionEndMsg.getDefaultInstance() : this.end_;
                }
                return this.endBuilder_.getMessage();
            }

            public Builder setEnd(AuctionEndMsg value) {
                if (this.endBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.end_ = value;
                    this.onChanged();
                } else {
                    this.endBuilder_.setMessage(value);
                }
                this.bitField0_ |= 64;
                return this;
            }

            public Builder setEnd(AuctionEndMsg.Builder builderForValue) {
                if (this.endBuilder_ == null) {
                    this.end_ = builderForValue.build();
                    this.onChanged();
                } else {
                    this.endBuilder_.setMessage(builderForValue.build());
                }
                this.bitField0_ |= 64;
                return this;
            }

            public Builder mergeEnd(AuctionEndMsg value) {
                if (this.endBuilder_ == null) {
                    this.end_ = (this.bitField0_ & 64) == 64 && this.end_ != null && this.end_ != AuctionEndMsg.getDefaultInstance() ? AuctionEndMsg.newBuilder(this.end_).mergeFrom(value).buildPartial() : value;
                    this.onChanged();
                } else {
                    this.endBuilder_.mergeFrom(value);
                }
                this.bitField0_ |= 64;
                return this;
            }

            public Builder clearEnd() {
                if (this.endBuilder_ == null) {
                    this.end_ = null;
                    this.onChanged();
                } else {
                    this.endBuilder_.clear();
                }
                this.bitField0_ &= -65;
                return this;
            }

            public AuctionEndMsg.Builder getEndBuilder() {
                this.bitField0_ |= 64;
                this.onChanged();
                return this.getEndFieldBuilder().getBuilder();
            }

            @Override
            public AuctionEndMsgOrBuilder getEndOrBuilder() {
                if (this.endBuilder_ != null) {
                    return this.endBuilder_.getMessageOrBuilder();
                }
                return this.end_ == null ? AuctionEndMsg.getDefaultInstance() : this.end_;
            }

            private SingleFieldBuilder<AuctionEndMsg, AuctionEndMsg.Builder, AuctionEndMsgOrBuilder> getEndFieldBuilder() {
                if (this.endBuilder_ == null) {
                    this.endBuilder_ = new SingleFieldBuilder(this.getEnd(), this.getParentForChildren(), this.isClean());
                    this.end_ = null;
                }
                return this.endBuilder_;
            }
        }

        public static enum Type implements ProtocolMessageEnum
        {
            AUCTION_START(0, 1),
            BID_COMMITMENT(1, 2),
            BID_RECEIPT(2, 3),
            BID_COMPARISON(3, 4),
            BIDDING_OVER(4, 5),
            CLAIM_WIN(5, 6),
            CONCEDE(6, 7),
            AUCTION_END(7, 8);
            
            public static final int AUCTION_START_VALUE = 1;
            public static final int BID_COMMITMENT_VALUE = 2;
            public static final int BID_RECEIPT_VALUE = 3;
            public static final int BID_COMPARISON_VALUE = 4;
            public static final int BIDDING_OVER_VALUE = 5;
            public static final int CLAIM_WIN_VALUE = 6;
            public static final int CONCEDE_VALUE = 7;
            public static final int AUCTION_END_VALUE = 8;
            private static final Internal.EnumLiteMap<Type> internalValueMap;
            private static final Type[] VALUES;
            private final int index;
            private final int value;

            @Override
            public final int getNumber() {
                return this.value;
            }

            public static Type valueOf(int value) {
                switch (value) {
                    case 1: {
                        return AUCTION_START;
                    }
                    case 2: {
                        return BID_COMMITMENT;
                    }
                    case 3: {
                        return BID_RECEIPT;
                    }
                    case 4: {
                        return BID_COMPARISON;
                    }
                    case 5: {
                        return BIDDING_OVER;
                    }
                    case 6: {
                        return CLAIM_WIN;
                    }
                    case 7: {
                        return CONCEDE;
                    }
                    case 8: {
                        return AUCTION_END;
                    }
                }
                return null;
            }

            public static Internal.EnumLiteMap<Type> internalGetValueMap() {
                return internalValueMap;
            }

            @Override
            public final Descriptors.EnumValueDescriptor getValueDescriptor() {
                return Type.getDescriptor().getValues().get(this.index);
            }

            @Override
            public final Descriptors.EnumDescriptor getDescriptorForType() {
                return Type.getDescriptor();
            }

            public static final Descriptors.EnumDescriptor getDescriptor() {
                return AuctionMsg.getDescriptor().getEnumTypes().get(0);
            }

            public static Type valueOf(Descriptors.EnumValueDescriptor desc) {
                if (desc.getType() != Type.getDescriptor()) {
                    throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
                }
                return VALUES[desc.getIndex()];
            }

            private Type(int index, int value) {
                this.index = index;
                this.value = value;
            }

            static {
                internalValueMap = new Internal.EnumLiteMap<Type>(){

                    @Override
                    public Type findValueByNumber(int number) {
                        return Type.valueOf(number);
                    }
                };
                VALUES = Type.values();
            }

        }

    }

    public static interface AuctionMsgOrBuilder
    extends MessageOrBuilder {
        public boolean hasAuctionId();

        public String getAuctionId();

        public ByteString getAuctionIdBytes();

        public boolean hasType();

        public AuctionMsg.Type getType();

        public boolean hasCommitment();

        public BidCommitmentMsg getCommitment();

        public BidCommitmentMsgOrBuilder getCommitmentOrBuilder();

        public boolean hasComparison();

        public BidComparisonMsg getComparison();

        public BidComparisonMsgOrBuilder getComparisonOrBuilder();

        public boolean hasStart();

        public AuctionStartMsg getStart();

        public AuctionStartMsgOrBuilder getStartOrBuilder();

        public boolean hasReveal();

        public RevealBidMsg getReveal();

        public RevealBidMsgOrBuilder getRevealOrBuilder();

        public boolean hasEnd();

        public AuctionEndMsg getEnd();

        public AuctionEndMsgOrBuilder getEndOrBuilder();
    }

}

