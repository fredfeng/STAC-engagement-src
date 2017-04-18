/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.parsing.simple;

import edu.cyberapex.parsing.simple.PARTAware;
import edu.cyberapex.parsing.simple.PARTStreamAware;
import edu.cyberapex.parsing.simple.PARTValue;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PARTArray
extends ArrayList
implements List,
PARTAware,
PARTStreamAware {
    private static final long serialVersionUID = 3957988303675231981L;

    public static void writePARTString(List list, Writer out) throws IOException {
        if (list == null) {
            out.write("null");
            return;
        }
        boolean first = true;
        Iterator iter = list.iterator();
        out.write(91);
        while (iter.hasNext()) {
            if (first) {
                first = false;
            } else {
                out.write(44);
            }
            Object value = iter.next();
            if (value == null) {
                out.write("null");
                continue;
            }
            PARTValue.writePARTString(value, out);
        }
        out.write(93);
    }

    @Override
    public void writePARTString(Writer out) throws IOException {
        PARTArray.writePARTString(this, out);
    }

    public static String toPARTString(List list) {
        if (list == null) {
            return "null";
        }
        boolean first = true;
        StringBuffer sb = new StringBuffer();
        Iterator iter = list.iterator();
        sb.append('[');
        while (iter.hasNext()) {
            if (first) {
                first = false;
            } else {
                sb.append(',');
            }
            Object value = iter.next();
            if (value == null) {
                PARTArray.toPARTStringHome(sb);
                continue;
            }
            sb.append(PARTValue.toPARTString(value));
        }
        sb.append(']');
        return sb.toString();
    }

    private static void toPARTStringHome(StringBuffer sb) {
        sb.append("null");
    }

    @Override
    public String toPARTString() {
        return PARTArray.toPARTString(this);
    }

    @Override
    public String toString() {
        return this.toPARTString();
    }
}

