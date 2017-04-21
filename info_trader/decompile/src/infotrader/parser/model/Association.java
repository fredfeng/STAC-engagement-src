/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.model;

import infotrader.parser.Options;
import infotrader.parser.model.AbstractCitation;
import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.Note;
import infotrader.parser.model.StringWithCustomTags;
import java.util.ArrayList;
import java.util.List;

public class Association
extends AbstractElement {
    private static final long serialVersionUID = -4189651960132766112L;
    private StringWithCustomTags associatedEntityType;
    private String associatedEntityXref;
    private List<AbstractCitation> citations;
    private List<Note> notes;
    private StringWithCustomTags relationship;

    public Association() {
        this.citations = this.getCitations(Options.isCollectionInitializationEnabled());
        this.notes = this.getNotes(Options.isCollectionInitializationEnabled());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof Association)) {
            return false;
        }
        Association other = (Association)obj;
        if (this.associatedEntityType == null ? other.associatedEntityType != null : !this.associatedEntityType.equals(other.associatedEntityType)) {
            return false;
        }
        if (this.associatedEntityXref == null ? other.associatedEntityXref != null : !this.associatedEntityXref.equals(other.associatedEntityXref)) {
            return false;
        }
        if (this.citations == null ? other.citations != null : !this.citations.equals(other.citations)) {
            return false;
        }
        if (this.notes == null ? other.notes != null : !this.notes.equals(other.notes)) {
            return false;
        }
        if (this.relationship == null ? other.relationship != null : !this.relationship.equals(other.relationship)) {
            return false;
        }
        return true;
    }

    public StringWithCustomTags getAssociatedEntityType() {
        return this.associatedEntityType;
    }

    public String getAssociatedEntityXref() {
        return this.associatedEntityXref;
    }

    public List<AbstractCitation> getCitations() {
        return this.citations;
    }

    public List<AbstractCitation> getCitations(boolean initializeIfNeeded) {
        if (initializeIfNeeded && this.citations == null) {
            this.citations = new ArrayList<AbstractCitation>(0);
        }
        return this.citations;
    }

    public List<Note> getNotes() {
        return this.notes;
    }

    public List<Note> getNotes(boolean initializeIfNeeded) {
        if (initializeIfNeeded && this.notes == null) {
            this.notes = new ArrayList<Note>(0);
        }
        return this.notes;
    }

    public StringWithCustomTags getRelationship() {
        return this.relationship;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = super.hashCode();
        result = 31 * result + (this.associatedEntityType == null ? 0 : this.associatedEntityType.hashCode());
        result = 31 * result + (this.associatedEntityXref == null ? 0 : this.associatedEntityXref.hashCode());
        result = 31 * result + (this.citations == null ? 0 : this.citations.hashCode());
        result = 31 * result + (this.notes == null ? 0 : this.notes.hashCode());
        result = 31 * result + (this.relationship == null ? 0 : this.relationship.hashCode());
        return result;
    }

    public void setAssociatedEntityType(StringWithCustomTags associatedEntityType) {
        this.associatedEntityType = associatedEntityType;
    }

    public void setAssociatedEntityXref(String associatedEntityXref) {
        this.associatedEntityXref = associatedEntityXref;
    }

    public void setRelationship(StringWithCustomTags relationship) {
        this.relationship = relationship;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Association [");
        if (this.associatedEntityType != null) {
            builder.append("associatedEntityType=");
            builder.append(this.associatedEntityType);
            builder.append(", ");
        }
        if (this.associatedEntityXref != null) {
            builder.append("associatedEntityXref=");
            builder.append(this.associatedEntityXref);
            builder.append(", ");
        }
        if (this.citations != null) {
            builder.append("citations=");
            builder.append(this.citations);
            builder.append(", ");
        }
        if (this.notes != null) {
            builder.append("notes=");
            builder.append(this.notes);
            builder.append(", ");
        }
        if (this.relationship != null) {
            builder.append("relationship=");
            builder.append(this.relationship);
            builder.append(", ");
        }
        if (this.customTags != null) {
            builder.append("customTags=");
            builder.append(this.customTags);
        }
        builder.append("]");
        return builder.toString();
    }
}

