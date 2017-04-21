/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.reader;

import com.graphhopper.reader.OSMElement;
import gnu.trove.list.TLongList;
import gnu.trove.list.array.TLongArrayList;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class OSMWay
extends OSMElement {
    protected final TLongList nodes = new TLongArrayList(5);

    public static OSMWay create(long id, XMLStreamReader parser) throws XMLStreamException {
        OSMWay way = new OSMWay(id);
        parser.nextTag();
        way.readNodes(parser);
        way.readTags(parser);
        return way;
    }

    public OSMWay(long id) {
        super(id, 1);
    }

    protected void readNodes(XMLStreamReader parser) throws XMLStreamException {
        int event = parser.getEventType();
        while (event != 8 && parser.getLocalName().equals("nd")) {
            if (event == 1) {
                String ref = parser.getAttributeValue(null, "ref");
                this.nodes.add(Long.parseLong(ref));
            }
            event = parser.nextTag();
        }
    }

    public TLongList getNodes() {
        return this.nodes;
    }

    @Override
    public String toString() {
        return "Way id:" + this.getId() + ", nodes:" + this.nodes.size() + ", tags:" + super.toString();
    }
}

