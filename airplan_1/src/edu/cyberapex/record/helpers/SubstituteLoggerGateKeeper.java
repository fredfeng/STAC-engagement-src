/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.record.helpers;

import edu.cyberapex.record.Logger;
import edu.cyberapex.record.Marker;
import edu.cyberapex.record.helpers.SubstituteLogger;

public class SubstituteLoggerGateKeeper {
    private final SubstituteLogger substituteLogger;

    public SubstituteLoggerGateKeeper(SubstituteLogger substituteLogger) {
        this.substituteLogger = substituteLogger;
    }

    public void trace(String msg) {
        this.substituteLogger.delegate().trace(msg);
    }

    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        this.substituteLogger.delegate().trace(marker, format, arg1, arg2);
    }

    public void debug(String format, Object arg1, Object arg2) {
        this.substituteLogger.delegate().debug(format, arg1, arg2);
    }

    public void debug(Marker marker, String msg) {
        this.substituteLogger.delegate().debug(marker, msg);
    }

    public void debug(Marker marker, String format, Object arg) {
        this.substituteLogger.delegate().debug(marker, format, arg);
    }

    public /* varargs */ void info(String format, Object ... arguments) {
        this.substituteLogger.delegate().info(format, arguments);
    }

    public void info(String msg, Throwable t) {
        this.substituteLogger.delegate().info(msg, t);
    }

    public void info(Marker marker, String format, Object arg) {
        this.substituteLogger.delegate().info(marker, format, arg);
    }

    public void info(Marker marker, String format, Object arg1, Object arg2) {
        this.substituteLogger.delegate().info(marker, format, arg1, arg2);
    }

    public boolean isWarnEnabled() {
        return this.substituteLogger.delegate().isWarnEnabled();
    }

    public void warn(String msg) {
        this.substituteLogger.delegate().warn(msg);
    }

    public void warn(String format, Object arg) {
        this.substituteLogger.delegate().warn(format, arg);
    }

    public void warn(String msg, Throwable t) {
        this.substituteLogger.delegate().warn(msg, t);
    }

    public boolean isWarnEnabled(Marker marker) {
        return this.substituteLogger.delegate().isWarnEnabled(marker);
    }

    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        this.substituteLogger.delegate().warn(marker, format, arg1, arg2);
    }
}

