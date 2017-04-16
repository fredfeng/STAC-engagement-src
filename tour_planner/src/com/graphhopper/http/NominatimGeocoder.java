/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.http;

import com.graphhopper.http.WebHelper;
import com.graphhopper.search.Geocoding;
import com.graphhopper.search.ReverseGeocoding;
import com.graphhopper.util.shapes.BBox;
import com.graphhopper.util.shapes.GHPlace;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NominatimGeocoder
implements Geocoding,
ReverseGeocoding {
    private String nominatimUrl;
    private String nominatimReverseUrl;
    private BBox bounds;
    private Logger logger;
    private int timeoutInMillis;
    private String userAgent;

    public static void main(String[] args) {
        System.out.println("search " + new NominatimGeocoder().names2places(new GHPlace("bayreuth"), new GHPlace("berlin")));
        System.out.println("reverse " + new NominatimGeocoder().places2names(new GHPlace(49.9027606, 11.577197), new GHPlace(52.5198535, 13.4385964)));
    }

    public NominatimGeocoder() {
        this("http://open.mapquestapi.com/nominatim/v1/search.php", "http://open.mapquestapi.com/nominatim/v1/reverse.php");
    }

    public NominatimGeocoder(String url, String reverseUrl) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.timeoutInMillis = 10000;
        this.userAgent = "GraphHopper Web Service";
        this.nominatimUrl = url;
        this.nominatimReverseUrl = reverseUrl;
    }

    public NominatimGeocoder setBounds(BBox bounds) {
        this.bounds = bounds;
        return this;
    }

    @Override
    public /* varargs */ List<GHPlace> names2places(GHPlace ... places) {
        ArrayList<GHPlace> resList = new ArrayList<GHPlace>();
        for (GHPlace place : places) {
            String url = this.nominatimUrl + "?format=json&q=" + WebHelper.encodeURL(place.getName()) + "&limit=3";
            if (this.bounds != null) {
                url = url + "&bounded=1&viewbox=" + this.bounds.minLon + "," + this.bounds.maxLat + "," + this.bounds.maxLon + "," + this.bounds.minLat;
            }
            try {
                HttpURLConnection hConn = this.openConnection(url);
                String str = WebHelper.readString(hConn.getInputStream());
                JSONObject json = new JSONArray(str).getJSONObject(0);
                double lat = json.getDouble("lat");
                double lon = json.getDouble("lon");
                GHPlace p = new GHPlace(lat, lon);
                p.setName(json.getString("display_name"));
                resList.add(p);
            }
            catch (Exception ex) {
                this.logger.error("problem while geocoding (search " + place + "): " + ex.getMessage());
            }
        }
        return resList;
    }

    @Override
    public /* varargs */ List<GHPlace> places2names(GHPlace ... points) {
        ArrayList<GHPlace> resList = new ArrayList<GHPlace>();
        for (GHPlace point : points) {
            try {
                String url = this.nominatimReverseUrl + "?lat=" + point.lat + "&lon=" + point.lon + "&format=json&zoom=16";
                HttpURLConnection hConn = this.openConnection(url);
                String str = WebHelper.readString(hConn.getInputStream());
                JSONObject json = new JSONObject(str);
                double lat = json.getDouble("lat");
                double lon = json.getDouble("lon");
                JSONObject address = json.getJSONObject("address");
                String name = "";
                if (address.has("road")) {
                    name = name + address.get("road") + ", ";
                }
                if (address.has("postcode")) {
                    name = name + address.get("postcode") + " ";
                }
                if (address.has("city")) {
                    name = name + address.get("city") + ", ";
                } else if (address.has("county")) {
                    name = name + address.get("county") + ", ";
                }
                if (address.has("state")) {
                    name = name + address.get("state") + ", ";
                }
                if (address.has("country")) {
                    name = name + address.get("country");
                }
                resList.add(new GHPlace(lat, lon).setName(name));
            }
            catch (Exception ex) {
                this.logger.error("problem while geocoding (reverse " + point + "): " + ex.getMessage());
            }
        }
        return resList;
    }

    HttpURLConnection openConnection(String url) throws IOException {
        HttpURLConnection hConn = (HttpURLConnection)new URL(url).openConnection();
        hConn.setRequestProperty("User-Agent", this.userAgent);
        hConn.setRequestProperty("content-charset", "UTF-8");
        hConn.setConnectTimeout(this.timeoutInMillis);
        hConn.setReadTimeout(this.timeoutInMillis);
        hConn.connect();
        return hConn;
    }

    public NominatimGeocoder setTimeout(int timeout) {
        this.timeoutInMillis = timeout;
        return this;
    }
}

