/*
 * Decompiled with CFR 0_121.
 */
package infotrader.sitedatastorage;

import infotrader.dataprocessing.DocumentDisplayGenerator;
import infotrader.parser.exception.InfoTraderParserException;
import infotrader.parser.exception.InfoTraderWriterException;
import infotrader.parser.exception.WriterCancelledException;
import infotrader.parser.model.Header;
import infotrader.parser.model.InfoTrader;
import infotrader.parser.model.Source;
import infotrader.parser.model.SourceSystem;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.parser.InfoTraderParser;
import infotrader.parser.writer.InfoTraderWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

public class DocumentStore {
    Map<String, File> files = new HashMap<String, File>();

    public void loadDoc(HttpServletResponse response, String name, Map<String, FileLink> links, boolean recurse, DocumentDisplayGenerator display) throws IOException, InfoTraderParserException, InfoTraderWriterException {
        Iterator<Source> its;
        File readFile = this.getFile(name);
        InfoTraderParser gp = new InfoTraderParser();
        gp.load(readFile);
        Map<String, Source> sources = gp.infoTrader.getSources();
        Collection<Source> sourcesvalues = sources.values();
        its = sourcesvalues.iterator();
        BufferedReader br = new BufferedReader(new FileReader(readFile));
        Throwable throwable = null;
        try {
            String line;
            while ((line = br.readLine()) != null) {
                display.appendln(line);
            }
            display.end();
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
        FileLink fileLink = new FileLink(name);
        links.put(name, fileLink);
        while (its.hasNext()) {
            Source next = its.next();
            String get = next.getTitle().get(0);
            fileLink.addLink(get);
            if (!recurse) continue;
            this.loadDoc(response, get, links, recurse, display);
        }
    }

    private static String toFileLoc(String file) throws IOException {
        String encode = URLEncoder.encode(file.toString(), "UTF-8");
        File file1 = new File("reports/" + encode);
        return file1.getAbsolutePath();
    }

    public void loadSitemap(DocumentDisplayGenerator display) throws FileNotFoundException, IOException, InfoTraderWriterException {
        BufferedReader br = new BufferedReader(new FileReader("Sitemap.xml"));
        Throwable throwable = null;
        try {
            String line;
            while ((line = br.readLine()) != null) {
                display.appendln(line);
            }
            display.end();
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
    }

    public void writeDoc(InfoTrader g, String timeString) throws WriterCancelledException, UnsupportedEncodingException, IOException, InfoTraderWriterException {
        InfoTraderWriter gwriter = new InfoTraderWriter(g);
        String encode = URLEncoder.encode(g.getHeader().getSourceSystem().getProductName().toString(), "UTF-8");
        File newdoc = new File("reports/" + timeString + encode);
        gwriter.write(newdoc);
        this.registerFile(g.getHeader().getSourceSystem().getProductName().toString(), newdoc);
    }

    public void registerFile(String name, File next) {
        this.files.put(name, next);
    }

    public File getFile(String name) {
        return this.files.get(name);
    }

    public class FileLink {
        String name;
        List<String> links;

        public FileLink(String name) {
            this.name = name;
            this.links = new ArrayList<String>();
        }

        public void addLink(String link) {
            this.links.add(link);
        }
    }

}

