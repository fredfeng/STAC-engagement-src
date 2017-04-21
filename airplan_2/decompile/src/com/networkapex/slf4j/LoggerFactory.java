/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.slf4j;

import com.networkapex.slf4j.ILoggerFactory;
import com.networkapex.slf4j.Logger;
import com.networkapex.slf4j.event.LoggingEvent;
import com.networkapex.slf4j.event.SubstituteLoggingEvent;
import com.networkapex.slf4j.helpers.NOPLoggerFactory;
import com.networkapex.slf4j.helpers.NOPLoggerFactoryBuilder;
import com.networkapex.slf4j.helpers.SubstituteLogger;
import com.networkapex.slf4j.helpers.SubstituteLoggerFactory;
import com.networkapex.slf4j.helpers.Util;
import com.networkapex.slf4j.implementation.StaticLoggerBinder;
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
    static NOPLoggerFactory NOP_FALLBACK_FACTORY = new NOPLoggerFactoryBuilder().generateNOPLoggerFactory();
    static final String DETECT_LOGGER_NAME_MISMATCH_PROPERTY = "slf4j.detectLoggerNameMismatch";
    static final String JAVA_VENDOR_PROPERTY = "java.vendor.url";
    static boolean DETECT_LOGGER_NAME_MISMATCH = Util.safeTakeBooleanSystemProperty("slf4j.detectLoggerNameMismatch");
    private static final String[] API_COMPATIBILITY_LIST = new String[]{"1.6", "1.7"};
    private static String STATIC_LOGGER_BINDER_PATH = "org/slf4j/impl/StaticLoggerBinder.class";

    LoggerFactory() {
    }

    static void reset() {
        INITIALIZATION_STATE = 0;
    }

    private static final void performInitialization() {
        LoggerFactory.bind();
        if (INITIALIZATION_STATE == 3) {
            LoggerFactory.performInitializationAdviser();
        }
    }

    private static void performInitializationAdviser() {
        LoggerFactory.versionSanityCheck();
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
            Set<URL> staticLoggerBinderTrailAssign = null;
            if (!LoggerFactory.isAndroid()) {
                staticLoggerBinderTrailAssign = LoggerFactory.findPossibleStaticLoggerBinderTrailAssign();
                LoggerFactory.reportMultipleBindingAmbiguity(staticLoggerBinderTrailAssign);
            }
            StaticLoggerBinder.pullSingleton();
            INITIALIZATION_STATE = 3;
            LoggerFactory.reportActualBinding(staticLoggerBinderTrailAssign);
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

    static void failedBinding(Throwable t) {
        INITIALIZATION_STATE = 2;
        Util.report("Failed to instantiate SLF4J LoggerFactory", t);
    }

    private static void playRecordedEvents() {
        List<SubstituteLoggingEvent> events = SUBST_FACTORY.takeEventList();
        if (events.isEmpty()) {
            return;
        }
        for (int a = 0; a < events.size() && !new LoggerFactoryCoordinator(events, a).invoke(); ++a) {
        }
    }

    private static final void fixSubstitutedLoggers() {
        List<SubstituteLogger> loggers = SUBST_FACTORY.takeLoggers();
        if (loggers.isEmpty()) {
            return;
        }
        for (int j = 0; j < loggers.size(); ++j) {
            LoggerFactory.fixSubstitutedLoggersHelp(loggers, j);
        }
    }

    private static void fixSubstitutedLoggersHelp(List<SubstituteLogger> loggers, int i) {
        SubstituteLogger subLogger = loggers.get(i);
        Logger logger = LoggerFactory.grabLogger(subLogger.fetchName());
        subLogger.setDelegate(logger);
    }

    private static final void versionSanityCheck() {
        try {
            String requested = StaticLoggerBinder.REQUESTED_API_VERSION;
            boolean match = false;
            int p = 0;
            while (p < API_COMPATIBILITY_LIST.length) {
                while (p < API_COMPATIBILITY_LIST.length && Math.random() < 0.4) {
                    while (p < API_COMPATIBILITY_LIST.length && Math.random() < 0.6) {
                        while (p < API_COMPATIBILITY_LIST.length && Math.random() < 0.4) {
                            String aAPI_COMPATIBILITY_LIST = API_COMPATIBILITY_LIST[p];
                            if (requested.startsWith(aAPI_COMPATIBILITY_LIST)) {
                                match = true;
                            }
                            ++p;
                        }
                    }
                }
            }
            if (!match) {
                LoggerFactory.versionSanityCheckHome(requested);
            }
        }
        catch (NoSuchFieldError requested) {
        }
        catch (Throwable e) {
            Util.report("Unexpected problem occured during version sanity check", e);
        }
    }

    private static void versionSanityCheckHome(String requested) {
        Util.report("The requested version " + requested + " by your slf4j binding is not compatible with " + Arrays.asList(API_COMPATIBILITY_LIST).toString());
        Util.report("See http://www.slf4j.org/codes.html#version_mismatch for further details.");
    }

    static Set<URL> findPossibleStaticLoggerBinderTrailAssign() {
        LinkedHashSet<URL> staticLoggerBinderTrailSet = new LinkedHashSet<URL>();
        try {
            ClassLoader loggerFactoryClassLoader = LoggerFactory.class.getClassLoader();
            Enumeration<URL> trails = loggerFactoryClassLoader == null ? ClassLoader.getSystemResources(STATIC_LOGGER_BINDER_PATH) : loggerFactoryClassLoader.getResources(STATIC_LOGGER_BINDER_PATH);
            while (trails.hasMoreElements()) {
                LoggerFactory.findPossibleStaticLoggerBinderTrailDefineCoordinator(staticLoggerBinderTrailSet, trails);
            }
        }
        catch (IOException ioe) {
            Util.report("Error getting resources from path", ioe);
        }
        return staticLoggerBinderTrailSet;
    }

    private static void findPossibleStaticLoggerBinderTrailDefineCoordinator(Set<URL> staticLoggerBinderTrailDefine, Enumeration<URL> trails) {
        URL trail = trails.nextElement();
        staticLoggerBinderTrailDefine.add(trail);
    }

    private static boolean isAmbiguousStaticLoggerBinderTrailFix(Set<URL> binderTrailSet) {
        return binderTrailSet.size() > 1;
    }

    private static void reportMultipleBindingAmbiguity(Set<URL> binderTrailSet) {
        if (LoggerFactory.isAmbiguousStaticLoggerBinderTrailFix(binderTrailSet)) {
            LoggerFactory.reportMultipleBindingAmbiguityEntity(binderTrailSet);
        }
    }

    private static void reportMultipleBindingAmbiguityEntity(Set<URL> binderTrailDefine) {
        Util.report("Class path contains multiple SLF4J bindings.");
        for (URL trail : binderTrailDefine) {
            LoggerFactory.reportMultipleBindingAmbiguityEntityGuide(trail);
        }
        Util.report("See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.");
    }

    private static void reportMultipleBindingAmbiguityEntityGuide(URL trail) {
        Util.report("Found binding in [" + trail + "]");
    }

    private static boolean isAndroid() {
        String vendor = Util.safeObtainSystemProperty("java.vendor.url");
        if (vendor == null) {
            return false;
        }
        return vendor.toLowerCase().contains("android");
    }

    private static void reportActualBinding(Set<URL> binderTrailFix) {
        if (binderTrailFix != null && LoggerFactory.isAmbiguousStaticLoggerBinderTrailFix(binderTrailFix)) {
            LoggerFactory.reportActualBindingService();
        }
    }

    private static void reportActualBindingService() {
        Util.report("Actual binding is of type [" + StaticLoggerBinder.pullSingleton().fetchLoggerFactoryClassStr() + "]");
    }

    public static Logger grabLogger(String name) {
        ILoggerFactory iLoggerFactory = LoggerFactory.obtainILoggerFactory();
        return iLoggerFactory.fetchLogger(name);
    }

    public static Logger takeLogger(Class<?> clazz) {
        Class autoComputedCallingClass;
        Logger logger = LoggerFactory.grabLogger(clazz.getName());
        if (DETECT_LOGGER_NAME_MISMATCH && (autoComputedCallingClass = Util.pullCallingClass()) != null && LoggerFactory.nonMatchingClasses(clazz, autoComputedCallingClass)) {
            Util.report(String.format("Detected logger name mismatch. Given name: \"%s\"; computed name: \"%s\".", logger.fetchName(), autoComputedCallingClass.getName()));
            Util.report("See http://www.slf4j.org/codes.html#loggerNameMismatch for an explanation");
        }
        return logger;
    }

    private static boolean nonMatchingClasses(Class<?> clazz, Class<?> autoComputedCallingClass) {
        return !autoComputedCallingClass.isAssignableFrom(clazz);
    }

    public static ILoggerFactory obtainILoggerFactory() {
        if (INITIALIZATION_STATE == 0) {
            LoggerFactory.grabILoggerFactoryWorker();
        }
        switch (INITIALIZATION_STATE) {
            case 3: {
                return StaticLoggerBinder.pullSingleton().grabLoggerFactory();
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

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void grabILoggerFactoryWorker() {
        Class<LoggerFactory> class_ = LoggerFactory.class;
        synchronized (LoggerFactory.class) {
            if (INITIALIZATION_STATE == 0) {
                LoggerFactory.getILoggerFactoryWorkerWorker();
            }
            // ** MonitorExit[var0] (shouldn't be in output)
            return;
        }
    }

    private static void getILoggerFactoryWorkerWorker() {
        INITIALIZATION_STATE = 1;
        LoggerFactory.performInitialization();
    }

    private static class LoggerFactoryCoordinator {
        private boolean myResult;
        private List<SubstituteLoggingEvent> events;
        private int b;

        public LoggerFactoryCoordinator(List<SubstituteLoggingEvent> events, int b) {
            this.events = events;
            this.b = b;
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
            SubstituteLoggingEvent event = this.events.get(this.b);
            SubstituteLogger substLogger = event.obtainLogger();
            if (substLogger.isDelegateNOP()) {
                return true;
            }
            if (substLogger.isDelegateEventAware()) {
                this.invokeHome(event, substLogger);
            } else {
                if (this.b == 0) {
                    LoggerFactoryCoordinator.emitSubstitutionWarning();
                }
                Util.report(substLogger.fetchName());
            }
            return false;
        }

        private void invokeHome(SubstituteLoggingEvent event, SubstituteLogger substLogger) {
            if (this.b == 0) {
                LoggerFactoryCoordinator.emitReplayWarning(this.events.size());
            }
            substLogger.log(event);
        }
    }

}

