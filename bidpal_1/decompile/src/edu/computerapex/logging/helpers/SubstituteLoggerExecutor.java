/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.logging.helpers;

import edu.computerapex.logging.Logger;
import edu.computerapex.logging.Marker;
import edu.computerapex.logging.helpers.SubstituteLogger;

public class SubstituteLoggerExecutor {
    private final SubstituteLogger substituteLogger;

    public SubstituteLoggerExecutor(SubstituteLogger substituteLogger) {
        this.substituteLogger = substituteLogger;
    }

    public void trace(String msg) {
        this.substituteLogger.delegate().trace(msg);
    }

    public void trace(String format, Object arg1, Object arg2) {
        this.substituteLogger.delegate().trace(format, arg1, arg2);
    }

    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        this.substituteLogger.delegate().trace(marker, format, arg1, arg2);
    }

    public void debug(Marker marker, String msg) {
        this.substituteLogger.delegate().debug(marker, msg);
    }

    public void debug(Marker marker, String format, Object arg) {
        this.substituteLogger.delegate().debug(marker, format, arg);
    }

    public /* varargs */ void debug(Marker marker, String format, Object ... arguments) {
        this.substituteLogger.delegate().debug(marker, format, arguments);
    }

    public void debug(Marker marker, String msg, Throwable t) {
        this.substituteLogger.delegate().debug(marker, msg, t);
    }

    public /* varargs */ void info(String format, Object ... arguments) {
        this.substituteLogger.delegate().info(format, arguments);
    }

    public boolean isInfoEnabled(Marker marker) {
        return this.substituteLogger.delegate().isInfoEnabled(marker);
    }

    public void info(Marker marker, String msg, Throwable t) {
        this.substituteLogger.delegate().info(marker, msg, t);
    }

    public void warn(String format, Object arg1, Object arg2) {
        this.substituteLogger.delegate().warn(format, arg1, arg2);
    }

    public /* varargs */ void warn(String format, Object ... arguments) {
        this.substituteLogger.delegate().warn(format, arguments);
    }

    public boolean isWarnEnabled(Marker marker) {
        return this.substituteLogger.delegate().isWarnEnabled(marker);
    }

    public void warn(Marker marker, String msg, Throwable t) {
        this.substituteLogger.delegate().warn(marker, msg, t);
    }

    public /* varargs */ void error(String format, Object ... arguments) {
        this.substituteLogger.delegate().error(format, arguments);
    }

    public boolean isErrorEnabled(Marker marker) {
        return this.substituteLogger.delegate().isErrorEnabled(marker);
    }

    public void error(Marker marker, String format, Object arg1, Object arg2) {
        this.substituteLogger.delegate().error(marker, format, arg1, arg2);
    }

    public /* varargs */ void error(Marker marker, String format, Object ... arguments) {
        this.substituteLogger.delegate().error(marker, format, arguments);
    }
}

