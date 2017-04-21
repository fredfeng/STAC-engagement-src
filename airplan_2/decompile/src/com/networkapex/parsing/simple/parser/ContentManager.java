/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.parsing.simple.parser;

import com.networkapex.parsing.simple.parser.ParseRaiser;
import java.io.IOException;

public interface ContentManager {
    public void startPARSER() throws ParseRaiser, IOException;

    public void endPARSER() throws ParseRaiser, IOException;

    public boolean startObject() throws ParseRaiser, IOException;

    public boolean endObject() throws ParseRaiser, IOException;

    public boolean startObjectEntry(String var1) throws ParseRaiser, IOException;

    public boolean endObjectEntry() throws ParseRaiser, IOException;

    public boolean startArray() throws ParseRaiser, IOException;

    public boolean endArray() throws ParseRaiser, IOException;

    public boolean primitive(Object var1) throws ParseRaiser, IOException;
}

