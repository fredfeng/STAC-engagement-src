/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.model;

import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.StringWithCustomTags;
import java.util.List;

public class SourceCallNumber
extends AbstractElement {
    private static final long serialVersionUID = 508420414070288759L;
    private StringWithCustomTags callNumber;
    private StringWithCustomTags mediaType;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof SourceCallNumber)) {
            return false;
        }
        SourceCallNumber other = (SourceCallNumber)obj;
        if (this.callNumber == null ? other.callNumber != null : !this.callNumber.equals(other.callNumber)) {
            return false;
        }
        if (this.mediaType == null ? other.mediaType != null : !this.mediaType.equals(other.mediaType)) {
            return false;
        }
        return true;
    }

    public StringWithCustomTags getCallNumber() {
        return this.callNumber;
    }

    public StringWithCustomTags getMediaType() {
        return this.mediaType;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = super.hashCode();
        result = 31 * result + (this.callNumber == null ? 0 : this.callNumber.hashCode());
        result = 31 * result + (this.mediaType == null ? 0 : this.mediaType.hashCode());
        return result;
    }

    public void setCallNumber(StringWithCustomTags callNumber) {
        this.callNumber = callNumber;
    }

    public void setMediaType(StringWithCustomTags mediaType) {
        this.mediaType = mediaType;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SourceCallNumber [");
        if (this.callNumber != null) {
            builder.append("callNumber=");
            builder.append(this.callNumber);
            builder.append(", ");
        }
        if (this.mediaType != null) {
            builder.append("mediaType=");
            builder.append(this.mediaType);
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

