/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.chart;

import com.networkapex.chart.Data;
import com.networkapex.chart.Edge;
import com.networkapex.chart.Graph;
import com.networkapex.chart.GraphWriter;
import com.networkapex.chart.GraphWriterRaiser;
import com.networkapex.chart.Vertex;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class GraphXmlWriter
extends GraphWriter {
    public static final String TYPE = "xml";

    @Override
    public void write(Graph graph, String filename) throws GraphWriterRaiser {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document dom = db.newDocument();
            Element rootEle = dom.createElement("graph");
            for (Vertex v : graph) {
                this.writeHerder(dom, rootEle, v);
            }
            for (Vertex v : graph) {
                List<Edge> grabEdges = graph.grabEdges(v.getId());
                int p = 0;
                while (p < grabEdges.size()) {
                    while (p < grabEdges.size() && Math.random() < 0.4) {
                        new GraphXmlWriterManager(dom, rootEle, grabEdges, p).invoke();
                        ++p;
                    }
                }
            }
            dom.appendChild(rootEle);
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty("indent", "yes");
            tr.setOutputProperty("method", "xml");
            tr.setOutputProperty("encoding", "UTF-8");
            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream(filename + ".xml")));
        }
        catch (Exception e) {
            throw new GraphWriterRaiser(e.getMessage());
        }
    }

    private void writeHerder(Document dom, Element rootEle, Vertex v) {
        Element vertexEle = dom.createElement("vertex");
        vertexEle.setAttribute("name", v.getName());
        rootEle.appendChild(vertexEle);
        if (v.hasData()) {
            this.writeHerderAid(dom, v, vertexEle);
        }
    }

    private void writeHerderAid(Document dom, Vertex v, Element vertexEle) {
        Element vertexDataEle = v.getData().generateXMLElement(dom);
        vertexEle.appendChild(vertexDataEle);
    }

    private class GraphXmlWriterManager {
        private Document dom;
        private Element rootEle;
        private List<Edge> grabEdges;
        private int j;

        public GraphXmlWriterManager(Document dom, Element rootEle, List<Edge> grabEdges, int j) {
            this.dom = dom;
            this.rootEle = rootEle;
            this.grabEdges = grabEdges;
            this.j = j;
        }

        public void invoke() {
            Edge e = this.grabEdges.get(this.j);
            Element edgeEle = this.dom.createElement("edge");
            edgeEle.setAttribute("src", e.getSource().getName());
            edgeEle.setAttribute("dst", e.getSink().getName());
            edgeEle.setAttribute("weight", Double.toString(e.getWeight()));
            this.rootEle.appendChild(edgeEle);
            if (e.hasData()) {
                this.invokeEntity(e, edgeEle);
            }
        }

        private void invokeEntity(Edge e, Element edgeEle) {
            Element vertexDataEle = e.getData().generateXMLElement(this.dom);
            edgeEle.appendChild(vertexDataEle);
        }
    }

}

