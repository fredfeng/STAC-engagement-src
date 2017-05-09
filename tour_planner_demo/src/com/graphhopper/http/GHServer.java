/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.http;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.ssl.SslSelectChannelConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceFilter;
import com.graphhopper.util.CmdArgs;

import mock.servlet.MockHttpServletRequest;
import mock.servlet.MockHttpServletResponse;

public class GHServer {
    private final CmdArgs args;
    private Server server;
    private final Logger logger;

    public static void main(String[] args) throws Exception {
        CmdArgs cmdArgs = CmdArgs.read(args);
        cmdArgs = CmdArgs.readFromConfigAndMerge(cmdArgs, "config", "graphhopper.config");
        new GHServer(cmdArgs).start();
    }

    public GHServer(CmdArgs args) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.args = args;
    }

    public void start() throws Exception {
        Injector injector = Guice.createInjector(this.createModule());
        this.start(injector);
    }

    public void start(Injector injector) throws Exception {
        ResourceHandler resHandler = new ResourceHandler();
        resHandler.setDirectoriesListed(false);
        resHandler.setWelcomeFiles(new String[]{"index.html"});
        resHandler.setResourceBase(this.args.get("jetty.resourcebase", "./src/main/webapp"));
        this.server = new Server();
        ServletContextHandler servHandler = new ServletContextHandler(0);
        servHandler.setErrorHandler(new GHErrorHandler());
        servHandler.setContextPath("/");
        servHandler.addServlet(new ServletHolder(new InvalidRequestServlet()), "/*");
        FilterHolder guiceFilter = new FilterHolder(injector.getInstance(GuiceFilter.class));
        servHandler.addFilter(guiceFilter, "/*", EnumSet.allOf(DispatcherType.class));
        SslSelectChannelConnector connector0 = new SslSelectChannelConnector();
        int httpPort = this.args.getInt("jetty.port", 8989);
        String host = this.args.get("jetty.host", "");
        connector0.setPort(httpPort);
        if (!host.isEmpty()) {
            connector0.setHost(host);
        }
        SslContextFactory cf = connector0.getSslContextFactory();
        String keyStore = this.args.get("jetty.keystore", "keystore");
        String keyStorePassword = this.args.get("jetty.keystore.password", "");
        cf.setKeyStore(keyStore);
        cf.setKeyStorePassword(keyStorePassword);
        cf.setKeyManagerPassword(keyStorePassword);
        cf.setTrustStore(keyStore);
        cf.setTrustStorePassword(keyStorePassword);
        cf.setTrustAll(true);
        this.server.addConnector(connector0);
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resHandler, servHandler});
        this.server.setHandler(handlers);
        this.server.start();
        this.logger.info("Started server at HTTP " + host + ":" + httpPort);
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        new TourServlet().doGet(request, response);
    }

    protected Module createModule() {
        return new AbstractModule(){

            @Override
            protected void configure() {
                this.binder().requireExplicitBindings();
                this.install(new DefaultModule(GHServer.this.args));
                this.install(new GHServletModule(GHServer.this.args));
                this.bind(GuiceFilter.class);
            }
        };
    }

    public void stop() {
        if (this.server == null) {
            return;
        }
        try {
            this.server.stop();
        }
        catch (Exception ex) {
            this.logger.error("Cannot stop jetty", ex);
        }
    }

}

