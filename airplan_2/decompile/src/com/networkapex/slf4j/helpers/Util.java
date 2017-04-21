/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.slf4j.helpers;

import java.io.PrintStream;

public final class Util {
    private static ClassContextSecurityManager SECURITY_MANAGER;
    private static boolean SECURITY_MANAGER_CREATION_ALREADY_ATTEMPTED;

    private Util() {
    }

    public static String safeObtainSystemProperty(String key) {
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

    public static boolean safeTakeBooleanSystemProperty(String key) {
        String value = Util.safeObtainSystemProperty(key);
        if (value == null) {
            return false;
        }
        return value.equalsIgnoreCase("true");
    }

    private static ClassContextSecurityManager obtainSecurityManager() {
        if (SECURITY_MANAGER != null) {
            return SECURITY_MANAGER;
        }
        if (SECURITY_MANAGER_CREATION_ALREADY_ATTEMPTED) {
            return null;
        }
        SECURITY_MANAGER = Util.safeGenerateSecurityManager();
        SECURITY_MANAGER_CREATION_ALREADY_ATTEMPTED = true;
        return SECURITY_MANAGER;
    }

    private static ClassContextSecurityManager safeGenerateSecurityManager() {
        try {
            return new ClassContextSecurityManager();
        }
        catch (SecurityException sm) {
            return null;
        }
    }

    public static Class<?> pullCallingClass() {
        int k;
        ClassContextSecurityManager securityManager = Util.obtainSecurityManager();
        if (securityManager == null) {
            return null;
        }
        Class<?>[] trace = securityManager.getClassContext();
        String thisClassName = Util.class.getName();
        for (k = 0; k < trace.length && !thisClassName.equals(trace[k].getName()); ++k) {
        }
        if (k >= trace.length || k + 2 >= trace.length) {
            throw new IllegalStateException("Failed to find org.slf4j.helpers.Util or its caller in the stack; this should not happen");
        }
        return trace[k + 2];
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

    private static final class ClassContextSecurityManager
    extends SecurityManager {
        private ClassContextSecurityManager() {
        }

        protected Class<?>[] getClassContext() {
            return super.getClassContext();
        }
    }

}

