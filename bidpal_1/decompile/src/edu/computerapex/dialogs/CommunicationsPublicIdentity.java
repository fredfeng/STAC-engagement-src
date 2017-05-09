/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.dialogs;

import edu.computerapex.dialogs.CommunicationsDeviation;
import edu.computerapex.dialogs.CommunicationsNetworkAddress;
import edu.computerapex.json.simple.JSONObject;
import edu.computerapex.json.simple.parser.JSONRetriever;
import edu.computerapex.json.simple.parser.ParseDeviation;
import edu.computerapex.math.EncryptionPublicKey;

public final class CommunicationsPublicIdentity
implements Comparable<CommunicationsPublicIdentity> {
    private final String id;
    private final EncryptionPublicKey publicKey;
    private final CommunicationsNetworkAddress callbackAddress;

    public CommunicationsPublicIdentity(String id, EncryptionPublicKey publicKey) {
        this(id, publicKey, null);
    }

    public CommunicationsPublicIdentity(String id, EncryptionPublicKey publicKey, CommunicationsNetworkAddress callbackAddress) {
        this.id = id;
        this.publicKey = publicKey;
        this.callbackAddress = callbackAddress;
    }

    public static CommunicationsPublicIdentity fromJson(String jsonString) throws CommunicationsDeviation {
        JSONRetriever retriever = new JSONRetriever();
        try {
            return CommunicationsPublicIdentity.fromJson((JSONObject)retriever.parse(jsonString));
        }
        catch (ParseDeviation e) {
            throw new CommunicationsDeviation(e);
        }
    }

    public static CommunicationsPublicIdentity fromJson(JSONObject json) {
        String id = (String)json.get("id");
        String callbackHost = (String)json.get("callbackHost");
        long callbackPort = (Long)json.get("callbackPort");
        EncryptionPublicKey publicKey = EncryptionPublicKey.fromJson((JSONObject)json.get("publicKey"));
        return new CommunicationsPublicIdentity(id, publicKey, new CommunicationsNetworkAddress(callbackHost, (int)callbackPort));
    }

    public String takeId() {
        return this.id;
    }

    public String obtainTruncatedId() {
        String tid = this.id;
        if (this.id.length() > 25) {
            tid = tid.substring(0, 25) + "...";
        }
        return tid;
    }

    public EncryptionPublicKey getPublicKey() {
        return this.publicKey;
    }

    public CommunicationsNetworkAddress fetchCallbackAddress() {
        return this.callbackAddress;
    }

    public boolean hasCallbackAddress() {
        return this.callbackAddress != null;
    }

    public String toString() {
        return "id: " + this.id + "\n" + this.publicKey;
    }

    public String toJson() {
        return this.toJSONObject().toJSONString();
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("id", this.id);
        json.put("callbackHost", this.callbackAddress.fetchHost());
        json.put("callbackPort", this.callbackAddress.fetchPort());
        json.put("publicKey", this.publicKey.toJSONObject());
        return json;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        CommunicationsPublicIdentity that = (CommunicationsPublicIdentity)o;
        if (!this.id.equals(that.id)) {
            return false;
        }
        if (!this.publicKey.equals(that.publicKey)) {
            return false;
        }
        return this.callbackAddress != null ? this.callbackAddress.equals(that.callbackAddress) : that.callbackAddress == null;
    }

    public int hashCode() {
        int result = this.id.hashCode();
        result = 31 * result + this.publicKey.hashCode();
        result = 31 * result + (this.callbackAddress != null ? this.callbackAddress.hashCode() : 0);
        return result;
    }

    public String toVerboseString() {
        String str = this.id + ":" + this.publicKey.toString() + ": ";
        str = this.callbackAddress != null ? str + this.callbackAddress : str + "NO_CALLBACK";
        return str;
    }

    @Override
    public int compareTo(CommunicationsPublicIdentity other) {
        return this.toVerboseString().compareTo(other.toVerboseString());
    }
}

