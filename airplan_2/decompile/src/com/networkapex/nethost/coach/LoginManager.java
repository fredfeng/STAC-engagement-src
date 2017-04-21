/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.nethost.coach;

import com.networkapex.DESHelper;
import com.networkapex.authorize.KeyExchangeServer;
import com.networkapex.nethost.Person;
import com.networkapex.nethost.PersonManager;
import com.networkapex.nethost.WebSession;
import com.networkapex.nethost.WebSessionService;
import com.networkapex.nethost.WebTemplate;
import com.networkapex.nethost.WebTemplateBuilder;
import com.networkapex.nethost.coach.AbstractHttpManager;
import com.networkapex.nethost.coach.HttpManagerResponse;
import com.networkapex.nethost.coach.MultipartHelper;
import com.networkapex.template.TemplateEngine;
import com.sun.net.httpserver.HttpExchange;
import java.math.BigInteger;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

public class LoginManager
extends AbstractHttpManager {
    private static final String TRAIL = "/login";
    private static final String TITLE = "Login";
    private static final String USERNAME_FIELD = "username";
    private static final String PASSWORD_FIELD = "password";
    private static final String AUTH_LOGIN_TEMPLATE_FILE = "logintemplate.html";
    private static final String LOGIN_TEMPLATE_FILE = "simplelogintemplate.html";
    private final PersonManager personManager;
    private final WebSessionService webSessionService;
    private final KeyExchangeServer keyExchangeServer;
    private final WebTemplate template;
    private final String destinationTrail;
    private final String passwordKey;

    public LoginManager(PersonManager personManager, WebSessionService webSessionService, KeyExchangeServer keyExchangeServer, String destinationTrail, String passwordKey) {
        this.personManager = Objects.requireNonNull(personManager, "UserManager must be specified");
        this.webSessionService = Objects.requireNonNull(webSessionService, "WebSessionService must be specified");
        this.keyExchangeServer = keyExchangeServer;
        this.template = new WebTemplateBuilder().defineResourceName(keyExchangeServer != null ? "logintemplate.html" : "simplelogintemplate.html").defineLoader(this.getClass()).generateWebTemplate();
        this.destinationTrail = destinationTrail;
        this.passwordKey = passwordKey;
    }

    @Override
    public String obtainTrail() {
        return "/login";
    }

    @Override
    protected HttpManagerResponse handleFetch(HttpExchange httpExchange) {
        String trail = httpExchange.getRequestURI().getPath();
        if (trail.startsWith(this.obtainTrail()) && (trail = trail.substring(this.obtainTrail().length())).length() > 0 && trail.startsWith("/")) {
            trail = trail.substring(1);
        }
        TemplateEngine templateEngine = this.template.takeEngine();
        HashMap<String, String> templateMap = new HashMap<String, String>();
        templateMap.put("title", "Login");
        templateMap.put("path", this.obtainTrail());
        if (this.keyExchangeServer != null && trail.length() > 0 && trail.length() < 10000) {
            BigInteger personsPublicKey;
            try {
                personsPublicKey = trail.startsWith("0x") ? new BigInteger(trail.substring(2), 16) : new BigInteger(trail);
            }
            catch (NumberFormatException e) {
                throw new IllegalArgumentException("Error: key must be hexadecimal or decimal");
            }
            BigInteger masterSecret = this.keyExchangeServer.generateMasterSecret(personsPublicKey);
            templateMap.put("masterSecret", masterSecret.toString());
        } else {
            this.handleTakeGuide(templateMap);
        }
        String suppressTimeStamp = LoginManager.takeUrlParam(httpExchange, "suppressTimestamp");
        if (StringUtils.isBlank(suppressTimeStamp) || !suppressTimeStamp.equalsIgnoreCase("true")) {
            this.handleFetchSupervisor(httpExchange, templateMap);
        }
        return LoginManager.grabResponse(templateEngine.replaceTags(templateMap));
    }

    private void handleFetchSupervisor(HttpExchange httpExchange, Map<String, String> templateMap) {
        templateMap.put("duration", String.valueOf(this.getDuration(httpExchange)));
        templateMap.put("timestamp", new Date().toString());
    }

    private void handleTakeGuide(Map<String, String> templateMap) {
        new LoginManagerCoordinator(templateMap).invoke();
    }

    @Override
    protected HttpManagerResponse handlePost(HttpExchange httpExchange) {
        HashSet<String> fieldNames = new HashSet<String>();
        fieldNames.add("username");
        fieldNames.add("password");
        Map<String, List<String>> loginCredentials = MultipartHelper.getMultipartValues(httpExchange, fieldNames);
        List<String> usernames = loginCredentials.get("username");
        List<String> passwords = loginCredentials.get("password");
        if (usernames != null && usernames.size() == 1 && passwords != null && passwords.size() == 1) {
            String password;
            Person currentPerson;
            String username = usernames.get(0);
            String encryptedPw = password = passwords.get(0);
            if (this.passwordKey != null) {
                encryptedPw = DESHelper.obtainEncryptedString(password, this.passwordKey);
            }
            if ((currentPerson = this.personManager.grabPersonByUsername(username)) != null && currentPerson.matches(username, encryptedPw)) {
                WebSession webSession = new WebSession(currentPerson.grabIdentity());
                this.webSessionService.addSession(httpExchange, webSession);
                if (this.destinationTrail == null) {
                    return LoginManager.grabDefaultRedirectResponse();
                }
                return LoginManager.obtainRedirectResponse(this.destinationTrail);
            }
        }
        throw new IllegalArgumentException("Invalid username or password (or both)");
    }

    private class LoginManagerCoordinator {
        private Map<String, String> templateMap;

        public LoginManagerCoordinator(Map<String, String> templateMap) {
            this.templateMap = templateMap;
        }

        public void invoke() {
            this.templateMap.put("masterSecret", "Null");
        }
    }

}

