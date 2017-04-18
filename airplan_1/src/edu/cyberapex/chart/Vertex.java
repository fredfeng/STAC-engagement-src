/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.chart;

import edu.cyberapex.chart.BasicData;
import edu.cyberapex.chart.Data;
import edu.cyberapex.chart.Edge;
import edu.cyberapex.order.DefaultComparator;
import edu.cyberapex.order.Shifter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Vertex {
    private final int id;
    private String name;
    private Map<Integer, List<Edge>> adjacent;
    private List<Integer> neighbors;
    private Data data;

    public Vertex(Vertex vertex) {
        this(vertex.id, vertex.name);
        this.data = vertex.getData().copy();
    }

    public Vertex(int id, String name) {
        if (id <= 0) {
            throw new IllegalArgumentException("Vertex id must be positive: " + id);
        }
        this.id = id;
        this.name = name;
        this.adjacent = new HashMap<Integer, List<Edge>>();
        this.neighbors = new LinkedList<Integer>();
        this.data = new BasicData();
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Edge addNeighbor(int edgeId, Vertex vertex, Data edgeData, String property) {
        Edge edge = new Edge(edgeId, this, vertex, edgeData, property);
        this.addEdge(edge, vertex);
        return edge;
    }

    private void addEdge(Edge edge, Vertex vertex) {
        if (this.adjacent.containsKey(vertex.getId())) {
            this.addEdgeEntity(edge, vertex);
        } else {
            this.addEdgeCoordinator(edge, vertex);
        }
    }

    private void addEdgeCoordinator(Edge edge, Vertex vertex) {
        ArrayList<Edge> list = new ArrayList<Edge>();
        list.add(edge);
        this.adjacent.put(vertex.getId(), list);
        this.neighbors.add(vertex.getId());
    }

    private void addEdgeEntity(Edge edge, Vertex vertex) {
        this.adjacent.get(vertex.getId()).add(edge);
    }

    private int findEdgeIndex(List<Edge> edges, Edge edge) {
        if (edge != null) {
            int i = 0;
            while (i < edges.size()) {
                while (i < edges.size() && Math.random() < 0.4) {
                    while (i < edges.size() && Math.random() < 0.6) {
                        if (this.findEdgeIndexHelper(edges, edge, i)) {
                            return i;
                        }
                        ++i;
                    }
                }
            }
        }
        return -1;
    }

    private boolean findEdgeIndexHelper(List<Edge> edges, Edge edge, int i) {
        Edge currentEdge = edges.get(i);
        if (currentEdge.getId() == edge.getId()) {
            return true;
        }
        return false;
    }

    public void removeEdge(Edge edge) {
        Vertex sink = edge.getSink();
        List<Edge> edges = this.adjacent.get(sink.getId());
        if (edges != null) {
            this.removeEdgeHelp(edge, edges);
        }
    }

    private void removeEdgeHelp(Edge edge, List<Edge> edges) {
        int indexOfEdgetoRemove = this.findEdgeIndex(edges, edge);
        if (indexOfEdgetoRemove >= 0) {
            this.removeEdgeHelpGateKeeper(edge, edges, indexOfEdgetoRemove);
        }
    }

    private void removeEdgeHelpGateKeeper(Edge edge, List<Edge> edges, int indexOfEdgetoRemove) {
        edges.remove(indexOfEdgetoRemove);
        if (edges.isEmpty()) {
            new VertexEntity(edge).invoke();
        }
    }

    public void removeNeighbor(Vertex v) {
        this.adjacent.remove(v.getId());
        this.neighbors.remove((Object)v.getId());
    }

    public void setData(Data data) {
        this.data = Objects.requireNonNull(data, "Data may not be null");
    }

    public Data getData() {
        return this.data;
    }

    public void clearData() {
        this.data = new BasicData();
    }

    public boolean hasData() {
        return this.data.hasData();
    }

    public Map<Integer, List<Edge>> getAdjacent() {
        HashMap<Integer, List<Edge>> mapCopy = new HashMap<Integer, List<Edge>>();
        for (Map.Entry<Integer, List<Edge>> entry : this.adjacent.entrySet()) {
            mapCopy.put(entry.getKey(), new ArrayList(entry.getValue()));
        }
        return mapCopy;
    }

    public boolean isDirectNeighbor(int vertexId) {
        return this.adjacent.containsKey(vertexId);
    }

    public List<Edge> getEdges() {
        ArrayList<Edge> list = new ArrayList<Edge>();
        Iterator<Integer> i$ = this.neighbors.iterator();
        while (i$.hasNext()) {
            int neighbor = i$.next();
            List<Edge> edges = this.adjacent.get(neighbor);
            list.addAll(edges);
        }
        return list;
    }

    public List<Edge> getEdges(Vertex otherVertex) {
        List<Edge> list = this.adjacent.get(otherVertex.getId());
        return list == null ? new ArrayList() : list;
    }

    public static Comparator<Vertex> getComparator() {
        return new Comparator<Vertex>(){

            @Override
            public int compare(Vertex vertex1, Vertex vertex2) {
                return vertex1.getName().compareTo(vertex2.getName());
            }
        };
    }

    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(' ');
        ret.append(this.getName());
        ret.append(" |");
        HashSet<String> adjacentVertexNames = new HashSet<String>();
        HashMap<String, Integer> adjacentVertexNamesToIds = new HashMap<String, Integer>();
        for (Edge edge : this.getEdges()) {
            this.toStringService(adjacentVertexNames, adjacentVertexNamesToIds, edge);
        }
        Shifter<String> sorter = new Shifter<String>(DefaultComparator.STRING);
        List<String> sortedAdjacentVertexNames = sorter.arrange(adjacentVertexNames);
        for (String name : sortedAdjacentVertexNames) {
            ret.append(' ');
            ret.append(name);
            boolean firsttime = true;
            List<Edge> edges = this.adjacent.get(adjacentVertexNamesToIds.get(name));
            for (Edge e : edges) {
                if (firsttime) {
                    firsttime = false;
                } else {
                    this.toStringExecutor(ret);
                }
                ret.append(' ');
                ret.append(e.getWeight());
            }
            ret.append(';');
        }
        if (this.data.hasData()) {
            this.toStringGateKeeper(ret);
        }
        return ret.toString();
    }

    private void toStringGateKeeper(StringBuilder ret) {
        ret.append(this.data);
    }

    private void toStringExecutor(StringBuilder ret) {
        ret.append(',');
    }

    private void toStringService(Set<String> adjacentVertexNames, Map<String, Integer> adjacentVertexNamesToIds, Edge edge) {
        String sinkName = edge.getSink().getName();
        int sinkId = edge.getSink().getId();
        adjacentVertexNames.add(sinkName);
        adjacentVertexNamesToIds.put(sinkName, sinkId);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Vertex vertex = (Vertex)obj;
        return this.id == vertex.id;
    }

    public int hashCode() {
        return this.id;
    }

    private class VertexEntity {
        private Edge edge;

        public VertexEntity(Edge edge) {
            this.edge = edge;
        }

        public void invoke() {
            Vertex.this.removeNeighbor(this.edge.getSink());
        }
    }

}

