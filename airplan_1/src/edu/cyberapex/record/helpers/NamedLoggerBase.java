/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.record.helpers;

import edu.cyberapex.record.Logger;
import edu.cyberapex.record.LoggerFactory;
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
        return LoggerFactory.pullLogger(this.fetchName());
    }
}

