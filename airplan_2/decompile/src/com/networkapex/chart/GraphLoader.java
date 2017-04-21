/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.chart;

import com.networkapex.chart.Graph;
import com.networkapex.chart.GraphFileLoader;
import com.networkapex.chart.GraphRaiser;
import com.networkapex.chart.ParserFileLoader;
import com.networkapex.chart.TextFileLoader;
import com.networkapex.chart.XmlFileLoader;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;

public class GraphLoader {
    private static Map<String, GraphFileLoader> extToLoaderMap = new HashMap<String, GraphFileLoader>();

    public static Graph loadGraph(String filename) throws FileNotFoundException, GraphRaiser {
        String extension = FilenameUtils.getExtension(filename);
        if (extToLoaderMap.containsKey(extension)) {
            return extToLoaderMap.get(extension).loadGraph(filename);
        }
        throw new GraphRaiser("There is no loader for a file with extension " + extension);
    }

    public static void registerLoader(GraphFileLoader loader) {
        List<String> extensions = loader.getExtensions();
        for (int j = 0; j < extensions.size(); ++j) {
            GraphLoader.registerLoaderAid(loader, extensions, j);
        }
    }

    private static void registerLoaderAid(GraphFileLoader loader, List<String> extensions, int p) {
        String extension = extensions.get(p);
        extToLoaderMap.put(extension, loader);
    }

    static {
        TextFileLoader.register();
        XmlFileLoader.register();
        ParserFileLoader.register();
    }
}

