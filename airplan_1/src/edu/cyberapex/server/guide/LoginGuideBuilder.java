/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.server.guide;

import edu.cyberapex.authenticate.KeyExchangeServer;
import edu.cyberapex.server.MemberOverseer;
import edu.cyberapex.server.WebSessionService;
import edu.cyberapex.server.guide.LoginGuide;

public class LoginGuideBuilder {
    private String destinationPath;
    private MemberOverseer memberOverseer;
    private WebSessionService webSessionService;
    private KeyExchangeServer keyExchangeServer;
    private String passwordKey;

    public LoginGuideBuilder fixDestinationPath(String destinationPath) {
        this.destinationPath = destinationPath;
        return this;
    }

    public LoginGuideBuilder setMemberOverseer(MemberOverseer memberOverseer) {
        this.memberOverseer = memberOverseer;
        return this;
    }

    public LoginGuideBuilder fixWebSessionService(WebSessionService webSessionService) {
        this.webSessionService = webSessionService;
        return this;
    }

    public LoginGuideBuilder assignKeyExchangeServer(KeyExchangeServer keyExchangeServer) {
        this.keyExchangeServer = keyExchangeServer;
        return this;
    }

    public LoginGuideBuilder definePasswordKey(String passwordKey) {
        this.passwordKey = passwordKey;
        return this;
    }

    public LoginGuide generateLoginGuide() {
        return new LoginGuide(this.memberOverseer, this.webSessionService, this.keyExchangeServer, this.destinationPath, this.passwordKey);
    }
}

