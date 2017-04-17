/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.model;

import infotrader.parser.Options;
import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.EventRecorded;
import infotrader.parser.model.Note;
import infotrader.parser.model.StringWithCustomTags;
import java.util.ArrayList;
import java.util.List;

public class SourceData
extends AbstractElement {
    private static final long serialVersionUID = -5082188791769651553L;
    private List<EventRecorded> eventsRecorded;
    private List<Note> notes;
    private StringWithCustomTags respAgency;

    public SourceData() {
        this.eventsRecorded = this.getEventsRecorded(Options.isCollectionInitializationEnabled());
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
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        SourceData other = (SourceData)obj;
        if (this.eventsRecorded == null ? other.eventsRecorded != null : !this.eventsRecorded.equals(other.eventsRecorded)) {
            return false;
        }
        if (this.notes == null ? other.notes != null : !this.notes.equals(other.notes)) {
            return false;
        }
        if (this.respAgency == null ? other.respAgency != null : !this.respAgency.equals(other.respAgency)) {
            return false;
        }
        return true;
    }

    public List<EventRecorded> getEventsRecorded() {
        return this.eventsRecorded;
    }

    public List<EventRecorded> getEventsRecorded(boolean initializeIfNeeded) {
        if (initializeIfNeeded && this.eventsRecorded == null) {
            this.eventsRecorded = new ArrayList<EventRecorded>(0);
        }
        return this.eventsRecorded;
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

    public StringWithCustomTags getRespAgency() {
        return this.respAgency;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = super.hashCode();
        result = 31 * result + (this.eventsRecorded == null ? 0 : this.eventsRecorded.hashCode());
        result = 31 * result + (this.notes == null ? 0 : this.notes.hashCode());
        result = 31 * result + (this.respAgency == null ? 0 : this.respAgency.hashCode());
        return result;
    }

    public void setRespAgency(StringWithCustomTags respAgency) {
        this.respAgency = respAgency;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SourceData [");
        if (this.eventsRecorded != null) {
            builder.append("eventsRecorded=");
            builder.append(this.eventsRecorded);
            builder.append(", ");
        }
        if (this.notes != null) {
            builder.append("notes=");
            builder.append(this.notes);
            builder.append(", ");
        }
        if (this.respAgency != null) {
            builder.append("respAgency=");
            builder.append(this.respAgency);
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

