/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.model;

import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.StringWithCustomTags;
import java.util.List;

public class HeaderSourceData
extends AbstractElement {
    private static final long serialVersionUID = 7952401381182039454L;
    private StringWithCustomTags copyright;
    private String name = "UNSPECIFIED";
    private StringWithCustomTags publishDate;

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
        HeaderSourceData other = (HeaderSourceData)obj;
        if (this.copyright == null ? other.copyright != null : !this.copyright.equals(other.copyright)) {
            return false;
        }
        if (this.name == null ? other.name != null : !this.name.equals(other.name)) {
            return false;
        }
        if (this.publishDate == null ? other.publishDate != null : !this.publishDate.equals(other.publishDate)) {
            return false;
        }
        return true;
    }

    public StringWithCustomTags getCopyright() {
        return this.copyright;
    }

    public String getName() {
        return this.name;
    }

    public StringWithCustomTags getPublishDate() {
        return this.publishDate;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = super.hashCode();
        result = 31 * result + (this.copyright == null ? 0 : this.copyright.hashCode());
        result = 31 * result + (this.name == null ? 0 : this.name.hashCode());
        result = 31 * result + (this.publishDate == null ? 0 : this.publishDate.hashCode());
        return result;
    }

    public void setCopyright(StringWithCustomTags copyright) {
        this.copyright = copyright;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPublishDate(StringWithCustomTags publishDate) {
        this.publishDate = publishDate;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("HeaderSourceData [");
        if (this.copyright != null) {
            builder.append("copyright=");
            builder.append(this.copyright);
            builder.append(", ");
        }
        if (this.name != null) {
            builder.append("name=");
            builder.append(this.name);
            builder.append(", ");
        }
        if (this.publishDate != null) {
            builder.append("publishDate=");
            builder.append(this.publishDate);
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

