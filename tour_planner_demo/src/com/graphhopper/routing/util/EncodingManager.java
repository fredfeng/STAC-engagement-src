/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.routing.util;

import com.graphhopper.reader.OSMNode;
import com.graphhopper.reader.OSMRelation;
import com.graphhopper.reader.OSMWay;
import com.graphhopper.routing.util.AbstractFlagEncoder;
import com.graphhopper.routing.util.Bike2WeightFlagEncoder;
import com.graphhopper.routing.util.BikeFlagEncoder;
import com.graphhopper.routing.util.CarFlagEncoder;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.routing.util.FootFlagEncoder;
import com.graphhopper.routing.util.MotorcycleFlagEncoder;
import com.graphhopper.routing.util.MountainBikeFlagEncoder;
import com.graphhopper.routing.util.RacingBikeFlagEncoder;
import com.graphhopper.routing.util.TurnWeighting;
import com.graphhopper.storage.Directory;
import com.graphhopper.storage.RAMDirectory;
import com.graphhopper.storage.StorableProperties;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.Helper;
import com.graphhopper.util.PMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class EncodingManager {
    public static final String CAR = "car";
    public static final String BIKE = "bike";
    public static final String BIKE2 = "bike2";
    public static final String RACINGBIKE = "racingbike";
    public static final String MOUNTAINBIKE = "mtb";
    public static final String FOOT = "foot";
    public static final String MOTORCYCLE = "motorcycle";
    private final List<AbstractFlagEncoder> edgeEncoders = new ArrayList<AbstractFlagEncoder>();
    private int nextWayBit = 0;
    private int nextNodeBit = 0;
    private int nextRelBit = 0;
    private int nextTurnBit = 0;
    private final int bitsForEdgeFlags;
    private final int bitsForTurnFlags = 32;
    private boolean enableInstructions = true;
    private static final String ERR = "Encoders are requesting more than %s bits of %s flags. ";
    private static final String WAY_ERR = "Decrease the number of vehicles or increase the flags to take long via graph.bytesForFlags=8";

    public EncodingManager(String flagEncodersStr) {
        this(flagEncodersStr, 4);
    }

    public EncodingManager(String flagEncodersStr, int bytesForFlags) {
        this(EncodingManager.parseEncoderString(flagEncodersStr), bytesForFlags);
    }

    public /* varargs */ EncodingManager(FlagEncoder ... flagEncoders) {
        this(Arrays.asList(flagEncoders));
    }

    public EncodingManager(List<? extends FlagEncoder> flagEncoders) {
        this(flagEncoders, 4);
    }

    public EncodingManager(List<? extends FlagEncoder> flagEncoders, int bytesForEdgeFlags) {
        if (bytesForEdgeFlags != 4 && bytesForEdgeFlags != 8) {
            throw new IllegalStateException("For 'edge flags' currently only 4 or 8 bytes supported");
        }
        this.bitsForEdgeFlags = bytesForEdgeFlags * 8;
        for (FlagEncoder flagEncoder : flagEncoders) {
            this.registerEncoder((AbstractFlagEncoder)flagEncoder);
        }
        if (this.edgeEncoders.isEmpty()) {
            throw new IllegalStateException("No vehicles found");
        }
    }

    public int getBytesForFlags() {
        return this.bitsForEdgeFlags / 8;
    }

    static List<FlagEncoder> parseEncoderString(String encoderList) {
        if (encoderList.contains(":")) {
            throw new IllegalArgumentException("EncodingManager does no longer use reflection instantiate encoders directly.");
        }
        String[] entries = encoderList.split(",");
        ArrayList<FlagEncoder> resultEncoders = new ArrayList<FlagEncoder>();
        for (String entry : entries) {
            FlagEncoder fe;
            if ((entry = entry.trim().toLowerCase()).isEmpty()) continue;
            String entryVal = "";
            if (entry.contains("|")) {
                entryVal = entry;
                entry = entry.split("\\|")[0];
            }
            PMap configuration = new PMap(entryVal);
            if (entry.equals("car")) {
                fe = new CarFlagEncoder(configuration);
            } else if (entry.equals("bike")) {
                fe = new BikeFlagEncoder(configuration);
            } else if (entry.equals("bike2")) {
                fe = new Bike2WeightFlagEncoder(configuration);
            } else if (entry.equals("racingbike")) {
                fe = new RacingBikeFlagEncoder(configuration);
            } else if (entry.equals("mtb")) {
                fe = new MountainBikeFlagEncoder(configuration);
            } else if (entry.equals("foot")) {
                fe = new FootFlagEncoder(configuration);
            } else if (entry.equals("motorcycle")) {
                fe = new MotorcycleFlagEncoder(configuration);
            } else {
                throw new IllegalArgumentException("entry in encoder list not supported " + entry);
            }
            if (configuration.has("version") && fe.getVersion() != configuration.getInt("version", -1)) {
                throw new IllegalArgumentException("Encoder " + entry + " was used in version " + configuration.getLong("version", -1) + ", but current version is " + fe.getVersion());
            }
            resultEncoders.add(fe);
        }
        return resultEncoders;
    }

    private void registerEncoder(AbstractFlagEncoder encoder) {
        int encoderCount = this.edgeEncoders.size();
        int usedBits = encoder.defineNodeBits(encoderCount, this.nextNodeBit);
        if (usedBits > this.bitsForEdgeFlags) {
            throw new IllegalArgumentException(String.format("Encoders are requesting more than %s bits of %s flags. ", this.bitsForEdgeFlags, "node"));
        }
        encoder.setNodeBitMask(usedBits - this.nextNodeBit, this.nextNodeBit);
        this.nextNodeBit = usedBits;
        usedBits = encoder.defineWayBits(encoderCount, this.nextWayBit);
        if (usedBits > this.bitsForEdgeFlags) {
            throw new IllegalArgumentException(String.format("Encoders are requesting more than %s bits of %s flags. ", this.bitsForEdgeFlags, "way") + "Decrease the number of vehicles or increase the flags to take long via graph.bytesForFlags=8");
        }
        encoder.setWayBitMask(usedBits - this.nextWayBit, this.nextWayBit);
        this.nextWayBit = usedBits;
        usedBits = encoder.defineRelationBits(encoderCount, this.nextRelBit);
        if (usedBits > this.bitsForEdgeFlags) {
            throw new IllegalArgumentException(String.format("Encoders are requesting more than %s bits of %s flags. ", this.bitsForEdgeFlags, "relation"));
        }
        encoder.setRelBitMask(usedBits - this.nextRelBit, this.nextRelBit);
        this.nextRelBit = usedBits;
        usedBits = encoder.defineTurnBits(encoderCount, this.nextTurnBit);
        if (usedBits > 32) {
            throw new IllegalArgumentException(String.format("Encoders are requesting more than %s bits of %s flags. ", this.bitsForEdgeFlags, "turn"));
        }
        this.nextTurnBit = usedBits;
        this.edgeEncoders.add(encoder);
    }

    public boolean supports(String encoder) {
        return this.getEncoder(encoder, false) != null;
    }

    public FlagEncoder getEncoder(String name) {
        return this.getEncoder(name, true);
    }

    private FlagEncoder getEncoder(String name, boolean throwExc) {
        for (AbstractFlagEncoder encoder : this.edgeEncoders) {
            if (!name.equalsIgnoreCase(encoder.toString())) continue;
            return encoder;
        }
        if (throwExc) {
            throw new IllegalArgumentException("Encoder for " + name + " not found. Existing: " + this.toDetailsString());
        }
        return null;
    }

    public long acceptWay(OSMWay way) {
        long includeWay = 0;
        for (AbstractFlagEncoder encoder : this.edgeEncoders) {
            includeWay |= encoder.acceptWay(way);
        }
        return includeWay;
    }

    public long handleRelationTags(OSMRelation relation, long oldRelationFlags) {
        long flags = 0;
        for (AbstractFlagEncoder encoder : this.edgeEncoders) {
            flags |= encoder.handleRelationTags(relation, oldRelationFlags);
        }
        return flags;
    }

    public long handleWayTags(OSMWay way, long includeWay, long relationFlags) {
        long flags = 0;
        for (AbstractFlagEncoder encoder : this.edgeEncoders) {
            flags |= encoder.handleWayTags(way, includeWay, relationFlags & encoder.getRelBitMask());
        }
        return flags;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        for (AbstractFlagEncoder encoder : this.edgeEncoders) {
            if (str.length() > 0) {
                str.append(",");
            }
            str.append(encoder.toString());
        }
        return str.toString();
    }

    public String toDetailsString() {
        StringBuilder str = new StringBuilder();
        for (AbstractFlagEncoder encoder : this.edgeEncoders) {
            if (str.length() > 0) {
                str.append(",");
            }
            str.append(encoder.toString()).append("|").append(encoder.getPropertiesString()).append("|version=").append(encoder.getVersion());
        }
        return str.toString();
    }

    public long flagsDefault(boolean forward, boolean backward) {
        long flags = 0;
        for (AbstractFlagEncoder encoder : this.edgeEncoders) {
            flags |= encoder.flagsDefault(forward, backward);
        }
        return flags;
    }

    public long reverseFlags(long flags) {
        int len = this.edgeEncoders.size();
        for (int i = 0; i < len; ++i) {
            flags = this.edgeEncoders.get(i).reverseFlags(flags);
        }
        return flags;
    }

    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + (this.edgeEncoders != null ? this.edgeEncoders.hashCode() : 0);
        return hash;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        EncodingManager other = (EncodingManager)obj;
        if (!(this.edgeEncoders == other.edgeEncoders || this.edgeEncoders != null && this.edgeEncoders.equals(other.edgeEncoders))) {
            return false;
        }
        return true;
    }

    public long handleNodeTags(OSMNode node) {
        long flags = 0;
        for (AbstractFlagEncoder encoder : this.edgeEncoders) {
            flags |= encoder.handleNodeTags(node);
        }
        return flags;
    }

    public EncodingManager setEnableInstructions(boolean enableInstructions) {
        this.enableInstructions = enableInstructions;
        return this;
    }

    public void applyWayTags(OSMWay way, EdgeIteratorState edge) {
        if (this.enableInstructions) {
            String name = EncodingManager.fixWayName(way.getTag("name"));
            String refName = EncodingManager.fixWayName(way.getTag("ref"));
            if (!Helper.isEmpty(refName)) {
                name = Helper.isEmpty(name) ? refName : name + ", " + refName;
            }
            edge.setName(name);
        }
        for (AbstractFlagEncoder encoder : this.edgeEncoders) {
            encoder.applyWayTags(way, edge);
        }
    }

    public List<FlagEncoder> fetchEdgeEncoders() {
        ArrayList<FlagEncoder> list = new ArrayList<FlagEncoder>();
        list.addAll(this.edgeEncoders);
        return list;
    }

    static String fixWayName(String str) {
        if (str == null) {
            return "";
        }
        return str.replaceAll(";[ ]*", ", ");
    }

    public boolean needsTurnCostsSupport() {
        for (FlagEncoder encoder : this.edgeEncoders) {
            if (!encoder.supports(TurnWeighting.class)) continue;
            return true;
        }
        return false;
    }

    public static EncodingManager create(String ghLoc) {
        RAMDirectory dir = new RAMDirectory(ghLoc, true);
        StorableProperties properties = new StorableProperties(dir);
        if (!properties.loadExisting()) {
            throw new IllegalStateException("Cannot load properties to fetch EncodingManager configuration at: " + dir.getLocation());
        }
        properties.checkVersions(false);
        String acceptStr = properties.get("graph.flagEncoders");
        if (acceptStr.isEmpty()) {
            throw new IllegalStateException("EncodingManager was not configured. And no one was found in the graph: " + dir.getLocation());
        }
        int bytesForFlags = 4;
        if ("8".equals(properties.get("graph.bytesForFlags"))) {
            bytesForFlags = 8;
        }
        return new EncodingManager(acceptStr, bytesForFlags);
    }
}

