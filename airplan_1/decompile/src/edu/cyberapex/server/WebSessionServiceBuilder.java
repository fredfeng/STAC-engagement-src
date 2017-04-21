/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.server;

import edu.cyberapex.server.WebSessionService;

public class WebSessionServiceBuilder {
    private long sessionExpirationInMinutes;
    private String applicationBaseName;

    public WebSessionServiceBuilder assignSessionExpirationInMinutes(long sessionExpirationInMinutes) {
        this.sessionExpirationInMinutes = sessionExpirationInMinutes;
        return this;
    }

    public WebSessionServiceBuilder fixApplicationBaseName(String applicationBaseName) {
        this.applicationBaseName = applicationBaseName;
        return this;
    }

    public WebSessionService generateWebSessionService() {
        return new WebSessionService(this.applicationBaseName, this.sessionExpirationInMinutes);
    }
}

