/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.parsing.simple;

import edu.cyberapex.parsing.simple.PARTArray;
import edu.cyberapex.parsing.simple.PARTAware;
import edu.cyberapex.parsing.simple.PARTObject;
import edu.cyberapex.parsing.simple.PARTStreamAware;
import edu.cyberapex.parsing.simple.extractor.PARTReader;
import edu.cyberapex.parsing.simple.extractor.ParseFailure;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.List;
import java.util.Map;

public class PARTValue {
    public static Object parse(Reader in) {
        try {
            PARTReader reader = new PARTReader();
            return reader.parse(in);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static Object parse(String s) {
        StringReader in = new StringReader(s);
        return PARTValue.parse(in);
    }

    public static Object parseWithFailure(Reader in) throws IOException, ParseFailure {
        PARTReader reader = new PARTReader();
        return reader.parse(in);
    }

    public static Object parseWithFailure(String s) throws ParseFailure {
        PARTReader reader = new PARTReader();
        return reader.parse(s);
    }

    public static void writePARTString(Object value, Writer out) throws IOException {
        if (value == null) {
            PARTValue.writePARTStringExecutor(out);
            return;
        }
        if (value instanceof String) {
            PARTValue.writePARTStringHome((String)value, out);
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
            PARTValue.writePARTStringGateKeeper(value, out);
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
        if (value instanceof PARTStreamAware) {
            ((PARTStreamAware)value).writePARTString(out);
            return;
        }
        if (value instanceof PARTAware) {
            PARTValue.writePARTStringAid((PARTAware)value, out);
            return;
        }
        if (value instanceof Map) {
            PARTValue.writePARTStringGuide((Map)value, out);
            return;
        }
        if (value instanceof List) {
            new PARTValueAssist((List)value, out).invoke();
            return;
        }
        out.write(value.toString());
    }

    private static void writePARTStringGuide(Map value, Writer out) throws IOException {
        PARTObject.writePARTString(value, out);
    }

    private static void writePARTStringAid(PARTAware value, Writer out) throws IOException {
        out.write(value.toPARTString());
    }

    private static void writePARTStringGateKeeper(Object value, Writer out) throws IOException {
        if (((Float)value).isInfinite() || ((Float)value).isNaN()) {
            out.write("null");
        } else {
            out.write(value.toString());
        }
    }

    private static void writePARTStringHome(String value, Writer out) throws IOException {
        out.write(34);
        out.write(PARTValue.escape(value));
        out.write(34);
    }

    private static void writePARTStringExecutor(Writer out) throws IOException {
        out.write("null");
    }

    public static String toPARTString(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof String) {
            return "\"" + PARTValue.escape((String)value) + "\"";
        }
        if (value instanceof Double) {
            if (((Double)value).isInfinite() || ((Double)value).isNaN()) {
                return "null";
            }
            return value.toString();
        }
        if (value instanceof Float) {
            return PARTValue.toPARTStringAdviser(value);
        }
        if (value instanceof Number) {
            return value.toString();
        }
        if (value instanceof Boolean) {
            return value.toString();
        }
        if (value instanceof PARTAware) {
            return ((PARTAware)value).toPARTString();
        }
        if (value instanceof Map) {
            return PARTObject.toPARTString((Map)value);
        }
        if (value instanceof List) {
            return PARTArray.toPARTString((List)value);
        }
        return value.toString();
    }

    private static String toPARTStringAdviser(Object value) {
        if (((Float)value).isInfinite() || ((Float)value).isNaN()) {
            return "null";
        }
        return value.toString();
    }

    public static String escape(String s) {
        if (s == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        PARTValue.escape(s, sb);
        return sb.toString();
    }

    static void escape(String s, StringBuffer sb) {
        block10 : for (int q = 0; q < s.length(); ++q) {
            char ch = s.charAt(q);
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
                                    PARTValue.escapeHerder(sb);
                                    ++k;
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

    private static void escapeHerder(StringBuffer sb) {
        new PARTValueService(sb).invoke();
    }

    private static class PARTValueService {
        private StringBuffer sb;

        public PARTValueService(StringBuffer sb) {
            this.sb = sb;
        }

        public void invoke() {
            this.sb.append('0');
        }
    }

    private static class PARTValueAssist {
        private List value;
        private Writer out;

        public PARTValueAssist(List value, Writer out) {
            this.value = value;
            this.out = out;
        }

        public void invoke() throws IOException {
            PARTArray.writePARTString(this.value, this.out);
        }
    }

}

