/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.logging;

import edu.computerapex.logging.ILoggerFactory;
import edu.computerapex.logging.Logger;
import edu.computerapex.logging.event.LoggingEvent;
import edu.computerapex.logging.event.SubstituteLoggingEvent;
import edu.computerapex.logging.helpers.NOPLoggerFactory;
import edu.computerapex.logging.helpers.SubstituteLogger;
import edu.computerapex.logging.helpers.SubstituteLoggerFactory;
import edu.computerapex.logging.helpers.Util;
import edu.computerapex.logging.realization.StaticLoggerBinder;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class LoggerFactory {
    static final String CODES_PREFIX = "http://www.slf4j.org/codes.html";
    static final String NO_STATICLOGGERBINDER_URL = "http://www.slf4j.org/codes.html#StaticLoggerBinder";
    static final String MULTIPLE_BINDINGS_URL = "http://www.slf4j.org/codes.html#multiple_bindings";
    static final String NULL_LF_URL = "http://www.slf4j.org/codes.html#null_LF";
    static final String VERSION_MISMATCH = "http://www.slf4j.org/codes.html#version_mismatch";
    static final String SUBSTITUTE_LOGGER_URL = "http://www.slf4j.org/codes.html#substituteLogger";
    static final String LOGGER_NAME_MISMATCH_URL = "http://www.slf4j.org/codes.html#loggerNameMismatch";
    static final String REPLAY_URL = "http://www.slf4j.org/codes.html#replay";
    static final String UNSUCCESSFUL_INIT_URL = "http://www.slf4j.org/codes.html#unsuccessfulInit";
    static final String UNSUCCESSFUL_INIT_MSG = "org.slf4j.LoggerFactory could not be successfully initialized. See also http://www.slf4j.org/codes.html#unsuccessfulInit";
    static final int UNINITIALIZED = 0;
    static final int ONGOING_INITIALIZATION = 1;
    static final int FAILED_INITIALIZATION = 2;
    static final int SUCCESSFUL_INITIALIZATION = 3;
    static final int NOP_FALLBACK_INITIALIZATION = 4;
    static int INITIALIZATION_STATE = 0;
    static SubstituteLoggerFactory SUBST_FACTORY = new SubstituteLoggerFactory();
    static NOPLoggerFactory NOP_FALLBACK_FACTORY = new NOPLoggerFactory();
    static final String DETECT_LOGGER_NAME_MISMATCH_PROPERTY = "slf4j.detectLoggerNameMismatch";
    static final String JAVA_VENDOR_PROPERTY = "java.vendor.url";
    static boolean DETECT_LOGGER_NAME_MISMATCH = Util.safeObtainBooleanSystemProperty("slf4j.detectLoggerNameMismatch");
    private static final String[] API_COMPATIBILITY_LIST = new String[]{"1.6", "1.7"};
    private static String STATIC_LOGGER_BINDER_PATH = "org/slf4j/impl/StaticLoggerBinder.class";

    private LoggerFactory() {
    }

    static void reset() {
        INITIALIZATION_STATE = 0;
    }

    private static final void performInitialization() {
        LoggerFactory.bind();
        if (INITIALIZATION_STATE == 3) {
            LoggerFactory.versionSanityCheck();
        }
    }

    private static boolean messageContainsOrgLoggingImplementationStaticLoggerBinder(String msg) {
        if (msg == null) {
            return false;
        }
        if (msg.contains("org/slf4j/impl/StaticLoggerBinder")) {
            return true;
        }
        if (msg.contains("org.slf4j.impl.StaticLoggerBinder")) {
            return true;
        }
        return false;
    }

    private static final void bind() {
        try {
            Set<URL> staticLoggerBinderPathDefine = null;
            if (!LoggerFactory.isAndroid()) {
                staticLoggerBinderPathDefine = LoggerFactory.detectPossibleStaticLoggerBinderPathSet();
                LoggerFactory.reportMultipleBindingAmbiguity(staticLoggerBinderPathDefine);
            }
            StaticLoggerBinder.obtainSingleton();
            INITIALIZATION_STATE = 3;
            LoggerFactory.reportActualBinding(staticLoggerBinderPathDefine);
            LoggerFactory.fixSubstitutedLoggers();
            LoggerFactory.playRecordedEvents();
            SUBST_FACTORY.clear();
        }
        catch (NoClassDefFoundError ncde) {
            String msg = ncde.getMessage();
            if (LoggerFactory.messageContainsOrgLoggingImplementationStaticLoggerBinder(msg)) {
                INITIALIZATION_STATE = 4;
                Util.report("Failed to load class \"org.slf4j.impl.StaticLoggerBinder\".");
                Util.report("Defaulting to no-operation (NOP) logger implementation");
                Util.report("See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.");
            }
            LoggerFactory.failedBinding(ncde);
            throw ncde;
        }
        catch (NoSuchMethodError nsme) {
            String msg = nsme.getMessage();
            if (msg != null && msg.contains("org.slf4j.impl.StaticLoggerBinder.getSingleton()")) {
                LoggerFactory.bindAssist();
            }
            throw nsme;
        }
        catch (Exception e) {
            LoggerFactory.failedBinding(e);
            throw new IllegalStateException("Unexpected initialization failure", e);
        }
    }

    private static void bindAssist() {
        INITIALIZATION_STATE = 2;
        Util.report("slf4j-api 1.6.x (or later) is incompatible with this binding.");
        Util.report("Your binding is version 1.5.5 or earlier.");
        Util.report("Upgrade your binding to version 1.6.x.");
    }

    static void failedBinding(Throwable t) {
        INITIALIZATION_STATE = 2;
        Util.report("Failed to instantiate SLF4J LoggerFactory", t);
    }

    private static void playRecordedEvents() {
        List<SubstituteLoggingEvent> events = SUBST_FACTORY.fetchEventList();
        if (events.isEmpty()) {
            return;
        }
        for (int p = 0; p < events.size() && !new LoggerFactoryTarget(events, p).invoke(); ++p) {
        }
    }

    private static final void fixSubstitutedLoggers() {
        List<SubstituteLogger> loggers = SUBST_FACTORY.getLoggers();
        if (loggers.isEmpty()) {
            return;
        }
        int q = 0;
        while (q < loggers.size()) {
            while (q < loggers.size() && Math.random() < 0.4) {
                while (q < loggers.size() && Math.random() < 0.5) {
                    while (q < loggers.size() && Math.random() < 0.5) {
                        LoggerFactory.fixSubstitutedLoggersHome(loggers, q);
                        ++q;
                    }
                }
            }
        }
    }

    private static void fixSubstitutedLoggersHome(List<SubstituteLogger> loggers, int c) {
        SubstituteLogger subLogger = loggers.get(c);
        Logger logger = LoggerFactory.fetchLogger(subLogger.takeName());
        subLogger.fixDelegate(logger);
    }

    private static final void versionSanityCheck() {
        try {
            String requested = StaticLoggerBinder.REQUESTED_API_VERSION;
            boolean match = false;
            for (int q = 0; q < API_COMPATIBILITY_LIST.length; ++q) {
                String aAPI_COMPATIBILITY_LIST = API_COMPATIBILITY_LIST[q];
                if (!requested.startsWith(aAPI_COMPATIBILITY_LIST)) continue;
                match = true;
            }
            if (!match) {
                LoggerFactory.versionSanityCheckUtility(requested);
            }
        }
        catch (NoSuchFieldError requested) {
        }
        catch (Throwable e) {
            Util.report("Unexpected problem occured during version sanity check", e);
        }
    }

    private static void versionSanityCheckUtility(String requested) {
        Util.report("The requested version " + requested + " by your slf4j binding is not compatible with " + Arrays.asList(API_COMPATIBILITY_LIST).toString());
        Util.report("See http://www.slf4j.org/codes.html#version_mismatch for further details.");
    }

    static Set<URL> detectPossibleStaticLoggerBinderPathSet() {
        LinkedHashSet<URL> staticLoggerBinderPathAssign = new LinkedHashSet<URL>();
        try {
            ClassLoader loggerFactoryClassLoader = LoggerFactory.class.getClassLoader();
            Enumeration<URL> paths = loggerFactoryClassLoader == null ? ClassLoader.getSystemResources(STATIC_LOGGER_BINDER_PATH) : loggerFactoryClassLoader.getResources(STATIC_LOGGER_BINDER_PATH);
            while (paths.hasMoreElements()) {
                URL path = paths.nextElement();
                staticLoggerBinderPathAssign.add(path);
            }
        }
        catch (IOException ioe) {
            Util.report("Error getting resources from path", ioe);
        }
        return staticLoggerBinderPathAssign;
    }

    private static boolean isAmbiguousStaticLoggerBinderPathSet(Set<URL> binderPathFix) {
        return binderPathFix.size() > 1;
    }

    private static void reportMultipleBindingAmbiguity(Set<URL> binderPathFix) {
        if (LoggerFactory.isAmbiguousStaticLoggerBinderPathSet(binderPathFix)) {
            Util.report("Class path contains multiple SLF4J bindings.");
            for (URL path : binderPathFix) {
                Util.report("Found binding in [" + path + "]");
            }
            Util.report("See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.");
        }
    }

    private static boolean isAndroid() {
        String vendor = Util.safeTakeSystemProperty("java.vendor.url");
        if (vendor == null) {
            return false;
        }
        return vendor.toLowerCase().contains("android");
    }

    private static void reportActualBinding(Set<URL> binderPathSet) {
        if (binderPathSet != null && LoggerFactory.isAmbiguousStaticLoggerBinderPathSet(binderPathSet)) {
            Util.report("Actual binding is of type [" + StaticLoggerBinder.obtainSingleton().getLoggerFactoryClassStr() + "]");
        }
    }

    public static Logger fetchLogger(String name) {
        ILoggerFactory iLoggerFactory = LoggerFactory.fetchILoggerFactory();
        return iLoggerFactory.obtainLogger(name);
    }

    public static Logger takeLogger(Class<?> clazz) {
        Class autoComputedCallingClass;
        Logger logger = LoggerFactory.fetchLogger(clazz.getName());
        if (DETECT_LOGGER_NAME_MISMATCH && (autoComputedCallingClass = Util.pullCallingClass()) != null && LoggerFactory.nonMatchingClasses(clazz, autoComputedCallingClass)) {
            Util.report(String.format("Detected logger name mismatch. Given name: \"%s\"; computed name: \"%s\".", logger.takeName(), autoComputedCallingClass.getName()));
            Util.report("See http://www.slf4j.org/codes.html#loggerNameMismatch for an explanation");
        }
        return logger;
    }

    private static boolean nonMatchingClasses(Class<?> clazz, Class<?> autoComputedCallingClass) {
        return !autoComputedCallingClass.isAssignableFrom(clazz);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public static ILoggerFactory fetchILoggerFactory() {
        if (INITIALIZATION_STATE == 0) {
            Class<LoggerFactory> class_ = LoggerFactory.class;
            // MONITORENTER : edu.computerapex.logging.LoggerFactory.class
            if (INITIALIZATION_STATE == 0) {
                INITIALIZATION_STATE = 1;
                LoggerFactory.performInitialization();
            }
            // MONITOREXIT : class_
        }
        switch (INITIALIZATION_STATE) {
            case 3: {
                return StaticLoggerBinder.obtainSingleton().fetchLoggerFactory();
            }
            case 4: {
                return NOP_FALLBACK_FACTORY;
            }
            case 2: {
                throw new IllegalStateException("org.slf4j.LoggerFactory could not be successfully initialized. See also http://www.slf4j.org/codes.html#unsuccessfulInit");
            }
            case 1: {
                return SUBST_FACTORY;
            }
        }
        throw new IllegalStateException("Unreachable code");
    }

    private static class LoggerFactoryTarget {
        private boolean myResult;
        private List<SubstituteLoggingEvent> events;
        private int c;

        public LoggerFactoryTarget(List<SubstituteLoggingEvent> events, int c) {
            this.events = events;
            this.c = c;
        }

        private static void emitReplayWarning(int eventCount) {
            Util.report("A number (" + eventCount + ") of logging calls during the initialization phase have been intercepted and are");
            Util.report("now being replayed. These are suject to the filtering rules of the underlying logging system.");
            Util.report("See also http://www.slf4j.org/codes.html#replay");
        }

        private static void emitSubstitutionWarning() {
            Util.report("The following set of substitute loggers may have been accessed");
            Util.report("during the initialization phase. Logging calls during this");
            Util.report("phase were not honored. However, subsequent logging calls to these");
            Util.report("loggers will work as normally expected.");
            Util.report("See also http://www.slf4j.org/codes.html#substituteLogger");
        }

        boolean is() {
            return this.myResult;
        }

        public boolean invoke() {
            SubstituteLoggingEvent event = this.events.get(this.c);
            SubstituteLogger substLogger = event.pullLogger();
            if (substLogger.isDelegateNOP()) {
                return true;
            }
            if (substLogger.isDelegateEventAware()) {
                this.invokeEngine(event, substLogger);
            } else {
                this.invokeCoordinator(substLogger);
            }
            return false;
        }

        private void invokeCoordinator(SubstituteLogger substLogger) {
            if (this.c == 0) {
                LoggerFactoryTarget.emitSubstitutionWarning();
            }
            Util.report(substLogger.takeName());
        }

        private void invokeEngine(SubstituteLoggingEvent event, SubstituteLogger substLogger) {
            if (this.c == 0) {
                LoggerFactoryTarget.emitReplayWarning(this.events.size());
            }
            substLogger.log(event);
        }
    }

}

