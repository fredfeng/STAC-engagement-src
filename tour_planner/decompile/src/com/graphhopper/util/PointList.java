/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.util;

import com.graphhopper.util.DistanceCalc;
import com.graphhopper.util.DistanceCalc3D;
import com.graphhopper.util.Helper;
import com.graphhopper.util.NumHelper;
import com.graphhopper.util.PointAccess;
import com.graphhopper.util.shapes.GHPoint;
import com.graphhopper.util.shapes.GHPoint3D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class PointList
implements Iterable<GHPoint3D>,
PointAccess {
    private static final DistanceCalc3D distCalc3D = Helper.DIST_3D;
    private static String ERR_MSG = "Tried to access PointList with too big index!";
    private double[] latitudes;
    private double[] longitudes;
    private double[] elevations;
    protected int size = 0;
    protected boolean is3D;
    public static PointList EMPTY = new PointList(0, true){

        @Override
        public void set(int index, double lat, double lon, double ele) {
            throw new RuntimeException("cannot change EMPTY PointList");
        }

        @Override
        public void add(double lat, double lon, double ele) {
            throw new RuntimeException("cannot change EMPTY PointList");
        }

        @Override
        public double getLatitude(int index) {
            throw new RuntimeException("cannot access EMPTY PointList");
        }

        @Override
        public double getLongitude(int index) {
            throw new RuntimeException("cannot access EMPTY PointList");
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public void clear() {
            throw new RuntimeException("cannot change EMPTY PointList");
        }

        @Override
        public void trimToSize(int newSize) {
            throw new RuntimeException("cannot change EMPTY PointList");
        }

        @Override
        public void parse2DJSON(String str) {
            throw new RuntimeException("cannot change EMPTY PointList");
        }

        @Override
        public double calcDistance(DistanceCalc calc) {
            throw new UnsupportedOperationException("cannot access EMPTY PointList");
        }

        @Override
        public PointList copy(int from, int end) {
            throw new RuntimeException("cannot copy EMPTY PointList");
        }

        @Override
        public PointList clone(boolean reverse) {
            throw new UnsupportedOperationException("cannot access EMPTY PointList");
        }

        @Override
        public double getElevation(int index) {
            throw new UnsupportedOperationException("cannot access EMPTY PointList");
        }

        @Override
        public double getLat(int index) {
            throw new UnsupportedOperationException("cannot access EMPTY PointList");
        }

        @Override
        public double getLon(int index) {
            throw new UnsupportedOperationException("cannot access EMPTY PointList");
        }

        @Override
        public double getEle(int index) {
            throw new UnsupportedOperationException("cannot access EMPTY PointList");
        }

        @Override
        public List<Double[]> toGeoJson() {
            throw new UnsupportedOperationException("cannot access EMPTY PointList");
        }

        @Override
        public void reverse() {
            throw new UnsupportedOperationException("cannot change EMPTY PointList");
        }

        @Override
        public int getSize() {
            return 0;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public GHPoint3D toGHPoint(int index) {
            throw new UnsupportedOperationException("cannot access EMPTY PointList");
        }

        @Override
        public boolean is3D() {
            throw new UnsupportedOperationException("cannot access EMPTY PointList");
        }
    };

    public PointList() {
        this(10, false);
    }

    public PointList(int cap, boolean is3D) {
        this.latitudes = new double[cap];
        this.longitudes = new double[cap];
        this.is3D = is3D;
        if (is3D) {
            this.elevations = new double[cap];
        }
    }

    @Override
    public boolean is3D() {
        return this.is3D;
    }

    @Override
    public int getDimension() {
        if (this.is3D) {
            return 3;
        }
        return 2;
    }

    @Override
    public void ensureNode(int nodeId) {
        this.incCap(nodeId + 1);
    }

    @Override
    public void setNode(int nodeId, double lat, double lon) {
        this.set(nodeId, lat, lon, Double.NaN);
    }

    @Override
    public void setNode(int nodeId, double lat, double lon, double ele) {
        this.set(nodeId, lat, lon, ele);
    }

    public void set(int index, double lat, double lon, double ele) {
        if (index >= this.size) {
            throw new ArrayIndexOutOfBoundsException("index has to be smaller than size " + this.size);
        }
        this.latitudes[index] = lat;
        this.longitudes[index] = lon;
        if (this.is3D) {
            this.elevations[index] = ele;
        } else if (!Double.isNaN(ele)) {
            throw new IllegalStateException("This is a 2D list we cannot store elevation: " + ele);
        }
    }

    private void incCap(int newSize) {
        if (newSize <= this.latitudes.length) {
            return;
        }
        int cap = newSize * 2;
        if (cap < 15) {
            cap = 15;
        }
        this.latitudes = Arrays.copyOf(this.latitudes, cap);
        this.longitudes = Arrays.copyOf(this.longitudes, cap);
        if (this.is3D) {
            this.elevations = Arrays.copyOf(this.elevations, cap);
        }
    }

    public void add(double lat, double lon) {
        if (this.is3D) {
            throw new IllegalStateException("Cannot add point without elevation data in 3D mode");
        }
        this.add(lat, lon, Double.NaN);
    }

    public void add(double lat, double lon, double ele) {
        int newSize = this.size + 1;
        this.incCap(newSize);
        this.latitudes[this.size] = lat;
        this.longitudes[this.size] = lon;
        if (this.is3D) {
            this.elevations[this.size] = ele;
        } else if (!Double.isNaN(ele)) {
            throw new IllegalStateException("This is a 2D list we cannot store elevation: " + ele);
        }
        this.size = newSize;
    }

    public void add(PointAccess nodeAccess, int index) {
        if (this.is3D) {
            this.add(nodeAccess.getLatitude(index), nodeAccess.getLongitude(index), nodeAccess.getElevation(index));
        } else {
            this.add(nodeAccess.getLatitude(index), nodeAccess.getLongitude(index));
        }
    }

    public void add(GHPoint point) {
        if (this.is3D) {
            this.add(point.lat, point.lon, ((GHPoint3D)point).ele);
        } else {
            this.add(point.lat, point.lon);
        }
    }

    public void add(PointList points) {
        int newSize = this.size + points.getSize();
        this.incCap(newSize);
        for (int i = 0; i < points.getSize(); ++i) {
            int tmp = this.size + i;
            this.latitudes[tmp] = points.getLatitude(i);
            this.longitudes[tmp] = points.getLongitude(i);
            if (!this.is3D) continue;
            this.elevations[tmp] = points.getElevation(i);
        }
        this.size = newSize;
    }

    public int size() {
        return this.size;
    }

    public int getSize() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public double getLat(int index) {
        return this.getLatitude(index);
    }

    @Override
    public double getLatitude(int index) {
        if (index >= this.size) {
            throw new ArrayIndexOutOfBoundsException(ERR_MSG + " index:" + index + ", size:" + this.size);
        }
        return this.latitudes[index];
    }

    @Override
    public double getLon(int index) {
        return this.getLongitude(index);
    }

    @Override
    public double getLongitude(int index) {
        if (index >= this.size) {
            throw new ArrayIndexOutOfBoundsException(ERR_MSG + " index:" + index + ", size:" + this.size);
        }
        return this.longitudes[index];
    }

    @Override
    public double getElevation(int index) {
        if (index >= this.size) {
            throw new ArrayIndexOutOfBoundsException(ERR_MSG + " index:" + index + ", size:" + this.size);
        }
        if (!this.is3D) {
            return Double.NaN;
        }
        return this.elevations[index];
    }

    @Override
    public double getEle(int index) {
        return this.getElevation(index);
    }

    public void reverse() {
        int max = this.size / 2;
        for (int i = 0; i < max; ++i) {
            int swapIndex = this.size - i - 1;
            double tmp = this.latitudes[i];
            this.latitudes[i] = this.latitudes[swapIndex];
            this.latitudes[swapIndex] = tmp;
            tmp = this.longitudes[i];
            this.longitudes[i] = this.longitudes[swapIndex];
            this.longitudes[swapIndex] = tmp;
            if (!this.is3D) continue;
            tmp = this.elevations[i];
            this.elevations[i] = this.elevations[swapIndex];
            this.elevations[swapIndex] = tmp;
        }
    }

    public void clear() {
        this.size = 0;
    }

    public void trimToSize(int newSize) {
        if (newSize > this.size) {
            throw new IllegalArgumentException("new size needs be smaller than old size");
        }
        this.size = newSize;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.size; ++i) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append('(');
            sb.append(this.latitudes[i]);
            sb.append(',');
            sb.append(this.longitudes[i]);
            if (this.is3D) {
                sb.append(',');
                sb.append(this.elevations[i]);
            }
            sb.append(')');
        }
        return sb.toString();
    }

    public List<Double[]> toGeoJson() {
        return this.toGeoJson(this.is3D);
    }

    public List<Double[]> toGeoJson(boolean includeElevation) {
        ArrayList<Double[]> points = new ArrayList<Double[]>(this.size);
        for (int i = 0; i < this.size; ++i) {
            if (includeElevation) {
                points.add(new Double[]{Helper.round6(this.getLongitude(i)), Helper.round6(this.getLatitude(i)), Helper.round2(this.getElevation(i))});
                continue;
            }
            points.add(new Double[]{Helper.round6(this.getLongitude(i)), Helper.round6(this.getLatitude(i))});
        }
        return points;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        PointList other = (PointList)obj;
        if (other.isEmpty() && other.isEmpty()) {
            return true;
        }
        if (this.getSize() != other.getSize() || this.is3D() != other.is3D()) {
            return false;
        }
        for (int i = 0; i < this.size; ++i) {
            if (!NumHelper.equalsEps(this.latitudes[i], other.latitudes[i])) {
                return false;
            }
            if (!NumHelper.equalsEps(this.longitudes[i], other.longitudes[i])) {
                return false;
            }
            if (!this.is3D || NumHelper.equalsEps(this.elevations[i], other.elevations[i])) continue;
            return false;
        }
        return true;
    }

    public PointList clone(boolean reverse) {
        PointList clonePL = new PointList(this.size, this.is3D);
        if (this.is3D) {
            for (int i = 0; i < this.size; ++i) {
                clonePL.add(this.latitudes[i], this.longitudes[i], this.elevations[i]);
            }
        } else {
            for (int i = 0; i < this.size; ++i) {
                clonePL.add(this.latitudes[i], this.longitudes[i]);
            }
        }
        if (reverse) {
            clonePL.reverse();
        }
        return clonePL;
    }

    public PointList copy(int from, int end) {
        if (from > end) {
            throw new IllegalArgumentException("from must be smaller or equals to end");
        }
        if (from < 0 || end > this.size) {
            throw new IllegalArgumentException("Illegal interval: " + from + ", " + end + ", size:" + this.size);
        }
        PointList copyPL = new PointList(this.size, this.is3D);
        if (this.is3D) {
            for (int i = from; i < end; ++i) {
                copyPL.add(this.latitudes[i], this.longitudes[i], this.elevations[i]);
            }
        } else {
            for (int i = from; i < end; ++i) {
                copyPL.add(this.latitudes[i], this.longitudes[i], Double.NaN);
            }
        }
        return copyPL;
    }

    public int hashCode() {
        int hash = 5;
        for (int i = 0; i < this.latitudes.length; ++i) {
            hash = 73 * hash + (int)Math.round(this.latitudes[i] * 1000000.0);
            hash = 73 * hash + (int)Math.round(this.longitudes[i] * 1000000.0);
        }
        hash = 73 * hash + this.size;
        return hash;
    }

    public double calcDistance(DistanceCalc calc) {
        double prevLat = Double.NaN;
        double prevLon = Double.NaN;
        double prevEle = Double.NaN;
        double dist = 0.0;
        for (int i = 0; i < this.size; ++i) {
            if (i > 0) {
                dist = this.is3D ? (dist += distCalc3D.calcDist(prevLat, prevLon, prevEle, this.latitudes[i], this.longitudes[i], this.elevations[i])) : (dist += calc.calcDist(prevLat, prevLon, this.latitudes[i], this.longitudes[i]));
            }
            prevLat = this.latitudes[i];
            prevLon = this.longitudes[i];
            if (!this.is3D) continue;
            prevEle = this.elevations[i];
        }
        return dist;
    }

    public void parse2DJSON(String str) {
        for (String latlon : str.split("\\[")) {
            if (latlon.trim().length() == 0) continue;
            String[] ll = latlon.split(",");
            String lat = ll[1].replace("]", "").trim();
            this.add(Double.parseDouble(lat), Double.parseDouble(ll[0].trim()), Double.NaN);
        }
    }

    public GHPoint3D toGHPoint(int index) {
        return new GHPoint3D(this.getLatitude(index), this.getLongitude(index), this.getElevation(index));
    }

    int getCapacity() {
        return this.latitudes.length;
    }

    @Override
    public Iterator<GHPoint3D> iterator() {
        return new Iterator<GHPoint3D>(){
            int counter;

            @Override
            public boolean hasNext() {
                return this.counter < PointList.this.getSize();
            }

            @Override
            public GHPoint3D next() {
                GHPoint3D point = PointList.this.toGHPoint(this.counter);
                ++this.counter;
                return point;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported.");
            }
        };
    }

}

