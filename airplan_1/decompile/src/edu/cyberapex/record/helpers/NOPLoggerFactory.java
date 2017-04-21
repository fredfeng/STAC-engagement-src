/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.record.helpers;

import edu.cyberapex.record.ILoggerFactory;
import edu.cyberapex.record.Logger;
import edu.cyberapex.record.helpers.NOPLogger;

public class NOPLoggerFactory
implements ILoggerFactory {
    @Override
    public Logger getLogger(String name) {
        return NOPLogger.NOP_LOGGER;
    }
}

