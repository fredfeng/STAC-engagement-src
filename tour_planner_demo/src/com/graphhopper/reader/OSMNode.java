/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.reader;

import com.graphhopper.reader.OSMElement;
import com.graphhopper.util.PointAccess;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class OSMNode
extends OSMElement {
    private final double lat;
    private final double lon;

    public static OSMNode create(long id, XMLStreamReader parser) throws XMLStreamException {
        OSMNode node = new OSMNode(id, Double.parseDouble(parser.getAttributeValue(null, "lat")), Double.parseDouble(parser.getAttributeValue(null, "lon")));
        parser.nextTag();
        node.readTags(parser);
        return node;
    }

    public OSMNode(long id, PointAccess pointAccess, int accessId) {
        super(id, 0);
        this.lat = pointAccess.getLatitude(accessId);
        this.lon = pointAccess.getLongitude(accessId);
        if (pointAccess.is3D()) {
            this.setTag("ele", pointAccess.getElevation(accessId));
        }
    }

    public OSMNode(long id, double lat, double lon) {
        super(id, 0);
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return this.lat;
    }

    public double getLon() {
        return this.lon;
    }

    public double getEle() {
        Object ele = this.getTags().get("ele");
        if (ele == null) {
            return Double.NaN;
        }
        return (Double)ele;
    }

    @Override
    public void setTag(String name, Object value) {
        if ("ele".equals(name)) {
            if (value == null) {
                value = null;
            } else if (value instanceof String) {
                String str = (String)value;
                if ((str = str.trim().replaceAll("\\,", ".")).isEmpty()) {
                    value = null;
                } else {
                    try {
                        value = Double.parseDouble(str);
                    }
                    catch (NumberFormatException ex) {
                        return;
                    }
                }
            } else {
                value = ((Number)value).doubleValue();
            }
        }
        super.setTag(name, value);
    }

    @Override
    public String toString() {
        StringBuilder txt = new StringBuilder();
        txt.append("Node: ");
        txt.append(this.getId());
        txt.append(" lat=");
        txt.append(this.getLat());
        txt.append(" lon=");
        txt.append(this.getLon());
        if (!this.getTags().isEmpty()) {
            txt.append("\n");
            txt.append(this.tagsToString());
        }
        return txt.toString();
    }
}

