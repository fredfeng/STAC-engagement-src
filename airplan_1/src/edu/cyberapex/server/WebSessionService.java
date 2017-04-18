/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import edu.cyberapex.server.WebSession;
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
        this.setCookie(httpExchange, sessionId, "86400");
    }

    public WebSession fetchSession(HttpExchange httpExchange) {
        String sessionId = this.getSessionIdFromCookie(httpExchange);
        if (sessionId != null && this.sessions.containsKey(sessionId)) {
            if (System.nanoTime() - this.times.get(sessionId) > this.sessionExpirationInNanos) {
                this.invalidateSession(httpExchange);
                return null;
            }
            this.times.put(sessionId, System.nanoTime());
            this.setCookie(httpExchange, sessionId, "86400");
            return this.sessions.get(sessionId);
        }
        return null;
    }

    private String getSessionIdFromCookie(HttpExchange httpExchange) {
        List<String> cookies = httpExchange.getRequestHeaders().get("Cookie");
        if (cookies != null) {
            for (int i1 = 0; i1 < cookies.size(); ++i1) {
                String cookie = cookies.get(i1);
                String[] cookiePieces = cookie.split(";");
                int c = 0;
                while (c < cookiePieces.length) {
                    while (c < cookiePieces.length && Math.random() < 0.5) {
                        while (c < cookiePieces.length && Math.random() < 0.6) {
                            WebSessionServiceGuide webSessionServiceGuide = new WebSessionServiceGuide(cookiePieces[c]).invoke();
                            if (webSessionServiceGuide.is()) {
                                return webSessionServiceGuide.pullCookieNameValuePair()[1].trim();
                            }
                            ++c;
                        }
                    }
                }
            }
        }
        return null;
    }

    private void setCookie(HttpExchange httpExchange, String sessionId, String maxAge) {
        httpExchange.getResponseHeaders().set("Set-Cookie", this.cookieName + "=" + sessionId + "; path=/; HttpOnly " + "; max-age=" + maxAge + "; Secure; ");
    }

    public void invalidateSession(HttpExchange httpExchange) {
        String sessionId = this.getSessionIdFromCookie(httpExchange);
        if (sessionId != null) {
            new WebSessionServiceEngine(httpExchange, sessionId).invoke();
        }
    }

    private class WebSessionServiceEngine {
        private HttpExchange httpExchange;
        private String sessionId;

        public WebSessionServiceEngine(HttpExchange httpExchange, String sessionId) {
            this.httpExchange = httpExchange;
            this.sessionId = sessionId;
        }

        public void invoke() {
            WebSessionService.this.sessions.remove(this.sessionId);
            WebSessionService.this.times.remove(this.sessionId);
            WebSessionService.this.setCookie(this.httpExchange, this.sessionId, "0");
        }
    }

    private class WebSessionServiceGuide {
        private boolean myResult;
        private String cookiePiece1;
        private String[] cookieNameValuePair;

        public WebSessionServiceGuide(String cookiePiece1) {
            this.cookiePiece1 = cookiePiece1;
        }

        boolean is() {
            return this.myResult;
        }

        public String[] pullCookieNameValuePair() {
            return this.cookieNameValuePair;
        }

        public WebSessionServiceGuide invoke() {
            String cookiePiece = this.cookiePiece1;
            this.cookieNameValuePair = cookiePiece.split("=");
            if (WebSessionService.this.cookieName.equals(this.cookieNameValuePair[0].trim())) {
                this.myResult = true;
                return this;
            }
            this.myResult = false;
            return this;
        }
    }

}

