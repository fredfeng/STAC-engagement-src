/*
 * Decompiled with CFR 0_121.
 */
package infotrader.datamodel;

import infotrader.datamodel.Node;
import infotrader.datamodel.SerializationPosition;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Element;

public abstract class NodeBase
implements Node {
    public static Map<String, Node> nodes = new HashMap<String, Node>();
    String name;

    public NodeBase() {
        this.name = null;
    }

    public NodeBase(String name) {
        this.name = name;
        nodes.put(name, this);
    }

    @Override
    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name;
    }

    @Override
    public abstract Element serialize(Node var1, SerializationPosition var2);

    void children() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

