/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  org.apache.commons.io.FileUtils
 */
package edu.cyberapex.server;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsServer;
import edu.cyberapex.authenticate.KeyExchangeServer;
import edu.cyberapex.server.MemberOverseer;
import edu.cyberapex.server.WebServerFactory;
import edu.cyberapex.server.WebSessionService;
import edu.cyberapex.server.WebSessionServiceBuilder;
import edu.cyberapex.server.guide.AbstractHttpGuide;
import edu.cyberapex.server.guide.AuthenticationGuide;
import edu.cyberapex.server.guide.LoginFilter;
import edu.cyberapex.server.guide.LoginGuide;
import edu.cyberapex.server.guide.LoginGuideBuilder;
import edu.cyberapex.server.guide.LogoutGuide;
import edu.cyberapex.server.guide.LogoutGuideBuilder;
import edu.cyberapex.server.guide.NoLoginFilter;
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

    public WebServer(String appBaseName, int port, InputStream resourceStream, String resourcePassword, File passwordKeyFile, File authenticateKeyFile) throws IOException, GeneralSecurityException {
        this.httpsServer = WebServerFactory.createServer(port, resourceStream, resourcePassword);
        this.webSessionService = new WebSessionServiceBuilder().fixApplicationBaseName(appBaseName).assignSessionExpirationInMinutes(1440).generateWebSessionService();
        String string = this.passwordKey = passwordKeyFile == null ? null : FileUtils.readFileToString((File)passwordKeyFile);
        if (authenticateKeyFile != null) {
            String authenticateKey = FileUtils.readFileToString((File)authenticateKeyFile);
            this.keyExchangeServer = new KeyExchangeServer(authenticateKey);
        } else {
            this.keyExchangeServer = null;
        }
    }

    public HttpsServer pullServer() {
        return this.httpsServer;
    }

    public void addDefaultAuthenticateGuides(MemberOverseer memberOverseer, String memberId) {
        this.loginFilter = new NoLoginFilter(memberOverseer, this.webSessionService, memberId);
    }

    public void addAuthenticateGuides(MemberOverseer memberOverseer, String loginDestinationPath) {
        LoginGuide loginGuide = new LoginGuideBuilder().setMemberOverseer(memberOverseer).fixWebSessionService(this.webSessionService).assignKeyExchangeServer(this.keyExchangeServer).fixDestinationPath(loginDestinationPath).definePasswordKey(this.passwordKey).generateLoginGuide();
        this.generateContext(loginGuide, false);
        LogoutGuide logoutGuide = new LogoutGuideBuilder().fixWebSessionService(this.webSessionService).generateLogoutGuide();
        this.generateContext(logoutGuide, false);
        String loginFilterPath = loginGuide.getPath();
        if (this.keyExchangeServer != null) {
            AuthenticationGuide authenticationGuide = new AuthenticationGuide(loginGuide.getPath());
            this.generateContext(authenticationGuide, false);
            loginFilterPath = authenticationGuide.getPath();
        }
        this.loginFilter = new LoginFilter(memberOverseer, this.webSessionService, loginFilterPath);
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

    public HttpContext generateContext(AbstractHttpGuide guide, boolean requireAuthenticate) {
        HttpContext context = this.httpsServer.createContext(guide.getPath(), guide);
        if (requireAuthenticate) {
            context.getFilters().add(this.loginFilter);
        }
        return context;
    }
}

