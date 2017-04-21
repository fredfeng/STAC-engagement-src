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

public class LoginFilter
extends Filter {
    private final PersonManager personManager;
    private final WebSessionService webSessionService;
    private final String authenticationManagerTrail;

    public LoginFilter(PersonManager personManager, WebSessionService webSessionService, String authenticationManagerTrail) {
        this.personManager = personManager;
        this.webSessionService = webSessionService;
        this.authenticationManagerTrail = authenticationManagerTrail;
    }

    @Override
    public void doFilter(HttpExchange httpExchange, Filter.Chain chain) throws IOException {
        WebSession webSession = this.webSessionService.fetchSession(httpExchange);
        Person person = null;
        if (webSession != null) {
            person = this.personManager.getPersonByIdentity(webSession.getPersonId());
        }
        if (person != null) {
            httpExchange.setAttribute("userId", webSession.getPersonId());
            chain.doFilter(httpExchange);
        } else {
            HttpManagerResponse response = AbstractHttpManager.obtainRedirectResponse(this.authenticationManagerTrail);
            response.transmitResponse(httpExchange);
        }
    }

    @Override
    public String description() {
        return "Login Filter";
    }
}

