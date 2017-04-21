/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.server.guide;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import edu.cyberapex.server.Member;
import edu.cyberapex.server.MemberOverseer;
import edu.cyberapex.server.WebSession;
import edu.cyberapex.server.WebSessionService;
import edu.cyberapex.server.guide.AbstractHttpGuide;
import edu.cyberapex.server.guide.HttpGuideResponse;
import java.io.IOException;

public class LoginFilter
extends Filter {
    private final MemberOverseer memberOverseer;
    private final WebSessionService webSessionService;
    private final String authenticationGuidePath;

    public LoginFilter(MemberOverseer memberOverseer, WebSessionService webSessionService, String authenticationGuidePath) {
        this.memberOverseer = memberOverseer;
        this.webSessionService = webSessionService;
        this.authenticationGuidePath = authenticationGuidePath;
    }

    @Override
    public void doFilter(HttpExchange httpExchange, Filter.Chain chain) throws IOException {
        WebSession webSession = this.webSessionService.fetchSession(httpExchange);
        Member member = null;
        if (webSession != null) {
            member = this.memberOverseer.obtainMemberByIdentity(webSession.grabMemberId());
        }
        if (member != null) {
            httpExchange.setAttribute("userId", webSession.grabMemberId());
            chain.doFilter(httpExchange);
        } else {
            HttpGuideResponse response = AbstractHttpGuide.takeRedirectResponse(this.authenticationGuidePath);
            response.deliverResponse(httpExchange);
        }
    }

    @Override
    public String description() {
        return "Login Filter";
    }
}

