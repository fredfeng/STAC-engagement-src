/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper;

import com.graphhopper.routing.util.WeightingMap;
import com.graphhopper.util.Helper;
import com.graphhopper.util.shapes.GHPoint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class GHRequest {
    private String algo = "";
    private final List<GHPoint> points;
    private final WeightingMap hints = new WeightingMap();
    private String vehicle = "";
    private boolean possibleToAdd = false;
    private Locale locale = Locale.US;
    private final List<Double> favoredHeadings;

    public GHRequest() {
        this(5);
    }

    public GHRequest(int size) {
        this.points = new ArrayList<GHPoint>(size);
        this.favoredHeadings = new ArrayList<Double>(size);
        this.possibleToAdd = true;
    }

    public GHRequest(double fromLat, double fromLon, double toLat, double toLon, double startHeading, double endHeading) {
        this(new GHPoint(fromLat, fromLon), new GHPoint(toLat, toLon), startHeading, endHeading);
    }

    public GHRequest(double fromLat, double fromLon, double toLat, double toLon) {
        this(new GHPoint(fromLat, fromLon), new GHPoint(toLat, toLon));
    }

    public GHRequest(GHPoint startPlace, GHPoint endPlace, double startHeading, double endHeading) {
        if (startPlace == null) {
            throw new IllegalStateException("'from' cannot be null");
        }
        if (endPlace == null) {
            throw new IllegalStateException("'to' cannot be null");
        }
        this.points = new ArrayList<GHPoint>(2);
        this.points.add(startPlace);
        this.points.add(endPlace);
        this.favoredHeadings = new ArrayList<Double>(2);
        this.validateAzimuthValue(startHeading);
        this.favoredHeadings.add(startHeading);
        this.validateAzimuthValue(endHeading);
        this.favoredHeadings.add(endHeading);
    }

    public GHRequest(GHPoint startPlace, GHPoint endPlace) {
        this(startPlace, endPlace, Double.NaN, Double.NaN);
    }

    public GHRequest(List<GHPoint> points, List<Double> favoredHeadings) {
        if (points.size() != favoredHeadings.size()) {
            throw new IllegalArgumentException("Size of headings (" + favoredHeadings.size() + ") must match size of points (" + points.size() + ")");
        }
        for (Double heading : favoredHeadings) {
            this.validateAzimuthValue(heading);
        }
        this.points = points;
        this.favoredHeadings = favoredHeadings;
    }

    public GHRequest(List<GHPoint> points) {
        this(points, Collections.nCopies(points.size(), Double.NaN));
    }

    public GHRequest addPoint(GHPoint point, double favoredHeading) {
        if (point == null) {
            throw new IllegalArgumentException("point cannot be null");
        }
        if (!this.possibleToAdd) {
            throw new IllegalStateException("Please call empty constructor if you intent to use more than two places via addPoint method.");
        }
        this.points.add(point);
        this.validateAzimuthValue(favoredHeading);
        this.favoredHeadings.add(favoredHeading);
        return this;
    }

    public GHRequest addPoint(GHPoint point) {
        this.addPoint(point, Double.NaN);
        return this;
    }

    public double getFavoredHeading(int i) {
        return this.favoredHeadings.get(i);
    }

    public boolean hasFavoredHeading(int i) {
        if (i >= this.favoredHeadings.size()) {
            return false;
        }
        return !Double.isNaN(this.favoredHeadings.get(i));
    }

    private void validateAzimuthValue(double heading) {
        if (!(Double.isNaN(heading) || Double.compare(heading, 360.0) <= 0 && Double.compare(heading, 0.0) >= 0)) {
            throw new IllegalArgumentException("Heading " + heading + " must be in range (0,360) or NaN");
        }
    }

    public List<GHPoint> getPoints() {
        return this.points;
    }

    public GHRequest setAlgorithm(String algo) {
        if (algo != null) {
            this.algo = algo;
        }
        return this;
    }

    public String getAlgorithm() {
        return this.algo;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public GHRequest setLocale(Locale locale) {
        if (locale != null) {
            this.locale = locale;
        }
        return this;
    }

    public GHRequest setLocale(String localeStr) {
        return this.setLocale(Helper.getLocale(localeStr));
    }

    public GHRequest setWeighting(String w) {
        this.hints.setWeighting(w);
        return this;
    }

    public String getWeighting() {
        return this.hints.getWeighting();
    }

    public GHRequest setVehicle(String vehicle) {
        if (vehicle != null) {
            this.vehicle = vehicle;
        }
        return this;
    }

    public String getVehicle() {
        return this.vehicle;
    }

    public String toString() {
        String res = "";
        for (GHPoint point : this.points) {
            if (res.isEmpty()) {
                res = point.toString();
                continue;
            }
            res = res + "; " + point.toString();
        }
        return res + "(" + this.algo + ")";
    }

    public WeightingMap getHints() {
        return this.hints;
    }
}

