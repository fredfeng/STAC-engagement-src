/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.reader;

import com.graphhopper.reader.OSMReader;
import com.graphhopper.routing.util.TurnCostEncoder;
import com.graphhopper.util.EdgeExplorer;
import com.graphhopper.util.EdgeIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class OSMTurnRelation {
    private final long fromOsmWayId;
    private final long viaOsmNodeId;
    private final long toOsmWayId;
    private final Type restriction;

    OSMTurnRelation(long fromWayID, long viaNodeID, long toWayID, Type restrictionType) {
        this.fromOsmWayId = fromWayID;
        this.viaOsmNodeId = viaNodeID;
        this.toOsmWayId = toWayID;
        this.restriction = restrictionType;
    }

    long getOsmIdFrom() {
        return this.fromOsmWayId;
    }

    long getOsmIdTo() {
        return this.toOsmWayId;
    }

    public Collection<TurnCostTableEntry> getRestrictionAsEntries(TurnCostEncoder encoder, EdgeExplorer edgeOutExplorer, EdgeExplorer edgeInExplorer, OSMReader osmReader) {
        int nodeVia = osmReader.getInternalNodeIdOfOsmNode(this.viaOsmNodeId);
        try {
            if (nodeVia == -1) {
                return Collections.emptyList();
            }
            int edgeIdFrom = -1;
            EdgeIterator iter = edgeInExplorer.setBaseNode(nodeVia);
            while (iter.next()) {
                if (osmReader.getOsmIdOfInternalEdge(iter.getEdge()) != this.fromOsmWayId) continue;
                edgeIdFrom = iter.getEdge();
                break;
            }
            if (edgeIdFrom == -1) {
                return Collections.emptyList();
            }
            ArrayList<TurnCostTableEntry> entries = new ArrayList<TurnCostTableEntry>();
            iter = edgeOutExplorer.setBaseNode(nodeVia);
            while (iter.next()) {
                int edgeId = iter.getEdge();
                long wayId = osmReader.getOsmIdOfInternalEdge(edgeId);
                if ((edgeId == edgeIdFrom || this.restriction != Type.ONLY || wayId == this.toOsmWayId) && (this.restriction != Type.NOT || wayId != this.toOsmWayId || wayId < 0)) continue;
                TurnCostTableEntry entry = new TurnCostTableEntry();
                entry.nodeVia = nodeVia;
                entry.edgeFrom = edgeIdFrom;
                entry.edgeTo = iter.getEdge();
                entry.flags = encoder.getTurnFlags(true, 0.0);
                entries.add(entry);
                if (this.restriction != Type.NOT) continue;
                break;
            }
            return entries;
        }
        catch (Exception e) {
            throw new IllegalStateException("Could not built turn table entry for relation of node with osmId:" + this.viaOsmNodeId, e);
        }
    }

    public String toString() {
        return "*-(" + this.fromOsmWayId + ")->" + this.viaOsmNodeId + "-(" + this.toOsmWayId + ")->*";
    }

    public static class TurnCostTableEntry {
        public int edgeFrom;
        public int nodeVia;
        public int edgeTo;
        public long flags;

        public long getItemId() {
            return (long)this.edgeFrom << 32 | (long)this.edgeTo;
        }

        public String toString() {
            return "*-(" + this.edgeFrom + ")->" + this.nodeVia + "-(" + this.edgeTo + ")->*";
        }
    }

    static enum Type {
        UNSUPPORTED,
        NOT,
        ONLY;
        
        private static final Map<String, Type> tags;

        private Type() {
        }

        public static Type getRestrictionType(String tag) {
            Type result = null;
            if (tag != null) {
                result = tags.get(tag);
            }
            return result != null ? result : UNSUPPORTED;
        }

        static {
            tags = new HashMap<String, Type>();
            tags.put("no_left_turn", NOT);
            tags.put("no_right_turn", NOT);
            tags.put("no_straight_on", NOT);
            tags.put("no_u_turn", NOT);
            tags.put("only_right_turn", ONLY);
            tags.put("only_left_turn", ONLY);
            tags.put("only_straight_on", ONLY);
        }
    }

}

