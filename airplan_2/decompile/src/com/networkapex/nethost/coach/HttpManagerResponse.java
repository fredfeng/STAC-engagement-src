/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.nethost.coach;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HttpManagerResponse {
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";
    private static final int DEFAULT_BUFFER_SIZE = 4096;
    private final int code;
    private final String response;

    public HttpManagerResponse() {
        this(200);
    }

    public HttpManagerResponse(int code) {
        this(code, "");
    }

    public HttpManagerResponse(String response) {
        this(200, response);
    }

    public HttpManagerResponse(int code, String response) {
        this.code = code;
        this.response = response == null ? "" : response;
    }

    protected String fetchContentType() {
        return "text/html; charset=UTF-8";
    }

    protected void addResponseHeaders(HttpExchange httpExchange) throws IOException {
        httpExchange.getResponseHeaders().set("Cache-Control", "no-cache");
        httpExchange.getResponseHeaders().add("Cache-Control", "no-store");
        httpExchange.getResponseHeaders().set("Expires", "0");
        httpExchange.getResponseHeaders().set("Pragma", "no-cache");
        httpExchange.getResponseHeaders().set("Content-Type", this.fetchContentType());
    }

    protected byte[] fetchResponseBytes(HttpExchange httpExchange) throws IOException {
        String response;
        String string = response = this.response == null ? "" : this.response;
        if (303 == this.code && !response.isEmpty()) {
            httpExchange.getResponseHeaders().set("Location", response.trim());
            response = "";
        }
        return response.getBytes("UTF-8");
    }

    public void transmitResponse(HttpExchange httpExchange) throws IOException {
        this.addResponseHeaders(httpExchange);
        byte[] bytes = this.fetchResponseBytes(httpExchange);
        if (bytes == null) {
            bytes = new byte[]{};
        }
        httpExchange.sendResponseHeaders(this.code, bytes.length);
        httpExchange.getResponseBody().write(bytes);
        this.drain(httpExchange.getRequestBody());
        httpExchange.close();
    }

    private void drain(InputStream inputStream) throws IOException {
        int count;
        byte[] buffer = new byte[4096];
        while ((count = inputStream.read(buffer)) != -1) {
        }
    }
}

