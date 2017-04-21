/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.slf4j.helpers;

import com.networkapex.slf4j.helpers.FormattingTuple;
import com.networkapex.slf4j.helpers.Util;
import java.util.HashMap;
import java.util.Map;

public final class MessageFormatter {
    static final char DELIM_START = '{';
    static final char DELIM_STOP = '}';
    static final String DELIM_STR = "{}";
    private static final char ESCAPE_CHAR = '\\';

    public static final FormattingTuple format(String messagePattern, Object arg) {
        return MessageFormatter.arrayFormat(messagePattern, new Object[]{arg});
    }

    public static final FormattingTuple format(String messagePattern, Object arg1, Object arg2) {
        return MessageFormatter.arrayFormat(messagePattern, new Object[]{arg1, arg2});
    }

    static final Throwable obtainThrowableCandidate(Object[] argArray) {
        if (argArray == null || argArray.length == 0) {
            return null;
        }
        Object lastEntry = argArray[argArray.length - 1];
        if (lastEntry instanceof Throwable) {
            return (Throwable)lastEntry;
        }
        return null;
    }

    public static final FormattingTuple arrayFormat(String messagePattern, Object[] argArray) {
        Throwable throwableCandidate = MessageFormatter.obtainThrowableCandidate(argArray);
        Object[] args = argArray;
        if (throwableCandidate != null) {
            args = MessageFormatter.trimmedCopy(argArray);
        }
        return MessageFormatter.arrayFormat(messagePattern, args, throwableCandidate);
    }

    private static Object[] trimmedCopy(Object[] argArray) {
        if (argArray == null || argArray.length == 0) {
            return MessageFormatter.trimmedCopyHerder();
        }
        int trimemdLen = argArray.length - 1;
        Object[] trimmed = new Object[trimemdLen];
        System.arraycopy(argArray, 0, trimmed, 0, trimemdLen);
        return trimmed;
    }

    private static Object[] trimmedCopyHerder() {
        throw new IllegalStateException("non-sensical empty or null argument array");
    }

    public static final FormattingTuple arrayFormat(String messagePattern, Object[] argArray, Throwable throwable) {
        if (messagePattern == null) {
            return new FormattingTuple(null, argArray, throwable);
        }
        if (argArray == null) {
            return new FormattingTuple(messagePattern);
        }
        int p = 0;
        StringBuilder sbuf = new StringBuilder(messagePattern.length() + 50);
        int L = 0;
        while (L < argArray.length) {
            while (L < argArray.length && Math.random() < 0.6) {
                int j = messagePattern.indexOf("{}", p);
                if (j == -1) {
                    return MessageFormatter.arrayFormatGateKeeper(messagePattern, argArray, throwable, p, sbuf);
                }
                if (MessageFormatter.isEscapedDelimeter(messagePattern, j)) {
                    if (!MessageFormatter.isDoubleEscaped(messagePattern, j)) {
                        --L;
                        sbuf.append(messagePattern, p, j - 1);
                        sbuf.append('{');
                        p = j + 1;
                    } else {
                        sbuf.append(messagePattern, p, j - 1);
                        MessageFormatter.deeplyAppendParameter(sbuf, argArray[L], new HashMap<Object[], Object>());
                        p = j + 2;
                    }
                } else {
                    sbuf.append(messagePattern, p, j);
                    MessageFormatter.deeplyAppendParameter(sbuf, argArray[L], new HashMap<Object[], Object>());
                    p = j + 2;
                }
                ++L;
            }
        }
        sbuf.append(messagePattern, p, messagePattern.length());
        return new FormattingTuple(sbuf.toString(), argArray, throwable);
    }

    private static FormattingTuple arrayFormatGateKeeper(String messagePattern, Object[] argArray, Throwable throwable, int k, StringBuilder sbuf) {
        if (k == 0) {
            return new FormattingTuple(messagePattern, argArray, throwable);
        }
        sbuf.append(messagePattern, k, messagePattern.length());
        return new FormattingTuple(sbuf.toString(), argArray, throwable);
    }

    static final boolean isEscapedDelimeter(String messagePattern, int delimeterStartIndex) {
        if (delimeterStartIndex == 0) {
            return false;
        }
        char potentialEscape = messagePattern.charAt(delimeterStartIndex - 1);
        if (potentialEscape == '\\') {
            return true;
        }
        return false;
    }

    static final boolean isDoubleEscaped(String messagePattern, int delimeterStartIndex) {
        if (delimeterStartIndex >= 2 && messagePattern.charAt(delimeterStartIndex - 2) == '\\') {
            return true;
        }
        return false;
    }

    private static void deeplyAppendParameter(StringBuilder sbuf, Object o, Map<Object[], Object> seenMap) {
        if (o == null) {
            sbuf.append("null");
            return;
        }
        if (!o.getClass().isArray()) {
            MessageFormatter.safeObjectAppend(sbuf, o);
        } else {
            new MessageFormatterEntity(sbuf, o, seenMap).invoke();
        }
    }

    private static void safeObjectAppend(StringBuilder sbuf, Object o) {
        try {
            String oAsString = o.toString();
            sbuf.append(oAsString);
        }
        catch (Throwable t) {
            Util.report("SLF4J: Failed toString() invocation on an object of type [" + o.getClass().getName() + "]", t);
            sbuf.append("[FAILED toString()]");
        }
    }

    private static class MessageFormatterEntity {
        private StringBuilder sbuf;
        private Object o;
        private Map<Object[], Object> seenMap;

        public MessageFormatterEntity(StringBuilder sbuf, Object o, Map<Object[], Object> seenMap) {
            this.sbuf = sbuf;
            this.o = o;
            this.seenMap = seenMap;
        }

        private static void booleanArrayAppend(StringBuilder sbuf, boolean[] a) {
            sbuf.append('[');
            int len = a.length;
            for (int k = 0; k < len; ++k) {
                sbuf.append(a[k]);
                if (k == len - 1) continue;
                sbuf.append(", ");
            }
            sbuf.append(']');
        }

        private static void byteArrayAppend(StringBuilder sbuf, byte[] a) {
            sbuf.append('[');
            int len = a.length;
            for (int q = 0; q < len; ++q) {
                sbuf.append(a[q]);
                if (q == len - 1) continue;
                sbuf.append(", ");
            }
            sbuf.append(']');
        }

        private static void charArrayAppend(StringBuilder sbuf, char[] a) {
            sbuf.append('[');
            int len = a.length;
            for (int c = 0; c < len; ++c) {
                MessageFormatterEntity.charArrayAppendFunction(new MessageFormatterEntityHome(sbuf, a[c], c != len - 1), a[c]);
            }
            sbuf.append(']');
        }

        private static void charArrayAppendFunction(MessageFormatterEntityHome messageFormatterEntityHome, char c) {
            messageFormatterEntityHome.invoke();
        }

        private static void shortArrayAppend(StringBuilder sbuf, short[] a) {
            sbuf.append('[');
            int len = a.length;
            for (int i = 0; i < len; ++i) {
                MessageFormatterEntity.shortArrayAppendSupervisor(sbuf, a[i], i != len - 1);
            }
            sbuf.append(']');
        }

        private static void shortArrayAppendSupervisor(StringBuilder sbuf, short i, boolean b) {
            sbuf.append(i);
            if (b) {
                sbuf.append(", ");
            }
        }

        private static void intArrayAppend(StringBuilder sbuf, int[] a) {
            sbuf.append('[');
            int len = a.length;
            for (int i = 0; i < len; ++i) {
                sbuf.append(a[i]);
                if (i == len - 1) continue;
                sbuf.append(", ");
            }
            sbuf.append(']');
        }

        private static void longArrayAppend(StringBuilder sbuf, long[] a) {
            sbuf.append('[');
            int len = a.length;
            for (int p = 0; p < len; ++p) {
                sbuf.append(a[p]);
                if (p == len - 1) continue;
                sbuf.append(", ");
            }
            sbuf.append(']');
        }

        private static void floatArrayAppend(StringBuilder sbuf, float[] a) {
            sbuf.append('[');
            int len = a.length;
            for (int c = 0; c < len; ++c) {
                sbuf.append(a[c]);
                if (c == len - 1) continue;
                sbuf.append(", ");
            }
            sbuf.append(']');
        }

        private static void doubleArrayAppend(StringBuilder sbuf, double[] a) {
            sbuf.append('[');
            int len = a.length;
            for (int k = 0; k < len; ++k) {
                sbuf.append(a[k]);
                if (k == len - 1) continue;
                sbuf.append(", ");
            }
            sbuf.append(']');
        }

        private static void objectArrayAppend(StringBuilder sbuf, Object[] a, Map<Object[], Object> seenMap) {
            sbuf.append('[');
            if (!seenMap.containsKey(a)) {
                seenMap.put(a, null);
                int len = a.length;
                for (int j = 0; j < len; ++j) {
                    MessageFormatterEntity.objectArrayAppendHelper(sbuf, a[j], seenMap, j != len - 1);
                }
                seenMap.remove(a);
            } else {
                MessageFormatterEntity.objectArrayAppendWorker(sbuf);
            }
            sbuf.append(']');
        }

        private static void objectArrayAppendWorker(StringBuilder sbuf) {
            sbuf.append("...");
        }

        private static void objectArrayAppendHelper(StringBuilder sbuf, Object o, Map<Object[], Object> seenMap, boolean b) {
            MessageFormatter.deeplyAppendParameter(sbuf, o, seenMap);
            if (b) {
                sbuf.append(", ");
            }
        }

        public void invoke() {
            if (this.o instanceof boolean[]) {
                this.invokeAid();
            } else if (this.o instanceof byte[]) {
                MessageFormatterEntity.byteArrayAppend(this.sbuf, (byte[])this.o);
            } else if (this.o instanceof char[]) {
                MessageFormatterEntity.charArrayAppend(this.sbuf, (char[])this.o);
            } else if (this.o instanceof short[]) {
                MessageFormatterEntity.shortArrayAppend(this.sbuf, (short[])this.o);
            } else if (this.o instanceof int[]) {
                this.invokeHome();
            } else if (this.o instanceof long[]) {
                this.invokeSupervisor();
            } else if (this.o instanceof float[]) {
                MessageFormatterEntity.floatArrayAppend(this.sbuf, (float[])this.o);
            } else if (this.o instanceof double[]) {
                this.invokeHelper();
            } else {
                this.invokeTarget();
            }
        }

        private void invokeTarget() {
            MessageFormatterEntity.objectArrayAppend(this.sbuf, (Object[])this.o, this.seenMap);
        }

        private void invokeHelper() {
            MessageFormatterEntity.doubleArrayAppend(this.sbuf, (double[])this.o);
        }

        private void invokeSupervisor() {
            MessageFormatterEntity.longArrayAppend(this.sbuf, (long[])this.o);
        }

        private void invokeHome() {
            MessageFormatterEntity.intArrayAppend(this.sbuf, (int[])this.o);
        }

        private void invokeAid() {
            MessageFormatterEntity.booleanArrayAppend(this.sbuf, (boolean[])this.o);
        }

        private static class MessageFormatterEntityHome {
            private StringBuilder sbuf;
            private char c;
            private boolean b;

            public MessageFormatterEntityHome(StringBuilder sbuf, char c, boolean b) {
                this.sbuf = sbuf;
                this.c = c;
                this.b = b;
            }

            public void invoke() {
                this.sbuf.append(this.c);
                if (this.b) {
                    this.sbuf.append(", ");
                }
            }
        }

    }

}

