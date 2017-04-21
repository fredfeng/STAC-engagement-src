/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.server.guide;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import edu.cyberapex.server.Member;
import edu.cyberapex.server.MemberOverseer;
import edu.cyberapex.server.WebSession;
import edu.cyberapex.server.WebSessionBuilder;
import edu.cyberapex.server.WebSessionService;
import edu.cyberapex.server.guide.AbstractHttpGuide;
import edu.cyberapex.server.guide.HttpGuideResponse;
import java.io.IOException;
import java.net.URI;

public class NoLoginFilter
extends Filter {
    private final MemberOverseer memberOverseer;
    private final WebSessionService webSessionService;
    private final String memberId;

    public NoLoginFilter(MemberOverseer memberOverseer, WebSessionService webSessionService, String memberId) {
        this.memberOverseer = memberOverseer;
        this.webSessionService = webSessionService;
        this.memberId = memberId;
    }

    @Override
    public void doFilter(HttpExchange httpExchange, Filter.Chain chain) throws IOException {
        WebSession webSession = this.webSessionService.fetchSession(httpExchange);
        if (webSession == null) {
            Member member = this.memberOverseer.obtainMemberByIdentity(this.memberId);
            webSession = new WebSessionBuilder().fixMemberId(this.memberId).generateWebSession();
            this.webSessionService.addSession(httpExchange, webSession);
            HttpGuideResponse response = AbstractHttpGuide.takeRedirectResponse(httpExchange.getRequestURI().toString());
            response.deliverResponse(httpExchange);
        } else {
            Member member = this.memberOverseer.obtainMemberByIdentity(webSession.grabMemberId());
            if (member != null) {
                httpExchange.setAttribute("userId", webSession.grabMemberId());
                chain.doFilter(httpExchange);
            } else {
                throw new IllegalArgumentException("No user associated with " + this.memberId);
            }
        }
    }

    @Override
    public String description() {
        return "No Login Filter";
    }
}

