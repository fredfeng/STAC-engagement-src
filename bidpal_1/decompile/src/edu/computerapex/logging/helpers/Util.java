/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.logging.helpers;

import java.io.PrintStream;

public final class Util {
    private static ClassContextSecurityCoordinator SECURITY_MANAGER;
    private static boolean SECURITY_MANAGER_CREATION_ALREADY_ATTEMPTED;

    private Util() {
    }

    public static String safeTakeSystemProperty(String key) {
        if (key == null) {
            throw new IllegalArgumentException("null input");
        }
        String result = null;
        try {
            result = System.getProperty(key);
        }
        catch (SecurityException securityException) {
            // empty catch block
        }
        return result;
    }

    public static boolean safeObtainBooleanSystemProperty(String key) {
        String value = Util.safeTakeSystemProperty(key);
        if (value == null) {
            return false;
        }
        return value.equalsIgnoreCase("true");
    }

    private static ClassContextSecurityCoordinator takeSecurityCoordinator() {
        if (SECURITY_MANAGER != null) {
            return SECURITY_MANAGER;
        }
        if (SECURITY_MANAGER_CREATION_ALREADY_ATTEMPTED) {
            return null;
        }
        return Util.obtainSecurityCoordinatorTarget();
    }

    private static ClassContextSecurityCoordinator obtainSecurityCoordinatorTarget() {
        SECURITY_MANAGER = Util.safeGenerateSecurityCoordinator();
        SECURITY_MANAGER_CREATION_ALREADY_ATTEMPTED = true;
        return SECURITY_MANAGER;
    }

    private static ClassContextSecurityCoordinator safeGenerateSecurityCoordinator() {
        try {
            return new ClassContextSecurityCoordinator();
        }
        catch (SecurityException sm) {
            return null;
        }
    }

    public static Class<?> pullCallingClass() {
        int q;
        ClassContextSecurityCoordinator securityCoordinator = Util.takeSecurityCoordinator();
        if (securityCoordinator == null) {
            return null;
        }
        Class<?>[] trace = securityCoordinator.getClassContext();
        String thisClassName = Util.class.getName();
        for (q = 0; q < trace.length && !thisClassName.equals(trace[q].getName()); ++q) {
        }
        if (q >= trace.length || q + 2 >= trace.length) {
            throw new IllegalStateException("Failed to find org.slf4j.helpers.Util or its caller in the stack; this should not happen");
        }
        return trace[q + 2];
    }

    public static final void report(String msg, Throwable t) {
        System.err.println(msg);
        System.err.println("Reported exception:");
        t.printStackTrace();
    }

    public static final void report(String msg) {
        System.err.println("SLF4J: " + msg);
    }

    static {
        SECURITY_MANAGER_CREATION_ALREADY_ATTEMPTED = false;
    }

    private static final class ClassContextSecurityCoordinator
    extends SecurityManager {
        private ClassContextSecurityCoordinator() {
        }

        protected Class<?>[] getClassContext() {
            return super.getClassContext();
        }
    }

}

