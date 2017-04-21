/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.chart;

import edu.cyberapex.chart.BasicData;
import edu.cyberapex.chart.ChartFailure;
import edu.cyberapex.chart.Data;
import edu.cyberapex.chart.Vertex;
import java.util.Comparator;
import java.util.Objects;

public class Edge
implements Comparable<Edge> {
    private final int id;
    private final Vertex source;
    private final Vertex sink;
    private Data data;
    private String currentProperty;

    public Edge(int id, Vertex source, Vertex sink, Data edgeData) {
        this(id, source, sink, edgeData, "weight");
    }

    public Edge(int id, Vertex source, Vertex sink, Data edgeData, String currentProperty) {
        if (id <= 0) {
            throw new IllegalArgumentException("Edge id must be positive: " + id);
        }
        this.id = id;
        this.source = source;
        this.sink = sink;
        this.data = edgeData != null ? edgeData : new BasicData();
        this.currentProperty = currentProperty;
    }

    public int getId() {
        return this.id;
    }

    public Double getWeight() {
        double d;
        String value = this.data.fetch(this.currentProperty);
        if (value == null) {
            return null;
        }
        try {
            d = Double.valueOf(value);
        }
        catch (NumberFormatException e) {
            return null;
        }
        return d;
    }

    public void setProperty(String property, double value) {
        this.setProperty(property, Double.toString(value));
    }

    public void setProperty(String property, String value) {
        this.data.put(property, value);
    }

    public Data getData() {
        return this.data;
    }

    public boolean hasData() {
        return this.data.hasData();
    }

    public void setData(Data data) {
        this.data = Objects.requireNonNull(data, "Edge Data may not be null");
    }

    public void clearData() {
        this.data = new BasicData();
    }

    public Vertex getSource() {
        return this.source;
    }

    public Vertex getSink() {
        return this.sink;
    }

    public String getCurrentProperty() {
        return this.currentProperty;
    }

    public void setCurrentProperty(String newCurrentProperty) throws ChartFailure {
        if (!this.data.containsKey(newCurrentProperty)) {
            this.setCurrentPropertyService(newCurrentProperty);
        }
        this.currentProperty = newCurrentProperty;
    }

    private void setCurrentPropertyService(String newCurrentProperty) throws ChartFailure {
        throw new ChartFailure("Invalid Edge property " + newCurrentProperty);
    }

    public String toString() {
        return "" + this.id + ": {" + this.source.getId() + ", " + this.sink.getId() + ", " + this.getWeight() + "}";
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Edge edge = (Edge)obj;
        return this.id == edge.id;
    }

    public int hashCode() {
        return this.id;
    }

    @Override
    public int compareTo(Edge e) {
        if (this.id == e.getId()) {
            return 0;
        }
        if (this.id < e.getId()) {
            return -1;
        }
        return 1;
    }

    public static Comparator<Edge> getComparator() {
        return new Comparator<Edge>(){

            @Override
            public int compare(Edge edge1, Edge edge2) {
                return edge1.compareTo(edge2);
            }
        };
    }

}

