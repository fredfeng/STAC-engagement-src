/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.util;

import com.graphhopper.GraphHopper;
import com.graphhopper.util.Helper;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.List;

public class Constants {
    public static final String JAVA_VERSION = System.getProperty("java.version");
    public static final String OS_NAME = System.getProperty("os.name");
    public static final boolean LINUX = OS_NAME.startsWith("Linux");
    public static final boolean WINDOWS = OS_NAME.startsWith("Windows");
    public static final boolean SUN_OS = OS_NAME.startsWith("SunOS");
    public static final boolean MAC_OS_X = OS_NAME.startsWith("Mac OS X");
    public static final String OS_ARCH = System.getProperty("os.arch");
    public static final String OS_VERSION = System.getProperty("os.version");
    public static final String JAVA_VENDOR = System.getProperty("java.vendor");
    public static final int VERSION_NODE = 4;
    public static final int VERSION_EDGE = 12;
    public static final int VERSION_SHORTCUT = 1;
    public static final int VERSION_GEOMETRY = 3;
    public static final int VERSION_LOCATION_IDX = 2;
    public static final int VERSION_NAME_IDX = 2;
    public static final String VERSION;
    public static final String BUILD_DATE;
    public static final boolean SNAPSHOT;

    public static String getVersions() {
        return "4,12,3,2,2,1";
    }

    static {
        String version = "0.0";
        try {
            List<String> v = Helper.readFile(new InputStreamReader(GraphHopper.class.getResourceAsStream("version"), Helper.UTF_CS));
            version = v.get(0);
        }
        catch (Exception ex) {
            System.err.println("GraphHopper Initialization ERROR: cannot read version!? " + ex.getMessage());
        }
        int indexM = version.indexOf("-");
        if ("${project.version}".equals(version)) {
            VERSION = "0.0";
            SNAPSHOT = true;
            System.err.println("GraphHopper Initialization WARNING: maven did not preprocess the version file! Do not use the jar for a release!");
        } else if ("0.0".equals(version)) {
            VERSION = "0.0";
            SNAPSHOT = true;
            System.err.println("GraphHopper Initialization WARNING: cannot get version!?");
        } else {
            String tmp = version;
            if (indexM >= 0) {
                tmp = version.substring(0, indexM);
            }
            SNAPSHOT = version.toLowerCase().contains("-snapshot");
            VERSION = tmp;
        }
        String buildDate = "";
        try {
            List<String> v = Helper.readFile(new InputStreamReader(GraphHopper.class.getResourceAsStream("builddate"), Helper.UTF_CS));
            buildDate = v.get(0);
        }
        catch (Exception v) {
            // empty catch block
        }
        BUILD_DATE = buildDate;
    }
}

