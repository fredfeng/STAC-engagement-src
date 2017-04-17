/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.model;

import infotrader.parser.Options;
import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.Note;
import infotrader.parser.model.SourceCallNumber;
import java.util.ArrayList;
import java.util.List;

public class RepositoryCitation
extends AbstractElement {
    private static final long serialVersionUID = -5547254435002018057L;
    private List<SourceCallNumber> callNumbers;
    private List<Note> notes;
    private String repositoryXref;

    public RepositoryCitation() {
        this.callNumbers = this.getCallNumbers(Options.isCollectionInitializationEnabled());
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
        if (!(obj instanceof RepositoryCitation)) {
            return false;
        }
        RepositoryCitation other = (RepositoryCitation)obj;
        if (this.callNumbers == null ? other.callNumbers != null : !this.callNumbers.equals(other.callNumbers)) {
            return false;
        }
        if (this.notes == null ? other.notes != null : !this.notes.equals(other.notes)) {
            return false;
        }
        if (this.repositoryXref == null ? other.repositoryXref != null : !this.repositoryXref.equals(other.repositoryXref)) {
            return false;
        }
        return true;
    }

    public List<SourceCallNumber> getCallNumbers() {
        return this.callNumbers;
    }

    public List<SourceCallNumber> getCallNumbers(boolean initializeIfNeeded) {
        if (initializeIfNeeded && this.callNumbers == null) {
            this.callNumbers = new ArrayList<SourceCallNumber>(0);
        }
        return this.callNumbers;
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

    public String getRepositoryXref() {
        return this.repositoryXref;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = super.hashCode();
        result = 31 * result + (this.callNumbers == null ? 0 : this.callNumbers.hashCode());
        result = 31 * result + (this.notes == null ? 0 : this.notes.hashCode());
        result = 31 * result + (this.repositoryXref == null ? 0 : this.repositoryXref.hashCode());
        return result;
    }

    public void setRepositoryXref(String repositoryXref) {
        this.repositoryXref = repositoryXref;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("RepositoryCitation [");
        if (this.callNumbers != null) {
            builder.append("callNumbers=");
            builder.append(this.callNumbers);
            builder.append(", ");
        }
        if (this.notes != null) {
            builder.append("notes=");
            builder.append(this.notes);
            builder.append(", ");
        }
        if (this.repositoryXref != null) {
            builder.append("repositoryXref=");
            builder.append(this.repositoryXref);
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

