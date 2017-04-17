/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.model;

import infotrader.parser.Options;
import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.CharacterSet;
import infotrader.parser.model.InfoTraderVersion;
import infotrader.parser.model.Note;
import infotrader.parser.model.SourceSystem;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.model.Submission;
import infotrader.parser.model.Submitter;
import java.util.ArrayList;
import java.util.List;

public class Header
extends AbstractElement {
    private static final long serialVersionUID = 2648392706111388922L;
    private CharacterSet characterSet = new CharacterSet();
    private List<String> copyrightData;
    private StringWithCustomTags date;
    private StringWithCustomTags destinationSystem;
    private StringWithCustomTags fileName;
    private InfoTraderVersion InfoTraderVersion;
    private StringWithCustomTags language;
    private List<Note> notes;
    private StringWithCustomTags placeHierarchy;
    private SourceSystem sourceSystem;
    private Submission submission;
    private Submitter submitter;
    private StringWithCustomTags time;

    public Header() {
        this.copyrightData = this.getCopyrightData(Options.isCollectionInitializationEnabled());
        this.InfoTraderVersion = new InfoTraderVersion();
        this.notes = this.getNotes(Options.isCollectionInitializationEnabled());
        this.sourceSystem = new SourceSystem();
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
        Header other = (Header)obj;
        if (this.characterSet == null ? other.characterSet != null : !this.characterSet.equals(other.characterSet)) {
            return false;
        }
        if (this.copyrightData == null ? other.copyrightData != null : !this.copyrightData.equals(other.copyrightData)) {
            return false;
        }
        if (this.date == null ? other.date != null : !this.date.equals(other.date)) {
            return false;
        }
        if (this.destinationSystem == null ? other.destinationSystem != null : !this.destinationSystem.equals(other.destinationSystem)) {
            return false;
        }
        if (this.fileName == null ? other.fileName != null : !this.fileName.equals(other.fileName)) {
            return false;
        }
        if (this.InfoTraderVersion == null ? other.InfoTraderVersion != null : !this.InfoTraderVersion.equals(other.InfoTraderVersion)) {
            return false;
        }
        if (this.language == null ? other.language != null : !this.language.equals(other.language)) {
            return false;
        }
        if (this.notes == null ? other.notes != null : !this.notes.equals(other.notes)) {
            return false;
        }
        if (this.placeHierarchy == null ? other.placeHierarchy != null : !this.placeHierarchy.equals(other.placeHierarchy)) {
            return false;
        }
        if (this.sourceSystem == null ? other.sourceSystem != null : !this.sourceSystem.equals(other.sourceSystem)) {
            return false;
        }
        if (this.submission == null ? other.submission != null : !this.submission.equals(other.submission)) {
            return false;
        }
        if (this.submitter == null ? other.submitter != null : !this.submitter.equals(other.submitter)) {
            return false;
        }
        if (this.time == null ? other.time != null : !this.time.equals(other.time)) {
            return false;
        }
        return true;
    }

    public CharacterSet getCharacterSet() {
        return this.characterSet;
    }

    public List<String> getCopyrightData() {
        return this.copyrightData;
    }

    public List<String> getCopyrightData(boolean initializeIfNeeded) {
        if (initializeIfNeeded && this.copyrightData == null) {
            this.copyrightData = new ArrayList<String>(0);
        }
        return this.copyrightData;
    }

    public StringWithCustomTags getDate() {
        return this.date;
    }

    public StringWithCustomTags getDestinationSystem() {
        return this.destinationSystem;
    }

    public StringWithCustomTags getFileName() {
        return this.fileName;
    }

    public InfoTraderVersion getInfoTraderVersion() {
        return this.InfoTraderVersion;
    }

    public StringWithCustomTags getLanguage() {
        return this.language;
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

    public StringWithCustomTags getPlaceHierarchy() {
        return this.placeHierarchy;
    }

    public SourceSystem getSourceSystem() {
        return this.sourceSystem;
    }

    public Submission getSubmission() {
        return this.submission;
    }

    public Submitter getSubmitter() {
        return this.submitter;
    }

    public StringWithCustomTags getTime() {
        return this.time;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = super.hashCode();
        result = 31 * result + (this.characterSet == null ? 0 : this.characterSet.hashCode());
        result = 31 * result + (this.copyrightData == null ? 0 : this.copyrightData.hashCode());
        result = 31 * result + (this.date == null ? 0 : this.date.hashCode());
        result = 31 * result + (this.destinationSystem == null ? 0 : this.destinationSystem.hashCode());
        result = 31 * result + (this.fileName == null ? 0 : this.fileName.hashCode());
        result = 31 * result + (this.InfoTraderVersion == null ? 0 : this.InfoTraderVersion.hashCode());
        result = 31 * result + (this.language == null ? 0 : this.language.hashCode());
        result = 31 * result + (this.notes == null ? 0 : this.notes.hashCode());
        result = 31 * result + (this.placeHierarchy == null ? 0 : this.placeHierarchy.hashCode());
        result = 31 * result + (this.sourceSystem == null ? 0 : this.sourceSystem.hashCode());
        result = 31 * result + (this.submission == null ? 0 : this.submission.hashCode());
        result = 31 * result + (this.submitter == null ? 0 : this.submitter.hashCode());
        result = 31 * result + (this.time == null ? 0 : this.time.hashCode());
        return result;
    }

    public void setCharacterSet(CharacterSet characterSet) {
        this.characterSet = characterSet;
    }

    public void setDate(StringWithCustomTags date) {
        this.date = date;
    }

    public void setDestinationSystem(StringWithCustomTags destinationSystem) {
        this.destinationSystem = destinationSystem;
    }

    public void setFileName(StringWithCustomTags fileName) {
        this.fileName = fileName;
    }

    public void setInfoTraderVersion(InfoTraderVersion InfoTraderVersion2) {
        this.InfoTraderVersion = InfoTraderVersion2;
    }

    public void setLanguage(StringWithCustomTags language) {
        this.language = language;
    }

    public void setPlaceHierarchy(StringWithCustomTags placeHierarchy) {
        this.placeHierarchy = placeHierarchy;
    }

    public void setSourceSystem(SourceSystem sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    public void setSubmitter(Submitter submitter) {
        this.submitter = submitter;
    }

    public void setTime(StringWithCustomTags time) {
        this.time = time;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Header [");
        if (this.characterSet != null) {
            builder.append("characterSet=");
            builder.append(this.characterSet);
            builder.append(", ");
        }
        if (this.copyrightData != null) {
            builder.append("copyrightData=");
            builder.append(this.copyrightData);
            builder.append(", ");
        }
        if (this.date != null) {
            builder.append("date=");
            builder.append(this.date);
            builder.append(", ");
        }
        if (this.destinationSystem != null) {
            builder.append("destinationSystem=");
            builder.append(this.destinationSystem);
            builder.append(", ");
        }
        if (this.fileName != null) {
            builder.append("fileName=");
            builder.append(this.fileName);
            builder.append(", ");
        }
        if (this.InfoTraderVersion != null) {
            builder.append("InfoTraderVersion=");
            builder.append(this.InfoTraderVersion);
            builder.append(", ");
        }
        if (this.language != null) {
            builder.append("language=");
            builder.append(this.language);
            builder.append(", ");
        }
        if (this.notes != null) {
            builder.append("notes=");
            builder.append(this.notes);
            builder.append(", ");
        }
        if (this.placeHierarchy != null) {
            builder.append("placeHierarchy=");
            builder.append(this.placeHierarchy);
            builder.append(", ");
        }
        if (this.sourceSystem != null) {
            builder.append("sourceSystem=");
            builder.append(this.sourceSystem);
            builder.append(", ");
        }
        if (this.submission != null) {
            builder.append("submission=");
            builder.append(this.submission);
            builder.append(", ");
        }
        if (this.submitter != null) {
            builder.append("submitter=");
            builder.append(this.submitter);
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

