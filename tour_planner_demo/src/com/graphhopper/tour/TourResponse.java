/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.tour;

import com.graphhopper.util.shapes.GHPoint;
import java.util.ArrayList;
import java.util.List;

public class TourResponse<P extends GHPoint> {
    private final List<Throwable> errors = new ArrayList<Throwable>(4);
    private List<P> points = new ArrayList<P>(0);

    private void check(String method) {
        if (this.hasErrors()) {
            throw new RuntimeException("error");
        }
    }

    public boolean hasErrors() {
        return !this.errors.isEmpty();
    }

    public List<Throwable> getErrors() {
        return this.errors;
    }

    public TourResponse<P> addError(Throwable error) {
        this.errors.add(error);
        return this;
    }

    public TourResponse<P> setPoints(List<P> points) {
        this.points = points;
        return this;
    }

    public List<? extends P> getPoints() {
        this.check("getPoints");
        return this.points;
    }
}

