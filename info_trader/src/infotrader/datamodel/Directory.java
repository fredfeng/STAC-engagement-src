/*
 * Decompiled with CFR 0_121.
 */
package infotrader.datamodel;

import infotrader.datamodel.DocumentI;
import infotrader.datamodel.HyperLink;
import infotrader.datamodel.Node;
import infotrader.datamodel.NodeBase;
import infotrader.datamodel.SerializationPosition;
import infotrader.dataprocessing.SiteMapGenerator;
import java.util.Enumeration;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

public class Directory
extends NodeBase {
    public Directory(String name) {
        super(name);
    }

    public static boolean isa(String name, SerializationPosition state) {
        if (state.currPos.getUserObject() instanceof HyperLink || state.currPos.getUserObject() instanceof DocumentI) {
            return false;
        }
        Object root = SiteMapGenerator.model.getRoot();
        boolean checkifdirmatches = Directory.checkifdirmatches(new Directory(root.toString()), (DefaultMutableTreeNode)root, name);
        return checkifdirmatches;
    }

    public Directory() {
    }

    private static boolean checkifdirmatches(NodeBase node, DefaultMutableTreeNode tnode, String name) {
        if (node instanceof Directory && node.getName().equalsIgnoreCase(name)) {
            return true;
        }
        Enumeration children = tnode.children();
        while (children.hasMoreElements()) {
            DefaultMutableTreeNode nextElement = (DefaultMutableTreeNode)children.nextElement();
            NodeBase chuserObject = (NodeBase)nextElement.getUserObject();
            if (!Directory.checkifdirmatches(chuserObject, nextElement, name)) continue;
            return true;
        }
        return false;
    }

    public static Element exists(String name, SerializationPosition state) {
        NodeList elementsByTagName = SiteMapGenerator.doc.getElementsByTagName("directory");
        for (int i = 0; i < elementsByTagName.getLength(); ++i) {
            org.w3c.dom.Node item = elementsByTagName.item(i);
            String toString = item.toString();
            NamedNodeMap attributes = item.getAttributes();
            org.w3c.dom.Node namedItem = attributes.getNamedItem("name");
            String nodeValue = namedItem.getNodeValue();
            if (!nodeValue.equalsIgnoreCase(state.currPos.toString())) continue;
            return Directory.serialize(state);
        }
        return null;
    }

    public static Element serialize(SerializationPosition state) {
        Element doc = state.doc;
        DefaultMutableTreeNode treeNode = state.currPos;
        Object value = treeNode.getUserObject();
        Element parentElement = null;
        parentElement = doc.getOwnerDocument().createElement("directory");
        Attr attrName = doc.getOwnerDocument().createAttribute("name");
        attrName.setNodeValue(state.currPos.toString());
        parentElement.getAttributes().setNamedItem(attrName);
        return parentElement;
    }

    @Override
    public Element serialize(Node node, SerializationPosition state) {
        Element doc = state.doc;
        DefaultMutableTreeNode treeNode = state.currPos;
        Object value = treeNode.getUserObject();
        Element parentElement = null;
        parentElement = doc.getOwnerDocument().createElement("directory");
        Directory book = (Directory)value;
        Attr attrName = doc.getOwnerDocument().createAttribute("name");
        attrName.setNodeValue(book.getName());
        parentElement.getAttributes().setNamedItem(attrName);
        return parentElement;
    }
}

