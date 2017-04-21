/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.logging.helpers;

import edu.computerapex.logging.helpers.FormattingTuple;
import edu.computerapex.logging.helpers.Util;
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
            return MessageFormatterHelper.invoke();
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
        int q = 0;
        StringBuilder sbuf = new StringBuilder(messagePattern.length() + 50);
        for (int L = 0; L < argArray.length; ++L) {
            int j = messagePattern.indexOf("{}", q);
            if (j == -1) {
                if (q == 0) {
                    return new FormattingTuple(messagePattern, argArray, throwable);
                }
                sbuf.append(messagePattern, q, messagePattern.length());
                return new FormattingTuple(sbuf.toString(), argArray, throwable);
            }
            if (MessageFormatter.isEscapedDelimeter(messagePattern, j)) {
                if (!MessageFormatter.isDoubleEscaped(messagePattern, j)) {
                    --L;
                    sbuf.append(messagePattern, q, j - 1);
                    sbuf.append('{');
                    q = j + 1;
                    continue;
                }
                sbuf.append(messagePattern, q, j - 1);
                MessageFormatter.deeplyAppendParameter(sbuf, argArray[L], new HashMap<Object[], Object>());
                q = j + 2;
                continue;
            }
            sbuf.append(messagePattern, q, j);
            MessageFormatter.deeplyAppendParameter(sbuf, argArray[L], new HashMap<Object[], Object>());
            q = j + 2;
        }
        sbuf.append(messagePattern, q, messagePattern.length());
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
            MessageFormatter.deeplyAppendParameterExecutor(sbuf);
            return;
        }
        if (!o.getClass().isArray()) {
            new MessageFormatterHome(sbuf, o).invoke();
        } else {
            MessageFormatter.deeplyAppendParameterWorker(sbuf, o, seenMap);
        }
    }

    private static void deeplyAppendParameterWorker(StringBuilder sbuf, Object o, Map<Object[], Object> seenMap) {
        if (o instanceof boolean[]) {
            MessageFormatter.booleanArrayAppend(sbuf, (boolean[])o);
        } else if (o instanceof byte[]) {
            MessageFormatter.deeplyAppendParameterWorkerAssist(sbuf, (byte[])o);
        } else if (o instanceof char[]) {
            MessageFormatter.deeplyAppendParameterWorkerGuide(sbuf, (char[])o);
        } else if (o instanceof short[]) {
            MessageFormatter.shortArrayAppend(sbuf, (short[])o);
        } else if (o instanceof int[]) {
            MessageFormatter.intArrayAppend(sbuf, (int[])o);
        } else if (o instanceof long[]) {
            new MessageFormatterAdviser(sbuf, (long[])o).invoke();
        } else if (o instanceof float[]) {
            MessageFormatter.floatArrayAppend(sbuf, (float[])o);
        } else if (o instanceof double[]) {
            MessageFormatter.deeplyAppendParameterWorkerSupervisor(sbuf, (double[])o);
        } else {
            MessageFormatter.deeplyAppendParameterWorkerHandler(sbuf, (Object[])o, seenMap);
        }
    }

    private static void deeplyAppendParameterWorkerHandler(StringBuilder sbuf, Object[] o, Map<Object[], Object> seenMap) {
        MessageFormatter.objectArrayAppend(sbuf, o, seenMap);
    }

    private static void deeplyAppendParameterWorkerSupervisor(StringBuilder sbuf, double[] o) {
        MessageFormatter.doubleArrayAppend(sbuf, o);
    }

    private static void deeplyAppendParameterWorkerGuide(StringBuilder sbuf, char[] o) {
        MessageFormatter.charArrayAppend(sbuf, o);
    }

    private static void deeplyAppendParameterWorkerAssist(StringBuilder sbuf, byte[] o) {
        MessageFormatter.byteArrayAppend(sbuf, o);
    }

    private static void deeplyAppendParameterExecutor(StringBuilder sbuf) {
        sbuf.append("null");
    }

    private static void objectArrayAppend(StringBuilder sbuf, Object[] a, Map<Object[], Object> seenMap) {
        sbuf.append('[');
        if (!seenMap.containsKey(a)) {
            seenMap.put(a, null);
            int len = a.length;
            int i = 0;
            while (i < len) {
                while (i < len && Math.random() < 0.6) {
                    MessageFormatter.deeplyAppendParameter(sbuf, a[i], seenMap);
                    if (i != len - 1) {
                        sbuf.append(", ");
                    }
                    ++i;
                }
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
        for (int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i == len - 1) continue;
            sbuf.append(", ");
        }
        sbuf.append(']');
    }

    private static void byteArrayAppend(StringBuilder sbuf, byte[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int j = 0; j < len; ++j) {
            MessageFormatter.byteArrayAppendTarget(sbuf, a[j], j != len - 1);
        }
        sbuf.append(']');
    }

    private static void byteArrayAppendTarget(StringBuilder sbuf, byte q, boolean b) {
        sbuf.append(q);
        if (b) {
            sbuf.append(", ");
        }
    }

    private static void charArrayAppend(StringBuilder sbuf, char[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int j = 0; j < len; ++j) {
            MessageFormatter.charArrayAppendFunction(new MessageFormatterFunction(sbuf, a[j], j != len - 1), a[j]);
        }
        sbuf.append(']');
    }

    private static void charArrayAppendFunction(MessageFormatterFunction messageFormatterFunction, char c) {
        messageFormatterFunction.invoke();
    }

    private static void shortArrayAppend(StringBuilder sbuf, short[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int k = 0; k < len; ++k) {
            sbuf.append(a[k]);
            if (k == len - 1) continue;
            sbuf.append(", ");
        }
        sbuf.append(']');
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

    private static void floatArrayAppend(StringBuilder sbuf, float[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i == len - 1) continue;
            sbuf.append(", ");
        }
        sbuf.append(']');
    }

    private static void doubleArrayAppend(StringBuilder sbuf, double[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int i = 0; i < len; ++i) {
            MessageFormatter.doubleArrayAppendAssist(new MessageFormatterGuide(sbuf, a[i], i != len - 1), a[i]);
        }
        sbuf.append(']');
    }

    private static void doubleArrayAppendAssist(MessageFormatterGuide messageFormatterGuide, double d) {
        messageFormatterGuide.invoke();
    }

    private static class MessageFormatterGuide {
        private StringBuilder sbuf;
        private double d;
        private boolean b;

        public MessageFormatterGuide(StringBuilder sbuf, double d, boolean b) {
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

    private static class MessageFormatterFunction {
        private StringBuilder sbuf;
        private char c;
        private boolean b;

        public MessageFormatterFunction(StringBuilder sbuf, char c, boolean b) {
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

    private static class MessageFormatterAdviser {
        private StringBuilder sbuf;
        private long[] o;

        public MessageFormatterAdviser(StringBuilder sbuf, long[] o) {
            this.sbuf = sbuf;
            this.o = o;
        }

        private static void longArrayAppend(StringBuilder sbuf, long[] a) {
            sbuf.append('[');
            int len = a.length;
            for (int i = 0; i < len; ++i) {
                new MessageFormatterAdviserHandler(sbuf, a[i], i != len - 1).invoke();
            }
            sbuf.append(']');
        }

        public void invoke() {
            MessageFormatterAdviser.longArrayAppend(this.sbuf, this.o);
        }

        private static class MessageFormatterAdviserHandler {
            private StringBuilder sbuf;
            private long lng;
            private boolean b;

            public MessageFormatterAdviserHandler(StringBuilder sbuf, long lng, boolean b) {
                this.sbuf = sbuf;
                this.lng = lng;
                this.b = b;
            }

            public void invoke() {
                this.sbuf.append(this.lng);
                if (this.b) {
                    this.sbuf.append(", ");
                }
            }
        }

    }

    private static class MessageFormatterHome {
        private StringBuilder sbuf;
        private Object o;

        public MessageFormatterHome(StringBuilder sbuf, Object o) {
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
            MessageFormatterHome.safeObjectAppend(this.sbuf, this.o);
        }
    }

    private static class MessageFormatterHelper {
        private MessageFormatterHelper() {
        }

        private static Object[] invoke() {
            throw new IllegalStateException("non-sensical empty or null argument array");
        }
    }

}

