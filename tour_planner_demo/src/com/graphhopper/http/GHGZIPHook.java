/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.http;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.eclipse.jetty.servlets.GzipFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GHGZIPHook
extends GzipFilter {
    private Logger logger;

    public GHGZIPHook() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        super.doFilter(req, res, chain);
    }
}

