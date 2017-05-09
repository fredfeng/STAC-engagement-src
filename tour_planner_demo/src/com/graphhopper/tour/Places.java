/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.tour;

import com.graphhopper.reader.OSMElement;
import com.graphhopper.reader.OSMInputFile;
import com.graphhopper.reader.OSMNode;
import com.graphhopper.util.CmdArgs;
import com.graphhopper.util.Helper;
import com.graphhopper.util.shapes.GHPlace;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Places {
    private static final Logger logger = LoggerFactory.getLogger(Places.class);

    private Places() {
    }

    public static List<String> names(List<? extends GHPlace> places) {
        ArrayList<String> names = new ArrayList<String>(places.size());
        for (GHPlace p : places) {
            names.add(p.getName());
        }
        return names;
    }

    public static <P extends GHPlace> Map<String, P> nameIndex(List<P> places) {
        HashMap<String, P> index = new HashMap<>();
        for (P p : places) {
            index.put(p.getName(), p);
        }
        return index;
    }

    public static <P extends GHPlace> List<P> selectByName(Map<String, P> index, List<String> names) {
        ArrayList<P> filtered = new ArrayList<>(names.size());
        for (String name : names) {
            P place = index.get(name);
            if (place == null) {
                throw new IllegalArgumentException("Could not find place \"" + name + "\"");
            }
            filtered.add(place);
        }
        return filtered;
    }

    public static <P extends GHPlace> List<P> selectByName(List<P> places, List<String> names) {
        return Places.selectByName(Places.nameIndex(places), names);
    }

    public static <P extends GHPlace> List<P> selectByName(Map<String, P> index, File namesFile) throws IOException {
        return Places.selectByName(index, Places.readLines(namesFile));
    }

    public static <P extends GHPlace> List<P> selectByName(List<P> places, File namesFile) throws IOException {
        return Places.selectByName(places, Places.readLines(namesFile));
    }

    public static List<GHPlace> load(CmdArgs args) throws IOException, XMLStreamException {
        String osmFile = args.get("places.osm", "");
        String csvFile = args.get("places.csv", "");
        if (Helper.isEmpty(osmFile) && Helper.isEmpty(csvFile)) {
            throw new IllegalArgumentException("You must specify a places file (places.osm=FILE or places.csv=FILE).");
        }
        if (!Helper.isEmpty(osmFile) && !Helper.isEmpty(csvFile)) {
            throw new IllegalArgumentException("Either places.osm or places.csv must be specified, not both.");
        }
        if (!Helper.isEmpty(osmFile)) {
            return Places.readOsm(new File(osmFile));
        }
        return Places.readCsv(new File(csvFile));
    }

    public static List<GHPlace> readOsm(File osmFile) throws IOException, XMLStreamException {
        if (!osmFile.exists()) {
            throw new IllegalStateException("Places file does not exist: " + osmFile.getAbsolutePath());
        }
        logger.info("Reading places file " + osmFile.getAbsolutePath());
        OSMInputFile in = new OSMInputFile(osmFile).open();
        Throwable throwable = null;
        try {
            List<GHPlace> list = Places.readOsm(in);
            return list;
        }
        catch (Throwable throwable2) {
            throwable = throwable2;
            throw throwable2;
        }
        finally {
            if (in != null) {
                if (throwable != null) {
                    try {
                        in.close();
                    }
                    catch (Throwable x2) {
                        throwable.addSuppressed(x2);
                    }
                } else {
                    in.close();
                }
            }
        }
    }

    private static List<GHPlace> readOsm(OSMInputFile in) throws XMLStreamException {
        OSMElement item;
        ArrayList<GHPlace> places = new ArrayList<GHPlace>();
        while ((item = in.getNext()) != null) {
            OSMNode node;
            if (!item.isType(0) || !(node = (OSMNode)item).hasTag("name", new String[0]) || !node.hasTag("place", new String[0])) continue;
            String name = node.getTag("name");
            GHPlace place = new GHPlace(node.getLat(), node.getLon()).setName(name);
            places.add(place);
        }
        logger.info("Read " + places.size() + " places");
        return places;
    }

    public static List<GHPlace> readCsv(File csvFile) throws IOException {
        if (!csvFile.exists()) {
            throw new IllegalStateException("Places file does not exist: " + csvFile.getAbsolutePath());
        }
        logger.info("Reading places file " + csvFile.getAbsolutePath());
        FileReader in = new FileReader(csvFile);
        Throwable throwable = null;
        try {
            List<GHPlace> list = Places.readCsv(new BufferedReader(in));
            return list;
        }
        catch (Throwable throwable2) {
            throwable = throwable2;
            throw throwable2;
        }
        finally {
            if (in != null) {
                if (throwable != null) {
                    try {
                        in.close();
                    }
                    catch (Throwable x2) {
                        throwable.addSuppressed(x2);
                    }
                } else {
                    in.close();
                }
            }
        }
    }

    public static List<GHPlace> readCsv(BufferedReader in) throws IOException {
        ArrayList<GHPlace> places = new ArrayList<GHPlace>();
        String expected = "Name,Lat,Lon";
        String line = in.readLine();
        if (line == null || !StringUtils.strip(line).equals(expected)) {
            throw new IllegalArgumentException("Expected header row, got " + line);
        }
        while ((line = in.readLine()) != null && !(line = StringUtils.strip(line)).equals("")) {
            places.add(Places.parseCsv(line));
        }
        logger.info("Read " + places.size() + " places");
        return places;
    }

    public static void writeCsv(List<? extends GHPlace> places, File csvFile) throws IOException {
        PrintStream out = new PrintStream(csvFile);
        Throwable throwable = null;
        try {
            Places.writeCsv(places, out);
        }
        catch (Throwable x2) {
            throwable = x2;
            throw x2;
        }
        finally {
            if (out != null) {
                if (throwable != null) {
                    try {
                        out.close();
                    }
                    catch (Throwable x2) {
                        throwable.addSuppressed(x2);
                    }
                } else {
                    out.close();
                }
            }
        }
    }

    public static void writeCsv(List<? extends GHPlace> places, PrintStream out) throws IOException {
        out.println("Name,Lat,Lon");
        for (GHPlace p : places) {
            out.println(Places.toCsv(p));
        }
    }

    private static String toCsv(GHPlace p) {
        return p.getName() + "," + p.getLat() + "," + p.getLon();
    }

    private static GHPlace parseCsv(String s) {
        String[] cols = StringUtils.split(StringUtils.strip(s), ',');
        if (cols.length != 3) {
            throw new IllegalArgumentException("Expected 3 CSV elements, got " + cols.length + ": " + s);
        }
        String name = cols[0];
        double lat = Double.parseDouble(cols[1]);
        double lon = Double.parseDouble(cols[2]);
        return new GHPlace(lat, lon).setName(name);
    }

    private static List<String> readLines(File file) throws IOException {
        ArrayList<String> lines;
        logger.info("Reading place names from file " + file.getPath());
        lines = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        Throwable throwable = null;
        try {
            String line = br.readLine();
            while (line != null) {
                lines.add(line);
                line = br.readLine();
            }
        }
        catch (Throwable x2) {
            throwable = x2;
            throw x2;
        }
        finally {
            if (br != null) {
                if (throwable != null) {
                    try {
                        br.close();
                    }
                    catch (Throwable x2) {
                        throwable.addSuppressed(x2);
                    }
                } else {
                    br.close();
                }
            }
        }
        return lines;
    }
}

