/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.airplan;

import com.networkapex.airplan.AirRaiser;
import com.networkapex.airplan.RouteMapLoader;
import com.networkapex.airplan.XmlRouteMapManager;
import com.networkapex.airplan.prototype.RouteMap;
import com.networkapex.airplan.save.AirDatabase;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class XmlFileLoader
implements RouteMapLoader {
    private static final int MAX_FILE_LENGTH = 30720;
    private static final String[] EXTENSIONS = new String[]{"xml"};

    @Override
    public RouteMap loadRouteMap(String fileName, AirDatabase database) throws FileNotFoundException, AirRaiser {
        try {
            File file = new File(fileName);
            if (file.length() > 30720) {
                return this.loadRouteMapHome();
            }
            XMLReader reader = XMLReaderFactory.createXMLReader();
            XmlRouteMapManager manager = new XmlRouteMapManager(database);
            reader.setContentHandler(manager);
            reader.parse(new InputSource(fileName));
            return manager.getRouteMap();
        }
        catch (SAXException e) {
            throw new AirRaiser(e);
        }
        catch (IOException e) {
            throw new AirRaiser(e);
        }
    }

    private RouteMap loadRouteMapHome() throws AirRaiser {
        throw new AirRaiser("This route map is too large for the system.");
    }

    @Override
    public List<String> takeExtensions() {
        return Arrays.asList(EXTENSIONS);
    }
}

