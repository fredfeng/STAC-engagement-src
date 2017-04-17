/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.model;

import infotrader.parser.Options;
import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.Note;
import infotrader.parser.model.StringWithCustomTags;
import java.util.ArrayList;
import java.util.List;

public class ChangeDate
extends AbstractElement {
    private static final long serialVersionUID = 6134688970119877487L;
    private StringWithCustomTags date;
    private List<Note> notes;
    private StringWithCustomTags time;

    public ChangeDate() {
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
        ChangeDate other = (ChangeDate)obj;
        if (this.date == null ? other.date != null : !this.date.equals(other.date)) {
            return false;
        }
        if (this.notes == null ? other.notes != null : !this.notes.equals(other.notes)) {
            return false;
        }
        if (this.time == null ? other.time != null : !this.time.equals(other.time)) {
            return false;
        }
        return true;
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

    public StringWithCustomTags getTime() {
        return this.time;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = super.hashCode();
        result = 31 * result + (this.date == null ? 0 : this.date.hashCode());
        result = 31 * result + (this.notes == null ? 0 : this.notes.hashCode());
        result = 31 * result + (this.time == null ? 0 : this.time.hashCode());
        return result;
    }

    public void setDate(StringWithCustomTags date) {
        this.date = date;
    }

    public void setTime(StringWithCustomTags time) {
        this.time = time;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ChangeDate [");
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
        if (this.time != null) {
            builder.append("time=");
            builder.append(this.time);
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

