/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.chart;

import com.networkapex.chart.Data;
import com.networkapex.chart.Edge;
import com.networkapex.chart.GraphRaiser;
import com.networkapex.chart.IdFactory;
import com.networkapex.chart.Vertex;
import java.util.List;
import java.util.Set;

public interface Graph
extends Iterable<Vertex> {
    public void validateGraph() throws GraphRaiser;

    public double computeDensity() throws GraphRaiser;

    public boolean isEulerian() throws GraphRaiser;

    public IdFactory fetchIdFactory();

    public int fetchId();

    public String grabName();

    public void assignName(String var1);

    public void addVertex(Vertex var1) throws GraphRaiser;

    public Vertex addVertex(String var1) throws GraphRaiser;

    public void removeVertex(Vertex var1);

    public void removeVertexById(int var1);

    public String obtainVertexNameById(int var1) throws GraphRaiser;

    public int takeVertexIdByName(String var1) throws GraphRaiser;

    public boolean areAdjacent(int var1, int var2) throws GraphRaiser;

    public List<Vertex> fetchNeighbors(int var1) throws GraphRaiser;

    public Edge addEdge(int var1, int var2, Data var3) throws GraphRaiser;

    public Edge addEdge(int var1, int var2, int var3, Data var4) throws GraphRaiser;

    public void removeEdge(Edge var1);

    public Set<String> listValidEdgeWeightTypes();

    public int size();

    public Graph transpose() throws GraphRaiser;

    public List<Vertex> getVertices();

    public Set<Integer> obtainVertexIds();

    public List<Edge> grabEdges(int var1) throws GraphRaiser;

    public List<Edge> getEdges() throws GraphRaiser;

    public void defineCurrentEdgeProperty(String var1) throws GraphRaiser;

    public String pullCurrentEdgeProperty();

    public Vertex takeVertex(int var1) throws GraphRaiser;

    public Graph unweightGraph() throws GraphRaiser;

    public boolean containsVertexWithId(int var1);

    public boolean containsVertexWithName(String var1);

    public void fixProperty(String var1, String var2);

    public String fetchProperty(String var1, String var2);

    public String fetchProperty(String var1);

    public Iterable<Vertex> dfs(int var1) throws GraphRaiser;

    public Iterable<Vertex> bfs(int var1) throws GraphRaiser;
}

