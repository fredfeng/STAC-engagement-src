/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 */
package edu.cyberapex.server.guide;

import com.sun.net.httpserver.HttpExchange;
import edu.cyberapex.DESHelper;
import edu.cyberapex.authenticate.KeyExchangeServer;
import edu.cyberapex.server.Member;
import edu.cyberapex.server.MemberOverseer;
import edu.cyberapex.server.WebSession;
import edu.cyberapex.server.WebSessionBuilder;
import edu.cyberapex.server.WebSessionService;
import edu.cyberapex.server.WebTemplate;
import edu.cyberapex.server.guide.AbstractHttpGuide;
import edu.cyberapex.server.guide.HttpGuideResponse;
import edu.cyberapex.server.guide.MultipartHelper;
import edu.cyberapex.template.TemplateEngine;
import java.math.BigInteger;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

public class LoginGuide
extends AbstractHttpGuide {
    private static final String PATH = "/login";
    private static final String TITLE = "Login";
    private static final String USERNAME_FIELD = "username";
    private static final String PASSWORD_FIELD = "password";
    private static final String AUTH_LOGIN_TEMPLATE_FILE = "logintemplate.html";
    private static final String LOGIN_TEMPLATE_FILE = "simplelogintemplate.html";
    private final MemberOverseer memberOverseer;
    private final WebSessionService webSessionService;
    private final KeyExchangeServer keyExchangeServer;
    private final WebTemplate template;
    private final String destinationPath;
    private final String passwordKey;

    public LoginGuide(MemberOverseer memberOverseer, WebSessionService webSessionService, KeyExchangeServer keyExchangeServer, String destinationPath, String passwordKey) {
        this.memberOverseer = Objects.requireNonNull(memberOverseer, "UserManager must be specified");
        this.webSessionService = Objects.requireNonNull(webSessionService, "WebSessionService must be specified");
        this.keyExchangeServer = keyExchangeServer;
        this.template = new WebTemplate(keyExchangeServer != null ? "logintemplate.html" : "simplelogintemplate.html", this.getClass());
        this.destinationPath = destinationPath;
        this.passwordKey = passwordKey;
    }

    @Override
    public String getPath() {
        return "/login";
    }

    @Override
    protected HttpGuideResponse handleGrab(HttpExchange httpExchange) {
        String path = httpExchange.getRequestURI().getPath();
        if (path.startsWith(this.getPath()) && (path = path.substring(this.getPath().length())).length() > 0 && path.startsWith("/")) {
            path = path.substring(1);
        }
        TemplateEngine templateEngine = this.template.getEngine();
        HashMap<String, String> templateMap = new HashMap<String, String>();
        templateMap.put("title", "Login");
        templateMap.put("path", this.getPath());
        if (this.keyExchangeServer != null && path.length() > 0 && path.length() < 10000) {
            BigInteger membersPublicKey;
            try {
                membersPublicKey = path.startsWith("0x") ? new BigInteger(path.substring(2), 16) : new BigInteger(path);
            }
            catch (NumberFormatException e) {
                throw new IllegalArgumentException("Error: key must be hexadecimal or decimal");
            }
            BigInteger masterSecret = this.keyExchangeServer.generateMasterSecret(membersPublicKey);
            templateMap.put("masterSecret", masterSecret.toString());
        } else {
            templateMap.put("masterSecret", "Null");
        }
        String suppressTimeStamp = LoginGuide.takeUrlParam(httpExchange, "suppressTimestamp");
        if (StringUtils.isBlank((CharSequence)suppressTimeStamp) || !suppressTimeStamp.equalsIgnoreCase("true")) {
            templateMap.put("duration", String.valueOf(this.fetchDuration(httpExchange)));
            templateMap.put("timestamp", new Date().toString());
        }
        return LoginGuide.takeResponse(templateEngine.replaceTags(templateMap));
    }

    @Override
    protected HttpGuideResponse handlePost(HttpExchange httpExchange) {
        HashSet<String> fieldNames = new HashSet<String>();
        fieldNames.add("username");
        fieldNames.add("password");
        Map<String, List<String>> loginCredentials = MultipartHelper.fetchMultipartValues(httpExchange, fieldNames);
        List<String> usernames = loginCredentials.get("username");
        List<String> passwords = loginCredentials.get("password");
        if (usernames != null && usernames.size() == 1 && passwords != null && passwords.size() == 1) {
            Member currentMember;
            String password;
            String username = usernames.get(0);
            String encryptedPw = password = passwords.get(0);
            if (this.passwordKey != null) {
                encryptedPw = DESHelper.obtainEncryptedString(password, this.passwordKey);
            }
            if ((currentMember = this.memberOverseer.pullMemberByUsername(username)) != null && currentMember.matches(username, encryptedPw)) {
                WebSession webSession = new WebSessionBuilder().fixMemberId(currentMember.fetchIdentity()).generateWebSession();
                this.webSessionService.addSession(httpExchange, webSession);
                if (this.destinationPath == null) {
                    return LoginGuide.getDefaultRedirectResponse();
                }
                return LoginGuide.takeRedirectResponse(this.destinationPath);
            }
        }
        throw new IllegalArgumentException("Invalid username or password (or both)");
    }
}

