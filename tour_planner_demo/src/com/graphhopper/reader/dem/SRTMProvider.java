/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.reader.dem;

import com.graphhopper.reader.dem.ElevationProvider;
import com.graphhopper.reader.dem.HeightTile;
import com.graphhopper.storage.DAType;
import com.graphhopper.storage.DataAccess;
import com.graphhopper.storage.Directory;
import com.graphhopper.storage.GHDirectory;
import com.graphhopper.util.BitUtil;
import com.graphhopper.util.Downloader;
import com.graphhopper.util.Helper;
import gnu.trove.map.hash.TIntObjectHashMap;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SRTMProvider
implements ElevationProvider {
    private static final BitUtil BIT_UTIL = BitUtil.BIG;
    private final Logger logger;
    private final int WIDTH = 1201;
    private Directory dir;
    private DAType daType;
    private Downloader downloader;
    private File cacheDir;
    private final TIntObjectHashMap<HeightTile> cacheData;
    private final TIntObjectHashMap<String> areas;
    private final double precision = 1.0E7;
    private final double invPrecision = 1.0E-7;
    private String baseUrl;
    private boolean calcMean;

    public static void main(String[] args) throws IOException {
        SRTMProvider provider = new SRTMProvider();
        System.out.println(provider.getEle(47.468668, 14.575127));
        System.out.println(provider.getEle(47.467753, 14.573911));
        System.out.println(provider.getEle(46.468835, 12.578777));
        System.out.println(provider.getEle(48.469123, 9.576393));
        provider.setCalcMean(true);
        System.out.println(provider.getEle(47.467753, 14.573911));
    }

    public SRTMProvider() {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.daType = DAType.MMAP;
        this.downloader = new Downloader("GraphHopper SRTMReader").setTimeout(10000);
        this.cacheDir = new File("/tmp/srtm");
        this.cacheData = new TIntObjectHashMap();
        this.areas = new TIntObjectHashMap();
        this.baseUrl = "http://dds.cr.usgs.gov/srtm/version2_1/SRTM3/";
        this.calcMean = false;
        this.init();
    }

    @Override
    public void setCalcMean(boolean calcMean) {
        this.calcMean = calcMean;
    }

    private SRTMProvider init() {
        try {
            String[] strs;
            for (String str : strs = new String[]{"Africa", "Australia", "Eurasia", "Islands", "North_America", "South_America"}) {
                InputStream is = this.getClass().getResourceAsStream(str + "_names.txt");
                for (String line : Helper.readFile(new InputStreamReader(is, Helper.UTF_CS))) {
                    int intKey;
                    String key;
                    int lat = Integer.parseInt(line.substring(1, 3));
                    if (line.substring(0, 1).charAt(0) == 'S') {
                        lat = - lat;
                    }
                    int lon = Integer.parseInt(line.substring(4, 7));
                    if (line.substring(3, 4).charAt(0) == 'W') {
                        lon = - lon;
                    }
                    if ((key = this.areas.put(intKey = this.calcIntKey(lat, lon), str)) == null) continue;
                    throw new IllegalStateException("do not overwrite existing! key " + intKey + " " + key + " vs. " + str);
                }
            }
            return this;
        }
        catch (Exception ex) {
            throw new IllegalStateException("Cannot load area names from classpath", ex);
        }
    }

    private int calcIntKey(double lat, double lon) {
        return (this.down(lat) + 90) * 1000 + this.down(lon) + 180;
    }

    public void setDownloader(Downloader downloader) {
        this.downloader = downloader;
    }

    @Override
    public ElevationProvider setCacheDir(File cacheDir) {
        if (cacheDir.exists() && !cacheDir.isDirectory()) {
            throw new IllegalArgumentException("Cache path has to be a directory");
        }
        try {
            this.cacheDir = cacheDir.getCanonicalFile();
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return this;
    }

    @Override
    public ElevationProvider setBaseURL(String baseUrl) {
        if (baseUrl == null || baseUrl.isEmpty()) {
            throw new IllegalArgumentException("baseUrl cannot be empty");
        }
        this.baseUrl = baseUrl;
        return this;
    }

    @Override
    public ElevationProvider setDAType(DAType daType) {
        this.daType = daType;
        return this;
    }

    int down(double val) {
        int intVal = (int)val;
        if (val >= 0.0 || (double)intVal - val < 1.0E-7) {
            return intVal;
        }
        return intVal - 1;
    }

    String getFileString(double lat, double lon) {
        int intKey = this.calcIntKey(lat, lon);
        String str = this.areas.get(intKey);
        if (str == null) {
            return null;
        }
        int minLat = Math.abs(this.down(lat));
        int minLon = Math.abs(this.down(lon));
        str = str + "/";
        str = lat >= 0.0 ? str + "N" : str + "S";
        if (minLat < 10) {
            str = str + "0";
        }
        str = str + minLat;
        str = lon >= 0.0 ? str + "E" : str + "W";
        if (minLon < 10) {
            str = str + "0";
        }
        if (minLon < 100) {
            str = str + "0";
        }
        str = str + minLon;
        return str;
    }

    @Override
    public double getEle(double lat, double lon) {
        int intKey = this.calcIntKey(lat = (double)((int)(lat * 1.0E7)) / 1.0E7, lon = (double)((int)(lon * 1.0E7)) / 1.0E7);
        HeightTile demProvider = this.cacheData.get(intKey);
        if (demProvider == null) {
            String fileDetails;
            if (!this.cacheDir.exists()) {
                this.cacheDir.mkdirs();
            }
            if ((fileDetails = this.getFileString(lat, lon)) == null) {
                return 0.0;
            }
            int minLat = this.down(lat);
            int minLon = this.down(lon);
            demProvider = new HeightTile(minLat, minLon, 1201, 1.0E7, 1);
            demProvider.setCalcMean(this.calcMean);
            this.cacheData.put(intKey, demProvider);
            DataAccess heights = this.getDirectory().find("dem" + intKey);
            demProvider.setHeights(heights);
            boolean loadExisting = false;
            try {
                loadExisting = heights.loadExisting();
            }
            catch (Exception ex) {
                this.logger.warn("cannot load dem" + intKey + ", error:" + ex.getMessage());
            }
            if (!loadExisting) {
                byte[] bytes = new byte[2884802];
                heights.create(bytes.length);
                try {
                    int len;
                    String zippedURL = this.baseUrl + "/" + fileDetails + ".hgt.zip";
                    File file = new File(this.cacheDir, new File(zippedURL).getName());
                    if (!file.exists()) {
                        for (int i = 0; i < 3; ++i) {
                            try {
                                this.downloader.downloadFile(zippedURL, file.getAbsolutePath());
                                break;
                            }
                            catch (SocketTimeoutException ex) {
                                Thread.sleep(2000);
                                continue;
                            }
                            catch (FileNotFoundException ex) {
                                zippedURL = this.baseUrl + "/" + fileDetails + "hgt.zip";
                            }
                        }
                    }
                    FileInputStream is = new FileInputStream(file);
                    ZipInputStream zis = new ZipInputStream(is);
                    zis.getNextEntry();
                    BufferedInputStream buff = new BufferedInputStream(zis);
                    while ((len = buff.read(bytes)) > 0) {
                        for (int bytePos = 0; bytePos < len; bytePos += 2) {
                            short val = BIT_UTIL.toShort(bytes, bytePos);
                            if (val < -1000 || val > 12000) {
                                val = -32768;
                            }
                            heights.setShort(bytePos, val);
                        }
                    }
                    heights.flush();
                }
                catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        return demProvider.getHeight(lat, lon);
    }

    @Override
    public void release() {
        this.cacheData.clear();
        if (this.dir != null) {
            this.dir.clear();
        }
    }

    public String toString() {
        return "SRTM";
    }

    private Directory getDirectory() {
        if (this.dir != null) {
            return this.dir;
        }
        this.logger.info(this.toString() + " Elevation Provider, from: " + this.baseUrl + ", to: " + this.cacheDir + ", as: " + this.daType);
        this.dir = new GHDirectory(this.cacheDir.getAbsolutePath(), this.daType);
        return this.dir;
    }
}

