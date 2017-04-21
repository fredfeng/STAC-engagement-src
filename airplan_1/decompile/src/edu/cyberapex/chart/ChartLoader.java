/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  org.apache.commons.io.FilenameUtils
 */
package edu.cyberapex.chart;

import edu.cyberapex.chart.Chart;
import edu.cyberapex.chart.ChartFailure;
import edu.cyberapex.chart.ChartFileLoader;
import edu.cyberapex.chart.PartFileLoader;
import edu.cyberapex.chart.TextFileLoader;
import edu.cyberapex.chart.XmlFileLoader;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;

public class ChartLoader {
    private static Map<String, ChartFileLoader> extToLoaderMap = new HashMap<String, ChartFileLoader>();

    public static Chart loadChart(String filename) throws FileNotFoundException, ChartFailure {
        String extension = FilenameUtils.getExtension((String)filename);
        if (extToLoaderMap.containsKey(extension)) {
            return extToLoaderMap.get(extension).loadChart(filename);
        }
        return ChartLoader.loadChartEntity(extension);
    }

    private static Chart loadChartEntity(String extension) throws ChartFailure {
        throw new ChartFailure("There is no loader for a file with extension " + extension);
    }

    public static void registerLoader(ChartFileLoader loader) {
        List<String> extensions = loader.fetchExtensions();
        for (int c = 0; c < extensions.size(); ++c) {
            String extension = extensions.get(c);
            extToLoaderMap.put(extension, loader);
        }
    }

    static {
        TextFileLoader.register();
        XmlFileLoader.register();
        PartFileLoader.register();
    }
}

