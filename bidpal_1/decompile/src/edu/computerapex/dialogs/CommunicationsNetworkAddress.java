/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.dialogs;

public final class CommunicationsNetworkAddress {
    private final String host;
    private final int port;

    public CommunicationsNetworkAddress(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public int fetchPort() {
        return this.port;
    }

    public String fetchHost() {
        return this.host;
    }

    public String toString() {
        return this.host + ":" + this.port;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        CommunicationsNetworkAddress that = (CommunicationsNetworkAddress)o;
        if (this.port != that.port) {
            return false;
        }
        return this.host != null ? this.host.equals(that.host) : that.host == null;
    }

    public int hashCode() {
        int result = this.host != null ? this.host.hashCode() : 0;
        result = 31 * result + this.port;
        return result;
    }
}

