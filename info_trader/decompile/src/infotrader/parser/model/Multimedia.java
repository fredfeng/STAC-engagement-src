/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.model;

import infotrader.parser.Options;
import infotrader.parser.model.AbstractCitation;
import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.ChangeDate;
import infotrader.parser.model.FileReference;
import infotrader.parser.model.Note;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.model.UserReference;
import java.util.ArrayList;
import java.util.List;

public class Multimedia
extends AbstractElement {
    private static final long serialVersionUID = 9046705221369603960L;
    private List<String> blob;
    private ChangeDate changeDate;
    private List<AbstractCitation> citations;
    private Multimedia continuedObject;
    private StringWithCustomTags embeddedMediaFormat;
    private StringWithCustomTags embeddedTitle;
    private List<FileReference> fileReferences;
    private List<Note> notes;
    private StringWithCustomTags recIdNumber;
    private List<UserReference> userReferences;
    private String xref;

    public Multimedia() {
        this.blob = this.getBlob(Options.isCollectionInitializationEnabled());
        this.citations = this.getCitations(Options.isCollectionInitializationEnabled());
        this.fileReferences = this.getFileReferences(Options.isCollectionInitializationEnabled());
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
        Multimedia other = (Multimedia)obj;
        if (this.blob == null ? other.blob != null : !this.blob.equals(other.blob)) {
            return false;
        }
        if (this.embeddedMediaFormat == null ? other.embeddedMediaFormat != null : !this.embeddedMediaFormat.equals(other.embeddedMediaFormat)) {
            return false;
        }
        if (this.changeDate == null ? other.changeDate != null : !this.changeDate.equals(other.changeDate)) {
            return false;
        }
        if (this.citations == null ? other.citations != null : !this.citations.equals(other.citations)) {
            return false;
        }
        if (this.continuedObject == null ? other.continuedObject != null : !this.continuedObject.equals(other.continuedObject)) {
            return false;
        }
        if (this.fileReferences == null ? other.fileReferences != null : !this.fileReferences.equals(other.fileReferences)) {
            return false;
        }
        if (this.notes == null ? other.notes != null : !this.notes.equals(other.notes)) {
            return false;
        }
        if (this.recIdNumber == null ? other.recIdNumber != null : !this.recIdNumber.equals(other.recIdNumber)) {
            return false;
        }
        if (this.embeddedTitle == null ? other.embeddedTitle != null : !this.embeddedTitle.equals(other.embeddedTitle)) {
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

    public List<String> getBlob() {
        return this.blob;
    }

    public List<String> getBlob(boolean initializeIfNeeded) {
        if (initializeIfNeeded && this.blob == null) {
            this.blob = new ArrayList<String>(0);
        }
        return this.blob;
    }

    public ChangeDate getChangeDate() {
        return this.changeDate;
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

    public Multimedia getContinuedObject() {
        return this.continuedObject;
    }

    public StringWithCustomTags getEmbeddedMediaFormat() {
        return this.embeddedMediaFormat;
    }

    public StringWithCustomTags getEmbeddedTitle() {
        return this.embeddedTitle;
    }

    public List<FileReference> getFileReferences() {
        return this.fileReferences;
    }

    public List<FileReference> getFileReferences(boolean initializeIfNeeded) {
        if (initializeIfNeeded && this.fileReferences == null) {
            this.fileReferences = new ArrayList<FileReference>(0);
        }
        return this.fileReferences;
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
        result = 31 * result + (this.blob == null ? 0 : this.blob.hashCode());
        result = 31 * result + (this.embeddedMediaFormat == null ? 0 : this.embeddedMediaFormat.hashCode());
        result = 31 * result + (this.changeDate == null ? 0 : this.changeDate.hashCode());
        result = 31 * result + (this.citations == null ? 0 : this.citations.hashCode());
        result = 31 * result + (this.continuedObject == null ? 0 : this.continuedObject.hashCode());
        result = 31 * result + (this.fileReferences == null ? 0 : this.fileReferences.hashCode());
        result = 31 * result + (this.notes == null ? 0 : this.notes.hashCode());
        result = 31 * result + (this.recIdNumber == null ? 0 : this.recIdNumber.hashCode());
        result = 31 * result + (this.embeddedTitle == null ? 0 : this.embeddedTitle.hashCode());
        result = 31 * result + (this.userReferences == null ? 0 : this.userReferences.hashCode());
        result = 31 * result + (this.xref == null ? 0 : this.xref.hashCode());
        return result;
    }

    public void setChangeDate(ChangeDate changeDate) {
        this.changeDate = changeDate;
    }

    public void setContinuedObject(Multimedia continuedObject) {
        this.continuedObject = continuedObject;
    }

    public void setEmbeddedMediaFormat(StringWithCustomTags embeddedMediaFormat) {
        this.embeddedMediaFormat = embeddedMediaFormat;
    }

    public void setEmbeddedTitle(StringWithCustomTags embeddedTitle) {
        this.embeddedTitle = embeddedTitle;
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
        builder.append("Multimedia [");
        if (this.blob != null) {
            builder.append("blob=");
            builder.append(this.blob);
            builder.append(", ");
        }
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
        if (this.continuedObject != null) {
            builder.append("continuedObject=");
            builder.append(this.continuedObject);
            builder.append(", ");
        }
        if (this.embeddedMediaFormat != null) {
            builder.append("embeddedMediaFormat=");
            builder.append(this.embeddedMediaFormat);
            builder.append(", ");
        }
        if (this.embeddedTitle != null) {
            builder.append("embeddedTitle=");
            builder.append(this.embeddedTitle);
            builder.append(", ");
        }
        if (this.fileReferences != null) {
            builder.append("fileReferences=");
            builder.append(this.fileReferences);
            builder.append(", ");
        }
        if (this.notes != null) {
            builder.append("notes=");
            builder.append(this.notes);
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

