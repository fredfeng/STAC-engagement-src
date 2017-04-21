/*
 * Decompiled with CFR 0_121.
 */
package infotrader.datamodel;

import infotrader.datamodel.Directory;
import infotrader.datamodel.DocumentI;
import infotrader.datamodel.Node;
import infotrader.datamodel.NodeBase;
import infotrader.datamodel.SerializationPosition;
import infotrader.dataprocessing.SiteMapGenerator;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

public class HyperLink
extends Directory {
    static int id = 0;

    public static boolean links_to_directory() {
        return false;
    }

    public static boolean links_to_existing() {
        return true;
    }

    public HyperLink(String linkstr) {
        String idstr = "-" + id;
        this.name = linkstr + idstr;
        ++id;
    }

    @Override
    public Element serialize(Node node, SerializationPosition state) {
        int lastIndexOf;
        Attr attrName;
        NodeBase dir;
        String name1 = node.getName();
        String namenorm = name1.substring(0, lastIndexOf = name1.lastIndexOf(45));
        Node get = NodeBase.nodes.get(namenorm);
        if (get == null) {
            return null;
        }
        Element doc = state.doc;
        TreeNode thenode = SiteMapGenerator.visitAllNodes(get.getName());
        DefaultMutableTreeNode noded = (DefaultMutableTreeNode)thenode;
        Object v = noded.getUserObject();
        Element parentElement = null;
        state.currPos = noded;
        if (v instanceof Directory) {
            dir = (Directory)v;
            attrName = doc.getOwnerDocument().createAttribute("url");
            attrName.setNodeValue(dir.getName());
            parentElement = doc.getOwnerDocument().createElement("link");
            parentElement.getAttributes().setNamedItem(attrName);
        }
        if (v instanceof DocumentI) {
            dir = (DocumentI)v;
            attrName = doc.getOwnerDocument().createAttribute("url");
            attrName.setNodeValue(dir.getName());
            parentElement = doc.getOwnerDocument().createElement("link");
            parentElement.getAttributes().setNamedItem(attrName);
        }
        return parentElement;
    }
}

