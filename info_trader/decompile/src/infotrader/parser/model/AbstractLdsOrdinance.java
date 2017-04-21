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

public abstract class AbstractLdsOrdinance
extends AbstractElement {
    private static final long serialVersionUID = -5525451103364917970L;
    protected List<AbstractCitation> citations;
    protected StringWithCustomTags date;
    protected List<Note> notes;
    protected StringWithCustomTags place;
    protected StringWithCustomTags status;
    protected StringWithCustomTags temple;

    public AbstractLdsOrdinance() {
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
        if (!(obj instanceof AbstractLdsOrdinance)) {
            return false;
        }
        AbstractLdsOrdinance other = (AbstractLdsOrdinance)obj;
        if (this.getCitations() == null ? other.getCitations() != null : !this.getCitations().equals(other.getCitations())) {
            return false;
        }
        if (this.date == null ? other.date != null : !this.date.equals(other.date)) {
            return false;
        }
        if (this.notes == null ? other.notes != null : !this.notes.equals(other.notes)) {
            return false;
        }
        if (this.place == null ? other.place != null : !this.place.equals(other.place)) {
            return false;
        }
        if (this.status == null ? other.status != null : !this.status.equals(other.status)) {
            return false;
        }
        if (this.temple == null ? other.temple != null : !this.temple.equals(other.temple)) {
            return false;
        }
        return true;
    }

    public List<AbstractCitation> getCitations() {
        return this.citations;
    }

    public List<AbstractCitation> getCitations(boolean initializeIfNeeded) {
        if (initializeIfNeeded && this.citations == null) {
            this.citations = new ArrayList<AbstractCitation>();
        }
        return this.citations;
    }

    public StringWithCustomTags getDate() {
        return this.date;
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

    public StringWithCustomTags getPlace() {
        return this.place;
    }

    public StringWithCustomTags getStatus() {
        return this.status;
    }

    public StringWithCustomTags getTemple() {
        return this.temple;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = super.hashCode();
        result = 31 * result + (this.getCitations() == null ? 0 : this.getCitations().hashCode());
        result = 31 * result + (this.date == null ? 0 : this.date.hashCode());
        result = 31 * result + (this.notes == null ? 0 : this.notes.hashCode());
        result = 31 * result + (this.place == null ? 0 : this.place.hashCode());
        result = 31 * result + (this.status == null ? 0 : this.status.hashCode());
        result = 31 * result + (this.temple == null ? 0 : this.temple.hashCode());
        return result;
    }

    public void setDate(StringWithCustomTags date) {
        this.date = date;
    }

    public void setPlace(StringWithCustomTags place) {
        this.place = place;
    }

    public void setStatus(StringWithCustomTags status) {
        this.status = status;
    }

    public void setTemple(StringWithCustomTags temple) {
        this.temple = temple;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AbstractLdsOrdinance [");
        if (this.citations != null) {
            builder.append("citations=");
            builder.append(this.citations);
            builder.append(", ");
        }
        if (this.date != null) {
            builder.append("date=");
            builder.append(this.date);
            builder.append(", ");
        }
        if (this.notes != null) {
            builder.append("notes=");
            builder.append(this.notes);
            builder.append(", ");
        }
        if (this.place != null) {
            builder.append("place=");
            builder.append(this.place);
            builder.append(", ");
        }
        if (this.status != null) {
            builder.append("status=");
            builder.append(this.status);
            builder.append(", ");
        }
        if (this.temple != null) {
            builder.append("temple=");
            builder.append(this.temple);
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

