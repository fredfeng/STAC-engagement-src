/*
 * Decompiled with CFR 0_121.
 */
package infotrader.datamodel;

import infotrader.datamodel.HyperLink;
import javax.swing.tree.DefaultMutableTreeNode;
import org.w3c.dom.Element;

public class SerializationPosition {
    public DefaultMutableTreeNode currPos;
    public Element doc;

    public SerializationPosition dup() {
        SerializationPosition statedup = new SerializationPosition();
        statedup.currPos = this.currPos;
        statedup.doc = this.doc;
        return statedup;
    }

    boolean contains(HyperLink next) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    void add(HyperLink next) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

