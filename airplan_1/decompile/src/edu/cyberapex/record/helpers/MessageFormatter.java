/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.record.helpers;

import edu.cyberapex.record.helpers.FormattingTuple;
import edu.cyberapex.record.helpers.Util;
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

    static final Throwable grabThrowableCandidate(Object[] argArray) {
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
        Throwable throwableCandidate = MessageFormatter.grabThrowableCandidate(argArray);
        Object[] args = argArray;
        if (throwableCandidate != null) {
            args = MessageFormatter.trimmedCopy(argArray);
        }
        return MessageFormatter.arrayFormat(messagePattern, args, throwableCandidate);
    }

    private static Object[] trimmedCopy(Object[] argArray) {
        if (argArray == null || argArray.length == 0) {
            throw new IllegalStateException("non-sensical empty or null argument array");
        }
        int trimemdLen = argArray.length - 1;
        Object[] trimmed = new Object[trimemdLen];
        System.arraycopy(argArray, 0, trimmed, 0, trimemdLen);
        return trimmed;
    }

    public static final FormattingTuple arrayFormat(String messagePattern, Object[] argArray, Throwable throwable) {
        if (messagePattern == null) {
            return new FormattingTuple(null, argArray, throwable);
        }
        if (argArray == null) {
            return new FormattingTuple(messagePattern);
        }
        int a = 0;
        StringBuilder sbuf = new StringBuilder(messagePattern.length() + 50);
        int L = 0;
        while (L < argArray.length) {
            while (L < argArray.length && Math.random() < 0.6) {
                int j = messagePattern.indexOf("{}", a);
                if (j == -1) {
                    return MessageFormatter.arrayFormatEntity(messagePattern, argArray, throwable, a, sbuf);
                }
                if (MessageFormatter.isEscapedDelimeter(messagePattern, j)) {
                    if (!MessageFormatter.isDoubleEscaped(messagePattern, j)) {
                        --L;
                        sbuf.append(messagePattern, a, j - 1);
                        sbuf.append('{');
                        a = j + 1;
                    } else {
                        sbuf.append(messagePattern, a, j - 1);
                        MessageFormatter.deeplyAppendParameter(sbuf, argArray[L], new HashMap<Object[], Object>());
                        a = j + 2;
                    }
                } else {
                    sbuf.append(messagePattern, a, j);
                    MessageFormatter.deeplyAppendParameter(sbuf, argArray[L], new HashMap<Object[], Object>());
                    a = j + 2;
                }
                ++L;
            }
        }
        sbuf.append(messagePattern, a, messagePattern.length());
        return new FormattingTuple(sbuf.toString(), argArray, throwable);
    }

    private static FormattingTuple arrayFormatEntity(String messagePattern, Object[] argArray, Throwable throwable, int k, StringBuilder sbuf) {
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
            new MessageFormatterAid(sbuf, o).invoke();
        } else {
            MessageFormatter.deeplyAppendParameterHome(sbuf, o, seenMap);
        }
    }

    private static void deeplyAppendParameterHome(StringBuilder sbuf, Object o, Map<Object[], Object> seenMap) {
        if (o instanceof boolean[]) {
            MessageFormatter.booleanArrayAppend(sbuf, (boolean[])o);
        } else if (o instanceof byte[]) {
            MessageFormatter.byteArrayAppend(sbuf, (byte[])o);
        } else if (o instanceof char[]) {
            MessageFormatter.charArrayAppend(sbuf, (char[])o);
        } else if (o instanceof short[]) {
            MessageFormatter.deeplyAppendParameterHomeTarget(sbuf, (short[])o);
        } else if (o instanceof int[]) {
            MessageFormatter.intArrayAppend(sbuf, (int[])o);
        } else if (o instanceof long[]) {
            MessageFormatter.longArrayAppend(sbuf, (long[])o);
        } else if (o instanceof float[]) {
            MessageFormatter.floatArrayAppend(sbuf, (float[])o);
        } else if (o instanceof double[]) {
            MessageFormatter.doubleArrayAppend(sbuf, (double[])o);
        } else {
            MessageFormatter.objectArrayAppend(sbuf, (Object[])o, seenMap);
        }
    }

    private static void deeplyAppendParameterHomeTarget(StringBuilder sbuf, short[] o) {
        MessageFormatter.shortArrayAppend(sbuf, o);
    }

    private static void objectArrayAppend(StringBuilder sbuf, Object[] a, Map<Object[], Object> seenMap) {
        sbuf.append('[');
        if (!seenMap.containsKey(a)) {
            seenMap.put(a, null);
            int len = a.length;
            for (int q = 0; q < len; ++q) {
                MessageFormatter.deeplyAppendParameter(sbuf, a[q], seenMap);
                if (q == len - 1) continue;
                sbuf.append(", ");
            }
            seenMap.remove(a);
        } else {
            sbuf.append("...");
        }
        sbuf.append(']');
    }

    private static void booleanArrayAppend(StringBuilder sbuf, boolean[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int p = 0; p < len; ++p) {
            sbuf.append(a[p]);
            if (p == len - 1) continue;
            sbuf.append(", ");
        }
        sbuf.append(']');
    }

    private static void byteArrayAppend(StringBuilder sbuf, byte[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int b = 0; b < len; ++b) {
            sbuf.append(a[b]);
            if (b == len - 1) continue;
            sbuf.append(", ");
        }
        sbuf.append(']');
    }

    private static void charArrayAppend(StringBuilder sbuf, char[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int j = 0; j < len; ++j) {
            MessageFormatter.charArrayAppendUtility(sbuf, a[j], j != len - 1);
        }
        sbuf.append(']');
    }

    private static void charArrayAppendUtility(StringBuilder sbuf, char c, boolean b) {
        sbuf.append(c);
        if (b) {
            sbuf.append(", ");
        }
    }

    private static void shortArrayAppend(StringBuilder sbuf, short[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int i = 0; i < len; ++i) {
            new MessageFormatterAdviser(sbuf, a[i], i != len - 1).invoke();
        }
        sbuf.append(']');
    }

    private static void intArrayAppend(StringBuilder sbuf, int[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int j = 0; j < len; ++j) {
            sbuf.append(a[j]);
            if (j == len - 1) continue;
            sbuf.append(", ");
        }
        sbuf.append(']');
    }

    private static void longArrayAppend(StringBuilder sbuf, long[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int j = 0; j < len; ++j) {
            sbuf.append(a[j]);
            if (j == len - 1) continue;
            sbuf.append(", ");
        }
        sbuf.append(']');
    }

    private static void floatArrayAppend(StringBuilder sbuf, float[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int p = 0; p < len; ++p) {
            MessageFormatter.floatArrayAppendExecutor(sbuf, a[p], p != len - 1);
        }
        sbuf.append(']');
    }

    private static void floatArrayAppendExecutor(StringBuilder sbuf, float f, boolean b) {
        sbuf.append(f);
        if (b) {
            sbuf.append(", ");
        }
    }

    private static void doubleArrayAppend(StringBuilder sbuf, double[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int j = 0; j < len; ++j) {
            MessageFormatter.doubleArrayAppendGateKeeper(new MessageFormatterEngine(sbuf, a[j], j != len - 1), a[j]);
        }
        sbuf.append(']');
    }

    private static void doubleArrayAppendGateKeeper(MessageFormatterEngine messageFormatterEngine, double d) {
        messageFormatterEngine.invoke();
    }

    private static class MessageFormatterEngine {
        private StringBuilder sbuf;
        private double d;
        private boolean b;

        public MessageFormatterEngine(StringBuilder sbuf, double d, boolean b) {
            this.sbuf = sbuf;
            this.d = d;
            this.b = b;
        }

        public void invoke() {
            this.sbuf.append(this.d);
            if (this.b) {
                this.sbuf.append(", ");
            }
        }
    }

    private static class MessageFormatterAdviser {
        private StringBuilder sbuf;
        private short i;
        private boolean b;

        public MessageFormatterAdviser(StringBuilder sbuf, short i, boolean b) {
            this.sbuf = sbuf;
            this.i = i;
            this.b = b;
        }

        public void invoke() {
            this.sbuf.append(this.i);
            if (this.b) {
                this.sbuf.append(", ");
            }
        }
    }

    private static class MessageFormatterAid {
        private StringBuilder sbuf;
        private Object o;

        public MessageFormatterAid(StringBuilder sbuf, Object o) {
            this.sbuf = sbuf;
            this.o = o;
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

        public void invoke() {
            MessageFormatterAid.safeObjectAppend(this.sbuf, this.o);
        }
    }

}

