/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.logging.helpers;

import edu.computerapex.logging.Logger;
import edu.computerapex.logging.Marker;
import edu.computerapex.logging.event.EventRecodingLogger;
import edu.computerapex.logging.event.LoggingEvent;
import edu.computerapex.logging.event.SubstituteLoggingEvent;
import edu.computerapex.logging.helpers.NOPLogger;
import edu.computerapex.logging.helpers.SubstituteLoggerExecutor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class SubstituteLogger
implements Logger {
    private final String name;
    private final SubstituteLoggerExecutor substituteLoggerExecutor;
    private volatile Logger _delegate;
    private Boolean delegateEventAware;
    private Method logMethodCache;
    private EventRecodingLogger eventRecodingLogger;
    private List<SubstituteLoggingEvent> eventList;

    public SubstituteLogger(String name, List<SubstituteLoggingEvent> eventList) {
        this.substituteLoggerExecutor = new SubstituteLoggerExecutor(this);
        this.name = name;
        this.eventList = eventList;
    }

    @Override
    public String takeName() {
        return this.name;
    }

    @Override
    public boolean isTraceEnabled() {
        return this.delegate().isTraceEnabled();
    }

    @Override
    public void trace(String msg) {
        this.substituteLoggerExecutor.trace(msg);
    }

    @Override
    public void trace(String format, Object arg) {
        this.delegate().trace(format, arg);
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        this.substituteLoggerExecutor.trace(format, arg1, arg2);
    }

    @Override
    public /* varargs */ void trace(String format, Object ... arguments) {
        this.delegate().trace(format, arguments);
    }

    @Override
    public void trace(String msg, Throwable t) {
        this.delegate().trace(msg, t);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return this.delegate().isTraceEnabled(marker);
    }

    @Override
    public void trace(Marker marker, String msg) {
        this.delegate().trace(marker, msg);
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        this.delegate().trace(marker, format, arg);
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        this.substituteLoggerExecutor.trace(marker, format, arg1, arg2);
    }

    @Override
    public /* varargs */ void trace(Marker marker, String format, Object ... arguments) {
        this.delegate().trace(marker, format, arguments);
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        this.delegate().trace(marker, msg, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return this.delegate().isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        this.delegate().debug(msg);
    }

    @Override
    public void debug(String format, Object arg) {
        this.delegate().debug(format, arg);
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        this.delegate().debug(format, arg1, arg2);
    }

    @Override
    public /* varargs */ void debug(String format, Object ... arguments) {
        this.delegate().debug(format, arguments);
    }

    @Override
    public void debug(String msg, Throwable t) {
        this.delegate().debug(msg, t);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return this.delegate().isDebugEnabled(marker);
    }

    @Override
    public void debug(Marker marker, String msg) {
        this.substituteLoggerExecutor.debug(marker, msg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        this.substituteLoggerExecutor.debug(marker, format, arg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        this.delegate().debug(marker, format, arg1, arg2);
    }

    @Override
    public /* varargs */ void debug(Marker marker, String format, Object ... arguments) {
        this.substituteLoggerExecutor.debug(marker, format, arguments);
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        this.substituteLoggerExecutor.debug(marker, msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return this.delegate().isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        this.delegate().info(msg);
    }

    @Override
    public void info(String format, Object arg) {
        this.delegate().info(format, arg);
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        this.delegate().info(format, arg1, arg2);
    }

    @Override
    public /* varargs */ void info(String format, Object ... arguments) {
        this.substituteLoggerExecutor.info(format, arguments);
    }

    @Override
    public void info(String msg, Throwable t) {
        this.delegate().info(msg, t);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return this.substituteLoggerExecutor.isInfoEnabled(marker);
    }

    @Override
    public void info(Marker marker, String msg) {
        this.delegate().info(marker, msg);
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        this.delegate().info(marker, format, arg);
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        this.delegate().info(marker, format, arg1, arg2);
    }

    @Override
    public /* varargs */ void info(Marker marker, String format, Object ... arguments) {
        this.delegate().info(marker, format, arguments);
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        this.substituteLoggerExecutor.info(marker, msg, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return this.delegate().isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        this.delegate().warn(msg);
    }

    @Override
    public void warn(String format, Object arg) {
        this.delegate().warn(format, arg);
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        this.substituteLoggerExecutor.warn(format, arg1, arg2);
    }

    @Override
    public /* varargs */ void warn(String format, Object ... arguments) {
        this.substituteLoggerExecutor.warn(format, arguments);
    }

    @Override
    public void warn(String msg, Throwable t) {
        this.delegate().warn(msg, t);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return this.substituteLoggerExecutor.isWarnEnabled(marker);
    }

    @Override
    public void warn(Marker marker, String msg) {
        this.delegate().warn(marker, msg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        this.delegate().warn(marker, format, arg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        this.delegate().warn(marker, format, arg1, arg2);
    }

    @Override
    public /* varargs */ void warn(Marker marker, String format, Object ... arguments) {
        this.delegate().warn(marker, format, arguments);
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        this.substituteLoggerExecutor.warn(marker, msg, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return this.delegate().isErrorEnabled();
    }

    @Override
    public void error(String msg) {
        this.delegate().error(msg);
    }

    @Override
    public void error(String format, Object arg) {
        this.delegate().error(format, arg);
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        this.delegate().error(format, arg1, arg2);
    }

    @Override
    public /* varargs */ void error(String format, Object ... arguments) {
        this.substituteLoggerExecutor.error(format, arguments);
    }

    @Override
    public void error(String msg, Throwable t) {
        this.delegate().error(msg, t);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return this.substituteLoggerExecutor.isErrorEnabled(marker);
    }

    @Override
    public void error(Marker marker, String msg) {
        this.delegate().error(marker, msg);
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        this.delegate().error(marker, format, arg);
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        this.substituteLoggerExecutor.error(marker, format, arg1, arg2);
    }

    @Override
    public /* varargs */ void error(Marker marker, String format, Object ... arguments) {
        this.substituteLoggerExecutor.error(marker, format, arguments);
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        this.delegate().error(marker, msg, t);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        SubstituteLogger that = (SubstituteLogger)o;
        if (!this.name.equals(that.name)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    Logger delegate() {
        return this._delegate != null ? this._delegate : this.pullEventRecordingLogger();
    }

    private Logger pullEventRecordingLogger() {
        if (this.eventRecodingLogger == null) {
            this.eventRecodingLogger = new EventRecodingLogger(this, this.eventList);
        }
        return this.eventRecodingLogger;
    }

    public void fixDelegate(Logger delegate) {
        this._delegate = delegate;
    }

    public boolean isDelegateEventAware() {
        if (this.delegateEventAware != null) {
            return this.delegateEventAware;
        }
        try {
            this.logMethodCache = this._delegate.getClass().getMethod("log", LoggingEvent.class);
            this.delegateEventAware = Boolean.TRUE;
        }
        catch (NoSuchMethodException e) {
            this.delegateEventAware = Boolean.FALSE;
        }
        return this.delegateEventAware;
    }

    public void log(LoggingEvent event) {
        if (this.isDelegateEventAware()) {
            this.logWorker(event);
        }
    }

    private void logWorker(LoggingEvent event) {
        try {
            this.logMethodCache.invoke(this._delegate, event);
        }
        catch (IllegalAccessException illegalAccessException) {
        }
        catch (IllegalArgumentException illegalArgumentException) {
        }
        catch (InvocationTargetException invocationTargetException) {
            // empty catch block
        }
    }

    public boolean isDelegateNOP() {
        return this._delegate instanceof NOPLogger;
    }
}

