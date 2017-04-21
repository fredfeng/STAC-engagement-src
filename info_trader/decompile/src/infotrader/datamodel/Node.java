/*
 * Decompiled with CFR 0_121.
 */
package infotrader.datamodel;

import infotrader.datamodel.SerializationPosition;
import org.w3c.dom.Element;

public interface Node {
    public Element serialize(Node var1, SerializationPosition var2);

    public String getName();
}

