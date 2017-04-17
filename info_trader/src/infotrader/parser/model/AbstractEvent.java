/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.model;

import infotrader.parser.Options;
import infotrader.parser.model.AbstractCitation;
import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.Address;
import infotrader.parser.model.Multimedia;
import infotrader.parser.model.Note;
import infotrader.parser.model.Place;
import infotrader.parser.model.StringWithCustomTags;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEvent
extends AbstractElement {
    private static final long serialVersionUID = 2745411202618610785L;
    protected Address address;
    protected StringWithCustomTags age;
    protected StringWithCustomTags cause;
    protected List<AbstractCitation> citations;
    protected StringWithCustomTags date;
    protected StringWithCustomTags description;
    protected List<StringWithCustomTags> emails;
    protected List<StringWithCustomTags> faxNumbers;
    protected List<Multimedia> multimedia;
    protected List<Note> notes;
    protected List<StringWithCustomTags> phoneNumbers;
    protected Place place;
    protected StringWithCustomTags religiousAffiliation;
    protected StringWithCustomTags respAgency;
    protected StringWithCustomTags restrictionNotice;
    protected StringWithCustomTags subType;
    protected List<StringWithCustomTags> wwwUrls;
    protected String yNull;

    public AbstractEvent() {
        this.citations = this.getCitations(Options.isCollectionInitializationEnabled());
        this.emails = this.getEmails(Options.isCollectionInitializationEnabled());
        this.faxNumbers = this.getFaxNumbers(Options.isCollectionInitializationEnabled());
        this.multimedia = this.getMultimedia(Options.isCollectionInitializationEnabled());
        this.notes = this.getNotes(Options.isCollectionInitializationEnabled());
        this.phoneNumbers = this.getPhoneNumbers(Options.isCollectionInitializationEnabled());
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
        if (!(obj instanceof AbstractEvent)) {
            return false;
        }
        AbstractEvent other = (AbstractEvent)obj;
        if (this.address == null ? other.address != null : !this.address.equals(other.address)) {
            return false;
        }
        if (this.age == null ? other.age != null : !this.age.equals(other.age)) {
            return false;
        }
        if (this.cause == null ? other.cause != null : !this.cause.equals(other.cause)) {
            return false;
        }
        if (this.citations == null ? other.citations != null : !this.citations.equals(other.citations)) {
            return false;
        }
        if (this.date == null ? other.date != null : !this.date.equals(other.date)) {
            return false;
        }
        if (this.description == null ? other.description != null : !this.description.equals(other.description)) {
            return false;
        }
        if (this.multimedia == null ? other.multimedia != null : !this.multimedia.equals(other.multimedia)) {
            return false;
        }
        if (this.notes == null ? other.notes != null : !this.notes.equals(other.notes)) {
            return false;
        }
        if (this.phoneNumbers == null ? other.phoneNumbers != null : !this.phoneNumbers.equals(other.phoneNumbers)) {
            return false;
        }
        if (this.place == null ? other.place != null : !this.place.equals(other.place)) {
            return false;
        }
        if (this.respAgency == null ? other.respAgency != null : !this.respAgency.equals(other.respAgency)) {
            return false;
        }
        if (this.subType == null ? other.subType != null : !this.subType.equals(other.subType)) {
            return false;
        }
        if (this.yNull == null ? other.yNull != null : !this.yNull.equals(other.yNull)) {
            return false;
        }
        if (this.religiousAffiliation == null ? other.religiousAffiliation != null : !this.religiousAffiliation.equals(other.religiousAffiliation)) {
            return false;
        }
        if (this.restrictionNotice == null ? other.restrictionNotice != null : !this.restrictionNotice.equals(other.restrictionNotice)) {
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
        return true;
    }

    public Address getAddress() {
        return this.address;
    }

    public StringWithCustomTags getAge() {
        return this.age;
    }

    public StringWithCustomTags getCause() {
        return this.cause;
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

    public StringWithCustomTags getDate() {
        return this.date;
    }

    public StringWithCustomTags getDescription() {
        return this.description;
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

    public List<StringWithCustomTags> getPhoneNumbers() {
        return this.phoneNumbers;
    }

    public List<StringWithCustomTags> getPhoneNumbers(boolean initializeIfNeeded) {
        if (initializeIfNeeded && this.phoneNumbers == null) {
            this.phoneNumbers = new ArrayList<StringWithCustomTags>(0);
        }
        return this.phoneNumbers;
    }

    public Place getPlace() {
        return this.place;
    }

    public StringWithCustomTags getReligiousAffiliation() {
        return this.religiousAffiliation;
    }

    public StringWithCustomTags getRespAgency() {
        return this.respAgency;
    }

    public StringWithCustomTags getRestrictionNotice() {
        return this.restrictionNotice;
    }

    public StringWithCustomTags getSubType() {
        return this.subType;
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

    public String getyNull() {
        return this.yNull;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = super.hashCode();
        result = 31 * result + (this.address == null ? 0 : this.address.hashCode());
        result = 31 * result + (this.age == null ? 0 : this.age.hashCode());
        result = 31 * result + (this.cause == null ? 0 : this.cause.hashCode());
        result = 31 * result + (this.citations == null ? 0 : this.citations.hashCode());
        result = 31 * result + (this.date == null ? 0 : this.date.hashCode());
        result = 31 * result + (this.description == null ? 0 : this.description.hashCode());
        result = 31 * result + (this.multimedia == null ? 0 : this.multimedia.hashCode());
        result = 31 * result + (this.notes == null ? 0 : this.notes.hashCode());
        result = 31 * result + (this.phoneNumbers == null ? 0 : this.phoneNumbers.hashCode());
        result = 31 * result + (this.place == null ? 0 : this.place.hashCode());
        result = 31 * result + (this.respAgency == null ? 0 : this.respAgency.hashCode());
        result = 31 * result + (this.subType == null ? 0 : this.subType.hashCode());
        result = 31 * result + (this.yNull == null ? 0 : this.yNull.hashCode());
        result = 31 * result + (this.religiousAffiliation == null ? 0 : this.religiousAffiliation.hashCode());
        result = 31 * result + (this.restrictionNotice == null ? 0 : this.restrictionNotice.hashCode());
        result = 31 * result + (this.faxNumbers == null ? 0 : this.faxNumbers.hashCode());
        result = 31 * result + (this.wwwUrls == null ? 0 : this.wwwUrls.hashCode());
        result = 31 * result + (this.emails == null ? 0 : this.emails.hashCode());
        return result;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setAge(StringWithCustomTags age) {
        this.age = age;
    }

    public void setCause(StringWithCustomTags cause) {
        this.cause = cause;
    }

    public void setDate(StringWithCustomTags date) {
        this.date = date;
    }

    public void setDescription(StringWithCustomTags description) {
        this.description = description;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public void setReligiousAffiliation(StringWithCustomTags religiousAffiliation) {
        this.religiousAffiliation = religiousAffiliation;
    }

    public void setRespAgency(StringWithCustomTags respAgency) {
        this.respAgency = respAgency;
    }

    public void setRestrictionNotice(StringWithCustomTags restrictionNotice) {
        this.restrictionNotice = restrictionNotice;
    }

    public void setSubType(StringWithCustomTags subType) {
        this.subType = subType;
    }

    public void setyNull(String yNull) {
        this.yNull = yNull;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AbstractEvent [");
        if (this.address != null) {
            builder.append("address=");
            builder.append(this.address);
            builder.append(", ");
        }
        if (this.age != null) {
            builder.append("age=");
            builder.append(this.age);
            builder.append(", ");
        }
        if (this.cause != null) {
            builder.append("cause=");
            builder.append(this.cause);
            builder.append(", ");
        }
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
        if (this.description != null) {
            builder.append("description=");
            builder.append(this.description);
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
        if (this.phoneNumbers != null) {
            builder.append("phoneNumbers=");
            builder.append(this.phoneNumbers);
            builder.append(", ");
        }
        if (this.place != null) {
            builder.append("place=");
            builder.append(this.place);
            builder.append(", ");
        }
        if (this.religiousAffiliation != null) {
            builder.append("religiousAffiliation=");
            builder.append(this.religiousAffiliation);
            builder.append(", ");
        }
        if (this.respAgency != null) {
            builder.append("respAgency=");
            builder.append(this.respAgency);
            builder.append(", ");
        }
        if (this.restrictionNotice != null) {
            builder.append("restrictionNotice=");
            builder.append(this.restrictionNotice);
            builder.append(", ");
        }
        if (this.subType != null) {
            builder.append("subType=");
            builder.append(this.subType);
            builder.append(", ");
        }
        if (this.wwwUrls != null) {
            builder.append("wwwUrls=");
            builder.append(this.wwwUrls);
            builder.append(", ");
        }
        if (this.yNull != null) {
            builder.append("yNull=");
            builder.append(this.yNull);
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

