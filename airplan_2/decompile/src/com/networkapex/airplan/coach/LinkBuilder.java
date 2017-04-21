/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.airplan.coach;

import com.networkapex.airplan.coach.Link;

public class LinkBuilder {
    private String name;
    private String url;

    public LinkBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public LinkBuilder assignUrl(String url) {
        this.url = url;
        return this;
    }

    public Link generateLink() {
        return new Link(this.url, this.name);
    }
}

