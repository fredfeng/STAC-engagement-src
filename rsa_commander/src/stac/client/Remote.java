/*
 * Decompiled with CFR 0_121.
 */
package stac.client;

import stac.crypto.PrivateKey;
import stac.crypto.PublicKey;

public class Remote {
    private PublicKey publicKey;

    public Remote() {
    }

    public Remote(PrivateKey senderKey) {
        this.publicKey = senderKey.toPublicKey();
    }

    public Remote(PublicKey senderKey) {
        this.publicKey = senderKey;
    }

    public PublicKey getKey() {
        return this.publicKey;
    }

    public void setKey(PublicKey key) {
        this.publicKey = key;
    }
}

