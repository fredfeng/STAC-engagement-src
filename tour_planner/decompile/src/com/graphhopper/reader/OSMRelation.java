/*
 * Decompiled with CFR 0_121.
 */
package com.graphhopper.reader;

import com.graphhopper.reader.OSMElement;
import java.util.ArrayList;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class OSMRelation
extends OSMElement {
    protected final ArrayList<Member> members = new ArrayList(5);

    public static OSMRelation create(long id, XMLStreamReader parser) throws XMLStreamException {
        OSMRelation rel = new OSMRelation(id);
        parser.nextTag();
        rel.readMembers(parser);
        rel.readTags(parser);
        return rel;
    }

    public OSMRelation(long id) {
        super(id, 2);
    }

    protected void readMembers(XMLStreamReader parser) throws XMLStreamException {
        int event = parser.getEventType();
        while (event != 8 && parser.getLocalName().equalsIgnoreCase("member")) {
            if (event == 1) {
                this.members.add(new Member(parser));
            }
            event = parser.nextTag();
        }
    }

    @Override
    public String toString() {
        return "Relation (" + this.getId() + ", " + this.members.size() + " members)";
    }

    public ArrayList<Member> getMembers() {
        return this.members;
    }

    public boolean isMetaRelation() {
        for (Member member : this.members) {
            if (member.type() != 2) continue;
            return true;
        }
        return false;
    }

    public boolean isMixedRelation() {
        boolean hasRel = false;
        boolean hasOther = false;
        for (Member member : this.members) {
            if (member.type() == 2) {
                hasRel = true;
            } else {
                hasOther = true;
            }
            if (!hasRel || !hasOther) continue;
            return true;
        }
        return false;
    }

    public void removeRelations() {
        for (int i = this.members.size() - 1; i >= 0; --i) {
            if (this.members.get(i).type() != 2) continue;
            this.members.remove(i);
        }
    }

    public void add(Member member) {
        this.members.add(member);
    }

    public static class Member {
        public static final int NODE = 0;
        public static final int WAY = 1;
        public static final int RELATION = 2;
        private static final String typeDecode = "nwr";
        private final int type;
        private final long ref;
        private final String role;

        public Member(XMLStreamReader parser) {
            String typeName = parser.getAttributeValue(null, "type");
            this.type = "nwr".indexOf(typeName.charAt(0));
            this.ref = Long.parseLong(parser.getAttributeValue(null, "ref"));
            this.role = parser.getAttributeValue(null, "role");
        }

        public Member(Member input) {
            this.type = input.type;
            this.ref = input.ref;
            this.role = input.role;
        }

        public Member(int type, long ref, String role) {
            this.type = type;
            this.ref = ref;
            this.role = role;
        }

        public String toString() {
            return "Member " + this.type + ":" + this.ref;
        }

        public int type() {
            return this.type;
        }

        public String role() {
            return this.role;
        }

        public long ref() {
            return this.ref;
        }
    }

}

