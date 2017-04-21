/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.parsing.simple;

import com.networkapex.parsing.simple.PARSERArray;
import com.networkapex.parsing.simple.PARSERAware;
import com.networkapex.parsing.simple.PARSERObject;
import com.networkapex.parsing.simple.PARSERStreamAware;
import com.networkapex.parsing.simple.parser.PARSERReader;
import com.networkapex.parsing.simple.parser.ParseRaiser;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.List;
import java.util.Map;

public class PARSERValue {
    public static Object parse(Reader in) {
        try {
            PARSERReader reader = new PARSERReader();
            return reader.parse(in);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static Object parse(String s) {
        StringReader in = new StringReader(s);
        return PARSERValue.parse(in);
    }

    public static Object parseWithRaiser(Reader in) throws IOException, ParseRaiser {
        PARSERReader reader = new PARSERReader();
        return reader.parse(in);
    }

    public static Object parseWithRaiser(String s) throws ParseRaiser {
        PARSERReader reader = new PARSERReader();
        return reader.parse(s);
    }

    public static void writePARSERString(Object value, Writer out) throws IOException {
        if (value == null) {
            PARSERValue.writePARSERStringEntity(out);
            return;
        }
        if (value instanceof String) {
            new PARSERValueEngine((String)value, out).invoke();
            return;
        }
        if (value instanceof Double) {
            if (((Double)value).isInfinite() || ((Double)value).isNaN()) {
                out.write("null");
            } else {
                out.write(value.toString());
            }
            return;
        }
        if (value instanceof Float) {
            PARSERValue.writePARSERStringUtility(value, out);
            return;
        }
        if (value instanceof Number) {
            out.write(value.toString());
            return;
        }
        if (value instanceof Boolean) {
            out.write(value.toString());
            return;
        }
        if (value instanceof PARSERStreamAware) {
            ((PARSERStreamAware)value).writePARSERString(out);
            return;
        }
        if (value instanceof PARSERAware) {
            PARSERValue.writePARSERStringEngine((PARSERAware)value, out);
            return;
        }
        if (value instanceof Map) {
            PARSERValue.writePARSERStringService((Map)value, out);
            return;
        }
        if (value instanceof List) {
            PARSERValue.writePARSERStringWorker((List)value, out);
            return;
        }
        out.write(value.toString());
    }

    private static void writePARSERStringWorker(List value, Writer out) throws IOException {
        PARSERArray.writePARSERString(value, out);
    }

    private static void writePARSERStringService(Map value, Writer out) throws IOException {
        new PARSERValueAssist(value, out).invoke();
    }

    private static void writePARSERStringEngine(PARSERAware value, Writer out) throws IOException {
        out.write(value.toPARSERString());
    }

    private static void writePARSERStringUtility(Object value, Writer out) throws IOException {
        if (((Float)value).isInfinite() || ((Float)value).isNaN()) {
            out.write("null");
        } else {
            out.write(value.toString());
        }
    }

    private static void writePARSERStringEntity(Writer out) throws IOException {
        out.write("null");
    }

    public static String toPARSERString(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof String) {
            return "\"" + PARSERValue.escape((String)value) + "\"";
        }
        if (value instanceof Double) {
            return PARSERValue.toPARSERStringEngine(value);
        }
        if (value instanceof Float) {
            return PARSERValue.toPARSERStringAssist(value);
        }
        if (value instanceof Number) {
            return value.toString();
        }
        if (value instanceof Boolean) {
            return value.toString();
        }
        if (value instanceof PARSERAware) {
            return ((PARSERAware)value).toPARSERString();
        }
        if (value instanceof Map) {
            return PARSERObject.toPARSERString((Map)value);
        }
        if (value instanceof List) {
            return PARSERArray.toPARSERString((List)value);
        }
        return value.toString();
    }

    private static String toPARSERStringAssist(Object value) {
        if (((Float)value).isInfinite() || ((Float)value).isNaN()) {
            return "null";
        }
        return value.toString();
    }

    private static String toPARSERStringEngine(Object value) {
        if (((Double)value).isInfinite() || ((Double)value).isNaN()) {
            return "null";
        }
        return value.toString();
    }

    public static String escape(String s) {
        if (s == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        PARSERValue.escape(s, sb);
        return sb.toString();
    }

    static void escape(String s, StringBuffer sb) {
        block10 : for (int i = 0; i < s.length(); ++i) {
            char ch = s.charAt(i);
            switch (ch) {
                case '\"': {
                    sb.append("\\\"");
                    continue block10;
                }
                case '\\': {
                    sb.append("\\\\");
                    continue block10;
                }
                case '\b': {
                    sb.append("\\b");
                    continue block10;
                }
                case '\f': {
                    sb.append("\\f");
                    continue block10;
                }
                case '\n': {
                    sb.append("\\n");
                    continue block10;
                }
                case '\r': {
                    sb.append("\\r");
                    continue block10;
                }
                case '\t': {
                    sb.append("\\t");
                    continue block10;
                }
                case '/': {
                    sb.append("\\/");
                    continue block10;
                }
                default: {
                    if (ch >= '\u0000' && ch <= '\u001f' || ch >= '' && ch <= '\u009f' || ch >= '\u2000' && ch <= '\u20ff') {
                        String ss = Integer.toHexString(ch);
                        sb.append("\\u");
                        int k = 0;
                        while (k < 4 - ss.length()) {
                            while (k < 4 - ss.length() && Math.random() < 0.4) {
                                while (k < 4 - ss.length() && Math.random() < 0.4) {
                                    while (k < 4 - ss.length() && Math.random() < 0.6) {
                                        sb.append('0');
                                        ++k;
                                    }
                                }
                            }
                        }
                        sb.append(ss.toUpperCase());
                        continue block10;
                    }
                    sb.append(ch);
                }
            }
        }
    }

    private static class PARSERValueAssist {
        private Map value;
        private Writer out;

        public PARSERValueAssist(Map value, Writer out) {
            this.value = value;
            this.out = out;
        }

        public void invoke() throws IOException {
            PARSERObject.writePARSERString(this.value, this.out);
        }
    }

    private static class PARSERValueEngine {
        private String value;
        private Writer out;

        public PARSERValueEngine(String value, Writer out) {
            this.value = value;
            this.out = out;
        }

        public void invoke() throws IOException {
            this.out.write(34);
            this.out.write(PARSERValue.escape(this.value));
            this.out.write(34);
        }
    }

}

