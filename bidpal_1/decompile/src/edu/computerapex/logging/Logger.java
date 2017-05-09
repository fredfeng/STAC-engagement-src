/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.logging;

import edu.computerapex.logging.Marker;

public interface Logger {
    public static final String ROOT_LOGGER_NAME = "ROOT";

    public String takeName();

    public boolean isTraceEnabled();

    public void trace(String var1);

    public void trace(String var1, Object var2);

    public void trace(String var1, Object var2, Object var3);

    public /* varargs */ void trace(String var1, Object ... var2);

    public void trace(String var1, Throwable var2);

    public boolean isTraceEnabled(Marker var1);

    public void trace(Marker var1, String var2);

    public void trace(Marker var1, String var2, Object var3);

    public void trace(Marker var1, String var2, Object var3, Object var4);

    public /* varargs */ void trace(Marker var1, String var2, Object ... var3);

    public void trace(Marker var1, String var2, Throwable var3);

    public boolean isDebugEnabled();

    public void debug(String var1);

    public void debug(String var1, Object var2);

    public void debug(String var1, Object var2, Object var3);

    public /* varargs */ void debug(String var1, Object ... var2);

    public void debug(String var1, Throwable var2);

    public boolean isDebugEnabled(Marker var1);

    public void debug(Marker var1, String var2);

    public void debug(Marker var1, String var2, Object var3);

    public void debug(Marker var1, String var2, Object var3, Object var4);

    public /* varargs */ void debug(Marker var1, String var2, Object ... var3);

    public void debug(Marker var1, String var2, Throwable var3);

    public boolean isInfoEnabled();

    public void info(String var1);

    public void info(String var1, Object var2);

    public void info(String var1, Object var2, Object var3);

    public /* varargs */ void info(String var1, Object ... var2);

    public void info(String var1, Throwable var2);

    public boolean isInfoEnabled(Marker var1);

    public void info(Marker var1, String var2);

    public void info(Marker var1, String var2, Object var3);

    public void info(Marker var1, String var2, Object var3, Object var4);

    public /* varargs */ void info(Marker var1, String var2, Object ... var3);

    public void info(Marker var1, String var2, Throwable var3);

    public boolean isWarnEnabled();

    public void warn(String var1);

    public void warn(String var1, Object var2);

    public /* varargs */ void warn(String var1, Object ... var2);

    public void warn(String var1, Object var2, Object var3);

    public void warn(String var1, Throwable var2);

    public boolean isWarnEnabled(Marker var1);

    public void warn(Marker var1, String var2);

    public void warn(Marker var1, String var2, Object var3);

    public void warn(Marker var1, String var2, Object var3, Object var4);

    public /* varargs */ void warn(Marker var1, String var2, Object ... var3);

    public void warn(Marker var1, String var2, Throwable var3);

    public boolean isErrorEnabled();

    public void error(String var1);

    public void error(String var1, Object var2);

    public void error(String var1, Object var2, Object var3);

    public /* varargs */ void error(String var1, Object ... var2);

    public void error(String var1, Throwable var2);

    public boolean isErrorEnabled(Marker var1);

    public void error(Marker var1, String var2);

    public void error(Marker var1, String var2, Object var3);

    public void error(Marker var1, String var2, Object var3, Object var4);

    public /* varargs */ void error(Marker var1, String var2, Object ... var3);

    public void error(Marker var1, String var2, Throwable var3);
}

