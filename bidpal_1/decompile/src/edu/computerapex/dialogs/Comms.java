/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.dialogs;

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
import com.google.protobuf.SingleFieldBuilder;
import com.google.protobuf.UnknownFieldSet;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public final class Comms {
    private static Descriptors.Descriptor internal_static_com_cyberpointllc_stac_comms_DHPublicKey_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_com_cyberpointllc_stac_comms_DHPublicKey_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_com_cyberpointllc_stac_comms_CommsMsg_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_com_cyberpointllc_stac_comms_CommsMsg_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_com_cyberpointllc_stac_comms_ClientSetup_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_com_cyberpointllc_stac_comms_ClientSetup_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_com_cyberpointllc_stac_comms_ServerSetup_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_com_cyberpointllc_stac_comms_ServerSetup_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_com_cyberpointllc_stac_comms_ClientResponse_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_com_cyberpointllc_stac_comms_ClientResponse_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_com_cyberpointllc_stac_comms_ServerResponse_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_com_cyberpointllc_stac_comms_ServerResponse_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_com_cyberpointllc_stac_comms_NetworkAddress_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_com_cyberpointllc_stac_comms_NetworkAddress_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_com_cyberpointllc_stac_comms_Identity_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_com_cyberpointllc_stac_comms_Identity_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_com_cyberpointllc_stac_comms_PublicKey_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_com_cyberpointllc_stac_comms_PublicKey_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_com_cyberpointllc_stac_comms_SignedMessage_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_com_cyberpointllc_stac_comms_SignedMessage_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_com_cyberpointllc_stac_comms_RsaTest_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_com_cyberpointllc_stac_comms_RsaTest_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_com_cyberpointllc_stac_comms_RsaResponse_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_com_cyberpointllc_stac_comms_RsaResponse_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_com_cyberpointllc_stac_comms_RsaResults_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_com_cyberpointllc_stac_comms_RsaResults_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_com_cyberpointllc_stac_comms_ClientResponseToFailure_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_com_cyberpointllc_stac_comms_ClientResponseToFailure_fieldAccessorTable;
    private static Descriptors.FileDescriptor descriptor;

    private Comms() {
    }

    public static void registerAllExtensions(ExtensionRegistry registry) {
    }

    public static Descriptors.FileDescriptor getDescriptor() {
        return descriptor;
    }

    static {
        String[] descriptorData = new String[]{"\n\u000bcomms.proto\u0012\u001ccom.cyberpointllc.stac.comms\"\u001a\n\u000bDHPublicKey\u0012\u000b\n\u0003key\u0018\u0001 \u0002(\f\"\u00f1\u0001\n\bCommsMsg\u00129\n\u0004type\u0018\u0001 \u0002(\u000e2+.com.cyberpointllc.stac.comms.CommsMsg.Type\u0012>\n\u000bclientSetup\u0018\u0002 \u0001(\u000b2).com.cyberpointllc.stac.comms.ClientSetup\u0012>\n\u000bserverSetup\u0018\u0003 \u0001(\u000b2).com.cyberpointllc.stac.comms.ServerSetup\"*\n\u0004Type\u0012\u0010\n\fCLIENT_SETUP\u0010\u0001\u0012\u0010\n\fSERVER_SETUP\u0010\u0002\"\n\u000bClientSetup\u00128\n\bidentity\u0018\u0001 \u0002(\u000b2&.com.cyberpointllc.stac.comms.Identity\u00126\n\u0003key\u0018\u0002 \u0002(", "\u000b2).com.cyberpointllc.stac.comms.DHPublicKey\"\u00b7\u0001\n\u000bServerSetup\u00128\n\bidentity\u0018\u0001 \u0002(\u000b2&.com.cyberpointllc.stac.comms.Identity\u00126\n\u0003key\u0018\u0002 \u0002(\u000b2).com.cyberpointllc.stac.comms.DHPublicKey\u00126\n\u0007rsaTest\u0018\u0003 \u0002(\u000b2%.com.cyberpointllc.stac.comms.RsaTest\"\u0088\u0001\n\u000eClientResponse\u0012>\n\u000brsaResponse\u0018\u0001 \u0002(\u000b2).com.cyberpointllc.stac.comms.RsaResponse\u00126\n\u0007rsaTest\u0018\u0002 \u0002(\u000b2%.com.cyberpointllc.stac.comms.RsaTest\"\u008e\u0001\n\u000eServerResponse\u0012>\n\u000brsaRespo", "nse\u0018\u0001 \u0002(\u000b2).com.cyberpointllc.stac.comms.RsaResponse\u0012<\n\nrsaResults\u0018\u0002 \u0002(\u000b2(.com.cyberpointllc.stac.comms.RsaResults\",\n\u000eNetworkAddress\u0012\f\n\u0004host\u0018\u0001 \u0002(\t\u0012\f\n\u0004port\u0018\u0002 \u0002(\u0005\"\u0099\u0001\n\bIdentity\u0012\n\n\u0002id\u0018\u0001 \u0002(\t\u0012:\n\tpublicKey\u0018\u0002 \u0002(\u000b2'.com.cyberpointllc.stac.comms.PublicKey\u0012E\n\u000fcallbackAddress\u0018\u0003 \u0001(\u000b2,.com.cyberpointllc.stac.comms.NetworkAddress\"'\n\tPublicKey\u0012\t\n\u0001e\u0018\u0001 \u0002(\f\u0012\u000f\n\u0007modulus\u0018\u0002 \u0002(\f\"i\n\rSignedMessage\u0012\f\n\u0004data\u0018\u0001 \u0002(\f\u0012\u0012\n\nsignedHa", "sh\u0018\u0002 \u0002(\f\u00126\n\u0007rsaTest\u0018\u0003 \u0001(\u000b2%.com.cyberpointllc.stac.comms.RsaTest\"\u0017\n\u0007RsaTest\u0012\f\n\u0004test\u0018\u0001 \u0002(\f\"\u001f\n\u000bRsaResponse\u0012\u0010\n\bresponse\u0018\u0001 \u0002(\f\"\u001d\n\nRsaResults\u0012\u000f\n\u0007results\u0018\u0001 \u0002(\f\"\u008f\u0001\n\u0017ClientResponseToFailure\u0012<\n\nrsaResults\u0018\u0001 \u0002(\u000b2(.com.cyberpointllc.stac.comms.RsaResults\u00126\n\u0007rsaTest\u0018\u0002 \u0002(\u000b2%.com.cyberpointllc.stac.comms.RsaTest"};
        Descriptors.FileDescriptor.InternalDescriptorAssigner assigner = new Descriptors.FileDescriptor.InternalDescriptorAssigner(){

            @Override
            public ExtensionRegistry assignDescriptors(Descriptors.FileDescriptor root) {
                descriptor = root;
                return null;
            }
        };
        Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(descriptorData, new Descriptors.FileDescriptor[0], assigner);
        internal_static_com_cyberpointllc_stac_comms_DHPublicKey_descriptor = Comms.getDescriptor().getMessageTypes().get(0);
        internal_static_com_cyberpointllc_stac_comms_DHPublicKey_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_com_cyberpointllc_stac_comms_DHPublicKey_descriptor, new String[]{"Key"});
        internal_static_com_cyberpointllc_stac_comms_CommsMsg_descriptor = Comms.getDescriptor().getMessageTypes().get(1);
        internal_static_com_cyberpointllc_stac_comms_CommsMsg_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_com_cyberpointllc_stac_comms_CommsMsg_descriptor, new String[]{"Type", "ClientSetup", "ServerSetup"});
        internal_static_com_cyberpointllc_stac_comms_ClientSetup_descriptor = Comms.getDescriptor().getMessageTypes().get(2);
        internal_static_com_cyberpointllc_stac_comms_ClientSetup_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_com_cyberpointllc_stac_comms_ClientSetup_descriptor, new String[]{"Identity", "Key"});
        internal_static_com_cyberpointllc_stac_comms_ServerSetup_descriptor = Comms.getDescriptor().getMessageTypes().get(3);
        internal_static_com_cyberpointllc_stac_comms_ServerSetup_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_com_cyberpointllc_stac_comms_ServerSetup_descriptor, new String[]{"Identity", "Key", "RsaTest"});
        internal_static_com_cyberpointllc_stac_comms_ClientResponse_descriptor = Comms.getDescriptor().getMessageTypes().get(4);
        internal_static_com_cyberpointllc_stac_comms_ClientResponse_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_com_cyberpointllc_stac_comms_ClientResponse_descriptor, new String[]{"RsaResponse", "RsaTest"});
        internal_static_com_cyberpointllc_stac_comms_ServerResponse_descriptor = Comms.getDescriptor().getMessageTypes().get(5);
        internal_static_com_cyberpointllc_stac_comms_ServerResponse_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_com_cyberpointllc_stac_comms_ServerResponse_descriptor, new String[]{"RsaResponse", "RsaResults"});
        internal_static_com_cyberpointllc_stac_comms_NetworkAddress_descriptor = Comms.getDescriptor().getMessageTypes().get(6);
        internal_static_com_cyberpointllc_stac_comms_NetworkAddress_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_com_cyberpointllc_stac_comms_NetworkAddress_descriptor, new String[]{"Host", "Port"});
        internal_static_com_cyberpointllc_stac_comms_Identity_descriptor = Comms.getDescriptor().getMessageTypes().get(7);
        internal_static_com_cyberpointllc_stac_comms_Identity_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_com_cyberpointllc_stac_comms_Identity_descriptor, new String[]{"Id", "PublicKey", "CallbackAddress"});
        internal_static_com_cyberpointllc_stac_comms_PublicKey_descriptor = Comms.getDescriptor().getMessageTypes().get(8);
        internal_static_com_cyberpointllc_stac_comms_PublicKey_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_com_cyberpointllc_stac_comms_PublicKey_descriptor, new String[]{"E", "Modulus"});
        internal_static_com_cyberpointllc_stac_comms_SignedMessage_descriptor = Comms.getDescriptor().getMessageTypes().get(9);
        internal_static_com_cyberpointllc_stac_comms_SignedMessage_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_com_cyberpointllc_stac_comms_SignedMessage_descriptor, new String[]{"Data", "SignedHash", "RsaTest"});
        internal_static_com_cyberpointllc_stac_comms_RsaTest_descriptor = Comms.getDescriptor().getMessageTypes().get(10);
        internal_static_com_cyberpointllc_stac_comms_RsaTest_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_com_cyberpointllc_stac_comms_RsaTest_descriptor, new String[]{"Test"});
        internal_static_com_cyberpointllc_stac_comms_RsaResponse_descriptor = Comms.getDescriptor().getMessageTypes().get(11);
        internal_static_com_cyberpointllc_stac_comms_RsaResponse_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_com_cyberpointllc_stac_comms_RsaResponse_descriptor, new String[]{"Response"});
        internal_static_com_cyberpointllc_stac_comms_RsaResults_descriptor = Comms.getDescriptor().getMessageTypes().get(12);
        internal_static_com_cyberpointllc_stac_comms_RsaResults_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_com_cyberpointllc_stac_comms_RsaResults_descriptor, new String[]{"Results"});
        internal_static_com_cyberpointllc_stac_comms_ClientResponseToFailure_descriptor = Comms.getDescriptor().getMessageTypes().get(13);
        internal_static_com_cyberpointllc_stac_comms_ClientResponseToFailure_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_com_cyberpointllc_stac_comms_ClientResponseToFailure_descriptor, new String[]{"RsaResults", "RsaTest"});
    }

    public static final class ClientResponseToFailure
    extends GeneratedMessage
    implements ClientResponseToFailureOrBuilder {
        private int bitField0_;
        public static final int RSARESULTS_FIELD_NUMBER = 1;
        private RsaResults rsaResults_;
        public static final int RSATEST_FIELD_NUMBER = 2;
        private RsaTest rsaTest_;
        private byte memoizedIsInitialized;
        private static final long serialVersionUID = 0;
        private static final ClientResponseToFailure DEFAULT_INSTANCE = new ClientResponseToFailure();
        @Deprecated
        public static final Parser<ClientResponseToFailure> PARSER = new AbstractParser<ClientResponseToFailure>(){

            @Override
            public ClientResponseToFailure parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                try {
                    return new ClientResponseToFailure(input, extensionRegistry);
                }
                catch (RuntimeException e) {
                    if (e.getCause() instanceof InvalidProtocolBufferException) {
                        throw (InvalidProtocolBufferException)e.getCause();
                    }
                    throw e;
                }
            }
        };

        private ClientResponseToFailure(GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
        }

        private ClientResponseToFailure() {
            this.memoizedIsInitialized = -1;
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private ClientResponseToFailure(CodedInputStream input, ExtensionRegistryLite extensionRegistry) {
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
                            RsaResults.Builder subBuilder = null;
                            if ((this.bitField0_ & 1) == 1) {
                                subBuilder = this.rsaResults_.toBuilder();
                            }
                            this.rsaResults_ = input.readMessage(RsaResults.parser(), extensionRegistry);
                            if (subBuilder != null) {
                                subBuilder.mergeFrom(this.rsaResults_);
                                this.rsaResults_ = subBuilder.buildPartial();
                            }
                            this.bitField0_ |= 1;
                            continue block11;
                        }
                        case 18: {
                            RsaTest.Builder subBuilder = null;
                            if ((this.bitField0_ & 2) == 2) {
                                subBuilder = this.rsaTest_.toBuilder();
                            }
                            this.rsaTest_ = input.readMessage(RsaTest.parser(), extensionRegistry);
                            if (subBuilder != null) {
                                subBuilder.mergeFrom(this.rsaTest_);
                                this.rsaTest_ = subBuilder.buildPartial();
                            }
                            this.bitField0_ |= 2;
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
            return internal_static_com_cyberpointllc_stac_comms_ClientResponseToFailure_descriptor;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_com_cyberpointllc_stac_comms_ClientResponseToFailure_fieldAccessorTable.ensureFieldAccessorsInitialized(ClientResponseToFailure.class, Builder.class);
        }

        @Override
        public boolean hasRsaResults() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override
        public RsaResults getRsaResults() {
            return this.rsaResults_ == null ? RsaResults.getDefaultInstance() : this.rsaResults_;
        }

        @Override
        public RsaResultsOrBuilder getRsaResultsOrBuilder() {
            return this.rsaResults_ == null ? RsaResults.getDefaultInstance() : this.rsaResults_;
        }

        @Override
        public boolean hasRsaTest() {
            return (this.bitField0_ & 2) == 2;
        }

        @Override
        public RsaTest getRsaTest() {
            return this.rsaTest_ == null ? RsaTest.getDefaultInstance() : this.rsaTest_;
        }

        @Override
        public RsaTestOrBuilder getRsaTestOrBuilder() {
            return this.rsaTest_ == null ? RsaTest.getDefaultInstance() : this.rsaTest_;
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
            if (!this.hasRsaResults()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.hasRsaTest()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.getRsaResults().isInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.getRsaTest().isInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public void writeTo(CodedOutputStream output) throws IOException {
            if ((this.bitField0_ & 1) == 1) {
                output.writeMessage(1, this.getRsaResults());
            }
            if ((this.bitField0_ & 2) == 2) {
                output.writeMessage(2, this.getRsaTest());
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
                size += CodedOutputStream.computeMessageSize(1, this.getRsaResults());
            }
            if ((this.bitField0_ & 2) == 2) {
                size += CodedOutputStream.computeMessageSize(2, this.getRsaTest());
            }
            this.memoizedSize = size += this.unknownFields.getSerializedSize();
            return size;
        }

        public static ClientResponseToFailure parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static ClientResponseToFailure parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static ClientResponseToFailure parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static ClientResponseToFailure parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static ClientResponseToFailure parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static ClientResponseToFailure parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static ClientResponseToFailure parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static ClientResponseToFailure parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static ClientResponseToFailure parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static ClientResponseToFailure parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        @Override
        public Builder newBuilderForType() {
            return ClientResponseToFailure.newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(ClientResponseToFailure prototype) {
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

        public static ClientResponseToFailure getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<ClientResponseToFailure> parser() {
            return PARSER;
        }

        public Parser<ClientResponseToFailure> getParserForType() {
            return PARSER;
        }

        @Override
        public ClientResponseToFailure getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }

        public static final class Builder
        extends GeneratedMessage.Builder<Builder>
        implements ClientResponseToFailureOrBuilder {
            private int bitField0_;
            private RsaResults rsaResults_ = null;
            private SingleFieldBuilder<RsaResults, RsaResults.Builder, RsaResultsOrBuilder> rsaResultsBuilder_;
            private RsaTest rsaTest_ = null;
            private SingleFieldBuilder<RsaTest, RsaTest.Builder, RsaTestOrBuilder> rsaTestBuilder_;

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_com_cyberpointllc_stac_comms_ClientResponseToFailure_descriptor;
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_com_cyberpointllc_stac_comms_ClientResponseToFailure_fieldAccessorTable.ensureFieldAccessorsInitialized(ClientResponseToFailure.class, Builder.class);
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
                    this.getRsaResultsFieldBuilder();
                    this.getRsaTestFieldBuilder();
                }
            }

            @Override
            public Builder clear() {
                super.clear();
                if (this.rsaResultsBuilder_ == null) {
                    this.rsaResults_ = null;
                } else {
                    this.rsaResultsBuilder_.clear();
                }
                this.bitField0_ &= -2;
                if (this.rsaTestBuilder_ == null) {
                    this.rsaTest_ = null;
                } else {
                    this.rsaTestBuilder_.clear();
                }
                this.bitField0_ &= -3;
                return this;
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_com_cyberpointllc_stac_comms_ClientResponseToFailure_descriptor;
            }

            @Override
            public ClientResponseToFailure getDefaultInstanceForType() {
                return ClientResponseToFailure.getDefaultInstance();
            }

            @Override
            public ClientResponseToFailure build() {
                ClientResponseToFailure result = this.buildPartial();
                if (!result.isInitialized()) {
                    throw AbstractMessage.Builder.newUninitializedMessageException(result);
                }
                return result;
            }

            @Override
            public ClientResponseToFailure buildPartial() {
                ClientResponseToFailure result = new ClientResponseToFailure(this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) == 1) {
                    to_bitField0_ |= 1;
                }
                if (this.rsaResultsBuilder_ == null) {
                    result.rsaResults_ = this.rsaResults_;
                } else {
                    result.rsaResults_ = this.rsaResultsBuilder_.build();
                }
                if ((from_bitField0_ & 2) == 2) {
                    to_bitField0_ |= 2;
                }
                if (this.rsaTestBuilder_ == null) {
                    result.rsaTest_ = this.rsaTest_;
                } else {
                    result.rsaTest_ = this.rsaTestBuilder_.build();
                }
                result.bitField0_ = to_bitField0_;
                this.onBuilt();
                return result;
            }

            @Override
            public Builder mergeFrom(Message other) {
                if (other instanceof ClientResponseToFailure) {
                    return this.mergeFrom((ClientResponseToFailure)other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(ClientResponseToFailure other) {
                if (other == ClientResponseToFailure.getDefaultInstance()) {
                    return this;
                }
                if (other.hasRsaResults()) {
                    this.mergeRsaResults(other.getRsaResults());
                }
                if (other.hasRsaTest()) {
                    this.mergeRsaTest(other.getRsaTest());
                }
                this.mergeUnknownFields(other.unknownFields);
                this.onChanged();
                return this;
            }

            @Override
            public final boolean isInitialized() {
                if (!this.hasRsaResults()) {
                    return false;
                }
                if (!this.hasRsaTest()) {
                    return false;
                }
                if (!this.getRsaResults().isInitialized()) {
                    return false;
                }
                if (!this.getRsaTest().isInitialized()) {
                    return false;
                }
                return true;
            }

            @Override
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                ClientResponseToFailure parsedMessage = null;
                try {
                    parsedMessage = ClientResponseToFailure.PARSER.parsePartialFrom(input, extensionRegistry);
                }
                catch (InvalidProtocolBufferException e) {
                    parsedMessage = (ClientResponseToFailure)e.getUnfinishedMessage();
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
            public boolean hasRsaResults() {
                return (this.bitField0_ & 1) == 1;
            }

            @Override
            public RsaResults getRsaResults() {
                if (this.rsaResultsBuilder_ == null) {
                    return this.rsaResults_ == null ? RsaResults.getDefaultInstance() : this.rsaResults_;
                }
                return this.rsaResultsBuilder_.getMessage();
            }

            public Builder setRsaResults(RsaResults value) {
                if (this.rsaResultsBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.rsaResults_ = value;
                    this.onChanged();
                } else {
                    this.rsaResultsBuilder_.setMessage(value);
                }
                this.bitField0_ |= 1;
                return this;
            }

            public Builder setRsaResults(RsaResults.Builder builderForValue) {
                if (this.rsaResultsBuilder_ == null) {
                    this.rsaResults_ = builderForValue.build();
                    this.onChanged();
                } else {
                    this.rsaResultsBuilder_.setMessage(builderForValue.build());
                }
                this.bitField0_ |= 1;
                return this;
            }

            public Builder mergeRsaResults(RsaResults value) {
                if (this.rsaResultsBuilder_ == null) {
                    this.rsaResults_ = (this.bitField0_ & 1) == 1 && this.rsaResults_ != null && this.rsaResults_ != RsaResults.getDefaultInstance() ? RsaResults.newBuilder(this.rsaResults_).mergeFrom(value).buildPartial() : value;
                    this.onChanged();
                } else {
                    this.rsaResultsBuilder_.mergeFrom(value);
                }
                this.bitField0_ |= 1;
                return this;
            }

            public Builder clearRsaResults() {
                if (this.rsaResultsBuilder_ == null) {
                    this.rsaResults_ = null;
                    this.onChanged();
                } else {
                    this.rsaResultsBuilder_.clear();
                }
                this.bitField0_ &= -2;
                return this;
            }

            public RsaResults.Builder getRsaResultsBuilder() {
                this.bitField0_ |= 1;
                this.onChanged();
                return this.getRsaResultsFieldBuilder().getBuilder();
            }

            @Override
            public RsaResultsOrBuilder getRsaResultsOrBuilder() {
                if (this.rsaResultsBuilder_ != null) {
                    return this.rsaResultsBuilder_.getMessageOrBuilder();
                }
                return this.rsaResults_ == null ? RsaResults.getDefaultInstance() : this.rsaResults_;
            }

            private SingleFieldBuilder<RsaResults, RsaResults.Builder, RsaResultsOrBuilder> getRsaResultsFieldBuilder() {
                if (this.rsaResultsBuilder_ == null) {
                    this.rsaResultsBuilder_ = new SingleFieldBuilder(this.getRsaResults(), this.getParentForChildren(), this.isClean());
                    this.rsaResults_ = null;
                }
                return this.rsaResultsBuilder_;
            }

            @Override
            public boolean hasRsaTest() {
                return (this.bitField0_ & 2) == 2;
            }

            @Override
            public RsaTest getRsaTest() {
                if (this.rsaTestBuilder_ == null) {
                    return this.rsaTest_ == null ? RsaTest.getDefaultInstance() : this.rsaTest_;
                }
                return this.rsaTestBuilder_.getMessage();
            }

            public Builder setRsaTest(RsaTest value) {
                if (this.rsaTestBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.rsaTest_ = value;
                    this.onChanged();
                } else {
                    this.rsaTestBuilder_.setMessage(value);
                }
                this.bitField0_ |= 2;
                return this;
            }

            public Builder setRsaTest(RsaTest.Builder builderForValue) {
                if (this.rsaTestBuilder_ == null) {
                    this.rsaTest_ = builderForValue.build();
                    this.onChanged();
                } else {
                    this.rsaTestBuilder_.setMessage(builderForValue.build());
                }
                this.bitField0_ |= 2;
                return this;
            }

            public Builder mergeRsaTest(RsaTest value) {
                if (this.rsaTestBuilder_ == null) {
                    this.rsaTest_ = (this.bitField0_ & 2) == 2 && this.rsaTest_ != null && this.rsaTest_ != RsaTest.getDefaultInstance() ? RsaTest.newBuilder(this.rsaTest_).mergeFrom(value).buildPartial() : value;
                    this.onChanged();
                } else {
                    this.rsaTestBuilder_.mergeFrom(value);
                }
                this.bitField0_ |= 2;
                return this;
            }

            public Builder clearRsaTest() {
                if (this.rsaTestBuilder_ == null) {
                    this.rsaTest_ = null;
                    this.onChanged();
                } else {
                    this.rsaTestBuilder_.clear();
                }
                this.bitField0_ &= -3;
                return this;
            }

            public RsaTest.Builder getRsaTestBuilder() {
                this.bitField0_ |= 2;
                this.onChanged();
                return this.getRsaTestFieldBuilder().getBuilder();
            }

            @Override
            public RsaTestOrBuilder getRsaTestOrBuilder() {
                if (this.rsaTestBuilder_ != null) {
                    return this.rsaTestBuilder_.getMessageOrBuilder();
                }
                return this.rsaTest_ == null ? RsaTest.getDefaultInstance() : this.rsaTest_;
            }

            private SingleFieldBuilder<RsaTest, RsaTest.Builder, RsaTestOrBuilder> getRsaTestFieldBuilder() {
                if (this.rsaTestBuilder_ == null) {
                    this.rsaTestBuilder_ = new SingleFieldBuilder(this.getRsaTest(), this.getParentForChildren(), this.isClean());
                    this.rsaTest_ = null;
                }
                return this.rsaTestBuilder_;
            }
        }

    }

    public static interface ClientResponseToFailureOrBuilder
    extends MessageOrBuilder {
        public boolean hasRsaResults();

        public RsaResults getRsaResults();

        public RsaResultsOrBuilder getRsaResultsOrBuilder();

        public boolean hasRsaTest();

        public RsaTest getRsaTest();

        public RsaTestOrBuilder getRsaTestOrBuilder();
    }

    public static final class RsaResults
    extends GeneratedMessage
    implements RsaResultsOrBuilder {
        private int bitField0_;
        public static final int RESULTS_FIELD_NUMBER = 1;
        private ByteString results_;
        private byte memoizedIsInitialized;
        private static final long serialVersionUID = 0;
        private static final RsaResults DEFAULT_INSTANCE = new RsaResults();
        @Deprecated
        public static final Parser<RsaResults> PARSER = new AbstractParser<RsaResults>(){

            @Override
            public RsaResults parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                try {
                    return new RsaResults(input, extensionRegistry);
                }
                catch (RuntimeException e) {
                    if (e.getCause() instanceof InvalidProtocolBufferException) {
                        throw (InvalidProtocolBufferException)e.getCause();
                    }
                    throw e;
                }
            }
        };

        private RsaResults(GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
        }

        private RsaResults() {
            this.memoizedIsInitialized = -1;
            this.results_ = ByteString.EMPTY;
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private RsaResults(CodedInputStream input, ExtensionRegistryLite extensionRegistry) {
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
                    this.results_ = input.readBytes();
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
            return internal_static_com_cyberpointllc_stac_comms_RsaResults_descriptor;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_com_cyberpointllc_stac_comms_RsaResults_fieldAccessorTable.ensureFieldAccessorsInitialized(RsaResults.class, Builder.class);
        }

        @Override
        public boolean hasResults() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override
        public ByteString getResults() {
            return this.results_;
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
            if (!this.hasResults()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public void writeTo(CodedOutputStream output) throws IOException {
            if ((this.bitField0_ & 1) == 1) {
                output.writeBytes(1, this.results_);
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
                size += CodedOutputStream.computeBytesSize(1, this.results_);
            }
            this.memoizedSize = size += this.unknownFields.getSerializedSize();
            return size;
        }

        public static RsaResults parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static RsaResults parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static RsaResults parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static RsaResults parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static RsaResults parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static RsaResults parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static RsaResults parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static RsaResults parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static RsaResults parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static RsaResults parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        @Override
        public Builder newBuilderForType() {
            return RsaResults.newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(RsaResults prototype) {
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

        public static RsaResults getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<RsaResults> parser() {
            return PARSER;
        }

        public Parser<RsaResults> getParserForType() {
            return PARSER;
        }

        @Override
        public RsaResults getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }

        public static final class Builder
        extends GeneratedMessage.Builder<Builder>
        implements RsaResultsOrBuilder {
            private int bitField0_;
            private ByteString results_ = ByteString.EMPTY;

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_com_cyberpointllc_stac_comms_RsaResults_descriptor;
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_com_cyberpointllc_stac_comms_RsaResults_fieldAccessorTable.ensureFieldAccessorsInitialized(RsaResults.class, Builder.class);
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
                this.results_ = ByteString.EMPTY;
                this.bitField0_ &= -2;
                return this;
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_com_cyberpointllc_stac_comms_RsaResults_descriptor;
            }

            @Override
            public RsaResults getDefaultInstanceForType() {
                return RsaResults.getDefaultInstance();
            }

            @Override
            public RsaResults build() {
                RsaResults result = this.buildPartial();
                if (!result.isInitialized()) {
                    throw AbstractMessage.Builder.newUninitializedMessageException(result);
                }
                return result;
            }

            @Override
            public RsaResults buildPartial() {
                RsaResults result = new RsaResults(this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) == 1) {
                    to_bitField0_ |= 1;
                }
                result.results_ = this.results_;
                result.bitField0_ = to_bitField0_;
                this.onBuilt();
                return result;
            }

            @Override
            public Builder mergeFrom(Message other) {
                if (other instanceof RsaResults) {
                    return this.mergeFrom((RsaResults)other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(RsaResults other) {
                if (other == RsaResults.getDefaultInstance()) {
                    return this;
                }
                if (other.hasResults()) {
                    this.setResults(other.getResults());
                }
                this.mergeUnknownFields(other.unknownFields);
                this.onChanged();
                return this;
            }

            @Override
            public final boolean isInitialized() {
                if (!this.hasResults()) {
                    return false;
                }
                return true;
            }

            @Override
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                RsaResults parsedMessage = null;
                try {
                    parsedMessage = RsaResults.PARSER.parsePartialFrom(input, extensionRegistry);
                }
                catch (InvalidProtocolBufferException e) {
                    parsedMessage = (RsaResults)e.getUnfinishedMessage();
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
            public boolean hasResults() {
                return (this.bitField0_ & 1) == 1;
            }

            @Override
            public ByteString getResults() {
                return this.results_;
            }

            public Builder setResults(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.results_ = value;
                this.onChanged();
                return this;
            }

            public Builder clearResults() {
                this.bitField0_ &= -2;
                this.results_ = RsaResults.getDefaultInstance().getResults();
                this.onChanged();
                return this;
            }
        }

    }

    public static interface RsaResultsOrBuilder
    extends MessageOrBuilder {
        public boolean hasResults();

        public ByteString getResults();
    }

    public static final class RsaResponse
    extends GeneratedMessage
    implements RsaResponseOrBuilder {
        private int bitField0_;
        public static final int RESPONSE_FIELD_NUMBER = 1;
        private ByteString response_;
        private byte memoizedIsInitialized;
        private static final long serialVersionUID = 0;
        private static final RsaResponse DEFAULT_INSTANCE = new RsaResponse();
        @Deprecated
        public static final Parser<RsaResponse> PARSER = new AbstractParser<RsaResponse>(){

            @Override
            public RsaResponse parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                try {
                    return new RsaResponse(input, extensionRegistry);
                }
                catch (RuntimeException e) {
                    if (e.getCause() instanceof InvalidProtocolBufferException) {
                        throw (InvalidProtocolBufferException)e.getCause();
                    }
                    throw e;
                }
            }
        };

        private RsaResponse(GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
        }

        private RsaResponse() {
            this.memoizedIsInitialized = -1;
            this.response_ = ByteString.EMPTY;
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private RsaResponse(CodedInputStream input, ExtensionRegistryLite extensionRegistry) {
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
                    this.response_ = input.readBytes();
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
            return internal_static_com_cyberpointllc_stac_comms_RsaResponse_descriptor;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_com_cyberpointllc_stac_comms_RsaResponse_fieldAccessorTable.ensureFieldAccessorsInitialized(RsaResponse.class, Builder.class);
        }

        @Override
        public boolean hasResponse() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override
        public ByteString getResponse() {
            return this.response_;
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
            if (!this.hasResponse()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public void writeTo(CodedOutputStream output) throws IOException {
            if ((this.bitField0_ & 1) == 1) {
                output.writeBytes(1, this.response_);
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
                size += CodedOutputStream.computeBytesSize(1, this.response_);
            }
            this.memoizedSize = size += this.unknownFields.getSerializedSize();
            return size;
        }

        public static RsaResponse parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static RsaResponse parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static RsaResponse parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static RsaResponse parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static RsaResponse parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static RsaResponse parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static RsaResponse parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static RsaResponse parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static RsaResponse parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static RsaResponse parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        @Override
        public Builder newBuilderForType() {
            return RsaResponse.newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(RsaResponse prototype) {
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

        public static RsaResponse getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<RsaResponse> parser() {
            return PARSER;
        }

        public Parser<RsaResponse> getParserForType() {
            return PARSER;
        }

        @Override
        public RsaResponse getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }

        public static final class Builder
        extends GeneratedMessage.Builder<Builder>
        implements RsaResponseOrBuilder {
            private int bitField0_;
            private ByteString response_ = ByteString.EMPTY;

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_com_cyberpointllc_stac_comms_RsaResponse_descriptor;
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_com_cyberpointllc_stac_comms_RsaResponse_fieldAccessorTable.ensureFieldAccessorsInitialized(RsaResponse.class, Builder.class);
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
                this.response_ = ByteString.EMPTY;
                this.bitField0_ &= -2;
                return this;
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_com_cyberpointllc_stac_comms_RsaResponse_descriptor;
            }

            @Override
            public RsaResponse getDefaultInstanceForType() {
                return RsaResponse.getDefaultInstance();
            }

            @Override
            public RsaResponse build() {
                RsaResponse result = this.buildPartial();
                if (!result.isInitialized()) {
                    throw AbstractMessage.Builder.newUninitializedMessageException(result);
                }
                return result;
            }

            @Override
            public RsaResponse buildPartial() {
                RsaResponse result = new RsaResponse(this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) == 1) {
                    to_bitField0_ |= 1;
                }
                result.response_ = this.response_;
                result.bitField0_ = to_bitField0_;
                this.onBuilt();
                return result;
            }

            @Override
            public Builder mergeFrom(Message other) {
                if (other instanceof RsaResponse) {
                    return this.mergeFrom((RsaResponse)other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(RsaResponse other) {
                if (other == RsaResponse.getDefaultInstance()) {
                    return this;
                }
                if (other.hasResponse()) {
                    this.setResponse(other.getResponse());
                }
                this.mergeUnknownFields(other.unknownFields);
                this.onChanged();
                return this;
            }

            @Override
            public final boolean isInitialized() {
                if (!this.hasResponse()) {
                    return false;
                }
                return true;
            }

            @Override
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                RsaResponse parsedMessage = null;
                try {
                    parsedMessage = RsaResponse.PARSER.parsePartialFrom(input, extensionRegistry);
                }
                catch (InvalidProtocolBufferException e) {
                    parsedMessage = (RsaResponse)e.getUnfinishedMessage();
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
            public boolean hasResponse() {
                return (this.bitField0_ & 1) == 1;
            }

            @Override
            public ByteString getResponse() {
                return this.response_;
            }

            public Builder setResponse(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.response_ = value;
                this.onChanged();
                return this;
            }

            public Builder clearResponse() {
                this.bitField0_ &= -2;
                this.response_ = RsaResponse.getDefaultInstance().getResponse();
                this.onChanged();
                return this;
            }
        }

    }

    public static interface RsaResponseOrBuilder
    extends MessageOrBuilder {
        public boolean hasResponse();

        public ByteString getResponse();
    }

    public static final class RsaTest
    extends GeneratedMessage
    implements RsaTestOrBuilder {
        private int bitField0_;
        public static final int TEST_FIELD_NUMBER = 1;
        private ByteString test_;
        private byte memoizedIsInitialized;
        private static final long serialVersionUID = 0;
        private static final RsaTest DEFAULT_INSTANCE = new RsaTest();
        @Deprecated
        public static final Parser<RsaTest> PARSER = new AbstractParser<RsaTest>(){

            @Override
            public RsaTest parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                try {
                    return new RsaTest(input, extensionRegistry);
                }
                catch (RuntimeException e) {
                    if (e.getCause() instanceof InvalidProtocolBufferException) {
                        throw (InvalidProtocolBufferException)e.getCause();
                    }
                    throw e;
                }
            }
        };

        private RsaTest(GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
        }

        private RsaTest() {
            this.memoizedIsInitialized = -1;
            this.test_ = ByteString.EMPTY;
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private RsaTest(CodedInputStream input, ExtensionRegistryLite extensionRegistry) {
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
                    this.test_ = input.readBytes();
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
            return internal_static_com_cyberpointllc_stac_comms_RsaTest_descriptor;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_com_cyberpointllc_stac_comms_RsaTest_fieldAccessorTable.ensureFieldAccessorsInitialized(RsaTest.class, Builder.class);
        }

        @Override
        public boolean hasTest() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override
        public ByteString getTest() {
            return this.test_;
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
            if (!this.hasTest()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public void writeTo(CodedOutputStream output) throws IOException {
            if ((this.bitField0_ & 1) == 1) {
                output.writeBytes(1, this.test_);
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
                size += CodedOutputStream.computeBytesSize(1, this.test_);
            }
            this.memoizedSize = size += this.unknownFields.getSerializedSize();
            return size;
        }

        public static RsaTest parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static RsaTest parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static RsaTest parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static RsaTest parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static RsaTest parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static RsaTest parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static RsaTest parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static RsaTest parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static RsaTest parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static RsaTest parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        @Override
        public Builder newBuilderForType() {
            return RsaTest.newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(RsaTest prototype) {
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

        public static RsaTest getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<RsaTest> parser() {
            return PARSER;
        }

        public Parser<RsaTest> getParserForType() {
            return PARSER;
        }

        @Override
        public RsaTest getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }

        public static final class Builder
        extends GeneratedMessage.Builder<Builder>
        implements RsaTestOrBuilder {
            private int bitField0_;
            private ByteString test_ = ByteString.EMPTY;

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_com_cyberpointllc_stac_comms_RsaTest_descriptor;
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_com_cyberpointllc_stac_comms_RsaTest_fieldAccessorTable.ensureFieldAccessorsInitialized(RsaTest.class, Builder.class);
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
                this.test_ = ByteString.EMPTY;
                this.bitField0_ &= -2;
                return this;
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_com_cyberpointllc_stac_comms_RsaTest_descriptor;
            }

            @Override
            public RsaTest getDefaultInstanceForType() {
                return RsaTest.getDefaultInstance();
            }

            @Override
            public RsaTest build() {
                RsaTest result = this.buildPartial();
                if (!result.isInitialized()) {
                    throw AbstractMessage.Builder.newUninitializedMessageException(result);
                }
                return result;
            }

            @Override
            public RsaTest buildPartial() {
                RsaTest result = new RsaTest(this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) == 1) {
                    to_bitField0_ |= 1;
                }
                result.test_ = this.test_;
                result.bitField0_ = to_bitField0_;
                this.onBuilt();
                return result;
            }

            @Override
            public Builder mergeFrom(Message other) {
                if (other instanceof RsaTest) {
                    return this.mergeFrom((RsaTest)other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(RsaTest other) {
                if (other == RsaTest.getDefaultInstance()) {
                    return this;
                }
                if (other.hasTest()) {
                    this.setTest(other.getTest());
                }
                this.mergeUnknownFields(other.unknownFields);
                this.onChanged();
                return this;
            }

            @Override
            public final boolean isInitialized() {
                if (!this.hasTest()) {
                    return false;
                }
                return true;
            }

            @Override
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                RsaTest parsedMessage = null;
                try {
                    parsedMessage = RsaTest.PARSER.parsePartialFrom(input, extensionRegistry);
                }
                catch (InvalidProtocolBufferException e) {
                    parsedMessage = (RsaTest)e.getUnfinishedMessage();
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
            public boolean hasTest() {
                return (this.bitField0_ & 1) == 1;
            }

            @Override
            public ByteString getTest() {
                return this.test_;
            }

            public Builder setTest(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.test_ = value;
                this.onChanged();
                return this;
            }

            public Builder clearTest() {
                this.bitField0_ &= -2;
                this.test_ = RsaTest.getDefaultInstance().getTest();
                this.onChanged();
                return this;
            }
        }

    }

    public static interface RsaTestOrBuilder
    extends MessageOrBuilder {
        public boolean hasTest();

        public ByteString getTest();
    }

    public static final class SignedMessage
    extends GeneratedMessage
    implements SignedMessageOrBuilder {
        private int bitField0_;
        public static final int DATA_FIELD_NUMBER = 1;
        private ByteString data_;
        public static final int SIGNEDHASH_FIELD_NUMBER = 2;
        private ByteString signedHash_;
        public static final int RSATEST_FIELD_NUMBER = 3;
        private RsaTest rsaTest_;
        private byte memoizedIsInitialized;
        private static final long serialVersionUID = 0;
        private static final SignedMessage DEFAULT_INSTANCE = new SignedMessage();
        @Deprecated
        public static final Parser<SignedMessage> PARSER = new AbstractParser<SignedMessage>(){

            @Override
            public SignedMessage parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                try {
                    return new SignedMessage(input, extensionRegistry);
                }
                catch (RuntimeException e) {
                    if (e.getCause() instanceof InvalidProtocolBufferException) {
                        throw (InvalidProtocolBufferException)e.getCause();
                    }
                    throw e;
                }
            }
        };

        private SignedMessage(GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
        }

        private SignedMessage() {
            this.memoizedIsInitialized = -1;
            this.data_ = ByteString.EMPTY;
            this.signedHash_ = ByteString.EMPTY;
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private SignedMessage(CodedInputStream input, ExtensionRegistryLite extensionRegistry) {
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
                            this.data_ = input.readBytes();
                            continue block12;
                        }
                        case 18: {
                            this.bitField0_ |= 2;
                            this.signedHash_ = input.readBytes();
                            continue block12;
                        }
                        case 26: 
                    }
                    RsaTest.Builder subBuilder = null;
                    if ((this.bitField0_ & 4) == 4) {
                        subBuilder = this.rsaTest_.toBuilder();
                    }
                    this.rsaTest_ = input.readMessage(RsaTest.parser(), extensionRegistry);
                    if (subBuilder != null) {
                        subBuilder.mergeFrom(this.rsaTest_);
                        this.rsaTest_ = subBuilder.buildPartial();
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
            return internal_static_com_cyberpointllc_stac_comms_SignedMessage_descriptor;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_com_cyberpointllc_stac_comms_SignedMessage_fieldAccessorTable.ensureFieldAccessorsInitialized(SignedMessage.class, Builder.class);
        }

        @Override
        public boolean hasData() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override
        public ByteString getData() {
            return this.data_;
        }

        @Override
        public boolean hasSignedHash() {
            return (this.bitField0_ & 2) == 2;
        }

        @Override
        public ByteString getSignedHash() {
            return this.signedHash_;
        }

        @Override
        public boolean hasRsaTest() {
            return (this.bitField0_ & 4) == 4;
        }

        @Override
        public RsaTest getRsaTest() {
            return this.rsaTest_ == null ? RsaTest.getDefaultInstance() : this.rsaTest_;
        }

        @Override
        public RsaTestOrBuilder getRsaTestOrBuilder() {
            return this.rsaTest_ == null ? RsaTest.getDefaultInstance() : this.rsaTest_;
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
            if (!this.hasData()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.hasSignedHash()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (this.hasRsaTest() && !this.getRsaTest().isInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public void writeTo(CodedOutputStream output) throws IOException {
            if ((this.bitField0_ & 1) == 1) {
                output.writeBytes(1, this.data_);
            }
            if ((this.bitField0_ & 2) == 2) {
                output.writeBytes(2, this.signedHash_);
            }
            if ((this.bitField0_ & 4) == 4) {
                output.writeMessage(3, this.getRsaTest());
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
                size += CodedOutputStream.computeBytesSize(1, this.data_);
            }
            if ((this.bitField0_ & 2) == 2) {
                size += CodedOutputStream.computeBytesSize(2, this.signedHash_);
            }
            if ((this.bitField0_ & 4) == 4) {
                size += CodedOutputStream.computeMessageSize(3, this.getRsaTest());
            }
            this.memoizedSize = size += this.unknownFields.getSerializedSize();
            return size;
        }

        public static SignedMessage parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static SignedMessage parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static SignedMessage parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static SignedMessage parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static SignedMessage parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static SignedMessage parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static SignedMessage parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static SignedMessage parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static SignedMessage parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static SignedMessage parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        @Override
        public Builder newBuilderForType() {
            return SignedMessage.newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(SignedMessage prototype) {
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

        public static SignedMessage getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<SignedMessage> parser() {
            return PARSER;
        }

        public Parser<SignedMessage> getParserForType() {
            return PARSER;
        }

        @Override
        public SignedMessage getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }

        public static final class Builder
        extends GeneratedMessage.Builder<Builder>
        implements SignedMessageOrBuilder {
            private int bitField0_;
            private ByteString data_ = ByteString.EMPTY;
            private ByteString signedHash_ = ByteString.EMPTY;
            private RsaTest rsaTest_ = null;
            private SingleFieldBuilder<RsaTest, RsaTest.Builder, RsaTestOrBuilder> rsaTestBuilder_;

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_com_cyberpointllc_stac_comms_SignedMessage_descriptor;
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_com_cyberpointllc_stac_comms_SignedMessage_fieldAccessorTable.ensureFieldAccessorsInitialized(SignedMessage.class, Builder.class);
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
                    this.getRsaTestFieldBuilder();
                }
            }

            @Override
            public Builder clear() {
                super.clear();
                this.data_ = ByteString.EMPTY;
                this.bitField0_ &= -2;
                this.signedHash_ = ByteString.EMPTY;
                this.bitField0_ &= -3;
                if (this.rsaTestBuilder_ == null) {
                    this.rsaTest_ = null;
                } else {
                    this.rsaTestBuilder_.clear();
                }
                this.bitField0_ &= -5;
                return this;
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_com_cyberpointllc_stac_comms_SignedMessage_descriptor;
            }

            @Override
            public SignedMessage getDefaultInstanceForType() {
                return SignedMessage.getDefaultInstance();
            }

            @Override
            public SignedMessage build() {
                SignedMessage result = this.buildPartial();
                if (!result.isInitialized()) {
                    throw AbstractMessage.Builder.newUninitializedMessageException(result);
                }
                return result;
            }

            @Override
            public SignedMessage buildPartial() {
                SignedMessage result = new SignedMessage(this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) == 1) {
                    to_bitField0_ |= 1;
                }
                result.data_ = this.data_;
                if ((from_bitField0_ & 2) == 2) {
                    to_bitField0_ |= 2;
                }
                result.signedHash_ = this.signedHash_;
                if ((from_bitField0_ & 4) == 4) {
                    to_bitField0_ |= 4;
                }
                if (this.rsaTestBuilder_ == null) {
                    result.rsaTest_ = this.rsaTest_;
                } else {
                    result.rsaTest_ = this.rsaTestBuilder_.build();
                }
                result.bitField0_ = to_bitField0_;
                this.onBuilt();
                return result;
            }

            @Override
            public Builder mergeFrom(Message other) {
                if (other instanceof SignedMessage) {
                    return this.mergeFrom((SignedMessage)other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(SignedMessage other) {
                if (other == SignedMessage.getDefaultInstance()) {
                    return this;
                }
                if (other.hasData()) {
                    this.setData(other.getData());
                }
                if (other.hasSignedHash()) {
                    this.setSignedHash(other.getSignedHash());
                }
                if (other.hasRsaTest()) {
                    this.mergeRsaTest(other.getRsaTest());
                }
                this.mergeUnknownFields(other.unknownFields);
                this.onChanged();
                return this;
            }

            @Override
            public final boolean isInitialized() {
                if (!this.hasData()) {
                    return false;
                }
                if (!this.hasSignedHash()) {
                    return false;
                }
                if (this.hasRsaTest() && !this.getRsaTest().isInitialized()) {
                    return false;
                }
                return true;
            }

            @Override
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                SignedMessage parsedMessage = null;
                try {
                    parsedMessage = SignedMessage.PARSER.parsePartialFrom(input, extensionRegistry);
                }
                catch (InvalidProtocolBufferException e) {
                    parsedMessage = (SignedMessage)e.getUnfinishedMessage();
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
            public boolean hasData() {
                return (this.bitField0_ & 1) == 1;
            }

            @Override
            public ByteString getData() {
                return this.data_;
            }

            public Builder setData(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.data_ = value;
                this.onChanged();
                return this;
            }

            public Builder clearData() {
                this.bitField0_ &= -2;
                this.data_ = SignedMessage.getDefaultInstance().getData();
                this.onChanged();
                return this;
            }

            @Override
            public boolean hasSignedHash() {
                return (this.bitField0_ & 2) == 2;
            }

            @Override
            public ByteString getSignedHash() {
                return this.signedHash_;
            }

            public Builder setSignedHash(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 2;
                this.signedHash_ = value;
                this.onChanged();
                return this;
            }

            public Builder clearSignedHash() {
                this.bitField0_ &= -3;
                this.signedHash_ = SignedMessage.getDefaultInstance().getSignedHash();
                this.onChanged();
                return this;
            }

            @Override
            public boolean hasRsaTest() {
                return (this.bitField0_ & 4) == 4;
            }

            @Override
            public RsaTest getRsaTest() {
                if (this.rsaTestBuilder_ == null) {
                    return this.rsaTest_ == null ? RsaTest.getDefaultInstance() : this.rsaTest_;
                }
                return this.rsaTestBuilder_.getMessage();
            }

            public Builder setRsaTest(RsaTest value) {
                if (this.rsaTestBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.rsaTest_ = value;
                    this.onChanged();
                } else {
                    this.rsaTestBuilder_.setMessage(value);
                }
                this.bitField0_ |= 4;
                return this;
            }

            public Builder setRsaTest(RsaTest.Builder builderForValue) {
                if (this.rsaTestBuilder_ == null) {
                    this.rsaTest_ = builderForValue.build();
                    this.onChanged();
                } else {
                    this.rsaTestBuilder_.setMessage(builderForValue.build());
                }
                this.bitField0_ |= 4;
                return this;
            }

            public Builder mergeRsaTest(RsaTest value) {
                if (this.rsaTestBuilder_ == null) {
                    this.rsaTest_ = (this.bitField0_ & 4) == 4 && this.rsaTest_ != null && this.rsaTest_ != RsaTest.getDefaultInstance() ? RsaTest.newBuilder(this.rsaTest_).mergeFrom(value).buildPartial() : value;
                    this.onChanged();
                } else {
                    this.rsaTestBuilder_.mergeFrom(value);
                }
                this.bitField0_ |= 4;
                return this;
            }

            public Builder clearRsaTest() {
                if (this.rsaTestBuilder_ == null) {
                    this.rsaTest_ = null;
                    this.onChanged();
                } else {
                    this.rsaTestBuilder_.clear();
                }
                this.bitField0_ &= -5;
                return this;
            }

            public RsaTest.Builder getRsaTestBuilder() {
                this.bitField0_ |= 4;
                this.onChanged();
                return this.getRsaTestFieldBuilder().getBuilder();
            }

            @Override
            public RsaTestOrBuilder getRsaTestOrBuilder() {
                if (this.rsaTestBuilder_ != null) {
                    return this.rsaTestBuilder_.getMessageOrBuilder();
                }
                return this.rsaTest_ == null ? RsaTest.getDefaultInstance() : this.rsaTest_;
            }

            private SingleFieldBuilder<RsaTest, RsaTest.Builder, RsaTestOrBuilder> getRsaTestFieldBuilder() {
                if (this.rsaTestBuilder_ == null) {
                    this.rsaTestBuilder_ = new SingleFieldBuilder(this.getRsaTest(), this.getParentForChildren(), this.isClean());
                    this.rsaTest_ = null;
                }
                return this.rsaTestBuilder_;
            }
        }

    }

    public static interface SignedMessageOrBuilder
    extends MessageOrBuilder {
        public boolean hasData();

        public ByteString getData();

        public boolean hasSignedHash();

        public ByteString getSignedHash();

        public boolean hasRsaTest();

        public RsaTest getRsaTest();

        public RsaTestOrBuilder getRsaTestOrBuilder();
    }

    public static final class PublicKey
    extends GeneratedMessage
    implements PublicKeyOrBuilder {
        private int bitField0_;
        public static final int E_FIELD_NUMBER = 1;
        private ByteString e_;
        public static final int MODULUS_FIELD_NUMBER = 2;
        private ByteString modulus_;
        private byte memoizedIsInitialized;
        private static final long serialVersionUID = 0;
        private static final PublicKey DEFAULT_INSTANCE = new PublicKey();
        @Deprecated
        public static final Parser<PublicKey> PARSER = new AbstractParser<PublicKey>(){

            @Override
            public PublicKey parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                try {
                    return new PublicKey(input, extensionRegistry);
                }
                catch (RuntimeException e) {
                    if (e.getCause() instanceof InvalidProtocolBufferException) {
                        throw (InvalidProtocolBufferException)e.getCause();
                    }
                    throw e;
                }
            }
        };

        private PublicKey(GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
        }

        private PublicKey() {
            this.memoizedIsInitialized = -1;
            this.e_ = ByteString.EMPTY;
            this.modulus_ = ByteString.EMPTY;
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private PublicKey(CodedInputStream input, ExtensionRegistryLite extensionRegistry) {
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
                            this.bitField0_ |= 1;
                            this.e_ = input.readBytes();
                            continue block11;
                        }
                        case 18: 
                    }
                    this.bitField0_ |= 2;
                    this.modulus_ = input.readBytes();
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
            return internal_static_com_cyberpointllc_stac_comms_PublicKey_descriptor;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_com_cyberpointllc_stac_comms_PublicKey_fieldAccessorTable.ensureFieldAccessorsInitialized(PublicKey.class, Builder.class);
        }

        @Override
        public boolean hasE() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override
        public ByteString getE() {
            return this.e_;
        }

        @Override
        public boolean hasModulus() {
            return (this.bitField0_ & 2) == 2;
        }

        @Override
        public ByteString getModulus() {
            return this.modulus_;
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
            if (!this.hasE()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.hasModulus()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public void writeTo(CodedOutputStream output) throws IOException {
            if ((this.bitField0_ & 1) == 1) {
                output.writeBytes(1, this.e_);
            }
            if ((this.bitField0_ & 2) == 2) {
                output.writeBytes(2, this.modulus_);
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
                size += CodedOutputStream.computeBytesSize(1, this.e_);
            }
            if ((this.bitField0_ & 2) == 2) {
                size += CodedOutputStream.computeBytesSize(2, this.modulus_);
            }
            this.memoizedSize = size += this.unknownFields.getSerializedSize();
            return size;
        }

        public static PublicKey parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static PublicKey parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static PublicKey parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static PublicKey parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static PublicKey parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static PublicKey parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static PublicKey parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static PublicKey parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static PublicKey parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static PublicKey parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        @Override
        public Builder newBuilderForType() {
            return PublicKey.newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(PublicKey prototype) {
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

        public static PublicKey getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<PublicKey> parser() {
            return PARSER;
        }

        public Parser<PublicKey> getParserForType() {
            return PARSER;
        }

        @Override
        public PublicKey getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }

        public static final class Builder
        extends GeneratedMessage.Builder<Builder>
        implements PublicKeyOrBuilder {
            private int bitField0_;
            private ByteString e_ = ByteString.EMPTY;
            private ByteString modulus_ = ByteString.EMPTY;

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_com_cyberpointllc_stac_comms_PublicKey_descriptor;
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_com_cyberpointllc_stac_comms_PublicKey_fieldAccessorTable.ensureFieldAccessorsInitialized(PublicKey.class, Builder.class);
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
                this.e_ = ByteString.EMPTY;
                this.bitField0_ &= -2;
                this.modulus_ = ByteString.EMPTY;
                this.bitField0_ &= -3;
                return this;
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_com_cyberpointllc_stac_comms_PublicKey_descriptor;
            }

            @Override
            public PublicKey getDefaultInstanceForType() {
                return PublicKey.getDefaultInstance();
            }

            @Override
            public PublicKey build() {
                PublicKey result = this.buildPartial();
                if (!result.isInitialized()) {
                    throw AbstractMessage.Builder.newUninitializedMessageException(result);
                }
                return result;
            }

            @Override
            public PublicKey buildPartial() {
                PublicKey result = new PublicKey(this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) == 1) {
                    to_bitField0_ |= 1;
                }
                result.e_ = this.e_;
                if ((from_bitField0_ & 2) == 2) {
                    to_bitField0_ |= 2;
                }
                result.modulus_ = this.modulus_;
                result.bitField0_ = to_bitField0_;
                this.onBuilt();
                return result;
            }

            @Override
            public Builder mergeFrom(Message other) {
                if (other instanceof PublicKey) {
                    return this.mergeFrom((PublicKey)other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(PublicKey other) {
                if (other == PublicKey.getDefaultInstance()) {
                    return this;
                }
                if (other.hasE()) {
                    this.setE(other.getE());
                }
                if (other.hasModulus()) {
                    this.setModulus(other.getModulus());
                }
                this.mergeUnknownFields(other.unknownFields);
                this.onChanged();
                return this;
            }

            @Override
            public final boolean isInitialized() {
                if (!this.hasE()) {
                    return false;
                }
                if (!this.hasModulus()) {
                    return false;
                }
                return true;
            }

            @Override
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                PublicKey parsedMessage = null;
                try {
                    parsedMessage = PublicKey.PARSER.parsePartialFrom(input, extensionRegistry);
                }
                catch (InvalidProtocolBufferException e) {
                    parsedMessage = (PublicKey)e.getUnfinishedMessage();
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
            public boolean hasE() {
                return (this.bitField0_ & 1) == 1;
            }

            @Override
            public ByteString getE() {
                return this.e_;
            }

            public Builder setE(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.e_ = value;
                this.onChanged();
                return this;
            }

            public Builder clearE() {
                this.bitField0_ &= -2;
                this.e_ = PublicKey.getDefaultInstance().getE();
                this.onChanged();
                return this;
            }

            @Override
            public boolean hasModulus() {
                return (this.bitField0_ & 2) == 2;
            }

            @Override
            public ByteString getModulus() {
                return this.modulus_;
            }

            public Builder setModulus(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 2;
                this.modulus_ = value;
                this.onChanged();
                return this;
            }

            public Builder clearModulus() {
                this.bitField0_ &= -3;
                this.modulus_ = PublicKey.getDefaultInstance().getModulus();
                this.onChanged();
                return this;
            }
        }

    }

    public static interface PublicKeyOrBuilder
    extends MessageOrBuilder {
        public boolean hasE();

        public ByteString getE();

        public boolean hasModulus();

        public ByteString getModulus();
    }

    public static final class Identity
    extends GeneratedMessage
    implements IdentityOrBuilder {
        private int bitField0_;
        public static final int ID_FIELD_NUMBER = 1;
        private volatile Object id_;
        public static final int PUBLICKEY_FIELD_NUMBER = 2;
        private PublicKey publicKey_;
        public static final int CALLBACKADDRESS_FIELD_NUMBER = 3;
        private NetworkAddress callbackAddress_;
        private byte memoizedIsInitialized;
        private static final long serialVersionUID = 0;
        private static final Identity DEFAULT_INSTANCE = new Identity();
        @Deprecated
        public static final Parser<Identity> PARSER = new AbstractParser<Identity>(){

            @Override
            public Identity parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                try {
                    return new Identity(input, extensionRegistry);
                }
                catch (RuntimeException e) {
                    if (e.getCause() instanceof InvalidProtocolBufferException) {
                        throw (InvalidProtocolBufferException)e.getCause();
                    }
                    throw e;
                }
            }
        };

        private Identity(GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
        }

        private Identity() {
            this.memoizedIsInitialized = -1;
            this.id_ = "";
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private Identity(CodedInputStream input, ExtensionRegistryLite extensionRegistry) {
            this();
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
                            ByteString bs = input.readBytes();
                            this.bitField0_ |= 1;
                            this.id_ = bs;
                            continue block12;
                        }
                        case 18: {
                            PublicKey.Builder subBuilder = null;
                            if ((this.bitField0_ & 2) == 2) {
                                subBuilder = this.publicKey_.toBuilder();
                            }
                            this.publicKey_ = input.readMessage(PublicKey.parser(), extensionRegistry);
                            if (subBuilder != null) {
                                subBuilder.mergeFrom(this.publicKey_);
                                this.publicKey_ = subBuilder.buildPartial();
                            }
                            this.bitField0_ |= 2;
                            continue block12;
                        }
                        case 26: {
                            NetworkAddress.Builder subBuilder = null;
                            if ((this.bitField0_ & 4) == 4) {
                                subBuilder = this.callbackAddress_.toBuilder();
                            }
                            this.callbackAddress_ = input.readMessage(NetworkAddress.parser(), extensionRegistry);
                            if (subBuilder != null) {
                                subBuilder.mergeFrom(this.callbackAddress_);
                                this.callbackAddress_ = subBuilder.buildPartial();
                            }
                            this.bitField0_ |= 4;
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
            return internal_static_com_cyberpointllc_stac_comms_Identity_descriptor;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_com_cyberpointllc_stac_comms_Identity_fieldAccessorTable.ensureFieldAccessorsInitialized(Identity.class, Builder.class);
        }

        @Override
        public boolean hasId() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override
        public String getId() {
            Object ref = this.id_;
            if (ref instanceof String) {
                return (String)ref;
            }
            ByteString bs = (ByteString)ref;
            String s = bs.toStringUtf8();
            if (bs.isValidUtf8()) {
                this.id_ = s;
            }
            return s;
        }

        @Override
        public ByteString getIdBytes() {
            Object ref = this.id_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String)ref);
                this.id_ = b;
                return b;
            }
            return (ByteString)ref;
        }

        @Override
        public boolean hasPublicKey() {
            return (this.bitField0_ & 2) == 2;
        }

        @Override
        public PublicKey getPublicKey() {
            return this.publicKey_ == null ? PublicKey.getDefaultInstance() : this.publicKey_;
        }

        @Override
        public PublicKeyOrBuilder getPublicKeyOrBuilder() {
            return this.publicKey_ == null ? PublicKey.getDefaultInstance() : this.publicKey_;
        }

        @Override
        public boolean hasCallbackAddress() {
            return (this.bitField0_ & 4) == 4;
        }

        @Override
        public NetworkAddress getCallbackAddress() {
            return this.callbackAddress_ == null ? NetworkAddress.getDefaultInstance() : this.callbackAddress_;
        }

        @Override
        public NetworkAddressOrBuilder getCallbackAddressOrBuilder() {
            return this.callbackAddress_ == null ? NetworkAddress.getDefaultInstance() : this.callbackAddress_;
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
            if (!this.hasId()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.hasPublicKey()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.getPublicKey().isInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (this.hasCallbackAddress() && !this.getCallbackAddress().isInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public void writeTo(CodedOutputStream output) throws IOException {
            if ((this.bitField0_ & 1) == 1) {
                GeneratedMessage.writeString(output, 1, this.id_);
            }
            if ((this.bitField0_ & 2) == 2) {
                output.writeMessage(2, this.getPublicKey());
            }
            if ((this.bitField0_ & 4) == 4) {
                output.writeMessage(3, this.getCallbackAddress());
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
                size += GeneratedMessage.computeStringSize(1, this.id_);
            }
            if ((this.bitField0_ & 2) == 2) {
                size += CodedOutputStream.computeMessageSize(2, this.getPublicKey());
            }
            if ((this.bitField0_ & 4) == 4) {
                size += CodedOutputStream.computeMessageSize(3, this.getCallbackAddress());
            }
            this.memoizedSize = size += this.unknownFields.getSerializedSize();
            return size;
        }

        public static Identity parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static Identity parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static Identity parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static Identity parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static Identity parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static Identity parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Identity parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static Identity parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static Identity parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static Identity parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        @Override
        public Builder newBuilderForType() {
            return Identity.newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(Identity prototype) {
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

        public static Identity getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<Identity> parser() {
            return PARSER;
        }

        public Parser<Identity> getParserForType() {
            return PARSER;
        }

        @Override
        public Identity getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }

        public static final class Builder
        extends GeneratedMessage.Builder<Builder>
        implements IdentityOrBuilder {
            private int bitField0_;
            private Object id_ = "";
            private PublicKey publicKey_ = null;
            private SingleFieldBuilder<PublicKey, PublicKey.Builder, PublicKeyOrBuilder> publicKeyBuilder_;
            private NetworkAddress callbackAddress_ = null;
            private SingleFieldBuilder<NetworkAddress, NetworkAddress.Builder, NetworkAddressOrBuilder> callbackAddressBuilder_;

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_com_cyberpointllc_stac_comms_Identity_descriptor;
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_com_cyberpointllc_stac_comms_Identity_fieldAccessorTable.ensureFieldAccessorsInitialized(Identity.class, Builder.class);
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
                    this.getPublicKeyFieldBuilder();
                    this.getCallbackAddressFieldBuilder();
                }
            }

            @Override
            public Builder clear() {
                super.clear();
                this.id_ = "";
                this.bitField0_ &= -2;
                if (this.publicKeyBuilder_ == null) {
                    this.publicKey_ = null;
                } else {
                    this.publicKeyBuilder_.clear();
                }
                this.bitField0_ &= -3;
                if (this.callbackAddressBuilder_ == null) {
                    this.callbackAddress_ = null;
                } else {
                    this.callbackAddressBuilder_.clear();
                }
                this.bitField0_ &= -5;
                return this;
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_com_cyberpointllc_stac_comms_Identity_descriptor;
            }

            @Override
            public Identity getDefaultInstanceForType() {
                return Identity.getDefaultInstance();
            }

            @Override
            public Identity build() {
                Identity result = this.buildPartial();
                if (!result.isInitialized()) {
                    throw AbstractMessage.Builder.newUninitializedMessageException(result);
                }
                return result;
            }

            @Override
            public Identity buildPartial() {
                Identity result = new Identity(this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) == 1) {
                    to_bitField0_ |= 1;
                }
                result.id_ = this.id_;
                if ((from_bitField0_ & 2) == 2) {
                    to_bitField0_ |= 2;
                }
                if (this.publicKeyBuilder_ == null) {
                    result.publicKey_ = this.publicKey_;
                } else {
                    result.publicKey_ = this.publicKeyBuilder_.build();
                }
                if ((from_bitField0_ & 4) == 4) {
                    to_bitField0_ |= 4;
                }
                if (this.callbackAddressBuilder_ == null) {
                    result.callbackAddress_ = this.callbackAddress_;
                } else {
                    result.callbackAddress_ = this.callbackAddressBuilder_.build();
                }
                result.bitField0_ = to_bitField0_;
                this.onBuilt();
                return result;
            }

            @Override
            public Builder mergeFrom(Message other) {
                if (other instanceof Identity) {
                    return this.mergeFrom((Identity)other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(Identity other) {
                if (other == Identity.getDefaultInstance()) {
                    return this;
                }
                if (other.hasId()) {
                    this.bitField0_ |= 1;
                    this.id_ = other.id_;
                    this.onChanged();
                }
                if (other.hasPublicKey()) {
                    this.mergePublicKey(other.getPublicKey());
                }
                if (other.hasCallbackAddress()) {
                    this.mergeCallbackAddress(other.getCallbackAddress());
                }
                this.mergeUnknownFields(other.unknownFields);
                this.onChanged();
                return this;
            }

            @Override
            public final boolean isInitialized() {
                if (!this.hasId()) {
                    return false;
                }
                if (!this.hasPublicKey()) {
                    return false;
                }
                if (!this.getPublicKey().isInitialized()) {
                    return false;
                }
                if (this.hasCallbackAddress() && !this.getCallbackAddress().isInitialized()) {
                    return false;
                }
                return true;
            }

            @Override
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                Identity parsedMessage = null;
                try {
                    parsedMessage = Identity.PARSER.parsePartialFrom(input, extensionRegistry);
                }
                catch (InvalidProtocolBufferException e) {
                    parsedMessage = (Identity)e.getUnfinishedMessage();
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
            public boolean hasId() {
                return (this.bitField0_ & 1) == 1;
            }

            @Override
            public String getId() {
                Object ref = this.id_;
                if (!(ref instanceof String)) {
                    ByteString bs = (ByteString)ref;
                    String s = bs.toStringUtf8();
                    if (bs.isValidUtf8()) {
                        this.id_ = s;
                    }
                    return s;
                }
                return (String)ref;
            }

            @Override
            public ByteString getIdBytes() {
                Object ref = this.id_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String)ref);
                    this.id_ = b;
                    return b;
                }
                return (ByteString)ref;
            }

            public Builder setId(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.id_ = value;
                this.onChanged();
                return this;
            }

            public Builder clearId() {
                this.bitField0_ &= -2;
                this.id_ = Identity.getDefaultInstance().getId();
                this.onChanged();
                return this;
            }

            public Builder setIdBytes(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.id_ = value;
                this.onChanged();
                return this;
            }

            @Override
            public boolean hasPublicKey() {
                return (this.bitField0_ & 2) == 2;
            }

            @Override
            public PublicKey getPublicKey() {
                if (this.publicKeyBuilder_ == null) {
                    return this.publicKey_ == null ? PublicKey.getDefaultInstance() : this.publicKey_;
                }
                return this.publicKeyBuilder_.getMessage();
            }

            public Builder setPublicKey(PublicKey value) {
                if (this.publicKeyBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.publicKey_ = value;
                    this.onChanged();
                } else {
                    this.publicKeyBuilder_.setMessage(value);
                }
                this.bitField0_ |= 2;
                return this;
            }

            public Builder setPublicKey(PublicKey.Builder builderForValue) {
                if (this.publicKeyBuilder_ == null) {
                    this.publicKey_ = builderForValue.build();
                    this.onChanged();
                } else {
                    this.publicKeyBuilder_.setMessage(builderForValue.build());
                }
                this.bitField0_ |= 2;
                return this;
            }

            public Builder mergePublicKey(PublicKey value) {
                if (this.publicKeyBuilder_ == null) {
                    this.publicKey_ = (this.bitField0_ & 2) == 2 && this.publicKey_ != null && this.publicKey_ != PublicKey.getDefaultInstance() ? PublicKey.newBuilder(this.publicKey_).mergeFrom(value).buildPartial() : value;
                    this.onChanged();
                } else {
                    this.publicKeyBuilder_.mergeFrom(value);
                }
                this.bitField0_ |= 2;
                return this;
            }

            public Builder clearPublicKey() {
                if (this.publicKeyBuilder_ == null) {
                    this.publicKey_ = null;
                    this.onChanged();
                } else {
                    this.publicKeyBuilder_.clear();
                }
                this.bitField0_ &= -3;
                return this;
            }

            public PublicKey.Builder getPublicKeyBuilder() {
                this.bitField0_ |= 2;
                this.onChanged();
                return this.getPublicKeyFieldBuilder().getBuilder();
            }

            @Override
            public PublicKeyOrBuilder getPublicKeyOrBuilder() {
                if (this.publicKeyBuilder_ != null) {
                    return this.publicKeyBuilder_.getMessageOrBuilder();
                }
                return this.publicKey_ == null ? PublicKey.getDefaultInstance() : this.publicKey_;
            }

            private SingleFieldBuilder<PublicKey, PublicKey.Builder, PublicKeyOrBuilder> getPublicKeyFieldBuilder() {
                if (this.publicKeyBuilder_ == null) {
                    this.publicKeyBuilder_ = new SingleFieldBuilder(this.getPublicKey(), this.getParentForChildren(), this.isClean());
                    this.publicKey_ = null;
                }
                return this.publicKeyBuilder_;
            }

            @Override
            public boolean hasCallbackAddress() {
                return (this.bitField0_ & 4) == 4;
            }

            @Override
            public NetworkAddress getCallbackAddress() {
                if (this.callbackAddressBuilder_ == null) {
                    return this.callbackAddress_ == null ? NetworkAddress.getDefaultInstance() : this.callbackAddress_;
                }
                return this.callbackAddressBuilder_.getMessage();
            }

            public Builder setCallbackAddress(NetworkAddress value) {
                if (this.callbackAddressBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.callbackAddress_ = value;
                    this.onChanged();
                } else {
                    this.callbackAddressBuilder_.setMessage(value);
                }
                this.bitField0_ |= 4;
                return this;
            }

            public Builder setCallbackAddress(NetworkAddress.Builder builderForValue) {
                if (this.callbackAddressBuilder_ == null) {
                    this.callbackAddress_ = builderForValue.build();
                    this.onChanged();
                } else {
                    this.callbackAddressBuilder_.setMessage(builderForValue.build());
                }
                this.bitField0_ |= 4;
                return this;
            }

            public Builder mergeCallbackAddress(NetworkAddress value) {
                if (this.callbackAddressBuilder_ == null) {
                    this.callbackAddress_ = (this.bitField0_ & 4) == 4 && this.callbackAddress_ != null && this.callbackAddress_ != NetworkAddress.getDefaultInstance() ? NetworkAddress.newBuilder(this.callbackAddress_).mergeFrom(value).buildPartial() : value;
                    this.onChanged();
                } else {
                    this.callbackAddressBuilder_.mergeFrom(value);
                }
                this.bitField0_ |= 4;
                return this;
            }

            public Builder clearCallbackAddress() {
                if (this.callbackAddressBuilder_ == null) {
                    this.callbackAddress_ = null;
                    this.onChanged();
                } else {
                    this.callbackAddressBuilder_.clear();
                }
                this.bitField0_ &= -5;
                return this;
            }

            public NetworkAddress.Builder getCallbackAddressBuilder() {
                this.bitField0_ |= 4;
                this.onChanged();
                return this.getCallbackAddressFieldBuilder().getBuilder();
            }

            @Override
            public NetworkAddressOrBuilder getCallbackAddressOrBuilder() {
                if (this.callbackAddressBuilder_ != null) {
                    return this.callbackAddressBuilder_.getMessageOrBuilder();
                }
                return this.callbackAddress_ == null ? NetworkAddress.getDefaultInstance() : this.callbackAddress_;
            }

            private SingleFieldBuilder<NetworkAddress, NetworkAddress.Builder, NetworkAddressOrBuilder> getCallbackAddressFieldBuilder() {
                if (this.callbackAddressBuilder_ == null) {
                    this.callbackAddressBuilder_ = new SingleFieldBuilder(this.getCallbackAddress(), this.getParentForChildren(), this.isClean());
                    this.callbackAddress_ = null;
                }
                return this.callbackAddressBuilder_;
            }
        }

    }

    public static interface IdentityOrBuilder
    extends MessageOrBuilder {
        public boolean hasId();

        public String getId();

        public ByteString getIdBytes();

        public boolean hasPublicKey();

        public PublicKey getPublicKey();

        public PublicKeyOrBuilder getPublicKeyOrBuilder();

        public boolean hasCallbackAddress();

        public NetworkAddress getCallbackAddress();

        public NetworkAddressOrBuilder getCallbackAddressOrBuilder();
    }

    public static final class NetworkAddress
    extends GeneratedMessage
    implements NetworkAddressOrBuilder {
        private int bitField0_;
        public static final int HOST_FIELD_NUMBER = 1;
        private volatile Object host_;
        public static final int PORT_FIELD_NUMBER = 2;
        private int port_;
        private byte memoizedIsInitialized;
        private static final long serialVersionUID = 0;
        private static final NetworkAddress DEFAULT_INSTANCE = new NetworkAddress();
        @Deprecated
        public static final Parser<NetworkAddress> PARSER = new AbstractParser<NetworkAddress>(){

            @Override
            public NetworkAddress parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                try {
                    return new NetworkAddress(input, extensionRegistry);
                }
                catch (RuntimeException e) {
                    if (e.getCause() instanceof InvalidProtocolBufferException) {
                        throw (InvalidProtocolBufferException)e.getCause();
                    }
                    throw e;
                }
            }
        };

        private NetworkAddress(GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
        }

        private NetworkAddress() {
            this.memoizedIsInitialized = -1;
            this.host_ = "";
            this.port_ = 0;
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private NetworkAddress(CodedInputStream input, ExtensionRegistryLite extensionRegistry) {
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
                            this.host_ = bs;
                            continue block11;
                        }
                        case 16: 
                    }
                    this.bitField0_ |= 2;
                    this.port_ = input.readInt32();
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
            return internal_static_com_cyberpointllc_stac_comms_NetworkAddress_descriptor;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_com_cyberpointllc_stac_comms_NetworkAddress_fieldAccessorTable.ensureFieldAccessorsInitialized(NetworkAddress.class, Builder.class);
        }

        @Override
        public boolean hasHost() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override
        public String getHost() {
            Object ref = this.host_;
            if (ref instanceof String) {
                return (String)ref;
            }
            ByteString bs = (ByteString)ref;
            String s = bs.toStringUtf8();
            if (bs.isValidUtf8()) {
                this.host_ = s;
            }
            return s;
        }

        @Override
        public ByteString getHostBytes() {
            Object ref = this.host_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String)ref);
                this.host_ = b;
                return b;
            }
            return (ByteString)ref;
        }

        @Override
        public boolean hasPort() {
            return (this.bitField0_ & 2) == 2;
        }

        @Override
        public int getPort() {
            return this.port_;
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
            if (!this.hasHost()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.hasPort()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public void writeTo(CodedOutputStream output) throws IOException {
            if ((this.bitField0_ & 1) == 1) {
                GeneratedMessage.writeString(output, 1, this.host_);
            }
            if ((this.bitField0_ & 2) == 2) {
                output.writeInt32(2, this.port_);
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
                size += GeneratedMessage.computeStringSize(1, this.host_);
            }
            if ((this.bitField0_ & 2) == 2) {
                size += CodedOutputStream.computeInt32Size(2, this.port_);
            }
            this.memoizedSize = size += this.unknownFields.getSerializedSize();
            return size;
        }

        public static NetworkAddress parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static NetworkAddress parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static NetworkAddress parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static NetworkAddress parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static NetworkAddress parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static NetworkAddress parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static NetworkAddress parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static NetworkAddress parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static NetworkAddress parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static NetworkAddress parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        @Override
        public Builder newBuilderForType() {
            return NetworkAddress.newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(NetworkAddress prototype) {
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

        public static NetworkAddress getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<NetworkAddress> parser() {
            return PARSER;
        }

        public Parser<NetworkAddress> getParserForType() {
            return PARSER;
        }

        @Override
        public NetworkAddress getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }

        public static final class Builder
        extends GeneratedMessage.Builder<Builder>
        implements NetworkAddressOrBuilder {
            private int bitField0_;
            private Object host_ = "";
            private int port_;

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_com_cyberpointllc_stac_comms_NetworkAddress_descriptor;
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_com_cyberpointllc_stac_comms_NetworkAddress_fieldAccessorTable.ensureFieldAccessorsInitialized(NetworkAddress.class, Builder.class);
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
                this.host_ = "";
                this.bitField0_ &= -2;
                this.port_ = 0;
                this.bitField0_ &= -3;
                return this;
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_com_cyberpointllc_stac_comms_NetworkAddress_descriptor;
            }

            @Override
            public NetworkAddress getDefaultInstanceForType() {
                return NetworkAddress.getDefaultInstance();
            }

            @Override
            public NetworkAddress build() {
                NetworkAddress result = this.buildPartial();
                if (!result.isInitialized()) {
                    throw AbstractMessage.Builder.newUninitializedMessageException(result);
                }
                return result;
            }

            @Override
            public NetworkAddress buildPartial() {
                NetworkAddress result = new NetworkAddress(this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) == 1) {
                    to_bitField0_ |= 1;
                }
                result.host_ = this.host_;
                if ((from_bitField0_ & 2) == 2) {
                    to_bitField0_ |= 2;
                }
                result.port_ = this.port_;
                result.bitField0_ = to_bitField0_;
                this.onBuilt();
                return result;
            }

            @Override
            public Builder mergeFrom(Message other) {
                if (other instanceof NetworkAddress) {
                    return this.mergeFrom((NetworkAddress)other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(NetworkAddress other) {
                if (other == NetworkAddress.getDefaultInstance()) {
                    return this;
                }
                if (other.hasHost()) {
                    this.bitField0_ |= 1;
                    this.host_ = other.host_;
                    this.onChanged();
                }
                if (other.hasPort()) {
                    this.setPort(other.getPort());
                }
                this.mergeUnknownFields(other.unknownFields);
                this.onChanged();
                return this;
            }

            @Override
            public final boolean isInitialized() {
                if (!this.hasHost()) {
                    return false;
                }
                if (!this.hasPort()) {
                    return false;
                }
                return true;
            }

            @Override
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                NetworkAddress parsedMessage = null;
                try {
                    parsedMessage = NetworkAddress.PARSER.parsePartialFrom(input, extensionRegistry);
                }
                catch (InvalidProtocolBufferException e) {
                    parsedMessage = (NetworkAddress)e.getUnfinishedMessage();
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
            public boolean hasHost() {
                return (this.bitField0_ & 1) == 1;
            }

            @Override
            public String getHost() {
                Object ref = this.host_;
                if (!(ref instanceof String)) {
                    ByteString bs = (ByteString)ref;
                    String s = bs.toStringUtf8();
                    if (bs.isValidUtf8()) {
                        this.host_ = s;
                    }
                    return s;
                }
                return (String)ref;
            }

            @Override
            public ByteString getHostBytes() {
                Object ref = this.host_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String)ref);
                    this.host_ = b;
                    return b;
                }
                return (ByteString)ref;
            }

            public Builder setHost(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.host_ = value;
                this.onChanged();
                return this;
            }

            public Builder clearHost() {
                this.bitField0_ &= -2;
                this.host_ = NetworkAddress.getDefaultInstance().getHost();
                this.onChanged();
                return this;
            }

            public Builder setHostBytes(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.host_ = value;
                this.onChanged();
                return this;
            }

            @Override
            public boolean hasPort() {
                return (this.bitField0_ & 2) == 2;
            }

            @Override
            public int getPort() {
                return this.port_;
            }

            public Builder setPort(int value) {
                this.bitField0_ |= 2;
                this.port_ = value;
                this.onChanged();
                return this;
            }

            public Builder clearPort() {
                this.bitField0_ &= -3;
                this.port_ = 0;
                this.onChanged();
                return this;
            }
        }

    }

    public static interface NetworkAddressOrBuilder
    extends MessageOrBuilder {
        public boolean hasHost();

        public String getHost();

        public ByteString getHostBytes();

        public boolean hasPort();

        public int getPort();
    }

    public static final class ServerResponse
    extends GeneratedMessage
    implements ServerResponseOrBuilder {
        private int bitField0_;
        public static final int RSARESPONSE_FIELD_NUMBER = 1;
        private RsaResponse rsaResponse_;
        public static final int RSARESULTS_FIELD_NUMBER = 2;
        private RsaResults rsaResults_;
        private byte memoizedIsInitialized;
        private static final long serialVersionUID = 0;
        private static final ServerResponse DEFAULT_INSTANCE = new ServerResponse();
        @Deprecated
        public static final Parser<ServerResponse> PARSER = new AbstractParser<ServerResponse>(){

            @Override
            public ServerResponse parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                try {
                    return new ServerResponse(input, extensionRegistry);
                }
                catch (RuntimeException e) {
                    if (e.getCause() instanceof InvalidProtocolBufferException) {
                        throw (InvalidProtocolBufferException)e.getCause();
                    }
                    throw e;
                }
            }
        };

        private ServerResponse(GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
        }

        private ServerResponse() {
            this.memoizedIsInitialized = -1;
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private ServerResponse(CodedInputStream input, ExtensionRegistryLite extensionRegistry) {
            this();
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
                            RsaResponse.Builder subBuilder = null;
                            if ((this.bitField0_ & 1) == 1) {
                                subBuilder = this.rsaResponse_.toBuilder();
                            }
                            this.rsaResponse_ = input.readMessage(RsaResponse.parser(), extensionRegistry);
                            if (subBuilder != null) {
                                subBuilder.mergeFrom(this.rsaResponse_);
                                this.rsaResponse_ = subBuilder.buildPartial();
                            }
                            this.bitField0_ |= 1;
                            continue block11;
                        }
                        case 18: {
                            RsaResults.Builder subBuilder = null;
                            if ((this.bitField0_ & 2) == 2) {
                                subBuilder = this.rsaResults_.toBuilder();
                            }
                            this.rsaResults_ = input.readMessage(RsaResults.parser(), extensionRegistry);
                            if (subBuilder != null) {
                                subBuilder.mergeFrom(this.rsaResults_);
                                this.rsaResults_ = subBuilder.buildPartial();
                            }
                            this.bitField0_ |= 2;
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
            return internal_static_com_cyberpointllc_stac_comms_ServerResponse_descriptor;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_com_cyberpointllc_stac_comms_ServerResponse_fieldAccessorTable.ensureFieldAccessorsInitialized(ServerResponse.class, Builder.class);
        }

        @Override
        public boolean hasRsaResponse() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override
        public RsaResponse getRsaResponse() {
            return this.rsaResponse_ == null ? RsaResponse.getDefaultInstance() : this.rsaResponse_;
        }

        @Override
        public RsaResponseOrBuilder getRsaResponseOrBuilder() {
            return this.rsaResponse_ == null ? RsaResponse.getDefaultInstance() : this.rsaResponse_;
        }

        @Override
        public boolean hasRsaResults() {
            return (this.bitField0_ & 2) == 2;
        }

        @Override
        public RsaResults getRsaResults() {
            return this.rsaResults_ == null ? RsaResults.getDefaultInstance() : this.rsaResults_;
        }

        @Override
        public RsaResultsOrBuilder getRsaResultsOrBuilder() {
            return this.rsaResults_ == null ? RsaResults.getDefaultInstance() : this.rsaResults_;
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
            if (!this.hasRsaResponse()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.hasRsaResults()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.getRsaResponse().isInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.getRsaResults().isInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public void writeTo(CodedOutputStream output) throws IOException {
            if ((this.bitField0_ & 1) == 1) {
                output.writeMessage(1, this.getRsaResponse());
            }
            if ((this.bitField0_ & 2) == 2) {
                output.writeMessage(2, this.getRsaResults());
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
                size += CodedOutputStream.computeMessageSize(1, this.getRsaResponse());
            }
            if ((this.bitField0_ & 2) == 2) {
                size += CodedOutputStream.computeMessageSize(2, this.getRsaResults());
            }
            this.memoizedSize = size += this.unknownFields.getSerializedSize();
            return size;
        }

        public static ServerResponse parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static ServerResponse parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static ServerResponse parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static ServerResponse parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static ServerResponse parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static ServerResponse parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static ServerResponse parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static ServerResponse parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static ServerResponse parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static ServerResponse parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        @Override
        public Builder newBuilderForType() {
            return ServerResponse.newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(ServerResponse prototype) {
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

        public static ServerResponse getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<ServerResponse> parser() {
            return PARSER;
        }

        public Parser<ServerResponse> getParserForType() {
            return PARSER;
        }

        @Override
        public ServerResponse getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }

        public static final class Builder
        extends GeneratedMessage.Builder<Builder>
        implements ServerResponseOrBuilder {
            private int bitField0_;
            private RsaResponse rsaResponse_ = null;
            private SingleFieldBuilder<RsaResponse, RsaResponse.Builder, RsaResponseOrBuilder> rsaResponseBuilder_;
            private RsaResults rsaResults_ = null;
            private SingleFieldBuilder<RsaResults, RsaResults.Builder, RsaResultsOrBuilder> rsaResultsBuilder_;

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_com_cyberpointllc_stac_comms_ServerResponse_descriptor;
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_com_cyberpointllc_stac_comms_ServerResponse_fieldAccessorTable.ensureFieldAccessorsInitialized(ServerResponse.class, Builder.class);
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
                    this.getRsaResponseFieldBuilder();
                    this.getRsaResultsFieldBuilder();
                }
            }

            @Override
            public Builder clear() {
                super.clear();
                if (this.rsaResponseBuilder_ == null) {
                    this.rsaResponse_ = null;
                } else {
                    this.rsaResponseBuilder_.clear();
                }
                this.bitField0_ &= -2;
                if (this.rsaResultsBuilder_ == null) {
                    this.rsaResults_ = null;
                } else {
                    this.rsaResultsBuilder_.clear();
                }
                this.bitField0_ &= -3;
                return this;
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_com_cyberpointllc_stac_comms_ServerResponse_descriptor;
            }

            @Override
            public ServerResponse getDefaultInstanceForType() {
                return ServerResponse.getDefaultInstance();
            }

            @Override
            public ServerResponse build() {
                ServerResponse result = this.buildPartial();
                if (!result.isInitialized()) {
                    throw AbstractMessage.Builder.newUninitializedMessageException(result);
                }
                return result;
            }

            @Override
            public ServerResponse buildPartial() {
                ServerResponse result = new ServerResponse(this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) == 1) {
                    to_bitField0_ |= 1;
                }
                if (this.rsaResponseBuilder_ == null) {
                    result.rsaResponse_ = this.rsaResponse_;
                } else {
                    result.rsaResponse_ = this.rsaResponseBuilder_.build();
                }
                if ((from_bitField0_ & 2) == 2) {
                    to_bitField0_ |= 2;
                }
                if (this.rsaResultsBuilder_ == null) {
                    result.rsaResults_ = this.rsaResults_;
                } else {
                    result.rsaResults_ = this.rsaResultsBuilder_.build();
                }
                result.bitField0_ = to_bitField0_;
                this.onBuilt();
                return result;
            }

            @Override
            public Builder mergeFrom(Message other) {
                if (other instanceof ServerResponse) {
                    return this.mergeFrom((ServerResponse)other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(ServerResponse other) {
                if (other == ServerResponse.getDefaultInstance()) {
                    return this;
                }
                if (other.hasRsaResponse()) {
                    this.mergeRsaResponse(other.getRsaResponse());
                }
                if (other.hasRsaResults()) {
                    this.mergeRsaResults(other.getRsaResults());
                }
                this.mergeUnknownFields(other.unknownFields);
                this.onChanged();
                return this;
            }

            @Override
            public final boolean isInitialized() {
                if (!this.hasRsaResponse()) {
                    return false;
                }
                if (!this.hasRsaResults()) {
                    return false;
                }
                if (!this.getRsaResponse().isInitialized()) {
                    return false;
                }
                if (!this.getRsaResults().isInitialized()) {
                    return false;
                }
                return true;
            }

            @Override
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                ServerResponse parsedMessage = null;
                try {
                    parsedMessage = ServerResponse.PARSER.parsePartialFrom(input, extensionRegistry);
                }
                catch (InvalidProtocolBufferException e) {
                    parsedMessage = (ServerResponse)e.getUnfinishedMessage();
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
            public boolean hasRsaResponse() {
                return (this.bitField0_ & 1) == 1;
            }

            @Override
            public RsaResponse getRsaResponse() {
                if (this.rsaResponseBuilder_ == null) {
                    return this.rsaResponse_ == null ? RsaResponse.getDefaultInstance() : this.rsaResponse_;
                }
                return this.rsaResponseBuilder_.getMessage();
            }

            public Builder setRsaResponse(RsaResponse value) {
                if (this.rsaResponseBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.rsaResponse_ = value;
                    this.onChanged();
                } else {
                    this.rsaResponseBuilder_.setMessage(value);
                }
                this.bitField0_ |= 1;
                return this;
            }

            public Builder setRsaResponse(RsaResponse.Builder builderForValue) {
                if (this.rsaResponseBuilder_ == null) {
                    this.rsaResponse_ = builderForValue.build();
                    this.onChanged();
                } else {
                    this.rsaResponseBuilder_.setMessage(builderForValue.build());
                }
                this.bitField0_ |= 1;
                return this;
            }

            public Builder mergeRsaResponse(RsaResponse value) {
                if (this.rsaResponseBuilder_ == null) {
                    this.rsaResponse_ = (this.bitField0_ & 1) == 1 && this.rsaResponse_ != null && this.rsaResponse_ != RsaResponse.getDefaultInstance() ? RsaResponse.newBuilder(this.rsaResponse_).mergeFrom(value).buildPartial() : value;
                    this.onChanged();
                } else {
                    this.rsaResponseBuilder_.mergeFrom(value);
                }
                this.bitField0_ |= 1;
                return this;
            }

            public Builder clearRsaResponse() {
                if (this.rsaResponseBuilder_ == null) {
                    this.rsaResponse_ = null;
                    this.onChanged();
                } else {
                    this.rsaResponseBuilder_.clear();
                }
                this.bitField0_ &= -2;
                return this;
            }

            public RsaResponse.Builder getRsaResponseBuilder() {
                this.bitField0_ |= 1;
                this.onChanged();
                return this.getRsaResponseFieldBuilder().getBuilder();
            }

            @Override
            public RsaResponseOrBuilder getRsaResponseOrBuilder() {
                if (this.rsaResponseBuilder_ != null) {
                    return this.rsaResponseBuilder_.getMessageOrBuilder();
                }
                return this.rsaResponse_ == null ? RsaResponse.getDefaultInstance() : this.rsaResponse_;
            }

            private SingleFieldBuilder<RsaResponse, RsaResponse.Builder, RsaResponseOrBuilder> getRsaResponseFieldBuilder() {
                if (this.rsaResponseBuilder_ == null) {
                    this.rsaResponseBuilder_ = new SingleFieldBuilder(this.getRsaResponse(), this.getParentForChildren(), this.isClean());
                    this.rsaResponse_ = null;
                }
                return this.rsaResponseBuilder_;
            }

            @Override
            public boolean hasRsaResults() {
                return (this.bitField0_ & 2) == 2;
            }

            @Override
            public RsaResults getRsaResults() {
                if (this.rsaResultsBuilder_ == null) {
                    return this.rsaResults_ == null ? RsaResults.getDefaultInstance() : this.rsaResults_;
                }
                return this.rsaResultsBuilder_.getMessage();
            }

            public Builder setRsaResults(RsaResults value) {
                if (this.rsaResultsBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.rsaResults_ = value;
                    this.onChanged();
                } else {
                    this.rsaResultsBuilder_.setMessage(value);
                }
                this.bitField0_ |= 2;
                return this;
            }

            public Builder setRsaResults(RsaResults.Builder builderForValue) {
                if (this.rsaResultsBuilder_ == null) {
                    this.rsaResults_ = builderForValue.build();
                    this.onChanged();
                } else {
                    this.rsaResultsBuilder_.setMessage(builderForValue.build());
                }
                this.bitField0_ |= 2;
                return this;
            }

            public Builder mergeRsaResults(RsaResults value) {
                if (this.rsaResultsBuilder_ == null) {
                    this.rsaResults_ = (this.bitField0_ & 2) == 2 && this.rsaResults_ != null && this.rsaResults_ != RsaResults.getDefaultInstance() ? RsaResults.newBuilder(this.rsaResults_).mergeFrom(value).buildPartial() : value;
                    this.onChanged();
                } else {
                    this.rsaResultsBuilder_.mergeFrom(value);
                }
                this.bitField0_ |= 2;
                return this;
            }

            public Builder clearRsaResults() {
                if (this.rsaResultsBuilder_ == null) {
                    this.rsaResults_ = null;
                    this.onChanged();
                } else {
                    this.rsaResultsBuilder_.clear();
                }
                this.bitField0_ &= -3;
                return this;
            }

            public RsaResults.Builder getRsaResultsBuilder() {
                this.bitField0_ |= 2;
                this.onChanged();
                return this.getRsaResultsFieldBuilder().getBuilder();
            }

            @Override
            public RsaResultsOrBuilder getRsaResultsOrBuilder() {
                if (this.rsaResultsBuilder_ != null) {
                    return this.rsaResultsBuilder_.getMessageOrBuilder();
                }
                return this.rsaResults_ == null ? RsaResults.getDefaultInstance() : this.rsaResults_;
            }

            private SingleFieldBuilder<RsaResults, RsaResults.Builder, RsaResultsOrBuilder> getRsaResultsFieldBuilder() {
                if (this.rsaResultsBuilder_ == null) {
                    this.rsaResultsBuilder_ = new SingleFieldBuilder(this.getRsaResults(), this.getParentForChildren(), this.isClean());
                    this.rsaResults_ = null;
                }
                return this.rsaResultsBuilder_;
            }
        }

    }

    public static interface ServerResponseOrBuilder
    extends MessageOrBuilder {
        public boolean hasRsaResponse();

        public RsaResponse getRsaResponse();

        public RsaResponseOrBuilder getRsaResponseOrBuilder();

        public boolean hasRsaResults();

        public RsaResults getRsaResults();

        public RsaResultsOrBuilder getRsaResultsOrBuilder();
    }

    public static final class ClientResponse
    extends GeneratedMessage
    implements ClientResponseOrBuilder {
        private int bitField0_;
        public static final int RSARESPONSE_FIELD_NUMBER = 1;
        private RsaResponse rsaResponse_;
        public static final int RSATEST_FIELD_NUMBER = 2;
        private RsaTest rsaTest_;
        private byte memoizedIsInitialized;
        private static final long serialVersionUID = 0;
        private static final ClientResponse DEFAULT_INSTANCE = new ClientResponse();
        @Deprecated
        public static final Parser<ClientResponse> PARSER = new AbstractParser<ClientResponse>(){

            @Override
            public ClientResponse parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                try {
                    return new ClientResponse(input, extensionRegistry);
                }
                catch (RuntimeException e) {
                    if (e.getCause() instanceof InvalidProtocolBufferException) {
                        throw (InvalidProtocolBufferException)e.getCause();
                    }
                    throw e;
                }
            }
        };

        private ClientResponse(GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
        }

        private ClientResponse() {
            this.memoizedIsInitialized = -1;
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private ClientResponse(CodedInputStream input, ExtensionRegistryLite extensionRegistry) {
            this();
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
                            RsaResponse.Builder subBuilder = null;
                            if ((this.bitField0_ & 1) == 1) {
                                subBuilder = this.rsaResponse_.toBuilder();
                            }
                            this.rsaResponse_ = input.readMessage(RsaResponse.parser(), extensionRegistry);
                            if (subBuilder != null) {
                                subBuilder.mergeFrom(this.rsaResponse_);
                                this.rsaResponse_ = subBuilder.buildPartial();
                            }
                            this.bitField0_ |= 1;
                            continue block11;
                        }
                        case 18: {
                            RsaTest.Builder subBuilder = null;
                            if ((this.bitField0_ & 2) == 2) {
                                subBuilder = this.rsaTest_.toBuilder();
                            }
                            this.rsaTest_ = input.readMessage(RsaTest.parser(), extensionRegistry);
                            if (subBuilder != null) {
                                subBuilder.mergeFrom(this.rsaTest_);
                                this.rsaTest_ = subBuilder.buildPartial();
                            }
                            this.bitField0_ |= 2;
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
            return internal_static_com_cyberpointllc_stac_comms_ClientResponse_descriptor;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_com_cyberpointllc_stac_comms_ClientResponse_fieldAccessorTable.ensureFieldAccessorsInitialized(ClientResponse.class, Builder.class);
        }

        @Override
        public boolean hasRsaResponse() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override
        public RsaResponse getRsaResponse() {
            return this.rsaResponse_ == null ? RsaResponse.getDefaultInstance() : this.rsaResponse_;
        }

        @Override
        public RsaResponseOrBuilder getRsaResponseOrBuilder() {
            return this.rsaResponse_ == null ? RsaResponse.getDefaultInstance() : this.rsaResponse_;
        }

        @Override
        public boolean hasRsaTest() {
            return (this.bitField0_ & 2) == 2;
        }

        @Override
        public RsaTest getRsaTest() {
            return this.rsaTest_ == null ? RsaTest.getDefaultInstance() : this.rsaTest_;
        }

        @Override
        public RsaTestOrBuilder getRsaTestOrBuilder() {
            return this.rsaTest_ == null ? RsaTest.getDefaultInstance() : this.rsaTest_;
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
            if (!this.hasRsaResponse()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.hasRsaTest()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.getRsaResponse().isInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.getRsaTest().isInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public void writeTo(CodedOutputStream output) throws IOException {
            if ((this.bitField0_ & 1) == 1) {
                output.writeMessage(1, this.getRsaResponse());
            }
            if ((this.bitField0_ & 2) == 2) {
                output.writeMessage(2, this.getRsaTest());
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
                size += CodedOutputStream.computeMessageSize(1, this.getRsaResponse());
            }
            if ((this.bitField0_ & 2) == 2) {
                size += CodedOutputStream.computeMessageSize(2, this.getRsaTest());
            }
            this.memoizedSize = size += this.unknownFields.getSerializedSize();
            return size;
        }

        public static ClientResponse parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static ClientResponse parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static ClientResponse parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static ClientResponse parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static ClientResponse parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static ClientResponse parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static ClientResponse parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static ClientResponse parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static ClientResponse parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static ClientResponse parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        @Override
        public Builder newBuilderForType() {
            return ClientResponse.newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(ClientResponse prototype) {
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

        public static ClientResponse getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<ClientResponse> parser() {
            return PARSER;
        }

        public Parser<ClientResponse> getParserForType() {
            return PARSER;
        }

        @Override
        public ClientResponse getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }

        public static final class Builder
        extends GeneratedMessage.Builder<Builder>
        implements ClientResponseOrBuilder {
            private int bitField0_;
            private RsaResponse rsaResponse_ = null;
            private SingleFieldBuilder<RsaResponse, RsaResponse.Builder, RsaResponseOrBuilder> rsaResponseBuilder_;
            private RsaTest rsaTest_ = null;
            private SingleFieldBuilder<RsaTest, RsaTest.Builder, RsaTestOrBuilder> rsaTestBuilder_;

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_com_cyberpointllc_stac_comms_ClientResponse_descriptor;
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_com_cyberpointllc_stac_comms_ClientResponse_fieldAccessorTable.ensureFieldAccessorsInitialized(ClientResponse.class, Builder.class);
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
                    this.getRsaResponseFieldBuilder();
                    this.getRsaTestFieldBuilder();
                }
            }

            @Override
            public Builder clear() {
                super.clear();
                if (this.rsaResponseBuilder_ == null) {
                    this.rsaResponse_ = null;
                } else {
                    this.rsaResponseBuilder_.clear();
                }
                this.bitField0_ &= -2;
                if (this.rsaTestBuilder_ == null) {
                    this.rsaTest_ = null;
                } else {
                    this.rsaTestBuilder_.clear();
                }
                this.bitField0_ &= -3;
                return this;
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_com_cyberpointllc_stac_comms_ClientResponse_descriptor;
            }

            @Override
            public ClientResponse getDefaultInstanceForType() {
                return ClientResponse.getDefaultInstance();
            }

            @Override
            public ClientResponse build() {
                ClientResponse result = this.buildPartial();
                if (!result.isInitialized()) {
                    throw AbstractMessage.Builder.newUninitializedMessageException(result);
                }
                return result;
            }

            @Override
            public ClientResponse buildPartial() {
                ClientResponse result = new ClientResponse(this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) == 1) {
                    to_bitField0_ |= 1;
                }
                if (this.rsaResponseBuilder_ == null) {
                    result.rsaResponse_ = this.rsaResponse_;
                } else {
                    result.rsaResponse_ = this.rsaResponseBuilder_.build();
                }
                if ((from_bitField0_ & 2) == 2) {
                    to_bitField0_ |= 2;
                }
                if (this.rsaTestBuilder_ == null) {
                    result.rsaTest_ = this.rsaTest_;
                } else {
                    result.rsaTest_ = this.rsaTestBuilder_.build();
                }
                result.bitField0_ = to_bitField0_;
                this.onBuilt();
                return result;
            }

            @Override
            public Builder mergeFrom(Message other) {
                if (other instanceof ClientResponse) {
                    return this.mergeFrom((ClientResponse)other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(ClientResponse other) {
                if (other == ClientResponse.getDefaultInstance()) {
                    return this;
                }
                if (other.hasRsaResponse()) {
                    this.mergeRsaResponse(other.getRsaResponse());
                }
                if (other.hasRsaTest()) {
                    this.mergeRsaTest(other.getRsaTest());
                }
                this.mergeUnknownFields(other.unknownFields);
                this.onChanged();
                return this;
            }

            @Override
            public final boolean isInitialized() {
                if (!this.hasRsaResponse()) {
                    return false;
                }
                if (!this.hasRsaTest()) {
                    return false;
                }
                if (!this.getRsaResponse().isInitialized()) {
                    return false;
                }
                if (!this.getRsaTest().isInitialized()) {
                    return false;
                }
                return true;
            }

            @Override
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                ClientResponse parsedMessage = null;
                try {
                    parsedMessage = ClientResponse.PARSER.parsePartialFrom(input, extensionRegistry);
                }
                catch (InvalidProtocolBufferException e) {
                    parsedMessage = (ClientResponse)e.getUnfinishedMessage();
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
            public boolean hasRsaResponse() {
                return (this.bitField0_ & 1) == 1;
            }

            @Override
            public RsaResponse getRsaResponse() {
                if (this.rsaResponseBuilder_ == null) {
                    return this.rsaResponse_ == null ? RsaResponse.getDefaultInstance() : this.rsaResponse_;
                }
                return this.rsaResponseBuilder_.getMessage();
            }

            public Builder setRsaResponse(RsaResponse value) {
                if (this.rsaResponseBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.rsaResponse_ = value;
                    this.onChanged();
                } else {
                    this.rsaResponseBuilder_.setMessage(value);
                }
                this.bitField0_ |= 1;
                return this;
            }

            public Builder setRsaResponse(RsaResponse.Builder builderForValue) {
                if (this.rsaResponseBuilder_ == null) {
                    this.rsaResponse_ = builderForValue.build();
                    this.onChanged();
                } else {
                    this.rsaResponseBuilder_.setMessage(builderForValue.build());
                }
                this.bitField0_ |= 1;
                return this;
            }

            public Builder mergeRsaResponse(RsaResponse value) {
                if (this.rsaResponseBuilder_ == null) {
                    this.rsaResponse_ = (this.bitField0_ & 1) == 1 && this.rsaResponse_ != null && this.rsaResponse_ != RsaResponse.getDefaultInstance() ? RsaResponse.newBuilder(this.rsaResponse_).mergeFrom(value).buildPartial() : value;
                    this.onChanged();
                } else {
                    this.rsaResponseBuilder_.mergeFrom(value);
                }
                this.bitField0_ |= 1;
                return this;
            }

            public Builder clearRsaResponse() {
                if (this.rsaResponseBuilder_ == null) {
                    this.rsaResponse_ = null;
                    this.onChanged();
                } else {
                    this.rsaResponseBuilder_.clear();
                }
                this.bitField0_ &= -2;
                return this;
            }

            public RsaResponse.Builder getRsaResponseBuilder() {
                this.bitField0_ |= 1;
                this.onChanged();
                return this.getRsaResponseFieldBuilder().getBuilder();
            }

            @Override
            public RsaResponseOrBuilder getRsaResponseOrBuilder() {
                if (this.rsaResponseBuilder_ != null) {
                    return this.rsaResponseBuilder_.getMessageOrBuilder();
                }
                return this.rsaResponse_ == null ? RsaResponse.getDefaultInstance() : this.rsaResponse_;
            }

            private SingleFieldBuilder<RsaResponse, RsaResponse.Builder, RsaResponseOrBuilder> getRsaResponseFieldBuilder() {
                if (this.rsaResponseBuilder_ == null) {
                    this.rsaResponseBuilder_ = new SingleFieldBuilder(this.getRsaResponse(), this.getParentForChildren(), this.isClean());
                    this.rsaResponse_ = null;
                }
                return this.rsaResponseBuilder_;
            }

            @Override
            public boolean hasRsaTest() {
                return (this.bitField0_ & 2) == 2;
            }

            @Override
            public RsaTest getRsaTest() {
                if (this.rsaTestBuilder_ == null) {
                    return this.rsaTest_ == null ? RsaTest.getDefaultInstance() : this.rsaTest_;
                }
                return this.rsaTestBuilder_.getMessage();
            }

            public Builder setRsaTest(RsaTest value) {
                if (this.rsaTestBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.rsaTest_ = value;
                    this.onChanged();
                } else {
                    this.rsaTestBuilder_.setMessage(value);
                }
                this.bitField0_ |= 2;
                return this;
            }

            public Builder setRsaTest(RsaTest.Builder builderForValue) {
                if (this.rsaTestBuilder_ == null) {
                    this.rsaTest_ = builderForValue.build();
                    this.onChanged();
                } else {
                    this.rsaTestBuilder_.setMessage(builderForValue.build());
                }
                this.bitField0_ |= 2;
                return this;
            }

            public Builder mergeRsaTest(RsaTest value) {
                if (this.rsaTestBuilder_ == null) {
                    this.rsaTest_ = (this.bitField0_ & 2) == 2 && this.rsaTest_ != null && this.rsaTest_ != RsaTest.getDefaultInstance() ? RsaTest.newBuilder(this.rsaTest_).mergeFrom(value).buildPartial() : value;
                    this.onChanged();
                } else {
                    this.rsaTestBuilder_.mergeFrom(value);
                }
                this.bitField0_ |= 2;
                return this;
            }

            public Builder clearRsaTest() {
                if (this.rsaTestBuilder_ == null) {
                    this.rsaTest_ = null;
                    this.onChanged();
                } else {
                    this.rsaTestBuilder_.clear();
                }
                this.bitField0_ &= -3;
                return this;
            }

            public RsaTest.Builder getRsaTestBuilder() {
                this.bitField0_ |= 2;
                this.onChanged();
                return this.getRsaTestFieldBuilder().getBuilder();
            }

            @Override
            public RsaTestOrBuilder getRsaTestOrBuilder() {
                if (this.rsaTestBuilder_ != null) {
                    return this.rsaTestBuilder_.getMessageOrBuilder();
                }
                return this.rsaTest_ == null ? RsaTest.getDefaultInstance() : this.rsaTest_;
            }

            private SingleFieldBuilder<RsaTest, RsaTest.Builder, RsaTestOrBuilder> getRsaTestFieldBuilder() {
                if (this.rsaTestBuilder_ == null) {
                    this.rsaTestBuilder_ = new SingleFieldBuilder(this.getRsaTest(), this.getParentForChildren(), this.isClean());
                    this.rsaTest_ = null;
                }
                return this.rsaTestBuilder_;
            }
        }

    }

    public static interface ClientResponseOrBuilder
    extends MessageOrBuilder {
        public boolean hasRsaResponse();

        public RsaResponse getRsaResponse();

        public RsaResponseOrBuilder getRsaResponseOrBuilder();

        public boolean hasRsaTest();

        public RsaTest getRsaTest();

        public RsaTestOrBuilder getRsaTestOrBuilder();
    }

    public static final class ServerSetup
    extends GeneratedMessage
    implements ServerSetupOrBuilder {
        private int bitField0_;
        public static final int IDENTITY_FIELD_NUMBER = 1;
        private Identity identity_;
        public static final int KEY_FIELD_NUMBER = 2;
        private DHPublicKey key_;
        public static final int RSATEST_FIELD_NUMBER = 3;
        private RsaTest rsaTest_;
        private byte memoizedIsInitialized;
        private static final long serialVersionUID = 0;
        private static final ServerSetup DEFAULT_INSTANCE = new ServerSetup();
        @Deprecated
        public static final Parser<ServerSetup> PARSER = new AbstractParser<ServerSetup>(){

            @Override
            public ServerSetup parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                try {
                    return new ServerSetup(input, extensionRegistry);
                }
                catch (RuntimeException e) {
                    if (e.getCause() instanceof InvalidProtocolBufferException) {
                        throw (InvalidProtocolBufferException)e.getCause();
                    }
                    throw e;
                }
            }
        };

        private ServerSetup(GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
        }

        private ServerSetup() {
            this.memoizedIsInitialized = -1;
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private ServerSetup(CodedInputStream input, ExtensionRegistryLite extensionRegistry) {
            this();
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
                            Identity.Builder subBuilder = null;
                            if ((this.bitField0_ & 1) == 1) {
                                subBuilder = this.identity_.toBuilder();
                            }
                            this.identity_ = input.readMessage(Identity.parser(), extensionRegistry);
                            if (subBuilder != null) {
                                subBuilder.mergeFrom(this.identity_);
                                this.identity_ = subBuilder.buildPartial();
                            }
                            this.bitField0_ |= 1;
                            continue block12;
                        }
                        case 18: {
                            DHPublicKey.Builder subBuilder = null;
                            if ((this.bitField0_ & 2) == 2) {
                                subBuilder = this.key_.toBuilder();
                            }
                            this.key_ = input.readMessage(DHPublicKey.parser(), extensionRegistry);
                            if (subBuilder != null) {
                                subBuilder.mergeFrom(this.key_);
                                this.key_ = subBuilder.buildPartial();
                            }
                            this.bitField0_ |= 2;
                            continue block12;
                        }
                        case 26: {
                            RsaTest.Builder subBuilder = null;
                            if ((this.bitField0_ & 4) == 4) {
                                subBuilder = this.rsaTest_.toBuilder();
                            }
                            this.rsaTest_ = input.readMessage(RsaTest.parser(), extensionRegistry);
                            if (subBuilder != null) {
                                subBuilder.mergeFrom(this.rsaTest_);
                                this.rsaTest_ = subBuilder.buildPartial();
                            }
                            this.bitField0_ |= 4;
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
            return internal_static_com_cyberpointllc_stac_comms_ServerSetup_descriptor;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_com_cyberpointllc_stac_comms_ServerSetup_fieldAccessorTable.ensureFieldAccessorsInitialized(ServerSetup.class, Builder.class);
        }

        @Override
        public boolean hasIdentity() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override
        public Identity getIdentity() {
            return this.identity_ == null ? Identity.getDefaultInstance() : this.identity_;
        }

        @Override
        public IdentityOrBuilder getIdentityOrBuilder() {
            return this.identity_ == null ? Identity.getDefaultInstance() : this.identity_;
        }

        @Override
        public boolean hasKey() {
            return (this.bitField0_ & 2) == 2;
        }

        @Override
        public DHPublicKey getKey() {
            return this.key_ == null ? DHPublicKey.getDefaultInstance() : this.key_;
        }

        @Override
        public DHPublicKeyOrBuilder getKeyOrBuilder() {
            return this.key_ == null ? DHPublicKey.getDefaultInstance() : this.key_;
        }

        @Override
        public boolean hasRsaTest() {
            return (this.bitField0_ & 4) == 4;
        }

        @Override
        public RsaTest getRsaTest() {
            return this.rsaTest_ == null ? RsaTest.getDefaultInstance() : this.rsaTest_;
        }

        @Override
        public RsaTestOrBuilder getRsaTestOrBuilder() {
            return this.rsaTest_ == null ? RsaTest.getDefaultInstance() : this.rsaTest_;
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
            if (!this.hasIdentity()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.hasKey()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.hasRsaTest()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.getIdentity().isInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.getKey().isInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.getRsaTest().isInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public void writeTo(CodedOutputStream output) throws IOException {
            if ((this.bitField0_ & 1) == 1) {
                output.writeMessage(1, this.getIdentity());
            }
            if ((this.bitField0_ & 2) == 2) {
                output.writeMessage(2, this.getKey());
            }
            if ((this.bitField0_ & 4) == 4) {
                output.writeMessage(3, this.getRsaTest());
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
                size += CodedOutputStream.computeMessageSize(1, this.getIdentity());
            }
            if ((this.bitField0_ & 2) == 2) {
                size += CodedOutputStream.computeMessageSize(2, this.getKey());
            }
            if ((this.bitField0_ & 4) == 4) {
                size += CodedOutputStream.computeMessageSize(3, this.getRsaTest());
            }
            this.memoizedSize = size += this.unknownFields.getSerializedSize();
            return size;
        }

        public static ServerSetup parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static ServerSetup parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static ServerSetup parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static ServerSetup parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static ServerSetup parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static ServerSetup parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static ServerSetup parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static ServerSetup parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static ServerSetup parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static ServerSetup parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        @Override
        public Builder newBuilderForType() {
            return ServerSetup.newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(ServerSetup prototype) {
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

        public static ServerSetup getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<ServerSetup> parser() {
            return PARSER;
        }

        public Parser<ServerSetup> getParserForType() {
            return PARSER;
        }

        @Override
        public ServerSetup getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }

        public static final class Builder
        extends GeneratedMessage.Builder<Builder>
        implements ServerSetupOrBuilder {
            private int bitField0_;
            private Identity identity_ = null;
            private SingleFieldBuilder<Identity, Identity.Builder, IdentityOrBuilder> identityBuilder_;
            private DHPublicKey key_ = null;
            private SingleFieldBuilder<DHPublicKey, DHPublicKey.Builder, DHPublicKeyOrBuilder> keyBuilder_;
            private RsaTest rsaTest_ = null;
            private SingleFieldBuilder<RsaTest, RsaTest.Builder, RsaTestOrBuilder> rsaTestBuilder_;

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_com_cyberpointllc_stac_comms_ServerSetup_descriptor;
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_com_cyberpointllc_stac_comms_ServerSetup_fieldAccessorTable.ensureFieldAccessorsInitialized(ServerSetup.class, Builder.class);
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
                    this.getIdentityFieldBuilder();
                    this.getKeyFieldBuilder();
                    this.getRsaTestFieldBuilder();
                }
            }

            @Override
            public Builder clear() {
                super.clear();
                if (this.identityBuilder_ == null) {
                    this.identity_ = null;
                } else {
                    this.identityBuilder_.clear();
                }
                this.bitField0_ &= -2;
                if (this.keyBuilder_ == null) {
                    this.key_ = null;
                } else {
                    this.keyBuilder_.clear();
                }
                this.bitField0_ &= -3;
                if (this.rsaTestBuilder_ == null) {
                    this.rsaTest_ = null;
                } else {
                    this.rsaTestBuilder_.clear();
                }
                this.bitField0_ &= -5;
                return this;
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_com_cyberpointllc_stac_comms_ServerSetup_descriptor;
            }

            @Override
            public ServerSetup getDefaultInstanceForType() {
                return ServerSetup.getDefaultInstance();
            }

            @Override
            public ServerSetup build() {
                ServerSetup result = this.buildPartial();
                if (!result.isInitialized()) {
                    throw AbstractMessage.Builder.newUninitializedMessageException(result);
                }
                return result;
            }

            @Override
            public ServerSetup buildPartial() {
                ServerSetup result = new ServerSetup(this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) == 1) {
                    to_bitField0_ |= 1;
                }
                if (this.identityBuilder_ == null) {
                    result.identity_ = this.identity_;
                } else {
                    result.identity_ = this.identityBuilder_.build();
                }
                if ((from_bitField0_ & 2) == 2) {
                    to_bitField0_ |= 2;
                }
                if (this.keyBuilder_ == null) {
                    result.key_ = this.key_;
                } else {
                    result.key_ = this.keyBuilder_.build();
                }
                if ((from_bitField0_ & 4) == 4) {
                    to_bitField0_ |= 4;
                }
                if (this.rsaTestBuilder_ == null) {
                    result.rsaTest_ = this.rsaTest_;
                } else {
                    result.rsaTest_ = this.rsaTestBuilder_.build();
                }
                result.bitField0_ = to_bitField0_;
                this.onBuilt();
                return result;
            }

            @Override
            public Builder mergeFrom(Message other) {
                if (other instanceof ServerSetup) {
                    return this.mergeFrom((ServerSetup)other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(ServerSetup other) {
                if (other == ServerSetup.getDefaultInstance()) {
                    return this;
                }
                if (other.hasIdentity()) {
                    this.mergeIdentity(other.getIdentity());
                }
                if (other.hasKey()) {
                    this.mergeKey(other.getKey());
                }
                if (other.hasRsaTest()) {
                    this.mergeRsaTest(other.getRsaTest());
                }
                this.mergeUnknownFields(other.unknownFields);
                this.onChanged();
                return this;
            }

            @Override
            public final boolean isInitialized() {
                if (!this.hasIdentity()) {
                    return false;
                }
                if (!this.hasKey()) {
                    return false;
                }
                if (!this.hasRsaTest()) {
                    return false;
                }
                if (!this.getIdentity().isInitialized()) {
                    return false;
                }
                if (!this.getKey().isInitialized()) {
                    return false;
                }
                if (!this.getRsaTest().isInitialized()) {
                    return false;
                }
                return true;
            }

            @Override
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                ServerSetup parsedMessage = null;
                try {
                    parsedMessage = ServerSetup.PARSER.parsePartialFrom(input, extensionRegistry);
                }
                catch (InvalidProtocolBufferException e) {
                    parsedMessage = (ServerSetup)e.getUnfinishedMessage();
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
            public boolean hasIdentity() {
                return (this.bitField0_ & 1) == 1;
            }

            @Override
            public Identity getIdentity() {
                if (this.identityBuilder_ == null) {
                    return this.identity_ == null ? Identity.getDefaultInstance() : this.identity_;
                }
                return this.identityBuilder_.getMessage();
            }

            public Builder setIdentity(Identity value) {
                if (this.identityBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.identity_ = value;
                    this.onChanged();
                } else {
                    this.identityBuilder_.setMessage(value);
                }
                this.bitField0_ |= 1;
                return this;
            }

            public Builder setIdentity(Identity.Builder builderForValue) {
                if (this.identityBuilder_ == null) {
                    this.identity_ = builderForValue.build();
                    this.onChanged();
                } else {
                    this.identityBuilder_.setMessage(builderForValue.build());
                }
                this.bitField0_ |= 1;
                return this;
            }

            public Builder mergeIdentity(Identity value) {
                if (this.identityBuilder_ == null) {
                    this.identity_ = (this.bitField0_ & 1) == 1 && this.identity_ != null && this.identity_ != Identity.getDefaultInstance() ? Identity.newBuilder(this.identity_).mergeFrom(value).buildPartial() : value;
                    this.onChanged();
                } else {
                    this.identityBuilder_.mergeFrom(value);
                }
                this.bitField0_ |= 1;
                return this;
            }

            public Builder clearIdentity() {
                if (this.identityBuilder_ == null) {
                    this.identity_ = null;
                    this.onChanged();
                } else {
                    this.identityBuilder_.clear();
                }
                this.bitField0_ &= -2;
                return this;
            }

            public Identity.Builder getIdentityBuilder() {
                this.bitField0_ |= 1;
                this.onChanged();
                return this.getIdentityFieldBuilder().getBuilder();
            }

            @Override
            public IdentityOrBuilder getIdentityOrBuilder() {
                if (this.identityBuilder_ != null) {
                    return this.identityBuilder_.getMessageOrBuilder();
                }
                return this.identity_ == null ? Identity.getDefaultInstance() : this.identity_;
            }

            private SingleFieldBuilder<Identity, Identity.Builder, IdentityOrBuilder> getIdentityFieldBuilder() {
                if (this.identityBuilder_ == null) {
                    this.identityBuilder_ = new SingleFieldBuilder(this.getIdentity(), this.getParentForChildren(), this.isClean());
                    this.identity_ = null;
                }
                return this.identityBuilder_;
            }

            @Override
            public boolean hasKey() {
                return (this.bitField0_ & 2) == 2;
            }

            @Override
            public DHPublicKey getKey() {
                if (this.keyBuilder_ == null) {
                    return this.key_ == null ? DHPublicKey.getDefaultInstance() : this.key_;
                }
                return this.keyBuilder_.getMessage();
            }

            public Builder setKey(DHPublicKey value) {
                if (this.keyBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.key_ = value;
                    this.onChanged();
                } else {
                    this.keyBuilder_.setMessage(value);
                }
                this.bitField0_ |= 2;
                return this;
            }

            public Builder setKey(DHPublicKey.Builder builderForValue) {
                if (this.keyBuilder_ == null) {
                    this.key_ = builderForValue.build();
                    this.onChanged();
                } else {
                    this.keyBuilder_.setMessage(builderForValue.build());
                }
                this.bitField0_ |= 2;
                return this;
            }

            public Builder mergeKey(DHPublicKey value) {
                if (this.keyBuilder_ == null) {
                    this.key_ = (this.bitField0_ & 2) == 2 && this.key_ != null && this.key_ != DHPublicKey.getDefaultInstance() ? DHPublicKey.newBuilder(this.key_).mergeFrom(value).buildPartial() : value;
                    this.onChanged();
                } else {
                    this.keyBuilder_.mergeFrom(value);
                }
                this.bitField0_ |= 2;
                return this;
            }

            public Builder clearKey() {
                if (this.keyBuilder_ == null) {
                    this.key_ = null;
                    this.onChanged();
                } else {
                    this.keyBuilder_.clear();
                }
                this.bitField0_ &= -3;
                return this;
            }

            public DHPublicKey.Builder getKeyBuilder() {
                this.bitField0_ |= 2;
                this.onChanged();
                return this.getKeyFieldBuilder().getBuilder();
            }

            @Override
            public DHPublicKeyOrBuilder getKeyOrBuilder() {
                if (this.keyBuilder_ != null) {
                    return this.keyBuilder_.getMessageOrBuilder();
                }
                return this.key_ == null ? DHPublicKey.getDefaultInstance() : this.key_;
            }

            private SingleFieldBuilder<DHPublicKey, DHPublicKey.Builder, DHPublicKeyOrBuilder> getKeyFieldBuilder() {
                if (this.keyBuilder_ == null) {
                    this.keyBuilder_ = new SingleFieldBuilder(this.getKey(), this.getParentForChildren(), this.isClean());
                    this.key_ = null;
                }
                return this.keyBuilder_;
            }

            @Override
            public boolean hasRsaTest() {
                return (this.bitField0_ & 4) == 4;
            }

            @Override
            public RsaTest getRsaTest() {
                if (this.rsaTestBuilder_ == null) {
                    return this.rsaTest_ == null ? RsaTest.getDefaultInstance() : this.rsaTest_;
                }
                return this.rsaTestBuilder_.getMessage();
            }

            public Builder setRsaTest(RsaTest value) {
                if (this.rsaTestBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.rsaTest_ = value;
                    this.onChanged();
                } else {
                    this.rsaTestBuilder_.setMessage(value);
                }
                this.bitField0_ |= 4;
                return this;
            }

            public Builder setRsaTest(RsaTest.Builder builderForValue) {
                if (this.rsaTestBuilder_ == null) {
                    this.rsaTest_ = builderForValue.build();
                    this.onChanged();
                } else {
                    this.rsaTestBuilder_.setMessage(builderForValue.build());
                }
                this.bitField0_ |= 4;
                return this;
            }

            public Builder mergeRsaTest(RsaTest value) {
                if (this.rsaTestBuilder_ == null) {
                    this.rsaTest_ = (this.bitField0_ & 4) == 4 && this.rsaTest_ != null && this.rsaTest_ != RsaTest.getDefaultInstance() ? RsaTest.newBuilder(this.rsaTest_).mergeFrom(value).buildPartial() : value;
                    this.onChanged();
                } else {
                    this.rsaTestBuilder_.mergeFrom(value);
                }
                this.bitField0_ |= 4;
                return this;
            }

            public Builder clearRsaTest() {
                if (this.rsaTestBuilder_ == null) {
                    this.rsaTest_ = null;
                    this.onChanged();
                } else {
                    this.rsaTestBuilder_.clear();
                }
                this.bitField0_ &= -5;
                return this;
            }

            public RsaTest.Builder getRsaTestBuilder() {
                this.bitField0_ |= 4;
                this.onChanged();
                return this.getRsaTestFieldBuilder().getBuilder();
            }

            @Override
            public RsaTestOrBuilder getRsaTestOrBuilder() {
                if (this.rsaTestBuilder_ != null) {
                    return this.rsaTestBuilder_.getMessageOrBuilder();
                }
                return this.rsaTest_ == null ? RsaTest.getDefaultInstance() : this.rsaTest_;
            }

            private SingleFieldBuilder<RsaTest, RsaTest.Builder, RsaTestOrBuilder> getRsaTestFieldBuilder() {
                if (this.rsaTestBuilder_ == null) {
                    this.rsaTestBuilder_ = new SingleFieldBuilder(this.getRsaTest(), this.getParentForChildren(), this.isClean());
                    this.rsaTest_ = null;
                }
                return this.rsaTestBuilder_;
            }
        }

    }

    public static interface ServerSetupOrBuilder
    extends MessageOrBuilder {
        public boolean hasIdentity();

        public Identity getIdentity();

        public IdentityOrBuilder getIdentityOrBuilder();

        public boolean hasKey();

        public DHPublicKey getKey();

        public DHPublicKeyOrBuilder getKeyOrBuilder();

        public boolean hasRsaTest();

        public RsaTest getRsaTest();

        public RsaTestOrBuilder getRsaTestOrBuilder();
    }

    public static final class ClientSetup
    extends GeneratedMessage
    implements ClientSetupOrBuilder {
        private int bitField0_;
        public static final int IDENTITY_FIELD_NUMBER = 1;
        private Identity identity_;
        public static final int KEY_FIELD_NUMBER = 2;
        private DHPublicKey key_;
        private byte memoizedIsInitialized;
        private static final long serialVersionUID = 0;
        private static final ClientSetup DEFAULT_INSTANCE = new ClientSetup();
        @Deprecated
        public static final Parser<ClientSetup> PARSER = new AbstractParser<ClientSetup>(){

            @Override
            public ClientSetup parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                try {
                    return new ClientSetup(input, extensionRegistry);
                }
                catch (RuntimeException e) {
                    if (e.getCause() instanceof InvalidProtocolBufferException) {
                        throw (InvalidProtocolBufferException)e.getCause();
                    }
                    throw e;
                }
            }
        };

        private ClientSetup(GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
        }

        private ClientSetup() {
            this.memoizedIsInitialized = -1;
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private ClientSetup(CodedInputStream input, ExtensionRegistryLite extensionRegistry) {
            this();
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
                            Identity.Builder subBuilder = null;
                            if ((this.bitField0_ & 1) == 1) {
                                subBuilder = this.identity_.toBuilder();
                            }
                            this.identity_ = input.readMessage(Identity.parser(), extensionRegistry);
                            if (subBuilder != null) {
                                subBuilder.mergeFrom(this.identity_);
                                this.identity_ = subBuilder.buildPartial();
                            }
                            this.bitField0_ |= 1;
                            continue block11;
                        }
                        case 18: {
                            DHPublicKey.Builder subBuilder = null;
                            if ((this.bitField0_ & 2) == 2) {
                                subBuilder = this.key_.toBuilder();
                            }
                            this.key_ = input.readMessage(DHPublicKey.parser(), extensionRegistry);
                            if (subBuilder != null) {
                                subBuilder.mergeFrom(this.key_);
                                this.key_ = subBuilder.buildPartial();
                            }
                            this.bitField0_ |= 2;
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
            return internal_static_com_cyberpointllc_stac_comms_ClientSetup_descriptor;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_com_cyberpointllc_stac_comms_ClientSetup_fieldAccessorTable.ensureFieldAccessorsInitialized(ClientSetup.class, Builder.class);
        }

        @Override
        public boolean hasIdentity() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override
        public Identity getIdentity() {
            return this.identity_ == null ? Identity.getDefaultInstance() : this.identity_;
        }

        @Override
        public IdentityOrBuilder getIdentityOrBuilder() {
            return this.identity_ == null ? Identity.getDefaultInstance() : this.identity_;
        }

        @Override
        public boolean hasKey() {
            return (this.bitField0_ & 2) == 2;
        }

        @Override
        public DHPublicKey getKey() {
            return this.key_ == null ? DHPublicKey.getDefaultInstance() : this.key_;
        }

        @Override
        public DHPublicKeyOrBuilder getKeyOrBuilder() {
            return this.key_ == null ? DHPublicKey.getDefaultInstance() : this.key_;
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
            if (!this.hasIdentity()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.hasKey()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.getIdentity().isInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.getKey().isInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public void writeTo(CodedOutputStream output) throws IOException {
            if ((this.bitField0_ & 1) == 1) {
                output.writeMessage(1, this.getIdentity());
            }
            if ((this.bitField0_ & 2) == 2) {
                output.writeMessage(2, this.getKey());
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
                size += CodedOutputStream.computeMessageSize(1, this.getIdentity());
            }
            if ((this.bitField0_ & 2) == 2) {
                size += CodedOutputStream.computeMessageSize(2, this.getKey());
            }
            this.memoizedSize = size += this.unknownFields.getSerializedSize();
            return size;
        }

        public static ClientSetup parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static ClientSetup parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static ClientSetup parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static ClientSetup parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static ClientSetup parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static ClientSetup parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static ClientSetup parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static ClientSetup parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static ClientSetup parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static ClientSetup parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        @Override
        public Builder newBuilderForType() {
            return ClientSetup.newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(ClientSetup prototype) {
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

        public static ClientSetup getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<ClientSetup> parser() {
            return PARSER;
        }

        public Parser<ClientSetup> getParserForType() {
            return PARSER;
        }

        @Override
        public ClientSetup getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }

        public static final class Builder
        extends GeneratedMessage.Builder<Builder>
        implements ClientSetupOrBuilder {
            private int bitField0_;
            private Identity identity_ = null;
            private SingleFieldBuilder<Identity, Identity.Builder, IdentityOrBuilder> identityBuilder_;
            private DHPublicKey key_ = null;
            private SingleFieldBuilder<DHPublicKey, DHPublicKey.Builder, DHPublicKeyOrBuilder> keyBuilder_;

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_com_cyberpointllc_stac_comms_ClientSetup_descriptor;
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_com_cyberpointllc_stac_comms_ClientSetup_fieldAccessorTable.ensureFieldAccessorsInitialized(ClientSetup.class, Builder.class);
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
                    this.getIdentityFieldBuilder();
                    this.getKeyFieldBuilder();
                }
            }

            @Override
            public Builder clear() {
                super.clear();
                if (this.identityBuilder_ == null) {
                    this.identity_ = null;
                } else {
                    this.identityBuilder_.clear();
                }
                this.bitField0_ &= -2;
                if (this.keyBuilder_ == null) {
                    this.key_ = null;
                } else {
                    this.keyBuilder_.clear();
                }
                this.bitField0_ &= -3;
                return this;
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_com_cyberpointllc_stac_comms_ClientSetup_descriptor;
            }

            @Override
            public ClientSetup getDefaultInstanceForType() {
                return ClientSetup.getDefaultInstance();
            }

            @Override
            public ClientSetup build() {
                ClientSetup result = this.buildPartial();
                if (!result.isInitialized()) {
                    throw AbstractMessage.Builder.newUninitializedMessageException(result);
                }
                return result;
            }

            @Override
            public ClientSetup buildPartial() {
                ClientSetup result = new ClientSetup(this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) == 1) {
                    to_bitField0_ |= 1;
                }
                if (this.identityBuilder_ == null) {
                    result.identity_ = this.identity_;
                } else {
                    result.identity_ = this.identityBuilder_.build();
                }
                if ((from_bitField0_ & 2) == 2) {
                    to_bitField0_ |= 2;
                }
                if (this.keyBuilder_ == null) {
                    result.key_ = this.key_;
                } else {
                    result.key_ = this.keyBuilder_.build();
                }
                result.bitField0_ = to_bitField0_;
                this.onBuilt();
                return result;
            }

            @Override
            public Builder mergeFrom(Message other) {
                if (other instanceof ClientSetup) {
                    return this.mergeFrom((ClientSetup)other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(ClientSetup other) {
                if (other == ClientSetup.getDefaultInstance()) {
                    return this;
                }
                if (other.hasIdentity()) {
                    this.mergeIdentity(other.getIdentity());
                }
                if (other.hasKey()) {
                    this.mergeKey(other.getKey());
                }
                this.mergeUnknownFields(other.unknownFields);
                this.onChanged();
                return this;
            }

            @Override
            public final boolean isInitialized() {
                if (!this.hasIdentity()) {
                    return false;
                }
                if (!this.hasKey()) {
                    return false;
                }
                if (!this.getIdentity().isInitialized()) {
                    return false;
                }
                if (!this.getKey().isInitialized()) {
                    return false;
                }
                return true;
            }

            @Override
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                ClientSetup parsedMessage = null;
                try {
                    parsedMessage = ClientSetup.PARSER.parsePartialFrom(input, extensionRegistry);
                }
                catch (InvalidProtocolBufferException e) {
                    parsedMessage = (ClientSetup)e.getUnfinishedMessage();
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
            public boolean hasIdentity() {
                return (this.bitField0_ & 1) == 1;
            }

            @Override
            public Identity getIdentity() {
                if (this.identityBuilder_ == null) {
                    return this.identity_ == null ? Identity.getDefaultInstance() : this.identity_;
                }
                return this.identityBuilder_.getMessage();
            }

            public Builder setIdentity(Identity value) {
                if (this.identityBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.identity_ = value;
                    this.onChanged();
                } else {
                    this.identityBuilder_.setMessage(value);
                }
                this.bitField0_ |= 1;
                return this;
            }

            public Builder setIdentity(Identity.Builder builderForValue) {
                if (this.identityBuilder_ == null) {
                    this.identity_ = builderForValue.build();
                    this.onChanged();
                } else {
                    this.identityBuilder_.setMessage(builderForValue.build());
                }
                this.bitField0_ |= 1;
                return this;
            }

            public Builder mergeIdentity(Identity value) {
                if (this.identityBuilder_ == null) {
                    this.identity_ = (this.bitField0_ & 1) == 1 && this.identity_ != null && this.identity_ != Identity.getDefaultInstance() ? Identity.newBuilder(this.identity_).mergeFrom(value).buildPartial() : value;
                    this.onChanged();
                } else {
                    this.identityBuilder_.mergeFrom(value);
                }
                this.bitField0_ |= 1;
                return this;
            }

            public Builder clearIdentity() {
                if (this.identityBuilder_ == null) {
                    this.identity_ = null;
                    this.onChanged();
                } else {
                    this.identityBuilder_.clear();
                }
                this.bitField0_ &= -2;
                return this;
            }

            public Identity.Builder getIdentityBuilder() {
                this.bitField0_ |= 1;
                this.onChanged();
                return this.getIdentityFieldBuilder().getBuilder();
            }

            @Override
            public IdentityOrBuilder getIdentityOrBuilder() {
                if (this.identityBuilder_ != null) {
                    return this.identityBuilder_.getMessageOrBuilder();
                }
                return this.identity_ == null ? Identity.getDefaultInstance() : this.identity_;
            }

            private SingleFieldBuilder<Identity, Identity.Builder, IdentityOrBuilder> getIdentityFieldBuilder() {
                if (this.identityBuilder_ == null) {
                    this.identityBuilder_ = new SingleFieldBuilder(this.getIdentity(), this.getParentForChildren(), this.isClean());
                    this.identity_ = null;
                }
                return this.identityBuilder_;
            }

            @Override
            public boolean hasKey() {
                return (this.bitField0_ & 2) == 2;
            }

            @Override
            public DHPublicKey getKey() {
                if (this.keyBuilder_ == null) {
                    return this.key_ == null ? DHPublicKey.getDefaultInstance() : this.key_;
                }
                return this.keyBuilder_.getMessage();
            }

            public Builder setKey(DHPublicKey value) {
                if (this.keyBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.key_ = value;
                    this.onChanged();
                } else {
                    this.keyBuilder_.setMessage(value);
                }
                this.bitField0_ |= 2;
                return this;
            }

            public Builder setKey(DHPublicKey.Builder builderForValue) {
                if (this.keyBuilder_ == null) {
                    this.key_ = builderForValue.build();
                    this.onChanged();
                } else {
                    this.keyBuilder_.setMessage(builderForValue.build());
                }
                this.bitField0_ |= 2;
                return this;
            }

            public Builder mergeKey(DHPublicKey value) {
                if (this.keyBuilder_ == null) {
                    this.key_ = (this.bitField0_ & 2) == 2 && this.key_ != null && this.key_ != DHPublicKey.getDefaultInstance() ? DHPublicKey.newBuilder(this.key_).mergeFrom(value).buildPartial() : value;
                    this.onChanged();
                } else {
                    this.keyBuilder_.mergeFrom(value);
                }
                this.bitField0_ |= 2;
                return this;
            }

            public Builder clearKey() {
                if (this.keyBuilder_ == null) {
                    this.key_ = null;
                    this.onChanged();
                } else {
                    this.keyBuilder_.clear();
                }
                this.bitField0_ &= -3;
                return this;
            }

            public DHPublicKey.Builder getKeyBuilder() {
                this.bitField0_ |= 2;
                this.onChanged();
                return this.getKeyFieldBuilder().getBuilder();
            }

            @Override
            public DHPublicKeyOrBuilder getKeyOrBuilder() {
                if (this.keyBuilder_ != null) {
                    return this.keyBuilder_.getMessageOrBuilder();
                }
                return this.key_ == null ? DHPublicKey.getDefaultInstance() : this.key_;
            }

            private SingleFieldBuilder<DHPublicKey, DHPublicKey.Builder, DHPublicKeyOrBuilder> getKeyFieldBuilder() {
                if (this.keyBuilder_ == null) {
                    this.keyBuilder_ = new SingleFieldBuilder(this.getKey(), this.getParentForChildren(), this.isClean());
                    this.key_ = null;
                }
                return this.keyBuilder_;
            }
        }

    }

    public static interface ClientSetupOrBuilder
    extends MessageOrBuilder {
        public boolean hasIdentity();

        public Identity getIdentity();

        public IdentityOrBuilder getIdentityOrBuilder();

        public boolean hasKey();

        public DHPublicKey getKey();

        public DHPublicKeyOrBuilder getKeyOrBuilder();
    }

    public static final class CommsMsg
    extends GeneratedMessage
    implements CommsMsgOrBuilder {
        private int bitField0_;
        public static final int TYPE_FIELD_NUMBER = 1;
        private int type_;
        public static final int CLIENTSETUP_FIELD_NUMBER = 2;
        private ClientSetup clientSetup_;
        public static final int SERVERSETUP_FIELD_NUMBER = 3;
        private ServerSetup serverSetup_;
        private byte memoizedIsInitialized;
        private static final long serialVersionUID = 0;
        private static final CommsMsg DEFAULT_INSTANCE = new CommsMsg();
        @Deprecated
        public static final Parser<CommsMsg> PARSER = new AbstractParser<CommsMsg>(){

            @Override
            public CommsMsg parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                try {
                    return new CommsMsg(input, extensionRegistry);
                }
                catch (RuntimeException e) {
                    if (e.getCause() instanceof InvalidProtocolBufferException) {
                        throw (InvalidProtocolBufferException)e.getCause();
                    }
                    throw e;
                }
            }
        };

        private CommsMsg(GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
        }

        private CommsMsg() {
            this.memoizedIsInitialized = -1;
            this.type_ = 1;
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private CommsMsg(CodedInputStream input, ExtensionRegistryLite extensionRegistry) {
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
                        case 8: {
                            int rawValue = input.readEnum();
                            Type value = Type.valueOf(rawValue);
                            if (value == null) {
                                unknownFields.mergeVarintField(1, rawValue);
                                continue block12;
                            }
                            this.bitField0_ |= 1;
                            this.type_ = rawValue;
                            continue block12;
                        }
                        case 18: {
                            ClientSetup.Builder subBuilder = null;
                            if ((this.bitField0_ & 2) == 2) {
                                subBuilder = this.clientSetup_.toBuilder();
                            }
                            this.clientSetup_ = input.readMessage(ClientSetup.parser(), extensionRegistry);
                            if (subBuilder != null) {
                                subBuilder.mergeFrom(this.clientSetup_);
                                this.clientSetup_ = subBuilder.buildPartial();
                            }
                            this.bitField0_ |= 2;
                            continue block12;
                        }
                        case 26: 
                    }
                    ServerSetup.Builder subBuilder = null;
                    if ((this.bitField0_ & 4) == 4) {
                        subBuilder = this.serverSetup_.toBuilder();
                    }
                    this.serverSetup_ = input.readMessage(ServerSetup.parser(), extensionRegistry);
                    if (subBuilder != null) {
                        subBuilder.mergeFrom(this.serverSetup_);
                        this.serverSetup_ = subBuilder.buildPartial();
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
            return internal_static_com_cyberpointllc_stac_comms_CommsMsg_descriptor;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_com_cyberpointllc_stac_comms_CommsMsg_fieldAccessorTable.ensureFieldAccessorsInitialized(CommsMsg.class, Builder.class);
        }

        @Override
        public boolean hasType() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override
        public Type getType() {
            Type result = Type.valueOf(this.type_);
            return result == null ? Type.CLIENT_SETUP : result;
        }

        @Override
        public boolean hasClientSetup() {
            return (this.bitField0_ & 2) == 2;
        }

        @Override
        public ClientSetup getClientSetup() {
            return this.clientSetup_ == null ? ClientSetup.getDefaultInstance() : this.clientSetup_;
        }

        @Override
        public ClientSetupOrBuilder getClientSetupOrBuilder() {
            return this.clientSetup_ == null ? ClientSetup.getDefaultInstance() : this.clientSetup_;
        }

        @Override
        public boolean hasServerSetup() {
            return (this.bitField0_ & 4) == 4;
        }

        @Override
        public ServerSetup getServerSetup() {
            return this.serverSetup_ == null ? ServerSetup.getDefaultInstance() : this.serverSetup_;
        }

        @Override
        public ServerSetupOrBuilder getServerSetupOrBuilder() {
            return this.serverSetup_ == null ? ServerSetup.getDefaultInstance() : this.serverSetup_;
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
            if (!this.hasType()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (this.hasClientSetup() && !this.getClientSetup().isInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (this.hasServerSetup() && !this.getServerSetup().isInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public void writeTo(CodedOutputStream output) throws IOException {
            if ((this.bitField0_ & 1) == 1) {
                output.writeEnum(1, this.type_);
            }
            if ((this.bitField0_ & 2) == 2) {
                output.writeMessage(2, this.getClientSetup());
            }
            if ((this.bitField0_ & 4) == 4) {
                output.writeMessage(3, this.getServerSetup());
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
                size += CodedOutputStream.computeEnumSize(1, this.type_);
            }
            if ((this.bitField0_ & 2) == 2) {
                size += CodedOutputStream.computeMessageSize(2, this.getClientSetup());
            }
            if ((this.bitField0_ & 4) == 4) {
                size += CodedOutputStream.computeMessageSize(3, this.getServerSetup());
            }
            this.memoizedSize = size += this.unknownFields.getSerializedSize();
            return size;
        }

        public static CommsMsg parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static CommsMsg parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static CommsMsg parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static CommsMsg parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static CommsMsg parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static CommsMsg parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static CommsMsg parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static CommsMsg parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static CommsMsg parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static CommsMsg parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        @Override
        public Builder newBuilderForType() {
            return CommsMsg.newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(CommsMsg prototype) {
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

        public static CommsMsg getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<CommsMsg> parser() {
            return PARSER;
        }

        public Parser<CommsMsg> getParserForType() {
            return PARSER;
        }

        @Override
        public CommsMsg getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }

        public static final class Builder
        extends GeneratedMessage.Builder<Builder>
        implements CommsMsgOrBuilder {
            private int bitField0_;
            private int type_ = 1;
            private ClientSetup clientSetup_ = null;
            private SingleFieldBuilder<ClientSetup, ClientSetup.Builder, ClientSetupOrBuilder> clientSetupBuilder_;
            private ServerSetup serverSetup_ = null;
            private SingleFieldBuilder<ServerSetup, ServerSetup.Builder, ServerSetupOrBuilder> serverSetupBuilder_;

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_com_cyberpointllc_stac_comms_CommsMsg_descriptor;
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_com_cyberpointllc_stac_comms_CommsMsg_fieldAccessorTable.ensureFieldAccessorsInitialized(CommsMsg.class, Builder.class);
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
                    this.getClientSetupFieldBuilder();
                    this.getServerSetupFieldBuilder();
                }
            }

            @Override
            public Builder clear() {
                super.clear();
                this.type_ = 1;
                this.bitField0_ &= -2;
                if (this.clientSetupBuilder_ == null) {
                    this.clientSetup_ = null;
                } else {
                    this.clientSetupBuilder_.clear();
                }
                this.bitField0_ &= -3;
                if (this.serverSetupBuilder_ == null) {
                    this.serverSetup_ = null;
                } else {
                    this.serverSetupBuilder_.clear();
                }
                this.bitField0_ &= -5;
                return this;
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_com_cyberpointllc_stac_comms_CommsMsg_descriptor;
            }

            @Override
            public CommsMsg getDefaultInstanceForType() {
                return CommsMsg.getDefaultInstance();
            }

            @Override
            public CommsMsg build() {
                CommsMsg result = this.buildPartial();
                if (!result.isInitialized()) {
                    throw AbstractMessage.Builder.newUninitializedMessageException(result);
                }
                return result;
            }

            @Override
            public CommsMsg buildPartial() {
                CommsMsg result = new CommsMsg(this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) == 1) {
                    to_bitField0_ |= 1;
                }
                result.type_ = this.type_;
                if ((from_bitField0_ & 2) == 2) {
                    to_bitField0_ |= 2;
                }
                if (this.clientSetupBuilder_ == null) {
                    result.clientSetup_ = this.clientSetup_;
                } else {
                    result.clientSetup_ = this.clientSetupBuilder_.build();
                }
                if ((from_bitField0_ & 4) == 4) {
                    to_bitField0_ |= 4;
                }
                if (this.serverSetupBuilder_ == null) {
                    result.serverSetup_ = this.serverSetup_;
                } else {
                    result.serverSetup_ = this.serverSetupBuilder_.build();
                }
                result.bitField0_ = to_bitField0_;
                this.onBuilt();
                return result;
            }

            @Override
            public Builder mergeFrom(Message other) {
                if (other instanceof CommsMsg) {
                    return this.mergeFrom((CommsMsg)other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(CommsMsg other) {
                if (other == CommsMsg.getDefaultInstance()) {
                    return this;
                }
                if (other.hasType()) {
                    this.setType(other.getType());
                }
                if (other.hasClientSetup()) {
                    this.mergeClientSetup(other.getClientSetup());
                }
                if (other.hasServerSetup()) {
                    this.mergeServerSetup(other.getServerSetup());
                }
                this.mergeUnknownFields(other.unknownFields);
                this.onChanged();
                return this;
            }

            @Override
            public final boolean isInitialized() {
                if (!this.hasType()) {
                    return false;
                }
                if (this.hasClientSetup() && !this.getClientSetup().isInitialized()) {
                    return false;
                }
                if (this.hasServerSetup() && !this.getServerSetup().isInitialized()) {
                    return false;
                }
                return true;
            }

            @Override
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                CommsMsg parsedMessage = null;
                try {
                    parsedMessage = CommsMsg.PARSER.parsePartialFrom(input, extensionRegistry);
                }
                catch (InvalidProtocolBufferException e) {
                    parsedMessage = (CommsMsg)e.getUnfinishedMessage();
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
            public boolean hasType() {
                return (this.bitField0_ & 1) == 1;
            }

            @Override
            public Type getType() {
                Type result = Type.valueOf(this.type_);
                return result == null ? Type.CLIENT_SETUP : result;
            }

            public Builder setType(Type value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.type_ = value.getNumber();
                this.onChanged();
                return this;
            }

            public Builder clearType() {
                this.bitField0_ &= -2;
                this.type_ = 1;
                this.onChanged();
                return this;
            }

            @Override
            public boolean hasClientSetup() {
                return (this.bitField0_ & 2) == 2;
            }

            @Override
            public ClientSetup getClientSetup() {
                if (this.clientSetupBuilder_ == null) {
                    return this.clientSetup_ == null ? ClientSetup.getDefaultInstance() : this.clientSetup_;
                }
                return this.clientSetupBuilder_.getMessage();
            }

            public Builder setClientSetup(ClientSetup value) {
                if (this.clientSetupBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.clientSetup_ = value;
                    this.onChanged();
                } else {
                    this.clientSetupBuilder_.setMessage(value);
                }
                this.bitField0_ |= 2;
                return this;
            }

            public Builder setClientSetup(ClientSetup.Builder builderForValue) {
                if (this.clientSetupBuilder_ == null) {
                    this.clientSetup_ = builderForValue.build();
                    this.onChanged();
                } else {
                    this.clientSetupBuilder_.setMessage(builderForValue.build());
                }
                this.bitField0_ |= 2;
                return this;
            }

            public Builder mergeClientSetup(ClientSetup value) {
                if (this.clientSetupBuilder_ == null) {
                    this.clientSetup_ = (this.bitField0_ & 2) == 2 && this.clientSetup_ != null && this.clientSetup_ != ClientSetup.getDefaultInstance() ? ClientSetup.newBuilder(this.clientSetup_).mergeFrom(value).buildPartial() : value;
                    this.onChanged();
                } else {
                    this.clientSetupBuilder_.mergeFrom(value);
                }
                this.bitField0_ |= 2;
                return this;
            }

            public Builder clearClientSetup() {
                if (this.clientSetupBuilder_ == null) {
                    this.clientSetup_ = null;
                    this.onChanged();
                } else {
                    this.clientSetupBuilder_.clear();
                }
                this.bitField0_ &= -3;
                return this;
            }

            public ClientSetup.Builder getClientSetupBuilder() {
                this.bitField0_ |= 2;
                this.onChanged();
                return this.getClientSetupFieldBuilder().getBuilder();
            }

            @Override
            public ClientSetupOrBuilder getClientSetupOrBuilder() {
                if (this.clientSetupBuilder_ != null) {
                    return this.clientSetupBuilder_.getMessageOrBuilder();
                }
                return this.clientSetup_ == null ? ClientSetup.getDefaultInstance() : this.clientSetup_;
            }

            private SingleFieldBuilder<ClientSetup, ClientSetup.Builder, ClientSetupOrBuilder> getClientSetupFieldBuilder() {
                if (this.clientSetupBuilder_ == null) {
                    this.clientSetupBuilder_ = new SingleFieldBuilder(this.getClientSetup(), this.getParentForChildren(), this.isClean());
                    this.clientSetup_ = null;
                }
                return this.clientSetupBuilder_;
            }

            @Override
            public boolean hasServerSetup() {
                return (this.bitField0_ & 4) == 4;
            }

            @Override
            public ServerSetup getServerSetup() {
                if (this.serverSetupBuilder_ == null) {
                    return this.serverSetup_ == null ? ServerSetup.getDefaultInstance() : this.serverSetup_;
                }
                return this.serverSetupBuilder_.getMessage();
            }

            public Builder setServerSetup(ServerSetup value) {
                if (this.serverSetupBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.serverSetup_ = value;
                    this.onChanged();
                } else {
                    this.serverSetupBuilder_.setMessage(value);
                }
                this.bitField0_ |= 4;
                return this;
            }

            public Builder setServerSetup(ServerSetup.Builder builderForValue) {
                if (this.serverSetupBuilder_ == null) {
                    this.serverSetup_ = builderForValue.build();
                    this.onChanged();
                } else {
                    this.serverSetupBuilder_.setMessage(builderForValue.build());
                }
                this.bitField0_ |= 4;
                return this;
            }

            public Builder mergeServerSetup(ServerSetup value) {
                if (this.serverSetupBuilder_ == null) {
                    this.serverSetup_ = (this.bitField0_ & 4) == 4 && this.serverSetup_ != null && this.serverSetup_ != ServerSetup.getDefaultInstance() ? ServerSetup.newBuilder(this.serverSetup_).mergeFrom(value).buildPartial() : value;
                    this.onChanged();
                } else {
                    this.serverSetupBuilder_.mergeFrom(value);
                }
                this.bitField0_ |= 4;
                return this;
            }

            public Builder clearServerSetup() {
                if (this.serverSetupBuilder_ == null) {
                    this.serverSetup_ = null;
                    this.onChanged();
                } else {
                    this.serverSetupBuilder_.clear();
                }
                this.bitField0_ &= -5;
                return this;
            }

            public ServerSetup.Builder getServerSetupBuilder() {
                this.bitField0_ |= 4;
                this.onChanged();
                return this.getServerSetupFieldBuilder().getBuilder();
            }

            @Override
            public ServerSetupOrBuilder getServerSetupOrBuilder() {
                if (this.serverSetupBuilder_ != null) {
                    return this.serverSetupBuilder_.getMessageOrBuilder();
                }
                return this.serverSetup_ == null ? ServerSetup.getDefaultInstance() : this.serverSetup_;
            }

            private SingleFieldBuilder<ServerSetup, ServerSetup.Builder, ServerSetupOrBuilder> getServerSetupFieldBuilder() {
                if (this.serverSetupBuilder_ == null) {
                    this.serverSetupBuilder_ = new SingleFieldBuilder(this.getServerSetup(), this.getParentForChildren(), this.isClean());
                    this.serverSetup_ = null;
                }
                return this.serverSetupBuilder_;
            }
        }

        public static enum Type implements ProtocolMessageEnum
        {
            CLIENT_SETUP(0, 1),
            SERVER_SETUP(1, 2);
            
            public static final int CLIENT_SETUP_VALUE = 1;
            public static final int SERVER_SETUP_VALUE = 2;
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
                        return CLIENT_SETUP;
                    }
                    case 2: {
                        return SERVER_SETUP;
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
                return CommsMsg.getDescriptor().getEnumTypes().get(0);
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

    public static interface CommsMsgOrBuilder
    extends MessageOrBuilder {
        public boolean hasType();

        public CommsMsg.Type getType();

        public boolean hasClientSetup();

        public ClientSetup getClientSetup();

        public ClientSetupOrBuilder getClientSetupOrBuilder();

        public boolean hasServerSetup();

        public ServerSetup getServerSetup();

        public ServerSetupOrBuilder getServerSetupOrBuilder();
    }

    public static final class DHPublicKey
    extends GeneratedMessage
    implements DHPublicKeyOrBuilder {
        private int bitField0_;
        public static final int KEY_FIELD_NUMBER = 1;
        private ByteString key_;
        private byte memoizedIsInitialized;
        private static final long serialVersionUID = 0;
        private static final DHPublicKey DEFAULT_INSTANCE = new DHPublicKey();
        @Deprecated
        public static final Parser<DHPublicKey> PARSER = new AbstractParser<DHPublicKey>(){

            @Override
            public DHPublicKey parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                try {
                    return new DHPublicKey(input, extensionRegistry);
                }
                catch (RuntimeException e) {
                    if (e.getCause() instanceof InvalidProtocolBufferException) {
                        throw (InvalidProtocolBufferException)e.getCause();
                    }
                    throw e;
                }
            }
        };

        private DHPublicKey(GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
        }

        private DHPublicKey() {
            this.memoizedIsInitialized = -1;
            this.key_ = ByteString.EMPTY;
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private DHPublicKey(CodedInputStream input, ExtensionRegistryLite extensionRegistry) {
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
                    this.key_ = input.readBytes();
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
            return internal_static_com_cyberpointllc_stac_comms_DHPublicKey_descriptor;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_com_cyberpointllc_stac_comms_DHPublicKey_fieldAccessorTable.ensureFieldAccessorsInitialized(DHPublicKey.class, Builder.class);
        }

        @Override
        public boolean hasKey() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override
        public ByteString getKey() {
            return this.key_;
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
            if (!this.hasKey()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public void writeTo(CodedOutputStream output) throws IOException {
            if ((this.bitField0_ & 1) == 1) {
                output.writeBytes(1, this.key_);
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
                size += CodedOutputStream.computeBytesSize(1, this.key_);
            }
            this.memoizedSize = size += this.unknownFields.getSerializedSize();
            return size;
        }

        public static DHPublicKey parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static DHPublicKey parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static DHPublicKey parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static DHPublicKey parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static DHPublicKey parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static DHPublicKey parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static DHPublicKey parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static DHPublicKey parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static DHPublicKey parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static DHPublicKey parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        @Override
        public Builder newBuilderForType() {
            return DHPublicKey.newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(DHPublicKey prototype) {
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

        public static DHPublicKey getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<DHPublicKey> parser() {
            return PARSER;
        }

        public Parser<DHPublicKey> getParserForType() {
            return PARSER;
        }

        @Override
        public DHPublicKey getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }

        public static final class Builder
        extends GeneratedMessage.Builder<Builder>
        implements DHPublicKeyOrBuilder {
            private int bitField0_;
            private ByteString key_ = ByteString.EMPTY;

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_com_cyberpointllc_stac_comms_DHPublicKey_descriptor;
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_com_cyberpointllc_stac_comms_DHPublicKey_fieldAccessorTable.ensureFieldAccessorsInitialized(DHPublicKey.class, Builder.class);
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
                this.key_ = ByteString.EMPTY;
                this.bitField0_ &= -2;
                return this;
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_com_cyberpointllc_stac_comms_DHPublicKey_descriptor;
            }

            @Override
            public DHPublicKey getDefaultInstanceForType() {
                return DHPublicKey.getDefaultInstance();
            }

            @Override
            public DHPublicKey build() {
                DHPublicKey result = this.buildPartial();
                if (!result.isInitialized()) {
                    throw AbstractMessage.Builder.newUninitializedMessageException(result);
                }
                return result;
            }

            @Override
            public DHPublicKey buildPartial() {
                DHPublicKey result = new DHPublicKey(this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) == 1) {
                    to_bitField0_ |= 1;
                }
                result.key_ = this.key_;
                result.bitField0_ = to_bitField0_;
                this.onBuilt();
                return result;
            }

            @Override
            public Builder mergeFrom(Message other) {
                if (other instanceof DHPublicKey) {
                    return this.mergeFrom((DHPublicKey)other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(DHPublicKey other) {
                if (other == DHPublicKey.getDefaultInstance()) {
                    return this;
                }
                if (other.hasKey()) {
                    this.setKey(other.getKey());
                }
                this.mergeUnknownFields(other.unknownFields);
                this.onChanged();
                return this;
            }

            @Override
            public final boolean isInitialized() {
                if (!this.hasKey()) {
                    return false;
                }
                return true;
            }

            @Override
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                DHPublicKey parsedMessage = null;
                try {
                    parsedMessage = DHPublicKey.PARSER.parsePartialFrom(input, extensionRegistry);
                }
                catch (InvalidProtocolBufferException e) {
                    parsedMessage = (DHPublicKey)e.getUnfinishedMessage();
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
            public boolean hasKey() {
                return (this.bitField0_ & 1) == 1;
            }

            @Override
            public ByteString getKey() {
                return this.key_;
            }

            public Builder setKey(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.key_ = value;
                this.onChanged();
                return this;
            }

            public Builder clearKey() {
                this.bitField0_ &= -2;
                this.key_ = DHPublicKey.getDefaultInstance().getKey();
                this.onChanged();
                return this;
            }
        }

    }

    public static interface DHPublicKeyOrBuilder
    extends MessageOrBuilder {
        public boolean hasKey();

        public ByteString getKey();
    }

}

