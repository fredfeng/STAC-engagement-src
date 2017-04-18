/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.record.helpers;

import edu.cyberapex.record.Logger;
import edu.cyberapex.record.Marker;
import edu.cyberapex.record.event.EventRecodingLogger;
import edu.cyberapex.record.event.LoggingEvent;
import edu.cyberapex.record.event.SubstituteLoggingEvent;
import edu.cyberapex.record.helpers.NOPLogger;
import edu.cyberapex.record.helpers.SubstituteLoggerGateKeeper;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class SubstituteLogger
implements Logger {
    private final String name;
    private final SubstituteLoggerGateKeeper substituteLoggerGateKeeper;
    private volatile Logger _delegate;
    private Boolean delegateEventAware;
    private Method logMethodCache;
    private EventRecodingLogger eventRecodingLogger;
    private List<SubstituteLoggingEvent> eventList;

    public SubstituteLogger(String name, List<SubstituteLoggingEvent> eventList) {
        this.substituteLoggerGateKeeper = new SubstituteLoggerGateKeeper(this);
        this.name = name;
        this.eventList = eventList;
    }

    @Override
    public String fetchName() {
        return this.name;
    }

    @Override
    public boolean isTraceEnabled() {
        return this.delegate().isTraceEnabled();
    }

    @Override
    public void trace(String msg) {
        this.substituteLoggerGateKeeper.trace(msg);
    }

    @Override
    public void trace(String format, Object arg) {
        this.delegate().trace(format, arg);
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        this.delegate().trace(format, arg1, arg2);
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
        this.substituteLoggerGateKeeper.trace(marker, format, arg1, arg2);
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
        this.substituteLoggerGateKeeper.debug(format, arg1, arg2);
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
        this.substituteLoggerGateKeeper.debug(marker, msg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        this.substituteLoggerGateKeeper.debug(marker, format, arg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        this.delegate().debug(marker, format, arg1, arg2);
    }

    @Override
    public /* varargs */ void debug(Marker marker, String format, Object ... arguments) {
        this.delegate().debug(marker, format, arguments);
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        this.delegate().debug(marker, msg, t);
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
        this.substituteLoggerGateKeeper.info(format, arguments);
    }

    @Override
    public void info(String msg, Throwable t) {
        this.substituteLoggerGateKeeper.info(msg, t);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return this.delegate().isInfoEnabled(marker);
    }

    @Override
    public void info(Marker marker, String msg) {
        this.delegate().info(marker, msg);
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        this.substituteLoggerGateKeeper.info(marker, format, arg);
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        this.substituteLoggerGateKeeper.info(marker, format, arg1, arg2);
    }

    @Override
    public /* varargs */ void info(Marker marker, String format, Object ... arguments) {
        this.delegate().info(marker, format, arguments);
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        this.delegate().info(marker, msg, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return this.substituteLoggerGateKeeper.isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        this.substituteLoggerGateKeeper.warn(msg);
    }

    @Override
    public void warn(String format, Object arg) {
        this.substituteLoggerGateKeeper.warn(format, arg);
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        this.delegate().warn(format, arg1, arg2);
    }

    @Override
    public /* varargs */ void warn(String format, Object ... arguments) {
        this.delegate().warn(format, arguments);
    }

    @Override
    public void warn(String msg, Throwable t) {
        this.substituteLoggerGateKeeper.warn(msg, t);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return this.substituteLoggerGateKeeper.isWarnEnabled(marker);
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
        this.substituteLoggerGateKeeper.warn(marker, format, arg1, arg2);
    }

    @Override
    public /* varargs */ void warn(Marker marker, String format, Object ... arguments) {
        this.delegate().warn(marker, format, arguments);
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        this.delegate().warn(marker, msg, t);
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
        this.delegate().error(format, arguments);
    }

    @Override
    public void error(String msg, Throwable t) {
        this.delegate().error(msg, t);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return this.delegate().isErrorEnabled(marker);
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
        this.delegate().error(marker, format, arg1, arg2);
    }

    @Override
    public /* varargs */ void error(Marker marker, String format, Object ... arguments) {
        this.delegate().error(marker, format, arguments);
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
        return this._delegate != null ? this._delegate : this.getEventRecordingLogger();
    }

    private Logger getEventRecordingLogger() {
        if (this.eventRecodingLogger == null) {
            this.fetchEventRecordingLoggerUtility();
        }
        return this.eventRecodingLogger;
    }

    private void fetchEventRecordingLoggerUtility() {
        this.eventRecodingLogger = new EventRecodingLogger(this, this.eventList);
    }

    public void defineDelegate(Logger delegate) {
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
            this.logGuide(event);
        }
    }

    private void logGuide(LoggingEvent event) {
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

