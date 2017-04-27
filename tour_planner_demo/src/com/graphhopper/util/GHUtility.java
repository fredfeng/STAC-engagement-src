/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.util;

import com.graphhopper.coll.GHBitSet;
import com.graphhopper.coll.GHBitSetImpl;
import com.graphhopper.routing.util.AllCHEdgesIterator;
import com.graphhopper.routing.util.AllEdgesIterator;
import com.graphhopper.routing.util.EdgeFilter;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.routing.util.Weighting;
import com.graphhopper.storage.CHGraph;
import com.graphhopper.storage.Directory;
import com.graphhopper.storage.GHDirectory;
import com.graphhopper.storage.Graph;
import com.graphhopper.storage.GraphExtension;
import com.graphhopper.storage.GraphHopperStorage;
import com.graphhopper.storage.GraphStorage;
import com.graphhopper.storage.MMapDirectory;
import com.graphhopper.storage.NodeAccess;
import com.graphhopper.storage.RAMDirectory;
import com.graphhopper.util.BitUtil;
import com.graphhopper.util.BreadthFirstSearch;
import com.graphhopper.util.CHEdgeExplorer;
import com.graphhopper.util.CHEdgeIterator;
import com.graphhopper.util.CHEdgeIteratorState;
import com.graphhopper.util.DepthFirstSearch;
import com.graphhopper.util.EdgeExplorer;
import com.graphhopper.util.EdgeIterator;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.PointList;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class GHUtility {
    public static List<String> getProblems(Graph g) {
        int nodeIndex = 0;
        ArrayList<String> problems = new ArrayList<String>();
        int nodes = g.getNodes();
        NodeAccess na = g.getNodeAccess();
        try {
            EdgeExplorer explorer = g.createEdgeExplorer();
            for (nodeIndex = 0; nodeIndex < nodes; ++nodeIndex) {
                double lon;
                double lat = na.getLatitude(nodeIndex);
                if (lat > 90.0 || lat < -90.0) {
                    problems.add("latitude is not within its bounds " + lat);
                }
                if ((lon = na.getLongitude(nodeIndex)) > 180.0 || lon < -180.0) {
                    problems.add("longitude is not within its bounds " + lon);
                }
                EdgeIterator iter = explorer.setBaseNode(nodeIndex);
                while (iter.next()) {
                    if (iter.getAdjNode() >= nodes) {
                        problems.add("edge of " + nodeIndex + " has a node " + iter.getAdjNode() + " greater or equal to getNodes");
                    }
                    if (iter.getAdjNode() >= 0) continue;
                    problems.add("edge of " + nodeIndex + " has a negative node " + iter.getAdjNode());
                }
            }
        }
        catch (Exception ex) {
            throw new RuntimeException("problem with node " + nodeIndex, ex);
        }
        return problems;
    }

    public static int count(EdgeIterator iter) {
        int counter = 0;
        while (iter.next()) {
            ++counter;
        }
        return counter;
    }

    public static /* varargs */ Set<Integer> asSet(int ... values) {
        HashSet<Integer> s = new HashSet<Integer>();
        for (int v : values) {
            s.add(v);
        }
        return s;
    }

    public static Set<Integer> getNeighbors(EdgeIterator iter) {
        LinkedHashSet<Integer> list = new LinkedHashSet<Integer>();
        while (iter.next()) {
            list.add(iter.getAdjNode());
        }
        return list;
    }

    public static List<Integer> getEdgeIds(EdgeIterator iter) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        while (iter.next()) {
            list.add(iter.getEdge());
        }
        return list;
    }

    public static void printEdgeInfo(Graph g, FlagEncoder encoder) {
        System.out.println("-- Graph n:" + g.getNodes() + " e:" + g.getAllEdges().getMaxId() + " ---");
        AllEdgesIterator iter = g.getAllEdges();
        while (iter.next()) {
            String sc = "";
            if (iter instanceof AllCHEdgesIterator) {
                AllCHEdgesIterator aeSkip = (AllCHEdgesIterator)iter;
                sc = aeSkip.isShortcut() ? "sc" : "  ";
            }
            String fwdStr = iter.isForward(encoder) ? "fwd" : "   ";
            String bckStr = iter.isBackward(encoder) ? "bckwd" : "";
            System.out.println(sc + " " + iter + " " + fwdStr + " " + bckStr);
        }
    }

    public static void printInfo(final Graph g, int startNode, final int counts, final EdgeFilter filter) {
        new BreadthFirstSearch(){
            int counter;

            @Override
            protected boolean goFurther(int nodeId) {
                System.out.println(GHUtility.getNodeInfo(g, nodeId, filter));
                if (this.counter++ > counts) {
                    return false;
                }
                return true;
            }
        }.start(g.createEdgeExplorer(), startNode);
    }

    public static String getNodeInfo(CHGraph g, int nodeId, EdgeFilter filter) {
        CHEdgeExplorer ex = g.createEdgeExplorer(filter);
        CHEdgeIterator iter = ex.setBaseNode(nodeId);
        NodeAccess na = g.getNodeAccess();
        String str = "" + nodeId + ":" + na.getLatitude(nodeId) + "," + na.getLongitude(nodeId) + "\n";
        while (iter.next()) {
            str = str + "  ->" + iter.getAdjNode() + "(" + iter.getSkippedEdge1() + "," + iter.getSkippedEdge2() + ") " + iter.getEdge() + " \t" + BitUtil.BIG.toBitString(iter.getFlags(), 8) + "\n";
        }
        return str;
    }

    public static String getNodeInfo(Graph g, int nodeId, EdgeFilter filter) {
        EdgeIterator iter = g.createEdgeExplorer(filter).setBaseNode(nodeId);
        NodeAccess na = g.getNodeAccess();
        String str = "" + nodeId + ":" + na.getLatitude(nodeId) + "," + na.getLongitude(nodeId) + "\n";
        while (iter.next()) {
            str = str + "  ->" + iter.getAdjNode() + " (" + iter.getDistance() + ") pillars:" + iter.fetchWayGeometry(0).getSize() + ", edgeId:" + iter.getEdge() + "\t" + BitUtil.BIG.toBitString(iter.getFlags(), 8) + "\n";
        }
        return str;
    }

    public static Graph shuffle(Graph g, Graph sortedGraph) {
        int len = g.getNodes();
        TIntArrayList list = new TIntArrayList(len, -1);
        list.fill(0, len, -1);
        for (int i = 0; i < len; ++i) {
            list.set(i, i);
        }
        list.shuffle(new Random());
        return GHUtility.createSortedGraph(g, sortedGraph, list);
    }

    public static Graph sortDFS(Graph g, Graph sortedGraph) {
        final TIntArrayList list = new TIntArrayList(g.getNodes(), -1);
        int nodes = g.getNodes();
        list.fill(0, nodes, -1);
        final GHBitSetImpl bitset = new GHBitSetImpl(nodes);
        final AtomicInteger ref = new AtomicInteger(-1);
        EdgeExplorer explorer = g.createEdgeExplorer();
        int startNode = 0;
        while (startNode >= 0 && startNode < nodes) {
            new DepthFirstSearch(){

                @Override
                protected GHBitSet createBitSet() {
                    return bitset;
                }

                @Override
                protected boolean goFurther(int nodeId) {
                    list.set(nodeId, ref.incrementAndGet());
                    return super.goFurther(nodeId);
                }
            }.start(explorer, startNode);
            startNode = bitset.nextClear(startNode + 1);
        }
        return GHUtility.createSortedGraph(g, sortedGraph, list);
    }

    static Graph createSortedGraph(Graph fromGraph, Graph toSortedGraph, TIntList oldToNewNodeList) {
        AllEdgesIterator eIter = fromGraph.getAllEdges();
        while (eIter.next()) {
            int base = eIter.getBaseNode();
            int newBaseIndex = oldToNewNodeList.get(base);
            int adj = eIter.getAdjNode();
            int newAdjIndex = oldToNewNodeList.get(adj);
            if (newBaseIndex < 0 || newAdjIndex < 0) continue;
            eIter.copyPropertiesTo(toSortedGraph.edge(newBaseIndex, newAdjIndex));
        }
        int nodes = fromGraph.getNodes();
        NodeAccess na = fromGraph.getNodeAccess();
        NodeAccess sna = toSortedGraph.getNodeAccess();
        for (int old = 0; old < nodes; ++old) {
            int newIndex = oldToNewNodeList.get(old);
            if (sna.is3D()) {
                sna.setNode(newIndex, na.getLatitude(old), na.getLongitude(old), na.getElevation(old));
                continue;
            }
            sna.setNode(newIndex, na.getLatitude(old), na.getLongitude(old));
        }
        return toSortedGraph;
    }

    public static Graph copyTo(Graph fromGraph, Graph toGraph) {
        AllEdgesIterator eIter = fromGraph.getAllEdges();
        while (eIter.next()) {
            int base = eIter.getBaseNode();
            int adj = eIter.getAdjNode();
            eIter.copyPropertiesTo(toGraph.edge(base, adj));
        }
        NodeAccess fna = fromGraph.getNodeAccess();
        NodeAccess tna = toGraph.getNodeAccess();
        int nodes = fromGraph.getNodes();
        for (int node = 0; node < nodes; ++node) {
            if (tna.is3D()) {
                tna.setNode(node, fna.getLatitude(node), fna.getLongitude(node), fna.getElevation(node));
                continue;
            }
            tna.setNode(node, fna.getLatitude(node), fna.getLongitude(node));
        }
        return toGraph;
    }

    static Directory guessDirectory(GraphStorage store) {
        String location = store.getDirectory().getLocation();
        if (store.getDirectory() instanceof MMapDirectory) {
            throw new IllegalStateException("not supported yet: mmap will overwrite existing storage at the same location");
        }
        boolean isStoring = ((GHDirectory)store.getDirectory()).isStoring();
        RAMDirectory outdir = new RAMDirectory(location, isStoring);
        return outdir;
    }

    public static GraphHopperStorage newStorage(GraphHopperStorage store) {
        Directory outdir = GHUtility.guessDirectory(store);
        boolean is3D = store.getNodeAccess().is3D();
        return new GraphHopperStorage(store.getCHWeightings(), outdir, store.getEncodingManager(), is3D, store.getExtension()).create(store.getNodes());
    }

    public static int getAdjNode(Graph g, int edge, int adjNode) {
        if (EdgeIterator.Edge.isValid(edge)) {
            EdgeIteratorState iterTo = g.getEdgeIteratorState(edge, adjNode);
            return iterTo.getAdjNode();
        }
        return adjNode;
    }

    public static EdgeIteratorState getEdge(Graph graph, int base, int adj) {
        EdgeIterator iter = graph.createEdgeExplorer().setBaseNode(base);
        while (iter.next()) {
            if (iter.getAdjNode() != adj) continue;
            return iter;
        }
        return null;
    }

    public static int createEdgeKey(int nodeA, int nodeB, int edgeId, boolean reverse) {
        edgeId <<= 1;
        if (reverse) {
            return nodeA > nodeB ? edgeId : edgeId + 1;
        }
        return nodeA > nodeB ? edgeId + 1 : edgeId;
    }

    public static boolean isSameEdgeKeys(int edgeKey1, int edgeKey2) {
        return edgeKey1 / 2 == edgeKey2 / 2;
    }

    public static int reverseEdgeKey(int edgeKey) {
        return edgeKey % 2 == 0 ? edgeKey + 1 : edgeKey - 1;
    }

    public static class DisabledEdgeIterator
    implements CHEdgeIterator {
        @Override
        public EdgeIterator detach(boolean reverse) {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }

        @Override
        public EdgeIteratorState setDistance(double dist) {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }

        @Override
        public EdgeIteratorState setFlags(long flags) {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }

        @Override
        public boolean next() {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }

        @Override
        public int getEdge() {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }

        @Override
        public int getBaseNode() {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }

        @Override
        public int getAdjNode() {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }

        @Override
        public double getDistance() {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }

        @Override
        public long getFlags() {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }

        @Override
        public PointList fetchWayGeometry(int type) {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }

        @Override
        public EdgeIteratorState setWayGeometry(PointList list) {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }

        @Override
        public String getName() {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }

        @Override
        public EdgeIteratorState setName(String name) {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }

        @Override
        public boolean getBoolean(int key, boolean reverse, boolean _default) {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }

        @Override
        public boolean isBackward(FlagEncoder encoder) {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }

        @Override
        public boolean isForward(FlagEncoder encoder) {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }

        @Override
        public int getAdditionalField() {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }

        @Override
        public EdgeIteratorState setAdditionalField(int value) {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }

        @Override
        public EdgeIteratorState copyPropertiesTo(EdgeIteratorState edge) {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }

        @Override
        public boolean isShortcut() {
            return false;
        }

        @Override
        public int getSkippedEdge1() {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }

        @Override
        public int getSkippedEdge2() {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }

        @Override
        public void setSkippedEdges(int edge1, int edge2) {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }

        @Override
        public double getWeight() {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }

        @Override
        public CHEdgeIteratorState setWeight(double weight) {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }

        @Override
        public boolean canBeOverwritten(long flags) {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }
    }

}

