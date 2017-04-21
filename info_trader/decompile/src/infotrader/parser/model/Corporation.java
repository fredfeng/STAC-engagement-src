/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.model;

import infotrader.parser.Options;
import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.Address;
import infotrader.parser.model.StringWithCustomTags;
import java.util.ArrayList;
import java.util.List;

public class Corporation
extends AbstractElement {
    private static final long serialVersionUID = -2468158832702366329L;
    private Address address;
    private String businessName = "UNSPECIFIED";
    private List<StringWithCustomTags> emails;
    private List<StringWithCustomTags> faxNumbers;
    private List<StringWithCustomTags> phoneNumbers;
    private List<StringWithCustomTags> wwwUrls;

    public Corporation() {
        this.emails = this.getEmails(Options.isCollectionInitializationEnabled());
        this.faxNumbers = this.getFaxNumbers(Options.isCollectionInitializationEnabled());
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
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Corporation other = (Corporation)obj;
        if (this.address == null ? other.address != null : !this.address.equals(other.address)) {
            return false;
        }
        if (this.businessName == null ? other.businessName != null : !this.businessName.equals(other.businessName)) {
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
        return true;
    }

    public Address getAddress() {
        return this.address;
    }

    public String getBusinessName() {
        return this.businessName;
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

    public List<StringWithCustomTags> getPhoneNumbers() {
        return this.phoneNumbers;
    }

    public List<StringWithCustomTags> getPhoneNumbers(boolean initializeIfNeeded) {
        if (initializeIfNeeded && this.phoneNumbers == null) {
            this.phoneNumbers = new ArrayList<StringWithCustomTags>(0);
        }
        return this.phoneNumbers;
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

    @Override
    public int hashCode() {
        int prime = 31;
        int result = super.hashCode();
        result = 31 * result + (this.address == null ? 0 : this.address.hashCode());
        result = 31 * result + (this.businessName == null ? 0 : this.businessName.hashCode());
        result = 31 * result + (this.phoneNumbers == null ? 0 : this.phoneNumbers.hashCode());
        result = 31 * result + (this.faxNumbers == null ? 0 : this.faxNumbers.hashCode());
        result = 31 * result + (this.wwwUrls == null ? 0 : this.wwwUrls.hashCode());
        result = 31 * result + (this.emails == null ? 0 : this.emails.hashCode());
        return result;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Corporation [");
        if (this.address != null) {
            builder.append("address=");
            builder.append(this.address);
            builder.append(", ");
        }
        if (this.businessName != null) {
            builder.append("businessName=");
            builder.append(this.businessName);
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
        if (this.phoneNumbers != null) {
            builder.append("phoneNumbers=");
            builder.append(this.phoneNumbers);
            builder.append(", ");
        }
        if (this.wwwUrls != null) {
            builder.append("wwwUrls=");
            builder.append(this.wwwUrls);
        }
        builder.append("]");
        return builder.toString();
    }
}

