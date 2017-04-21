/*
 * Decompiled with CFR 0_121.
 */
package infotrader.datamodel;

import infotrader.datamodel.HyperLink;
import infotrader.datamodel.Node;
import infotrader.datamodel.NodeBase;
import infotrader.datamodel.SerializationPosition;
import java.util.ArrayList;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

public class DocumentI
extends NodeBase {
    List<HyperLink> link;
    SerializationPosition state;
    private final String catagory;

    public DocumentI(String cat) {
        super(cat);
        this.catagory = cat;
        this.link = new ArrayList<HyperLink>();
    }

    public String getCatagory() {
        return this.catagory;
    }

    @Override
    public Element serialize(Node node, SerializationPosition state) {
        Element doc = state.doc;
        DefaultMutableTreeNode treeNode = state.currPos;
        Object value = treeNode.getUserObject();
        Element parentElement = null;
        parentElement = doc.getOwnerDocument().createElement("doc");
        DocumentI book = (DocumentI)value;
        Attr attrName = doc.getOwnerDocument().createAttribute("name");
        attrName.setNodeValue(book.getCatagory());
        parentElement.getAttributes().setNamedItem(attrName);
        this.state = state;
        for (HyperLink next : this.link) {
            if (!this.state.contains(next)) continue;
            state.add(next);
            next.serialize(next, state);
        }
        return parentElement;
    }

    public void addLink(String linkstr) {
        HyperLink hlink = new HyperLink(linkstr);
        this.link.add(hlink);
    }
}

