/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.dialogs;

import edu.computerapex.dialogs.CommunicationsDeviation;
import edu.computerapex.dialogs.CommunicationsNetworkAddress;
import edu.computerapex.dialogs.CommunicationsPublicIdentity;
import edu.computerapex.json.simple.JSONObject;
import edu.computerapex.json.simple.parser.JSONRetriever;
import edu.computerapex.math.EncryptionPrivateKey;
import edu.computerapex.math.EncryptionPublicKey;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;

public class CommunicationsIdentity {
    private final String id;
    private final EncryptionPrivateKey key;
    private final CommunicationsNetworkAddress callbackAddress;

    public CommunicationsIdentity(String id, EncryptionPrivateKey key) {
        this(id, key, null);
    }

    public CommunicationsIdentity(String id, EncryptionPrivateKey key, CommunicationsNetworkAddress callbackAddress) {
        this.id = id;
        this.key = key;
        this.callbackAddress = callbackAddress;
    }

    public static CommunicationsIdentity loadFromFile(File identityFile) throws CommunicationsDeviation {
        JSONRetriever retriever = new JSONRetriever();
        try {
            JSONObject json = (JSONObject)retriever.parse(new FileReader(identityFile));
            JSONObject privateKeyJson = (JSONObject)json.get("privateKey");
            EncryptionPrivateKey privateKey = EncryptionPrivateKey.generateKeyFromJson(privateKeyJson);
            String id = (String)json.get("id");
            String callbackHost = (String)json.get("callbackHost");
            long callbackPort = (Long)json.get("callbackPort");
            return new CommunicationsIdentity(id, privateKey, new CommunicationsNetworkAddress(callbackHost, (int)callbackPort));
        }
        catch (Exception e) {
            throw new CommunicationsDeviation(e);
        }
    }

    public String toJson() {
        JSONObject json = new JSONObject();
        json.put("id", this.id);
        json.put("callbackHost", this.callbackAddress.fetchHost());
        json.put("callbackPort", this.callbackAddress.fetchPort());
        json.put("privateKey", this.key.toJSONObject());
        return json.toJSONString();
    }

    public String obtainId() {
        return this.id;
    }

    public String pullTruncatedId() {
        String tid = this.id;
        if (this.id.length() > 25) {
            tid = tid.substring(0, 25) + "...";
        }
        return tid;
    }

    public EncryptionPublicKey grabPublicKey() {
        return this.key.getPublicKey();
    }

    public EncryptionPrivateKey fetchPrivateKey() {
        return this.key;
    }

    public boolean hasCallbackAddress() {
        return this.callbackAddress != null;
    }

    public CommunicationsNetworkAddress pullCallbackAddress() {
        return this.callbackAddress;
    }

    public CommunicationsPublicIdentity takePublicIdentity() {
        return new CommunicationsPublicIdentity(this.id, this.grabPublicKey(), this.callbackAddress);
    }

    public String toString() {
        return "id: " + this.id + "\n" + this.key;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        CommunicationsIdentity identity = (CommunicationsIdentity)o;
        if (!this.id.equals(identity.id)) {
            return false;
        }
        if (!this.key.equals(identity.key)) {
            return false;
        }
        return this.callbackAddress != null ? this.callbackAddress.equals(identity.callbackAddress) : identity.callbackAddress == null;
    }

    public int hashCode() {
        int result = this.id.hashCode();
        result = 31 * result + this.key.hashCode();
        result = 31 * result + (this.callbackAddress != null ? this.callbackAddress.hashCode() : 0);
        return result;
    }
}

