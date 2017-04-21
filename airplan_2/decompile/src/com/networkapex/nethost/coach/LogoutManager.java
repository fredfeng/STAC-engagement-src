/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.nethost.coach;

import com.networkapex.nethost.WebSessionService;
import com.networkapex.nethost.coach.AbstractHttpManager;
import com.networkapex.nethost.coach.HttpManagerResponse;
import com.sun.net.httpserver.HttpExchange;

public class LogoutManager
extends AbstractHttpManager {
    private final WebSessionService webSessionService;
    public static final String TRAIL = "/logout";
    public static final String TITLE = "Logout";

    public LogoutManager(WebSessionService webSessionService) {
        this.webSessionService = webSessionService;
    }

    @Override
    public String obtainTrail() {
        return "/logout";
    }

    @Override
    protected HttpManagerResponse handleFetch(HttpExchange httpExchange) {
        this.webSessionService.invalidateSession(httpExchange);
        return LogoutManager.grabDefaultRedirectResponse();
    }
}

