/*
 * Decompiled with CFR 0_121.
 */
package infotrader.dataprocessing;

import infotrader.datamodel.Directory;
import infotrader.datamodel.DocumentI;
import infotrader.datamodel.HyperLink;
import infotrader.datamodel.Node;
import infotrader.datamodel.NodeBase;
import infotrader.datamodel.SerializationPosition;
import infotrader.dataprocessing.NodeCreationException;
import infotrader.sitedatastorage.DocumentStore;
import infotrader.sitedatastorage.ReadDirStruct;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

public class SiteMapGenerator {
    public static TreeModel model;
    public static Document doc;
    String reqdate;
    DocumentStore dstore;
    ArrayList<SiteElement> tlistofelements;
    private static ArrayList<SiteElement> listofelements;
    public static List<SiteElement> items;

    public SiteMapGenerator(DocumentStore dstore) {
        this.dstore = dstore;
        this.tlistofelements = new ArrayList();
        for (SiteElement n : listofelements) {
            this.tlistofelements.add(n);
        }
    }

    public void commit_changes_to_sitemap() {
        listofelements = this.tlistofelements;
    }

    public void init(String reqdate) {
        this.populatefromCache();
        NodeBase.nodes = new HashMap<String, Node>();
        this.reqdate = reqdate;
    }

    public static void createCache(String name, String type, String parent) {
        if (items == null) {
            items = new ArrayList<SiteElement>();
        }
        boolean add = true;
        SiteElement se = new SiteElement(name, type, parent);
        if (add) {
            items.add(se);
        }
    }

    public void populatefromCache() {
        if (items == null) {
            ReadDirStruct.load(this.dstore);
        }
        Iterator<SiteElement> iterator = items.iterator();
        while (iterator.hasNext()) {
            this.tlistofelements.add(iterator.next());
        }
    }

    public void create(String name, String type, String parent) {
        boolean add = true;
        SiteElement se = new SiteElement(name, type, parent);
        for (SiteElement n : this.tlistofelements) {
            if (!n.name.equalsIgnoreCase(name) || !n.type.equalsIgnoreCase(type) || (parent != null || n.parent != null) && !parent.equalsIgnoreCase(n.parent)) continue;
            add = false;
        }
        if (add) {
            this.tlistofelements.add(se);
        }
    }

    private void createNodes(DefaultMutableTreeNode top) throws NodeCreationException {
        for (SiteElement next : this.tlistofelements) {
            this.createNode(next);
        }
    }

    private String createNode(SiteElement next) throws NodeCreationException {
        Node p;
        DefaultMutableTreeNode node = null;
        DefaultMutableTreeNode foundParent = (DefaultMutableTreeNode)model.getRoot();
        boolean exists = SiteMapGenerator.checkifExists(next.name);
        if (next.type.equalsIgnoreCase("Directory")) {
            if (!(next.parent == null || (p = SiteMapGenerator.getNode(next.parent)) != null && p instanceof Directory)) {
                throw new NodeCreationException("ERROR:Parent directory does not exist");
            }
            node = new DefaultMutableTreeNode(new Directory(next.name));
        }
        if (next.type.equalsIgnoreCase("Document")) {
            p = SiteMapGenerator.getNode(next.parent);
            if (p == null || !(p instanceof Directory)) {
                throw new NodeCreationException("ERROR:Parent directory does not exist");
            }
            node = new DefaultMutableTreeNode(new DocumentI(next.name));
        }
        if (next.type.equalsIgnoreCase("HyperLink")) {
            p = SiteMapGenerator.getNode(next.parent);
            if (p == null || !(p instanceof DocumentI)) {
                throw new NodeCreationException("ERROR:Parent document does not exist");
            }
            SiteElement siteElement = new SiteElement(next.name, null, null);
            if (this.tlistofelements.contains(siteElement)) {
                node = new DefaultMutableTreeNode(new HyperLink(next.name));
                exists = false;
            } else {
                exists = true;
            }
        }
        if (node != null && !exists && (foundParent = SiteMapGenerator.findParent(next.parent)) != null) {
            foundParent.add(node);
        }
        return "OK";
    }

    private static boolean checkifExists(String nodename) {
        return NodeBase.nodes.containsKey(nodename);
    }

    private static Node getNode(String nodename) {
        return NodeBase.nodes.get(nodename);
    }

    private static DefaultMutableTreeNode findParent(String pnodename) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
        Enumeration breadthFirstEnumeration = root.breadthFirstEnumeration();
        while (breadthFirstEnumeration.hasMoreElements() && pnodename != null) {
            DefaultMutableTreeNode nextElement = (DefaultMutableTreeNode)breadthFirstEnumeration.nextElement();
            Object n = nextElement.getUserObject();
            if (!(n instanceof Directory) && !(n instanceof DocumentI) || !n.toString().equalsIgnoreCase(pnodename)) continue;
            return nextElement;
        }
        return root;
    }

    public void genSiteMap() throws NodeCreationException {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("");
        model = new DefaultTreeModel(top);
        this.createNodes(top);
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            doc = factory.newDocumentBuilder().newDocument();
            DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
            SiteMapGenerator.serializeTree(root, doc);
            NodeList childNodes = doc.getChildNodes();
            Element item = (Element)childNodes.item(0);
            item.setAttribute("name", this.reqdate);
            Transformer tf = TransformerFactory.newInstance().newTransformer();
            tf.setOutputProperty("indent", "yes");
            tf.setOutputProperty("method", "xml");
            tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            doc.normalizeDocument();
            DOMSource domSource = new DOMSource(doc);
            StreamResult sr = new StreamResult(new File("Sitemap.xml"));
            tf.transform(domSource, sr);
        }
        catch (ParserConfigurationException | TransformerException ex) {
            ex.printStackTrace();
        }
    }

    public static void serializeNode(SerializationPosition state) {
        Element serializedXMLElement = null;
        Node userObject = (Node)state.currPos.getUserObject();
        if ((Node)state.currPos.getUserObject() instanceof HyperLink && HyperLink.links_to_existing()) {
            serializedXMLElement = ((Node)state.currPos.getUserObject()).serialize((Node)state.currPos.getUserObject(), state);
        }
        if (Directory.isa(state.currPos.getUserObject().toString(), state) && serializedXMLElement == null) {
            serializedXMLElement = Directory.exists(state.currPos.getUserObject().toString(), state);
            if (serializedXMLElement == null) {
                serializedXMLElement = userObject.serialize(userObject, state);
            }
        } else if ((Node)state.currPos.getUserObject() instanceof DocumentI && serializedXMLElement == null) {
            boolean exists = false;
            for (org.w3c.dom.Node parentNode = state.doc.getParentNode(); parentNode != null; parentNode = parentNode.getParentNode()) {
                String nodeValue;
                org.w3c.dom.Node namedItem;
                NamedNodeMap attributes = parentNode.getAttributes();
                if (attributes == null || (namedItem = attributes.getNamedItem("name")) == null || !parentNode.getNodeName().equalsIgnoreCase("doc") || !(nodeValue = namedItem.getNodeValue()).equalsIgnoreCase(state.currPos.toString())) continue;
                exists = true;
                break;
            }
            if (!exists) {
                serializedXMLElement = ((Node)state.currPos.getUserObject()).serialize((Node)state.currPos.getUserObject(), state);
            } else {
                Element oneTimeSerializedXMLElement = ((Node)state.currPos.getUserObject()).serialize((Node)state.currPos.getUserObject(), state);
                state.doc.appendChild(oneTimeSerializedXMLElement);
            }
        }
        if (serializedXMLElement != null) {
            state.doc.appendChild(serializedXMLElement);
            Enumeration kiddies = state.currPos.children();
            while (kiddies.hasMoreElements()) {
                DefaultMutableTreeNode child = (DefaultMutableTreeNode)kiddies.nextElement();
                SerializationPosition dup = state.dup();
                dup.currPos = child;
                dup.doc = serializedXMLElement;
                SiteMapGenerator.serializeNode(dup);
            }
        }
    }

    protected static void serializeTree(DefaultMutableTreeNode treeNode, Document doc) {
        Element rootElement;
        SerializationPosition s = new SerializationPosition();
        String value = treeNode.getUserObject().toString();
        s.doc = rootElement = doc.createElement("directory");
        doc.appendChild(rootElement);
        Attr attrName = doc.createAttribute("name");
        attrName.setNodeValue(value);
        rootElement.getAttributes().setNamedItem(attrName);
        Enumeration kiddies = treeNode.children();
        while (kiddies.hasMoreElements()) {
            DefaultMutableTreeNode child;
            s.currPos = child = (DefaultMutableTreeNode)kiddies.nextElement();
            SiteMapGenerator.serializeNode(s);
        }
    }

    protected static void parseTreeNode(DefaultMutableTreeNode treeNode, Element doc) {
        Attr attrName;
        Object value = treeNode.getUserObject();
        Element parentElement = null;
        if (value instanceof DocumentI) {
            parentElement = doc.getOwnerDocument().createElement("doc");
            DocumentI book = (DocumentI)value;
            attrName = doc.getOwnerDocument().createAttribute("name");
            attrName.setNodeValue(book.getCatagory());
            parentElement.getAttributes().setNamedItem(attrName);
        } else if (value instanceof Directory) {
            parentElement = doc.getOwnerDocument().createElement("directory");
            Directory book = (Directory)value;
            attrName = doc.getOwnerDocument().createAttribute("name");
            attrName.setNodeValue(book.getName());
            parentElement.getAttributes().setNamedItem(attrName);
        }
        doc.appendChild(parentElement);
        Enumeration kiddies = treeNode.children();
        while (kiddies.hasMoreElements()) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode)kiddies.nextElement();
            SiteMapGenerator.parseTreeNode(child, parentElement);
        }
    }

    public static TreeNode visitAllNodes(String term) {
        TreeNode root = (TreeNode)model.getRoot();
        return SiteMapGenerator.visitAllNodes(new TreePath(root), term);
    }

    public static TreeNode visitAllNodes(TreePath parent, String term) {
        TreeNode nodet = (TreeNode)parent.getLastPathComponent();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)nodet;
        Object userObject = node.getUserObject();
        if (nodet.toString().equalsIgnoreCase(term) && !(nodet instanceof HyperLink)) {
            return nodet;
        }
        if (node.getChildCount() >= 0) {
            Enumeration e = node.children();
            while (e.hasMoreElements()) {
                TreeNode n = (TreeNode)e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                TreeNode v = SiteMapGenerator.visitAllNodes(path, term);
                if (v == null) continue;
                return v;
            }
        }
        return null;
    }

    static {
        listofelements = new ArrayList();
    }

    static class SiteElement {
        String name;
        String type;
        String parent;

        public SiteElement(String name, String type, String parent) {
            this.name = name;
            this.type = type;
            this.parent = parent;
        }

        public boolean equals(Object o) {
            SiteElement se = (SiteElement)o;
            if (se.type.equalsIgnoreCase("Hyperlink") || se.type.equalsIgnoreCase("Directory")) {
                return false;
            }
            return this.name.equalsIgnoreCase(se.name);
        }
    }

}

