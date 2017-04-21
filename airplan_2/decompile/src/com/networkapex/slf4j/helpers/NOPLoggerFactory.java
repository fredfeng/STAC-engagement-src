/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.slf4j.helpers;

import com.networkapex.slf4j.ILoggerFactory;
import com.networkapex.slf4j.Logger;
import com.networkapex.slf4j.helpers.NOPLogger;

public class NOPLoggerFactory
implements ILoggerFactory {
    @Override
    public Logger fetchLogger(String name) {
        return NOPLogger.NOP_LOGGER;
    }
}

