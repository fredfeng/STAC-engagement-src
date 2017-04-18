/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.chart;

import java.util.Set;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface Data {
    public String fetch(String var1);

    public void put(String var1, String var2);

    public void delete(String var1);

    public boolean containsKey(String var1);

    public boolean hasData();

    public Set<String> keySet();

    public int size();

    public Data copy();

    public Element generateXMLElement(Document var1);
}

