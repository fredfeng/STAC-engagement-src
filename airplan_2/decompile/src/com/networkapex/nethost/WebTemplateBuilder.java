/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.nethost;

import com.networkapex.nethost.WebTemplate;

public class WebTemplateBuilder {
    private Class<?> loader;
    private String resourceName;

    public WebTemplateBuilder defineLoader(Class<?> loader) {
        this.loader = loader;
        return this;
    }

    public WebTemplateBuilder defineResourceName(String resourceName) {
        this.resourceName = resourceName;
        return this;
    }

    public WebTemplate generateWebTemplate() {
        return new WebTemplate(this.resourceName, this.loader);
    }
}

