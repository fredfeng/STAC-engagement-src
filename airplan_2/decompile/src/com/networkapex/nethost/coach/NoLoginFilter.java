/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.nethost.coach;

import com.networkapex.nethost.Person;
import com.networkapex.nethost.PersonManager;
import com.networkapex.nethost.WebSession;
import com.networkapex.nethost.WebSessionService;
import com.networkapex.nethost.coach.AbstractHttpManager;
import com.networkapex.nethost.coach.HttpManagerResponse;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.net.URI;

public class NoLoginFilter
extends Filter {
    private final PersonManager personManager;
    private final WebSessionService webSessionService;
    private final String personId;

    public NoLoginFilter(PersonManager personManager, WebSessionService webSessionService, String personId) {
        this.personManager = personManager;
        this.webSessionService = webSessionService;
        this.personId = personId;
    }

    @Override
    public void doFilter(HttpExchange httpExchange, Filter.Chain chain) throws IOException {
        WebSession webSession = this.webSessionService.fetchSession(httpExchange);
        if (webSession == null) {
            Person person = this.personManager.getPersonByIdentity(this.personId);
            webSession = new WebSession(this.personId);
            this.webSessionService.addSession(httpExchange, webSession);
            HttpManagerResponse response = AbstractHttpManager.obtainRedirectResponse(httpExchange.getRequestURI().toString());
            response.transmitResponse(httpExchange);
        } else {
            Person person = this.personManager.getPersonByIdentity(webSession.getPersonId());
            if (person != null) {
                httpExchange.setAttribute("userId", webSession.getPersonId());
                chain.doFilter(httpExchange);
            } else {
                throw new IllegalArgumentException("No user associated with " + this.personId);
            }
        }
    }

    @Override
    public String description() {
        return "No Login Filter";
    }
}

