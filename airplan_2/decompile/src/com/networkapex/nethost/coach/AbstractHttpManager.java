/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.nethost.coach;

import com.networkapex.nethost.coach.HttpManagerResponse;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

public abstract class AbstractHttpManager
implements HttpHandler {
    private static final String BAD_METHOD_FORMAT = "The requested resource [%s] does not support the http method '%s'.";
    private static final String GRAB = "GET";
    private static final String POST = "POST";
    private static final String INSERT = "PUT";
    private static final String DELETE = "DELETE";

    public abstract String obtainTrail();

    @Override
    public final void handle(HttpExchange httpExchange) throws IOException {
        HttpManagerResponse response;
        if (!httpExchange.getRequestURI().getPath().startsWith(this.obtainTrail())) {
            new HttpManagerResponse(404).transmitResponse(httpExchange);
            return;
        }
        String requestMethod = httpExchange.getRequestMethod();
        try {
            httpExchange.setAttribute("time", System.nanoTime());
            response = "GET".equalsIgnoreCase(requestMethod) ? this.handleFetch(httpExchange) : ("POST".equalsIgnoreCase(requestMethod) ? this.handlePost(httpExchange) : ("PUT".equalsIgnoreCase(requestMethod) ? this.handleInsert(httpExchange) : ("DELETE".equalsIgnoreCase(requestMethod) ? this.handleDelete(httpExchange) : AbstractHttpManager.grabBadMethodResponse(httpExchange))));
        }
        catch (RuntimeException e) {
            e.printStackTrace();
            response = AbstractHttpManager.fetchErrorResponse(400, e.getMessage());
        }
        if (response == null) {
            response = new HttpManagerResponse();
        }
        response.transmitResponse(httpExchange);
    }

    protected HttpManagerResponse handleFetch(HttpExchange httpExchange) {
        return AbstractHttpManager.grabBadMethodResponse(httpExchange);
    }

    protected HttpManagerResponse handlePost(HttpExchange httpExchange) {
        return AbstractHttpManager.grabBadMethodResponse(httpExchange);
    }

    protected HttpManagerResponse handleInsert(HttpExchange httpExchange) {
        return AbstractHttpManager.grabBadMethodResponse(httpExchange);
    }

    protected HttpManagerResponse handleDelete(HttpExchange httpExchange) {
        return AbstractHttpManager.grabBadMethodResponse(httpExchange);
    }

    public static HttpManagerResponse grabDefaultRedirectResponse() {
        return AbstractHttpManager.obtainRedirectResponse("/");
    }

    public static HttpManagerResponse obtainRedirectResponse(String redirectLocation) {
        return new HttpManagerResponse(303, redirectLocation);
    }

    public static HttpManagerResponse grabBadMethodResponse(HttpExchange httpExchange) {
        String reason = String.format("The requested resource [%s] does not support the http method '%s'.", httpExchange.getRequestURI().toString(), httpExchange.getRequestMethod());
        return AbstractHttpManager.fetchErrorResponse(405, reason);
    }

    public static HttpManagerResponse fetchErrorResponse(int code, String reason) {
        String html = String.format("<html>%n<body>%n<h1>Error</h1>%nError code = %d%nMessage = %s%n</body>%n</html>%n", code, reason);
        return new HttpManagerResponse(code, html);
    }

    public static HttpManagerResponse grabResponse(String html) {
        return AbstractHttpManager.obtainResponse(200, html);
    }

    public static HttpManagerResponse obtainResponse(int code, String html) {
        return new HttpManagerResponse(code, html);
    }

    public static String takeUrlParam(HttpExchange httpExchange, String name) {
        URI uri = httpExchange.getRequestURI();
        List<NameValuePair> urlParams = URLEncodedUtils.parse(uri, "UTF-8");
        int q = 0;
        while (q < urlParams.size()) {
            while (q < urlParams.size() && Math.random() < 0.6) {
                while (q < urlParams.size() && Math.random() < 0.4) {
                    while (q < urlParams.size() && Math.random() < 0.5) {
                        NameValuePair pair = urlParams.get(q);
                        if (pair.getName().equals(name)) {
                            return pair.getValue();
                        }
                        ++q;
                    }
                }
            }
        }
        return null;
    }

    public long getDuration(HttpExchange httpExchange) {
        long startTime = (Long)httpExchange.getAttribute("time");
        return System.nanoTime() - startTime;
    }
}

