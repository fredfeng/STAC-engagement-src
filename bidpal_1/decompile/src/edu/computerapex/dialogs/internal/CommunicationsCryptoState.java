/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.dialogs.internal;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import edu.computerapex.allow.KeyExchangeServer;
import edu.computerapex.dialogs.Comms;
import edu.computerapex.dialogs.CommunicationsDeviation;
import edu.computerapex.dialogs.CommunicationsIdentity;
import edu.computerapex.dialogs.CommunicationsPublicIdentity;
import edu.computerapex.dialogs.SerializerUtil;
import edu.computerapex.math.EncryptionPrivateKey;
import edu.computerapex.math.EncryptionPublicKey;
import edu.computerapex.math.EncryptionUtil;
import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CommunicationsCryptoState {
    private static final int KEYSIZE = 256;
    private static final int KEYSIZE_BYTES = 32;
    private static final int IV_SIZE_BYTES = 16;
    private State state = State.INITIAL;
    private final CommunicationsIdentity ourIdentity;
    private CommunicationsPublicIdentity theirIdentity = null;
    private BigInteger theirPublicKey = null;
    private KeyExchangeServer ourKeyExchangeServer;
    private SecretKey sessionKey = null;
    private SecretKey hmacKey;
    private final Mac hmac;
    private final Cipher encryptCipher;
    private final Cipher decryptCipher;
    private BigInteger ourTestNumber = new BigInteger(256, new Random());
    private BigInteger theirTestNumber = null;

    public CommunicationsCryptoState(CommunicationsIdentity ourIdentity) throws CommunicationsDeviation {
        try {
            this.ourIdentity = ourIdentity;
            this.ourKeyExchangeServer = new KeyExchangeServer();
            this.encryptCipher = Cipher.getInstance("AES/CTR/NoPadding");
            this.decryptCipher = Cipher.getInstance("AES/CTR/NoPadding");
            this.hmac = Mac.getInstance("HmacSHA256");
        }
        catch (Exception e) {
            throw new CommunicationsDeviation(e);
        }
    }

    public boolean hasSetupMessage() {
        return this.state.ordinal() < State.FAILURE.ordinal();
    }

    public boolean isReady() {
        return this.state == State.READY;
    }

    public boolean hasFailed() {
        return this.state == State.FAILURE;
    }

    public CommunicationsPublicIdentity obtainTheirIdentity() {
        return this.theirIdentity;
    }

    public byte[] obtainNextSetupMessage() throws CommunicationsDeviation, InvalidParameterSpecException, InvalidKeyException {
        switch (this.state) {
            case INITIAL: {
                byte[] msg = this.grabDHPublicKeyMessage();
                this.state = State.WAIT_SERVER_KEY;
                return msg;
            }
            case SEND_SERVER_KEY: {
                byte[] msg = this.grabDHPublicKeyMessage();
                this.state = State.WAIT_CLIENT_IDENTITY;
                return msg;
            }
            case SEND_CLIENT_IDENTITY: {
                byte[] msg = this.obtainClientSetupMsg();
                this.state = State.WAIT_SERVER_IDENTITY_AND_TEST;
                return this.encrypt(msg);
            }
            case SEND_SERVER_IDENTITY_AND_TEST: {
                byte[] msg = this.obtainServerSetupMsg();
                this.state = State.WAIT_CLIENT_RESPONSE_AND_TEST;
                return this.encrypt(msg);
            }
            case SEND_CLIENT_RESPONSE_AND_TEST: {
                byte[] msg = this.grabClientResponseMsg();
                this.state = State.WAIT_SERVER_RESPONSE_AND_RESULTS;
                return this.encrypt(msg);
            }
            case SEND_SERVER_RESPONSE_AND_RESULTS: {
                byte[] msg = this.getServerResponseMsg(true);
                this.state = State.WAIT_CLIENT_RESULTS;
                return this.encrypt(msg);
            }
            case SEND_CLIENT_RESULTS: {
                byte[] msg = this.takeEncryptionResults(true).toByteArray();
                this.state = State.READY;
                return this.encrypt(msg);
            }
            case SEND_SERVER_RSA_FAILURE: {
                this.state = State.FAILURE;
            }
            case SEND_CLIENT_RSA_FAILURE: {
                this.state = State.FAILURE;
            }
            case SEND_SECOND_RESULTS_AND_TEST: {
                byte[] msg = this.getClientResponseToFailure(false);
                this.state = State.WAIT_SECOND_RESPONSE;
                return this.encrypt(msg);
            }
            case SEND_SECOND_SUCCESS_AND_TEST: {
                byte[] msg = this.getClientResponseToFailure(true);
                this.state = State.READY;
                return this.encrypt(msg);
            }
            case SEND_SECOND_RESPONSE: {
                byte[] msg = this.pullEncryptionResponseMsg().toByteArray();
                this.state = State.WAIT_CLIENT_RESULTS_AND_TEST;
                return this.encrypt(msg);
            }
        }
        throw new CommunicationsDeviation("Invalid state when getting next setup message: " + (Object)((Object)this.state));
    }

    private byte[] grabDHPublicKeyMessage() {
        BigInteger publicKey = this.ourKeyExchangeServer.pullPublicKey();
        Comms.DHPublicKey dhPublicKey = Comms.DHPublicKey.newBuilder().setKey(ByteString.copyFrom(publicKey.toByteArray())).build();
        return dhPublicKey.toByteArray();
    }

    private byte[] getClientResponseToFailure(boolean passedTest) {
        Comms.RsaResults results = this.takeEncryptionResults(passedTest);
        Comms.RsaTest test = this.fetchEncryptionTestMsg(true);
        Comms.ClientResponseToFailure responseToFailure = Comms.ClientResponseToFailure.newBuilder().setRsaResults(results).setRsaTest(test).build();
        return responseToFailure.toByteArray();
    }

    private byte[] obtainServerSetupMsg() throws CommunicationsDeviation {
        try {
            Comms.Identity identity = SerializerUtil.serializeIdentity(this.ourIdentity.takePublicIdentity());
            Comms.RsaTest EncryptionTest = this.fetchEncryptionTestMsg(false);
            Comms.DHPublicKey theirKey = SerializerUtil.serializeDHPublicKey(this.theirPublicKey);
            Comms.ServerSetup.Builder serverSetupBuilder = Comms.ServerSetup.newBuilder().setIdentity(identity).setRsaTest(EncryptionTest).setKey(theirKey);
            Comms.CommsMsg communicationsMsg = Comms.CommsMsg.newBuilder().setType(Comms.CommsMsg.Type.SERVER_SETUP).setServerSetup(serverSetupBuilder).build();
            return this.fetchSignedMessage(communicationsMsg);
        }
        catch (Exception e) {
            throw new CommunicationsDeviation(e);
        }
    }

    private byte[] obtainClientSetupMsg() throws CommunicationsDeviation {
        Comms.Identity identity = SerializerUtil.serializeIdentity(this.ourIdentity.takePublicIdentity());
        Comms.DHPublicKey theirKey = SerializerUtil.serializeDHPublicKey(this.theirPublicKey);
        Comms.ClientSetup.Builder clientSetupBuilder = Comms.ClientSetup.newBuilder().setIdentity(identity).setKey(theirKey);
        Comms.CommsMsg communicationsMsg = Comms.CommsMsg.newBuilder().setType(Comms.CommsMsg.Type.CLIENT_SETUP).setClientSetup(clientSetupBuilder).build();
        return this.fetchSignedMessage(communicationsMsg);
    }

    private byte[] grabClientResponseMsg() {
        Comms.RsaResponse response = this.pullEncryptionResponseMsg();
        Comms.RsaTest test = this.fetchEncryptionTestMsg(false);
        Comms.ClientResponse clientResponse = Comms.ClientResponse.newBuilder().setRsaResponse(response).setRsaTest(test).build();
        return clientResponse.toByteArray();
    }

    private byte[] getServerResponseMsg(boolean passed) {
        Comms.RsaResponse response = this.pullEncryptionResponseMsg();
        Comms.RsaResults results = this.takeEncryptionResults(passed);
        Comms.ServerResponse serverResponse = Comms.ServerResponse.newBuilder().setRsaResponse(response).setRsaResults(results).build();
        return serverResponse.toByteArray();
    }

    private Comms.RsaTest fetchEncryptionTestMsg(boolean updateOurTestNumber) {
        if (updateOurTestNumber) {
            this.ourTestNumber = new BigInteger(256, new Random());
        }
        byte[] encryptedTestNumber = this.theirIdentity.getPublicKey().encrypt(this.ourTestNumber.toByteArray());
        Comms.RsaTest EncryptionTest = Comms.RsaTest.newBuilder().setTest(ByteString.copyFrom(encryptedTestNumber)).build();
        return EncryptionTest;
    }

    private Comms.RsaResponse pullEncryptionResponseMsg() {
        Comms.RsaResponse EncryptionResponse = Comms.RsaResponse.newBuilder().setResponse(ByteString.copyFrom(this.theirTestNumber.toByteArray())).build();
        return EncryptionResponse;
    }

    private Comms.RsaResults takeEncryptionResults(boolean passed) {
        BigInteger passedTest = BigInteger.ONE;
        if (!passed) {
            passedTest = BigInteger.TEN;
        }
        Comms.RsaResults results = Comms.RsaResults.newBuilder().setResults(ByteString.copyFrom(passedTest.toByteArray())).build();
        return results;
    }

    private byte[] fetchSignedMessage(Comms.CommsMsg communicationsMsg) throws CommunicationsDeviation {
        ByteString data = communicationsMsg.toByteString();
        Comms.SignedMessage signedMessage = Comms.SignedMessage.newBuilder().setData(data).setSignedHash(ByteString.copyFrom(EncryptionUtil.sign(data.toByteArray(), this.ourIdentity.fetchPrivateKey()))).build();
        return signedMessage.toByteArray();
    }

    public void processNextSetupMessage(byte[] msg) throws CommunicationsDeviation {
        block13 : {
            try {
                switch (this.state) {
                    case INITIAL: {
                        this.handleClientDHPublicKey(msg);
                        break block13;
                    }
                    case WAIT_SERVER_KEY: {
                        this.handleServerDHPublicKey(msg);
                        break block13;
                    }
                    case WAIT_CLIENT_IDENTITY: {
                        this.handleClientSetupMsg(this.decrypt(msg));
                        break block13;
                    }
                    case WAIT_SERVER_IDENTITY_AND_TEST: {
                        this.handleServerSetupMsg(this.decrypt(msg));
                        break block13;
                    }
                    case WAIT_CLIENT_RESPONSE_AND_TEST: {
                        this.handleClientResponse(this.decrypt(msg));
                        break block13;
                    }
                    case WAIT_SERVER_RESPONSE_AND_RESULTS: {
                        this.handleServerResponse(this.decrypt(msg));
                        break block13;
                    }
                    case WAIT_CLIENT_RESULTS: {
                        this.handleClientResults(this.decrypt(msg));
                        break block13;
                    }
                    case WAIT_CLIENT_RESULTS_AND_TEST: {
                        this.handleClientResultsAndTest(this.decrypt(msg));
                        break block13;
                    }
                    case WAIT_SECOND_RESPONSE: {
                        this.handleSecondResponse(this.decrypt(msg));
                        break block13;
                    }
                }
                throw new CommunicationsDeviation("Invalid state when processing message: " + (Object)((Object)this.state));
            }
            catch (Exception e) {
                throw new CommunicationsDeviation(e);
            }
        }
    }

    private void handleSecondResponse(byte[] msg) throws InvalidProtocolBufferException, CommunicationsDeviation {
        Comms.RsaResponse response = Comms.RsaResponse.parseFrom(msg);
        boolean success = this.verifyEncryptionResponseMsg(response);
        this.state = success ? State.SEND_SECOND_SUCCESS_AND_TEST : State.SEND_SECOND_RESULTS_AND_TEST;
    }

    private void handleClientResults(byte[] msg) throws InvalidProtocolBufferException {
        Comms.RsaResults serverResults = Comms.RsaResults.parseFrom(msg);
        boolean serverResultSuccess = this.processEncryptionResults(serverResults);
        if (serverResultSuccess) {
            this.handleClientResultsAdviser();
        } else {
            this.handleClientResultsAssist();
        }
    }

    private void handleClientResultsAssist() {
        this.state = State.FAILURE;
    }

    private void handleClientResultsAdviser() {
        this.state = State.READY;
    }

    private void handleClientResultsAndTest(byte[] msg) throws InvalidProtocolBufferException {
        Comms.ClientResponseToFailure clientResponse = Comms.ClientResponseToFailure.parseFrom(msg);
        boolean success = this.processEncryptionResults(clientResponse.getRsaResults());
        if (!success) {
            new CommunicationsCryptoStateFunction(clientResponse).invoke();
        } else {
            this.state = State.READY;
        }
    }

    private void handleClientResponse(byte[] msg) throws InvalidProtocolBufferException, CommunicationsDeviation {
        Comms.ClientResponse clientResponse = Comms.ClientResponse.parseFrom(msg);
        boolean clientSuccess = this.verifyEncryptionResponseMsg(clientResponse.getRsaResponse());
        this.processEncryptionTestMsg(clientResponse.getRsaTest());
        if (clientSuccess) {
            this.state = State.SEND_SERVER_RESPONSE_AND_RESULTS;
        } else {
            this.handleClientResponseGateKeeper();
        }
    }

    private void handleClientResponseGateKeeper() {
        this.state = State.SEND_SERVER_RSA_FAILURE;
    }

    private void handleServerResponse(byte[] msg) throws InvalidProtocolBufferException, CommunicationsDeviation {
        Comms.ServerResponse serverResponse = Comms.ServerResponse.parseFrom(msg);
        Comms.RsaResults clientResults = serverResponse.getRsaResults();
        boolean clientResultSuccess = this.processEncryptionResults(clientResults);
        if (!clientResultSuccess) {
            this.state = State.FAILURE;
            return;
        }
        boolean serverSuccess = this.verifyEncryptionResponseMsg(serverResponse.getRsaResponse());
        if (serverSuccess) {
            this.state = State.SEND_CLIENT_RESULTS;
        } else {
            new CommunicationsCryptoStateUtility().invoke();
        }
    }

    private boolean processEncryptionResults(Comms.RsaResults results) throws InvalidProtocolBufferException {
        BigInteger testResults = EncryptionUtil.toBigInt(results.getResults().toByteArray());
        if (!testResults.equals(BigInteger.ONE)) {
            return false;
        }
        return true;
    }

    private void handleClientDHPublicKey(byte[] msg) throws InvalidProtocolBufferException, InvalidKeyException, NoSuchAlgorithmException {
        Comms.DHPublicKey publicKeyMessage = Comms.DHPublicKey.parseFrom(msg);
        this.theirPublicKey = SerializerUtil.deserializeDHPublicKey(publicKeyMessage);
        BigInteger key = EncryptionUtil.toBigInt(publicKeyMessage.getKey().toByteArray());
        this.assignSessionAndHmacKeys(key);
        this.state = State.SEND_SERVER_KEY;
    }

    private void handleServerDHPublicKey(byte[] msg) throws InvalidProtocolBufferException, InvalidKeyException, NoSuchAlgorithmException {
        Comms.DHPublicKey publicKeyMessage = Comms.DHPublicKey.parseFrom(msg);
        this.theirPublicKey = EncryptionUtil.toBigInt(publicKeyMessage.getKey().toByteArray());
        this.assignSessionAndHmacKeys(this.theirPublicKey);
        this.state = State.SEND_CLIENT_IDENTITY;
    }

    private void assignSessionAndHmacKeys(BigInteger DHPublicKey2) throws NoSuchAlgorithmException, InvalidKeyException {
        BigInteger masterSecret = this.ourKeyExchangeServer.generateMasterSecret(DHPublicKey2);
        byte[] secretByteArray = EncryptionUtil.fromBigInt(masterSecret, 192);
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        byte[] hashedByteArray = messageDigest.digest(secretByteArray);
        int splitLength = (int)Math.floor(hashedByteArray.length / 2);
        this.sessionKey = new SecretKeySpec(Arrays.copyOfRange(hashedByteArray, 0, splitLength), "AES");
        this.hmacKey = new SecretKeySpec(Arrays.copyOfRange(hashedByteArray, splitLength, hashedByteArray.length), "HmacSHA256");
        this.hmac.init(this.hmacKey);
    }

    private void handleClientSetupMsg(byte[] msg) throws InvalidProtocolBufferException, CommunicationsDeviation {
        Comms.SignedMessage signedMessage = Comms.SignedMessage.parseFrom(msg);
        byte[] data = signedMessage.getData().toByteArray();
        byte[] sig = signedMessage.getSignedHash().toByteArray();
        Comms.CommsMsg communicationsMsg = Comms.CommsMsg.parseFrom(data);
        if (communicationsMsg.getType() != Comms.CommsMsg.Type.CLIENT_SETUP || !communicationsMsg.hasClientSetup()) {
            this.handleClientSetupMsgAdviser(communicationsMsg);
        }
        Comms.ClientSetup clientSetup = communicationsMsg.getClientSetup();
        this.theirIdentity = SerializerUtil.deserializeIdentity(clientSetup.getIdentity());
        BigInteger key = SerializerUtil.deserializeDHPublicKey(clientSetup.getKey());
        if (!key.equals(this.ourKeyExchangeServer.pullPublicKey()) || !EncryptionUtil.verifySig(data, sig, this.theirIdentity.getPublicKey())) {
            this.handleClientSetupMsgUtility();
        }
        this.state = State.SEND_SERVER_IDENTITY_AND_TEST;
    }

    private void handleClientSetupMsgUtility() throws CommunicationsDeviation {
        throw new CommunicationsDeviation("Invalid client message signature!");
    }

    private void handleClientSetupMsgAdviser(Comms.CommsMsg communicationsMsg) throws CommunicationsDeviation {
        throw new CommunicationsDeviation("Invalid comms message. Expecting CLIENT_SETUP, got: " + communicationsMsg.getType());
    }

    private void handleServerSetupMsg(byte[] msg) throws InvalidProtocolBufferException, CommunicationsDeviation {
        Comms.SignedMessage signedMessage = Comms.SignedMessage.parseFrom(msg);
        byte[] data = signedMessage.getData().toByteArray();
        byte[] sig = signedMessage.getSignedHash().toByteArray();
        Comms.CommsMsg communicationsMsg = Comms.CommsMsg.parseFrom(data);
        if (communicationsMsg.getType() != Comms.CommsMsg.Type.SERVER_SETUP || !communicationsMsg.hasServerSetup()) {
            this.handleServerSetupMsgAssist(communicationsMsg);
        }
        Comms.ServerSetup serverSetup = communicationsMsg.getServerSetup();
        this.theirIdentity = SerializerUtil.deserializeIdentity(serverSetup.getIdentity());
        this.processEncryptionTestMsg(serverSetup.getRsaTest());
        BigInteger key = SerializerUtil.deserializeDHPublicKey(serverSetup.getKey());
        if (!key.equals(this.ourKeyExchangeServer.pullPublicKey()) || !EncryptionUtil.verifySig(data, sig, this.theirIdentity.getPublicKey())) {
            throw new CommunicationsDeviation("Invalid client message signature!");
        }
        this.state = State.SEND_CLIENT_RESPONSE_AND_TEST;
    }

    private void handleServerSetupMsgAssist(Comms.CommsMsg communicationsMsg) throws CommunicationsDeviation {
        throw new CommunicationsDeviation("Invalid comms message. Expecting SERVER_SETUP, got: " + communicationsMsg.getType());
    }

    private void processEncryptionTestMsg(Comms.RsaTest EncryptionTest) throws InvalidProtocolBufferException {
        byte[] theirTestBytes = EncryptionTest.getTest().toByteArray();
        byte[] theirDecryptedBytes = EncryptionUtil.decrypt(theirTestBytes, this.ourIdentity.fetchPrivateKey(), 32);
        this.theirTestNumber = EncryptionUtil.toBigInt(theirDecryptedBytes);
    }

    private boolean verifyEncryptionResponseMsg(Comms.RsaResponse EncryptionResponse) throws InvalidProtocolBufferException, CommunicationsDeviation {
        byte[] theirResponseBytes = EncryptionResponse.getResponse().toByteArray();
        BigInteger theirResponse = EncryptionUtil.toBigInt(theirResponseBytes);
        if (theirResponse.equals(this.ourTestNumber)) {
            return true;
        }
        return false;
    }

    public byte[] encrypt(byte[] data) throws CommunicationsDeviation, InvalidParameterSpecException, InvalidKeyException {
        this.encryptCipher.init(1, this.sessionKey);
        IvParameterSpec spec = this.encryptCipher.getParameters().getParameterSpec(IvParameterSpec.class);
        byte[] iv = spec.getIV();
        byte[] cipherText = this.encryptCipher.update(data);
        byte[] mac = this.hmac.doFinal(cipherText);
        byte[] encrypted = new byte[mac.length + cipherText.length + iv.length];
        System.arraycopy(iv, 0, encrypted, 0, iv.length);
        System.arraycopy(mac, 0, encrypted, iv.length, mac.length);
        System.arraycopy(cipherText, 0, encrypted, mac.length + iv.length, cipherText.length);
        return encrypted;
    }

    public byte[] decrypt(byte[] data) throws CommunicationsDeviation, InvalidAlgorithmParameterException, InvalidKeyException {
        byte[] iv = Arrays.copyOfRange(data, 0, 16);
        this.decryptCipher.init(2, (Key)this.sessionKey, new IvParameterSpec(iv));
        byte[] cipherText = Arrays.copyOfRange(data, 48, data.length);
        byte[] decrypted = this.decryptCipher.update(cipherText);
        byte[] providedMac = Arrays.copyOfRange(data, 16, 48);
        byte[] computedMac = this.hmac.doFinal(cipherText);
        if (!Arrays.equals(providedMac, computedMac)) {
            throw new CommunicationsDeviation("Computed and provided mac differ!:\n" + Arrays.toString(computedMac) + "\n" + Arrays.toString(providedMac));
        }
        return decrypted;
    }

    private class CommunicationsCryptoStateUtility {
        private CommunicationsCryptoStateUtility() {
        }

        public void invoke() {
            CommunicationsCryptoState.this.state = State.SEND_CLIENT_RSA_FAILURE;
        }
    }

    private class CommunicationsCryptoStateFunction {
        private Comms.ClientResponseToFailure clientResponse;

        public CommunicationsCryptoStateFunction(Comms.ClientResponseToFailure clientResponse) {
            this.clientResponse = clientResponse;
        }

        public void invoke() throws InvalidProtocolBufferException {
            CommunicationsCryptoState.this.processEncryptionTestMsg(this.clientResponse.getRsaTest());
            CommunicationsCryptoState.this.state = State.FAILURE;
        }
    }

    static enum State {
        INITIAL,
        SEND_SERVER_KEY,
        WAIT_SERVER_KEY,
        SEND_CLIENT_IDENTITY,
        WAIT_CLIENT_IDENTITY,
        SEND_SERVER_IDENTITY_AND_TEST,
        WAIT_SERVER_IDENTITY_AND_TEST,
        SEND_CLIENT_RESPONSE_AND_TEST,
        WAIT_CLIENT_RESPONSE_AND_TEST,
        SEND_SERVER_RESPONSE_AND_RESULTS,
        WAIT_SERVER_RESPONSE_AND_RESULTS,
        SEND_CLIENT_RESULTS,
        WAIT_CLIENT_RESULTS,
        SEND_SERVER_RSA_FAILURE,
        SEND_CLIENT_RSA_FAILURE,
        SEND_SECOND_RESPONSE,
        SEND_SECOND_RESULTS_AND_TEST,
        WAIT_CLIENT_RESULTS_AND_TEST,
        SEND_SECOND_SUCCESS_AND_TEST,
        WAIT_SECOND_RESPONSE,
        FAILURE,
        READY;
        

        private State() {
        }
    }

}

