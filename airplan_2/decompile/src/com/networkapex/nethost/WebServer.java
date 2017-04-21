/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.nethost;

import com.networkapex.authorize.KeyExchangeServer;
import com.networkapex.nethost.PersonManager;
import com.networkapex.nethost.WebServerFactory;
import com.networkapex.nethost.WebSessionService;
import com.networkapex.nethost.coach.AbstractHttpManager;
import com.networkapex.nethost.coach.AuthenticationManager;
import com.networkapex.nethost.coach.AuthenticationManagerBuilder;
import com.networkapex.nethost.coach.LoginFilter;
import com.networkapex.nethost.coach.LoginManager;
import com.networkapex.nethost.coach.LogoutManager;
import com.networkapex.nethost.coach.NoLoginFilter;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsServer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.List;
import org.apache.commons.io.FileUtils;

public class WebServer {
    private static final long DEFAULT_SESSION_TIMEOUT_IN_MINUTES = 1440;
    private final HttpsServer httpsServer;
    private final WebSessionService webSessionService;
    private final KeyExchangeServer keyExchangeServer;
    private final String passwordKey;
    private static final int SECONDS_TO_WAIT_TO_CLOSE = 0;
    private Filter loginFilter;

    public WebServer(String appBaseName, int port, InputStream resourceStream, String resourcePassword, File passwordKeyFile) throws IOException, GeneralSecurityException {
        this(appBaseName, port, resourceStream, resourcePassword, passwordKeyFile, null);
    }

    public WebServer(String appBaseName, int port, InputStream resourceStream, String resourcePassword, File passwordKeyFile, File authorizeKeyFile) throws IOException, GeneralSecurityException {
        this.httpsServer = WebServerFactory.createServer(port, resourceStream, resourcePassword);
        this.webSessionService = new WebSessionService(appBaseName, 1440);
        String string = this.passwordKey = passwordKeyFile == null ? null : FileUtils.readFileToString(passwordKeyFile);
        if (authorizeKeyFile != null) {
            String authorizeKey = FileUtils.readFileToString(authorizeKeyFile);
            this.keyExchangeServer = new KeyExchangeServer(authorizeKey);
        } else {
            this.keyExchangeServer = null;
        }
    }

    public HttpsServer getServer() {
        return this.httpsServer;
    }

    public void addDefaultAuthorizeManagers(PersonManager personManager, String personId) {
        this.loginFilter = new NoLoginFilter(personManager, this.webSessionService, personId);
    }

    public void addAuthorizeManagers(PersonManager personManager, String loginDestinationTrail) {
        LoginManager loginManager = new LoginManager(personManager, this.webSessionService, this.keyExchangeServer, loginDestinationTrail, this.passwordKey);
        this.generateContext(loginManager, false);
        LogoutManager logoutManager = new LogoutManager(this.webSessionService);
        this.generateContext(logoutManager, false);
        String loginFilterTrail = loginManager.obtainTrail();
        if (this.keyExchangeServer != null) {
            AuthenticationManager authenticationManager = new AuthenticationManagerBuilder().defineRedirectResponseTrail(loginManager.obtainTrail()).generateAuthenticationManager();
            this.generateContext(authenticationManager, false);
            loginFilterTrail = authenticationManager.obtainTrail();
        }
        this.loginFilter = new LoginFilter(personManager, this.webSessionService, loginFilterTrail);
    }

    public WebSessionService getWebSessionService() {
        return this.webSessionService;
    }

    public void stop() {
        this.httpsServer.stop(0);
    }

    public void stop(int secondsToWaitToClose) {
        this.httpsServer.stop(secondsToWaitToClose);
    }

    public void start() {
        this.httpsServer.start();
    }

    public HttpContext generateContext(AbstractHttpManager manager, boolean requireAuthorize) {
        HttpContext context = this.httpsServer.createContext(manager.obtainTrail(), manager);
        if (requireAuthorize) {
            context.getFilters().add(this.loginFilter);
        }
        return context;
    }
}

