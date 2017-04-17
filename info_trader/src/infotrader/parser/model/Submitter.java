/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.model;

import infotrader.parser.Options;
import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.Address;
import infotrader.parser.model.ChangeDate;
import infotrader.parser.model.Multimedia;
import infotrader.parser.model.Note;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.model.UserReference;
import java.util.ArrayList;
import java.util.List;

public class Submitter
extends AbstractElement {
    private static final long serialVersionUID = 964849855689332389L;
    private Address address;
    private ChangeDate changeDate;
    private List<StringWithCustomTags> emails;
    private List<StringWithCustomTags> faxNumbers;
    private List<StringWithCustomTags> languagePref;
    private List<Multimedia> multimedia;
    private StringWithCustomTags name;
    private List<Note> notes;
    private List<StringWithCustomTags> phoneNumbers;
    private StringWithCustomTags recIdNumber;
    private StringWithCustomTags regFileNumber;
    private List<UserReference> userReferences;
    private List<StringWithCustomTags> wwwUrls;
    private String xref;

    public Submitter() {
        this.emails = this.getEmails(Options.isCollectionInitializationEnabled());
        this.faxNumbers = this.getFaxNumbers(Options.isCollectionInitializationEnabled());
        this.languagePref = this.getLanguagePref(Options.isCollectionInitializationEnabled());
        this.multimedia = this.getMultimedia(Options.isCollectionInitializationEnabled());
        this.notes = this.getNotes(Options.isCollectionInitializationEnabled());
        this.phoneNumbers = this.getPhoneNumbers(Options.isCollectionInitializationEnabled());
        this.userReferences = this.getUserReferences(Options.isCollectionInitializationEnabled());
        this.wwwUrls = this.getWwwUrls(Options.isCollectionInitializationEnabled());
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
        Submitter other = (Submitter)obj;
        if (this.address == null ? other.address != null : !this.address.equals(other.address)) {
            return false;
        }
        if (this.changeDate == null ? other.changeDate != null : !this.changeDate.equals(other.changeDate)) {
            return false;
        }
        if (this.languagePref == null ? other.languagePref != null : !this.languagePref.equals(other.languagePref)) {
            return false;
        }
        if (this.multimedia == null ? other.multimedia != null : !this.multimedia.equals(other.multimedia)) {
            return false;
        }
        if (this.name == null ? other.name != null : !this.name.equals(other.name)) {
            return false;
        }
        if (this.notes == null ? other.notes != null : !this.notes.equals(other.notes)) {
            return false;
        }
        if (this.phoneNumbers == null ? other.phoneNumbers != null : !this.phoneNumbers.equals(other.phoneNumbers)) {
            return false;
        }
        if (this.wwwUrls == null ? other.wwwUrls != null : !this.wwwUrls.equals(other.wwwUrls)) {
            return false;
        }
        if (this.faxNumbers == null ? other.faxNumbers != null : !this.faxNumbers.equals(other.faxNumbers)) {
            return false;
        }
        if (this.emails == null ? other.emails != null : !this.emails.equals(other.emails)) {
            return false;
        }
        if (this.recIdNumber == null ? other.recIdNumber != null : !this.recIdNumber.equals(other.recIdNumber)) {
            return false;
        }
        if (this.regFileNumber == null ? other.regFileNumber != null : !this.regFileNumber.equals(other.regFileNumber)) {
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

    public Address getAddress() {
        return this.address;
    }

    public ChangeDate getChangeDate() {
        return this.changeDate;
    }

    public List<StringWithCustomTags> getEmails() {
        return this.emails;
    }

    public List<StringWithCustomTags> getEmails(boolean initializeIfNeeded) {
        if (initializeIfNeeded && this.emails == null) {
            this.emails = new ArrayList<StringWithCustomTags>(0);
        }
        return this.emails;
    }

    public List<StringWithCustomTags> getFaxNumbers() {
        return this.faxNumbers;
    }

    public List<StringWithCustomTags> getFaxNumbers(boolean initializeIfNeeded) {
        if (initializeIfNeeded && this.faxNumbers == null) {
            this.faxNumbers = new ArrayList<StringWithCustomTags>(0);
        }
        return this.faxNumbers;
    }

    public List<StringWithCustomTags> getLanguagePref() {
        return this.languagePref;
    }

    public List<StringWithCustomTags> getLanguagePref(boolean initializeIfNeeded) {
        if (initializeIfNeeded && this.languagePref == null) {
            this.languagePref = new ArrayList<StringWithCustomTags>(0);
        }
        return this.languagePref;
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

    public StringWithCustomTags getName() {
        return this.name;
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

    public List<StringWithCustomTags> getPhoneNumbers() {
        return this.phoneNumbers;
    }

    public List<StringWithCustomTags> getPhoneNumbers(boolean initializeIfNeeded) {
        if (initializeIfNeeded && this.phoneNumbers == null) {
            this.phoneNumbers = new ArrayList<StringWithCustomTags>(0);
        }
        return this.phoneNumbers;
    }

    public StringWithCustomTags getRecIdNumber() {
        return this.recIdNumber;
    }

    public StringWithCustomTags getRegFileNumber() {
        return this.regFileNumber;
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

    public List<StringWithCustomTags> getWwwUrls() {
        return this.wwwUrls;
    }

    public List<StringWithCustomTags> getWwwUrls(boolean initializeIfNeeded) {
        if (initializeIfNeeded && this.wwwUrls == null) {
            this.wwwUrls = new ArrayList<StringWithCustomTags>(0);
        }
        return this.wwwUrls;
    }

    public String getXref() {
        return this.xref;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = super.hashCode();
        result = 31 * result + (this.address == null ? 0 : this.address.hashCode());
        result = 31 * result + (this.changeDate == null ? 0 : this.changeDate.hashCode());
        result = 31 * result + (this.languagePref == null ? 0 : this.languagePref.hashCode());
        result = 31 * result + (this.multimedia == null ? 0 : this.multimedia.hashCode());
        result = 31 * result + (this.name == null ? 0 : this.name.hashCode());
        result = 31 * result + (this.notes == null ? 0 : this.notes.hashCode());
        result = 31 * result + (this.phoneNumbers == null ? 0 : this.phoneNumbers.hashCode());
        result = 31 * result + (this.faxNumbers == null ? 0 : this.faxNumbers.hashCode());
        result = 31 * result + (this.wwwUrls == null ? 0 : this.wwwUrls.hashCode());
        result = 31 * result + (this.emails == null ? 0 : this.emails.hashCode());
        result = 31 * result + (this.recIdNumber == null ? 0 : this.recIdNumber.hashCode());
        result = 31 * result + (this.regFileNumber == null ? 0 : this.regFileNumber.hashCode());
        result = 31 * result + (this.userReferences == null ? 0 : this.userReferences.hashCode());
        result = 31 * result + (this.xref == null ? 0 : this.xref.hashCode());
        return result;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setChangeDate(ChangeDate changeDate) {
        this.changeDate = changeDate;
    }

    public void setName(StringWithCustomTags name) {
        this.name = name;
    }

    public void setRecIdNumber(StringWithCustomTags recIdNumber) {
        this.recIdNumber = recIdNumber;
    }

    public void setRegFileNumber(StringWithCustomTags regFileNumber) {
        this.regFileNumber = regFileNumber;
    }

    public void setXref(String xref) {
        this.xref = xref;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Submitter [");
        if (this.address != null) {
            builder.append("address=");
            builder.append(this.address);
            builder.append(", ");
        }
        if (this.changeDate != null) {
            builder.append("changeDate=");
            builder.append(this.changeDate);
            builder.append(", ");
        }
        if (this.emails != null) {
            builder.append("emails=");
            builder.append(this.emails);
            builder.append(", ");
        }
        if (this.faxNumbers != null) {
            builder.append("faxNumbers=");
            builder.append(this.faxNumbers);
            builder.append(", ");
        }
        if (this.languagePref != null) {
            builder.append("languagePref=");
            builder.append(this.languagePref);
            builder.append(", ");
        }
        if (this.multimedia != null) {
            builder.append("multimedia=");
            builder.append(this.multimedia);
            builder.append(", ");
        }
        if (this.name != null) {
            builder.append("name=");
            builder.append(this.name);
            builder.append(", ");
        }
        if (this.notes != null) {
            builder.append("notes=");
            builder.append(this.notes);
            builder.append(", ");
        }
        if (this.phoneNumbers != null) {
            builder.append("phoneNumbers=");
            builder.append(this.phoneNumbers);
            builder.append(", ");
        }
        if (this.recIdNumber != null) {
            builder.append("recIdNumber=");
            builder.append(this.recIdNumber);
            builder.append(", ");
        }
        if (this.regFileNumber != null) {
            builder.append("regFileNumber=");
            builder.append(this.regFileNumber);
            builder.append(", ");
        }
        if (this.userReferences != null) {
            builder.append("userReferences=");
            builder.append(this.userReferences);
            builder.append(", ");
        }
        if (this.wwwUrls != null) {
            builder.append("wwwUrls=");
            builder.append(this.wwwUrls);
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

