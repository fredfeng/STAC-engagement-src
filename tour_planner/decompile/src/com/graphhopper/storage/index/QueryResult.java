/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.storage.index;

import com.graphhopper.util.DistanceCalc;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.PointList;
import com.graphhopper.util.shapes.GHPoint;
import com.graphhopper.util.shapes.GHPoint3D;

public class QueryResult {
    private double queryDistance = Double.MAX_VALUE;
    private int wayIndex = -1;
    private int closestNode = -1;
    private EdgeIteratorState closestEdge;
    private final GHPoint queryPoint;
    private GHPoint3D snappedPoint;
    private Position snappedPosition;

    public QueryResult(double queryLat, double queryLon) {
        this.queryPoint = new GHPoint(queryLat, queryLon);
    }

    public void setClosestNode(int node) {
        this.closestNode = node;
    }

    public int getClosestNode() {
        return this.closestNode;
    }

    public void setQueryDistance(double dist) {
        this.queryDistance = dist;
    }

    public double getQueryDistance() {
        return this.queryDistance;
    }

    public void setWayIndex(int wayIndex) {
        this.wayIndex = wayIndex;
    }

    public int getWayIndex() {
        return this.wayIndex;
    }

    public void setSnappedPosition(Position pos) {
        this.snappedPosition = pos;
    }

    public Position getSnappedPosition() {
        return this.snappedPosition;
    }

    public boolean isValid() {
        return this.closestNode >= 0;
    }

    public void setClosestEdge(EdgeIteratorState detach) {
        this.closestEdge = detach;
    }

    public EdgeIteratorState getClosestEdge() {
        return this.closestEdge;
    }

    public GHPoint getQueryPoint() {
        return this.queryPoint;
    }

    public GHPoint3D getSnappedPoint() {
        if (this.snappedPoint == null) {
            throw new IllegalStateException("Calculate snapped point before!");
        }
        return this.snappedPoint;
    }

    public void calcSnappedPoint(DistanceCalc distCalc) {
        double adjLon;
        if (this.closestEdge == null) {
            throw new IllegalStateException("No closest edge?");
        }
        PointList fullPL = this.getClosestEdge().fetchWayGeometry(3);
        double tmpLat = fullPL.getLatitude(this.wayIndex);
        double tmpLon = fullPL.getLongitude(this.wayIndex);
        double tmpEle = fullPL.getElevation(this.wayIndex);
        if (this.snappedPosition != Position.EDGE) {
            this.snappedPoint = new GHPoint3D(tmpLat, tmpLon, tmpEle);
            return;
        }
        double queryLat = this.getQueryPoint().lat;
        double queryLon = this.getQueryPoint().lon;
        double adjLat = fullPL.getLatitude(this.wayIndex + 1);
        if (distCalc.validEdgeDistance(queryLat, queryLon, tmpLat, tmpLon, adjLat, adjLon = fullPL.getLongitude(this.wayIndex + 1))) {
            GHPoint tmpPoint = distCalc.calcCrossingPointToEdge(queryLat, queryLon, tmpLat, tmpLon, adjLat, adjLon);
            double adjEle = fullPL.getElevation(this.wayIndex + 1);
            this.snappedPoint = new GHPoint3D(tmpPoint.lat, tmpPoint.lon, (tmpEle + adjEle) / 2.0);
        } else {
            this.snappedPoint = new GHPoint3D(tmpLat, tmpLon, tmpEle);
        }
    }

    public String toString() {
        if (this.closestEdge != null) {
            return "" + this.closestEdge.getBaseNode() + "-" + this.closestEdge.getAdjNode() + "  " + this.snappedPoint;
        }
        return "" + this.closestNode + ", " + this.queryPoint + ", " + this.wayIndex;
    }

    public static enum Position {
        EDGE,
        TOWER,
        PILLAR;
        

        private Position() {
        }
    }

}

