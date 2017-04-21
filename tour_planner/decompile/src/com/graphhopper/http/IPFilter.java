/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.http;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IPFilter
implements Filter {
    private final Logger logger;
    private final Set<String> whites;
    private final Set<String> blacks;

    public IPFilter(String whiteList, String blackList) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.whites = this.createSet(whiteList.split(","));
        this.blacks = this.createSet(blackList.split(","));
        if (!this.whites.isEmpty()) {
            this.logger.debug("whitelist:" + this.whites);
        }
        if (!blackList.isEmpty()) {
            this.logger.debug("blacklist:" + this.blacks);
        }
        if (!this.blacks.isEmpty() && !this.whites.isEmpty()) {
            throw new IllegalArgumentException("blacklist and whitelist at the same time?");
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String ip = request.getRemoteAddr();
        if (this.accept(ip)) {
            chain.doFilter(request, response);
        } else {
            this.logger.warn("Did not accept IP " + ip);
            ((HttpServletResponse)response).sendError(403);
        }
    }

    public boolean accept(String ip) {
        if (this.whites.isEmpty() && this.blacks.isEmpty()) {
            return true;
        }
        if (!this.whites.isEmpty()) {
            for (String w : this.whites) {
                if (!this.simpleMatch(ip, w)) continue;
                return true;
            }
            return false;
        }
        if (this.blacks.isEmpty()) {
            throw new IllegalStateException("cannot happen");
        }
        for (String b : this.blacks) {
            if (!this.simpleMatch(ip, b)) continue;
            return false;
        }
        return true;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    private Set<String> createSet(String[] split) {
        HashSet<String> set = new HashSet<String>(split.length);
        for (String str : split) {
            if ((str = str.trim()).isEmpty()) continue;
            set.add(str);
        }
        return set;
    }

    public boolean simpleMatch(String ip, String pattern) {
        String[] ipParts;
        for (String ipPart : ipParts = pattern.split("\\*")) {
            int idx = ip.indexOf(ipPart);
            if (idx == -1) {
                return false;
            }
            ip = ip.substring(idx + ipPart.length());
        }
        return true;
    }
}

