/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.model;

import infotrader.parser.Options;
import infotrader.parser.model.AbstractCitation;
import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.ChangeDate;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.model.UserReference;
import java.util.ArrayList;
import java.util.List;

public class Note
extends AbstractElement {
    private static final long serialVersionUID = 8355989906882622025L;
    private ChangeDate changeDate;
    private List<AbstractCitation> citations;
    private List<String> lines;
    private List<Note> notes;
    private StringWithCustomTags recIdNumber;
    private List<UserReference> userReferences;
    private String xref;

    public Note() {
        this.citations = this.getCitations(Options.isCollectionInitializationEnabled());
        this.lines = this.getLines(Options.isCollectionInitializationEnabled());
        this.notes = this.getNotes(Options.isCollectionInitializationEnabled());
        this.userReferences = this.getUserReferences(Options.isCollectionInitializationEnabled());
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
        Note other = (Note)obj;
        if (this.changeDate == null ? other.changeDate != null : !this.changeDate.equals(other.changeDate)) {
            return false;
        }
        if (this.citations == null ? other.citations != null : !this.citations.equals(other.citations)) {
            return false;
        }
        if (this.lines == null ? other.lines != null : !this.lines.equals(other.lines)) {
            return false;
        }
        if (this.recIdNumber == null ? other.recIdNumber != null : !this.recIdNumber.equals(other.recIdNumber)) {
            return false;
        }
        if (this.userReferences == null ? other.userReferences != null : !this.userReferences.equals(other.userReferences)) {
            return false;
        }
        if (this.xref == null ? other.xref != null : !this.xref.equals(other.xref)) {
            return false;
        }
        return true;
    }

    public ChangeDate getChangeDate() {
        return this.changeDate;
    }

    public List<Note> getNotes(boolean initializeIfNeeded) {
        if (initializeIfNeeded && this.notes == null) {
            this.notes = new ArrayList<Note>(0);
        }
        return this.notes;
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

    public List<String> getLines() {
        return this.lines;
    }

    public List<String> getLines(boolean initializeIfNeeded) {
        if (initializeIfNeeded && this.lines == null) {
            this.lines = new ArrayList<String>(0);
        }
        return this.lines;
    }

    public StringWithCustomTags getRecIdNumber() {
        return this.recIdNumber;
    }

    public List<UserReference> getUserReferences() {
        return this.userReferences;
    }

    public List<UserReference> getUserReferences(boolean initializeIfNeeded) {
        if (initializeIfNeeded && this.userReferences == null) {
            this.userReferences = new ArrayList<UserReference>(0);
        }
        return this.userReferences;
    }

    public String getXref() {
        return this.xref;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = super.hashCode();
        result = 31 * result + (this.changeDate == null ? 0 : this.changeDate.hashCode());
        result = 31 * result + (this.citations == null ? 0 : this.citations.hashCode());
        result = 31 * result + (this.lines == null ? 0 : this.lines.hashCode());
        result = 31 * result + (this.recIdNumber == null ? 0 : this.recIdNumber.hashCode());
        result = 31 * result + (this.userReferences == null ? 0 : this.userReferences.hashCode());
        result = 31 * result + (this.xref == null ? 0 : this.xref.hashCode());
        return result;
    }

    public void setChangeDate(ChangeDate changeDate) {
        this.changeDate = changeDate;
    }

    public void setRecIdNumber(StringWithCustomTags recIdNumber) {
        this.recIdNumber = recIdNumber;
    }

    public void setXref(String xref) {
        this.xref = xref;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Note [");
        if (this.changeDate != null) {
            builder.append("changeDate=");
            builder.append(this.changeDate);
            builder.append(", ");
        }
        if (this.citations != null) {
            builder.append("citations=");
            builder.append(this.citations);
            builder.append(", ");
        }
        if (this.lines != null) {
            builder.append("lines=");
            builder.append(this.lines);
            builder.append(", ");
        }
        if (this.recIdNumber != null) {
            builder.append("recIdNumber=");
            builder.append(this.recIdNumber);
            builder.append(", ");
        }
        if (this.userReferences != null) {
            builder.append("userReferences=");
            builder.append(this.userReferences);
            builder.append(", ");
        }
        if (this.xref != null) {
            builder.append("xref=");
            builder.append(this.xref);
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

