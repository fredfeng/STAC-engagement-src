/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.http;

import com.graphhopper.util.PointList;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

public class WebHelper {
    public static String encodeURL(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        }
        catch (Exception _ignore) {
            return str;
        }
    }

    public static PointList decodePolyline(String encoded, int initCap, boolean is3D) {
        PointList poly = new PointList(initCap, is3D);
        int index = 0;
        int len = encoded.length();
        int lat = 0;
        int lng = 0;
        int ele = 0;
        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 31) << shift;
                shift += 5;
            } while (b >= 32);
            int deltaLatitude = (result & 1) != 0 ? ~ (result >> 1) : result >> 1;
            lat += deltaLatitude;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 31) << shift;
                shift += 5;
            } while (b >= 32);
            int deltaLongitude = (result & 1) != 0 ? ~ (result >> 1) : result >> 1;
            lng += deltaLongitude;
            if (is3D) {
                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 31) << shift;
                    shift += 5;
                } while (b >= 32);
                int deltaElevation = (result & 1) != 0 ? ~ (result >> 1) : result >> 1;
                poly.add((double)lat / 100000.0, (double)lng / 100000.0, (double)(ele += deltaElevation) / 100.0);
                continue;
            }
            poly.add((double)lat / 100000.0, (double)lng / 100000.0);
        }
        return poly;
    }

    public static String encodePolyline(PointList poly) {
        if (poly.isEmpty()) {
            return "";
        }
        return WebHelper.encodePolyline(poly, poly.is3D());
    }

    public static String encodePolyline(PointList poly, boolean includeElevation) {
        StringBuilder sb = new StringBuilder();
        int size = poly.getSize();
        int prevLat = 0;
        int prevLon = 0;
        int prevEle = 0;
        for (int i = 0; i < size; ++i) {
            int num = (int)Math.floor(poly.getLatitude(i) * 100000.0);
            WebHelper.encodeNumber(sb, num - prevLat);
            prevLat = num;
            num = (int)Math.floor(poly.getLongitude(i) * 100000.0);
            WebHelper.encodeNumber(sb, num - prevLon);
            prevLon = num;
            if (!includeElevation) continue;
            num = (int)Math.floor(poly.getElevation(i) * 100.0);
            WebHelper.encodeNumber(sb, num - prevEle);
            prevEle = num;
        }
        return sb.toString();
    }

    private static void encodeNumber(StringBuilder sb, int num) {
        if ((num <<= 1) < 0) {
            num ^= -1;
        }
        while (num >= 32) {
            int nextValue = (32 | num & 31) + 63;
            sb.append((char)nextValue);
            num >>= 5;
        }
        sb.append((char)(num += 63));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String readString(InputStream inputStream) throws IOException {
        String encoding = "UTF-8";
        BufferedInputStream in = new BufferedInputStream(inputStream, 4096);
        try {
            int numRead;
            byte[] buffer = new byte[4096];
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
}

