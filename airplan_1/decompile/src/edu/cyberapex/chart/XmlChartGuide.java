/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.chart;

import edu.cyberapex.chart.BasicData;
import edu.cyberapex.chart.Chart;
import edu.cyberapex.chart.ChartFactory;
import edu.cyberapex.chart.ChartFailure;
import edu.cyberapex.chart.Data;
import edu.cyberapex.chart.Edge;
import edu.cyberapex.chart.Vertex;
import java.util.Set;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlChartGuide
extends DefaultHandler {
    private Chart chart;
    private String curVertexName;
    private Data curData;
    private DataElement curDataEntry;
    private EdgeElement curEdge;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        if (localName.equals("graph")) {
            if (this.chart != null) {
                this.startElementHerder();
            }
            this.chart = ChartFactory.newInstance();
        } else if (localName.equals("vertex")) {
            this.startElementService(atts);
        } else if (localName.equals("data")) {
            new XmlChartGuideEngine().invoke();
        } else if (localName.equals("entry")) {
            new XmlChartGuideWorker(atts).invoke();
        } else if (localName.equals("edge")) {
            this.startElementFunction(atts);
        }
    }

    private void startElementFunction(Attributes atts) throws SAXException {
        if (this.curEdge != null) {
            this.startElementFunctionGuide();
        }
        if (this.chart == null) {
            this.startElementFunctionHandler();
        }
        this.curEdge = new EdgeElement(atts);
    }

    private void startElementFunctionHandler() throws SAXException {
        throw new SAXException("Graph must be specified before an Edge");
    }

    private void startElementFunctionGuide() throws SAXException {
        throw new SAXException("We can't handle nested edges");
    }

    private void startElementService(Attributes atts) throws SAXException {
        if (this.curVertexName != null) {
            throw new SAXException("We can't handle nested vertices");
        }
        if (this.chart == null) {
            throw new SAXException("Graph must be specified before a Vertex");
        }
        this.curVertexName = atts.getValue("name");
    }

    private void startElementHerder() throws SAXException {
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
            this.endElementSupervisor();
        } else if (localName.equals("entry")) {
            this.curData.put(this.curDataEntry.getKey(), this.curDataEntry.fetchValue());
            this.curDataEntry = null;
        } else if (localName.equals("edge")) {
            this.endElementHelp();
        }
    }

    private void endElementHelp() throws SAXException {
        try {
            int sourceVertex = this.chart.getVertexIdByName(this.curEdge.takeSrc());
            int sinkVertex = this.chart.getVertexIdByName(this.curEdge.fetchDst());
            Data data = XmlChartGuide.obtainData(this.curEdge.pullData(), this.curData);
            this.chart.addEdge(sourceVertex, sinkVertex, data);
            this.curData = null;
        }
        catch (ChartFailure e) {
            throw new SAXException(e);
        }
        this.curEdge = null;
    }

    private void endElementSupervisor() throws SAXException {
        try {
            Vertex vertex = this.chart.addVertex(this.curVertexName);
            if (this.curData != null) {
                vertex.setData(this.curData);
                this.curData = null;
            }
        }
        catch (ChartFailure e) {
            throw new SAXException(e);
        }
        this.curVertexName = null;
    }

    public Chart getChart() {
        return this.chart;
    }

    private static Data obtainData(Data edgeData, Data entryData) {
        Data data = entryData;
        if (entryData == null) {
            data = edgeData != null ? edgeData : new BasicData();
        } else if (edgeData != null) {
            for (String key : edgeData.keySet()) {
                data.put(key, edgeData.fetch(key));
            }
        }
        return data;
    }

    private class XmlChartGuideWorker {
        private Attributes atts;

        public XmlChartGuideWorker(Attributes atts) {
            this.atts = atts;
        }

        public void invoke() throws SAXException {
            if (XmlChartGuide.this.curData == null) {
                this.invokeExecutor();
            }
            if (XmlChartGuide.this.curDataEntry != null) {
                throw new SAXException("Entry tags may not be nested");
            }
            XmlChartGuide.this.curDataEntry = new DataElement(this.atts);
        }

        private void invokeExecutor() throws SAXException {
            throw new SAXException("Entry must be a child of a <data> element");
        }
    }

    private class XmlChartGuideEngine {
        private XmlChartGuideEngine() {
        }

        public void invoke() throws SAXException {
            if (XmlChartGuide.this.curVertexName == null && XmlChartGuide.this.curEdge == null) {
                throw new SAXException("Invalid <data> element; must be a child of vertex or edge");
            }
            XmlChartGuide.this.curData = new BasicData();
        }
    }

    private static class DataElement {
        private String key;
        private StringBuffer value = new StringBuffer();

        public DataElement(Attributes atts) {
            this.key = atts.getValue("key");
        }

        public String getKey() {
            return this.key;
        }

        public void addCharacters(char[] ch, int start, int length) {
            this.value.append(ch, start, length);
        }

        public String fetchValue() {
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

        public String fetchDst() {
            return this.dst;
        }

        public Data pullData() {
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

