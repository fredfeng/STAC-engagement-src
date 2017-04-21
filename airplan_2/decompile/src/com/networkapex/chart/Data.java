/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.chart;

import java.util.Set;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface Data {
    public String pull(String var1);

    public void place(String var1, String var2);

    public void delete(String var1);

    public boolean containsKey(String var1);

    public boolean hasData();

    public Set<String> keyAssign();

    public int size();

    public Data copy();

    public Element generateXMLElement(Document var1);
}

