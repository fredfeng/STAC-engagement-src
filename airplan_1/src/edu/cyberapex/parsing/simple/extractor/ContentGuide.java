/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.parsing.simple.extractor;

import edu.cyberapex.parsing.simple.extractor.ParseFailure;
import java.io.IOException;

public interface ContentGuide {
    public void startPART() throws ParseFailure, IOException;

    public void endPART() throws ParseFailure, IOException;

    public boolean startObject() throws ParseFailure, IOException;

    public boolean endObject() throws ParseFailure, IOException;

    public boolean startObjectEntry(String var1) throws ParseFailure, IOException;

    public boolean endObjectEntry() throws ParseFailure, IOException;

    public boolean startArray() throws ParseFailure, IOException;

    public boolean endArray() throws ParseFailure, IOException;

    public boolean primitive(Object var1) throws ParseFailure, IOException;
}

