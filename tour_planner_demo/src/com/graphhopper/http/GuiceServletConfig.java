/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.http;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceServletContextListener;
import com.graphhopper.http.DefaultModule;
import com.graphhopper.http.GHServletModule;
import com.graphhopper.util.CmdArgs;

public class GuiceServletConfig
extends GuiceServletContextListener {
    private final CmdArgs args;

    public GuiceServletConfig() {
        try {
            this.args = CmdArgs.readFromConfig("config.properties", "graphhopper.config");
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(this.createDefaultModule(), this.createServletModule());
    }

    protected Module createDefaultModule() {
        return new DefaultModule(this.args);
    }

    protected Module createServletModule() {
        return new GHServletModule(this.args);
    }
}

