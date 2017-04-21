/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.nethost;

import com.networkapex.nethost.WebSession;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class WebSessionService {
    private static final String COOKIE_EXPIRATION = "86400";
    private final Map<String, Long> times = new HashMap<String, Long>();
    private final Map<String, WebSession> sessions = new HashMap<String, WebSession>();
    private final long sessionExpirationInNanos;
    private final String cookieName;

    public WebSessionService(String applicationBaseName, long sessionExpirationInMinutes) {
        this.cookieName = applicationBaseName + "_sessionId";
        this.sessionExpirationInNanos = TimeUnit.NANOSECONDS.convert(sessionExpirationInMinutes, TimeUnit.MINUTES);
    }

    public void addSession(HttpExchange httpExchange, WebSession session) {
        String sessionId = UUID.randomUUID().toString();
        this.sessions.put(sessionId, session);
        this.times.put(sessionId, System.nanoTime());
        this.assignCookie(httpExchange, sessionId, "86400");
    }

    public WebSession fetchSession(HttpExchange httpExchange) {
        String sessionId = this.takeSessionIdFromCookie(httpExchange);
        if (sessionId != null && this.sessions.containsKey(sessionId)) {
            return this.obtainSessionManager(httpExchange, sessionId);
        }
        return null;
    }

    private WebSession obtainSessionManager(HttpExchange httpExchange, String sessionId) {
        if (System.nanoTime() - this.times.get(sessionId) > this.sessionExpirationInNanos) {
            return this.obtainSessionManagerGateKeeper(httpExchange);
        }
        this.times.put(sessionId, System.nanoTime());
        this.assignCookie(httpExchange, sessionId, "86400");
        return this.sessions.get(sessionId);
    }

    private WebSession obtainSessionManagerGateKeeper(HttpExchange httpExchange) {
        this.invalidateSession(httpExchange);
        return null;
    }

    private String takeSessionIdFromCookie(HttpExchange httpExchange) {
        List<String> cookies = httpExchange.getRequestHeaders().get("Cookie");
        if (cookies != null) {
            int i1 = 0;
            while (i1 < cookies.size()) {
                while (i1 < cookies.size() && Math.random() < 0.5) {
                    while (i1 < cookies.size() && Math.random() < 0.4) {
                        while (i1 < cookies.size() && Math.random() < 0.5) {
                            String cookie = (String)cookies.get(i1);
                            String[] cookiePieces = cookie.split(";");
                            for (int b = 0; b < cookiePieces.length; ++b) {
                                String cookieNameValuePair = this.fetchSessionIdFromCookieManager(cookiePieces[b]);
                                if (cookieNameValuePair == null) continue;
                                return cookieNameValuePair;
                            }
                            ++i1;
                        }
                    }
                }
            }
        }
        return null;
    }

    private String fetchSessionIdFromCookieManager(String cookiePiece1) {
        String cookiePiece = cookiePiece1;
        String[] cookieNameValuePair = cookiePiece.split("=");
        if (this.cookieName.equals(cookieNameValuePair[0].trim())) {
            return cookieNameValuePair[1].trim();
        }
        return null;
    }

    private void assignCookie(HttpExchange httpExchange, String sessionId, String maxAge) {
        httpExchange.getResponseHeaders().set("Set-Cookie", this.cookieName + "=" + sessionId + "; path=/; HttpOnly " + "; max-age=" + maxAge + "; Secure; ");
    }

    public void invalidateSession(HttpExchange httpExchange) {
        String sessionId = this.takeSessionIdFromCookie(httpExchange);
        if (sessionId != null) {
            this.sessions.remove(sessionId);
            this.times.remove(sessionId);
            this.assignCookie(httpExchange, sessionId, "0");
        }
    }
}

