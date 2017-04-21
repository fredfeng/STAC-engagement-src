/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.chart;

import edu.cyberapex.chart.BasicData;
import edu.cyberapex.chart.BreadthFirstSearcher;
import edu.cyberapex.chart.BreadthFirstSearcherBuilder;
import edu.cyberapex.chart.Chart;
import edu.cyberapex.chart.ChartDensity;
import edu.cyberapex.chart.ChartFactory;
import edu.cyberapex.chart.ChartFailure;
import edu.cyberapex.chart.Data;
import edu.cyberapex.chart.DepthFirstSearcher;
import edu.cyberapex.chart.Edge;
import edu.cyberapex.chart.IdFactory;
import edu.cyberapex.chart.Vertex;
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
import java.util.Properties;
import java.util.Set;

public class AdjacencyListChart
implements Chart {
    private final IdFactory idFactory;
    private final Map<Integer, Vertex> verticesById;
    private final Map<String, Vertex> verticesByName;
    private final Properties properties;
    private String name;
    private String currentEdgeProperty = "weight";

    public AdjacencyListChart(IdFactory idFactory) {
        this(idFactory, null);
    }

    public AdjacencyListChart(IdFactory idFactory, String name) {
        this.idFactory = Objects.requireNonNull(idFactory, "IdFactory may not be null");
        this.verticesById = new HashMap<Integer, Vertex>();
        this.verticesByName = new HashMap<String, Vertex>();
        this.properties = new Properties();
        if (name == null || name.trim().isEmpty()) {
            this.AdjacencyListChartTarget(idFactory);
        } else {
            this.AdjacencyListChartEngine(name);
        }
    }

    private void AdjacencyListChartEngine(String name) {
        this.name = name.trim();
    }

    private void AdjacencyListChartTarget(IdFactory idFactory) {
        this.name = Long.toString(idFactory.fetchChartId());
    }

    @Override
    public double computeDensity() throws ChartFailure {
        int numSimpleEdges = ChartDensity.countEdges(this);
        int numVertices = this.takeVertices().size();
        if (numVertices == 0 || numVertices == 1) {
            return 1.0;
        }
        return (double)numSimpleEdges / (double)(numVertices * (numVertices - 1));
    }

    @Override
    public boolean hasOddDegree() throws ChartFailure {
        List<Vertex> takeVertices = this.takeVertices();
        for (int q = 0; q < takeVertices.size(); ++q) {
            Vertex v = takeVertices.get(q);
            List<Edge> edges = this.getEdges(v.getId());
            if (edges.size() % 2 == 0) continue;
            return false;
        }
        return true;
    }

    @Override
    public IdFactory pullIdFactory() {
        return this.idFactory;
    }

    @Override
    public int obtainId() {
        return this.idFactory.fetchChartId();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void fixName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.fixNameWorker(name);
        }
    }

    private void fixNameWorker(String name) {
        this.name = name.trim();
    }

    @Override
    public Vertex addVertex(String name) throws ChartFailure {
        Vertex vertex = new Vertex(this.idFactory.fetchNextVertexId(), name);
        this.addVertex(vertex);
        return vertex;
    }

    @Override
    public void addVertex(Vertex vertex) throws ChartFailure {
        this.assertVertexWithId(vertex.getId(), false);
        this.assertVertexWithName(vertex.getName(), false);
        this.verticesById.put(vertex.getId(), vertex);
        this.verticesByName.put(vertex.getName(), vertex);
    }

    @Override
    public void removeVertex(Vertex vertex) {
        if (vertex != null) {
            this.removeVertexSupervisor(vertex);
        }
    }

    private void removeVertexSupervisor(Vertex vertex) {
        this.verticesById.remove(vertex.getId());
        this.verticesByName.remove(vertex.getName());
    }

    @Override
    public void removeVertexById(int vertexId) {
        this.removeVertex(this.verticesById.get(vertexId));
    }

    @Override
    public String takeVertexNameById(int vertexId) throws ChartFailure {
        this.assertVertexWithId(vertexId, true);
        return this.verticesById.get(vertexId).getName();
    }

    @Override
    public int getVertexIdByName(String vertexName) throws ChartFailure {
        this.assertVertexWithName(vertexName, true);
        return this.verticesByName.get(vertexName).getId();
    }

    private void assertVertexWithId(int vertexId, boolean exists) throws ChartFailure {
        if (exists != this.verticesById.containsKey(vertexId)) {
            throw new ChartFailure("Vertex with ID " + vertexId + (exists ? " does not exist" : " already exists") + " in this Graph.");
        }
    }

    private void assertVertexWithName(String vertexName, boolean exists) throws ChartFailure {
        if (exists != this.verticesByName.containsKey(vertexName)) {
            this.assertVertexWithNameEngine(vertexName, exists);
        }
    }

    private void assertVertexWithNameEngine(String vertexName, boolean exists) throws ChartFailure {
        throw new ChartFailure("Vertex with name " + vertexName + (exists ? " does not exist" : " already exists") + " in this Graph.");
    }

    @Override
    public Iterator<Vertex> iterator() {
        return this.verticesById.values().iterator();
    }

    @Override
    public boolean areAdjacent(int first, int second) throws ChartFailure {
        this.assertVertexWithId(first, true);
        this.assertVertexWithId(second, true);
        return this.verticesById.get(first).isDirectNeighbor(second);
    }

    @Override
    public List<Vertex> getNeighbors(int vertexId) throws ChartFailure {
        this.assertVertexWithId(vertexId, true);
        LinkedList<Vertex> neighbors = new LinkedList<Vertex>();
        List<Edge> edges = this.verticesById.get(vertexId).getEdges();
        for (int c = 0; c < edges.size(); ++c) {
            this.getNeighborsAid(neighbors, edges, c);
        }
        return neighbors;
    }

    private void getNeighborsAid(List<Vertex> neighbors, List<Edge> edges, int j) {
        Edge edge = edges.get(j);
        neighbors.add(edge.getSink());
    }

    @Override
    public Edge addEdge(int sourceId, int sinkId, Data edgeData) throws ChartFailure {
        return this.addEdge(this.idFactory.getNextEdgeId(), sourceId, sinkId, edgeData);
    }

    @Override
    public Edge addEdge(int edgeId, int sourceId, int sinkId, Data edgeData) throws ChartFailure {
        this.assertVertexWithId(sourceId, true);
        this.assertVertexWithId(sinkId, true);
        return this.verticesById.get(sourceId).addNeighbor(edgeId, this.verticesById.get(sinkId), edgeData, this.currentEdgeProperty);
    }

    @Override
    public void removeEdge(Edge edge) {
        if (edge != null) {
            edge.getSource().removeEdge(edge);
        }
    }

    @Override
    public Set<String> listValidEdgeWeightTypes() {
        HashSet<String> edgeWeightTypes = new HashSet<String>();
        for (Vertex vertex : this) {
            List<Edge> edges = vertex.getEdges();
            int i = 0;
            while (i < edges.size()) {
                while (i < edges.size() && Math.random() < 0.5) {
                    while (i < edges.size() && Math.random() < 0.6) {
                        new AdjacencyListChartGuide(edgeWeightTypes, edges, i).invoke();
                        ++i;
                    }
                }
            }
        }
        return edgeWeightTypes;
    }

    @Override
    public int size() {
        return this.verticesById.size();
    }

    @Override
    public Chart transpose() throws ChartFailure {
        Chart transChart = ChartFactory.newInstance();
        List<Vertex> vertices = this.takeVertices();
        for (int i = 0; i < vertices.size(); ++i) {
            this.transposeAssist(transChart, vertices, i);
        }
        for (int i1 = 0; i1 < vertices.size(); ++i1) {
            Vertex source = vertices.get(i1);
            List<Edge> edges = source.getEdges();
            for (int k = 0; k < edges.size(); ++k) {
                Edge edge = edges.get(k);
                Vertex sink = edge.getSink();
                transChart.addEdge(sink.getId(), source.getId(), edge.getData());
            }
        }
        return transChart;
    }

    private void transposeAssist(Chart transChart, List<Vertex> vertices, int a) throws ChartFailure {
        Vertex vertex = vertices.get(a);
        transChart.addVertex(new Vertex(vertex));
    }

    @Override
    public List<Vertex> takeVertices() {
        ArrayList<Vertex> vertices = new ArrayList<Vertex>(this.verticesById.values());
        return vertices;
    }

    @Override
    public Set<Integer> fetchVertexIds() {
        return new HashSet<Integer>(this.verticesById.keySet());
    }

    @Override
    public List<Edge> getEdges(int vertexId) throws ChartFailure {
        this.assertVertexWithId(vertexId, true);
        return new ArrayList<Edge>(this.verticesById.get(vertexId).getEdges());
    }

    @Override
    public List<Edge> grabEdges() throws ChartFailure {
        ArrayList<Edge> edges = new ArrayList<Edge>();
        for (Vertex vertex : this) {
            this.grabEdgesTarget(edges, vertex);
        }
        return edges;
    }

    private void grabEdgesTarget(List<Edge> edges, Vertex vertex) {
        edges.addAll(vertex.getEdges());
    }

    @Override
    public void defineCurrentEdgeProperty(String edgeProperty) throws ChartFailure {
        this.currentEdgeProperty = edgeProperty;
        List<Edge> grabEdges = this.grabEdges();
        for (int a = 0; a < grabEdges.size(); ++a) {
            Edge edge = grabEdges.get(a);
            edge.setCurrentProperty(edgeProperty);
        }
    }

    @Override
    public String fetchCurrentEdgeProperty() {
        return this.currentEdgeProperty;
    }

    @Override
    public Vertex obtainVertex(int vertexId) throws ChartFailure {
        this.assertVertexWithId(vertexId, true);
        return this.verticesById.get(vertexId);
    }

    @Override
    public Chart unweightChart() throws ChartFailure {
        Chart unweightedChart = ChartFactory.newInstance();
        List<Vertex> vertices = this.takeVertices();
        for (int i = 0; i < vertices.size(); ++i) {
            Vertex vertex = vertices.get(i);
            unweightedChart.addVertex(new Vertex(vertex));
        }
        BasicData data = new BasicData(1);
        for (int i1 = 0; i1 < vertices.size(); ++i1) {
            Vertex source = vertices.get(i1);
            List<Edge> edges = source.getEdges();
            for (int p = 0; p < edges.size(); ++p) {
                Edge edge = edges.get(p);
                unweightedChart.addEdge(source.getId(), edge.getSink().getId(), data.copy());
            }
        }
        return unweightedChart;
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
    public String takeProperty(String key, String defaultValue) {
        return this.properties.getProperty(key, defaultValue);
    }

    @Override
    public String fetchProperty(String key) {
        return this.properties.getProperty(key);
    }

    @Override
    public Iterable<Vertex> dfs(int startId) throws ChartFailure {
        return new DepthFirstSearcher(this, this.obtainVertex(startId));
    }

    @Override
    public Iterable<Vertex> bfs(int startId) throws ChartFailure {
        return new BreadthFirstSearcherBuilder().assignChart(this).assignStart(this.obtainVertex(startId)).generateBreadthFirstSearcher();
    }

    public String toString() {
        StringBuilder ret = new StringBuilder();
        Shifter<String> sorter = new Shifter<String>(DefaultComparator.STRING);
        List<String> sortedVertices = sorter.arrange(this.verticesByName.keySet());
        for (int k = 0; k < sortedVertices.size(); ++k) {
            this.toStringService(ret, sortedVertices, k);
        }
        return ret.toString();
    }

    private void toStringService(StringBuilder ret, List<String> sortedVertices, int a) {
        String key = sortedVertices.get(a);
        ret.append(this.verticesByName.get(key));
        ret.append('\n');
    }

    private class AdjacencyListChartGuide {
        private Set<String> edgeWeightTypes;
        private List<Edge> edges;
        private int j;

        public AdjacencyListChartGuide(Set<String> edgeWeightTypes, List<Edge> edges, int j) {
            this.edgeWeightTypes = edgeWeightTypes;
            this.edges = edges;
            this.j = j;
        }

        public void invoke() {
            Edge edge = this.edges.get(this.j);
            this.edgeWeightTypes.addAll(edge.getData().keySet());
        }
    }

}

