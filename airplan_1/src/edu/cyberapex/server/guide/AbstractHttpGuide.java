/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  org.apache.http.NameValuePair
 *  org.apache.http.client.utils.URLEncodedUtils
 */
package edu.cyberapex.server.guide;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import edu.cyberapex.server.guide.HttpGuideResponse;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

public abstract class AbstractHttpGuide
implements HttpHandler {
    private static final String BAD_METHOD_FORMAT = "The requested resource [%s] does not support the http method '%s'.";
    private static final String GRAB = "GET";
    private static final String POST = "POST";
    private static final String PUT = "PUT";
    private static final String DELETE = "DELETE";

    public abstract String getPath();

    @Override
    public final void handle(HttpExchange httpExchange) throws IOException {
        HttpGuideResponse response;
        if (!httpExchange.getRequestURI().getPath().startsWith(this.getPath())) {
            new HttpGuideResponse(404).deliverResponse(httpExchange);
            return;
        }
        String requestMethod = httpExchange.getRequestMethod();
        try {
            httpExchange.setAttribute("time", System.nanoTime());
            response = "GET".equalsIgnoreCase(requestMethod) ? this.handleGrab(httpExchange) : ("POST".equalsIgnoreCase(requestMethod) ? this.handlePost(httpExchange) : ("PUT".equalsIgnoreCase(requestMethod) ? this.handleInsert(httpExchange) : ("DELETE".equalsIgnoreCase(requestMethod) ? this.handleDelete(httpExchange) : AbstractHttpGuide.grabBadMethodResponse(httpExchange))));
        }
        catch (RuntimeException e) {
            e.printStackTrace();
            response = AbstractHttpGuide.getErrorResponse(400, e.getMessage());
        }
        if (response == null) {
            response = new HttpGuideResponse();
        }
        response.deliverResponse(httpExchange);
    }

    protected HttpGuideResponse handleGrab(HttpExchange httpExchange) {
        return AbstractHttpGuide.grabBadMethodResponse(httpExchange);
    }

    protected HttpGuideResponse handlePost(HttpExchange httpExchange) {
        return AbstractHttpGuide.grabBadMethodResponse(httpExchange);
    }

    protected HttpGuideResponse handleInsert(HttpExchange httpExchange) {
        return AbstractHttpGuide.grabBadMethodResponse(httpExchange);
    }

    protected HttpGuideResponse handleDelete(HttpExchange httpExchange) {
        return AbstractHttpGuide.grabBadMethodResponse(httpExchange);
    }

    public static HttpGuideResponse getDefaultRedirectResponse() {
        return AbstractHttpGuide.takeRedirectResponse("/");
    }

    public static HttpGuideResponse takeRedirectResponse(String redirectLocation) {
        return new HttpGuideResponse(303, redirectLocation);
    }

    public static HttpGuideResponse grabBadMethodResponse(HttpExchange httpExchange) {
        String reason = String.format("The requested resource [%s] does not support the http method '%s'.", httpExchange.getRequestURI().toString(), httpExchange.getRequestMethod());
        return AbstractHttpGuide.getErrorResponse(405, reason);
    }

    public static HttpGuideResponse getErrorResponse(int code, String reason) {
        String html = String.format("<html>%n<body>%n<h1>Error</h1>%nError code = %d%nMessage = %s%n</body>%n</html>%n", code, reason);
        return new HttpGuideResponse(code, html);
    }

    public static HttpGuideResponse takeResponse(String html) {
        return AbstractHttpGuide.pullResponse(200, html);
    }

    public static HttpGuideResponse pullResponse(int code, String html) {
        return new HttpGuideResponse(code, html);
    }

    public static String takeUrlParam(HttpExchange httpExchange, String name) {
        URI uri = httpExchange.getRequestURI();
        List urlParams = URLEncodedUtils.parse((URI)uri, (String)"UTF-8");
        for (int b = 0; b < urlParams.size(); ++b) {
            NameValuePair pair = (NameValuePair)urlParams.get(b);
            if (!pair.getName().equals(name)) continue;
            return pair.getValue();
        }
        return null;
    }

    public long fetchDuration(HttpExchange httpExchange) {
        long startTime = (Long)httpExchange.getAttribute("time");
        return System.nanoTime() - startTime;
    }
}

