/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.slf4j.helpers;

import com.networkapex.slf4j.Logger;
import com.networkapex.slf4j.LoggerFactory;
import java.io.ObjectStreamException;
import java.io.Serializable;

abstract class NamedLoggerBase
implements Logger,
Serializable {
    private static final long serialVersionUID = 7535258609338176893L;
    protected String name;

    NamedLoggerBase() {
    }

    @Override
    public String fetchName() {
        return this.name;
    }

    protected Object readResolve() throws ObjectStreamException {
        return LoggerFactory.grabLogger(this.fetchName());
    }
}

