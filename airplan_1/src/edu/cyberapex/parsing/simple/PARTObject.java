/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.parsing.simple;

import edu.cyberapex.parsing.simple.PARTAware;
import edu.cyberapex.parsing.simple.PARTStreamAware;
import edu.cyberapex.parsing.simple.PARTValue;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PARTObject
extends HashMap
implements Map,
PARTAware,
PARTStreamAware {
    private static final long serialVersionUID = -503443796854799292L;

    public PARTObject() {
    }

    public PARTObject(Map map) {
        super(map);
    }

    public static void writePARTString(Map map, Writer out) throws IOException {
        if (map == null) {
            PARTObject.writePARTStringWorker(out);
            return;
        }
        boolean first = true;
        Iterator iter = map.entrySet().iterator();
        out.write(123);
        while (iter.hasNext()) {
            if (first) {
                first = false;
            } else {
                out.write(44);
            }
            Map.Entry entry = (Map.Entry) iter.next();
            out.write(34);
            out.write(PARTObject.escape(String.valueOf(entry.getKey())));
            out.write(34);
            out.write(58);
            PARTValue.writePARTString(entry.getValue(), out);
        }
        out.write(125);
    }

    private static void writePARTStringWorker(Writer out) throws IOException {
        out.write("null");
    }

    @Override
    public void writePARTString(Writer out) throws IOException {
        PARTObject.writePARTString(this, out);
    }

    public static String toPARTString(Map map) {
        if (map == null) {
            return "null";
        }
        StringBuffer sb = new StringBuffer();
        boolean first = true;
        Iterator iter = map.entrySet().iterator();
        sb.append('{');
        while (iter.hasNext()) {
            if (first) {
                first = false;
            } else {
                sb.append(',');
            }
            Map.Entry entry = (Map.Entry) iter.next();
            PARTObject.toPARTString(String.valueOf(entry.getKey()), entry.getValue(), sb);
        }
        sb.append('}');
        return sb.toString();
    }

    @Override
    public String toPARTString() {
        return PARTObject.toPARTString(this);
    }

    private static String toPARTString(String key, Object value, StringBuffer sb) {
        sb.append('\"');
        if (key == null) {
            sb.append("null");
        } else {
            PARTValue.escape(key, sb);
        }
        sb.append('\"').append(':');
        sb.append(PARTValue.toPARTString(value));
        return sb.toString();
    }

    @Override
    public String toString() {
        return this.toPARTString();
    }

    public static String toString(String key, Object value) {
        StringBuffer sb = new StringBuffer();
        PARTObject.toPARTString(key, value, sb);
        return sb.toString();
    }

    public static String escape(String s) {
        return PARTValue.escape(s);
    }
}

