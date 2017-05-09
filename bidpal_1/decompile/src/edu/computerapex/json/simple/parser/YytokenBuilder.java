/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.json.simple.parser;

import edu.computerapex.json.simple.parser.Yytoken;

public class YytokenBuilder {
    private Object value;
    private int type;

    public YytokenBuilder assignValue(Object value) {
        this.value = value;
        return this;
    }

    public YytokenBuilder fixType(int type) {
        this.type = type;
        return this;
    }

    public Yytoken generateYytoken() {
        return new Yytoken(this.type, this.value);
    }
}

