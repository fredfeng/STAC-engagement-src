/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.json.simple;

import edu.computerapex.json.simple.JSONArray;
import edu.computerapex.json.simple.JSONAware;
import edu.computerapex.json.simple.JSONObject;
import edu.computerapex.json.simple.JSONStreamAware;
import edu.computerapex.json.simple.parser.JSONRetriever;
import edu.computerapex.json.simple.parser.ParseDeviation;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.List;
import java.util.Map;

public class JSONValue {
    public static Object parse(Reader in) {
        try {
            JSONRetriever retriever = new JSONRetriever();
            return retriever.parse(in);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static Object parse(String s) {
        StringReader in = new StringReader(s);
        return JSONValue.parse(in);
    }

    public static Object parseWithDeviation(Reader in) throws IOException, ParseDeviation {
        JSONRetriever retriever = new JSONRetriever();
        return retriever.parse(in);
    }

    public static Object parseWithDeviation(String s) throws ParseDeviation {
        JSONRetriever retriever = new JSONRetriever();
        return retriever.parse(s);
    }

    public static void writeJSONString(Object value, Writer out) throws IOException {
        if (value == null) {
            new JSONValueHelper(out).invoke();
            return;
        }
        if (value instanceof String) {
            JSONValue.writeJSONStringUtility((String)value, out);
            return;
        }
        if (value instanceof Double) {
            JSONValue.writeJSONStringEngine(value, out);
            return;
        }
        if (value instanceof Float) {
            JSONValue.writeJSONStringFunction(value, out);
            return;
        }
        if (value instanceof Number) {
            out.write(value.toString());
            return;
        }
        if (value instanceof Boolean) {
            JSONValue.writeJSONStringAdviser(value, out);
            return;
        }
        if (value instanceof JSONStreamAware) {
            ((JSONStreamAware)value).writeJSONString(out);
            return;
        }
        if (value instanceof JSONAware) {
            out.write(((JSONAware)value).toJSONString());
            return;
        }
        if (value instanceof Map) {
            new JSONValueService((Map)value, out).invoke();
            return;
        }
        if (value instanceof List) {
            JSONArray.writeJSONString((List)value, out);
            return;
        }
        out.write(value.toString());
    }

    private static void writeJSONStringAdviser(Object value, Writer out) throws IOException {
        out.write(value.toString());
    }

    private static void writeJSONStringFunction(Object value, Writer out) throws IOException {
        if (((Float)value).isInfinite() || ((Float)value).isNaN()) {
            out.write("null");
        } else {
            out.write(value.toString());
        }
    }

    private static void writeJSONStringEngine(Object value, Writer out) throws IOException {
        if (((Double)value).isInfinite() || ((Double)value).isNaN()) {
            out.write("null");
        } else {
            out.write(value.toString());
        }
    }

    private static void writeJSONStringUtility(String value, Writer out) throws IOException {
        out.write(34);
        out.write(JSONValue.escape(value));
        out.write(34);
    }

    public static String toJSONString(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof String) {
            return "\"" + JSONValue.escape((String)value) + "\"";
        }
        if (value instanceof Double) {
            if (((Double)value).isInfinite() || ((Double)value).isNaN()) {
                return "null";
            }
            return value.toString();
        }
        if (value instanceof Float) {
            if (((Float)value).isInfinite() || ((Float)value).isNaN()) {
                return "null";
            }
            return value.toString();
        }
        if (value instanceof Number) {
            return value.toString();
        }
        if (value instanceof Boolean) {
            return value.toString();
        }
        if (value instanceof JSONAware) {
            return ((JSONAware)value).toJSONString();
        }
        if (value instanceof Map) {
            return JSONObject.toJSONString((Map)value);
        }
        if (value instanceof List) {
            return JSONArray.toJSONString((List)value);
        }
        return value.toString();
    }

    public static String escape(String s) {
        if (s == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        JSONValue.escape(s, sb);
        return sb.toString();
    }

    static void escape(String s, StringBuffer sb) {
        block10 : for (int c = 0; c < s.length(); ++c) {
            char ch = s.charAt(c);
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
                        for (int k = 0; k < 4 - ss.length(); ++k) {
                            sb.append('0');
                        }
                        sb.append(ss.toUpperCase());
                        continue block10;
                    }
                    JSONValue.escapeCoordinator(sb, ch);
                }
            }
        }
    }

    private static void escapeCoordinator(StringBuffer sb, char ch) {
        sb.append(ch);
    }

    private static class JSONValueService {
        private Map value;
        private Writer out;

        public JSONValueService(Map value, Writer out) {
            this.value = value;
            this.out = out;
        }

        public void invoke() throws IOException {
            JSONObject.writeJSONString(this.value, this.out);
        }
    }

    private static class JSONValueHelper {
        private Writer out;

        public JSONValueHelper(Writer out) {
            this.out = out;
        }

        public void invoke() throws IOException {
            this.out.write("null");
        }
    }

}

