/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.model;

import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.StringWithCustomTags;
import java.util.List;

public class UserReference
extends AbstractElement {
    private static final long serialVersionUID = -7283713193577447000L;
    private StringWithCustomTags referenceNum;
    private StringWithCustomTags type;

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
        UserReference other = (UserReference)obj;
        if (this.referenceNum == null ? other.referenceNum != null : !this.referenceNum.equals(other.referenceNum)) {
            return false;
        }
        if (this.type == null ? other.type != null : !this.type.equals(other.type)) {
            return false;
        }
        return true;
    }

    public StringWithCustomTags getReferenceNum() {
        return this.referenceNum;
    }

    public StringWithCustomTags getType() {
        return this.type;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = super.hashCode();
        result = 31 * result + (this.referenceNum == null ? 0 : this.referenceNum.hashCode());
        result = 31 * result + (this.type == null ? 0 : this.type.hashCode());
        return result;
    }

    public void setReferenceNum(StringWithCustomTags referenceNum) {
        this.referenceNum = referenceNum;
    }

    public void setType(StringWithCustomTags type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UserReference [");
        if (this.referenceNum != null) {
            builder.append("referenceNum=");
            builder.append(this.referenceNum);
            builder.append(", ");
        }
        if (this.type != null) {
            builder.append("type=");
            builder.append(this.type);
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

