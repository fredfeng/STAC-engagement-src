/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.chart;

import edu.cyberapex.chart.Chart;
import edu.cyberapex.chart.ChartFailure;
import edu.cyberapex.chart.ChartFileLoader;
import edu.cyberapex.chart.ChartLoader;
import edu.cyberapex.chart.XmlChartGuide;
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
implements ChartFileLoader {
    private static final String[] EXTENSIONS = new String[]{"xml"};

    public static void register() {
        ChartLoader.registerLoader(new XmlFileLoader());
    }

    @Override
    public Chart loadChart(String filename) throws FileNotFoundException, ChartFailure {
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            XmlChartGuide guide = new XmlChartGuide();
            reader.setContentHandler(guide);
            reader.parse(new InputSource(filename));
            return guide.getChart();
        }
        catch (SAXException e) {
            throw new ChartFailure(e);
        }
        catch (IOException e) {
            throw new ChartFailure(e);
        }
    }

    @Override
    public List<String> fetchExtensions() {
        return new ArrayList<String>(Arrays.asList(EXTENSIONS));
    }
}

