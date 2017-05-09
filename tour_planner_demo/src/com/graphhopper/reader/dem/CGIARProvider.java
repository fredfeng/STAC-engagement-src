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
import com.graphhopper.util.Downloader;
import com.graphhopper.util.Helper;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.xmlgraphics.image.codec.tiff.TIFFDecodeParam;
import org.apache.xmlgraphics.image.codec.tiff.TIFFImageDecoder;
import org.apache.xmlgraphics.image.codec.util.SeekableStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CGIARProvider
implements ElevationProvider {
    private static final int WIDTH = 6000;
    private Downloader downloader = new Downloader("GraphHopper CGIARReader").setTimeout(10000);
    private final Logger logger;
    private final Map<String, HeightTile> cacheData;
    private File cacheDir;
    private String baseUrl;
    private Directory dir;
    private DAType daType;
    final double precision = 1.0E7;
    private final double invPrecision = 1.0E-7;
    private final int degree = 5;
    private boolean calcMean;
    private boolean autoRemoveTemporary;

    public CGIARProvider() {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.cacheData = new HashMap<String, HeightTile>();
        this.cacheDir = new File("/tmp/cgiar");
        this.baseUrl = "http://srtm.csi.cgiar.org/SRT-ZIP/SRTM_V41/SRTM_Data_GeoTiff";
        this.daType = DAType.MMAP;
        this.calcMean = false;
        this.autoRemoveTemporary = true;
    }

    @Override
    public void setCalcMean(boolean eleCalcMean) {
        this.calcMean = eleCalcMean;
    }

    public void setAutoRemoveTemporaryFiles(boolean autoRemoveTemporary) {
        this.autoRemoveTemporary = autoRemoveTemporary;
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

    protected File getCacheDir() {
        return this.cacheDir;
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

    @Override
    public double getEle(double lat, double lon) {
        if (lat > 60.0 || lat < -60.0) {
            return 0.0;
        }
        String name = this.getFileName(lat = (double)((int)(lat * 1.0E7)) / 1.0E7, lon = (double)((int)(lon * 1.0E7)) / 1.0E7);
        HeightTile demProvider = this.cacheData.get(name);
        if (demProvider == null) {
            if (!this.cacheDir.exists()) {
                this.cacheDir.mkdirs();
            }
            int minLat = this.down(lat);
            int minLon = this.down(lon);
            demProvider = new HeightTile(minLat, minLon, 6000, 5.0E7, 5);
            demProvider.setCalcMean(this.calcMean);
            this.cacheData.put(name, demProvider);
            DataAccess heights = this.getDirectory().find(name + ".gh");
            demProvider.setHeights(heights);
            boolean loadExisting = false;
            try {
                loadExisting = heights.loadExisting();
            }
            catch (Exception ex) {
                this.logger.warn("cannot load " + name + ", error:" + ex.getMessage());
            }
            if (!loadExisting) {
                Raster raster;
                String tifName;
                File file;
                tifName = name + ".tif";
                String zippedURL = this.baseUrl + "/" + name + ".zip";
                file = new File(this.cacheDir, new File(zippedURL).getName());
                if (!file.exists()) {
                    try {
                        for (int i = 0; i < 3; ++i) {
                            try {
                                this.downloader.downloadFile(zippedURL, file.getAbsolutePath());
                                break;
                            }
                            catch (SocketTimeoutException ex) {
                                Thread.sleep(2000);
                                continue;
                            }
                            catch (IOException ex) {
                                demProvider.setSeaLevel(true);
                                heights.setSegmentSize(100).create(10).flush();
                                return 0.0;
                            }
                        }
                    }
                    catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
                heights.create(72000000);
                SeekableStream ss = null;
                try {
                    FileInputStream is = new FileInputStream(file);
                    ZipInputStream zis = new ZipInputStream(is);
                    ZipEntry entry = zis.getNextEntry();
                    while (entry != null && !entry.getName().equals(tifName)) {
                        entry = zis.getNextEntry();
                    }
                    ss = SeekableStream.wrapInputStream(zis, true);
                    TIFFImageDecoder imageDecoder = new TIFFImageDecoder(ss, new TIFFDecodeParam());
                    raster = imageDecoder.decodeAsRaster();
                }
                catch (Exception e) {
                    throw new RuntimeException("Can't decode " + tifName, e);
                }
                finally {
                    if (ss != null) {
                        Helper.close(ss);
                    }
                }
                int height = raster.getHeight();
                int width = raster.getWidth();
                int x = 0;
                int y = 0;
                try {
                    for (y = 0; y < height; ++y) {
                        for (x = 0; x < width; ++x) {
                            short val = (short)raster.getPixel(x, y, (int[])null)[0];
                            if (val < -1000 || val > 12000) {
                                val = -32768;
                            }
                            heights.setShort(2 * (y * 6000 + x), val);
                        }
                    }
                    heights.flush();
                }
                catch (Exception ex) {
                    throw new RuntimeException("Problem at x:" + x + ", y:" + y, ex);
                }
            }
        }
        if (demProvider.isSeaLevel()) {
            return 0.0;
        }
        return demProvider.getHeight(lat, lon);
    }

    int down(double val) {
        int intVal = (int)(val / 5.0) * 5;
        if (val < 0.0 && (double)intVal - val >= 1.0E-7) {
            intVal -= 5;
        }
        return intVal;
    }

    protected String getFileName(double lat, double lon) {
        lon = 1.0 + (180.0 + lon) / 5.0;
        int lonInt = (int)lon;
        int latInt = (int)(lat = 1.0 + (60.0 - lat) / 5.0);
        if (Math.abs((double)latInt - lat) < 2.0E-8) {
            --latInt;
        }
        String str = "srtm_";
        str = str + (lonInt < 10 ? "0" : "");
        str = str + lonInt;
        str = str + (latInt < 10 ? "_0" : "_");
        str = str + latInt;
        return str;
    }

    @Override
    public void release() {
        this.cacheData.clear();
        if (this.autoRemoveTemporary && this.dir != null) {
            this.dir.clear();
        }
    }

    public String toString() {
        return "CGIAR";
    }

    private Directory getDirectory() {
        if (this.dir != null) {
            return this.dir;
        }
        this.logger.info(this.toString() + " Elevation Provider, from: " + this.baseUrl + ", to: " + this.cacheDir + ", as: " + this.daType);
        this.dir = new GHDirectory(this.cacheDir.getAbsolutePath(), this.daType);
        return this.dir;
    }

    public static void main(String[] args) {
        CGIARProvider provider = new CGIARProvider();
        System.out.println(provider.getEle(46.0, -20.0));
        System.out.println(provider.getEle(49.949784, 11.57517));
        System.out.println(provider.getEle(49.968668, 11.575127));
        System.out.println(provider.getEle(49.968682, 11.574842));
        System.out.println(provider.getEle(-22.532854, -65.110474));
        System.out.println(provider.getEle(38.065392, -87.099609));
        System.out.println(provider.getEle(40.0, -105.2277023));
        System.out.println(provider.getEle(39.99999999, -105.2277023));
        System.out.println(provider.getEle(39.9999999, -105.2277023));
        System.out.println(provider.getEle(39.999999, -105.2277023));
        System.out.println(provider.getEle(29.840644, -42.890625));
    }
}

