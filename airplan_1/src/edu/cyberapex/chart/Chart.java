/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.chart;

import edu.cyberapex.chart.ChartFailure;
import edu.cyberapex.chart.Data;
import edu.cyberapex.chart.Edge;
import edu.cyberapex.chart.IdFactory;
import edu.cyberapex.chart.Vertex;
import java.util.List;
import java.util.Set;

public interface Chart
extends Iterable<Vertex> {
    public double computeDensity() throws ChartFailure;

    public boolean hasOddDegree() throws ChartFailure;

    public IdFactory pullIdFactory();

    public int obtainId();

    public String getName();

    public void fixName(String var1);

    public void addVertex(Vertex var1) throws ChartFailure;

    public Vertex addVertex(String var1) throws ChartFailure;

    public void removeVertex(Vertex var1);

    public void removeVertexById(int var1);

    public String takeVertexNameById(int var1) throws ChartFailure;

    public int getVertexIdByName(String var1) throws ChartFailure;

    public boolean areAdjacent(int var1, int var2) throws ChartFailure;

    public List<Vertex> getNeighbors(int var1) throws ChartFailure;

    public Edge addEdge(int var1, int var2, Data var3) throws ChartFailure;

    public Edge addEdge(int var1, int var2, int var3, Data var4) throws ChartFailure;

    public void removeEdge(Edge var1);

    public Set<String> listValidEdgeWeightTypes();

    public int size();

    public Chart transpose() throws ChartFailure;

    public List<Vertex> takeVertices();

    public Set<Integer> fetchVertexIds();

    public List<Edge> getEdges(int var1) throws ChartFailure;

    public List<Edge> grabEdges() throws ChartFailure;

    public void defineCurrentEdgeProperty(String var1) throws ChartFailure;

    public String fetchCurrentEdgeProperty();

    public Vertex obtainVertex(int var1) throws ChartFailure;

    public Chart unweightChart() throws ChartFailure;

    public boolean containsVertexWithId(int var1);

    public boolean containsVertexWithName(String var1);

    public void fixProperty(String var1, String var2);

    public String takeProperty(String var1, String var2);

    public String fetchProperty(String var1);

    public Iterable<Vertex> dfs(int var1) throws ChartFailure;

    public Iterable<Vertex> bfs(int var1) throws ChartFailure;
}

