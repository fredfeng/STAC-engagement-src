/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.chart;

import com.networkapex.chart.BasicData;
import com.networkapex.chart.Data;
import com.networkapex.chart.Edge;
import com.networkapex.chart.Graph;
import com.networkapex.chart.GraphFactory;
import com.networkapex.chart.GraphRaiser;
import com.networkapex.chart.Vertex;
import java.util.Set;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlGraphManager
extends DefaultHandler {
    private Graph graph;
    private String curVertexName;
    private Data curData;
    private DataElement curDataEntry;
    private EdgeElement curEdge;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        if (localName.equals("graph")) {
            if (this.graph != null) {
                this.startElementHelp();
            }
            this.graph = GraphFactory.newInstance();
        } else if (localName.equals("vertex")) {
            if (this.curVertexName != null) {
                this.startElementHerder();
            }
            if (this.graph == null) {
                throw new SAXException("Graph must be specified before a Vertex");
            }
            this.curVertexName = atts.getValue("name");
        } else if (localName.equals("data")) {
            this.startElementGuide();
        } else if (localName.equals("entry")) {
            this.startElementEntity(atts);
        } else if (localName.equals("edge")) {
            this.startElementService(atts);
        }
    }

    private void startElementService(Attributes atts) throws SAXException {
        if (this.curEdge != null) {
            this.startElementServiceHerder();
        }
        if (this.graph == null) {
            throw new SAXException("Graph must be specified before an Edge");
        }
        this.curEdge = new EdgeElement(atts);
    }

    private void startElementServiceHerder() throws SAXException {
        new XmlGraphManagerHelper().invoke();
    }

    private void startElementEntity(Attributes atts) throws SAXException {
        if (this.curData == null) {
            this.startElementEntityGuide();
        }
        if (this.curDataEntry != null) {
            this.startElementEntityHome();
        }
        this.curDataEntry = new DataElement(atts);
    }

    private void startElementEntityHome() throws SAXException {
        throw new SAXException("Entry tags may not be nested");
    }

    private void startElementEntityGuide() throws SAXException {
        throw new SAXException("Entry must be a child of a <data> element");
    }

    private void startElementGuide() throws SAXException {
        if (this.curVertexName == null && this.curEdge == null) {
            this.startElementGuideSupervisor();
        }
        this.curData = new BasicData();
    }

    private void startElementGuideSupervisor() throws SAXException {
        throw new SAXException("Invalid <data> element; must be a child of vertex or edge");
    }

    private void startElementHerder() throws SAXException {
        throw new SAXException("We can't handle nested vertices");
    }

    private void startElementHelp() throws SAXException {
        throw new SAXException("We can't handle nested graphs");
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (this.curDataEntry != null) {
            this.curDataEntry.addCharacters(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equals("vertex")) {
            this.endElementExecutor();
        } else if (localName.equals("entry")) {
            this.endElementHome();
        } else if (localName.equals("edge")) {
            try {
                int sourceVertex = this.graph.takeVertexIdByName(this.curEdge.takeSrc());
                int sinkVertex = this.graph.takeVertexIdByName(this.curEdge.getDst());
                Data data = XmlGraphManager.pullData(this.curEdge.grabData(), this.curData);
                this.graph.addEdge(sourceVertex, sinkVertex, data);
                this.curData = null;
            }
            catch (GraphRaiser e) {
                throw new SAXException(e);
            }
            this.curEdge = null;
        }
    }

    private void endElementHome() {
        this.curData.place(this.curDataEntry.grabKey(), this.curDataEntry.getValue());
        this.curDataEntry = null;
    }

    private void endElementExecutor() throws SAXException {
        try {
            Vertex vertex = this.graph.addVertex(this.curVertexName);
            if (this.curData != null) {
                this.endElementExecutorTarget(vertex);
            }
        }
        catch (GraphRaiser e) {
            throw new SAXException(e);
        }
        this.curVertexName = null;
    }

    private void endElementExecutorTarget(Vertex vertex) {
        vertex.setData(this.curData);
        this.curData = null;
    }

    public Graph obtainGraph() {
        return this.graph;
    }

    private static Data pullData(Data edgeData, Data entryData) {
        Data data = entryData;
        if (entryData == null) {
            data = edgeData != null ? edgeData : new BasicData();
        } else if (edgeData != null) {
            for (String key : edgeData.keyAssign()) {
                XmlGraphManager.pullDataGuide(edgeData, data, key);
            }
        }
        return data;
    }

    private static void pullDataGuide(Data edgeData, Data data, String key) {
        data.place(key, edgeData.pull(key));
    }

    private class XmlGraphManagerHelper {
        private XmlGraphManagerHelper() {
        }

        public void invoke() throws SAXException {
            throw new SAXException("We can't handle nested edges");
        }
    }

    private static class DataElement {
        private String key;
        private StringBuffer value = new StringBuffer();

        public DataElement(Attributes atts) {
            this.key = atts.getValue("key");
        }

        public String grabKey() {
            return this.key;
        }

        public void addCharacters(char[] ch, int start, int length) {
            this.value.append(ch, start, length);
        }

        public String getValue() {
            return this.value.toString();
        }
    }

    private static class EdgeElement {
        private String src;
        private String dst;
        private String weight;

        EdgeElement(Attributes atts) {
            this.src = atts.getValue("src");
            this.dst = atts.getValue("dst");
            this.weight = atts.getValue("weight");
        }

        public String takeSrc() {
            return this.src;
        }

        public String getDst() {
            return this.dst;
        }

        public Data grabData() {
            BasicData data = null;
            if (this.weight != null) {
                try {
                    double value = Double.parseDouble(this.weight);
                    data = new BasicData(value);
                }
                catch (NumberFormatException value) {
                    // empty catch block
                }
            }
            return data != null ? data : new BasicData();
        }
    }

}

