/*
 * Decompiled with CFR 0_121.
 */
package infotrader.sitedatastorage;

import infotrader.dataprocessing.SiteMapGenerator;
import infotrader.parser.exception.InfoTraderParserException;
import infotrader.parser.model.Header;
import infotrader.parser.model.InfoTrader;
import infotrader.parser.model.SourceSystem;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.parser.InfoTraderParser;
import infotrader.sitedatastorage.DocumentStore;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReadDirStruct {
    public static Map<String, String> restricteddir;

    public static void load(DocumentStore te) {
        restricteddir = new HashMap<String, String>();
        List<File> subdirs = ReadDirStruct.getSubdirs(new File("dirs"));
        for (File next : subdirs) {
            System.out.println(next.getAbsoluteFile());
            System.out.println(next.getName());
            System.out.println(next.getParent());
            System.out.println("dir:" + next.isDirectory());
            String parent = next.getParent();
            if (parent.startsWith("dirs/")) {
                parent = parent.substring("dirs/".length());
            }
            if (next.getParent().equalsIgnoreCase("dirs")) {
                parent = null;
                restricteddir.put(next.getName(), next.getName());
            }
            SiteMapGenerator.createCache(next.getName(), "Directory", parent);
        }
        ArrayList<File> deepSubfiles = new ArrayList<File>();
        List<File> subdirs2 = ReadDirStruct.getFnames(new File("dirs"), deepSubfiles);
        Iterator<File> it = subdirs2.iterator();
        while (it.hasNext()) {
            try {
                File next = it.next();
                System.out.println(next.getAbsoluteFile());
                System.out.println(next.getName());
                System.out.println("dir:" + next.isDirectory());
                InfoTraderParser gp = new InfoTraderParser();
                gp.load(next);
                Header header = gp.infoTrader.getHeader();
                SourceSystem data = header.getSourceSystem();
                StringWithCustomTags productName = data.getProductName();
                String systemId = data.getSystemId();
                te.registerFile(productName.toString(), next);
                SiteMapGenerator.createCache(productName.toString(), "Document", systemId);
            }
            catch (IOException ex) {
                Logger.getLogger(ReadDirStruct.class.getName()).log(Level.SEVERE, null, ex.getMessage());
            }
            catch (InfoTraderParserException ex) {
                Logger.getLogger(ReadDirStruct.class.getName()).log(Level.SEVERE, null, ex.getMessage());
            }
        }
    }

    static List<File> getSubdirs(File file) {
        List<File> subdirs = Arrays.asList(file.listFiles(new FileFilter(){

            @Override
            public boolean accept(File f) {
                return f.isDirectory();
            }
        }));
        subdirs = new ArrayList<File>(subdirs);
        ArrayList<File> deepSubdirs = new ArrayList<File>();
        for (File subdir : subdirs) {
            deepSubdirs.addAll(ReadDirStruct.getSubdirs(subdir));
        }
        subdirs.addAll(deepSubdirs);
        return subdirs;
    }

    static List<File> getFils(File file) {
        List<File> subfils = Arrays.asList(file.listFiles(new FileFilter(){

            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".ged");
            }
        }));
        subfils = new ArrayList<File>(subfils);
        ArrayList<File> deepSubdirs = new ArrayList<File>();
        for (File subdir : subfils) {
            deepSubdirs.addAll(ReadDirStruct.getSubdirs(subdir));
        }
        subfils.addAll(deepSubdirs);
        return subfils;
    }

    public static List<File> getFnames(File sDir, List<File> deepSubfiles) {
        File[] faFiles;
        for (File file : faFiles = sDir.listFiles()) {
            if (file.getName().endsWith(".ged")) {
                deepSubfiles.add(file);
            }
            if (!file.isDirectory()) continue;
            ReadDirStruct.getFnames(file, deepSubfiles);
        }
        return deepSubfiles;
    }

    public static void displayIt(File node) {
        if (node.isDirectory()) {
            String[] subNote;
            for (String filename : subNote = node.list()) {
                ReadDirStruct.displayIt(new File(node, filename));
            }
        }
    }

}

