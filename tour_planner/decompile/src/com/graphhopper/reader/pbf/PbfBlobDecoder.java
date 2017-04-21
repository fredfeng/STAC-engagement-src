/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.reader.pbf;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.ProtocolStringList;
import com.graphhopper.reader.OSMElement;
import com.graphhopper.reader.OSMNode;
import com.graphhopper.reader.OSMRelation;
import com.graphhopper.reader.OSMWay;
import com.graphhopper.reader.pbf.PbfBlobDecoderListener;
import com.graphhopper.reader.pbf.PbfFieldDecoder;
import gnu.trove.list.TLongList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import org.openstreetmap.osmosis.osmbinary.Fileformat;
import org.openstreetmap.osmosis.osmbinary.Osmformat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PbfBlobDecoder
implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(PbfBlobDecoder.class);
    private final boolean checkData = false;
    private final String blobType;
    private final byte[] rawBlob;
    private final PbfBlobDecoderListener listener;
    private List<OSMElement> decodedEntities;

    public PbfBlobDecoder(String blobType, byte[] rawBlob, PbfBlobDecoderListener listener) {
        this.blobType = blobType;
        this.rawBlob = rawBlob;
        this.listener = listener;
    }

    private byte[] readBlobContent() throws IOException {
        byte[] blobData;
        Fileformat.Blob blob = Fileformat.Blob.parseFrom(this.rawBlob);
        if (blob.hasRaw()) {
            blobData = blob.getRaw().toByteArray();
        } else if (blob.hasZlibData()) {
            Inflater inflater = new Inflater();
            inflater.setInput(blob.getZlibData().toByteArray());
            blobData = new byte[blob.getRawSize()];
            try {
                inflater.inflate(blobData);
            }
            catch (DataFormatException e) {
                throw new RuntimeException("Unable to decompress PBF blob.", e);
            }
            if (!inflater.finished()) {
                throw new RuntimeException("PBF blob contains incomplete compressed data.");
            }
        } else {
            throw new RuntimeException("PBF blob uses unsupported compression, only raw or zlib may be used.");
        }
        return blobData;
    }

    private void processOsmHeader(byte[] data) throws InvalidProtocolBufferException {
        Osmformat.HeaderBlock header = Osmformat.HeaderBlock.parseFrom(data);
        List<String> supportedFeatures = Arrays.asList("OsmSchema-V0.6", "DenseNodes");
        ArrayList<String> unsupportedFeatures = new ArrayList<String>();
        for (String feature : header.getRequiredFeaturesList()) {
            if (supportedFeatures.contains(feature)) continue;
            unsupportedFeatures.add(feature);
        }
        if (unsupportedFeatures.size() > 0) {
            throw new RuntimeException("PBF file contains unsupported features " + unsupportedFeatures);
        }
    }

    private Map<String, String> buildTags(List<Integer> keys, List<Integer> values, PbfFieldDecoder fieldDecoder) {
        Iterator<Integer> keyIterator = keys.iterator();
        Iterator<Integer> valueIterator = values.iterator();
        if (keyIterator.hasNext()) {
            HashMap<String, String> tags = new HashMap<String, String>(keys.size());
            while (keyIterator.hasNext()) {
                String key = fieldDecoder.decodeString(keyIterator.next());
                String value = fieldDecoder.decodeString(valueIterator.next());
                tags.put(key, value);
            }
            return tags;
        }
        return null;
    }

    private void processNodes(List<Osmformat.Node> nodes, PbfFieldDecoder fieldDecoder) {
        for (Osmformat.Node node : nodes) {
            Map<String, String> tags = this.buildTags(node.getKeysList(), node.getValsList(), fieldDecoder);
            OSMNode osmNode = new OSMNode(node.getId(), fieldDecoder.decodeLatitude(node.getLat()), fieldDecoder.decodeLatitude(node.getLon()));
            osmNode.setTags(tags);
            this.decodedEntities.add(osmNode);
        }
    }

    private void processNodes(Osmformat.DenseNodes nodes, PbfFieldDecoder fieldDecoder) {
        List<Long> idList = nodes.getIdList();
        List<Long> latList = nodes.getLatList();
        List<Long> lonList = nodes.getLonList();
        Iterator<Integer> keysValuesIterator = nodes.getKeysValsList().iterator();
        long nodeId = 0;
        long latitude = 0;
        long longitude = 0;
        for (int i = 0; i < idList.size(); ++i) {
            int keyIndex;
            nodeId += idList.get(i).longValue();
            latitude += latList.get(i).longValue();
            longitude += lonList.get(i).longValue();
            HashMap<String, String> tags = null;
            while (keysValuesIterator.hasNext() && (keyIndex = keysValuesIterator.next().intValue()) != 0) {
                int valueIndex = keysValuesIterator.next();
                if (tags == null) {
                    tags = new HashMap<String, String>();
                }
                tags.put(fieldDecoder.decodeString(keyIndex), fieldDecoder.decodeString(valueIndex));
            }
            OSMNode node = new OSMNode(nodeId, (double)latitude / 1.0E7, (double)longitude / 1.0E7);
            node.setTags(tags);
            this.decodedEntities.add(node);
        }
    }

    private void processWays(List<Osmformat.Way> ways, PbfFieldDecoder fieldDecoder) {
        for (Osmformat.Way way : ways) {
            Map<String, String> tags = this.buildTags(way.getKeysList(), way.getValsList(), fieldDecoder);
            OSMWay osmWay = new OSMWay(way.getId());
            osmWay.setTags(tags);
            long nodeId = 0;
            TLongList wayNodes = osmWay.getNodes();
            Iterator<Long> i$ = way.getRefsList().iterator();
            while (i$.hasNext()) {
                long nodeIdOffset = i$.next();
                wayNodes.add(nodeId += nodeIdOffset);
            }
            this.decodedEntities.add(osmWay);
        }
    }

    private void buildRelationMembers(OSMRelation relation, List<Long> memberIds, List<Integer> memberRoles, List<Osmformat.Relation.MemberType> memberTypes, PbfFieldDecoder fieldDecoder) {
        ArrayList<OSMRelation.Member> members = relation.getMembers();
        Iterator<Long> memberIdIterator = memberIds.iterator();
        Iterator<Integer> memberRoleIterator = memberRoles.iterator();
        Iterator<Osmformat.Relation.MemberType> memberTypeIterator = memberTypes.iterator();
        long refId = 0;
        while (memberIdIterator.hasNext()) {
            Osmformat.Relation.MemberType memberType = memberTypeIterator.next();
            refId += memberIdIterator.next().longValue();
            int entityType = 0;
            if (memberType == Osmformat.Relation.MemberType.WAY) {
                entityType = 1;
            } else if (memberType == Osmformat.Relation.MemberType.RELATION) {
                entityType = 2;
            }
            OSMRelation.Member member = new OSMRelation.Member(entityType, refId, fieldDecoder.decodeString(memberRoleIterator.next()));
            members.add(member);
        }
    }

    private void processRelations(List<Osmformat.Relation> relations, PbfFieldDecoder fieldDecoder) {
        for (Osmformat.Relation relation : relations) {
            Map<String, String> tags = this.buildTags(relation.getKeysList(), relation.getValsList(), fieldDecoder);
            OSMRelation osmRelation = new OSMRelation(relation.getId());
            osmRelation.setTags(tags);
            this.buildRelationMembers(osmRelation, relation.getMemidsList(), relation.getRolesSidList(), relation.getTypesList(), fieldDecoder);
            this.decodedEntities.add(osmRelation);
        }
    }

    private void processOsmPrimitives(byte[] data) throws InvalidProtocolBufferException {
        Osmformat.PrimitiveBlock block = Osmformat.PrimitiveBlock.parseFrom(data);
        PbfFieldDecoder fieldDecoder = new PbfFieldDecoder(block);
        for (Osmformat.PrimitiveGroup primitiveGroup : block.getPrimitivegroupList()) {
            log.debug("Processing OSM primitive group.");
            this.processNodes(primitiveGroup.getDense(), fieldDecoder);
            this.processNodes(primitiveGroup.getNodesList(), fieldDecoder);
            this.processWays(primitiveGroup.getWaysList(), fieldDecoder);
            this.processRelations(primitiveGroup.getRelationsList(), fieldDecoder);
        }
    }

    private void runAndTrapExceptions() {
        try {
            this.decodedEntities = new ArrayList<OSMElement>();
            if ("OSMHeader".equals(this.blobType)) {
                this.processOsmHeader(this.readBlobContent());
            } else if ("OSMData".equals(this.blobType)) {
                this.processOsmPrimitives(this.readBlobContent());
            } else if (log.isDebugEnabled()) {
                log.debug("Skipping unrecognised blob type " + this.blobType);
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Unable to process PBF blob", e);
        }
    }

    @Override
    public void run() {
        try {
            this.runAndTrapExceptions();
            this.listener.complete(this.decodedEntities);
        }
        catch (RuntimeException e) {
            this.listener.error(e);
        }
    }
}

