/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.record;

import edu.cyberapex.record.ILoggerFactory;
import edu.cyberapex.record.Logger;
import edu.cyberapex.record.event.LoggingEvent;
import edu.cyberapex.record.event.SubstituteLoggingEvent;
import edu.cyberapex.record.helpers.NOPLoggerFactory;
import edu.cyberapex.record.helpers.SubstituteLogger;
import edu.cyberapex.record.helpers.SubstituteLoggerFactory;
import edu.cyberapex.record.helpers.Util;
import edu.cyberapex.record.implementation.StaticLoggerBinder;
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
    static boolean DETECT_LOGGER_NAME_MISMATCH = Util.safePullBooleanSystemProperty("slf4j.detectLoggerNameMismatch");
    private static final String[] API_COMPATIBILITY_LIST = new String[]{"1.6", "1.7"};
    private static String STATIC_LOGGER_BINDER_PATH = "org/slf4j/impl/StaticLoggerBinder.class";

    private LoggerFactory() {
    }

    static void reset() {
        INITIALIZATION_STATE = 0;
    }

    private static boolean messageContainsOrgRecordImplStaticLoggerBinder(String msg) {
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
            Set<URL> staticLoggerBinderPathFix = null;
            if (!LoggerFactory.isAndroid()) {
                staticLoggerBinderPathFix = LoggerFactory.retrievePossibleStaticLoggerBinderPathSet();
                LoggerFactory.reportMultipleBindingAmbiguity(staticLoggerBinderPathFix);
            }
            StaticLoggerBinder.getSingleton();
            INITIALIZATION_STATE = 3;
            LoggerFactory.reportActualBinding(staticLoggerBinderPathFix);
            LoggerFactory.fixSubstitutedLoggers();
            LoggerFactory.playRecordedEvents();
            SUBST_FACTORY.clear();
        }
        catch (NoClassDefFoundError ncde) {
            String msg = ncde.getMessage();
            if (LoggerFactory.messageContainsOrgRecordImplStaticLoggerBinder(msg)) {
                LoggerFactory.bindTarget();
            } else {
                LoggerFactory.bindEntity(ncde);
            }
        }
        catch (NoSuchMethodError nsme) {
            String msg = nsme.getMessage();
            if (msg != null && msg.contains("org.slf4j.impl.StaticLoggerBinder.getSingleton()")) {
                INITIALIZATION_STATE = 2;
                Util.report("slf4j-api 1.6.x (or later) is incompatible with this binding.");
                Util.report("Your binding is version 1.5.5 or earlier.");
                Util.report("Upgrade your binding to version 1.6.x.");
            }
            throw nsme;
        }
        catch (Exception e) {
            LoggerFactory.failedBinding(e);
            throw new IllegalStateException("Unexpected initialization failure", e);
        }
    }

    private static void bindEntity(NoClassDefFoundError ncde) {
        LoggerFactory.failedBinding(ncde);
        throw ncde;
    }

    private static void bindTarget() {
        INITIALIZATION_STATE = 4;
        Util.report("Failed to load class \"org.slf4j.impl.StaticLoggerBinder\".");
        Util.report("Defaulting to no-operation (NOP) logger implementation");
        Util.report("See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.");
    }

    static void failedBinding(Throwable t) {
        INITIALIZATION_STATE = 2;
        Util.report("Failed to instantiate SLF4J LoggerFactory", t);
    }

    private static void playRecordedEvents() {
        List<SubstituteLoggingEvent> events = SUBST_FACTORY.getEventList();
        if (events.isEmpty()) {
            return;
        }
        for (int a = 0; a < events.size() && !LoggerFactory.playRecordedEventsAdviser(events, a); ++a) {
        }
    }

    private static boolean playRecordedEventsAdviser(List<SubstituteLoggingEvent> events, int q) {
        SubstituteLoggingEvent event = events.get(q);
        SubstituteLogger substLogger = event.grabLogger();
        if (substLogger.isDelegateNOP()) {
            return true;
        }
        if (substLogger.isDelegateEventAware()) {
            LoggerFactory.playRecordedEventsAdviserAssist(events, q, event, substLogger);
        } else {
            if (q == 0) {
                LoggerFactory.emitSubstitutionWarning();
            }
            Util.report(substLogger.fetchName());
        }
        return false;
    }

    private static void playRecordedEventsAdviserAssist(List<SubstituteLoggingEvent> events, int q, SubstituteLoggingEvent event, SubstituteLogger substLogger) {
        new LoggerFactoryUtility(events, q, event, substLogger).invoke();
    }

    private static final void fixSubstitutedLoggers() {
        List<SubstituteLogger> loggers = SUBST_FACTORY.takeLoggers();
        if (loggers.isEmpty()) {
            return;
        }
        for (int p = 0; p < loggers.size(); ++p) {
            LoggerFactory.fixSubstitutedLoggersHelp(loggers, p);
        }
    }

    private static void fixSubstitutedLoggersHelp(List<SubstituteLogger> loggers, int q) {
        SubstituteLogger subLogger = loggers.get(q);
        Logger logger = LoggerFactory.pullLogger(subLogger.fetchName());
        subLogger.defineDelegate(logger);
    }

    private static void emitSubstitutionWarning() {
        Util.report("The following set of substitute loggers may have been accessed");
        Util.report("during the initialization phase. Logging calls during this");
        Util.report("phase were not honored. However, subsequent logging calls to these");
        Util.report("loggers will work as normally expected.");
        Util.report("See also http://www.slf4j.org/codes.html#substituteLogger");
    }

    static Set<URL> retrievePossibleStaticLoggerBinderPathSet() {
        LinkedHashSet<URL> staticLoggerBinderPathDefine = new LinkedHashSet<URL>();
        try {
            ClassLoader loggerFactoryClassLoader = LoggerFactory.class.getClassLoader();
            Enumeration<URL> paths = loggerFactoryClassLoader == null ? ClassLoader.getSystemResources(STATIC_LOGGER_BINDER_PATH) : loggerFactoryClassLoader.getResources(STATIC_LOGGER_BINDER_PATH);
            while (paths.hasMoreElements()) {
                URL path = paths.nextElement();
                staticLoggerBinderPathDefine.add(path);
            }
        }
        catch (IOException ioe) {
            Util.report("Error getting resources from path", ioe);
        }
        return staticLoggerBinderPathDefine;
    }

    private static boolean isAmbiguousStaticLoggerBinderPathSet(Set<URL> binderPathAssign) {
        return binderPathAssign.size() > 1;
    }

    private static void reportMultipleBindingAmbiguity(Set<URL> binderPathDefine) {
        if (LoggerFactory.isAmbiguousStaticLoggerBinderPathSet(binderPathDefine)) {
            Util.report("Class path contains multiple SLF4J bindings.");
            for (URL path : binderPathDefine) {
                LoggerFactory.reportMultipleBindingAmbiguityExecutor(path);
            }
            Util.report("See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.");
        }
    }

    private static void reportMultipleBindingAmbiguityExecutor(URL path) {
        Util.report("Found binding in [" + path + "]");
    }

    private static boolean isAndroid() {
        String vendor = Util.safeGrabSystemProperty("java.vendor.url");
        if (vendor == null) {
            return false;
        }
        return vendor.toLowerCase().contains("android");
    }

    private static void reportActualBinding(Set<URL> binderPathDefine) {
        if (binderPathDefine != null && LoggerFactory.isAmbiguousStaticLoggerBinderPathSet(binderPathDefine)) {
            LoggerFactory.reportActualBindingEngine();
        }
    }

    private static void reportActualBindingEngine() {
        Util.report("Actual binding is of type [" + StaticLoggerBinder.getSingleton().pullLoggerFactoryClassStr() + "]");
    }

    public static Logger pullLogger(String name) {
        ILoggerFactory iLoggerFactory = LoggerFactory.fetchILoggerFactory();
        return iLoggerFactory.getLogger(name);
    }

    public static Logger getLogger(Class<?> clazz) {
        Class autoComputedCallingClass;
        Logger logger = LoggerFactory.pullLogger(clazz.getName());
        if (DETECT_LOGGER_NAME_MISMATCH && (autoComputedCallingClass = Util.obtainCallingClass()) != null && LoggerFactory.nonMatchingClasses(clazz, autoComputedCallingClass)) {
            LoggerFactory.grabLoggerEntity(logger, autoComputedCallingClass);
        }
        return logger;
    }

    private static void grabLoggerEntity(Logger logger, Class<?> autoComputedCallingClass) {
        Util.report(String.format("Detected logger name mismatch. Given name: \"%s\"; computed name: \"%s\".", logger.fetchName(), autoComputedCallingClass.getName()));
        Util.report("See http://www.slf4j.org/codes.html#loggerNameMismatch for an explanation");
    }

    private static boolean nonMatchingClasses(Class<?> clazz, Class<?> autoComputedCallingClass) {
        return !autoComputedCallingClass.isAssignableFrom(clazz);
    }

    public static ILoggerFactory fetchILoggerFactory() {
        if (INITIALIZATION_STATE == 0) {
            LoggerFactoryHelper.invoke();
        }
        switch (INITIALIZATION_STATE) {
            case 3: {
                return StaticLoggerBinder.getSingleton().fetchLoggerFactory();
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

    private static class LoggerFactoryHelper {
        private LoggerFactoryHelper() {
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private static void invoke() {
            Class<LoggerFactory> class_ = LoggerFactory.class;
            synchronized (LoggerFactory.class) {
                if (LoggerFactory.INITIALIZATION_STATE == 0) {
                    LoggerFactory.INITIALIZATION_STATE = 1;
                    LoggerFactoryHelper.performInitialization();
                }
                // ** MonitorExit[var0] (shouldn't be in output)
                return;
            }
        }

        private static final void performInitialization() {
            LoggerFactory.bind();
            if (LoggerFactory.INITIALIZATION_STATE == 3) {
                LoggerFactoryHelper.performInitializationExecutor();
            }
        }

        private static void performInitializationExecutor() {
            LoggerFactoryHelperHelp.invoke();
        }

        private static class LoggerFactoryHelperHelp {
            private LoggerFactoryHelperHelp() {
            }

            private static void invoke() {
                LoggerFactoryHelperHelp.versionSanityCheck();
            }

            private static final void versionSanityCheck() {
                try {
                    String requested = StaticLoggerBinder.REQUESTED_API_VERSION;
                    boolean match = false;
                    for (int i = 0; i < API_COMPATIBILITY_LIST.length; ++i) {
                        String aAPI_COMPATIBILITY_LIST = API_COMPATIBILITY_LIST[i];
                        if (!requested.startsWith(aAPI_COMPATIBILITY_LIST)) continue;
                        match = true;
                    }
                    if (!match) {
                        Util.report("The requested version " + requested + " by your slf4j binding is not compatible with " + Arrays.asList(API_COMPATIBILITY_LIST).toString());
                        Util.report("See http://www.slf4j.org/codes.html#version_mismatch for further details.");
                    }
                }
                catch (NoSuchFieldError requested) {
                }
                catch (Throwable e) {
                    Util.report("Unexpected problem occured during version sanity check", e);
                }
            }
        }

    }

    private static class LoggerFactoryUtility {
        private List<SubstituteLoggingEvent> events;
        private int q;
        private SubstituteLoggingEvent event;
        private SubstituteLogger substLogger;

        public LoggerFactoryUtility(List<SubstituteLoggingEvent> events, int q, SubstituteLoggingEvent event, SubstituteLogger substLogger) {
            this.events = events;
            this.q = q;
            this.event = event;
            this.substLogger = substLogger;
        }

        private static void emitReplayWarning(int eventCount) {
            Util.report("A number (" + eventCount + ") of logging calls during the initialization phase have been intercepted and are");
            Util.report("now being replayed. These are suject to the filtering rules of the underlying logging system.");
            Util.report("See also http://www.slf4j.org/codes.html#replay");
        }

        public void invoke() {
            if (this.q == 0) {
                LoggerFactoryUtility.emitReplayWarning(this.events.size());
            }
            this.substLogger.log(this.event);
        }
    }

}

