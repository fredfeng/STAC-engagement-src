/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.json.simple.parser;

import edu.computerapex.json.simple.parser.ParseDeviation;
import java.io.IOException;

public interface ContentHandler {
    public void startJSON() throws ParseDeviation, IOException;

    public void endJSON() throws ParseDeviation, IOException;

    public boolean startObject() throws ParseDeviation, IOException;

    public boolean endObject() throws ParseDeviation, IOException;

    public boolean startObjectEntry(String var1) throws ParseDeviation, IOException;

    public boolean endObjectEntry() throws ParseDeviation, IOException;

    public boolean startArray() throws ParseDeviation, IOException;

    public boolean endArray() throws ParseDeviation, IOException;

    public boolean primitive(Object var1) throws ParseDeviation, IOException;
}

