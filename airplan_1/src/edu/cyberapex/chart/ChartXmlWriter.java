/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.chart;

import edu.cyberapex.chart.Chart;
import edu.cyberapex.chart.ChartWriter;
import edu.cyberapex.chart.ChartWriterFailure;
import edu.cyberapex.chart.Data;
import edu.cyberapex.chart.Edge;
import edu.cyberapex.chart.Vertex;
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

public class ChartXmlWriter
extends ChartWriter {
    public static final String TYPE = "xml";

    @Override
    public void write(Chart chart, String filename) throws ChartWriterFailure {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document dom = db.newDocument();
            Element rootEle = dom.createElement("graph");
            for (Vertex v : chart) {
                Element vertexEle = dom.createElement("vertex");
                vertexEle.setAttribute("name", v.getName());
                rootEle.appendChild(vertexEle);
                if (!v.hasData()) continue;
                this.writeHelp(dom, v, vertexEle);
            }
            for (Vertex v : chart) {
                List<Edge> edges = chart.getEdges(v.getId());
                for (int a = 0; a < edges.size(); ++a) {
                    Edge e = edges.get(a);
                    Element edgeEle = dom.createElement("edge");
                    edgeEle.setAttribute("src", e.getSource().getName());
                    edgeEle.setAttribute("dst", e.getSink().getName());
                    edgeEle.setAttribute("weight", Double.toString(e.getWeight()));
                    rootEle.appendChild(edgeEle);
                    if (!e.hasData()) continue;
                    this.writeAid(dom, e, edgeEle);
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
            throw new ChartWriterFailure(e.getMessage());
        }
    }

    private void writeAid(Document dom, Edge e, Element edgeEle) {
        Element vertexDataEle = e.getData().generateXMLElement(dom);
        edgeEle.appendChild(vertexDataEle);
    }

    private void writeHelp(Document dom, Vertex v, Element vertexEle) {
        Element vertexDataEle = v.getData().generateXMLElement(dom);
        vertexEle.appendChild(vertexDataEle);
    }
}

