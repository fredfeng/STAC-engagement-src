/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.logging.helpers;

import edu.computerapex.logging.ILoggerFactory;
import edu.computerapex.logging.Logger;
import edu.computerapex.logging.helpers.NOPLogger;

public class NOPLoggerFactory
implements ILoggerFactory {
    @Override
    public Logger obtainLogger(String name) {
        return NOPLogger.NOP_LOGGER;
    }
}

