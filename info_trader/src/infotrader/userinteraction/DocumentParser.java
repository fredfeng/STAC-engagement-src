/*
 * Decompiled with CFR 0_121.
 */
package infotrader.userinteraction;

import infotrader.dataprocessing.DocumentDisplayGenerator;
import infotrader.dataprocessing.NodeCreationException;
import infotrader.dataprocessing.SiteMapGenerator;
import infotrader.messaging.controller.module.RunInfoTrader;
import infotrader.parser.exception.InfoTraderParserException;
import infotrader.parser.exception.InfoTraderWriterException;
import infotrader.parser.exception.WriterCancelledException;
import infotrader.parser.model.Header;
import infotrader.parser.model.InfoTrader;
import infotrader.parser.model.Source;
import infotrader.parser.model.SourceSystem;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.parser.InfoTraderParser;
import infotrader.sitedatastorage.DocumentStore;
import infotrader.sitedatastorage.ReadDirStruct;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DocumentParser
extends HttpServlet {
    SiteMapGenerator te;
    DocumentStore dstore = new DocumentStore();

    public DocumentParser() throws IOException, NodeCreationException {
        this.te = new SiteMapGenerator(this.dstore);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String reqdate = sdf.format(cal.getTime());
        this.te.init(reqdate);
        this.te.genSiteMap();
        this.te.commit_changes_to_sitemap();
    }

    @Override
    protected synchronized void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String path;
            StringBuffer requestURL = request.getRequestURL();
            URL url = new URL(requestURL.toString());
            System.out.println("path:" + url.getPath());
            switch (path = url.getPath()) {
                case "/gdoc.cgi": {
                    this.getDoc(request, response);
                    break;
                }
                case "/doc.cgi": {
                    this.putDoc(request, response);
                    break;
                }
                default: {
                    throw new IllegalArgumentException("ERROR:Unknown request type");
                }
            }
        }
        catch (InfoTraderParserException ex) {
            response.getWriter().println(ex.getMessage());
        }
        catch (InfoTraderWriterException ex) {
            response.getWriter().println(ex.getMessage());
        }
        catch (NodeCreationException ex) {
            response.getWriter().println(ex.getMessage());
        }
    }

    private void getDoc(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, InfoTraderParserException, InfoTraderWriterException {
        boolean recurse = false;
        String name = request.getParameter("name");
        DocumentDisplayGenerator display = new DocumentDisplayGenerator();
        if (name.startsWith("Sitemap")) {
            this.dstore.loadSitemap(display);
        } else {
            String getAll = request.getParameter("getAll");
            if (getAll.equalsIgnoreCase("true")) {
                recurse = true;
            }
            HashMap<String, DocumentStore.FileLink> links = new HashMap<String, DocumentStore.FileLink>();
            this.dstore.loadDoc(response, name, links, recurse, display);
        }
        response.getWriter().print(display.getResult());
    }

    private void putDoc(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, InfoTraderParserException, WriterCancelledException, InfoTraderWriterException, NodeCreationException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String reqdate = sdf.format(cal.getTime());
        ServletInputStream inputStream = request.getInputStream();
        BufferedInputStream in = new BufferedInputStream(inputStream);
        byte[] contents = new byte[1024];
        int bytesRead = 0;
        String strFileContents = "";
        while ((bytesRead = in.read(contents)) != -1) {
            strFileContents = strFileContents + new String(contents, 0, bytesRead);
        }
        String decode = URLDecoder.decode(strFileContents, StandardCharsets.UTF_8.name());
        long reqtime = System.currentTimeMillis();
        String timeString = Long.toString(reqtime);
        RunInfoTrader.clean("./message");
        File message = new File("message/" + timeString + ".ged");
        PrintWriter out = new PrintWriter(message);
        out.print(decode);
        out.close();
        this.te = new SiteMapGenerator(this.dstore);
        this.te.init(reqdate);
        InfoTraderParser gp = new InfoTraderParser();
        gp.load(message);
        InfoTrader g = gp.infoTrader;
        Header header = g.getHeader();
        SourceSystem data = header.getSourceSystem();
        StringWithCustomTags productName = data.getProductName();
        System.out.println("Document name:" + productName);
        String systemId = data.getSystemId();
        System.out.println("Folder to store document:" + systemId);
        if (ReadDirStruct.restricteddir.containsKey(systemId)) {
            throw new NodeCreationException(systemId + "not a valid dir");
        }
        this.te.create(productName.toString(), "Document", systemId);
        Map<String, Source> sources = g.getSources();
        Collection<Source> sourcesvalues = sources.values();
        Iterator<Source> its = sourcesvalues.iterator();
        HashMap<String, Source> links = new HashMap<String, Source>();
        if (its.hasNext()) {
            Source next = its.next();
            System.out.println("File to link to:" + next.getTitle());
            if (!links.containsKey(next.getTitle().get(0))) {
                this.te.create(next.getTitle().get(0), "HyperLink", productName.toString());
                links.put(next.getTitle().get(0), next);
            }
        }
        this.dstore.writeDoc(g, reqdate);
        this.te.genSiteMap();
        this.te.commit_changes_to_sitemap();
        response.getWriter().println("OK");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static String readFile(String file) throws IOException {
        String encode = URLEncoder.encode(file.toString(), "UTF-8");
        BufferedReader reader = new BufferedReader(new FileReader("reports/" + encode));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");
        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
            String string = stringBuilder.toString();
            return string;
        }
        finally {
            reader.close();
        }
    }
}

