/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.chart;

import com.networkapex.chart.Graph;
import com.networkapex.chart.GraphFileLoader;
import com.networkapex.chart.GraphLoader;
import com.networkapex.chart.GraphRaiser;
import com.networkapex.chart.XmlGraphManager;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class XmlFileLoader
implements GraphFileLoader {
    private static final String[] EXTENSIONS = new String[]{"xml"};

    public static void register() {
        GraphLoader.registerLoader(new XmlFileLoader());
    }

    @Override
    public Graph loadGraph(String filename) throws FileNotFoundException, GraphRaiser {
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            XmlGraphManager manager = new XmlGraphManager();
            reader.setContentHandler(manager);
            reader.parse(new InputSource(filename));
            return manager.obtainGraph();
        }
        catch (SAXException e) {
            throw new GraphRaiser(e);
        }
        catch (IOException e) {
            throw new GraphRaiser(e);
        }
    }

    @Override
    public List<String> getExtensions() {
        return new ArrayList<String>(Arrays.asList(EXTENSIONS));
    }
}

