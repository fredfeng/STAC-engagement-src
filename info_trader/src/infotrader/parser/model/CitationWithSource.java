/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.model;

import infotrader.parser.Options;
import infotrader.parser.model.AbstractCitation;
import infotrader.parser.model.CitationData;
import infotrader.parser.model.Multimedia;
import infotrader.parser.model.Note;
import infotrader.parser.model.Source;
import infotrader.parser.model.StringWithCustomTags;
import java.util.ArrayList;
import java.util.List;

public class CitationWithSource
extends AbstractCitation {
    private static final long serialVersionUID = 1886846774727359828L;
    private StringWithCustomTags certainty;
    private List<CitationData> data;
    private StringWithCustomTags eventCited;
    private List<Multimedia> multimedia;
    private StringWithCustomTags roleInEvent;
    private Source source;
    private StringWithCustomTags whereInSource;

    public CitationWithSource() {
        this.data = this.getData(Options.isCollectionInitializationEnabled());
        this.multimedia = this.getMultimedia(Options.isCollectionInitializationEnabled());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        CitationWithSource other = (CitationWithSource)obj;
        if (this.certainty == null ? other.certainty != null : !this.certainty.equals(other.certainty)) {
            return false;
        }
        if (this.data == null ? other.data != null : !this.data.equals(other.data)) {
            return false;
        }
        if (this.eventCited == null ? other.eventCited != null : !this.eventCited.equals(other.eventCited)) {
            return false;
        }
        if (this.multimedia == null ? other.multimedia != null : !this.multimedia.equals(other.multimedia)) {
            return false;
        }
        if (this.getNotes() == null ? other.getNotes() != null : !this.getNotes().equals(other.getNotes())) {
            return false;
        }
        if (this.roleInEvent == null ? other.roleInEvent != null : !this.roleInEvent.equals(other.roleInEvent)) {
            return false;
        }
        if (this.source == null ? other.source != null : !this.source.equals(other.source)) {
            return false;
        }
        if (this.whereInSource == null ? other.whereInSource != null : !this.whereInSource.equals(other.whereInSource)) {
            return false;
        }
        return true;
    }

    public StringWithCustomTags getCertainty() {
        return this.certainty;
    }

    public List<CitationData> getData() {
        return this.data;
    }

    public List<CitationData> getData(boolean initializeIfNeeded) {
        if (initializeIfNeeded && this.data == null) {
            this.data = new ArrayList<CitationData>(0);
        }
        return this.data;
    }

    public StringWithCustomTags getEventCited() {
        return this.eventCited;
    }

    public List<Multimedia> getMultimedia() {
        return this.multimedia;
    }

    public List<Multimedia> getMultimedia(boolean initializeIfNeeded) {
        if (initializeIfNeeded && this.multimedia == null) {
            this.multimedia = new ArrayList<Multimedia>(0);
        }
        return this.multimedia;
    }

    public StringWithCustomTags getRoleInEvent() {
        return this.roleInEvent;
    }

    public Source getSource() {
        return this.source;
    }

    public StringWithCustomTags getWhereInSource() {
        return this.whereInSource;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = super.hashCode();
        result = 31 * result + (this.certainty == null ? 0 : this.certainty.hashCode());
        result = 31 * result + (this.data == null ? 0 : this.data.hashCode());
        result = 31 * result + (this.eventCited == null ? 0 : this.eventCited.hashCode());
        result = 31 * result + (this.multimedia == null ? 0 : this.multimedia.hashCode());
        result = 31 * result + (this.getNotes() == null ? 0 : this.getNotes().hashCode());
        result = 31 * result + (this.roleInEvent == null ? 0 : this.roleInEvent.hashCode());
        result = 31 * result + (this.source == null ? 0 : this.source.hashCode());
        result = 31 * result + (this.whereInSource == null ? 0 : this.whereInSource.hashCode());
        return result;
    }

    public void setCertainty(StringWithCustomTags certainty) {
        this.certainty = certainty;
    }

    public void setEventCited(StringWithCustomTags eventCited) {
        this.eventCited = eventCited;
    }

    public void setRoleInEvent(StringWithCustomTags roleInEvent) {
        this.roleInEvent = roleInEvent;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public void setWhereInSource(StringWithCustomTags whereInSource) {
        this.whereInSource = whereInSource;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CitationWithSource [");
        if (this.certainty != null) {
            builder.append("certainty=");
            builder.append(this.certainty);
            builder.append(", ");
        }
        if (this.data != null) {
            builder.append("data=");
            builder.append(this.data);
            builder.append(", ");
        }
        if (this.eventCited != null) {
            builder.append("eventCited=");
            builder.append(this.eventCited);
            builder.append(", ");
        }
        if (this.multimedia != null) {
            builder.append("multimedia=");
            builder.append(this.multimedia);
            builder.append(", ");
        }
        if (this.roleInEvent != null) {
            builder.append("roleInEvent=");
            builder.append(this.roleInEvent);
            builder.append(", ");
        }
        if (this.source != null) {
            builder.append("source=");
            builder.append(this.source);
            builder.append(", ");
        }
        if (this.whereInSource != null) {
            builder.append("whereInSource=");
            builder.append(this.whereInSource);
            builder.append(", ");
        }
        if (this.notes != null) {
            builder.append("notes=");
            builder.append(this.notes);
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

