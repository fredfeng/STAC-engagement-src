/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing;

import com.graphhopper.routing.util.DefaultEdgeFilter;
import com.graphhopper.routing.util.EdgeFilter;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.storage.EdgeEntry;
import com.graphhopper.storage.Graph;
import com.graphhopper.storage.NodeAccess;
import com.graphhopper.util.AngleCalc;
import com.graphhopper.util.EdgeExplorer;
import com.graphhopper.util.EdgeIterator;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.FinishInstruction;
import com.graphhopper.util.Instruction;
import com.graphhopper.util.InstructionAnnotation;
import com.graphhopper.util.InstructionList;
import com.graphhopper.util.PointAccess;
import com.graphhopper.util.PointList;
import com.graphhopper.util.RoundaboutInstruction;
import com.graphhopper.util.StopWatch;
import com.graphhopper.util.Translation;
import gnu.trove.TIntCollection;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import java.util.ArrayList;
import java.util.List;

public class Path {
    private static final AngleCalc ac = new AngleCalc();
    protected Graph graph;
    private FlagEncoder encoder;
    protected double distance;
    protected boolean reverseOrder = true;
    protected long time;
    private boolean found;
    protected EdgeEntry edgeEntry;
    final StopWatch extractSW = new StopWatch("extract");
    private int fromNode = -1;
    protected int endNode = -1;
    private TIntList edgeIds;
    private double weight = Double.MAX_VALUE;
    private NodeAccess nodeAccess;

    public Path(Graph graph, FlagEncoder encoder) {
        this.graph = graph;
        this.nodeAccess = graph.getNodeAccess();
        this.encoder = encoder;
        this.edgeIds = new TIntArrayList();
    }

    Path(Path p) {
        this(p.graph, p.encoder);
        this.weight = p.weight;
        this.edgeIds = new TIntArrayList(p.edgeIds);
        this.edgeEntry = p.edgeEntry;
    }

    public Path setEdgeEntry(EdgeEntry edgeEntry) {
        this.edgeEntry = edgeEntry;
        return this;
    }

    protected void addEdge(int edge) {
        this.edgeIds.add(edge);
    }

    protected Path setEndNode(int end) {
        this.endNode = end;
        return this;
    }

    protected Path setFromNode(int from) {
        this.fromNode = from;
        return this;
    }

    private int getFromNode() {
        if (this.fromNode < 0) {
            throw new IllegalStateException("Call extract() before retrieving fromNode");
        }
        return this.fromNode;
    }

    public boolean isFound() {
        return this.found;
    }

    public Path setFound(boolean found) {
        this.found = found;
        return this;
    }

    void reverseOrder() {
        if (!this.reverseOrder) {
            throw new IllegalStateException("Switching order multiple times is not supported");
        }
        this.reverseOrder = false;
        this.edgeIds.reverse();
    }

    public double getDistance() {
        return this.distance;
    }

    public long getMillis() {
        return this.time;
    }

    public long getTime() {
        return this.time;
    }

    public double getWeight() {
        return this.weight;
    }

    public Path setWeight(double w) {
        this.weight = w;
        return this;
    }

    public Path extract() {
        if (this.isFound()) {
            throw new IllegalStateException("Extract can only be called once");
        }
        this.extractSW.start();
        EdgeEntry goalEdge = this.edgeEntry;
        this.setEndNode(goalEdge.adjNode);
        while (EdgeIterator.Edge.isValid(goalEdge.edge)) {
            this.processEdge(goalEdge.edge, goalEdge.adjNode);
            goalEdge = goalEdge.parent;
        }
        this.setFromNode(goalEdge.adjNode);
        this.reverseOrder();
        this.extractSW.stop();
        return this.setFound(true);
    }

    public EdgeIteratorState getFinalEdge() {
        return this.graph.getEdgeIteratorState(this.edgeIds.get(this.edgeIds.size() - 1), this.endNode);
    }

    public long getExtractTime() {
        return this.extractSW.getNanos();
    }

    public String getDebugInfo() {
        return this.extractSW.toString();
    }

    protected void processEdge(int edgeId, int adjNode) {
        EdgeIteratorState iter = this.graph.getEdgeIteratorState(edgeId, adjNode);
        double dist = iter.getDistance();
        this.distance += dist;
        this.time += this.calcMillis(dist, iter.getFlags(), false);
        this.addEdge(edgeId);
    }

    protected long calcMillis(double distance, long flags, boolean revert) {
        double speed;
        if (revert && !this.encoder.isBackward(flags) || !revert && !this.encoder.isForward(flags)) {
            throw new IllegalStateException("Calculating time should not require to read speed from edge in wrong direction. Reverse:" + revert + ", fwd:" + this.encoder.isForward(flags) + ", bwd:" + this.encoder.isBackward(flags));
        }
        double d = speed = revert ? this.encoder.getReverseSpeed(flags) : this.encoder.getSpeed(flags);
        if (Double.isInfinite(speed) || Double.isNaN(speed) || speed < 0.0) {
            throw new IllegalStateException("Invalid speed stored in edge! " + speed);
        }
        if (speed == 0.0) {
            throw new IllegalStateException("Speed cannot be 0 for unblocked edge, use access properties to mark edge blocked! Should only occur for shortest path calculation. See #242.");
        }
        return (long)(distance * 3600.0 / speed);
    }

    private void forEveryEdge(EdgeVisitor visitor) {
        int tmpNode = this.getFromNode();
        int len = this.edgeIds.size();
        for (int i = 0; i < len; ++i) {
            EdgeIteratorState edgeBase = this.graph.getEdgeIteratorState(this.edgeIds.get(i), tmpNode);
            if (edgeBase == null) {
                throw new IllegalStateException("Edge " + this.edgeIds.get(i) + " was empty when requested with node " + tmpNode + ", array index:" + i + ", edges:" + this.edgeIds.size());
            }
            tmpNode = edgeBase.getBaseNode();
            edgeBase = this.graph.getEdgeIteratorState(edgeBase.getEdge(), tmpNode);
            visitor.next(edgeBase, i);
        }
    }

    public List<EdgeIteratorState> calcEdges() {
        final ArrayList<EdgeIteratorState> edges = new ArrayList<EdgeIteratorState>(this.edgeIds.size());
        if (this.edgeIds.isEmpty()) {
            return edges;
        }
        this.forEveryEdge(new EdgeVisitor(){

            @Override
            public void next(EdgeIteratorState eb, int i) {
                edges.add(eb);
            }
        });
        return edges;
    }

    public TIntList calcNodes() {
        final TIntArrayList nodes = new TIntArrayList(this.edgeIds.size() + 1);
        if (this.edgeIds.isEmpty()) {
            if (this.isFound()) {
                nodes.add(this.endNode);
            }
            return nodes;
        }
        int tmpNode = this.getFromNode();
        nodes.add(tmpNode);
        this.forEveryEdge(new EdgeVisitor(){

            @Override
            public void next(EdgeIteratorState eb, int i) {
                nodes.add(eb.getAdjNode());
            }
        });
        return nodes;
    }

    public PointList calcPoints() {
        final PointList points = new PointList(this.edgeIds.size() + 1, this.nodeAccess.is3D());
        if (this.edgeIds.isEmpty()) {
            if (this.isFound()) {
                points.add(this.graph.getNodeAccess(), this.endNode);
            }
            return points;
        }
        int tmpNode = this.getFromNode();
        points.add(this.nodeAccess, tmpNode);
        this.forEveryEdge(new EdgeVisitor(){

            @Override
            public void next(EdgeIteratorState eb, int index) {
                PointList pl = eb.fetchWayGeometry(2);
                for (int j = 0; j < pl.getSize(); ++j) {
                    points.add(pl, j);
                }
            }
        });
        return points;
    }

    public InstructionList calcInstructions(final Translation tr) {
        final InstructionList ways = new InstructionList(this.edgeIds.size() / 4, tr);
        if (this.edgeIds.isEmpty()) {
            if (this.isFound()) {
                ways.add(new FinishInstruction(this.nodeAccess, this.endNode));
            }
            return ways;
        }
        final int tmpNode = this.getFromNode();
        this.forEveryEdge(new EdgeVisitor(){
            private double prevLat;
            private double prevLon;
            private double doublePrevLat;
            private double doublePrevLong;
            private int prevNode;
            private double prevOrientation;
            private Instruction prevInstruction;
            private boolean prevInRoundabout;
            private String name;
            private String prevName;
            private InstructionAnnotation annotation;
            private InstructionAnnotation prevAnnotation;
            private EdgeExplorer outEdgeExplorer;

            @Override
            public void next(EdgeIteratorState edge, int index) {
                boolean lastEdge;
                int adjNode;
                double adjLat;
                boolean isRoundabout;
                double adjLon;
                int baseNode;
                PointList wayGeo;
                double latitude;
                double longitude;
                adjNode = edge.getAdjNode();
                baseNode = edge.getBaseNode();
                long flags = edge.getFlags();
                adjLat = Path.this.nodeAccess.getLatitude(adjNode);
                adjLon = Path.this.nodeAccess.getLongitude(adjNode);
                wayGeo = edge.fetchWayGeometry(3);
                isRoundabout = Path.this.encoder.isBool(flags, 2);
                if (wayGeo.getSize() <= 2) {
                    latitude = adjLat;
                    longitude = adjLon;
                } else {
                    latitude = wayGeo.getLatitude(1);
                    longitude = wayGeo.getLongitude(1);
                    assert (Double.compare(this.prevLat, Path.this.nodeAccess.getLatitude(baseNode)) == 0);
                    assert (Double.compare(this.prevLon, Path.this.nodeAccess.getLongitude(baseNode)) == 0);
                }
                this.name = edge.getName();
                this.annotation = Path.this.encoder.getAnnotation(flags, tr);
                if (this.prevName == null && !isRoundabout) {
                    int sign = 0;
                    this.prevInstruction = new Instruction(sign, this.name, this.annotation, new PointList(10, Path.this.nodeAccess.is3D()));
                    ways.add(this.prevInstruction);
                    this.prevName = this.name;
                    this.prevAnnotation = this.annotation;
                } else if (isRoundabout) {
                    if (!this.prevInRoundabout) {
                        int sign = 6;
                        RoundaboutInstruction roundaboutInstruction = new RoundaboutInstruction(sign, this.name, this.annotation, new PointList(10, Path.this.nodeAccess.is3D()));
                        if (this.prevName != null) {
                            EdgeIterator edgeIter = this.outEdgeExplorer.setBaseNode(baseNode);
                            while (edgeIter.next()) {
                                if (edgeIter.getAdjNode() == this.prevNode || Path.this.encoder.isBool(edgeIter.getFlags(), 2)) continue;
                                roundaboutInstruction.increaseExitNumber();
                                break;
                            }
                            this.prevOrientation = ac.calcOrientation(this.doublePrevLat, this.doublePrevLong, this.prevLat, this.prevLon);
                            double orientation = ac.calcOrientation(this.prevLat, this.prevLon, latitude, longitude);
                            orientation = ac.alignOrientation(this.prevOrientation, orientation);
                            double delta = orientation - this.prevOrientation;
                            roundaboutInstruction.setDirOfRotation(delta);
                        } else {
                            this.prevOrientation = ac.calcOrientation(this.prevLat, this.prevLon, latitude, longitude);
                            this.prevName = this.name;
                            this.prevAnnotation = this.annotation;
                        }
                        this.prevInstruction = roundaboutInstruction;
                        ways.add(this.prevInstruction);
                    }
                    EdgeIterator edgeIter = this.outEdgeExplorer.setBaseNode(adjNode);
                    while (edgeIter.next()) {
                        if (Path.this.encoder.isBool(edgeIter.getFlags(), 2)) continue;
                        ((RoundaboutInstruction)this.prevInstruction).increaseExitNumber();
                        break;
                    }
                } else if (this.prevInRoundabout) {
                    this.prevInstruction.setName(this.name);
                    double orientation = ac.calcOrientation(this.prevLat, this.prevLon, latitude, longitude);
                    orientation = ac.alignOrientation(this.prevOrientation, orientation);
                    double deltaInOut = orientation - this.prevOrientation;
                    double recentOrientation = ac.calcOrientation(this.doublePrevLat, this.doublePrevLong, this.prevLat, this.prevLon);
                    orientation = ac.alignOrientation(recentOrientation, orientation);
                    double deltaOut = orientation - recentOrientation;
                    this.prevInstruction = ((RoundaboutInstruction)this.prevInstruction).setRadian(deltaInOut).setDirOfRotation(deltaOut).setExited();
                    this.prevName = this.name;
                    this.prevAnnotation = this.annotation;
                } else if (!this.name.equals(this.prevName) || !this.annotation.equals(this.prevAnnotation)) {
                    this.prevOrientation = ac.calcOrientation(this.doublePrevLat, this.doublePrevLong, this.prevLat, this.prevLon);
                    double orientation = ac.calcOrientation(this.prevLat, this.prevLon, latitude, longitude);
                    orientation = ac.alignOrientation(this.prevOrientation, orientation);
                    double delta = orientation - this.prevOrientation;
                    double absDelta = Math.abs(delta);
                    int sign = absDelta < 0.2 ? 0 : (absDelta < 0.8 ? (delta > 0.0 ? -1 : 1) : (absDelta < 1.8 ? (delta > 0.0 ? -2 : 2) : (delta > 0.0 ? -3 : 3)));
                    this.prevInstruction = new Instruction(sign, this.name, this.annotation, new PointList(10, Path.this.nodeAccess.is3D()));
                    ways.add(this.prevInstruction);
                    this.prevName = this.name;
                    this.prevAnnotation = this.annotation;
                }
                this.updatePointsAndInstruction(edge, wayGeo);
                if (wayGeo.getSize() <= 2) {
                    this.doublePrevLat = this.prevLat;
                    this.doublePrevLong = this.prevLon;
                } else {
                    int beforeLast = wayGeo.getSize() - 2;
                    this.doublePrevLat = wayGeo.getLatitude(beforeLast);
                    this.doublePrevLong = wayGeo.getLongitude(beforeLast);
                }
                this.prevInRoundabout = isRoundabout;
                this.prevNode = baseNode;
                this.prevLat = adjLat;
                this.prevLon = adjLon;
                boolean bl = lastEdge = index == Path.this.edgeIds.size() - 1;
                if (lastEdge) {
                    if (isRoundabout) {
                        double orientation = ac.calcOrientation(this.doublePrevLat, this.doublePrevLong, this.prevLat, this.prevLon);
                        orientation = ac.alignOrientation(this.prevOrientation, orientation);
                        double delta = orientation - this.prevOrientation;
                        ((RoundaboutInstruction)this.prevInstruction).setRadian(delta);
                    }
                    ways.add(new FinishInstruction(Path.this.nodeAccess, adjNode));
                }
            }

            private void updatePointsAndInstruction(EdgeIteratorState edge, PointList pl) {
                int len = pl.size() - 1;
                for (int i = 0; i < len; ++i) {
                    this.prevInstruction.getPoints().add(pl, i);
                }
                double newDist = edge.getDistance();
                this.prevInstruction.setDistance(newDist + this.prevInstruction.getDistance());
                long flags = edge.getFlags();
                this.prevInstruction.setTime(Path.this.calcMillis(newDist, flags, false) + this.prevInstruction.getTime());
            }
        });
        return ways;
    }

    public String toString() {
        return "distance:" + this.getDistance() + ", edges:" + this.edgeIds.size();
    }

    public String toDetailsString() {
        String str = "";
        for (int i = 0; i < this.edgeIds.size(); ++i) {
            if (i > 0) {
                str = str + "->";
            }
            str = str + this.edgeIds.get(i);
        }
        return this.toString() + ", found:" + this.isFound() + ", " + str;
    }

    private static interface EdgeVisitor {
        public void next(EdgeIteratorState var1, int var2);
    }

}

