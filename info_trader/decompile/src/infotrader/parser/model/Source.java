/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.model;

import infotrader.parser.Options;
import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.ChangeDate;
import infotrader.parser.model.Multimedia;
import infotrader.parser.model.Note;
import infotrader.parser.model.RepositoryCitation;
import infotrader.parser.model.SourceData;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.model.UserReference;
import java.util.ArrayList;
import java.util.List;

public class Source
extends AbstractElement {
    private static final long serialVersionUID = 5580720679037154352L;
    private ChangeDate changeDate;
    private SourceData data;
    private List<Multimedia> multimedia;
    private List<Note> notes;
    private List<String> originatorsAuthors;
    private List<String> publicationFacts;
    private StringWithCustomTags recIdNumber;
    private RepositoryCitation repositoryCitation;
    private StringWithCustomTags sourceFiledBy;
    private List<String> sourceText;
    private List<String> title;
    private List<UserReference> userReferences;
    private String xref;

    public Source(String xref) {
        this.multimedia = this.getMultimedia(Options.isCollectionInitializationEnabled());
        this.notes = this.getNotes(Options.isCollectionInitializationEnabled());
        this.originatorsAuthors = this.getOriginatorsAuthors(Options.isCollectionInitializationEnabled());
        this.publicationFacts = this.getPublicationFacts(Options.isCollectionInitializationEnabled());
        this.sourceText = this.getSourceText(Options.isCollectionInitializationEnabled());
        this.title = this.getTitle(Options.isCollectionInitializationEnabled());
        this.userReferences = this.getUserReferences(Options.isCollectionInitializationEnabled());
        if (xref != null) {
            this.xref = xref;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof Source)) {
            return false;
        }
        Source other = (Source)obj;
        if (this.changeDate == null ? other.changeDate != null : !this.changeDate.equals(other.changeDate)) {
            return false;
        }
        if (this.data == null ? other.data != null : !this.data.equals(other.data)) {
            return false;
        }
        if (this.multimedia == null ? other.multimedia != null : !this.multimedia.equals(other.multimedia)) {
            return false;
        }
        if (this.notes == null ? other.notes != null : !this.notes.equals(other.notes)) {
            return false;
        }
        if (this.originatorsAuthors == null ? other.originatorsAuthors != null : !this.originatorsAuthors.equals(other.originatorsAuthors)) {
            return false;
        }
        if (this.publicationFacts == null ? other.publicationFacts != null : !this.publicationFacts.equals(other.publicationFacts)) {
            return false;
        }
        if (this.recIdNumber == null ? other.recIdNumber != null : !this.recIdNumber.equals(other.recIdNumber)) {
            return false;
        }
        if (this.repositoryCitation == null ? other.repositoryCitation != null : !this.repositoryCitation.equals(other.repositoryCitation)) {
            return false;
        }
        if (this.sourceFiledBy == null ? other.sourceFiledBy != null : !this.sourceFiledBy.equals(other.sourceFiledBy)) {
            return false;
        }
        if (this.sourceText == null ? other.sourceText != null : !this.sourceText.equals(other.sourceText)) {
            return false;
        }
        if (this.title == null ? other.title != null : !this.title.equals(other.title)) {
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

    public SourceData getData() {
        return this.data;
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

    public List<Note> getNotes() {
        return this.notes;
    }

    public List<Note> getNotes(boolean initializeIfNeeded) {
        if (initializeIfNeeded && this.notes == null) {
            this.notes = new ArrayList<Note>(0);
        }
        return this.notes;
    }

    public List<String> getOriginatorsAuthors() {
        return this.originatorsAuthors;
    }

    public List<String> getOriginatorsAuthors(boolean initializeIfNeeded) {
        if (initializeIfNeeded && this.originatorsAuthors == null) {
            this.originatorsAuthors = new ArrayList<String>(0);
        }
        return this.originatorsAuthors;
    }

    public List<String> getPublicationFacts() {
        return this.publicationFacts;
    }

    public List<String> getPublicationFacts(boolean initializeIfNeeded) {
        if (initializeIfNeeded && this.publicationFacts == null) {
            this.publicationFacts = new ArrayList<String>(0);
        }
        return this.publicationFacts;
    }

    public StringWithCustomTags getRecIdNumber() {
        return this.recIdNumber;
    }

    public RepositoryCitation getRepositoryCitation() {
        return this.repositoryCitation;
    }

    public StringWithCustomTags getSourceFiledBy() {
        return this.sourceFiledBy;
    }

    public List<String> getSourceText() {
        return this.sourceText;
    }

    public List<String> getSourceText(boolean initializeIfNeeded) {
        if (initializeIfNeeded && this.sourceText == null) {
            this.sourceText = new ArrayList<String>(0);
        }
        return this.sourceText;
    }

    public List<String> getTitle() {
        return this.title;
    }

    public List<String> getTitle(boolean initializeIfNeeded) {
        if (initializeIfNeeded && this.title == null) {
            this.title = new ArrayList<String>(0);
        }
        return this.title;
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
        result = 31 * result + (this.data == null ? 0 : this.data.hashCode());
        result = 31 * result + (this.multimedia == null ? 0 : this.multimedia.hashCode());
        result = 31 * result + (this.notes == null ? 0 : this.notes.hashCode());
        result = 31 * result + (this.originatorsAuthors == null ? 0 : this.originatorsAuthors.hashCode());
        result = 31 * result + (this.publicationFacts == null ? 0 : this.publicationFacts.hashCode());
        result = 31 * result + (this.recIdNumber == null ? 0 : this.recIdNumber.hashCode());
        result = 31 * result + (this.repositoryCitation == null ? 0 : this.repositoryCitation.hashCode());
        result = 31 * result + (this.sourceFiledBy == null ? 0 : this.sourceFiledBy.hashCode());
        result = 31 * result + (this.sourceText == null ? 0 : this.sourceText.hashCode());
        result = 31 * result + (this.title == null ? 0 : this.title.hashCode());
        result = 31 * result + (this.userReferences == null ? 0 : this.userReferences.hashCode());
        result = 31 * result + (this.xref == null ? 0 : this.xref.hashCode());
        return result;
    }

    public void setChangeDate(ChangeDate changeDate) {
        this.changeDate = changeDate;
    }

    public void setData(SourceData data) {
        this.data = data;
    }

    public void setRecIdNumber(StringWithCustomTags recIdNumber) {
        this.recIdNumber = recIdNumber;
    }

    public void setRepositoryCitation(RepositoryCitation repositoryCitation) {
        this.repositoryCitation = repositoryCitation;
    }

    public void setSourceFiledBy(StringWithCustomTags sourceFiledBy) {
        this.sourceFiledBy = sourceFiledBy;
    }

    public void setXref(String xref) {
        this.xref = xref;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Source [");
        if (this.changeDate != null) {
            builder.append("changeDate=");
            builder.append(this.changeDate);
            builder.append(", ");
        }
        if (this.data != null) {
            builder.append("data=");
            builder.append(this.data);
            builder.append(", ");
        }
        if (this.multimedia != null) {
            builder.append("multimedia=");
            builder.append(this.multimedia);
            builder.append(", ");
        }
        if (this.notes != null) {
            builder.append("notes=");
            builder.append(this.notes);
            builder.append(", ");
        }
        if (this.originatorsAuthors != null) {
            builder.append("originatorsAuthors=");
            builder.append(this.originatorsAuthors);
            builder.append(", ");
        }
        if (this.publicationFacts != null) {
            builder.append("publicationFacts=");
            builder.append(this.publicationFacts);
            builder.append(", ");
        }
        if (this.recIdNumber != null) {
            builder.append("recIdNumber=");
            builder.append(this.recIdNumber);
            builder.append(", ");
        }
        if (this.repositoryCitation != null) {
            builder.append("repositoryCitation=");
            builder.append(this.repositoryCitation);
            builder.append(", ");
        }
        if (this.sourceFiledBy != null) {
            builder.append("sourceFiledBy=");
            builder.append(this.sourceFiledBy);
            builder.append(", ");
        }
        if (this.sourceText != null) {
            builder.append("sourceText=");
            builder.append(this.sourceText);
            builder.append(", ");
        }
        if (this.title != null) {
            builder.append("title=");
            builder.append(this.title);
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

