/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.server.guide;

import com.sun.net.httpserver.HttpExchange;
import edu.cyberapex.server.WebSessionService;
import edu.cyberapex.server.guide.AbstractHttpGuide;
import edu.cyberapex.server.guide.HttpGuideResponse;

public class LogoutGuide
extends AbstractHttpGuide {
    private final WebSessionService webSessionService;
    public static final String PATH = "/logout";
    public static final String TITLE = "Logout";

    public LogoutGuide(WebSessionService webSessionService) {
        this.webSessionService = webSessionService;
    }

    @Override
    public String getPath() {
        return "/logout";
    }

    @Override
    protected HttpGuideResponse handleGrab(HttpExchange httpExchange) {
        this.webSessionService.invalidateSession(httpExchange);
        return LogoutGuide.getDefaultRedirectResponse();
    }
}

