/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.nethost;

import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.concurrent.Executor;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManager;

public class WebServerFactory {
    public static HttpsServer createServer(int port, InputStream resourceStream, String resourcePassword) throws IOException, GeneralSecurityException {
        SSLContext sslContext = WebServerFactory.createContext(resourceStream, resourcePassword.toCharArray());
        HttpsServer server = HttpsServer.create(new InetSocketAddress(port), 0);
        server.setHttpsConfigurator(new HttpsConfigurator(sslContext){

            @Override
            public void configure(HttpsParameters params) {
                try {
                    SSLContext c = SSLContext.getDefault();
                    SSLEngine engine = c.createSSLEngine();
                    String[] suites = new String[]{"TLS_RSA_WITH_AES_128_CBC_SHA256"};
                    params.setCipherSuites(suites);
                    params.setProtocols(engine.getEnabledProtocols());
                    SSLParameters defaultSSLParameters = c.getDefaultSSLParameters();
                    params.setSSLParameters(defaultSSLParameters);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        server.setExecutor(null);
        return server;
    }

    private static SSLContext createContext(InputStream inputStream, char[] password) throws IOException, GeneralSecurityException {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(inputStream, password);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, password);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());
        return sslContext;
    }

}

