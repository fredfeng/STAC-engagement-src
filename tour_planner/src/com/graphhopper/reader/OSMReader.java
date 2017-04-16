/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.reader;

import com.graphhopper.coll.GHLongIntBTree;
import com.graphhopper.coll.LongIntMap;
import com.graphhopper.reader.DataReader;
import com.graphhopper.reader.OSMElement;
import com.graphhopper.reader.OSMInputFile;
import com.graphhopper.reader.OSMNode;
import com.graphhopper.reader.OSMRelation;
import com.graphhopper.reader.OSMTurnRelation;
import com.graphhopper.reader.OSMWay;
import com.graphhopper.reader.PillarInfo;
import com.graphhopper.reader.dem.ElevationProvider;
import com.graphhopper.routing.util.DefaultEdgeFilter;
import com.graphhopper.routing.util.EdgeFilter;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.routing.util.TurnCostEncoder;
import com.graphhopper.routing.util.TurnWeighting;
import com.graphhopper.storage.Directory;
import com.graphhopper.storage.Graph;
import com.graphhopper.storage.GraphExtension;
import com.graphhopper.storage.GraphHopperStorage;
import com.graphhopper.storage.GraphStorage;
import com.graphhopper.storage.NodeAccess;
import com.graphhopper.storage.TurnCostExtension;
import com.graphhopper.util.DistanceCalc;
import com.graphhopper.util.DistanceCalc3D;
import com.graphhopper.util.DouglasPeucker;
import com.graphhopper.util.EdgeExplorer;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.Helper;
import com.graphhopper.util.PointAccess;
import com.graphhopper.util.PointList;
import com.graphhopper.util.StopWatch;
import com.graphhopper.util.shapes.GHPoint;
import gnu.trove.list.TLongList;
import gnu.trove.list.array.TLongArrayList;
import gnu.trove.map.TIntLongMap;
import gnu.trove.map.TLongLongMap;
import gnu.trove.map.hash.TIntLongHashMap;
import gnu.trove.map.hash.TLongLongHashMap;
import gnu.trove.map.hash.TLongObjectHashMap;
import gnu.trove.set.TLongSet;
import gnu.trove.set.hash.TLongHashSet;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OSMReader
implements DataReader {
    protected static final int EMPTY = -1;
    protected static final int PILLAR_NODE = 1;
    protected static final int TOWER_NODE = -2;
    private static final Logger logger = LoggerFactory.getLogger(OSMReader.class);
    private long locations;
    private long skippedLocations;
    private final GraphStorage ghStorage;
    private final Graph graph;
    private final NodeAccess nodeAccess;
    private EncodingManager encodingManager = null;
    private int workerThreads = -1;
    protected long zeroCounter = 0;
    private LongIntMap osmNodeIdToInternalNodeMap;
    private TLongLongHashMap osmNodeIdToNodeFlagsMap;
    private TLongLongHashMap osmWayIdToRouteWeightMap;
    private TLongHashSet osmWayIdSet = new TLongHashSet();
    private TIntLongMap edgeIdToOsmWayIdMap;
    private final TLongList barrierNodeIds = new TLongArrayList();
    protected PillarInfo pillarInfo;
    private final DistanceCalc distCalc = Helper.DIST_EARTH;
    private final DistanceCalc3D distCalc3D = Helper.DIST_3D;
    private final DouglasPeucker simplifyAlgo = new DouglasPeucker();
    private boolean doSimplify = true;
    private int nextTowerId = 0;
    private int nextPillarId = 0;
    private long newUniqueOsmId = -9223372036854775807L;
    private ElevationProvider eleProvider = ElevationProvider.NOOP;
    private final boolean exitOnlyPillarNodeException = true;
    private File osmFile;
    private final Map<FlagEncoder, EdgeExplorer> outExplorerMap = new HashMap<FlagEncoder, EdgeExplorer>();
    private final Map<FlagEncoder, EdgeExplorer> inExplorerMap = new HashMap<FlagEncoder, EdgeExplorer>();

    public OSMReader(GraphHopperStorage ghStorage) {
        this.ghStorage = ghStorage;
        this.graph = ghStorage;
        this.nodeAccess = this.graph.getNodeAccess();
        this.osmNodeIdToInternalNodeMap = new GHLongIntBTree(200);
        this.osmNodeIdToNodeFlagsMap = new TLongLongHashMap(200, 0.5f, 0, 0);
        this.osmWayIdToRouteWeightMap = new TLongLongHashMap(200, 0.5f, 0, 0);
        this.pillarInfo = new PillarInfo(this.nodeAccess.is3D(), ghStorage.getDirectory());
    }

    @Override
    public void readGraph() throws IOException {
        if (this.encodingManager == null) {
            throw new IllegalStateException("Encoding manager was not set.");
        }
        if (this.osmFile == null) {
            throw new IllegalStateException("No OSM file specified");
        }
        if (!this.osmFile.exists()) {
            throw new IllegalStateException("Your specified OSM file does not exist:" + this.osmFile.getAbsolutePath());
        }
        StopWatch sw1 = new StopWatch().start();
        this.preProcess(this.osmFile);
        sw1.stop();
        StopWatch sw2 = new StopWatch().start();
        this.writeOsm2Graph(this.osmFile);
        sw2.stop();
        logger.info("time(pass1): " + (int)sw1.getSeconds() + " pass2: " + (int)sw2.getSeconds() + " total:" + (int)(sw1.getSeconds() + sw2.getSeconds()));
    }

    void preProcess(File osmFile) {
        OSMInputFile in = null;
        try {
            OSMElement item;
            in = new OSMInputFile(osmFile).setWorkerThreads(this.workerThreads).open();
            long tmpWayCounter = 1;
            long tmpRelationCounter = 1;
            while ((item = in.getNext()) != null) {
                boolean valid;
                OSMWay way;
                if (item.isType(1) && (valid = this.filterWay(way = (OSMWay)item))) {
                    TLongList wayNodes = way.getNodes();
                    int s = wayNodes.size();
                    for (int index = 0; index < s; ++index) {
                        this.prepareHighwayNode(wayNodes.get(index));
                    }
                    if (++tmpWayCounter % 5000000 == 0) {
                        logger.info(Helper.nf(tmpWayCounter) + " (preprocess), osmIdMap:" + Helper.nf(this.getNodeMap().getSize()) + " (" + this.getNodeMap().getMemoryUsage() + "MB) " + Helper.getMemInfo());
                    }
                }
                if (!item.isType(2)) continue;
                OSMRelation relation = (OSMRelation)item;
                if (!relation.isMetaRelation() && relation.hasTag("type", (Object)"route")) {
                    this.prepareWaysWithRelationInfo(relation);
                }
                if (relation.hasTag("type", (Object)"restriction")) {
                    this.prepareRestrictionRelation(relation);
                }
                if (++tmpRelationCounter % 50000 != 0) continue;
                logger.info(Helper.nf(tmpRelationCounter) + " (preprocess), osmWayMap:" + Helper.nf(this.getRelFlagsMap().size()) + " " + Helper.getMemInfo());
            }
        }
        catch (Exception ex) {
            throw new RuntimeException("Problem while parsing file", ex);
        }
        finally {
            Helper.close(in);
        }
    }

    private void prepareRestrictionRelation(OSMRelation relation) {
        OSMTurnRelation turnRelation = this.createTurnRelation(relation);
        if (turnRelation != null) {
            this.getOsmWayIdSet().add(turnRelation.getOsmIdFrom());
            this.getOsmWayIdSet().add(turnRelation.getOsmIdTo());
        }
    }

    private TLongSet getOsmWayIdSet() {
        return this.osmWayIdSet;
    }

    private TIntLongMap getEdgeIdToOsmWayIdMap() {
        if (this.edgeIdToOsmWayIdMap == null) {
            this.edgeIdToOsmWayIdMap = new TIntLongHashMap(this.getOsmWayIdSet().size(), 0.5f, -1, -1);
        }
        return this.edgeIdToOsmWayIdMap;
    }

    boolean filterWay(OSMWay item) {
        if (item.getNodes().size() < 2) {
            return false;
        }
        if (!item.hasTags()) {
            return false;
        }
        return this.encodingManager.acceptWay(item) > 0;
    }

    private void writeOsm2Graph(File osmFile) {
        long counter;
        int tmp = (int)Math.max(this.getNodeMap().getSize() / 50, 100);
        logger.info("creating graph. Found nodes (pillar+tower):" + Helper.nf(this.getNodeMap().getSize()) + ", " + Helper.getMemInfo());
        this.ghStorage.create(tmp);
        long wayStart = -1;
        long relationStart = -1;
        counter = 1;
        OSMInputFile in = null;
        try {
            OSMElement item;
            in = new OSMInputFile(osmFile).setWorkerThreads(this.workerThreads).open();
            LongIntMap nodeFilter = this.getNodeMap();
            while ((item = in.getNext()) != null) {
                switch (item.getType()) {
                    case 0: {
                        if (nodeFilter.get(item.getId()) == -1) break;
                        this.processNode((OSMNode)item);
                        break;
                    }
                    case 1: {
                        if (wayStart < 0) {
                            logger.info(Helper.nf(counter) + ", now parsing ways");
                            wayStart = counter;
                        }
                        this.processWay((OSMWay)item);
                        break;
                    }
                    case 2: {
                        if (relationStart < 0) {
                            logger.info(Helper.nf(counter) + ", now parsing relations");
                            relationStart = counter;
                        }
                        this.processRelation((OSMRelation)item);
                    }
                }
                if (++counter % 100000000 != 0) continue;
                logger.info(Helper.nf(counter) + ", locs:" + Helper.nf(this.locations) + " (" + this.skippedLocations + ") " + Helper.getMemInfo());
            }
        }
        catch (Exception ex) {
            throw new RuntimeException("Couldn't process file " + osmFile + ", error: " + ex.getMessage(), ex);
        }
        finally {
            Helper.close(in);
        }
        this.finishedReading();
        if (this.graph.getNodes() == 0) {
            throw new IllegalStateException("osm must not be empty. read " + counter + " lines and " + this.locations + " locations");
        }
    }

    void processWay(OSMWay way) {
        long wayFlags;
        if (way.getNodes().size() < 2) {
            return;
        }
        if (!way.hasTags()) {
            return;
        }
        long wayOsmId = way.getId();
        long includeWay = this.encodingManager.acceptWay(way);
        if (includeWay == 0) {
            return;
        }
        long relationFlags = this.getRelFlagsMap().get(way.getId());
        TLongList osmNodeIds = way.getNodes();
        if (osmNodeIds.size() > 1) {
            int first = this.getNodeMap().get(osmNodeIds.get(0));
            int last = this.getNodeMap().get(osmNodeIds.get(osmNodeIds.size() - 1));
            double firstLat = this.getTmpLatitude(first);
            double firstLon = this.getTmpLongitude(first);
            double lastLat = this.getTmpLatitude(last);
            double lastLon = this.getTmpLongitude(last);
            if (!(Double.isNaN(firstLat) || Double.isNaN(firstLon) || Double.isNaN(lastLat) || Double.isNaN(lastLon))) {
                double estimatedDist = this.distCalc.calcDist(firstLat, firstLon, lastLat, lastLon);
                way.setTag("estimated_distance", estimatedDist);
                way.setTag("estimated_center", new GHPoint((firstLat + lastLat) / 2.0, (firstLon + lastLon) / 2.0));
            }
        }
        if ((wayFlags = this.encodingManager.handleWayTags(way, includeWay, relationFlags)) == 0) {
            return;
        }
        ArrayList<EdgeIteratorState> createdEdges = new ArrayList<EdgeIteratorState>();
        int size = osmNodeIds.size();
        int lastBarrier = -1;
        for (int i = 0; i < size; ++i) {
            long nodeId = osmNodeIds.get(i);
            long nodeFlags = this.getNodeFlagsMap().get(nodeId);
            if (nodeFlags <= 0 || (nodeFlags & wayFlags) <= 0) continue;
            this.getNodeFlagsMap().put(nodeId, 0);
            long newNodeId = this.addBarrierNode(nodeId);
            if (i > 0) {
                if (lastBarrier < 0) {
                    lastBarrier = 0;
                }
                long[] transfer = osmNodeIds.toArray(lastBarrier, i - lastBarrier + 1);
                transfer[transfer.length - 1] = newNodeId;
                TLongArrayList partIds = new TLongArrayList(transfer);
                createdEdges.addAll(this.addOSMWay(partIds, wayFlags, wayOsmId));
                createdEdges.addAll(this.addBarrierEdge(newNodeId, nodeId, wayFlags, nodeFlags, wayOsmId));
            } else {
                createdEdges.addAll(this.addBarrierEdge(nodeId, newNodeId, wayFlags, nodeFlags, wayOsmId));
                osmNodeIds.set(0, newNodeId);
            }
            lastBarrier = i;
        }
        if (lastBarrier >= 0) {
            if (lastBarrier < size - 1) {
                long[] transfer = osmNodeIds.toArray(lastBarrier, size - lastBarrier);
                TLongArrayList partNodeIds = new TLongArrayList(transfer);
                createdEdges.addAll(this.addOSMWay(partNodeIds, wayFlags, wayOsmId));
            }
        } else {
            createdEdges.addAll(this.addOSMWay(way.getNodes(), wayFlags, wayOsmId));
        }
        for (EdgeIteratorState edge : createdEdges) {
            this.encodingManager.applyWayTags(way, edge);
        }
    }

    public void processRelation(OSMRelation relation) throws XMLStreamException {
        OSMTurnRelation turnRelation;
        GraphExtension extendedStorage;
        if (relation.hasTag("type", (Object)"restriction") && (turnRelation = this.createTurnRelation(relation)) != null && (extendedStorage = this.graph.getExtension()) instanceof TurnCostExtension) {
            TurnCostExtension tcs = (TurnCostExtension)extendedStorage;
            Collection<OSMTurnRelation.TurnCostTableEntry> entries = this.analyzeTurnRelation(turnRelation);
            for (OSMTurnRelation.TurnCostTableEntry entry : entries) {
                tcs.addTurnInfo(entry.edgeFrom, entry.nodeVia, entry.edgeTo, entry.flags);
            }
        }
    }

    public Collection<OSMTurnRelation.TurnCostTableEntry> analyzeTurnRelation(OSMTurnRelation turnRelation) {
        TLongObjectHashMap<OSMTurnRelation.TurnCostTableEntry> entries = new TLongObjectHashMap<OSMTurnRelation.TurnCostTableEntry>();
        for (FlagEncoder encoder : this.encodingManager.fetchEdgeEncoders()) {
            for (OSMTurnRelation.TurnCostTableEntry entry : this.analyzeTurnRelation(encoder, turnRelation)) {
                OSMTurnRelation.TurnCostTableEntry oldEntry = (OSMTurnRelation.TurnCostTableEntry)entries.get(entry.getItemId());
                if (oldEntry != null) {
                    oldEntry.flags |= entry.flags;
                    continue;
                }
                entries.put(entry.getItemId(), entry);
            }
        }
        return entries.valueCollection();
    }

    public Collection<OSMTurnRelation.TurnCostTableEntry> analyzeTurnRelation(FlagEncoder encoder, OSMTurnRelation turnRelation) {
        if (!encoder.supports(TurnWeighting.class)) {
            return Collections.emptyList();
        }
        EdgeExplorer edgeOutExplorer = this.outExplorerMap.get(encoder);
        EdgeExplorer edgeInExplorer = this.inExplorerMap.get(encoder);
        if (edgeOutExplorer == null || edgeInExplorer == null) {
            edgeOutExplorer = this.graph.createEdgeExplorer(new DefaultEdgeFilter(encoder, false, true));
            this.outExplorerMap.put(encoder, edgeOutExplorer);
            edgeInExplorer = this.graph.createEdgeExplorer(new DefaultEdgeFilter(encoder, true, false));
            this.inExplorerMap.put(encoder, edgeInExplorer);
        }
        return turnRelation.getRestrictionAsEntries(encoder, edgeOutExplorer, edgeInExplorer, this);
    }

    public long getOsmIdOfInternalEdge(int edgeId) {
        return this.getEdgeIdToOsmWayIdMap().get(edgeId);
    }

    public int getInternalNodeIdOfOsmNode(long nodeOsmId) {
        int id = this.getNodeMap().get(nodeOsmId);
        if (id < -2) {
            return - id - 3;
        }
        return -1;
    }

    double getTmpLatitude(int id) {
        if (id == -1) {
            return Double.NaN;
        }
        if (id < -2) {
            id = - id - 3;
            return this.nodeAccess.getLatitude(id);
        }
        if (id > 2) {
            return this.pillarInfo.getLatitude(id -= 3);
        }
        return Double.NaN;
    }

    double getTmpLongitude(int id) {
        if (id == -1) {
            return Double.NaN;
        }
        if (id < -2) {
            id = - id - 3;
            return this.nodeAccess.getLongitude(id);
        }
        if (id > 2) {
            return this.pillarInfo.getLon(id -= 3);
        }
        return Double.NaN;
    }

    private void processNode(OSMNode node) {
        if (this.isInBounds(node)) {
            long nodeFlags;
            this.addNode(node);
            if (node.hasTags() && (nodeFlags = this.encodingManager.handleNodeTags(node)) != 0) {
                this.getNodeFlagsMap().put(node.getId(), nodeFlags);
            }
            ++this.locations;
        } else {
            ++this.skippedLocations;
        }
    }

    boolean addNode(OSMNode node) {
        int nodeType = this.getNodeMap().get(node.getId());
        if (nodeType == -1) {
            return false;
        }
        double lat = node.getLat();
        double lon = node.getLon();
        double ele = this.getElevation(node);
        if (nodeType == -2) {
            this.addTowerNode(node.getId(), lat, lon, ele);
        } else if (nodeType == 1) {
            this.pillarInfo.setNode(this.nextPillarId, lat, lon, ele);
            this.getNodeMap().put(node.getId(), this.nextPillarId + 3);
            ++this.nextPillarId;
        }
        return true;
    }

    protected double getElevation(OSMNode node) {
        return this.eleProvider.getEle(node.getLat(), node.getLon());
    }

    void prepareWaysWithRelationInfo(OSMRelation osmRelation) {
        if (this.encodingManager.handleRelationTags(osmRelation, 0) == 0) {
            return;
        }
        int size = osmRelation.getMembers().size();
        for (int index = 0; index < size; ++index) {
            long newRelationFlags;
            OSMRelation.Member member = osmRelation.getMembers().get(index);
            if (member.type() != 1) continue;
            long osmId = member.ref();
            long oldRelationFlags = this.getRelFlagsMap().get(osmId);
            if (oldRelationFlags == (newRelationFlags = this.encodingManager.handleRelationTags(osmRelation, oldRelationFlags))) continue;
            this.getRelFlagsMap().put(osmId, newRelationFlags);
        }
    }

    void prepareHighwayNode(long osmId) {
        int tmpIndex = this.getNodeMap().get(osmId);
        if (tmpIndex == -1) {
            this.getNodeMap().put(osmId, 1);
        } else if (tmpIndex > -1) {
            this.getNodeMap().put(osmId, -2);
        }
    }

    int addTowerNode(long osmId, double lat, double lon, double ele) {
        if (this.nodeAccess.is3D()) {
            this.nodeAccess.setNode(this.nextTowerId, lat, lon, ele);
        } else {
            this.nodeAccess.setNode(this.nextTowerId, lat, lon);
        }
        int id = - this.nextTowerId + 3;
        this.getNodeMap().put(osmId, id);
        ++this.nextTowerId;
        return id;
    }

    Collection<EdgeIteratorState> addOSMWay(TLongList osmNodeIds, long flags, long wayOsmId) {
        PointList pointList = new PointList(osmNodeIds.size(), this.nodeAccess.is3D());
        ArrayList<EdgeIteratorState> newEdges = new ArrayList<EdgeIteratorState>(5);
        int firstNode = -1;
        int lastIndex = osmNodeIds.size() - 1;
        int lastInBoundsPillarNode = -1;
        try {
            for (int i = 0; i < osmNodeIds.size(); ++i) {
                long osmId = osmNodeIds.get(i);
                int tmpNode = this.getNodeMap().get(osmId);
                if (tmpNode == -1 || tmpNode == -2) continue;
                if (tmpNode == 1) {
                    if (pointList.isEmpty() || lastInBoundsPillarNode <= 2) continue;
                    tmpNode = lastInBoundsPillarNode;
                    tmpNode = this.handlePillarNode(tmpNode, osmId, null, true);
                    tmpNode = - tmpNode - 3;
                    if (pointList.getSize() > 1 && firstNode >= 0) {
                        newEdges.add(this.addEdge(firstNode, tmpNode, pointList, flags, wayOsmId));
                        pointList.clear();
                        pointList.add(this.nodeAccess, tmpNode);
                    }
                    firstNode = tmpNode;
                    lastInBoundsPillarNode = -1;
                    continue;
                }
                if (tmpNode <= 2 && tmpNode >= -2) {
                    throw new AssertionError((Object)("Mapped index not in correct bounds " + tmpNode + ", " + osmId));
                }
                if (tmpNode > 2) {
                    boolean convertToTowerNode;
                    boolean bl = convertToTowerNode = i == 0 || i == lastIndex;
                    if (!convertToTowerNode) {
                        lastInBoundsPillarNode = tmpNode;
                    }
                    tmpNode = this.handlePillarNode(tmpNode, osmId, pointList, convertToTowerNode);
                }
                if (tmpNode >= -2) continue;
                tmpNode = - tmpNode - 3;
                pointList.add(this.nodeAccess, tmpNode);
                if (firstNode >= 0) {
                    newEdges.add(this.addEdge(firstNode, tmpNode, pointList, flags, wayOsmId));
                    pointList.clear();
                    pointList.add(this.nodeAccess, tmpNode);
                }
                firstNode = tmpNode;
            }
        }
        catch (RuntimeException ex) {
            logger.error("Couldn't properly add edge with osm ids:" + osmNodeIds, ex);
            throw ex;
        }
        return newEdges;
    }

    EdgeIteratorState addEdge(int fromIndex, int toIndex, PointList pointList, long flags, long wayOsmId) {
        if (fromIndex < 0 || toIndex < 0) {
            throw new AssertionError((Object)("to or from index is invalid for this edge " + fromIndex + "->" + toIndex + ", points:" + pointList));
        }
        if (pointList.getDimension() != this.nodeAccess.getDimension()) {
            throw new AssertionError((Object)("Dimension does not match for pointList vs. nodeAccess " + pointList.getDimension() + " <-> " + this.nodeAccess.getDimension()));
        }
        double towerNodeDistance = 0.0;
        double prevLat = pointList.getLatitude(0);
        double prevLon = pointList.getLongitude(0);
        double prevEle = pointList.is3D() ? pointList.getElevation(0) : Double.NaN;
        double ele = Double.NaN;
        PointList pillarNodes = new PointList(pointList.getSize() - 2, this.nodeAccess.is3D());
        int nodes = pointList.getSize();
        for (int i = 1; i < nodes; ++i) {
            double lat = pointList.getLatitude(i);
            double lon = pointList.getLongitude(i);
            if (pointList.is3D()) {
                ele = pointList.getElevation(i);
                towerNodeDistance += this.distCalc3D.calcDist(prevLat, prevLon, prevEle, lat, lon, ele);
                prevEle = ele;
            } else {
                towerNodeDistance += this.distCalc.calcDist(prevLat, prevLon, lat, lon);
            }
            prevLat = lat;
            prevLon = lon;
            if (nodes <= 2 || i >= nodes - 1) continue;
            if (pillarNodes.is3D()) {
                pillarNodes.add(lat, lon, ele);
                continue;
            }
            pillarNodes.add(lat, lon);
        }
        if (towerNodeDistance < 1.0E-4) {
            ++this.zeroCounter;
            towerNodeDistance = 1.0E-4;
        }
        if (Double.isInfinite(towerNodeDistance) || Double.isNaN(towerNodeDistance)) {
            logger.warn("Bug in OSM or GraphHopper. Illegal tower node distance " + towerNodeDistance + " reset to 1m, osm way " + wayOsmId);
            towerNodeDistance = 1.0;
        }
        EdgeIteratorState iter = this.graph.edge(fromIndex, toIndex).setDistance(towerNodeDistance).setFlags(flags);
        if (nodes > 2) {
            if (this.doSimplify) {
                this.simplifyAlgo.simplify(pillarNodes);
            }
            iter.setWayGeometry(pillarNodes);
        }
        this.storeOsmWayID(iter.getEdge(), wayOsmId);
        return iter;
    }

    private void storeOsmWayID(int edgeId, long osmWayId) {
        if (this.getOsmWayIdSet().contains(osmWayId)) {
            this.getEdgeIdToOsmWayIdMap().put(edgeId, osmWayId);
        }
    }

    private int handlePillarNode(int tmpNode, long osmId, PointList pointList, boolean convertToTowerNode) {
        double lat = this.pillarInfo.getLatitude(tmpNode -= 3);
        double lon = this.pillarInfo.getLongitude(tmpNode);
        double ele = this.pillarInfo.getElevation(tmpNode);
        if (lat == Double.MAX_VALUE || lon == Double.MAX_VALUE) {
            throw new RuntimeException("Conversion pillarNode to towerNode already happended!? osmId:" + osmId + " pillarIndex:" + tmpNode);
        }
        if (convertToTowerNode) {
            this.pillarInfo.setNode(tmpNode, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
            tmpNode = this.addTowerNode(osmId, lat, lon, ele);
        } else if (pointList.is3D()) {
            pointList.add(lat, lon, ele);
        } else {
            pointList.add(lat, lon);
        }
        return tmpNode;
    }

    protected void finishedReading() {
        this.printInfo("way");
        this.pillarInfo.clear();
        this.eleProvider.release();
        this.osmNodeIdToInternalNodeMap = null;
        this.osmNodeIdToNodeFlagsMap = null;
        this.osmWayIdToRouteWeightMap = null;
        this.osmWayIdSet = null;
        this.edgeIdToOsmWayIdMap = null;
    }

    long addBarrierNode(long nodeId) {
        OSMNode newNode;
        int graphIndex = this.getNodeMap().get(nodeId);
        if (graphIndex < -2) {
            graphIndex = - graphIndex - 3;
            newNode = new OSMNode(this.createNewNodeId(), this.nodeAccess, graphIndex);
        } else {
            newNode = new OSMNode(this.createNewNodeId(), this.pillarInfo, graphIndex -= 3);
        }
        long id = newNode.getId();
        this.prepareHighwayNode(id);
        this.addNode(newNode);
        return id;
    }

    private long createNewNodeId() {
        return this.newUniqueOsmId++;
    }

    Collection<EdgeIteratorState> addBarrierEdge(long fromId, long toId, long flags, long nodeFlags, long wayOsmId) {
        this.barrierNodeIds.clear();
        this.barrierNodeIds.add(fromId);
        this.barrierNodeIds.add(toId);
        return this.addOSMWay(this.barrierNodeIds, flags &= nodeFlags ^ -1, wayOsmId);
    }

    OSMTurnRelation createTurnRelation(OSMRelation relation) {
        OSMTurnRelation.Type type = OSMTurnRelation.Type.getRestrictionType(relation.getTag("restriction"));
        if (type != OSMTurnRelation.Type.UNSUPPORTED) {
            long fromWayID = -1;
            long viaNodeID = -1;
            long toWayID = -1;
            for (OSMRelation.Member member : relation.getMembers()) {
                if (1 == member.type()) {
                    if ("from".equals(member.role())) {
                        fromWayID = member.ref();
                        continue;
                    }
                    if (!"to".equals(member.role())) continue;
                    toWayID = member.ref();
                    continue;
                }
                if (0 != member.type() || !"via".equals(member.role())) continue;
                viaNodeID = member.ref();
            }
            if (fromWayID >= 0 && toWayID >= 0 && viaNodeID >= 0) {
                return new OSMTurnRelation(fromWayID, viaNodeID, toWayID, type);
            }
        }
        return null;
    }

    boolean isInBounds(OSMNode node) {
        return true;
    }

    protected LongIntMap getNodeMap() {
        return this.osmNodeIdToInternalNodeMap;
    }

    protected TLongLongMap getNodeFlagsMap() {
        return this.osmNodeIdToNodeFlagsMap;
    }

    TLongLongHashMap getRelFlagsMap() {
        return this.osmWayIdToRouteWeightMap;
    }

    public OSMReader setEncodingManager(EncodingManager em) {
        this.encodingManager = em;
        return this;
    }

    public OSMReader setWayPointMaxDistance(double maxDist) {
        this.doSimplify = maxDist > 0.0;
        this.simplifyAlgo.setMaxDistance(maxDist);
        return this;
    }

    public OSMReader setWorkerThreads(int numOfWorkers) {
        this.workerThreads = numOfWorkers;
        return this;
    }

    public OSMReader setElevationProvider(ElevationProvider eleProvider) {
        if (eleProvider == null) {
            throw new IllegalStateException("Use the NOOP elevation provider instead of null or don't call setElevationProvider");
        }
        if (!this.nodeAccess.is3D() && ElevationProvider.NOOP != eleProvider) {
            throw new IllegalStateException("Make sure you graph accepts 3D data");
        }
        this.eleProvider = eleProvider;
        return this;
    }

    public OSMReader setOSMFile(File osmFile) {
        this.osmFile = osmFile;
        return this;
    }

    private void printInfo(String str) {
        logger.info("finished " + str + " processing." + " nodes: " + this.graph.getNodes() + ", osmIdMap.size:" + this.getNodeMap().getSize() + ", osmIdMap:" + this.getNodeMap().getMemoryUsage() + "MB" + ", nodeFlagsMap.size:" + this.getNodeFlagsMap().size() + ", relFlagsMap.size:" + this.getRelFlagsMap().size() + ", zeroCounter:" + this.zeroCounter + " " + Helper.getMemInfo());
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }
}

