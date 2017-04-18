/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.flightplanner;

import edu.cyberapex.flightplanner.AirFailure;
import edu.cyberapex.flightplanner.RouteMapLoader;
import edu.cyberapex.flightplanner.XmlRouteMapGuide;
import edu.cyberapex.flightplanner.framework.RouteMap;
import edu.cyberapex.flightplanner.store.AirDatabase;
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
    public RouteMap loadRouteMap(String fileName, AirDatabase database) throws FileNotFoundException, AirFailure {
        try {
            File file = new File(fileName);
            if (file.length() > 30720) {
                return this.loadRouteMapHome();
            }
            XMLReader reader = XMLReaderFactory.createXMLReader();
            XmlRouteMapGuide guide = new XmlRouteMapGuide(database);
            reader.setContentHandler(guide);
            reader.parse(new InputSource(fileName));
            return guide.takeRouteMap();
        }
        catch (SAXException e) {
            throw new AirFailure(e);
        }
        catch (IOException e) {
            throw new AirFailure(e);
        }
    }

    private RouteMap loadRouteMapHome() throws AirFailure {
        throw new AirFailure("This route map is too large for the system.");
    }

    @Override
    public List<String> takeExtensions() {
        return Arrays.asList(EXTENSIONS);
    }
}

