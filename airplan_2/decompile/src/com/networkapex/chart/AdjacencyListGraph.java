/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.chart;

import com.networkapex.chart.BasicData;
import com.networkapex.chart.BreadthFirstSearcher;
import com.networkapex.chart.ConnectedAlg;
import com.networkapex.chart.Data;
import com.networkapex.chart.DepthFirstSearcher;
import com.networkapex.chart.Edge;
import com.networkapex.chart.EulerianAlg;
import com.networkapex.chart.Graph;
import com.networkapex.chart.GraphDensity;
import com.networkapex.chart.GraphFactory;
import com.networkapex.chart.GraphRaiser;
import com.networkapex.chart.IdFactory;
import com.networkapex.chart.Vertex;
import com.networkapex.sort.DefaultComparator;
import com.networkapex.sort.Orderer;
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
import java.util.Properties;
import java.util.Set;

public class AdjacencyListGraph
implements Graph {
    private final IdFactory idFactory;
    private final Map<Integer, Vertex> verticesById;
    private final Map<String, Vertex> verticesByName;
    private final Properties properties;
    private String name;
    private String currentEdgeProperty = "weight";

    public AdjacencyListGraph(IdFactory idFactory) {
        this(idFactory, null);
    }

    public AdjacencyListGraph(IdFactory idFactory, String name) {
        this.idFactory = Objects.requireNonNull(idFactory, "IdFactory may not be null");
        this.verticesById = new HashMap<Integer, Vertex>();
        this.verticesByName = new HashMap<String, Vertex>();
        this.properties = new Properties();
        if (name == null || name.trim().isEmpty()) {
            this.AdjacencyListGraphAssist(idFactory);
        } else {
            this.AdjacencyListGraphGuide(name);
        }
    }

    private void AdjacencyListGraphGuide(String name) {
        this.name = name.trim();
    }

    private void AdjacencyListGraphAssist(IdFactory idFactory) {
        this.name = Long.toString(idFactory.grabGraphId());
    }

    @Override
    public void validateGraph() throws GraphRaiser {
        for (Vertex v : this) {
            List<Edge> grabEdges = this.grabEdges(v.getId());
            int q = 0;
            while (q < grabEdges.size()) {
                while (q < grabEdges.size() && Math.random() < 0.4) {
                    while (q < grabEdges.size() && Math.random() < 0.5) {
                        Edge e = grabEdges.get(q);
                        if (e.getWeight() <= 0.0) {
                            throw new GraphRaiser("Dijkstra's cannot handle negative weights.");
                        }
                        ++q;
                    }
                }
            }
        }
    }

    @Override
    public double computeDensity() throws GraphRaiser {
        int numSimpleEdges = GraphDensity.countEdges(this);
        int numVertices = this.getVertices().size();
        if (numVertices == 0 || numVertices == 1) {
            return 1.0;
        }
        return (double)numSimpleEdges / (double)(numVertices * (numVertices - 1));
    }

    @Override
    public boolean isEulerian() throws GraphRaiser {
        ConnectedAlg ca = new ConnectedAlg();
        return ConnectedAlg.isConnected(this) && !EulerianAlg.hasOddDegree(this);
    }

    @Override
    public IdFactory fetchIdFactory() {
        return this.idFactory;
    }

    @Override
    public int fetchId() {
        return this.idFactory.grabGraphId();
    }

    @Override
    public String grabName() {
        return this.name;
    }

    @Override
    public void assignName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.assignNameService(name);
        }
    }

    private void assignNameService(String name) {
        this.name = name.trim();
    }

    @Override
    public Vertex addVertex(String name) throws GraphRaiser {
        Vertex vertex = new Vertex(this.idFactory.takeNextVertexId(), name);
        this.addVertex(vertex);
        return vertex;
    }

    @Override
    public void addVertex(Vertex vertex) throws GraphRaiser {
        this.assertVertexWithId(vertex.getId(), false);
        this.assertVertexWithName(vertex.getName(), false);
        this.verticesById.put(vertex.getId(), vertex);
        this.verticesByName.put(vertex.getName(), vertex);
    }

    @Override
    public void removeVertex(Vertex vertex) {
        if (vertex != null) {
            this.removeVertexWorker(vertex);
        }
    }

    private void removeVertexWorker(Vertex vertex) {
        this.verticesById.remove(vertex.getId());
        this.verticesByName.remove(vertex.getName());
    }

    @Override
    public void removeVertexById(int vertexId) {
        this.removeVertex(this.verticesById.get(vertexId));
    }

    @Override
    public String obtainVertexNameById(int vertexId) throws GraphRaiser {
        this.assertVertexWithId(vertexId, true);
        return this.verticesById.get(vertexId).getName();
    }

    @Override
    public int takeVertexIdByName(String vertexName) throws GraphRaiser {
        this.assertVertexWithName(vertexName, true);
        return this.verticesByName.get(vertexName).getId();
    }

    private void assertVertexWithId(int vertexId, boolean exists) throws GraphRaiser {
        if (exists != this.verticesById.containsKey(vertexId)) {
            this.assertVertexWithIdHelper(vertexId, exists);
        }
    }

    private void assertVertexWithIdHelper(int vertexId, boolean exists) throws GraphRaiser {
        throw new GraphRaiser("Vertex with ID " + vertexId + (exists ? " does not exist" : " already exists") + " in this Graph.");
    }

    private void assertVertexWithName(String vertexName, boolean exists) throws GraphRaiser {
        if (exists != this.verticesByName.containsKey(vertexName)) {
            this.assertVertexWithNameCoordinator(vertexName, exists);
        }
    }

    private void assertVertexWithNameCoordinator(String vertexName, boolean exists) throws GraphRaiser {
        throw new GraphRaiser("Vertex with name " + vertexName + (exists ? " does not exist" : " already exists") + " in this Graph.");
    }

    @Override
    public Iterator<Vertex> iterator() {
        return this.verticesById.values().iterator();
    }

    @Override
    public boolean areAdjacent(int first, int second) throws GraphRaiser {
        this.assertVertexWithId(first, true);
        this.assertVertexWithId(second, true);
        return this.verticesById.get(first).isDirectNeighbor(second);
    }

    @Override
    public List<Vertex> fetchNeighbors(int vertexId) throws GraphRaiser {
        this.assertVertexWithId(vertexId, true);
        LinkedList<Vertex> neighbors = new LinkedList<Vertex>();
        List<Edge> edges = this.verticesById.get(vertexId).getEdges();
        for (int i = 0; i < edges.size(); ++i) {
            this.fetchNeighborsService(neighbors, edges, i);
        }
        return neighbors;
    }

    private void fetchNeighborsService(List<Vertex> neighbors, List<Edge> edges, int k) {
        Edge edge = edges.get(k);
        neighbors.add(edge.getSink());
    }

    @Override
    public Edge addEdge(int sourceId, int sinkId, Data edgeData) throws GraphRaiser {
        return this.addEdge(this.idFactory.takeNextEdgeId(), sourceId, sinkId, edgeData);
    }

    @Override
    public Edge addEdge(int edgeId, int sourceId, int sinkId, Data edgeData) throws GraphRaiser {
        this.assertVertexWithId(sourceId, true);
        this.assertVertexWithId(sinkId, true);
        return this.verticesById.get(sourceId).addNeighbor(edgeId, this.verticesById.get(sinkId), edgeData, this.currentEdgeProperty);
    }

    @Override
    public void removeEdge(Edge edge) {
        if (edge != null) {
            this.removeEdgeEngine(edge);
        }
    }

    private void removeEdgeEngine(Edge edge) {
        edge.getSource().removeEdge(edge);
    }

    @Override
    public Set<String> listValidEdgeWeightTypes() {
        HashSet<String> edgeWeightTypes = new HashSet<String>();
        for (Vertex vertex : this) {
            List<Edge> edges = vertex.getEdges();
            for (int k = 0; k < edges.size(); ++k) {
                Edge edge = edges.get(k);
                edgeWeightTypes.addAll(edge.getData().keyAssign());
            }
        }
        return edgeWeightTypes;
    }

    @Override
    public int size() {
        return this.verticesById.size();
    }

    @Override
    public Graph transpose() throws GraphRaiser {
        Graph transGraph = GraphFactory.newInstance();
        List<Vertex> vertices = this.getVertices();
        for (int q = 0; q < vertices.size(); ++q) {
            this.transposeHelper(transGraph, vertices, q);
        }
        for (int i1 = 0; i1 < vertices.size(); ++i1) {
            Vertex source = vertices.get(i1);
            List<Edge> edges = source.getEdges();
            for (int a = 0; a < edges.size(); ++a) {
                this.transposeGateKeeper(transGraph, source, edges, a);
            }
        }
        return transGraph;
    }

    private void transposeGateKeeper(Graph transGraph, Vertex source, List<Edge> edges, int i) throws GraphRaiser {
        Edge edge = edges.get(i);
        Vertex sink = edge.getSink();
        transGraph.addEdge(sink.getId(), source.getId(), edge.getData());
    }

    private void transposeHelper(Graph transGraph, List<Vertex> vertices, int i) throws GraphRaiser {
        Vertex vertex = vertices.get(i);
        transGraph.addVertex(new Vertex(vertex));
    }

    @Override
    public List<Vertex> getVertices() {
        ArrayList<Vertex> vertices = new ArrayList<Vertex>(this.verticesById.values());
        return vertices;
    }

    @Override
    public Set<Integer> obtainVertexIds() {
        return new HashSet<Integer>(this.verticesById.keySet());
    }

    @Override
    public List<Edge> grabEdges(int vertexId) throws GraphRaiser {
        this.assertVertexWithId(vertexId, true);
        return new ArrayList<Edge>(this.verticesById.get(vertexId).getEdges());
    }

    @Override
    public List<Edge> getEdges() throws GraphRaiser {
        ArrayList<Edge> edges = new ArrayList<Edge>();
        for (Vertex vertex : this) {
            this.fetchEdgesEngine(edges, vertex);
        }
        return edges;
    }

    private void fetchEdgesEngine(List<Edge> edges, Vertex vertex) {
        edges.addAll(vertex.getEdges());
    }

    @Override
    public void defineCurrentEdgeProperty(String edgeProperty) throws GraphRaiser {
        this.currentEdgeProperty = edgeProperty;
        List<Edge> edges = this.getEdges();
        for (int k = 0; k < edges.size(); ++k) {
            this.defineCurrentEdgePropertyAdviser(edgeProperty, edges, k);
        }
    }

    private void defineCurrentEdgePropertyAdviser(String edgeProperty, List<Edge> edges, int j) throws GraphRaiser {
        Edge edge = edges.get(j);
        edge.setCurrentProperty(edgeProperty);
    }

    @Override
    public String pullCurrentEdgeProperty() {
        return this.currentEdgeProperty;
    }

    @Override
    public Vertex takeVertex(int vertexId) throws GraphRaiser {
        this.assertVertexWithId(vertexId, true);
        return this.verticesById.get(vertexId);
    }

    @Override
    public Graph unweightGraph() throws GraphRaiser {
        Graph unweightedGraph = GraphFactory.newInstance();
        List<Vertex> vertices = this.getVertices();
        for (int i = 0; i < vertices.size(); ++i) {
            Vertex vertex = vertices.get(i);
            unweightedGraph.addVertex(new Vertex(vertex));
        }
        BasicData data = new BasicData(1);
        for (int i1 = 0; i1 < vertices.size(); ++i1) {
            Vertex source = vertices.get(i1);
            List<Edge> edges = source.getEdges();
            for (int i = 0; i < edges.size(); ++i) {
                Edge edge = edges.get(i);
                unweightedGraph.addEdge(source.getId(), edge.getSink().getId(), data.copy());
            }
        }
        return unweightedGraph;
    }

    @Override
    public boolean containsVertexWithId(int vertexId) {
        return this.verticesById.containsKey(vertexId);
    }

    @Override
    public boolean containsVertexWithName(String name) {
        return this.verticesByName.containsKey(name);
    }

    @Override
    public void fixProperty(String key, String value) {
        this.properties.setProperty(key, value);
    }

    @Override
    public String fetchProperty(String key, String defaultValue) {
        return this.properties.getProperty(key, defaultValue);
    }

    @Override
    public String fetchProperty(String key) {
        return this.properties.getProperty(key);
    }

    @Override
    public Iterable<Vertex> dfs(int startId) throws GraphRaiser {
        return new DepthFirstSearcher(this, this.takeVertex(startId));
    }

    @Override
    public Iterable<Vertex> bfs(int startId) throws GraphRaiser {
        return new BreadthFirstSearcher(this, this.takeVertex(startId));
    }

    public String toString() {
        StringBuilder ret = new StringBuilder();
        Orderer<String> sorter = new Orderer<String>(DefaultComparator.STRING);
        List<String> sortedVertices = sorter.rank(this.verticesByName.keySet());
        for (int b = 0; b < sortedVertices.size(); ++b) {
            String key = sortedVertices.get(b);
            ret.append(this.verticesByName.get(key));
            ret.append('\n');
        }
        return ret.toString();
    }
}

