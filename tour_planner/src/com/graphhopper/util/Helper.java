/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.util;

import com.graphhopper.util.DistanceCalc;
import com.graphhopper.util.DistanceCalc3D;
import com.graphhopper.util.DistanceCalcEarth;
import com.graphhopper.util.DistancePlaneProjection;
import com.graphhopper.util.PointList;
import com.graphhopper.util.shapes.BBox;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.charset.Charset;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Helper {
    public static final DistanceCalc DIST_EARTH = new DistanceCalcEarth();
    public static final DistanceCalc3D DIST_3D = new DistanceCalc3D();
    public static final DistancePlaneProjection DIST_PLANE = new DistancePlaneProjection();
    private static final Logger logger = LoggerFactory.getLogger(Helper.class);
    public static Charset UTF_CS = Charset.forName("UTF-8");
    public static final long MB = 0x100000;
    private static final float DEGREE_FACTOR = 5368709.0f;
    private static final float ELE_FACTOR = 1000.0f;

    public static ArrayList<Integer> tIntListToArrayList(TIntList from) {
        int len = from.size();
        ArrayList<Integer> list = new ArrayList<Integer>(len);
        for (int i = 0; i < len; ++i) {
            list.add(from.get(i));
        }
        return list;
    }

    public static Locale getLocale(String param) {
        int index;
        int pointIndex = param.indexOf(46);
        if (pointIndex > 0) {
            param = param.substring(0, pointIndex);
        }
        if ((index = (param = param.replace("-", "_")).indexOf("_")) < 0) {
            return new Locale(param);
        }
        return new Locale(param.substring(0, index), param.substring(index + 1));
    }

    static String packageToPath(Package pkg) {
        return pkg.getName().replaceAll("\\.", File.separator);
    }

    public static int countBitValue(int maxTurnCosts) {
        int intVal;
        double val = Math.log(maxTurnCosts) / Math.log(2.0);
        if (val == (double)(intVal = (int)val)) {
            return intVal;
        }
        return intVal + 1;
    }

    private Helper() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void loadProperties(Map<String, String> map, Reader tmpReader) throws IOException {
        BufferedReader reader = new BufferedReader(tmpReader);
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("//") || line.startsWith("#") || Helper.isEmpty(line)) continue;
                int index = line.indexOf("=");
                if (index < 0) {
                    logger.warn("Skipping configuration at line:" + line);
                    continue;
                }
                String field = line.substring(0, index);
                String value = line.substring(index + 1);
                map.put(field, value);
            }
        }
        finally {
            reader.close();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void saveProperties(Map<String, String> map, Writer tmpWriter) throws IOException {
        BufferedWriter writer = new BufferedWriter(tmpWriter);
        try {
            for (Map.Entry<String, String> e : map.entrySet()) {
                writer.append(e.getKey());
                writer.append('=');
                writer.append(e.getValue());
                writer.append('\n');
            }
        }
        finally {
            writer.close();
        }
    }

    public static List<String> readFile(String file) throws IOException {
        return Helper.readFile(new InputStreamReader((InputStream)new FileInputStream(file), UTF_CS));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static List<String> readFile(Reader simpleReader) throws IOException {
        BufferedReader reader = new BufferedReader(simpleReader);
        try {
            String line;
            ArrayList<String> res = new ArrayList<String>();
            while ((line = reader.readLine()) != null) {
                res.add(line);
            }
            ArrayList<String> arrayList = res;
            return arrayList;
        }
        finally {
            reader.close();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String isToString(InputStream inputStream) throws IOException {
        int size = 8192;
        String encoding = "UTF-8";
        BufferedInputStream in = new BufferedInputStream(inputStream, size);
        try {
            int numRead;
            byte[] buffer = new byte[size];
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            while ((numRead = in.read(buffer)) != -1) {
                output.write(buffer, 0, numRead);
            }
            String string = output.toString(encoding);
            return string;
        }
        finally {
            in.close();
        }
    }

    public static int idealIntArraySize(int need) {
        return Helper.idealByteArraySize(need * 4) / 4;
    }

    public static int idealByteArraySize(int need) {
        for (int i = 4; i < 32; ++i) {
            if (need > (1 << i) - 12) continue;
            return (1 << i) - 12;
        }
        return need;
    }

    public static boolean removeDir(File file) {
        if (!file.exists()) {
            return true;
        }
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                Helper.removeDir(f);
            }
        }
        return file.delete();
    }

    public static long getTotalMB() {
        return Runtime.getRuntime().totalMemory() / 0x100000;
    }

    public static long getUsedMB() {
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 0x100000;
    }

    public static String getMemInfo() {
        return "totalMB:" + Helper.getTotalMB() + ", usedMB:" + Helper.getUsedMB();
    }

    public static int getSizeOfObjectRef(int factor) {
        return factor * 12;
    }

    public static int getSizeOfLongArray(int length, int factor) {
        return factor * 16 + 8 * length;
    }

    public static int getSizeOfObjectArray(int length, int factor) {
        return factor * 16 + 4 * length;
    }

    public static void close(Closeable cl) {
        try {
            if (cl != null) {
                cl.close();
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Couldn't close resource", ex);
        }
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static boolean isFileMapped(ByteBuffer bb) {
        if (bb instanceof MappedByteBuffer) {
            try {
                ((MappedByteBuffer)bb).isLoaded();
                return true;
            }
            catch (UnsupportedOperationException unsupportedOperationException) {
                // empty catch block
            }
        }
        return false;
    }

    public static int calcIndexSize(BBox graphBounds) {
        if (!graphBounds.isValid()) {
            throw new IllegalArgumentException("Bounding box is not valid to calculate index size: " + graphBounds);
        }
        double dist = DIST_EARTH.calcDist(graphBounds.maxLat, graphBounds.minLon, graphBounds.minLat, graphBounds.maxLon);
        dist = Math.min(dist / 1000.0, 50000.0);
        return Math.max(2000, (int)(dist * dist));
    }

    public static String pruneFileEnd(String file) {
        int index = file.lastIndexOf(".");
        if (index < 0) {
            return file;
        }
        return file.substring(0, index);
    }

    public static /* varargs */ TIntList createTList(int ... list) {
        TIntArrayList res = new TIntArrayList(list.length);
        for (int val : list) {
            res.add(val);
        }
        return res;
    }

    public static /* varargs */ PointList createPointList(double ... list) {
        if (list.length % 2 != 0) {
            throw new IllegalArgumentException("list should consist of lat,lon pairs!");
        }
        int max = list.length / 2;
        PointList res = new PointList(max, false);
        for (int i = 0; i < max; ++i) {
            res.add(list[2 * i], list[2 * i + 1], Double.NaN);
        }
        return res;
    }

    public static /* varargs */ PointList createPointList3D(double ... list) {
        if (list.length % 3 != 0) {
            throw new IllegalArgumentException("list should consist of lat,lon,ele tuples!");
        }
        int max = list.length / 3;
        PointList res = new PointList(max, true);
        for (int i = 0; i < max; ++i) {
            res.add(list[3 * i], list[3 * i + 1], list[3 * i + 2]);
        }
        return res;
    }

    public static final int degreeToInt(double deg) {
        if (deg >= Double.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        if (deg <= -1.7976931348623157E308) {
            return -2147483647;
        }
        return (int)(deg * 5368709.0);
    }

    public static final double intToDegree(int storedInt) {
        if (storedInt == Integer.MAX_VALUE) {
            return Double.MAX_VALUE;
        }
        if (storedInt == -2147483647) {
            return -1.7976931348623157E308;
        }
        return (double)storedInt / 5368709.0;
    }

    public static final int eleToInt(double ele) {
        if (ele >= 2.147483647E9) {
            return Integer.MAX_VALUE;
        }
        return (int)(ele * 1000.0);
    }

    public static final double intToEle(int integEle) {
        if (integEle == Integer.MAX_VALUE) {
            return Double.MAX_VALUE;
        }
        return (float)integEle / 1000.0f;
    }

    public static void cleanMappedByteBuffer(final ByteBuffer buffer) {
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Object>(){

                @Override
                public Object run() throws Exception {
                    try {
                        Method getCleanerMethod = buffer.getClass().getMethod("cleaner", new Class[0]);
                        getCleanerMethod.setAccessible(true);
                        Object cleaner = getCleanerMethod.invoke(buffer, new Object[0]);
                        if (cleaner != null) {
                            cleaner.getClass().getMethod("clean", new Class[0]).invoke(cleaner, new Object[0]);
                        }
                    }
                    catch (NoSuchMethodException getCleanerMethod) {
                        // empty catch block
                    }
                    return null;
                }
            });
        }
        catch (PrivilegedActionException e) {
            throw new RuntimeException("unable to unmap the mapped buffer", e);
        }
    }

    public static void cleanHack() {
        System.gc();
    }

    public static String nf(long no) {
        return NumberFormat.getInstance(Locale.FRANCE).format(no);
    }

    public static String firstBig(String sayText) {
        if (sayText == null || sayText.length() <= 0) {
            return sayText;
        }
        return "" + Character.toUpperCase(sayText.charAt(0)) + sayText.substring(1);
    }

    public static final double keepIn(double value, double min, double max) {
        return Math.max(min, Math.min(value, max));
    }

    public static double round(double value, int exponent) {
        double factor = Math.pow(10.0, exponent);
        return (double)Math.round(value * factor) / factor;
    }

    public static final double round6(double value) {
        return (double)Math.round(value * 1000000.0) / 1000000.0;
    }

    public static final double round4(double value) {
        return (double)Math.round(value * 10000.0) / 10000.0;
    }

    public static final double round2(double value) {
        return (double)Math.round(value * 100.0) / 100.0;
    }

}

