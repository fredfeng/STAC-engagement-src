/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.reader.pbf;

import com.google.protobuf.ByteString;
import java.util.Date;
import org.openstreetmap.osmosis.osmbinary.Osmformat;

public class PbfFieldDecoder {
    private static final double COORDINATE_SCALING_FACTOR = 1.0E-9;
    private String[] strings;
    private int coordGranularity;
    private long coordLatitudeOffset;
    private long coordLongitudeOffset;
    private int dateGranularity;

    public PbfFieldDecoder(Osmformat.PrimitiveBlock primitiveBlock) {
        this.coordGranularity = primitiveBlock.getGranularity();
        this.coordLatitudeOffset = primitiveBlock.getLatOffset();
        this.coordLongitudeOffset = primitiveBlock.getLonOffset();
        this.dateGranularity = primitiveBlock.getDateGranularity();
        Osmformat.StringTable stringTable = primitiveBlock.getStringtable();
        this.strings = new String[stringTable.getSCount()];
        for (int i = 0; i < this.strings.length; ++i) {
            this.strings[i] = stringTable.getS(i).toStringUtf8();
        }
    }

    public double decodeLatitude(long rawLatitude) {
        return 1.0E-9 * (double)(this.coordLatitudeOffset + (long)this.coordGranularity * rawLatitude);
    }

    public double decodeLongitude(long rawLongitude) {
        return 1.0E-9 * (double)(this.coordLongitudeOffset + (long)this.coordGranularity * rawLongitude);
    }

    public Date decodeTimestamp(long rawTimestamp) {
        return new Date((long)this.dateGranularity * rawTimestamp);
    }

    public String decodeString(int rawString) {
        return this.strings[rawString];
    }
}

