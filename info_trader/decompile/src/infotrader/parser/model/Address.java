/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.model;

import infotrader.parser.Options;
import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.StringWithCustomTags;
import java.util.ArrayList;
import java.util.List;

public class Address
extends AbstractElement {
    private static final long serialVersionUID = 8172155175015540119L;
    private StringWithCustomTags addr1;
    private StringWithCustomTags addr2;
    private StringWithCustomTags city;
    private StringWithCustomTags country;
    private List<String> lines;
    private StringWithCustomTags postalCode;
    private StringWithCustomTags stateProvince;

    public Address() {
        this.lines = this.getLines(Options.isCollectionInitializationEnabled());
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
        Address other = (Address)obj;
        if (this.addr1 == null ? other.addr1 != null : !this.addr1.equals(other.addr1)) {
            return false;
        }
        if (this.addr2 == null ? other.addr2 != null : !this.addr2.equals(other.addr2)) {
            return false;
        }
        if (this.city == null ? other.city != null : !this.city.equals(other.city)) {
            return false;
        }
        if (this.country == null ? other.country != null : !this.country.equals(other.country)) {
            return false;
        }
        if (this.lines == null ? other.lines != null : !this.lines.equals(other.lines)) {
            return false;
        }
        if (this.postalCode == null ? other.postalCode != null : !this.postalCode.equals(other.postalCode)) {
            return false;
        }
        if (this.stateProvince == null ? other.stateProvince != null : !this.stateProvince.equals(other.stateProvince)) {
            return false;
        }
        return true;
    }

    public StringWithCustomTags getAddr1() {
        return this.addr1;
    }

    public StringWithCustomTags getAddr2() {
        return this.addr2;
    }

    public StringWithCustomTags getCity() {
        return this.city;
    }

    public StringWithCustomTags getCountry() {
        return this.country;
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

    public StringWithCustomTags getPostalCode() {
        return this.postalCode;
    }

    public StringWithCustomTags getStateProvince() {
        return this.stateProvince;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = super.hashCode();
        result = 31 * result + (this.addr1 == null ? 0 : this.addr1.hashCode());
        result = 31 * result + (this.addr2 == null ? 0 : this.addr2.hashCode());
        result = 31 * result + (this.city == null ? 0 : this.city.hashCode());
        result = 31 * result + (this.country == null ? 0 : this.country.hashCode());
        result = 31 * result + (this.lines == null ? 0 : this.lines.hashCode());
        result = 31 * result + (this.postalCode == null ? 0 : this.postalCode.hashCode());
        result = 31 * result + (this.stateProvince == null ? 0 : this.stateProvince.hashCode());
        return result;
    }

    public void setAddr1(StringWithCustomTags addr1) {
        this.addr1 = addr1;
    }

    public void setAddr2(StringWithCustomTags addr2) {
        this.addr2 = addr2;
    }

    public void setCity(StringWithCustomTags city) {
        this.city = city;
    }

    public void setCountry(StringWithCustomTags country) {
        this.country = country;
    }

    public void setPostalCode(StringWithCustomTags postalCode) {
        this.postalCode = postalCode;
    }

    public void setStateProvince(StringWithCustomTags stateProvince) {
        this.stateProvince = stateProvince;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Address [");
        if (this.addr1 != null) {
            builder.append("addr1=");
            builder.append(this.addr1);
            builder.append(", ");
        }
        if (this.addr2 != null) {
            builder.append("addr2=");
            builder.append(this.addr2);
            builder.append(", ");
        }
        if (this.city != null) {
            builder.append("city=");
            builder.append(this.city);
            builder.append(", ");
        }
        if (this.country != null) {
            builder.append("country=");
            builder.append(this.country);
            builder.append(", ");
        }
        if (this.lines != null) {
            builder.append("lines=");
            builder.append(this.lines);
            builder.append(", ");
        }
        if (this.postalCode != null) {
            builder.append("postalCode=");
            builder.append(this.postalCode);
            builder.append(", ");
        }
        if (this.stateProvince != null) {
            builder.append("stateProvince=");
            builder.append(this.stateProvince);
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

